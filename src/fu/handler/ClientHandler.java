package fu.handler;

import fu.server.Server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.StringTokenizer;

public final class ClientHandler implements Runnable {

    private String name;
    private DataInputStream dis;
    private DataOutputStream dos;
    private Socket socket;
    private ArrayList<String> users = new ArrayList<>();
    private ArrayList all = new ArrayList<>();

    public ClientHandler(Socket socket, ArrayList all, ArrayList users,
            DataInputStream dis, DataOutputStream dos) {
        this.dis = dis;
        this.dos = dos;
        this.socket = socket;
        this.all = all;
        this.users = users;

        try {
            this.name = dis.readUTF();
            System.out.println(this.name + " Online.");

            all.add(this.socket);
            users.add(this.name);

//            tellEveryOne("\t\t****** " + this.name + " Logged in at " + (new Date()) + " ******");
            sendNewUserList();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            String received;
            while (true) {
                received = dis.readUTF();

                if (received.toLowerCase().equals(Server.LOGOUT_MESSAGE)) {
                    users.remove(name);
                    all.remove(socket);
                    sendNewUserList();
                    break;
                }

                StringTokenizer st = new StringTokenizer(received, "#");
                String Sender = st.nextToken();
                String MsgToSend = st.nextToken();
                String recipient = st.nextToken();

                Server.ar.forEach((clientHandler) -> {
                    System.out.println("User online: " + clientHandler.name);
                });

                for (ClientHandler mc : Server.ar) {
                    System.out.println("Contact in list: " + mc.name);
                    System.out.println("Recipient: " + recipient);

                    if (mc.name.equals(recipient)) {
                        mc.dos.writeUTF(Sender + ": " + MsgToSend);
                        System.out.println("Vao ne: " + MsgToSend);
                        break;
                    }
                    
                }
                System.out.println("------------------- End for  --------------------");
            }
            dos.writeUTF(Server.LOGOUT_MESSAGE);
            dos.flush();

            Server.i--;

            tellEveryOne("\t\t****** " + name + " Logged out at " + (new Date()) + " ******");
            sendNewUserList();

            socket.close();
            dos.close();
            dis.close();
        } catch (IOException e) {
        }
    }

    public void sendNewUserList() {
        tellEveryOne(Server.UPDATE_USERS + users.toString());
    }

    public void tellEveryOne(String msg) {
        Iterator i = this.all.iterator();
        while (i.hasNext()) {
            try {
                Socket temp = (Socket) i.next();
                DataOutputStream dos = new DataOutputStream(temp.getOutputStream());
                dos.writeUTF(msg.trim());
                dos.flush();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
