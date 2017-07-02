package de.lukaskoerfer.p2pchat;

import de.lukaskoerfer.p2pchat.data.BaseMessage;

public interface ReceiveCallback {

    void ReceiveMessage(BaseMessage msg);

}
