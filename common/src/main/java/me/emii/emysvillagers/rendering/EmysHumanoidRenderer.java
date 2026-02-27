package me.emii.emysvillagers.rendering;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.network.chat.Component;
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
    protected void renderNameTag(T entity, Component displayName, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, float partialTick) {
        Component customName = Component.literal("qtepie");
        super.renderNameTag(entity, customName, poseStack, bufferSource, packedLight, partialTick);
    }

    @Override                                                                                            
    protected boolean shouldShowName(T entity) {
        return true;                                                                                     
    }   

    @Override
    protected void scale(T entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(0.9375f, 0.9375f, 0.9375f);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return ResourceLocation.fromNamespaceAndPath("emysvillagers", "textures/entity/villager/default.png");
    }

}
