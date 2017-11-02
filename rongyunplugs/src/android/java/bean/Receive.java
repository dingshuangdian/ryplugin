package cordova.plugin.ismartnet.rongcloud.bean;

/**
 * Created by lvping on 2017/9/18.
 */

public class Receive {

  /**
   * msg :
   * result : 1:已领 2：领完 0：未领
   * success : true
   */

  private String msg;
  private String result;
  private boolean success;

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }
}
