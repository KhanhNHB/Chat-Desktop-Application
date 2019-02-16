package fu.server;

import fu.handler.ClientHandler;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JTextPane;

public class Server {

    public static Vector<ClientHandler> ar = new Vector<>();
    public static ArrayList<String> users = new ArrayList<>();
    public static ArrayList all = new ArrayList<>();
    public final static int PORT = 9999;
    public final static String UPDATE_USERS = "updateuserlist:";
    public final static String LOGOUT_MESSAGE = "@@logoutme@@:";
    public static int i = 0;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        Socket socket;
        while (true) {
            System.out.println("Server waiting a new client......");
            socket = serverSocket.accept();

            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            JTextPane txtMesseges = new JTextPane();
            ClientHandler mtch = new ClientHandler(socket, all, users, dis, dos, txtMesseges);
            ar.add(mtch);

            Thread t = new Thread(mtch);
            Server.i++;
            System.out.println("Adding this client to active client: " + i + " list");
            t.start();

        }
    }
}
