package me.emii.emysvillagers;

import me.emii.emysvillagers.rendering.EmysHumanoidModel;
import me.emii.emysvillagers.rendering.EmysHumanoidRenderer;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

@Mod(EmysVillagers.MOD_ID)
public class EmysVillagersNeoForge {

    public EmysVillagersNeoForge(IEventBus eventBus) {
        eventBus.addListener(EmysVillagersNeoForge::onRegisterRenderers);
        eventBus.addListener(EmysVillagersNeoForge::onRegisterLayers);

        NeoForge.EVENT_BUS.addListener(EmysVillagersNeoForge::onEntityJoin);

        EmysVillagers.init();
    }

    private static void onEntityJoin(EntityJoinLevelEvent event) {
        EmysVillagers.HUMANOID_MANAGER.onEntityJoin(event.getEntity());
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