package me.emii.emysvillagers.tags;

import me.emii.emysvillagers.EmysVillagers;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class TagHelperFabric implements ITagHelper {

    public static final AttachmentType<CompoundTag> DATA =
    AttachmentRegistry.create(
        ResourceLocation.fromNamespaceAndPath(EmysVillagers.MOD_ID, "data"),
        builder -> builder.initializer(CompoundTag::new).persistent(CompoundTag.CODEC)
    );

    @Override
    public CompoundTag getCompoundTag(Entity entity) {
        return entity.getAttachedOrCreate(DATA);
    }
    
}
