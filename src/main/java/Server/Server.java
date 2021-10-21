package Server;

import Model.User;
import Service.UserService;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;
import java.util.LinkedList;
import java.util.TreeMap;

public class Server {
    public static final int PORT = 32002;
    public static LinkedList<ServerData> clients = new LinkedList<ServerData>();
    public static TreeMap<Integer, Chatroom> chatrooms = new TreeMap<Integer, Chatroom>();
    public static Integer chatId = 1;
    public static UserService userService = new UserService();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        UserService userService = new UserService();
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                clients.add(new ServerData(socket));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
