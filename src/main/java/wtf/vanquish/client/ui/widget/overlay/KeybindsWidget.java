package wtf.vanquish.client.ui.widget.overlay;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import wtf.vanquish.api.module.Module;
import wtf.vanquish.api.module.ModuleManager;
import wtf.vanquish.api.system.backend.KeyStorage;
import wtf.vanquish.api.utils.color.UIColors;
import wtf.vanquish.api.utils.render.RenderUtil;
import wtf.vanquish.client.features.modules.render.InterfaceModule;
import wtf.vanquish.client.ui.widget.ContainerWidget;
import java.awt.Color;
import java.util.*;

public class KeybindsWidget extends ContainerWidget {
    private final Map<Module, Float> animMap = new HashMap<>();

    public KeybindsWidget() { super(3f, 120f); }
    @Override public String getName() { return "Keybinds"; }
    @Override protected Map<String, ContainerElement.ColoredString> getCurrentData() { return null; }

    @Override
    public void render(MatrixStack ms) {
        // Обновление анимаций
        ModuleManager.getInstance().getModules().forEach(m -> {
            boolean t = m.isEnabled() && m.hasBind();
            float cur = animMap.getOrDefault(m, 0f);
            animMap.put(m, cur + ((t ? 1f : 0f) - cur) * 0.15f);
        });
        animMap.entrySet().removeIf(e -> e.getValue() < 0.05f && !e.getKey().isEnabled());

        float x = getDraggable().getX();
        float y = getDraggable().getY();
        float width = getDraggable().getWidth();

        boolean isRightSide = x + (width / 2f) > MinecraftClient.getInstance().getWindow().getScaledWidth() / 2f;
        boolean white = InterfaceModule.isWhite();

        float h = scaled(11), p = scaled(3f), fS = scaled(6f);

        Color bg = white ? new Color(255, 255, 255, 255) : new Color(12, 12, 18, 240);
        Color lineC = white ? new Color(100, 100, 100, 100) : new Color(255, 255, 255, 40);
        Color textCBase = white ? new Color(30, 30, 30) : new Color(255, 255, 255);

        String title = "Keybinds";
        float titleW = getSemiBoldFont().getWidth(title, fS);
        float maxW = titleW + p * 6; // Запас для заголовка

        for (Map.Entry<Module, Float> e : animMap.entrySet()) {
            float w = getMediumFont().getWidth(e.getKey().getName(), fS) +
                    getMediumFont().getWidth("[" + KeyStorage.getBind(e.getKey().getBind()) + "]", fS) + p * 4;
            if (w > maxW) maxW = w;
        }

        float totalContentHeight = 0;
        for (float anim : animMap.values()) {
            totalContentHeight += h * anim;
        }
        float totalH = h + totalContentHeight;

        float renderX = isRightSide ? (x + width - maxW) : x;

        RenderUtil.RECT.draw(ms, renderX, y, maxW, totalH, 2, bg);

        getSemiBoldFont().drawGradientText(ms, title, renderX + (maxW - titleW) / 2f, y + h/2 - fS/2 + 0.5f, fS,
                UIColors.primary(), UIColors.secondary(), titleW);

        RenderUtil.RECT.draw(ms, renderX + p, y + h - 0.5f, maxW - p * 2, 0.5f, 0, lineC);

        float cY = y + h;
        for (Module m : ModuleManager.getInstance().getModules()) {
            if (!animMap.containsKey(m)) continue;
            float anim = animMap.get(m);

            if (anim > 0.05f) {
                int alpha = (int)(255 * anim);
                Color txtC = new Color(textCBase.getRed(), textCBase.getGreen(), textCBase.getBlue(), alpha);

                String key = "[" + KeyStorage.getBind(m.getBind()) + "]";
                float itemY = cY + (h * anim) / 2f - fS / 2f;

                getMediumFont().drawText(ms, m.getName(), renderX + p, itemY, fS, txtC);

                float kW = getMediumFont().getWidth(key, fS);
                getMediumFont().drawText(ms, key, renderX + maxW - p - kW, itemY, fS, txtC);
            }
            cY += h * anim;
        }

        getDraggable().setWidth(maxW);
        getDraggable().setHeight(totalH);
    }
}