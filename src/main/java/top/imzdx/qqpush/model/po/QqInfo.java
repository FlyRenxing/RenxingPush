package top.imzdx.qqpush.model.po;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class QqInfo {

    private long number;
    @JsonIgnore
    private String pwd;
    private String name;
    private long state;
    private String remarks;


}
