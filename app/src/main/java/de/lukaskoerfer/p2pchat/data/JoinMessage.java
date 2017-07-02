package de.lukaskoerfer.p2pchat.data;

import de.lukaskoerfer.p2pchat.ClientInfo;

/**
 * Created by Koerfer on 10.03.2016.
 */
public class JoinMessage extends BaseMessage {

    private ClientInfo Client;

    public JoinMessage(ClientInfo clientInfo) {
        this.Client = clientInfo;
    }

    public ClientInfo getClient() {
        return this.Client;
    }

    public String serialize() {
        return "JOIN" + this.Client.serialize();
    }

    public static JoinMessage Deserialize(String serialized) {
        ClientInfo clientInfo = ClientInfo.Deserialize(serialized);
        return new JoinMessage(clientInfo);
    }

}
