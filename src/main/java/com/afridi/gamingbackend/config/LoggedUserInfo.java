package com.afridi.gamingbackend.config;

public class LoggedUserInfo {

    private static String loggedUserId;

    private static String firstName;

    private static String lastName;

    private static String mobileNo;

    private static Double acBalance;

    private static Boolean isActive;

    public static String getLoggedUserId() {
        return loggedUserId;
    }

    public static void setLoggedUserId(String loggedUserId) {
        LoggedUserInfo.loggedUserId = loggedUserId;
    }

    public static String getFirstName() {
        return firstName;
    }

    public static void setFirstName(String firstName) {
        LoggedUserInfo.firstName = firstName;
    }

    public static String getLastName() {
        return lastName;
    }

    public static void setLastName(String lastName) {
        LoggedUserInfo.lastName = lastName;
    }

    public static String getMobileNo() {
        return mobileNo;
    }

    public static void setMobileNo(String mobileNo) {
        LoggedUserInfo.mobileNo = mobileNo;
    }

    public static Double getAcBalance() {
        return acBalance;
    }

    public static void setAcBalance(Double acBalance) {
        LoggedUserInfo.acBalance = acBalance;
    }

    public static Boolean getIsActive() {
        return isActive;
    }

    public static void setIsActive(Boolean isActive) {
        LoggedUserInfo.isActive = isActive;
    }
}
