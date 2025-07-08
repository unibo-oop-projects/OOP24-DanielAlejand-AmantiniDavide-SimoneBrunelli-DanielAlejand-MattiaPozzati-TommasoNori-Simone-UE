package it.unibo.exam.model;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Manages the game leaderboard with persistent storage in a text file.
 * Maintains a top 10 ranking based on scores.
 */
public final class LeaderboardManage {

    private static final Logger LOGGER = Logger.getLogger(LeaderboardManage.class.getName());
    private static final String LEADERBOARD_FILE = "leaderboard.txt";
    private static final int MAX_ENTRIES = 10;
    private static final String SEPARATOR = "|";

    private List<LeaderboardEntry> entries;

    /**
     * Creates a new LeaderboardManager and loads existing data.
     */
    public LeaderboardManage() {
        this.entries = new ArrayList<>();
        loadLeaderboard();
    }

    /**
     * Adds a new score to the leaderboard if it qualifies for top 10.
     * 
     * @param playerName the name of the player
     * @param score the total score achieved
     * @param totalTime the total time taken in seconds
     * @return true if the score was added to the leaderboard, false otherwise
     */

    public boolean addScore(final String playerName, final int score, final int totalTime) {
        if (playerName == null || playerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Player name cannot be null or empty");
        }

        final LocalDateTime date = LocalDateTime.now();
        final LeaderboardEntry newEntry = new LeaderboardEntry(
            playerName.trim(), score, totalTime, date);

        // Add the new entry
        entries.add(newEntry);

        // Sort by score (descending), then by time (ascending) for ties
        entries.sort((a, b) -> {
            final int scoreComparison = Integer.compare(b.getScore(), a.getScore());
            if (scoreComparison == 0) {
                return Integer.compare(a.getTotalTime(), b.getTotalTime());
            }
            return scoreComparison;
        });

        // Keep only top 10
        final boolean wasAdded = entries.indexOf(newEntry) < MAX_ENTRIES;
        if (entries.size() > MAX_ENTRIES) {
            entries = entries.subList(0, MAX_ENTRIES);
        }

        // Save to file
        saveLeaderboard();

        LOGGER.info("Score added for " + playerName + ": " + score + " points, " + totalTime + "s");
        return wasAdded;
    }

    /**
     * Gets the top 10 leaderboard entries.
     * 
     * @return immutable list of leaderboard entries
     */
    public List<LeaderboardEntry> getTop10() {
        return Collections.unmodifiableList(new ArrayList<>(entries));
    }

    /**
     * Gets the rank of a specific score (1-based).
     * 
     * @param score the score to check
     * @param totalTime the total time (used for tie-breaking)
     * @return the rank (1-10) or -1 if not in top 10
     */
    public int getRank(final int score, final int totalTime) {
        final LeaderboardEntry tempEntry = new LeaderboardEntry("temp", score, totalTime, LocalDateTime.now());

        final List<LeaderboardEntry> tempList = new ArrayList<>(entries);
        tempList.add(tempEntry);

        tempList.sort((a, b) -> {
            final int scoreComparison = Integer.compare(b.getScore(), a.getScore());
            if (scoreComparison == 0) {
                return Integer.compare(a.getTotalTime(), b.getTotalTime());
            }
            return scoreComparison;
        });

        final int rank = tempList.indexOf(tempEntry) + 1;
        return rank <= MAX_ENTRIES ? rank : -1;
    }

    /**
     * Checks if a score would qualify for the top 10.
     * 
     * @param score the score to check
     * @param totalTime the total time
     * @return true if the score would make it to top 10
     */
    public boolean wouldQualify(final int score, final int totalTime) {
        if (entries.size() < MAX_ENTRIES) {
            return true;
        }

        final LeaderboardEntry lastEntry = entries.get(entries.size() - 1);
        return score > lastEntry.getScore() 
            || (score == lastEntry.getScore() && totalTime < lastEntry.getTotalTime());
    }

    /**
     * Gets the current number of entries in the leaderboard.
     * 
     * @return number of entries (0-10)
     */
    public int getSize() {
        return entries.size();
    }

    /**
     * Checks if the leaderboard is empty.
     * 
     * @return true if no entries exist
     */
    public boolean isEmpty() {
        return entries.isEmpty();
    }

    /**
     * Clears all leaderboard entries (for testing or reset purposes).
     */
    public void clear() {
        entries.clear();
        saveLeaderboard();
        LOGGER.info("Leaderboard cleared");
    }

    /**
     * Loads the leaderboard from the text file.
     */
    private void loadLeaderboard() {
        final File file = new File(LEADERBOARD_FILE);
        if (!file.exists()) {
            LOGGER.info("Leaderboard file does not exist, starting with empty leaderboard");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                final LeaderboardEntry entry = parseEntry(line);
                if (entry != null) {
                    entries.add(entry);
                }
            }

            // Sort entries after loading
            entries.sort((a, b) -> {
                final int scoreComparison = Integer.compare(b.getScore(), a.getScore());
                if (scoreComparison == 0) {
                    return Integer.compare(a.getTotalTime(), b.getTotalTime());
                }
                return scoreComparison;
            });

            LOGGER.info("Loaded " + entries.size() + " leaderboard entries");

        } catch (final IOException e) {
            LOGGER.log(Level.SEVERE, "Error loading leaderboard", e);
        }
    }

    /**
     * Saves the leaderboard to the text file.
     */
    private void saveLeaderboard() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LEADERBOARD_FILE))) {
            for (final LeaderboardEntry entry : entries) {
                writer.println(formatEntry(entry));
            }
            LOGGER.info("Leaderboard saved with " + entries.size() + " entries");

        } catch (final IOException e) {
            LOGGER.log(Level.SEVERE, "Error saving leaderboard", e);
        }
    }

    /**
     * Parses a line from the file into a LeaderboardEntry.
     * 
     * @param line the line to parse
     * @return the parsed entry or null if invalid
     */
    private LeaderboardEntry parseEntry(final String line) {
        try {
            final String[] parts = line.split("\\" + SEPARATOR);
            if (parts.length != 4) {
                LOGGER.warning("Invalid leaderboard entry format: " + line);
                return null;
            }

            final String name = parts[0];
            final int score = Integer.parseInt(parts[1]);
            final int time = Integer.parseInt(parts[2]);
            final LocalDateTime date = LocalDateTime.parse(parts[3], DateTimeFormatter.ISO_LOCAL_DATE_TIME);

            return new LeaderboardEntry(name, score, time, date);

        } catch (final Exception e) {
            LOGGER.log(Level.WARNING, "Error parsing leaderboard entry: " + line, e);
            return null;
        }
    }

    /**
     * Formats a LeaderboardEntry for file storage.
     * 
     * @param entry the entry to format
     * @return the formatted string
     */
    private String formatEntry(final LeaderboardEntry entry) {
        return entry.getPlayerName() + SEPARATOR 
            + entry.getScore() + SEPARATOR 
            + entry.getTotalTime() + SEPARATOR 
            + entry.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
