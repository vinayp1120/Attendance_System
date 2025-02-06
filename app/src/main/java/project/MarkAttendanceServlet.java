package project;

import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/mark-attendance")
public class MarkAttendanceServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int studentId;
        int subjectId;

        // Validate input parameters
        try {
            studentId = Integer.parseInt(request.getParameter("student_id"));
            subjectId = Integer.parseInt(request.getParameter("subject_id"));
        } catch (NumberFormatException e) {
            response.getWriter().write("Error: Invalid input parameters");
            return;
        }

        if (studentId <= 0 || subjectId <= 0) {
            response.getWriter().write("Error: Invalid input parameters");
            return;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Update the database connection string as needed
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db", "root", "kali");
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO attendance (student_id, subject_id, Date, time) VALUES (?, ?, ?, ?)");
            stmt.setInt(1, studentId);
            stmt.setInt(2, subjectId);
            Date currentDate = Date.valueOf(LocalDate.now()); // Set current date
            Timestamp currentTime = Timestamp.valueOf(LocalDateTime.now()); // Set current timestamp
            stmt.setDate(3, currentDate);
            stmt.setTimestamp(4, currentTime);
            
            // Debug logging
            System.out.println("Inserting Attendance: Student ID = " + studentId + ", Subject ID = " + subjectId + ", Date = " + currentDate + ", Time = " + currentTime);
            
            stmt.executeUpdate();

            response.getWriter().write("Attendance Marked Successfully");
            conn.close();
        } catch (SQLException e) {
            response.getWriter().write("SQL Error: " + e.getMessage());
        } catch (Exception e) {
            response.getWriter().write("Error: " + e.getMessage());
        }
    }
}
