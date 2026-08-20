package com.example.dailyfieldreport;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WebServerTest {

    @Test
    void parseSubmissionExtractsFieldsAndPhotoData() throws Exception {
        Method parseSubmission = WebServer.class.getDeclaredMethod("parseSubmission", String.class);
        parseSubmission.setAccessible(true);

        String json = "{\"fields\":{\"projectName\":\"Alpha Project\",\"format\":\"pdf\"},\"photosBase64\":[\"data:image/png;base64,AAAA\"]}";

        Object submission = parseSubmission.invoke(null, json);

        Field fieldsField = submission.getClass().getDeclaredField("fields");
        fieldsField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, String> fields = (Map<String, String>) fieldsField.get(submission);

        Field photosField = submission.getClass().getDeclaredField("photosBase64");
        photosField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<String> photos = (List<String>) photosField.get(submission);

        assertEquals("Alpha Project", fields.get("projectName"));
        assertEquals("pdf", fields.get("format"));
        assertEquals(1, photos.size());
        assertTrue(photos.get(0).contains("data:image/png;base64"));
    }

    @Test
    void buildPersonnelSectionIncludesTableHeaderAndTotalHours() throws Exception {
        Method buildSection = WebServer.class.getDeclaredMethod("buildPersonnelSection", String.class, String.class);
        buildSection.setAccessible(true);

        String personnelData = "Acme | Carpenter | 2 | 8 | John Doe\n" +
                "Delta | Laborer | 1 | 4 | Jane Smith";

        String result = (String) buildSection.invoke(null, personnelData, "12.50");

        assertTrue(result.contains("Company | Trade / Role | No. of Workers | Hours Worked | Foreman / Supervisor"));
        assertTrue(result.contains("Acme | Carpenter | 2 | 8 | John Doe"));
        assertTrue(result.contains("Total Hours: 12.50"));
    }
}
