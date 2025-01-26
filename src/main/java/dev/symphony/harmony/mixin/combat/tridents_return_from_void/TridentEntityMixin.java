package dev.symphony.harmony.mixin.combat.tridents_return_from_void;

import dev.symphony.harmony.Harmony;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TridentEntity.class)
public abstract class TridentEntityMixin extends Entity {
    @Shadow public int returnTimer;
    @Shadow private boolean dealtDamage;

    protected TridentEntityMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tickMixin(CallbackInfo ci) {
        if(Harmony.CONFIG.tridentsReturnFromVoid()){
            if (!this.getWorld().isClient) {
                if (!this.dealtDamage && this.getBlockPos().getY() < 1) {
                    this.dealtDamage = true;
                    this.returnTimer = 0;
                }
            }
        }
    }
}