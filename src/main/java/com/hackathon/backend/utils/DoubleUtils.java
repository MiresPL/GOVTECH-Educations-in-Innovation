package com.hackathon.backend.utils;

public class DoubleUtils {
    public static double round(double valueToRound, int numberOfDecimalPlaces) {
        double multipicationFactor = Math.pow(10.0D, (double)numberOfDecimalPlaces);
        double interestedInZeroDPs = valueToRound * multipicationFactor;
        return (double)Math.round(interestedInZeroDPs) / multipicationFactor;
    }
}
