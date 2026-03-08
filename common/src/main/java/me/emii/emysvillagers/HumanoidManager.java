package me.emii.emysvillagers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import me.emii.emysvillagers.accessor.HumanoidData;
import me.emii.emysvillagers.accessor.IHumanoidDataAccessor;

import java.io.InputStreamReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public class HumanoidManager {

    public static enum BodyType { FEM, MASC}
    private static final Random RANDOM = new Random();

    private String[] femNames = {"Villager"};
    private String[] mascNames = {"Villager"};

    private ResourceLocation[] bases;
    private ResourceLocation[] femFaces;
    private ResourceLocation[] femHairs;
    private ResourceLocation[] femLegs;
    private ResourceLocation[] femTops;
    private ResourceLocation[] mascFaces;
    private ResourceLocation[] mascHairs;
    private ResourceLocation[] mascLegs;
    private ResourceLocation[] mascTops;

    public static final SoundEvent FEM_AMBIENT_SOUNDS = SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(EmysVillagers.MOD_ID, "entity.villager.fem.ambient"));
    public static final SoundEvent MASC_AMBIENT_SOUNDS = SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(EmysVillagers.MOD_ID, "entity.villager.masc.ambient"));
    public static final SoundEvent FEM_TRADE_SOUNDS = SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(EmysVillagers.MOD_ID, "entity.villager.fem.trade"));
    public static final SoundEvent MASC_TRADE_SOUNDS = SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(EmysVillagers.MOD_ID, "entity.villager.masc.trade"));
    public static final SoundEvent FEM_DEATH_SOUNDS = SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(EmysVillagers.MOD_ID, "entity.villager.fem.death"));
    public static final SoundEvent MASC_DEATH_SOUNDS = SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(EmysVillagers.MOD_ID, "entity.villager.masc.death"));
    public static final SoundEvent FEM_HURT_SOUNDS = SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(EmysVillagers.MOD_ID, "entity.villager.fem.hurt"));
    public static final SoundEvent MASC_HURT_SOUNDS = SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(EmysVillagers.MOD_ID, "entity.villager.masc.hurt"));
    public static final SoundEvent FEM_YES_SOUNDS = SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(EmysVillagers.MOD_ID, "entity.villager.fem.yes"));
    public static final SoundEvent MASC_YES_SOUNDS = SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(EmysVillagers.MOD_ID, "entity.villager.masc.yes"));
    public static final SoundEvent FEM_NO_SOUNDS = SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(EmysVillagers.MOD_ID, "entity.villager.fem.no"));
    public static final SoundEvent MASC_NO_SOUNDS = SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(EmysVillagers.MOD_ID, "entity.villager.masc.no"));

    public HumanoidManager() {
        loadNames();
        loadTextures();
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
            tag.putString(Constants.NAME, pickName(getBodyType(tag.getByte(Constants.BODY_TYPE))));
        }
        if (!tag.contains(Constants.PITCH)) {
            tag.putFloat(Constants.PITCH, RANDOM.nextFloat(-0.10f, 0.20f));
        }

        ((IHumanoidDataAccessor)(Object)entity).emysvillagers$setData(new HumanoidData(getBodyType(tag.getByte(Constants.BODY_TYPE)), tag.getString(Constants.NAME), tag.getFloat(Constants.PITCH)));
    }

    private String pickName(BodyType type) {
        if (type == BodyType.FEM) {
            return femNames[RANDOM.nextInt(femNames.length)];
        } else {
            return mascNames[RANDOM.nextInt(mascNames.length)];
        }
    }

    public static BodyType getBodyType(int val) {
        try {
            return BodyType.values()[val];
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

    private void loadTextures() {
        try (InputStream stream = HumanoidManager.class.getResourceAsStream("/data/emysvillagers/textures.json")) {
            JsonObject json = new Gson().fromJson(new InputStreamReader(stream), JsonObject.class);

            JsonObject temp;
            int defaultWeight;

            // Bases
            temp = json.getAsJsonObject("bases");
            defaultWeight = temp.has("default_weight") ? temp.get("default_weight").getAsInt() : 1;
            bases = parseTextures(temp.getAsJsonArray("textures"), defaultWeight);

            // Fem Faces
            temp = json.getAsJsonObject("fem_faces");
            defaultWeight = temp.has("default_weight") ? temp.get("default_weight").getAsInt() : 1;
            femFaces = parseTextures(temp.getAsJsonArray("textures"), defaultWeight);

            // Fem Hairs
            temp = json.getAsJsonObject("fem_hair");
            defaultWeight = temp.has("default_weight") ? temp.get("default_weight").getAsInt() : 1;
            femHairs = parseTextures(temp.getAsJsonArray("textures"), defaultWeight);

            // Fem Legs
            temp = json.getAsJsonObject("fem_legs");
            defaultWeight = temp.has("default_weight") ? temp.get("default_weight").getAsInt() : 1;
            femLegs = parseTextures(temp.getAsJsonArray("textures"), defaultWeight);

            // Fem Tops
            temp = json.getAsJsonObject("fem_tops");
            defaultWeight = temp.has("default_weight") ? temp.get("default_weight").getAsInt() : 1;
            femTops = parseTextures(temp.getAsJsonArray("textures"), defaultWeight);

            // Masc Faces
            temp = json.getAsJsonObject("masc_faces");
            defaultWeight = temp.has("default_weight") ? temp.get("default_weight").getAsInt() : 1;
            mascFaces = parseTextures(temp.getAsJsonArray("textures"), defaultWeight);

            // Masc Hairs
            temp = json.getAsJsonObject("masc_hair");
            defaultWeight = temp.has("default_weight") ? temp.get("default_weight").getAsInt() : 1;
            mascHairs = parseTextures(temp.getAsJsonArray("textures"), defaultWeight);

            // Masc Legs
            temp = json.getAsJsonObject("masc_legs");
            defaultWeight = temp.has("default_weight") ? temp.get("default_weight").getAsInt() : 1;
            mascLegs = parseTextures(temp.getAsJsonArray("textures"), defaultWeight);

            // Masc Tops
            temp = json.getAsJsonObject("masc_tops");
            defaultWeight = temp.has("default_weight") ? temp.get("default_weight").getAsInt() : 1;
            mascTops = parseTextures(temp.getAsJsonArray("textures"), defaultWeight);

        } catch (Exception e) {
            Log.error("Failed to load textures :(");
        }
    }

    private ResourceLocation[] parseTextures(JsonArray arr, int defaultWeight) {
        List<ResourceLocation> list = new ArrayList<>();
        for (JsonElement el : arr) {
            String path;
            int weight = defaultWeight;

            if (el.isJsonObject()) {
                path = el.getAsJsonObject().get("texture").getAsString();
                weight = el.getAsJsonObject().has("weight") ? el.getAsJsonObject().get("weight").getAsInt() : defaultWeight;
            } else {
                path = el.getAsString();
            }

            String[] parts = path.split(":");
            ResourceLocation location = ResourceLocation.fromNamespaceAndPath(parts[0], parts[1]);
            for (int i = 0; i < weight; i++) list.add(location);
        }
        return list.toArray(new ResourceLocation[0]);
    }
}
