package com.example.dailyfieldreport;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ReportExporterTest {

    @Test
    void saveTextAsPdfCreatesPdfFile(@TempDir Path tempDir) throws Exception {
        Path output = tempDir.resolve("report.pdf");

        ReportExporter.saveTextAsPDF("Project Name: Test Project\nTotal Hours: 12.50", List.of(), output.toString());

        assertTrue(Files.exists(output), "PDF file should be created.");
        assertTrue(Files.size(output) > 0, "PDF file should contain data.");
    }

    @Test
    void saveTextAsWordCreatesDocxFile(@TempDir Path tempDir) throws Exception {
        Path output = tempDir.resolve("report.docx");

        ReportExporter.saveTextAsWord("Project Name: Test Project\nTotal Hours: 12.50", List.of(), output.toString());

        assertTrue(Files.exists(output), "DOCX file should be created.");
        assertTrue(Files.size(output) > 0, "DOCX file should contain data.");
    }
}
