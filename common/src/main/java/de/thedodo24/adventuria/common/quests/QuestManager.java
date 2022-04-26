package de.thedodo24.adventuria.common.quests;

import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDatabase;
import com.arangodb.velocypack.VPackSlice;
import com.google.common.collect.Lists;
import de.thedodo24.adventuria.common.CommonModule;
import de.thedodo24.adventuria.common.arango.CollectionManager;
import de.thedodo24.adventuria.common.job.JobType;
import de.thedodo24.adventuria.common.player.User;
import lombok.Getter;
import org.bukkit.Material;

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
                        put(Material.ACACIA_WOOD, 9*64);
                        put(Material.BIRCH_WOOD, 9*64);
                        put(Material.DARK_OAK_WOOD, 9*64);
                        put(Material.JUNGLE_WOOD, 9*64);
                        put(Material.OAK_WOOD, 9*64);
                        put(Material.SPRUCE_WOOD, 9*64);
                    }},
                    true,
                    CollectQuests.GET,
                    "Sammle 9 Stacks Holz"));
            getOrGenerate(getHighestID() + 1, key -> new CollectQuest(key,
                    JobType.WOOD,
                    new HashMap<>(){{
                        put(Material.ACACIA_SAPLING, 2*64);
                        put(Material.BIRCH_SAPLING, 2*64);
                        put(Material.DARK_OAK_SAPLING, 2*64);
                        put(Material.JUNGLE_SAPLING, 2*64);
                        put(Material.OAK_SAPLING, 2*64);
                        put(Material.SPRUCE_SAPLING, 2*64);
                    }},
                    true,
                    CollectQuests.GET,
                    "Sammle 2 Stacks Setzlinge"));
            getOrGenerate(getHighestID() + 1, key -> new CollectQuest(key,
                    JobType.FISH,
                    new HashMap<>(){{
                        put(Material.COD, 2*64);
                        put(Material.SALMON, 2*64);
                    }},
                    true,
                    CollectQuests.FISH,
                    "Angle 5 Stacks Kabeljau oder Lachs"));
            getOrGenerate(getHighestID() + 1, key -> new CollectQuest(key,
                    JobType.FISH,
                    new HashMap<>(){{
                        put(Material.COD, 2*64);
                        put(Material.SALMON, 2*64);
                    }},
                    true,
                    CollectQuests.FISH,
                    "Sammle in 9 Eimern Kabeljau/Lachs/Puffer-Fische (max. 1) und Tropenfische:\n" +
                            "- Sammle 1 Kugelfisch, 4 Kabeljau, 3 Lachs & 1 Tropenfisch im Eimer\n" +
                            "- Sammle 1 Kugelfisch, 3 Kabeljau, 2 Lachs & 3 Tropenfisch im Eimer\n" +
                            "- Sammle 2 Kabeljau, 2 Lachs & 5 Tropenfisch im Eimer\n" +
                            "- Sammle 1 Kugelfisch, 1 Kabeljau, 2 Lachs & 5 Tropenfisch im Eimer\n" +
                            "- Sammle 1 Kugelfisch, 3 Kabeljau, 5 Lachs"));
            getOrGenerate(getHighestID() + 1, key -> new CollectQuest(key,
                    JobType.HUNT,
                    new HashMap<>(){{
                        put(Material.GUNPOWDER, 64);
                        put(Material.BONE, 64);
                        put(Material.ROTTEN_FLESH, 64);
                        put(Material.STRING, 64);
                    }},
                    true,
                    CollectQuests.GET,
                    "Sammle 1 Stack Gunpowder, Knochen, Verrottendes-Fleisch oder Fäden"));
            getOrGenerate(getHighestID() + 1, key -> new CollectQuest(key,
                    JobType.HUNT,
                    new HashMap<>(){{
                        put(Material.GUNPOWDER, 64);
                        put(Material.BONE, 64);
                    }},
                    true,
                    CollectQuests.GET,
                    "Jäger Aufgabe 2"));
            getOrGenerate(getHighestID() + 1, key -> new CollectQuest(key,
                    JobType.BUTCHER,
                    new HashMap<>(){{
                        put(Material.BEEF, 64);
                        put(Material.PORKCHOP, 64);
                        put(Material.MUTTON, 64);
                        put(Material.CHICKEN, 64);
                    }},
                    true,
                    CollectQuests.GET,
                    "Sammle 1 Stack Rinder-, Schweine-, Hammel- oder Hühnchenfleisch"));
            getOrGenerate(getHighestID() + 1, key -> new CollectQuest(key,
                    JobType.BUTCHER,
                    new HashMap<>(){{
                        put(Material.BEEF, 64);
                        put(Material.PORKCHOP, 64);
                    }},
                    true,
                    CollectQuests.GET,
                    "Metzger Aufgabe 2"));
            getOrGenerate(getHighestID() + 1, key -> new CollectQuest(key,
                    JobType.MINER,
                    new HashMap<>(){{
                        put(Material.COBBLESTONE, 9*64);
                        put(Material.DEEPSLATE, 9*64);
                    }},
                    true,
                    CollectQuests.GET,
                    "Sammle 9 Stacks Bruchstein oder Tiefenschiefer"));
            getOrGenerate(getHighestID() + 1, key -> new CollectQuest(key,
                    JobType.MINER,
                    new HashMap<>(){{
                        put(Material.RAW_IRON, 2*64);
                        put(Material.RAW_COPPER, 2*64);
                        put(Material.COAL, 2*64);
                    }},
                    true,
                    CollectQuests.GET,
                    "Sammle 2 Stacks Roheisen, Rohkupfer oder Kohle"));
            getOrGenerate(getHighestID() + 1, key -> new CollectQuest(key,
                    JobType.GENERAL,
                    new HashMap<>(){{
                        put(Material.STICK, 64);
                    }},
                    true,
                    CollectQuests.GET,
                    "Testaufgabe Allgemein 1"));
            getOrGenerate(getHighestID() + 1, key -> new CollectQuest(key,
                    JobType.GENERAL,
                    new HashMap<>(){{
                        put(Material.STONE, 64);
                    }},
                    true,
                    CollectQuests.BUILD,
                    "Testaufgabe Allgemein 2"));
            getOrGenerate(getHighestID() + 1, key -> new CollectQuest(key,
                    JobType.GENERAL,
                    new HashMap<>(){{
                        put(Material.SALMON, 64);
                    }},
                    true,
                    CollectQuests.FISH,
                    "Testaufgabe Allgemein 3"));
            getOrGenerate(getHighestID() + 1, key -> new CollectQuest(key,
                    JobType.GENERAL,
                    new HashMap<>(){{
                        put(Material.BRICK, 64);
                    }},
                    true,
                    CollectQuests.BUILD,
                    "Testaufgabe Allgemein 4"));
            getOrGenerate(getHighestID() + 1, key -> new CollectQuest(key,
                    JobType.GENERAL,
                    new HashMap<>(){{
                        put(Material.GRANITE, 64);
                    }},
                    true,
                    CollectQuests.BUILD,
                    "Testaufgabe Allgemein 5"));
            getOrGenerate(getHighestID() + 1, key -> new CollectQuest(key,
                    JobType.GENERAL,
                    new HashMap<>(){{
                        put(Material.DIRT, 64);
                    }},
                    true,
                    CollectQuests.BUILD,
                    "Testaufgabe Allgemein 6"));
            getOrGenerate(getHighestID() + 1, key -> new CollectQuest(key,
                    JobType.EXTRA,
                    new HashMap<>(){{
                        put(Material.GRAVEL, 64);
                    }},
                    true,
                    CollectQuests.BUILD,
                    "Testaufgabe Extra 1"));
            getOrGenerate(getHighestID() + 1, key -> new CollectQuest(key,
                    JobType.EXTRA,
                    new HashMap<>(){{
                        put(Material.IRON_BLOCK, 64);
                    }},
                    true,
                    CollectQuests.BUILD,
                    "Testaufgabe Extra 2"));
            getOrGenerate(getHighestID() + 1, key -> new CollectQuest(key,
                    JobType.EXTRA,
                    new HashMap<>(){{
                        put(Material.WHITE_WOOL, 64);
                    }},
                    true,
                    CollectQuests.BUILD,
                    "Testaufgabe Extra 3"));
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
                u.clearFinishedQuests();
                u.setJobQuests(JobType.EXTRA, getFinishedQuests(2, getShuffledQuestList(JobType.GENERAL)));
            });
        }
    }
}
