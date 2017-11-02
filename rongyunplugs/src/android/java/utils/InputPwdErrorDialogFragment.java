package cordova.plugin.ismartnet.rongcloud.utils;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.guoji.tpco.R;

/**
 * Created by lvping on 2017/9/11.
 */

public class InputPwdErrorDialogFragment extends BaseDialogFragment {
  private TextView tv_title;
  private TextView tv_left;
  private TextView tv_right;
  private InputPwdErrorDialogFragment.InputPwdErrorListener mListener;

  public InputPwdErrorDialogFragment() {
  }

  public static InputPwdErrorDialogFragment newInstance(String var0, String var1, String var2) {
    InputPwdErrorDialogFragment var3 = new InputPwdErrorDialogFragment();
    Bundle var4 = new Bundle();
    var4.putString("title", var0);
    var4.putString("left", var1);
    var4.putString("right", var2);
    var3.setArguments(var4);
    return var3;
  }

  public int getLayoutId() {
    return R.layout._dialog_pwd_error;
  }

  public void initView() {
    this.tv_title = (TextView)this.rootView.findViewById(R.id.tv_title);
    this.tv_left = (TextView)this.rootView.findViewById(R.id.tv_left);
    this.tv_right = (TextView)this.rootView.findViewById(R.id.tv_right);
  }

  public void initListener() {
    this.tv_left.setOnClickListener(this);
    this.tv_right.setOnClickListener(this);
  }

  protected void initData(Bundle var1) {
    if(var1 != null) {
      String var2 = var1.getString("title");
      String var3 = var1.getString("left");
      String var4 = var1.getString("right");
      this.tv_title.setText(var2);
      this.tv_left.setText(var3);
      this.tv_right.setText(var4);
    }

  }

  public void onClick(View var1) {
    int var2 = var1.getId();
    this.dismiss();
    if(var2 == R.id.tv_left) {
      this.mListener.onLeft();
    } else if(var2 == R.id.tv_right) {
      this.mListener.onRight();
    }

  }

  public void setListener(InputPwdErrorDialogFragment.InputPwdErrorListener var1) {
    this.mListener = var1;
  }

  public interface InputPwdErrorListener {
    void onLeft();

    void onRight();
  }
}
