package ac.anticheat.taco.listeners;

import ac.anticheat.taco.TacoAC;
import ac.anticheat.taco.data.PlayerData;
import ac.anticheat.taco.managers.CheckManager;
import ac.anticheat.taco.managers.PlayerDataManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class CheckListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        PlayerData playerData = PlayerDataManager.get(e.getPlayer());
        CheckManager.registerChecks(e.getPlayer());
        TacoAC.getInstance().getLogger().info("Зарегистрированы проверки для " + e.getPlayer().getName());
        if (playerData.toggleAlertsOnJoin() && !playerData.alerts()) {
            playerData.setAlerts(true);
            playerData.getBukkitPlayer().sendMessage("§aАлерты автоматически включены. Чтобы выключить автоматическое включение алертов измените значение в alerts.enable-alerts-on-join");
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        CheckManager.unregisterChecks(e.getPlayer());
        TacoAC.getInstance().getLogger().info("Удалены проверки для " + e.getPlayer().getName());
        PlayerDataManager.remove(e.getPlayer());
    }
}
