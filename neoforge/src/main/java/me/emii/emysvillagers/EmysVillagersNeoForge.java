package me.emii.emysvillagers;


import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(EmysVillagers.MOD_ID)
public class EmysVillagersNeoForge {

    public EmysVillagersNeoForge(IEventBus eventBus) {

        EmysVillagers.init();

    }
}