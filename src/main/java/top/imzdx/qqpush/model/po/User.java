package top.imzdx.qqpush.model.po;


import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class User implements Serializable {

    private long uid;
    private String name;
    private String password;
    private long admin;
    private String config;
    private String cipher;


}
