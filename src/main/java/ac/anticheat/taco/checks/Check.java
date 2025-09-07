package ac.anticheat.taco.checks;

import ac.anticheat.taco.TacoAC;
import ac.anticheat.taco.animations.Animation;
import ac.anticheat.taco.managers.PlayerDataManager;
import ac.anticheat.taco.utils.BukkitSchedulerUtil;
import ac.anticheat.taco.utils.ConfigUtil;
import ac.anticheat.taco.utils.HexUtil;
import ac.anticheat.taco.data.PlayerData;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public abstract class Check {
    private final String name;
    protected final PlayerData playerData;
    private final boolean enabled;
    private final boolean setback;
    private final String punishCommand;
    private boolean alert;
    private int violations;
    private int maxViolations;

    private BukkitTask decayTask;
    private Location lastLocation;

    public Check(String name, PlayerData playerData) {
        this.name = name;
        this.playerData = playerData;
        this.enabled = ConfigUtil.getBoolean("checks." + name + ".enabled", true);
        this.setback = false;
        this.punishCommand = ConfigUtil.getString("checks." + name + ".punish-command", "");
        this.alert = ConfigUtil.getBoolean("checks." + name + ".alert", true);
        this.maxViolations = ConfigUtil.getInt("checks." + name + ".max-violations", 10);

        this.lastLocation = playerData.getBukkitPlayer().getLocation().clone();

        startDecayTask();
    }

    public Check(String name, PlayerData playerData, boolean setback) {
        this.name = name;
        this.playerData = playerData;
        this.enabled = ConfigUtil.getBoolean("checks." + name + ".enabled", true);
        this.setback = setback;
        this.punishCommand = ConfigUtil.getString("checks." + name + ".punish-command", "");
        this.alert = ConfigUtil.getBoolean("checks." + name + ".alert", true);
        this.maxViolations = ConfigUtil.getInt("checks." + name + ".max-violations", 10);

        this.lastLocation = playerData.getBukkitPlayer().getLocation().clone();

        startDecayTask();
    }

    protected void punish() {
        punish("");
    }

    protected void punish(String verbose) {
        boolean logInConsole = ConfigUtil.getBoolean("alerts.log-in-console", true);

        if (violations < maxViolations) {
            violations++;
        }

        String rawMessage = ConfigUtil.getString("alerts.message", "");

        String message = HexUtil.translateHexColors(rawMessage)
                .replace("{prefix}", HexUtil.translateHexColors(ConfigUtil.getString("anticheat.prefix", "(Префикс)")))
                .replace("{player}", playerData.getBukkitPlayer().getName())
                .replace("{check}", getName())
                .replace("{violations}", String.valueOf(getViolations()))
                .replace("{verbose}", verbose);

        if (alert && violations <= maxViolations && playerData.getBukkitPlayer().isOnline()) {
            for (Player online : Bukkit.getOnlinePlayers()) {
                if (online.hasPermission("tacoac.alerts")) {
                    PlayerData targetData = PlayerDataManager.get(online);
                    if (targetData != null && targetData.alerts()) {
                        online.sendMessage(message);
                    }
                }
            }
        }

        if (logInConsole) {
            TacoAC.getInstance().getLogger().info(message);
        }

        if (setback && playerData.getBukkitPlayer().isOnline()) {
            setback();
        }

        if (getViolations() >= getMaxViolations()) {
            Animation.start(playerData.getBukkitPlayer());
            dispatchCommand(punishCommand);
        }
    }

    protected void setback() {
        if (lastLocation != null && playerData.getBukkitPlayer().isOnline()) {
            BukkitSchedulerUtil.runSync(() -> playerData.getBukkitPlayer().teleport(lastLocation));
        }
    }

    protected void updateLastLocation(Location loc) {
        if (loc != null) {
            this.lastLocation = loc.clone();
        }
    }

    private void dispatchCommand(String command) {
        String finalCommand = HexUtil.translateHexColors(command);

        BukkitSchedulerUtil.runSync(() -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalCommand.replace("{player}", playerData.getBukkitPlayer().getName()));
            resetViolations();
        });
    }

    protected void cancel(PacketReceiveEvent event) {
        event.setCancelled(true);
    }

    private void startDecayTask() {
        long rawDelay = ConfigUtil.getInt(getConfigPath() + ".remove-violations-after", 300);
        long delay = rawDelay * 20;

        this.decayTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (violations > 0) {
                    int decay = ConfigUtil.getInt(getConfigPath() + ".decay", 1);
                    int oldViolations = violations;

                    violations -= decay;
                    if (violations < 0) violations = 0;

                    if (oldViolations != violations) {
                        TacoAC.getInstance().getLogger().info(playerData.getBukkitPlayer().getName() + " - " + getName() +
                                ": количество нарушений уменьшено на " + decay + ", теперь: " + violations);
                    }
                }
            }
        }.runTaskTimer(TacoAC.getInstance(), delay, delay);
    }

    public void cancelDecayTask() {
        if (decayTask != null) {
            decayTask.cancel();
        }
    }

    public void updateLastLocation() {
        if (playerData != null && playerData.getBukkitPlayer().isOnline()) {
            this.lastLocation = playerData.getBukkitPlayer().getLocation().clone();
        }
    }

    public void resetViolations() {
        this.violations = 0;
    }

    public String getName() {
        return name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getConfigPath() {
        return "checks." + name;
    }

    public boolean alert() {
        return alert;
    }

    public int getViolations() {
        return violations;
    }

    public int getMaxViolations() {
        return maxViolations;
    }
}