package me.emii.emysvillagers;

import me.emii.emysvillagers.rendering.EmysHumanoidModel;
import me.emii.emysvillagers.rendering.EmysHumanoidRenderer;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@Mod(EmysVillagers.MOD_ID)
public class EmysVillagersNeoForge {

    public EmysVillagersNeoForge(IEventBus eventBus) {

        eventBus.addListener(EmysVillagersNeoForge::onRegisterRenderers);
        eventBus.addListener(EmysVillagersNeoForge::onRegisterLayers);

        EmysVillagers.init();

    }

    @SubscribeEvent
    private static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityType.VILLAGER, EmysHumanoidRenderer::new);
    }

    @SubscribeEvent
    private static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(EmysHumanoidRenderer.HUMANOID, EmysHumanoidModel::createBodyLayer);
    }
}