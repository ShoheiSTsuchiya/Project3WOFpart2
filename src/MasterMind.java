import java.util.Random;
import java.util.Scanner;
import java.util.List;

public class MasterMind extends GuessingGame {
    private static final int CODESIZE = 4;
    private static final String COLORS = "RGBYOP";
    private String secret;
    private Scanner scanner = new Scanner(System.in); //reuse scanner

    public MasterMind() {
        this.secret = generateSecret();
    }

    @Override
    protected GameRecord play() {
        this.secret = generateSecret();
        this.attempts = 0;
        this.score = 0;

        System.out.println("Welcome to MasterMind!");
        System.out.println("Please guess the 4 color code. Colors: R G B Y O P");
        int score = 0;

        while (!isGameOver(10)) {
            System.out.println("Attempt " + (attempts + 1) + "/10");
            System.out.print("Enter your guess: ");
            String guess = scanner.next().toUpperCase();

            if (guess.length() != CODESIZE|| !isValidGuess(guess)) {
                System.out.println("Invalid input. Enter exactly 4 colors.");
                continue;
            }

            incrementAttempts(); // use method from GuessingGame
            boolean[] exactsFound = new boolean[CODESIZE];
            int exacts = checkExacts(new StringBuilder(secret), new StringBuilder(guess), exactsFound);
            int partials = checkPartials(new StringBuilder(secret), new StringBuilder(guess), exactsFound);

            score += (2 * exacts) + partials;  // exacts: 2 points, partials: 1 point

            System.out.println(exacts + " exact, " + partials + " partial");

            if (exacts == CODESIZE) {
                System.out.println("Congratulations! You've guessed the secret code!");
                System.out.println("The secret code was: " + secret);
                break;
            }
        }

        if (attempts == 10) {
            System.out.println("Game Over! The secret code was: " + secret);
        }

        return new GameRecord("Player", score);
    }

    private String generateSecret() {
        Random rand = new Random();
        StringBuilder secretBuilder = new StringBuilder(CODESIZE);
        for (int i = 0; i < CODESIZE; i++) {
            secretBuilder.append(COLORS.charAt(rand.nextInt(COLORS.length())));
        }
        return secretBuilder.toString();
    }

    private int checkExacts(StringBuilder secretSB, StringBuilder guessSB, boolean[] exactsFound) {
        int exacts = 0;
        for (int i = 0; i < CODESIZE; i++) {
            if (secretSB.charAt(i) == guessSB.charAt(i)) {
                exacts++;
                exactsFound[i] = true;
                secretSB.setCharAt(i, '*');
                guessSB.setCharAt(i, '*');
            }
        }
        return exacts;
    }

    private int checkPartials(StringBuilder secretSB, StringBuilder guessSB, boolean[] exactsFound) {
        int partials = 0;
        for (int i = 0; i < CODESIZE; i++) {
            if (exactsFound[i]) continue;

            for (int j = 0; j < CODESIZE; j++) {
                if (!exactsFound[j] && secretSB.charAt(i) == guessSB.charAt(j)) {
                    partials++;
                    secretSB.setCharAt(i, '*');
                    guessSB.setCharAt(j, '*');
                    break;
                }
            }
        }
        return partials;
    }

    private boolean isValidGuess(String guess) {
        for (char c : guess.toCharArray()) {
            if (COLORS.indexOf(c) == -1) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        MasterMind game = new MasterMind();
        AllGamesRecord allGamesRecord = game.playAll();

        // output the result here
        System.out.println("Game Results:");
        System.out.println(allGamesRecord);


        double averageScore = allGamesRecord.average();
        System.out.println("Average Score: " + averageScore);

        List<GameRecord> topScores = allGamesRecord.highGameList(2);
        System.out.println("Top 2 Scores:");
        for (GameRecord record : topScores) {
            System.out.println(record);
        }
    }
}
