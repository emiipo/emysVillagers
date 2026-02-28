package me.emii.emysvillagers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.util.Random;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;

public class HumanoidManager {

    private static final Random RANDOM = new Random();
    private String[] mascNames = {"Villager"};
    private String[] femNames = {"Villager"};

    public HumanoidManager() {
        loadNames();
    }

    public void onEntityJoin(Entity entity) {
        if (entity.level().isClientSide()) return;

        if (entity instanceof Villager villager && !villager.hasCustomName()) {
            villager.setCustomName(Component.literal(pickName()));
        }
    }

    private String pickName() {
        if (RANDOM.nextBoolean()) {
            return femNames[RANDOM.nextInt(femNames.length)];
        } else {
            return mascNames[RANDOM.nextInt(mascNames.length)];
        }
    }

    private void loadNames() {
        try (InputStream stream = HumanoidManager.class.getResourceAsStream("/data/emysvillagers/names.json")) {
            JsonObject json = new Gson().fromJson(new InputStreamReader(stream), JsonObject.class);
            femNames = new Gson().fromJson(json.getAsJsonArray("fem"), String[].class);
            mascNames = new Gson().fromJson(json.getAsJsonArray("masc"), String[].class);
        } catch (Exception e) {
            Log.error("Failed to load names :(");
        }
    }
}
