package com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai;

import com.github.wolfshotz.wyrmroost.entities.dragon.AbstractDragonEntity;
import net.minecraft.entity.ai.controller.BodyController;
import net.minecraft.util.math.MathHelper;

/**
 * Created by com.github.WolfShotz - 8/26/19 - 16:12
 * <p>
 * Disallows rotations while sitting, sleeping, and helps control yaw while controlling
 */
public class DragonBodyController extends BodyController
{
    public AbstractDragonEntity dragon;

    public DragonBodyController(AbstractDragonEntity dragon)
    {
        super(dragon);
        this.dragon = dragon;
    }

    @Override
    public void tick()
    {
        // No body rotations while sitting or sleeping
        if (dragon.isSleeping()) return;

        // Clamp the head rotation to 70 degrees while sitting
        if (dragon.isInSittingPose())
        {
            clampHeadRotation(70f);
            return;
        }

        // clamp head to 120 degrees, rotate body according to head
        if (dragon.canBeControlledByRider() || dragon.isFlying())
        {
            clampHeadRotation(120f);
            dragon.bodyYaw = dragon.yaw = MathHelper.wrapDegrees(MathHelper.stepAngleTowards(dragon.headYaw, dragon.bodyYaw, dragon.getYawRotationSpeed()));
            return;
        }

        super.tick();
    }

    public void clampHeadRotation(float clampDeg)
    {
        dragon.headYaw = MathHelper.stepAngleTowards(dragon.headYaw, dragon.bodyYaw, clampDeg);
    }
}
