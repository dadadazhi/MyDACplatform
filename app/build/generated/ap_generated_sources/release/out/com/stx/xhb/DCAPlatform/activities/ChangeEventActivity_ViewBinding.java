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

public class ChangeEventActivity_ViewBinding<T extends ChangeEventActivity> implements Unbinder {
  protected T target;

  private View view2131230763;

  private View view2131230890;

  private View view2131230817;

  @UiThread
  public ChangeEventActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.add_item, "method 'onClick'");
    view2131230763 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.image_event, "method 'onClick'");
    view2131230890 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.create_button, "method 'onClick'");
    view2131230817 = view;
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

    view2131230763.setOnClickListener(null);
    view2131230763 = null;
    view2131230890.setOnClickListener(null);
    view2131230890 = null;
    view2131230817.setOnClickListener(null);
    view2131230817 = null;

    this.target = null;
  }
}
