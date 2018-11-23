// Generated code from Butter Knife. Do not modify!
package bcaasc.io.ethdemo.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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

    target.tvFee = Utils.findRequiredViewAsType(source, R.id.tv_fee, "field 'tvFee'", TextView.class);
    target.tvAddress = Utils.findRequiredViewAsType(source, R.id.tv_address, "field 'tvAddress'", TextView.class);
    target.ibReceive = Utils.findRequiredViewAsType(source, R.id.ib_receive, "field 'ibReceive'", ImageButton.class);
    target.llMyAddress = Utils.findRequiredViewAsType(source, R.id.ll_my_address, "field 'llMyAddress'", LinearLayout.class);
    target.tvGetBalance = Utils.findRequiredViewAsType(source, R.id.tv_get_balance, "field 'tvGetBalance'", TextView.class);
    target.tvBalance = Utils.findRequiredViewAsType(source, R.id.tv_balance, "field 'tvBalance'", TextView.class);
    target.llMyBalance = Utils.findRequiredViewAsType(source, R.id.ll_my_balance, "field 'llMyBalance'", LinearLayout.class);
    target.etAmount = Utils.findRequiredViewAsType(source, R.id.et_amount, "field 'etAmount'", EditText.class);
    target.btnToAddress = Utils.findRequiredViewAsType(source, R.id.btn_to_address, "field 'btnToAddress'", TextView.class);
    target.tvToAddress = Utils.findRequiredViewAsType(source, R.id.tv_to_address, "field 'tvToAddress'", TextView.class);
    target.ibScan = Utils.findRequiredViewAsType(source, R.id.ib_scan, "field 'ibScan'", ImageButton.class);
    target.llToSendAddress = Utils.findRequiredViewAsType(source, R.id.ll_to_send_address, "field 'llToSendAddress'", LinearLayout.class);
    target.btnPush = Utils.findRequiredViewAsType(source, R.id.btn_push, "field 'btnPush'", Button.class);
    target.btnGetTxList = Utils.findRequiredViewAsType(source, R.id.btn_get_tx_list, "field 'btnGetTxList'", Button.class);
    target.llSendAction = Utils.findRequiredViewAsType(source, R.id.ll_send_action, "field 'llSendAction'", LinearLayout.class);
    target.tvGetTxStatus = Utils.findRequiredViewAsType(source, R.id.tv_get_tx_status, "field 'tvGetTxStatus'", TextView.class);
    target.tvTxHash = Utils.findRequiredViewAsType(source, R.id.tv_tx_hash, "field 'tvTxHash'", TextView.class);
    target.ibScanHash = Utils.findRequiredViewAsType(source, R.id.ib_scan_hash, "field 'ibScanHash'", ImageButton.class);
    target.llQuery = Utils.findRequiredViewAsType(source, R.id.ll_query, "field 'llQuery'", LinearLayout.class);
    target.tvContent = Utils.findRequiredViewAsType(source, R.id.tv_content, "field 'tvContent'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MainNewActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tvFee = null;
    target.tvAddress = null;
    target.ibReceive = null;
    target.llMyAddress = null;
    target.tvGetBalance = null;
    target.tvBalance = null;
    target.llMyBalance = null;
    target.etAmount = null;
    target.btnToAddress = null;
    target.tvToAddress = null;
    target.ibScan = null;
    target.llToSendAddress = null;
    target.btnPush = null;
    target.btnGetTxList = null;
    target.llSendAction = null;
    target.tvGetTxStatus = null;
    target.tvTxHash = null;
    target.ibScanHash = null;
    target.llQuery = null;
    target.tvContent = null;
  }
}
