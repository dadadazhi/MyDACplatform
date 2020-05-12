// Generated code from Butter Knife. Do not modify!
package com.stx.xhb.DCAPlatform.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.stx.xhb.DCAPlatform.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LoginAcountAdapter$Viewholder_ViewBinding<T extends LoginAcountAdapter.Viewholder> implements Unbinder {
  protected T target;

  @UiThread
  public LoginAcountAdapter$Viewholder_ViewBinding(T target, View source) {
    this.target = target;

    target.tv_acount = Utils.findRequiredViewAsType(source, R.id.tv_acount, "field 'tv_acount'", TextView.class);
    target.ivbtn_delete = Utils.findRequiredViewAsType(source, R.id.ivbtn_delete, "field 'ivbtn_delete'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.tv_acount = null;
    target.ivbtn_delete = null;

    this.target = null;
  }
}
