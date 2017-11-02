package cordova.plugin.ismartnet.rongcloud.bean;

/**
 * Created by lvping on 2017/9/15.
 */

public class Order {

  /**
   * msg :
   * result : 1
   * success : true
   */

  private String msg;
  private int result;
  private boolean success;

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public int getResult() {
    return result;
  }

  public void setResult(int result) {
    this.result = result;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }
}
