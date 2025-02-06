# Attendance QR Code System

## Project Overview
This project is a QR Code Attendance System that allows students to mark their attendance by scanning QR codes associated with their subjects. The application uses a webcam to scan QR codes and records attendance in a MySQL database.

## Key Components
- **AttendanceApp.java**: The main application that provides the user interface for scanning QR codes and marking attendance.
- **MarkAttendanceServlet.java**: A servlet that handles the marking of attendance by inserting student and subject IDs into the database.
- **QrScanner.java**: A utility class that manages the webcam and scans QR codes.
- **QrGenerator.java**: A utility class that generates QR codes for subject IDs.
- **QrDecoder.java**: A utility class that decodes QR code images to retrieve the subject IDs.

## Database Schema
The `attendance` table in the MySQL database has the following structure:
```
+------------+-----------+------+-----+---------+----------------+
| Field      | Type      | Null | Key | Default | Extra          |
+------------+-----------+------+-----+---------+----------------+
| id         | int       | NO   | PRI | NULL    | auto_increment |
| student_id | int       | YES  | MUL | NULL    |                |
| subject_id | int       | YES  | MUL | NULL    |                |
+------------+-----------+------+-----+---------+----------------+
```

## Workflow
1. **User Interface**: The application starts and displays a user interface for entering the student roll number and scanning QR codes.
2. **Scanning QR Codes**: When the "Scan QR Code" button is clicked, the webcam activates, and the application attempts to scan a QR code.
3. **Validating QR Code**: If a QR code is detected, the application retrieves the subject ID from the QR code.
4. **Checking Validity**: The application checks if the scanned subject ID is valid by comparing it against a predefined list of subjects stored in a map.
5. **Marking Attendance**: If valid, the application marks the attendance by sending the student ID and subject ID to the `MarkAttendanceServlet`, which inserts the data into the database.
6. **Feedback**: The user receives feedback on whether the attendance was marked successfully or if there was an error.

## Setup Instructions
1. Clone the repository to your local machine.
2. Ensure you have Java and Gradle installed.
3. Set up a MySQL database and create the `attendance` table with the appropriate schema.
4. Update the database connection string in `MarkAttendanceServlet.java` as needed.
5. Run the application using Gradle:
   ```bash
   ./gradlew runAttendanceApp
   ```

## Usage Instructions
1. Open the application.
2. Enter your roll number in the provided field.
3. Click the "Scan QR Code" button to activate the webcam and scan the QR code.
4. The application will display whether the attendance was marked successfully or if there was an error.

## License
This project is licensed under the MIT License.
