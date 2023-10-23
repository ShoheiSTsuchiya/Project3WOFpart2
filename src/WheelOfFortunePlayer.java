import java.util.HashSet;
import java.util.Random;
import java.util.Set;

interface WheelOfFortunePlayer {
    char nextGuess();
    String playerId();
    void reset();
}

class SequentialAIPlayer implements WheelOfFortunePlayer {
    private char currentGuess = 'a';  // Fixed initialization of currentGuess

    @Override
    public char nextGuess() {
        if (currentGuess > 'z') {  // Added check for exceeding 'z'
            System.err.println("All letters have been guessed");
            return 0;
        }
        return currentGuess++;  // Increment currentGuess after returning its value
    }

    @Override
    public String playerId() {
        return "Sequential AI Player";
    }

    @Override
    public void reset() {
        currentGuess = 'a';  // Reset currentGuess to 'a' when the player is reset
    }
}

class RandomAIPlayer implements WheelOfFortunePlayer {
    private final Random random = new Random();
    private final Set<Character> guessedChars = new HashSet<>();

    @Override
    public char nextGuess() {
        char guess;
        if (guessedChars.size() == 26) {  // Added check for all letters guessed
            System.err.println("All letters have been guessed");
            return 0;
        }
        do {
            guess = (char) (random.nextInt(26) + 'a');
        } while (guessedChars.contains(guess));
        guessedChars.add(guess);
        return guess;
    }

    @Override
    public String playerId() {
        return "Random AI Player";
    }

    @Override
    public void reset() {
        guessedChars.clear();  // Clear the guessed characters when the player is reset
    }
}


class MostCommonAIPlayer implements WheelOfFortunePlayer {
    private char[] commonLetters = {'e', 'a', 'r', 'i', 'o', 't', 'n', 's', 'l', 'c'};
    private int currentIdx = 0;
    private  Set<Character> guessedChars = new HashSet<>();
    private  Random random = new Random();
    @Override
    public char nextGuess() {
        char guess;

        if (currentIdx < commonLetters.length) {
            guess = commonLetters[currentIdx++];
            // If the character has already been guessed, skip to the next one
            while (guessedChars.contains(guess) && currentIdx < commonLetters.length) {
                guess = commonLetters[currentIdx++];
            }

            if (guessedChars.contains(guess)) {
                // If all common letters have been guessed, proceed to random guessing
                return getRandomGuess();
            }

            guessedChars.add(guess);
            return guess;
        }

        // Getting a random character if all common letters have been guessed
        return getRandomGuess();
    }

    // This method returns a random character that hasn't been guessed yet
    private char getRandomGuess() {
        char guess;
        do {
            guess = (char) (random.nextInt(26) + 'a');
        } while (guessedChars.contains(guess));

        guessedChars.add(guess);
        return guess;
    }

    @Override
    public String playerId() {
        return "Most Common AI Player";
    }

    @Override
    public void reset() {
        currentIdx = 0;
        guessedChars.clear();
    }
}


