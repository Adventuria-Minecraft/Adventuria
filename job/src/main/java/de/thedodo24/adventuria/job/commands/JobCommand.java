package de.thedodo24.adventuria.job.commands;

import com.google.common.collect.Lists;
import de.thedodo24.adventuria.common.CommonModule;
import de.thedodo24.adventuria.common.inventory.ClickableItem;
import de.thedodo24.adventuria.common.inventory.SimpleInventory;
import de.thedodo24.adventuria.common.inventory.SimpleInventoryTypes;
import de.thedodo24.adventuria.common.job.JobType;
import de.thedodo24.adventuria.common.player.User;
import de.thedodo24.adventuria.common.utils.Language;
import de.thedodo24.adventuria.common.utils.SkullItems;
import org.bukkit.Sound;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class JobCommand implements CommandExecutor, TabCompleter {

    public JobCommand() {
        PluginCommand cmd = CommonModule.getInstance().getPlugin().getCommand("job");
        cmd.setExecutor(this);
        cmd.setTabCompleter(this);
    }

    private String prefix = Language.getLanguage().get("job-prefix");

    /*
        /job
     */

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player p) {
            if(args.length == 0) {
                User user = CommonModule.getInstance().getManager().getUserManager().get(p.getUniqueId());
                SimpleInventory simpleInventory = new SimpleInventory("§9» Jobmenü", 3*9, null, SimpleInventoryTypes.JOB_MENU);
                SimpleInventory getNewJob = new SimpleInventory("§9» Neuen Job wählen", 3*9, null, SimpleInventoryTypes.JOB_GET_NEW);
                int[] glassPanes = {0,1,2,3,5,6,7,8,18,19,20,21,22,23,24,25,26}; // 11 / 15
                int[] newJobsGlassPanes = {0,1,2,3,4,5,6,7,8,18,19,20,21,22,23,24,25,26};
                simpleInventory.insertGlassPane(glassPanes, simpleInventory.getInventoryKey());
                getNewJob.insertGlassPane(newJobsGlassPanes, getNewJob.getInventoryKey());

                ClickableItem newJob = new ClickableItem(SkullItems.getGreenPlusSkull("§aNeuer Job"), "new-job", getNewJob, p.getUniqueId());

                ClickableItem changeJob = new ClickableItem(SkullItems.getOrangeNewJob("§6Job wechseln"), "change-job", getNewJob, p.getUniqueId());
                ClickableItem deleteJob = new ClickableItem(SkullItems.getRedMinusSkull("§cJob kündigen", Lists.newArrayList()), "remove-job", null, p.getUniqueId());

                ItemStack jobSkull;
                JobType jobType;
                if(user.hasIndividualJob()) {
                    jobType = user.getIndividualJob();
                    simpleInventory.getInventory().setItem(11, changeJob.getItemStack());
                    simpleInventory.getInventory().setItem(15, deleteJob.getItemStack());
                } else {
                    jobType = JobType.GENERAL;
                    simpleInventory.getInventory().setItem(11, newJob.getItemStack());
                    simpleInventory.getInventory().setItem(15, newJob.getItemStack());
                }
                List<String> lore = Lists.newArrayList( " ");
                lore.addAll(JobType.jobTypes().stream()
                        .filter(j -> j != JobType.EXTRA && j != JobType.EVENT && j != JobType.GENERAL)
                        .map(j -> "§7» " + j.getName() + ": §9" + user.getXP(j) + "XP").toList());
                jobSkull = SkullItems.getJobSkull(jobType, "§9» " + jobType.getName(), lore);
                simpleInventory.getInventory().setItem(4, jobSkull);


                p.openInventory(simpleInventory.getInventory());
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
            } else {
                p.sendMessage(prefix + Language.getLanguage().get("job-help"));
            }
        } else {
            sender.sendMessage(prefix + Language.getLanguage().get("haveto-player"));
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return null;
    }
}
