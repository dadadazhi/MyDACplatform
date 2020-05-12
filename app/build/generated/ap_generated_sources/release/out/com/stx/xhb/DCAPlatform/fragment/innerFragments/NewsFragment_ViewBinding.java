// Generated code from Butter Knife. Do not modify!
package com.stx.xhb.DCAPlatform.fragment.innerFragments;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.classic.common.MultipleStatusView;
import com.stx.xhb.DCAPlatform.R;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import java.lang.IllegalStateException;
import java.lang.Override;

public class NewsFragment_ViewBinding<T extends NewsFragment> implements Unbinder {
  protected T target;

  @UiThread
  public NewsFragment_ViewBinding(T target, View source) {
    this.target = target;

    target.multiplestatusview = Utils.findRequiredViewAsType(source, R.id.multiplestatusview, "field 'multiplestatusview'", MultipleStatusView.class);
    target.ptrLayout = Utils.findRequiredViewAsType(source, R.id.ptr_layout, "field 'ptrLayout'", PtrClassicFrameLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.multiplestatusview = null;
    target.ptrLayout = null;

    this.target = null;
  }
}
