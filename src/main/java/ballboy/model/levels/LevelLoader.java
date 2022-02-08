package ballboy.model.levels;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import ballboy.model.Level;
import ballboy.model.factories.EntityFactory;

public class LevelLoader {
    /**
     * Takes in all the necessary parameters built in App class and iterates through the config level list to create new Level objects for each one
     * @param parsedConfiguration
     * @param engine
     * @param entityFactoryRegistry
     * @param frameDurationMilli
     * @return Returns the completed level list
     */
    public static List<Level> getLevelList(JSONObject parsedConfiguration, PhysicsEngine engine, EntityFactory entityFactoryRegistry, double frameDurationMilli) {
        Integer levelIndex = ((Number) parsedConfiguration.get("currentLevelIndex")).intValue();
        JSONArray levelConfigs = (JSONArray) parsedConfiguration.get("levels");

        List<Level> res = new ArrayList<>();
        for (int i = levelIndex; i < levelConfigs.size(); i++) {
            JSONObject levelConfig = (JSONObject) levelConfigs.get(i);
            Level level = new LevelImpl(levelConfig, engine, entityFactoryRegistry, frameDurationMilli);
            res.add(level);
        }
        return res;
    }
}
