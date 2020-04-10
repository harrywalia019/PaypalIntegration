package com.dXs.paypal.integration.factory;

import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.FragmentActivity;
import com.dXs.paypal.integration.interfaces.IPaypalPresnetr;
import com.dXs.paypal.integration.utils.Const;
import com.dXs.paypal.integration.utils.PaypalUtil;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import org.json.JSONException;
import java.math.BigDecimal;


public class PaypalFactory<T> implements IPaypalPresnetr<T> {

    private static PaypalFactory mInstance;
    private static Context context;
    private Intent serviceIntent = null;
    private PayPalConfiguration config;

    private PaypalFactory() {
        super();
        config = getPaypalConfig(context);
    }

    public static synchronized PaypalFactory getInstance(Context ctx) {
        context = ctx;
        if (mInstance == null) {
            mInstance = new PaypalFactory();
        }

        return mInstance;
    }

    @Override
    public PayPalConfiguration getPaypalConfig(Context ctx)
    {
        if (config==null)
            config = new PayPalConfiguration()
                    .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                    .clientId(PaypalUtil.PAYPAL_CLIENT_ID);

        return config;
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public void startPaypalService() {
        getPaypalConfig(context);
        //start paypal service
            getPaypalServiceIntent();
            if (serviceIntent!=null) {
                serviceIntent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, getPaypalConfig(context));
                context.startService(serviceIntent);
            }
    }

    @Override
    public void stopPaypalService() {
        if (serviceIntent!=null) {
            context.stopService(serviceIntent);
            serviceIntent = null;
        }
    }

    @Override
    public void openPaymentScreen(String amount) {
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(amount)),"USD",
                "Purchase Goods",PayPalPayment.PAYMENT_INTENT_SALE);
        Intent paymentIntent = new Intent(context, PaymentActivity.class);
        paymentIntent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        paymentIntent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);

        ((FragmentActivity)context).startActivityForResult(paymentIntent, Const.PAYPAL_REQUEST_CODE);
    }

    @Override
    public Intent getPaypalServiceIntent() {
        if (serviceIntent==null)
            serviceIntent = new Intent(context, PayPalService.class);

        return serviceIntent;
    }

    @Override
    public boolean isPaymentConfirmed(Intent data) {
        PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

        return confirmation!=null?true:false;
    }

    @Override
    public String getPaymentResultString(Intent data) {
        PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
        String result = null;
        try {
            result = confirmation.toJSONObject().toString(4);
        } catch (JSONException e) {

        }
        return result;
    }

}
