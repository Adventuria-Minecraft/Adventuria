package de.thedodo24.adventuria.job.listener;

import com.google.common.collect.Lists;
import de.thedodo24.adventuria.common.CommonModule;
import de.thedodo24.adventuria.common.inventory.ClickableItem;
import de.thedodo24.adventuria.common.inventory.InventoryManager;
import de.thedodo24.adventuria.common.inventory.SimpleInventory;
import de.thedodo24.adventuria.common.job.JobType;
import de.thedodo24.adventuria.common.player.User;
import de.thedodo24.adventuria.common.quests.CollectQuest;
import de.thedodo24.adventuria.common.quests.CollectQuests;
import de.thedodo24.adventuria.common.quests.Quest;
import de.thedodo24.adventuria.common.quests.QuestType;
import de.thedodo24.adventuria.common.utils.Language;
import de.thedodo24.adventuria.common.utils.SkullItems;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.units.qual.A;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class InventoryListener implements Listener {

    private final String prefix = Language.getLanguage().get("job-prefix");


    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        User user = CommonModule.getInstance().getManager().getUserManager().get(p.getUniqueId());
        Inventory inventory = e.getClickedInventory();
        if(inventory != null) {
            if(InventoryManager.checkInventory(inventory)) {
                SimpleInventory simpleInventory = InventoryManager.getSimpleInventory(inventory);
                if(simpleInventory != null) {
                    e.setCancelled(true);
                    ItemStack itemStack = e.getCurrentItem();
                    if(itemStack != null && itemStack.hasItemMeta()) {
                        ItemMeta meta = itemStack.getItemMeta();
                        if(meta.getPersistentDataContainer().has(CommonModule.getInstance().getClickableItemKey(), PersistentDataType.STRING)) {
                            String key = meta.getPersistentDataContainer().get(CommonModule.getInstance().getClickableItemKey(), PersistentDataType.STRING);
                            HashMap<String, ClickableItem> clickableItems = ClickableItem.getClickableItems().get(p.getUniqueId().toString());
                            ClickableItem clickableItem = clickableItems.get(key);
                            clickableItems.remove(key);
                            if(clickableItems.size() == 0)
                                ClickableItem.getClickableItems().remove(p.getUniqueId().toString());
                            else
                                ClickableItem.getClickableItems().replace(p.getUniqueId().toString(), clickableItems);

                            switch (simpleInventory.getSimpleInventoryType()) {
                                case JOB_MENU -> {
                                    switch (key) {
                                        case "new-job", "change-job" -> {
                                            SimpleInventory nextInventory = clickableItem.getNextInventory();
                                            Inventory nextRealInventory = nextInventory.getInventory();
                                            int i = 11;
                                            for(JobType jobType : JobType.realJobTypes()) {
                                                ClickableItem clickableJobItem = new ClickableItem(SkullItems.getJobSkull(jobType, "§9" + jobType.getName(), Lists.newArrayList()), "job-" + jobType, null, p.getUniqueId());
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
                                                p.sendMessage(prefix + Language.getLanguage().get("job-cancel", individualJob.getName()));
                                            } else {
                                                p.sendMessage(prefix + Language.getLanguage().get("job-cancel-error"));
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
                                                if(individualJob.equals(jobType)) {
                                                    p.sendMessage(prefix + Language.getLanguage().get("job-already", individualJob.getName()));
                                                    p.closeInventory();
                                                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                                                    return;
                                                }
                                                user.removeJob(user.getIndividualJob());
                                            }
                                            user.addJob(jobType);
                                            p.closeInventory();
                                            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1,1);
                                            InventoryManager.getSimpleInventories().remove(simpleInventory.getInventoryKey());
                                            if(individualJob == null)
                                                p.sendMessage(prefix + Language.getLanguage().get("job-new", jobType.getName()));
                                            else
                                                p.sendMessage(prefix + Language.getLanguage().get("job-changed", jobType.getName(), individualJob.getName()));
                                            List<Quest> newQuests = user.getJobQuests(user.getIndividualJob());
                                            p.sendMessage(prefix + Language.getLanguage().get("job-new-quest", newQuests.get(0).getQuestText(), newQuests.get(1).getQuestText()));
                                        }
                                    }
                                }
                                case QUEST_MENU -> {
                                    if(key.startsWith("quest-")) {
                                        String[] splittedKey = key.split("-");
                                        long questId = Long.parseLong(splittedKey[1]);
                                        Quest quest = CommonModule.getInstance().getManager().getQuestManager().get(questId);
                                        if(quest.getQuestType().equals(QuestType.COLLECT)) {
                                            CollectQuest collectQuest = quest.toCollectQuest();
                                            if(collectQuest.isCountedAsAll()) {
                                                if(user.getQuestProgress(collectQuest) < collectQuest.getCollectMap().get(collectQuest.getCollectMap().keySet().stream().findFirst().get())) {
                                                    p.closeInventory();
                                                    p.sendMessage(prefix + Language.getLanguage().get("quest-not-completed"));
                                                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                                                    return;
                                                }
                                            } else {
                                                if(collectQuest.getCollectMap().keySet().stream()
                                                        .anyMatch(k -> user.getQuestProgress(collectQuest, k) < collectQuest.getCollectMap().get(k))) {
                                                    p.closeInventory();
                                                    p.sendMessage(prefix + Language.getLanguage().get("quest-not-completed"));
                                                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                                                    return;
                                                }
                                            }
                                            if(collectQuest.getCollectQuestType().equals(CollectQuests.FISH) || collectQuest.getCollectQuestType().equals(CollectQuests.GET)) {
                                                PlayerInventory playerInventory = p.getInventory();
                                                List<Boolean> boolList = Lists.newArrayList();
                                                collectQuest.getCollectMap().forEach((material, i) -> {
                                                    AtomicInteger sum = new AtomicInteger();
                                                    HashMap<Integer, ? extends ItemStack> allItems = playerInventory.all(material);
                                                    allItems.values().forEach(is -> {
                                                        sum.getAndAdd(is.getAmount());
                                                    });
                                                    boolList.add(sum.get() >= i);
                                                });
                                                if(collectQuest.isCountedAsAll()) {
                                                    if(boolList.stream().noneMatch(b -> b)) {
                                                        p.sendMessage(prefix + Language.getLanguage().get("quest-not-in-inventory"));
                                                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                                                        p.closeInventory();
                                                        return;
                                                    }
                                                } else {
                                                    if(boolList.stream().anyMatch(b -> !b)) {
                                                        p.sendMessage(prefix + Language.getLanguage().get("quest-not-in-inventory"));
                                                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                                                        p.closeInventory();
                                                        return;
                                                    }
                                                }
                                                collectQuest.getCollectMap().forEach((material, i) -> {
                                                    AtomicInteger a = new AtomicInteger(i);
                                                    playerInventory.all(material).forEach((place, is) -> {
                                                        if(a.get() > 0) {
                                                            if((a.get() - is.getAmount()) >= 0) {
                                                                a.addAndGet(-is.getAmount());
                                                                playerInventory.setItem(place, null);
                                                            } else {
                                                                is.setAmount(is.getAmount() - a.get());
                                                                playerInventory.setItem(place, is);
                                                                a.set(0);
                                                            }
                                                        }
                                                    });
                                                });
                                            }
                                                    p.sendMessage(prefix + Language.getLanguage().get("quest-completed"));
                                                    p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                                                    user.setQuestProgress(collectQuest, -1L);
                                                    user.addXP(collectQuest.getJobType(), 20);
                                            p.closeInventory();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        Inventory inventory = e.getInventory();
        if (InventoryManager.checkInventory(inventory)) {
            SimpleInventory simpleInventory = InventoryManager.getSimpleInventory(inventory);
            if (simpleInventory != null) {
                InventoryManager.getSimpleInventories().remove(simpleInventory.getInventoryKey());
            }
        }
    }

}
