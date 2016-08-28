package com.nbourses.oyeok.realmModels;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ritesh on 19/08/16.
 */
public class DealStatus extends RealmObject {


    @PrimaryKey
    private String ok_id;


    private String status;



    public String getOk_id(){
        return ok_id;
    }

    public void setOk_id(String ok_id){
        this.ok_id=ok_id;
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status=status;
    }
}

