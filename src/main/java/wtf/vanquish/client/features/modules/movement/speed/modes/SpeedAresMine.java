package wtf.vanquish.client.features.modules.movement.speed.modes;

import java.util.Iterator;
import java.util.function.Supplier;
import lombok.Generated;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import wtf.vanquish.api.module.setting.BooleanSetting;
import wtf.vanquish.api.module.setting.Setting;
import wtf.vanquish.api.module.setting.SliderSetting;
import wtf.vanquish.api.utils.player.MoveUtil;
import wtf.vanquish.client.features.modules.movement.speed.SpeedMode;

public class SpeedAresMine extends SpeedMode {
    private final SliderSetting boxExpand = (new SliderSetting("Box Expand")).value(0.5F).range(0.1F, 2.0F).step(0.1F);
    private final SliderSetting elytraBoxExpand = (new SliderSetting("Elytra Box Expand")).value(1.0F).range(0.1F, 3.0F).step(0.1F);
    private final BooleanSetting armorStands = (new BooleanSetting("Armor Stands")).value(true);
    private final SliderSetting setbackThreshold = (new SliderSetting("Setback Threshold")).value(1000.0F).range(500.0F, 5000.0F).step(100.0F);
    private long lastSetbackTime = 0L;
    private boolean isPostPhase = false;

    @Override
    public String getName() {
        return "AresMine";
    }

    public SpeedAresMine(Supplier<Boolean> condition) {
        this.boxExpand.setVisible(condition);
        this.elytraBoxExpand.setVisible(condition);
        this.armorStands.setVisible(condition);
        this.setbackThreshold.setVisible(condition);
        this.addSettings(new Setting[]{this.boxExpand, this.elytraBoxExpand, this.armorStands, this.setbackThreshold});
    }

    @Override
    public void onUpdate() {
        this.isPostPhase = !this.isPostPhase;

        if (Math.random() < 0.01D) {
            this.lastSetbackTime = System.currentTimeMillis();
        }
    }

    @Override
    public void onTravel() {
        if (this.isPostPhase && (float)(System.currentTimeMillis() - this.lastSetbackTime) > (Float)this.setbackThreshold.getValue() && MoveUtil.isMoving()) {
            int collisions = 0;

            float currentBoxExpand = mc.player.isGliding() ? (Float)this.elytraBoxExpand.getValue() : (Float)this.boxExpand.getValue();

            Iterator var3 = mc.world.getEntities().iterator();

            label47:
            while(true) {
                Entity entity;
                do {
                    do {
                        do {
                            if (!var3.hasNext()) {
                                if (collisions > 0) {
                                    double[] motion = MoveUtil.forward(0.08D * (double)collisions);
                                    mc.player.addVelocity(motion[0], 0.0D, motion[1]);
                                }
                                break label47;
                            }

                            entity = (Entity)var3.next();
                        } while(entity == mc.player);
                    } while(entity instanceof ArmorStandEntity && !(Boolean)this.armorStands.getValue());
                } while(!(entity instanceof LivingEntity) && !(entity instanceof AbstractMinecartEntity));

                if (mc.player.getBoundingBox().expand((double)currentBoxExpand).intersects(entity.getBoundingBox())) {
                    ++collisions;
                }
            }
        }

        this.applyTargetSuction();
    }

    private void applyTargetSuction() {
    }

    @Generated
    public SliderSetting getBoxExpand() { return this.boxExpand; }
    @Generated
    public SliderSetting getElytraBoxExpand() { return this.elytraBoxExpand; }
    @Generated
    public BooleanSetting getArmorStands() { return this.armorStands; }
    @Generated
    public SliderSetting getSetbackThreshold() { return this.setbackThreshold; }
}