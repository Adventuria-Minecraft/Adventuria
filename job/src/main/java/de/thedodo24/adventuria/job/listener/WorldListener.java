package de.thedodo24.adventuria.job.listener;

import com.google.common.collect.Lists;
import de.thedodo24.adventuria.common.CommonModule;
import de.thedodo24.adventuria.common.job.JobType;
import de.thedodo24.adventuria.common.player.User;
import de.thedodo24.adventuria.common.quests.CollectQuest;
import de.thedodo24.adventuria.common.quests.CollectQuests;
import de.thedodo24.adventuria.common.quests.Quest;
import de.thedodo24.adventuria.common.quests.QuestType;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class WorldListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
            Player p = e.getPlayer();
            Material material = e.getBlock().getType();
            User u = CommonModule.getInstance().getManager().getUserManager().get(p.getUniqueId());
            List<Quest> jobQuests = Lists.newArrayList(u.getJobQuests(JobType.GENERAL));
            if(u.hasIndividualJob()) {
                List<Quest> individualQuests = u.getJobQuests(u.getIndividualJob());
                jobQuests.addAll(individualQuests);
                if(individualQuests.stream().filter(u::checkFinishedQuest).count() == individualQuests.size()) {
                    jobQuests.addAll(u.getJobQuests(JobType.EXTRA));
                }
            }
            if(jobQuests.stream().anyMatch(q -> q.getQuestType().equals(QuestType.COLLECT))) {
                List<CollectQuest> collectQuests = jobQuests.stream().filter(q -> q.getQuestType().equals(QuestType.COLLECT)).map(Quest::toCollectQuest).filter(q -> q.getCollectQuestType() == CollectQuests.GET).toList();
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

}
