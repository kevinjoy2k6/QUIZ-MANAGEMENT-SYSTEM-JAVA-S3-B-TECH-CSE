-- ============================================================
-- Quiz Management System — Database Schema & Sample Data
-- MySQL 8.0+
-- ============================================================

-- Create and select database
CREATE DATABASE IF NOT EXISTS quiz_management
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE quiz_management;

-- ============================================================
-- TABLE 1: categories
-- ============================================================
CREATE TABLE IF NOT EXISTS categories (
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

-- ============================================================
-- TABLE 2: users
-- ============================================================
CREATE TABLE IF NOT EXISTS users (
    id        INT AUTO_INCREMENT PRIMARY KEY,
    username  VARCHAR(50)  NOT NULL UNIQUE,
    password  VARCHAR(255) NOT NULL,
    role      ENUM('admin','student','guest') NOT NULL DEFAULT 'student',
    full_name VARCHAR(100) NOT NULL
);

-- ============================================================
-- TABLE 3: quizzes
-- ============================================================
CREATE TABLE IF NOT EXISTS quizzes (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    title       VARCHAR(150) NOT NULL,
    description TEXT,
    category_id INT,
    time_limit  INT NOT NULL DEFAULT 10,   -- minutes
    CONSTRAINT fk_quiz_category
        FOREIGN KEY (category_id) REFERENCES categories(id)
        ON DELETE SET NULL
);

-- ============================================================
-- TABLE 4: questions
-- ============================================================
CREATE TABLE IF NOT EXISTS questions (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    quiz_id        INT NOT NULL,
    question_text  TEXT NOT NULL,
    option_a       VARCHAR(255) NOT NULL,
    option_b       VARCHAR(255) NOT NULL,
    option_c       VARCHAR(255) NOT NULL,
    option_d       VARCHAR(255) NOT NULL,
    correct_answer CHAR(1) NOT NULL,       -- 'A','B','C','D'
    CONSTRAINT fk_question_quiz
        FOREIGN KEY (quiz_id) REFERENCES quizzes(id)
        ON DELETE CASCADE
);

-- ============================================================
-- TABLE 5: results
-- ============================================================
CREATE TABLE IF NOT EXISTS results (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    user_id         INT NOT NULL,
    quiz_id         INT NOT NULL,
    score           INT NOT NULL DEFAULT 0,
    total_questions INT NOT NULL DEFAULT 0,
    percentage      DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    attempt_date    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_result_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_result_quiz
        FOREIGN KEY (quiz_id) REFERENCES quizzes(id)
        ON DELETE CASCADE
);

-- ============================================================
-- SAMPLE DATA
-- ============================================================

-- Categories
INSERT INTO categories (name) VALUES
    ('Java Programming'),
    ('Data Structures'),
    ('Computer Networks'),
    ('Operating Systems'),
    ('Database Management');

-- Users
-- Admin: username=admin, password=admin123
-- Student: username=student1, password=pass123
INSERT INTO users (username, password, role, full_name) VALUES
    ('admin',    'admin123',  'admin',   'Administrator'),
    ('student1', 'pass123',   'student', 'Alice Johnson'),
    ('student2', 'pass123',   'student', 'Bob Smith');

-- Quizzes
INSERT INTO quizzes (title, description, category_id, time_limit) VALUES
    ('Java Basics',
     'Test your knowledge of core Java concepts including OOP, data types, and control flow.',
     1, 10),
    ('Data Structures',
     'Questions on arrays, linked lists, stacks, queues, trees, and graphs.',
     2, 15),
    ('Computer Networks',
     'Explore networking protocols, OSI model, TCP/IP, and more.',
     3, 12);

-- Questions for Quiz 1: Java Basics (quiz_id = 1)
INSERT INTO questions (quiz_id, question_text, option_a, option_b, option_c, option_d, correct_answer) VALUES
(1, 'What is the default value of an int variable in Java?',
    '0', 'null', '-1', 'undefined', 'A'),

(1, 'Which keyword is used to define a class in Java?',
    'define', 'struct', 'class', 'object', 'C'),

(1, 'What does JVM stand for?',
    'Java Virtual Machine', 'Java Variable Method', 'Java Verified Module', 'Java Version Manager', 'A'),

(1, 'Which OOP concept allows a class to inherit properties from another class?',
    'Encapsulation', 'Polymorphism', 'Abstraction', 'Inheritance', 'D'),

(1, 'What is the size of an int in Java?',
    '2 bytes', '4 bytes', '8 bytes', '16 bytes', 'B'),

(1, 'Which method is the entry point of a Java program?',
    'start()', 'init()', 'main()', 'run()', 'C'),

(1, 'What is the parent class of all Java classes?',
    'Base', 'Super', 'Object', 'Root', 'C'),

(1, 'Which keyword prevents a class from being inherited?',
    'static', 'sealed', 'final', 'private', 'C'),

(1, 'What does the "this" keyword refer to in Java?',
    'The parent class', 'The current object', 'A static method', 'The main method', 'B'),

(1, 'Which exception is thrown when dividing by zero in Java?',
    'NullPointerException', 'ArithmeticException', 'NumberFormatException', 'ArrayIndexOutOfBoundsException', 'B');

-- Questions for Quiz 2: Data Structures (quiz_id = 2)
INSERT INTO questions (quiz_id, question_text, option_a, option_b, option_c, option_d, correct_answer) VALUES
(2, 'Which data structure follows the LIFO principle?',
    'Queue', 'Stack', 'Array', 'Linked List', 'B'),

(2, 'What is the time complexity of binary search?',
    'O(n)', 'O(n^2)', 'O(log n)', 'O(1)', 'C'),

(2, 'In a linked list, each node contains:',
    'Only data', 'Only a pointer', 'Data and a pointer to next node', 'Data and an index', 'C'),

(2, 'Which data structure is used to implement recursion?',
    'Queue', 'Stack', 'Tree', 'Graph', 'B'),

(2, 'What is the maximum number of children a binary tree node can have?',
    '1', '2', '3', 'Unlimited', 'B'),

(2, 'Which sorting algorithm has the best average-case time complexity?',
    'Bubble Sort', 'Selection Sort', 'Quick Sort', 'Insertion Sort', 'C'),

(2, 'A queue follows which principle?',
    'LIFO', 'FIFO', 'FILO', 'LILO', 'B'),

(2, 'What is the time complexity of accessing an element in an array by index?',
    'O(n)', 'O(log n)', 'O(n^2)', 'O(1)', 'D'),

(2, 'Which tree traversal visits left subtree, root, then right subtree?',
    'Pre-order', 'Post-order', 'In-order', 'Level-order', 'C'),

(2, 'A hash table provides average time complexity of ___ for search.',
    'O(n)', 'O(log n)', 'O(1)', 'O(n^2)', 'C'),

(2, 'Which graph traversal uses a queue?',
    'DFS', 'BFS', 'Both', 'Neither', 'B'),

(2, 'What is the space complexity of merge sort?',
    'O(1)', 'O(log n)', 'O(n)', 'O(n^2)', 'C'),

(2, 'AVL tree is a type of:',
    'Binary Search Tree', 'Self-balancing BST', 'B-Tree', 'Red-Black Tree', 'B'),

(2, 'Doubly linked list allows traversal in:',
    'Only forward direction', 'Only backward direction', 'Both directions', 'Random access', 'C'),

(2, 'Which data structure is ideal for implementing a priority queue?',
    'Stack', 'Queue', 'Heap', 'Array', 'C');

-- Questions for Quiz 3: Computer Networks (quiz_id = 3)
INSERT INTO questions (quiz_id, question_text, option_a, option_b, option_c, option_d, correct_answer) VALUES
(3, 'How many layers does the OSI model have?',
    '4', '5', '6', '7', 'D'),

(3, 'Which protocol is used to assign IP addresses automatically?',
    'DNS', 'DHCP', 'FTP', 'SMTP', 'B'),

(3, 'What does HTTP stand for?',
    'HyperText Transfer Protocol', 'High Transfer Text Protocol', 'Hyperlink Text Transfer Process', 'HyperText Transmission Protocol', 'A'),

(3, 'Which layer of the OSI model is responsible for routing?',
    'Data Link', 'Transport', 'Network', 'Session', 'C'),

(3, 'Which protocol provides reliable, connection-oriented communication?',
    'UDP', 'IP', 'TCP', 'ICMP', 'C'),

(3, 'The default port number for HTTPS is:',
    '80', '21', '443', '22', 'C'),

(3, 'What does DNS stand for?',
    'Data Network System', 'Domain Name System', 'Dynamic Network Service', 'Direct Name Server', 'B'),

(3, 'Which topology connects every device to a central hub?',
    'Ring', 'Bus', 'Mesh', 'Star', 'D'),

(3, 'IPv4 address length is:',
    '16 bits', '32 bits', '64 bits', '128 bits', 'B'),

(3, 'Which protocol is used for sending emails?',
    'POP3', 'IMAP', 'SMTP', 'FTP', 'C'),

(3, 'A MAC address operates at which OSI layer?',
    'Network', 'Transport', 'Data Link', 'Physical', 'C'),

(3, 'What is the purpose of a firewall?',
    'Speed up network', 'Filter network traffic', 'Assign IP addresses', 'Connect to the internet', 'B');
