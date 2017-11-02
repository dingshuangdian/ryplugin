package cordova.plugin.ismartnet.rongcloud.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lvping on 2017/9/18.
 */

public class RpItemModel{


  /**
   * msg :
   * result : {"issueTypeId":2,"unpaidAmount":0,"splitNum":1,"receiveNum":1,"orderAmount":1,"receiveAmount":1,"record":[{"userId":1068,"bonusAmount":1,"receiveTime":"00:00","headImg":"http://orgytjqvx.bkt.clouddn.com/default_headimg.png","userName":"why"}],"isReceive":"false"}
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

  public static class ResultBean implements Serializable{
    /**
     * issueTypeId : 2
     * unpaidAmount : 0
     * splitNum : 1
     * receiveNum : 1
     * orderAmount : 1
     * receiveAmount : 1
     * record : [{"userId":1068,"bonusAmount":1,"receiveTime":"00:00","headImg":"http://orgytjqvx.bkt.clouddn.com/default_headimg.png","userName":"why"}]
     * isReceive : false
     */

    private int issueTypeId;
    private float unpaidAmount;
    private int splitNum;
    private int receiveNum;
    private float orderAmount;
    private float receiveAmount;
    private String isReceive;
    private List<RecordBean> record;

    public int getIssueTypeId() {
      return issueTypeId;
    }

    public void setIssueTypeId(int issueTypeId) {
      this.issueTypeId = issueTypeId;
    }

    public float getUnpaidAmount() {
      return unpaidAmount;
    }

    public void setUnpaidAmount(float unpaidAmount) {
      this.unpaidAmount = unpaidAmount;
    }

    public int getSplitNum() {
      return splitNum;
    }

    public void setSplitNum(int splitNum) {
      this.splitNum = splitNum;
    }

    public int getReceiveNum() {
      return receiveNum;
    }

    public void setReceiveNum(int receiveNum) {
      this.receiveNum = receiveNum;
    }

    public float getOrderAmount() {
      return orderAmount;
    }

    public void setOrderAmount(float orderAmount) {
      this.orderAmount = orderAmount;
    }

    public float getReceiveAmount() {
      return receiveAmount;
    }

    public void setReceiveAmount(int receiveAmount) {
      this.receiveAmount = receiveAmount;
    }

    public String getIsReceive() {
      return isReceive;
    }

    public void setIsReceive(String isReceive) {
      this.isReceive = isReceive;
    }

    public List<RecordBean> getRecord() {
      return record;
    }

    public void setRecord(List<RecordBean> record) {
      this.record = record;
    }

    public static class RecordBean implements Serializable{
      /**
       * userId : 1068
       * bonusAmount : 1
       * receiveTime : 00:00
       * headImg : http://orgytjqvx.bkt.clouddn.com/default_headimg.png
       * userName : why
       */

      private String userId;
      private String bonusAmount;
      private String receiveTime;
      private String headImg;
      private String userName;
      private int flag;

      public int getFlag() {
        return flag;
      }

      public void setFlag(int flag) {
        this.flag = flag;
      }

      public String getUserId() {
        return userId;
      }

      public void setUserId(String userId) {
        this.userId = userId;
      }

      public String getBonusAmount() {
        return bonusAmount;
      }

      public void setBonusAmount(String bonusAmount) {
        this.bonusAmount = bonusAmount;
      }

      public String getReceiveTime() {
        return receiveTime;
      }

      public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
      }

      public String getHeadImg() {
        return headImg;
      }

      public void setHeadImg(String headImg) {
        this.headImg = headImg;
      }

      public String getUserName() {
        return userName;
      }

      public void setUserName(String userName) {
        this.userName = userName;
      }
    }
  }
}
