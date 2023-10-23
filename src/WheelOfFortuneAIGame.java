import java.util.ArrayList;
import java.util.List;

public class WheelOfFortuneAIGame extends WheelOfFortune {

    private List<WheelOfFortunePlayer> aiPlayers;
    private int currentPlayerIndex = 0;
    private static  int MAX_WRONG_GUESSES = 10;  // Max wrong guesses allowed

    public WheelOfFortuneAIGame(List<WheelOfFortunePlayer> players) {
        super(players.get(0)); // Pass the first player to the superclass constructor
        this.aiPlayers = new ArrayList<>(players);
    }//
    @Override

    protected char getGuess(String previousGuesses) {
        // Get the index of the current player
        int index = currentPlayerIndex;

        // Retrieve the current player from the aiPlayers list
        WheelOfFortunePlayer currentPlayer = aiPlayers.get(index);

        // Ask the current player for their next guess
        char nextGuess = currentPlayer.nextGuess();

        // Return the next guess
        return nextGuess;
    }

    @Override
    public GameRecord play() {
        int score = 0;
        StringBuilder previousGuesses = new StringBuilder();
        int wrongGuesses = 0;

        while (hiddenPhrase.toString().contains("*") && wrongGuesses < MAX_WRONG_GUESSES) {
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
            System.out.println("Congratulations AI! The phrase was: " + phrase);
        }

        return new GameRecord(player.playerId(), score);
    }

    @Override
    protected boolean playNext() {
        if (currentPlayerIndex < aiPlayers.size() - 1) {
            currentPlayerIndex++;
            setPlayer(aiPlayers.get(currentPlayerIndex));
            resetGame();
            return true;
        }
        return false;
    }

    private void resetGame() {
        selectRandomPhrase();
        player.reset();
    }

    public static void main(String[] args) {
        // Using the actual AI players instead of dummy implementations
        WheelOfFortunePlayer player1 = new SequentialAIPlayer();
        WheelOfFortunePlayer player2 = new RandomAIPlayer();
        WheelOfFortunePlayer player3 = new MostCommonAIPlayer();

        List<WheelOfFortunePlayer> players = List.of(player1, player2, player3);
        WheelOfFortuneAIGame game = new WheelOfFortuneAIGame(players);
        AllGamesRecord allGamesRecord = game.playAll();

        System.out.println("Average score: " + allGamesRecord.average());
        System.out.println("Top 3 scores: " + allGamesRecord.highGameList(3));
        System.out.println("Average score for " + player1.playerId() + ": " + allGamesRecord.average(player1.playerId()));
    }
}
