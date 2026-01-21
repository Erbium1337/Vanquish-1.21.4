package wtf.vanquish.client.features.modules.combat;

import lombok.Getter;
import net.minecraft.entity.Entity;
import wtf.vanquish.api.module.Category;
import wtf.vanquish.api.module.Module;
import wtf.vanquish.api.module.ModuleRegister;
import wtf.vanquish.api.system.configs.FriendManager;

@ModuleRegister(name = "No Entity Trace", category = Category.COMBAT)
public class NoEntityTraceModule extends Module {
    @Getter private static final NoEntityTraceModule instance = new NoEntityTraceModule();

    @Override
    public void onEvent() {

    }

    public boolean shouldCancelResult(Entity entity) {
        boolean noFriendHurt = NoFriendHurtModule.getInstance().isEnabled() &&
                entity != null && FriendManager.getInstance().contains(entity.getName().getString());
        return noFriendHurt || isEnabled();
    }
}
