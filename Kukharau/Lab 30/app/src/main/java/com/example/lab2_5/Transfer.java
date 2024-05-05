package com.example.lab2_5;

public class Transfer {
    public static String sendDistance = "";
    public static String sendDuration = "";
    public static void setDistance(String distance){
        sendDistance = distance;
    }
    public static void setDuration(String duration){
        sendDuration = duration;
    }
    public static String getDistance(){
        return sendDistance;
    }
    public static String getDuration(){
        return sendDuration;
    }
}
