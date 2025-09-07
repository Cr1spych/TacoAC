package ac.anticheat.taco.checks.impl.inventory;

import ac.anticheat.taco.checks.Check;
import ac.anticheat.taco.checks.type.PacketCheck;
import ac.anticheat.taco.data.PlayerData;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;

public class InventoryB extends Check implements PacketCheck {
    public InventoryB(PlayerData playerData) {
        super("InventoryB", playerData);
    }

    public double buffer;
    public double threshold = 3;

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.CLICK_WINDOW) {
            if (playerData.getPacketData().getMillisSinceLastClickWindow() < 30) {
                if (++buffer > threshold) {
                    punish();
                    playerData.getInventory().closeInventory();
                    buffer = 0;
                }
            } else {
                buffer = 0;
            }
        }
    }
}
