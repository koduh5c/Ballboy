package ballboy.model.levels;

import java.util.List;

import ballboy.model.Level;
import javafx.scene.text.Text;

public interface LevelManager {

    /**
     * 
     * @return Scoreboard of the current level
     */
    Scoreboard scoreboard();
    
    List<Level> levelList();

    void save();

    void load();

    DataManager dataManager();

    void setLevelIndex(int levelIndex);

    int levelIndex();

    Level currentLevel();

    /**
     * If the currentlevel flags finished and is the last level in the list, set the winner text visible and start a timer for 5 seconds to display on the game screen before shutting off.
     * 
     * If not the last level but flags level finished, increment level index so the level manager switches to the next level while carrying over the totalscore from the previous level and setting it as the totalscore of the new level scoreboard.
     */
    void endLevel();

    /**
     * Constantly update the scoreboard text with the most recent score output
     * Run endLevel() to check if the level has ended
     * Finally update the currentLevel
     */
    void update();

    Text buildWinnerText(double width, double height);

    Text buildScoreboardText(double width, double height);
}
