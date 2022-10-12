package de.thedodo24.xenrodsystem.common.quests;

import de.thedodo24.xenrodsystem.common.job.JobType;
import lombok.Getter;
import org.bukkit.entity.EntityType;

import java.util.HashMap;

@Getter
public class EntityQuest extends Quest {

    private EntityQuests entityQuestType;
    private HashMap<EntityType, Long> entityHashMap;
    private boolean countAsAll;


    public EntityQuest(Long key, JobType jobType, String questText, EntityQuests entityQuestType, HashMap<EntityType, Long> entityHashMap, boolean countAsAll) {
        super(key, jobType, questText);
        this.entityQuestType = entityQuestType;
        this.entityHashMap = entityHashMap;
        this.countAsAll = countAsAll;
        HashMap<String, Long> stringMap = new HashMap<>();
        entityHashMap.forEach((k, val) -> stringMap.put(k.toString(), val));
        getValues().put("entityMap", stringMap);
        getValues().put("entityQuestType", entityQuestType);
        getValues().put("countAsAll", countAsAll);
        getValues().put("type", QuestType.ENTITY.toString());
    }


}
