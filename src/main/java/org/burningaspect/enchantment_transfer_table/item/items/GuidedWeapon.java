package org.burningaspect.enchantment_transfer_table.item.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.SpectralArrowEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.burningaspect.enchantment_transfer_table.misc.Utils;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class GuidedWeapon extends BowItem {

    private LivingEntity target;
    private final int range = 70;

    public GuidedWeapon(Settings settings) {
        super(settings);
    }

    public void setTarget(LivingEntity target) {
        this.target = target;
    }

    public LivingEntity getTarget() {
        return target;
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if(this.getTarget() == null) {
            this.setTarget(findNearestLivingEntity(user, world, this.range));
            return;
        }

        if(this.getTarget() != null) {
            if(this.getTarget().isDead()) {
                this.setTarget(findNearestLivingEntity(user, world, this.range));
            }
        }

        if(this.getTarget() != null) {
            if(this.getTarget().getPos().distanceTo(user.getPos()) > this.range) {
                this.setTarget(findNearestLivingEntity(user, world, this.range));
            }
        }

        super.onStoppedUsing(stack, world, user, remainingUseTicks);
    }

    private LivingEntity findNearestLivingEntity(LivingEntity user, World world, int radius) {
        return world.getClosestEntity(
                world.getEntitiesByClass(LivingEntity.class, new Box(
                                user.getPos().getX() - radius,
                                user.getPos().getY() - radius,
                                user.getPos().getZ() - radius,
                                user.getPos().getX() + radius,
                                user.getPos().getY() + radius,
                                user.getPos().getZ() + radius),
                        x -> (user.canSee(x) && x.isAlive())
                                && (x instanceof PlayerEntity || x instanceof HostileEntity) &&
                                !(x.isInvulnerable() || x.isInvisible())),
                TargetPredicate.DEFAULT, user, radius, radius, radius);
    }

    @Override
    protected void shoot(LivingEntity shooter, ProjectileEntity projectile, int index, float speed, float divergence, float yaw, @Nullable LivingEntity target) {
        //shootInvisibleArrowAtTarget(shooter);
        super.shoot(shooter, projectile, index, speed, divergence, yaw, target);

        if(this.getTarget() == null) {
            this.setTarget(findNearestLivingEntity(shooter, shooter.getWorld(), this.range));
        }

        if(this.getTarget() != null) {
            if(this.getTarget().isDead()) {
                this.setTarget(findNearestLivingEntity(shooter, shooter.getWorld(), this.range));
            }
        }

        Vec3d direction = this.getTarget().getPos().subtract(0, 0.5, 0)
                .subtract(shooter.getPos()).normalize();
        projectile.setVelocity(direction.x, direction.y, direction.z, 12.0f, 0);
        projectile.setNoGravity(true);
        projectile.setInvisible(true);

        if(this.getTarget() != null) {
            if(this.getTarget().isAlive()) {
                this.getTarget().setGlowing(true);

                Utils.drawParticleTrail(shooter.getWorld(), shooter.getPos(), this.getTarget().getPos(),
                        ParticleTypes.FLAME, (int) this.getTarget().getPos().
                                distanceTo(shooter.getPos()));

                Thread stopGlow = new Thread(() -> {
                    try {
                        Thread.sleep(500);

                        if(!(this.getTarget() == null)) {
                            if(this.getTarget().isAlive()) {
                                this.target.setGlowing(false);
                            }
                        }
                    } catch(InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });

                stopGlow.start();
            }
        }
    }

    /*
    private void shootInvisibleArrowAtTarget(LivingEntity player) {
        World world = player.getWorld();
        LivingEntity target = this.getTarget();

        if (target != null) {
            SpectralArrowEntity arrow = new SpectralArrowEntity(
                    world, player, player.getActiveItem(), player.getActiveItem());

            arrow.setSilent(true);
            arrow.setNoGravity(true);
            arrow.setInvisible(true);

            Vec3d direction = target.getPos().subtract(player.getPos()).normalize();
            arrow.setVelocity(direction.x, direction.y, direction.z, 5.0f, 0);

            world.spawnEntity(arrow);
        }
    }
     */
}
