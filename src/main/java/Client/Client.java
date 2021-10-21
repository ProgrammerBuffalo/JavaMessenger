package Client;

import java.io.*;
import java.net.Socket;

public class Client {
    public static Socket clientSocket;
    public static BufferedReader reader;
    public static BufferedReader in;
    public static BufferedWriter out;

    public static String userName = "";

    private static final int PORT = 32002;

    public static void main(String[] args) throws IOException {
        try {
            clientSocket = new Socket("localhost", PORT);
            reader = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            while(true){
                System.out.println("Choose: \n1: Register\n2: Authorize");
                int num = Integer.parseInt(reader.readLine());
                System.out.println("Input login");
                String login = reader.readLine();
                System.out.println("Input password");
                String password = reader.readLine();
                MessageData messageData = new MessageData();
                messageData.text = "<Login>" + login + "</Login>" + "<Password>" + password + "</Password>";

                switch(num) {
                    case 1: {
                        messageData.commandCode = "REGISTER";
                        System.out.println("Input nickname");
                        String nickname = reader.readLine();
                        messageData.text += "<Name>" + nickname + "</Name>";
                        out.write(MessageConverter.convertToString(messageData) + "\n");
                        out.flush();
                        break;
                    }
                    case 2: {
                        messageData.commandCode = "AUTHORIZE";
                        out.write(MessageConverter.convertToString(messageData) + "\n");
                        out.flush();
                        break;
                    }
                }
                MessageData serverData = MessageConverter.convertToClass(in.readLine());
                System.out.println(serverData.commandCode);
                if(serverData.commandCode.equals("AUTHORIZED")){
                    userName = serverData.sender;
                    break;
                }
                else{
                    System.out.println("Login or password doesnt exist!");
                }
            }

            showHelp();

            new MessageReader().start();
            new MessageWriter().start();

            while (true) { }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static void showHelp(){
        System.out.println("\t=====COMMAND LIST=====");
        System.out.println("/help - shows command list");
        System.out.println("/join chatId - joins to chat");
        System.out.println("/send message_text - sends message to joined chat");
        System.out.println("/getchat - gets all available chats");
        System.out.println("/create chatName - creates chatroom");
        System.out.println("/leave - leaves joined chat");
    }
}

