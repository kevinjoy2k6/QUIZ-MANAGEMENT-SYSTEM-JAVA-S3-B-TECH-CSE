/**
 * Quiz.java
 *
 * Model class representing a Quiz in the system.
 *
 * OOP Concept: Encapsulation — all fields private with getters/setters.
 */
import java.util.ArrayList;
import java.util.List;

public class Quiz {

    // ─── FIELDS ───────────────────────────────────────────────────────────────
    private int    id;
    private String title;
    private String description;
    private int    categoryId;
    private String categoryName;   // for display purposes (joined from categories table)
    private int    timeLimit;      // in minutes
    private List<Question> questions;

    // ─── CONSTRUCTORS ─────────────────────────────────────────────────────────

    /** Default constructor */
    public Quiz() {
        this.questions = new ArrayList<>();
    }

    /** Constructor for creating a new quiz (no ID yet) */
    public Quiz(String title, String description, int categoryId, int timeLimit) {
        this.title       = title;
        this.description = description;
        this.categoryId  = categoryId;
        this.timeLimit   = timeLimit;
        this.questions   = new ArrayList<>();
    }

    /** Full constructor (loaded from database) */
    public Quiz(int id, String title, String description, int categoryId,
                String categoryName, int timeLimit) {
        this.id           = id;
        this.title        = title;
        this.description  = description;
        this.categoryId   = categoryId;
        this.categoryName = categoryName;
        this.timeLimit    = timeLimit;
        this.questions    = new ArrayList<>();
    }

    // ─── GETTERS ──────────────────────────────────────────────────────────────

    public int    getId()           { return id; }
    public String getTitle()        { return title; }
    public String getDescription()  { return description; }
    public int    getCategoryId()   { return categoryId; }
    public String getCategoryName() { return categoryName; }
    public int    getTimeLimit()    { return timeLimit; }
    public List<Question> getQuestions() { return questions; }

    // ─── SETTERS ──────────────────────────────────────────────────────────────

    public void setId(int id)                   { this.id = id; }
    public void setTitle(String t)              { this.title = t; }
    public void setDescription(String d)        { this.description = d; }
    public void setCategoryId(int cid)          { this.categoryId = cid; }
    public void setCategoryName(String name)    { this.categoryName = name; }
    public void setTimeLimit(int t)             { this.timeLimit = t; }
    public void setQuestions(List<Question> q)  { this.questions = q; }

    // ─── UTILITY ──────────────────────────────────────────────────────────────

    /** Adds a single question to the quiz's question list */
    public void addQuestion(Question q) {
        this.questions.add(q);
    }

    /** Returns the number of questions in this quiz */
    public int getQuestionCount() {
        return questions == null ? 0 : questions.size();
    }

    @Override
    public String toString() {
        return title + " (" + getQuestionCount() + " questions, " + timeLimit + " min)";
    }
}
