package yonyou.esn.openapi.bo;

/**
 * Created by mantantan on 2018/1/17.
 */
public class OpenApiPush {
    public OpenApiPush() {

    }

    public OpenApiPush(String msgSignature, String timestamp, String nonce, String encrypt) {
        setMsgSignature(msgSignature);
        setTimestamp(timestamp);
        setNonce(nonce);
        setEncrypt(encrypt);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMsgSignature() {
        return msgSignature;
    }

    public void setMsgSignature(String msgSignature) {
        this.msgSignature = msgSignature;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getEncrypt() {
        return encrypt;
    }

    public void setEncrypt(String encrypt) {
        this.encrypt = encrypt;
    }

    private int id;
    private String msgSignature;
    private String timestamp;
    private String nonce;
    private String encrypt;
}
