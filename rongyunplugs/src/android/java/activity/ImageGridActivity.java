package cordova.plugin.ismartnet.rongcloud.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import cordova.plugin.ismartnet.rongcloud.fragment.ImageGridFragment;

/**
 * Created by Lvping on 2017/11/5.
 */

public class ImageGridActivity extends FragmentActivity {

  private static final String TAG = "ImageGridActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (getSupportFragmentManager().findFragmentByTag(TAG) == null) {
      final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
      ft.add(android.R.id.content, new ImageGridFragment(), TAG);
      ft.commit();
    }
  }


  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
  }
}
