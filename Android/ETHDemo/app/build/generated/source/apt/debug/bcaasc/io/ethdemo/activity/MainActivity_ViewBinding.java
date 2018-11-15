// Generated code from Butter Knife. Do not modify!
package bcaasc.io.ethdemo.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import bcaasc.io.ethdemo.R;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MainActivity_ViewBinding implements Unbinder {
  private MainActivity target;

  @UiThread
  public MainActivity_ViewBinding(MainActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public MainActivity_ViewBinding(MainActivity target, View source) {
    this.target = target;

    target.tvAddress = Utils.findRequiredViewAsType(source, R.id.tv_address, "field 'tvAddress'", TextView.class);
    target.btnGetBalance = Utils.findRequiredViewAsType(source, R.id.btn_get_balance, "field 'btnGetBalance'", Button.class);
    target.btnGetTxList = Utils.findRequiredViewAsType(source, R.id.btn_get_tx_list, "field 'btnGetTxList'", Button.class);
    target.btnPush = Utils.findRequiredViewAsType(source, R.id.btn_push, "field 'btnPush'", Button.class);
    target.btnGetTxStatus = Utils.findRequiredViewAsType(source, R.id.btn_get_tx_status, "field 'btnGetTxStatus'", Button.class);
    target.etContent = Utils.findRequiredViewAsType(source, R.id.et_content, "field 'etContent'", EditText.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MainActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tvAddress = null;
    target.btnGetBalance = null;
    target.btnGetTxList = null;
    target.btnPush = null;
    target.btnGetTxStatus = null;
    target.etContent = null;
  }
}
