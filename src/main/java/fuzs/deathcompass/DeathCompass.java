package fuzs.deathcompass;

import fuzs.deathcompass.api.event.LivingDropsCallback;
import fuzs.deathcompass.config.ServerConfig;
import fuzs.deathcompass.handler.DeathCompassHandler;
import fuzs.deathcompass.registry.ModRegistry;
import fuzs.puzzleslib.config.AbstractConfig;
import fuzs.puzzleslib.config.ConfigHolder;
import fuzs.puzzleslib.config.ConfigHolderImpl;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeathCompass implements ModInitializer {
    public static final String MOD_ID = "deathcompass";
    public static final String MOD_NAME = "Death Compass";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    @SuppressWarnings("Convert2MethodRef")
    public static final ConfigHolder<AbstractConfig, ServerConfig> CONFIG = ConfigHolder.server(() -> new ServerConfig());

    public static void onConstructMod() {
        ((ConfigHolderImpl<?, ?>) CONFIG).addConfigs(MOD_ID);
        ModRegistry.touch();
        registerHandlers();
    }

    private static void registerHandlers() {
        final DeathCompassHandler handler = new DeathCompassHandler();
        LivingDropsCallback.EVENT.register((entity, source, lootingLevel, recentlyHit) -> {
            handler.onLivingDrops(entity, source, lootingLevel, recentlyHit);
            return true;
        });
        ServerPlayerEvents.COPY_FROM.register(handler::onPlayerClone);
    }

    @Override
    public void onInitialize() {
        onConstructMod();
    }
}
