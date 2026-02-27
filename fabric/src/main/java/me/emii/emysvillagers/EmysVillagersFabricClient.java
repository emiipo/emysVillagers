package me.emii.emysvillagers;

import me.emii.emysvillagers.rendering.EmysHumanoidModel;
import me.emii.emysvillagers.rendering.EmysHumanoidRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.world.entity.EntityType;

public class EmysVillagersFabricClient implements ClientModInitializer {
    
    @Override
    public void onInitializeClient() {
        
        EntityRendererRegistry.register(EntityType.VILLAGER, EmysHumanoidRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(EmysHumanoidRenderer.HUMANOID, EmysHumanoidModel::createBodyLayer);

    }
}
