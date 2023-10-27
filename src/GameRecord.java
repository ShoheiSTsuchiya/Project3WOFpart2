public class GameRecord implements Comparable<GameRecord> {
    private final String playerId;
    private final int score;

    public GameRecord(String playerId, int score) {
        this.playerId = playerId;
        this.score = score;
    }

    public String getPlayerId() {
        return playerId;
    }

    public int getScore() {

        return score;
    }

    @Override
    public int compareTo(GameRecord other) {
        return Integer.compare(other.score, this.score);  // Sort in descending order
    }

    @Override
    public String toString() {
        return String.format("Player ID: %s, Score: %d", playerId, score);
    }
}
