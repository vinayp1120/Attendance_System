package project;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import javax.swing.*;
import java.sql.*;

public class AttendanceChart {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Attendance Report");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db", "root", "");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS attended FROM attendance WHERE student_id = 1");

            int attended = 0;
            int totalClasses = 20; // Assume total conducted classes

            if (rs.next()) attended = rs.getInt("attended");

            dataset.setValue("Attended", attended);
            dataset.setValue("Missed", totalClasses - attended);

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFreeChart chart = ChartFactory.createPieChart("Attendance Report", dataset, true, true, false);
        frame.add(new ChartPanel(chart));
        frame.setVisible(true);
    }
}
