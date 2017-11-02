package cordova.plugin.ismartnet.rongcloud.msg;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;

/**
 * Created by lvping on 2017/9/11.
 */
@MessageTag(
  value = "ZWHL:sendBonusMsg",
  flag = 3
)
public class RedPacketMessage extends MessageContent {
  private String content;
  private String Bribery_ID;
  public static final Parcelable.Creator<RedPacketMessage> CREATOR = new Parcelable.Creator() {
    public RedPacketMessage createFromParcel(Parcel var1) {
      return new RedPacketMessage(var1);
    }

    public RedPacketMessage[] newArray(int var1) {
      return new RedPacketMessage[var1];
    }
  };

  @Override
  public byte[] encode() {
    JSONObject var1 = new JSONObject();
    try {
      var1.put("memo", getContent());
      var1.put("bonusId", getBribery_ID());
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

  public RedPacketMessage(byte[] data) {
    String jsonStr = null;

    try {
      jsonStr = new String(data, "UTF-8");
    } catch (UnsupportedEncodingException e1) {

    }
    try {
      JSONObject jsonObj = new JSONObject(jsonStr);
      if (jsonObj.has("memo")) {
        this.setContent(jsonObj.optString("memo"));
      }
      if (jsonObj.has("bonusId")) {
        this.setBribery_ID(jsonObj.optString("bonusId"));
      }
    } catch (JSONException e) {
    }
  }

  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(Bribery_ID);
    dest.writeString(content);
  }

  public RedPacketMessage(Parcel var1) {
    Bribery_ID = var1.readString();
    content = var1.readString();
  }

  public String getContent() {
    return this.content;
  }

  public void setContent(String var1) {
    this.content = var1;
  }

  public String getBribery_ID() {
    return this.Bribery_ID;
  }

  public void setBribery_ID(String var1) {
    this.Bribery_ID = var1;
  }
}
