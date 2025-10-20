// Set default dates to today
document.addEventListener('DOMContentLoaded', function() {
    const today = new Date();
    const dateString = today.toISOString().split('T')[0];
    const timeString = today.toTimeString().slice(0, 5);
    
    document.getElementById('reportDate').value = dateString;
    document.getElementById('reportTime').value = timeString;
    document.getElementById('signatureDate').value = dateString;
});

// Form submission handler
document.getElementById('fieldReportForm').addEventListener('submit', function(e) {
    e.preventDefault();
    
    if (validateForm()) {
        // Collect form data
        const formData = collectFormData();
        
        // In a real application, you would send this data to a server
        console.log('Form Data:', formData);
        
        // Save to localStorage for demo purposes
        saveReport(formData);
        
        // Show success message
        showSuccessMessage();
        
        // Optionally reset form after a delay
        setTimeout(() => {
            if (confirm('Report submitted successfully! Would you like to create a new report?')) {
                this.reset();
                // Reset dates to today
                const today = new Date();
                const dateString = today.toISOString().split('T')[0];
                const timeString = today.toTimeString().slice(0, 5);
                document.getElementById('reportDate').value = dateString;
                document.getElementById('reportTime').value = timeString;
                document.getElementById('signatureDate').value = dateString;
            }
        }, 2000);
    }
});

// Form validation
function validateForm() {
    const requiredFields = document.querySelectorAll('[required]');
    let isValid = true;
    
    requiredFields.forEach(field => {
        if (!field.value.trim()) {
            field.style.borderColor = '#e74c3c';
            isValid = false;
        } else {
            field.style.borderColor = '#ddd';
        }
    });
    
    if (!isValid) {
        alert('Please fill in all required fields marked with *');
    }
    
    return isValid;
}

// Collect form data
function collectFormData() {
    const form = document.getElementById('fieldReportForm');
    const formData = new FormData(form);
    const data = {};
    
    for (let [key, value] of formData.entries()) {
        data[key] = value;
    }
    
    return data;
}

// Save report to localStorage
function saveReport(data) {
    const reports = JSON.parse(localStorage.getItem('dailyReports') || '[]');
    data.id = Date.now();
    data.submittedAt = new Date().toISOString();
    reports.push(data);
    localStorage.setItem('dailyReports', JSON.stringify(reports));
}

// Show success message
function showSuccessMessage() {
    const successMessage = document.getElementById('successMessage');
    successMessage.style.display = 'block';
    
    setTimeout(() => {
        successMessage.style.display = 'none';
    }, 3000);
}

// Preview button handler
document.getElementById('previewBtn').addEventListener('click', function() {
    if (validateForm()) {
        const formData = collectFormData();
        displayPreview(formData);
        document.getElementById('previewModal').style.display = 'block';
    }
});

// Display preview
function displayPreview(data) {
    const previewContent = document.getElementById('previewContent');
    let html = '';
    
    // Basic Information
    html += '<h3>Basic Information</h3>';
    html += `<p><strong>Date:</strong> ${data.reportDate || 'N/A'}</p>`;
    html += `<p><strong>Time:</strong> ${data.reportTime || 'N/A'}</p>`;
    html += `<p><strong>Project Name:</strong> ${data.projectName || 'N/A'}</p>`;
    html += `<p><strong>Project Location:</strong> ${data.projectLocation || 'N/A'}</p>`;
    html += `<p><strong>General Contractor:</strong> ${data.contractor || 'N/A'}</p>`;
    html += `<p><strong>Inspector Name:</strong> ${data.inspector || 'N/A'}</p>`;
    
    // Weather Conditions
    html += '<h3>Weather Conditions</h3>';
    html += `<p><strong>Temperature:</strong> ${data.temperature ? data.temperature + '°F' : 'N/A'}</p>`;
    html += `<p><strong>Conditions:</strong> ${data.weatherCondition || 'N/A'}</p>`;
    
    // Work Performed
    html += '<h3>Work Performed Today</h3>';
    html += `<p><strong>Description:</strong> ${data.workDescription || 'N/A'}</p>`;
    html += `<p><strong>Start Time:</strong> ${data.workStartTime || 'N/A'}</p>`;
    html += `<p><strong>End Time:</strong> ${data.workEndTime || 'N/A'}</p>`;
    
    // Labor & Equipment
    html += '<h3>Labor & Equipment</h3>';
    html += `<p><strong>Number of Workers:</strong> ${data.numWorkers || '0'}</p>`;
    html += `<p><strong>Number of Supervisors:</strong> ${data.numSupervisors || '0'}</p>`;
    html += `<p><strong>Equipment on Site:</strong> ${data.equipment || 'N/A'}</p>`;
    html += `<p><strong>Subcontractors Present:</strong> ${data.subcontractors || 'N/A'}</p>`;
    
    // Materials
    html += '<h3>Materials</h3>';
    html += `<p><strong>Materials Delivered:</strong> ${data.materialsDelivered || 'N/A'}</p>`;
    html += `<p><strong>Materials Used:</strong> ${data.materialsUsed || 'N/A'}</p>`;
    
    // Issues & Delays
    html += '<h3>Issues, Delays & Observations</h3>';
    html += `<p><strong>Issues/Problems:</strong> ${data.issues || 'None reported'}</p>`;
    html += `<p><strong>Delays:</strong> ${data.delays || 'None reported'}</p>`;
    html += `<p><strong>Safety Observations:</strong> ${data.safetyObservations || 'None reported'}</p>`;
    
    // Quality & Inspections
    html += '<h3>Quality & Inspections</h3>';
    html += `<p><strong>Inspections Performed:</strong> ${data.inspections || 'N/A'}</p>`;
    html += `<p><strong>Quality Issues:</strong> ${data.qualityIssues || 'None reported'}</p>`;
    
    // Additional Notes
    html += '<h3>Additional Notes</h3>';
    html += `<p>${data.additionalNotes || 'No additional notes'}</p>`;
    
    // Signature
    html += '<h3>Signature</h3>';
    html += `<p><strong>Signed by:</strong> ${data.signatureName || 'N/A'}</p>`;
    html += `<p><strong>Date:</strong> ${data.signatureDate || 'N/A'}</p>`;
    
    previewContent.innerHTML = html;
}

// Close modal handlers
document.querySelector('.close').addEventListener('click', function() {
    document.getElementById('previewModal').style.display = 'none';
});

document.getElementById('closePreviewBtn').addEventListener('click', function() {
    document.getElementById('previewModal').style.display = 'none';
});

// Close modal when clicking outside
window.addEventListener('click', function(event) {
    const modal = document.getElementById('previewModal');
    if (event.target === modal) {
        modal.style.display = 'none';
    }
});

// Print button handler
document.getElementById('printBtn').addEventListener('click', function() {
    window.print();
});

// Real-time field validation
document.querySelectorAll('input[required], textarea[required]').forEach(field => {
    field.addEventListener('blur', function() {
        if (!this.value.trim()) {
            this.style.borderColor = '#e74c3c';
        } else {
            this.style.borderColor = '#27ae60';
        }
    });
    
    field.addEventListener('input', function() {
        if (this.value.trim()) {
            this.style.borderColor = '#27ae60';
        }
    });
});

// Auto-save draft functionality (optional)
let autoSaveTimer;
document.getElementById('fieldReportForm').addEventListener('input', function() {
    clearTimeout(autoSaveTimer);
    autoSaveTimer = setTimeout(() => {
        const formData = collectFormData();
        localStorage.setItem('draftReport', JSON.stringify(formData));
        console.log('Draft auto-saved');
    }, 2000);
});

// Load draft on page load if exists
window.addEventListener('load', function() {
    const draft = localStorage.getItem('draftReport');
    if (draft) {
        const shouldLoadDraft = confirm('A draft report was found. Would you like to load it?');
        if (shouldLoadDraft) {
            const draftData = JSON.parse(draft);
            loadFormData(draftData);
        } else {
            localStorage.removeItem('draftReport');
        }
    }
});

// Load data into form
function loadFormData(data) {
    Object.keys(data).forEach(key => {
        const field = document.getElementById(key);
        if (field) {
            field.value = data[key];
        }
    });
}
