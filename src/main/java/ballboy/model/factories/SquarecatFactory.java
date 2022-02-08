package ballboy.model.factories;

import ballboy.ConfigurationParseException;
import ballboy.model.Entity;
import ballboy.model.Level;
import ballboy.model.entities.behaviour.SquarecatBehaviourStrategy;
import ballboy.model.entities.collision.SquarecatCollisionStrategy;
import ballboy.model.entities.DynamicEntityImpl;
import ballboy.model.entities.utilities.AxisAlignedBoundingBox;
import ballboy.model.entities.utilities.AxisAlignedBoundingBoxImpl;
import ballboy.model.entities.utilities.KinematicState;
import ballboy.model.entities.utilities.SquarecatStateImpl;
import ballboy.model.entities.utilities.Vector2D;
import javafx.scene.image.Image;
import org.json.simple.JSONObject;

public class SquarecatFactory implements EntityFactory {

    @Override
    public Entity createEntity(
            Level level,
            JSONObject config) {
        try {
            double startX = 0;
            double startY = 0;

            double distanceFromHero = (double) config.getOrDefault("distanceFromHero", 40.0);
            double velocity = (double) config.getOrDefault("velocity", 2.0);

            String imageName = (String) config.getOrDefault("image", "squarecat.png");
            String size = (String) config.get("size");

            double height;
            if (size.equals("small")) {
                height = 10.0;
            } else if (size.equals("medium")) {
                height = 25.0;
            } else if (size.equals("large")) {
                height = 50.0;
            } else {
                throw new ConfigurationParseException(String.format("Invalid squarecat size %s", size));
            }

            Image image = new Image(imageName);
            // preserve image ratio
            double width = height * image.getWidth() / image.getHeight();

            Vector2D startingPosition = new Vector2D(startX, startY);

            KinematicState kinematicState = new SquarecatStateImpl.SquarecatStateBuilder()
                    .setPosition(startingPosition)
                    .build();

            AxisAlignedBoundingBox volume = new AxisAlignedBoundingBoxImpl(
                    startingPosition,
                    height,
                    width
            );

            return new DynamicEntityImpl(
                    kinematicState,
                    volume,
                    Entity.Layer.FOREGROUND,
                    new Image(imageName),
                    new SquarecatCollisionStrategy(level),
                    new SquarecatBehaviourStrategy(level, distanceFromHero, velocity),
                    "squarecat"
            );

        } catch (Exception e) {
            throw new ConfigurationParseException(
                    String.format("Invalid squarecat entity configuration | %s | %s", config, e));
        }
    }
}
