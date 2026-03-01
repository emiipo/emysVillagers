package me.emii.emysvillagers.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import me.emii.emysvillagers.Constants;
import me.emii.emysvillagers.EmysVillagers;
import me.emii.emysvillagers.HumanoidManager;
import me.emii.emysvillagers.HumanoidManager.BodyType;
import me.emii.emysvillagers.accessor.HumanoidData;
import me.emii.emysvillagers.accessor.IHumanoidDataAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;

@Mixin(Villager.class)
public class VillagerMixin implements IHumanoidDataAccessor {
    private static final EntityDataAccessor<Byte> BODY_TYPE = SynchedEntityData.defineId(Villager.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<String> NAME = SynchedEntityData.defineId(Villager.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Float> PITCH = SynchedEntityData.defineId(Villager.class, EntityDataSerializers.FLOAT);

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    protected void defineHumanoidData(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(BODY_TYPE, (byte)0);
        builder.define(NAME, "");
        builder.define(PITCH, 0f);
    }

    @Inject(method = "getAmbientSound", at = @At("RETURN"), cancellable = true)
    protected SoundEvent replaceAmbientSound(CallbackInfoReturnable<SoundEvent> cir) {
        CompoundTag tag = EmysVillagers.TAG_HELPER.getCompoundTag((Entity)(Object)this);
        if (!tag.contains(Constants.BODY_TYPE)) return null;

        SoundEvent rVal = cir.getReturnValue();
        BodyType bodyType = emysvillagers$getData().bodyType();

        if (rVal == SoundEvents.VILLAGER_TRADE) {
            if(bodyType == BodyType.FEM) return HumanoidManager.FEM_TRADE_SOUNDS;
            else if (bodyType == BodyType.MASC) return HumanoidManager.MASC_TRADE_SOUNDS;
        }
        else if (rVal == SoundEvents.VILLAGER_AMBIENT) {
            if(bodyType == BodyType.FEM) return HumanoidManager.FEM_AMBIENT_SOUNDS;
            else if (bodyType == BodyType.MASC) return HumanoidManager.MASC_AMBIENT_SOUNDS;
        }

        return rVal;
    }

    @Inject(method = "getDeathSound", at = @At("RETURN"), cancellable = true)
    protected SoundEvent replaceDeathSound(CallbackInfoReturnable<SoundEvent> cir) {
        BodyType bodyType = emysvillagers$getData().bodyType();

        if(bodyType == BodyType.FEM) return HumanoidManager.FEM_DEATH_SOUNDS;
        else return HumanoidManager.MASC_DEATH_SOUNDS;
    }

    @Inject(method = "getHurtSound", at = @At("RETURN"), cancellable = true)
    protected SoundEvent replaceHurtSound(DamageSource damageSource, CallbackInfoReturnable<SoundEvent> cir) {
        BodyType bodyType = emysvillagers$getData().bodyType();

        if(bodyType == BodyType.FEM) return HumanoidManager.FEM_HURT_SOUNDS;
        else return HumanoidManager.MASC_HURT_SOUNDS;
    }

    @Redirect(method = "setUnhappy", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/npc/Villager;makeSound(Lnet/minecraft/sounds/SoundEvent;)V"))
    private void redirectUnhappySound(Villager villager, SoundEvent soundEvent) {
        BodyType bodyType = emysvillagers$getData().bodyType();

        if(bodyType == BodyType.FEM) villager.makeSound(HumanoidManager.FEM_NO_SOUNDS);
        else villager.makeSound(HumanoidManager.MASC_NO_SOUNDS);
    }

    @Override
    public HumanoidData emysvillagers$getData() {
        SynchedEntityData entityData = ((Villager)(Object)this).getEntityData();

        return new HumanoidData(
            HumanoidManager.getBodyType(entityData.get(BODY_TYPE)),
            entityData.get(NAME),
            entityData.get(PITCH)
        );
    }

    @Override
    public void emysvillagers$setData(HumanoidData data) {
        Villager villager = (Villager)(Object)this;
        if (villager.level().isClientSide()) return;

        SynchedEntityData entityData = villager.getEntityData();
        entityData.set(BODY_TYPE, (byte)data.bodyType().ordinal());
        entityData.set(NAME, data.name());
        entityData.set(PITCH, data.pitch());
    }
    
    @Override
    public void emysvillagers$setName(String name) {
        Villager villager = (Villager)(Object)this;
        if (villager.level().isClientSide()) return;
        villager.getEntityData().set(NAME, name);
    }
}
