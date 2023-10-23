public abstract class Game {

    protected AllGamesRecord allGamesRecord = new AllGamesRecord();

    // Abstract method plays the game and returns a GameRecord
    protected abstract GameRecord play();

    // Abstract method returns whether or not to play the next game
    protected abstract boolean playNext();

    // playAll() method plays the game, records the results, and returns an AllGamesRecord object
    public AllGamesRecord playAll() {
        do {
            GameRecord gameRecord = play();
            allGamesRecord.add(gameRecord);
        } while (playNext());  // Continues the game as long as playNext() returns true

        return allGamesRecord;
    }
}
