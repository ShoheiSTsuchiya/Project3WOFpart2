

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
        } catch (IOException var3) {
            var3.printStackTrace();
            this.phrases = List.of("EXAMPLE PHRASE");
        }

    }

    protected void selectRandomPhrase() {
        this.phrase = this.randomPhrase(this.phrases);
        this.hiddenPhrase = this.createHiddenPhrase(this.phrase);
    }

    protected abstract char getGuess(String var1);

    public GameRecord play() {
        int score = 0;
        StringBuilder previousGuesses = new StringBuilder();

        while(this.hiddenPhrase.toString().contains("*")) {
            char guess = this.getGuess(previousGuesses.toString());
            previousGuesses.append(guess);
            int matchCount = this.processGuess(guess);
            if (matchCount > 0) {
                score += matchCount;
            }
        }

        GameRecord gameRecord = new GameRecord(this.player.playerId(), score);
        return gameRecord;
    }

    private String randomPhrase(List<String> phraseList) {
        Random rand = new Random();
        int index = rand.nextInt(phraseList.size());
        String selectedPhrase = (String)phraseList.get(index);
        phraseList.remove(index);
        return selectedPhrase;
    }

    private StringBuilder createHiddenPhrase(String phrase) {
        StringBuilder hidden = new StringBuilder();
        char[] var3 = phrase.toCharArray();
        int var4 = var3.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            char c = var3[var5];
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

        for(int i = 0; i < this.phrase.length(); ++i) {
            if (Character.toLowerCase(guess) == Character.toLowerCase(this.phrase.charAt(i))) {
                this.hiddenPhrase.setCharAt(i, this.phrase.charAt(i));
                ++matchCount;
            }
        }

        return matchCount;
    }

    protected void setPlayer(WheelOfFortunePlayer newPlayer) {
        this.player = newPlayer;
    }

    public String getHiddenPhrase() {
        return this.hiddenPhrase.toString();
    }

    protected void setPhrase(String newPhrase) {
        this.phrase = newPhrase;
        this.hiddenPhrase = this.createHiddenPhrase(newPhrase);
    }
}
