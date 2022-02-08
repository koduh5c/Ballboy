package ballboy.model.levels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ballboy.model.Entity;
import ballboy.model.Level;
import ballboy.model.entities.DynamicEntity;
import ballboy.model.entities.utilities.Vector2D;


public class DataManager {
    // Level
    private List<List<Entity>> savedEntities = new ArrayList<>();
    private List<List<Vector2D>> savedPositions = new ArrayList<>();
    private int savedLevelIndex;
    
    // Scoreboard
    private List<Map<String, Integer>> savedScoreboards = new ArrayList<>();
    private List<StringBuilder> savedScoreTexts = new ArrayList<>();
    private List<Integer> savedTotalScores = new ArrayList<>();

    /**
     * Clears all saved lists prior to use.
     * Iterates level list in levelManager and adds all relevant objects/values to its relevants lists
     * For Level:
     * - All alive/not yet removed entities
     * - Positions of all these entities
     * - Index of level the game was saved on
     * For Scoreboard:
     * - All scoreboards in every level
     * - All scoreboard texts in every level
     * - All totalscores in every level
     * @param levelManager
     */
    public void save(LevelManager levelManager) {
        savedEntities.clear();
        savedPositions.clear();
        savedScoreboards.clear();
        savedScoreTexts.clear();
        savedTotalScores.clear();

        for (Level l : levelManager.levelList()) {
            // Saving levels
            List<Entity> tempLevel = new ArrayList<>();
            List<Vector2D> tempPos = new ArrayList<>();
            for (Entity e  : l.getEntities()) {
                tempLevel.add(e);
                tempPos.add(new Vector2D(e.getPosition().getX(), e.getPosition().getY()));
            }
            savedEntities.add(tempLevel);
            savedPositions.add(tempPos);
            savedLevelIndex = levelManager.levelIndex();
            
            // Saving scoreboards
            Scoreboard scoreboard = l.scoreboard();
            Map<String, Integer> tempMap = new HashMap<>();
            for (String s : scoreboard.scoreboard().keySet()) {
                tempMap.put(s, scoreboard.scoreboard().get(s));
            }
            savedScoreboards.add(tempMap);
            savedScoreTexts.add(new StringBuilder(scoreboard.scoreText().toString()));
            savedTotalScores.add(scoreboard.totalScore());
        }
    }

    /**
     * Only runs if none of the attribute lists are empty.
     * Replaces all saved values by repeating the same iterative process as save method of above
     * @param levelManager
     */
    public void load(LevelManager levelManager) {
        if (!this.savedEntities.isEmpty() &&
            !this.savedPositions.isEmpty() &&
            !this.savedScoreboards.isEmpty() &&
            !this.savedScoreTexts.isEmpty() &&
            !this.savedTotalScores.isEmpty()
        ) {
            for (int i = 0; i < levelManager.levelList().size(); i++) {
                Level level = levelManager.levelList().get(i);

                // Loading levels
                List<Entity> entities = savedEntities.get(i);
                List<Vector2D> positions = savedPositions.get(i);
                for (int j = 0; j < entities.size(); j++) {
                    Entity entity = entities.get(j);
                    if (entity instanceof DynamicEntity) {
                        Vector2D position = positions.get(j);
                        ((DynamicEntity) entity).setPosition(position);
                    }
                }
                level.replaceEntities(entities);
                if (i >= savedLevelIndex) {
                    level.resetLevel();
                }

                // Loading scoreboards
                level.scoreboard().setScoreboard(savedScoreboards.get(i));
                level.scoreboard().setScoreText(savedScoreTexts.get(i));
                level.scoreboard().setTotalScore(savedTotalScores.get(i));

                level.scoreboard().updateScoreboard();
            }
            levelManager.setLevelIndex(savedLevelIndex);
        }
    }
}
