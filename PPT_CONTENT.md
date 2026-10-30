# PPT CONTENT — Quiz Management System
## B.Tech CSE Project Presentation
### Neo-Brutalist Theme: Background #FFF8E7 | Black borders | Yellow/Blue/Green accents

---

## SLIDE 1 — TITLE SLIDE

```
┌─────────────────────────────────────────────────────────────────┐
│                                                                  │
│  ████████████████████████████████████████████████████████████  │
│                                                                  │
│         QUIZ MANAGEMENT SYSTEM                                   │
│                                                                  │
│         A Desktop Application                                    │
│         Java Swing  •  MySQL  •  JDBC                           │
│                                                                  │
│  ─────────────────────────────────────────────────────────────  │
│                                                                  │
│  Submitted By:    [Your Name]                                    │
│  Roll No:         [Your Roll No]                                 │
│  Branch:          B.Tech CSE                                     │
│  Institution:     [Your College Name]                            │
│  Guide:           [Guide Name]                                   │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

**Background:** Black | **Title:** YELLOW bold | **Details:** White

---

## SLIDE 2 — INTRODUCTION

**Heading:** INTRODUCTION

**Content:**
- The Quiz Management System is a desktop application for creating and taking quizzes
- Built with Java Swing (GUI), MySQL (Database), and JDBC (Connectivity)
- Designed for educational environments — works completely offline
- Supports three user roles: Admin, Student, and Guest

**Visual:** Three role cards side by side
```
┌──────────┐  ┌──────────┐  ┌──────────┐
│  ADMIN   │  │ STUDENT  │  │  GUEST   │
│   ⚙️     │  │   📚     │  │   👤     │
│ Manages  │  │  Takes   │  │  Quick   │
│ quizzes  │  │  quizzes │  │  access  │
└──────────┘  └──────────┘  └──────────┘
```

---

## SLIDE 3 — PROBLEM STATEMENT

**Heading:** PROBLEM STATEMENT

**Content:**
❌ Paper-based quizzes are slow to prepare and evaluate  
❌ Online platforms require internet access  
❌ No instant automated scoring  
❌ No permanent result storage  
❌ Difficult to manage large question banks  

**Our Solution:**
✅ Offline desktop application  
✅ Automated scoring and result storage  
✅ Admin panel for easy quiz management  

---

## SLIDE 4 — OBJECTIVES

**Heading:** OBJECTIVES

**Seven objectives in numbered list:**

1. Develop a Java Swing desktop application
2. Implement Admin, Student, and Guest roles
3. Enable full CRUD operations on quizzes and questions
4. Build a timed quiz interface with countdown
5. Auto-calculate and store quiz results
6. Provide guest access without registration
7. Design a modern Neo-Brutalist interface

---

## SLIDE 5 — EXISTING vs PROPOSED SYSTEM

**Heading:** EXISTING vs PROPOSED SYSTEM

| Existing System | Proposed System |
|----------------|-----------------|
| Paper-based | Digital Desktop App |
| Manual scoring | Automatic scoring |
| No result storage | MySQL database storage |
| Internet required | Works offline |
| No guest access | Guest mode supported |
| Plain interface | Neo-Brutalist modern UI |
| Single user | Multi-role system |

---

## SLIDE 6 — FEATURES

**Heading:** KEY FEATURES

**Three columns:**

**Admin Features:**
- Create / Edit / Delete Quizzes
- Add / Delete Questions
- View all users' results
- Dashboard with statistics

**Student Features:**
- Register & Login
- Browse available quizzes
- Take timed quizzes
- View result history

**Guest Features:**
- No registration needed
- Enter name → Take quiz
- View result instantly
- Same quiz experience

---

## SLIDE 7 — TECHNOLOGY STACK

**Heading:** TECHNOLOGY STACK

**Visual block diagram:**

```
┌─────────────────────────────────┐
│   FRONTEND / GUI                │
│   Java Swing (JFrame, JPanel,   │
│   CardLayout, Timer)            │
└───────────────┬─────────────────┘
                │ JDBC
┌───────────────▼─────────────────┐
│   DATABASE CONNECTIVITY          │
│   MySQL Connector/J (JDBC)       │
│   PreparedStatement              │
└───────────────┬─────────────────┘
                │ SQL
┌───────────────▼─────────────────┐
│   DATABASE                      │
│   MySQL 8.0                     │
│   5 Tables with Foreign Keys    │
└─────────────────────────────────┘
```

---

## SLIDE 8 — SYSTEM ARCHITECTURE

**Heading:** SYSTEM ARCHITECTURE

```
PRESENTATION LAYER
  QuizFrame  →  QuizTakingFrame  →  ResultFrame

        ↓ calls

DATA ACCESS LAYER
  UserDAO          QuizDAO

        ↓ uses

MODEL LAYER
  User    Quiz    Question

        ↓ connects via

DATABASE LAYER
  DatabaseConnection (Singleton)

        ↓ JDBC

MySQL Database
  users | quizzes | questions | results | categories
```

---

## SLIDE 9 — CLASS ARCHITECTURE (10 CLASSES)

**Heading:** CLASS ARCHITECTURE — 10 JAVA CLASSES

**Two-column layout:**

**Models (3):**
- `User.java` — User data model
- `Quiz.java` — Quiz data model
- `Question.java` — Question data model

**Data Access (2):**
- `UserDAO.java` — User DB operations
- `QuizDAO.java` — Quiz/Question/Result DB operations

**GUI (3):**
- `QuizFrame.java` — Main window (11 screens)
- `QuizTakingFrame.java` — Quiz-taking interface
- `ResultFrame.java` — Score & review screen

**Infrastructure (2):**
- `DatabaseConnection.java` — JDBC singleton
- `Main.java` — Application entry point

---

## SLIDE 10 — DATABASE DESIGN

**Heading:** DATABASE DESIGN — 5 TABLES

**Visual schema:**

```
categories ──FK──► quizzes ──FK──► questions
                      │
                      └──FK──► results ◄──FK── users
```

**Tables:**
| Table | Key Columns |
|-------|-------------|
| users | id, username, password, role, full_name |
| quizzes | id, title, category_id FK, time_limit |
| questions | id, quiz_id FK, options A-D, correct_answer |
| results | id, user_id FK, quiz_id FK, score, percentage |
| categories | id, name |

---

## SLIDE 11 — ER DIAGRAM

**Heading:** ER DIAGRAM

```
[categories] ──1────M── [quizzes] ──1────M── [questions]
                             │
                             └──1────M──[results]──M────1──[users]
```

**Relationships:**
- One category has many quizzes (1:M)
- One quiz has many questions (1:M) — CASCADE DELETE
- One user has many results (1:M)
- One quiz has many results (1:M)

---

## SLIDE 12 — LOGIN & REGISTRATION

**Heading:** LOGIN & REGISTRATION

**Split layout:**

LEFT (Login Screen):
```
QUIZ
MANAGEMENT      │  LOGIN
SYSTEM          │
                │  Username [_________]
Test your       │  Password [_________]
knowledge.      │
                │  [ LOGIN → ]
                │
                │  [ REGISTER ]
                │  [ CONTINUE AS GUEST ]
```

Features:
- Input validation
- Error messages
- Guest mode button
- Keyboard-friendly (Enter key submits)

---

## SLIDE 13 — ADMIN DASHBOARD

**Heading:** ADMIN DASHBOARD

**Screenshot description:**
- Black sidebar: Dashboard | Manage Quizzes | View Results | Logout
- 4 stat cards: Total Quizzes | Total Questions | Total Users | Total Attempts
- Quick action buttons: + CREATE QUIZ | MANAGE QUIZZES | VIEW ALL RESULTS

**Admin Quiz Table:**
| # | Title | Category | Questions | Time | Actions |
|---|-------|----------|-----------|------|---------|
| 1 | Java Basics | Java | 10 | 10 min | EDIT DELETE QUESTIONS |

---

## SLIDE 14 — QUIZ MANAGEMENT

**Heading:** QUIZ MANAGEMENT (ADMIN)

**Create Quiz Form:**
- Quiz Title field
- Description field
- Category dropdown
- Time Limit (minutes)
- [ CREATE QUIZ → ] button

**After Creation:**
- Add questions form appears
- Question text + Option A/B/C/D
- Correct Answer dropdown (A/B/C/D)
- [ SAVE QUESTION → ]
- Existing questions listed with DELETE button

---

## SLIDE 15 — QUIZ TAKING INTERFACE

**Heading:** QUIZ TAKING INTERFACE

```
JAVA BASICS                Question 4 / 10      TIME LEFT
                                                  08:42
████████░░░░░░░░░░░░   Progress: 40%

┌──────────────────────────────────────────────────────┐
│  QUESTION 4                                           │
│                                                       │
│  What is inheritance in Java?                         │
│                                                       │
│  ┌─────────────────────────┐  ┌────────────────────┐ │
│  │ A  Reusing properties   │  │ B  Creating DB     │ │
│  └─────────────────────────┘  └────────────────────┘ │
│  ┌─────────────────────────┐  ┌────────────────────┐ │
│  │ C  Compiling code       │  │ D  Handle except.  │ │
│  └─────────────────────────┘  └────────────────────┘ │
└──────────────────────────────────────────────────────┘

   [ ← PREVIOUS ]                      [ NEXT → ]
```

---

## SLIDE 16 — RESULT SCREEN

**Heading:** RESULT SCREEN

```
┌────────────────────┐  ┌─────────────────────────────────┐
│  QUIZ COMPLETE!    │  │  ANSWER REVIEW                   │
│  Java Basics       │  │                                  │
│                    │  │  Q1. What is JVM?               │
│       87%          │  │  ✓ CORRECT                       │
│       SCORE        │  │                                  │
│                    │  │  Q2. What is int size?          │
│   8 / 10 CORRECT   │  │  ✗ WRONG                        │
│                    │  │  Your: A — 2 bytes              │
│  CORRECT    8      │  │  Correct: B — 4 bytes           │
│  WRONG      2      │  │                                  │
│  PERCENTAGE 80%    │  │  Q3. Entry point of Java?       │
│                    │  │  ✓ CORRECT                       │
│  ⭐ Grade: A        │  │                                  │
│                    │  └─────────────────────────────────┘
│  [ VIEW ANSWERS ]  │
│  [ DASHBOARD ]     │
└────────────────────┘
```

---

## SLIDE 17 — NEO-BRUTALIST UI/UX

**Heading:** NEO-BRUTALIST DESIGN LANGUAGE

**Design principles shown:**

```
COLOR PALETTE:
■ #FFF8E7  ■ #111111  ■ FFD93D  ■ 4D96FF  ■ 6BCB77  ■ FF5C5C

BORDER STYLE:        BUTTON STYLE:         TYPOGRAPHY:
┌──────────┐         Normal:               HEADING (Bold 24px)
│  CARD    │         ┌────────┐            Subheading (Bold 16px)
│          │         │ CLICK  │            Body text (Regular 14px)
└──────────┘         └────────┘
████ Hard shadow      Hover:               FONT: Segoe UI
                     ┌────────┐
                     │ CLICK  │
                     └────────┘
                     ████████
```

---

## SLIDE 18 — OOP CONCEPTS

**Heading:** OOP CONCEPTS DEMONSTRATED

**Four cards:**

**Encapsulation**
```java
// User.java
private String password;
public String getPassword() {
    return password;
}
```

**Abstraction**
```java
// GUI calls simple method
User u = userDAO.login(user, pass);
// JDBC details hidden in DAO
```

**Inheritance**
```java
class QuizTakingFrame
    extends JFrame { ... }
```

**Polymorphism**
```java
// Override paintComponent
// for custom button rendering
protected void paintComponent
    (Graphics g) { ... }
```

---

## SLIDE 19 — JDBC IMPLEMENTATION

**Heading:** JDBC IMPLEMENTATION

**Code snippets:**

```java
// Step 1: Load Driver
Class.forName("com.mysql.cj.jdbc.Driver");

// Step 2: Get Connection
Connection conn = DriverManager.getConnection(url, user, pass);

// Step 3: PreparedStatement (SQL-Injection Safe!)
String sql = "SELECT * FROM users WHERE username=? AND password=?";
PreparedStatement stmt = conn.prepareStatement(sql);
stmt.setString(1, username);  // bound safely
stmt.setString(2, password);

// Step 4: Execute
ResultSet rs = stmt.executeQuery();

// Step 5: Close Resources
rs.close(); stmt.close();
```

---

## SLIDE 20 — TESTING

**Heading:** TESTING & RESULTS

**Test Summary:**

| Category | Tests | Passed |
|----------|-------|--------|
| Authentication | 5 | 5 |
| Registration | 4 | 4 |
| Guest Mode | 2 | 2 |
| Quiz Taking | 8 | 8 |
| Timer | 3 | 3 |
| Submission | 4 | 4 |
| Admin CRUD | 7 | 7 |
| DB Error | 2 | 2 |
| **Total** | **35** | **35** |

**Result: 100% test cases passed ✅**

---

## SLIDE 21 — ADVANTAGES & LIMITATIONS

**Heading:** ADVANTAGES & LIMITATIONS

**Advantages:**
✅ Works completely offline  
✅ No internet required  
✅ Automatic scoring and grading  
✅ Role-based access control  
✅ Guest mode — zero friction  
✅ SQL injection protection (PreparedStatement)  
✅ Modern, professional UI design  
✅ Permanent result storage  

**Limitations:**
⚠ Plain text passwords (prototype level)  
⚠ No image-based questions  
⚠ No question shuffling  
⚠ Single machine (no network)  

---

## SLIDE 22 — CONCLUSION

**Heading:** CONCLUSION

**Content:**
The Quiz Management System successfully achieves all objectives:

- ✅ 10 Java classes with clean Model-DAO architecture
- ✅ Three user roles with appropriate access control
- ✅ Full quiz and question management for admins
- ✅ Timed quiz-taking with auto-submit
- ✅ Instant result calculation and permanent storage
- ✅ Guest mode for friction-free participation
- ✅ Modern Neo-Brutalist design
- ✅ PreparedStatement for SQL injection prevention

**Key Concepts Demonstrated:**
OOP (Encapsulation, Abstraction, Inheritance, Polymorphism)  
JDBC | MySQL | Java Swing | Event-Driven Programming

**Thank You!**

---

## PPT DESIGN NOTES

**Fonts:**
- Heading slides: Impact or Bebas Neue (bold, large)
- Body: Calibri or Segoe UI

**Colors:**
- Slide background: #FFF8E7 (cream)
- Section headers: Black rectangle + Yellow text
- Code blocks: Black background + Yellow/Green monospace
- Success highlights: #6BCB77 (green)
- Error highlights: #FF5C5C (red)
- Accent elements: #4D96FF (blue)

**Slide template:**
- Left border: 8px thick black vertical bar
- Title: Bold, 36pt, black
- Subtitles: Bold, 20pt, colored
- Body: Regular, 16pt, dark grey

**Consistent elements:**
- Slide number in bottom-right corner
- "QUIZ MANAGEMENT SYSTEM" in top-left corner (small, grey)
- Black thick border on code blocks
