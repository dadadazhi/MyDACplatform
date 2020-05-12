// Generated code from Butter Knife. Do not modify!
package com.stx.xhb.DCAPlatform.activities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.stx.xhb.DCAPlatform.R;
import com.xw.repo.XEditText;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LoginActivity_ViewBinding<T extends LoginActivity> implements Unbinder {
  protected T target;

  private View view2131230787;

  private View view2131230906;

  private View view2131231092;

  @UiThread
  public LoginActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.et_acount = Utils.findRequiredViewAsType(source, R.id.et_acount, "field 'et_acount'", XEditText.class);
    target.et_pwd = Utils.findRequiredViewAsType(source, R.id.et_pwd, "field 'et_pwd'", XEditText.class);
    view = Utils.findRequiredView(source, R.id.btn_login, "field 'btn_login' and method 'onClick'");
    target.btn_login = Utils.castView(view, R.id.btn_login, "field 'btn_login'", Button.class);
    view2131230787 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.iv_btn = Utils.findRequiredViewAsType(source, R.id.iv_btn, "field 'iv_btn'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.ivbtn_drop, "method 'onClick'");
    view2131230906 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.tv_register, "method 'onClick'");
    view2131231092 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.et_acount = null;
    target.et_pwd = null;
    target.btn_login = null;
    target.iv_btn = null;

    view2131230787.setOnClickListener(null);
    view2131230787 = null;
    view2131230906.setOnClickListener(null);
    view2131230906 = null;
    view2131231092.setOnClickListener(null);
    view2131231092 = null;

    this.target = null;
  }
}
