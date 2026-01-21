package wtf.vanquish.client.features.modules.movement.speed.modes;

import java.util.function.Supplier;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import wtf.vanquish.api.utils.player.MoveUtil;
import wtf.vanquish.client.features.modules.movement.speed.SpeedMode;

public class SpeedMetahvh extends SpeedMode {
    private float melonBallSpeed = 0.36F;

    @Override
    public String getName() {
        return "Metahvh";
    }

    public SpeedMetahvh(Supplier<Boolean> condition) {
        // Конструктор
    }

    @Override
    public void onUpdate() {
        if (mc.player != null) {
            // Получаем предмет в левой руке
            ItemStack offHandItem = mc.player.getOffHandStack();

            // Получаем эффекты скорости и замедления
            StatusEffectInstance speedEffect = mc.player.getStatusEffect(StatusEffects.SPEED);
            StatusEffectInstance slownessEffect = mc.player.getStatusEffect(StatusEffects.SLOWNESS);

            // Получаем название предмета
            String itemName = offHandItem.getName().getString();
            float appliedSpeed = 0.0F;

            if (speedEffect != null) {
                // Если Скорость II (amplifier 1 в коде равен II уровню в игре, но тут проверка на 2, значит III или специфичный стак)
                if (speedEffect.getAmplifier() == 2) {
                    if (itemName.contains("Ломтик Дыни")) {
                        appliedSpeed = 0.41755F;
                    } else {
                        appliedSpeed = this.melonBallSpeed * 1.155F;
                    }
                } else if (speedEffect.getAmplifier() == 1) { // Скорость II
                    appliedSpeed = this.melonBallSpeed;
                }
            } else {
                // Если эффекта скорости нет
                appliedSpeed = this.melonBallSpeed * 0.68F;
            }

            // Если есть замедление
            if (slownessEffect != null) {
                appliedSpeed *= 0.835F;
            }

            // Если игрок в воздухе (!onGround)
            if (!mc.player.isOnGround()) {
                appliedSpeed *= 1.435F;
            }

            // Установка скорости в MoveUtil
            MoveUtil.setSpeed((double) appliedSpeed);
        }
    }
}