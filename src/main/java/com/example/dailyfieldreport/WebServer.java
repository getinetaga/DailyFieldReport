package com.example.dailyfieldreport;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WebServer {
    private static final Logger LOGGER = Logger.getLogger(WebServer.class.getName());

    // Submission type used by the HTTP JSON parser
    private static class Submission {
        Map<String, String> fields;
        List<String> photosBase64;
    }

    public static void main(String[] args) throws Exception {
        // default port (can be overridden with -Ddfr.port=<port>)
        int port = 9090;
        String portProp = System.getProperty("dfr.port");
        if (portProp != null && !portProp.isEmpty()) {
            try {
                port = Integer.parseInt(portProp);
            } catch (NumberFormatException nfe) {
                System.err.println("Invalid dfr.port value '" + portProp + "', using default " + port);
            }
        }
        startServer(port);
    }

    public static void startServer(int port) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new StaticHandler());
        server.createContext("/submit", new SubmitHandler());
        server.createContext("/save", new SaveHandler());
        server.setExecutor(java.util.concurrent.Executors.newCachedThreadPool());
        server.start();
        LOGGER.info("Web server started on http://localhost:" + port);
    }

    static class StaticHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            if (path.equals("/") || path.equals("/index.html")) {
                serveResource(exchange, "/web/index.html", "index.html");
            } else if (path.equals("/report") || path.equals("/report.html")) {
                serveResource(exchange, "/web/report.html", "report.html");
            } else {
                byte[] msg = "Not found".getBytes(StandardCharsets.UTF_8);
                exchange.sendResponseHeaders(404, msg.length);
                exchange.getResponseBody().write(msg);
                exchange.close();
            }
        }

        private void serveResource(HttpExchange exchange, String resourcePath, String label) throws IOException {
            try (InputStream is = WebServer.class.getResourceAsStream(resourcePath)) {
                if (is == null) {
                    byte[] msg = (label + " page not found").getBytes(StandardCharsets.UTF_8);
                    exchange.sendResponseHeaders(500, msg.length);
                    exchange.getResponseBody().write(msg);
                    exchange.close();
                    return;
                }
                byte[] bytes = is.readAllBytes();
                Headers h = exchange.getResponseHeaders();
                h.set("Content-Type", "text/html; charset=utf-8");
                exchange.sendResponseHeaders(200, bytes.length);
                exchange.getResponseBody().write(bytes);
            } catch (Throwable t) {
                LOGGER.log(Level.SEVERE, "Error serving " + label, t);
                byte[] msg = "Server error".getBytes(StandardCharsets.UTF_8);
                exchange.sendResponseHeaders(500, msg.length);
                exchange.getResponseBody().write(msg);
            } finally {
                exchange.close();
            }
        }
    }

    static class SaveHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                exchange.sendResponseHeaders(405, -1);
                exchange.close();
                return;
            }

            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Submission sub;
            try {
                sub = parseSubmission(body);
            } catch (Throwable parseEx) {
                LOGGER.log(Level.WARNING, "Failed to parse save payload", parseEx);
                exchange.sendResponseHeaders(400, -1);
                exchange.close();
                return;
            }

            Map<String,String> f = sub.fields == null ? Collections.emptyMap() : sub.fields;
            String savedName = saveReportToDisk(f, buildFullTextReport(f, sub.photosBase64));
            String json = "{\"status\":\"saved\",\"filename\":\"" + savedName + "\"}";
            byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
            Headers h = exchange.getResponseHeaders();
            h.set("Content-Type", "application/json; charset=utf-8");
            exchange.sendResponseHeaders(200, bytes.length);
            exchange.getResponseBody().write(bytes);
            exchange.close();
        }
    }

    static class SubmitHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                exchange.sendResponseHeaders(405, -1);
                exchange.close();
                return;
            }

            Headers reqHeaders = exchange.getRequestHeaders();
            String contentType = reqHeaders.getFirst("Content-Type");
            if (contentType == null || !contentType.toLowerCase().contains("application/json")) {
                exchange.sendResponseHeaders(415, -1);
                exchange.close();
                return;
            }

            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Submission sub;
            try {
                sub = parseSubmission(body);
            } catch (Throwable parseEx) {
                LOGGER.log(Level.WARNING, "Failed to parse submission JSON", parseEx);
                exchange.sendResponseHeaders(400, -1);
                exchange.close();
                return;
            }

            Map<String,String> f = sub.fields == null ? Collections.emptyMap() : sub.fields;

            // Build ordered sections 1..10
            java.util.LinkedHashMap<String,String> sections = new java.util.LinkedHashMap<>();

            // 1. General Info
            StringBuilder gen = new StringBuilder();
            gen.append("Project Name: ").append(f.getOrDefault("projectName","")).append("\n");
            gen.append("Project No.: ").append(f.getOrDefault("projectNo","")).append("\n");
            gen.append("Location: ").append(f.getOrDefault("location","")).append("\n");
            gen.append("Date: ").append(f.getOrDefault("date","")).append("\n");
            gen.append("Weather: ").append(f.getOrDefault("weather","")).append("\n");
            gen.append("Temperature: ").append(f.getOrDefault("temperature","")).append("\n");
            gen.append("Wind: ").append(f.getOrDefault("wind","")).append("\n");
            sections.put("1. General Info", gen.toString());

            // 2. Personnel
            sections.put("2. Personnel", buildPersonnelSection(
                    f.getOrDefault("personnel", ""),
                    f.getOrDefault("personnelTotalHours", "0")));

            // 3. Equipment
            sections.put("3. Equipment", f.getOrDefault("equipment", ""));

            // 4. Work Performed
            sections.put("4. Work Performed", f.getOrDefault("workPerformed", ""));

            // 5. Materials
            sections.put("5. Materials", f.getOrDefault("materials", ""));

            // 6. Inspections
            sections.put("6. Inspections", f.getOrDefault("inspections", ""));

            // 7. Safety
            StringBuilder safety = new StringBuilder();
            safety.append("Meeting Held: ").append(Boolean.parseBoolean(f.getOrDefault("safetyMeeting","false"))?"Yes":"No").append("\n");
            safety.append("Topic: ").append(f.getOrDefault("safetyTopic","")).append("\n");
            safety.append("Attendees: ").append(f.getOrDefault("safetyAttendees","")).append("\n");
            safety.append("Incidents/Near Misses: ").append(Boolean.parseBoolean(f.getOrDefault("safetyIncident","false"))?"Yes":"No").append("\n");
            sections.put("7. Safety", safety.toString());

            // 8. Delays/Issues
            sections.put("8. Delays/Issues", f.getOrDefault("delays", ""));

            // 9. Coordination
            StringBuilder coord = new StringBuilder();
            coord.append("Visitors: ").append(f.getOrDefault("visitors","")).append("\n");
            coord.append("Meetings: ").append(f.getOrDefault("meetings","")).append("\n");
            coord.append("Directions: ").append(f.getOrDefault("directions","")).append("\n");
            sections.put("9. Coordination", coord.toString());

            // 10. Attachments
            StringBuilder attach = new StringBuilder();
            if (sub.photosBase64 != null && !sub.photosBase64.isEmpty()) attach.append("Photos attached: ").append(sub.photosBase64.size()).append("\n");
            sections.put("10. Attachments", attach.toString());

            // Decode photos to temp files
            List<Path> tempPhotoPaths = new ArrayList<>();
            Path pdfOut = null;
            Path docxOut = null;
            try {
                if (sub.photosBase64 != null) {
                    int i = 0;
                    for (String b64 : sub.photosBase64) {
                        if (b64 == null || b64.isEmpty()) continue;
                        String clean = b64;
                        int comma = clean.indexOf(',');
                        if (comma >= 0) clean = clean.substring(comma + 1);
                        byte[] bytes = Base64.getDecoder().decode(clean);
                        Path tmp = Files.createTempFile("dfr-photo-" + (i++), ".png");
                        Files.write(tmp, bytes);
                        tempPhotoPaths.add(tmp);
                    }
                }

                // Respect the requested format (from web UI: pdf, docx, or txt)
                String requestedFormat = f.getOrDefault("format", "pdf").toLowerCase();
                boolean wantPdf = "pdf".equals(requestedFormat);
                boolean wantWord = "docx".equals(requestedFormat) || "word".equals(requestedFormat);
                boolean pdfAvailable = true;
                try {
                    Class.forName("org.apache.pdfbox.pdmodel.PDDocument");
                } catch (Throwable cnfe) {
                    pdfAvailable = false;
                }

                // Build a plain-text representation of the sections for the fallback
                StringBuilder fullTextBuilder = new StringBuilder();
                for (Map.Entry<String,String> se : sections.entrySet()) {
                   fullTextBuilder.append(se.getKey()).append("\n");
                   fullTextBuilder.append(se.getValue()).append("\n\n");
                }
                String fullText = fullTextBuilder.toString();

                if (wantPdf && pdfAvailable) {
                   try {
                       pdfOut = Files.createTempFile("dfr-report-", ".pdf");
                       Files.deleteIfExists(pdfOut);
                       ReportExporter.saveSectionsAsPDF(sections, tempPhotoPaths, pdfOut.toString());
                       byte[] pdfBytes = Files.readAllBytes(pdfOut);
                       Headers rh = exchange.getResponseHeaders();
                       rh.set("Content-Type", "application/pdf");
                       rh.set("Content-Disposition", "attachment; filename=DailyFieldReport.pdf");
                       exchange.sendResponseHeaders(200, pdfBytes.length);
                       exchange.getResponseBody().write(pdfBytes);
                       exchange.getResponseBody().flush();
                       return;
                   } catch (Throwable pdfEx) {
                       LOGGER.log(Level.WARNING, "PDF export failed, falling back to text", pdfEx);
                   }
                }

                if (wantWord) {
                   try {
                       docxOut = Files.createTempFile("dfr-report-", ".docx");
                       Files.deleteIfExists(docxOut);
                       ReportExporter.saveTextAsWord(fullText, tempPhotoPaths, docxOut.toString());
                       byte[] docxBytes = Files.readAllBytes(docxOut);
                       Headers rh = exchange.getResponseHeaders();
                       rh.set("Content-Type", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
                       rh.set("Content-Disposition", "attachment; filename=DailyFieldReport.docx");
                       exchange.sendResponseHeaders(200, docxBytes.length);
                       exchange.getResponseBody().write(docxBytes);
                       exchange.getResponseBody().flush();
                       return;
                   } catch (Throwable wordEx) {
                       LOGGER.log(Level.WARNING, "Word export failed, falling back to text", wordEx);
                   }
                }

                byte[] txtBytes = fullText.getBytes(StandardCharsets.UTF_8);
                Headers rh = exchange.getResponseHeaders();
                rh.set("Content-Type", "text/plain; charset=utf-8");
                rh.set("Content-Disposition", "attachment; filename=DailyFieldReport.txt");
                exchange.sendResponseHeaders(200, txtBytes.length);
                exchange.getResponseBody().write(txtBytes);

            } catch (Throwable genEx) {
                LOGGER.log(Level.SEVERE, "Error generating report", genEx);
                byte[] msg = "Server error generating report".getBytes(StandardCharsets.UTF_8);
                exchange.sendResponseHeaders(500, msg.length);
                exchange.getResponseBody().write(msg);
            } finally {
                // cleanup temp photos
                for (Path pth : tempPhotoPaths) {
                    try { Files.deleteIfExists(pth); } catch (Throwable ignored) {}
                }
                if (pdfOut != null) {
                    try { Files.deleteIfExists(pdfOut); } catch (Throwable ignored) {}
                }
                if (docxOut != null) {
                    try { Files.deleteIfExists(docxOut); } catch (Throwable ignored) {}
                }
                exchange.close();
            }
        }
    }

    private static String buildFullTextReport(Map<String, String> fields, List<String> photosBase64) {
        java.util.LinkedHashMap<String, String> sections = new java.util.LinkedHashMap<>();
        sections.put("1. General Info", buildGeneralInfoSection(fields));
        sections.put("2. Personnel", buildPersonnelSection(fields.getOrDefault("personnel", ""), fields.getOrDefault("personnelTotalHours", "0")));
        sections.put("3. Equipment", fields.getOrDefault("equipment", ""));
        sections.put("4. Work Performed", fields.getOrDefault("workPerformed", ""));
        sections.put("5. Materials", fields.getOrDefault("materials", ""));
        sections.put("6. Inspections", fields.getOrDefault("inspections", ""));
        StringBuilder safety = new StringBuilder();
        safety.append("Meeting Held: ").append(Boolean.parseBoolean(fields.getOrDefault("safetyMeeting", "false")) ? "Yes" : "No").append("\n");
        safety.append("Topic: ").append(fields.getOrDefault("safetyTopic", "")).append("\n");
        safety.append("Attendees: ").append(fields.getOrDefault("safetyAttendees", "")).append("\n");
        safety.append("Incidents/Near Misses: ").append(Boolean.parseBoolean(fields.getOrDefault("safetyIncident", "false")) ? "Yes" : "No").append("\n");
        sections.put("7. Safety", safety.toString());
        sections.put("8. Delays/Issues", fields.getOrDefault("delays", ""));
        StringBuilder coord = new StringBuilder();
        coord.append("Visitors: ").append(fields.getOrDefault("visitors", "")).append("\n");
        coord.append("Meetings: ").append(fields.getOrDefault("meetings", "")).append("\n");
        coord.append("Directions: ").append(fields.getOrDefault("directions", "")).append("\n");
        sections.put("9. Coordination", coord.toString());
        StringBuilder attach = new StringBuilder();
        if (photosBase64 != null && !photosBase64.isEmpty()) attach.append("Photos attached: ").append(photosBase64.size()).append("\n");
        sections.put("10. Attachments", attach.toString());

        StringBuilder text = new StringBuilder();
        for (Map.Entry<String,String> section : sections.entrySet()) {
            text.append(section.getKey()).append("\n");
            text.append(section.getValue()).append("\n\n");
        }
        return text.toString();
    }

    private static String buildGeneralInfoSection(Map<String, String> fields) {
        StringBuilder gen = new StringBuilder();
        gen.append("Report Name: ").append(fields.getOrDefault("reportName", "Daily Report")).append("\n");
        gen.append("Project Name: ").append(fields.getOrDefault("projectName", "")).append("\n");
        gen.append("Project No.: ").append(fields.getOrDefault("projectNo", "")).append("\n");
        gen.append("Location: ").append(fields.getOrDefault("location", "")).append("\n");
        gen.append("Date: ").append(fields.getOrDefault("date", "")).append("\n");
        gen.append("Weather: ").append(fields.getOrDefault("weather", "")).append("\n");
        gen.append("Temperature: ").append(fields.getOrDefault("temperature", "")).append("\n");
        gen.append("Wind: ").append(fields.getOrDefault("wind", "")).append("\n");
        return gen.toString();
    }

    private static String saveReportToDisk(Map<String, String> fields, String content) {
        String dateValue = fields.getOrDefault("date", java.time.LocalDate.now().toString());
        String reportName = fields.getOrDefault("reportName", "Daily Report");
        if (reportName == null || reportName.trim().isEmpty()) {
            reportName = "Daily Report";
        }

        String safeDate = dateValue.trim();
        if (safeDate.isEmpty()) {
            safeDate = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE);
        }
        String normalizedDate = safeDate.replace('-', '.');
        String cleanName = sanitizeFileToken(reportName);
        if (cleanName.isEmpty()) cleanName = "Daily Report";

        Path reportsDir = Path.of("reports");
        try {
            Files.createDirectories(reportsDir);
        } catch (IOException ioEx) {
            LOGGER.log(Level.WARNING, "Could not create reports directory", ioEx);
            reportsDir = Path.of(System.getProperty("java.io.tmpdir"));
        }

        int nextNo = nextReportNumber(reportsDir, normalizedDate, cleanName);
        String safeName = cleanName + " NO." + nextNo;
        String fileName = normalizedDate + "_" + safeName + ".txt";
        Path target = reportsDir.resolve(fileName);
        try {
            Files.writeString(target, content, StandardCharsets.UTF_8);
            return target.getFileName().toString();
        } catch (IOException ioEx) {
            LOGGER.log(Level.SEVERE, "Error saving report to disk", ioEx);
            return fileName;
        }
    }

    private static int nextReportNumber(Path reportsDir, String normalizedDate, String cleanName) {
        int[] highest = {0};
        try {
            if (Files.exists(reportsDir)) {
                try (var stream = Files.list(reportsDir)) {
                    stream.filter(path -> path.getFileName().toString().startsWith(normalizedDate + "_"))
                          .filter(path -> path.getFileName().toString().contains(sanitizeFileToken(cleanName)))
                          .forEach(path -> {
                              String fileName = path.getFileName().toString();
                              int marker = fileName.indexOf(" NO.");
                              if (marker > 0) {
                                  int start = marker + 4;
                                  int end = fileName.lastIndexOf('.');
                                  if (end > start) {
                                      try {
                                          int value = Integer.parseInt(fileName.substring(start, end));
                                          if (value > highest[0]) highest[0] = value;
                                      } catch (NumberFormatException ignored) {
                                      }
                                  }
                              }
                          });
                }
            }
        } catch (IOException ignored) {
        }
        return highest[0] + 1;
    }

    private static String sanitizeFileToken(String value) {
        if (value == null) return "Daily Report";
        String raw = value.trim();
        if (raw.isEmpty()) return "Daily Report";
        String cleaned = raw.replaceAll("[\\/:*?\"<>|]", "-");
        cleaned = cleaned.replaceAll("\\s+", " ").trim();
        if (cleaned.isEmpty()) return "Daily Report";
        return cleaned;
    }

    // Very small tolerant JSON parser for the specific expected payload shape:
    // { "fields": {"k":"v", ...}, "photosBase64": ["data:...,...", ...] }
    // This is intentionally permissive and not a full JSON implementation.
    private static Submission parseSubmission(String json) {
        Submission s = new Submission();
        s.fields = new HashMap<>();
        s.photosBase64 = new ArrayList<>();

        if (json == null || json.trim().isEmpty()) return s;

        // Extract the "fields" object
        int fieldsIndex = json.indexOf("\"fields\"");
        if (fieldsIndex >= 0) {
            int braceStart = json.indexOf('{', fieldsIndex);
            if (braceStart >= 0) {
                int braceEnd = findMatchingBrace(json, braceStart);
                if (braceEnd > braceStart) {
                    String obj = json.substring(braceStart + 1, braceEnd);
                    Map<String,String> map = parseSimpleJsonObject(obj);
                    s.fields.putAll(map);
                }
            }
        }

        // Extract the "photosBase64" array
        int photosIndex = json.indexOf("\"photosBase64\"");
        if (photosIndex >= 0) {
            int arrayStart = json.indexOf('[', photosIndex);
            if (arrayStart >= 0) {
                int arrayEnd = json.indexOf(']', arrayStart);
                if (arrayEnd > arrayStart) {
                    String arr = json.substring(arrayStart + 1, arrayEnd);
                    List<String> items = parseJsonStringArray(arr);
                    s.photosBase64.addAll(items);
                }
            }
        }

        return s;
    }

    private static int findMatchingBrace(String s, int start) {
        int depth = 0;
        for (int i = start; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '{') depth++;
            else if (c == '}') {
                depth--;
                if (depth == 0) return i;
            }
        }
        return -1;
    }

    private static Map<String,String> parseSimpleJsonObject(String obj) {
        Map<String,String> map = new HashMap<>();
        // Split on commas that are not inside quotes
        List<String> parts = splitTopLevel(obj, ',');
        for (String p : parts) {
            int colon = p.indexOf(':');
            if (colon < 0) continue;
            String key = unquote(p.substring(0, colon).trim());
            String val = unquote(p.substring(colon + 1).trim());
            if (key != null) map.put(key, val == null ? "" : val);
        }
        return map;
    }

    private static String buildPersonnelSection(String personnelData, String totalHoursValue) {
        StringBuilder out = new StringBuilder();
        out.append("Company | Trade / Role | No. of Workers | Hours Worked | Foreman / Supervisor\n");
        double totalHours = 0.0;

        if (personnelData != null && !personnelData.trim().isEmpty()) {
            String[] lines = personnelData.split("\n");
            for (String line : lines) {
                String trimmed = line.trim();
                if (trimmed.isEmpty()) continue;
                out.append(trimmed).append("\n");
                String[] parts = trimmed.split("\\|");
                if (parts.length > 3) {
                    String hours = parts[3].trim();
                    try {
                        totalHours += Double.parseDouble(hours);
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
        }

        if (totalHoursValue != null && !totalHoursValue.trim().isEmpty()) {
            try {
                totalHours = Double.parseDouble(totalHoursValue.trim());
            } catch (NumberFormatException ignored) {
            }
        }

        out.append("\nTotal Hours: ").append(String.format(java.util.Locale.US, "%.2f", totalHours)).append("\n");
        return out.toString();
    }

    private static List<String> parseJsonStringArray(String arr) {
        List<String> out = new ArrayList<>();
        List<String> parts = splitTopLevel(arr, ',');
        for (String p : parts) {
            String v = unquote(p.trim());
            if (v != null && !v.isEmpty()) out.add(v);
        }
        return out;
    }

    // Split a string by a delimiter, ignoring delimiters inside double quotes
    private static List<String> splitTopLevel(String s, char delim) {
        List<String> parts = new ArrayList<>();
        if (s == null) return parts;
        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '"') {
                // handle escaped quotes
                boolean escaped = i > 0 && s.charAt(i - 1) == '\\';
                if (!escaped) inQuotes = !inQuotes;
            }
            if (c == delim && !inQuotes) {
                parts.add(cur.toString());
                cur.setLength(0);
            } else {
                cur.append(c);
            }
        }
        if (cur.length() > 0) parts.add(cur.toString());
        return parts;
    }

    private static String unquote(String s) {
        if (s == null) return null;
        s = s.trim();
        if (s.startsWith("\"") && s.endsWith("\"") && s.length() >= 2) {
            String inner = s.substring(1, s.length() - 1);
            // unescape simple sequences
            inner = inner.replaceAll("\\\\\"", "\"");
            inner = inner.replaceAll("\\\\n", "\n");
            inner = inner.replaceAll("\\\\r", "\r");
            inner = inner.replaceAll("\\\\t", "\t");
            return inner;
        }
        // also accept bare words
        if (s.equals("null")) return null;
        if (s.startsWith("\"")) return s.substring(1);
        if (s.endsWith("\"")) return s.substring(0, s.length()-1);
        return s;
    }
}
