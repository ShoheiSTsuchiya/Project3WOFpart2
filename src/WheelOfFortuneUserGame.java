import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class WheelOfFortuneUserGame extends WheelOfFortune {

    private Scanner scanner;
    private static int MAX_WRONG_GUESSES = 5;

    public WheelOfFortuneUserGame(WheelOfFortunePlayer player) {
        super(player);
        this.scanner = new Scanner(System.in);
        selectRandomPhrase();
    }

    protected void selectRandomPhrase() {
        List<String> phraseList;
        try {
            phraseList = Files.readAllLines(Paths.get("phrases.txt"));
            String randomPhrase = getRandomPhrase(phraseList);
            setPhrase(randomPhrase);
        } catch (IOException e) {
            System.out.println("Error reading the phrases.txt file.");
            e.printStackTrace();
        }
    }

    private String getRandomPhrase(List<String> phrases) {
        if (phrases == null || phrases.isEmpty()) {
            return "Phrase.txt is empty. ";
        }
        Random random = new Random();
        int index = random.nextInt(phrases.size());
        return phrases.get(index);
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
        player.reset();
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
                System.out.println("Wrong guess! You have " + (MAX_WRONG_GUESSES - wrongGuesses) + " attempts left.");
            }
        }

        if (wrongGuesses >= MAX_WRONG_GUESSES) {
            System.out.println("Game over! You've run out of attempts.");
        } else {
            System.out.println("Congratulations! You've guessed the phrase: " + phrase);
        }

        return new GameRecord(player.playerId(), score);
    }

    @Override
    protected boolean playNext() {
        System.out.print("Do you want to play again? (yes/no): ");
        String response = scanner.next().toLowerCase();
        if (response.equals("yes")) {
            selectRandomPhrase();
            return true;
        } else {
            return false;
        }
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
            public void reset() {
                // Implement reset logic if necessary
            }
        };

        WheelOfFortuneUserGame game = new WheelOfFortuneUserGame(player);
        AllGamesRecord allGamesRecord = game.playAll();

        System.out.println("Average score: " + allGamesRecord.average());
        System.out.println("Top 3 scores: " + allGamesRecord.highGameList(3));
    }
}
