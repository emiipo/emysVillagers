package me.emii.emysvillagers.tags;

import me.emii.emysvillagers.EmysVillagers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;

public class TagHelperNeoForge implements ITagHelper {

    @Override
    public CompoundTag getCompoundTag(Entity entity) {
        CompoundTag tag = entity.getPersistentData();
        if (!tag.contains(EmysVillagers.MOD_ID)) {
            tag.put(EmysVillagers.MOD_ID, new CompoundTag());
        }

        return tag.getCompound(EmysVillagers.MOD_ID);
    }
    
}
