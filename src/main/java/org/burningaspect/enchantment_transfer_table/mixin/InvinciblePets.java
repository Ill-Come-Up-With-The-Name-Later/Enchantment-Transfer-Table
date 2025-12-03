package org.burningaspect.enchantment_transfer_table.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class InvinciblePets {

    @Inject(method = "damage", at = @At("HEAD"))
    private void makeInvulnerable(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if(entity instanceof TameableEntity tameable) {
            if(tameable.isTamed()) {
                if(source.getAttacker() instanceof LivingEntity attacker) {
                    if(!attacker.equals(tameable.getOwner())) {
                        return;
                    }
                } else if(source.getSource() instanceof ProjectileEntity projectile) {
                    if(projectile.getOwner() instanceof LivingEntity) {
                        LivingEntity attacker = (LivingEntity) source.getAttacker();

                        assert attacker != null;
                        if(!attacker.equals(tameable.getOwner())) {
                            return;
                        }
                    }
                }
            }
        }
    }
}
