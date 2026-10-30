# 🎯 Quiz Management System

A complete Java desktop application for managing and taking quizzes.  
Built with **Java Swing + MySQL + JDBC** — designed for B.Tech CSE projects.

---

## 📁 Project Structure

```
QuizManagementSystem/
├── src/                         ← Java source files (10 classes exactly)
│   ├── Main.java
│   ├── DatabaseConnection.java
│   ├── User.java
│   ├── Quiz.java
│   ├── Question.java
│   ├── UserDAO.java
│   ├── QuizDAO.java
│   ├── QuizFrame.java
│   ├── QuizTakingFrame.java
│   └── ResultFrame.java
│
├── database/
│   └── quiz_management.sql      ← Run this first!
│
├── lib/
│   └── mysql-connector-j-*.jar  ← Download and place here
│
├── out/                         ← Compiled .class files go here
├── README.md
└── PROJECT_DOCUMENTATION.md
```

---

## ⚙️ Setup Instructions

### Step 1 — Install Requirements

| Requirement | Version |
|-------------|---------|
| Java JDK    | 11 or higher |
| MySQL       | 8.0 or higher |
| MySQL Connector/J | 8.x (JDBC driver) |

### Step 2 — Download MySQL Connector/J

1. Visit: https://dev.mysql.com/downloads/connector/j/
2. Download the **Platform Independent** `.zip` or `.tar.gz`
3. Extract and copy `mysql-connector-j-*.jar` to the `lib/` folder

### Step 3 — Set Up the Database

Open MySQL Workbench (or your MySQL client) and run:

```sql
SOURCE /path/to/QuizManagementSystem/database/quiz_management.sql;
```

Or run from the command line:

```bash
mysql -u root -p < database/quiz_management.sql
```

### Step 4 — Configure Database Credentials

Open `src/DatabaseConnection.java` and update:

```java
private static final String USERNAME = "root";      // your MySQL username
private static final String PASSWORD = "root";      // your MySQL password
```

Change these to match your actual MySQL credentials.

### Step 5 — Compile

From the project root directory:

**Windows (PowerShell / CMD):**
```cmd
javac -encoding UTF-8 -cp "lib\mysql-connector-j-*.jar" -d out src\*.java
```

**Linux / macOS:**
```bash
javac -encoding UTF-8 -cp "lib/mysql-connector-j-*.jar" -d out src/*.java
```

### Step 6 — Run

**Windows:**
```cmd
java -cp "out;lib\mysql-connector-j-*.jar" Main
```

**Linux / macOS:**
```bash
java -cp "out:lib/mysql-connector-j-*.jar" Main
```

---

## 🔑 Default Login Credentials

| Role    | Username   | Password   |
|---------|------------|------------|
| Admin   | `admin`    | `admin123` |
| Student | `student1` | `pass123`  |
| Student | `student2` | `pass123`  |

**Guest:** Click "CONTINUE AS GUEST" — no password needed!

---

## 🎮 Features

### All Users
- Modern Neo-Brutalist UI design
- Take quizzes with countdown timer
- View detailed results with answer review
- Auto-submit when time expires

### Registered Students
- Register with full name and username
- View personalized dashboard with statistics
- Track quiz history

### Admin
- Create, edit, delete quizzes
- Add, edit, delete questions
- View all users' results
- Dashboard with system statistics

### Guest Mode
- No account required
- Enter name → select quiz → take quiz → view result

---

## 🗄️ Database Tables

| Table       | Purpose                              |
|-------------|--------------------------------------|
| `users`     | Stores admin, student, and guest users |
| `quizzes`   | Quiz title, description, category, time limit |
| `questions` | Questions with 4 options + correct answer |
| `results`   | Quiz attempt scores and percentages  |
| `categories`| Quiz categories (Java, Networks, etc.) |

---

## 🏗️ Architecture

- **10 Java classes** — exactly as required
- **Model-DAO pattern**: Models (`User`, `Quiz`, `Question`) separated from DB logic (`UserDAO`, `QuizDAO`)
- **CardLayout** for panel-based navigation within one window
- **PreparedStatement** for all SQL queries (SQL injection safe)
- **Singleton** connection pattern in `DatabaseConnection`

---

## ❗ Troubleshooting

| Problem | Solution |
|---------|----------|
| "MySQL JDBC Driver not found" | Place `mysql-connector-j.jar` in `lib/` folder |
| "Could not connect to database" | Check MySQL is running, credentials match |
| "Access denied for user root" | Update USERNAME/PASSWORD in DatabaseConnection.java |
| Blank screen after login | Make sure SQL script ran successfully |
| Compilation error | Ensure JDK 11+ is installed and on PATH |

---

## 📝 Sample Data Included

- **3 quizzes**: Java Basics (10 Q), Data Structures (15 Q), Computer Networks (12 Q)
- **5 categories**: Java Programming, Data Structures, Computer Networks, Operating Systems, DBMS
- **3 user accounts**: admin, student1, student2
