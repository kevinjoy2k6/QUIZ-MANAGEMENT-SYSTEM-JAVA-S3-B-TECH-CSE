/**
 * Question.java
 *
 * Model class representing a single quiz question with four options.
 *
 * OOP Concept: Encapsulation — all fields private with getters/setters.
 */
public class Question {

    // ─── FIELDS ───────────────────────────────────────────────────────────────
    private int    id;
    private int    quizId;
    private String questionText;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctAnswer;   // "A", "B", "C", or "D"

    // ─── CONSTRUCTORS ─────────────────────────────────────────────────────────

    /** Default constructor */
    public Question() {}

    /** Constructor for creating a new question (no ID yet) */
    public Question(int quizId, String questionText,
                    String optionA, String optionB,
                    String optionC, String optionD,
                    String correctAnswer) {
        this.quizId        = quizId;
        this.questionText  = questionText;
        this.optionA       = optionA;
        this.optionB       = optionB;
        this.optionC       = optionC;
        this.optionD       = optionD;
        this.correctAnswer = correctAnswer.toUpperCase();
    }

    /** Full constructor (loaded from database) */
    public Question(int id, int quizId, String questionText,
                    String optionA, String optionB,
                    String optionC, String optionD,
                    String correctAnswer) {
        this.id            = id;
        this.quizId        = quizId;
        this.questionText  = questionText;
        this.optionA       = optionA;
        this.optionB       = optionB;
        this.optionC       = optionC;
        this.optionD       = optionD;
        this.correctAnswer = correctAnswer.toUpperCase();
    }

    // ─── GETTERS ──────────────────────────────────────────────────────────────

    public int    getId()            { return id; }
    public int    getQuizId()        { return quizId; }
    public String getQuestionText()  { return questionText; }
    public String getOptionA()       { return optionA; }
    public String getOptionB()       { return optionB; }
    public String getOptionC()       { return optionC; }
    public String getOptionD()       { return optionD; }
    public String getCorrectAnswer() { return correctAnswer; }

    // ─── SETTERS ──────────────────────────────────────────────────────────────

    public void setId(int id)                      { this.id = id; }
    public void setQuizId(int qid)                 { this.quizId = qid; }
    public void setQuestionText(String t)          { this.questionText = t; }
    public void setOptionA(String a)               { this.optionA = a; }
    public void setOptionB(String b)               { this.optionB = b; }
    public void setOptionC(String c)               { this.optionC = c; }
    public void setOptionD(String d)               { this.optionD = d; }
    public void setCorrectAnswer(String ans) {
        this.correctAnswer = ans.toUpperCase();
    }

    // ─── UTILITY ──────────────────────────────────────────────────────────────

    /**
     * Returns the option text for a given letter.
     * e.g., getOption("A") → optionA value
     */
    public String getOption(String letter) {
        switch (letter.toUpperCase()) {
            case "A": return optionA;
            case "B": return optionB;
            case "C": return optionC;
            case "D": return optionD;
            default:  return "";
        }
    }

    /**
     * Checks if the given answer letter matches the correct answer.
     */
    public boolean isCorrect(String answer) {
        return correctAnswer.equalsIgnoreCase(answer);
    }

    @Override
    public String toString() {
        return "Q" + id + ": " + questionText + " [Answer: " + correctAnswer + "]";
    }
}
