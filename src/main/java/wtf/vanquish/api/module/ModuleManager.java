package wtf.vanquish.api.module;

import lombok.Getter;
import wtf.vanquish.client.features.modules.combat.*;
import wtf.vanquish.client.features.modules.combat.elytratarget.ElytraTargetModule;
import wtf.vanquish.client.features.modules.movement.*;
import wtf.vanquish.client.features.modules.movement.fly.FlightModule;
import wtf.vanquish.client.features.modules.movement.nitrofirework.NitroFireworkModule;
import wtf.vanquish.client.features.modules.movement.noslow.NoSlowModule;
import wtf.vanquish.client.features.modules.movement.speed.SpeedModule;
import wtf.vanquish.client.features.modules.movement.spider.SpiderModule;
import wtf.vanquish.client.features.modules.other.*;
import wtf.vanquish.client.features.modules.player.*;
import wtf.vanquish.client.features.modules.render.*;
import wtf.vanquish.client.features.modules.render.motionblur.MotionBlurModule;
import wtf.vanquish.client.features.modules.render.nametags.NameTagsModule;
import wtf.vanquish.client.features.modules.render.particles.ParticlesModule;
import wtf.vanquish.client.features.modules.render.targetesp.TargetEspModule;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ModuleManager {
    @Getter private final static ModuleManager instance = new ModuleManager();

    private final List<Module> modules = new ArrayList<>();

    public void load() {
        register(
                SprintModule.getInstance(),
                ClickGUIModule.getInstance(),
                AmbienceModule.getInstance(),
                StrafeModule.getInstance(),
                SpeedModule.getInstance(),
                NoJumpDelayModule.getInstance(),
                AuraModule.getInstance(),
                MoveFixModule.getInstance(),
                AutoRespawnModule.getInstance(),
                InterfaceModule.getInstance(),
                AutoLeaveModule.getInstance(),
                NameTagsModule.getInstance(),
                AutoToolModule.getInstance(),
                TPAcceptModule.getInstance(),
                VelocityModule.getInstance(),
                NoSlowModule.getInstance(),
                AutoTotemModule.getInstance(),
                AutoGAppleModule.getInstance(),
                InventoryMoveModule.getInstance(),
                ItemSwapModule.getInstance(),
                RemovalsModule.getInstance(),
                ClickPearlModule.getInstance(),
                ElytraSwapModule.getInstance(),
                ElytraMotionModule.getInstance(),
                NitroFireworkModule.getInstance(),
                SwingAnimationModule.getInstance(),
                ViewModelModule.getInstance(),
                AutoBuffModule.getInstance(),
                JoinerModule.getInstance(),
                CameraClipModule.getInstance(),
                PointersModule.getInstance(),
                MouseTweaksModule.getInstance(),
                NoDesyncModule.getInstance(),
                TimerModule.getInstance(),
                AssistantModule.getInstance(),
                ElytraTargetModule.getInstance(),
                AutoExplosionModule.getInstance(),
                NoWebModule.getInstance(),
                NoClipModule.getInstance(),
                PredictionsModule.getInstance(),
                NoFriendHurtModule.getInstance(),
                NoEntityTraceModule.getInstance(),
                ParticlesModule.getInstance(),
                RecorderModule.getInstance(),
                HitboxDesyncModule.getInstance(),
                FastBreakModule.getInstance(),
                TapeMouseModule.getInstance(),
                SeeInvisiblesModule.getInstance(),
                HealthResolverModule.getInstance(),
                StreamerModule.getInstance(),
                JumpCircleModule.getInstance(),
                ToggleSoundsModule.getInstance(),
                MotionBlurModule.getInstance(),
                FakeLagModule.getInstance(),
                NightVisionModule.getInstance(),
                NoServerPackModule.getInstance(),
                AuctionHelperModule.getInstance(),
                TrailsModule.getInstance(),
                TargetEspModule.getInstance(),
                SpiderModule.getInstance(),
                FlightModule.getInstance()
        );

        modules.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
    }

    public void register(Module... modules) {
        this.modules.addAll(List.of(modules));
    }
}