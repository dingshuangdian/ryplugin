package cordova.plugin.ismartnet.rongcloud.adapter;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import java.util.List;
import cordova.plugin.ismartnet.rongcloud.bean.GroupMsg;
import cordova.plugin.ismartnet.rongcloud.utils.ResourcesUtils;
import cordova.plugin.ismartnet.rongcloud.utils.RongGenerate;
import cordova.plugin.ismartnet.rongcloud.utils.StringUtil;
/**
 * Created by lvping on 2017/10/11.
 */

public class GridAdapter extends BaseAdapter{
  private List<GroupMsg.ResultBean> list;
  Context context;


  public GridAdapter(Context context, List<GroupMsg.ResultBean> list) {
    if (list.size() >= 31) {
      this.list = list.subList(0, 30);
    } else {
      this.list = list;
    }

    this.context = context;
  }


  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    if (convertView == null) {
      convertView = LayoutInflater.from(context).inflate(ResourcesUtils.getLayoutId(context,"social_chatsetting_gridview_item"),parent, false);
    }
    SimpleDraweeView iv_avatar = (SimpleDraweeView) convertView.findViewById(ResourcesUtils.getId(context,"iv_avatar"));
    TextView tv_username = (TextView) convertView.findViewById(ResourcesUtils.getId(context,"tv_username"));
    // 普通成员
      final GroupMsg.ResultBean bean = list.get(position);
        tv_username.setText(bean.getUName());
    if (!StringUtil.isEmptyAndNull(bean.getHeadImg())) {
      iv_avatar.setImageURI(Uri.parse(bean.getHeadImg()));
    } else {
      //iv_header.setImageURI(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getResources().getResourcePackageName(R.drawable.de_default_portrait) + "/" + getResources().getResourceTypeName(R.drawable.de_default_portrait) + "/" + getResources().getResourceEntryName(R.drawable.de_default_portrait)));
      iv_avatar.setImageURI(RongGenerate.generateDefaultAvatar(bean.getUName(), bean.getUId()));
    }
   /*   iv_avatar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          UserInfo userInfo = new UserInfo(bean.getUserId(), bean.getName(), TextUtils.isEmpty(bean.getPortraitUri().toString()) ? Uri.parse(RongGenerate.generateDefaultAvatar(bean.getName(), bean.getUserId())) : bean.getPortraitUri());
          Intent intent = new Intent(context, UserDetailActivity.class);
          Friend friend = CharacterParser.getInstance().generateFriendFromUserInfo(userInfo);
          intent.putExtra("friend", friend);
          intent.putExtra("conversationType", Conversation.ConversationType.GROUP.getValue());
          //Groups not Serializable,just need group name
          intent.putExtra("groupName", mGroup.getName());
          intent.putExtra("type", CLICK_CONVERSATION_USER_PORTRAIT);
          context.startActivity(intent);
        }
      });*/
    return convertView;
  }

  @Override
  public int getCount() {
      return list.size();
  }
  @Override
  public Object getItem(int position) {
    return list.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  /**
   * 传入新的数据 刷新UI的方法
   */
  public void updateListView(List<GroupMsg.ResultBean> list) {
    this.list = list;
    notifyDataSetChanged();
  }


}
