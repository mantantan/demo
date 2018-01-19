package yonyou.esn.openapi.enums;

/**
 * Created by mantantan on 2018/1/19.
 */
public enum PushType {
    TICKET(0, "suite_ticket"),
    TEMP_CODE(1, "authorized");

    private int code;
    private String message;
    private PushType(int code, String message){
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
