package cordova.plugin.ismartnet.rongcloud.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;

import cordova.plugin.ismartnet.rongcloud.utils.ResourcesUtils;

/**
 * Created by lvping on 2017/10/11.
 */

public class DialogWithYesOrNoUtils {
  private static DialogWithYesOrNoUtils instance = null;

  public static DialogWithYesOrNoUtils getInstance() {
    if (instance == null) {
      instance = new DialogWithYesOrNoUtils();
    }
    return instance;
  }

  private DialogWithYesOrNoUtils() {
  }

  public void showDialog(Context context, String titleInfo, final DialogWithYesOrNoUtils.DialogCallBack callBack) {
    AlertDialog.Builder alterDialog = new AlertDialog.Builder(context);
    alterDialog.setMessage(titleInfo);
    alterDialog.setCancelable(true);

    alterDialog.setPositiveButton(ResourcesUtils.getStringId(context,"confirm"), new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        callBack.executeEvent();
      }
    });
    alterDialog.setNegativeButton(ResourcesUtils.getStringId(context,"cancel"), new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.cancel();
      }
    });
    alterDialog.show();
  }
public interface DialogCallBack {
  void executeEvent();

  void executeEditEvent(String editText);
}


  public void showEditDialog(Context context, String hintText, String OKText, final DialogWithYesOrNoUtils.DialogCallBack callBack) {
    final EditText et_search;
    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    LinearLayout layout = (LinearLayout) inflater.inflate(ResourcesUtils.getLayoutId(context,"dialog_view"), null);
    dialog.setView(layout);
    et_search = (EditText) layout.findViewById(ResourcesUtils.getId(context,"searchC"));
    et_search.setHint(hintText);
    dialog.setPositiveButton(OKText, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int which) {
        String s = et_search.getText().toString().trim();
        callBack.executeEditEvent(s);
      }
    });

    dialog.setNegativeButton(ResourcesUtils.getStringId(context,"cancel"), new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int which) {

      }

    });
    dialog.show();
  }
}
