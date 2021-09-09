package top.imzdx.qqpush.model.po;

import lombok.Data;

@Data
public class MessageLog {

  private long id;
  private String content;
  private String meta;
  private long uid;
  private java.sql.Timestamp time;


}
