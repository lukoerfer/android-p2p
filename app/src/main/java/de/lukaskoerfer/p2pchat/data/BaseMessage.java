package de.lukaskoerfer.p2pchat.data;

/**
 * Created by Koerfer on 10.03.2016.
 */
public abstract class BaseMessage {

    public abstract String serialize();

    public static BaseMessage Deserialize(String serialized) {
        if (serialized.startsWith("JOIN")) {
            return JoinMessage.Deserialize(serialized.substring(4));
        } else if (serialized.startsWith("LEAVE")) {
            return LeaveMessage.Deserialize(serialized.substring(5));
        } else if (serialized.startsWith("CHAT")) {
            return ChatMessage.Deserialize(serialized.substring(4));
        } else {
            throw new IllegalArgumentException("Not deserializable");
        }
    }

}
