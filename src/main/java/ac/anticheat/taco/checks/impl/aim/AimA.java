package ac.anticheat.taco.checks.impl.aim;

import ac.anticheat.taco.checks.Check;
import ac.anticheat.taco.checks.type.PacketCheck;
import ac.anticheat.taco.utils.PacketUtil;
import ac.anticheat.taco.data.PlayerData;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;

public class AimA extends Check implements PacketCheck {
    public AimA(PlayerData playerData) {
        super("AimA", playerData);
    }

    private double buffer;

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (!isEnabled()) return;

        if (PacketUtil.isRotation(event)) {
            if (playerData.getRotationData().deltaYaw > 70 && playerData.getRotationData().lastDeltaYaw < 8) {
                buffer++;
            } else {
                buffer = Math.max(0, buffer - 0.25);
            }
        }

        if (PacketUtil.isAttack(event)) {
            if (buffer > 1) {
                punish();
                buffer = 0;
            }
        }
    }
}
