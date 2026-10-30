/**
 * QuizTakingFrame.java
 *
 * The quiz-taking interface. Displayed as a separate JFrame window.
 * Features:
 *  - Question-by-question navigation (Previous / Next)
 *  - Countdown timer using javax.swing.Timer
 *  - Large, selectable answer cards with visual feedback
 *  - Progress bar
 *  - Confirmation before submission
 *  - Auto-submit when timer reaches 0
 *  - Score calculation and result saving
 *
 * OOP Concept: Encapsulation (private fields), Uses Quiz/Question/User models.
 */

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class QuizTakingFrame extends JFrame {

    // ─── CONSTANTS ─────────────────────────────────────────────────────────────
    private static final Color BG      = QuizFrame.BG;
    private static final Color BLACK   = QuizFrame.BLACK;
    private static final Color WHITE   = QuizFrame.WHITE;
    private static final Color YELLOW  = QuizFrame.YELLOW;
    private static final Color GREEN   = QuizFrame.GREEN;
    private static final Color BLUE    = QuizFrame.BLUE;
    private static final Color RED     = QuizFrame.RED;
    private static final Color ORANGE  = QuizFrame.ORANGE;
    private static final Color PINK    = QuizFrame.PINK;

    // ─── STATE ─────────────────────────────────────────────────────────────────
    private QuizFrame  parentFrame;
    private User       currentUser;
    private Quiz       quiz;
    private QuizDAO    quizDAO;
    private List<Question> questions;
    private int        currentIndex    = 0;
    private String[]   userAnswers;     // stores selected answers per question index
    private int        secondsLeft;
    private Timer      countdownTimer;
    private boolean    submitted       = false;

    // ─── UI COMPONENTS ──────────────────────────────────────────────────────────
    private JLabel  quizTitleLabel;
    private JLabel  questionNumberLabel;
    private JLabel  timerLabel;
    private JLabel  timerWarning;
    private JProgressBar progressBar;
    private JLabel  questionTextLabel;
    private JPanel  optionsPanel;
    private JButton prevBtn;
    private JButton nextBtn;
    private JButton submitBtn;
    private JPanel  optionCards[] = new JPanel[4];
    private ButtonGroup btnGroup;

    // ─── CONSTRUCTOR ───────────────────────────────────────────────────────────

    public QuizTakingFrame(QuizFrame parent, User user, Quiz quiz, QuizDAO quizDAO) {
        this.parentFrame = parent;
        this.currentUser = user;
        this.quiz        = quiz;
        this.quizDAO     = quizDAO;
        this.questions   = quiz.getQuestions();
        this.userAnswers = new String[questions.size()];
        this.secondsLeft = quiz.getTimeLimit() * 60;

        setTitle("Quiz — " + quiz.getTitle());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new Dimension(1100, 700));
        setSize(1200, 750);
        setLocationRelativeTo(null);

        // Warn on window close
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showConfirmDialog(QuizTakingFrame.this,
                    "Exit the quiz? Your progress will be lost.",
                    "Exit Quiz", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    stopTimer();
                    dispose();
                    parentFrame.onQuizComplete();
                }
            }
        });

        buildUI();
        showQuestion(0);
        startTimer();
        setVisible(true);
    }

    // ─── BUILD UI ──────────────────────────────────────────────────────────────

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);
        add(root);

        // ── TOP BAR ──────────────────────────────────────────────────────────
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(BLACK);
        topBar.setBorder(new EmptyBorder(16, 30, 16, 30));

        quizTitleLabel = QuizFrame.makeLabel(quiz.getTitle().toUpperCase(), QuizFrame.FONT_MED, YELLOW);

        // Timer panel (right side of top bar)
        JPanel timerPanel = new JPanel();
        timerPanel.setBackground(BLACK);
        timerPanel.setLayout(new BoxLayout(timerPanel, BoxLayout.Y_AXIS));
        timerLabel = QuizFrame.makeLabel("00:00", QuizFrame.FONT_BIG, WHITE);
        timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel timerCaption = QuizFrame.makeLabel("TIME LEFT", QuizFrame.FONT_SMALL, new Color(0xAA,0xAA,0xAA));
        timerCaption.setAlignmentX(Component.CENTER_ALIGNMENT);
        timerWarning = QuizFrame.makeLabel("", QuizFrame.FONT_SMALL, ORANGE);
        timerWarning.setAlignmentX(Component.CENTER_ALIGNMENT);
        timerPanel.add(timerCaption);
        timerPanel.add(timerLabel);
        timerPanel.add(timerWarning);

        questionNumberLabel = QuizFrame.makeLabel("Question 1 / " + questions.size(), QuizFrame.FONT_BODY, new Color(0xCC,0xCC,0xCC));

        topBar.add(quizTitleLabel, BorderLayout.WEST);
        topBar.add(questionNumberLabel, BorderLayout.CENTER);
        topBar.add(timerPanel, BorderLayout.EAST);

        // ── PROGRESS BAR ──────────────────────────────────────────────────────
        progressBar = new JProgressBar(0, questions.size());
        progressBar.setValue(0);
        progressBar.setStringPainted(false);
        progressBar.setForeground(GREEN);
        progressBar.setBackground(new Color(0x33, 0x33, 0x33));
        progressBar.setBorderPainted(false);
        progressBar.setPreferredSize(new Dimension(0, 8));

        JPanel topSection = new JPanel(new BorderLayout());
        topSection.add(topBar, BorderLayout.CENTER);
        topSection.add(progressBar, BorderLayout.SOUTH);

        // ── CENTER — QUESTION CARD ────────────────────────────────────────────
        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(BG);
        center.setBorder(new EmptyBorder(30, 60, 20, 60));

        JPanel questionCard = new JPanel();
        questionCard.setLayout(new BoxLayout(questionCard, BoxLayout.Y_AXIS));
        questionCard.setBackground(WHITE);
        questionCard.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(2, 2, 6, 6, BLACK),
            new EmptyBorder(36, 40, 36, 40)
        ));
        questionCard.setPreferredSize(new Dimension(900, 520));

        // Question number badge
        JLabel qNumBadge = QuizFrame.makeLabel("QUESTION " + (currentIndex + 1), QuizFrame.FONT_LABEL, BLUE);
        qNumBadge.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Question text
        questionTextLabel = new JLabel();
        questionTextLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        questionTextLabel.setForeground(BLACK);
        questionTextLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Options panel (2x2 grid)
        optionsPanel = new JPanel(new GridLayout(2, 2, 14, 14));
        optionsPanel.setBackground(WHITE);
        optionsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        optionsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));

        questionCard.add(qNumBadge);
        questionCard.add(Box.createVerticalStrut(16));
        questionCard.add(questionTextLabel);
        questionCard.add(Box.createVerticalStrut(28));
        questionCard.add(optionsPanel);

        // Store reference to badge for updating
        this.questionCard_badge = qNumBadge;
        this.questionCard = questionCard;

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1; gbc.weighty = 1;
        center.add(questionCard, gbc);

        // ── BOTTOM NAV ────────────────────────────────────────────────────────
        JPanel bottomBar = new JPanel(new BorderLayout());
        bottomBar.setBackground(BG);
        bottomBar.setBorder(new EmptyBorder(14, 60, 24, 60));

        prevBtn   = QuizFrame.makeButton("← PREVIOUS", WHITE, BLACK);
        prevBtn.setBackground(WHITE);
        prevBtn.setFont(QuizFrame.FONT_MED);
        prevBtn.setPreferredSize(new Dimension(160, 52));

        nextBtn   = QuizFrame.makeButton("NEXT →", BLACK, WHITE);
        nextBtn.setBackground(BLACK);
        nextBtn.setPreferredSize(new Dimension(160, 52));

        submitBtn = QuizFrame.makeButton("SUBMIT QUIZ ✓", GREEN, WHITE);
        submitBtn.setPreferredSize(new Dimension(200, 52));
        submitBtn.setVisible(false);

        JPanel navLeft  = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        navLeft.setBackground(BG);
        navLeft.add(prevBtn);

        JPanel navRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        navRight.setBackground(BG);
        navRight.add(nextBtn);
        navRight.add(submitBtn);

        bottomBar.add(navLeft, BorderLayout.WEST);
        bottomBar.add(navRight, BorderLayout.EAST);

        // ── EVENT HANDLERS ────────────────────────────────────────────────────
        prevBtn.addActionListener(e -> navigateTo(currentIndex - 1));
        nextBtn.addActionListener(e -> navigateTo(currentIndex + 1));
        submitBtn.addActionListener(e -> confirmAndSubmit());

        root.add(topSection, BorderLayout.NORTH);
        root.add(center, BorderLayout.CENTER);
        root.add(bottomBar, BorderLayout.SOUTH);
    }

    // Stored refs for updates
    private JLabel questionCard_badge;
    private JPanel questionCard;

    // ─── SHOW QUESTION ─────────────────────────────────────────────────────────

    private void showQuestion(int index) {
        if (index < 0 || index >= questions.size()) return;
        currentIndex = index;

        Question q = questions.get(index);

        // Update header
        questionNumberLabel.setText("Question " + (index + 1) + " / " + questions.size());
        questionCard_badge.setText("QUESTION " + (index + 1));
        progressBar.setValue(index + 1);

        // Update question text (wrap long questions)
        questionTextLabel.setText("<html><body style='width:750px'>" + escapeHtml(q.getQuestionText()) + "</body></html>");

        // Rebuild option cards
        optionsPanel.removeAll();
        String[] letters  = {"A", "B", "C", "D"};
        String[] texts    = {q.getOptionA(), q.getOptionB(), q.getOptionC(), q.getOptionD()};
        String   selected = userAnswers[index];

        for (int i = 0; i < 4; i++) {
            final String letter = letters[i];
            JPanel card = buildOptionCard(letter, texts[i], letter.equals(selected));
            optionCards[i] = card;
            card.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    selectAnswer(letter, index);
                }
                public void mouseEntered(MouseEvent e) {
                    if (!letter.equals(userAnswers[currentIndex])) {
                        card.setBackground(new Color(0xF0, 0xF0, 0xFF));
                        card.repaint();
                    }
                }
                public void mouseExited(MouseEvent e) {
                    if (!letter.equals(userAnswers[currentIndex])) {
                        card.setBackground(WHITE);
                        card.repaint();
                    }
                }
            });
            optionsPanel.add(card);
        }

        // Navigation buttons
        prevBtn.setEnabled(index > 0);
        boolean isLast = (index == questions.size() - 1);
        nextBtn.setVisible(!isLast);
        submitBtn.setVisible(isLast);

        optionsPanel.revalidate();
        optionsPanel.repaint();
    }

    private JPanel buildOptionCard(String letter, String text, boolean selected) {
        JPanel card = new JPanel(new BorderLayout(14, 0));
        card.setBackground(selected ? new Color(0xDD, 0xEE, 0xFF) : WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            selected
                ? new MatteBorder(2, 2, 4, 4, BLUE)
                : new MatteBorder(2, 2, 4, 4, BLACK),
            new EmptyBorder(14, 16, 14, 16)
        ));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel badge = new JLabel(letter);
        badge.setFont(QuizFrame.FONT_MED);
        badge.setForeground(selected ? WHITE : BLACK);
        badge.setBackground(selected ? BLUE : new Color(0xEE, 0xEE, 0xEE));
        badge.setOpaque(true);
        badge.setHorizontalAlignment(SwingConstants.CENTER);
        badge.setPreferredSize(new Dimension(36, 36));
        badge.setBorder(new LineBorder(selected ? BLUE : BLACK, 1));

        JLabel txt = new JLabel("<html><body style='width:280px'>" + escapeHtml(text) + "</body></html>");
        txt.setFont(QuizFrame.FONT_BODY);
        txt.setForeground(BLACK);

        card.add(badge, BorderLayout.WEST);
        card.add(txt, BorderLayout.CENTER);
        return card;
    }

    private void selectAnswer(String letter, int questionIndex) {
        userAnswers[questionIndex] = letter;
        // Re-render option cards for visual feedback
        Question q = questions.get(questionIndex);
        String[] letters = {"A", "B", "C", "D"};
        String[] texts   = {q.getOptionA(), q.getOptionB(), q.getOptionC(), q.getOptionD()};
        optionsPanel.removeAll();
        for (int i = 0; i < 4; i++) {
            final String l = letters[i];
            JPanel card = buildOptionCard(l, texts[i], l.equals(letter));
            card.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) { selectAnswer(l, currentIndex); }
                public void mouseEntered(MouseEvent e) {
                    if (!l.equals(userAnswers[currentIndex])) {
                        card.setBackground(new Color(0xF0,0xF0,0xFF)); card.repaint();
                    }
                }
                public void mouseExited(MouseEvent e) {
                    if (!l.equals(userAnswers[currentIndex])) {
                        card.setBackground(WHITE); card.repaint();
                    }
                }
            });
            optionCards[i] = card;
            optionsPanel.add(card);
        }
        optionsPanel.revalidate();
        optionsPanel.repaint();
    }

    private void navigateTo(int index) {
        if (index >= 0 && index < questions.size()) {
            showQuestion(index);
        }
    }

    // ─── TIMER ─────────────────────────────────────────────────────────────────

    private void startTimer() {
        updateTimerDisplay();
        countdownTimer = new Timer(1000, e -> {
            secondsLeft--;
            updateTimerDisplay();
            if (secondsLeft <= 0) {
                stopTimer();
                JOptionPane.showMessageDialog(this,
                    "⏰ Time's up! Your quiz is being submitted automatically.",
                    "Time's Up!", JOptionPane.WARNING_MESSAGE);
                submitQuiz(true);  // auto-submit
            } else if (secondsLeft <= 120) {
                // Under 2 minutes — show warning
                timerLabel.setForeground(RED);
                timerWarning.setText("⚠ Time is running out!");
            }
        });
        countdownTimer.start();
    }

    private void stopTimer() {
        if (countdownTimer != null && countdownTimer.isRunning()) {
            countdownTimer.stop();
        }
    }

    private void updateTimerDisplay() {
        int mins = secondsLeft / 60;
        int secs = secondsLeft % 60;
        timerLabel.setText(String.format("%02d:%02d", mins, secs));
    }

    // ─── SUBMISSION ────────────────────────────────────────────────────────────

    private void confirmAndSubmit() {
        if (submitted) return;

        // Count unanswered questions
        int unanswered = 0;
        for (String ans : userAnswers) { if (ans == null) unanswered++; }

        String msg = "Are you sure you want to submit?";
        if (unanswered > 0) {
            msg += "\n\n⚠ You have " + unanswered + " unanswered question(s).";
        }

        int confirm = JOptionPane.showConfirmDialog(this, msg,
            "Submit Quiz", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            stopTimer();
            submitQuiz(false);
        }
    }

    private void submitQuiz(boolean autoSubmit) {
        if (submitted) return;
        submitted = true;
        stopTimer();

        // Calculate score
        int score = 0;
        for (int i = 0; i < questions.size(); i++) {
            if (userAnswers[i] != null && questions.get(i).isCorrect(userAnswers[i])) {
                score++;
            }
        }

        // Save result to database
        quizDAO.saveResult(currentUser.getId(), quiz.getId(), score, questions.size());

        // Open result frame
        dispose();
        new ResultFrame(parentFrame, currentUser, quiz, quizDAO,
                        questions, userAnswers, score, questions.size());
    }

    // ─── UTILITY ───────────────────────────────────────────────────────────────

    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
