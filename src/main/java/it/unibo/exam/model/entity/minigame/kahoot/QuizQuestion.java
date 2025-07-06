package it.unibo.exam.model.entity.minigame.kahoot;

import java.util.List;

/**
 * Represents a single quiz question with multiple choice answers.
 * Immutable class that contains the question text, answer options,
 * and the index of the correct answer.
 */
public final class QuizQuestion {

    private final String questionText;
    private final List<String> answers;
    private final int correctAnswer;

    /**
     * Creates a new quiz question.
     *
     * @param questionText the question text
     * @param answers list of possible answers
     * @param correctAnswer index of the correct answer (0-based)
     * @throws IllegalArgumentException if parameters are invalid
     */
    public QuizQuestion(final String questionText, final List<String> answers, final int correctAnswer) {
        if (questionText == null || questionText.trim().isEmpty()) {
            throw new IllegalArgumentException("Question text cannot be null or empty");
        }
        if (answers == null || answers.isEmpty()) {
            throw new IllegalArgumentException("Answers list cannot be null or empty");
        }
        if (correctAnswer < 0 || correctAnswer >= answers.size()) {
            throw new IllegalArgumentException("Correct answer index must be valid: " + correctAnswer);
        }

        this.questionText = questionText.trim();
        this.answers = List.copyOf(answers);
        this.correctAnswer = correctAnswer;
    }

    /**
     * Gets the question text.
     *
     * @return the question text
     */
    public String getQuestionText() {
        return questionText;
    }

    /**
     * Gets the list of possible answers.
     *
     * @return an immutable list of answers
     */
    public List<String> getAnswers() {
        return answers;
    }

    /**
     * Gets the index of the correct answer.
     *
     * @return the correct answer index (0-based)
     */
    public int getCorrectAnswer() {
        return correctAnswer;
    }

    /**
     * Checks if the given answer index is correct.
     *
     * @param answerIndex the answer index to check
     * @return true if the answer is correct, false otherwise
     */
    public boolean isCorrectAnswer(final int answerIndex) {
        return answerIndex == correctAnswer;
    }

    @Override
    public String toString() {
        return "QuizQuestion{" 
        + "questionText='" + questionText + '\'' 
                + ", answers=" + answers 
                + ", correctAnswer=" + correctAnswer
                + '}';
    }
}
