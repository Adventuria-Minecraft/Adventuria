package de.thedodo24.xenrodsystem.common.quests;

import de.thedodo24.xenrodsystem.common.arango.ArangoWritable;
import de.thedodo24.xenrodsystem.common.job.JobType;
import org.bukkit.Material;

import java.util.HashMap;

public class CollectQuest extends Quest implements ArangoWritable<Long> {

    public CollectQuest(Long key, JobType jobType, HashMap<Material, Long> toCollect, boolean countAsAll, CollectQuests collectQuestType, String questText) {
        super(key, jobType, questText);
        HashMap<String, Long> stringToCollect = new HashMap<>();
        toCollect.keySet().forEach(s -> stringToCollect.put(s.toString(), toCollect.get(s)));
        getValues().put("collectMap", stringToCollect);
        getValues().put("countAsAll", countAsAll);
        getValues().put("collectQuestType", collectQuestType.toString());
        getValues().put("type", QuestType.COLLECT.toString());
    }

    public HashMap<Material, Integer> getCollectMap() {
        HashMap<String, Long> stringIntegerHashMap = (HashMap<String, Long>) getValues().get("collectMap");
        HashMap<Material, Integer> materialIntegerHashMap = new HashMap<>();
        stringIntegerHashMap.keySet().forEach(s -> materialIntegerHashMap.put(Material.valueOf(s), stringIntegerHashMap.get(s).intValue()));
        return materialIntegerHashMap;
    }

    public boolean isCountedAsAll() {
        return (boolean) getValues().get("countAsAll");
    }

    public CollectQuests getCollectQuestType() {
        return CollectQuests.valueOf((String) getValues().get("collectQuestType"));
    }


}
