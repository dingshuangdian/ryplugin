package cordova.plugin.ismartnet.rongcloud.msg;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cordova.plugin.ismartnet.rongcloud.activity.VideoActivity;
import cordova.plugin.ismartnet.rongcloud.utils.ResourcesUtils;
import cordova.plugin.ismartnet.rongcloud.utils.StringUtil;
import cordova.plugin.ismartnet.rongcloud.utils.TextFormater;
import cordova.plugin.ismartnet.rongcloud.utils.ToastUtils;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.AsyncImageView;
import io.rong.imkit.widget.RCMessageFrameLayout;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;

/**
 * Created by lvping on 2017/11/6.
 */
@ProviderTag(
  messageContent = VideoMessage.class,
  showReadState = false
)
public class VideoMessageProvider extends IContainerItemProvider.MessageProvider<VideoMessage> {
  private Context mContext;

  @Override
  public void bindView(View view, int i, VideoMessage videoMessage, UIMessage uiMessage) {
    VideoMessageProvider.ViewHolder viewHolder = (ViewHolder) view.getTag();

    if (uiMessage.getMessageDirection() == Message.MessageDirection.SEND) {
      view.setBackgroundResource(ResourcesUtils.getDrawableId(mContext,"rc_ic_bubble_no_right"));
    } else {
      view.setBackgroundResource(ResourcesUtils.getDrawableId(mContext,"rc_ic_bubble_no_left"));
    }
    viewHolder.imageView.setImageURI(videoMessage.getThumbUri());
    String size = TextFormater.getDataSize(videoMessage.getSize());
    viewHolder.sizeView.setText(size);
    int progress = uiMessage.getProgress();
    Message.SentStatus status = uiMessage.getSentStatus();
    if (status.equals(Message.SentStatus.SENDING) && progress < 100) {
      viewHolder.message.setText(progress + "%");
      viewHolder.rl.setVisibility(View.VISIBLE);
    } else {
      viewHolder.rl.setVisibility(View.GONE);
    }
    //viewHolder.timeLengthView.setText("30");

  }

  @Override
  public Spannable getContentSummary(VideoMessage videoMessage) {
    return videoMessage != null && !StringUtil.isEmpty(TextFormater.getDataSize(videoMessage.getSize()).trim()) ? new SpannableString(TextFormater.getDataSize(videoMessage.getSize()).trim()) : null;
  }

  @Override
  public void onItemClick(View view, int i, VideoMessage videoMessage, UIMessage uiMessage) {
    Message.SentStatus status = uiMessage.getSentStatus();
    if (status.equals(Message.SentStatus.SENDING) && uiMessage.getProgress() < 100) {
      ToastUtils.show(mContext, "正在上传，请稍等...");
      return;
    }
  Log.e("mMediaUrl", videoMessage.getMediaUrl() + "");
    String mMediaUrl = videoMessage.getMediaUrl() + "";
    Intent intent = new Intent(mContext, VideoActivity.class);
    intent.putExtra("mMediaUrl", mMediaUrl);
    mContext.startActivity(intent);


  }

  @Override
  public View newView(Context context, ViewGroup viewGroup) {
    mContext = context;

    View convertView = LayoutInflater.from(context).inflate(ResourcesUtils.getLayoutId(context,"ease_row_sent_video"), null);
    VideoMessageProvider.ViewHolder holder = new VideoMessageProvider.ViewHolder();
    holder.imageView = (AsyncImageView) convertView.findViewById(ResourcesUtils.getId(context,"chatting_content_iv"));
    holder.sizeView = (TextView) convertView.findViewById(ResourcesUtils.getId(context,"chatting_size_iv"));
    holder.timeLengthView = (TextView) convertView.findViewById(ResourcesUtils.getId(context,"chatting_length"));
    holder.message = (TextView) convertView.findViewById(ResourcesUtils.getId(context,"rc_msg"));
    holder.rcf = (RCMessageFrameLayout) convertView.findViewById(ResourcesUtils.getId(context,"rcf"));
    holder.rl = (RelativeLayout) convertView.findViewById(ResourcesUtils.getId(context,"rl"));
    convertView.setTag(holder);
    return convertView;
  }

  class ViewHolder {
    private AsyncImageView imageView;
    private TextView sizeView;
    private TextView timeLengthView;
    private TextView message;
    private RelativeLayout rl;
    RCMessageFrameLayout rcf;
  }
}
