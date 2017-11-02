package cordova.plugin.ismartnet.rongcloud.bean;

/**
 * Created by lvping on 2017/9/7.
 */

public class Group {


  /**
   * msg :
   * result : {"groupName":"华师大励志成长营","groupId":2,"portraitUri":"","totalUsers":8}
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
     * groupName : 华师大励志成长营
     * groupId : 2
     * portraitUri :
     * totalUsers : 8
     */

    private String groupName;
    private int groupId;
    private String portraitUri;
    private String totalUsers;

    public String getGroupName() {
      return groupName;
    }

    public void setGroupName(String groupName) {
      this.groupName = groupName;
    }

    public int getGroupId() {
      return groupId;
    }

    public void setGroupId(int groupId) {
      this.groupId = groupId;
    }

    public String getPortraitUri() {
      return portraitUri;
    }

    public void setPortraitUri(String portraitUri) {
      this.portraitUri = portraitUri;
    }

    public String getTotalUsers() {
      return totalUsers;
    }

    public void setTotalUsers(String totalUsers) {
      this.totalUsers = totalUsers;
    }
  }
}
