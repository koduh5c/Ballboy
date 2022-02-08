package ballboy.model.levels;

import java.util.HashMap;
import java.util.Map;

import ballboy.model.Entity;
import ballboy.model.Level;

public class ScoreboardImpl implements Scoreboard {
    private Map<String, String> scoremap = new HashMap<>();
    private Map<String, Integer> scoreboard = new HashMap<>();
    private StringBuilder scoreText = new StringBuilder();
    private int totalScore = 0;

    public ScoreboardImpl(Level currentLevel) {
        this.initScoreboard(currentLevel);
    }

    @Override
    public Map<String, Integer> scoreboard() {
        return this.scoreboard;
    }

    @Override
    public void initScoreboard(Level currentLevel) {
        this.scoremap = currentLevel.scoremap();
        for (String s : scoremap.keySet()) {
            this.scoreboard.put(scoremap.get(s), 0);
        }
        this.updateScoreboard();
    }   

    @Override
    public void updateScoreboard() {
        this.scoreText.setLength(0);
        this.scoreText.append("SCOREBOARD\n");
        for (String s : this.scoreboard.keySet()) {
            this.scoreText.append(String.format("%s:%d\n", s, scoreboard.get(s)));
        }
        this.scoreText.append(String.format("%s:%d\n", "total", this.totalScore));
    }

    @Override
    public void setScoreboard(Map<String, Integer> scoreboard) {
        this.scoreboard.clear();
        for (String s : scoreboard.keySet()) {
            this.scoreboard.put(s, scoreboard.get(s));
        }
    }

    @Override
    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    @Override
    public void setScoreText(StringBuilder scoreText) {
        this.scoreText = scoreText;
    }

    @Override
    public int totalScore() {
        return this.totalScore;
    }

    @Override
    public StringBuilder scoreText() {
        return this.scoreText;
    }

    @Override
    public void killCount(Entity entity) {
        if (entity != null) {
            String[] arr = entity.getImage().getUrl().split("/");
            String colour = this.scoremap.get(arr[arr.length - 1]);

            this.scoreboard.put(colour, this.scoreboard.get(colour) + 1);
            this.totalScore++;

            this.updateScoreboard();
        }
    }
}
