package ac.anticheat.taco.utils;

import ac.anticheat.taco.TacoAC;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class BukkitSchedulerUtil {

    private static final Plugin plugin = TacoAC.getInstance();

    public static void runSync(Runnable task) {
        if (Bukkit.isPrimaryThread()) {
            task.run();
        } else {
            Bukkit.getScheduler().runTask(plugin, task);
        }
    }

    public static void runLater(Runnable task, long delayTicks) {
        Bukkit.getScheduler().runTaskLater(plugin, task, delayTicks);
    }

    public static void runRepeating(Runnable task, long delayTicks, long periodTicks) {
        Bukkit.getScheduler().runTaskTimer(plugin, task, delayTicks, periodTicks);
    }
}
