// Generated code from Butter Knife. Do not modify!
package com.stx.xhb.DCAPlatform.fragment;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class GameFragment$$ViewBinder<T extends com.stx.xhb.DCAPlatform.fragment.GameFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165389, "field 'ptrLayout'");
    target.ptrLayout = finder.castView(view, 2131165389, "field 'ptrLayout'");
    view = finder.findRequiredView(source, 2131165360, "field 'multiplestatusview'");
    target.multiplestatusview = finder.castView(view, 2131165360, "field 'multiplestatusview'");
  }

  @Override public void unbind(T target) {
    target.ptrLayout = null;
    target.multiplestatusview = null;
  }
}
