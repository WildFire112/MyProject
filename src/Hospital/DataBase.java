package Hospital;

import org.jetbrains.annotations.NonNls;

import java.sql.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

class DataBase {
    @NonNls private final String url = "jdbc:sqlite:E:/test.db";

    DataBase() {}

    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    void createNewDatabase() {
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();

                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        createTables();
    }

    private void createTables() {
        // SQL statement for creating a new table
        @NonNls String sql1 = "CREATE TABLE IF NOT EXISTS Doctors (\n" +
                "    Name      TEXT NOT NULL,\n" +
                "    ID        INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    StartTime TEXT NOT NULL,\n" +
                "    EndTime   TEXT NOT NULL\n" +
                ");";

        @NonNls String sql2 = "CREATE TABLE IF NOT EXISTS Time (\n" +
                "    Date     TEXT NOT NULL,\n" +
                "    Value    TEXT NOT NULL,\n" +
                "    Name     TEXT NOT NULL,\n" +
                "    DoctorID INTEGER REFERENCES Doctors (ID)\n" +
                ");";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql1);
            stmt.execute(sql2);
            System.out.println("Created!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    HashMap<String, Doctor> getDoctors() {
        HashMap<String, Doctor> docs = new HashMap<>();
        @NonNls String sql = "SELECT Name, ID, StartTime, EndTime FROM Doctors";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                LocalTime start = LocalTime.parse(rs.getString("StartTime"), DateTimeFormatter.ofPattern("HH:mm"));
                LocalTime end = LocalTime.parse(rs.getString("EndTime"), DateTimeFormatter.ofPattern("HH:mm"));
                Doctor doc = new Doctor(rs.getString("Name"), start, end, rs.getString("ID"));
                docs.put(rs.getString("Name"), doc);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return docs;
    }

    Doctor getDoctor(String doctor_name) {
        @NonNls String sql = "SELECT Name, ID, StartTime, EndTime FROM Doctors WHERE Name = ?";
        Doctor doc = null;
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, doctor_name);

            ResultSet rs = pstmt.executeQuery();

            LocalTime start = LocalTime.parse(rs.getString("StartTime"), DateTimeFormatter.ofPattern("HH:mm"));
            LocalTime end = LocalTime.parse(rs.getString("EndTime"), DateTimeFormatter.ofPattern("HH:mm"));
            doc = new Doctor(rs.getString("Name"), start, end, rs.getString("ID"));

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return doc;
    }

    void deleteDoctor(String id) {
        @NonNls String sql = "DELETE FROM Doctors WHERE id = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setString(1, id);
            // execute the delete statement
            pstmt.executeUpdate();

            System.out.println("Deleted");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    void addDoctor(String name, String start, String end) {
        @NonNls String sql = "INSERT INTO Doctors(Name, StartTime, EndTime) VALUES(?, ?, ?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, start);
            pstmt.setString(3, end);
            pstmt.executeUpdate();

            System.out.println(name + " added");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    void addTime(String doctor_id, String date_value, String value, String name) {
        @NonNls String sql = "INSERT INTO Time (Date, Value, Name, DoctorID) VALUES(?, ?, ?, ?)";

        System.out.println(date_value + " " + value + " " + name);
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, date_value);
            pstmt.setString(2, value);
            pstmt.setString(3, name);
            pstmt.setString(4, doctor_id);
            pstmt.executeUpdate();

            System.out.println(value + " added");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    void changeTime(String doctor_id, String time_start, String time_end) {
        @NonNls String sql = "UPDATE Doctors SET StartTime = ?, EndTime = ? WHERE ID = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setString(1, time_start);
            pstmt.setString(2, time_end);
            pstmt.setString(3, doctor_id);
            // update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    HashMap<LocalTime, String> getTime(String doctor_id, String date) {
        @NonNls String sql = "SELECT Name, Value FROM Time WHERE DoctorID = ? AND Date = ?";

        HashMap<LocalTime, String> time = new HashMap<>();
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, doctor_id);
            pstmt.setString(2, date);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                System.out.println(rs.getString("Name") + " " + rs.getString("Value"));

                LocalTime t = LocalTime.parse(rs.getString("Value"), DateTimeFormatter.ofPattern("HH:mm"));
                time.put(t, rs.getString("Name"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return time;
    }
}
