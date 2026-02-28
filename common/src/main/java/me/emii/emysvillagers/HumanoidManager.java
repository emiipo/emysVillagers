package me.emii.emysvillagers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import me.emii.emysvillagers.accessor.HumanoidData;
import me.emii.emysvillagers.accessor.IHumanoidDataAccessor;

import java.io.InputStreamReader;
import java.io.InputStream;
import java.util.Random;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public class HumanoidManager {

    public static enum BodyType { FEM, MASC}
    private static final Random RANDOM = new Random();

    private String[] mascNames = {"Villager"};
    private String[] femNames = {"Villager"};

    public HumanoidManager() {
        loadNames();
    }

    public void onEntityJoin(Entity entity) {
        if (entity.level().isClientSide()) return;
        if(entity.getType() != EntityType.VILLAGER/* FUTURE PROOFING: && entity.getType() != EntityType.WANDERING_TRADER && entity.getType() != EntityType.ZOMBIE_VILLAGER && entity.getType() != EntityType.WITCH && entity.getType() != EntityType.PILLAGER && entity.getType() != EntityType.EVOKER && entity.getType() != EntityType.VINDICATOR*/) return;

        CompoundTag tag = EmysVillagers.TAG_HELPER.getCompoundTag(entity);

        if (!tag.contains(Constants.BODY_TYPE)) {
            BodyType[] values = BodyType.values();
            BodyType type = values[RANDOM.nextInt(values.length)];
            tag.putByte(Constants.BODY_TYPE, (byte)type.ordinal());
        }
        if (!tag.contains(Constants.NAME)) {
            tag.putString(Constants.NAME, pickName(getBodyType(tag)));
        }

        ((IHumanoidDataAccessor)(Object)entity).emysvillagers$setData(new HumanoidData(getBodyType(tag), tag.getString(Constants.NAME)));
    }

    private String pickName(BodyType type) {
        if (type == BodyType.FEM) {
            return femNames[RANDOM.nextInt(femNames.length)];
        } else {
            return mascNames[RANDOM.nextInt(mascNames.length)];
        }
    }

    private BodyType getBodyType(CompoundTag tag) {
        try {
            return BodyType.values()[tag.getByte(Constants.BODY_TYPE)];
        } catch (ArrayIndexOutOfBoundsException e) {
            Log.warn(e.toString());
            return BodyType.FEM; // Default to this for now, maybe do NONE later
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
