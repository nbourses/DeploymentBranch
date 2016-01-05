package com.nbourses.oyeok.Firebase;

import com.firebase.client.Firebase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Abhinandan on 1/5/2016.
 */
public class DroomChatFirebase {
    Firebase firebaseReference;
    public DroomChatFirebase(String url){
        this.firebaseReference=new Firebase(url);
        firebaseReference=firebaseReference.child("DroomChat");
    }

    public void createChatRoom(String okId,String userId1,String userId2,DroomDetails droomDetails){
        Firebase chatFirebaseReference1,chatFirebaseReference2;
        chatFirebaseReference1=firebaseReference.child(userId1);
        chatFirebaseReference2=firebaseReference.child(userId2);
        chatFirebaseReference1=chatFirebaseReference1.child(okId);
        chatFirebaseReference2=chatFirebaseReference2.child(okId);
        chatFirebaseReference1.setValue(droomDetails);
        chatFirebaseReference2.setValue(droomDetails);
    }

    public void updateChatRoom(String okId,String userId1,String userId2,DroomDetails droomDetails){
        Map<String,Object> map= new HashMap<String, Object>();
        map.put("title",droomDetails.getTitle());
        map.put("timestamp",droomDetails.getTimestamp());
        map.put("lastMessage",droomDetails.getLastMessage());
        map.put("status",droomDetails.getStatus());
        Firebase chatFirebaseReference1,chatFirebaseReference2;
        chatFirebaseReference1=firebaseReference.child(userId1);
        chatFirebaseReference2=firebaseReference.child(userId2);
        chatFirebaseReference1.child(okId).updateChildren(map);
        chatFirebaseReference2.child(okId).updateChildren(map);

    }
}
