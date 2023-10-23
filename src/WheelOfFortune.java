import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

public abstract class WheelOfFortune extends Game {
    protected String phrase;
    protected StringBuilder hiddenPhrase;
    protected List<String> phrases;
    protected WheelOfFortunePlayer player;

    public WheelOfFortune(WheelOfFortunePlayer player) {
        this.player = player;
        try {
            this.phrases = Files.readAllLines(Paths.get("phrases.txt"));
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception, for example, you might want to initialize phrases to an empty list
            // or provide some default phrases, or propagate the exception up the stack
            this.phrases = List.of("EXAMPLE PHRASE");
        }
        selectRandomPhrase();
    }

    protected void selectRandomPhrase() {
        phrase = randomPhrase(phrases);
        hiddenPhrase = createHiddenPhrase(phrase);
    }

    protected abstract char getGuess(String previousGuesses);

    @Override
    public GameRecord play() {
        //initialized score and previous guess values
        int score = 0;
        StringBuilder previousGuesses = new StringBuilder();

        while (hiddenPhrase.toString().contains("*")) {
            char guess = getGuess(previousGuesses.toString());
            previousGuesses.append(guess);

            int matchCount = processGuess(guess);
            if (matchCount > 0) {
                score += matchCount; // Adjust scoring mechanism as needed
            }
        }

        // Create a game record with the player's ID and final score
        GameRecord gameRecord = new GameRecord(player.playerId(), score);

        return gameRecord; // This line has been corrected
    }

    private String randomPhrase(List<String> phraseList) {
        Random rand = new Random();
        int index = rand.nextInt(phraseList.size());
        String selectedPhrase = phraseList.get(index);
        phraseList.remove(index); // Remove the selected phrase from the list
        return selectedPhrase;
    }

    private StringBuilder createHiddenPhrase(String phrase) {
        StringBuilder hidden = new StringBuilder();
        for (char c : phrase.toCharArray()) {
            if (c == ' ') {
                hidden.append(' ');
            } else {
                hidden.append('*');
            }
        }
        return hidden;
    }

    protected int processGuess(char guess) {
        int matchCount = 0;
        for (int i = 0; i < phrase.length(); i++) {
            if (Character.toLowerCase(guess) == Character.toLowerCase(phrase.charAt(i))) {
                hiddenPhrase.setCharAt(i, phrase.charAt(i));
                matchCount++;
            }
        }
        return matchCount;
    }

    protected void setPlayer(WheelOfFortunePlayer newPlayer) {
        this.player = newPlayer;
    }

    public String getHiddenPhrase() {
        return hiddenPhrase.toString();
    }

    protected void setPhrase(String newPhrase) {
        phrase = newPhrase;
        hiddenPhrase = createHiddenPhrase(newPhrase);
    }
}
