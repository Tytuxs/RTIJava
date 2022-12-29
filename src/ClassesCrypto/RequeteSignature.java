package ClassesCrypto;

import java.io.Serializable;

public class RequeteSignature implements Serializable {
    private String Message;
    private byte[] signature;

    public RequeteSignature(String message, byte[] sign) {
        this.Message = message;
        this.signature = sign;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }
}
