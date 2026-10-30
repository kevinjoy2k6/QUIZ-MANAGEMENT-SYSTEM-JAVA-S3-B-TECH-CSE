/**
 * ResultFrame.java
 *
 * Displays the quiz result after submission.
 * Shows:
 *  - Score and percentage (large typography)
 *  - Correct / Wrong counts
 *  - Visual progress bar for score
 *  - Detailed answer review (each question with selected vs. correct answer)
 *  - Navigation to dashboard or another quiz
 *
 * OOP Concept: Encapsulation of result data; uses Quiz, Question, User models.
 */

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ResultFrame extends JFrame {

    // ─── COLOUR CONSTANTS ──────────────────────────────────────────────────────
    private static final Color BG     = QuizFrame.BG;
    private static final Color BLACK  = QuizFrame.BLACK;
    private static final Color WHITE  = QuizFrame.WHITE;
    private static final Color YELLOW = QuizFrame.YELLOW;
    private static final Color GREEN  = QuizFrame.GREEN;
    private static final Color RED    = QuizFrame.RED;
    private static final Color BLUE   = QuizFrame.BLUE;
    private static final Color ORANGE = QuizFrame.ORANGE;

    // ─── FIELDS ────────────────────────────────────────────────────────────────
    private QuizFrame      parentFrame;
    private User           currentUser;
    private Quiz           quiz;
    private QuizDAO        quizDAO;
    private List<Question> questions;
    private String[]       userAnswers;
    private int            score;
    private int            total;
    private double         percentage;

    // ─── CONSTRUCTOR ───────────────────────────────────────────────────────────

    public ResultFrame(QuizFrame parent, User user, Quiz quiz, QuizDAO quizDAO,
                       List<Question> questions, String[] userAnswers,
                       int score, int total) {
        this.parentFrame = parent;
        this.currentUser = user;
        this.quiz        = quiz;
        this.quizDAO     = quizDAO;
        this.questions   = questions;
        this.userAnswers = userAnswers;
        this.score       = score;
        this.total       = total;
        this.percentage  = (total > 0) ? ((double) score / total) * 100.0 : 0.0;

        setTitle("Quiz Result — " + quiz.getTitle());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1100, 750);
        setMinimumSize(new Dimension(900, 650));
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { goBack(); }
        });

        buildUI();
        setVisible(true);
    }

    // ─── BUILD UI ──────────────────────────────────────────────────────────────

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);
        add(root);

        // ── LEFT — Score summary ──────────────────────────────────────────────
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBackground(BLACK);
        left.setPreferredSize(new Dimension(380, Integer.MAX_VALUE));
        left.setBorder(new EmptyBorder(50, 40, 50, 40));

        JLabel completeLabel = QuizFrame.makeLabel("QUIZ COMPLETE!", QuizFrame.FONT_MED, YELLOW);
        completeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel quizNameLabel = QuizFrame.makeLabel(quiz.getTitle(), QuizFrame.FONT_BODY, new Color(0xCC,0xCC,0xCC));
        quizNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Big percentage
        String pctText = String.format("%.0f%%", percentage);
        JLabel pctLabel = new JLabel(pctText);
        pctLabel.setFont(new Font("Segoe UI", Font.BOLD, 80));
        pctLabel.setForeground(getScoreColor());
        pctLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel scoreSubLabel = QuizFrame.makeLabel("SCORE", QuizFrame.FONT_LABEL, new Color(0xAA,0xAA,0xAA));
        scoreSubLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel scoreDetailLabel = QuizFrame.makeLabel(score + " / " + total + " CORRECT",
                                                      QuizFrame.FONT_MED, WHITE);
        scoreDetailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Score progress bar
        JProgressBar bar = new JProgressBar(0, 100);
        bar.setValue((int) percentage);
        bar.setStringPainted(false);
        bar.setForeground(getScoreColor());
        bar.setBackground(new Color(0x33,0x33,0x33));
        bar.setBorderPainted(false);
        bar.setPreferredSize(new Dimension(0, 14));
        bar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 14));
        bar.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel barPctLabel = QuizFrame.makeLabel(pctText, QuizFrame.FONT_SMALL, new Color(0xAA,0xAA,0xAA));
        barPctLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Stat boxes
        JPanel statsGrid = new JPanel(new GridLayout(2, 2, 10, 10));
        statsGrid.setBackground(BLACK);
        statsGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));
        statsGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        statsGrid.add(makeResultStat("CORRECT",    String.valueOf(score),          GREEN));
        statsGrid.add(makeResultStat("WRONG",      String.valueOf(total - score),  RED));
        statsGrid.add(makeResultStat("TOTAL",      String.valueOf(total),          BLUE));
        statsGrid.add(makeResultStat("PERCENTAGE", String.format("%.1f%%", percentage), YELLOW));

        // Grade label
        JLabel gradeLabel = QuizFrame.makeLabel(getGrade(), new Font("Segoe UI", Font.BOLD, 18), getScoreColor());
        gradeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        gradeLabel.setBorder(new EmptyBorder(4, 0, 0, 0));

        // Buttons
        JButton reviewBtn = QuizFrame.makeButton("VIEW ANSWERS", BLUE, WHITE);
        reviewBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        reviewBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));

        JButton dashBtn   = QuizFrame.makeButton("BACK TO DASHBOARD", YELLOW, BLACK);
        dashBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        dashBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));

        reviewBtn.addActionListener(e -> toggleReview());
        dashBtn.addActionListener(e -> goBack());

        left.add(completeLabel);
        left.add(Box.createVerticalStrut(4));
        left.add(quizNameLabel);
        left.add(Box.createVerticalStrut(24));
        left.add(pctLabel);
        left.add(scoreSubLabel);
        left.add(Box.createVerticalStrut(8));
        left.add(scoreDetailLabel);
        left.add(Box.createVerticalStrut(20));
        left.add(bar);
        left.add(Box.createVerticalStrut(4));
        left.add(barPctLabel);
        left.add(Box.createVerticalStrut(24));
        left.add(statsGrid);
        left.add(Box.createVerticalStrut(12));
        left.add(gradeLabel);
        left.add(Box.createVerticalGlue());
        left.add(reviewBtn);
        left.add(Box.createVerticalStrut(10));
        left.add(dashBtn);

        // ── RIGHT — Answer review ──────────────────────────────────────────────
        reviewPanel = buildReviewPanel();
        reviewScroll = new JScrollPane(reviewPanel);
        reviewScroll.setBorder(BorderFactory.createEmptyBorder());
        reviewScroll.getViewport().setBackground(BG);
        reviewScroll.setVisible(false);  // hidden initially

        JPanel emptyRight = new JPanel(new GridBagLayout());
        emptyRight.setBackground(BG);
        emptyRight.setName("empty");

        JPanel centerMsg = new JPanel();
        centerMsg.setBackground(BG);
        centerMsg.setLayout(new BoxLayout(centerMsg, BoxLayout.Y_AXIS));

        JLabel emoji = QuizFrame.makeLabel(percentage >= 50 ? "🎉" : "📚",
                                           new Font("Segoe UI", Font.PLAIN, 60), BLACK);
        emoji.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel msg = QuizFrame.makeLabel(
            percentage >= 80 ? "Excellent work! Keep it up!" :
            percentage >= 50 ? "Good effort! Review and improve." :
                               "Keep studying and try again!",
            QuizFrame.FONT_MED, new Color(0x44,0x44,0x44));
        msg.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerMsg.add(emoji);
        centerMsg.add(Box.createVerticalStrut(12));
        centerMsg.add(msg);

        emptyRight.add(centerMsg);

        rightContainer = new JPanel(new CardLayout());
        rightContainer.setBackground(BG);
        rightContainer.add(emptyRight,  "EMPTY");
        rightContainer.add(reviewScroll, "REVIEW");
        rightContainerLayout = (CardLayout) rightContainer.getLayout();

        root.add(left, BorderLayout.WEST);
        root.add(rightContainer, BorderLayout.CENTER);
    }

    // Stored for toggling
    private JPanel     reviewPanel;
    private JScrollPane reviewScroll;
    private JPanel     rightContainer;
    private CardLayout rightContainerLayout;
    private boolean    reviewVisible = false;

    // ─── ANSWER REVIEW ─────────────────────────────────────────────────────────

    private JPanel buildReviewPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG);
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel header = QuizFrame.makeLabel("ANSWER REVIEW", QuizFrame.FONT_BIG, BLACK);
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(header);
        panel.add(Box.createVerticalStrut(20));

        for (int i = 0; i < questions.size(); i++) {
            Question q    = questions.get(i);
            String chosen = userAnswers[i];
            boolean correct = (chosen != null) && q.isCorrect(chosen);

            JPanel card = new JPanel();
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            card.setBackground(WHITE);
            card.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(2, 4, 4, 2, correct ? GREEN : RED),
                new EmptyBorder(14, 16, 14, 16)
            ));
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));

            // Question text
            JLabel qText = new JLabel("<html><b>Q" + (i+1) + ".</b> " + escapeHtml(q.getQuestionText()) + "</html>");
            qText.setFont(QuizFrame.FONT_BODY);
            qText.setAlignmentX(Component.LEFT_ALIGNMENT);

            // Status badge
            String statusText = correct ? "✓ CORRECT" : (chosen == null ? "⊘ NOT ANSWERED" : "✗ WRONG");
            Color  statusColor = correct ? GREEN       : RED;
            JLabel status = QuizFrame.makeLabel(statusText, QuizFrame.FONT_LABEL, statusColor);
            status.setAlignmentX(Component.LEFT_ALIGNMENT);

            card.add(qText);
            card.add(Box.createVerticalStrut(8));
            card.add(status);

            if (!correct) {
                if (chosen != null) {
                    JLabel yourAns = QuizFrame.makeLabel(
                        "Your answer:    " + chosen + " — " + escapeHtml(q.getOption(chosen)),
                        QuizFrame.FONT_SMALL, RED);
                    yourAns.setAlignmentX(Component.LEFT_ALIGNMENT);
                    card.add(yourAns);
                }
                JLabel corrAns = QuizFrame.makeLabel(
                    "Correct answer: " + q.getCorrectAnswer() + " — " + escapeHtml(q.getOption(q.getCorrectAnswer())),
                    QuizFrame.FONT_SMALL, GREEN);
                corrAns.setAlignmentX(Component.LEFT_ALIGNMENT);
                card.add(corrAns);
            }

            panel.add(card);
            panel.add(Box.createVerticalStrut(10));
        }

        return panel;
    }

    private void toggleReview() {
        reviewVisible = !reviewVisible;
        rightContainerLayout.show(rightContainer, reviewVisible ? "REVIEW" : "EMPTY");
    }

    // ─── NAVIGATION ────────────────────────────────────────────────────────────

    private void goBack() {
        dispose();
        parentFrame.onQuizComplete();
    }

    // ─── HELPERS ───────────────────────────────────────────────────────────────

    private Color getScoreColor() {
        if (percentage >= 80) return GREEN;
        if (percentage >= 50) return YELLOW;
        return RED;
    }

    private String getGrade() {
        if (percentage >= 90) return "🏆 Grade: A+  — Outstanding!";
        if (percentage >= 80) return "⭐ Grade: A   — Excellent!";
        if (percentage >= 70) return "✅ Grade: B   — Good job!";
        if (percentage >= 60) return "📘 Grade: C   — Satisfactory";
        if (percentage >= 50) return "⚠ Grade: D   — Needs Improvement";
        return "❌ Grade: F   — Better luck next time";
    }

    private JPanel makeResultStat(String label, String value, Color color) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(new Color(0x1A, 0x1A, 0x1A));
        p.setBorder(new EmptyBorder(10, 12, 10, 12));

        JLabel valLabel = QuizFrame.makeLabel(value, QuizFrame.FONT_BIG, color);
        valLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lbl = QuizFrame.makeLabel(label, QuizFrame.FONT_SMALL, new Color(0x88,0x88,0x88));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        p.add(valLabel);
        p.add(lbl);
        return p;
    }

    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
