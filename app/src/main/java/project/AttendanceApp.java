package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;

public class AttendanceApp {
    public static void main(String[] args) {
        // Set FlatLaf Theme
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
        } catch (Exception ex) {
            System.out.println("Failed to initialize FlatLaf");
        }

        JFrame frame = new JFrame("QR Code Attendance System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);
        frame.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Scan QR Code to Mark Attendance", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JTextField studentIdField = new JTextField(); // Text field for student ID
        JLabel resultLabel = new JLabel("Waiting for scan...", JLabel.CENTER);
        JButton scanButton = new JButton("Scan QR Code");

        scanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String qrData = QrScanner.scanQRCode(); // Get Subject ID from QR
                if (!qrData.equals("QR Code not detected!")) {
                    int studentId = Integer.parseInt(studentIdField.getText()); // Get student ID from text field
                    markAttendance(qrData, studentId); // Use the input student ID
                    resultLabel.setText("Attendance Marked for Subject ID: " + qrData);
                } else {
                    resultLabel.setText("QR Code not detected!");
                }
            }
        });

        frame.add(titleLabel, BorderLayout.NORTH);
        frame.add(studentIdField, BorderLayout.CENTER); // Add text field to the center
        frame.add(scanButton, BorderLayout.SOUTH);
        frame.add(resultLabel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    // Function to send attendance data to backend
    public static void markAttendance(String subjectId, int studentId) {
        try {
            URL url = new URL("http://localhost:8080/mark-attendance");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            String postData = "student_id=" + studentId + "&subject_id=" + subjectId;
            OutputStream os = conn.getOutputStream();
            os.write(postData.getBytes());
            os.flush();
            os.close();

            conn.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
