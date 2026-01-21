package wtf.vanquish.client.features.modules.movement.speed.modes;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.math.Vec3d;
import wtf.vanquish.api.module.setting.BooleanSetting;
import wtf.vanquish.api.module.setting.Setting;
import wtf.vanquish.api.module.setting.SliderSetting;
import wtf.vanquish.api.utils.player.MoveUtil;
import wtf.vanquish.client.features.modules.movement.speed.SpeedMode;

import java.util.function.Supplier;

public class SpeedHollyWorld extends SpeedMode {

    private final SliderSetting speed = new SliderSetting("Speed").value(8.0f).range(1.0f, 20.0f).step(0.1f);
    private final SliderSetting range = new SliderSetting("Range").value(3.0f).range(0.5f, 10.0f).step(0.1f);
    private final SliderSetting expand = new SliderSetting("Expand").value(0.5f).range(0.1f, 2.0f).step(0.1f);
    private final BooleanSetting onlyPlayers = new BooleanSetting("Only Players").value(true);
    private final BooleanSetting armorStands = new BooleanSetting("Armor Stands").value(false);
    private final BooleanSetting requireMoving = new BooleanSetting("Require Moving").value(true);

    private boolean isPostPhase = false;

    public SpeedHollyWorld(Supplier<Boolean> condition) {
        speed.setVisible(condition);
        range.setVisible(condition);
        expand.setVisible(condition);
        onlyPlayers.setVisible(condition);
        armorStands.setVisible(condition);
        requireMoving.setVisible(condition);

        addSettings(new Setting[]{speed, range, expand, onlyPlayers, armorStands, requireMoving});
    }

    @Override
    public String getName() {
        return "HollyWorld";
    }

    @Override
    public void onUpdate() {
        this.isPostPhase = !this.isPostPhase;
    }

    @Override
    public void onTravel() {
        if (mc.player == null || mc.world == null) return;
        if (!isPostPhase) return;

        if ((Boolean) requireMoving.getValue() && !MoveUtil.isMoving()) return;

        int collisions = 0;
        double expandVal = (Float) expand.getValue();

        for (Entity ent : mc.world.getEntities()) {
            if (ent == mc.player) continue;

            if (ent instanceof ArmorStandEntity && !(Boolean) armorStands.getValue()) continue;
            if ((Boolean) onlyPlayers.getValue() && !(ent instanceof PlayerEntity)) continue;

            if (ent instanceof LivingEntity || ent instanceof BoatEntity) {
                if (mc.player.getBoundingBox().expand(expandVal).intersects(ent.getBoundingBox())) {
                    collisions++;
                }
            }
        }

        if (collisions <= 0) return;

        double finalBoost = (Float) speed.getValue() * 0.01D * collisions;

        Entity target = null;
        double bestDistSq = Double.MAX_VALUE;
        double maxRangeSq = Math.pow((Float) range.getValue(), 2);

        for (Entity ent : mc.world.getEntities()) {
            if (ent == mc.player) continue;
            if (ent instanceof ArmorStandEntity && !(Boolean) armorStands.getValue()) continue;
            if ((Boolean) onlyPlayers.getValue() && !(ent instanceof PlayerEntity)) continue;
            if (!(ent instanceof LivingEntity) && !(ent instanceof BoatEntity)) continue;

            double distSq = mc.player.squaredDistanceTo(ent);
            if (distSq <= maxRangeSq && distSq < bestDistSq) {
                bestDistSq = distSq;
                target = ent;
            }
        }

        if (target != null) {
            double[] dir = getDirectionToPoint(mc.player.getPos(), target.getPos(), finalBoost);
            mc.player.addVelocity(dir[0], 0.0, dir[1]);
        }
    }

    private double[] getDirectionToPoint(Vec3d from, Vec3d to, double spd) {
        double dx = to.x - from.x;
        double dz = to.z - from.z;
        double len = Math.sqrt(dx * dx + dz * dz);
        if (len < 0.001) return new double[]{0.0, 0.0};
        return new double[]{dx / len * spd, dz / len * spd};
    }
}