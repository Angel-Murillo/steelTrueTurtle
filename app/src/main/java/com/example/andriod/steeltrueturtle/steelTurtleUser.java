package com.example.andriod.steeltrueturtle;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class steelTurtleUser{
    public String name;
    public String phone;
    public String lineName;
    public String location;
    public String time;
    public String description;
    public String emails;
    //may use or not
    public String securityCode;

    public String gmail;

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
    {return time;}
    public void setTime(String time){
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

    //all new
    public String getName() {

        return name;
    }
    public void setName(String name) {

        this.name = name;
    }

    public String getEmail() {

        return emails;
    }
    public void setEmail(String emails) {

        this.emails= emails;
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
    public String getSecurityCode() {

        return securityCode;
    }
    public void setSecurityCode(String securityCode) {

        this.securityCode = securityCode;
    }
}
