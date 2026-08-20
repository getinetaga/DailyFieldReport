DailyFieldReport — build & test instructions

What I changed
- Web UI expanded to include personnel/equipment/materials/inspections/delays.
- Embedded web server (`WebServer`) now prefers PDF export when run with PDFBox available; otherwise it returns a plain-text attachment.
- `ReportExporter` contains PDF/Word exporters that use PDFBox/POI when those libraries are present on the classpath.
- Placeholder logo generation in Swing app when `/logo.png` is missing.

Prerequisites (local)
- Java 17 or newer installed and on PATH.
- Maven installed (recommended) to download dependencies and run tests.

Build (Maven)
1. From the project root run:

   mvn -DskipTests package

   This will download dependencies (pdfbox, poi, gson, junit) and compile the project.

Run tests

   mvn test

Run the embedded web server (recommended via Maven so dependencies are on classpath)

1) With Maven (exec plugin configured in pom.xml):

   mvn -DskipTests exec:java -Dexec.mainClass=com.example.dailyfieldreport.WebServer

2) Or run the compiled classes (after `mvn package`) using the dependency jars copied into target/dependency:

   mvn -DskipTests dependency:copy-dependencies
   java -cp target/classes;target/dependency/* com.example.dailyfieldreport.WebServer

Open the web UI

- Navigate to http://localhost:8080 in Chrome (or your browser). Fill the form and choose export format PDF or Plain Text.
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

curl -X POST -H "Content-Type: application/json" --data-binary @payload.json "http://localhost:8080/submit" --output DailyFieldReport.pdf

If the server is running with PDFBox and POI available on the classpath, the response will be a PDF (`Content-Type: application/pdf`) and saved to DailyFieldReport.pdf. If libraries are not available, server will return a plain-text fallback file.

Manual test — PowerShell example

$payload = Get-Content payload.json -Raw
Invoke-RestMethod -Uri 'http://localhost:8080/submit' -Method POST -ContentType 'application/json' -Body $payload -OutFile DailyFieldReport.pdf

Notes when PDF doesn't generate

- If the server returns a text file, ensure you built and ran the server with Maven (so `pdfbox` is on the runtime classpath). Use the `dependency:copy-dependencies` approach and run with `java -cp target/classes;target/dependency/* ...`.

If you'd like, I can:
- Try to run `mvn package` and `mvn test` here if you install Maven in this environment or provide access, or
- Continue improving the PDF layout (embedding the UI snapshot as the first page and photos following), or
- Add server-side parsing to translate the personnel/equipment textareas into table-formatted sections in the PDF.


