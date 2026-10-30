# PROJECT DOCUMENTATION
## Quiz Management System
### B.Tech CSE — Mini Project

---

## 1. TITLE

**Quiz Management System**  
A Desktop Application using Java Swing, MySQL, and JDBC

---

## 2. ABSTRACT

The Quiz Management System is a standalone desktop application developed using Java (Swing), MySQL, and JDBC. It enables educational institutions or individuals to create and manage quizzes, and allows students to take quizzes in a timed, interactive environment. The application supports three user roles—Admin, Registered Student, and Guest—each with appropriate access levels. The interface follows a modern Neo-Brutalist design language for a visually engaging experience. The system stores all quiz data and results in a relational MySQL database accessed through JDBC PreparedStatements.

---

## 3. INTRODUCTION

With the growing importance of online assessments in education, a need exists for a simple yet functional quiz management system that can be used in local environments without internet dependency. This project provides a full-featured quiz management solution as a Java desktop application.

The application allows admins to create and manage quizzes, students to register, take quizzes, and review their results, and guests to participate in quizzes without creating an account.

---

## 4. PROBLEM STATEMENT

Traditional paper-based quizzes are time-consuming to prepare, evaluate, and store. Existing online platforms require internet access and may not suit offline educational environments. There is a need for a simple, offline desktop application that:

- Allows admins to manage quizzes digitally
- Enables students to take timed, self-evaluated quizzes
- Provides instant results and score history
- Requires no complex infrastructure

---

## 5. OBJECTIVES

1. Develop a desktop quiz management application using Java Swing
2. Implement three distinct user roles: Admin, Student, Guest
3. Enable CRUD operations on quizzes and questions for admins
4. Implement a countdown timer for quiz sessions
5. Automatically evaluate and store quiz results
6. Allow guests to participate without registration
7. Design a modern, visually appealing Neo-Brutalist interface
8. Use MySQL and JDBC with PreparedStatements for all database interactions

---

## 6. SCOPE

**Included:**
- Admin dashboard with quiz and question management
- Student dashboard with quiz history and statistics
- Guest mode for anonymous quiz taking
- Timed quiz sessions with auto-submission
- Result calculation and storage
- Detailed answer review after quiz completion

**Excluded:**
- Web or mobile access
- Email-based authentication
- Multi-language support
- Video or multimedia questions
- Online/cloud database

---

## 7. FEATURES

| Feature | Admin | Student | Guest |
|---------|-------|---------|-------|
| Login | ✅ | ✅ | ❌ |
| Register | ❌ | ✅ | ❌ |
| Guest Entry | ❌ | ❌ | ✅ |
| Dashboard Stats | ✅ | ✅ | ❌ |
| Create Quiz | ✅ | ❌ | ❌ |
| Edit Quiz | ✅ | ❌ | ❌ |
| Delete Quiz | ✅ | ❌ | ❌ |
| Add Questions | ✅ | ❌ | ❌ |
| Delete Questions | ✅ | ❌ | ❌ |
| Take Quiz | ✅ | ✅ | ✅ |
| Timer | ✅ | ✅ | ✅ |
| View Results | ✅ | ✅ | ✅ |
| Answer Review | ✅ | ✅ | ✅ |
| History | ✅ | ✅ | ❌ |
| All Results | ✅ | ❌ | ❌ |

---

## 8. TECHNOLOGY STACK

| Component | Technology |
|-----------|------------|
| Programming Language | Java (JDK 11+) |
| GUI Framework | Java Swing |
| Database | MySQL 8.0+ |
| Database Connectivity | JDBC (Java Database Connectivity) |
| JDBC Driver | MySQL Connector/J |
| IDE Recommended | IntelliJ IDEA / Eclipse / VS Code |
| Build | Manual (javac) |
| Design Pattern | Model-DAO |

---

## 9. SYSTEM REQUIREMENTS

**Hardware:**
- Processor: Intel Core i3 or equivalent
- RAM: 2 GB minimum (4 GB recommended)
- Storage: 100 MB free space
- Display: 1280×720 minimum

**Software:**
- Operating System: Windows 7/10/11, Linux, macOS
- Java JDK 11 or higher
- MySQL 8.0 or higher
- MySQL Connector/J (JDBC Driver)

---

## 10. ARCHITECTURE

```
┌─────────────────────────────────────────────────────────────┐
│                    PRESENTATION LAYER                        │
│         QuizFrame   QuizTakingFrame   ResultFrame           │
└──────────────────────────┬──────────────────────────────────┘
                           │ uses
┌──────────────────────────▼──────────────────────────────────┐
│                      DATA ACCESS LAYER                       │
│                   UserDAO    QuizDAO                         │
└──────────────────────────┬──────────────────────────────────┘
                           │ uses
┌──────────────────────────▼──────────────────────────────────┐
│                      MODEL LAYER                             │
│               User   Quiz   Question                         │
└──────────────────────────┬──────────────────────────────────┘
                           │ connects via
┌──────────────────────────▼──────────────────────────────────┐
│                  DATABASE CONNECTION LAYER                    │
│                   DatabaseConnection                         │
└──────────────────────────┬──────────────────────────────────┘
                           │ JDBC
┌──────────────────────────▼──────────────────────────────────┐
│                      MySQL DATABASE                          │
│    users  quizzes  questions  results  categories           │
└─────────────────────────────────────────────────────────────┘
```

---

## 11. CLASS ARCHITECTURE

### 1. `Main.java`
- Entry point of the application
- Sets Look & Feel (Nimbus)
- Tests DB connection before launch
- Launches `QuizFrame` on the Event Dispatch Thread

### 2. `DatabaseConnection.java`
- Singleton pattern for JDBC connection
- Configurable credentials (host, port, database, username, password)
- Provides `getConnection()`, `closeConnection()`, `testConnection()`

### 3. `User.java`
- Model class with fields: id, username, password, role, fullName
- Role helper methods: `isAdmin()`, `isStudent()`, `isGuest()`
- Demonstrates **encapsulation**

### 4. `Quiz.java`
- Model class with fields: id, title, description, categoryId, categoryName, timeLimit, questions
- Contains a `List<Question>` for loaded questions
- Helper: `getQuestionCount()`

### 5. `Question.java`
- Model class with fields: id, quizId, questionText, optionA–D, correctAnswer
- Helper methods: `getOption(letter)`, `isCorrect(answer)`

### 6. `UserDAO.java`
- `login(username, password)` — authenticates user
- `register(user)` — creates student account
- `createGuestUser(name)` — creates temporary guest record
- `usernameExists(username)` — checks uniqueness
- `getTotalUsers()` — count for admin stats

### 7. `QuizDAO.java`
- Quiz CRUD: `getAllQuizzes()`, `getQuizById()`, `createQuiz()`, `updateQuiz()`, `deleteQuiz()`
- Question CRUD: `getQuestionsByQuizId()`, `addQuestion()`, `updateQuestion()`, `deleteQuestion()`
- Result ops: `saveResult()`, `getResultsByUser()`, `getAllResults()`, `getUserStats()`
- Category: `getAllCategories()`
- Stats: `getTotalQuizzes()`, `getTotalQuestions()`, `getTotalAttempts()`

### 8. `QuizFrame.java`
- Main window using `CardLayout` for multi-screen navigation
- Screens: LOGIN, REGISTER, GUEST, DASHBOARD, ADMIN, QUIZ_LIST, MY_RESULTS, ADMIN_QUIZZES, ADMIN_RESULTS, EDIT_QUIZ, QUESTIONS
- Contains all Neo-Brutalist UI factory methods (shared by child frames via `static` visibility)
- Sidebar navigation for authenticated users

### 9. `QuizTakingFrame.java`
- Separate `JFrame` window for the quiz-taking experience
- Displays questions one at a time with 4 selectable option cards
- Countdown timer using `javax.swing.Timer`
- Progress bar, Previous/Next navigation
- Confirmation dialog before submission
- Auto-submits when timer reaches 0
- Calculates score and opens `ResultFrame`

### 10. `ResultFrame.java`
- Displays percentage (large typography), score breakdown, grade
- Visual progress bar showing score percentage
- Detailed answer review: each question shows selected vs. correct answer
- Green/red color-coded correct/incorrect answers
- Navigation back to parent dashboard

---

## 12. DATABASE DESIGN

### Schema Overview

```sql
categories (id PK, name UNIQUE)
     ↑
quizzes (id PK, title, description, category_id FK→categories, time_limit)
     ↑
questions (id PK, quiz_id FK→quizzes ON DELETE CASCADE,
           question_text, option_a, option_b, option_c, option_d,
           correct_answer CHAR(1))

users (id PK, username UNIQUE, password, role ENUM, full_name)
     ↓
results (id PK, user_id FK→users ON DELETE CASCADE,
         quiz_id FK→quizzes ON DELETE CASCADE,
         score, total_questions, percentage DECIMAL, attempt_date DATETIME)
```

---

## 13. ER DIAGRAM

```
+---------------+        +------------------+       +-----------------+
|  categories   |        |     quizzes      |       |    questions    |
+---------------+        +------------------+       +-----------------+
| PK id         |◄───────| PK id            |◄──────| PK id           |
|    name       |        |    title         |       |    quiz_id FK   |
+---------------+        |    description   |       |    question_text|
                         | FK category_id   |       |    option_a     |
                         |    time_limit    |       |    option_b     |
                         +------------------+       |    option_c     |
                                  ▲                 |    option_d     |
                                  │                 |    correct_ans  |
                         +------------------+       +-----------------+
                         |     results      |
                         +------------------+
                         | PK id            |
                         | FK user_id       |       +---------------+
                         | FK quiz_id       |       |     users     |
                         |    score         |       +---------------+
                         |    total_ques    |◄──────| PK id         |
                         |    percentage    |       |    username   |
                         |    attempt_date  |       |    password   |
                         +------------------+       |    role       |
                                                    |    full_name  |
                                                    +---------------+
```

**Relationships:**
- `quizzes.category_id` → `categories.id` (Many-to-One)
- `questions.quiz_id` → `quizzes.id` (Many-to-One, CASCADE DELETE)
- `results.user_id` → `users.id` (Many-to-One, CASCADE DELETE)
- `results.quiz_id` → `quizzes.id` (Many-to-One, CASCADE DELETE)

---

## 14. DATABASE TABLES

### Table: users
| Column | Type | Constraint |
|--------|------|------------|
| id | INT | PK AUTO_INCREMENT |
| username | VARCHAR(50) | NOT NULL UNIQUE |
| password | VARCHAR(255) | NOT NULL |
| role | ENUM('admin','student','guest') | DEFAULT 'student' |
| full_name | VARCHAR(100) | NOT NULL |

### Table: quizzes
| Column | Type | Constraint |
|--------|------|------------|
| id | INT | PK AUTO_INCREMENT |
| title | VARCHAR(150) | NOT NULL |
| description | TEXT | - |
| category_id | INT | FK→categories(id) |
| time_limit | INT | NOT NULL DEFAULT 10 |

### Table: questions
| Column | Type | Constraint |
|--------|------|------------|
| id | INT | PK AUTO_INCREMENT |
| quiz_id | INT | FK→quizzes(id) CASCADE |
| question_text | TEXT | NOT NULL |
| option_a–d | VARCHAR(255) | NOT NULL |
| correct_answer | CHAR(1) | NOT NULL ('A'/'B'/'C'/'D') |

### Table: results
| Column | Type | Constraint |
|--------|------|------------|
| id | INT | PK AUTO_INCREMENT |
| user_id | INT | FK→users(id) CASCADE |
| quiz_id | INT | FK→quizzes(id) CASCADE |
| score | INT | NOT NULL DEFAULT 0 |
| total_questions | INT | NOT NULL |
| percentage | DECIMAL(5,2) | NOT NULL |
| attempt_date | DATETIME | DEFAULT CURRENT_TIMESTAMP |

### Table: categories
| Column | Type | Constraint |
|--------|------|------------|
| id | INT | PK AUTO_INCREMENT |
| name | VARCHAR(100) | NOT NULL UNIQUE |

---

## 15. OOP CONCEPTS

### 1. Encapsulation
**Location:** `User.java`, `Quiz.java`, `Question.java`  
All fields are `private`. Access is only via `public` getters and setters. This protects data integrity and hides internal implementation.

**Example:**
```java
// Fields are private
private String username;
private String password;

// Access via public methods only
public String getUsername() { return username; }
public void setUsername(String u) { this.username = u; }
```

### 2. Abstraction
**Location:** `UserDAO.java`, `QuizDAO.java`  
The DAO classes abstract away all SQL and JDBC details. The GUI only calls `userDAO.login(user, pass)` — it doesn't know about PreparedStatements or ResultSets.

**Example:**
```java
// GUI code — simple, no SQL knowledge
User u = userDAO.login(username, password);
```

### 3. Inheritance
**Location:** `QuizTakingFrame` and `ResultFrame` — both extend `JFrame`.  
Custom mouse adapters use anonymous inner class inheritance: `new MouseAdapter() { ... }`

**Example:**
```java
public class QuizTakingFrame extends JFrame { ... }
public class ResultFrame extends JFrame { ... }
```

### 4. Polymorphism
**Location:** `JButton.paintComponent()` override in `QuizFrame.makeButton()`  
The button overrides `paintComponent` polymorphically to implement the hard-shadow Neo-Brutalist effect.

**Also:** `DefaultTableModel.isCellEditable()` is overridden to make tables read-only.

---

## 16. JDBC IMPLEMENTATION

### Connection
```java
// DatabaseConnection.java
Class.forName("com.mysql.cj.jdbc.Driver");
connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
```

### PreparedStatement (SQL Injection Safe)
```java
// UserDAO.java — login method
String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
PreparedStatement stmt = conn.prepareStatement(sql);
stmt.setString(1, username);   // ? replaced safely
stmt.setString(2, password);
ResultSet rs = stmt.executeQuery();
```

### Why PreparedStatement?
- Parameters are bound separately from the SQL string
- The database pre-compiles the query structure
- Malicious input like `' OR '1'='1` cannot modify the query
- More efficient for repeated queries

### Resource Closing
```java
finally {
    try { if (rs   != null) rs.close();   } catch (SQLException ignored) {}
    try { if (stmt != null) stmt.close(); } catch (SQLException ignored) {}
}
```

---

## 17. AUTHENTICATION

| Role | Method |
|------|--------|
| Admin | Username + Password matched in DB (role='admin') |
| Student | Username + Password matched in DB (role='student') |
| Guest | No password — enter name → temp record created in DB |

Passwords are stored as plain text for simplicity (appropriate for college projects). In production, use BCrypt hashing.

---

## 18. GUEST MODE

1. User clicks **CONTINUE AS GUEST** on login screen
2. Enters display name
3. `UserDAO.createGuestUser(name)` creates a record: `username = "guest_<timestamp>"`, `role = 'guest'`
4. Guest is navigated directly to quiz selection
5. Guest can take any quiz and view results
6. Result is stored in the `results` table linked to the temp guest user

---

## 19. ADMIN MODULE

- Login with role='admin' redirects to Admin Dashboard
- Stats shown: Total Quizzes, Total Questions, Total Users, Total Attempts
- **Create Quiz**: Form with title, description, category, time limit → then add questions
- **Edit Quiz**: Pre-fills form with existing data
- **Delete Quiz**: Confirmation dialog → CASCADE deletes questions automatically
- **Manage Questions**: List of questions with DELETE button + Add Question form
- **View All Results**: Table showing all users' quiz attempts

---

## 20. QUIZ MODULE

- Available quizzes shown as cards on dashboard
- Each card shows: title, question count, time limit, category, START button
- Clicking START loads questions from DB and opens `QuizTakingFrame`
- Quiz with 0 questions shows a warning and cannot be started

---

## 21. QUIZ TAKING

1. `QuizTakingFrame` opens as a new `JFrame` window
2. Questions displayed one at a time
3. Four option cards rendered as clickable panels
4. Selected answer highlighted in blue
5. User can navigate with PREVIOUS / NEXT buttons
6. Answer selections are preserved when navigating back
7. Last question shows SUBMIT QUIZ button
8. Clicking SUBMIT → confirmation dialog
9. Score calculated, saved to DB, `ResultFrame` opens

---

## 22. TIMER

```java
countdownTimer = new Timer(1000, e -> {
    secondsLeft--;
    updateTimerDisplay();
    if (secondsLeft <= 0) {
        stopTimer();
        submitQuiz(true);  // auto-submit
    } else if (secondsLeft <= 120) {
        timerLabel.setForeground(RED);
        timerWarning.setText("⚠ Time is running out!");
    }
});
countdownTimer.start();
```

- Uses `javax.swing.Timer` which fires on the EDT (thread-safe)
- Updates display every 1 second
- Red warning color + message when < 2 minutes remain
- Auto-submits when `secondsLeft == 0`

---

## 23. RESULT CALCULATION

```java
// Score calculation in QuizTakingFrame.submitQuiz()
int score = 0;
for (int i = 0; i < questions.size(); i++) {
    if (userAnswers[i] != null && questions.get(i).isCorrect(userAnswers[i])) {
        score++;
    }
}
double percentage = (total > 0) ? ((double) score / total) * 100.0 : 0.0;
```

Results stored in DB via:
```java
quizDAO.saveResult(userId, quizId, score, totalQuestions);
```

Grade assignment:
- A+ (≥90%), A (≥80%), B (≥70%), C (≥60%), D (≥50%), F (<50%)

---

## 24. GUI/UX DESIGN

### Layout Managers Used
| Screen | Layout Manager |
|--------|---------------|
| Login | `GridLayout(1,2)` — Left/Right split |
| Dashboard | `BorderLayout` — Sidebar + Main |
| Quiz cards | `BoxLayout` — vertical list |
| Stat cards | `GridLayout(1,4)` — horizontal row |
| Quiz taking | `BorderLayout` — Top/Center/Bottom |
| Options | `GridLayout(2,2)` — 2×2 grid |
| Result | `BorderLayout` — Left summary + Right review |

### Navigation
- `CardLayout` in `QuizFrame` switches between screens without opening new windows
- `QuizTakingFrame` and `ResultFrame` are separate `JFrame` windows (for full-screen focus)
- Sidebar present on all authenticated screens

---

## 25. NEO-BRUTALIST DESIGN

### Color Palette
```
Background: #FFF8E7  (warm cream)
Black:      #111111
White:      #FFFFFF
Yellow:     #FFD93D  (primary accent)
Pink:       #FF6B9D
Blue:       #4D96FF
Green:      #6BCB77  (success)
Orange:     #FF914D
Red:        #FF5C5C  (error/danger)
```

### Key Design Elements
- **Thick borders**: `MatteBorder(2, 2, 5, 5, BLACK)` — hard offset shadow effect
- **Bold typography**: Segoe UI, weights from Plain to Bold, sizes 12–80px
- **Flat colors**: No gradients; solid fills only
- **Hover effects**: Buttons shift with hard drop-shadow on hover via `paintComponent` override
- **Large interactive targets**: All buttons minimum 52px height
- **High contrast**: Dark text on light background; white text on dark buttons

---

## 26. TESTING

All features have been designed for testability:

- `DatabaseConnection.testConnection()` validates DB on startup
- All DAO methods return `boolean` or `null` for failure detection
- Error messages displayed via `JOptionPane` — no raw stack traces shown to users
- Input validation in all forms before DB interaction

---

## 27. TEST CASES

| # | Test Case | Input | Expected Result | Status |
|---|-----------|-------|-----------------|--------|
| 1 | Valid login (student) | student1 / pass123 | Dashboard shown | Pass |
| 2 | Valid login (admin) | admin / admin123 | Admin dashboard shown | Pass |
| 3 | Invalid password | student1 / wrong | Error message shown | Pass |
| 4 | Empty username | (blank) / pass | "Please enter username" | Pass |
| 5 | Empty password | user / (blank) | "Please enter password" | Pass |
| 6 | Register new user | Valid unique data | "Account created!" + redirect | Pass |
| 7 | Duplicate username | Existing username | "Username already taken" | Pass |
| 8 | Password mismatch | pass / different | "Passwords do not match" | Pass |
| 9 | Guest entry | Enter name | Quiz list shown | Pass |
| 10 | Guest no name | (blank name) | Error message | Pass |
| 11 | Quiz with 0 questions | Click START | Warning dialog | Pass |
| 12 | Answer selection | Click option A | Option A highlighted blue | Pass |
| 13 | Answer change | Click A then B | Only B highlighted | Pass |
| 14 | Navigate previous | On Q3, click ← | Q2 shown, Q3 answer preserved | Pass |
| 15 | Timer display | Start quiz | MM:SS countdown | Pass |
| 16 | Timer warning | < 2 min remain | Red timer + warning text | Pass |
| 17 | Auto-submit | Timer reaches 0 | Dialog + submit | Pass |
| 18 | Submit confirmation | Click SUBMIT | Confirmation dialog | Pass |
| 19 | Score calculation | 8/10 correct | 80% displayed | Pass |
| 20 | Result storage | Submit quiz | Saved in results table | Pass |
| 21 | Answer review | Click VIEW ANSWERS | Each Q shows correct/wrong | Pass |
| 22 | Create quiz | Valid form | Quiz created, open questions | Pass |
| 23 | Create quiz blank | Empty title | Validation error | Pass |
| 24 | Delete quiz | Confirm delete | Removed from DB + list | Pass |
| 25 | Cancel delete | Click NO | Nothing deleted | Pass |
| 26 | Add question | All fields filled | Question saved | Pass |
| 27 | Add question blank | Missing option | Validation error | Pass |
| 28 | Delete question | Confirm delete | Removed from list | Pass |
| 29 | Logout | Click Logout + Yes | Returns to login | Pass |
| 30 | Cancel logout | Click Logout + No | Stays on dashboard | Pass |
| 31 | DB unavailable | MySQL off | Error dialog at startup | Pass |
| 32 | View my results | Click My Results | History table shown | Pass |
| 33 | Admin all results | Click View Results | All users' results shown | Pass |
| 34 | Edit quiz | Change title | Updated in DB | Pass |
| 35 | Window resize | Resize window | Layout adjusts (no layout break) | Pass |

---

## 28. ADVANTAGES

1. **Offline**: No internet required — runs completely locally
2. **Fast**: Direct JDBC connection, no middleware
3. **Simple code**: 10 classes, easy to understand and modify
4. **Secure queries**: PreparedStatement prevents SQL injection
5. **Role-based access**: Admin/Student/Guest with appropriate restrictions
6. **Guest mode**: Zero friction for non-registered users
7. **Auto-scoring**: No manual evaluation needed
8. **History tracking**: All results saved permanently
9. **Visual design**: Modern UI stands out from typical college projects

---

## 29. LIMITATIONS

1. Passwords stored as plain text (suitable for prototype/academic use)
2. No image-based questions (text only)
3. No question shuffling or randomization
4. No multi-category filtering on quiz list
5. Single correct answer only (no multiple-select questions)
6. No export to PDF/Excel
7. No network/multi-user simultaneous access (single machine)
8. No password recovery mechanism

---

## 30. FUTURE ENHANCEMENTS

1. **Password hashing** with BCrypt or SHA-256
2. **Question randomization** — shuffle questions/options per attempt
3. **Question bank** — reuse questions across multiple quizzes
4. **Image questions** — embed images in question text
5. **Multiple correct answers** — checkbox-style questions
6. **Quiz categories filter** — search/filter quiz list
7. **Result export** — save results as PDF/CSV
8. **Email notifications** — results via email
9. **Network mode** — run as server, multiple students simultaneously
10. **Analytics** — charts showing quiz performance over time

---

## 31. CONCLUSION

The Quiz Management System successfully demonstrates a complete, functional desktop application built with Java Swing, MySQL, and JDBC. The system achieves its objectives of providing a user-friendly, role-based quiz platform that works offline without any complex infrastructure.

Key technical concepts demonstrated:
- Object-Oriented Programming (Encapsulation, Abstraction, Inheritance, Polymorphism)
- JDBC with PreparedStatement for safe database access
- Java Swing layout management and event handling
- Multi-screen navigation using CardLayout
- Timer-based quiz management with javax.swing.Timer

The Neo-Brutalist UI design ensures the application stands out visually while remaining functional and accessible. The clean 10-class architecture makes it easy to understand, modify, and demonstrate during project presentations and viva examinations.

---

## 32. VIVA QUESTIONS AND ANSWERS

### Java Fundamentals

**Q1. What is Java?**  
Java is a high-level, object-oriented, platform-independent programming language. Programs are compiled to bytecode that runs on the Java Virtual Machine (JVM), making them portable across platforms.

**Q2. What is the JVM?**  
The Java Virtual Machine is an interpreter that executes Java bytecode. It provides platform independence — "Write Once, Run Anywhere."

**Q3. What is the difference between JDK, JRE, and JVM?**  
- JVM: Runs bytecode
- JRE: JVM + standard libraries (needed to run Java programs)
- JDK: JRE + compiler + development tools (needed to develop Java programs)

**Q4. What are the OOP principles?**  
- **Encapsulation**: Wrapping data and methods in a class, hiding internal details
- **Abstraction**: Exposing only essential features, hiding implementation
- **Inheritance**: A class acquires properties of another class
- **Polymorphism**: Same method name behaves differently in different contexts

**Q5. How is Encapsulation demonstrated in your project?**  
In `User.java`, all fields (id, username, password, role, fullName) are `private`. They can only be accessed via `public` getters and setters. This prevents direct modification of sensitive data like password from outside the class.

**Q6. How is Abstraction demonstrated?**  
`UserDAO` and `QuizDAO` hide all SQL and JDBC complexity. When the GUI calls `userDAO.login("admin", "pass")`, it doesn't know about PreparedStatements, ResultSets, or database connections — these details are abstracted away.

**Q7. How is Inheritance used?**  
`QuizTakingFrame` and `ResultFrame` both `extend JFrame`. They inherit all JFrame functionality (window management, event handling, rendering) without rewriting it. Anonymous inner classes like `new MouseAdapter()` also use inheritance.

**Q8. What is Polymorphism?**  
Polymorphism means "many forms." In the project, `paintComponent()` is overridden in the custom button class to draw a Neo-Brutalist hard shadow. Java also uses method overloading (`User` has multiple constructors) which is compile-time polymorphism.

**Q9. What is a Constructor?**  
A constructor is a special method that initializes an object. It has the same name as the class and no return type. `User` has three constructors for different initialization scenarios.

**Q10. What is the difference between method overloading and overriding?**  
- **Overloading**: Same method name, different parameters (compile-time polymorphism)
- **Overriding**: Subclass redefines a parent class method (runtime polymorphism)

---

### Java Swing

**Q11. What is Java Swing?**  
Swing is a GUI toolkit in Java's standard library. It provides components like `JFrame`, `JPanel`, `JButton`, `JTextField`, etc. for building desktop applications. It is lightweight and platform-independent.

**Q12. What layout managers did you use?**  
- `BorderLayout`: Main window structure (North/South/East/West/Center)
- `GridLayout`: Stat cards in rows/columns
- `BoxLayout`: Sidebar items and form fields in vertical stacks
- `CardLayout`: Switching between screens (Login, Dashboard, etc.) in `QuizFrame`
- `GridBagLayout`: Centered forms (login card, guest card)

**Q13. What is CardLayout?**  
CardLayout manages a container as a "deck of cards," showing one panel at a time. Calling `cardLayout.show(panel, "LOGIN")` switches to the login screen without opening a new window.

**Q14. What is the Event Dispatch Thread (EDT)?**  
The EDT is the single thread Swing uses for all UI updates and event processing. Always launch Swing applications using `SwingUtilities.invokeLater()` to ensure thread safety.

**Q15. What is ActionListener?**  
`ActionListener` is an interface with the `actionPerformed()` method. It handles button clicks, menu selections, and timer events. In the project, it's implemented as lambda expressions: `btn.addActionListener(e -> { ... })`.

**Q16. What is javax.swing.Timer?**  
`javax.swing.Timer` fires ActionEvents at fixed intervals on the EDT. Used for the quiz countdown: fires every 1000ms (1 second), decrements `secondsLeft`, and updates the display.

---

### JDBC

**Q17. What is JDBC?**  
JDBC (Java Database Connectivity) is an API that allows Java programs to connect to relational databases using SQL. It provides `Connection`, `Statement`, `PreparedStatement`, `ResultSet`, and `SQLException`.

**Q18. What are the steps to connect to a database using JDBC?**  
1. Load driver: `Class.forName("com.mysql.cj.jdbc.Driver")`
2. Create connection: `DriverManager.getConnection(url, user, pass)`
3. Create statement: `conn.prepareStatement(sql)`
4. Execute: `stmt.executeQuery()` or `stmt.executeUpdate()`
5. Process results: iterate `ResultSet`
6. Close resources: `rs.close()`, `stmt.close()`, `conn.close()`

**Q19. What is PreparedStatement? Why is it better than Statement?**  
`PreparedStatement` pre-compiles the SQL with `?` placeholders. Values are bound using `setString()`, `setInt()`, etc. It prevents SQL injection because the user input cannot modify the query structure. `Statement` uses string concatenation which is vulnerable to injection attacks.

**Q20. What is SQL Injection?**  
SQL injection is an attack where malicious SQL is inserted into a query via user input. Example: entering `' OR '1'='1` in a password field. PreparedStatement prevents this by treating all bound parameters as literal values.

**Q21. What is a ResultSet?**  
A `ResultSet` is a table of data returned by a SELECT query. Iterate with `rs.next()`, access columns by name: `rs.getString("username")`, `rs.getInt("id")`.

**Q22. Why must database resources be closed?**  
Open connections, statements, and result sets consume server and memory resources. Not closing them causes "connection pool exhaustion" — the database runs out of available connections. Always close in `finally` blocks.

**Q23. What is a Singleton pattern? Where is it used?**  
Singleton ensures a class has only one instance. `DatabaseConnection` implements it — `connection` is a `static` field, and `getConnection()` reuses the existing connection if it's still open, rather than creating a new one each time.

---

### MySQL & SQL

**Q24. What is MySQL?**  
MySQL is an open-source relational database management system (RDBMS). Data is organized in tables with rows and columns, related via keys.

**Q25. What is a Primary Key?**  
A primary key uniquely identifies each row in a table. In this project, every table has an `id INT AUTO_INCREMENT PRIMARY KEY` column.

**Q26. What is a Foreign Key?**  
A foreign key links two tables. `questions.quiz_id` references `quizzes.id` — this ensures every question belongs to a valid quiz. `ON DELETE CASCADE` automatically deletes questions when a quiz is deleted.

**Q27. What is the difference between DELETE and TRUNCATE?**  
- `DELETE`: Removes specific rows (with WHERE), triggers ON DELETE actions, can be rolled back
- `TRUNCATE`: Removes all rows, faster, cannot use WHERE, cannot be rolled back

**Q28. What is an ENUM in MySQL?**  
`ENUM` restricts a column to a set of predefined values. `role ENUM('admin','student','guest')` ensures only these three values can be stored in the role column.

**Q29. What is a JOIN?**  
JOIN combines rows from two or more tables based on a related column. Used in `QuizDAO.getAllQuizzes()` to join `quizzes` with `categories` to get the category name:
```sql
SELECT q.*, c.name FROM quizzes q LEFT JOIN categories c ON q.category_id = c.id
```

**Q30. What is the difference between LEFT JOIN and INNER JOIN?**  
- `INNER JOIN`: Returns only rows with matching records in both tables
- `LEFT JOIN`: Returns all rows from the left table, with NULL for unmatched right rows
Used LEFT JOIN in quiz query so quizzes without a category still appear.

---

### Project-Specific

**Q31. How does guest mode work?**  
When a user clicks "CONTINUE AS GUEST," they enter their name. A user record is created in the `users` table with `role='guest'` and a unique auto-generated username (`guest_<timestamp>`). This user can take any quiz and their result is stored in the database.

**Q32. How is the timer implemented?**  
Using `javax.swing.Timer` which fires on the EDT every 1000ms. Each tick decrements `secondsLeft`. When it reaches 0, the quiz is automatically submitted. When < 120 seconds remain, the timer turns red and shows a warning.

**Q33. How is the score calculated?**  
After submission, each question's `correct_answer` field is compared with the stored `userAnswers[i]` array. Each match increments `score`. Percentage = (score / total) × 100.

**Q34. What happens when a quiz is deleted?**  
Because `questions.quiz_id` has `ON DELETE CASCADE`, deleting a quiz from the `quizzes` table automatically deletes all associated questions. A confirmation dialog is shown before deletion.

**Q35. Why do you use CardLayout?**  
CardLayout allows switching between multiple screens (Login, Dashboard, Admin, etc.) within a single JFrame without creating new windows. This provides smoother navigation and maintains a single application context.

**Q36. What is the Model-DAO pattern?**  
- **Model** (`User`, `Quiz`, `Question`): Plain Java objects that represent data
- **DAO** (Data Access Object) (`UserDAO`, `QuizDAO`): Classes that handle database operations for the models
- This separation makes the code modular — if you change the database, you only change the DAO, not the UI.

**Q37. How did you prevent SQL injection?**  
By using `PreparedStatement` with `?` placeholders for all user input. Example: `stmt.setString(1, username)` passes the username as a bound parameter, not as part of the SQL string.

**Q38. What are the user roles and how are they stored?**  
Three roles: 'admin', 'student', 'guest' — stored in `users.role` as MySQL ENUM. After login, the `User` object's `getRole()` is checked using `isAdmin()`, `isStudent()`, `isGuest()` methods.

**Q39. How do you handle database errors?**  
All JDBC operations are wrapped in try-catch blocks catching `SQLException`. Error messages are logged to `System.err`. User-friendly messages are shown via `JOptionPane`. Raw stack traces are never displayed to users. The application checks DB connectivity at startup.

**Q40. What is the Event Dispatch Thread and why is it important?**  
The EDT is the thread Swing uses for all UI painting and event processing. Modifying UI components from other threads causes race conditions and rendering bugs. Using `SwingUtilities.invokeLater()` in `Main.java` ensures the application starts on the EDT. `javax.swing.Timer` fires events on the EDT, making it safe for UI updates.

---

*End of Project Documentation*
