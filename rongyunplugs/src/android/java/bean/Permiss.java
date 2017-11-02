package cordova.plugin.ismartnet.rongcloud.bean;

/**
 * Created by lvping on 2017/10/16.
 */

public class Permiss {

  /**
   * msg :
   * result : {"bonusAuth":"0","announcementAuth":"0"}
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
     * bonusAuth : 0
     * announcementAuth : 0
     */

    private String bonusAuth;
    private String announcementAuth;

    public String getBonusAuth() {
      return bonusAuth;
    }

    public void setBonusAuth(String bonusAuth) {
      this.bonusAuth = bonusAuth;
    }

    public String getAnnouncementAuth() {
      return announcementAuth;
    }

    public void setAnnouncementAuth(String announcementAuth) {
      this.announcementAuth = announcementAuth;
    }
  }
}
