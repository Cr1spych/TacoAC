package ac.anticheat.taco.data;

import ac.anticheat.taco.checks.Check;
import ac.anticheat.taco.checks.type.PacketCheck;
import ac.anticheat.taco.utils.PacketUtil;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction;

public class ActionData extends Check implements PacketCheck {

    private long lastAttack = -1L;
    private boolean attack = false;
    private boolean interact = false;
    private boolean startSprint = false;
    private boolean stopSprint = false;

    public ActionData(PlayerData playerData) {
        super("ActionData", playerData);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (PacketUtil.isAttack(event)) {
            lastAttack = System.nanoTime();
            attack = true;
            return;
        }

        if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
            interact = true;
            return;
        }

        if (event.getPacketType() == PacketType.Play.Client.ENTITY_ACTION) {
            WrapperPlayClientEntityAction wrapper = new WrapperPlayClientEntityAction(event);
            switch (wrapper.getAction()) {
                case START_SPRINTING -> {
                    startSprint = true;
                    stopSprint = false;
                }
                case STOP_SPRINTING -> {
                    stopSprint = true;
                    startSprint = false;
                }
            }
        }

        if (event.getPacketType() != PacketType.Play.Client.KEEP_ALIVE) {
            attack = false;
            interact = false;
            startSprint = false;
            stopSprint = false;
        }
    }

    public boolean hasAttackedSince(long timeMillis) {
        if (lastAttack == -1) return false;
        long elapsedNanos = System.nanoTime() - lastAttack;
        return (elapsedNanos / 1_000_000) < timeMillis;
    }

    public long getLastAttackMillis() {
        return lastAttack / 1_000_000;
    }

    public long getLastAttackNanos() {
        return lastAttack;
    }

    public boolean attack() {
        return attack;
    }

    public boolean interact() {
        return interact;
    }

    public boolean startSprint() {
        return startSprint;
    }

    public boolean stopSprint() {
        return stopSprint;
    }
}