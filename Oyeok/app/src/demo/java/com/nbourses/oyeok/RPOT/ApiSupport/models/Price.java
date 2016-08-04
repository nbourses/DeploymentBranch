package com.nbourses.oyeok.RPOT.ApiSupport.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Price {

   @SerializedName("ll_min")
   @Expose
   private String llMin;
   @SerializedName("ll_max")
   @Expose
   private String llMax;
   @SerializedName("or_min")
   @Expose
   private String orMin;
   @SerializedName("or_max")
   @Expose
   private String orMax;

   /**
    *
    * @return
    * The llMin
    */
   public String getLlMin() {
       return llMin;
   }

   /**
    *
    * @param llMin
    * The ll_min
    */
   public void setLlMin(String llMin) {
       this.llMin = llMin;
   }

   /**
    *
    * @return
    * The llMax
    */
   public String getLlMax() {
       return llMax;
   }

   /**
    *
    * @param llMax
    * The ll_max
    */
   public void setLlMax(String llMax) {
       this.llMax = llMax;
   }

   /**
    *
    * @return
    * The orMin
    */
   public String getOrMin() {
       return orMin;
   }

   /**
    *
    * @param orMin
    * The or_min
    */
   public void setOrMin(String orMin) {
       this.orMin = orMin;
   }

   /**
    *
    * @return
    * The orMax
    */
   public String getOrMax() {
       return orMax;
   }

   /**
    *
    * @param orMax
    * The or_max
    */
   public void setOrMax(String orMax) {
       this.orMax = orMax;
   }

}
