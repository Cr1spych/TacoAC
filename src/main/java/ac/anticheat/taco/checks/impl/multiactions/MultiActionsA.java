package ac.anticheat.taco.checks.impl.multiactions;

import ac.anticheat.taco.checks.Check;
import ac.anticheat.taco.checks.type.PacketCheck;
import ac.anticheat.taco.utils.PacketUtil;
import ac.anticheat.taco.data.PlayerData;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;

public class MultiActionsA extends Check implements PacketCheck {
    public MultiActionsA(PlayerData playerData) {
        super("MultiActionsA", playerData);
    }

    private double buffer;

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (!isEnabled()) return;

        if (PacketUtil.isAttack(event)) {
            if (playerData.getBukkitPlayer().isHandRaised()) {
                if (++buffer > 2) {
                    buffer = 0;
                    punish();
                }
            } else {
                buffer = 0;
            }
        }
    }
}
