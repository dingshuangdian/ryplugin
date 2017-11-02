package cordova.plugin.ismartnet.rongcloud.retrofit.api;

import java.util.Map;

import cordova.plugin.ismartnet.rongcloud.bean.Group;
import cordova.plugin.ismartnet.rongcloud.bean.GroupMsg;
import cordova.plugin.ismartnet.rongcloud.bean.Order;
import cordova.plugin.ismartnet.rongcloud.bean.Permiss;
import cordova.plugin.ismartnet.rongcloud.bean.Receive;
import cordova.plugin.ismartnet.rongcloud.bean.User;
import cordova.plugin.ismartnet.rongcloud.model.RpItemModel;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by John on 2017/6/30.
 */

public interface RetrofitService {
  @POST()
  Call<User> getUser(@Url String url, @QueryMap Map<String, String> map);

  @POST()
  Call<Group> getGroup(@Url String url, @QueryMap Map<String, String> map);

  @FormUrlEncoded
  @POST()
  Call<Order> createBonus(@Url String url, @FieldMap Map<String, String> map);

  @POST()
  Call<Receive> getReceive(@Url String url, @QueryMap Map<String, String> map);

  @POST()
  Call<Receive> getRed(@Url String url, @QueryMap Map<String, String> map);

  @POST()
  Call<RpItemModel> getRpItem(@Url String url, @QueryMap Map<String, String> map);

  @POST()
  Call<GroupMsg> getGroupMembers(@Url String url, @QueryMap Map<String, String> map);

  @POST()
  Call<Permiss> getPermiss(@Url String url, @QueryMap Map<String, String> map);

}
