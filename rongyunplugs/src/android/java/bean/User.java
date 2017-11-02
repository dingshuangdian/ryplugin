package cordova.plugin.ismartnet.rongcloud.bean;

/**
 * Created by lvping on 2017/9/4.
 */

public class User {

  /**
   * msg :
   * result : {"userId":1026,"userName":"why","headImg":"http://192.168.1.64:8080/images/PM-20170711165514-LN1W6O.jpg"}
   * success : true
   */

  private String msg;
  private ResultBean result;
  private boolean success;

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public ResultBean getResult() {
    return result;
  }

  public void setResult(ResultBean result) {
    this.result = result;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public static class ResultBean {
    /**
     * userId : 1026
     * userName : why
     * headImg : http://192.168.1.64:8080/images/PM-20170711165514-LN1W6O.jpg
     */

    private String userId;
    private String userName;
    private String headImg;

    public String getUserId() {
      return userId;
    }

    public void setUserId(String userId) {
      this.userId = userId;
    }

    public String getUserName() {
      return userName;
    }

    public void setUserName(String userName) {
      this.userName = userName;
    }

    public String getHeadImg() {
      return headImg;
    }

    public void setHeadImg(String headImg) {
      this.headImg = headImg;
    }
  }
}
