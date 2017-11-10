package cordova.plugin.ismartnet.rongcloud.activity;
import android.annotation.TargetApi;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.view.View;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
import java.util.ArrayList;
import java.util.List;
import cordova.plugin.ismartnet.rongcloud.bean.SwitchVideoModel;
import cordova.plugin.ismartnet.rongcloud.utils.ResourcesUtils;
import cordova.plugin.ismartnet.rongcloud.video.util.OnTransitionListener;
import cordova.plugin.ismartnet.rongcloud.video.util.SampleVideo;


/**
 * Created by lvping on 2017/11/8.
 */

public class VideoActivity extends AppCompatActivity {

  public final static String IMG_TRANSITION = "IMG_TRANSITION";
  public final static String TRANSITION = "TRANSITION";
  SampleVideo videoPlayer;

  OrientationUtils orientationUtils;

  private boolean isTransition;

  private Transition transition;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(ResourcesUtils.getLayoutId(this,"activity_play"));
    isTransition = getIntent().getBooleanExtra(TRANSITION, false);
    init();
  }

  private void init() {
    videoPlayer= (SampleVideo) findViewById(ResourcesUtils.getId(this,"video_player"));
    String url = getIntent().getStringExtra("mMediaUrl");
    SwitchVideoModel switchVideoModel = new SwitchVideoModel(url);
    List<SwitchVideoModel> list = new ArrayList<>();
    list.add(switchVideoModel);
    videoPlayer.setUp(list, true, "");

   /* //增加封面
    ImageView imageView = new ImageView(this);
    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    imageView.setImageResource(R.mipmap.xxx1);
    videoPlayer.setThumbImageView(imageView);*/

    //增加title
    //videoPlayer.getTitleTextView().setVisibility(View.VISIBLE);
    //videoPlayer.setShowPauseCover(false);

    //videoPlayer.setSpeed(2f);

    //设置返回键
    videoPlayer.getBackButton().setVisibility(View.VISIBLE);

    //设置旋转
    orientationUtils = new OrientationUtils(this, videoPlayer);

    //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
    videoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        orientationUtils.resolveByClick();
      }
    });

    //videoPlayer.setBottomProgressBarDrawable(getResources().getDrawable(R.drawable.video_new_progress));
    //videoPlayer.setDialogVolumeProgressBar(getResources().getDrawable(R.drawable.video_new_volume_progress_bg));
    //videoPlayer.setDialogProgressBar(getResources().getDrawable(R.drawable.video_new_progress));
    //videoPlayer.setBottomShowProgressBarDrawable(getResources().getDrawable(R.drawable.video_new_seekbar_progress),
    //getResources().getDrawable(R.drawable.video_new_seekbar_thumb));
    //videoPlayer.setDialogProgressColor(getResources().getColor(R.color.colorAccent), -11);

    //是否可以滑动调整
    videoPlayer.setIsTouchWiget(true);

    //设置返回按键功能
    videoPlayer.getBackButton().setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });

    //过渡动画
    initTransition();
  }


  @Override
  protected void onPause() {
    super.onPause();
    videoPlayer.onVideoPause();
  }

  @Override
  protected void onResume() {
    super.onResume();
    videoPlayer.onVideoResume();
  }

  @TargetApi(Build.VERSION_CODES.KITKAT)
  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (orientationUtils != null)
      orientationUtils.releaseListener();
  }

  @Override
  public void onBackPressed() {
    //先返回正常状态
    if (orientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
      videoPlayer.getFullscreenButton().performClick();
      return;
    }
    //释放所有
    videoPlayer.setStandardVideoAllCallBack(null);
    GSYVideoPlayer.releaseAllVideos();
    if (isTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      super.onBackPressed();
    } else {
      new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
          finish();
          overridePendingTransition(ResourcesUtils.getAnimId(VideoActivity.this,"abc_fade_in"),ResourcesUtils.getAnimId(VideoActivity.this,"abc_fade_out"));
        }
      }, 500);
    }
  }


  private void initTransition() {
    if (isTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      postponeEnterTransition();
      ViewCompat.setTransitionName(videoPlayer, IMG_TRANSITION);
      addTransitionListener();
      startPostponedEnterTransition();
    } else {
      videoPlayer.startPlayLogic();
    }
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  private boolean addTransitionListener() {
    transition = getWindow().getSharedElementEnterTransition();
    if (transition != null) {
      transition.addListener(new OnTransitionListener(){
        @Override
        public void onTransitionEnd(Transition transition) {
          super.onTransitionEnd(transition);
          videoPlayer.startPlayLogic();
          transition.removeListener(this);
        }
      });
      return true;
    }
    return false;
  }

}
