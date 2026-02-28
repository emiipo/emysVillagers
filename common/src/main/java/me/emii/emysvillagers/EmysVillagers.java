package me.emii.emysvillagers;

import java.util.ServiceLoader;
import me.emii.emysvillagers.tags.ITagHelper;

public class EmysVillagers {

    public static final String MOD_ID = "emysvillagers";
    public static final String MOD_NAME = "emys Villagers";

    public static HumanoidManager HUMANOID_MANAGER = new HumanoidManager();
    public static final ITagHelper TAG_HELPER = load(ITagHelper.class);

    public static void init() {
        Log.info("Loaded :)");
    }

    private static <T> T load(Class<T> classToLoad) {
        final T loadedService = ServiceLoader.load(classToLoad)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + classToLoad.getName()));
        return loadedService;
    }

}