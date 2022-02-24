package fuzs.deathcompass.client;

import fuzs.deathcompass.client.renderer.item.DeathCompassPropertyFunction;
import fuzs.deathcompass.registry.ModRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.resources.ResourceLocation;

public class DeathCompassClient implements ClientModInitializer {
    public static void onClientSetup() {
        FabricModelPredicateProviderRegistry.register(ModRegistry.DEATH_COMPASS_ITEM, new ResourceLocation("angle"), new DeathCompassPropertyFunction());
    }

    @Override
    public void onInitializeClient() {
        onClientSetup();
    }
}
