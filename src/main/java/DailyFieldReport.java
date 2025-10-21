/*
 * Launcher kept in the default package so existing run configurations that reference
 * the unqualified main class name `DailyFieldReport` will continue to work.
 * The real implementation lives in package com.example.dailyfieldreport.
 */

public class DailyFieldReport {
    public static void main(String[] args) {
        com.example.dailyfieldreport.DailyFieldReport.main(args);
    }
}

