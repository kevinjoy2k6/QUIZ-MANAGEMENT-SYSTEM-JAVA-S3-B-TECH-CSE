/**
 * QuizFrame.java
 *
 * The main application window. Uses CardLayout to switch between screens:
 *   - LOGIN        (login form)
 *   - REGISTER     (registration form)
 *   - GUEST        (guest name entry)
 *   - DASHBOARD    (student dashboard)
 *   - ADMIN        (admin dashboard)
 *   - QUIZ_LIST    (select quiz to take)
 *   - MY_RESULTS   (user's result history)
 *   - ADMIN_QUIZZES  (admin quiz table)
 *   - ADMIN_RESULTS  (all results for admin)
 *   - EDIT_QUIZ    (create/edit quiz form)
 *   - QUESTIONS    (question management for a quiz)
 *
 * OOP Concept: Encapsulation (private fields), Abstraction (helper methods hide Swing detail)
 */

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class QuizFrame extends JFrame {

    // ─── CONSTANTS — NEO-BRUTALIST PALETTE ────────────────────────────────────
    static final Color BG         = new Color(0xFF, 0xF8, 0xE7);  // warm cream
    static final Color BLACK      = new Color(0x11, 0x11, 0x11);
    static final Color WHITE      = Color.WHITE;
    static final Color YELLOW     = new Color(0xFF, 0xD9, 0x3D);
    static final Color PINK       = new Color(0xFF, 0x6B, 0x9D);
    static final Color BLUE       = new Color(0x4D, 0x96, 0xFF);
    static final Color GREEN      = new Color(0x6B, 0xCB, 0x77);
    static final Color ORANGE     = new Color(0xFF, 0x91, 0x4D);
    static final Color RED        = new Color(0xFF, 0x5C, 0x5C);
    static final Color CARD_BG    = new Color(0xFF, 0xFF, 0xFF);

    static final Font FONT_HUGE   = new Font("Segoe UI", Font.BOLD, 40);
    static final Font FONT_BIG    = new Font("Segoe UI", Font.BOLD, 24);
    static final Font FONT_MED    = new Font("Segoe UI", Font.BOLD, 16);
    static final Font FONT_BODY   = new Font("Segoe UI", Font.PLAIN, 14);
    static final Font FONT_SMALL  = new Font("Segoe UI", Font.PLAIN, 12);
    static final Font FONT_LABEL  = new Font("Segoe UI", Font.BOLD, 13);

    // ─── PANEL CARD NAMES ─────────────────────────────────────────────────────
    private static final String CARD_LOGIN          = "LOGIN";
    private static final String CARD_REGISTER       = "REGISTER";
    private static final String CARD_GUEST          = "GUEST";
    private static final String CARD_DASHBOARD      = "DASHBOARD";
    private static final String CARD_ADMIN          = "ADMIN";
    private static final String CARD_QUIZ_LIST      = "QUIZ_LIST";
    private static final String CARD_MY_RESULTS     = "MY_RESULTS";
    private static final String CARD_ADMIN_QUIZZES  = "ADMIN_QUIZZES";
    private static final String CARD_ADMIN_RESULTS  = "ADMIN_RESULTS";
    private static final String CARD_EDIT_QUIZ      = "EDIT_QUIZ";
    private static final String CARD_QUESTIONS      = "QUESTIONS";

    // ─── STATE ────────────────────────────────────────────────────────────────
    private CardLayout cardLayout;
    private JPanel     cardPanel;
    private User       currentUser;          // logged-in user
    private Quiz       editingQuiz;          // quiz being created/edited
    private UserDAO    userDAO  = new UserDAO();
    private QuizDAO    quizDAO  = new QuizDAO();

    // Kept as fields so we can refresh them when switching cards
    private JPanel     dashboardPanel;
    private JPanel     adminPanel;
    private JPanel     quizListPanel;
    private JPanel     myResultsPanel;
    private JPanel     adminQuizzesPanel;
    private JPanel     adminResultsPanel;
    private JPanel     editQuizPanel;
    private JPanel     questionsPanel;

    // ─── CONSTRUCTOR ──────────────────────────────────────────────────────────

    public QuizFrame() {
        setTitle("Quiz Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1280, 720));
        setSize(1366, 768);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG);

        cardLayout = new CardLayout();
        cardPanel  = new JPanel(cardLayout);
        cardPanel.setBackground(BG);

        // Build each screen
        cardPanel.add(buildLoginPanel(),    CARD_LOGIN);
        cardPanel.add(buildRegisterPanel(), CARD_REGISTER);
        cardPanel.add(buildGuestPanel(),    CARD_GUEST);

        // Placeholder panels — built lazily after login
        dashboardPanel     = new JPanel(); dashboardPanel.setBackground(BG);
        adminPanel         = new JPanel(); adminPanel.setBackground(BG);
        quizListPanel      = new JPanel(); quizListPanel.setBackground(BG);
        myResultsPanel     = new JPanel(); myResultsPanel.setBackground(BG);
        adminQuizzesPanel  = new JPanel(); adminQuizzesPanel.setBackground(BG);
        adminResultsPanel  = new JPanel(); adminResultsPanel.setBackground(BG);
        editQuizPanel      = new JPanel(); editQuizPanel.setBackground(BG);
        questionsPanel     = new JPanel(); questionsPanel.setBackground(BG);

        cardPanel.add(dashboardPanel,    CARD_DASHBOARD);
        cardPanel.add(adminPanel,        CARD_ADMIN);
        cardPanel.add(quizListPanel,     CARD_QUIZ_LIST);
        cardPanel.add(myResultsPanel,    CARD_MY_RESULTS);
        cardPanel.add(adminQuizzesPanel, CARD_ADMIN_QUIZZES);
        cardPanel.add(adminResultsPanel, CARD_ADMIN_RESULTS);
        cardPanel.add(editQuizPanel,     CARD_EDIT_QUIZ);
        cardPanel.add(questionsPanel,    CARD_QUESTIONS);

        add(cardPanel);
        setVisible(true);
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  SCREEN: LOGIN
    // ══════════════════════════════════════════════════════════════════════════

    private JPanel buildLoginPanel() {
        JPanel root = new JPanel(new GridLayout(1, 2));
        root.setBackground(BG);

        // LEFT — branding
        JPanel left = new JPanel();
        left.setBackground(YELLOW);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBorder(new EmptyBorder(80, 60, 80, 60));

        JLabel logo = new JLabel("<html><div style='line-height:1.1'>QUIZ<br>MANAGEMENT<br>SYSTEM</div></html>");
        logo.setFont(FONT_HUGE);
        logo.setForeground(BLACK);
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel tagline = new JLabel("<html><br><br>Test your knowledge.<br>Track your progress.</html>");
        tagline.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        tagline.setForeground(BLACK);
        tagline.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Decorative block
        JPanel deco = new JPanel();
        deco.setBackground(BLACK);
        deco.setMaximumSize(new Dimension(80, 8));
        deco.setAlignmentX(Component.LEFT_ALIGNMENT);

        left.add(logo);
        left.add(Box.createVerticalStrut(20));
        left.add(deco);
        left.add(tagline);

        // RIGHT — form
        JPanel right = new JPanel();
        right.setBackground(BG);
        right.setLayout(new GridBagLayout());
        right.setBorder(new EmptyBorder(60, 70, 60, 70));

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(BG);
        form.setMaximumSize(new Dimension(400, 600));

        JLabel title = makeLabel("LOGIN", FONT_BIG, BLACK);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sub = makeLabel("Welcome back! Please sign in.", FONT_BODY, new Color(0x55, 0x55, 0x55));
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField userField = makeTextField("Username");
        JPasswordField passField = makePasswordField("Password");
        JLabel errorLabel = makeLabel("", FONT_SMALL, RED);
        errorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton loginBtn = makeButton("LOGIN  →", BLUE, WHITE);
        loginBtn.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel orLabel = makeLabel("─────────────────────────", FONT_SMALL, new Color(0xCC, 0xCC, 0xCC));
        orLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton registerBtn = makeButtonOutline("REGISTER");
        registerBtn.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton guestBtn = makeButtonOutline("CONTINUE AS GUEST");
        guestBtn.setBackground(new Color(0xFF, 0xF0, 0xD0));
        guestBtn.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Assemble form
        form.add(title);
        form.add(Box.createVerticalStrut(6));
        form.add(sub);
        form.add(Box.createVerticalStrut(28));
        form.add(makeFormLabel("USERNAME"));
        form.add(Box.createVerticalStrut(6));
        form.add(userField);
        form.add(Box.createVerticalStrut(16));
        form.add(makeFormLabel("PASSWORD"));
        form.add(Box.createVerticalStrut(6));
        form.add(passField);
        form.add(Box.createVerticalStrut(8));
        form.add(errorLabel);
        form.add(Box.createVerticalStrut(20));
        form.add(loginBtn);
        form.add(Box.createVerticalStrut(20));
        form.add(orLabel);
        form.add(Box.createVerticalStrut(12));
        form.add(registerBtn);
        form.add(Box.createVerticalStrut(10));
        form.add(guestBtn);

        right.add(form);

        // ─── EVENT HANDLERS ────────────────────────────────────────────────
        loginBtn.addActionListener(e -> {
            String user = userField.getText().trim();
            String pass = new String(passField.getPassword()).trim();

            if (user.isEmpty()) { errorLabel.setText("⚠  Please enter your username."); return; }
            if (pass.isEmpty()) { errorLabel.setText("⚠  Please enter your password."); return; }

            User u = userDAO.login(user, pass);
            if (u == null) {
                errorLabel.setText("⚠  Invalid username or password.");
                passField.setText("");
            } else {
                errorLabel.setText("");
                currentUser = u;
                userField.setText("");
                passField.setText("");
                if (u.isAdmin()) {
                    refreshAdminPanel();
                    showCard(CARD_ADMIN);
                } else {
                    refreshDashboard();
                    showCard(CARD_DASHBOARD);
                }
            }
        });

        // Allow Enter key to submit login
        ActionListener loginAction = loginBtn.getActionListeners()[0];
        userField.addActionListener(loginAction);
        passField.addActionListener(loginAction);

        registerBtn.addActionListener(e -> showCard(CARD_REGISTER));
        guestBtn.addActionListener(e -> showCard(CARD_GUEST));

        root.add(left);
        root.add(right);
        return root;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  SCREEN: REGISTER
    // ══════════════════════════════════════════════════════════════════════════

    private JPanel buildRegisterPanel() {
        JPanel root = new JPanel(new GridLayout(1, 2));
        root.setBackground(BG);

        // LEFT — branding
        JPanel left = new JPanel();
        left.setBackground(PINK);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBorder(new EmptyBorder(80, 60, 80, 60));

        JLabel logo = new JLabel("<html><div style='line-height:1.1'>CREATE<br>YOUR<br>ACCOUNT</div></html>");
        logo.setFont(FONT_HUGE);
        logo.setForeground(WHITE);
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel tagline = new JLabel("<html><br>Join thousands of learners.<br>Start testing today.</html>");
        tagline.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        tagline.setForeground(WHITE);
        tagline.setAlignmentX(Component.LEFT_ALIGNMENT);

        left.add(logo);
        left.add(Box.createVerticalStrut(20));
        left.add(tagline);

        // RIGHT — form
        JPanel right = new JPanel(new GridBagLayout());
        right.setBackground(BG);
        right.setBorder(new EmptyBorder(40, 70, 40, 70));

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(BG);

        JLabel title   = makeLabel("REGISTER", FONT_BIG, BLACK);
        JTextField nameField     = makeTextField("Full Name");
        JTextField usernameField = makeTextField("Username");
        JPasswordField passField  = makePasswordField("Password");
        JPasswordField pass2Field = makePasswordField("Confirm Password");
        JLabel msgLabel = makeLabel("", FONT_SMALL, RED);
        JButton createBtn = makeButton("CREATE ACCOUNT  →", GREEN, WHITE);
        JButton backBtn   = makeButtonOutline("← BACK TO LOGIN");

        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        msgLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        createBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        backBtn.setAlignmentX(Component.LEFT_ALIGNMENT);

        form.add(title);
        form.add(Box.createVerticalStrut(24));
        form.add(makeFormLabel("FULL NAME")); form.add(Box.createVerticalStrut(6)); form.add(nameField);
        form.add(Box.createVerticalStrut(14));
        form.add(makeFormLabel("USERNAME"));  form.add(Box.createVerticalStrut(6)); form.add(usernameField);
        form.add(Box.createVerticalStrut(14));
        form.add(makeFormLabel("PASSWORD"));  form.add(Box.createVerticalStrut(6)); form.add(passField);
        form.add(Box.createVerticalStrut(14));
        form.add(makeFormLabel("CONFIRM PASSWORD")); form.add(Box.createVerticalStrut(6)); form.add(pass2Field);
        form.add(Box.createVerticalStrut(8));
        form.add(msgLabel);
        form.add(Box.createVerticalStrut(20));
        form.add(createBtn);
        form.add(Box.createVerticalStrut(12));
        form.add(backBtn);

        right.add(form);

        createBtn.addActionListener(e -> {
            String name  = nameField.getText().trim();
            String uname = usernameField.getText().trim();
            String pass  = new String(passField.getPassword()).trim();
            String pass2 = new String(pass2Field.getPassword()).trim();

            if (name.isEmpty() || uname.isEmpty() || pass.isEmpty() || pass2.isEmpty()) {
                msgLabel.setForeground(RED); msgLabel.setText("⚠  All fields are required."); return;
            }
            if (uname.length() < 3) {
                msgLabel.setForeground(RED); msgLabel.setText("⚠  Username must be at least 3 characters."); return;
            }
            if (pass.length() < 4) {
                msgLabel.setForeground(RED); msgLabel.setText("⚠  Password must be at least 4 characters."); return;
            }
            if (!pass.equals(pass2)) {
                msgLabel.setForeground(RED); msgLabel.setText("⚠  Passwords do not match."); return;
            }
            if (userDAO.usernameExists(uname)) {
                msgLabel.setForeground(RED); msgLabel.setText("⚠  Username already taken. Choose another."); return;
            }

            User newUser = new User(uname, pass, "student", name);
            if (userDAO.register(newUser)) {
                msgLabel.setForeground(GREEN);
                msgLabel.setText("✓  Account created! You can now log in.");
                nameField.setText(""); usernameField.setText("");
                passField.setText(""); pass2Field.setText("");
                Timer t = new Timer(1800, ev -> showCard(CARD_LOGIN));
                t.setRepeats(false); t.start();
            } else {
                msgLabel.setForeground(RED); msgLabel.setText("⚠  Registration failed. Please try again.");
            }
        });

        backBtn.addActionListener(e -> showCard(CARD_LOGIN));
        root.add(left);
        root.add(right);
        return root;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  SCREEN: GUEST ENTRY
    // ══════════════════════════════════════════════════════════════════════════

    private JPanel buildGuestPanel() {
        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(BG);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BLACK, 3),
            new EmptyBorder(50, 60, 50, 60)
        ));
        card.setPreferredSize(new Dimension(480, 380));

        // Hard shadow
        card.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(3, 3, 7, 7, BLACK),
            new EmptyBorder(48, 58, 48, 58)
        ));

        JLabel title = makeLabel("GUEST MODE", FONT_BIG, BLACK);
        JLabel sub   = makeLabel("Enter your name to start a quiz", FONT_BODY, new Color(0x55,0x55,0x55));
        JTextField nameField = makeTextField("Your name");
        JLabel errLabel = makeLabel("", FONT_SMALL, RED);
        JButton startBtn = makeButton("START AS GUEST  →", ORANGE, WHITE);
        JButton backBtn  = makeButtonOutline("← BACK TO LOGIN");

        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);
        nameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        errLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        startBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        backBtn.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(title);
        card.add(Box.createVerticalStrut(6));
        card.add(sub);
        card.add(Box.createVerticalStrut(30));
        card.add(makeFormLabel("YOUR NAME"));
        card.add(Box.createVerticalStrut(8));
        card.add(nameField);
        card.add(Box.createVerticalStrut(8));
        card.add(errLabel);
        card.add(Box.createVerticalStrut(24));
        card.add(startBtn);
        card.add(Box.createVerticalStrut(12));
        card.add(backBtn);

        startBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) { errLabel.setText("⚠  Please enter your name."); return; }
            User guest = userDAO.createGuestUser(name);
            if (guest == null) { errLabel.setText("⚠  Could not connect to database."); return; }
            currentUser = guest;
            nameField.setText("");
            errLabel.setText("");
            refreshQuizList();
            showCard(CARD_QUIZ_LIST);
        });
        backBtn.addActionListener(e -> showCard(CARD_LOGIN));

        root.add(card);
        return root;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  SCREEN: STUDENT DASHBOARD
    // ══════════════════════════════════════════════════════════════════════════

    private void refreshDashboard() {
        dashboardPanel.removeAll();
        dashboardPanel.setLayout(new BorderLayout());
        dashboardPanel.setBackground(BG);

        dashboardPanel.add(buildSidebar(false), BorderLayout.WEST);

        JPanel main = new JPanel();
        main.setBackground(BG);
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBorder(new EmptyBorder(40, 40, 40, 40));

        // Welcome header
        JLabel welcome = makeLabel("Welcome, " + currentUser.getFullName() + "! 👋", FONT_BIG, BLACK);
        welcome.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Stats cards
        double[] stats = quizDAO.getUserStats(currentUser.getId());
        int totalQuizzes = quizDAO.getTotalQuizzes();
        JPanel statsRow = new JPanel(new GridLayout(1, 4, 16, 0));
        statsRow.setBackground(BG);
        statsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));
        statsRow.add(makeStatCard("AVAILABLE\nQUIZZES", String.valueOf(totalQuizzes), YELLOW));
        statsRow.add(makeStatCard("QUIZZES\nTAKEN",    String.valueOf((int)stats[0]),  BLUE));
        statsRow.add(makeStatCard("AVERAGE\nSCORE",    String.format("%.0f%%", stats[1]), GREEN));
        statsRow.add(makeStatCard("BEST\nSCORE",       String.format("%.0f%%", stats[2]), PINK));

        // Quiz cards below
        JLabel qLabel = makeLabel("AVAILABLE QUIZZES", FONT_MED, BLACK);
        qLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        qLabel.setBorder(new EmptyBorder(30, 0, 16, 0));

        JPanel quizzesGrid = new JPanel();
        quizzesGrid.setBackground(BG);
        quizzesGrid.setLayout(new BoxLayout(quizzesGrid, BoxLayout.Y_AXIS));
        quizzesGrid.setAlignmentX(Component.LEFT_ALIGNMENT);

        List<Quiz> quizzes = quizDAO.getAllQuizzes();
        if (quizzes.isEmpty()) {
            JLabel empty = makeLabel("No quizzes available yet. Check back soon!", FONT_BODY, new Color(0x88,0x88,0x88));
            empty.setAlignmentX(Component.LEFT_ALIGNMENT);
            quizzesGrid.add(empty);
        } else {
            for (Quiz q : quizzes) {
                int qCount = quizDAO.getQuestionCount(q.getId());
                quizzesGrid.add(buildQuizCard(q, qCount));
                quizzesGrid.add(Box.createVerticalStrut(12));
            }
        }

        JScrollPane scroll = new JScrollPane(quizzesGrid);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setBackground(BG);
        scroll.getViewport().setBackground(BG);
        scroll.setAlignmentX(Component.LEFT_ALIGNMENT);

        main.add(welcome);
        main.add(Box.createVerticalStrut(24));
        main.add(statsRow);
        main.add(qLabel);
        main.add(scroll);

        dashboardPanel.add(main, BorderLayout.CENTER);
        dashboardPanel.revalidate();
        dashboardPanel.repaint();
    }

    private JPanel buildQuizCard(Quiz q, int qCount) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(2, 2, 5, 5, BLACK),
            new EmptyBorder(16, 20, 16, 20)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        JPanel left = new JPanel();
        left.setBackground(WHITE);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel titleLabel = makeLabel(q.getTitle(), FONT_MED, BLACK);
        JLabel metaLabel  = makeLabel(qCount + " Questions  •  " + q.getTimeLimit() + " Minutes  •  " + q.getCategoryName(), FONT_SMALL, new Color(0x66,0x66,0x66));
        left.add(titleLabel);
        left.add(Box.createVerticalStrut(4));
        left.add(metaLabel);

        JButton startBtn = makeButton("START →", GREEN, WHITE);
        startBtn.setPreferredSize(new Dimension(130, 44));
        startBtn.addActionListener(e -> {
            if (qCount == 0) {
                JOptionPane.showMessageDialog(this, "This quiz has no questions yet.", "Empty Quiz", JOptionPane.WARNING_MESSAGE);
                return;
            }
            List<Question> questions = quizDAO.getQuestionsByQuizId(q.getId());
            q.setQuestions(questions);
            new QuizTakingFrame(this, currentUser, q, quizDAO);
            setVisible(false);
        });

        card.add(left, BorderLayout.CENTER);
        card.add(startBtn, BorderLayout.EAST);
        return card;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  SCREEN: ADMIN DASHBOARD
    // ══════════════════════════════════════════════════════════════════════════

    private void refreshAdminPanel() {
        adminPanel.removeAll();
        adminPanel.setLayout(new BorderLayout());
        adminPanel.setBackground(BG);

        adminPanel.add(buildSidebar(true), BorderLayout.WEST);

        JPanel main = new JPanel();
        main.setBackground(BG);
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBorder(new EmptyBorder(40, 40, 40, 40));

        JLabel header = makeLabel("ADMIN DASHBOARD", FONT_BIG, BLACK);
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel subHdr = makeLabel("Manage quizzes, questions, and view results.", FONT_BODY, new Color(0x55,0x55,0x55));
        subHdr.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Stat cards
        JPanel statsRow = new JPanel(new GridLayout(1, 4, 16, 0));
        statsRow.setBackground(BG);
        statsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));
        statsRow.add(makeStatCard("TOTAL\nQUIZZES",   String.valueOf(quizDAO.getTotalQuizzes()),   YELLOW));
        statsRow.add(makeStatCard("TOTAL\nQUESTIONS", String.valueOf(quizDAO.getTotalQuestions()), BLUE));
        statsRow.add(makeStatCard("TOTAL\nUSERS",     String.valueOf(userDAO.getTotalUsers()),      GREEN));
        statsRow.add(makeStatCard("TOTAL\nATTEMPTS",  String.valueOf(quizDAO.getTotalAttempts()),  ORANGE));

        // Quick actions
        JLabel actLabel = makeLabel("QUICK ACTIONS", FONT_MED, BLACK);
        actLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        actLabel.setBorder(new EmptyBorder(30, 0, 16, 0));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        actions.setBackground(BG);
        actions.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton createBtn  = makeButton("+ CREATE QUIZ",    YELLOW, BLACK);
        JButton manageBtn  = makeButton("MANAGE QUIZZES",   BLUE,   WHITE);
        JButton resultsBtn = makeButton("VIEW ALL RESULTS",  GREEN, WHITE);

        createBtn.addActionListener(e  -> openEditQuizScreen(null));
        manageBtn.addActionListener(e  -> { refreshAdminQuizzes(); showCard(CARD_ADMIN_QUIZZES); });
        resultsBtn.addActionListener(e -> { refreshAdminResults(); showCard(CARD_ADMIN_RESULTS); });

        actions.add(createBtn);
        actions.add(manageBtn);
        actions.add(resultsBtn);

        main.add(header);
        main.add(Box.createVerticalStrut(4));
        main.add(subHdr);
        main.add(Box.createVerticalStrut(24));
        main.add(statsRow);
        main.add(actLabel);
        main.add(actions);

        adminPanel.add(main, BorderLayout.CENTER);
        adminPanel.revalidate();
        adminPanel.repaint();
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  SCREEN: ADMIN QUIZ TABLE
    // ══════════════════════════════════════════════════════════════════════════

    private void refreshAdminQuizzes() {
        adminQuizzesPanel.removeAll();
        adminQuizzesPanel.setLayout(new BorderLayout());
        adminQuizzesPanel.setBackground(BG);
        adminQuizzesPanel.add(buildSidebar(true), BorderLayout.WEST);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BG);
        main.setBorder(new EmptyBorder(40, 40, 40, 40));

        JLabel header = makeLabel("MANAGE QUIZZES", FONT_BIG, BLACK);

        JButton backBtn   = makeButtonOutline("← DASHBOARD");
        JButton createBtn = makeButton("+ CREATE QUIZ", YELLOW, BLACK);
        backBtn.addActionListener(e   -> { refreshAdminPanel(); showCard(CARD_ADMIN); });
        createBtn.addActionListener(e -> openEditQuizScreen(null));

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(BG);
        topBar.add(header, BorderLayout.WEST);
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnRow.setBackground(BG);
        btnRow.add(backBtn); btnRow.add(createBtn);
        topBar.add(btnRow, BorderLayout.EAST);

        // Table
        List<Quiz> quizzes = quizDAO.getAllQuizzes();
        String[] cols = {"#", "TITLE", "CATEGORY", "QUESTIONS", "TIME (MIN)", "ACTIONS"};
        Object[][] data  = new Object[quizzes.size()][6];
        for (int i = 0; i < quizzes.size(); i++) {
            Quiz q = quizzes.get(i);
            data[i] = new Object[]{
                i+1, q.getTitle(), q.getCategoryName(),
                quizDAO.getQuestionCount(q.getId()), q.getTimeLimit(), ""
            };
        }

        DefaultTableModel model = new DefaultTableModel(data, cols) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = styleTable(new JTable(model));

        // Action column
        table.getColumnModel().getColumn(5).setCellRenderer((t, val, sel, foc, row, col) -> {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 2));
            p.setBackground(sel ? new Color(0xDD,0xEE,0xFF) : WHITE);
            JButton e = makeSmallBtn("EDIT",  BLUE,   WHITE);
            JButton d = makeSmallBtn("DELETE", RED,   WHITE);
            JButton q = makeSmallBtn("QUESTIONS", ORANGE, WHITE);
            p.add(e); p.add(d); p.add(q);
            return p;
        });

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                if (row < 0 || col != 5) return;
                Quiz selected = quizzes.get(row);

                Rectangle cellRect = table.getCellRect(row, col, false);
                int xInCell = e.getX() - cellRect.x;
                int cellW   = cellRect.width;

                if (xInCell < cellW / 3) {
                    // EDIT
                    openEditQuizScreen(selected);
                } else if (xInCell < 2 * cellW / 3) {
                    // DELETE
                    int confirm = JOptionPane.showConfirmDialog(QuizFrame.this,
                        "Delete \"" + selected.getTitle() + "\" and all its questions?",
                        "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (confirm == JOptionPane.YES_OPTION) {
                        if (quizDAO.deleteQuiz(selected.getId())) {
                            JOptionPane.showMessageDialog(QuizFrame.this, "Quiz deleted successfully.");
                            refreshAdminQuizzes(); showCard(CARD_ADMIN_QUIZZES);
                        }
                    }
                } else {
                    // QUESTIONS
                    openQuestionsScreen(selected);
                }
            }
        });
        table.setRowHeight(46);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new LineBorder(BLACK, 2));

        main.add(topBar, BorderLayout.NORTH);
        main.add(Box.createVerticalStrut(20), BorderLayout.NORTH);
        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setBackground(BG);
        tableWrapper.setBorder(new EmptyBorder(20, 0, 0, 0));
        tableWrapper.add(scroll, BorderLayout.CENTER);
        main.add(tableWrapper, BorderLayout.CENTER);

        adminQuizzesPanel.add(main, BorderLayout.CENTER);
        adminQuizzesPanel.revalidate();
        adminQuizzesPanel.repaint();
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  SCREEN: ADMIN ALL RESULTS
    // ══════════════════════════════════════════════════════════════════════════

    private void refreshAdminResults() {
        adminResultsPanel.removeAll();
        adminResultsPanel.setLayout(new BorderLayout());
        adminResultsPanel.setBackground(BG);
        adminResultsPanel.add(buildSidebar(true), BorderLayout.WEST);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BG);
        main.setBorder(new EmptyBorder(40, 40, 40, 40));

        JLabel header = makeLabel("ALL RESULTS", FONT_BIG, BLACK);
        JButton backBtn = makeButtonOutline("← DASHBOARD");
        backBtn.addActionListener(e -> { refreshAdminPanel(); showCard(CARD_ADMIN); });

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(BG);
        topBar.add(header, BorderLayout.WEST);
        topBar.add(backBtn, BorderLayout.EAST);

        List<String[]> results = quizDAO.getAllResults();
        String[] cols = {"USER", "QUIZ", "SCORE", "PERCENTAGE", "DATE"};
        Object[][] data = new Object[results.size()][5];
        for (int i = 0; i < results.size(); i++) {
            data[i] = results.get(i);
        }
        JTable table = styleTable(new JTable(new DefaultTableModel(data, cols) {
            public boolean isCellEditable(int r, int c) { return false; }
        }));
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new LineBorder(BLACK, 2));

        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setBackground(BG);
        tableWrapper.setBorder(new EmptyBorder(20, 0, 0, 0));
        tableWrapper.add(scroll);

        main.add(topBar, BorderLayout.NORTH);
        main.add(tableWrapper, BorderLayout.CENTER);

        adminResultsPanel.add(main, BorderLayout.CENTER);
        adminResultsPanel.revalidate();
        adminResultsPanel.repaint();
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  SCREEN: EDIT / CREATE QUIZ
    // ══════════════════════════════════════════════════════════════════════════

    private void openEditQuizScreen(Quiz existing) {
        editingQuiz = existing;
        editQuizPanel.removeAll();
        editQuizPanel.setLayout(new BorderLayout());
        editQuizPanel.setBackground(BG);
        editQuizPanel.add(buildSidebar(currentUser.isAdmin()), BorderLayout.WEST);

        JPanel main = new JPanel(new GridBagLayout());
        main.setBackground(BG);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(2, 2, 6, 6, BLACK),
            new EmptyBorder(40, 50, 40, 50)
        ));
        form.setPreferredSize(new Dimension(580, 520));

        boolean editing = (existing != null);
        JLabel title = makeLabel(editing ? "EDIT QUIZ" : "CREATE NEW QUIZ", FONT_BIG, BLACK);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField titleField = makeTextField("Quiz Title");
        JTextField descField  = makeTextField("Brief description of the quiz");
        JTextField timeField  = makeTextField("Time limit in minutes (e.g. 10)");

        // Category dropdown
        List<String[]> cats = quizDAO.getAllCategories();
        String[] catNames = cats.stream().map(c -> c[1]).toArray(String[]::new);
        int[]    catIds   = cats.stream().mapToInt(c -> Integer.parseInt(c[0])).toArray();
        JComboBox<String> catBox = new JComboBox<>(catNames);
        stylizeComboBox(catBox);

        if (editing) {
            titleField.setText(existing.getTitle());
            descField.setText(existing.getDescription());
            timeField.setText(String.valueOf(existing.getTimeLimit()));
            for (int i = 0; i < catIds.length; i++) {
                if (catIds[i] == existing.getCategoryId()) { catBox.setSelectedIndex(i); break; }
            }
        }

        JLabel msgLabel = makeLabel("", FONT_SMALL, RED);
        msgLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton saveBtn = makeButton(editing ? "SAVE CHANGES  →" : "CREATE QUIZ  →", editing ? BLUE : YELLOW, editing ? WHITE : BLACK);
        saveBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        JButton cancelBtn = makeButtonOutline("CANCEL");
        cancelBtn.setAlignmentX(Component.LEFT_ALIGNMENT);

        form.add(title);
        form.add(Box.createVerticalStrut(26));
        form.add(makeFormLabel("QUIZ TITLE")); form.add(Box.createVerticalStrut(6)); form.add(titleField);
        form.add(Box.createVerticalStrut(14));
        form.add(makeFormLabel("DESCRIPTION")); form.add(Box.createVerticalStrut(6)); form.add(descField);
        form.add(Box.createVerticalStrut(14));
        form.add(makeFormLabel("CATEGORY")); form.add(Box.createVerticalStrut(6)); form.add(catBox);
        form.add(Box.createVerticalStrut(14));
        form.add(makeFormLabel("TIME LIMIT (MINUTES)")); form.add(Box.createVerticalStrut(6)); form.add(timeField);
        form.add(Box.createVerticalStrut(10));
        form.add(msgLabel);
        form.add(Box.createVerticalStrut(24));
        form.add(saveBtn);
        form.add(Box.createVerticalStrut(10));
        form.add(cancelBtn);

        saveBtn.addActionListener(e -> {
            String t    = titleField.getText().trim();
            String desc = descField.getText().trim();
            String timeStr = timeField.getText().trim();
            if (t.isEmpty()) { msgLabel.setForeground(RED); msgLabel.setText("⚠  Quiz title is required."); return; }
            int timeLimit;
            try { timeLimit = Integer.parseInt(timeStr); if (timeLimit < 1) throw new NumberFormatException(); }
            catch (NumberFormatException ex) { msgLabel.setForeground(RED); msgLabel.setText("⚠  Enter a valid time limit (e.g. 10)."); return; }

            int selCatIndex = catBox.getSelectedIndex();
            int catId = (selCatIndex >= 0 && catIds.length > 0) ? catIds[selCatIndex] : 1;

            if (editing) {
                existing.setTitle(t); existing.setDescription(desc);
                existing.setCategoryId(catId); existing.setTimeLimit(timeLimit);
                if (quizDAO.updateQuiz(existing)) {
                    JOptionPane.showMessageDialog(QuizFrame.this, "Quiz updated successfully!");
                    refreshAdminQuizzes(); showCard(CARD_ADMIN_QUIZZES);
                }
            } else {
                Quiz newQuiz = new Quiz(t, desc, catId, timeLimit);
                int newId = quizDAO.createQuiz(newQuiz);
                if (newId > 0) {
                    newQuiz.setId(newId);
                    JOptionPane.showMessageDialog(QuizFrame.this, "Quiz created! Now add questions.");
                    openQuestionsScreen(quizDAO.getQuizById(newId));
                } else {
                    msgLabel.setForeground(RED); msgLabel.setText("⚠  Failed to create quiz. Try again.");
                }
            }
        });

        cancelBtn.addActionListener(e -> { refreshAdminQuizzes(); showCard(CARD_ADMIN_QUIZZES); });

        main.add(form);
        editQuizPanel.add(main, BorderLayout.CENTER);
        editQuizPanel.revalidate();
        editQuizPanel.repaint();
        showCard(CARD_EDIT_QUIZ);
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  SCREEN: MANAGE QUESTIONS
    // ══════════════════════════════════════════════════════════════════════════

    private void openQuestionsScreen(Quiz quiz) {
        questionsPanel.removeAll();
        questionsPanel.setLayout(new BorderLayout());
        questionsPanel.setBackground(BG);
        questionsPanel.add(buildSidebar(true), BorderLayout.WEST);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BG);
        main.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Top bar
        JLabel header = makeLabel("QUESTIONS — " + quiz.getTitle(), FONT_BIG, BLACK);
        header.setBorder(new EmptyBorder(0,0,4,0));

        JButton backBtn  = makeButtonOutline("← QUIZZES");
        backBtn.addActionListener(e -> { refreshAdminQuizzes(); showCard(CARD_ADMIN_QUIZZES); });

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(BG);
        topBar.add(header, BorderLayout.CENTER);
        topBar.add(backBtn, BorderLayout.EAST);

        // Existing questions list
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(BG);

        List<Question> questions = quizDAO.getQuestionsByQuizId(quiz.getId());
        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            listPanel.add(buildQuestionRow(i + 1, q, quiz));
            listPanel.add(Box.createVerticalStrut(10));
        }

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(BG);

        // ADD QUESTION form at bottom
        JPanel addForm = buildAddQuestionForm(quiz);

        main.add(topBar, BorderLayout.NORTH);
        main.add(scroll, BorderLayout.CENTER);
        main.add(addForm, BorderLayout.SOUTH);

        questionsPanel.add(main, BorderLayout.CENTER);
        questionsPanel.revalidate();
        questionsPanel.repaint();
        showCard(CARD_QUESTIONS);
    }

    private JPanel buildQuestionRow(int num, Question q, Quiz quiz) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(2, 2, 4, 4, BLACK),
            new EmptyBorder(12, 16, 12, 16)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel qText = makeLabel("Q" + num + ": " + q.getQuestionText(), FONT_BODY, BLACK);
        JLabel ans   = makeLabel("✓ Correct: " + q.getCorrectAnswer() + " — " + q.getOption(q.getCorrectAnswer()), FONT_SMALL, GREEN);

        JPanel textCol = new JPanel();
        textCol.setBackground(WHITE);
        textCol.setLayout(new BoxLayout(textCol, BoxLayout.Y_AXIS));
        textCol.add(qText); textCol.add(Box.createVerticalStrut(3)); textCol.add(ans);

        JButton delBtn = makeSmallBtn("DELETE", RED, WHITE);
        delBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Delete this question?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (quizDAO.deleteQuestion(q.getId())) {
                    JOptionPane.showMessageDialog(this, "Question deleted.");
                    openQuestionsScreen(quiz);
                }
            }
        });

        card.add(textCol, BorderLayout.CENTER);
        card.add(delBtn, BorderLayout.EAST);
        return card;
    }

    private JPanel buildAddQuestionForm(Quiz quiz) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(BG);
        wrapper.setBorder(new EmptyBorder(16, 0, 0, 0));

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(0xFF, 0xFF, 0xF5));
        card.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(2, 2, 5, 5, BLACK),
            new EmptyBorder(20, 24, 20, 24)
        ));

        JLabel formTitle = makeLabel("ADD NEW QUESTION", FONT_MED, BLACK);
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField qText = makeTextField("Question text");
        JTextField opA   = makeTextField("Option A");
        JTextField opB   = makeTextField("Option B");
        JTextField opC   = makeTextField("Option C");
        JTextField opD   = makeTextField("Option D");

        String[] answers = {"A", "B", "C", "D"};
        JComboBox<String> correctBox = new JComboBox<>(answers);
        stylizeComboBox(correctBox);
        correctBox.setMaximumSize(new Dimension(120, 44));
        correctBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel msgLabel = makeLabel("", FONT_SMALL, RED);
        msgLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton saveBtn = makeButton("SAVE QUESTION  →", GREEN, WHITE);
        saveBtn.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Two-column options layout
        JPanel optRow1 = new JPanel(new GridLayout(1, 2, 10, 0));
        optRow1.setBackground(new Color(0xFF,0xFF,0xF5));
        optRow1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        optRow1.add(opA); optRow1.add(opB);
        JPanel optRow2 = new JPanel(new GridLayout(1, 2, 10, 0));
        optRow2.setBackground(new Color(0xFF,0xFF,0xF5));
        optRow2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        optRow2.add(opC); optRow2.add(opD);

        card.add(formTitle);
        card.add(Box.createVerticalStrut(14));
        card.add(makeFormLabel("QUESTION TEXT")); card.add(Box.createVerticalStrut(5)); card.add(qText);
        card.add(Box.createVerticalStrut(10));
        card.add(makeFormLabel("OPTIONS")); card.add(Box.createVerticalStrut(5)); card.add(optRow1);
        card.add(Box.createVerticalStrut(6)); card.add(optRow2);
        card.add(Box.createVerticalStrut(10));
        card.add(makeFormLabel("CORRECT ANSWER")); card.add(Box.createVerticalStrut(5)); card.add(correctBox);
        card.add(Box.createVerticalStrut(6));
        card.add(msgLabel);
        card.add(Box.createVerticalStrut(12));
        card.add(saveBtn);

        saveBtn.addActionListener(e -> {
            String qt  = qText.getText().trim();
            String a   = opA.getText().trim();
            String b   = opB.getText().trim();
            String c   = opC.getText().trim();
            String d   = opD.getText().trim();
            String ans = (String) correctBox.getSelectedItem();

            if (qt.isEmpty() || a.isEmpty() || b.isEmpty() || c.isEmpty() || d.isEmpty()) {
                msgLabel.setForeground(RED); msgLabel.setText("⚠  All fields are required."); return;
            }

            Question newQ = new Question(quiz.getId(), qt, a, b, c, d, ans);
            int newId = quizDAO.addQuestion(newQ);
            if (newId > 0) {
                msgLabel.setForeground(GREEN); msgLabel.setText("✓  Question saved successfully!");
                qText.setText(""); opA.setText(""); opB.setText(""); opC.setText(""); opD.setText("");
                correctBox.setSelectedIndex(0);
                // Refresh list
                openQuestionsScreen(quiz);
            } else {
                msgLabel.setForeground(RED); msgLabel.setText("⚠  Failed to save question.");
            }
        });

        wrapper.add(card);
        return wrapper;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  SCREEN: QUIZ LIST (student/guest)
    // ══════════════════════════════════════════════════════════════════════════

    private void refreshQuizList() {
        quizListPanel.removeAll();
        quizListPanel.setLayout(new BorderLayout());
        quizListPanel.setBackground(BG);

        boolean isGuest = (currentUser != null && currentUser.isGuest());

        if (!isGuest) {
            quizListPanel.add(buildSidebar(false), BorderLayout.WEST);
        }

        JPanel main = new JPanel();
        main.setBackground(BG);
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBorder(new EmptyBorder(40, 40, 40, 40));

        JLabel header = makeLabel("SELECT A QUIZ", FONT_BIG, BLACK);
        header.setAlignmentX(Component.LEFT_ALIGNMENT);

        String greeting = isGuest
            ? "Hello, " + currentUser.getFullName() + "! Choose a quiz to get started."
            : "Choose a quiz to start. Good luck!";
        JLabel sub = makeLabel(greeting, FONT_BODY, new Color(0x55,0x55,0x55));
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel quizzesPanel = new JPanel();
        quizzesPanel.setBackground(BG);
        quizzesPanel.setLayout(new BoxLayout(quizzesPanel, BoxLayout.Y_AXIS));

        List<Quiz> quizzes = quizDAO.getAllQuizzes();
        if (quizzes.isEmpty()) {
            quizzesPanel.add(makeLabel("No quizzes available.", FONT_BODY, new Color(0x88,0x88,0x88)));
        } else {
            for (Quiz q : quizzes) {
                int qCount = quizDAO.getQuestionCount(q.getId());
                JPanel card = buildQuizCard(q, qCount);
                card.setAlignmentX(Component.LEFT_ALIGNMENT);
                quizzesPanel.add(card);
                quizzesPanel.add(Box.createVerticalStrut(12));
            }
        }

        JScrollPane scroll = new JScrollPane(quizzesPanel);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(BG);
        scroll.setAlignmentX(Component.LEFT_ALIGNMENT);

        if (isGuest) {
            JButton logoutBtn = makeButtonOutline("← EXIT");
            logoutBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
            logoutBtn.addActionListener(e -> {
                currentUser = null;
                showCard(CARD_LOGIN);
            });
            main.add(logoutBtn);
            main.add(Box.createVerticalStrut(16));
        }

        main.add(header);
        main.add(Box.createVerticalStrut(8));
        main.add(sub);
        main.add(Box.createVerticalStrut(24));
        main.add(scroll);

        quizListPanel.add(main, BorderLayout.CENTER);
        quizListPanel.revalidate();
        quizListPanel.repaint();
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  SCREEN: MY RESULTS (student)
    // ══════════════════════════════════════════════════════════════════════════

    void refreshMyResults() {
        myResultsPanel.removeAll();
        myResultsPanel.setLayout(new BorderLayout());
        myResultsPanel.setBackground(BG);
        myResultsPanel.add(buildSidebar(false), BorderLayout.WEST);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BG);
        main.setBorder(new EmptyBorder(40, 40, 40, 40));

        JLabel header = makeLabel("MY QUIZ HISTORY", FONT_BIG, BLACK);

        List<String[]> results = quizDAO.getResultsByUser(currentUser.getId());
        String[] cols = {"QUIZ", "SCORE", "PERCENTAGE", "DATE"};
        Object[][] data = new Object[results.size()][4];
        for (int i = 0; i < results.size(); i++) data[i] = results.get(i);

        JTable table = styleTable(new JTable(new DefaultTableModel(data, cols) {
            public boolean isCellEditable(int r, int c) { return false; }
        }));
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new LineBorder(BLACK, 2));

        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setBackground(BG);
        tableWrapper.setBorder(new EmptyBorder(20, 0, 0, 0));
        tableWrapper.add(scroll);

        main.add(header, BorderLayout.NORTH);
        main.add(tableWrapper, BorderLayout.CENTER);

        myResultsPanel.add(main, BorderLayout.CENTER);
        myResultsPanel.revalidate();
        myResultsPanel.repaint();
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  SIDEBAR
    // ══════════════════════════════════════════════════════════════════════════

    private JPanel buildSidebar(boolean isAdmin) {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(BLACK);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(220, Integer.MAX_VALUE));
        sidebar.setBorder(new EmptyBorder(30, 0, 30, 0));

        JLabel logo = makeLabel("QUIZ\nSYSTEM", FONT_MED, YELLOW);
        logo.setBorder(new EmptyBorder(0, 24, 0, 24));
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel divider = new JPanel();
        divider.setBackground(new Color(0x33,0x33,0x33));
        divider.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));

        sidebar.add(Box.createVerticalStrut(4));
        sidebar.add(logo);
        sidebar.add(Box.createVerticalStrut(24));
        sidebar.add(divider);
        sidebar.add(Box.createVerticalStrut(20));

        if (isAdmin) {
            sidebar.add(makeSidebarBtn("Dashboard", e -> { refreshAdminPanel(); showCard(CARD_ADMIN); }));
            sidebar.add(makeSidebarBtn("Manage Quizzes", e -> { refreshAdminQuizzes(); showCard(CARD_ADMIN_QUIZZES); }));
            sidebar.add(makeSidebarBtn("View Results", e -> { refreshAdminResults(); showCard(CARD_ADMIN_RESULTS); }));
        } else {
            sidebar.add(makeSidebarBtn("Dashboard", e -> { refreshDashboard(); showCard(CARD_DASHBOARD); }));
            sidebar.add(makeSidebarBtn("Take a Quiz", e -> { refreshQuizList(); showCard(CARD_QUIZ_LIST); }));
            sidebar.add(makeSidebarBtn("My Results", e -> { refreshMyResults(); showCard(CARD_MY_RESULTS); }));
        }

        sidebar.add(Box.createVerticalGlue());
        sidebar.add(divider);

        // User info
        if (currentUser != null) {
            JLabel userLbl = makeLabel(currentUser.getFullName(), FONT_LABEL, WHITE);
            JLabel roleLbl = makeLabel(currentUser.getRole().toUpperCase(), FONT_SMALL, new Color(0xAA,0xAA,0xAA));
            userLbl.setBorder(new EmptyBorder(0, 24, 2, 24));
            roleLbl.setBorder(new EmptyBorder(0, 24, 0, 24));
            sidebar.add(Box.createVerticalStrut(14));
            sidebar.add(userLbl);
            sidebar.add(roleLbl);
            sidebar.add(Box.createVerticalStrut(10));
        }

        sidebar.add(makeSidebarBtn("Logout", e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                currentUser = null;
                showCard(CARD_LOGIN);
            }
        }));

        return sidebar;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  PUBLIC HELPERS (called from QuizTakingFrame / ResultFrame)
    // ══════════════════════════════════════════════════════════════════════════

    /** Called when quiz completes — restores window and shows dashboard */
    public void onQuizComplete() {
        setVisible(true);
        if (currentUser == null) { showCard(CARD_LOGIN); return; }
        if (currentUser.isAdmin())   { refreshAdminPanel(); showCard(CARD_ADMIN); }
        else if (currentUser.isGuest()) { refreshQuizList(); showCard(CARD_QUIZ_LIST); }
        else { refreshDashboard(); showCard(CARD_DASHBOARD); }
    }

    public User getCurrentUser() { return currentUser; }

    // ══════════════════════════════════════════════════════════════════════════
    //  UI FACTORY HELPERS — Neo-Brutalist Components
    // ══════════════════════════════════════════════════════════════════════════

    private void showCard(String name) {
        cardLayout.show(cardPanel, name);
    }

    static JLabel makeLabel(String text, Font font, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(font);
        l.setForeground(color);
        return l;
    }

    private JLabel makeFormLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_LABEL);
        l.setForeground(new Color(0x44, 0x44, 0x44));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    static JTextField makeTextField(String placeholder) {
        JTextField f = new JTextField(placeholder) {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setColor(new Color(0xAA, 0xAA, 0xAA));
                    g2.setFont(getFont().deriveFont(Font.ITALIC));
                    g2.drawString(placeholder, 12, getHeight() / 2 + 5);
                }
            }
        };
        f.setFont(FONT_BODY);
        f.setForeground(BLACK);
        f.setBackground(WHITE);
        f.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(2, 2, 4, 4, BLACK),
            new EmptyBorder(10, 12, 10, 12)
        ));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        f.setAlignmentX(Component.LEFT_ALIGNMENT);
        // Clear placeholder behavior
        f.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (f.getText().equals(placeholder)) f.setText("");
            }
        });
        f.setText(""); // start empty so placeholder shows
        return f;
    }

    static JPasswordField makePasswordField(String placeholder) {
        JPasswordField f = new JPasswordField();
        f.setFont(FONT_BODY);
        f.setForeground(BLACK);
        f.setBackground(WHITE);
        f.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(2, 2, 4, 4, BLACK),
            new EmptyBorder(10, 12, 10, 12)
        ));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        f.setAlignmentX(Component.LEFT_ALIGNMENT);
        return f;
    }

    static JButton makeButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text) {
            boolean hovered = false;
            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
                    public void mouseExited(MouseEvent e)  { hovered = false; repaint(); }
                });
            }
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Hard shadow on hover
                if (hovered) {
                    g2.setColor(BLACK);
                    g2.fillRect(5, 5, getWidth()-5, getHeight()-5);
                }
                g2.setColor(bg);
                g2.fillRect(0, 0, getWidth() - (hovered?5:0), getHeight() - (hovered?5:0));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(FONT_MED);
        btn.setForeground(fg);
        btn.setBackground(bg);
        btn.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BLACK, 2),
            new EmptyBorder(12, 24, 12, 24)
        ));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        return btn;
    }

    static JButton makeButtonOutline(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_MED);
        btn.setForeground(BLACK);
        btn.setBackground(WHITE);
        btn.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(2, 2, 4, 4, BLACK),
            new EmptyBorder(10, 20, 10, 20)
        ));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        return btn;
    }

    private JButton makeSmallBtn(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_SMALL);
        btn.setForeground(fg);
        btn.setBackground(bg);
        btn.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BLACK, 1),
            new EmptyBorder(4, 10, 4, 10)
        ));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton makeSidebarBtn(String text, ActionListener action) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_LABEL);
        btn.setForeground(new Color(0xDD, 0xDD, 0xDD));
        btn.setBackground(BLACK);
        btn.setBorder(new EmptyBorder(14, 24, 14, 24));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(true);
        btn.setMaximumSize(new Dimension(220, 50));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(0x2A,0x2A,0x2A)); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(BLACK); }
        });
        btn.addActionListener(action);
        return btn;
    }

    private JPanel makeStatCard(String label, String value, Color accent) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(2, 2, 6, 6, BLACK),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JPanel accentBar = new JPanel();
        accentBar.setBackground(accent);
        accentBar.setMaximumSize(new Dimension(40, 5));
        accentBar.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel valLabel = makeLabel(value, FONT_HUGE, BLACK);
        valLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lbl = makeLabel("<html>" + label.replace("\n", "<br>") + "</html>",
                               new Font("Segoe UI", Font.BOLD, 12),
                               new Color(0x66, 0x66, 0x66));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(accentBar);
        card.add(Box.createVerticalStrut(12));
        card.add(valLabel);
        card.add(Box.createVerticalStrut(4));
        card.add(lbl);
        return card;
    }

    private JTable styleTable(JTable table) {
        table.setFont(FONT_BODY);
        table.setRowHeight(40);
        table.setBackground(WHITE);
        table.setForeground(BLACK);
        table.setSelectionBackground(new Color(0xDD, 0xEE, 0xFF));
        table.setSelectionForeground(BLACK);
        table.setGridColor(new Color(0xEE, 0xEE, 0xEE));
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));

        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_LABEL);
        header.setBackground(BLACK);
        header.setForeground(WHITE);
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(0, 44));
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.LEFT);
        return table;
    }

    private void stylizeComboBox(JComboBox<?> box) {
        box.setFont(FONT_BODY);
        box.setBackground(WHITE);
        box.setForeground(BLACK);
        box.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(2, 2, 4, 4, BLACK),
            new EmptyBorder(8, 10, 8, 10)
        ));
        box.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        box.setAlignmentX(Component.LEFT_ALIGNMENT);
    }
}
