package de.thedodo24.adventuria.common.utils;

import de.thedodo24.adventuria.common.CommonModule;
import de.thedodo24.adventuria.common.classes.Board;
import de.thedodo24.adventuria.common.player.CustomScoreboardType;
import de.thedodo24.adventuria.common.player.ScoreboardModule;
import de.thedodo24.adventuria.common.player.User;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@Getter
public class ManagerScoreboard {

    @Getter
    public static Map<UUID, ManagerScoreboard> scoreboardMap = new HashMap<>();
    private String formatValue(double v) { return NumberFormat.getCurrencyInstance(Locale.GERMANY).format(v).split(" ")[0] + " A"; }

    private UUID uuid;
    private Board board;

    public ManagerScoreboard(Player player) {
        this.uuid = player.getUniqueId();
        this.board = new Board("§2§lAdvi§a§lStatistiken");
        player.setScoreboard(setBoardLines(player).getScoreboard());
        scoreboardMap.put(uuid, this);
    }

    public void sendScoreboard(Player player) {
        player.setScoreboard(setBoardLines(player).getScoreboard());
    }

    public void removeScoreboard(Player player) { player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard()); }

    private Board setBoardLines(Player player) {
        User user = CommonModule.getInstance().getManager().getUserManager().get(player.getUniqueId());
        Map<String, Map<String, String>> scoreboard = user.getCustomScoreboard();
        ScoreboardModule module = new ScoreboardModule();
        board.setValue(1, module.getPlaceholder(1), "§8-------------");
        board.setValue(2, module.getPlaceholder(2), "");
        int line = 3;
        for(int i = 0; i < scoreboard.keySet().size(); i++) {
            Map<String, String> scoreboardLine = scoreboard.get(String.valueOf(i));
            board.setValue(line, module.getPrefixSuffix(), "§7" + module.getValue(CustomScoreboardType.valueOf(scoreboardLine.get("type")), user, scoreboardLine.get("value")));
            line++;
            board.setValue(line, module.getPrefixPrefix(), module.getPattern(CustomScoreboardType.valueOf(scoreboardLine.get("type"))));
            line++;
            board.setValue(line, module.getPlaceholder(line), "");
            line++;
        }
        for(int i = line; i < 22; i++) {
            board.removeLine(i);
        }
        board.setValue(line, module.getPlaceholder(line), "§8--------------");
        return board;
    }


}
