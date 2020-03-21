package com.hoangdieuctu.boot.docker;


public class ServerApplication {

    static String timeConversion(String s) {
        String f = s.substring(0, s.length() - 2);
        String[] split = f.split(":");

        int hour = Integer.parseInt(split[0]);
        if (s.endsWith("PM") && hour != 12) {
            hour += 12;
        }
        if (s.endsWith("AM") && hour == 12) {
            hour = 0;
        }

        return String.format("%02d:%s:%s", hour, split[1], split[2]);
    }


    public static void main(String[] args) {
        System.out.println(timeConversion("07:05:45PM"));
        System.out.println(timeConversion("07:05:45AM"));
        System.out.println(timeConversion("12:00:00AM"));
        System.out.println(timeConversion("12:00:00PM"));
    }
}
