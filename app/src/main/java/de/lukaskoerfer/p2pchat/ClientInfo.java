package de.lukaskoerfer.p2pchat;

/**
 * Created by Koerfer on 23.03.2016.
 */
public class ClientInfo {

    private String DeviceAddress;
    private String Username;

    public ClientInfo(String deviceAddress, String username) {
        this.DeviceAddress = deviceAddress;
        this.Username = username;
    }

    public static ClientInfo GetComparable(String deviceAddress) {
        return new ClientInfo(deviceAddress, "");
    }

    public String serialize() {
        return this.DeviceAddress + "#" + this.Username;
    }

    public static ClientInfo Deserialize(String serialized) {
        int index = serialized.indexOf("#");
        String deviceAddress = serialized.substring(0, index - 1);
        String username = serialized.substring(index + 1);
        return new ClientInfo(deviceAddress, username);
    }

    public String getDeviceAddress() {
        return this.DeviceAddress;
    }

    public String getUsername() {
        return this.Username;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof ClientInfo) {
            ClientInfo client = (ClientInfo) object;
            return (client.DeviceAddress == this.DeviceAddress);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        long decimal = Long.parseLong(this.DeviceAddress.replace(":", ""));
        return Long.valueOf(decimal).hashCode();
    }
}
