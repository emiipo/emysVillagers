package me.emii.emysvillagers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import me.emii.emysvillagers.accessor.HumanoidData;
import me.emii.emysvillagers.accessor.IHumanoidDataAccessor;

import java.io.InputStreamReader;
import java.io.InputStream;
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
    private String[] femSkins = {"emysvillagers:textures/entity/villager/fem/skin_1.png"};
    private String[] mascSkins = {"emysvillagers:textures/entity/villager/masc/skin_1.png"};

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
        loadSkins();
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
        if (!tag.contains(Constants.SKIN)) {
            BodyType type = getBodyType(tag.getByte(Constants.BODY_TYPE));
            tag.putString(Constants.SKIN, pickSkin(type));
        }

        ((IHumanoidDataAccessor)(Object)entity).emysvillagers$setData(new HumanoidData(getBodyType(tag.getByte(Constants.BODY_TYPE)), tag.getString(Constants.NAME), tag.getFloat(Constants.PITCH), tag.getString(Constants.SKIN)));
    }

    private String pickName(BodyType type) {
        if (type == BodyType.FEM) {
            return femNames[RANDOM.nextInt(femNames.length)];
        } else {
            return mascNames[RANDOM.nextInt(mascNames.length)];
        }
    }

    private String pickSkin(BodyType type) {
        if (type == BodyType.FEM) {
            return femSkins[RANDOM.nextInt(femSkins.length)];
        } else {
            return mascSkins[RANDOM.nextInt(mascSkins.length)];
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

    private void loadSkins() {
        try (InputStream stream = HumanoidManager.class.getResourceAsStream("/data/emysvillagers/fem_skins.json")) {
            JsonObject json = new Gson().fromJson(new InputStreamReader(stream), JsonObject.class);
            femSkins = new Gson().fromJson(json.getAsJsonArray("textures"), String[].class);
        } catch (Exception e) {
            Log.error("Failed to load fem skins :(");
        }
        try (InputStream stream = HumanoidManager.class.getResourceAsStream("/data/emysvillagers/masc_skins.json")) {
            JsonObject json = new Gson().fromJson(new InputStreamReader(stream), JsonObject.class);
            mascSkins = new Gson().fromJson(json.getAsJsonArray("textures"), String[].class);
        } catch (Exception e) {
            Log.error("Failed to load masc skins :(");
        }
    }
}
