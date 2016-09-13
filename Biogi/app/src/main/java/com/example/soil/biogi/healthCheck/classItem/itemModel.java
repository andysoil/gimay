package com.example.soil.biogi.healthCheck.classItem;

/**
 * Created by soil on 2016/6/19.
 */
public class itemModel {
    private String id,name,range,value ;
    public itemModel( String name,String id,String range,String value){
        this.id = id ;
        this.name = name ;
        this.range = range;
        this.value = value ;
    }
    public void setName(String name){
        this.name = name ;
    }
    public String getName(){
        return name ;
    }
    public void setId(String id){
        this.id = id ;
    }
    public String getId(){
        return id ;
    }
    public void setRange(String range){
        this.range = range ;
    }
    public String getRange(){
        return range ;
    }

    public void setValue(String value){
        this.value = value ;
    }
    public String getValue(){
        return value ;
    }
}
