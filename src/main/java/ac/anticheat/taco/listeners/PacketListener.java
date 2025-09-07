package ac.anticheat.taco.listeners;

import ac.anticheat.taco.checks.Check;
import ac.anticheat.taco.checks.type.PacketCheck;
import ac.anticheat.taco.utils.PacketUtil;
import ac.anticheat.taco.managers.CheckManager;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import org.bukkit.entity.Player;

public class PacketListener implements com.github.retrooper.packetevents.event.PacketListener {

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        Player player = event.getPlayer();

        if (player == null) return;

        if (PacketUtil.isMovement(event)) {
            for (Check check : CheckManager.getChecks(player)) {
                check.updateLastLocation();
            }
        }

        for (Check check : CheckManager.getChecks(player)) {
            if (check instanceof PacketCheck packetCheck) {
                packetCheck.onPacketReceive(event);
            }
        }
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        Player player = event.getPlayer();

        if (player == null) return;

        for (Check check : CheckManager.getChecks(player)) {
            if (check instanceof PacketCheck packetCheck) {
                packetCheck.onPacketSend(event);
            }
        }
    }
}
