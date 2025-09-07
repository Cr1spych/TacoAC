package ac.anticheat.taco.checks.impl.inventory;

import ac.anticheat.taco.checks.Check;
import ac.anticheat.taco.checks.type.PacketCheck;
import ac.anticheat.taco.data.PlayerData;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;

public class InventoryC extends Check implements PacketCheck {
    public InventoryC(PlayerData playerData) {
        super("InventoryC", playerData);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (playerData.getActionData().attack()) {
            if (playerData.getInventory().isOpened()) {
                punish();
            }
        }
    }
}
