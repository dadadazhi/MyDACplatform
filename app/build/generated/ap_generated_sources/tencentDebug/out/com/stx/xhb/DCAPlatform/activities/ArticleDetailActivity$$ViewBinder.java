// Generated code from Butter Knife. Do not modify!
package com.stx.xhb.DCAPlatform.activities;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ArticleDetailActivity$$ViewBinder<T extends com.stx.xhb.DCAPlatform.activities.ArticleDetailActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165233, "method 'onClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick();
        }
      });
  }

  @Override public void unbind(T target) {
  }
}
