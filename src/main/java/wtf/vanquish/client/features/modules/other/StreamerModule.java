package wtf.vanquish.client.features.modules.other;

import lombok.Getter;
import wtf.vanquish.api.module.Category;
import wtf.vanquish.api.module.Module;
import wtf.vanquish.api.module.ModuleRegister;
import wtf.vanquish.api.module.setting.BooleanSetting;
import wtf.vanquish.api.module.setting.MultiBooleanSetting;
import wtf.vanquish.api.system.configs.FriendManager;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ModuleRegister(name = "Streamer", category = Category.OTHER)
public class StreamerModule extends Module {
    @Getter private static final StreamerModule instance = new StreamerModule();

    @Getter private MultiBooleanSetting hide = new MultiBooleanSetting("Hides").value(
            new BooleanSetting("Name").value(true),
            new BooleanSetting("Hide friends").value(true).setVisible(() -> getHide().isEnabled("Name")),
            new BooleanSetting("No Fun Time").value(false)
    );

    public StreamerModule() {
        addSettings(hide);
    }

    private final ConcurrentHashMap<String, Integer> friendCounter = new ConcurrentHashMap<>();
    private final AtomicInteger globalCounter = new AtomicInteger(1);

    public String getProtectedName() {
        return this.isEnabled() ? "Шикарная" : mc.getSession().getUsername();
    }

    public String getProtectedFriendName(String name) {
        return this.isEnabled() && hide.isEnabled("Name") && hide.isEnabled("Hide friends") && FriendManager.getInstance().contains(name) ? generateProtectedFriendName(name) : name;
    }

    public String generateProtectedFriendName(String originalName) {
        int id = friendCounter.computeIfAbsent(originalName.toLowerCase(), key -> globalCounter.getAndIncrement());
        return "Подружка " + id;
    }

    @Override
    public void onEvent() {

    }
}
