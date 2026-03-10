package me.emii.emysvillagers.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import me.emii.emysvillagers.EmysVillagers;
import me.emii.emysvillagers.accessor.HumanoidData;
import me.emii.emysvillagers.accessor.IHumanoidDataAccessor;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.npc.Villager;

public class EmysHumanoidRenderer<T extends Mob> extends MobRenderer<T, EmysHumanoidModel<T>>{
    
    private final EmysHumanoidModel<T>[] models;
    public static final ModelLayerLocation HUMANOID_FEM = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(EmysVillagers.MOD_ID, "humanoid_fem"), "main");
    public static final ModelLayerLocation HUMANOID_MASC = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(EmysVillagers.MOD_ID, "humanoid_masc"), "main");

    public EmysHumanoidRenderer(EntityRendererProvider.Context context) {
        this(context, 0.5f);
    }

    public EmysHumanoidRenderer(EntityRendererProvider.Context context, float shadowRadius) {
        super(context, new EmysHumanoidModel<>(context.bakeLayer(HUMANOID_FEM)), shadowRadius);
        models = new EmysHumanoidModel[]{
            new EmysHumanoidModel<>(context.bakeLayer(HUMANOID_FEM)),
            new EmysHumanoidModel<>(context.bakeLayer(HUMANOID_MASC))
        };
    }

    @Override
    public void render(T entity, float yaw, float delta, PoseStack poseStack, MultiBufferSource buffers, int light) {
        HumanoidData data = ((IHumanoidDataAccessor)(Object)entity).emysvillagers$getData();
        this.model = models[data.bodyType().ordinal()];
        super.render(entity, yaw, delta, poseStack, buffers, light);
    }

    @Override
    protected void renderNameTag(T entity, Component name, PoseStack poseStack, MultiBufferSource buffers, int packedLight, float partialTick) {
        Component displayName = entity.hasCustomName() ? entity.getCustomName() : Component.literal(((IHumanoidDataAccessor)entity).emysvillagers$getData().name());

        MutableComponent finalName = displayName.copy();

        if (entity instanceof Villager villager) {
            ResourceLocation profKey = BuiltInRegistries.VILLAGER_PROFESSION.getKey(villager.getVillagerData().getProfession());
            if (profKey != null && !profKey.getPath().equals("none")) {
                finalName = Component.translatable("entity.minecraft.villager." + profKey.getPath()).append(" ").append(displayName.copy());
            }
        }

        super.renderNameTag(entity, finalName, poseStack, buffers, packedLight, partialTick);
    }

    @Override                                                                                            
    protected boolean shouldShowName(T entity) {
        return this.entityRenderDispatcher.distanceToSqr(entity) < 100.0;                                                                                  
    }   

    @Override
    protected void scale(T entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(0.9375f, 0.9375f, 0.9375f);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return ResourceLocation.fromNamespaceAndPath(EmysVillagers.MOD_ID, "textures/entity/villager/default.png");
    }

}
