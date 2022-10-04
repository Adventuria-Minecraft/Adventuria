package de.thedodo24.adventuria.job.listener;

import com.google.common.collect.Lists;
import de.thedodo24.adventuria.common.CommonModule;
import de.thedodo24.adventuria.common.job.JobType;
import de.thedodo24.adventuria.common.player.User;
import de.thedodo24.adventuria.common.quests.*;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerListener implements Listener {

    private HashMap<UUID, Integer> fishMap = new HashMap<>();
    @Getter
    public static HashMap<UUID, HashMap<Material, Integer>> killMap = new HashMap<>();
    @Getter
    public static HashMap<UUID, Long> killCooldown = new HashMap<>();

    @EventHandler
    public void onFish(PlayerFishEvent e) {
        Player p = e.getPlayer();
        Entity entity = e.getCaught();
        if(entity != null) {
            if(fishMap.containsKey(p.getUniqueId()))
                fishMap.replace(p.getUniqueId(), entity.getEntityId());
            else
                fishMap.put(p.getUniqueId(), entity.getEntityId());
        }
    }

    @EventHandler
    public void onKill(EntityDeathEvent ev) {
        Entity e = ev.getEntity();
        Player killer = ev.getEntity().getKiller();
        if(killer != null && !(e instanceof Player)) {
            User user = CommonModule.getInstance().getManager().getUserManager().get(killer.getUniqueId());
            if(user.getCollectQuestsByType(CollectQuests.COLLECT).stream().anyMatch(q -> !user.checkFinishedQuest(q))) {
                List<ItemStack> drops = ev.getDrops();
                HashMap<Material, Integer> dropsAmount = new HashMap<>();
                drops.forEach(is -> dropsAmount.put(is.getType(), is.getAmount()));
                if(killCooldown.containsKey(killer.getUniqueId())) {
                    killCooldown.replace(killer.getUniqueId(), System.currentTimeMillis() + 60000);
                    HashMap<Material, Integer> oldMap = killMap.get(killer.getUniqueId());
                    oldMap.putAll(dropsAmount);
                    killMap.replace(killer.getUniqueId(), oldMap);
                } else {
                    killCooldown.put(killer.getUniqueId(), System.currentTimeMillis() + 60000);
                    killMap.put(killer.getUniqueId(), dropsAmount);
                }
            } else if(user.getEntityQuestByType(EntityQuests.KILL).stream().anyMatch(q -> !user.checkFinishedQuest(q))) {
                List<EntityQuest> entityQuests = user.getEntityQuestByType(EntityQuests.KILL).stream().filter(q -> q.getEntityHashMap().containsKey(e.getType())).toList();
                if(entityQuests.size() > 0) {
                    for(EntityQuest quest : entityQuests) {
                        if(quest.isCountAsAll()) {
                            user.addQuestProgress(quest, 1);
                        } else {
                            user.addQuestProgress(quest, 1, e.getType());
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent e) {
        if(e.getEntity() instanceof Player p) {
            User u = CommonModule.getInstance().getManager().getUserManager().get(p.getUniqueId());
            if(fishMap.containsKey(p.getUniqueId())) {
                Item pickUp = e.getItem();
                if(pickUp.getEntityId() == fishMap.get(p.getUniqueId())) {
                    fishMap.remove(p.getUniqueId());
                    Material material = pickUp.getItemStack().getType();
                    List<Quest> jobQuests = Lists.newArrayList(u.getJobQuests(JobType.GENERAL));
                    if(u.hasIndividualJob()) {
                        List<Quest> individualQuests = u.getJobQuests(u.getIndividualJob());
                        jobQuests.addAll(individualQuests);
                        if(individualQuests.stream().filter(u::checkFinishedQuest).count() == individualQuests.size()) {
                            jobQuests.addAll(u.getJobQuests(JobType.EXTRA));
                        }
                    }
                    if(jobQuests.stream().anyMatch(q -> q.getQuestType().equals(QuestType.COLLECT))) {
                        List<CollectQuest> collectQuests = jobQuests.stream().filter(q -> q.getQuestType().equals(QuestType.COLLECT)).map(Quest::toCollectQuest).filter(q -> q.getCollectQuestType() == CollectQuests.FISH).toList();
                        for(CollectQuest q : collectQuests) {
                            if(q.getCollectMap().containsKey(material)) {
                                if (q.isCountedAsAll())
                                    u.addQuestProgress(q, 1);
                                else
                                    u.addQuestProgress(q, 1, material);
                            }
                        }
                    }
                }
            } else if(killMap.containsKey(p.getUniqueId())) {
                HashMap<Material, Integer> materialHashMap = killMap.get(p.getUniqueId());
                u.getCollectQuestsByType(CollectQuests.COLLECT).forEach(cq -> materialHashMap.keySet().forEach(material -> {
                    if(cq.getCollectMap().containsKey(material)) {
                        if(cq.isCountedAsAll())
                            u.addQuestProgress(cq, materialHashMap.get(material));
                        else
                            u.addQuestProgress(cq, materialHashMap.get(material), material);
                    }
                    materialHashMap.remove(material);
                }));
                if(materialHashMap.size() > 0)
                    killMap.replace(p.getUniqueId(), materialHashMap);
                else {
                    killMap.remove(p.getUniqueId());
                    killCooldown.remove(p.getUniqueId());
                }
            }

        }
    }

}
