// Generated code from Butter Knife. Do not modify!
package com.stx.xhb.DCAPlatform.activities;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class AboutActivity$$ViewBinder<T extends com.stx.xhb.DCAPlatform.activities.AboutActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165189, "field 'aboutToolbar'");
    target.aboutToolbar = finder.castView(view, 2131165189, "field 'aboutToolbar'");
    view = finder.findRequiredView(source, 2131165568, "field 'version'");
    target.version = finder.castView(view, 2131165568, "field 'version'");
    view = finder.findRequiredView(source, 2131165244, "method 'onClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131165246, "method 'onClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.aboutToolbar = null;
    target.version = null;
  }
}
