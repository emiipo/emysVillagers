package me.emii.emysvillagers.tags;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;

public interface ITagHelper {
    CompoundTag getCompoundTag(Entity entity);
}
