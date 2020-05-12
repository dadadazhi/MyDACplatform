// Generated code from Butter Knife. Do not modify!
package com.stx.xhb.DCAPlatform.activities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.stx.xhb.DCAPlatform.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SendMessageActivity_ViewBinding<T extends SendMessageActivity> implements Unbinder {
  protected T target;

  private View view2131230817;

  private View view2131230773;

  private View view2131231000;

  private View view2131231001;

  @UiThread
  public SendMessageActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.create_button, "method 'onClick'");
    view2131230817 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.am_title, "method 'onClick'");
    view2131230773 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.select1, "method 'onClick'");
    view2131231000 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.select2, "method 'onClick'");
    view2131231001 = view;
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

    view2131230817.setOnClickListener(null);
    view2131230817 = null;
    view2131230773.setOnClickListener(null);
    view2131230773 = null;
    view2131231000.setOnClickListener(null);
    view2131231000 = null;
    view2131231001.setOnClickListener(null);
    view2131231001 = null;

    this.target = null;
  }
}
