package Client;

import java.io.IOException;

public class MessageWriter extends Thread{
    @Override
    public void run(){
        try {
            while (true){
                String userData = Client.reader.readLine();
                String commandCode = userData;
                if(userData.contains(" "))
                    commandCode = userData.substring(0, userData.indexOf(' '));
                switch (commandCode) {
                    case "/help": {
                        Client.showHelp();
                        break;
                    }
                    //JOIN
                    case "/join": {
                        MessageData messageData = new MessageData();
                        messageData.text = userData.substring(userData.indexOf(' ') + 1);
                        try{
                            Integer.parseInt(messageData.text);
                            messageData.commandCode = "JOIN";
                            Client.out.write(MessageConverter.convertToString(messageData) + "\n");
                            Client.out.flush();
                        }
                        catch (Exception ex){
                            System.out.println("please, input only id (integer)");
                        }
                        break;
                    }
                    //GETCHAT
                    case "/getchat": {
                        MessageData messageData = new MessageData();
                        messageData.commandCode = "GETCHAT";
                        Client.out.write(MessageConverter.convertToString(messageData) + "\n");
                        Client.out.flush();
                        System.out.println("=================");
                        break;
                    }
                    //CREATE
                    case "/create": {
                        MessageData messageData = new MessageData();
                        messageData.commandCode = "CREATE";
                        messageData.text = userData.substring(userData.indexOf(' ') + 1);
                        Client.out.write(MessageConverter.convertToString(messageData) + "\n");
                        Client.out.flush();
                        System.out.println("=================");
                        break;
                    }
                    //LEAVE
                    case "/leave": {
                        MessageData messageData = new MessageData();
                        messageData.commandCode = "LEAVE";
                        Client.out.write(MessageConverter.convertToString(messageData) + "\n");
                        Client.out.flush();
                        System.out.println("=================");
                        break;
                    }
                    //SEND
                    case "/send": {
                        MessageData messageData = new MessageData();
                        messageData.text = userData.substring(userData.indexOf(' ') + 1);
                        messageData.commandCode = "SEND";
                        Client.out.write(MessageConverter.convertToString(messageData) + "\n");
                        Client.out.flush();
                        break;
                    }
                    default: {
                        System.out.println("Command doesnt exist");
                        break;
                    }
                }
            }
        }
        catch (IOException | IllegalAccessException ex){
            ex.printStackTrace();
        }
    }
}
