package cordova.plugin.ismartnet.rongcloud.view;

/**
 * Created by lvping on 2017/9/14.
 */
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.guoji.tpco.R;


public class CircleImageView extends ImageView {
  private static final ScaleType SCALE_TYPE;
  private static final Bitmap.Config BITMAP_CONFIG;
  private static final int COLORDRAWABLE_DIMENSION = 2;
  private static final int DEFAULT_BORDER_WIDTH = 0;
  private static final int DEFAULT_BORDER_COLOR = -16777216;
  private static final int DEFAULT_FILL_COLOR = 0;
  private static final boolean DEFAULT_BORDER_OVERLAY = false;
  private final RectF mDrawableRect;
  private final RectF mBorderRect;
  private final Matrix mShaderMatrix;
  private final Paint mBitmapPaint;
  private final Paint mBorderPaint;
  private final Paint mFillPaint;
  private int mBorderColor;
  private int mBorderWidth;
  private int mFillColor;
  private Bitmap mBitmap;
  private BitmapShader mBitmapShader;
  private int mBitmapWidth;
  private int mBitmapHeight;
  private float mDrawableRadius;
  private float mBorderRadius;
  private ColorFilter mColorFilter;
  private boolean mReady;
  private boolean mSetupPending;
  private boolean mBorderOverlay;
  private boolean mDisableCircularTransformation;

  public CircleImageView(Context var1) {
    super(var1);
    this.mDrawableRect = new RectF();
    this.mBorderRect = new RectF();
    this.mShaderMatrix = new Matrix();
    this.mBitmapPaint = new Paint();
    this.mBorderPaint = new Paint();
    this.mFillPaint = new Paint();
    this.mBorderColor = -16777216;
    this.mBorderWidth = 0;
    this.mFillColor = 0;
    this.init();
  }

  public CircleImageView(Context var1, AttributeSet var2) {
    this(var1, var2, 0);
  }

  public CircleImageView(Context var1, AttributeSet var2, int var3) {
    super(var1, var2, var3);
    this.mDrawableRect = new RectF();
    this.mBorderRect = new RectF();
    this.mShaderMatrix = new Matrix();
    this.mBitmapPaint = new Paint();
    this.mBorderPaint = new Paint();
    this.mFillPaint = new Paint();
    this.mBorderColor = R.color.white;
    this.mBorderWidth = 0;
    this.mFillColor = 0;
    TypedArray var4 = var1.obtainStyledAttributes(var2, R.styleable.Ry_CircleImageView, var3, 0);
    this.mBorderWidth = var4.getDimensionPixelSize(R.styleable.Ry_CircleImageView_ry_civ_border_width, 0);
    this.mBorderColor = var4.getColor(R.styleable.Ry_CircleImageView_ry_civ_border_color, getResources().getColor(R.color.white));
    this.mBorderOverlay = var4.getBoolean(R.styleable.Ry_CircleImageView_ry_civ_border_overlay, false);
    this.mFillColor = var4.getColor(R.styleable.Ry_CircleImageView_ry_civ_fill_color, 0);
    var4.recycle();
    this.init();
  }

  private void init() {
    super.setScaleType(SCALE_TYPE);
    this.mReady = true;
    if(this.mSetupPending) {
      this.setup();
      this.mSetupPending = false;
    }

  }

  public ScaleType getScaleType() {
    return SCALE_TYPE;
  }

  public void setScaleType(ScaleType var1) {
    if(var1 != SCALE_TYPE) {
      throw new IllegalArgumentException(String.format("ScaleType %s not supported.", new Object[]{var1}));
    }
  }

  public void setAdjustViewBounds(boolean var1) {
    if(var1) {
      throw new IllegalArgumentException("adjustViewBounds not supported.");
    }
  }

  protected void onDraw(Canvas var1) {
    if(this.mDisableCircularTransformation) {
      super.onDraw(var1);
    } else if(this.mBitmap != null) {
      if(this.mFillColor != 0) {
        var1.drawCircle(this.mDrawableRect.centerX(), this.mDrawableRect.centerY(), this.mDrawableRadius, this.mFillPaint);
      }

      var1.drawCircle(this.mDrawableRect.centerX(), this.mDrawableRect.centerY(), this.mDrawableRadius, this.mBitmapPaint);
      if(this.mBorderWidth > 0) {
        var1.drawCircle(this.mBorderRect.centerX(), this.mBorderRect.centerY(), this.mBorderRadius, this.mBorderPaint);
      }

    }
  }

  protected void onSizeChanged(int var1, int var2, int var3, int var4) {
    super.onSizeChanged(var1, var2, var3, var4);
    this.setup();
  }

  public void setPadding(int var1, int var2, int var3, int var4) {
    super.setPadding(var1, var2, var3, var4);
    this.setup();
  }

  public void setPaddingRelative(int var1, int var2, int var3, int var4) {
    super.setPaddingRelative(var1, var2, var3, var4);
    this.setup();
  }

  public int getBorderColor() {
    return this.mBorderColor;
  }

  public void setBorderColor(int var1) {
    if(var1 != this.mBorderColor) {
      this.mBorderColor = var1;
      this.mBorderPaint.setColor(this.mBorderColor);
      this.invalidate();
    }
  }

  @Deprecated
  public void setBorderColorResource(@ColorRes int var1) {
    this.setBorderColor(this.getContext().getResources().getColor(var1));
  }

  @Deprecated
  public int getFillColor() {
    return this.mFillColor;
  }

  @Deprecated
  public void setFillColor(int var1) {
    if(var1 != this.mFillColor) {
      this.mFillColor = var1;
      this.mFillPaint.setColor(var1);
      this.invalidate();
    }
  }

  @Deprecated
  public void setFillColorResource(@ColorRes int var1) {
    this.setFillColor(this.getContext().getResources().getColor(var1));
  }

  public int getBorderWidth() {
    return this.mBorderWidth;
  }

  public void setBorderWidth(int var1) {
    if(var1 != this.mBorderWidth) {
      this.mBorderWidth = var1;
      this.setup();
    }
  }

  public boolean isBorderOverlay() {
    return this.mBorderOverlay;
  }

  public void setBorderOverlay(boolean var1) {
    if(var1 != this.mBorderOverlay) {
      this.mBorderOverlay = var1;
      this.setup();
    }
  }

  public boolean isDisableCircularTransformation() {
    return this.mDisableCircularTransformation;
  }

  public void setDisableCircularTransformation(boolean var1) {
    if(this.mDisableCircularTransformation != var1) {
      this.mDisableCircularTransformation = var1;
      this.initializeBitmap();
    }
  }

  public void setImageBitmap(Bitmap var1) {
    super.setImageBitmap(var1);
    this.initializeBitmap();
  }

  public void setImageDrawable(Drawable var1) {
    super.setImageDrawable(var1);
    this.initializeBitmap();
  }

  public void setImageResource(@DrawableRes int var1) {
    super.setImageResource(var1);
    this.initializeBitmap();
  }

  public void setImageURI(Uri var1) {
    super.setImageURI(var1);
    this.initializeBitmap();
  }

  public void setColorFilter(ColorFilter var1) {
    if(var1 != this.mColorFilter) {
      this.mColorFilter = var1;
      this.applyColorFilter();
      this.invalidate();
    }
  }

  public ColorFilter getColorFilter() {
    return this.mColorFilter;
  }

  private void applyColorFilter() {
    if(this.mBitmapPaint != null) {
      this.mBitmapPaint.setColorFilter(this.mColorFilter);
    }

  }

  private Bitmap getBitmapFromDrawable(Drawable var1) {
    if(var1 == null) {
      return null;
    } else if(var1 instanceof BitmapDrawable) {
      return ((BitmapDrawable)var1).getBitmap();
    } else {
      try {
        Bitmap var2;
        if(var1 instanceof ColorDrawable) {
          var2 = Bitmap.createBitmap(2, 2, BITMAP_CONFIG);
        } else {
          var2 = Bitmap.createBitmap(var1.getIntrinsicWidth(), var1.getIntrinsicHeight(), BITMAP_CONFIG);
        }

        Canvas var3 = new Canvas(var2);
        var1.setBounds(0, 0, var3.getWidth(), var3.getHeight());
        var1.draw(var3);
        return var2;
      } catch (Exception var4) {
        var4.printStackTrace();
        return null;
      }
    }
  }

  private void initializeBitmap() {
    if(this.mDisableCircularTransformation) {
      this.mBitmap = null;
    } else {
      this.mBitmap = this.getBitmapFromDrawable(this.getDrawable());
    }

    this.setup();
  }

  private void setup() {
    if(!this.mReady) {
      this.mSetupPending = true;
    } else if(this.getWidth() != 0 || this.getHeight() != 0) {
      if(this.mBitmap == null) {
        this.invalidate();
      } else {
        this.mBitmapShader = new BitmapShader(this.mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        this.mBitmapPaint.setAntiAlias(true);
        this.mBitmapPaint.setShader(this.mBitmapShader);
        this.mBorderPaint.setStyle(Paint.Style.STROKE);
        this.mBorderPaint.setAntiAlias(true);
        this.mBorderPaint.setColor(this.mBorderColor);
        this.mBorderPaint.setStrokeWidth((float)this.mBorderWidth);
        this.mFillPaint.setStyle(Paint.Style.FILL);
        this.mFillPaint.setAntiAlias(true);
        this.mFillPaint.setColor(this.mFillColor);
        this.mBitmapHeight = this.mBitmap.getHeight();
        this.mBitmapWidth = this.mBitmap.getWidth();
        this.mBorderRect.set(this.calculateBounds());
        this.mBorderRadius = Math.min((this.mBorderRect.height() - (float)this.mBorderWidth) / 2.0F, (this.mBorderRect.width() - (float)this.mBorderWidth) / 2.0F);
        this.mDrawableRect.set(this.mBorderRect);
        if(!this.mBorderOverlay && this.mBorderWidth > 0) {
          this.mDrawableRect.inset((float)this.mBorderWidth - 1.0F, (float)this.mBorderWidth - 1.0F);
        }

        this.mDrawableRadius = Math.min(this.mDrawableRect.height() / 2.0F, this.mDrawableRect.width() / 2.0F);
        this.applyColorFilter();
        this.updateShaderMatrix();
        this.invalidate();
      }
    }
  }

  private RectF calculateBounds() {
    int var1 = this.getWidth() - this.getPaddingLeft() - this.getPaddingRight();
    int var2 = this.getHeight() - this.getPaddingTop() - this.getPaddingBottom();
    int var3 = Math.min(var1, var2);
    float var4 = (float)this.getPaddingLeft() + (float)(var1 - var3) / 2.0F;
    float var5 = (float)this.getPaddingTop() + (float)(var2 - var3) / 2.0F;
    return new RectF(var4, var5, var4 + (float)var3, var5 + (float)var3);
  }

  private void updateShaderMatrix() {
    float var2 = 0.0F;
    float var3 = 0.0F;
    this.mShaderMatrix.set((Matrix)null);
    float var1;
    if((float)this.mBitmapWidth * this.mDrawableRect.height() > this.mDrawableRect.width() * (float)this.mBitmapHeight) {
      var1 = this.mDrawableRect.height() / (float)this.mBitmapHeight;
      var2 = (this.mDrawableRect.width() - (float)this.mBitmapWidth * var1) * 0.5F;
    } else {
      var1 = this.mDrawableRect.width() / (float)this.mBitmapWidth;
      var3 = (this.mDrawableRect.height() - (float)this.mBitmapHeight * var1) * 0.5F;
    }

    this.mShaderMatrix.setScale(var1, var1);
    this.mShaderMatrix.postTranslate((float)((int)(var2 + 0.5F)) + this.mDrawableRect.left, (float)((int)(var3 + 0.5F)) + this.mDrawableRect.top);
    this.mBitmapShader.setLocalMatrix(this.mShaderMatrix);
  }

  static {
    SCALE_TYPE = ScaleType.CENTER_CROP;
    BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
  }
}
