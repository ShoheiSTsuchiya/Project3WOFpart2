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
        int thisScore = this.score;
        int otherScore = other.score;

        // Compare the scores
        if (thisScore > otherScore) {
            return 1;  // Return 1 if this score is greater than the other score
        } else if (thisScore < otherScore) {
            return -1; // Return -1 if this score is less than the other score
        } else {
            return 0;  // Return 0 if the scores are equal
        }
    }

    @Override
    public String toString() {
        return String.format("Player ID: %s, Score: %d", playerId, score);
    }
}
