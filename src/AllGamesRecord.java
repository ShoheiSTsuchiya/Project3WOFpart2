import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllGamesRecord {
    // A list to store all game records
    private final List<GameRecord> gameRecords;
    // A map to store lists of game records for each player
    private final Map<String, List<GameRecord>> recordsPlayer;

    public AllGamesRecord() {
        this.gameRecords = new ArrayList<>();
        this.recordsPlayer = new HashMap<>();
    }

    // Adds a GameRecord to the AllGamesRecord
    public void add(GameRecord record) {
        gameRecords.add(record);
        // Compute if absent is used to initialize the list if the player ID is not already in the map
        recordsPlayer.computeIfAbsent(record.getPlayerId(), k -> new ArrayList<>()).add(record);
    }

    // Returns the average score for all games added to the record
    public double average() {
        if (gameRecords.isEmpty()) {
            return 0.0;
        }

        int totalScore = 0;
        for (GameRecord record : gameRecords) {
            totalScore += record.getScore();
        }
        // The result is rounded to three decimal places
        return Math.round(((double) totalScore / gameRecords.size()) * 1000.0) / 1000.0;
    }

    // Returns the average score for all games of a particular player
    public double average(String playerId) {
        List<GameRecord> playerRecords = recordsPlayer.get(playerId);
        if (playerRecords == null || playerRecords.isEmpty()) {
            return 0.0;
        }

        int totalScore = 0;
        for (GameRecord record : playerRecords) {
            totalScore += record.getScore();
        }
        return (double) totalScore / playerRecords.size();
    }

    // Returns a sorted list of the top n scores, including player and score
    public List<GameRecord> highGameList(int n) {
        List<GameRecord> sortedRecords = new ArrayList<>(gameRecords);
        Collections.sort(sortedRecords);  // Sorting in descending order, using GameRecord's compareTo
        return sortedRecords.subList(0, Math.min(n, sortedRecords.size()));
    }

    // Returns a sorted list of the top n scores for the specified player
    public List<GameRecord> highGameList(String playerId, int n) {
        List<GameRecord> playerRecords = recordsPlayer.get(playerId);
        if (playerRecords == null || playerRecords.isEmpty()) {
            return new ArrayList<>();
        }

        List<GameRecord> sortedRecords = new ArrayList<>(playerRecords);
        Collections.sort(sortedRecords);  // Sorting in descending order, using GameRecord's compareTo
        return sortedRecords.subList(0, Math.min(n, sortedRecords.size()));
    }

    @Override
    public String toString() {
        String result = "AllGamesRecord{" +
                "gameRecords=" + gameRecords +
                ", recordsByPlayer=" + recordsPlayer +
                '}';
        return result;
    }
}
