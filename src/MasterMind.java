import java.util.Random;
import java.util.Scanner;

public class MasterMind extends Game {
    private static final int CODESIZE = 4;
    private static final String COLORS = "RGBYOP";
    private String secret;
    private int attempts;

    public MasterMind() {
        this.secret = generateSecret();
        this.attempts = 0;
    }

    @Override
    protected GameRecord play() {
        System.out.println("Welcome to MasterMind!");
        System.out.println("Try to guess the 4 color code. Colors: R G B Y O P");
        Scanner scanner = new Scanner(System.in);

        while (attempts < 10) {
            System.out.println("Attempt " + (attempts + 1) + "/10");
            System.out.print("Enter your guess: ");
            String guess = scanner.next().toUpperCase();

            if (guess.length() != CODESIZE) {
                System.out.println("Invalid input! Enter exactly 4 colors.");
                continue;
            }

            attempts++;
            int exacts = checkExacts(new StringBuilder(secret), new StringBuilder(guess));
            int partials = checkPartials(new StringBuilder(secret), new StringBuilder(guess));

            System.out.println(exacts + " exact, " + partials + " partial");

            if (exacts == CODESIZE) {
                System.out.println("Congratulations! You've guessed the secret code!");
                return new GameRecord("Player", 10 - attempts + 1);
            }
        }

        System.out.println("Game Over! The secret code was: " + secret);
        return new GameRecord("Player", 0);
    }

    @Override
    protected boolean playNext() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Do you want to play again? (yes/no): ");
        String response = scanner.next().toLowerCase();
        if ("yes".equals(response)) {
            this.secret = generateSecret();
            this.attempts = 0;
            return true;
        }
        return false;
    }

    private String generateSecret() {
        Random rand = new Random();
        StringBuilder secretBuilder = new StringBuilder(CODESIZE);
        for (int i = 0; i < CODESIZE; i++) {
            secretBuilder.append(COLORS.charAt(rand.nextInt(COLORS.length())));
        }
        return secretBuilder.toString();
    }

    private int checkExacts(StringBuilder secretSB, StringBuilder guessSB) {
        int exacts = 0;
        for (int i = 0; i < CODESIZE; i++) {
            if (secretSB.charAt(i) == guessSB.charAt(i)) {
                exacts++;
                secretSB.setCharAt(i, '-');
                guessSB.setCharAt(i, '*');
            }
        }
        return exacts;
    }
//need to fix that do not include "exact" one.
    private int checkPartials(StringBuilder secretSB, StringBuilder guessSB) {
        int partials = 0;
        for (int i = 0; i < CODESIZE; i++) {
            for (int j = 0; j < CODESIZE; j++) {
                if (secretSB.charAt(i) == guessSB.charAt(j)) {
                    partials++;
                    secretSB.setCharAt(i, '-');
                    guessSB.setCharAt(j, '*');
                }
            }
        }
        return partials;
    }

    public static void main(String[] args) {
        MasterMind game = new MasterMind();
        AllGamesRecord allGamesRecord = game.playAll();

        // output the result here
        System.out.println("Game Results:");
        System.out.println(allGamesRecord);
        System.out.println("Average Score: " + allGamesRecord.average());
        System.out.println("Top 2 Scores: " + allGamesRecord.highGameList(2));


    }
}
