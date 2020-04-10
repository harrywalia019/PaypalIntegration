package com.dXs.paypal.integration.interfaces;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.paypal.android.sdk.payments.PayPalConfiguration;

public interface IPaypalPresnetr<T> {

    /**
     * This method is used to get a Paypal config object
     */
    public PayPalConfiguration getPaypalConfig(Context ctx);

    public Context getContext();

    void startPaypalService();

    /**
     * This method will be called from the {@link androidx.fragment.app.FragmentActivity.onDestoy()}
     */
    void stopPaypalService();

    void openPaymentScreen(String amount);
    Intent getPaypalServiceIntent();
    boolean isPaymentConfirmed(Intent data);
    String getPaymentResultString(Intent data);
}
