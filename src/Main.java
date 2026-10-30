/**
 * Main.java
 *
 * Application entry point for the Quiz Management System.
 *
 * Responsibilities:
 * 1. Set the Look and Feel to Nimbus for better font rendering
 * 2. Test the database connection on startup
 * 3. Launch QuizFrame on the Swing Event Dispatch Thread (EDT)
 *
 * OOP Concept: Single Responsibility — this class only starts the application.
 */

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {

        // ── Set Look and Feel ──────────────────────────────────────────────────
        // Nimbus gives better default fonts than Metal; our custom painting overrides the rest.
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Fall back to system L&F if Nimbus unavailable
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
        }

        // ── Enable antialiased text system-wide ───────────────────────────────
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        // ── Check database connection BEFORE opening UI ────────────────────────
        if (!DatabaseConnection.testConnection()) {
            // Show error dialog with helpful instructions
            JOptionPane.showMessageDialog(
                null,
                "⚠  Could not connect to the MySQL database!\n\n" +
                "Please make sure:\n" +
                "  1. MySQL server is running\n" +
                "  2. The database 'quiz_management' exists\n" +
                "  3. Your credentials are correct in DatabaseConnection.java\n\n" +
                "Default credentials:  root / root\n\n" +
                "Run: database/quiz_management.sql to set up the database.",
                "Database Connection Error",
                JOptionPane.ERROR_MESSAGE
            );
            System.exit(1);   // Exit if DB is unavailable
        }

        // ── Launch the main application window on the EDT ─────────────────────
        SwingUtilities.invokeLater(() -> {
            new QuizFrame();
        });
    }
}
