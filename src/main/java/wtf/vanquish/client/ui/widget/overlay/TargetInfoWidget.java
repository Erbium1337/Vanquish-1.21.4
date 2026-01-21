package wtf.vanquish.client.ui.widget.overlay;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import wtf.vanquish.api.utils.animation.AnimationUtil;
import wtf.vanquish.api.utils.color.UIColors;
import wtf.vanquish.api.utils.math.MathUtil;
import wtf.vanquish.api.utils.render.RenderUtil;
import wtf.vanquish.client.features.modules.combat.AuraModule;
import wtf.vanquish.client.features.modules.render.InterfaceModule;
import wtf.vanquish.client.ui.widget.Widget;

import java.awt.*;

public class TargetInfoWidget extends Widget {
    private final AnimationUtil showAnimation = new AnimationUtil();
    private float healthAnimation = 0f;
    private LivingEntity target;

    public TargetInfoWidget() {
        super(4f, 4f);
    }

    @Override
    public String getName() {
        return "Target info";
    }

    @Override
    public void render(MatrixStack ms) {
        update();

        LivingEntity currentTarget = getTarget();
        if (currentTarget != null) target = currentTarget;

        float anim = (float) showAnimation.getValue();
        if (anim <= 0.05f || target == null) return;

        float x = getDraggable().getX();
        float y = getDraggable().getY();

        float padding = scaled(3.5f);
        float headSize = scaled(26);
        float fontSizeName = scaled(8);
        float fontSizeHP = scaled(7);
        float barHeight = scaled(4f);

        float targetHealthPercent = MathHelper.clamp(target.getHealth() / target.getMaxHealth(), 0f, 1f);
        healthAnimation = MathUtil.interpolate(healthAnimation, targetHealthPercent, 0.15f);

        String name = target.getName().getString();
        String hpText = String.format("%.1f HP", target.getHealth() + target.getAbsorptionAmount());

        float textWidth = Math.max(getSemiBoldFont().getWidth(name, fontSizeName), getMediumFont().getWidth(hpText, fontSizeHP));
        float totalW = padding + headSize + padding + Math.max(textWidth, scaled(50)) + padding;
        float totalH = headSize + (padding * 2);

        int tAlpha = (int) (255 * anim);
        boolean white = InterfaceModule.isWhite();
        Color bg = white ? new Color(255, 255, 255, 255) : new Color(0, 0, 0, 255);
        Color textColor = white ? new Color(30, 30, 30, tAlpha) : new Color(255, 255, 255, tAlpha);
        Color hpColor = UIColors.primary(tAlpha);

        RenderUtil.RECT.draw(ms, x, y, totalW, totalH, 4, bg);

        float headX = x + padding;
        float headY = y + padding;
        if (target instanceof PlayerEntity player) {
            RenderUtil.TEXTURE_RECT.drawHead(ms, player, headX, headY, headSize, headSize, 1f, 3, new Color(255, 255, 255, tAlpha));
        } else {
            RenderUtil.RECT.draw(ms, headX, headY, headSize, headSize, 3, new Color(40, 40, 45, tAlpha));
            getSemiBoldFont().drawCenteredText(ms, "?", headX + headSize/2f, headY + headSize/2f - 3, fontSizeName, textColor);
        }

        float contentX = headX + headSize + padding;

        getSemiBoldFont().drawText(ms, name, contentX, headY + 1, fontSizeName, textColor);

        getMediumFont().drawText(ms, hpText, contentX, headY + fontSizeName + 4, fontSizeHP, textColor);

        float barY = headY + headSize - barHeight - 2.5f;
        float barWidth = totalW - (contentX - x) - padding;

        RenderUtil.RECT.draw(ms, contentX, barY, barWidth, barHeight, 1, new Color(0, 0, 0, 255));

        RenderUtil.RECT.draw(ms, contentX, barY, barWidth * healthAnimation, barHeight, 1, hpColor);

        getDraggable().setWidth(totalW);
        getDraggable().setHeight(totalH);
    }

    private void update() {
        showAnimation.update();
        showAnimation.run(getTarget() != null ? 1.0 : 0.0, getDuration(), getEasing());
    }

    private LivingEntity getTarget() {
        AuraModule aura = AuraModule.getInstance();
        if (aura.isEnabled() && aura.target != null) return aura.target;
        if (mc.currentScreen instanceof ChatScreen) return mc.player;
        return null;
    }
}