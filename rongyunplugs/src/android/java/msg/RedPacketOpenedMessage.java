package cordova.plugin.ismartnet.rongcloud.msg;

import android.os.Parcel;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.rong.common.ParcelUtils;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;

/**
 * Created by lvping on 2017/9/14.
 */
@MessageTag(
  value = "ZWHL:receiveBonusMsg",
  flag = 1
)

public class RedPacketOpenedMessage extends MessageContent {
  private String receiveUserId;
  private String receiveUserName;
  private String sendUserName;
  public static final Creator<RedPacketOpenedMessage> CREATOR = new Creator() {
    public RedPacketOpenedMessage createFromParcel(Parcel var1) {
      return new RedPacketOpenedMessage(var1);
    }

    public RedPacketOpenedMessage[] newArray(int var1) {
      return new RedPacketOpenedMessage[var1];
    }
  };

  public RedPacketOpenedMessage() {
  }

  public static RedPacketOpenedMessage obtain(String var0, String var1, String var2) {
    RedPacketOpenedMessage var6 = new RedPacketOpenedMessage();
    //发的人的id;
    var6.setReceiveUserId(var0);
    Log.e("var0",var0);
    //收的人的名称
    var6.setReceiveUserName(var1);
    Log.e("var1",var1);
    //发的人的名称
    var6.setSendUserName(var2);
    Log.e("var2",var2);
    return var6;
  }

  public byte[] encode() {
    JSONObject var1 = new JSONObject();

    try {
      if (!TextUtils.isEmpty(this.getReceiveUserId())) {
        var1.put("receiveUserId", this.receiveUserId);
      }

      if (!TextUtils.isEmpty(this.getReceiveUserName())) {
        var1.put("receiveUserName", this.receiveUserName);
      }

      if (!TextUtils.isEmpty(this.getSendUserName())) {
        var1.put("sendUserName", this.sendUserName);
      }
    } catch (JSONException var4) {
      var4.printStackTrace();
    }

    try {
      return var1.toString().getBytes("UTF-8");
    } catch (UnsupportedEncodingException var3) {
      var3.printStackTrace();
      return null;
    }
  }

  private String getEmotion(String var1) {
    Pattern var2 = Pattern.compile("\\[/u([0-9A-Fa-f]+)\\]");
    Matcher var3 = var2.matcher(var1);
    StringBuffer var4 = new StringBuffer();

    while (var3.find()) {
      int var5 = Integer.parseInt(var3.group(1), 16);
      var3.appendReplacement(var4, String.valueOf(Character.toChars(var5)));
    }

    var3.appendTail(var4);
    return var4.toString();
  }
  public RedPacketOpenedMessage(byte[] var1) {
    String var2 = null;

    try {
      var2 = new String(var1, "UTF-8");
    } catch (UnsupportedEncodingException var5) {
      var5.printStackTrace();
    }

    try {
      JSONObject var3 = new JSONObject(var2);
      if (var3.has("receiveUserId")) {
        this.setReceiveUserId(var3.optString("receiveUserId"));
      }
      if (var3.has("receiveUserName")) {
        this.setReceiveUserName(var3.optString("receiveUserName"));
      }
      if (var3.has("sendUserName")) {
        this.setSendUserName(var3.optString("sendUserName"));
      }
    } catch (JSONException var4) {
      var4.printStackTrace();
    }
  }
  public int describeContents() {
    return 0;
  }

  public void writeToParcel(Parcel var1, int var2) {
    ParcelUtils.writeToParcel(var1, this.receiveUserId);
    ParcelUtils.writeToParcel(var1, this.receiveUserName);
    ParcelUtils.writeToParcel(var1, this.sendUserName);
  }
  protected RedPacketOpenedMessage(Parcel var1) {
    this.setReceiveUserId(ParcelUtils.readFromParcel(var1));
    this.setReceiveUserName(ParcelUtils.readFromParcel(var1));
    this.setSendUserName(ParcelUtils.readFromParcel(var1));
  }
  public String getSendUserName() {
    return this.sendUserName;
  }

  public void setSendUserName(String var1) {
    this.sendUserName = var1;
  }

  public void setReceiveUserName(String var1) {
    this.receiveUserName = var1;
  }

  public String getReceiveUserName() {
    return this.receiveUserName;
  }

  public String getReceiveUserId() {
    return this.receiveUserId;
  }

  public void setReceiveUserId(String var1) {
    this.receiveUserId = var1;
  }
}
