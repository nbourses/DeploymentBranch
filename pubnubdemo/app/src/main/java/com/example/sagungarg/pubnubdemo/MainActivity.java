package com.example.sagungarg.pubnubdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private EditText message;
    private Button send;
    Callback callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        message = (EditText) findViewById(R.id.message);
        send = (Button) findViewById(R.id.send);


        final Pubnub pubnub = new Pubnub("pub-c-da891650-b0d6-4cfc-901c-60ca47bfcf90", "sub-c-c85c5622-b36d-11e5-bd0b-02ee2ddab7fe");

        try {
            pubnub.subscribe("demo_tutorial1", new Callback() {
                public void successCallback(String channel, final Object message) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, message.toString(), Toast.LENGTH_LONG).show();
                        }
                    });

                }

                public void errorCallback(String channel, final PubnubError error) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, error.getErrorString(), Toast.LENGTH_LONG).show();
                        }
                    });}
            });
        } catch (PubnubException e) {
            e.printStackTrace();
        }

         callback = new Callback() {
            public void successCallback(String channel, Object response) {
                //System.out.println(response.toString());
                Log.i("chat history",response.toString());
                String data= response.toString();
                try {
                    //JSONObject rData= new JSONObject(data);
                    JSONArray aData= new JSONArray(data).getJSONArray(0);

                    for(int i=0;i<aData.length();i++) {
                        JSONObject rData = aData.getJSONObject(i);
                        Log.i("object"+i,rData.toString());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            public void errorCallback(String channel, PubnubError error) {
                System.out.println(error.toString());
            }
        };



        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject p = new JSONObject();
                try {
                    p.put("receiver_id","pratyush");
                    p.put("message","bvhd");
                    p.put("sender_id","pratik");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                pubnub.publish("demo_tutorial1", p, new Callback() {});
                pubnub.history("demo_tutorial1", 100, true, callback);

            }
        });



    }
}
