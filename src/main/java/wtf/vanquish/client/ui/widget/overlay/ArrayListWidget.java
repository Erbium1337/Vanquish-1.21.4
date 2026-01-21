package wtf.vanquish.client.ui.widget.overlay;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import wtf.vanquish.api.module.Module;
import wtf.vanquish.api.module.ModuleManager;
import wtf.vanquish.api.utils.color.UIColors;
import wtf.vanquish.api.utils.render.RenderUtil;
import wtf.vanquish.client.features.modules.render.InterfaceModule;
import wtf.vanquish.client.ui.widget.Widget;

import java.awt.Color;
import java.util.List;
import java.util.stream.Collectors;

public class ArrayListWidget extends Widget {

    public ArrayListWidget() {
        super(2f, 30f);
    }

    @Override
    public String getName() {
        return "ArrayList";
    }

    @Override
    public void render(MatrixStack ms) {
        float x = getDraggable().getX();
        float y = getDraggable().getY();
        float width = getDraggable().getWidth();

        boolean isRightSide = x + (width / 2f) > MinecraftClient.getInstance().getWindow().getScaledWidth() / 2f;

        boolean white = InterfaceModule.isWhite();

        float fontSize = scaled(6);
        float paddingH = scaled(3.5f);
        float paddingV = scaled(1.2f);
        float stripeWidth = 1.2f;

        List<Module> enabledModules = ModuleManager.getInstance().getModules().stream()
                .filter(Module::isEnabled)
                .sorted((m1, m2) -> {
                    float w1 = getMediumFont().getWidth(m1.getName(), fontSize);
                    float w2 = getMediumFont().getWidth(m2.getName(), fontSize);
                    return Float.compare(w2, w1);
                })
                .collect(Collectors.toList());

        float currentY = y;
        float maxSeenWidth = 0;

        Color bg = white ? new Color(255, 255, 255, 255) : new Color(0, 0, 0, 255);
        Color textColor = white ? new Color(30, 30, 30) : new Color(255, 255, 255);

        for (Module module : enabledModules) {
            String moduleName = module.getName();
            float textWidth = getMediumFont().getWidth(moduleName, fontSize);

            float rectWidth = textWidth + (paddingH * 2) + stripeWidth;
            float rectHeight = fontSize + (paddingV * 2);

            float moduleX = isRightSide ? (x + width - rectWidth) : x;

            RenderUtil.RECT.draw(ms, moduleX, currentY, rectWidth, rectHeight, 0, bg);

            float stripeX = isRightSide ? (moduleX + rectWidth - stripeWidth) : moduleX;
            RenderUtil.GRADIENT_RECT.draw(ms,
                    stripeX, currentY, stripeWidth, rectHeight, 0f,
                    UIColors.primary(), UIColors.primary(),
                    UIColors.secondary(), UIColors.secondary()
            );

            float textX = isRightSide ? (moduleX + paddingH) : (moduleX + stripeWidth + paddingH);

            if (white) {
                getMediumFont().drawGradientText(
                        ms, moduleName, textX, currentY + paddingV + 0.5f,
                        fontSize, UIColors.primary(), UIColors.secondary(), textWidth
                );
            } else {
                getMediumFont().drawGradientText(
                        ms, moduleName, textX, currentY + paddingV + 0.5f,
                        fontSize, UIColors.primary(), UIColors.secondary(), textWidth
                );
            }

            if (rectWidth > maxSeenWidth) maxSeenWidth = rectWidth;
            currentY += rectHeight;
        }

        getDraggable().setWidth(maxSeenWidth);
        getDraggable().setHeight(enabledModules.isEmpty() ? scaled(10) : (currentY - y));
    }
}