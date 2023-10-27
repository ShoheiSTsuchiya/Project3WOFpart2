import java.util.ArrayList;
import java.util.List;


public class WheelOfFortuneAIGame extends WheelOfFortune {

    private List<WheelOfFortunePlayer> aiPlayers;
    private int currentPlayerIndex = 0;
    private static final int MAX_WRONG_GUESSES = 10;  // Max wrong guesses allowed

    public WheelOfFortuneAIGame() {
        super(new SequentialAIPlayer()); // Pass the default player to the superclass constructor
        this.aiPlayers = new ArrayList<>();
        this.aiPlayers.add(new SequentialAIPlayer());
    }

    public WheelOfFortuneAIGame(WheelOfFortunePlayer player) {
        super(player);
        this.aiPlayers = new ArrayList<>();
        this.aiPlayers.add(player);
    }

    public WheelOfFortuneAIGame(List<WheelOfFortunePlayer> players) {
        super(players.get(0)); // Pass the first player to the superclass constructor
        this.aiPlayers = new ArrayList<>(players);
    }

    @Override
    protected char getGuess(String previousGuesses) {
        WheelOfFortunePlayer currentPlayer = aiPlayers.get(currentPlayerIndex % aiPlayers.size());
        return currentPlayer.nextGuess();
    }

    @Override
    public GameRecord play() {
        int score = 0;
        StringBuilder previousGuesses = new StringBuilder();
        int wrongGuesses = 0;

        WheelOfFortunePlayer currentPlayer = aiPlayers.get(currentPlayerIndex % aiPlayers.size());
        currentPlayerIndex++;

        while (getHiddenPhrase().contains("*") && wrongGuesses < MAX_WRONG_GUESSES) {
            char guess = getGuess(previousGuesses.toString());
            previousGuesses.append(guess);

            int matchCount = processGuess(guess);
            if (matchCount > 0) {
                score += matchCount;
            } else {
                wrongGuesses++;
            }
        }

        if (wrongGuesses >= MAX_WRONG_GUESSES) {
            System.out.println("Game over! AI failed to guess the phrase.");
        } else {
            System.out.println("Congratulations AI! The phrase was: " + this.phrase);
        }

        return new GameRecord(currentPlayer.playerId(), score);
    }

    public AllGamesRecord playAll() {
        AllGamesRecord allGamesRecord = new AllGamesRecord();
        currentPlayerIndex = 0;

        while (!phrases.isEmpty()) {
            selectRandomPhrase();
            for (WheelOfFortunePlayer aiPlayer : aiPlayers) {
                GameRecord gameRecord = play();
                allGamesRecord.add(gameRecord);
                aiPlayer.reset();
            }

        }

        return allGamesRecord;
    }

    @Override
    protected boolean playNext() {
        return !phrases.isEmpty();
    }

    public static void main(String[] args) {
        // Assuming classes like SequentialAIPlayer, RandomAIPlayer, MostCommonAIPlayer exist
        WheelOfFortunePlayer player1 = new SequentialAIPlayer();
        WheelOfFortunePlayer player2 = new RandomAIPlayer();
        WheelOfFortunePlayer player3 = new MostCommonAIPlayer();

        List<WheelOfFortunePlayer> players = List.of(player1, player2, player3);
        WheelOfFortuneAIGame game = new WheelOfFortuneAIGame(players);
        AllGamesRecord allGamesRecord = game.playAll();

        System.out.println("All Results: " + allGamesRecord);
        System.out.println("Average Score: " + allGamesRecord.average());
        System.out.println("Top 2 Scores: " + allGamesRecord.highGameList(2));
        for(WheelOfFortunePlayer player : players) {
            System.out.println("Average Score for " + player.playerId() + ": " + String.format("%.3f", allGamesRecord.average(player.playerId())));
        }
    }
}
