//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import javax.swing.*;
import java.awt.*;

public class DailyFieldReport {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DailyFieldReport().createAndShowGUI());
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Daily Field Report");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 700);
        frame.setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.add("1. General Info", createGeneralInfoPanel());
        tabbedPane.add("2. Personnel", createPersonnelPanel());
        tabbedPane.add("3. Equipment", createEquipmentPanel());
        tabbedPane.add("4. Work Performed", createWorkPerformedPanel());
        tabbedPane.add("5. Materials", createMaterialsPanel());
        tabbedPane.add("6. Inspections", createInspectionsPanel());
        tabbedPane.add("7. Safety", createSafetyPanel());
        tabbedPane.add("8. Delays/Issues", createDelaysPanel());
        tabbedPane.add("9. Coordination", createCoordinationPanel());
        tabbedPane.add("10. Attachments", createAttachmentsPanel());

        frame.add(tabbedPane);
        frame.setVisible(true);
    }

    private JPanel createGeneralInfoPanel() {
        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));

        panel.add(new JLabel("Project Name:"));
        panel.add(new JTextField());

        panel.add(new JLabel("Project No.:"));
        panel.add(new JTextField());

        panel.add(new JLabel("Location:"));
        panel.add(new JTextField());

        panel.add(new JLabel("Date:"));
        panel.add(new JTextField());

        panel.add(new JLabel("Weather AM / PM:"));
        panel.add(new JTextField());

        panel.add(new JLabel("Temperature AM / PM:"));
        panel.add(new JTextField());

        panel.add(new JLabel("Wind:"));
        panel.add(new JTextField());

        return panel;
    }

    private JPanel createPersonnelPanel() {
        String[] columns = {"Company", "Trade / Role", "No. of Workers", "Hours Worked", "Foreman / Supervisor"};
        JTable table = new JTable(5, columns.length);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(new JLabel("Total Workers On Site: __________"), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createEquipmentPanel() {
        String[] columns = {"Equipment", "Type / Size", "Quantity", "Operating (Y/N)", "Idle Reason"};
        JTable table = new JTable(5, columns.length);
        return wrapTableWithLabel(table);
    }

    private JPanel createWorkPerformedPanel() {
        JTextArea area = new JTextArea(10, 70);
        return wrapTextAreaWithLabel(area, "Describe work activities, including locations, quantities, and methods used:");
    }

    private JPanel createMaterialsPanel() {
        String[] columns = {"Material", "Supplier", "Quantity", "Location Used / Stored", "Inspection Status"};
        JTable table = new JTable(5, columns.length);
        return wrapTableWithLabel(table);
    }

    private JPanel createInspectionsPanel() {
        String[] columns = {"Inspection / Test", "Inspector / Agency", "Result", "Remarks"};
        JTable table = new JTable(5, columns.length);
        return wrapTableWithLabel(table);
    }

    private JPanel createSafetyPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 1));

        panel.add(new JCheckBox("Safety Meeting Held"));
        panel.add(new JLabel("Topic:"));
        panel.add(new JTextField());
        panel.add(new JLabel("Attendees:"));
        panel.add(new JTextField());
        panel.add(new JCheckBox("Incidents / Near Misses"));

        return panel;
    }

    private JPanel createDelaysPanel() {
        String[] columns = {"Description", "Impact (Schedule / Cost / Safety)", "Corrective Action / Remarks"};
        JTable table = new JTable(5, columns.length);
        return wrapTableWithLabel(table);
    }

    private JPanel createCoordinationPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 1));
        panel.add(new JLabel("Visitors (Owner, CM, Inspector, Others):"));
        panel.add(new JTextField());
        panel.add(new JLabel("Meetings / Coordination Notes:"));
        panel.add(new JTextField());
        panel.add(new JLabel("Directions / Instructions Received:"));
        panel.add(new JTextField());
        return panel;
    }

    private JPanel createAttachmentsPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 1));
        panel.add(new JCheckBox("Photos attached"));
        panel.add(new JCheckBox("Test results attached"));
        panel.add(new JCheckBox("Drawings / Sketches attached"));
        return panel;
    }

    private JPanel wrapTableWithLabel(JTable table) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel wrapTextAreaWithLabel(JTextArea area, String label) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(label), BorderLayout.NORTH);
        panel.add(new JScrollPane(area), BorderLayout.CENTER);
        return panel;
    }
}
