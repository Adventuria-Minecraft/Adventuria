package de.thedodo24.adventuria.common.quests;

import de.thedodo24.adventuria.common.CommonModule;
import de.thedodo24.adventuria.common.job.JobType;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class CollectQuest extends Quest {

    public CollectQuest(Long key, JobType jobType, HashMap<Material, Integer> toCollect, boolean countAsAll, CollectQuests collectQuestType, String questText) {
        super(key, jobType, questText);
        HashMap<String, Integer> stringToCollect = new HashMap<>();
        toCollect.keySet().forEach(s -> stringToCollect.put(s.toString(), toCollect.get(s)));
        getValues().put("collectMap", stringToCollect);
        getValues().put("countAsAll", countAsAll);
        getValues().put("collectQuestType", collectQuestType.toString());
    }

    public HashMap<Material, Integer> getCollectMap() {
        HashMap<String, Integer> stringIntegerHashMap = (HashMap<String, Integer>) getValues().get("collectMap");
        HashMap<Material, Integer> materialIntegerHashMap = new HashMap<>();
        stringIntegerHashMap.keySet().forEach(s -> materialIntegerHashMap.put(Material.valueOf(s), stringIntegerHashMap.get(s)));
        return materialIntegerHashMap;
    }

    public boolean isCountedAsAll() {
        return (boolean) getValues().get("countAsAll");
    }

    public CollectQuests getCollectQuestType() {
        return CollectQuests.valueOf((String) getValues().get("collectQuestType"));
    }


}
