import java.util.Random;
import java.util.Scanner;
import java.util.List;

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
        int score = 0;

        while (attempts < 10) {
            System.out.println("Attempt " + (attempts + 1) + "/10");
            System.out.print("Enter your guess: ");
            String guess = scanner.next().toUpperCase();

            if (guess.length() != CODESIZE) {
                System.out.println("Invalid input! Enter exactly 4 colors.");
                continue;
            }

            attempts++;
            boolean[] exactsFound = new boolean[CODESIZE];
            int exacts = checkExacts(new StringBuilder(secret), new StringBuilder(guess), exactsFound);
            int partials = checkPartials(new StringBuilder(secret), new StringBuilder(guess), exactsFound);

            score += (2 * exacts) + partials;  // Exactが1つ当たるごとに2点、Partialが1つ当たるごとに1点を加算

            System.out.println(exacts + " exact, " + partials + " partial");

            if (exacts == CODESIZE) {
                System.out.println("Congratulations! You've guessed the secret code!");
                return new GameRecord("Player", score);
            }
        }

        System.out.println("Game Over! The secret code was: " + secret);
        return new GameRecord("Player", score);
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


    private int checkExacts(StringBuilder secretSB, StringBuilder guessSB, boolean[] exactsFound) {
        int exacts = 0;
        for (int i = 0; i < CODESIZE; i++) {
            if (secretSB.charAt(i) == guessSB.charAt(i)) {
                exacts++;
                exactsFound[i] = true;
            }
        }
        return exacts;
    }

    private int checkPartials(StringBuilder secretSB, StringBuilder guessSB, boolean[] exactsFound) {
        int partials = 0;
        for (int i = 0; i < CODESIZE; i++) {
            if (exactsFound[i]) continue;

            for (int j = 0; j < CODESIZE; j++) {
                if (i != j && secretSB.charAt(i) == guessSB.charAt(j)) {
                    partials++;
                    break;
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


        double averageScore = allGamesRecord.average();
        System.out.println("Average Score: " + averageScore);

        List<GameRecord> topScores = allGamesRecord.highGameList(2);
        System.out.println("Top 2 Scores:");
        for (GameRecord record : topScores) {
            System.out.println(record);
        }

    }
}
