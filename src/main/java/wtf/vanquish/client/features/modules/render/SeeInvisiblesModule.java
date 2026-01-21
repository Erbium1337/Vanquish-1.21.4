package wtf.vanquish.client.features.modules.render;

import lombok.Getter;
import wtf.vanquish.api.event.EventListener;
import wtf.vanquish.api.event.Listener;
import wtf.vanquish.api.event.events.render.EntityColorEvent;
import wtf.vanquish.api.module.Category;
import wtf.vanquish.api.module.Module;
import wtf.vanquish.api.module.ModuleRegister;
import wtf.vanquish.api.module.setting.SliderSetting;
import wtf.vanquish.api.utils.color.ColorUtil;

import java.awt.*;

@ModuleRegister(name = "See Invisibles", category = Category.RENDER)
public class SeeInvisiblesModule extends Module {
    @Getter private static final SeeInvisiblesModule instance = new SeeInvisiblesModule();

    private final SliderSetting alpha = new SliderSetting("Alpha").value(0.3f).range(0.0f, 1f).step(0.1f);

    public SeeInvisiblesModule() {
        addSettings(alpha);
    }

    // TODO: пофиксить hurt.
    @Override
    public void onEvent() {
        EventListener entityColorEvent = EntityColorEvent.getInstance().subscribe(new Listener<>(event -> {
            int donichka = (int) (alpha.getValue() * 255);
            event.color(ColorUtil.setAlpha(new Color(event.color()), donichka).getRGB());
            EntityColorEvent.getInstance().setCancel(true);
        }));

        addEvents(entityColorEvent);
    }
}
