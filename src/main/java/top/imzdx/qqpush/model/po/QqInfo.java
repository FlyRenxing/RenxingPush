package top.imzdx.qqpush.model.po;


import com.fasterxml.jackson.annotation.JsonIgnore;

public class QqInfo {

    private long number;
    @JsonIgnore
    private String pwd;
    private String name;
    private long state;
    private String remarks;


    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }


    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public long getState() {
        return state;
    }

    public void setState(long state) {
        this.state = state;
    }


    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
