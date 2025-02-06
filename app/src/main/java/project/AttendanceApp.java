package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class AttendanceApp {
    private static final Map<String, String[]> SUBJECTS = new HashMap<>();
    
    static {
        SUBJECTS.put("101", new String[]{ "c++","A"});
        SUBJECTS.put("102", new String[]{"C", "B"});
        SUBJECTS.put("103", new String[]{"Java", "Surendra"});
        SUBJECTS.put("104", new String[]{"Big Data", "Venkatsir"});
        SUBJECTS.put("105", new String[]{"Deep Learning", "Sundar"});
    }

    public static void main(String[] args) {
        // Apply FlatLaf Dark Theme
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
        } catch (Exception ex) {
            System.out.println("Failed to initialize FlatLaf");
        }

        JFrame frame = new JFrame("QR Code Attendance System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 450);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        // Panel with custom background
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, Color.decode("#4A00E0"), getWidth(), getHeight(), Color.decode("#8E2DE2"));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("QR Code Attendance System", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);

        JLabel studentIdLabel = new JLabel("Enter Roll Number:");
        studentIdLabel.setFont(new Font("Arial", Font.BOLD, 14));
        studentIdLabel.setForeground(Color.WHITE);

        JTextField studentIdField = new JTextField(15);
        studentIdField.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton scanButton = new JButton("Scan QR Code");
        scanButton.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
        scanButton.setPreferredSize(new Dimension(200, 50));
        scanButton.setBackground(new Color(255, 69, 0));
        scanButton.setForeground(Color.WHITE);
        scanButton.setFocusPainted(false);

        JLabel resultLabel = new JLabel("Waiting for scan...", JLabel.CENTER);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 14));
        resultLabel.setForeground(Color.YELLOW);

        JLabel subjectLabel = new JLabel("", JLabel.CENTER);
        subjectLabel.setFont(new Font("Arial", Font.BOLD, 14));
        subjectLabel.setForeground(Color.CYAN);

        // Action for scanning QR Code
        scanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(() -> {
                    String studentId = studentIdField.getText().trim();
                    if (studentId.isEmpty()) {
                        resultLabel.setText("⚠ Enter your Roll Number first!");
                        subjectLabel.setText("");
                        return;
                    }

                    String qrData = QrScanner.scanQRCode(); // Scan for Subject ID
                    System.out.println("Scanned QR Data: " + qrData); // Debug log
                    if (!qrData.equals("QR Code not detected!")) {
                        System.out.println("Checking against SUBJECTS map..."); // Debug log
                        if (SUBJECTS.containsKey(qrData)) {
                            String subjectName = SUBJECTS.get(qrData)[0];
                            String facultyName = SUBJECTS.get(qrData)[1];

                            markAttendance(qrData, Integer.parseInt(studentId));
                            resultLabel.setText("Attendance Marked for Student ID: " + studentId); 
                            subjectLabel.setText("Subject: " + subjectName + " | Faculty: " + facultyName); 

                        } else {
                            resultLabel.setText("Invalid Subject ID!");
                            subjectLabel.setText("");
                        }
                    } else {
                        resultLabel.setText("QR Code not detected!");
                        subjectLabel.setText("");
                    }
                }).start();
            }
        });

        // Layout alignment
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(titleLabel, gbc);

        gbc.gridy = 1;
        panel.add(studentIdLabel, gbc);

        gbc.gridy = 2;
        panel.add(studentIdField, gbc);

        gbc.gridy = 3;
        panel.add(scanButton, gbc);

        gbc.gridy = 4;
        panel.add(resultLabel, gbc);

        gbc.gridy = 5;
        panel.add(subjectLabel, gbc);

        frame.add(panel);
        frame.setVisible(true);

        // Start webcam display
        QrScanner.displayWebcam(frame);
    }

    // Function to send attendance data to backend
    public static void markAttendance(String subjectId, int studentId) {
        try {
            URL url = new URL("http://localhost:8080/attendance/mark-attendance");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            String postData = "student_id=" + studentId + "&subject_id=" + subjectId;
            OutputStream os = conn.getOutputStream();
            os.write(postData.getBytes());
            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                System.out.println("Error marking attendance: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
