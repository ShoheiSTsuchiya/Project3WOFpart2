import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class WheelOfFortuneUserGame extends WheelOfFortune {

    private Scanner scanner;
    private static final int MAX_WRONG_GUESSES = 5;
    private List<String> usedPhrases;

    public WheelOfFortuneUserGame(WheelOfFortunePlayer player) {
        super(player);
        this.scanner = new Scanner(System.in);
        this.usedPhrases = new ArrayList<>();
        if (phrases.isEmpty()) {
            System.out.println("Error: No phrases are available to play the game.");
            System.exit(1);
        }
        selectRandomPhrase();
    }

    @Override
    protected char getGuess(String previousGuesses) {
        char guess = ' ';
        boolean isValidGuess = false;

        while (!isValidGuess) {
            System.out.println("Current hidden phrase: " + getHiddenPhrase());
            System.out.println("Previous guesses: " + previousGuesses);
            System.out.print("Enter your guess: ");
            String input = scanner.next();

            if (input.length() == 1 && Character.isLetter(input.charAt(0))) {
                guess = Character.toUpperCase(input.charAt(0));

                if (previousGuesses.indexOf(guess) == -1) {
                    isValidGuess = true;
                } else {
                    System.out.println("You already guessed that letter. Please try a different one.");
                }
            } else {
                System.out.println("Invalid input. Please enter a single letter.");
            }
        }

        return guess;
    }

    @Override
    public GameRecord play() {
        player.reset();  // Reset player's state
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
                System.out.println("Wrong guess. You have " + (MAX_WRONG_GUESSES - wrongGuesses) + " attempts left.");
            }
        }

        if (wrongGuesses >= MAX_WRONG_GUESSES) {
            System.out.println("Game over! You've run out of attempts. The correct phrase was: " + phrase);
        } else {
            System.out.println("Congratulations! You've guessed the phrase: " + phrase);
        }

        return new GameRecord(player.playerId(), score);
    }

    @Override
    protected boolean playNext() {
        if (usedPhrases.size() == phrases.size()) { // End game if all phrases have been used
            System.out.println("All phrases have been used. Game Over!");
            return false;
        }
        boolean next = super.playNext(); // Call playNext from GuessingGame
        if (next == true) {
            player.reset();  // Reset player's state
            selectRandomPhrase();
        }
        return next;
    }

    public String getHiddenPhrase() {
        return hiddenPhrase.toString();
    }

    public static void main(String[] args) {
        WheelOfFortunePlayer player = new WheelOfFortunePlayer() {
            @Override
            public char nextGuess() {
                return 'a'; // This can be modified as needed
            }
            @Override
            public String playerId() {
                return "User1";
            }
            @Override
            public void reset(){}

        };

        WheelOfFortuneUserGame game = new WheelOfFortuneUserGame(player);
        AllGamesRecord allGamesRecord = game.playAll();

        System.out.println("Average score: " + allGamesRecord.average());
        System.out.println("Top 2 scores: " + allGamesRecord.highGameList(2));
    }
}