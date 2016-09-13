package com.example.soil.biogi.healthCheck;

/**
 * Created by soil on 2016/5/3.
 */
public class healthModel {
    private String date,value ;
    private String score ;

    public healthModel(){

    }

    public String getDate(){return  date;}

    public void setDate(String date){this.date = date ;}

    public String getValue(){
        return value ;
    }

    public void setValue(String value){
        this.value =value ;
    }

    public String getScore(){return  score;}

    public void setScore(String count){this.score =score;}


}
