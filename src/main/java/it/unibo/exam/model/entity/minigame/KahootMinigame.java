package it.unibo.exam.model.entity.minigame;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Kahoot-style quiz minigame with multiple choice questions.
 */
public final class KahootMinigame implements Minigame {

    private static final int WINDOW_WIDTH = 600;
    private static final int SIZE = 28;
    private static final int PSIZE = 28;
    private static final int WINDOW_HEIGHT = 400;
    private static final double PASS_THRESHOLD = 0.6;
    private static final int PENALTY_SECONDS = 1;
    private static final int HEADER_BACKGROUND_RED = 70;
    private static final int HEADER_BACKGROUND_GREEN = 130;
    private static final int HEADER_BACKGROUND_BLUE = 180;
    private static final int BORDER_SIZE = 20;
    private static final int FONT_SIZE_LARGE = 16;
    private static final int FONT_SIZE_MEDIUM = 14;
    private static final int FONT_SIZE_SMALL = 18;
    private static final int FONT_SIZE_EXTRA_LARGE = 24;
    private static final int QUESTION_BORDER = 30;
    private static final int GRID_GAP = 15;
    private static final int ANSWER_BORDER_TOP = 20;
    private static final int ANSWER_BORDER_SIDE = 50;
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 60;
    private static final int FEEDBACK_DELAY = 2000;
    private static final int COLOR_RED = 255;
    private static final int COLOR_GREEN = 77;
    private static final int COLOR_BLUE = 150;
    private static final int COLOR_YELLOW_RED = 255;
    private static final int COLOR_YELLOW_GREEN = 195;
    private static final int COLOR_YELLOW_BLUE = 77;
    private static final int MILLIS_TO_SECONDS = 1000;
    private static final String FONT_FAMILY = "Arial";

    private JFrame gameFrame;
    private MinigameCallback callback;
    private long startTime;
    private int currentQuestionIndex;
    private int correctAnswers;
    private int wrongAnswers;

    // List of quiz questions with answers and correct answer indices
    private final List<QuizQuestion> questions = List.of(
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

    /**
     * Starts the minigame with the given parent frame and completion callback.
     * This method initializes the game state and creates the game window.
     *
     * @param parentFrame the parent frame for centering the game window
     * @param onComplete callback to invoke when the game completes
     */
    @Override
    public void start(final JFrame parentFrame, final MinigameCallback onComplete) {
        this.callback = onComplete;
        this.startTime = System.currentTimeMillis();
        this.currentQuestionIndex = 0;
        this.correctAnswers = 0;
        this.wrongAnswers = 0;

        createGameWindow(parentFrame);
        showNextQuestion();
    }

    /**
     * Stops the minigame by disposing of the game window.
     * This method ensures proper cleanup when the game is terminated.
     */
    @Override
    public void stop() {
        if (gameFrame != null) {
            gameFrame.dispose();
        }
    }

    /**
     * Returns the display name of this minigame.
     *
     * @return the name of the minigame
     */
    @Override
    public String getName() {
        return "Quiz Kahoot";
    }

    /**
     * Returns a description of how to play this minigame.
     *
     * @return the game description
     */
    @Override
    public String getDescription() {
        return "Answer all questions correctly to win! Wrong answers add 1 second penalty.";
    }

    /**
     * Creates and configures the main game window.
     *
     * @param parentFrame the parent frame for centering
     */
    private void createGameWindow(final JFrame parentFrame) {
        gameFrame = new JFrame("Quiz Kahoot - " + getName());
        gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gameFrame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        gameFrame.setLocationRelativeTo(parentFrame);
        gameFrame.setResizable(false);

        // Add window close listener to handle incomplete games
        gameFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(final java.awt.event.WindowEvent e) {
                if (callback != null) {
                    callback.onComplete(false, getElapsedSecondsWithPenalty());
                }
            }
        });
    }

    /**
     * Displays the next question in the quiz sequence.
     * If all questions are answered, completes the quiz.
     */
    private void showNextQuestion() {
        if (currentQuestionIndex >= questions.size()) {
            // Quiz completed - show final results and check if player passed
            showFinalResults();
            return;
        }

        final QuizQuestion questionItem = questions.get(currentQuestionIndex);

        // Clear previous content
        gameFrame.getContentPane().removeAll();
        gameFrame.setLayout(new BorderLayout());

        // Create header panel with progress information
        final JPanel headerPanel = createHeaderPanel();

        // Create question display panel
        final JPanel questionPanel = createQuestionPanel(questionItem);

        // Create answer buttons panel
        final JPanel answersPanel = createAnswersPanel(questionItem);

        // Add all panels to the frame
        gameFrame.add(headerPanel, BorderLayout.NORTH);
        gameFrame.add(questionPanel, BorderLayout.CENTER);
        gameFrame.add(answersPanel, BorderLayout.SOUTH);

        gameFrame.revalidate();
        gameFrame.repaint();
        gameFrame.setVisible(true);
    }

    /**
     * Creates the header panel with progress and score information.
     *
     * @return configured header panel
     */
    private JPanel createHeaderPanel() {
        final JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(HEADER_BACKGROUND_RED, HEADER_BACKGROUND_GREEN, HEADER_BACKGROUND_BLUE));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE));

        final JLabel progressLabel = new JLabel(String.format("Question %d/%d",
                                        currentQuestionIndex + 1, questions.size()));
        progressLabel.setForeground(Color.WHITE);
        progressLabel.setFont(new Font(FONT_FAMILY, Font.BOLD, FONT_SIZE_LARGE));

        final JLabel scoreLabel = new JLabel(String.format("Correct: %d | Wrong: %d",
                                     correctAnswers, wrongAnswers));
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(new Font(FONT_FAMILY, Font.PLAIN, FONT_SIZE_MEDIUM));

        headerPanel.add(progressLabel, BorderLayout.WEST);
        headerPanel.add(scoreLabel, BorderLayout.EAST);

        return headerPanel;
    }

    /**
     * Creates the question display panel.
     *
     * @param questionItem the question to display
     * @return configured question panel
     */
    private JPanel createQuestionPanel(final QuizQuestion questionItem) {
        final JPanel questionPanel = new JPanel();
        questionPanel.setBackground(Color.WHITE);
        questionPanel.setBorder(BorderFactory.createEmptyBorder(QUESTION_BORDER, QUESTION_BORDER,
                QUESTION_BORDER, QUESTION_BORDER));

        final JLabel questionLabel = new JLabel(questionItem.getQuestionText());
        questionLabel.setFont(new Font(FONT_FAMILY, Font.BOLD, FONT_SIZE_SMALL));
        questionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        questionPanel.add(questionLabel);

        return questionPanel;
    }

    /**
     * Creates the panel with answer buttons.
     *
     * @param questionItem the question with answers
     * @return configured answers panel
     */
    private JPanel createAnswersPanel(final QuizQuestion questionItem) {
        final JPanel answersPanel = new JPanel(new GridLayout(2, 2, GRID_GAP, GRID_GAP));
        answersPanel.setBorder(BorderFactory.createEmptyBorder(ANSWER_BORDER_TOP, ANSWER_BORDER_SIDE,
                ANSWER_BORDER_SIDE, ANSWER_BORDER_SIDE));
        answersPanel.setBackground(Color.WHITE);

        final List<String> answers = questionItem.getAnswers();
        final Color[] buttonColors = {
            new Color(COLOR_RED, COLOR_GREEN, COLOR_GREEN),   // Red
            new Color(COLOR_GREEN, COLOR_BLUE, COLOR_RED),  // Blue
            new Color(COLOR_YELLOW_RED, COLOR_YELLOW_GREEN, COLOR_YELLOW_BLUE),  // Yellow
            new Color(COLOR_GREEN, COLOR_RED, COLOR_BLUE),   // Green
        };

        for (int i = 0; i < answers.size(); i++) {
            final int answerIndex = i;
            final JButton answerButton = new JButton(answers.get(i));
            answerButton.setFont(new Font(FONT_FAMILY, Font.BOLD, FONT_SIZE_MEDIUM));
            answerButton.setBackground(buttonColors[i]);
            answerButton.setForeground(Color.WHITE);
            answerButton.setFocusPainted(false);
            answerButton.setBorderPainted(false);
            answerButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));

            answerButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    handleAnswer(answerIndex, questionItem.getCorrectAnswer());
                }
            });

            answersPanel.add(answerButton);
        }

        return answersPanel;
    }

    /**
     * Handles the player's answer selection.
     *
     * @param selectedAnswer the index of the selected answer
     * @param correctAnswer the index of the correct answer
     */
    private void handleAnswer(final int selectedAnswer, final int correctAnswer) {
        if (selectedAnswer == correctAnswer) {
            correctAnswers++;
            showFeedback("Correct!", new Color(COLOR_GREEN, COLOR_RED, COLOR_BLUE));
        } else {
            wrongAnswers++; // Incrementa contatore errori
            showFeedback("<html><center>Wrong!<br>The correct answer was: " + 
                questions.get(currentQuestionIndex).getAnswers().get(correctAnswer) + 
                "<br>+1 second penalty!</center></html>",
                new Color(COLOR_RED, COLOR_GREEN, COLOR_GREEN));
        }

        currentQuestionIndex++;

        // Wait a moment before showing the next question
        final Timer timer = new Timer(FEEDBACK_DELAY, e -> showNextQuestion());
        timer.setRepeats(false);
        timer.start();
    }

    /**
     * Shows feedback to the player after answering.
     *
     * @param message the feedback message
     * @param color the background color for the feedback
     */
    private void showFeedback(final String message, final Color color) {
        gameFrame.getContentPane().removeAll();

        final JPanel feedbackPanel = new JPanel(new BorderLayout());
        feedbackPanel.setBackground(color);

        // Gestione multi-line per messaggi con \n
        if (message.contains("\n")) {
            final JPanel textPanel = new JPanel();
            textPanel.setLayout(new javax.swing.BoxLayout(textPanel, javax.swing.BoxLayout.Y_AXIS));
            textPanel.setBackground(color);

            final String[] lines = message.split("\n");
            for (final String line : lines) {
                final JLabel lineLabel = new JLabel(line);
                lineLabel.setFont(new Font(FONT_FAMILY, Font.BOLD, FONT_SIZE_EXTRA_LARGE));
                lineLabel.setForeground(Color.WHITE);
                lineLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
                textPanel.add(lineLabel);
            }

            feedbackPanel.add(textPanel, BorderLayout.CENTER);
        } else {
            final JLabel feedbackLabel = new JLabel(message);
            feedbackLabel.setFont(new Font(FONT_FAMILY, Font.BOLD, FONT_SIZE_EXTRA_LARGE));
            feedbackLabel.setForeground(Color.WHITE);
            feedbackLabel.setHorizontalAlignment(SwingConstants.CENTER);
            feedbackPanel.add(feedbackLabel, BorderLayout.CENTER);
        }

        gameFrame.add(feedbackPanel);
        gameFrame.revalidate();
        gameFrame.repaint();
    }

    /**
     * Mostra i risultati finali del quiz con statistiche complete.
     */
    private void showFinalResults() {
        gameFrame.getContentPane().removeAll();

        final boolean success = correctAnswers >= questions.size() * PASS_THRESHOLD;
        final int baseTime = getElapsedSeconds();
        final int penaltyTime = wrongAnswers * PENALTY_SECONDS;
        final int finalTime = baseTime + penaltyTime;

        final Color bgColor = success ? new Color(COLOR_GREEN, COLOR_RED, COLOR_BLUE) 
                                     : new Color(COLOR_RED, COLOR_GREEN, COLOR_GREEN);

        final JPanel resultsPanel = new JPanel(new BorderLayout());
        resultsPanel.setBackground(bgColor);
        resultsPanel.setBorder(BorderFactory.createEmptyBorder(
            QUESTION_BORDER, QUESTION_BORDER, QUESTION_BORDER, QUESTION_BORDER));

        final JLabel titleLabel = new JLabel(success ? "QUIZ COMPLETED!" : "QUIZ FAILED!");
        titleLabel.setFont(new Font(FONT_FAMILY, Font.BOLD, SIZE));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        final JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new javax.swing.BoxLayout(statsPanel, javax.swing.BoxLayout.Y_AXIS));
        statsPanel.setBackground(bgColor);

        addStatLine(statsPanel, " ", bgColor);
        addStatLine(statsPanel, "Correct answers: " + correctAnswers + "/" + questions.size(), bgColor);
        addStatLine(statsPanel, "Wrong answers: " + wrongAnswers, bgColor);
        addStatLine(statsPanel, " ", bgColor);
        addStatLine(statsPanel, "Base time: " + baseTime + " seconds", bgColor);

        if (wrongAnswers > 0) {
            addStatLine(statsPanel, "Penalty time: +" + penaltyTime + " seconds (" 
            + wrongAnswers + " wrong × " + PENALTY_SECONDS + "s)", bgColor);
        }

        final JLabel finalTimeLabel = new JLabel("Final time: " + finalTime + " seconds");
        finalTimeLabel.setFont(new Font(FONT_FAMILY, Font.BOLD, 16));
        finalTimeLabel.setForeground(Color.WHITE);
        finalTimeLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        statsPanel.add(finalTimeLabel);

        addStatLine(statsPanel, " ", bgColor);
        addStatLine(statsPanel, success ? "Well done!" : "Try again next time!", bgColor);

        final JButton okButton = new JButton("OK");
        okButton.setFont(new Font(FONT_FAMILY, Font.BOLD, FONT_SIZE_SMALL));
        okButton.setPreferredSize(new Dimension(100, PSIZE));
        okButton.setBackground(Color.WHITE);
        okButton.setForeground(Color.BLACK);
        okButton.setFocusPainted(false);

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                gameFrame.dispose();
                if (callback != null) {
                    callback.onComplete(true, finalTime);
                }
            }
        });

        final JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(bgColor);
        buttonPanel.add(okButton);

        resultsPanel.add(titleLabel, BorderLayout.NORTH);
        resultsPanel.add(statsPanel, BorderLayout.CENTER);
        resultsPanel.add(buttonPanel, BorderLayout.SOUTH);

        gameFrame.add(resultsPanel);
        gameFrame.revalidate();
        gameFrame.repaint();
    }

    private void addStatLine(final JPanel parent, final String text, final Color bgColor) {
        final JLabel label = new JLabel(text);
        label.setFont(new Font(FONT_FAMILY, Font.PLAIN, 16));
        label.setForeground(bgColor);
        label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        label.setOpaque(false);
        parent.add(label);
    }

    /**
     * Calculates the elapsed time since the minigame started (without penalty).
     *
     * @return elapsed time in seconds
     */
    private int getElapsedSeconds() {
        return (int) ((System.currentTimeMillis() - startTime) / MILLIS_TO_SECONDS);
    }

    /**
     * Calculates the elapsed time including penalty for wrong answers.
     *
     * @return elapsed time in seconds with penalty applied
     */
    private int getElapsedSecondsWithPenalty() {
        return getElapsedSeconds() + (wrongAnswers * PENALTY_SECONDS);
    }

    /**
     * Inner class representing a quiz question with multiple choice answers.
     */
    private static final class QuizQuestion {
        private final String questionText;
        private final List<String> answers;
        private final int correctAnswer;

        /**
         * Creates a new question.
         *
         * @param questionText the question text
         * @param answers list of possible answers
         * @param correctAnswer index of the correct answer (0-based)
         */
        QuizQuestion(final String questionText, final List<String> answers, final int correctAnswer) {
            this.questionText = questionText;
            this.answers = List.copyOf(answers); // Immutable copy
            this.correctAnswer = correctAnswer;
        }

        String getQuestionText() {
            return questionText;
        }

        List<String> getAnswers() {
            return answers;
        }

        int getCorrectAnswer() {
            return correctAnswer;
        }
    }
}
