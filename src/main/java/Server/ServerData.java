package Server;

import Model.User;
import Service.UserService;

import java.io.*;
import java.net.Socket;
import java.util.Map;

public class ServerData extends Thread{
    private Socket socket;
    public int id = 0;

    private String name;

    public BufferedReader in;
    public BufferedWriter out;

    public ServerData(Socket socket) throws IOException {
        this.socket = socket;

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void run() {
        String data;
        try {
            while (true) {
                data = in.readLine();
                System.out.println(data);

                MessageData messageData = MessageConverter.Convert(data);
                switch (messageData.commandCode) {
                    case "REGISTER": {
                        String nickname = messageData.text.substring(messageData.text.indexOf("<Name>") + 6, messageData.text.indexOf("</Name>"));
                        String login = messageData.text.substring(messageData.text.indexOf("<Login>") + 7, messageData.text.indexOf("</Login>"));
                        String password = messageData.text.substring(messageData.text.indexOf("<Password>") + 10, messageData.text.indexOf("</Password>"));
                        Server.userService.addUser(new User(nickname, login, password));
                    }
                    case "AUTHORIZE": {
                        MessageData message = new MessageData();
                        String login = messageData.text.substring(messageData.text.indexOf("<Login>") + 7, messageData.text.indexOf("</Login>"));
                        String password = messageData.text.substring(messageData.text.indexOf("<Password>") + 10, messageData.text.indexOf("</Password>"));
                        User user = Server.userService.findUser(login, password);
                        System.out.println(user.id);
                        System.out.println(user.nickname);
                        if(user == null){
                            message.commandCode = "FAULT";
                            out.write(MessageConverter.convertToString(message) + "\n");
                            out.flush();
                            break;
                        }
                        message.text = "<Name>" + user.nickname + "</Name>";
                        this.id = user.id;
                        this.name = user.nickname;
                        message.commandCode = "AUTHORIZED";
                        out.write(MessageConverter.convertToString(message) + "\n");
                        out.flush();
                        break;
                    }
                    case "GETCHAT": {
                        MessageData availableChats = new MessageData();
                        availableChats.commandCode = "GETCHAT";
                        for (Map.Entry<Integer, Chatroom> chat : Server.chatrooms.entrySet()) {
                            if (chat.getValue().clients.contains(this))
                                continue;
                            availableChats.text += "<Chat>" + "id: " + chat.getKey() + " | " + chat.getValue().chatName + "</Chat>";
                        }
                        System.out.println(MessageConverter.convertToString(availableChats));
                        out.write(MessageConverter.convertToString(availableChats) + "\n");
                        out.flush();
                        break;
                    }
                    case "CREATE": {
                        Chatroom chatroom = new Chatroom();
                        chatroom.chatName = messageData.text;
                        Server.chatrooms.put(Server.chatId++, chatroom);
                        break;
                    }
                    case "JOIN": {
                        int chatId = Integer.parseInt(messageData.text);
                        boolean joined = true;
                        for (Map.Entry<Integer, Chatroom> chat : Server.chatrooms.entrySet()) {
                            if(chat.getValue().clients.contains(this)) {
                                if (chat.getKey() == chatId) {
                                    joined = false;
                                    break;
                                }
                                else {
                                    chat.getValue().messageNotify(this.id, this.name, "left from chat");
                                    chat.getValue().clients.remove(this);
                                }
                            }
                        }
                        if(joined) {
                            Chatroom chatroom = Server.chatrooms.get(chatId);
                            chatroom.clients.add(this);
                            chatroom.messageNotify(this.id, this.name, " joined to chat");
                            MessageData message = new MessageData();
                            message.commandCode = "ADDED";
                            message.text = chatroom.chatName;
                            out.write(MessageConverter.convertToString(message) + "\n");
                            out.flush();
                        }
                        break;
                    }
                    case "SEND": {
                        Chatroom chatroom = null;
                        for (Map.Entry<Integer, Chatroom> chat : Server.chatrooms.entrySet()) {
                            if (chat.getValue().clients.contains(this)) {
                                chatroom = chat.getValue();
                                break;
                            }
                        }
                        if (chatroom == null)
                            return;
                        chatroom.messageNotify(this.id, this.name, messageData.text);
                        break;
                    }
                    case "LEAVE": {
                        for(Map.Entry<Integer, Chatroom> chat : Server.chatrooms.entrySet()){
                            if(chat.getValue().clients.remove(this)){
                                chat.getValue().messageNotify(this.id, this.name, "left from chat");
                                break;
                            }
                        }
                        break;
                    }
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}