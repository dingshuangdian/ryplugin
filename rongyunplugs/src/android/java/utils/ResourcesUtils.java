package cordova.plugin.ismartnet.rongcloud.utils;

import android.content.Context;

import java.lang.reflect.Field;

/**
 * Created by lvping on 2017/11/8.
 */

public class ResourcesUtils {
  public static int getLayoutId(Context paramContext, String paramString) {
    return paramContext.getResources().getIdentifier(paramString, "layout",
      paramContext.getPackageName());
  }

  public static int getStringId(Context paramContext, String paramString) {
    return paramContext.getResources().getIdentifier(paramString, "string",
      paramContext.getPackageName());
  }

  public static int getDrawableId(Context paramContext, String paramString) {
    return paramContext.getResources().getIdentifier(paramString,
      "drawable", paramContext.getPackageName());
  }

  public static int getStyleId(Context paramContext, String paramString) {
    return paramContext.getResources().getIdentifier(paramString,
      "style", paramContext.getPackageName());
  }

  public static int getId(Context paramContext, String paramString) {
    return paramContext.getResources().getIdentifier(paramString,"id", paramContext.getPackageName());
  }

  public static int getColorId(Context paramContext, String paramString) {
    return paramContext.getResources().getIdentifier(paramString,
      "color", paramContext.getPackageName());
  }
  public static int getArrayId(Context paramContext, String paramString) {
    return paramContext.getResources().getIdentifier(paramString,
      "array", paramContext.getPackageName());
  }
  public static int getAnimId(Context paramContext, String paramString) {
    return paramContext.getResources().getIdentifier(paramString,
      "anim", paramContext.getPackageName());
  }
  public static int getDimenId(Context paramContext, String paramString) {
    return paramContext.getResources().getIdentifier(paramString,
      "dimen", paramContext.getPackageName());
  }

  private static Object getResourceId(Context context,String name, String type) {

    String className = context.getPackageName() +".R";

    try {

      Class cls = Class.forName(className);

      for (Class childClass : cls.getClasses()) {

        String simple = childClass.getSimpleName();

        if (simple.equals(type)) {

          for (Field field : childClass.getFields()) {

            String fieldName = field.getName();

            if (fieldName.equals(name)) {

              System.out.println(fieldName);

              return field.get(null);

            }

          }

        }

      }

    } catch (Exception e) {

      e.printStackTrace();

    }

    return null;

  }

  /**

   *获取到styleable的数据

   * @paramcontext

   * @param paramString

   * @return

   */

  public static int getStyleable(Context paramContext, String paramString) {

    return ((Integer)getResourceId(paramContext, paramString,"styleable")).intValue();

  }

  /**

   * 获取styleable的ID号数组

   * @paramcontext

   * @param paramString

   * @return

   */

  public static int[] getStyleableArray(Context paramContext,String paramString) {

    return (int[])getResourceId(paramContext, paramString,"styleable");

  }

}
