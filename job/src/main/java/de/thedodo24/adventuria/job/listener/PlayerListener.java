package de.thedodo24.adventuria.job.listener;

import com.google.common.collect.Lists;
import de.thedodo24.adventuria.common.CommonModule;
import de.thedodo24.adventuria.common.inventory.ClickableItem;
import de.thedodo24.adventuria.common.inventory.InventoryManager;
import de.thedodo24.adventuria.common.inventory.SimpleInventory;
import de.thedodo24.adventuria.common.inventory.SimpleInventoryTypes;
import de.thedodo24.adventuria.common.job.JobManager;
import de.thedodo24.adventuria.common.job.JobType;
import de.thedodo24.adventuria.common.player.User;
import de.thedodo24.adventuria.common.utils.Language;
import de.thedodo24.adventuria.common.utils.SkullItems;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class PlayerListener implements Listener {

    private final String prefix = Language.get("job-prefix");


    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        User user = CommonModule.getInstance().getManager().getUserManager().get(p.getUniqueId());
        Inventory inventory = e.getClickedInventory();
        if(inventory != null) {
            if(InventoryManager.checkInventory(inventory)) {
                SimpleInventory simpleInventory = InventoryManager.getSimpleInventory(inventory);
                if(simpleInventory != null) {
                    ItemStack itemStack = e.getCurrentItem();
                    if(itemStack != null && itemStack.hasItemMeta()) {
                        ItemMeta meta = itemStack.getItemMeta();
                        if(meta.getPersistentDataContainer().has(CommonModule.getInstance().getClickableItemKey(), PersistentDataType.STRING)) {
                            String key = meta.getPersistentDataContainer().get(CommonModule.getInstance().getClickableItemKey(), PersistentDataType.STRING);
                            ClickableItem clickableItem = ClickableItem.getClickableItems().get(key);
                            ClickableItem.getClickableItems().remove(key);

                            switch (simpleInventory.getSimpleInventoryType()) {
                                case JOB_MENU -> {
                                    switch (key) {
                                        case "new-job", "change-job" -> {
                                            SimpleInventory nextInventory = clickableItem.getNextInventory();
                                            Inventory nextRealInventory = nextInventory.getInventory();
                                            int i = 11;
                                            for(JobType jobType : JobType.realJobTypes()) {
                                                ClickableItem clickableJobItem = new ClickableItem(SkullItems.getJobSkull(jobType, "§9" + jobType.getName(), Lists.newArrayList()), "job-" + jobType, null);
                                                nextRealInventory.setItem(i, clickableJobItem.getItemStack());
                                                i++;
                                            }
                                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                                            p.openInventory(nextRealInventory);
                                            InventoryManager.getSimpleInventories().remove(simpleInventory.getInventoryKey());
                                        }
                                        case "remove-job" -> {
                                            if(user.hasIndividualJob()) {
                                                JobType individualJob = user.getIndividualJob();
                                                user.removeJob(individualJob);
                                                p.sendMessage(prefix + Language.get("job-cancel", individualJob.getName()));
                                            } else {
                                                p.sendMessage(prefix + Language.get("job-cancel-error"));
                                            }
                                            p.closeInventory();
                                            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                                            InventoryManager.getSimpleInventories().remove(simpleInventory.getInventoryKey());
                                        }
                                    }
                                }
                                case JOB_GET_NEW -> {
                                    if(key.startsWith("job-")) {
                                        String[] splittedKey = key.split("-");
                                        String jobTypeString = splittedKey[1];
                                        JobType jobType = JobType.fromString(jobTypeString);
                                        if(jobType != null && jobType.isRealJob()) {
                                            JobType individualJob = null;
                                            if(user.hasIndividualJob()) {
                                                individualJob = user.getIndividualJob();
                                                user.removeJob(user.getIndividualJob());
                                            }
                                            user.addJob(jobType);
                                            p.closeInventory();
                                            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1,1);
                                            InventoryManager.getSimpleInventories().remove(simpleInventory.getInventoryKey());
                                            if(individualJob == null)
                                                p.sendMessage(prefix + Language.get("job-new", jobType.getName()));
                                            else
                                                p.sendMessage(prefix + Language.get("job-changed", jobType.getName(), individualJob.getName()));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                e.setCancelled(true);
            }
        }
    }

}
