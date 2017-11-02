package cordova.plugin.ismartnet.rongcloud.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

/**
 * Created by lvping on 2017/10/10.
 */

public class GroupMemberDao extends AbstractDao<GroupMember,Void>{
  public static final String TABLENAME = "GROUP_MEMBER";

  /**
   * Properties of entity GroupMember.<br/>
   * Can be used for QueryBuilder and for referencing column names.
   */
  public static class Properties {
    public final static Property GroupId = new Property(0, String.class, "groupId", false, "GROUP_ID");
    public final static Property UserId = new Property(1, String.class, "userId", false, "USER_ID");
    public final static Property Name = new Property(2, String.class, "name", false, "NAME");
    public final static Property PortraitUri = new Property(3, String.class, "portraitUri", false, "PORTRAIT_URI");
    public final static Property DisplayName = new Property(4, String.class, "displayName", false, "DISPLAY_NAME");
    public final static Property NameSpelling = new Property(5, String.class, "nameSpelling", false, "NAME_SPELLING");
    public final static Property DisplayNameSpelling = new Property(6, String.class, "displayNameSpelling", false, "DISPLAY_NAME_SPELLING");
    public final static Property GroupName = new Property(7, String.class, "groupName", false, "GROUP_NAME");
    public final static Property GroupNameSpelling = new Property(8, String.class, "groupNameSpelling", false, "GROUP_NAME_SPELLING");
    public final static Property GroupPortraitUri = new Property(9, String.class, "groupPortraitUri", false, "GROUP_PORTRAIT_URI");
  }

  public GroupMemberDao(DaoConfig config) {
    super(config);
  }

  public GroupMemberDao(DaoConfig config, AbstractDaoSession daoSession) {
    super(config, daoSession);
  }

  /** Creates the underlying database table. */
  public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
    String constraint = ifNotExists ? "IF NOT EXISTS " : "";
    db.execSQL("CREATE TABLE " + constraint + "'GROUP_MEMBER' (" + //
      "'GROUP_ID' TEXT NOT NULL ," + // 0: groupId
      "'USER_ID' TEXT NOT NULL ," + // 1: userId
      "'NAME' TEXT," + // 2: name
      "'PORTRAIT_URI' TEXT," + // 3: portraitUri
      "'DISPLAY_NAME' TEXT," + // 4: displayName
      "'NAME_SPELLING' TEXT," + // 5: nameSpelling
      "'DISPLAY_NAME_SPELLING' TEXT," + // 6: displayNameSpelling
      "'GROUP_NAME' TEXT," + // 7: groupName
      "'GROUP_NAME_SPELLING' TEXT," + // 8: groupNameSpelling
      "'GROUP_PORTRAIT_URI' TEXT);"); // 9: groupPortraitUri
    // Add Indexes
    db.execSQL("CREATE INDEX " + constraint + "IDX_GROUP_MEMBER_NAME_DISPLAY_NAME_NAME_SPELLING_DISPLAY_NAME_SPELLING_GROUP_NAME_GROUP_NAME_SPELLING ON GROUP_MEMBER" +
      " (NAME,DISPLAY_NAME,NAME_SPELLING,DISPLAY_NAME_SPELLING,GROUP_NAME,GROUP_NAME_SPELLING);");
  }

  /** Drops the underlying database table. */
  public static void dropTable(SQLiteDatabase db, boolean ifExists) {
    String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'GROUP_MEMBER'";
    db.execSQL(sql);
  }

  @Override
  protected GroupMember readEntity(Cursor cursor, int i) {
    GroupMember entity = new GroupMember(
      cursor.getString(i + 0), // groupId
      cursor.getString(i + 1), // userId
      cursor.isNull(i + 2) ? null : cursor.getString(i + 2), // name
      cursor.isNull(i + 3) ? null : Uri.parse(cursor.getString(i + 3)), // portraitUri
      cursor.isNull(i + 4) ? null : cursor.getString(i + 4), // groupName
      cursor.isNull(i + 5) ? null : cursor.getString(i + 5) // groupPortraitUri
    );

    return entity;
  }

  @Override
  protected Void readKey(Cursor cursor, int i) {
    return null;
  }

  @Override
  protected void readEntity(Cursor cursor, GroupMember groupMember, int i) {
    groupMember.setGroupId(cursor.getString(i + 0));
    groupMember.setUserId(cursor.getString(i + 1));
    groupMember.setName(cursor.isNull(i + 2) ? null : cursor.getString(i + 2));
    groupMember.setPortraitUri(cursor.isNull(i + 3) ? null : Uri.parse(cursor.getString(i + 3)));
    groupMember.setGroupName(cursor.isNull(i + 4) ? null : cursor.getString(i + 4));
    groupMember.setGroupPortraitUri(cursor.isNull(i + 5) ? null : cursor.getString(i + 5));
  }

  @Override
  protected void bindValues(SQLiteStatement sqLiteStatement, GroupMember groupMember) {
    sqLiteStatement.clearBindings();
    sqLiteStatement.bindString(1, groupMember.getGroupId());
    sqLiteStatement.bindString(2, groupMember.getUserId());

    String name = groupMember.getName();
    if (name != null) {
      sqLiteStatement.bindString(3, name);
    }

    Uri portraitUri = groupMember.getPortraitUri();
    if (portraitUri != null) {
      sqLiteStatement.bindString(4, portraitUri.toString());
    }


    String groupName = groupMember.getGroupName();
    if (groupName != null) {
      sqLiteStatement.bindString(5, groupName);
    }
    String groupPortraitUri = groupMember.getGroupPortraitUri();
    if (groupPortraitUri != null) {
      sqLiteStatement.bindString(6, groupPortraitUri);
    }
  }

  @Override
  protected Void updateKeyAfterInsert(GroupMember groupMember, long l) {
    return null;
  }

  @Override
  protected Void getKey(GroupMember groupMember) {
    return null;
  }

  @Override
  protected boolean isEntityUpdateable() {
    return false;
  }
}
