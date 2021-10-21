package Client;

import java.lang.reflect.Field;

public class MessageConverter {
    public static MessageData convertToClass(String str) throws IllegalAccessException {
        MessageData messageData = new MessageData();
        for (Field field : messageData.getClass().getDeclaredFields()) {
            String fieldName = field.getName();
            int start = str.indexOf("<" + fieldName + ">") + fieldName.length() + 2;
            int end = str.lastIndexOf("</" + fieldName + ">");
            field.setAccessible(true);
            field.set(messageData, str.substring(start, end));
        }
        return messageData;
    }

    public static String convertToString(MessageData messageData) throws IllegalAccessException {
        StringBuilder messageString = new StringBuilder();
        for(Field field : messageData.getClass().getDeclaredFields()){
            String fieldName = field.getName();
            messageString.append("<"+fieldName+">" + field.get(messageData) + "</"+fieldName+">");
        }
        return messageString.toString();
    }
}
