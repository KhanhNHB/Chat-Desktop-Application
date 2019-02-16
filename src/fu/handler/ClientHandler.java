package fu.handler;

import fu.server.Server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import javax.swing.JTextPane;

public class ClientHandler implements Runnable {

    private String name;
    private DataInputStream dis;
    private DataOutputStream dos;
    private Socket socket;
    private ArrayList<String> users = new ArrayList<>();
    private ArrayList all = new ArrayList<>();
    private JTextPane txtMessege;
    
    public ClientHandler(Socket socket, ArrayList all, ArrayList users,
            DataInputStream dis, DataOutputStream dos, JTextPane txtMessge) {
        this.dis = dis;
        this.dos = dos;
        this.socket = socket;
        this.all = all;
        this.users = users;
        this.txtMessege = txtMessge;
        
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
                    break;
                }

                StringTokenizer st = new StringTokenizer(received, "#");
                String MsgToSend = st.nextToken();
                String recipient = st.nextToken();

                for (ClientHandler clientHandler : Server.ar) {
                    System.out.println("User online: " + clientHandler.name);
                }
                
                for (ClientHandler mc : Server.ar) {
                    System.out.println("Client: " + mc.name);
                    System.out.println("Recipient: " + recipient);
                    
                    if (mc.name.equals(recipient)) {
                        mc.dos.writeUTF(MsgToSend);
                        break;
                    }
                }
            }
            dos.writeUTF(Server.LOGOUT_MESSAGE);
            dos.flush();

            users.remove(name);
            all.remove(socket);
            Server.i--;

//            tellEveryOne("\t\t****** " + name + " Logged out at " + (new Date()) + " ******");
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
