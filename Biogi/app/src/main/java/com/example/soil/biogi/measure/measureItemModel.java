package com.example.soil.biogi.measure;

/**
 * Created by soil on 2016/4/27.
 */
public class measureItemModel {
    private String value, indate,name,inspectid,unit ;
    public measureItemModel(String name, String value, String date,String id,String unit){
        this.value =value ;
        this.name = name ;
        this.indate =date ;
        this.inspectid = id ;
        this.unit = unit ;
    }
    public String getName(){return name;}
    public void setName(String title){this.name = title ;

    }
    public String getYear(){return indate;}

    public void setYear(String year){
        this.indate = year ;

    }
    public String getValue(){
        return value;
    }
    public void setValues(String values) {
        this.value = values;
    }

    public String getId(){return inspectid;}
    public void setId(String id){this.inspectid = id ;

    }
    public String getUnit(){return  unit;}
    public void setUnit(String unit){this.unit = unit;}
}


