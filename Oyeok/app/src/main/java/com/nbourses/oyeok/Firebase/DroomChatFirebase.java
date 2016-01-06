package com.nbourses.oyeok.Firebase;

import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

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
        Map<String,Object> map1= new HashMap<String, Object>();
        map1.put("title",droomDetails.getTitle());
        map1.put("timestamp",droomDetails.getTimestamp());
        map1.put("lastMessage",droomDetails.getLastMessage());
        map1.put("status",droomDetails.getStatus());
        Map<String,Object> map2= new HashMap<String, Object>();
        map2.put("title",droomDetails.getTitle());
        map2.put("timestamp",droomDetails.getTimestamp());
        map2.put("lastMessage",droomDetails.getLastMessage());
        map2.put("status","Unread");
        Firebase chatFirebaseReference1,chatFirebaseReference2;
        chatFirebaseReference1=firebaseReference.child(userId1);
        chatFirebaseReference2=firebaseReference.child(userId2);
        chatFirebaseReference1.child(okId).updateChildren(map1);
        chatFirebaseReference2.child(okId).updateChildren(map2);
    }

    public void updateUserChat(String okId,String userId,DroomDetails droomDetails){
        Map<String,Object> map1= new HashMap<String, Object>();
        map1.put("title",droomDetails.getTitle());
        map1.put("timestamp",droomDetails.getTimestamp());
        map1.put("lastMessage",droomDetails.getLastMessage());
        map1.put("status",droomDetails.getStatus());
        Firebase chatFirebaseReference1,chatFirebaseReference2;
        chatFirebaseReference1=firebaseReference.child(userId);
        chatFirebaseReference1.child(okId).updateChildren(map1);

    }

    public DroomDetails getChatRoom(String okId,String userId1){
        final DroomDetails[] droomDetails = new DroomDetails[1];
        Firebase firebaseReference1=firebaseReference.child(userId1).child(okId);
        firebaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                droomDetails[0] =(DroomDetails)snapshot.getValue();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        return droomDetails[0];
    }

    public HashMap<String,HashMap<String,String>> getDroomList(String userId){
        Firebase firebaseReference1=firebaseReference.child(userId);
        final HashMap<String,HashMap<String,String>> listOfChildren=new HashMap<String,HashMap<String,String>>();
        firebaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                int i=0;
                for (DataSnapshot child : snapshot.getChildren()) {
                    listOfChildren.put(child.getKey(), (HashMap) child.getValue());
                    Log.i("Test2", "" + listOfChildren.size());
                    i++;
                }
                Log.i("Test3",listOfChildren.toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        return listOfChildren;
    }
}
