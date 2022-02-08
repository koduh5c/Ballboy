package ballboy.model;

import java.util.List;

import ballboy.model.levels.DataManager;
import ballboy.model.levels.LevelManager;
import ballboy.model.levels.LevelManagerImpl;

/**
 * Implementation of the GameEngine interface.
 * This provides a common interface for the entire game.
 */
public class GameEngineImpl implements GameEngine {
    private LevelManager levelManager;

    public GameEngineImpl(List<Level> levelList) {
        this.levelManager = new LevelManagerImpl(levelList, new DataManager());
    }

    @Override
    public Level getCurrentLevel() {
        return this.levelManager.currentLevel();
    }

    @Override
    public boolean boostHeight() {
        return this.getCurrentLevel().boostHeight();
    }

    @Override
    public boolean dropHeight() {
        return this.getCurrentLevel().dropHeight();
    }

    @Override
    public boolean moveLeft() {
        return this.getCurrentLevel().moveLeft();
    }

    @Override
    public boolean moveRight() {
        return this.getCurrentLevel().moveRight();
    }

    @Override
    public void tick() {
        this.levelManager.update();
    }

    @Override
    public LevelManager levelManager() {
        return this.levelManager;
    }
}