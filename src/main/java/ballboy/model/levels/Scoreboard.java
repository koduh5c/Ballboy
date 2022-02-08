package ballboy.model.levels;

import java.util.Map;

import ballboy.model.Entity;
import ballboy.model.Level;

public interface Scoreboard {
    Map<String, Integer> scoreboard();

    /**
     * Reads in the scoremap parsed during level construction for the code colour configured for enemy image file names
     * @param currentLevel
     */
    void initScoreboard(Level currentLevel);

    void updateScoreboard();

    void setScoreboard(Map<String, Integer> scoreboard);

    void setTotalScore(int totalScore);

    void setScoreText(StringBuilder scoreText);

    int totalScore();

    StringBuilder scoreText();

    /**
     * Used inside SquarecatCollisionStrategy
     * If squarecat collides with an enemy, it reads its image file name and increments its corresponding coded colour in scoremap as well as incrementing the totalscore.
     * @param entity
     */
    void killCount(Entity entity);
}   
