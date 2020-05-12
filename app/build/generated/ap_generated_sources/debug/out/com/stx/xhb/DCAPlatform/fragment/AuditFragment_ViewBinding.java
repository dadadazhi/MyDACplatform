// Generated code from Butter Knife. Do not modify!
package com.stx.xhb.DCAPlatform.fragment;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.stx.xhb.DCAPlatform.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AuditFragment_ViewBinding<T extends AuditFragment> implements Unbinder {
  protected T target;

  private View view2131231005;

  private View view2131231006;

  private View view2131231007;

  private View view2131231004;

  private View view2131230735;

  private View view2131230736;

  private View view2131230871;

  private View view2131231183;

  @UiThread
  public AuditFragment_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.setting_iv_ch, "method 'onClick'");
    view2131231005 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.setting_iv_clearCache, "method 'onClick'");
    view2131231006 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.setting_iv_out, "method 'onClick'");
    view2131231007 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.setting_iv_about, "method 'onClick'");
    view2131231004 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.ac_registered, "method 'onClick'");
    view2131230735 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.ac_underway, "method 'onClick'");
    view2131230736 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.head_portrait, "method 'onClick'");
    view2131230871 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.username, "method 'onClick'");
    view2131231183 = view;
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
    if (this.target == null) throw new IllegalStateException("Bindings already cleared.");

    view2131231005.setOnClickListener(null);
    view2131231005 = null;
    view2131231006.setOnClickListener(null);
    view2131231006 = null;
    view2131231007.setOnClickListener(null);
    view2131231007 = null;
    view2131231004.setOnClickListener(null);
    view2131231004 = null;
    view2131230735.setOnClickListener(null);
    view2131230735 = null;
    view2131230736.setOnClickListener(null);
    view2131230736 = null;
    view2131230871.setOnClickListener(null);
    view2131230871 = null;
    view2131231183.setOnClickListener(null);
    view2131231183 = null;

    this.target = null;
  }
}
