package ac.anticheat.taco.utils;

import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HexUtil {

    private static final Pattern HEX_PATTERN = Pattern.compile("(?i)&#[a-f0-9]{6}|#[a-f0-9]{6}");

    public static ChatColor fromHex(String hex) {
        if (hex == null || hex.isEmpty()) return ChatColor.RESET;

        try {
            if (hex.startsWith("#") && hex.length() == 7) {
                return ChatColor.of(hex);
            } else {
                return ChatColor.valueOf(hex.toUpperCase());
            }
        } catch (IllegalArgumentException e) {
            return ChatColor.RESET;
        }
    }

    public static String translateHexColors(String message) {
        if (message == null || message.isEmpty()) return "";

        Matcher matcher = HEX_PATTERN.matcher(message);
        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            String hex = matcher.group().replace("&#", "#");
            matcher.appendReplacement(buffer, ChatColor.of(hex).toString());
        }

        matcher.appendTail(buffer);

        return ChatColor.translateAlternateColorCodes('&', buffer.toString());
    }
}
