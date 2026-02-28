package me.emii.emysvillagers;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;

public class EmysVillagersFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            EmysVillagers.HUMANOID_MANAGER.onEntityJoin(entity);
        }); 

        EmysVillagers.init();
    }
}
