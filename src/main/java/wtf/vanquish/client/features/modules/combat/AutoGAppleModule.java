package wtf.vanquish.client.features.modules.combat;

import lombok.Getter;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import wtf.vanquish.api.event.Listener;
import wtf.vanquish.api.event.EventListener;
import wtf.vanquish.api.event.events.player.other.UpdateEvent;
import wtf.vanquish.api.module.Category;
import wtf.vanquish.api.module.Module;
import wtf.vanquish.api.module.ModuleRegister;
import wtf.vanquish.api.module.setting.BooleanSetting;
import wtf.vanquish.api.module.setting.SliderSetting;

@ModuleRegister(name = "Auto GApple", category = Category.COMBAT)
public class AutoGAppleModule extends Module {
    @Getter private static final AutoGAppleModule instance = new AutoGAppleModule();

    private final SliderSetting health = new SliderSetting("Health").value(18f).range(4f, 20f).step(1f);
    private final BooleanSetting useEnchanted = new BooleanSetting("Use enchanted").value(true);
    // Настройка в секундах: от 0.1 до 5 секунд с шагом 0.1
    private final SliderSetting delaySeconds = new SliderSetting("Delay (sec)").value(0.5f).range(0f, 5f).step(0.1f);

    private boolean active;
    private long lastEatTime;

    public AutoGAppleModule() {
        addSettings(health, useEnchanted, delaySeconds);
    }

    @Override
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<>(event -> {
            boolean validItem = mc.player.getOffHandStack().getItem() == Items.GOLDEN_APPLE ||
                    (useEnchanted.getValue() && mc.player.getOffHandStack().getItem() == Items.ENCHANTED_GOLDEN_APPLE);

            // Проверка условий: низкое ХП и наличие яблока
            if (validItem && mc.player.getHealth() <= health.getValue()) {

                // Проверка задержки: переводим секунды из настройки в миллисекунды (* 1000)
                long requiredDelay = (long) (delaySeconds.getValue() * 1000L);

                if (System.currentTimeMillis() - lastEatTime >= requiredDelay) {
                    active = true;
                    if (!mc.player.isUsingItem()) {
                        mc.interactionManager.interactItem(mc.player, Hand.OFF_HAND);
                        KeyBinding.setKeyPressed(mc.options.useKey.getDefaultKey(), true);
                        mc.player.setCurrentHand(Hand.OFF_HAND);
                    }
                }
            } else if (active && mc.player.isUsingItem()) {
                // Когда ХП восстановилось или яблоко кончилось — сбрасываем состояние
                mc.interactionManager.stopUsingItem(mc.player);
                if (!(mc.mouse.wasRightButtonClicked() && mc.currentScreen == null)) {
                    KeyBinding.setKeyPressed(mc.options.useKey.getDefaultKey(), false);
                }
                active = false;
                // Запоминаем время, когда ПРЕКРАТИЛИ есть, чтобы задержка шла от последнего съеденного яблока
                lastEatTime = System.currentTimeMillis();
            }
        }));

        addEvents(updateEvent);
    }
}