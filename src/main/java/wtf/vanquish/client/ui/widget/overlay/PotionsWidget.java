package wtf.vanquish.client.ui.widget.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.Identifier;
import net.minecraft.util.Language;
import wtf.vanquish.api.utils.color.UIColors;
import wtf.vanquish.api.utils.other.TextUtil;
import wtf.vanquish.api.utils.render.RenderUtil;
import wtf.vanquish.client.features.modules.render.InterfaceModule;
import wtf.vanquish.client.ui.widget.ContainerWidget;
import java.awt.Color;
import java.util.*;

public class PotionsWidget extends ContainerWidget {
    private final Map<String, Float> animMap = new HashMap<>();

    public PotionsWidget() { super(3f, 120f); }
    @Override public String getName() { return "Potions"; }
    @Override protected Map<String, ContainerElement.ColoredString> getCurrentData() { return null; }

    private static final Set<String> BAD = Set.of("wither", "poison", "slowness", "weakness", "mining_fatigue", "nausea", "blindness", "hunger", "levitation", "unluck");
    private static final Set<String> GOOD = Set.of("speed", "strength", "regeneration", "fire_resistance", "absorption");

    @Override
    public void render(MatrixStack ms) {
        if (mc.player == null) return;

        Collection<StatusEffectInstance> effects = mc.player.getActiveStatusEffects().values();

        effects.forEach(e -> {
            String k = e.getTranslationKey();
            animMap.put(k, animMap.getOrDefault(k, 0f) + (1f - animMap.getOrDefault(k, 0f)) * 0.15f);
        });
        animMap.entrySet().removeIf(e -> mc.player.getActiveStatusEffects().values().stream()
                .noneMatch(ef -> ef.getTranslationKey().equals(e.getKey())) && e.getValue() < 0.05f);

        float x = getDraggable().getX();
        float y = getDraggable().getY();
        float width = getDraggable().getWidth();

        boolean isRightSide = x + (width / 2f) > MinecraftClient.getInstance().getWindow().getScaledWidth() / 2f;
        boolean white = InterfaceModule.isWhite();

        float h = scaled(11), p = scaled(3f), fS = scaled(6f), iconS = scaled(7);

        Color bg = white ? new Color(255, 255, 255, 255) : new Color(12, 12, 18, 240);
        Color lineC = white ? new Color(100, 100, 100, 100) : new Color(255, 255, 255, 40);
        Color durationColorBase = white ? new Color(60, 60, 60) : new Color(255, 255, 255);

        String title = "Effects";
        float titleW = getSemiBoldFont().getWidth(title, fS);
        float maxW = titleW + p * 8;

        float totalContentHeight = 0;
        for (StatusEffectInstance e : effects) {
            String name = Language.getInstance().get(e.getTranslationKey()) + (e.getAmplifier() > 0 ? " " + (e.getAmplifier() + 1) : "");
            float w = p + iconS + p + getMediumFont().getWidth(name, fS) + p + getMediumFont().getWidth(TextUtil.getDurationText(e.getDuration()), fS) + p;
            if (w > maxW) maxW = w;

            float anim = animMap.getOrDefault(e.getTranslationKey(), 0f);
            totalContentHeight += h * anim;
        }

        float totalH = h + totalContentHeight;

        float renderX = isRightSide ? (x + width - maxW) : x;

        RenderUtil.RECT.draw(ms, renderX, y, maxW, totalH, 2, bg);

        getSemiBoldFont().drawGradientText(ms, title, renderX + (maxW - titleW) / 2f, y + h/2 - fS/2 + 0.5f, fS,
                UIColors.primary(), UIColors.secondary(), titleW);

        RenderUtil.RECT.draw(ms, renderX + p, y + h - 0.5f, maxW - p * 2, 0.5f, 0, lineC);

        float cY = y + h;
        for (StatusEffectInstance e : effects) {
            String k = e.getTranslationKey();
            if (!animMap.containsKey(k)) continue;
            float anim = animMap.get(k);

            if (anim > 0.05f) {
                int tAlpha = (int)(255 * anim);
                String id = e.getEffectType().getKey().get().getValue().getPath();

                Color nameC;
                if (BAD.contains(id)) {
                    nameC = UIColors.negativeColor();
                } else if (GOOD.contains(id)) {
                    nameC = UIColors.positiveColor();
                } else {
                    nameC = white ? new Color(20, 20, 20) : UIColors.textColor();
                }
                nameC = new Color(nameC.getRed(), nameC.getGreen(), nameC.getBlue(), tAlpha);

                Identifier tex = Identifier.of("minecraft", "textures/mob_effect/" + id + ".png");
                int texId = mc.getTextureManager().getTexture(tex).getGlId();
                RenderSystem.setShaderColor(1f, 1f, 1f, anim);
                RenderUtil.TEXTURE_RECT.draw(ms, renderX + p, cY + (h * anim)/2 - iconS/2, iconS, iconS, 0f, new Color(255, 255, 255, tAlpha), 0f, 0f, 1f, 1f, texId);
                RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

                String name = Language.getInstance().get(k) + (e.getAmplifier() > 0 ? " " + (e.getAmplifier() + 1) : "");
                String dur = TextUtil.getDurationText(e.getDuration());
                float tY = cY + (h * anim)/2 - fS/2;

                getMediumFont().drawText(ms, name, renderX + p + iconS + p, tY, fS, nameC);

                float dW = getMediumFont().getWidth(dur, fS);
                Color durC = new Color(durationColorBase.getRed(), durationColorBase.getGreen(), durationColorBase.getBlue(), tAlpha);
                getMediumFont().drawText(ms, dur, renderX + maxW - p - dW, tY, fS, durC);
            }
            cY += h * anim;
        }

        getDraggable().setWidth(maxW);
        getDraggable().setHeight(totalH);
    }
}