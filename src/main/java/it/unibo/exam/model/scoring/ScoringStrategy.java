package it.unibo.exam.model.scoring;

public interface ScoringStrategy {
    int calculate(int timeTaken, int roomId);
}
