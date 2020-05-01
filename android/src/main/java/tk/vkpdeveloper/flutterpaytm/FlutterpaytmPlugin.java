package tk.vkpdeveloper.flutterpaytm;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

public class FlutterpaytmPlugin implements MethodCallHandler {
  public static Activity activity;
  Result result;
  String TAG = getClass().getName();

  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "flutterpaytm");
    FlutterpaytmPlugin.activity = registrar.activity();
    channel.setMethodCallHandler(new FlutterpaytmPlugin());
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    this.result = result;
    String mid = call.argument("mid");
    String checksum = call.argument("checksum").toString();
    String verificationURL = call.argument("verificationURL").toString();
    String orderId = call.argument("orderId").toString();
    String customerId = call.argument("customerId").toString();
    String amount = call.argument("amount").toString();
    String website = call.argument("website").toString();
    String industry = call.argument("industry").toString();
    boolean isTesting = call.argument("isTesting");
    if (call.method.equals("startPayment")) {
      PaytmPaymentGateway gateway = new PaytmPaymentGateway(mid, checksum, verificationURL, orderId, customerId, amount, website, industry, isTesting);
      gateway.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    } else {
      result.notImplemented();
    }
  }

  void sendResponse(Map<Object, Object> parmasMap) {
    result.success(parmasMap);
  }

  public class PaytmPaymentGateway extends AsyncTask<ArrayList<String>, Void, String> {
    private String orderId;
    private String customerId;
    private String amount;
    private String mid;
    private String verifyUrl;
    private String website;
    private String industryType;
    private boolean isTesting;
    private String CHECKSUMHASH;

    PaytmPaymentGateway(String mid, String checksum, String verificationURL, String orderId, String customerId, String amount, String website, String industry, boolean isTesting) {
      this.mid = mid;
      this.verifyUrl = verificationURL;
      this.orderId = orderId;
      this.customerId = customerId;
      this.amount = amount;
      this.website = website;
      this.industryType = industry;
      this.isTesting = isTesting;
      this.CHECKSUMHASH = checksum;
    }

    private ProgressDialog dialog = new ProgressDialog(activity);

    protected void onPreExecute() {
      dialog.setCancelable(false);
      this.dialog.setMessage("Please wait");
      this.dialog.show();
    }

    protected String doInBackground(ArrayList<String>... alldata) {
      return CHECKSUMHASH;
    }

    protected void onPostExecute(String result) {
      if (dialog.isShowing()) {
        dialog.dismiss();
      }
      PaytmPGService Service;
      if(isTesting){
        Service = PaytmPGService.getStagingService();
      }else{
        Service = PaytmPGService.getProductionService();
      }

      HashMap<String, String> paramMap = new HashMap<String, String>();
      paramMap.put("MID", mid);
      paramMap.put("ORDER_ID", orderId);
      paramMap.put("CUST_ID", customerId);
      paramMap.put("CHANNEL_ID", "WAP");
      paramMap.put("TXN_AMOUNT", amount);
      paramMap.put("WEBSITE", website);
      paramMap.put("CALLBACK_URL" ,verifyUrl+"?ORDER_ID="+orderId);
      paramMap.put("CHECKSUMHASH" ,CHECKSUMHASH);
      paramMap.put("INDUSTRY_TYPE_ID", industryType);

      PaytmOrder Order = new PaytmOrder(paramMap);
      Service.initialize(Order,null);
      Service.startPaymentTransaction(activity, true, true,
              new PaytmPaymentTransactionCallback() {
                @Override
                public void onTransactionResponse(Bundle inResponse) {
                  Map<Object, Object> parma = new HashMap<>();
                  for (String key : inResponse.keySet()) {
                    parma.put(key, inResponse.getString(key));
                  }
                  sendResponse(parma);
                }

                @Override
                public void networkNotAvailable() {
                  Map<Object, Object> parma = new HashMap<>();
                  parma.put("error", true);
                  parma.put("issue", "network");
                  parma.put("result", "Network not available");
                  sendResponse(parma);
                }

                @Override
                public void clientAuthenticationFailed(String inErrorMessage) {
                  Map<Object, Object> parma = new HashMap<>();
                  parma.put("error", true);
                  parma.put("issue", "auth failed");
                  parma.put("result", "Client auth failed");
                  sendResponse(parma);
                }

                @Override
                public void someUIErrorOccurred(String inErrorMessage) {
                  Map<Object, Object> parma = new HashMap<>();
                  parma.put("error", true);
                  parma.put("issue", "ui error");
                  parma.put("result", "UI Error Occurred");
                  sendResponse(parma);
                }

                @Override
                public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                  Map<Object, Object> parma = new HashMap<>();
                  parma.put("error", true);
                  parma.put("issue", "page load fail");
                  parma.put("result", "Page does not loaded successfully");
                  sendResponse(parma);
                }

                @Override
                public void onBackPressedCancelTransaction() {
                  Map<Object, Object> parma = new HashMap<>();
                  parma.put("error", true);
                  parma.put("issue", "back pressed");
                  parma.put("result", "Back pressed from payment");
                  sendResponse(parma);
                }

                @Override
                public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                  Map<Object, Object> parma = new HashMap<>();
                  parma.put("error", true);
                  parma.put("issue", "cancel");
                  parma.put("result", "Payment cancelled");
                  sendResponse(parma);
                }
              });


    }

  }

}
