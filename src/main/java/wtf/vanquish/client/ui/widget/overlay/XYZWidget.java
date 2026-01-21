package wtf.vanquish.client.ui.widget.overlay;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import wtf.vanquish.api.utils.color.UIColors;
import wtf.vanquish.api.utils.render.RenderUtil;
import wtf.vanquish.client.features.modules.render.InterfaceModule;
import wtf.vanquish.client.ui.widget.Widget;
import java.awt.Color;

public class XYZWidget extends Widget {
    public XYZWidget() { super(4f, 4f); }
    @Override public String getName() { return "XYZ"; }

    @Override
    public void render(MatrixStack ms) {
        if (mc.player == null) return;

        float x = getDraggable().getX();
        float y = getDraggable().getY();
        float width = getDraggable().getWidth();

        boolean isRightSide = x + (width / 2f) > MinecraftClient.getInstance().getWindow().getScaledWidth() / 2f;
        boolean white = InterfaceModule.isWhite();

        float h = scaled(14), p = scaled(5);
        float fS = scaled(6);

        String posX = String.format("%.1f", mc.player.getX());
        String posY = String.format("%.1f", mc.player.getY());
        String posZ = String.format("%.1f", mc.player.getZ());

        String fullPos = posX + " " + posY + " " + posZ;
        String title = "XYZ";

        float wTitle = getSemiBoldFont().getWidth(title, fS);
        float wPos = getMediumFont().getWidth(fullPos, fS);

        float totalW = p + wTitle + p + wPos + p;

        float renderX = isRightSide ? (x + width - totalW) : x;

        Color bg = white ? new Color(255, 255, 255, 255) : new Color(0, 0, 0, 255);
        Color textC = white ? new Color(30, 30, 30) : new Color(255, 255, 255);

        RenderUtil.RECT.draw(ms, renderX, y, totalW, h, 2, bg);

        float textY = y + (h / 2f) - (fS / 2f) + 0.5f;

        getSemiBoldFont().drawGradientText(ms, title, renderX + p, textY, fS, UIColors.primary(), UIColors.secondary(), wTitle);

        getMediumFont().drawText(ms, fullPos, renderX + p + wTitle + p, textY, fS, textC);

        getDraggable().setWidth(totalW);
        getDraggable().setHeight(h);
    }
}