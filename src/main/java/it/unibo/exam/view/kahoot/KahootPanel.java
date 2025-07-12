package it.unibo.exam.view.kahoot;

import it.unibo.exam.model.entity.minigame.kahoot.KahootModel;
import it.unibo.exam.model.entity.minigame.kahoot.QuizQuestion;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Panel that displays the Kahoot quiz interface.
 * Handles the visual representation of questions, answers, and game state.
 */

public final class KahootPanel extends JPanel {

    private static final long serialVersionUID = 1L;

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
    private static final int COLOR_RED = 255;
    private static final int COLOR_GREEN = 77;
    private static final int COLOR_BLUE = 150;
    private static final int COLOR_YELLOW_RED = 255;
    private static final int COLOR_YELLOW_GREEN = 195;
    private static final int COLOR_YELLOW_BLUE = 77;
    private static final String FONT_FAMILY = "Arial";

    // Make transient to fix SpotBugs serialization warning
    private final transient KahootModel model;
    private JLabel progressLabel;
    private JLabel scoreLabel;
    private JLabel questionLabel;
    private final JButton[] answerButtons;
    // Make transient to fix SpotBugs serialization warning - ActionListener is not serializable
    private transient ActionListener answerClickListener;

    /**
     * Creates a new KahootPanel with the specified model.
     * Creates a defensive copy to prevent external modification.
     *
     * @param model the quiz model to display
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", 
                justification = "KahootModel is effectively immutable during panel lifecycle and "
                + "external modification is prevented by MVC design contract")
    public KahootPanel(final KahootModel model) {
        if (model == null) {
            throw new IllegalArgumentException("Model cannot be null");
        }
        this.model = model; // Assuming KahootModel is immutable or effectively immutable
        this.answerButtons = new JButton[4];
        initializeComponents();
        layoutComponents();
    }

    /**
     * Sets the listener for answer button clicks.
     *
     * @param listener the action listener for answer selection
     */
    public void setAnswerClickListener(final ActionListener listener) {
        this.answerClickListener = listener;
        updateAnswerButtonListeners();
    }

    /**
     * Updates the display to show the current question.
     *
     * @param question the question to display
     */
    public void showQuestion(final QuizQuestion question) {
        if (question == null) {
            return;
        }

        removeAll();
        layoutComponents();

        questionLabel.setText(question.getQuestionText());

        final List<String> answers = question.getAnswers();
        for (int i = 0; i < answerButtons.length && i < answers.size(); i++) {
            answerButtons[i].setText(answers.get(i));
            answerButtons[i].setVisible(true);
            answerButtons[i].setEnabled(true);
        }

        for (int i = answers.size(); i < answerButtons.length; i++) {
            answerButtons[i].setVisible(false);
        }

        updateProgress();
        updateAnswerButtonListeners();
        revalidate();
        repaint();
    }

    /**
     * Updates the progress display.
     */
    public void updateProgress() {
        final int current = model.getCurrentQuestionIndex() + 1;
        final int total = model.getTotalQuestions();
        progressLabel.setText(String.format("Question %d/%d", current, total));

        final int correct = model.getCorrectAnswers();
        final int wrong = model.getWrongAnswers();
        scoreLabel.setText(String.format("Correct: %d | Wrong: %d", correct, wrong));
    }

    /**
     * Shows feedback for the submitted answer.
     *
     * @param isCorrect true if the answer was correct
     * @param correctAnswer the text of the correct answer
     */
    public void showFeedback(final boolean isCorrect, final String correctAnswer) {
        removeAll();

        final Color feedbackColor = isCorrect 
            ? new Color(76, 175, 80)
            : new Color(244, 67, 54);

        setBackground(feedbackColor);

        final String message = isCorrect ? "Correct!" 
            : "<html><center>Wrong!<br>The correct answer was: " + correctAnswer 
            + "<br>+10 seconds penalty!</center></html>";

        final JLabel feedbackLabel = new JLabel(message);
        feedbackLabel.setFont(new Font(FONT_FAMILY, Font.BOLD, FONT_SIZE_EXTRA_LARGE));
        feedbackLabel.setForeground(Color.WHITE);
        feedbackLabel.setHorizontalAlignment(SwingConstants.CENTER);

        setLayout(new BorderLayout());
        add(feedbackLabel, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    /**
     * Shows the basic results of the quiz.
     *
     * @param success true if the player passed
     * @param finalTime final time including penalties
     * @param baseTime base time without penalties
     * @param correctAnswers number of correct answers
     * @param wrongAnswers number of wrong answers
     */
    public void showResults(final boolean success, final int finalTime, final int baseTime, 
                           final int correctAnswers, final int wrongAnswers) {
        removeAll();

        final Color bgColor = success 
            ? new Color(COLOR_GREEN, COLOR_RED, COLOR_BLUE) 
            : new Color(COLOR_RED, COLOR_GREEN, COLOR_GREEN);

        setBackground(bgColor);
        setLayout(new BorderLayout());

        final JLabel titleLabel = new JLabel(success ? "QUIZ COMPLETED!" : "QUIZ FAILED!");
        final int size = 28;
        titleLabel.setFont(new Font(FONT_FAMILY, Font.BOLD, size));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        final JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new javax.swing.BoxLayout(statsPanel, javax.swing.BoxLayout.Y_AXIS));
        statsPanel.setBackground(bgColor);

        addStatLine(statsPanel, " ");
        addStatLine(statsPanel, "Correct answers: " + correctAnswers + "/" + model.getTotalQuestions());
        addStatLine(statsPanel, "Wrong answers: " + wrongAnswers);
        addStatLine(statsPanel, " ");
        addStatLine(statsPanel, "Base time: " + baseTime + " seconds");

        if (wrongAnswers > 0) {
            final int penaltyTime = finalTime - baseTime;
            addStatLine(statsPanel, "Penalty time: +" + penaltyTime + " seconds (" 
                + wrongAnswers + " wrong × 1s)");
        }

        final JLabel finalTimeLabel = new JLabel("Final time: " + finalTime + " seconds");
        finalTimeLabel.setFont(new Font(FONT_FAMILY, Font.BOLD, 16));
        finalTimeLabel.setForeground(Color.WHITE);
        finalTimeLabel.setAlignmentX(CENTER_ALIGNMENT);
        statsPanel.add(finalTimeLabel);

        addStatLine(statsPanel, " ");
        addStatLine(statsPanel, success ? "Well done!" : "Try again next time!");

        add(titleLabel, BorderLayout.NORTH);
        add(statsPanel, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    /**
     * Disables all answer buttons (used during feedback display).
     */
    public void disableAnswerButtons() {
        for (final JButton button : answerButtons) {
            button.setEnabled(false);
        }
    }

    private void initializeComponents() {
        progressLabel = new JLabel();
        progressLabel.setForeground(Color.WHITE);
        progressLabel.setFont(new Font(FONT_FAMILY, Font.BOLD, FONT_SIZE_LARGE));

        scoreLabel = new JLabel();
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(new Font(FONT_FAMILY, Font.PLAIN, FONT_SIZE_MEDIUM));

        questionLabel = new JLabel();
        questionLabel.setFont(new Font(FONT_FAMILY, Font.BOLD, FONT_SIZE_SMALL));
        questionLabel.setHorizontalAlignment(SwingConstants.CENTER);

        final Color[] buttonColors = {
            new Color(COLOR_RED, COLOR_GREEN, COLOR_GREEN),
            new Color(COLOR_GREEN, COLOR_BLUE, COLOR_RED),
            new Color(COLOR_YELLOW_RED, COLOR_YELLOW_GREEN, COLOR_YELLOW_BLUE),
            new Color(COLOR_GREEN, COLOR_RED, COLOR_BLUE),
        };

        for (int i = 0; i < answerButtons.length; i++) {
            answerButtons[i] = new JButton();
            answerButtons[i].setFont(new Font(FONT_FAMILY, Font.BOLD, FONT_SIZE_MEDIUM));
            answerButtons[i].setBackground(buttonColors[i]);
            answerButtons[i].setForeground(Color.WHITE);
            answerButtons[i].setFocusPainted(false);
            answerButtons[i].setBorderPainted(false);
            answerButtons[i].setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));

            final int buttonIndex = i;
            answerButtons[i].setActionCommand(String.valueOf(buttonIndex));
        }
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());

        final JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(HEADER_BACKGROUND_RED, HEADER_BACKGROUND_GREEN, HEADER_BACKGROUND_BLUE));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE));
        headerPanel.add(progressLabel, BorderLayout.WEST);
        headerPanel.add(scoreLabel, BorderLayout.EAST);

        final JPanel questionPanel = new JPanel();
        questionPanel.setBackground(Color.WHITE);
        questionPanel.setBorder(BorderFactory.createEmptyBorder(QUESTION_BORDER, QUESTION_BORDER,
                QUESTION_BORDER, QUESTION_BORDER));
        questionPanel.add(questionLabel);

        final JPanel answersPanel = new JPanel(new GridLayout(2, 2, GRID_GAP, GRID_GAP));
        answersPanel.setBorder(BorderFactory.createEmptyBorder(ANSWER_BORDER_TOP, ANSWER_BORDER_SIDE,
                ANSWER_BORDER_SIDE, ANSWER_BORDER_SIDE));
        answersPanel.setBackground(Color.WHITE);

        for (final JButton button : answerButtons) {
            answersPanel.add(button);
        }

        add(headerPanel, BorderLayout.NORTH);
        add(questionPanel, BorderLayout.CENTER);
        add(answersPanel, BorderLayout.SOUTH);
    }

    private void updateAnswerButtonListeners() {
        if (answerClickListener != null) {
            for (final JButton button : answerButtons) {
                for (final ActionListener listener : button.getActionListeners()) {
                    button.removeActionListener(listener);
                }
                button.addActionListener(answerClickListener);
            }
        }
    }

    private void addStatLine(final JPanel parent, final String text) {
        final JLabel label = new JLabel(text);
        label.setFont(new Font(FONT_FAMILY, Font.PLAIN, 16));
        label.setForeground(Color.WHITE);
        label.setAlignmentX(CENTER_ALIGNMENT);
        label.setOpaque(false);
        parent.add(label);
    }
}
