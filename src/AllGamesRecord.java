import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllGamesRecord {
    private final List<GameRecord> gameRecords;
    private final Map<String, List<GameRecord>> recordsPlayer;

    public AllGamesRecord() {
        this.gameRecords = new ArrayList<>();
        this.recordsPlayer = new HashMap<>();
    }

    public void add(GameRecord record) {
        gameRecords.add(record);
        recordsPlayer.computeIfAbsent(record.getPlayerId(), k -> new ArrayList<>()).add(record);
    }

    public double average() {
        if (gameRecords.isEmpty()) {
            return 0.0;
        }

        int totalScore = 0;
        for (GameRecord record : gameRecords) {
            totalScore += record.getScore();
        }
        return Math.round(((double) totalScore / gameRecords.size()) * 1000.0) / 1000.0;
    }

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

    public List<GameRecord> highGameList(int n) {
        List<GameRecord> sortedRecords = new ArrayList<>(gameRecords);
        Collections.sort(sortedRecords);
        return sortedRecords.subList(0, Math.min(n, sortedRecords.size()));
    }

    public List<GameRecord> highGameList(String playerId, int n) {
        List<GameRecord> playerRecords = recordsPlayer.get(playerId);
        if (playerRecords == null || playerRecords.isEmpty()) {
            return new ArrayList<>();
        }

        List<GameRecord> sortedRecords = new ArrayList<>(playerRecords);
        Collections.sort(sortedRecords);
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
