package cordova.plugin.ismartnet.rongcloud.bean;

import java.util.List;

/**
 * Created by lvping on 2017/10/10.
 */

public class GroupMsg {

  /**
   * msg :
   * result : [{"uId":4,"uName":"企业管理员","headImg":"http://192.168.1.64:8080/images/PM-20170711165514-LN1W6O.jpg"},{"uId":1053,"uName":"tps","headImg":"http://192.168.1.64:8080/TPS/TP/assets/images/defaultHeadImg.png"},{"uId":1070,"uName":"其榕","headImg":"http://192.168.1.64:8080/TPS/TP/assets/images/defaultHeadImg.png"},{"uId":1071,"uName":"其榕2","headImg":"/images/UM-20170918143233-7G8Y8T.png"},{"uId":1073,"uName":"dsd","headImg":"http://192.168.1.64:8080/TPS/TP/assets/images/defaultHeadImg.png"},{"uId":1060,"uName":"cc","headImg":"http://192.168.1.64:8080/TPS/TP/assets/images/defaultHeadImg.png"}]
   * success : true
   */

  private String msg;
  private boolean success;
  private List<ResultBean> result;

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public List<ResultBean> getResult() {
    return result;
  }

  public void setResult(List<ResultBean> result) {
    this.result = result;
  }

  public static class ResultBean {
    /**
     * uId : 4
     * uName : 企业管理员
     * headImg : http://192.168.1.64:8080/images/PM-20170711165514-LN1W6O.jpg
     */

    private String uId;
    private String uName;
    private String headImg;

    public String getUId() {
      return uId;
    }

    public void setUId(String uId) {
      this.uId = uId;
    }

    public String getUName() {
      return uName;
    }

    public void setUName(String uName) {
      this.uName = uName;
    }

    public String getHeadImg() {
      return headImg;
    }

    public void setHeadImg(String headImg) {
      this.headImg = headImg;
    }
  }
}
