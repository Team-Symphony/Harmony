package dev.symphony.harmony.mixin.mobs.permissive_parrot_perching;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Entity.class)
public abstract class EntityImplMixin {

    @WrapMethod(method = "setSneaking")
    public void setSneakingImpl(boolean sneaking, Operation<Void> original) {
        original.call(sneaking);
    }

}
