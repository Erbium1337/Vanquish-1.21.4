package wtf.vanquish.api.system.configs;

import lombok.Getter;
import wtf.vanquish.api.system.files.AbstractFile;

public class FriendManager extends AbstractFile {
    @Getter private static final FriendManager instance = new FriendManager();

    @Override
    public String fileName() {
        return "friends";
    }
}
