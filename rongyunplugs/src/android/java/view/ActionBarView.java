package cordova.plugin.ismartnet.rongcloud.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guoji.tpco.R;


/**
 * Created by lvping on 2017/9/11.
 */

public class ActionBarView extends LinearLayout {
  private Context context;
  private FrameLayout layout;
  private TextView tv_content;
  private ImageView iv_back;
  private boolean isBackFinish;

  public ActionBarView(Context var1) {
    this(var1, (AttributeSet)null);
  }

  public ActionBarView(Context var1, AttributeSet var2) {
    this(var1, var2, 0);
  }

  @SuppressLint({"NewApi"})
  public ActionBarView(Context var1, AttributeSet var2, int var3) {
    super(var1, var2, var3);
    this.context = var1;
    this.initView(var2, var3);
  }

  private void initView(AttributeSet var1, int var2) {
    View var3 = LayoutInflater.from(this.context).inflate(R.layout._layout_actionbar, this);
    this.layout = (FrameLayout)var3.findViewById(R.id.layout);
    this.tv_content = (TextView)var3.findViewById(R.id.tv_content);
    this.iv_back = (ImageView)var3.findViewById(R.id.iv_back);
    TypedArray var4 = this.context.getTheme().obtainStyledAttributes(var1, R.styleable.ActionBarView, var2, 0);
    int var5 = var4.getIndexCount();

    for(int var6 = 0; var6 < var5; ++var6) {
      int var7 = var4.getIndex(var6);
      if(var7 == R.styleable.ActionBarView_content) {
        this.tv_content.setText(var4.getString(var7));
      } else if(var7 == R.styleable.ActionBarView_bankground) {
        int var8 = var4.getColor(var7, 0);
        this.layout.setBackgroundColor(var8);
      } else if(var7 == R.styleable.ActionBarView_isBackFinish) {
        this.isBackFinish = var4.getBoolean(var7, true);
      } else if(var7 == R.styleable.ActionBarView_leftIcon) {
        Drawable var9 = var4.getDrawable(var7);
        this.iv_back.setBackgroundDrawable(var9);
      }
    }

    var4.recycle();
  }

  public void setBarColor(String var1) {
    if(var1 != null && var1.length() > 0) {
      if(!var1.startsWith("#")) {
        var1 = "#" + var1;
      }

      this.layout.setBackgroundColor(Color.parseColor(var1));
    }

  }

  public ImageView getIvBack() {
    return this.iv_back;
  }

  public void setTitle(String var1) {
    this.tv_content.setText(var1);
  }
}
