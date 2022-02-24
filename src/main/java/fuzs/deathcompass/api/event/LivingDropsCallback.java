package fuzs.deathcompass.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public interface LivingDropsCallback {
    Event<LivingDropsCallback> EVENT = EventFactory.createArrayBacked(LivingDropsCallback.class, callbacks -> (LivingEntity entity, DamageSource source, int lootingLevel, boolean recentlyHit) -> {
        for (LivingDropsCallback callback : callbacks) {
            if (!callback.onLivingDrops(entity, source, lootingLevel, recentlyHit)) {
                return false;
            }
        }
        return true;
    });

    /**
     * called before any loot from an entity is dropped
     * @param entity the entity dropping the loot
     * @param source the damage source that killed <code>entity</code>
     * @param lootingLevel level of looting on the killing weapon
     * @param recentlyHit access to lastHurtByPlayerTime field
     * @return false to cancel any drops (including xp) just like disabling doMobLoot gamerule, i.e. saddles from pigs and player inventory will still be dropped
     */
    boolean onLivingDrops(LivingEntity entity, DamageSource source, int lootingLevel, boolean recentlyHit);
}
