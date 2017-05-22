package com.example.andriod.steeltrueturtle;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class steelTurtleUser{
    private String name;
    private String phone;
    private String lineName;
    private String location;
    private String time;
    private String description;
    private String gmail;

    public steelTurtleUser() {
    }
    public steelTurtleUser(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }
    //used to instantiate client
    public steelTurtleUser(String name, String phone, String gmail) {
        this.name = name;
        this.phone = phone;
        this.gmail = gmail;
    }
    //used to instantiate host
    public steelTurtleUser(String name, String phone, String lineName,String location,String time ,String description, String gmail) {
        this.name = name;
        this.phone= phone;
        this.lineName = lineName;
        this.location = location;
        this.time = time;
        this.description = description;
        this.gmail = gmail;
    }
    public String getTime()
    {
        return time;
    }
    public void setTime(String time)
    {
        this.time = time;
    }
    public String getPhone()
    {return phone;}
    public void setPhone(String phone){

        this.phone = phone;
    }
    public String getGmail()
    {

        return gmail;
    }
    public void setGmail(String gmail)

    {
        this.gmail= gmail;
    }


    public String getName() {

        return name;
    }
    public void setName(String name) {

        this.name = name;
    }


    public String getLocation() {

        return location;
    }
    public void setlocation(String location) {

        this.location= location;
    }
    public String getLineName() {

        return lineName;
    }
    public void setLineName(String lineName) {

        this.lineName= lineName;
    }
    public String getDescription() {

        return description;
    }
    public void setDescription(String description) {

        this.description = description;
    }

}
