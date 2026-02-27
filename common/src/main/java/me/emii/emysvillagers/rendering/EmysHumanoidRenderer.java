package me.emii.emysvillagers.rendering;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;

public class EmysHumanoidRenderer<T extends Mob> extends MobRenderer<T, EmysHumanoidModel<T>>{
    
    public static final ModelLayerLocation HUMANOID = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath("emysvillagers", "humanoid"), "main");

    public EmysHumanoidRenderer(EntityRendererProvider.Context context) {
        this(context, 0.5f);
    }

    public EmysHumanoidRenderer(EntityRendererProvider.Context context, float shadowRadius) {
        super(context, new EmysHumanoidModel<>(context.bakeLayer(HUMANOID)), shadowRadius);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return ResourceLocation.fromNamespaceAndPath("emysvillagers", "textures/entity/villager/default.png");
    }

}
