package ballboy.model.entities.behaviour;

import ballboy.model.Level;
import ballboy.model.entities.DynamicEntity;
import ballboy.model.entities.utilities.Vector2D;

public class SquarecatBehaviourStrategy implements BehaviourStrategy {
    private Level level;
    private double distanceFromHero;
    private double cursorX;
    private double cursorY;
    private double velocity;

    public SquarecatBehaviourStrategy(Level level, double distanceFromHero, double velocity) {
        this.level = level;
        this.distanceFromHero = distanceFromHero;
        this.cursorX = -distanceFromHero;
        this.cursorY = -distanceFromHero;
        this.velocity = velocity;
    }
    
    /**
     * Makes squarecat orbit the hero in clockwise direction following the shape of a square.
     * @param entity The squarecat
     * @param framDurationMilli
     */
    @Override
    public void behave(DynamicEntity entity, double frameDurationMilli) {
        double heroX = this.level.getHeroX();
        double heroY = this.level.getHeroY();

        if ((cursorY == -distanceFromHero) && (cursorX < distanceFromHero)) {
            cursorX += velocity;
        } else if ((cursorX == distanceFromHero) && (cursorY < distanceFromHero)) {
            cursorY += velocity;
        } else if ((cursorY == distanceFromHero) && (cursorX > -distanceFromHero)) {
            cursorX -= velocity;
        } else if ((cursorX == -distanceFromHero) && (cursorY > -distanceFromHero)) {
            cursorY -= velocity;
        }

        entity.setPosition(new Vector2D(heroX + cursorX, heroY + cursorY + entity.getHeight() / 2));
    }
    
}
