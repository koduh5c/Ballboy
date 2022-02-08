package ballboy.model.levels;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import ballboy.model.Level;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class LevelManagerImpl implements LevelManager {
    // Level
    private List<Level> levelList;
    private int levelIndex = 0;
    private Text winnerText = new Text("WINNER!");

    // DataManager
    private DataManager dataManager;

    // Scoreboard
    private Text scoreText = new Text();

    public LevelManagerImpl(List<Level> levelList, DataManager dataManager) {
        this.levelList = levelList;
        this.dataManager = dataManager;
    }

    @Override
    public Scoreboard scoreboard() {
        return this.currentLevel().scoreboard();
    }

    @Override
    public List<Level> levelList() {
        return this.levelList;
    }

    @Override
    public void save() {
        this.dataManager.save(this);
    }

    @Override
    public void load() {
        this.dataManager.load(this);
    }

    @Override
    public DataManager dataManager() {
        return this.dataManager;
    }

    @Override
    public void setLevelIndex(int levelIndex) {
        this.levelIndex = levelIndex;
    }

    @Override
    public int levelIndex() {
        return this.levelIndex;
    }

    @Override
    public Level currentLevel() {
        return this.levelList.get(levelIndex);
    }

    @Override
    public void endLevel() {
        if (this.currentLevel().hasFinishedLevel()) {
            if (this.levelIndex == this.levelList.size() - 1) {
                this.winnerText.setVisible(true);
                TimerTask task = new TimerTask() {
                    
                    @Override
                    public void run() {
                        System.exit(0);
                    }
                };
                long delay = TimeUnit.SECONDS.toMillis(5);
                Timer t = new Timer();
                t.schedule(task, delay);
            }
            if (this.levelIndex < this.levelList.size() - 1) {
                int totalScoreFromPreviousLevel = this.scoreboard().totalScore();
                this.levelIndex++;
                this.scoreboard().setTotalScore(totalScoreFromPreviousLevel);
            }
            this.scoreboard().updateScoreboard();
        }
    }

    @Override
    public void update() {
        this.scoreText.setText(this.scoreboard().scoreText().toString());
        this.endLevel();
        this.currentLevel().update();
    }

    @Override
    public Text buildWinnerText(double width, double height) {
        this.winnerText.setFont(Font.font("Verdana", FontWeight.BOLD, 40));
        this.winnerText.setX(width / 2 - this.winnerText.getLayoutBounds().getWidth() / 2);
        this.winnerText.setY(height / 2 - this.winnerText.getLayoutBounds().getHeight() / 2);
        this.winnerText.setVisible(false);
        return this.winnerText;
    }

    @Override
    public Text buildScoreboardText(double width, double height) {
        this.scoreText.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        this.scoreText.setX(width * 0.8);
        this.scoreText.setY(height * 0.1);
        return this.scoreText;
    }
}
