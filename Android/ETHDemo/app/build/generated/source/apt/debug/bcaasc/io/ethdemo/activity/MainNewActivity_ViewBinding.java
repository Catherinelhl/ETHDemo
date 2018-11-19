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

public class MainNewActivity_ViewBinding implements Unbinder {
  private MainNewActivity target;

  @UiThread
  public MainNewActivity_ViewBinding(MainNewActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public MainNewActivity_ViewBinding(MainNewActivity target, View source) {
    this.target = target;

    target.etAddressTo = Utils.findRequiredViewAsType(source, R.id.et_address_to, "field 'etAddressTo'", EditText.class);
    target.etAmount = Utils.findRequiredViewAsType(source, R.id.et_amount, "field 'etAmount'", EditText.class);
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
    MainNewActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.etAddressTo = null;
    target.etAmount = null;
    target.tvAddress = null;
    target.btnGetBalance = null;
    target.btnGetTxList = null;
    target.btnPush = null;
    target.btnGetTxStatus = null;
    target.etContent = null;
  }
}
