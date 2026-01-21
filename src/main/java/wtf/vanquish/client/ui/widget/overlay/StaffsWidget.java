package wtf.vanquish.client.ui.widget.overlay;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import wtf.vanquish.api.system.configs.StaffManager;
import wtf.vanquish.api.utils.color.UIColors;
import wtf.vanquish.api.utils.framelimiter.FrameLimiter;
import wtf.vanquish.api.utils.other.ReplaceUtil;
import wtf.vanquish.api.utils.player.PlayerUtil;
import wtf.vanquish.api.utils.render.RenderUtil;
import wtf.vanquish.client.features.modules.render.InterfaceModule;
import wtf.vanquish.client.ui.widget.ContainerWidget;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.scoreboard.Team;
import net.minecraft.world.GameMode;

import java.awt.*;
import java.util.*;
import java.util.List;

public class StaffsWidget extends ContainerWidget {
    private final FrameLimiter frameLimiter = new FrameLimiter(false);
    private List<Staff> cacheStaffs = new ArrayList<>();
    private final Map<String, Float> animMap = new HashMap<>();

    public record Staff(String name, String rawName, Status status) {}

    public enum Status {
        ONLINE("Online"), NEAR("Near"), GM3("Gm3"), VANISH("Vanish");
        private final String label;
        Status(String label) { this.label = label; }
        public String getLabel() { return label; }
    }

    public StaffsWidget() { super(100f, 100f); }
    @Override public String getName() { return "Staffs"; }
    @Override protected Map<String, ContainerElement.ColoredString> getCurrentData() { return null; }

    @Override
    public void render(MatrixStack ms) {
        List<Staff> staffs = getStaffList();

        staffs.forEach(s -> {
            String k = s.rawName();
            animMap.put(k, animMap.getOrDefault(k, 0f) + (1f - animMap.getOrDefault(k, 0f)) * 0.15f);
        });
        animMap.entrySet().removeIf(e -> staffs.stream().noneMatch(s -> s.rawName().equals(e.getKey())) && e.getValue() < 0.05f);

        float x = getDraggable().getX();
        float y = getDraggable().getY();
        float width = getDraggable().getWidth();

        boolean isRightSide = x + (width / 2f) > MinecraftClient.getInstance().getWindow().getScaledWidth() / 2f;
        boolean white = InterfaceModule.isWhite();

        float h = scaled(11), p = scaled(3f), fS = scaled(6f);

        Color bg = white ? new Color(255, 255, 255, 255) : new Color(12, 12, 18, 240);
        Color lineC = white ? new Color(100, 100, 100, 100) : new Color(255, 255, 255, 40);
        Color nameBaseColor = white ? new Color(30, 30, 30) : new Color(255, 255, 255);

        String title = "Staff Online";
        float titleW = getSemiBoldFont().getWidth(title, fS);
        float maxW = titleW + p * 6;

        float totalContentHeight = 0;
        for (Staff s : staffs) {
            float w = p + getMediumFont().getWidth(s.name(), fS) + p + getMediumFont().getWidth(s.status().getLabel(), fS) + p;
            if (w > maxW) maxW = w;

            float anim = animMap.getOrDefault(s.rawName(), 0f);
            totalContentHeight += h * anim;
        }

        float totalH = h + totalContentHeight;

        float renderX = isRightSide ? (x + width - maxW) : x;

        RenderUtil.RECT.draw(ms, renderX, y, maxW, totalH, 2, bg);

        getSemiBoldFont().drawGradientText(ms, title, renderX + (maxW - titleW) / 2f, y + h/2 - fS/2 + 0.5f, fS,
                UIColors.primary(), UIColors.secondary(), titleW);

        RenderUtil.RECT.draw(ms, renderX + p, y + h - 0.5f, maxW - p * 2, 0.5f, 0, lineC);

        float cY = y + h;
        for (Staff s : staffs) {
            String k = s.rawName();
            if (!animMap.containsKey(k)) continue;
            float anim = animMap.get(k);

            if (anim > 0.05f) {
                int tAlpha = (int) (255 * anim);

                Color statusC = switch (s.status()) {
                    case ONLINE -> UIColors.positiveColor();
                    case NEAR -> UIColors.middleColor();
                    default -> UIColors.negativeColor();
                };

                Color nameColor = new Color(nameBaseColor.getRed(), nameBaseColor.getGreen(), nameBaseColor.getBlue(), tAlpha);
                Color labelColor = new Color(statusC.getRed(), statusC.getGreen(), statusC.getBlue(), tAlpha);

                float tY = cY + (h * anim) / 2 - fS / 2;

                getMediumFont().drawText(ms, s.name(), renderX + p, tY, fS, nameColor);

                String label = s.status().getLabel();
                float lW = getMediumFont().getWidth(label, fS);
                getMediumFont().drawText(ms, label, renderX + maxW - p - lW, tY, fS, labelColor);
            }

            cY += h * anim;
        }

        getDraggable().setWidth(maxW);
        getDraggable().setHeight(totalH);
    }

    private List<Staff> getStaffList() {
        frameLimiter.execute(15, () -> {
            List<Staff> list = new ArrayList<>();
            if (mc.player != null && !mc.isInSingleplayer()) {
                list.addAll(getOnlineStaff());
                list.addAll(getVanishedPlayers());
            }
            cacheStaffs = list;
        });
        return cacheStaffs;
    }

    private List<Staff> getOnlineStaff() {
        List<Staff> staff = new ArrayList<>();
        if (mc.player == null || mc.player.networkHandler == null) return staff;

        for (PlayerListEntry player : mc.player.networkHandler.getPlayerList()) {
            String rawName = player.getProfile().getName();
            if (!PlayerUtil.isValidName(rawName)) continue;

            Team team = player.getScoreboardTeam();
            String prefix = team != null ? ReplaceUtil.replaceSymbols(team.getPrefix().getString()) : "";

            if (StaffManager.getInstance().contains(rawName) || isStaffPrefix(prefix.toLowerCase())) {
                Status status = Status.ONLINE;
                if (player.getGameMode() == GameMode.SPECTATOR) status = Status.GM3;
                else if (mc.world != null && mc.world.getPlayers().stream().anyMatch(p -> p.getGameProfile().getName().equals(rawName))) {
                    status = Status.NEAR;
                }
                staff.add(new Staff(prefix + rawName, rawName, status));
            }
        }
        return staff;
    }

    private List<Staff> getVanishedPlayers() {
        List<Staff> vanished = new ArrayList<>();
        if (mc.world == null || mc.world.getScoreboard() == null || mc.getNetworkHandler() == null) return vanished;

        Set<String> onlineNames = new HashSet<>();
        mc.getNetworkHandler().getPlayerList().forEach(e -> onlineNames.add(e.getProfile().getName()));

        for (Team team : mc.world.getScoreboard().getTeams()) {
            for (String name : team.getPlayerList()) {
                if (PlayerUtil.isValidName(name) && !onlineNames.contains(name)) {
                    if (StaffManager.getInstance().contains(name)) {
                        vanished.add(new Staff(name, name, Status.VANISH));
                    }
                }
            }
        }
        return vanished;
    }

    private boolean isStaffPrefix(String prefix) {
        String p = prefix.toLowerCase();
        return p.contains("helper") || p.contains("moder") || p.contains("admin") || p.contains("owner") ||
                p.contains("curator") || p.contains("куратор") || p.contains("модер") || p.contains("админ") ||
                p.contains("хелпер") || p.contains("стажер");
    }
}