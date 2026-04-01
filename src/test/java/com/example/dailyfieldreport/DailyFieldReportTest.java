package com.example.dailyfieldreport;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DailyFieldReportTest {

    @Test
    public void testExportFormDataContainsFields() {
        DailyFieldReport dfr = new DailyFieldReport();
        // set some fields via reflection since fields are private (we could add setters but test keeps minimal changes)
        try {
            java.lang.reflect.Field f = DailyFieldReport.class.getDeclaredField("projectNameField");
            f.setAccessible(true);
            javax.swing.JTextField tf = (javax.swing.JTextField) f.get(dfr);
            tf.setText("Test Project");

            java.lang.reflect.Field wf = DailyFieldReport.class.getDeclaredField("workPerformedArea");
            wf.setAccessible(true);
            javax.swing.JTextArea ta = new javax.swing.JTextArea();
            ta.setText("Performed testing");
            wf.set(dfr, ta);

            String out = dfr.exportFormDataToString();
            assertTrue(out.contains("Test Project"), "Export should contain project name");
            assertTrue(out.contains("Performed testing"), "Export should contain work performed text");
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}

