public class DBTest {
    public static void main(String[] args) {
        ScoreDB db = new ScoreDB();
        db.registerPlayer("Testspeler");
        db.displayScoreboard();
    }
}