package com.nbourses.oyeok.game;

/**
 * Created by sushil on 26/09/16.
 */

public class GamingIntentService extends Thread {
    int position;

    public GamingIntentService(int minPrime) {
        this.position = minPrime;
    }

    public void run() {
        // compute primes larger than minPrime
        //h.sendEmptyMessageDelayed(0,5000);
    }


  /*  private void threadMsg(String msg) {

        if (!msg.equals(null) && !msg.equals("")) {
            Message msgObj = handler.obtainMessage();
            Bundle b = new Bundle();
            b.putString("message", msg);
            msgObj.setData(b);
            handler.sendMessage(msgObj);
        }

    }*/
}


