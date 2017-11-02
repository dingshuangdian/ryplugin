package cordova.plugin.ismartnet.rongcloud.utils;

import android.text.TextUtils;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * Created by lvping on 2017/9/8.
 */

public class StringUtil {
  public static final String UTF_8 = "utf-8";

  public StringUtil() {
  }

  public static boolean isEmpty(String var0) {
    return var0 == null || var0.trim().length() <= 0;
  }

  public static boolean isNotEmpty(String var0) {
    return var0 != null && var0.trim().length() > 0;
  }

  public static boolean isNotEmptyAndNull(String var0) {
    return var0 != null && var0.trim().length() > 0 && !"null".equals(var0);
  }

  public static boolean isEmptyAndNull(String var0) {
    return isEmpty(var0) || "null".equals(var0);
  }

  public static boolean checkBankCard(String var0) {
    if(TextUtils.isEmpty(var0)) {
      return false;
    } else {
      char var1 = getBankCardCheckCode(var0.substring(0, var0.length() - 1));
      return var1 == 78?false:var0.charAt(var0.length() - 1) == var1;
    }
  }
  public static <T> T checkNotNull(T t, String message) {
    if (t == null) {
      throw new NullPointerException(message);
    }
    return t;
  }

  public static char getBankCardCheckCode(String var0) {
    if(var0 != null && var0.trim().length() != 0 && var0.matches("\\d+")) {
      char[] var1 = var0.trim().toCharArray();
      int var2 = 0;
      int var3 = var1.length - 1;

      for(int var4 = 0; var3 >= 0; ++var4) {
        int var5 = var1[var3] - 48;
        if(var4 % 2 == 0) {
          var5 *= 2;
          var5 = var5 / 10 + var5 % 10;
        }

        var2 += var5;
        --var3;
      }

      return var2 % 10 == 0?'0':(char)(10 - var2 % 10 + 48);
    } else {
      return 'N';
    }
  }

  public static boolean isIDCard(String var0) {
    return isEmpty(var0)?false:var0.length() == 15 || var0.length() == 18;
  }

  public static boolean isNumber(String var0) {
    return !isEmpty(var0)?var0.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$"):false;
  }

  public static String formatMoney(double var0) {
    BigDecimal var2 = new BigDecimal(var0);
    return var2.setScale(2, 4).toString();
  }

  public static String formatMoney(String var0) {
    BigDecimal var1 = new BigDecimal(var0.trim());
    return var1.setScale(2, 4).toString();
  }

  public static float formatMoneyDouble(double var0) {
    BigDecimal var2 = new BigDecimal(var0);
    return var2.setScale(2, 4).floatValue();
  }

  public static float formatMoneyDouble(String var0) {
    BigDecimal var1 = new BigDecimal(var0);
    return var1.setScale(2, 4).floatValue();
  }

  public static Double string2double(String var0) {
    Double var1 = null;
    if(isEmpty(var0)) {
      return var1;
    } else {
      try {
        var1 = Double.valueOf(var0);
      } catch (NumberFormatException var3) {
        var3.printStackTrace();
      }

      return var1;
    }
  }

  public static boolean isMobile(String var0) {
    if(isEmpty(var0)) {
      return false;
    } else {
      String var1 = "^((13[0-9])|(17[0-9])|(15[^4,\\D])|(18[0-9])|(14[0-9]))\\d{8}$";
      return Pattern.matches(var1, var0);
    }
  }

  public static String replace(String var0) {
    byte var1;
    int var2;
    StringBuilder var3;
    int var4;
    StringBuilder var5;
    if(var0.length() >= 8) {
      var1 = 4;
      var2 = var0.length() - 4;
      var3 = new StringBuilder(var2 - var1);

      for(var4 = 0; var4 < var2 - var1; ++var4) {
        var3 = var3.append("*");
      }

      var5 = new StringBuilder(var0);
      return var5.replace(var1, var2, var3.toString()).toString();
    } else if(var0.length() == 0) {
      return var0;
    } else {
      var1 = 2;
      var2 = var0.length() - 2;
      var3 = new StringBuilder(var2 - var1);

      for(var4 = 0; var4 < var2 - var1; ++var4) {
        var3 = var3.append("*");
      }

      var5 = new StringBuilder(var0);
      return var5.replace(var1, var2, var3.toString()).toString();
    }
  }

  public static String nameReplace(String var0) {
    if(isEmpty(var0)) {
      var0 = "";
    }

    int var1 = var0.length();
    StringBuilder var2 = new StringBuilder();
    if(var1 <= 1) {
      var2.append("*");
    } else if(var1 == 2) {
      var2.append("*").append(var0.charAt(var1 - 1));
    } else if(var1 == 3) {
      var2.append("**").append(var0.charAt(var1 - 1));
    } else {
      var2.append("***").append(var0.charAt(var1 - 1));
    }

    return var2.toString();
  }

  public static String idCardReplace(String var0) {
    if(isEmpty(var0)) {
      return "";
    } else {
      StringBuffer var1 = new StringBuffer();
      if(var0.length() <= 15) {
        var1.append(var0.charAt(0)).append("*************").append(var0.charAt(var0.length() - 1));
      } else {
        var1.append(var0.charAt(0)).append("****************").append(var0.charAt(var0.length() - 1));
      }

      return var1.toString();
    }
  }
  /**
   * 普通类反射获取泛型方式，获取需要实际解析的类型
   *
   * @param <T>
   * @return
   */
  public static <T> Type findNeedClass(Class<T> cls) {
    //以下代码是通过泛型解析实际参数,泛型必须传
    Type genType = cls.getGenericSuperclass();
    Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
    Type type = params[0];
    Type finalNeedType;
    if (params.length > 1) {//这个类似是：CacheResult<SkinTestResult> 2层
      if (!(type instanceof ParameterizedType)) throw new IllegalStateException("没有填写泛型参数");
      finalNeedType = ((ParameterizedType) type).getActualTypeArguments()[0];
      //Type rawType = ((ParameterizedType) type).getRawType();
    } else {//这个类似是:SkinTestResult  1层
      finalNeedType = type;
    }
    return finalNeedType;
  }

  /**
   * 普通类反射获取泛型方式，获取最顶层的类型
   */
  public static <T> Type findRawType(Class<T> cls) {
    Type genType = cls.getGenericSuperclass();
    return getGenericType((ParameterizedType) genType, 0);
  }

  public static Type getGenericType(ParameterizedType parameterizedType, int i) {
    Type genericType = parameterizedType.getActualTypeArguments()[i];
    if (genericType instanceof ParameterizedType) { // 处理多级泛型
      return ((ParameterizedType) genericType).getRawType();
    } else if (genericType instanceof GenericArrayType) { // 处理数组泛型
      return ((GenericArrayType) genericType).getGenericComponentType();
    } else if (genericType instanceof TypeVariable) { // 处理泛型擦拭对象
      return getClass(((TypeVariable) genericType).getBounds()[0], 0);
    } else {
      return genericType;
    }
  }

  public static Class getClass(Type type, int i) {
    if (type instanceof ParameterizedType) { // 处理泛型类型
      return getGenericClass((ParameterizedType) type, i);
    } else if (type instanceof TypeVariable) {
      return getClass(((TypeVariable) type).getBounds()[0], 0); // 处理泛型擦拭对象
    } else {// class本身也是type，强制转型
      return (Class) type;
    }
  }

  public static Class getGenericClass(ParameterizedType parameterizedType, int i) {
    Type genericClass = parameterizedType.getActualTypeArguments()[i];
    if (genericClass instanceof ParameterizedType) { // 处理多级泛型
      return (Class) ((ParameterizedType) genericClass).getRawType();
    } else if (genericClass instanceof GenericArrayType) { // 处理数组泛型
      return (Class) ((GenericArrayType) genericClass).getGenericComponentType();
    } else if (genericClass instanceof TypeVariable) { // 处理泛型擦拭对象
      return getClass(((TypeVariable) genericClass).getBounds()[0], 0);
    } else {
      return (Class) genericClass;
    }
  }
}
