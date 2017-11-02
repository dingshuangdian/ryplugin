package cordova.plugin.ismartnet.rongcloud.dao;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import cordova.plugin.ismartnet.rongcloud.bean.Groups;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

/**
 * Created by lvping on 2017/10/10.
 */

public class DaoSession extends AbstractDaoSession {
  private final DaoConfig groupMemberDaoConfig;
  private final DaoConfig groupsDaoConfig;
  private final GroupMemberDao groupMemberDao;
  private final GroupsDao groupsDao;

  public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
    daoConfigMap) {
    super(db);


    groupMemberDaoConfig = daoConfigMap.get(GroupMemberDao.class).clone();
    groupMemberDaoConfig.initIdentityScope(type);
    groupsDaoConfig = daoConfigMap.get(GroupsDao.class).clone();
    groupsDaoConfig.initIdentityScope(type);

    groupMemberDao = new GroupMemberDao(groupMemberDaoConfig, this);
    groupsDao = new GroupsDao(groupsDaoConfig, this);
    registerDao(GroupMember.class, groupMemberDao);
    registerDao(Groups.class, groupsDao);
  }

  public void clear() {

    groupMemberDaoConfig.getIdentityScope().clear();
    groupsDaoConfig.getIdentityScope().clear();
  }
  public GroupMemberDao getGroupMemberDao() {
    return groupMemberDao;
  }
  public GroupsDao getGroupsDao() {
    return groupsDao;
  }
}
