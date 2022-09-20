package de.thedodo24.adventuria.common.player;

import com.arangodb.entity.BaseDocument;
import com.google.common.collect.Lists;
import de.thedodo24.adventuria.common.CommonModule;
import de.thedodo24.adventuria.common.arango.ArangoWritable;
import de.thedodo24.adventuria.common.job.JobType;
import de.thedodo24.adventuria.common.quests.*;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class User implements ArangoWritable<UUID> {

    private UUID key;

    public Map<String, Object> values = new HashMap<>();

    public User(UUID key) {
        this.key = key;
        values.put("moneyBalance", (long) 0);
        values.put("ontime", new HashMap<String, Object>() {{
            put("totalOntime", (long) 0);
            put("weekOntime", (long) 0);
            put("dayOntime", (long) 0);
            put("afkTime", (long) 0);
            put("ontimeHistory", new HashMap<String, HashMap<String, Long>>() {{
                put("afkTime", new HashMap<>());
                put("ontime", new HashMap<>());
            }});
        }});
        List<Quest> extraQuests = CommonModule.getInstance().getManager().getQuestManager().getFinishedQuests(2,
                CommonModule.getInstance().getManager().getQuestManager().getShuffledQuestList(JobType.GENERAL)).stream().toList();
        HashMap<String, Object> extraHashMap = new HashMap<>();
        for(Quest eq : extraQuests) {
            if(eq.getQuestType().equals(QuestType.COLLECT)) {
                CollectQuest cq = eq.toCollectQuest();
                if(!cq.isCountedAsAll()) {
                    HashMap<String, Long> materialList = new HashMap<>();
                    cq.getCollectMap().keySet().forEach(m -> materialList.put(m.toString(), 0L));
                    extraHashMap.put(String.valueOf(cq.getKey()), materialList);
                } else {
                    extraHashMap.put(String.valueOf(cq.getKey()), 0L);
                }
            } else
                extraHashMap.put(eq.getKey() + "", false);
        }
        HashMap<String, Object> generalHashMap = new HashMap<>();
        List<Quest> activeGeneralQuests = CommonModule.getInstance().getManager().getJobManager().getJob(JobType.GENERAL).getActiveQuests();
        for(Quest gq : activeGeneralQuests) {
            if(gq.getQuestType().equals(QuestType.COLLECT)) {
                CollectQuest cq = gq.toCollectQuest();
                if(!cq.isCountedAsAll()) {
                    HashMap<String, Long> materialList = new HashMap<>();
                    cq.getCollectMap().keySet().forEach(m -> materialList.put(m.toString(), 0L));
                    generalHashMap.put(String.valueOf(cq.getKey()), materialList);
                } else {
                    generalHashMap.put(String.valueOf(cq.getKey()), 0L);
                }
            } else
                generalHashMap.put(gq.getKey() + "", false);
        }
        values.put("job", new HashMap<String, Object>() {{
            put("activeJobs", new HashMap<String, HashMap<String, Object>>(){{
                put("100", generalHashMap);
                put("200", extraHashMap);
            }});
            JobType.jobTypes().forEach(jobType -> put(jobType.toString(), 0L));
        }});
    }

    @Override
    public UUID getKey() {
        return this.key;
    }

    @Override
    public void read(BaseDocument document) {
        this.values = document.getProperties();
    }

    @Override
    public void save(BaseDocument document) {
        document.setProperties(values);
    }

    public Object getProperty(String key) {
        if(this.values.containsKey(key))
            return this.values.get(key);
        return null;
    }

    public <Type> void updateProperty(String property, Type value) {
        this.updateProperty$(property, value);
    }

    private <Type> void  updateProperty$(String property, Type value) {
        if(this.values.containsKey(property))
            this.values.replace(property, value);
        else
            this.values.put(property, value);
        CommonModule.getInstance().getManager().getUserManager().save(this);
    }

    public void deleteProperty(String property) {
        this.values.remove(property);
        CommonModule.getInstance().getManager().getUserManager().save(this);
    }

    public boolean isSetProperty(String property) {
        return this.values.containsKey(property);
    }

    public String getName() {
        return (String) getProperty("name");
    }

    public void setName(String name) {
        updateProperty("name", name);
        CommonModule.getInstance().getManager().getUserManager().getNames().put(name, key);
    }


    // ECONOMY

    public long getBalance() {
        return (long) getProperty("moneyBalance");
    }

    public long depositMoney(long v) {
        final long finalBalance = v + getBalance();
        updateProperty("moneyBalance", finalBalance);
        return finalBalance;
    }

    public long withdrawMoney(long v) {
        final long finalBalance = getBalance() - v;
        updateProperty("moneyBalance", finalBalance);
        return finalBalance;
    }

    public long setBalance(long v) {
        updateProperty("moneyBalance", v);
        return v;
    }

    // ONTIME

    public Map<String, Object> getOntimeMap() {
        final Map<String, Object> ontimeMap = (Map<String, Object>) getProperty("ontime");
        ontimeMap.remove("ontimeHistory");
        return ontimeMap;
    }

    public long getTotalOntime() {
        return (long) ((Map<String, Object>) getProperty("ontime")).get("totalOntime");
    }
    public long getWeekOntime() {
        return (Long) ((Map<String, Object>) getProperty("ontime")).get("weekOntime");
    }

    public long getDayOntime() {
        return (Long) ((Map<String, Object>) getProperty("ontime")).get("dayOntime");
    }

    public long getAfkTime() {
        return (Long) ((Map<String, Object>) getProperty("ontime")).get("afkTime");
    }

    public long updateTotalOntime(long ontime) {
        Map<String, Object> ontimeMap = (Map<String, Object>) getProperty("ontime");
        ontimeMap.replace("totalOntime", (long) ontimeMap.get("totalOntime") + ontime);
        updateProperty("ontime", ontimeMap);
        return (long) ontimeMap.get("totalOntime");
    }

    public long updateWeekOntime(long ontime) {
        Map<String, Object> ontimeMap = (Map<String, Object>) getProperty("ontime");
        ontimeMap.replace("weekOntime", (long) ontimeMap.get("weekOntime") + ontime);
        updateProperty("ontime", ontimeMap);
        return (long) ontimeMap.get("weekOntime");
    }

    public long updateDayOntime(long ontime) {
        Map<String, Object> ontimeMap = (Map<String, Object>) getProperty("ontime");
        ontimeMap.replace("dayOntime", (long) ontimeMap.get("dayOntime") + ontime);
        updateProperty("ontime", ontimeMap);
        return (long) ontimeMap.get("dayOntime");
    }

    public long updateAfkTime(long ontime) {
        Map<String, Object> ontimeMap = (Map<String, Object>) getProperty("ontime");
        ontimeMap.replace("afkTime", (long) ontimeMap.get("afkTime") + ontime);
        updateProperty("ontime", ontimeMap);
        return (long) ontimeMap.get("afkTime");
    }

    public void updateOntime(long ontime) {
        updateTotalOntime(ontime);
        updateWeekOntime(ontime);
        updateDayOntime(ontime);
    }

    public void setTotalOntime(long ontime) {
        Map<String, Object> ontimeMap = (Map<String, Object>) getProperty("ontime");
        ontimeMap.replace("totalOntime", ontime);
        updateProperty("ontime", ontimeMap);
    }
    public void setWeekOntime(long ontime) {
        Map<String, Object> ontimeMap = (Map<String, Object>) getProperty("ontime");
        ontimeMap.replace("weekOntime", ontime);
        updateProperty("ontime", ontimeMap);
    }
    public void setDayOntime(long ontime) {
        Map<String, Object> ontimeMap = (Map<String, Object>) getProperty("ontime");
        ontimeMap.replace("dayOntime", ontime);
        updateProperty("ontime", ontimeMap);
    }
    public void setAfkTime(long ontime) {
        Map<String, Object> ontimeMap = (Map<String, Object>) getProperty("ontime");
        ontimeMap.replace("afkTime", ontime);
        updateProperty("ontime", ontimeMap);
    }

    public Map<String, Long> getOntimeHistoryMap() {
        return ((Map<String, HashMap<String, Long>>) ((Map<String, Object>) getProperty("ontime")).get("ontimeHistory")).get("ontime");
    }

    public Map<String, Long> getAfkTimeHistoryMap() {
        return ((Map<String, HashMap<String, Long>>) ((Map<String, Object>) getProperty("ontime")).get("ontimeHistory")).get("afkTime");
    }

    public long getOntimeHistory(int week) {
        if(week >= 1 && week <= 9) {
            return ((Map<String, Map<String, Long>>) ((Map<String, Object>) getProperty("ontime")).get("ontimeHistory")).get("ontime").get(week);
        }
        return 0;
    }

    public long getAfkHistory(int week) {
        if(week >= 1 && week <= 9) {
            return ((Map<String, Map<String, Long>>) ((Map<String, Object>) getProperty("ontime")).get("ontimeHistory")).get("afkTime").get(week);
        }
        return 0;
    }

    public long getAfkOfTheWeekHistory(int week) {
        if(week >= 1 && week <= 9) {
            Map<String, Object> ontimeMap = (Map<String, Object>) getProperty("ontime");
            Map<String, Map<String, Long>> ontimeHistoryMap = (Map<String, Map<String, Long>>) ontimeMap.get("ontimeHistory");
            Map<String, Long> afkTimeHistoryMap = ontimeHistoryMap.get("afkTime");
            return afkTimeHistoryMap.get(week) - afkTimeHistoryMap.get(week + 1);
        }
        return 0;
    }

    public void addOntimeHistory(long ontime, long afkTime) {
        Map<String, Object> ontimeMap = (Map<String, Object>) getProperty("ontime");
        Map<String, Map<String, Long>> ontimeHistoryMap = (Map<String, Map<String, Long>>) ontimeMap.get("ontimeHistory");
        Map<String, Long> historyOntimeMap = ontimeHistoryMap.get("ontime");
        Map<String, Long> afkTimeHistoryMap = ontimeHistoryMap.get("afkTime");

        Map<String, Long> newHistoryOntimeMap = new HashMap<>();
        historyOntimeMap.forEach((key, value) -> {
            if(Integer.parseInt(key) < 9)
                newHistoryOntimeMap.put(String.valueOf(Integer.parseInt(key) + 1), value);
        });
        Map<String, Long> newAfkTimeHistoryMap = new HashMap<>();
        afkTimeHistoryMap.forEach((key, value) -> {
            if(Integer.parseInt(key) < 10)
                newAfkTimeHistoryMap.put(String.valueOf(Integer.parseInt(key) + 1), value);
        });
        newHistoryOntimeMap.put("1", ontime);
        newAfkTimeHistoryMap.put("1", afkTime);
        ontimeHistoryMap.replace("ontime", newHistoryOntimeMap);
        ontimeHistoryMap.replace("afkTime", newAfkTimeHistoryMap);
    }

    //JOBS

    private HashMap<String, Object> jobParent() {
        return (HashMap<String, Object>) getProperty("job");
    }

    private Object getJobProperty(String property) {
        return jobParent().get(property);
    }

    private <Type> void updateJobProperty(String property, Type value) {
        HashMap<String, Object> jobParent = jobParent();
        if(jobParent.containsKey(property))
            jobParent.replace(property, value);
        else
            jobParent.put(property, value);
        updateProperty("job", jobParent);
    }

    public boolean hasIndividualJob() {
        return getActiveJobs().stream().filter(j -> j != 100 && j != 200 && j != 300)
                .anyMatch(j -> activeJobsParent().get(j + "").containsKey("active") && (long) activeJobsParent().get(j + "").get("active") == -1L);
    }

    private HashMap<String, HashMap<String, Object>> activeJobsParent() {
        return ((HashMap<String, HashMap<String, Object>>) getJobProperty("activeJobs"));
    }


    private List<Long> getActiveJobs() {
        return activeJobsParent().keySet().stream().map(Long::parseLong).toList();
    }

    public JobType getIndividualJob() {
        return JobType.byId(getActiveJobs().stream().filter(j -> j != (long) JobType.GENERAL.getId() && j != (long) JobType.EXTRA.getId() && j != (long) JobType.EVENT.getId())
                .filter(j -> activeJobsParent().get(j + "").containsKey("active") && (long) activeJobsParent().get(j + "").get("active") == -1L).findAny().get());
    }

    public int getXP(JobType jobType) {
        return Math.toIntExact((long) getJobProperty(jobType.toString()));
    }

    public void setXP(JobType jobType, int xp) {
        updateJobProperty(jobType.toString(), (long) xp);
    }

    public void addXP(JobType jobType, int xp) {
        setXP(jobType, getXP(jobType) + xp);
    }

    public void removeJob(JobType jobType) {
        HashMap<String, HashMap<String, Object>> activeJobParent = activeJobsParent();
        HashMap<String, Object> jobParent = activeJobParent.get(jobType.getId() + "");
        jobParent.replace("active", CommonModule.getInstance().getNextWeek());
        activeJobParent.replace(jobType.getId()+"", jobParent);
        updateJobProperty("activeJobs", activeJobParent);
    }

    public void addJob(JobType jobType) {
        HashMap<String, HashMap<String, Object>> activeJobParent = activeJobsParent();
        if(activeJobParent.containsKey("" + jobType.getId())) {
            HashMap<String, Object> jobParent = activeJobParent.get(jobType.getId() + "");
            long time = (long) jobParent.get("active");
            if(time != -1) {
                if(CommonModule.getInstance().getNextWeek() > time) {
                    activeJobParent.remove(jobType.getId() + "");
                } else {
                    jobParent.replace("active", -1L);
                    activeJobParent.replace(jobType.getId() + "", jobParent);
                    updateJobProperty("activeJobs", activeJobParent);
                    return;
                }
            }
        }
        activeJobParent.put(jobType.getId() + "", new HashMap<>() {{
                put("active", -1L);
                List<Quest> shuffledQuestList = CommonModule.getInstance().getManager().getQuestManager().getShuffledQuestList(jobType);
                for (Quest shuffledQuest : shuffledQuestList) {
                    if (shuffledQuest.getQuestType().equals(QuestType.COLLECT)) {
                        CollectQuest collectShuffledQuest = shuffledQuest.toCollectQuest();
                        if (!collectShuffledQuest.isCountedAsAll()) {
                            HashMap<String, Long> materialList = new HashMap<>();
                            collectShuffledQuest.getCollectMap().keySet().forEach(m -> materialList.put(m.toString(), 0L));
                            put(String.valueOf(collectShuffledQuest.getKey()), materialList);
                        } else {
                            put(String.valueOf(collectShuffledQuest.getKey()), 0L);
                        }
                    } else {
                        put(shuffledQuest.getKey() + "", false);
                    }
                }
            }});
        updateJobProperty("activeJobs", activeJobParent);
    }

    private HashMap<String, Object> questList(long job) {
        HashMap<String, HashMap<String, Object>> jobHashMap = activeJobsParent();
        HashMap<String, Object> stringBooleanHashMap = jobHashMap.get(String.valueOf(job));
        HashMap<String, Object> longBooleanHashMap = new HashMap<>();
        stringBooleanHashMap.keySet().forEach(key -> longBooleanHashMap.put(key, stringBooleanHashMap.get(key)));
        return longBooleanHashMap;
    }

    private void updateQuestList(HashMap<String, Object> questObjectMap, long job) {
        HashMap<String, HashMap<String, Object>> jobHashMap = activeJobsParent();
        jobHashMap.replace(job + "", questObjectMap);
        updateJobProperty("activeJobs", jobHashMap);
    }


    public List<Quest> getJobQuests(JobType type) {
        HashMap<String, Object> longQuestList = questList(type.getId());
        return longQuestList.keySet().stream().filter(key -> !key.equalsIgnoreCase("active"))
                .map(q -> CommonModule.getInstance().getManager().getQuestManager().get(Long.parseLong(q))).toList();
    }

    public List<Quest> getQuestsByType(QuestType type) {
        List<Quest> quests = getJobQuests(JobType.GENERAL);
        if(hasIndividualJob()) {
            List<Quest> individualQuests = getJobQuests(getIndividualJob());
            quests.addAll(individualQuests);
            if(individualQuests.stream().filter(this::checkFinishedQuest).count() == individualQuests.size())
                quests.addAll(getJobQuests(JobType.EXTRA));
        }
        return quests.stream().filter(q -> q.getQuestType() == type).toList();
    }

    public List<CollectQuest> getCollectQuestsByType(CollectQuests type) {
        return getQuestsByType(QuestType.COLLECT).stream().map(Quest::toCollectQuest).filter(cq -> cq.getCollectQuestType() == type).toList();
    }

    public void setJobQuests(JobType type, List<Quest> jobQuests) {
        HashMap<String, Object> questHashMap = new HashMap<>();
        for(Quest q : jobQuests) {
            if(q.getQuestType().equals(QuestType.COLLECT)) {
                CollectQuest cq = q.toCollectQuest();
                HashMap<String, Long> materialMap = new HashMap<>();
                if(!cq.isCountedAsAll()) {
                    cq.getCollectMap().keySet().forEach(m -> materialMap.put(m.toString(), 0L));
                    questHashMap.put(cq.getKey() + "", materialMap);
                } else {
                    questHashMap.put(cq.getKey() + "", 0L);
                }
            } else
                questHashMap.put(q.getKey() + "", false);
        }
        questHashMap.put("active", activeJobsParent().get(type.getId() + "").get("active"));
        updateQuestList(questHashMap, type.getId());
    }

    public void updateFinishedQuest(Quest quest, boolean value) {
        if(quest.getQuestType().equals(QuestType.COLLECT)) {
            HashMap<String, Object> questList = questList(quest.getJobType().getId());
            questList.replace(quest.getKey() + "", value);
            updateQuestList(questList, quest.getJobType().getId());
        }
    }

    public void setQuestProgress(Quest quest, long progress) {
        if(quest.getQuestType().equals(QuestType.COLLECT)) {
            CollectQuest cq = quest.toCollectQuest();
            HashMap<String, Object> questList = questList(quest.getJobType().getId());
                questList.replace(quest.getKey() + "", progress);
                updateQuestList(questList, quest.getJobType().getId());
        }
    }

    public void setQuestProgress(Quest quest, long progress, Material material) {
        if(quest.getQuestType().equals(QuestType.COLLECT)) {
            CollectQuest cq = quest.toCollectQuest();
            HashMap<String, Object> questList = questList(quest.getJobType().getId());
            if(!cq.isCountedAsAll()) {
                HashMap<Material, Long> materialLongMap = (HashMap<Material, Long>) questList.get(quest);
                materialLongMap.replace(material, progress);
                questList.replace(quest.getKey() + "", materialLongMap);
                updateQuestList(questList, quest.getJobType().getId());
            }
        }
    }

    public void addQuestProgress(Quest quest, long progress) {
        if(quest.getQuestType().equals(QuestType.COLLECT)) {
            CollectQuest cq = quest.toCollectQuest();
            HashMap<String, Object> questList = questList(quest.getJobType().getId());
            if(cq.isCountedAsAll()) {
                long p = (long) questList.get(quest.getKey() + "");
                questList.replace(quest.getKey() + "", p + progress);
                updateQuestList(questList, quest.getJobType().getId());
            }
        }
    }

    public void addQuestProgress(Quest quest, long progress, Material material) {
        if(quest.getQuestType().equals(QuestType.COLLECT)) {
            CollectQuest cq = quest.toCollectQuest();
            HashMap<String, Object> questList = questList(quest.getJobType().getId());
            if(!cq.isCountedAsAll()) {
                Object o =questList.get(quest.getKey() + "");
                if(o instanceof HashMap<?,?>) {
                    HashMap<String, Long> materialLongMap = (HashMap<String, Long>) questList.get(quest.getKey() + "");
                    long p = materialLongMap.get(material.toString());
                    materialLongMap.replace(material.toString(), progress + p);
                    questList.replace(quest.getKey() + "", materialLongMap);
                    updateQuestList(questList, quest.getJobType().getId());
                }
            }
        }
    }

    public long getQuestProgress(Quest quest) {
        if(quest.getQuestType().equals(QuestType.COLLECT)) {
            CollectQuest cq = quest.toCollectQuest();
                HashMap<String, Object> questList = questList(quest.getJobType().getId());
                if (questList.containsKey(quest.getKey() + "")) {
                    Object o = questList.get(quest.getKey() + "");
                    if(o instanceof Long) {
                        long progress = (long) o;
                        long max = cq.getCollectMap().get(cq.getCollectMap().keySet().stream().findFirst().get());
                        return Math.min(progress, max);
                    }
                }
        }
        return 0L;
    }
    public long getQuestProgress(Quest quest, Material material) {
        if(quest.getQuestType().equals(QuestType.COLLECT)) {
            CollectQuest cq = quest.toCollectQuest();
            if(!cq.isCountedAsAll()) {
                HashMap<String, Object> questList = questList(quest.getJobType().getId());
                if (questList.containsKey(quest.getKey() + "")) {
                    HashMap<String, Long> materialLongMap = (HashMap<String, Long>) questList.get(quest.getKey() + "");
                    long progress = materialLongMap.get(material.toString());
                    long max = cq.getCollectMap().get(material);
                    return Math.min(progress, max);
                }
            }
        }
        return 0L;
    }

    public boolean checkFinishedQuest(Quest quest) {
        if(quest.getQuestType().equals(QuestType.CHECK)) {
            HashMap<String, Object> questList = questList(quest.getJobType().getId());
            if (questList.containsKey(quest.getKey() + "")) {
                return (boolean) questList.get(quest.getKey() + "");
            }
        } else if(quest.getQuestType().equals(QuestType.COLLECT)) {
            CollectQuest cq = quest.toCollectQuest();
            return getQuestProgress(cq) == -1L;
        }
        return false;
    }

    public void clearGeneralQuests() {
        HashMap<String, HashMap<String, Object>> jobHashMap = activeJobsParent();
        HashMap<String, Object> generalHashMap = new HashMap<>();
        List<Quest> activeGeneralQuests = CommonModule.getInstance().getManager().getJobManager().getJob(JobType.GENERAL).getActiveQuests();
        for(Quest gq : activeGeneralQuests) {
            if(gq.getQuestType().equals(QuestType.COLLECT)) {
                CollectQuest cq = gq.toCollectQuest();
                if(!cq.isCountedAsAll()) {
                    HashMap<String, Long> materialList = new HashMap<>();
                    cq.getCollectMap().keySet().forEach(m -> materialList.put(m.toString(), 0L));
                    generalHashMap.put(String.valueOf(cq.getKey()), materialList);
                } else {
                    generalHashMap.put(String.valueOf(cq.getKey()), 0L);
                }
            } else
                generalHashMap.put(gq.getKey() + "", false);
        }
        jobHashMap.replace("" + JobType.GENERAL.getId(), generalHashMap);
        updateJobProperty("activeJobs", jobHashMap);
    }

}
