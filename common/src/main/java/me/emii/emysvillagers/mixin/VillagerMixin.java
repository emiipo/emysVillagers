package me.emii.emysvillagers.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import me.emii.emysvillagers.HumanoidManager.BodyType;
import me.emii.emysvillagers.accessor.HumanoidData;
import me.emii.emysvillagers.accessor.IHumanoidDataAccessor;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.npc.Villager;

@Mixin(Villager.class)
public class VillagerMixin implements IHumanoidDataAccessor {
    private static final EntityDataAccessor<Byte> BODY_TYPE = SynchedEntityData.defineId(Villager.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<String> NAME = SynchedEntityData.defineId(Villager.class, EntityDataSerializers.STRING);

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    protected void defineHumanoidData(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(BODY_TYPE, (byte)0);
        builder.define(NAME, "");
    }

    @Override
    public HumanoidData emysvillagers$getData() {
        Villager villager = (Villager)(Object)this;
        return new HumanoidData(
            BodyType.values()[villager.getEntityData().get(BODY_TYPE)],
            villager.getEntityData().get(NAME)
        );
    }

    @Override
    public void emysvillagers$setData(HumanoidData data) {
        Villager villager = (Villager)(Object)this;
        if (villager.level().isClientSide()) return;
        villager.getEntityData().set(BODY_TYPE, (byte)data.bodyType().ordinal());
        villager.getEntityData().set(NAME, data.name());
    }
    
    @Override
    public void emysvillagers$setName(String name) {
        Villager villager = (Villager)(Object)this;
        if (villager.level().isClientSide()) return;
        villager.getEntityData().set(NAME, name);
    }
}
