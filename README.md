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

```text
http://localhost:9090
```

## Export formats
- PDF
- TXT
- DOCX / Word document

## Notes
This branch aligns the current app work with the repo’s `main` branch while keeping the embedded report-export flow and polished UI in place.
