package yonyou.esn.openapi.bo;

import java.util.Date;

/**
 * Created by mantantan on 2018/1/19.
 */
public class PermanentCodeBo {
    private int id;
    private String qzId;
    private String suiteKey;
    private String permanentCode;
    private Date createTime;

    public PermanentCodeBo(String qzId, String suiteKey, String permanentCode){
        this.qzId = qzId;
        this.suiteKey = suiteKey;
        this.permanentCode = permanentCode;
    }

    public PermanentCodeBo(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQzId() {
        return qzId;
    }

    public void setQzId(String qzId) {
        this.qzId = qzId;
    }

    public String getSuiteKey() {
        return suiteKey;
    }

    public void setSuiteKey(String suiteKey) {
        this.suiteKey = suiteKey;
    }

    public String getPermanentCode() {
        return permanentCode;
    }

    public void setPermanentCode(String permanentCode) {
        this.permanentCode = permanentCode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
