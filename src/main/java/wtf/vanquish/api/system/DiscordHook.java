package wtf.vanquish.api.system;

import eu.donyka.discord.RPCHandler;
import eu.donyka.discord.discord.RichPresence;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import wtf.vanquish.api.system.interfaces.QuickImports;

@UtilityClass
public class DiscordHook implements QuickImports {
    @SneakyThrows
    public void startRPC() {
        RPCHandler.setOnReady(user -> {
            RichPresence presence = RichPresence.builder()
                    .details("Version: " + "Beta")
                    .largeImageKey("ava")
                    .largeImageText(user.getUsername())
                    .build();

            RPCHandler.updatePresence(presence);
        });

        RPCHandler.setOnDisconnected(error -> {
            System.out.println("RPC Disconnected: " + error);
        });

        RPCHandler.setOnErrored(error -> {
            System.out.println("RPC Errored: " + error);
        });

        RPCHandler.startup("1462541555683295343", false);
    }

    public void stopRPC() {
        RPCHandler.shutdown();
    }
}
