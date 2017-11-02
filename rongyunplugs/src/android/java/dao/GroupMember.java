package cordova.plugin.ismartnet.rongcloud.dao;

import android.net.Uri;

import io.rong.imlib.model.UserInfo;

/**
 * Created by lvping on 2017/10/10.
 */

public class GroupMember extends UserInfo {

  /**
   * Not-null value.
   */
  private String groupId;
  private String nameSpelling;
  private String displayNameSpelling;
  private String groupName;
  private String groupNameSpelling;
  private String groupPortraitUri;

  public GroupMember(String userId, String name, Uri portraitUri) {
    super(userId, name, portraitUri);
  }

  public GroupMember(String groupId, String userId, String name, Uri portraitUri, String groupName,String groupPortraitUri) {
    super(userId, name, portraitUri);
    this.groupId = groupId;
    this.groupName = groupName;
    this.groupPortraitUri = groupPortraitUri;
  }

  public GroupMember(String groupId, String userId, String name, Uri portraitUri,String groupName) {
    super(userId, name, portraitUri);
    this.groupId = groupId;
    this.groupName = groupName;

  }

  public GroupMember(String groupId, String userId, String name, Uri portraitUri) {
    super(userId, name, portraitUri);
    this.groupId = groupId;

  }

  /**
   * Not-null value.
   */
  public String getGroupId() {
    return groupId;
  }

  /**
   * Not-null value; ensure this value is available before it is saved to the database.
   */
  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }



  public String getNameSpelling() {
    return nameSpelling;
  }

  public void setNameSpelling(String nameSpelling) {
    this.nameSpelling = nameSpelling;
  }

  public String getDisplayNameSpelling() {
    return displayNameSpelling;
  }

  public void setDisplayNameSpelling(String displayNameSpelling) {
    this.displayNameSpelling = displayNameSpelling;
  }

  public String getGroupName() {
    return groupName;
  }

  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  public String getGroupNameSpelling() {
    return groupNameSpelling;
  }

  public void setGroupNameSpelling(String groupNameSpelling) {
    this.groupNameSpelling = groupNameSpelling;
  }

  public String getGroupPortraitUri() {
    return groupPortraitUri;
  }

  public void setGroupPortraitUri(String groupPortraitUri) {
    this.groupPortraitUri = groupPortraitUri;
  }
}
