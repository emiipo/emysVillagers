package me.emii.emysvillagers;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(EmysVillagers.MOD_ID)
public class EmysVillagersForge {

    public EmysVillagersForge() {
        MinecraftForge.EVENT_BUS.addListener(EmysVillagersForge::onEntityJoin);

        EmysVillagers.init();
    }

    private static void onEntityJoin(EntityJoinLevelEvent event) {
        EmysVillagers.HUMANOID_MANAGER.onEntityJoin(event.getEntity());
    }
}