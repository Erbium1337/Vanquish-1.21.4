package wtf.vanquish.client.features.modules.combat;

import lombok.Getter;
import wtf.vanquish.api.module.Category;
import wtf.vanquish.api.module.Module;
import wtf.vanquish.api.module.ModuleRegister;

@ModuleRegister(name = "No Friend Hurt", category = Category.COMBAT)
public class NoFriendHurtModule extends Module {
    @Getter private static final NoFriendHurtModule instance = new NoFriendHurtModule();

    @Override
    public void onEvent() {

    }
}
