import java.util.Scanner;
public abstract class GuessingGame extends Game {

    protected int score;
    protected int attempts;
    protected Scanner scanner = new Scanner(System.in);

    protected void incrementAttempts() {
        this.attempts++;
    }

    protected boolean isGameOver(int maxAttempts) {
        return this.attempts >= maxAttempts;
    }

    @Override
    protected abstract GameRecord play();

    protected boolean playNext() {
        System.out.print("Do you want to play again? (yes/no): ");
        String response = scanner.next().toLowerCase();
        return "yes".equals(response);
    }
}
