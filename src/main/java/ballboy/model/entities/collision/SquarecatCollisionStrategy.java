package ballboy.model.entities.collision;

import ballboy.model.Entity;
import ballboy.model.Level;
import ballboy.model.entities.DynamicEntity;
import ballboy.model.entities.DynamicEntityImpl;

public class SquarecatCollisionStrategy implements CollisionStrategy {
    private final Level level;

    public SquarecatCollisionStrategy(Level level) {
        this.level = level;
    }

    /**
     * Increments the relevant colour on the scoreboard and removes that entity (enemy) from level entity list.
     * @param currentEntity The squarecat
     * @param hitEntity Any entity designated as the enemy
     */
    @Override
    public void collideWith(Entity currentEntity, Entity hitEntity) {
        if ((hitEntity instanceof DynamicEntity) && (!this.level.isHero(hitEntity))) {
            if (((DynamicEntityImpl) hitEntity).id().equals("enemy")) {
                this.level.scoreboard().killCount(hitEntity);
                this.level.removeEntity(hitEntity);
            }
        }
    }
}
