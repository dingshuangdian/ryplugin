package cordova.plugin.ismartnet.rongcloud.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import cordova.plugin.ismartnet.rongcloud.base.BaseActivity;
import cordova.plugin.ismartnet.rongcloud.bean.GroupMsg;
import cordova.plugin.ismartnet.rongcloud.retrofit.HttpUtil;
import cordova.plugin.ismartnet.rongcloud.retrofit.api.Api;
import cordova.plugin.ismartnet.rongcloud.retrofit.callback.MyCallBack;
import cordova.plugin.ismartnet.rongcloud.utils.LoadingDialog;
import cordova.plugin.ismartnet.rongcloud.utils.ResourcesUtils;
import cordova.plugin.ismartnet.rongcloud.utils.RongGenerate;
import cordova.plugin.ismartnet.rongcloud.utils.SharedPreferences;
import cordova.plugin.ismartnet.rongcloud.utils.StringUtil;
import cordova.plugin.ismartnet.rongcloud.utils.ToastUtils;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by lvping on 2017/8/31.
 */

/**
 * 会话页面
 * 1，设置 ActionBar title
 * 2，加载会话页面
 * 3，push 和 通知 判断
 */
public class ConversationActivity extends BaseActivity{
  private String TAG = ConversationActivity.class.getSimpleName();
  /**
   * 群id
   */
  private String mTargetId;
  private Conversation.ConversationType mConversationType;
  private String title;
  private LoadingDialog mDialog;
  private Button mRightButton;
  private boolean isFromPush = false;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(ResourcesUtils.getLayoutId(this,"con_layout"));

    mDialog = new LoadingDialog(this);

    mRightButton = getHeadRightButton();
    Intent intent = getIntent();
    if (intent == null || intent.getData() == null) {
      return;
    }
    mTargetId = intent.getData().getQueryParameter("targetId");
    Log.e("Conversation.targetId", mTargetId);
    mConversationType = Conversation.ConversationType.valueOf(intent.getData()
      .getLastPathSegment().toUpperCase(Locale.US));
    title = intent.getData().getQueryParameter("title");
    Log.e("Conversation.title", title);
    if (mConversationType.equals(Conversation.ConversationType.GROUP)) {
      mRightButton.setBackground(getResources().getDrawable(ResourcesUtils.getDrawableId(this,"icon2_menu")));
    } else {
      mRightButton.setVisibility(View.GONE);
      mRightButton.setClickable(false);
    }
    mRightButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        enterSettingActivity();

      }
    });
    isPushMessage(intent);
    setGroupActionBar();
    RongIM.getInstance().setGroupMembersProvider(new RongIM.IGroupMembersProvider() {
      @Override
      public void getGroupMembers(String s, RongIM.IGroupMemberCallback iGroupMemberCallback) {
        getGroupMembersForCall();
        mCallMemberResult = iGroupMemberCallback;
      }
    });
  }

  private RongIM.IGroupMemberCallback mCallMemberResult;

  private void getGroupMembersForCall() {
    HashMap<String, String> map = new HashMap<String, String>();
    map.put("tokenId", SharedPreferences.getInstance(mContext).getStringValue(Api.BD_TOKEN_ID));
    map.put("taskId", mTargetId);
    Log.e("taskIdmTargetId", mTargetId);
    Call<GroupMsg> groupMsgCall = HttpUtil.getService().getGroupMembers(SharedPreferences.getInstance(mContext).getStringValue(Api.GROUP_CHAT), map);
    groupMsgCall.enqueue(new MyCallBack<GroupMsg>() {
      @Override
      public void onSuccess(Response<GroupMsg> response) {
        List<UserInfo> userInfos = new ArrayList<>();
        if (response.body().getResult() != null) {
          for (GroupMsg.ResultBean groupMember : response.body().getResult()) {
            if (groupMember != null) {
              if (StringUtil.isEmptyAndNull(groupMember.getHeadImg())) {
                groupMember.setHeadImg(RongGenerate.generateDefaultAvatar(groupMember.getUName(), groupMember.getUId()));
              }
              String userIconn = groupMember.getHeadImg();
              UserInfo userInfo = new UserInfo(groupMember.getUId(), groupMember.getUName(), Uri.parse(userIconn));
              userInfos.add(userInfo);
            }
          }
        }
        mCallMemberResult.onGetGroupMembersResult(userInfos);
      }

      @Override
      public void onFail(String message) {

      }
    });
  }

  /**
   * 判断是否是 Push 消息，判断是否需要做 connect 操作
   */
  private void isPushMessage(Intent intent) {
    if (intent == null || intent.getData() == null)
      return;
    //push
    if (intent.getData().getScheme().equals("rong") && intent.getData().getQueryParameter("isFromPush") != null) {
//isFromPush
      //通过intent.getData().getQueryParameter("push") 为true，判断是否是push消息
      if (intent.getData().getQueryParameter("isFromPush").equals("true")) {
        //只有收到系统消息和不落地 push 消息的时候，pushId 不为 null。而且这两种消息只能通过 server 来发送，客户端发送不了。
        //RongIM.getInstance().getRongIMClient().recordNotificationEvent(id);
        if (mDialog != null && !mDialog.isShowing()) {
          mDialog.show();
        }

        enterActivity();
      } else if (RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
        if (mDialog != null && !mDialog.isShowing()) {
          mDialog.show();
        }

        enterActivity();
      } else {

        enterFragment(mConversationType, mTargetId);
      }

    } else {
      if (RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
        if (mDialog != null && !mDialog.isShowing()) {
          mDialog.show();
        }
        new android.os.Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
            enterActivity();
          }
        }, 300);
      } else {
        enterFragment(mConversationType, mTargetId);
      }
    }
  }
/*  */

  /**
   * 得到不落地 push 消息
   *//*
  private void getPushMessage(Intent intent) {
    if (intent != null && intent.getData() != null && intent.getData().getScheme().equals("rong")) {
      String path = intent.getData().getPath();
      if (path.contains("push_message")) {
        if (!RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED)) {
          LoadDialog.show(mContext);
          RongIM.connect(SharedPreferences.getInstance(mContext).getStringValue(Api.RY_USER_TOKEN), new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
              LoadDialog.dismiss(mContext);
            }
            @Override
            public void onSuccess(String s) {
              LoadDialog.dismiss(mContext);
            }
            @Override
            public void onError(RongIMClient.ErrorCode e) {
              LoadDialog.dismiss(mContext);
            }
          });
        }
      }
    }
  }*/
  private void enterActivity() {
    reconnect(SharedPreferences.getInstance(mContext).getStringValue(Api.RY_USER_TOKEN));
    Log.e("融云连接", "融云连接");
  }


  private ConversationFragment fragment;

  /**
   * 加载会话页面 ConversationFragmentEx 继承自 ConversationFragment
   *
   * @param mConversationType 会话类型
   * @param mTargetId         会话 Id
   */
  private void enterFragment(Conversation.ConversationType mConversationType, String mTargetId) {
    fragment = new ConversationFragment();

    Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
      .appendPath("conversation").appendPath(mConversationType.getName().toLowerCase())
      .appendQueryParameter("targetId", mTargetId).build();
    fragment.setUri(uri);
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    //xxx 为你要加载的 id
    transaction.add(ResourcesUtils.getId(this,"rong_content"), fragment);
    transaction.commitAllowingStateLoss();
  }


/*  *//**
   * 设置会话页面 Title
   *
   * @param conversationType 会话类型
   * @param targetId         目标 Id
   *//*
  private void setActionBarTitle(Conversation.ConversationType conversationType, String targetId) {

    if (conversationType == null)
      return;
    if (conversationType.equals(Conversation.ConversationType.GROUP)) {
      setGroupActionBar(targetId);
    } else {
      setTitle("聊天");
    }
  }*/

  /**
   * 设置群聊界面 ActionBar
   */
  private void setGroupActionBar() {
    if (!TextUtils.isEmpty(title)) {
      setTitle(title);
      Log.e(TAG, title.toString());
    } else {
      setTitle("群聊");
    }
  }

  /**
   * 根据 targetid 和 ConversationType 进入到设置页面
   */
  private void enterSettingActivity() {

    Intent intent = null;

    if (mConversationType == Conversation.ConversationType.GROUP) {
      intent = new Intent(this, GroupDetailActivity.class);
      intent.putExtra("conversationType", Conversation.ConversationType.GROUP);
      intent.putExtra("TargetId", mTargetId);
      if (intent != null) {
        startActivityForResult(intent, 500);
      }
    }
  }


  private void reconnect(String token) {
    RongIM.connect(token, new RongIMClient.ConnectCallback() {
      @Override
      public void onTokenIncorrect() {
        Log.e(TAG, "---onTokenIncorrect--");
      }

      @Override
      public void onSuccess(String s) {
        Log.e(TAG, "---onSuccess--" + s);
        if (mDialog != null)
          mDialog.dismiss();
        enterFragment(mConversationType, mTargetId);
      }

      @Override
      public void onError(RongIMClient.ErrorCode e) {
        Log.e(TAG, "---onError--" + e);
        if (mDialog != null)
          mDialog.dismiss();
        enterFragment(mConversationType, mTargetId);
        ToastUtils.show(mContext, "网络连接失败，请稍后再试~！");
      }
    });
  }
  @Override
  protected void onPause() {
    super.onPause();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (KeyEvent.KEYCODE_BACK == event.getKeyCode()) {
      if (fragment != null && !fragment.onBackPressed()) {
        hintKbTwo();
        ConversationActivity.this.finish();
      }
    }
    return false;
  }

  @Override
  public void onHeadLeftButtonClick(View v) {
    if (fragment != null && !fragment.onBackPressed()) {
      if (fragment.isLocationSharing()) {
        fragment.showQuitLocationSharingDialog(this);
        return;
      }
      hintKbTwo();
      ConversationActivity.this.finish();

    }

  }

  private void hintKbTwo() {
    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    if (imm.isActive() && getCurrentFocus() != null) {
      if (getCurrentFocus().getWindowToken() != null) {
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
      }
    }
  }
}
