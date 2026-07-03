package application;

import java.sql.*;
import application.models.Doctor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

// ============================= TOO Much Comments as i was trying to clarify as much as possible  =========================================

// In the Database Part, AI has Involved in the Syntax to make sure Avoid error and due to The lack of experience 
public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:hospital.db";
    private static Connection connection;

    
   // Call this once when the app starts
    public static void connect() {
        try {
            // Set WAL mode using a temporary connection first (avoids lock contention)
            try (Connection tempConn = DriverManager.getConnection(DB_URL);
                 Statement stmt = tempConn.createStatement()) {
                stmt.execute("PRAGMA journal_mode=WAL;");
                stmt.execute("PRAGMA busy_timeout=5000;");
            }

            connection = DriverManager.getConnection(DB_URL);
            createTables();
            System.out.println("Connected to database.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void finishTreatment(int appointmentId, String treatmentNotes) throws SQLException {
        String sql = "UPDATE appointments SET status = 'Completed', notes = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, treatmentNotes);
            ps.setInt(2, appointmentId);
            ps.executeUpdate();
        }
    }
    
    
    private static void insertDefaultDoctors() throws SQLException {
        // Only seed if no doctors exist yet to make sure it's only inserted in the very first launch
        String checkSql = "SELECT COUNT(*) FROM doctors";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(checkSql)) {
        	// Check if the count > 0 cancel the process
            if (rs.getInt(1) > 0) return;
        }

        String insertSql = "INSERT INTO doctors (username, password, specialty, gender) VALUES (?, ?, ?, ?)";
        
        String[][] doctors = {
            // Neurology
            { "dr.mohamed",    "neuro123",  "Neurology", "Male"},
            { "dr.sara",     "neuro456",  "Neurology", "Female"},
            // Cardiology
            { "dr.moaz", "cardio123", "Cardiology", "Male"},
            { "dr.laila",   "cardio456", "Cardiology", "Female"},
            // Orthopedics
            { "dr.khaled",   "ortho123",  "Orthopedics", "Male"},
            { "dr.nermen",    "ortho456",  "Orthopedics", "Female"},
            // Cancer Care
            { "dr.youssef", "cancer123", "Cancer Care", "Male"},
            { "dr.mariam",  "cancer456", "Cancer Care", "Female"}
        };

        try (PreparedStatement ps = connection.prepareStatement(insertSql)) {
            for (String[] doc : doctors) {
            // Fill the boxes which we defined before Value ? ? ? ?	
                ps.setString(1, doc[0]);
                ps.setString(2, doc[1]);
                ps.setString(3, doc[2]);
                ps.setString(4, doc[3]);
           // After Filling the Boxes we hand it in a form of database to be saved permanently 
                ps.executeUpdate();
            }
        }
        // Tracking the Program flow
        System.out.println("Default doctors inserted successfully.");
    }
    
    private static void initializeBeds() throws SQLException {
        // Check if the beds table already has any data
        String checkSql = "SELECT COUNT(*) FROM beds";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(checkSql)) {
            
            // If the count is greater than 0 it means beds are already initialized.
            // We 'return' to skip the rest of the method and keep existing data.
        	if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("Beds already exist in database. Skipping initialization...");
                return; 
            }
        }

        // only runs IF the table was empty
        String insertSql = "INSERT INTO beds (bed_number, clinic_name, patient_id) VALUES (?, 'General Ward', NULL)";
        
        try (PreparedStatement ps = connection.prepareStatement(insertSql)) {
        	// 801 to 810 as to give beds more Aesthetic look
            for (int i = 801; i <= 810; i++) {
                ps.setString(1, String.valueOf(i));
                ps.executeUpdate();
            }
        }
        // Tracking the Program flow
        System.out.println("Initial 10 beds created (801-810).");
    }
    
    
    // Book a new appointment
    public static void bookAppointment(int patientId, int doctorId, String date, String time, String notes) throws SQLException {
        String sql = "INSERT INTO appointments (patient_id, doctor_id, date, time, notes) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ps.setInt(2, doctorId);
            ps.setString(3, date);
            ps.setString(4, time);
            ps.setString(5, notes);
            ps.executeUpdate();
        }
    }

    // Get all appointments for a PATIENT (used in patient dashboard)
    public static ResultSet getPatientAppointments(int patientId) throws SQLException {
        String sql = 
            "SELECT a.id, d.username AS doctor, d.specialty, a.date, a.time, a.status, a.notes " +
            "FROM appointments a " +
            "JOIN doctors d ON a.doctor_id = d.id " +
            "WHERE a.patient_id = ? " +
            "ORDER BY a.date, a.time";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, patientId);
        return ps.executeQuery();
    }

    // Get all appointments for a DOCTOR (used in doctor dashboard)
    public static ResultSet getDoctorAppointments(int doctorId) throws SQLException {
        String sql = 
            "SELECT a.id, p.first_name || ' ' || p.second_name AS patient, p.phone, a.date, a.time, a.status, a.notes " +
            "FROM appointments a " +
            "JOIN patients p ON a.patient_id = p.id " +
            "WHERE a.doctor_id = ? " +
            "ORDER BY a.date, a.time";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, doctorId);
        return ps.executeQuery();
    }

    // Update appointment status (Scheduled → Completed or Cancelled)
    public static void updateAppointmentStatus(int appointmentId, String status) throws SQLException {
        String sql = "UPDATE appointments SET status = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, appointmentId);
            ps.executeUpdate();
        }
    }
    

    private static void createTables() throws SQLException {
    	// This is the Blueprint of Our APP Structure here where the tables been made
    	String patients = """
    		    CREATE TABLE IF NOT EXISTS patients (
    		        id INTEGER PRIMARY KEY AUTOINCREMENT,
    		        first_name TEXT, second_name TEXT, phone TEXT, gender TEXT, dob TEXT,
    		        username TEXT UNIQUE, password TEXT,
    		        height REAL, weight REAL, blood_type TEXT, chronic_disease TEXT,
    		        ecg TEXT, x_ray TEXT, oncology TEXT, mri TEXT, diagnosis TEXT -- New Fields
    		    );
    		""";
    	
    	
    	String bills = """
                CREATE TABLE IF NOT EXISTS bills (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    patient_id INTEGER,
                    ecg TEXT,
                    xray TEXT,
                    oncology TEXT,
                    mri TEXT,
                    bed TEXT,
                    num_appointments INTEGER,
                    total_price REAL,
                    issued_date TEXT,
                    status TEXT,
                    FOREIGN KEY (patient_id) REFERENCES patients(id)
                );
                """;

    	
    	String doctors = """
    		    CREATE TABLE IF NOT EXISTS doctors (
    		        id INTEGER PRIMARY KEY AUTOINCREMENT,
    		        username TEXT NOT NULL UNIQUE,
    		        password TEXT NOT NULL,
    		        specialty TEXT,
    		        gender TEXT -- Added gender column
    		    );
    		""";
        String appointments =
        	    "CREATE TABLE IF NOT EXISTS appointments (" +
        	    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
        	    "patient_id INTEGER NOT NULL," +
        	    "doctor_id  INTEGER NOT NULL," +
        	    "date       TEXT NOT NULL," +   // format 'YYYY-MM-DD'
        	    "time       TEXT NOT NULL," +   // format 'HH:MM'
        	    "status     TEXT DEFAULT 'Scheduled'," + // Scheduled / Completed / Cancelled 
        	    "notes      TEXT," +
        	    "FOREIGN KEY (patient_id) REFERENCES patients(id)," +
        	    "FOREIGN KEY (doctor_id)  REFERENCES doctors(id))";
        String bedsTableSql = """
        	    CREATE TABLE IF NOT EXISTS beds (
        	        id INTEGER PRIMARY KEY AUTOINCREMENT,
        	        bed_number TEXT NOT NULL UNIQUE, -- Prevents two beds from having the same code
        	        clinic_name TEXT NOT NULL,
        	        patient_id INTEGER,
        	        FOREIGN KEY (patient_id) REFERENCES patients(id)
        	    );
        	""";
        	
        // The most important part the courier which converts these Strings or text to real SQLite tables in the Engine
        	try (Statement stmt = connection.createStatement()) {
        	    stmt.execute(patients);
        	    stmt.execute(doctors);
        	    stmt.execute(appointments);
        	    stmt.execute(bills);
            	stmt.execute(bedsTableSql);
        	} catch(Exception ex) {
        		ex.printStackTrace();
        		System.out.println("Somthing went wrong and cannot execute");
        	}

        	// Default intializations that come already in the Hospital and the user cannot make a change in them
        	insertDefaultDoctors();
        	initializeBeds();
    }
    
 
    public static int getScheduledAppointmentId(int patientId, int doctorId) throws SQLException {
        String sql = "SELECT id FROM appointments WHERE patient_id = ? AND doctor_id = ? AND status = 'Scheduled' ORDER BY date ASC, time ASC LIMIT 1";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, String.valueOf(patientId));
            ps.setString(2, String.valueOf(doctorId));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        return -1; // Not found credits to Eng.Abdelbasit
    }
    
    public static void updateMedicalRecords(int patientId, String ecg, String xray, String oncology, String mri, String diagnosis) throws SQLException {
        String sql = "UPDATE patients SET ecg = ?, x_ray = ?, oncology = ?, mri = ?, diagnosis = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, ecg);
            ps.setString(2, xray);
            ps.setString(3, oncology);
            ps.setString(4, mri);
            ps.setString(5, diagnosis);
            ps.setInt(6, patientId);
            ps.executeUpdate();
        }
    }
    
    public static ResultSet getAllAppointments() throws SQLException {
        String sql = "SELECT * FROM appointments ORDER BY date ASC, time ASC";
        return connection.createStatement().executeQuery(sql);
    }
    
    public static ResultSet getNearestAppointment(int id, boolean isDoctor) throws SQLException {
        String idColumn = isDoctor ? "doctor_id" : "patient_id";
        String sql = "SELECT * FROM appointments WHERE " + idColumn + " = ? AND status = 'Scheduled' AND date >= date('now') ORDER BY date ASC, time ASC LIMIT 1";
        
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        return ps.executeQuery();
    }
    
    public static int getPendingAppointmentCountByDoctor(int doctorId) {
        // We filter by 'Scheduled' because that is the default status for new bookings
        String sql = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND status = 'Scheduled'";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public static int getPatientCount() {
        String sql = "SELECT COUNT(*) FROM patients";
        // We use the existing connection established in connect()
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1); // Returns the total number of rows
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public static int getFreeBedCount() {
        // We only count beds where no patient is assigned
        String sql = "SELECT COUNT(*) FROM beds WHERE patient_id IS NULL";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public static javafx.collections.ObservableList<String> getFreeBedNumbers() {
        javafx.collections.ObservableList<String> freeBeds = javafx.collections.FXCollections.observableArrayList();
        // Select bed_number from beds where no patient is assigned
        String sql = "SELECT bed_number FROM beds WHERE patient_id IS NULL";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                // Add each available bed number to the list
                freeBeds.add(rs.getString("bed_number"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return freeBeds;
    }
    public static ResultSet getAllBeds() throws SQLException {
        String sql = """
            SELECT b.id, b.bed_number, b.clinic_name, p.first_name, p.id AS p_id
            FROM beds b
            LEFT JOIN patients p ON b.patient_id = p.id
        """;
        return connection.createStatement().executeQuery(sql);
    }

    public static void assignBed(int bedId, int patientId) throws SQLException {
        String sql = "UPDATE beds SET patient_id = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ps.setInt(2, bedId);
            ps.executeUpdate();
        }
    }

    public static void releaseBed(int bedId) throws SQLException {
        String sql = "UPDATE beds SET patient_id = NULL WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, bedId);
            ps.executeUpdate();
        }
    }
    
    public static javafx.collections.ObservableList<application.models.Doctor> getDoctorsBySpecialty(String specialty) {
        javafx.collections.ObservableList<application.models.Doctor> doctorList = javafx.collections.FXCollections.observableArrayList();
        String sql = "SELECT * FROM doctors WHERE specialty = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, specialty);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                //
                doctorList.add(new application.models.Doctor(
                    rs.getInt("id"),
                    rs.getString("username"), 
                    rs.getString("gender"),
                    rs.getString("specialty")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctorList;
    }
    
    public static javafx.collections.ObservableList<application.models.Patient> getPatientsByDoctor(int doctorId) {
        javafx.collections.ObservableList<application.models.Patient> patientList = javafx.collections.FXCollections.observableArrayList();
        // This query finds the earliest upcoming appointment for each patient and sorts the patients by that time
        String sql = """
            SELECT p.*, MIN(a.date || ' ' || a.time) as next_app
            FROM patients p
            JOIN appointments a ON p.id = a.patient_id
            WHERE a.doctor_id = ? AND a.status = 'Scheduled'
            GROUP BY p.id
            ORDER BY next_app ASC
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                patientList.add(new application.models.Patient(
                    rs.getInt("id"),
                    rs.getString("first_name") + " " + rs.getString("second_name"),
                    rs.getString("gender"),
                    rs.getString("dob"),
                    rs.getDouble("height"),
                    rs.getDouble("weight"),
                    rs.getString("blood_type"),
                    rs.getString("chronic_disease")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patientList;
    }
    
    public static boolean hasBed(int patientId) {
        String sql = "SELECT 1 FROM beds WHERE patient_id = ? LIMIT 1";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // Returns true if a row exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static javafx.collections.ObservableList<application.models.Bed> getAvailableBed() {
        javafx.collections.ObservableList<application.models.Bed> availableBeds = javafx.collections.FXCollections.observableArrayList();
        String sql = "SELECT * FROM beds WHERE patient_id IS NULL";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                availableBeds.add(new application.models.Bed(
                    rs.getInt("id"),
                    rs.getString("bed_number"),
                    rs.getString("clinic_name"),
                    null // Occupant is null because we only fetch free beds
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return availableBeds;
    }

    public static void autoCancelPastAppointments() {
        if (connection == null) {
            System.err.println("Cannot run auto-cancel: no database connection.");
            return;  // ← prevents NullPointerException if connect() failed
        }
        String sql = "UPDATE appointments SET notes = 'Cancelled: Past Date', status = 'Canceled' " +
                     "WHERE date < ? AND status NOT LIKE 'Paid%'";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, java.time.LocalDate.now().toString());
            ps.executeUpdate();
            System.out.println("Past appointments automatically checked and updated.");
        } catch (SQLException e) {
            System.err.println("Error running auto-cancel updates:");
            e.printStackTrace();
        }
    }
    
    public static boolean isUsernameExists(String username) {
        String sql = "SELECT 1 FROM patients WHERE username = ? UNION SELECT 1 FROM doctors WHERE username = ? LIMIT 1";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username.trim().toLowerCase());
            ps.setString(2, username.trim().toLowerCase());
            
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // Returns true if a match was found in either table
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int getBedId(int patientId) {
                String sql = "SELECT id FROM beds WHERE patient_id = ? ";

                try (PreparedStatement ps = connection.prepareStatement(sql)) {
                    ps.setInt(1, patientId);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            return rs.getInt("id");
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return -1;
                }
        
        public static void saveBill(int patientId, String ecg, String xray, String oncology, String mri, String bed, int numAppointments, double totalPrice, String issuedDate, String status) {
        
        String sql = "INSERT INTO bills (patient_id, ecg, xray, oncology, mri, bed, " +
                     "num_appointments, total_price, issued_date, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, patientId);
                ps.setString(2, ecg);
                ps.setString(3, xray);
                ps.setString(4, oncology);
                ps.setString(5, mri);
                ps.setString(6, bed);
                ps.setInt(7, numAppointments);
                ps.setDouble(8, totalPrice);
                ps.setString(9, issuedDate);
                ps.setString(10, status);

                ps.executeUpdate();
                System.out.println("Bill saved to database successfully!");

            } catch (SQLException e) {
                System.err.println("Error while saving the bill:");
                e.printStackTrace();
            }
        }
        
        public static int getDoneAppointmentCountByPatient(int patientId) {
            String sql = "SELECT COUNT(*) FROM appointments WHERE patient_id = ? AND status = 'Completed'";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, patientId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return 0;
        }
        
        public static int getCanceledAppointmentCountByPatient(int patientId) {
            String sql = "SELECT COUNT(*) FROM appointments WHERE patient_id = ? AND status = 'Canceled'";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, patientId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return 0;
        }
        
        public static boolean hasEmergencyAppointment(int patientId) {
            // Looks for any appointment notes starting with or containing "Emergency" for this patient
            String sql = "SELECT 1 FROM appointments WHERE patient_id = ? AND notes LIKE 'Emergency%' LIMIT 1";
            
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, patientId);
                
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next(); // Returns true if an emergency record already exists
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
    
        public static javafx.collections.ObservableList<application.models.Patient> getAllPatientsByDoctor(int doctorId) {
            javafx.collections.ObservableList<application.models.Patient> patientList = javafx.collections.FXCollections.observableArrayList();
            // This query finds the earliest upcoming appointment for each patient and sorts the patients by that time
            String sql = """
                SELECT p.*, MIN(a.date || ' ' || a.time) as next_app
                FROM patients p
                JOIN appointments a ON p.id = a.patient_id
                WHERE a.doctor_id = ? AND a.status IN ('Scheduled', 'Completed')
                GROUP BY p.id
                ORDER BY next_app ASC
            """;

            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, doctorId);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    patientList.add(new application.models.Patient(
                        rs.getInt("id"),
                        rs.getString("first_name") + " " + rs.getString("second_name"),
                        rs.getString("gender"),
                        rs.getString("dob"),
                        rs.getDouble("height"),
                        rs.getDouble("weight"),
                        rs.getString("blood_type"),
                        rs.getString("chronic_disease")
                    ));
                }
                
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return patientList;
        }
        
        public static void updateAppointmentsToPaid(int patientId) {
            String sql = "UPDATE appointments SET status = 'Paid' " +
                         "WHERE patient_id = ? AND status IN ('Completed', 'Canceled')";

            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, patientId);
                ps.executeUpdate(); 

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
    public static Connection getConnection() {
        return connection;
    }
}