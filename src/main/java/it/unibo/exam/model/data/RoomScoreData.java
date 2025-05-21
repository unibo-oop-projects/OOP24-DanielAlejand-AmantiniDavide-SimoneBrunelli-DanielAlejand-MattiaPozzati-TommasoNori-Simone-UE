package it.unibo.exam.model.data;

/**
 * Data class for storing the player's score and completion state for a room.
 */
public class RoomScoreData {
    private final String roomName;
    private final int timeTaken;
    private final int pointsGained;
    private final boolean completed;

    /**
     * Constructs a RoomScoreData object with the given details.
     *
     * @param roomName     the name of the room
     * @param timeTaken    time taken to complete the minigame (seconds or ms)
     * @param pointsGained points gained for the room
     * @param completed    true if the room has been completed, false otherwise
     */
    public RoomScoreData(final String roomName, final int timeTaken, final int pointsGained, final boolean completed) {
        this.roomName = roomName;
        this.timeTaken = timeTaken;
        this.pointsGained = pointsGained;
        this.completed = completed;
    }

    /**
     * Gets the name of the room.
     *
     * @return the room's name
     */
    public String getRoomName() {
        return roomName;
    }

    /**
     * Gets the time taken to complete the room's minigame.
     *
     * @return the time taken (in seconds or milliseconds, depending on usage)
     */
    public int getTimeTaken() {
        return timeTaken;
    }

    /**
     * Gets the number of points gained for completing the room.
     *
     * @return the points gained
     */
    public int getPointsGained() {
        return pointsGained;
    }

    /**
     * Indicates whether the room has been completed.
     *
     * @return true if the room has been completed, false otherwise
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * Returns a string representation of this RoomScoreData.
     *
     * @return a string summarizing the room score data
     */
    @Override
    public String toString() {
        return "RoomScoreData{" 
            + "roomName='"    + roomName    + '\'' 
            + ", timeTaken="  + timeTaken  // in seconds or ms
            + ", pointsGained=" + pointsGained
            + ", completed=" + completed
            + '}';
    }

}
