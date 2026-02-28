package me.emii.emysvillagers;

import me.emii.emysvillagers.HumanoidManager.BodyType;
import me.emii.emysvillagers.rendering.EmysHumanoidModel;
import me.emii.emysvillagers.rendering.EmysHumanoidRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.api.distmarker.Dist;

@EventBusSubscriber(modid = EmysVillagers.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class EmysVillagersForgeClient {

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityType.VILLAGER, EmysHumanoidRenderer::new);
    }

    @SubscribeEvent
    public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(EmysHumanoidRenderer.HUMANOID_FEM, () -> EmysHumanoidModel.createBodyLayer(BodyType.FEM));
        event.registerLayerDefinition(EmysHumanoidRenderer.HUMANOID_MASC, () -> EmysHumanoidModel.createBodyLayer(BodyType.MASC));
    }

}
