# Daily Construction Field Report

A comprehensive web-based application for creating daily construction field reports. This tool helps construction inspectors, supervisors, and project managers document daily activities, track progress, and maintain accurate records of construction projects.

## Features

### Comprehensive Report Sections
- **Basic Information**: Date, time, project details, and inspector information
- **Weather Conditions**: Temperature and weather tracking
- **Work Performed**: Detailed description of daily activities with start/end times
- **Labor & Equipment**: Track workforce and equipment on site
- **Materials**: Document materials delivered and used
- **Issues & Delays**: Record problems, delays, and safety observations
- **Quality & Inspections**: Document inspections and quality concerns
- **Additional Notes**: Space for any other relevant information
- **Digital Signature**: Sign and date reports

### Key Features
- ✅ **Required Field Validation**: Ensures critical information is captured
- 👁️ **Live Preview**: Preview reports before submission
- 🖨️ **Print Support**: Print-friendly format for physical copies
- 💾 **Auto-save Draft**: Automatically saves work in progress
- 📱 **Responsive Design**: Works on desktop, tablet, and mobile devices
- 🎨 **Modern UI**: Clean, professional interface with gradient styling
- 💿 **Local Storage**: Saves submitted reports locally (can be extended to server)

## Getting Started

### Prerequisites
- A modern web browser (Chrome, Firefox, Safari, or Edge)
- No installation or server required!

### Usage

1. **Open the Application**
   ```bash
   # Simply open index.html in your web browser
   # On most systems, you can double-click the file
   # Or use a command like:
   open index.html        # macOS
   start index.html       # Windows
   xdg-open index.html    # Linux
   ```

2. **Fill Out the Report**
   - Complete all required fields (marked with *)
   - Fill in relevant sections for the day's activities
   - Use the auto-save feature to prevent data loss

3. **Preview Your Report**
   - Click "Preview Report" to review your entries
   - Make any necessary corrections

4. **Submit the Report**
   - Click "Submit Report" to save
   - Reports are stored locally in your browser
   - Option to create a new report after submission

5. **Print or Export**
   - Use the "Print Report" button from the preview
   - Browser print dialog allows saving as PDF

## Form Sections Explained

### Basic Information
Required fields to identify the project and report:
- Report date and time
- Project name and location
- Contractor and inspector names

### Weather Conditions
Important for construction scheduling and quality control:
- Temperature (°F)
- Weather conditions (clear, cloudy, rain, snow, etc.)

### Work Performed Today
The heart of the report:
- Detailed description of all work completed
- Start and end times
- Progress made

### Labor & Equipment
Track resources on site:
- Number of workers and supervisors
- Equipment used
- Subcontractors present

### Materials
Document material flow:
- Materials delivered to site
- Materials used/installed
- Quantities and specifications

### Issues, Delays & Observations
Critical for project management:
- Problems encountered
- Delays and their causes
- Safety observations and incidents

### Quality & Inspections
Quality assurance documentation:
- Inspections performed
- Results and findings
- Quality issues or defects

### Additional Notes
Any other relevant information not covered above

### Signature
Digital signature to authenticate the report:
- Inspector's typed name
- Date of signature

## Technical Details

### Technology Stack
- **HTML5**: Semantic markup for accessibility
- **CSS3**: Modern styling with gradients and animations
- **JavaScript (Vanilla)**: No frameworks required, pure JavaScript for functionality

### Browser Compatibility
- Chrome/Edge (latest)
- Firefox (latest)
- Safari (latest)
- Mobile browsers (iOS Safari, Chrome Mobile)

### Data Storage
- Reports are stored in browser's localStorage
- Draft auto-save prevents data loss
- Can be extended to connect to a backend server/database

## Customization

### Adding Fields
To add new fields to the form:
1. Edit `index.html` to add the HTML input/textarea
2. Update `script.js` `displayPreview()` function to include the field in preview
3. Optionally update `styles.css` for custom styling

### Changing Colors
Main color scheme is defined in `styles.css`:
- Primary gradient: `#667eea` to `#764ba2`
- Success color: `#27ae60`
- Error color: `#e74c3c`

### Backend Integration
To save reports to a server:
1. Modify the `saveReport()` function in `script.js`
2. Add an API endpoint to receive form data
3. Replace localStorage with API calls

## Screenshots

The application features:
- Clean, modern interface with gradient header
- Organized sections for easy data entry
- Responsive design for all devices
- Professional preview and print layouts

## Future Enhancements

Potential improvements:
- PDF export functionality
- Photo upload capability
- Drawing/markup tools for site plans
- Email report functionality
- Cloud storage integration
- Multi-user support with authentication
- Report history and search
- Export to Excel/CSV

## License

This project is open source and available for use in construction projects.

## Support

For issues or questions, please open an issue on the GitHub repository.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.