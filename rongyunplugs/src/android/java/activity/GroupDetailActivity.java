package cordova.plugin.ismartnet.rongcloud.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.guoji.tpco.R;

import java.util.HashMap;
import java.util.List;

import cordova.plugin.ismartnet.rongcloud.adapter.GridAdapter;
import cordova.plugin.ismartnet.rongcloud.base.BaseActivity;
import cordova.plugin.ismartnet.rongcloud.bean.Group;
import cordova.plugin.ismartnet.rongcloud.bean.GroupMsg;
import cordova.plugin.ismartnet.rongcloud.retrofit.HttpUtil;
import cordova.plugin.ismartnet.rongcloud.retrofit.api.Api;
import cordova.plugin.ismartnet.rongcloud.retrofit.callback.MyCallBack;
import cordova.plugin.ismartnet.rongcloud.utils.LoadDialog;
import cordova.plugin.ismartnet.rongcloud.utils.SharedPreferences;
import cordova.plugin.ismartnet.rongcloud.utils.ToastUtils;
import cordova.plugin.ismartnet.rongcloud.view.DemoGridView;
import io.rong.imlib.model.Conversation;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by lvping on 2017/10/10.
 */

public class GroupDetailActivity extends BaseActivity implements View.OnClickListener {
  private DemoGridView mGridView;
  private List<GroupMsg.ResultBean> mGroupMember;
  private Group mGroup;
  //private TextView mTextViewMemberSize;
  private String fromConversationId;
  private LinearLayout mGroupNotice;
  private Conversation.ConversationType mConversationType;
  private boolean isFromConversation;
  private LinearLayout group_announcement;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail_group);
    initViews();
    setTitle(R.string.group_info);
    //群组会话界面点进群组详情
    fromConversationId = getIntent().getStringExtra("TargetId");
    mConversationType = (Conversation.ConversationType) getIntent().getSerializableExtra("conversationType");
    if (!TextUtils.isEmpty(fromConversationId)) {
      isFromConversation = true;
    }
    if (isFromConversation) {//群组会话页进入
      LoadDialog.show(mContext);
      //getGroups();
      getGroupMembers();
    }
  }

  private void initGroupMemberData() {
    if (mGroupMember != null && mGroupMember.size() > 0) {
      setTitle(getString(R.string.group_info) + "(" + mGroupMember.size() + ")");
      //mTextViewMemberSize.setText(getString(R.string.group_member_size) + "(" + mGroupMember.size() + ")");
      mGridView.setAdapter(new GridAdapter(mContext, mGroupMember));
    } else {
      return;
    }
  }

  private void getGroups() {
    HashMap<String, String> map = new HashMap<String, String>();
    map.put("tokenId", SharedPreferences.getInstance(mContext).getStringValue(Api.BD_TOKEN_ID));
    map.put("taskId", fromConversationId);
    Call<cordova.plugin.ismartnet.rongcloud.bean.Group> groupCall = HttpUtil.getService().getGroup(SharedPreferences.getInstance(mContext).getStringValue(Api.GETGROUP_INFO), map);
    groupCall.enqueue(new MyCallBack<cordova.plugin.ismartnet.rongcloud.bean.Group>() {
      @Override
      public void onSuccess(Response<cordova.plugin.ismartnet.rongcloud.bean.Group> response) {
        if (response.body() != null) {
          mGroup = response.body();
          //initGroupData();
        }
      }

      @Override
      public void onFail(String message) {
        ToastUtils.show(mContext, message);
      }
    });
  }

  private void initGroupData() {

  }

  private void getGroupMembers() {
    HashMap<String, String> map = new HashMap<String, String>();
    map.put("tokenId", SharedPreferences.getInstance(mContext).getStringValue(Api.BD_TOKEN_ID));
    map.put("taskId", fromConversationId);
    Call<GroupMsg> groupMsgCall = HttpUtil.getService().getGroupMembers(SharedPreferences.getInstance(mContext).getStringValue(Api.GROUP_CHAT), map);
    groupMsgCall.enqueue(new MyCallBack<GroupMsg>() {
      @Override
      public void onSuccess(Response<GroupMsg> response) {
        LoadDialog.dismiss(mContext);
        if (response.body().getResult() != null && response.body().getResult().size() > 0) {
          mGroupMember = response.body().getResult();
          initGroupMemberData();
        }
      }

      @Override
      public void onFail(String message) {
        LoadDialog.dismiss(mContext);
        ToastUtils.show(mContext, message);
      }
    });

  }

  private void initViews() {
    //mTextViewMemberSize = (TextView) findViewById(R.id.group_member_size);
    mGridView = (DemoGridView) findViewById(R.id.gridview);
    mGroupNotice = (LinearLayout) findViewById(R.id.group_announcement);
    group_announcement = (LinearLayout) findViewById(R.id.group_announcement);
    if (SharedPreferences.getInstance(mContext).getBooleanValue(Api.SHOWANNOUNCEMENT)) {
      group_announcement.setVisibility(View.VISIBLE);
    } else {
      group_announcement.setVisibility(View.GONE);
    }
    mGroupNotice.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.group_announcement:
        Intent tempIntent = new Intent(mContext, GroupNoticeActivity.class);
        tempIntent.putExtra("conversationType", Conversation.ConversationType.GROUP.getValue());
        tempIntent.putExtra("targetId", fromConversationId);
        startActivity(tempIntent);
        break;
    }

  }
}
