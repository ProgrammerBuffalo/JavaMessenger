package Server;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

public class Chatroom {
    public Collection<ServerData> clients;
    public String chatName = "";

    public Chatroom(){
        clients = new LinkedList<ServerData>();
    }

    public void messageNotify(int userId, String userName, String messageText) throws IOException, IllegalAccessException {
        MessageData messageData = new MessageData();
        messageData.text = messageText;
        messageData.commandCode = "SEND";
        messageData.sender = userName;
        for(ServerData client : clients) {
            if(client.id != userId) {
                client.out.write(MessageConverter.convertToString(messageData) + "\n");
                client.out.flush();
            }
        }
    }
}
