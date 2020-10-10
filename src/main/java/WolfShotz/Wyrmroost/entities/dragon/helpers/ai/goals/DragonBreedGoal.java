package WolfShotz.Wyrmroost.entities.dragon.helpers.ai.goals;

import WolfShotz.Wyrmroost.entities.dragon.AbstractDragonEntity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.EnumSet;

public class DragonBreedGoal extends Goal
{
    protected final AbstractDragonEntity dragon;
    protected final int limit;
    protected final EntityPredicate predicate;
    protected AbstractDragonEntity targetMate;
    protected int spawnBabyDelay;

    public DragonBreedGoal(AbstractDragonEntity dragon, int limit)
    {
        setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        this.dragon = dragon;
        this.limit = limit;
        this.predicate = new EntityPredicate()
                .setDistance(dragon.getWidth() * 8)
                .allowInvulnerable()
                .allowFriendlyFire()
                .setLineOfSiteRequired()
                .setCustomPredicate(e -> ((AbstractDragonEntity) e).canMateWith(dragon));
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean shouldExecute()
    {
        if (limit > 0 && dragon.breedCount >= limit)
        {
            dragon.resetInLove();
            return false;
        }
        if (!dragon.isInLove()) return false;
        return (targetMate = getNearbyMate()) != null;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting()
    {
        return targetMate.isAlive() && targetMate.isInLove() && dragon.isInLove() && spawnBabyDelay < 60;
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask()
    {
        targetMate = null;
        spawnBabyDelay = 0;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick()
    {
        dragon.getLookController().setLookPositionWithEntity(targetMate, 10f, dragon.getVerticalFaceSpeed());
        dragon.getNavigator().tryMoveToEntityLiving(targetMate, 1);
        if (++spawnBabyDelay >= 60 && dragon.getDistance(targetMate) < dragon.getWidth() * 2)
            dragon.func_234177_a_((ServerWorld) dragon.world, targetMate);
    }

    /**
     * Loops through nearby animals and finds another animal of the same type that can be mated with. Returns the first
     * valid mate found.
     */
    @Nullable
    protected AbstractDragonEntity getNearbyMate()
    {
        return dragon.world.getTargettableEntitiesWithinAABB(dragon.getClass(), predicate, dragon, dragon.getBoundingBox().grow(dragon.getWidth() * 8))
                .stream()
                .min(Comparator.comparingDouble(dragon::getDistanceSq)).orElse(null);
    }
}