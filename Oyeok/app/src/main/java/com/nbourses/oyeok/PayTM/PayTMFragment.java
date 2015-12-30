package com.nbourses.oyeok.PayTM;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.nbourses.oyeok.R;
import com.paytm.pgsdk.PaytmClientCertificate;
import com.paytm.pgsdk.PaytmMerchant;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by YASH_SHAH on 21/12/2015.
 */
public class PayTMFragment extends Fragment implements View.OnClickListener {

    private Context context;
    private Button bPay;
    private int randomInt;
    private PaytmPGService service;
    private final String TAG = "PayTMFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getContext();
        View view = inflater.inflate(R.layout.fragment_paytm,
                container, false);
        bPay = (Button) view.findViewById(R.id.bpay);
        bPay.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        randomInt = 0;
        service = null;

        Random randomGenerator = new Random();
        randomInt = randomGenerator.nextInt(1000);
        //for testing environment
        service = PaytmPGService.getStagingService();
        //for production environment
        /*Service = PaytmPGService.getProductionService();*/

        /*PaytmMerchant constructor takes two parameters
        1) Checksum generation url
        2) Checksum verification url
        Merchant should replace the below values with his values*/

        //below parameter map is required to construct PaytmOrder object, Merchant should replace below map values with his own values

        Map<String, String> paramMap = new HashMap<String, String>();

        paramMap.put("REQUEST_TYPE", "DEFAULT");
        paramMap.put("ORDER_ID", "42TRIPS000" + randomInt);
        paramMap.put("MID", "Crypsi82771941768698");
        paramMap.put("CUST_ID", "9711139557");
        paramMap.put("MOBILE_NO", "9711139557");
        paramMap.put("EMAIL_ID", "test@test.com");
        paramMap.put("CHANNEL_ID", "WAP");
        paramMap.put("INDUSTRY_TYPE_ID", "Retail");
        paramMap.put("WEBSITE", "Crypsisapp");
        paramMap.put("TXN_AMOUNT", ""+100);
        paramMap.put("THEME", "merchant");



        PaytmOrder paytmOrder = new PaytmOrder(paramMap);
        PaytmMerchant Merchant = new PaytmMerchant("http://dev.42trips.com/paytm/checksum", "http://dev.42trips.com/paytm/checksum_verification");
        PaytmClientCertificate certificate = null;

        service.initialize(paytmOrder, Merchant, certificate);


        service.startPaymentTransaction(context, false, false, new PaytmPaymentTransactionCallback() {
            @Override
            public void onTransactionSuccess(Bundle bundle) {
                Log.e(TAG, "Transaction Success :" + bundle);
            }

            @Override
            public void onTransactionFailure(String s, Bundle bundle) {
                Log.e(TAG, "Transaction Failure :" + s + "\n" + bundle);
            }

            @Override
            public void networkNotAvailable() {
                Log.e(TAG, "network unavailable :");
            }

            @Override
            public void clientAuthenticationFailed(String s) {
                Log.e(TAG, "clientAuthenticationFailed :" + s);
            }

            @Override
            public void someUIErrorOccurred(String s) {
                Log.e(TAG, "someUIErrorOccurred :" + s);
            }

            @Override
            public void onErrorLoadingWebPage(int i, String s, String s2) {
                Log.e(TAG, "errorLoadingWebPage :" + i + "\n" + s + "\n" + s2);
            }
        });

    }
}
