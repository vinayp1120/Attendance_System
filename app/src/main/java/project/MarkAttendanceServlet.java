package project;

import java.io.*;
import java.sql.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/mark-attendance")
public class MarkAttendanceServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int studentId = Integer.parseInt(request.getParameter("student_id"));
        int subjectId = Integer.parseInt(request.getParameter("subject_id"));

        // Validate input parameters
        if (studentId <= 0 || subjectId <= 0) {
            response.getWriter().write("Error: Invalid input parameters");
            return;
        }

        try {
            // Update the database connection string as needed
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db", "root", "");
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO attendance (student_id, subject_id) VALUES (?, ?)");
            stmt.setInt(1, studentId);
            stmt.setInt(2, subjectId);
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
