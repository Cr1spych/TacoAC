package ac.anticheat.taco.checks.impl.aim;

import ac.anticheat.taco.checks.Check;
import ac.anticheat.taco.checks.type.PacketCheck;
import ac.anticheat.taco.utils.PacketUtil;
import ac.anticheat.taco.data.PlayerData;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;

public class AimB extends Check implements PacketCheck {
    public AimB(PlayerData playerData) {
        super("AimB", playerData);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (!isEnabled()) return;

        if (PacketUtil.isRotation(event)) {
            if (Math.abs(playerData.getRotationData().pitch) > 90) {
                punish("Pitch=" + playerData.getRotationData().pitch);
            }
        }
    }
}
