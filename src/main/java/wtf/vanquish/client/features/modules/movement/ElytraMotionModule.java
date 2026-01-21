package wtf.vanquish.client.features.modules.movement;

import lombok.Getter;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.util.math.Vec3d;
import wtf.vanquish.api.event.Listener;
import wtf.vanquish.api.event.EventListener;
import wtf.vanquish.api.event.events.player.move.MoveEvent;
import wtf.vanquish.api.module.Category;
import wtf.vanquish.api.module.Module;
import wtf.vanquish.api.module.ModuleRegister;
import wtf.vanquish.api.module.setting.BooleanSetting;
import wtf.vanquish.api.module.setting.SliderSetting;
import wtf.vanquish.api.utils.player.InventoryUtil;
import wtf.vanquish.api.utils.other.SlownessManager;
import wtf.vanquish.api.utils.rotation.RotationUtil;
import wtf.vanquish.client.features.modules.combat.AuraModule;
import wtf.vanquish.client.features.modules.combat.elytratarget.ElytraTargetModule;
import wtf.vanquish.client.features.modules.player.ElytraSwapModule;

@ModuleRegister(name = "Elytra Motion", category = Category.MOVEMENT)
public class ElytraMotionModule extends Module {
    @Getter private static final ElytraMotionModule instance = new ElytraMotionModule();

    private final SliderSetting distance = new SliderSetting("Distance").value(3f).range(0.1f, 6f).step(0.1f);
    private final BooleanSetting swapChestplate = new BooleanSetting("Swap chestplate").value(true);
    private final BooleanSetting useFirework = new BooleanSetting("Use firework").value(true).setVisible(swapChestplate::getValue);
    private final BooleanSetting legit = new BooleanSetting("Legit").value(false).setVisible(() -> swapChestplate.getValue() && useFirework.getValue());

    private boolean swappedToChestplate = false;

    private final InventoryUtil.ItemUsage itemUsage = new InventoryUtil.ItemUsage(Items.FIREWORK_ROCKET, this);

    public ElytraMotionModule() {
        addSettings(distance, swapChestplate, useFirework, legit);
        itemUsage.setUseRotation(false);
    }

    @Override
    public void onEvent() {
        EventListener moveEvent = MoveEvent.getInstance().subscribe(new Listener<>(0, event -> {
            AuraModule aura = AuraModule.getInstance();
            LivingEntity target = aura.target;

            if (!aura.isEnabled() || target == null) return;

            Vec3d targetPos = RotationUtil.getSpot(target);
            ElytraTargetModule elytraTarget = ElytraTargetModule.getInstance();

            if (elytraTarget.isEnabled() && elytraTarget.elytraRotationProcessor.using()) {
                targetPos = elytraTarget.elytraRotationProcessor.getPredictedPos(target);
            }

            float targetDistance = (float) targetPos.distanceTo(mc.player.getEyePos());

            if (targetDistance < distance.getValue() && (swappedToChestplate || mc.player.isGliding())) {
                if (InventoryUtil.hasElytraEquipped() && swapChestplate.getValue()) {
                    swapToChestplate();
                    swappedToChestplate = true;
                }
                event.set(Vec3d.ZERO);
                MoveEvent.getInstance().setCancel(true);
            } else if (swappedToChestplate) {
                swapToElytra();
                swappedToChestplate = false;
            }
        }));

        addEvents(moveEvent);
    }

    private void fullySwap() {
        if (SlownessManager.isEnabled()) {
            SlownessManager.applySlowness(10, () -> ElytraSwapModule.getInstance().swapChestplate());
        } else ElytraSwapModule.getInstance().swapChestplate();
    }

    private void swapToChestplate() {
        if (!InventoryUtil.hasElytraEquipped() || ElytraSwapModule.getInstance().findChestplateSlot() == -1) return;

        fullySwap();
    }

    private void swapToElytra() {
        if (InventoryUtil.hasElytraEquipped() || ElytraSwapModule.getInstance().findElytraSlot() == -1) return;

        fullySwap();

        if (!mc.player.isGliding()) {
            mc.player.startGliding();
            mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));

            if (useFirework.getValue()) itemUsage.handleUse(legit.getValue());
        }
    }
}
