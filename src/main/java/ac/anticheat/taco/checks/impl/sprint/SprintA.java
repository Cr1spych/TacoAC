package ac.anticheat.taco.checks.impl.sprint;

import ac.anticheat.taco.checks.Check;
import ac.anticheat.taco.checks.type.PacketCheck;
import ac.anticheat.taco.data.PlayerData;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction;
import org.bukkit.GameMode;

public class SprintA extends Check implements PacketCheck {
    public SprintA(PlayerData playerData) {
        super("SprintA", playerData);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (!isEnabled()) return;

        if (playerData.getBukkitPlayer().getGameMode() == GameMode.CREATIVE || playerData.getBukkitPlayer().getGameMode() == GameMode.SPECTATOR) return;

        if (event.getPacketType() == PacketType.Play.Client.ENTITY_ACTION) {
            WrapperPlayClientEntityAction wrapper = new WrapperPlayClientEntityAction(event);
            if (wrapper.getAction() == WrapperPlayClientEntityAction.Action.START_SPRINTING) {
                if (playerData.getBukkitPlayer().isInsideVehicle() || playerData.getBukkitPlayer().getFoodLevel() < 6 || playerData.getBukkitPlayer().isSprinting()) {
                    punish("In vehicle=" + playerData.getBukkitPlayer().isInsideVehicle() + ", Food level=" + playerData.getBukkitPlayer().getFoodLevel() + ", Sprinting=" + playerData.getBukkitPlayer().isSprinting());
                }
            }
        }
    }
}
