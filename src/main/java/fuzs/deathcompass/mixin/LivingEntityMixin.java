package fuzs.deathcompass.mixin;

import fuzs.deathcompass.api.event.LivingDropsCallback;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "dropAllDeathLoot", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;shouldDropLoot()Z"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    protected void dropAllDeathLoot(DamageSource damageSource, CallbackInfo callbackInfo, boolean recentlyHit, Entity attacker, int lootingLevel) {
        if (!LivingDropsCallback.EVENT.invoker().onLivingDrops((LivingEntity) (Object) this, damageSource, lootingLevel, recentlyHit)) {
            this.dropEquipment();
            callbackInfo.cancel();
        }
    }

    @Shadow
    protected abstract void dropEquipment();
}
