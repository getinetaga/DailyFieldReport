# Daily Field Report

A Java web application for creating and exporting daily field reports. The project includes a browser form, embedded Java HTTP server, and export flows for PDF, plain text, and Word documents.

## Features
- Embedded web UI for daily report entry
- PDF export with section headers, images, and photo captions
- Plain text export fallback
- Word document export
- Personnel totals and professional report layout
- Local Java HTTP server, no external framework required

## Project layout
- `src/main/java/com/example/dailyfieldreport` — server, report generation, and exporters
- `src/main/resources/web` — embedded web UI and styling
- `src/test/java/com/example/dailyfieldreport` — automated tests

## Prerequisites
- Java 17 or newer
- Optional: Maven for packaging and dependency management

## Run the app
From the project root:

```bash
javac -cp "target/lib/*" -d target/classes $(find src/main/java -name "*.java" | tr '\n' ' ')
java -cp "target/classes;target/lib/*" com.example.dailyfieldreport.WebServer
```

If the project is built with Maven, you can also run the app through the standard Java entry point defined in the project.

## Open the web UI
Open:

1) With Maven (exec plugin configured in pom.xml):

   mvn -DskipTests exec:java -Dexec.mainClass=com.example.dailyfieldreport.WebServer

2) Or run the compiled classes (after `mvn package`) using the dependency jars copied into target/dependency:

   mvn -DskipTests dependency:copy-dependencies
   java -cp target/classes;target/dependency/* com.example.dailyfieldreport.WebServer

Open the web UI

- Navigate to http://localhost:9090 in Chrome (or your browser). Fill the form and choose export format PDF or Plain Text.
- Use the "Preview & Edit" button to open the report in a dedicated editing page, then download the final PDF from there.
- Saved reports are stored in the `reports/` folder using the pattern `YYYY.MM.DD_Report Name NO.n.txt` (for example, `2026.08.20_Daily Report NO.1.txt`).

Manual test — request a PDF via curl (example)

Save this JSON to payload.json (adjust fields as needed):

{
  "fields": {
    "projectName": "Test Project",
    "projectNo": "123",
    "location": "Site A",
    "date": "2026-04-01",
    "weather": "Sunny",
    "temperature": "AM 55 / PM 65",
    "wind": "Light",
    "workPerformed": "Installed foundations",
    "personnel": "Acme | Carpentry | 5 | 8 | John Doe",
    "equipment": "Excavator | CAT 320 | 1 | Y |",
    "materials": "Concrete | Supplier X | 10 m3 | Storage Yard | Inspected",
    "inspections": "Soil compaction: Pass",
    "delays": "Delivery late 2h",
    "format": "pdf"
  },
  "photosBase64": []
}

Then run (curl example):

curl -X POST -H "Content-Type: application/json" --data-binary @payload.json "http://localhost:9090/submit" --output DailyFieldReport.pdf

If the server is running with PDFBox and POI available on the classpath, the response will be a PDF (`Content-Type: application/pdf`) and saved to DailyFieldReport.pdf. If libraries are not available, server will return a plain-text fallback file.

Manual test — PowerShell example

$payload = Get-Content payload.json -Raw
Invoke-RestMethod -Uri 'http://localhost:9090/submit' -Method POST -ContentType 'application/json' -Body $payload -OutFile DailyFieldReport.pdf

Notes when PDF doesn't generate

- If the server returns a text file, ensure you built and ran the server with Maven (so `pdfbox` is on the runtime classpath). Use the `dependency:copy-dependencies` approach and run with `java -cp target/classes;target/dependency/* ...`.

If you'd like, I can:
- Try to run `mvn package` and `mvn test` here if you install Maven in this environment or provide access, or
- Continue improving the PDF layout (embedding the UI snapshot as the first page and photos following), or
- Add server-side parsing to translate the personnel/equipment textareas into table-formatted sections in the PDF.

## Export formats
- PDF
- TXT
- DOCX / Word document

## Notes
This branch aligns the current app work with the repo’s `main` branch while keeping the embedded report-export flow and polished UI in place.
