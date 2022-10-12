package de.thedodo24.xenrodsystem.common.quests;

import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDatabase;
import com.arangodb.velocypack.VPackSlice;
import com.google.common.collect.Lists;
import de.thedodo24.xenrodsystem.common.CommonModule;
import de.thedodo24.xenrodsystem.common.arango.CollectionManager;
import de.thedodo24.xenrodsystem.common.job.JobType;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Getter
public class QuestManager extends CollectionManager<Quest, Long> {

    private ArangoDatabase database;

    public QuestManager(ArangoDatabase database) {
        super("quests", (key -> new Quest(key, JobType.GENERAL, "")), database, ((key, obj) -> true));
        this.database = database;
        createQuests();
    }

    private void createQuests() {
        if(getHighestID() == 0) {
            getOrGenerate(getHighestID(), key -> new CollectQuest(key,
                    JobType.WOOD,
                    new HashMap<>(){{
                        put(Material.ACACIA_LOG, 4L);
                        put(Material.BIRCH_LOG, 4L);
                        put(Material.DARK_OAK_LOG, 4L);
                        put(Material.JUNGLE_LOG, 4L);
                        put(Material.OAK_LOG, 4L);
                        put(Material.SPRUCE_LOG, 4L);
                    }},
                    true,
                    CollectQuests.GET,
                    "Sammle 16 Stücke Holz"));
            getOrGenerate(getHighestID() + 1, key -> new CollectQuest(key,
                    JobType.WOOD,
                    new HashMap<>(){{
                        put(Material.ACACIA_SAPLING, 2L);
                        put(Material.BIRCH_SAPLING, 2L);
                        put(Material.DARK_OAK_SAPLING, 2L);
                        put(Material.JUNGLE_SAPLING, 2L);
                        put(Material.OAK_SAPLING, 2L);
                        put(Material.SPRUCE_SAPLING, 2L);
                    }},
                    true,
                    CollectQuests.GET,
                    "Sammle 2 Setzlinge"));
            getOrGenerate(getHighestID() + 1, key -> new CollectQuest(key,
                    JobType.FISH,
                    new HashMap<>(){{
                        put(Material.SALMON, 2L);
                    }},
                    true,
                    CollectQuests.FISH,
                    "Angle 2 Lachs"));
            getOrGenerate(getHighestID() + 1, key -> new CollectQuest(key,
                    JobType.FISH,
                    new HashMap<>(){{
                        put(Material.COD, 2L);
                    }},
                    true,
                    CollectQuests.FISH,
                    "Angle 2 Kabeljau"));
            getOrGenerate(getHighestID() + 1, key -> new CollectQuest(key,
                    JobType.HUNT,
                    new HashMap<>(){{
                        put(Material.GUNPOWDER, 2L);
                    }},
                    true,
                    CollectQuests.COLLECT,
                    "Sammle 2 Gunpowder"));
            getOrGenerate(getHighestID() + 1, key -> new EntityQuest(key,
                    JobType.HUNT,
                    "Töte 2 Schweine",
                    EntityQuests.KILL,
                    new HashMap<>() {{
                        put(EntityType.PIG, 5L);
                    }},
                    false
                    ));
            getOrGenerate(getHighestID() + 1, key -> new CollectQuest(key,
                    JobType.BUTCHER,
                    new HashMap<>(){{
                        put(Material.BEEF, 2L);
                    }},
                    true,
                    CollectQuests.COLLECT,
                    "Sammle 2 Rinderfleisch"));
            getOrGenerate(getHighestID() + 1, key -> new EntityQuest(key,
                    JobType.BUTCHER,
                    "Töte 2 Kühe",
                    EntityQuests.KILL,

                    new HashMap<>(){{
                        put(EntityType.COW, 2L);
                    }},
                    true));
            getOrGenerate(getHighestID() + 1, key -> new CollectQuest(key,
                    JobType.MINER,
                    new HashMap<>(){{
                        put(Material.COBBLESTONE, 6L);
                    }},
                    true,
                    CollectQuests.GET,
                    "Sammle 6 Cobblestone"));
            getOrGenerate(getHighestID() + 1, key -> new CollectQuest(key,
                    JobType.MINER,
                    new HashMap<>(){{
                        put(Material.COAL_BLOCK, 4L);
                        put(Material.GOLD_BLOCK, 4L);
                        put(Material.IRON_BLOCK, 4L);
                    }},
                    false,
                    CollectQuests.GET,
                    "Mine 4 Kohleblöcke, Goldblöcke und Eisenblöcke"));
            getOrGenerate(getHighestID() + 1, key -> new CollectQuest(key,
                    JobType.GENERAL,
                    new HashMap<>(){{
                        put(Material.STICK, 2L);
                        put(Material.STRING, 2L);
                    }},
                    false,
                    CollectQuests.COLLECT,
                    "Sammle 2 Sticks und 2 Fäden"));
            getOrGenerate(getHighestID() + 1, key -> new CollectQuest(key,
                    JobType.GENERAL,
                    new HashMap<>(){{
                        put(Material.GLASS, 2L);
                    }},
                    true,
                    CollectQuests.COLLECT,
                    "Sammle 2 Gräßer"));
            getOrGenerate(getHighestID() + 1, key -> new CollectQuest(key,
                    JobType.GENERAL,
                    new HashMap<>(){{
                        put(Material.GRAVEL, 4L);
                        put(Material.DIRT, 8L);
                    }},
                    false,
                    CollectQuests.BUILD,
                    "Baue 4 Kiesblöcke und 8 Erdblöcke"));
            getOrGenerate(getHighestID() + 1, key -> new CollectQuest(key,
                    JobType.GENERAL,
                    new HashMap<>(){{
                        put(Material.BEDROCK, 64L);
                        put(Material.ACACIA_LOG, 64L);
                    }},
                    false,
                    CollectQuests.GET,
                    "Testaufgabe Allgemein 4"));
            getOrGenerate(getHighestID() + 1, key -> new CollectQuest(key,
                    JobType.GENERAL,
                    new HashMap<>(){{
                        put(Material.GRANITE, 4L);
                        put(Material.ANDESITE, 4L);
                    }},
                    true,
                    CollectQuests.BUILD,
                    "Baue 4 Granit- oder Andesitblöcke"));
            getOrGenerate(getHighestID() + 1, key -> new EntityQuest(key,
                    JobType.GENERAL,
                    "Töte 2 Hühner",
                    EntityQuests.KILL,
                    new HashMap<>(){{
                        put(EntityType.CHICKEN, 2L);
                    }},
                    true
                    ));
            getOrGenerate(getHighestID() + 1, key -> new EntityQuest(key,
                    JobType.EXTRA,
                    "Töte 2 Zombies oder Kühe",
                    EntityQuests.KILL,
                    new HashMap<>(){{
                        put(EntityType.ZOMBIE, 2L);
                        put(EntityType.COW, 2L);
                    }},
                    false));
            getOrGenerate(getHighestID() + 1, key -> new EntityQuest(key,
                    JobType.EXTRA,
                    "Töte 2 Creeper und Skelette",
                    EntityQuests.KILL,
                    new HashMap<>(){{
                        put(EntityType.CREEPER, 2L);
                        put(EntityType.SKELETON, 2L);
                    }},
                    true));
            getOrGenerate(getHighestID() + 1, key -> new CollectQuest(key,
                    JobType.EXTRA,
                    new HashMap<>(){{
                        put(Material.WHITE_WOOL, 4L);
                        put(Material.BLACK_WOOL, 4L);
                    }},
                    false,
                    CollectQuests.BUILD,
                    "Baue 4 weiße und 4 schwarze Wolle"));
        }
    }

    public List<Quest> getQuestByJob(JobType job) {
        ArangoCursor<VPackSlice> cursor = getDatabase().query("FOR quest IN " + this.collection.name() + " FILTER quest.job == '"+job.toString()+"' RETURN {id: quest._key}",
                VPackSlice.class);
        List<Quest> list = Lists.newArrayList();
        while(cursor.hasNext()) {
            Quest quest = get(Long.parseLong(cursor.next().get("id").getAsString()));
            list.add(quest);
        }
        this.closeCursor(cursor);
        return list;
    }

    public long getHighestID() {
        ArangoCursor<VPackSlice> cursor = getDatabase().query("FOR quest IN " + this.collection.name() + " SORT TO_NUMBER(quest._key) DESC LIMIT 1 RETURN {id: quest._key}",
                VPackSlice.class);
        long highestID = 0;
        while(cursor.hasNext()) {
            long id = Long.parseLong(cursor.next().get("id").getAsString());
            highestID = id;
        }
        this.closeCursor(cursor);
        return highestID;
    }

    public List<Quest> getShuffledQuestList(JobType type) {
        List<Quest> generalQuests = getQuestByJob(type);
        Collections.shuffle(generalQuests);
        return generalQuests;
    }

    public List<Quest> getFinishedQuests(int limit, List<Quest> questList) {
        return questList.stream().limit(limit).toList();
    }


    public void checkTime() {
        final long currentTime = System.currentTimeMillis();
        if(currentTime >= CommonModule.getInstance().getNextWeek()) {
            List<Quest> shuffledQuestList = getShuffledQuestList(JobType.GENERAL);
            CommonModule.getInstance().getManager().getJobManager().getJob(JobType.GENERAL).replaceActiveQuests(getFinishedQuests(5, shuffledQuestList));
            CommonModule.getInstance().getManager().getUserManager().getUsers().forEach(u -> {
                if(u.hasIndividualJob()) {
                    JobType job = u.getIndividualJob();
                    List<Quest> individualQuestList = getShuffledQuestList(job);
                    u.setJobQuests(job, getFinishedQuests(2, individualQuestList));
                }
                u.clearGeneralQuests();
                u.setJobQuests(JobType.EXTRA, getFinishedQuests(2, getShuffledQuestList(JobType.GENERAL)));
            });
        }
    }
}
