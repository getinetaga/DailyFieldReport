# Daily Field Report Application

A Java console application for managing daily field reports. This application allows users to create, view, and manage field reports for construction projects, maintenance work, or any field-based activities.

## Features

- ✅ Create new field reports with detailed information
- ✅ View all existing reports
- ✅ Search reports by date
- ✅ In-memory storage of reports during application runtime
- ✅ Data validation for required fields
- ✅ User-friendly console interface

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── fieldreport/
│   │           ├── DailyFieldReportApplication.java    # Main application class
│   │           ├── model/
│   │           │   └── FieldReport.java                # Data model for field reports
│   │           └── service/
│   │               └── FieldReportService.java         # Service layer for CRUD operations
│   └── resources/                                      # Resource files
└── test/
    └── java/
        └── com/
            └── fieldreport/
                └── model/
                    └── FieldReportTest.java             # Unit tests for FieldReport model
```

## Requirements

- Java 17 or higher (currently configured for Java 23)
- Maven (for dependency management and building with full features)

## Quick Start (Manual Compilation)

1. **Compile the application:**
   ```bash
   # Create the target directory
   mkdir -p target/classes
   
   # Compile all Java files
   javac -d target/classes src/main/java/com/fieldreport/model/*.java
   javac -cp target/classes -d target/classes src/main/java/com/fieldreport/service/*.java
   javac -cp target/classes -d target/classes src/main/java/com/fieldreport/*.java
   ```

2. **Run the application:**
   ```bash
   java -cp target/classes com.fieldreport.DailyFieldReportApplication
   ```

## Using Maven (Recommended)

If you have Maven installed, you can use the full-featured version with JSON persistence and logging:

1. **Build the project:**
   ```bash
   mvn clean compile
   ```

2. **Run the application:**
   ```bash
   mvn exec:java -Dexec.mainClass="com.fieldreport.DailyFieldReportApplication"
   ```

3. **Run tests:**
   ```bash
   mvn test
   ```

## How to Use

1. **Start the application** - You'll see a welcome message and main menu
2. **Choose an option:**
   - **1**: Create a new field report
   - **2**: View all existing reports
   - **3**: View reports by specific date
   - **4**: Exit the application

3. **Creating a report**: Enter the required information:
   - Reporter name
   - Location
   - Project name
   - Weather conditions
   - Work description
   - Notes (optional)

## Field Report Data Structure

Each field report contains:
- **ID**: Unique identifier (auto-generated)
- **Date**: Report date (defaults to current date)
- **Created At**: Timestamp when report was created
- **Reporter Name**: Name of the person filing the report
- **Location**: Work site location
- **Project Name**: Name of the project
- **Weather Conditions**: Weather description
- **Work Description**: Detailed description of work performed
- **Notes**: Additional notes or issues (optional)

## Example Usage

```
=== Daily Field Report Application ===
Welcome to the Daily Field Report System!

--- Main Menu ---
1. Create New Field Report
2. View All Reports
3. View Report by Date
4. Exit
Please enter your choice (1-4): 1

--- Create New Field Report ---
Enter your name: John Doe
Enter location: Construction Site A
Enter project name: Building Renovation
Enter weather conditions: Sunny, 22°C
Enter work description: Completed foundation inspection and started framing work
Enter any issues or notes: Minor delay due to material delivery
✓ Field report created successfully!
Report ID: a1b2c3d4-e5f6-7890-abcd-ef1234567890
```

## Development

### Adding New Features

- **Models**: Add new data models in `src/main/java/com/fieldreport/model/`
- **Services**: Add business logic in `src/main/java/com/fieldreport/service/`
- **Tests**: Add unit tests in `src/test/java/com/fieldreport/`

### Testing

Run the included unit tests:
```bash
javac -cp ".:path/to/junit-jupiter-*.jar" -d target/test-classes src/test/java/com/fieldreport/model/*.java
java -cp "target/classes:target/test-classes:path/to/junit-jupiter-*.jar" org.junit.jupiter.launcher.core.Launcher
```

## Future Enhancements

- [ ] Persistent storage (JSON/Database)
- [ ] Export reports to PDF/CSV
- [ ] Search and filter functionality
- [ ] Report templates
- [ ] Photo attachments
- [ ] Multi-user support
- [ ] Web interface

## License

This project is open source and available under the MIT License.