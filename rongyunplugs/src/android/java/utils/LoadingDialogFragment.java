package cordova.plugin.ismartnet.rongcloud.utils;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.guoji.tpco.R;


/**
 * Created by lvping on 2017/9/11.
 */

public class LoadingDialogFragment extends DialogFragment {
  TextView tvMessage;
  ImageView ivSuccess;
  ImageView ivFailure;
  ImageView ivProgressSpinner;
  AnimationDrawable adProgressSpinner;
  private static LoadingDialogFragment.LoadingDialogListener mListener;

  public LoadingDialogFragment() {
  }

  public static LoadingDialogFragment getInstance() {
    mListener = null;
    return new LoadingDialogFragment();
  }

  public static LoadingDialogFragment getInstance(LoadingDialogFragment.LoadingDialogListener var0) {
    mListener = var0;
    return new LoadingDialogFragment();
  }

  public Dialog onCreateDialog(Bundle var1) {
    LayoutInflater var2 = this.getActivity().getLayoutInflater();
    View var3 = var2.inflate(R.layout.w_dialog_progress, (ViewGroup)null);
    this.tvMessage = (TextView)var3.findViewById(R.id.textview_message);
    this.ivSuccess = (ImageView)var3.findViewById(R.id.imageview_success);
    this.ivFailure = (ImageView)var3.findViewById(R.id.imageview_failure);
    this.ivProgressSpinner = (ImageView)var3.findViewById(R.id.imageview_progress_spinner);
    Dialog var4 = new Dialog(this.getActivity(), R.style.DialogTheme);
    var4.setCanceledOnTouchOutside(false);
    ViewGroup.LayoutParams var5 = new ViewGroup.LayoutParams(ScreenUtil.dp2px(this.getActivity(), 100), ScreenUtil.dp2px(this.getActivity(), 100));
    var4.addContentView(var3, var5);
    return var4;
  }

  public void onActivityCreated(Bundle var1) {
    super.onActivityCreated(var1);
    this.initData(var1);
  }

  protected void initData(Bundle var1) {
    this.ivProgressSpinner.setImageResource(R.drawable.round_spinner_fade);
    this.adProgressSpinner = (AnimationDrawable)this.ivProgressSpinner.getDrawable();
    this.adProgressSpinner.start();
  }

  public void setMessage(String var1) {
    if(this.tvMessage != null) {
      this.tvMessage.setText(var1);
    }

  }

  public void setMessage(int var1) {
    if(this.tvMessage != null) {
      this.tvMessage.setText(this.getResources().getString(var1));
    }

  }

  public void dismissAllowingStateLoss() {
    super.dismissAllowingStateLoss();
    this.reset();
  }

  public DialogFragment showAllowingStateLoss(FragmentManager var1) {
    FragmentTransaction var2 = var1.beginTransaction();
    var2.add(this, this.getClass().getName());
    var2.commitAllowingStateLoss();
    return this;
  }

  protected void reset() {
    if(this.adProgressSpinner != null) {
      this.adProgressSpinner.stop();
      this.adProgressSpinner = null;
    }

    if(this.ivProgressSpinner != null) {
      this.ivProgressSpinner.setVisibility(View.VISIBLE);
    }

    if(this.ivFailure != null) {
      this.ivFailure.setVisibility(View.GONE);
    }

    if(this.ivSuccess != null) {
      this.ivSuccess.setVisibility(View.GONE);
    }

    if(this.tvMessage != null) {
      this.tvMessage.setText("正在加载 ...");
    }

  }

  public void dismissWithSuccess(String var1) {
    this.showSuccessImage();
    if(var1 != null) {
      if(this.tvMessage != null) {
        this.tvMessage.setText(var1);
      }
    } else if(this.tvMessage != null) {
      this.tvMessage.setText("");
    }

    this.dismissHUD();
  }

  protected void showSuccessImage() {
    if(this.ivProgressSpinner != null) {
      this.ivProgressSpinner.setVisibility(View.GONE);
    }

    if(this.ivSuccess != null) {
      this.ivSuccess.setVisibility(View.VISIBLE);
    }

  }

  protected void dismissHUD() {
  AsyncTask var1 = new AsyncTask() {

      @Override
      protected Long doInBackground(Object[] params) {
        SystemClock.sleep(500L);
        return null;
      }
      @Override
      protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if (LoadingDialogFragment.this.isAdded()) {
          LoadingDialogFragment.this.reset();
        } else if (LoadingDialogFragment.this.isAdded()) {
          LoadingDialogFragment.this.reset();
        }
      }
    };
    var1.execute(new Object[0]);
  }

  public void onCancel(DialogInterface var1) {
    super.onCancel(var1);
    if(mListener != null) {
      mListener.onCancel();
    }

  }

  public interface LoadingDialogListener {
    void onCancel();
  }
}
