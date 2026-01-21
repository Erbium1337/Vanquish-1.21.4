package wtf.vanquish.client.ui.widget.overlay;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import wtf.vanquish.api.system.backend.ClientInfo;
import wtf.vanquish.api.utils.color.UIColors;
import wtf.vanquish.api.utils.render.RenderUtil;
import wtf.vanquish.client.features.modules.render.InterfaceModule;
import wtf.vanquish.client.ui.widget.Widget;
import java.awt.Color;

public class WatermarkWidget extends Widget {
    private float animatedFps = 0;
    private float animatedBps = 0;

    public WatermarkWidget() { super(4f, 4f); }
    @Override public String getName() { return "Watermark"; }

    @Override public void render(MatrixStack ms) {
        if (mc.player == null) return;

        float x = getDraggable().getX(), y = getDraggable().getY(), h = scaled(12), p = scaled(4);

        InterfaceModule module = InterfaceModule.getInstance();
        boolean showName = module.watermarkElements.isEnabled("Name");
        boolean showFps = module.watermarkElements.isEnabled("FPS");
        boolean showIp = module.watermarkElements.isEnabled("IP");
        boolean showBps = module.watermarkElements.isEnabled("BPS");

        if (!showName && !showFps && !showIp && !showBps) return;

        animatedFps = MathHelper.lerp(0.1f, animatedFps, mc.getCurrentFps());
        String fpsText = Math.round(animatedFps) + " fps";
        String ipText = (mc.getCurrentServerEntry() != null) ? mc.getCurrentServerEntry().address : "singleplayer";

        double deltaX = mc.player.getX() - mc.player.prevX;
        double deltaZ = mc.player.getZ() - mc.player.prevZ;
        float currentBps = (float) (Math.sqrt(deltaX * deltaX + deltaZ * deltaZ) * 20);
        animatedBps = MathHelper.lerp(0.1f, animatedBps, currentBps);
        String bpsText = String.format("%.2f", animatedBps) + " bps";

        String separator = "|";
        boolean white = InterfaceModule.isWhite();
        Color bg = white ? new Color(255, 255, 255, 255) : new Color(12, 12, 18, 240);
        Color textC = white ? new Color(30, 30, 30, 255) : Color.WHITE;
        Color sepC = white ? new Color(100, 100, 100, 180) : new Color(150, 150, 150, 150);

        float fS = scaled(6), gap = scaled(3);
        float wSep = getSemiBoldFont().getWidth(separator, fS);

        float wTotal = p;
        if (showName) wTotal += getSemiBoldFont().getWidth(ClientInfo.REAL, fS) + gap;
        if (showFps) {
            if (wTotal > p) wTotal += wSep + gap;
            wTotal += getSemiBoldFont().getWidth(fpsText, fS) + gap;
        }
        if (showIp) {
            if (wTotal > p) wTotal += wSep + gap;
            wTotal += getSemiBoldFont().getWidth(ipText, fS) + gap;
        }
        if (showBps) {
            if (wTotal > p) wTotal += wSep + gap;
            wTotal += getSemiBoldFont().getWidth(bpsText, fS) + gap;
        }
        wTotal += p - gap;

        RenderUtil.RECT.draw(ms, x, y, wTotal, h, 2, bg);

        float currentX = x + p;
        float textY = y + h/2 - fS/2 + 1;

        if (showName) {
            float w = getSemiBoldFont().getWidth(ClientInfo.REAL, fS);
            getSemiBoldFont().drawGradientText(ms, ClientInfo.REAL, currentX, textY, fS, UIColors.primary(), UIColors.secondary(), w);
            currentX += w + gap;
        }

        if (showFps) {
            if (currentX > x + p) {
                getSemiBoldFont().drawText(ms, separator, currentX, textY, fS, sepC);
                currentX += wSep + gap;
            }
            getSemiBoldFont().drawText(ms, fpsText, currentX, textY, fS, textC);
            currentX += getSemiBoldFont().getWidth(fpsText, fS) + gap;
        }

        if (showIp) {
            if (currentX > x + p) {
                getSemiBoldFont().drawText(ms, separator, currentX, textY, fS, sepC);
                currentX += wSep + gap;
            }
            getSemiBoldFont().drawText(ms, ipText, currentX, textY, fS, textC);
            currentX += getSemiBoldFont().getWidth(ipText, fS) + gap;
        }

        if (showBps) {
            if (currentX > x + p) {
                getSemiBoldFont().drawText(ms, separator, currentX, textY, fS, sepC);
                currentX += wSep + gap;
            }
            getSemiBoldFont().drawText(ms, bpsText, currentX, textY, fS, textC);
        }

        getDraggable().setWidth(wTotal);
        getDraggable().setHeight(h);
    }
}