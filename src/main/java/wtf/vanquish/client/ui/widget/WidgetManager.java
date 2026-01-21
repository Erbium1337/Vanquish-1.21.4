package wtf.vanquish.client.ui.widget;

import lombok.Getter;
import wtf.vanquish.api.event.Listener;
import wtf.vanquish.api.event.events.render.Render2DEvent;
import wtf.vanquish.client.features.modules.render.InterfaceModule;
import wtf.vanquish.client.ui.widget.overlay.*;

import java.util.ArrayList;
import java.util.List;

@Getter
public class WidgetManager {
    @Getter private final static WidgetManager instance = new WidgetManager();

    private final List<Widget> widgets = new ArrayList<>();

    public void load() {
        register(
                new WatermarkWidget(),
                new KeybindsWidget(),
                new PotionsWidget(),
                new StaffsWidget(),
                new BossBarWidget(),

                new ArrayListWidget(),

                new TargetInfoWidget(),
                new XYZWidget()
        );

        InterfaceModule.getInstance().init();

        Render2DEvent.getInstance().subscribe(new Listener<>(event -> {
            if (InterfaceModule.getInstance().isEnabled()) {
                for (Widget widget : widgets) {
                    if (widget.isEnabled()) widget.render(event.matrixStack());
                }
            }
        }));
    }

    // Метод для поиска виджета по классу
    @SuppressWarnings("unchecked")
    public <T extends Widget> T getWidget(Class<T> clazz) {
        return (T) widgets.stream()
                .filter(widget -> widget.getClass() == clazz)
                .findFirst()
                .orElse(null);
    }

    public void register(Widget... widgets) {
        this.widgets.addAll(List.of(widgets));
    }
}