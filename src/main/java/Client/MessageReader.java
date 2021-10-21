package Client;

import java.io.IOException;

public class MessageReader extends Thread {
    @Override
    public void run(){
        try{
            while (true){
                MessageData data = MessageConverter.convertToClass(Client.in.readLine());
                switch (data.commandCode) {
                    case "ADDED": {
                        System.out.println("\t\t" + data.text);
                        break;
                    }
                    case "GETCHAT": {
                        StringBuilder stringBuilder = new StringBuilder();
                        while (data.text.length() > 0) {
                            int lastIndex = data.text.indexOf("</Chat>");
                            String text = data.text.substring(6, lastIndex);
                            stringBuilder.append(text + "\n");
                            data.text = data.text.substring(lastIndex + 7);
                        }
                        System.out.println(stringBuilder);
                        break;
                    }
                    case "SEND": {
                        System.out.println(data.sender + ": " + data.text);
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
