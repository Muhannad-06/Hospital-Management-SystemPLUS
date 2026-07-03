# Hospital Management System

A desktop **Hospital Management System** built with **JavaFX** and **SQLite**. It supports separate patient and doctor experiences: registration, login, appointment booking (including emergency bookings), doctor dashboards, billing, and an integrated AI chat assistant.

## Features

- **Welcome / role selection screen** — choose to continue as a patient or a doctor
- **Patient registration & login**
- **Doctor login & dashboard** — view and manage appointments
- **Appointment booking**, including a dedicated **emergency booking** flow
- **Patient dashboard** for managing bookings and viewing info
- **Billing** screen for generating/viewing bills
- **AI Chat assistant** (`ChatBotService`) for in-app help
- **SQLite-backed persistence** (`hospital.db`) via `DatabaseManager`
- Custom UI styling (`application.css`) and lightweight UI animations (`AestheticAnimations`)

## Tech Stack

| Layer          | Technology |
|----------------|------------|
| UI              | JavaFX (FXML + CSS) |
| Language        | Java 21 |
| Database        | SQLite (via `sqlite-jdbc`) |
| Build           | Maven |

## Project Structure

```
src/
├── module-info.java
└── application/
    ├── WelcomeScreen.java          # App entry point
    ├── DatabaseManager.java        # SQLite connection & queries
    ├── Session.java                # Current logged-in user/session state
    ├── StageManager.java           # Scene/stage navigation
    ├── AestheticAnimations.java    # UI animation helpers
    ├── BloodType.java              # Enum/model helper
    ├── application.css             # Global stylesheet
    ├── FXMLs/                      # All FXML view layouts
    │   ├── welcomeScreen.fxml
    │   ├── login.fxml
    │   ├── register.fxml
    │   ├── patientDashboard.fxml
    │   ├── doctorDashboard.fxml
    │   ├── doctors.fxml
    │   ├── booking.fxml
    │   ├── emergencyBooking.fxml
    │   ├── bill.fxml
    │   └── aiChat.fxml
    ├── controllers/                # FXML controllers (one per view)
    ├── models/                     # Domain models: Person, Patient, Doctor, Bed
    ├── services/                   # ChatBotService (AI chat)
    └── image/                      # App icons & illustrations
```

## Getting Started

### Prerequisites

- **JDK 21+**
- **Maven 3.9+**
- Internet access on first build (Maven will download JavaFX and `sqlite-jdbc` dependencies)

### Run the app

```bash
git clone <this-repo-url>
cd HospitalManagmentSystem
mvn clean javafx:run
```

### Build a runnable jar

```bash
mvn clean package
```

### Using Eclipse instead of Maven

This project originated as an Eclipse JavaFX project. If you prefer Eclipse:

1. Install the [e(fx)clipse](https://www.eclipse.org/efxclipse/) plugin.
2. `File → Import → Existing Maven Project`, point it at this folder.
3. Add the JavaFX SDK to your module path if it isn't picked up automatically.
4. Run `application.WelcomeScreen` as a Java application.

## Database

The app ships with a starter SQLite database (`hospital.db`) in the project root. `DatabaseManager` opens this file at runtime — delete it if you want to start from a clean/empty database (the app will need matching schema creation logic to regenerate tables).

## Notes

- `*.db-shm` / `*.db-wal` are SQLite runtime write-ahead-log files and are intentionally excluded via `.gitignore`.
- Compiled output (`bin/`, `target/`) and IDE metadata (`.classpath`, `.project`, `.settings/`) are also excluded — regenerate them locally via Maven or your IDE.

## License
**Copyright (c) 2026 Muhannad El-Shahiedy**
All Rights Reserved.
