package com.example.dailyfieldreport;

import org.junit.jupiter.api.Test;

import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DailyFieldReportTest {

    @Test
    void exportFormDataToStringIncludesProjectNameAndWorkPerformed() throws Exception {
        DailyFieldReport report = new DailyFieldReport();

        Field projectNameField = DailyFieldReport.class.getDeclaredField("projectNameField");
        projectNameField.setAccessible(true);
        JTextField projectName = (JTextField) projectNameField.get(report);
        projectName.setText("Test Project");

        Field workPerformedField = DailyFieldReport.class.getDeclaredField("workPerformedArea");
        workPerformedField.setAccessible(true);
        JTextArea workPerformed = new JTextArea();
        workPerformed.setText("Performed testing and documentation");
        workPerformedField.set(report, workPerformed);

        String output = report.exportFormDataToString();

        assertTrue(output.contains("Test Project"), "Export should include the project name.");
        assertTrue(output.contains("Performed testing and documentation"), "Export should include work performed details.");
    }

    @Test
    void exportFormDataToStringIncludesWeatherAndLocationFields() throws Exception {
        DailyFieldReport report = new DailyFieldReport();

        Field locationField = DailyFieldReport.class.getDeclaredField("locationField");
        locationField.setAccessible(true);
        ((JTextField) locationField.get(report)).setText("Site A");

        Field weatherField = DailyFieldReport.class.getDeclaredField("weatherField");
        weatherField.setAccessible(true);
        ((JTextField) weatherField.get(report)).setText("Sunny / 65F");

        String output = report.exportFormDataToString();

        assertTrue(output.contains("Site A"), "Export should include the location.");
        assertTrue(output.contains("Sunny / 65F"), "Export should include weather conditions.");
    }
}

