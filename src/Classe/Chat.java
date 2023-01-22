package Classe;

import java.io.Serializable;

public class Chat implements Serializable {
    private int PORT_CHAT;
    private String IP;

    public int getPORT_CHAT() {
        return PORT_CHAT;
    }

    public void setPORT_CHAT(int PORT_CHAT) {
        this.PORT_CHAT = PORT_CHAT;
    }

    public String getIp() {
        return IP;
    }

    public void setIp(String ip) {
        this.IP = ip;
    }
}
