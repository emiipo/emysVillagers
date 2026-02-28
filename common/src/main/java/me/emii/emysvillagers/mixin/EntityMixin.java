package me.emii.emysvillagers.mixin;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import me.emii.emysvillagers.Constants;
import me.emii.emysvillagers.EmysVillagers;
import me.emii.emysvillagers.accessor.IHumanoidDataAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

@Mixin(Entity.class)
public class EntityMixin {

    @Inject(method = "setCustomName", at = @At("TAIL"))
    private void onCustomNameSet(@Nullable Component name, CallbackInfo ci) {
        if(name == null) return;

        Entity mob = (Entity)(Object)this;
        if(mob.level().isClientSide() || mob.getType() != EntityType.VILLAGER) return;
    
        String newName = name.getString();
        CompoundTag tag = EmysVillagers.TAG_HELPER.getCompoundTag(mob);
        tag.putString(Constants.NAME, newName);
        ((IHumanoidDataAccessor)(Object)mob).emysvillagers$setName(newName);
    }
}
