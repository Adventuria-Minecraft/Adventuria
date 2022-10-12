package de.thedodo24.xenrodsystem.job.commands;

import com.google.common.collect.Lists;
import de.thedodo24.xenrodsystem.common.CommonModule;
import de.thedodo24.xenrodsystem.common.inventory.ClickableItem;
import de.thedodo24.xenrodsystem.common.inventory.SimpleInventory;
import de.thedodo24.xenrodsystem.common.inventory.SimpleInventoryTypes;
import de.thedodo24.xenrodsystem.common.job.JobType;
import de.thedodo24.xenrodsystem.common.player.User;
import de.thedodo24.xenrodsystem.common.quests.Quest;
import de.thedodo24.xenrodsystem.common.utils.Language;
import de.thedodo24.xenrodsystem.common.utils.SkullItems;
import org.bukkit.Sound;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class QuestCommand implements CommandExecutor, TabCompleter {

    public QuestCommand() {
        PluginCommand cmd = CommonModule.getInstance().getPlugin().getCommand("quest");
        cmd.setExecutor(this);
        cmd.setTabCompleter(this);
    }

    private String prefix = Language.getLanguage().get("job-prefix");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player p) {
            if(args.length == 0) {
                User user = CommonModule.getInstance().getManager().getUserManager().get(p.getUniqueId());
                SimpleInventory inventory;
                int[] glassPanes;
                List<Quest> generalQuests = CommonModule.getInstance().getManager().getJobManager().get(JobType.GENERAL.getId()).getActiveQuests();
                List<ItemStack> generalToPlace = Lists.newArrayList();
                List<ItemStack> individualToPlace = Lists.newArrayList();
                List<ItemStack> extraToPlace = Lists.newArrayList();
                int i = 1;
                for(Quest q : generalQuests) {
                        if(user.checkFinishedQuest(q)) {
                            generalToPlace.add(SkullItems.getGreenCheckMark("§aAllgemeinaufgabe " + i, q.getQuestLore(user, true)));
                        } else {
                            generalToPlace.add(new ClickableItem(SkullItems.getRedMinusSkull("§cAllgemeinaufgabe " + i, q.getQuestLore(user, false)), "quest-"+q.getKey(), null, p.getUniqueId()).getItemStack());
                        }
                    i++;
                }
                if(user.hasIndividualJob()) {
                    List<Quest> individualQuests = user.getJobQuests(user.getIndividualJob());
                    int x = 1;
                    for(Quest q : individualQuests) {
                        if(user.checkFinishedQuest(q)) {
                            individualToPlace.add(SkullItems.getGreenCheckMark("§aJobaufgabe " + x, q.getQuestLore(user, true)));
                        } else {
                            individualToPlace.add(new ClickableItem(SkullItems.getRedMinusSkull("§cJobaufgabe " + x, q.getQuestLore(user, false)), "quest-"+q.getKey(), null, p.getUniqueId()).getItemStack());
                        }
                        x++;
                    }
                    if(individualQuests.stream().filter(user::checkFinishedQuest).count() == individualQuests.size()) {
                        List<Quest> extraQuests = user.getJobQuests(JobType.EXTRA);
                        int y = 1;
                        for(Quest q : extraQuests) {
                            if(user.checkFinishedQuest(q)) {
                                extraToPlace.add(SkullItems.getGreenCheckMark("§aExtraaufgabe " + y, q.getQuestLore(user, true)));
                            } else {
                                extraToPlace.add(new ClickableItem(SkullItems.getRedMinusSkull("§cExtraaufgabe " + y, q.getQuestLore(user, false)), "quest-"+q.getKey(), null, p.getUniqueId()).getItemStack());
                            }
                            y++;
                        }
                    }
                }

                if(extraToPlace.size() > 0) {
                    inventory = new SimpleInventory("§9» Aufgabenübersicht", 6 * 9, null, SimpleInventoryTypes.QUEST_MENU);
                    glassPanes = new int[]{0, 1, 2, 3, 5, 6, 7, 8,
                            9, 10, 16, 17,
                            18, 19, 20, 22, 24, 25, 26,
                            27, 28, 29, 30, 31, 32, 33, 34, 35,
                            36, 37, 38, 40, 42, 43, 44,
                            45, 46, 47, 48, 49, 50, 51, 52, 53};
                    inventory.getInventory().setItem(39, extraToPlace.get(0));
                    inventory.getInventory().setItem(41, extraToPlace.get(1));
                    inventory.getInventory().setItem(21, individualToPlace.get(0));
                    inventory.getInventory().setItem(23, individualToPlace.get(1));
                    inventory.getInventory().setItem(11, generalToPlace.get(0));
                    inventory.getInventory().setItem(12, generalToPlace.get(1));
                    inventory.getInventory().setItem(13, generalToPlace.get(2));
                    inventory.getInventory().setItem(14, generalToPlace.get(3));
                    inventory.getInventory().setItem(15, generalToPlace.get(4));
                } else if(individualToPlace.size() > 0) {
                    inventory = new SimpleInventory("§9» Aufgabenübersicht", 4 * 9, null, SimpleInventoryTypes.QUEST_MENU);
                    glassPanes = new int[]{0, 1, 2, 3, 5, 6, 7, 8,
                            9, 10, 16, 17,
                            18, 19, 20, 22, 24, 25, 26,
                            27, 28, 29, 30, 31, 32, 33, 34, 35};
                    inventory.getInventory().setItem(21, individualToPlace.get(0));
                    inventory.getInventory().setItem(23, individualToPlace.get(1));
                    inventory.getInventory().setItem(11, generalToPlace.get(0));
                    inventory.getInventory().setItem(12, generalToPlace.get(1));
                    inventory.getInventory().setItem(13, generalToPlace.get(2));
                    inventory.getInventory().setItem(14, generalToPlace.get(3));
                    inventory.getInventory().setItem(15, generalToPlace.get(4));
                } else {
                    inventory = new SimpleInventory("§9» Aufgabenübersicht", 3 * 9, null, SimpleInventoryTypes.QUEST_MENU);
                    glassPanes = new int[]{0, 1, 2, 3, 5, 6, 7, 8,
                            9, 10, 16, 17,
                            18, 19, 20, 21, 22, 23, 24, 25, 26};
                    inventory.getInventory().setItem(11, generalToPlace.get(0));
                    inventory.getInventory().setItem(12, generalToPlace.get(1));
                    inventory.getInventory().setItem(13, generalToPlace.get(2));
                    inventory.getInventory().setItem(14, generalToPlace.get(3));
                    inventory.getInventory().setItem(15, generalToPlace.get(4));
                }
                ItemStack jobSkull;
                JobType jobType = user.hasIndividualJob() ? user.getIndividualJob() : JobType.GENERAL;
                List<String> lore = Lists.newArrayList(" " , user.hasIndividualJob() ? "§7» " + user.getXP(user.getIndividualJob()) + "XP" : "");
                jobSkull = SkullItems.getJobSkull(jobType, "§9» " + jobType.getName(), lore);
                inventory.getInventory().setItem(4, jobSkull);
                inventory.insertGlassPane(glassPanes, inventory.getInventoryKey());
                p.openInventory(inventory.getInventory());
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
            } else {
                p.sendMessage(prefix + "/quest");
            }
        } else {
            sender.sendMessage("Du musst ein Spieler sein.");
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return null;
    }
}
