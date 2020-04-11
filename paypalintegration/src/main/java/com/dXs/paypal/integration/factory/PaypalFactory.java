//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.dXs.paypal.integration.factory;

import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.FragmentActivity;
import com.dXs.paypal.integration.interfaces.IPaypalPresnetr;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import java.math.BigDecimal;
import org.json.JSONException;

public class PaypalFactory<T> implements IPaypalPresnetr<T> {
    private static PaypalFactory mInstance;
    private static Context context;
    private Intent serviceIntent = null;
    private PayPalConfiguration config;

    private PaypalFactory() {
        this.config = this.getPaypalConfig(context);
    }

    public static synchronized PaypalFactory getInstance(Context ctx) {
        context = ctx;
        if (mInstance == null) {
            mInstance = new PaypalFactory();
        }

        return mInstance;
    }

    public PayPalConfiguration getPaypalConfig(Context ctx) {
        if (this.config == null) {
            this.config = (new PayPalConfiguration()).environment("sandbox").clientId("Aa-v3b7SnUGbHMl93L9xs59WcHL8IeinCGSA8a6Og6vsOneRLHGgBFHRpmziHKiTPGHVNyEFE6LXK-xv");
        }

        return this.config;
    }

    public Context getContext() {
        return context;
    }

    public void startPaypalService() {
        this.getPaypalConfig(context);
        this.getPaypalServiceIntent();
        if (this.serviceIntent != null) {
            this.serviceIntent.putExtra("com.paypal.android.sdk.paypalConfiguration", this.getPaypalConfig(context));
            context.startService(this.serviceIntent);
        }

    }

    public void stopPaypalService() {
        if (this.serviceIntent != null) {
            context.stopService(this.serviceIntent);
            this.serviceIntent = null;
        }

    }

    public void openPaymentScreen(String amount) {
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(amount)), "USD", "Purchase Goods", "sale");
        Intent paymentIntent = new Intent(context, PaymentActivity.class);
        paymentIntent.putExtra("com.paypal.android.sdk.paypalConfiguration", this.config);
        paymentIntent.putExtra("com.paypal.android.sdk.payment", payPalPayment);
        ((FragmentActivity)context).startActivityForResult(paymentIntent, 7777);
    }

    public Intent getPaypalServiceIntent() {
        if (this.serviceIntent == null) {
            this.serviceIntent = new Intent(context, PayPalService.class);
        }

        return this.serviceIntent;
    }

    public boolean isPaymentConfirmed(Intent data) {
        PaymentConfirmation confirmation = (PaymentConfirmation)data.getParcelableExtra("com.paypal.android.sdk.paymentConfirmation");
        return confirmation != null;
    }

    public String getPaymentResultString(Intent data) {
        PaymentConfirmation confirmation = (PaymentConfirmation)data.getParcelableExtra("com.paypal.android.sdk.paymentConfirmation");
        String result = null;

        try {
            result = confirmation.toJSONObject().toString(4);
        } catch (JSONException var5) {
        }

        return result;
    }
}
