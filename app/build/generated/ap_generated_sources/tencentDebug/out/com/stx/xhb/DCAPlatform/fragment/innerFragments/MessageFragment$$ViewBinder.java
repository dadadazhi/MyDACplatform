// Generated code from Butter Knife. Do not modify!
package com.stx.xhb.DCAPlatform.fragment.innerFragments;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MessageFragment$$ViewBinder<T extends com.stx.xhb.DCAPlatform.fragment.innerFragments.MessageFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165360, "field 'multiplestatusview'");
    target.multiplestatusview = finder.castView(view, 2131165360, "field 'multiplestatusview'");
    view = finder.findRequiredView(source, 2131165389, "field 'ptrLayout'");
    target.ptrLayout = finder.castView(view, 2131165389, "field 'ptrLayout'");
  }

  @Override public void unbind(T target) {
    target.multiplestatusview = null;
    target.ptrLayout = null;
  }
}
