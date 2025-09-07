package ac.anticheat.taco;

import ac.anticheat.taco.commands.TacoCommand;
import ac.anticheat.taco.listeners.CheckListener;
import ac.anticheat.taco.listeners.InventoryListener;
import ac.anticheat.taco.listeners.PacketListener;
import ac.anticheat.taco.managers.CheckManager;
import ac.anticheat.taco.player.PlayerInventory;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import org.bukkit.plugin.java.JavaPlugin;

public class TacoAC extends JavaPlugin {
    private static TacoAC instance;

    @Override
    public void onLoad() {
        registerPacketListeners();
    }

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        if (PacketEvents.getAPI().getServerManager().getVersion() != ServerVersion.V_1_16_5) {
            ServerVersion version = PacketEvents.getAPI().getServerManager().getVersion();
            getLogger().severe("§4Вы используете версию " + version + ". Не удивляйтесь если увидите ложные срабатывания. Рекомендуется перейти на 1.16.5");
        } else {
            getLogger().info("§aВы используете версию " + PacketEvents.getAPI().getServerManager().getVersion() + ". Можете жить спокойно");
        }

        getLogger().info("§aTacoAC запущен");

        getServer().getOnlinePlayers().forEach(player -> CheckManager.registerChecks(player));

        new TacoCommand(this);
        registerBukkitListeners();
    }

    @Override
    public void onDisable() {
        getServer().getOnlinePlayers().forEach(player -> CheckManager.unregisterChecks(player));
    }

    public static TacoAC getInstance() {
        return instance;
    }

    private void registerPacketListeners() {
        PacketEvents.getAPI().getEventManager().registerListener(
                new PacketListener(), PacketListenerPriority.NORMAL);

        PacketEvents.getAPI().getEventManager().registerListener(
                new InventoryListener(), PacketListenerPriority.NORMAL);
    }

    private void registerBukkitListeners() {
        getServer().getPluginManager().registerEvents(new CheckListener(), this);
    }
}
