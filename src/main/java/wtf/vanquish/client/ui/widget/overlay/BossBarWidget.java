package wtf.vanquish.client.ui.widget.overlay;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.gui.hud.ClientBossBar;
import wtf.vanquish.api.utils.render.RenderUtil;
import wtf.vanquish.client.features.modules.render.InterfaceModule;
import wtf.vanquish.client.ui.widget.Widget;

import java.awt.*;
import java.util.Map;
import java.util.UUID;

public class BossBarWidget extends Widget {
    private float widgetWidth = 0f;
    private float widgetHeight = 0f;

    private String lastSpawn = "";

    public BossBarWidget() {
        super(3f, 50f);
    }

    @Override
    public String getName() {
        return "BossBar";
    }

    @Override
    public void render(MatrixStack matrixStack) {
        float x = getDraggable().getX();
        float y = getDraggable().getY();
        float width = getDraggable().getWidth();
        float gap = getGap();

        if (mc.player == null || mc.world == null || mc.inGameHud == null) {
            getDraggable().setWidth(0f);
            getDraggable().setHeight(0f);
            return;
        }

        BossBarHud bossBarHud = mc.inGameHud.getBossBarHud();
        if (bossBarHud == null) {
            getDraggable().setWidth(0f);
            getDraggable().setHeight(0f);
            return;
        }

        Map<UUID, ClientBossBar> bossBars = bossBarHud.bossBars;
        if (bossBars.isEmpty()) {
            getDraggable().setWidth(0f);
            getDraggable().setHeight(0f);
            return;
        }

        boolean isRightSide = x + (width / 2f) > MinecraftClient.getInstance().getWindow().getScaledWidth() / 2f;
        boolean white = InterfaceModule.isWhite();

        float currentY = y;
        float maxWidth = 0f;
        float totalHeight = 0f;

        for (ClientBossBar bossBar : bossBars.values()) {
            float[] barDimensions = renderBossBar(matrixStack, x, currentY, width, isRightSide, white, bossBar);
            maxWidth = Math.max(maxWidth, barDimensions[2]);
            totalHeight += barDimensions[3] + gap;
            currentY += barDimensions[3] + gap;
        }

        if (totalHeight > 0) totalHeight -= gap;

        widgetWidth = maxWidth;
        widgetHeight = totalHeight;

        getDraggable().setWidth(widgetWidth);
        getDraggable().setHeight(widgetHeight);
    }

    private float[] renderBossBar(MatrixStack matrixStack, float x, float y, float fullWidth, boolean isRightSide, boolean white, ClientBossBar bossBar) {
        float fontSize = scaled(6f);
        float barHeight = scaled(5f);
        float barWidth = scaled(110f);
        float internalGap = getGap() * 0.8f;

        String rawName = bossBar.getName().getString();
        String name = formatBossName(rawName);

        float progress = bossBar.getPercent();

        float textWidth = getMediumFont().getWidth(name, fontSize);
        float contentWidth = Math.max(barWidth + internalGap * 2f, textWidth + internalGap * 3f);
        float backgroundHeight = fontSize + barHeight + internalGap * 2.5f;
        float round = backgroundHeight * 0.25f;

        float moduleX = isRightSide ? (x + fullWidth - contentWidth) : x;

        Color backgroundColor = white ? new Color(255, 255, 255, 255) : new Color(12, 12, 18, 240);
        Color textColor = white ? new Color(30, 30, 30) : Color.WHITE;
        Color barEmptyColor = white ? new Color(200, 200, 200, 150) : new Color(30, 30, 30, 255);
        Color barColor = getBossBarColor(bossBar.getColor());

        RenderUtil.RECT.draw(matrixStack, moduleX, y, contentWidth, backgroundHeight, round, backgroundColor);

        float textX = moduleX + (contentWidth - textWidth) / 2f;
        float textY = y + internalGap;
        getMediumFont().drawText(matrixStack, name, textX, textY, fontSize, textColor);

        float barX = moduleX + (contentWidth - barWidth) / 2f;
        float barY = y + fontSize + internalGap * 1.5f;
        float barRound = barHeight * 0.4f;
        RenderUtil.RECT.draw(matrixStack, barX, barY, barWidth, barHeight, barRound, barEmptyColor);

        float progressWidth = barWidth * progress;
        if (progressWidth > 0) {
            RenderUtil.RECT.draw(matrixStack, barX, barY, progressWidth, barHeight, barRound, barColor);
        }

        return new float[]{moduleX, y, contentWidth, backgroundHeight};
    }

    private String formatBossName(String text) {
        if (text == null || text.trim().isEmpty()) return text;

        String check = text.trim();

        if (check.equalsIgnoreCase("n")) {
            lastSpawn = "Спавн 1";
            return lastSpawn;
        }
        if (check.equalsIgnoreCase("o")) {
            lastSpawn = "Спавн 2";
            return lastSpawn;
        }
        if (check.equalsIgnoreCase("p")) {
            lastSpawn = "Спавн 3";
            return lastSpawn;
        }

        if (check.equalsIgnoreCase("e")) {
            if (!lastSpawn.isEmpty()) {
                return "Варп пвп / " + lastSpawn;
            }
            return "Варп пвп";
        }

        return text;
    }

    private Color getBossBarColor(ClientBossBar.Color color) {
        switch (color) {
            case PINK:   return new Color(255, 105, 180);
            case BLUE:   return new Color(0, 191, 255);
            case RED:    return new Color(255, 50, 50);
            case GREEN:  return new Color(0, 255, 127);
            case YELLOW: return new Color(255, 215, 0);
            case PURPLE: return new Color(160, 32, 240);
            case WHITE:  return new Color(245, 245, 245);
            default:     return new Color(150, 150, 150);
        }
    }
}