package ac.anticheat.taco.checks.impl.inventory;

import ac.anticheat.taco.checks.Check;
import ac.anticheat.taco.checks.type.PacketCheck;
import ac.anticheat.taco.data.PlayerData;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;

public class InventoryA extends Check implements PacketCheck {
    public InventoryA(PlayerData playerData) {
        super("InventoryA", playerData);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (!isEnabled()) return;

        if (event.getPacketType() == PacketType.Play.Client.CLICK_WINDOW) {
            if (playerData.getBukkitPlayer().isSprinting() || playerData.getBukkitPlayer().isHandRaised() || playerData.getBukkitPlayer().isSneaking()) {
                punish("Sprinting=" + playerData.getBukkitPlayer().isSprinting() + ", Hand raised=" + playerData.getBukkitPlayer().isHandRaised() + ", Sneaking=" + playerData.getBukkitPlayer().isSneaking());
                playerData.getInventory().closeInventory();
            }
        }
    }
}
