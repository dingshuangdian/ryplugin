package cordova.plugin.ismartnet.rongcloud.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by lvping on 2017/9/18.
 */

public abstract class DetailBaseAdapter<T> extends BaseAdapter {
  protected Context mContext;
  protected List<T> mDatas;
  protected LayoutInflater mInflater;

  public DetailBaseAdapter(Context var1, List<T> var2) {
    this.mContext = var1;
    this.mInflater = LayoutInflater.from(var1);
    this.mDatas = var2;
  }

  @Override
  public int getCount() {
    return this.mDatas == null ? 0 : this.mDatas.size();
  }

  @Override
  public T getItem(int position) {
    return this.mDatas == null?null:this.mDatas.get(position);
  }

  @Override
  public long getItemId(int position) {
    return (long)position;
  }
}
