package cordova.plugin.ismartnet.rongcloud.bean;

/**
 * Created by lvping on 2017/11/8.
 */

public class SwitchVideoModel {
  private String url;
  private String name;

  public SwitchVideoModel(String name, String url) {
    this.name = name;
    this.url = url;
  }
  public SwitchVideoModel(String url) {
    this.url = url;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return this.name;
  }
}
