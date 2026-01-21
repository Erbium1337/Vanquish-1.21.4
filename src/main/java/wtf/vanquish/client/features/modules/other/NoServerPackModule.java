package wtf.vanquish.client.features.modules.other;

import lombok.Getter;
import net.minecraft.network.packet.c2s.common.ResourcePackStatusC2SPacket;
import net.minecraft.network.packet.s2c.common.ResourcePackSendS2CPacket;
import wtf.vanquish.api.event.EventListener;
import wtf.vanquish.api.event.Listener;
import wtf.vanquish.api.event.events.client.PacketEvent;
import wtf.vanquish.api.module.Category;
import wtf.vanquish.api.module.Module;
import wtf.vanquish.api.module.ModuleRegister;

@ModuleRegister(name = "No Server Pack", category = Category.OTHER)
public class NoServerPackModule extends Module {
    @Getter private static final NoServerPackModule instance = new NoServerPackModule();

    @Override
    public void onEvent() {
        EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener<>(event -> {
            if (event.isReceive() && event.packet() instanceof ResourcePackSendS2CPacket packet) {
                sendPacket(new ResourcePackStatusC2SPacket(packet.id(), ResourcePackStatusC2SPacket.Status.ACCEPTED));
                sendPacket(new ResourcePackStatusC2SPacket(packet.id(), ResourcePackStatusC2SPacket.Status.SUCCESSFULLY_LOADED));
                PacketEvent.getInstance().setCancel(true);
            }
        }));

        addEvents(packetEvent);
    }
}
