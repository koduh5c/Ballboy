package ballboy;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ballboy.model.Entity;
import ballboy.model.GameEngine;
import ballboy.model.GameEngineImpl;
import ballboy.model.Level;
import ballboy.model.entities.ControllableDynamicEntity;
import ballboy.model.entities.DynamicEntity;
import ballboy.model.entities.utilities.Vector2D;
import ballboy.model.factories.BallboyFactory;
import ballboy.model.factories.CloudFactory;
import ballboy.model.factories.EnemyFactory;
import ballboy.model.factories.EntityFactoryRegistry;
import ballboy.model.factories.FinishFactory;
import ballboy.model.factories.SquarecatFactory;
import ballboy.model.factories.StaticEntityFactory;
import ballboy.model.levels.DataManager;
import ballboy.model.levels.LevelLoader;
import ballboy.model.levels.LevelManager;
import ballboy.model.levels.PhysicsEngine;
import ballboy.model.levels.PhysicsEngineImpl;
import ballboy.model.levels.Scoreboard;
import ballboy.model.levels.ScoreboardImpl;
import ballboy.view.GameWindow;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import org.json.simple.JSONObject;

public class AppTest {
    private GameEngine ge;
    private GameWindow gw;
    
    @BeforeAll
    public static void setupJavaFx() throws InterruptedException {
        Semaphore available = new Semaphore(0, true);
        Platform.startup(available::release);
        available.acquire();
    }

    @BeforeEach
    public void initAll() {
        ConfigurationParser configuration = new ConfigurationParser();
        JSONObject parsedConfiguration = null;
        try {
            parsedConfiguration = configuration.parseConfig("config.json");
        } catch (ConfigurationParseException e) {
            System.out.println(e);
            System.exit(-1);
        }

        final double frameDurationMilli = 17;
        PhysicsEngine engine = new PhysicsEngineImpl(frameDurationMilli);

        EntityFactoryRegistry entityFactoryRegistry = new EntityFactoryRegistry();
        entityFactoryRegistry.registerFactory("cloud", new CloudFactory());
        entityFactoryRegistry.registerFactory("enemy", new EnemyFactory());
        entityFactoryRegistry.registerFactory("background", new StaticEntityFactory(Entity.Layer.BACKGROUND));
        entityFactoryRegistry.registerFactory("static", new StaticEntityFactory(Entity.Layer.FOREGROUND));
        entityFactoryRegistry.registerFactory("finish", new FinishFactory());
        entityFactoryRegistry.registerFactory("hero", new BallboyFactory());
        entityFactoryRegistry.registerFactory("squarecat", new SquarecatFactory());

        List<Level> levelList = LevelLoader.getLevelList(
                parsedConfiguration,
                engine,
                entityFactoryRegistry,
                frameDurationMilli
            );

        ge = new GameEngineImpl(levelList);

        gw = new GameWindow(ge, 640, 400, frameDurationMilli);
    }

    @Test
    public void testIncorrectConfig() {
        ConfigurationParser configuration = new ConfigurationParser();
        JSONObject parsedConfiguration = null;
        try {
            parsedConfiguration = configuration.parseConfig("incorrect_config.json");
        } catch (ConfigurationParseException e) {
            System.out.println(e);
            System.exit(-1);
        } catch (NullPointerException e) {

        }

        final double frameDurationMilli = 17;
        PhysicsEngine engine = new PhysicsEngineImpl(frameDurationMilli);

        EntityFactoryRegistry entityFactoryRegistry = new EntityFactoryRegistry();
        entityFactoryRegistry.registerFactory("cloud", new CloudFactory());
        entityFactoryRegistry.registerFactory("enemy", new EnemyFactory());
        entityFactoryRegistry.registerFactory("background", new StaticEntityFactory(Entity.Layer.BACKGROUND));
        entityFactoryRegistry.registerFactory("static", new StaticEntityFactory(Entity.Layer.FOREGROUND));
        entityFactoryRegistry.registerFactory("finish", new FinishFactory());
        entityFactoryRegistry.registerFactory("hero", new BallboyFactory());
        entityFactoryRegistry.registerFactory("squarecat", new SquarecatFactory());

        List<Level> levelList = null;

        try {
            levelList = LevelLoader.getLevelList(
                parsedConfiguration,
                engine,
                entityFactoryRegistry,
                frameDurationMilli
            );
        } catch (NullPointerException e) {

        }
        

        ge = new GameEngineImpl(levelList);
    }

    @Test
    public void testGameEngine() {
        assertNotNull(ge.getCurrentLevel());
        assertTrue(ge.boostHeight());
        assertTrue(ge.dropHeight());
        assertTrue(ge.moveLeft());
        assertTrue(ge.moveRight());
        assertNotNull(ge.levelManager());
    }

    @Test
    public void testLevel() {
        Level l = ge.getCurrentLevel();
        assertEquals(12, l.getEntities().size());
        assertEquals(620.0, l.getLevelHeight());
        assertEquals(2000.0, l.getLevelWidth());
        assertEquals(50.0, l.getHeroHeight());
        assertEquals(29, (int) l.getHeroWidth());
        assertEquals(150.0, l.getHeroX());
        assertEquals(300.0, l.getHeroY());
        assertEquals("0x001100ff", l.getFloorColor().toString());
        assertEquals(600.0, l.getFloorHeight());
        assertEquals(700.0, l.getGravity());

        Entity hero = null;
        Entity finish = null;
        Entity random = null;
        for (Entity e : l.getEntities()) {
            if (e.id().equals("ballboy")) {
                hero = e;
            } else if (e.id().equals("finish")) {
                finish = e;
            } else {
                if (random == null) {
                    random = e;
                }
            }
        }
        assertFalse(l.isHero(random));
        assertFalse(l.isFinish(random));
        assertTrue(l.isHero(hero));
        assertTrue(l.isFinish(finish));
        assertTrue(l.scoreboard() instanceof Scoreboard);
        Scoreboard previousScoreboard = l.scoreboard();
        l.setScoreboard(new ScoreboardImpl(l));
        assertNotEquals(previousScoreboard, l.scoreboard());

        l.removeEntity(random);
        assertEquals(11, l.getEntities().size());

        l.finish();
        assertTrue(l.hasFinishedLevel());
        l.resetLevel();
        assertFalse(l.hasFinishedLevel());

        List<Entity> previousList = l.getEntities();
        l.replaceEntities(Arrays.asList(hero, finish));
        assertEquals(l.getEntities(), previousList);
    }

    @Test
    public void testScoreboard() {
        Scoreboard sb = ge.levelManager().scoreboard();
        assertTrue(sb.scoreboard() instanceof Map<?,?>);
        Map<String, Integer> previousScoreboard = sb.scoreboard();
        sb.setScoreboard(new ScoreboardImpl(ge.getCurrentLevel()).scoreboard());
        assertEquals(previousScoreboard, sb.scoreboard());
        sb.setTotalScore(10);
        assertEquals(10, sb.totalScore());
        sb.setScoreText(new StringBuilder("TEST"));
        assertEquals("TEST", sb.scoreText().toString());
    }

    @Test
    public void testLevelManager() {
        LevelManager lm = ge.levelManager();
        assertEquals(3, lm.levelList().size());
        assertTrue(lm.dataManager() instanceof DataManager);
        lm.setLevelIndex(1);
        assertEquals(1, lm.levelIndex());
        assertEquals("WINNER!", lm.buildWinnerText(100, 100).getText());
        assertEquals("", lm.buildScoreboardText(100, 100).getText());

        lm.load();

        assertEquals(12, lm.currentLevel().getEntities().size());

        lm.save();

        lm.currentLevel().removeEntity(lm.currentLevel().getEntities().get(0));

        assertEquals(11, lm.currentLevel().getEntities().size());

        lm.load();

        assertEquals(12, lm.currentLevel().getEntities().size());
    }

    @Test
    public void testEntity() {
        ControllableDynamicEntity hero = null;
        DynamicEntity squarecat = null;
        Entity finish = null;
        Entity enemy = null;
        for (Entity e : ge.getCurrentLevel().getEntities()) {
            if ((e.id().equals("ballboy") && (hero == null))) {
                hero = (ControllableDynamicEntity) e;
            } else if ((e.id().equals("enemy") && (enemy == null))) {
                enemy = e;
            } else if ((e.id().equals("squarecat")) && (squarecat == null)) {
                squarecat = (DynamicEntity) e;
            } else if ((e.id().equals("finish")) && (finish == null)) {
                finish = e;
            }
        }
        Vector2D initialPos = hero.getPosition();
        ge.moveRight();
        ge.tick();
        assertNotEquals(hero.getPositionBeforeLastUpdate(), hero.getPosition());
        hero.reset();
        assertEquals(initialPos, hero.getPosition());
        hero.moveRight();
        ge.tick();
        hero.collideWith(enemy);
        assertEquals(initialPos.getX(), hero.getPosition().getX());
        assertEquals(initialPos.getY(), hero.getPosition().getY());
        hero.setHorizontalAcceleration(100);
        assertEquals(100, hero.getHorizontalAcceleration());
        assertTrue(hero.getImage() instanceof Image);       
        assertEquals(Entity.Layer.FOREGROUND, hero.getLayer());

        squarecat.collideWith(enemy);
        assertFalse(ge.getCurrentLevel().getEntities().contains(enemy));


        hero.collideWith(finish);
        for (int i = 0; i < 200; i++) { ge.tick(); }
        assertEquals(1, ge.levelManager().levelIndex());
    }

    @Test
    public void testGameWindow() {
        assertTrue(gw.getScene() instanceof Scene);
        gw.run();
    }

    @AfterEach
    public void tearDownAll() {
        ge = null;
        gw = null;
    }

    @AfterAll
    public static void cleanupJavaFx() {
        Platform.exit();
    }
}
