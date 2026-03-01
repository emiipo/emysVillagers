package me.emii.emysvillagers.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import me.emii.emysvillagers.HumanoidManager;
import me.emii.emysvillagers.HumanoidManager.BodyType;
import me.emii.emysvillagers.accessor.IHumanoidDataAccessor;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.npc.AbstractVillager;

@Mixin(AbstractVillager.class)
public class AbstractVillagerMixin {

    @Inject(method = "getNotifyTradeSound", at = @At("RETURN"), cancellable = true)
    public SoundEvent replaceNotifyTradeSound(CallbackInfoReturnable<SoundEvent> cir) {
        BodyType bodyType = ((IHumanoidDataAccessor)(Object)this).emysvillagers$getData().bodyType();

        if(bodyType == BodyType.FEM) return HumanoidManager.FEM_YES_SOUNDS;
        else return HumanoidManager.MASC_YES_SOUNDS;
    }
    
    @Inject(method = "getTradeUpdatedSound", at = @At("RETURN"), cancellable = true)
    protected SoundEvent replaceTradeUpdatedSound(boolean isYesSound, CallbackInfoReturnable<SoundEvent> cir) {
        BodyType bodyType = ((IHumanoidDataAccessor)(Object)this).emysvillagers$getData().bodyType();

        if (isYesSound) {
            if(bodyType == BodyType.FEM) return HumanoidManager.FEM_YES_SOUNDS;
            else return HumanoidManager.MASC_YES_SOUNDS;
        }
        else {
            if(bodyType == BodyType.FEM) return HumanoidManager.FEM_NO_SOUNDS;
            else return HumanoidManager.MASC_NO_SOUNDS;
        }
    }

    @Inject(method = "playCelebrateSound", at = @At("HEAD"), cancellable = true)
    public void playCustomCelebrateSound(CallbackInfo ci) {
        BodyType bodyType = ((IHumanoidDataAccessor)(Object)this).emysvillagers$getData().bodyType();

        AbstractVillager abstractVillager = ((AbstractVillager)(Object)this);
        if(bodyType == BodyType.FEM) abstractVillager.makeSound(HumanoidManager.FEM_YES_SOUNDS);
        else abstractVillager.makeSound(HumanoidManager.MASC_YES_SOUNDS);

        ci.cancel();
    }
}
