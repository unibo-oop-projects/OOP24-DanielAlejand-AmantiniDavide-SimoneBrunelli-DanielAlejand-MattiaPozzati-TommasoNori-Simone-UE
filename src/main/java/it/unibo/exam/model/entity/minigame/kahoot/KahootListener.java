package it.unibo.exam.model.entity.minigame.kahoot;

/**
 * Listener interface for Kahoot quiz events.
 * Implementations receive notifications about quiz state changes.
 */
public interface KahootListener {

    /**
     * Called when the quiz starts.
     */
    void onQuizStarted();

    /**
     * Called when an answer is submitted.
     *
     * @param isCorrect true if the answer was correct
     * @param correctAnswer the text of the correct answer
     */
    void onAnswerSubmitted(boolean isCorrect, String correctAnswer);

    /**
     * Called when moving to the next question.
     *
     * @param question the next question to display
     */
    void onNextQuestion(QuizQuestion question);

    /**
     * Called when the quiz is completed.
     *
     * @param success true if the player passed the quiz
     * @param elapsedTime time taken in seconds
     * @param correctAnswers number of correct answers
     * @param wrongAnswers number of wrong answers
     */
    void onQuizCompleted(boolean success, int elapsedTime, int correctAnswers, int wrongAnswers);
}
