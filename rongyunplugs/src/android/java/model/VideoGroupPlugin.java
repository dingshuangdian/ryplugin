package cordova.plugin.ismartnet.rongcloud.model;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import cordova.plugin.ismartnet.rongcloud.activity.RecorderVideoActivity;
import cordova.plugin.ismartnet.rongcloud.msg.VideoMessage;
import cordova.plugin.ismartnet.rongcloud.utils.ResourcesUtils;
import io.rong.imkit.RongExtension;
import io.rong.imkit.RongIM;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

/**
 * Created by Lvping on 2017/11/5.
 */

public class VideoGroupPlugin implements IPluginModule {
  private Conversation.ConversationType conversationType;
  private String targetId;
  private Context mContext;
  private String filePath = null;
  private static final String TAG = "VideoGroupPlugin";

  @Override
  public Drawable obtainDrawable(Context context) {
    mContext = context;
    return ContextCompat.getDrawable(context, ResourcesUtils.getDrawableId(context,"rc_video_selector"));
  }

  @Override
  public String obtainTitle(Context context) {
    return "视频";
  }

  @Override
  public void onClick(Fragment fragment, RongExtension rongExtension) {
    this.conversationType = rongExtension.getConversationType();
    this.targetId = rongExtension.getTargetId();
    Log.e("targetId",targetId);
    Intent var3 = new Intent(rongExtension.getContext(), RecorderVideoActivity.class);
    rongExtension.startActivityForPluginResult(var3, 100, this);

  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == 100) {
        Uri uri = data.getParcelableExtra("uri");
        Log.e("uri", uri + "");
        String[] projects = new String[]{MediaStore.Video.Media.DATA,
          MediaStore.Video.Media.DURATION};
        Cursor cursor = mContext.getContentResolver().query(
          uri, projects, null,
          null, null);
        int duration = 0;
        if (cursor.moveToFirst()) {
          // path：MediaStore.Audio.Media.DATA
          filePath = cursor.getString(cursor
            .getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
          Log.e("filePath", filePath);
          // duration：MediaStore.Audio.Media.DURATION
          duration = cursor
            .getInt(cursor
              .getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
          Log.e(TAG, "duration:" + duration);
        }
        VideoMessage videoMessage = VideoMessage.obtain(Uri.parse("file://" + filePath),duration);
        Message message = Message.obtain(targetId, Conversation.ConversationType.GROUP, videoMessage);
        RongIM.getInstance().sendMediaMessage(message, null, null, new IRongCallback.ISendMediaMessageCallback() {
          @Override
          public void onProgress(Message message, int i) {

          }

          @Override
          public void onCanceled(Message message) {

          }

          @Override
          public void onAttached(Message message) {

          }

          @Override
          public void onSuccess(Message message) {
            Log.e("onSuccess", "onSuccess");
          }

          @Override
          public void onError(Message message, RongIMClient.ErrorCode errorCode) {

          }
        });
        if (cursor != null) {
          cursor.close();
          cursor = null;
        }
      }
      // getActivity().setResult(Activity.RESULT_OK, getActivity().getIntent().putExtra("path", filePath).putExtra("dur", duration));
    }
  }
}
