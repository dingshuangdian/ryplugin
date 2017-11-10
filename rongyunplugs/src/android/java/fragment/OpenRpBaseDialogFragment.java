package cordova.plugin.ismartnet.rongcloud.fragment;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cordova.plugin.ismartnet.rongcloud.inter.CPa;
import cordova.plugin.ismartnet.rongcloud.utils.ResourcesUtils;
import cordova.plugin.ismartnet.rongcloud.utils.ScreenUtil;

/**
 * Created by lvping on 2017/9/14.
 */

public abstract class OpenRpBaseDialogFragment extends DialogFragment implements CPa {
  protected View rootView;
  protected Activity fromActivity;

  public OpenRpBaseDialogFragment() {
  }
  public Dialog onCreateDialog(Bundle var1) {
    LayoutInflater var2 = this.getActivity().getLayoutInflater();
    this.rootView = var2.inflate(this.getLayoutId(), (ViewGroup) null);
    this.fromActivity = this.getActivity();
    this.initView();
    this.initListener();
    this.initData(this.getArguments());
    Dialog var3 = new Dialog(this.getActivity(), ResourcesUtils.getStyleId(getActivity(),"DialogTheme"));
    ViewGroup.LayoutParams var4 = new ViewGroup.LayoutParams(ScreenUtil.dp2px(this.getActivity(), 300), -2);
    var3.addContentView(this.rootView, var4);
    return var3;
  }

  public final void onClick(View var1) {
    this.onClick(var1.getId());
  }

  public void initView() {
  }

  public void initListener() {
  }

  protected void initData(Bundle var1) {
  }
}
