package com.nbourses.oyeok.Firebase;

import android.app.Activity;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.nbourses.oyeok.Database.DBHelper;
import com.nbourses.oyeok.Database.DatabaseConstants;
import com.nbourses.oyeok.Database.SharedPrefs;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Abhinandan on 1/5/2016.
 */
public class DroomChatFirebase {
    Firebase firebaseReference;
    HashMap<String,HashMap<String,String>> listOfChildren;

    public void setmCallBack(ChatList mCallBack) {
        this.mCallBack = mCallBack;
    }

    ChatList mCallBack;
    public DroomChatFirebase(String url){
        this.firebaseReference=new Firebase(url);
        firebaseReference=firebaseReference.child("DroomChat");

    }


    public void createChatRoom(String okId,String userId1,String userId2,DroomDetails droomDetails){
        Log.i("Mmmi",""+userId1+"  "+userId2+"  "+okId);
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
                droomDetails[0] = (DroomDetails) snapshot.getValue();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        return droomDetails[0];
    }

    public void getChatList(final Activity activity){
        final Set<String> chatList = null;
        final DBHelper dbHelper =new DBHelper(activity);
        Firebase firebaseReference1=firebaseReference.child(dbHelper.getValue(DatabaseConstants.userId));
        //final JSONObject[] jsonObject = new JSONObject[1];
        firebaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                int i=0;
                for (DataSnapshot child : snapshot.getChildren()) {
                    chatList.add(child.getKey());
                    i++;
                }
                Log.i("Test3", listOfChildren.toString());
                SharedPrefs.save(activity,"ChatList",chatList);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    public void getDroomList(String userId,Activity activity){
        Firebase firebaseReference1=firebaseReference.child(userId);
        listOfChildren=new HashMap<String,HashMap<String,String>>();
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
                mCallBack.sendData(listOfChildren);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
