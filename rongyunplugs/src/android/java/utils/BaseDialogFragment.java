package cordova.plugin.ismartnet.rongcloud.utils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guoji.tpco.R;

import cordova.plugin.ismartnet.rongcloud.inter.CPa;

/**
 * Created by lvping on 2017/9/11.
 */

public class BaseDialogFragment extends DialogFragment implements CPa {
  protected View rootView;
  protected Activity fromActivity;

  public BaseDialogFragment() {
  }

  public Dialog onCreateDialog(Bundle var1) {
    LayoutInflater var2 = this.getActivity().getLayoutInflater();
    this.rootView = var2.inflate(this.getLayoutId(), (ViewGroup)null);
    this.fromActivity = this.getActivity();
    this.initView();
    this.initListener();
    this.initData(this.getArguments());
    Dialog var3 = new Dialog(this.getActivity(), R.style.DialogTheme);
    ViewGroup.LayoutParams var4 = new ViewGroup.LayoutParams(ScreenUtil.dp2px(this.getActivity(), 300), -2);
    var3.addContentView(this.rootView, var4);
    return var3;
  }

  public void onClick(View var1) {
    this.onClick(var1.getId());
  }


  @Override
  public int getLayoutId() {
    return 0;
  }

  public void onClick(int var1) {
  }

  public void initView() {
  }

  public void initListener() {
  }

  protected void initData(Bundle var1) {
  }
}
