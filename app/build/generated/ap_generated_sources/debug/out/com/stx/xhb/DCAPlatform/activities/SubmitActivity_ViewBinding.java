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

public class SubmitActivity_ViewBinding<T extends SubmitActivity> implements Unbinder {
  protected T target;

  private View view2131230802;

  private View view2131230979;

  private View view2131230800;

  @UiThread
  public SubmitActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.choose_word, "method 'onClick'");
    view2131230802 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.roundButton, "method 'onClick'");
    view2131230979 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.check_word, "method 'onClick'");
    view2131230800 = view;
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

    view2131230802.setOnClickListener(null);
    view2131230802 = null;
    view2131230979.setOnClickListener(null);
    view2131230979 = null;
    view2131230800.setOnClickListener(null);
    view2131230800 = null;

    this.target = null;
  }
}
