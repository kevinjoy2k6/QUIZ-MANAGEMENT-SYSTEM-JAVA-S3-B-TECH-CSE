/**
 * QuizDAO.java
 *
 * Data Access Object for Quiz, Question, Category, and Result operations.
 * All database queries use PreparedStatement to prevent SQL injection.
 *
 * OOP Concept: Abstraction — hides SQL complexity from the GUI layer.
 * JDBC: PreparedStatement, ResultSet, proper resource closing.
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuizDAO {

    // ══════════════════════════════════════════════════════════
    //  QUIZ OPERATIONS
    // ══════════════════════════════════════════════════════════

    /**
     * Returns all quizzes from the database (with category name joined).
     */
    public List<Quiz> getAllQuizzes() {
        List<Quiz> quizzes = new ArrayList<>();
        String sql = "SELECT q.id, q.title, q.description, q.category_id, " +
                     "       COALESCE(c.name, 'General') AS category_name, q.time_limit " +
                     "FROM quizzes q " +
                     "LEFT JOIN categories c ON q.category_id = c.id " +
                     "ORDER BY q.id";

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = DatabaseConnection.getConnection().prepareStatement(sql);
            rs   = stmt.executeQuery();
            while (rs.next()) {
                Quiz quiz = new Quiz(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getInt("category_id"),
                    rs.getString("category_name"),
                    rs.getInt("time_limit")
                );
                quizzes.add(quiz);
            }
        } catch (SQLException e) {
            System.err.println("[QuizDAO] getAllQuizzes error: " + e.getMessage());
        } finally {
            closeResources(rs, stmt);
        }
        return quizzes;
    }

    /**
     * Returns a single Quiz by its ID (without questions).
     */
    public Quiz getQuizById(int quizId) {
        String sql = "SELECT q.id, q.title, q.description, q.category_id, " +
                     "       COALESCE(c.name, 'General') AS category_name, q.time_limit " +
                     "FROM quizzes q " +
                     "LEFT JOIN categories c ON q.category_id = c.id " +
                     "WHERE q.id = ?";

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = DatabaseConnection.getConnection().prepareStatement(sql);
            stmt.setInt(1, quizId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return new Quiz(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getInt("category_id"),
                    rs.getString("category_name"),
                    rs.getInt("time_limit")
                );
            }
        } catch (SQLException e) {
            System.err.println("[QuizDAO] getQuizById error: " + e.getMessage());
        } finally {
            closeResources(rs, stmt);
        }
        return null;
    }

    /**
     * Inserts a new quiz into the database.
     *
     * @return the generated quiz ID, or -1 on failure
     */
    public int createQuiz(Quiz quiz) {
        String sql = "INSERT INTO quizzes (title, description, category_id, time_limit) VALUES (?, ?, ?, ?)";

        PreparedStatement stmt = null;
        ResultSet keys = null;

        try {
            stmt = DatabaseConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, quiz.getTitle());
            stmt.setString(2, quiz.getDescription());
            stmt.setInt(3, quiz.getCategoryId());
            stmt.setInt(4, quiz.getTimeLimit());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                keys = stmt.getGeneratedKeys();
                if (keys.next()) return keys.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("[QuizDAO] createQuiz error: " + e.getMessage());
        } finally {
            if (keys != null) try { keys.close(); } catch (SQLException ignored) {}
            closeResources(null, stmt);
        }
        return -1;
    }

    /**
     * Updates an existing quiz's details.
     *
     * @return true if update succeeded
     */
    public boolean updateQuiz(Quiz quiz) {
        String sql = "UPDATE quizzes SET title = ?, description = ?, category_id = ?, time_limit = ? WHERE id = ?";

        PreparedStatement stmt = null;
        try {
            stmt = DatabaseConnection.getConnection().prepareStatement(sql);
            stmt.setString(1, quiz.getTitle());
            stmt.setString(2, quiz.getDescription());
            stmt.setInt(3, quiz.getCategoryId());
            stmt.setInt(4, quiz.getTimeLimit());
            stmt.setInt(5, quiz.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[QuizDAO] updateQuiz error: " + e.getMessage());
        } finally {
            closeResources(null, stmt);
        }
        return false;
    }

    /**
     * Deletes a quiz and all its questions (CASCADE handles questions).
     *
     * @return true if deletion succeeded
     */
    public boolean deleteQuiz(int quizId) {
        String sql = "DELETE FROM quizzes WHERE id = ?";
        PreparedStatement stmt = null;

        try {
            stmt = DatabaseConnection.getConnection().prepareStatement(sql);
            stmt.setInt(1, quizId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[QuizDAO] deleteQuiz error: " + e.getMessage());
        } finally {
            closeResources(null, stmt);
        }
        return false;
    }

    /**
     * Returns the count of questions for a given quiz.
     */
    public int getQuestionCount(int quizId) {
        String sql = "SELECT COUNT(*) FROM questions WHERE quiz_id = ?";
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = DatabaseConnection.getConnection().prepareStatement(sql);
            stmt.setInt(1, quizId);
            rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("[QuizDAO] getQuestionCount error: " + e.getMessage());
        } finally {
            closeResources(rs, stmt);
        }
        return 0;
    }

    /**
     * Returns total number of quizzes.
     */
    public int getTotalQuizzes() {
        String sql = "SELECT COUNT(*) FROM quizzes";
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = DatabaseConnection.getConnection().prepareStatement(sql);
            rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("[QuizDAO] getTotalQuizzes error: " + e.getMessage());
        } finally {
            closeResources(rs, stmt);
        }
        return 0;
    }

    /**
     * Returns total number of questions across all quizzes.
     */
    public int getTotalQuestions() {
        String sql = "SELECT COUNT(*) FROM questions";
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = DatabaseConnection.getConnection().prepareStatement(sql);
            rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("[QuizDAO] getTotalQuestions error: " + e.getMessage());
        } finally {
            closeResources(rs, stmt);
        }
        return 0;
    }

    /**
     * Returns total number of quiz attempts (all results).
     */
    public int getTotalAttempts() {
        String sql = "SELECT COUNT(*) FROM results";
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = DatabaseConnection.getConnection().prepareStatement(sql);
            rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("[QuizDAO] getTotalAttempts error: " + e.getMessage());
        } finally {
            closeResources(rs, stmt);
        }
        return 0;
    }

    // ══════════════════════════════════════════════════════════
    //  QUESTION OPERATIONS
    // ══════════════════════════════════════════════════════════

    /**
     * Returns all questions for a given quiz ID.
     */
    public List<Question> getQuestionsByQuizId(int quizId) {
        List<Question> questions = new ArrayList<>();
        String sql = "SELECT id, quiz_id, question_text, option_a, option_b, option_c, option_d, correct_answer " +
                     "FROM questions WHERE quiz_id = ? ORDER BY id";

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = DatabaseConnection.getConnection().prepareStatement(sql);
            stmt.setInt(1, quizId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                questions.add(new Question(
                    rs.getInt("id"),
                    rs.getInt("quiz_id"),
                    rs.getString("question_text"),
                    rs.getString("option_a"),
                    rs.getString("option_b"),
                    rs.getString("option_c"),
                    rs.getString("option_d"),
                    rs.getString("correct_answer")
                ));
            }
        } catch (SQLException e) {
            System.err.println("[QuizDAO] getQuestions error: " + e.getMessage());
        } finally {
            closeResources(rs, stmt);
        }
        return questions;
    }

    /**
     * Inserts a new question.
     *
     * @return the generated question ID, or -1 on failure
     */
    public int addQuestion(Question q) {
        String sql = "INSERT INTO questions (quiz_id, question_text, option_a, option_b, option_c, option_d, correct_answer) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement stmt = null;
        ResultSet keys = null;

        try {
            stmt = DatabaseConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, q.getQuizId());
            stmt.setString(2, q.getQuestionText());
            stmt.setString(3, q.getOptionA());
            stmt.setString(4, q.getOptionB());
            stmt.setString(5, q.getOptionC());
            stmt.setString(6, q.getOptionD());
            stmt.setString(7, q.getCorrectAnswer());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                keys = stmt.getGeneratedKeys();
                if (keys.next()) return keys.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("[QuizDAO] addQuestion error: " + e.getMessage());
        } finally {
            if (keys != null) try { keys.close(); } catch (SQLException ignored) {}
            closeResources(null, stmt);
        }
        return -1;
    }

    /**
     * Updates an existing question.
     *
     * @return true if update succeeded
     */
    public boolean updateQuestion(Question q) {
        String sql = "UPDATE questions SET question_text=?, option_a=?, option_b=?, option_c=?, option_d=?, correct_answer=? WHERE id=?";

        PreparedStatement stmt = null;
        try {
            stmt = DatabaseConnection.getConnection().prepareStatement(sql);
            stmt.setString(1, q.getQuestionText());
            stmt.setString(2, q.getOptionA());
            stmt.setString(3, q.getOptionB());
            stmt.setString(4, q.getOptionC());
            stmt.setString(5, q.getOptionD());
            stmt.setString(6, q.getCorrectAnswer());
            stmt.setInt(7, q.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[QuizDAO] updateQuestion error: " + e.getMessage());
        } finally {
            closeResources(null, stmt);
        }
        return false;
    }

    /**
     * Deletes a question by its ID.
     *
     * @return true if deletion succeeded
     */
    public boolean deleteQuestion(int questionId) {
        String sql = "DELETE FROM questions WHERE id = ?";
        PreparedStatement stmt = null;

        try {
            stmt = DatabaseConnection.getConnection().prepareStatement(sql);
            stmt.setInt(1, questionId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[QuizDAO] deleteQuestion error: " + e.getMessage());
        } finally {
            closeResources(null, stmt);
        }
        return false;
    }

    // ══════════════════════════════════════════════════════════
    //  RESULT OPERATIONS
    // ══════════════════════════════════════════════════════════

    /**
     * Saves a quiz result to the database.
     *
     * @param userId         the user who took the quiz
     * @param quizId         the quiz that was taken
     * @param score          number of correct answers
     * @param totalQuestions total number of questions
     * @return true if save succeeded
     */
    public boolean saveResult(int userId, int quizId, int score, int totalQuestions) {
        double percentage = (totalQuestions > 0) ? ((double) score / totalQuestions) * 100.0 : 0.0;
        String sql = "INSERT INTO results (user_id, quiz_id, score, total_questions, percentage) VALUES (?, ?, ?, ?, ?)";

        PreparedStatement stmt = null;
        try {
            stmt = DatabaseConnection.getConnection().prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, quizId);
            stmt.setInt(3, score);
            stmt.setInt(4, totalQuestions);
            stmt.setDouble(5, percentage);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[QuizDAO] saveResult error: " + e.getMessage());
        } finally {
            closeResources(null, stmt);
        }
        return false;
    }

    /**
     * Returns a formatted list of all results for a given user.
     * Each entry is a String array: [quizTitle, score/total, percentage, date]
     */
    public List<String[]> getResultsByUser(int userId) {
        List<String[]> results = new ArrayList<>();
        String sql = "SELECT q.title, r.score, r.total_questions, r.percentage, r.attempt_date " +
                     "FROM results r " +
                     "JOIN quizzes q ON r.quiz_id = q.id " +
                     "WHERE r.user_id = ? " +
                     "ORDER BY r.attempt_date DESC";

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = DatabaseConnection.getConnection().prepareStatement(sql);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                results.add(new String[]{
                    rs.getString("title"),
                    rs.getInt("score") + " / " + rs.getInt("total_questions"),
                    String.format("%.1f%%", rs.getDouble("percentage")),
                    rs.getString("attempt_date").substring(0, 10)   // just the date
                });
            }
        } catch (SQLException e) {
            System.err.println("[QuizDAO] getResultsByUser error: " + e.getMessage());
        } finally {
            closeResources(rs, stmt);
        }
        return results;
    }

    /**
     * Returns all results across all users (for admin view).
     * Each entry: [userName, quizTitle, score/total, percentage, date]
     */
    public List<String[]> getAllResults() {
        List<String[]> results = new ArrayList<>();
        String sql = "SELECT u.full_name, q.title, r.score, r.total_questions, r.percentage, r.attempt_date " +
                     "FROM results r " +
                     "JOIN users u ON r.user_id = u.id " +
                     "JOIN quizzes q ON r.quiz_id = q.id " +
                     "ORDER BY r.attempt_date DESC";

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = DatabaseConnection.getConnection().prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                results.add(new String[]{
                    rs.getString("full_name"),
                    rs.getString("title"),
                    rs.getInt("score") + " / " + rs.getInt("total_questions"),
                    String.format("%.1f%%", rs.getDouble("percentage")),
                    rs.getString("attempt_date").substring(0, 10)
                });
            }
        } catch (SQLException e) {
            System.err.println("[QuizDAO] getAllResults error: " + e.getMessage());
        } finally {
            closeResources(rs, stmt);
        }
        return results;
    }

    /**
     * Returns quiz statistics for a user: [quizzesTaken, avgPercentage, bestScore]
     */
    public double[] getUserStats(int userId) {
        String sql = "SELECT COUNT(*) AS taken, " +
                     "       COALESCE(AVG(percentage),0) AS avg_pct, " +
                     "       COALESCE(MAX(percentage),0) AS best " +
                     "FROM results WHERE user_id = ?";

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = DatabaseConnection.getConnection().prepareStatement(sql);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return new double[]{
                    rs.getInt("taken"),
                    rs.getDouble("avg_pct"),
                    rs.getDouble("best")
                };
            }
        } catch (SQLException e) {
            System.err.println("[QuizDAO] getUserStats error: " + e.getMessage());
        } finally {
            closeResources(rs, stmt);
        }
        return new double[]{0, 0, 0};
    }

    // ══════════════════════════════════════════════════════════
    //  CATEGORY OPERATIONS
    // ══════════════════════════════════════════════════════════

    /**
     * Returns all categories as [id, name] pairs.
     */
    public List<String[]> getAllCategories() {
        List<String[]> cats = new ArrayList<>();
        String sql = "SELECT id, name FROM categories ORDER BY name";
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = DatabaseConnection.getConnection().prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                cats.add(new String[]{ String.valueOf(rs.getInt("id")), rs.getString("name") });
            }
        } catch (SQLException e) {
            System.err.println("[QuizDAO] getAllCategories error: " + e.getMessage());
        } finally {
            closeResources(rs, stmt);
        }
        return cats;
    }

    // ─── PRIVATE HELPER ───────────────────────────────────────────────────────

    private void closeResources(ResultSet rs, PreparedStatement stmt) {
        try { if (rs   != null) rs.close();   } catch (SQLException ignored) {}
        try { if (stmt != null) stmt.close(); } catch (SQLException ignored) {}
    }
}
