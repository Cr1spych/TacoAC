package ac.anticheat.taco.commands;

import ac.anticheat.taco.TacoAC;
import ac.anticheat.taco.checks.Check;
import ac.anticheat.taco.data.PlayerData;
import ac.anticheat.taco.managers.PlayerDataManager;
import ac.anticheat.taco.utils.ConfigUtil;
import ac.anticheat.taco.managers.CheckManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TacoCommand implements CommandExecutor {

    private final TacoAC plugin;

    public TacoCommand(TacoAC plugin) {
        this.plugin = plugin;
        plugin.getCommand("taco").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendUsage(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload" -> reload(sender);
            case "checks" -> checks(sender);
            case "help" -> sendHelp(sender);
            case "alerts" -> toggleAlerts(sender);
            default -> sendUsage(sender);
        }

        return true;
    }

    private void reload(CommandSender sender) {
        if (!sender.hasPermission("tacoac.reload")) {
            sender.sendMessage("§cУ вас нет прав на выполнение этой команды");
            return;
        }

        sender.sendMessage("§aПерезагрузка конфига...");
        plugin.reloadConfig();
        ConfigUtil.reload();
        sender.sendMessage("§aПерезагрузка прошла успешно");
    }

    private void checks(CommandSender sender) {
        if (!sender.hasPermission("tacoac.checks")) {
            sender.sendMessage("§cУ вас нет прав на выполнение этой команды");
            return;
        }

        sender.sendMessage("§aВключённые чеки:");
        for (Player player : Bukkit.getOnlinePlayers()) {
            for (Check check : CheckManager.getChecks(player)) {
                if (check.isEnabled()) {
                    sender.sendMessage(" §a- §f" + check.getName());
                }
            }
            break;
        }
    }

    private void toggleAlerts(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cЭту команду может использовать только игрок");
            return;
        }

        PlayerData playerData = PlayerDataManager.get((Player) sender);

        playerData.toggleAlerts();
        if (playerData.alerts()) {
            sender.sendMessage("§aАлерты включены");
        } else {
            sender.sendMessage("§cАлерты выключены");
        }
    }

    private void sendHelp(CommandSender sender) {
        if (!sender.hasPermission("tacoac.help")) {
            sender.sendMessage("§cУ вас нет прав на выполнение этой команды");
            return;
        }

        sender.sendMessage("""
                §aСписок команд:
                 §a- §f/taco reload §7- перезагрузка конфига
                 §a- §f/taco checks §7- список включённых чеков
                 §a- §f/taco help §7- помощь
                """);
    }

    private void sendUsage(CommandSender sender) {
        sender.sendMessage("§eИспользование: /taco <reload|checks|help>");
    }
}