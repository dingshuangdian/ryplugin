package cordova.plugin.ismartnet.rongcloud.activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import cordova.plugin.ismartnet.rongcloud.base.BaseActivity;
import cordova.plugin.ismartnet.rongcloud.utils.ResourcesUtils;
import cordova.plugin.ismartnet.rongcloud.view.DialogWithYesOrNoUtils;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.emoticon.AndroidEmoji;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.MentionedInfo;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;
/**
 * Created by lvping on 2017/10/11.
 */

public class GroupNoticeActivity extends BaseActivity implements View.OnClickListener,TextWatcher{
  EditText mEdit;
  Conversation.ConversationType mConversationType;
  String mTargetId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(ResourcesUtils.getLayoutId(this,"activity_group_notice"));
    mEdit = (EditText) findViewById(ResourcesUtils.getId(this,"edit_area"));
    Intent intent = getIntent();
    mConversationType = Conversation.ConversationType.setValue(intent.getIntExtra("conversationType", 0));
    mTargetId = getIntent().getStringExtra("targetId");
    setTitle(ResourcesUtils.getStringId(this,"group_announcement"));
    Button rightButton = getHeadRightButton();
    rightButton.setVisibility(View.GONE);
    mHeadRightText.setVisibility(View.VISIBLE);
    mHeadRightText.setText(ResourcesUtils.getStringId(this,"Done"));
    mHeadRightText.setTextColor(getResources().getColor(android.R.color.darker_gray));
    mHeadRightText.setClickable(false);
    mHeadRightText.setOnClickListener(this);
    mEdit.addTextChangedListener(this);
  }

  @Override
  public void onHeadLeftButtonClick(View v) {
    DialogWithYesOrNoUtils.getInstance().showDialog(this, getString(ResourcesUtils.getStringId(this,"group_notice_exist_confirm")), new DialogWithYesOrNoUtils.DialogCallBack() {
      @Override
      public void executeEvent() {
        finish();
      }

      @Override
      public void executeEditEvent(String editText) {

      }
    });
  }

  @Override
  public void onClick(View v) {
        DialogWithYesOrNoUtils.getInstance().showDialog(this, getString(ResourcesUtils.getStringId(this,"group_notice_post_confirm")), new DialogWithYesOrNoUtils.DialogCallBack() {
          @Override
          public void executeEvent() {
            TextMessage textMessage = TextMessage.obtain(RongContext.getInstance().getString(ResourcesUtils.getStringId(GroupNoticeActivity.this,"group_notice_prefix"))+ mEdit.getText().toString());
            MentionedInfo mentionedInfo = new MentionedInfo(MentionedInfo.MentionedType.ALL, null, null);
            textMessage.setMentionedInfo(mentionedInfo);

            RongIM.getInstance().sendMessage(Message.obtain(mTargetId, mConversationType, textMessage), null, null, new IRongCallback.ISendMessageCallback() {
              @Override
              public void onAttached(Message message) {

              }

              @Override
              public void onSuccess(Message message) {

              }
              @Override
              public void onError(Message message, RongIMClient.ErrorCode errorCode) {

              }
            });
            finish();
          }
          @Override
          public void executeEditEvent(String editText) {

          }
        });
  }

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {

  }

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {
    if (s.toString().length() > 0) {
      mHeadRightText.setClickable(true);
      mHeadRightText.setTextColor(ResourcesUtils.getColorId(this,"white"));
    } else {
      mHeadRightText.setClickable(false);
      mHeadRightText.setTextColor(ResourcesUtils.getColorId(this,"color"));
    }
  }

  @Override
  public void afterTextChanged(Editable s) {
    if ( s != null) {
      int start = mEdit.getSelectionStart();
      int end = mEdit.getSelectionEnd();
      mEdit.removeTextChangedListener(this);
      mEdit.setText(AndroidEmoji.ensure(s.toString()));
      mEdit.addTextChangedListener(this);
      mEdit.setSelection(start, end);
    }
  }
}
