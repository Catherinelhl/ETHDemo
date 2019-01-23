// Generated code from Butter Knife. Do not modify!
package bcaasc.io.ethdemo.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
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

    target.etAddress = Utils.findRequiredViewAsType(source, R.id.et_address, "field 'etAddress'", EditText.class);
    target.llMyAddress = Utils.findRequiredViewAsType(source, R.id.ll_my_address, "field 'llMyAddress'", LinearLayout.class);
    target.tvGetBalance = Utils.findRequiredViewAsType(source, R.id.tv_get_balance, "field 'tvGetBalance'", TextView.class);
    target.tvBalance = Utils.findRequiredViewAsType(source, R.id.tv_balance, "field 'tvBalance'", TextView.class);
    target.llMyBalance = Utils.findRequiredViewAsType(source, R.id.ll_my_balance, "field 'llMyBalance'", LinearLayout.class);
    target.etAmount = Utils.findRequiredViewAsType(source, R.id.et_amount, "field 'etAmount'", EditText.class);
    target.etToAddress = Utils.findRequiredViewAsType(source, R.id.et_to_address, "field 'etToAddress'", EditText.class);
    target.ibScan = Utils.findRequiredViewAsType(source, R.id.ib_scan, "field 'ibScan'", ImageButton.class);
    target.ibScanAddress = Utils.findRequiredViewAsType(source, R.id.ib_scan_address, "field 'ibScanAddress'", ImageButton.class);
    target.llToSendAddress = Utils.findRequiredViewAsType(source, R.id.ll_to_send_address, "field 'llToSendAddress'", LinearLayout.class);
    target.btnPush = Utils.findRequiredViewAsType(source, R.id.btn_push, "field 'btnPush'", Button.class);
    target.btnGetTxList = Utils.findRequiredViewAsType(source, R.id.btn_get_tx_list, "field 'btnGetTxList'", Button.class);
    target.llSendAction = Utils.findRequiredViewAsType(source, R.id.ll_send_action, "field 'llSendAction'", LinearLayout.class);
    target.tvGetTxStatus = Utils.findRequiredViewAsType(source, R.id.tv_get_tx_status, "field 'tvGetTxStatus'", TextView.class);
    target.tvTxHash = Utils.findRequiredViewAsType(source, R.id.tv_tx_hash, "field 'tvTxHash'", TextView.class);
    target.ibScanHash = Utils.findRequiredViewAsType(source, R.id.ib_scan_hash, "field 'ibScanHash'", ImageButton.class);
    target.llQuery = Utils.findRequiredViewAsType(source, R.id.ll_query, "field 'llQuery'", LinearLayout.class);
    target.tvContent = Utils.findRequiredViewAsType(source, R.id.tv_content, "field 'tvContent'", TextView.class);
    target.etFee = Utils.findRequiredViewAsType(source, R.id.et_fee, "field 'etFee'", EditText.class);
    target.switchNet = Utils.findRequiredViewAsType(source, R.id.switch_net, "field 'switchNet'", Switch.class);
    target.etPrivateKey = Utils.findRequiredViewAsType(source, R.id.et_private_key, "field 'etPrivateKey'", EditText.class);
    target.ibScanPrivateKey = Utils.findRequiredViewAsType(source, R.id.ib_scan_private_key, "field 'ibScanPrivateKey'", ImageButton.class);
    target.llMyPrivateKey = Utils.findRequiredViewAsType(source, R.id.ll_my_private_key, "field 'llMyPrivateKey'", LinearLayout.class);
    target.llSendAmount = Utils.findRequiredViewAsType(source, R.id.ll_send_amount, "field 'llSendAmount'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MainNewActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.etAddress = null;
    target.llMyAddress = null;
    target.tvGetBalance = null;
    target.tvBalance = null;
    target.llMyBalance = null;
    target.etAmount = null;
    target.etToAddress = null;
    target.ibScan = null;
    target.ibScanAddress = null;
    target.llToSendAddress = null;
    target.btnPush = null;
    target.btnGetTxList = null;
    target.llSendAction = null;
    target.tvGetTxStatus = null;
    target.tvTxHash = null;
    target.ibScanHash = null;
    target.llQuery = null;
    target.tvContent = null;
    target.etFee = null;
    target.switchNet = null;
    target.etPrivateKey = null;
    target.ibScanPrivateKey = null;
    target.llMyPrivateKey = null;
    target.llSendAmount = null;
  }
}
