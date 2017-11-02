package cordova.plugin.ismartnet.rongcloud.dao;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import cordova.plugin.ismartnet.rongcloud.App;
import cordova.plugin.ismartnet.rongcloud.bean.GroupMsg;
import cordova.plugin.ismartnet.rongcloud.bean.Groups;
import cordova.plugin.ismartnet.rongcloud.bean.User;
import cordova.plugin.ismartnet.rongcloud.retrofit.HttpUtil;
import cordova.plugin.ismartnet.rongcloud.retrofit.api.Api;
import cordova.plugin.ismartnet.rongcloud.retrofit.callback.MyCallBack;
import cordova.plugin.ismartnet.rongcloud.utils.DialogDisplay;
import cordova.plugin.ismartnet.rongcloud.utils.RongGenerate;
import cordova.plugin.ismartnet.rongcloud.utils.SharedPreferences;
import cordova.plugin.ismartnet.rongcloud.utils.StringUtil;
import cordova.plugin.ismartnet.rongcloud.utils.ToastUtils;
import io.rong.common.RLog;
import io.rong.imkit.RongIM;
import io.rong.imkit.tools.CharacterParser;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * Created by lvping on 2017/10/10.
 */

public class UserInfoManager {
  private final static String TAG = "UserInfoManager";
  /**
   * 用户信息全部未同步
   */
  private static final int NONE = 0;//00000
  /**
   * 用户信息全部同步成功
   */
  private static final int ALL = 27;//11011
  /**
   * 群组信息同步成功
   */
  private static final int GROUPS = 2;//00010
  /**
   * 群成员信息部分同步成功,n个群组n次访问网络,存在部分同步的情况
   */
  private static final int PARTGROUPMEMBERS = 4;//00100
  /**
   * 群成员信息同步成功
   */
  private static final int GROUPMEMBERS = 8;//01000

  private static UserInfoManager sInstance;
  static Handler mHandler;
  private final Context mContext;
  private List<Groups> mGroupsList;//同步群组成员信息时需要这个数据
  private GroupMemberDao mGroupMemberDao;
  private GroupsDao mGroupsDao;
  private DBManager mDBManager;
  private LinkedHashMap<String, UserInfo> mUserInfoCache;
  private int mGetAllUserInfoState;
  private boolean doingGetAllUserInfo = false;
  private Handler mWorkHandler;
  private HandlerThread mWorkThread;

  public static UserInfoManager getInstance() {
    return sInstance;
  }

  public UserInfoManager(Context context) {
    mContext = context;

    mHandler = new Handler(Looper.getMainLooper());
    mGroupsList = null;
  }

  public static void init(Context context) {
    RLog.d(TAG, "SealUserInfoManager init");
    sInstance = new UserInfoManager(context);
    SealAppContext.init(context);
  }

  /**
   * 修改数据库的存贮路径为.../appkey/userID/,
   * 必须确保userID存在后才能初始化DBManager
   */
  public void openDB() {
    RLog.e(TAG, "SealUserInfoManager openDB");
    if (mDBManager == null || !mDBManager.isInitialized()) {
      mDBManager = DBManager.init(mContext);
      mWorkThread = new HandlerThread("sealUserInfoManager");
      mWorkThread.start();
      mWorkHandler = new Handler(mWorkThread.getLooper());
      mGroupMemberDao = getGroupMemberDao();
      mGroupsDao = getGroupsDao();

      mUserInfoCache = new LinkedHashMap<>();
    }
    mGetAllUserInfoState = SharedPreferences.getInstance(mContext).getIntValue(Api.GETALLUSERINFOSTATE);
    RLog.e(TAG, "SealUserInfoManager mGetAllUserInfoState = " + mGetAllUserInfoState);
  }

  public void initDB() {
    mWorkThread = new HandlerThread("sealUserInfoManager");
    mWorkThread.start();
    mWorkHandler = new Handler(mWorkThread.getLooper());
    mGroupMemberDao = getGroupMemberDao();
    mGroupsDao = getGroupsDao();

    mUserInfoCache = new LinkedHashMap<>();
    mGetAllUserInfoState = SharedPreferences.getInstance(mContext).getIntValue(Api.GETALLUSERINFOSTATE);
    RLog.e(TAG, "SealUserInfoManager mGetAllUserInfoState = " + mGetAllUserInfoState);
  }

  public void closeDB() {
    RLog.e(TAG, "SealUserInfoManager closeDB");
    if (mDBManager != null) {
      mDBManager.uninit();
      mDBManager = null;
      mGroupMemberDao = null;
    }
    if (mUserInfoCache != null) {
      mUserInfoCache.clear();
      mUserInfoCache = null;
    }
    mGroupsList = null;
  }

  private GroupMemberDao getGroupMemberDao() {
    if (mDBManager != null && mDBManager.getDaoSession() != null) {
      return mDBManager.getDaoSession().getGroupMemberDao();
    } else {
      return null;
    }
  }

  private GroupsDao getGroupsDao() {
    if (mDBManager != null && mDBManager.getDaoSession() != null) {
      return mDBManager.getDaoSession().getGroupsDao();
    } else {
      return null;
    }
  }

  private void setGetAllUserInfoDone() {
    RLog.e(TAG, "SealUserInfoManager setGetAllUserInfoDone = " + mGetAllUserInfoState);
    doingGetAllUserInfo = false;
    SharedPreferences.getInstance(mContext).putIntValue(Api.GETALLUSERINFOSTATE, mGetAllUserInfoState);
  }


  /**
   * kit中提供用户信息的用户信息提供者
   * 1.读缓存
   * 3.读群组成员数据库
   * 4.网络获取
   */
  public void getUserInfo(final String userId) {
    if (TextUtils.isEmpty(userId)) {
      return;
    }
    if (mUserInfoCache != null) {
      UserInfo userInfo = mUserInfoCache.get(userId);
      if (userInfo != null) {
        RongIM.getInstance().refreshUserInfoCache(userInfo);
        RLog.e(TAG, "SealUserInfoManager getUserInfo from cache " + userId + " "
          + userInfo.getName() + " " + userInfo.getPortraitUri());
        return;
      }
    }
    mWorkHandler.post(new Runnable() {
      @Override
      public void run() {
        UserInfo userInfo;
        List<GroupMember> groupMemberList = getGroupMembersWithUserId(userId);
        if (groupMemberList != null && groupMemberList.size() > 0) {
          GroupMember groupMember = groupMemberList.get(0);
          userInfo = new UserInfo(groupMember.getUserId(), groupMember.getName(),
            groupMember.getPortraitUri());
          RLog.e(TAG, "SealUserInfoManager getUserInfo from GroupMember db " + userId + " "
            + userInfo.getName() + " " + userInfo.getPortraitUri());
          RongIM.getInstance().refreshUserInfoCache(userInfo);
          return;
        }
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("uId", userId);
        map.put("tokenId", SharedPreferences.getInstance(mContext).getStringValue(Api.BD_TOKEN_ID));
        Call<User> userCall = HttpUtil.getService().getUser(SharedPreferences.getInstance(mContext).getStringValue(Api.GETUSER_INFO), map);
        userCall.enqueue(new MyCallBack<User>() {
          @Override
          public void onSuccess(Response<User> response) {
            String userName;
            if (StringUtil.isNotEmptyAndNull(response.body().getResult().getUserName())) {
              userName = "匿名用户";
            } else {
              userName = response.body().getResult().getUserName();
            }
            if (StringUtil.isEmptyAndNull(response.body().getResult().getHeadImg())) {
              response.body().getResult().setHeadImg(RongGenerate.generateDefaultAvatar(userName, userId));
            }
            String userIconn = response.body().getResult().getHeadImg();
            SharedPreferences.getInstance(mContext).putStringValue(Api.ICON, userIconn);
            Log.e("userName", userName);
            UserInfo userInfo = new UserInfo(userId, userName, Uri.parse(userIconn));
            RongIM.getInstance().refreshUserInfoCache(userInfo);
          }

          @Override
          public void onFail(String message) {
            ToastUtils.show(mContext, message);
          }
        });
      }
    });
  }

  /**
   * 获取群组信息
   *
   * @param groupsId
   */
  public void getGroupInfo(final String groupsId) {
    if (TextUtils.isEmpty(groupsId)) {
      return;
    }
    mWorkHandler.post(new Runnable() {
      @Override
      public void run() {
        Group groupInfo;
        Groups groups = getGroupsByID(groupsId);
        if (groups != null) {
          groupInfo = new Group(groupsId, groups.getName(), Uri.parse(groups.getPortraitUri()));
          RongIM.getInstance().refreshGroupInfoCache(groupInfo);
          RLog.e(TAG, "SealUserInfoManager getGroupInfo from db " + groupsId + " "
            + groupInfo.getName() + " " + groupInfo.getPortraitUri());
          return;
        }
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("tokenId", SharedPreferences.getInstance(mContext).getStringValue(Api.BD_TOKEN_ID));
        map.put("taskId", groupsId);
        Call<cordova.plugin.ismartnet.rongcloud.bean.Group> groupCall = HttpUtil.getService().getGroup(SharedPreferences.getInstance(mContext).getStringValue(Api.GETGROUP_INFO), map);
        groupCall.enqueue(new MyCallBack<cordova.plugin.ismartnet.rongcloud.bean.Group>() {
          @Override
          public void onSuccess(Response<cordova.plugin.ismartnet.rongcloud.bean.Group> response) {
            String groupName = response.body().getResult().getGroupName();
            if (StringUtil.isEmptyAndNull(response.body().getResult().getPortraitUri())) {
              response.body().getResult().setPortraitUri(RongGenerate.generateDefaultAvatar(groupName, groupsId));
            }
            String icon = response.body().getResult().getPortraitUri();
            String num = response.body().getResult().getTotalUsers();
            SharedPreferences.getInstance(mContext).putStringValue(Api.GROUPNUM, num);
            Group group = new Group(groupsId, groupName, Uri.parse(icon));
            RongIM.getInstance().refreshGroupInfoCache(group);
          }

          @Override
          public void onFail(String message) {
            ToastUtils.show(mContext, message);
          }
        });
      }
    });
  }

  public void getGroupMember(final String groupID) {
    mWorkHandler.post(new Runnable() {
      @Override
      public void run() {
        if (doingGetAllUserInfo)
          return;
        if (groupID != null) {
          DialogDisplay.getInstance().dialogLoading((Activity) mContext, "正在加载数据...");
          HashMap<String, String> map = new HashMap<String, String>();
          map.put("tokenId", SharedPreferences.getInstance(mContext).getStringValue(Api.BD_TOKEN_ID));
          map.put("taskId", groupID);
          Call<GroupMsg> groupMsgCall = HttpUtil.getService().getGroupMembers(SharedPreferences.getInstance(mContext).getStringValue(Api.GROUP_CHAT), map);
          groupMsgCall.enqueue(new Callback<GroupMsg>() {
            @Override
            public void onResponse(Call<GroupMsg> call, Response<GroupMsg> response) {
              if (response.code() == 200) {
                List<GroupMsg.ResultBean> list = response.body().getResult();
                if (list != null && list.size() > 0) {
                  syncDeleteGroupMembers(groupID);
                  addGroupMembers(list, groupID);
                }
                DialogDisplay.getInstance().dialogCloseLoading((Activity) mContext);
              } else if (response.code() == 401) {
                DialogDisplay.getInstance().dialogCloseLoading((Activity) mContext);
                setGetAllUserInfoWithPartGroupMembersState();
                SharedPreferences.getInstance(mContext).putIntValue(Api.GETALLUSERINFOSTATE, mGetAllUserInfoState);
                ToastUtils.show(mContext, "用户授权过期，请重新登陆");
                mHandler.postDelayed(new Runnable() {
                  @Override
                  public void run() {
                    App.getInstance().exit();
                  }
                }, 2000);
              }
            }

            @Override
            public void onFailure(Call<GroupMsg> call, Throwable t) {
              DialogDisplay.getInstance().dialogCloseLoading((Activity) mContext);
              ToastUtils.show(mContext, "网络连接超时，请稍后再试~！");
            }
          });
        }
      }
    });
  }

  private boolean isNetworkConnected() {
    ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo ni = cm.getActiveNetworkInfo();
    return ni != null && ni.isConnectedOrConnecting();
  }

  /**
   * 删除一个群组里的所有群成员
   *
   * @param groupID 群组ID
   */
  private void syncDeleteGroupMembers(String groupID) {
    if (mGroupMemberDao != null) {
      mGroupMemberDao.queryBuilder()
        .where(GroupMemberDao.Properties.GroupId.eq(groupID))
        .buildDelete().executeDeleteWithoutDetachingEntities();
    }
  }

  private void syncDeleteGroupMembers() {
    if (mGroupMemberDao != null) {
      mGroupMemberDao.deleteAll();
    }
  }

  /**
   * 同步接口,获取1个群组信息
   *
   * @param groupID 群组ID
   * @return Groups 群组信息
   */
  public Groups getGroupsByID(String groupID) {
    if (TextUtils.isEmpty(groupID)) {
      return null;
    } else {
      if (mGroupsDao != null) {
        return mGroupsDao.load(groupID);
      } else {
        return null;
      }
    }
  }

  /**
   * 同步获取群组成员信息
   *
   * @param userId 用户Id
   * @return List<GroupMember> 群组成员列表
   */
  public List<GroupMember> getGroupMembersWithUserId(String userId) {
    if (TextUtils.isEmpty(userId)) {
      return null;
    } else {
      if (mGroupMemberDao != null) {
        return mGroupMemberDao.queryBuilder().
          where(GroupMemberDao.Properties.UserId.eq(userId)).list();
      } else {
        return null;
      }
    }
  }

  /**
   * 同步接口,从server获取的群成员信息插入数据库,目前只有同步接口,如果需要可以加异步接口
   *
   * @param list    server获取的群组信息
   * @param groupID 群组ID
   * @return List<GroupMember> 群组成员列表
   */
  private List<GroupMember> addGroupMembers(final List<GroupMsg.ResultBean> list, final String groupID) {
    if (list != null && list.size() > 0) {
      List<GroupMember> groupsMembersList = setCreatedToTop(list, groupID);
      if (groupsMembersList != null && groupsMembersList.size() > 0) {
        for (GroupMember groupMember : groupsMembersList) {
          if (groupMember != null && (groupMember.getPortraitUri() == null || TextUtils.isEmpty(groupMember.getPortraitUri().toString()))) {
            Uri portrait = getPortrait(groupMember);
            groupMember.setPortraitUri(portrait);
          }
        }
        if (mGroupMemberDao != null) {
          mGroupMemberDao.insertOrReplaceInTx(groupsMembersList);
        }
      }
      return groupsMembersList;
    } else {
      return null;
    }
  }

  private List<GroupMember> setCreatedToTop(List<GroupMsg.ResultBean> groupMember, String groupID) {
    List<GroupMember> newList = new ArrayList<>();
    GroupMember created = null;
    for (GroupMsg.ResultBean group : groupMember) {
      String groupName = null;
      String groupPortraitUri = null;
      Groups groups = getGroupsByID(groupID);
      if (groups != null) {
        groupName = groups.getName();
        groupPortraitUri = groups.getPortraitUri();
      }
      GroupMember newMember = new GroupMember(groupID,
        group.getUId(),
        group.getUName(),
        Uri.parse(group.getHeadImg()),
        groupName,
        groupPortraitUri);
      newList.add(newMember);
    }
    if (created != null) {
      newList.add(created);
    }
    Collections.reverse(newList);
    return newList;
  }

  private Uri getPortrait(GroupMember groupMember) {
    if (groupMember != null) {
      if (groupMember.getPortraitUri() == null || TextUtils.isEmpty(groupMember.getPortraitUri().toString())) {
        if (TextUtils.isEmpty(groupMember.getUserId())) {
          return null;
        } else {
          UserInfo userInfo = mUserInfoCache.get(groupMember.getUserId());
          if (userInfo != null) {
            if (userInfo.getPortraitUri() != null && !TextUtils.isEmpty(userInfo.getPortraitUri().toString())) {
              return userInfo.getPortraitUri();
            } else {
              mUserInfoCache.remove(groupMember.getUserId());
            }
          }
          List<GroupMember> groupMemberList = getGroupMembersWithUserId(groupMember.getUserId());
          if (groupMemberList != null && groupMemberList.size() > 0) {
            GroupMember member = groupMemberList.get(0);
            if (member.getPortraitUri() != null && !TextUtils.isEmpty(member.getPortraitUri().toString())) {
              return member.getPortraitUri();
            }
          }
          String portrait = RongGenerate.generateDefaultAvatar(groupMember.getName(), groupMember.getUserId());
          if (!TextUtils.isEmpty(portrait)) {
            userInfo = new UserInfo(groupMember.getUserId(), groupMember.getName(), Uri.parse(portrait));
            mUserInfoCache.put(groupMember.getUserId(), userInfo);
            return Uri.parse(portrait);
          } else {
            return null;
          }
        }
      } else {
        return groupMember.getPortraitUri();
      }
    }
    return null;
  }

  private boolean hasGetAllGroupMembers() {
    return ((mGetAllUserInfoState & GROUPMEMBERS) != 0)
      && ((mGetAllUserInfoState & PARTGROUPMEMBERS) == 0);
  }

  private boolean hasGetPartGroupMembers() {
    return ((mGetAllUserInfoState & GROUPMEMBERS) == 0)
      && ((mGetAllUserInfoState & PARTGROUPMEMBERS) != 0);
  }

  private void setGetAllUserInfoWithPartGroupMembersState() {
    mGetAllUserInfoState &= ~GROUPMEMBERS;
    mGetAllUserInfoState |= PARTGROUPMEMBERS;
  }

  private boolean hasGetAllUserInfo() {
    return mGetAllUserInfoState == ALL;
  }

  private boolean needGetAllUserInfo() {
    return mGetAllUserInfoState == NONE;
  }

}
