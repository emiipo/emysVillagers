package me.emii.emysvillagers.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import me.emii.emysvillagers.accessor.IHumanoidDataAccessor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "getVoicePitch", at = @At("RETURN"), cancellable = true)      
    private void modifyVoicePitch(CallbackInfoReturnable<Float> cir) {
        LivingEntity livingEntity = (LivingEntity)(Object)this;
        if (livingEntity.getType() != EntityType.VILLAGER) return;

        float basePitch = cir.getReturnValue();
        float modifier = 1f + ((IHumanoidDataAccessor)(Object)livingEntity).emysvillagers$getData().pitch();

        cir.setReturnValue(basePitch * modifier);
    }

}
