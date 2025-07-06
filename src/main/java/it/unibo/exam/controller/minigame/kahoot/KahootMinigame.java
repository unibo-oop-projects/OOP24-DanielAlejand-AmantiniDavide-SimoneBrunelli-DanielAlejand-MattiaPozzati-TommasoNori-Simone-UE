package it.unibo.exam.controller.minigame.kahoot;

import it.unibo.exam.model.entity.minigame.Minigame;
import it.unibo.exam.model.entity.minigame.MinigameCallback;
import it.unibo.exam.model.entity.minigame.kahoot.KahootModel;
import it.unibo.exam.model.entity.minigame.kahoot.KahootListener;
import it.unibo.exam.model.entity.minigame.kahoot.QuizQuestion;
import it.unibo.exam.view.kahoot.KahootPanel;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Controller for the Kahoot-style quiz minigame implementing MVC pattern.
 * Enhanced with detailed results display showing real time, penalties, and score breakdown.
 */
public final class KahootMinigame implements Minigame, KahootListener {

    private static final int WINDOW_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 400;
    private static final int FEEDBACK_DELAY = 2000;
    private static final int PENALTY_SECONDS = 1;
    private static final int TITLE_FONT_SIZE = 24;
    private static final int HEADER_FONT_SIZE = 16;
    private static final int STAT_FONT_SIZE = 14;
    private static final int FINAL_TIME_FONT_SIZE = 20;
    private static final int BUTTON_FONT_SIZE = 16;
    private static final int SPACER_FONT_SIZE = 6;
    private static final int FIVE_PIXELS = 5;
    private static final int TEN_PIXELS = 10;
    private static final int TWENTY_PIXELS = 20;
    private static final int THIRTY_PIXELS = 30;
    private static final int FORTY_PIXELS = 40;
    private static final int BUTTON_WIDTH = 150;
    private static final int BUTTON_HEIGHT = 40;
    private static final int THREAD_SLEEP_DELAY = 1000;

    private static final List<QuizQuestion> DEFAULT_QUESTIONS = List.of(
        new QuizQuestion("What is the capital of Italy?",
                    List.of("Rome", "Milan", "Naples", "Turin"), 0),
        new QuizQuestion("Who wrote 'The Divine Comedy'?",
                    List.of("Petrarch", "Boccaccio", "Dante", "Manzoni"), 2),
        new QuizQuestion("What is the result of 2 + 2?",
                    List.of("3", "4", "5", "6"), 1),
        new QuizQuestion("In what year did the Berlin Wall fall?",
                    List.of("1987", "1988", "1989", "1990"), 2),
        new QuizQuestion("Which planet is closest to the Sun?",
                    List.of("Venus", "Mercury", "Earth", "Mars"), 1)
    );

    private JFrame gameFrame;
    private MinigameCallback callback;
    private KahootModel model;
    private KahootPanel view;

    /**
     * Creates a new KahootMinigame with default questions.
     */
    public KahootMinigame() {
        this(DEFAULT_QUESTIONS);
    }

    /**
     * Creates a new KahootMinigame with custom questions.
     *
     * @param questions the list of questions for the quiz
     */
    public KahootMinigame(final List<QuizQuestion> questions) {
        this.model = new KahootModel(questions);
    }

    @Override
    public void start(final JFrame parent, final MinigameCallback callback) {
        this.callback = callback;

        createGameWindow(parent);
        setupMVC();

        model.startQuiz();
        showCurrentQuestion();
    }

    @Override
    public void stop() {
        if (gameFrame != null) {
            gameFrame.dispose();
        }
    }

    @Override
    public String getName() {
        return "Quiz Kahoot";
    }

    @Override
    public String getDescription() {
        return "Answer all questions correctly to win! Wrong answers add 1 second penalty.";
    }

    @Override
    public void onQuizStarted() {
        // Quiz has started, view is ready
    }

    @Override
    public void onAnswerSubmitted(final boolean isCorrect, final String correctAnswer) {
        view.disableAnswerButtons();

        javax.swing.SwingUtilities.invokeLater(() -> {
            view.showFeedback(isCorrect, correctAnswer);

            new Thread(() -> {
                try {
                    Thread.sleep(FEEDBACK_DELAY);

                    javax.swing.SwingUtilities.invokeLater(() -> {
                        if (model.isGameCompleted()) {
                            showDetailedFinalResults();
                        } else {
                            showCurrentQuestion();
                        }
                    });

                } catch (final InterruptedException e) {
                    Thread.currentThread().interrupt();
                    javax.swing.SwingUtilities.invokeLater(this::showDetailedFinalResults);
                }
            }).start();
        });
    }

    @Override
    public void onNextQuestion(final QuizQuestion question) {
        view.showQuestion(question);
    }

    @Override
    public void onQuizCompleted(final boolean success, final int elapsedTime,
                               final int correctAnswers, final int wrongAnswers) {
        // Results will be shown by onAnswerSubmitted after feedback delay
    }

    private void createGameWindow(final JFrame parent) {
        gameFrame = new JFrame("Quiz Kahoot - " + getName());
        gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gameFrame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        gameFrame.setLocationRelativeTo(parent);
        gameFrame.setResizable(false);

        gameFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(final java.awt.event.WindowEvent e) {
                if (callback != null) {
                    final int finalTime = model != null 
                        ? model.getFinalTimeWithPenalty(PENALTY_SECONDS) 
                        : 0;
                    callback.onComplete(false, finalTime);
                }
            }
        });
    }

    private void setupMVC() {
        view = new KahootPanel(model);

        model.addListener(this);

        view.setAnswerClickListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final int selectedAnswer = Integer.parseInt(e.getActionCommand());
                model.submitAnswer(selectedAnswer);
            }
        });

        gameFrame.add(view);
        gameFrame.setVisible(true);
    }

    private void showCurrentQuestion() {
        final QuizQuestion currentQuestion = model.getCurrentQuestion();
        if (currentQuestion != null) {
            view.showQuestion(currentQuestion);
        }
    }

    /**
     * Shows detailed final results with synchronized timing values.
     */
    private void showDetailedFinalResults() {
        final boolean success = model.hasPassedQuiz();
        final int baseTime = model.getElapsedTimeSeconds();
        final int correctAnswers = model.getCorrectAnswers();
        final int wrongAnswers = model.getWrongAnswers();
        final int finalTime = model.getFinalTimeWithPenalty(PENALTY_SECONDS);
        final int totalQuestions = model.getTotalQuestions();

        gameFrame.getContentPane().removeAll();

        final JPanel resultsPanel = createDetailedResultsPanel(
            success, baseTime, finalTime, correctAnswers, wrongAnswers, totalQuestions);

        gameFrame.add(resultsPanel);
        gameFrame.revalidate();
        gameFrame.repaint();

        new Thread(() -> {
            try {
                Thread.sleep(THREAD_SLEEP_DELAY);
                javax.swing.SwingUtilities.invokeLater(() -> addOkButtonToDetailedResults(resultsPanel));
            } catch (final InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    /**
     * Creates a detailed results panel showing all statistics.
     *
     * @param success whether the quiz was passed
     * @param baseTime the base time taken
     * @param finalTime the final time including penalties
     * @param correctAnswers the number of correct answers
     * @param wrongAnswers the number of wrong answers
     * @param totalQuestions the total number of questions
     * @return the detailed results panel
     */
    private JPanel createDetailedResultsPanel(final boolean success, final int baseTime,
                                            final int finalTime, final int correctAnswers,
                                            final int wrongAnswers, final int totalQuestions) {
        final JPanel panel = new JPanel(new BorderLayout());

        final Color bgColor = success
            ? new Color(76, 175, 80)
            : new Color(244, 67, 54);

        panel.setBackground(bgColor);

        final JLabel titleLabel = new JLabel(
            success ? "QUIZ COMPLETED!" : "QUIZ FAILED",
            SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, TITLE_FONT_SIZE));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(TWENTY_PIXELS, TEN_PIXELS, 
            TWENTY_PIXELS, TEN_PIXELS));

        final JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new javax.swing.BoxLayout(statsPanel, javax.swing.BoxLayout.Y_AXIS));
        statsPanel.setBackground(bgColor);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(TEN_PIXELS, THIRTY_PIXELS, 
            TWENTY_PIXELS, THIRTY_PIXELS));

        addStatsHeader(statsPanel, "SCORE", bgColor);
        addStatLine(statsPanel, String.format("Correct Answers: %d/%d", correctAnswers, totalQuestions), bgColor);
        addStatLine(statsPanel, String.format("Wrong Answers: %d", wrongAnswers), bgColor);

        final double percentage = (correctAnswers * 100.0) / totalQuestions;
        addStatLine(statsPanel, String.format("Percentage: %.1f%%", percentage), bgColor);

        addSpacer(statsPanel);

        addStatsHeader(statsPanel, "TIME ANALYSIS", bgColor);
        addStatLine(statsPanel, String.format("Real Time: %d seconds", baseTime), bgColor);

        if (wrongAnswers > 0) {
            final int totalPenalty = wrongAnswers * PENALTY_SECONDS;
            addStatLine(statsPanel, String.format("Penalty: +%d seconds", totalPenalty), bgColor);
            addStatLine(statsPanel, String.format("(%d wrong - total: %d sec)", wrongAnswers, totalPenalty), bgColor);
        } else {
            addStatLine(statsPanel, "No penalties! Perfect!", bgColor);
        }

        addSpacer(statsPanel);

        final JLabel finalTimeLabel = new JLabel(
            String.format("FINAL TIME: %d seconds", finalTime),
            SwingConstants.CENTER);
        finalTimeLabel.setFont(new Font("Arial", Font.BOLD, FINAL_TIME_FONT_SIZE));
        finalTimeLabel.setForeground(Color.YELLOW);
        finalTimeLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        finalTimeLabel.setBorder(BorderFactory.createEmptyBorder(TEN_PIXELS, 0, TEN_PIXELS, 0));
        statsPanel.add(finalTimeLabel);

        addSpacer(statsPanel);
        addStatsHeader(statsPanel, "PERFORMANCE", bgColor);
        // final String performance = getPerformanceRating(correctAnswers, totalQuestions, finalTime);
        // addStatLine(statsPanel, performance, bgColor);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(statsPanel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Adds a statistics header to the panel.
     *
     * @param parent the parent panel
     * @param text the header text
     * @param bgColor the background color
     */
    private void addStatsHeader(final JPanel parent, final String text, final Color bgColor) {
        final JLabel header = new JLabel(text, SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, HEADER_FONT_SIZE));
        header.setForeground(Color.YELLOW);
        header.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        header.setBorder(BorderFactory.createEmptyBorder(FIVE_PIXELS, 0, FIVE_PIXELS, 0));
        parent.add(header);
    }

    /**
     * Adds a statistics line to the panel.
     *
     * @param parent the parent panel
     * @param text the stat text
     * @param bgColor the background color
     */
    private void addStatLine(final JPanel parent, final String text, final Color bgColor) {
        final JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, STAT_FONT_SIZE));
        label.setForeground(Color.WHITE);
        label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        parent.add(label);
    }

    /**
     * Adds a spacer to separate sections.
     *
     * @param parent the parent panel
     */
    private void addSpacer(final JPanel parent) {
        final JLabel spacer = new JLabel(" ");
        spacer.setFont(new Font("Arial", Font.PLAIN, SPACER_FONT_SIZE));
        spacer.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        parent.add(spacer);
    }

    // /**
    //  * Generates a performance rating based on accuracy and speed.
    //  */
    // private String getPerformanceRating(final int correct, final int total, final int time) {
    //     final double accuracy = (double) correct / total;
    //     final String accuracyRating;
    //
    //     if (accuracy >= 0.9) {
    //         accuracyRating = "EXCELLENT";
    //     } else if (accuracy >= 0.7) {
    //         accuracyRating = "GOOD";
    //     } else if (accuracy >= 0.5) {
    //         accuracyRating = "FAIR";
    //     } else {
    //         accuracyRating = "KEEP STUDYING";
    //     }
    //
    //     final String speedRating;
    //     if (time <= 10) {
    //         speedRating = "LIGHTNING FAST";
    //     } else if (time <= 18) {
    //         speedRating = "SPEEDY";
    //     } else if (time <= 20) {
    //         speedRating = "STEADY";
    //     } else {
    //         speedRating = "THOUGHTFUL";
    //     }
    //
    //     return String.format("%s • %s", accuracyRating, speedRating);
    // }

    /**
     * Adds an OK button to the detailed results panel.
     *
     * @param resultsPanel the results panel to add the button to
     */
    private void addOkButtonToDetailedResults(final JPanel resultsPanel) {
        final JButton okButton = new JButton("Continue");
        okButton.setFont(new Font("Arial", Font.BOLD, BUTTON_FONT_SIZE));
        okButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        okButton.setBackground(Color.WHITE);
        okButton.setForeground(Color.BLACK);
        okButton.setFocusPainted(false);
        okButton.setBorder(BorderFactory.createRaisedBevelBorder());

        okButton.addActionListener(e -> {
            gameFrame.dispose();
            if (callback != null) {
                final int finalTime = model.getFinalTimeWithPenalty(PENALTY_SECONDS);
                callback.onComplete(true, finalTime);
            }
        });

        final JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(resultsPanel.getBackground());
        buttonPanel.add(okButton);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(TEN_PIXELS, TWENTY_PIXELS, 
            TWENTY_PIXELS, TWENTY_PIXELS));

        resultsPanel.add(buttonPanel, BorderLayout.SOUTH);
        gameFrame.revalidate();
        gameFrame.repaint();

        okButton.requestFocusInWindow();
    }
}
