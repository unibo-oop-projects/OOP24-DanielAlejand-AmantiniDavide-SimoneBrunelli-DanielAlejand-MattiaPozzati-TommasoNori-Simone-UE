package it.unibo.exam.controller.minigame.kahoot;

import it.unibo.exam.model.entity.minigame.Minigame;
import it.unibo.exam.model.entity.minigame.MinigameCallback;
import it.unibo.exam.model.entity.minigame.kahoot.KahootModel;
import it.unibo.exam.model.entity.minigame.kahoot.KahootListener;
import it.unibo.exam.model.entity.minigame.kahoot.QuizQuestion;
import it.unibo.exam.view.kahoot.KahootPanel;
import it.unibo.exam.model.scoring.ScoringStrategy;
import it.unibo.exam.model.scoring.TieredScoringStrategy;
import it.unibo.exam.model.scoring.TimeBonusDecorator;
import it.unibo.exam.model.scoring.CapDecorator;

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
import java.util.Objects;

/**
 * Controller for the Kahoot-style quiz minigame implementing MVC pattern.
 * Enhanced with Strategy + Decorator pattern for flexible scoring system.
 */
public final class KahootMinigame implements Minigame, KahootListener {

    private static final int WINDOW_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 400;
    private static final int FEEDBACK_DELAY = 2000;
    private static final int PENALTY_SECONDS = 10;
    private static final int ROOM_ID = 2; // Kahoot is in room 2

    // Scoring configuration constants
    private static final int BONUS_TIME_THRESHOLD_SECONDS = 15; // Faster threshold for quiz
    private static final int BONUS_POINTS = 15;
    private static final int MAX_POINTS_CAP = 120;

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
    private static final int BUTTON_WIDTH = 150;
    private static final int BUTTON_HEIGHT = 40;
    private static final int THREAD_SLEEP_DELAY = 1000;
    private static final String FONT_FAMILY = "Arial";

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
    private final ScoringStrategy scoringStrategy;

    /**
     * No-arg constructor for factory instantiation (uses default scoring).
     * Configures scoring strategy with time bonus and point cap decorators.
     */
    public KahootMinigame() {
        this(
            new CapDecorator(
                new TimeBonusDecorator(
                    new TieredScoringStrategy(),
                    BONUS_TIME_THRESHOLD_SECONDS,
                    BONUS_POINTS
                ),
                MAX_POINTS_CAP
            )
        );
    }

    /**
     * Full constructor allows custom scoring strategy.
     *
     * @param scoringStrategy the strategy used to compute final score
     */
    public KahootMinigame(final ScoringStrategy scoringStrategy) {
        this.scoringStrategy = Objects.requireNonNull(scoringStrategy,
            "scoringStrategy must not be null");
    }

    /**
     * Creates a new KahootMinigame with custom questions and default scoring.
     *
     * @param questions the list of questions for the quiz
     */
    public KahootMinigame(final List<QuizQuestion> questions) {
        this();
        this.model = new KahootModel(questions);
    }

    /**
     * Creates a new KahootMinigame with custom questions and scoring strategy.
     *
     * @param questions the list of questions for the quiz
     * @param scoringStrategy the strategy used to compute final score
     */
    public KahootMinigame(final List<QuizQuestion> questions, final ScoringStrategy scoringStrategy) {
        this.scoringStrategy = Objects.requireNonNull(scoringStrategy,
            "scoringStrategy must not be null");
        this.model = new KahootModel(questions);
    }

    @Override
    public void start(final JFrame parent, final MinigameCallback callback) {
        this.callback = callback;

        // Initialize model if not already done
        if (this.model == null) {
            this.model = new KahootModel(DEFAULT_QUESTIONS);
        }

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
        return "Answer all questions correctly to win! Wrong answers add 10 second penalty.";
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
                    callback.onComplete(false, finalTime, 0);
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
     * Shows detailed final results with strategy-based scoring calculation.
     */
    private void showDetailedFinalResults() {
        final boolean success = model.hasPassedQuiz();
        final int baseTime = model.getElapsedTimeSeconds();
        final int correctAnswers = model.getCorrectAnswers();
        final int wrongAnswers = model.getWrongAnswers();
        final int finalTime = model.getFinalTimeWithPenalty(PENALTY_SECONDS);
        final int totalQuestions = model.getTotalQuestions();

        // Calculate score using the strategy pattern
        final int calculatedScore = scoringStrategy.calculate(finalTime, ROOM_ID);

        gameFrame.getContentPane().removeAll();

        final JPanel resultsPanel = createDetailedResultsPanel(
            success, baseTime, finalTime, correctAnswers, wrongAnswers, totalQuestions, calculatedScore);

        gameFrame.add(resultsPanel);
        gameFrame.revalidate();
        gameFrame.repaint();

        new Thread(() -> {
            try {
                Thread.sleep(THREAD_SLEEP_DELAY);
                javax.swing.SwingUtilities.invokeLater(() -> addOkButtonToDetailedResults(
                    resultsPanel, finalTime, calculatedScore));
            } catch (final InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    /**
     * Creates a detailed results panel showing all statistics including calculated score.
     *
     * @param success whether the quiz was passed
     * @param baseTime the base time taken
     * @param finalTime the final time including penalties
     * @param correctAnswers the number of correct answers
     * @param wrongAnswers the number of wrong answers
     * @param totalQuestions the total number of questions
     * @param calculatedScore the score calculated by the scoring strategy
     * @return the detailed results panel
     */
    private JPanel createDetailedResultsPanel(final boolean success, final int baseTime,
                                            final int finalTime, final int correctAnswers,
                                            final int wrongAnswers, final int totalQuestions,
                                            final int calculatedScore) {
        final JPanel panel = new JPanel(new BorderLayout());

        final Color bgColor = success
            ? new Color(76, 175, 80)
            : new Color(244, 67, 54);

        panel.setBackground(bgColor);

        final JLabel titleLabel = new JLabel(
            success ? "QUIZ COMPLETED!" : "QUIZ FAILED",
            SwingConstants.CENTER);
        titleLabel.setFont(new Font(FONT_FAMILY, Font.BOLD, TITLE_FONT_SIZE));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(TWENTY_PIXELS, TEN_PIXELS, 
            TWENTY_PIXELS, TEN_PIXELS));

        final JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new javax.swing.BoxLayout(statsPanel, javax.swing.BoxLayout.Y_AXIS));
        statsPanel.setBackground(bgColor);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(TEN_PIXELS, THIRTY_PIXELS, 
            TWENTY_PIXELS, THIRTY_PIXELS));

        addStatsHeader(statsPanel, "QUIZ RESULTS");
        addStatLine(statsPanel, String.format("Correct Answers: %d/%d", correctAnswers, totalQuestions));
        addStatLine(statsPanel, String.format("Wrong Answers: %d", wrongAnswers));

        final double percentage = correctAnswers * 100.0 / totalQuestions;
        addStatLine(statsPanel, String.format("Accuracy: %.1f%%", percentage));

        addSpacer(statsPanel);

        addStatsHeader(statsPanel, "TIME ANALYSIS");
        addStatLine(statsPanel, String.format("Base Time: %d seconds", baseTime));

        if (wrongAnswers > 0) {
            final int totalPenalty = wrongAnswers * PENALTY_SECONDS;
            addStatLine(statsPanel, String.format("Penalty: +%d seconds", totalPenalty));
            addStatLine(statsPanel, String.format("(%d wrong %d sec)", wrongAnswers, PENALTY_SECONDS));
        } else {
            addStatLine(statsPanel, "No penalties! Perfect!");
        }

        addSpacer(statsPanel);

        final JLabel finalTimeLabel = new JLabel(
            String.format("FINAL TIME: %d seconds", finalTime),
            SwingConstants.CENTER);
        finalTimeLabel.setFont(new Font(FONT_FAMILY, Font.BOLD, FINAL_TIME_FONT_SIZE));
        finalTimeLabel.setForeground(Color.YELLOW);
        finalTimeLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        finalTimeLabel.setBorder(BorderFactory.createEmptyBorder(TEN_PIXELS, 0, TEN_PIXELS, 0));
        statsPanel.add(finalTimeLabel);

        addSpacer(statsPanel);

        addStatsHeader(statsPanel, "SCORING");
        addStatLine(statsPanel, String.format("Points Earned: %d", calculatedScore));

        // Show bonus information if applicable
        if (finalTime < BONUS_TIME_THRESHOLD_SECONDS) {
            addStatLine(statsPanel, String.format("Speed Bonus: +%d points!", BONUS_POINTS));
        }

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(statsPanel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Adds a statistics header to the panel.
     *
     * @param parent the parent panel
     * @param text the header text
     */
    private void addStatsHeader(final JPanel parent, final String text) {
        final JLabel header = new JLabel(text, SwingConstants.CENTER);
        header.setFont(new Font(FONT_FAMILY, Font.BOLD, HEADER_FONT_SIZE));
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
     */
    private void addStatLine(final JPanel parent, final String text) {
        final JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font(FONT_FAMILY, Font.PLAIN, STAT_FONT_SIZE));
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
        spacer.setFont(new Font(FONT_FAMILY, Font.PLAIN, SPACER_FONT_SIZE));
        spacer.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        parent.add(spacer);
    }

    /**
     * Adds an OK button to the detailed results panel.
     *
     * @param resultsPanel the results panel to add the button to
     * @param finalTime the final time to pass to callback
     * @param calculatedScore the score to pass to callback
     */
    private void addOkButtonToDetailedResults(final JPanel resultsPanel, 
                                             final int finalTime,
                                             final int calculatedScore) {
        final JButton okButton = new JButton("Continue");
        okButton.setFont(new Font(FONT_FAMILY, Font.BOLD, BUTTON_FONT_SIZE));
        okButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        okButton.setBackground(Color.WHITE);
        okButton.setForeground(Color.BLACK);
        okButton.setFocusPainted(false);
        okButton.setBorder(BorderFactory.createRaisedBevelBorder());

        okButton.addActionListener(e -> {
            gameFrame.dispose();
            if (callback != null) {
                // Pass the final time to maintain consistency with the callback interface
                callback.onComplete(true, finalTime, calculatedScore);
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

    /**
     * Gets the scoring strategy used by this minigame.
     * Useful for testing and debugging.
     *
     * @return the scoring strategy
     */
    public ScoringStrategy getScoringStrategy() {
        return scoringStrategy;
    }
}
