package ac.anticheat.taco.data;

import ac.anticheat.taco.player.PlayerInventory;
import ac.anticheat.taco.utils.ConfigUtil;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.User;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class PlayerData {
    private final Player bukkitPlayer;
    private final User user;

    private ActionData actionData;
    private RotationData rotationData;
    private PacketData packetData;

    private PlayerInventory inventory;

    private boolean alerts = false;
    private boolean enableAlertsOnJoin;

    public PlayerData(Player bukkitPlayer) {
        this.bukkitPlayer = bukkitPlayer;
        this.user = PacketEvents.getAPI().getPlayerManager().getUser(bukkitPlayer);
        this.actionData = new ActionData(this);
        this.packetData = new PacketData(this);
        this.rotationData = new RotationData(this);
        this.inventory = new PlayerInventory(bukkitPlayer);

        this.enableAlertsOnJoin = ConfigUtil.getBoolean("alerts.enable-alerts-on-join", true);
    }

    public void toggleAlerts() {
        this.alerts = !this.alerts;
    }

    public void setAlerts(boolean state) {
        this.alerts = state;
    }

    public boolean alerts() {
        return this.alerts;
    }

    public boolean toggleAlertsOnJoin() {
        return this.enableAlertsOnJoin;
    }
}
