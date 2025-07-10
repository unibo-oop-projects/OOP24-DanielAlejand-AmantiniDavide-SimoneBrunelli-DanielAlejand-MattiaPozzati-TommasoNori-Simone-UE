package it.unibo.exam.model.entity.minigame.kahoot;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Model for the Kahoot quiz that manages game state and business logic.
 * Implements the Observer pattern to notify state changes.
 */
public final class KahootModel {

    private static final double DEFAULT_PASS_THRESHOLD = 0.6;

    private final List<QuizQuestion> questions;
    private final List<KahootListener> listeners = new CopyOnWriteArrayList<>();
    private int currentQuestionIndex;
    private int correctAnswers;
    private int wrongAnswers;
    private long startTime;
    private long endTime;
    private boolean gameCompleted;
    private boolean timeFinalized;

    /**
     * Constructs a KahootModel with the specified questions.
     *
     * @param questions the list of quiz questions
     */
    public KahootModel(final List<QuizQuestion> questions) {
        if (questions == null || questions.isEmpty()) {
            throw new IllegalArgumentException("Questions list cannot be null or empty");
        }
        this.questions = List.copyOf(questions);
        this.currentQuestionIndex = 0;
        this.correctAnswers = 0;
        this.wrongAnswers = 0;
        this.gameCompleted = false;
    }

    /**
     * Starts the quiz by recording the start time.
     */
    public void startQuiz() {
        this.startTime = System.currentTimeMillis();
        notifyQuizStarted();
    }

    /**
     * Processes the player's answer for the current question.
     *
     * @param selectedAnswer the index of the selected answer
     * @return true if the answer is correct, false otherwise
     */
    public boolean submitAnswer(final int selectedAnswer) {
        if (gameCompleted || currentQuestionIndex >= questions.size()) {
            throw new IllegalStateException("Cannot submit answer: quiz is completed or no current question");
        }

        final QuizQuestion currentQuestion = questions.get(currentQuestionIndex);
        final boolean isCorrect = currentQuestion.isCorrectAnswer(selectedAnswer);

        if (isCorrect) {
            correctAnswers++;
        } else {
            wrongAnswers++;
        }

        notifyAnswerSubmitted(isCorrect, currentQuestion.getAnswers().get(currentQuestion.getCorrectAnswer()));

        currentQuestionIndex++;

        if (currentQuestionIndex >= questions.size()) {
            gameCompleted = true;
            finalizeTime();
            notifyQuizCompleted();
        }

        return isCorrect;
    }

    /**
     * Finalizes the quiz timing when completed.
     * This ensures consistent time values for scoring and display.
     */
    private void finalizeTime() {
        if (!timeFinalized) {
            this.endTime = System.currentTimeMillis();
            this.timeFinalized = true;
        }
    }

    /**
     * Gets the current question.
     *
     * @return the current question or null if quiz is completed
     */
    public QuizQuestion getCurrentQuestion() {
        if (currentQuestionIndex >= questions.size()) {
            return null;
        }
        return questions.get(currentQuestionIndex);
    }

    /**
     * Gets the current question index (0-based).
     *
     * @return the current question index
     */
    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    /**
     * Gets the total number of questions.
     *
     * @return the total number of questions
     */
    public int getTotalQuestions() {
        return questions.size();
    }

    /**
     * Gets the number of correct answers.
     *
     * @return the number of correct answers
     */
    public int getCorrectAnswers() {
        return correctAnswers;
    }

    /**
     * Gets the number of wrong answers.
     *
     * @return the number of wrong answers
     */
    public int getWrongAnswers() {
        return wrongAnswers;
    }

    /**
     * Calculates elapsed time since quiz start in seconds.
     * Uses finalized time if quiz is completed.
     *
     * @return elapsed time in seconds
     */
    public int getElapsedTimeSeconds() {
        if (startTime == 0) {
            return 0;
        }

        final long currentTime = timeFinalized ? endTime : System.currentTimeMillis();
        return (int) ((currentTime - startTime) / 1000);
    }

    /**
     * Calculates final time including penalties for wrong answers.
     *
     * @param penaltyPerWrongAnswer penalty in seconds for each wrong answer
     * @return final time with penalty
     */
    public int getFinalTimeWithPenalty(final int penaltyPerWrongAnswer) {
        return getElapsedTimeSeconds() + (wrongAnswers * penaltyPerWrongAnswer);
    }

    /**
     * Checks if the quiz has been completed.
     *
     * @return true if quiz is completed, false otherwise
     */
    public boolean isGameCompleted() {
        return gameCompleted;
    }

    /**
     * Calculates if player passed the quiz based on a threshold.
     *
     * @param passThreshold the pass threshold (e.g. 0.6 for 60%)
     * @return true if player passed the quiz
     */
    public boolean hasPassedQuiz(final double passThreshold) {
        return correctAnswers >= questions.size() * passThreshold;
    }

    /**
     * Calculates if player passed the quiz using default threshold (60%).
     *
     * @return true if player passed the quiz
     */
    public boolean hasPassedQuiz() {
        return hasPassedQuiz(DEFAULT_PASS_THRESHOLD);
    }

    /**
     * Adds a listener to receive state change notifications.
     *
     * @param listener the listener to add
     */
    public void addListener(final KahootListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes a previously added listener.
     *
     * @param listener the listener to remove
     */
    public void removeListener(final KahootListener listener) {
        listeners.remove(listener);
    }

    private void notifyQuizStarted() {
        for (final KahootListener listener : listeners) {
            listener.onQuizStarted();
        }
    }

    private void notifyAnswerSubmitted(final boolean isCorrect, final String correctAnswer) {
        for (final KahootListener listener : listeners) {
            listener.onAnswerSubmitted(isCorrect, correctAnswer);
        }
    }

    private void notifyQuizCompleted() {
        for (final KahootListener listener : listeners) {
            listener.onQuizCompleted(
                hasPassedQuiz(),
                getElapsedTimeSeconds(),
                correctAnswers,
                wrongAnswers
            );
        }
    }
}
