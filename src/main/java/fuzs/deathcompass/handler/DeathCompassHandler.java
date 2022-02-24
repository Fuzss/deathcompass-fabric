package fuzs.deathcompass.handler;

import fuzs.deathcompass.DeathCompass;
import fuzs.deathcompass.capability.DeathTrackerCapability;
import fuzs.deathcompass.registry.ModRegistry;
import fuzs.deathcompass.world.item.DeathCompassItem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;

import java.util.Optional;

public class DeathCompassHandler {
    public void onLivingDrops(LivingEntity entity, DamageSource source, int lootingLevel, boolean recentlyHit) {
        if (entity instanceof ServerPlayer player) {
            Optional<DeathTrackerCapability> optional = ModRegistry.DEATH_TRACKER_CAPABILITY.maybeGet(player);
            if (optional.isPresent()) {
                if (!player.getInventory().isEmpty() || !DeathCompass.CONFIG.server().onlyOnItemsLost) {
                    DeathTrackerCapability.saveLastDeathData(optional.orElseThrow(IllegalStateException::new), player.blockPosition(), player.level.dimension());
                } else {
                    DeathTrackerCapability.clearLastDeathData(optional.orElseThrow(IllegalStateException::new));
                }
            }
        }
    }

    public void onPlayerClone(ServerPlayer oldPlayer, ServerPlayer newPlayer, boolean alive) {
        if (alive) return;
        if (ModRegistry.DEATH_TRACKER_CAPABILITY.maybeGet(oldPlayer).map(DeathTrackerCapability::hasLastDeathData).orElse(false)) {
            ServerLevel world = oldPlayer.getLevel();
            if (DeathCompass.CONFIG.server().ignoreKeepInventory || !world.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
                if (!DeathCompass.CONFIG.server().survivalPlayersOnly || !oldPlayer.isCreative() && !oldPlayer.isSpectator()) {
                    final Optional<ItemStack> deathCompass = DeathCompassItem.createDeathCompass(oldPlayer);
                    deathCompass.ifPresent(itemStack -> newPlayer.getInventory().add(itemStack));
                }
            }
        }
    }
}
