package de.thedodo24.adventuria.common.player;

import com.arangodb.entity.BaseDocument;
import com.google.common.collect.Lists;
import de.thedodo24.adventuria.common.CommonModule;
import de.thedodo24.adventuria.common.arango.ArangoWritable;
import de.thedodo24.adventuria.common.job.JobType;
import de.thedodo24.adventuria.common.quests.Quest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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
        List<Long> extraQuests = CommonModule.getInstance().getManager().getQuestManager().getFinishedQuests(2,
                CommonModule.getInstance().getManager().getQuestManager().getShuffledQuestList(JobType.GENERAL)).stream().map(Quest::getKey).toList();
        HashMap<String, Long> extraHashMap = new HashMap<>();
        for(Long eq : extraQuests) {
            extraHashMap.put(String.valueOf(eq), 0L);
        }
        values.put("job", new HashMap<String, Object>() {{
            put("activeJobs", new HashMap<String, HashMap<String, Long>>(){{
                put("100", new HashMap<>());
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
        return getActiveJobs().stream().anyMatch(j -> j != 100 && j != 200 && j != 300);
    }

    private List<Integer> getActiveJobs() {
        return ((HashMap<String, HashMap<String, Long>>) getJobProperty("activeJobs")).keySet().stream().map(Integer::parseInt).collect(Collectors.toList());
    }

    private HashMap<String, HashMap<String, Long>> activeJobsParent() {
        return ((HashMap<String, HashMap<String, Long>>) getJobProperty("activeJobs"));
    }

    public JobType getIndividualJob() {
        return JobType.byId(getActiveJobs().stream().filter(j -> j != 100 && j != 200 && j != 300).findAny().get());
    }

    public int getXP(JobType jobType) {
        return Math.toIntExact((long) getJobProperty(jobType.toString()));
    }

    public void setXP(JobType jobType, int xp) {
        updateJobProperty(jobType.toString(), (long) xp);
    }

    public void removeJob(JobType jobType) {
        HashMap<String, HashMap<String, Long>> activeJobParent = activeJobsParent();
        activeJobParent.remove(jobType.getId() + "");
        updateJobProperty("activeJobs", activeJobParent);
    }

    public void addJob(JobType jobType) {
        HashMap<String, HashMap<String, Long>> activeJobParent = activeJobsParent();
        if(!activeJobParent.containsKey(""+ jobType.getId()))
            activeJobParent.put(jobType.getId() + "", new HashMap<>(){{
                List<Quest> shuffledQuestList = CommonModule.getInstance().getManager().getQuestManager().getShuffledQuestList(jobType);
                put(shuffledQuestList.get(0).getKey() + "", 0L);
                put(shuffledQuestList.get(1).getKey() + "", 0L);
            }});
        updateJobProperty("activeJobs", activeJobParent);
    }

    private HashMap<Long, Long> getQuestList(long job) {
        HashMap<String, HashMap<String, Long>> jobHashMap = (HashMap<String, HashMap<String, Long>>) getJobProperty("activeJobs");
        HashMap<String, Long> stringBooleanHashMap = jobHashMap.get(String.valueOf(job));
        HashMap<Long, Long> longBooleanHashMap = new HashMap<>();
        stringBooleanHashMap.keySet().forEach(key -> longBooleanHashMap.put(Long.parseLong(key), stringBooleanHashMap.get(key)));
        return longBooleanHashMap;
    }


    public List<Quest> getJobQuests(JobType type) {
        HashMap<Long, Long> longQuestList = getQuestList(type.getId());
        return longQuestList.keySet().stream().map(l -> CommonModule.getInstance().getManager().getQuestManager().get(l)).toList();
    }

    public void setJobQuests(JobType type, List<Quest> jobQuests) {
        HashMap<String, HashMap<String, Long>> jobHashMap = (HashMap<String, HashMap<String, Long>>) getJobProperty("activeJobs");
        HashMap<String, Long> questHashMap = new HashMap<>();
        for(Quest q : jobQuests) {
            questHashMap.put(q.getKey() + "", 0L);
        }
        jobHashMap.replace(type.getId() + "", questHashMap);
        updateJobProperty("activeJobs", jobHashMap);
    }

    public void addFinishedQuest(Quest quest) {
        HashMap<String, HashMap<String, Long>> jobHashMap = (HashMap<String, HashMap<String, Long>>) getJobProperty("activeJobs");
        HashMap<String, Long> questList = jobHashMap.get(JobType.GENERAL.getId() + "");
        questList.put(quest.getKey() + "", -1L);
        jobHashMap.replace(JobType.GENERAL.getId() + "", questList);
        updateJobProperty("activeJobs", jobHashMap);
    }

    public void setQuestProgress(Quest quest, long progress) {
        HashMap<String, HashMap<String, Long>> jobHashMap = (HashMap<String, HashMap<String, Long>>) getJobProperty("activeJobs");
        HashMap<String, Long> questList = jobHashMap.get(quest.getJobType().getId() + "");
        questList.replace(quest.getKey() + "", progress);
        jobHashMap.replace(quest.getJobType().getId() + "", questList);
        updateJobProperty("activeJobs", jobHashMap);
    }

    public void addQuestProgress(Quest quest, long progress) {
        HashMap<String, HashMap<String, Long>> jobHashMap = (HashMap<String, HashMap<String, Long>>) getJobProperty("activeJobs");
        HashMap<String, Long> questList = jobHashMap.get(quest.getJobType().getId() + "");
        questList.replace(quest.getKey() + "", questList.get(quest.getKey() + "") + progress);
        jobHashMap.replace(quest.getJobType().getId() + "", questList);
        updateJobProperty("activeJobs", jobHashMap);
    }

    public long getQuestProgress(Quest quest) {
        HashMap<String, HashMap<String, Long>> jobHashMap = (HashMap<String, HashMap<String, Long>>) getJobProperty("activeJobs");
        HashMap<String, Long> questList = jobHashMap.get("" + quest.getJobType().getId());
        if(questList != null) {
            if (questList.containsKey(quest.getKey() + "")) {
                return questList.get(quest.getKey() + "");
            }
        }
        return 0L;
    }

    public boolean checkFinishedQuest(Quest quest) {
        HashMap<String, HashMap<String, Long>> jobHashMap = (HashMap<String, HashMap<String, Long>>) getJobProperty("activeJobs");
        HashMap<String, Long> questList = jobHashMap.get("" + quest.getJobType().getId());
        if(questList != null) {
            if (questList.containsKey(quest.getKey() + "")) {
                return questList.get(quest.getKey() + "") == -1L;
            }
        }
        return false;
    }

    public void clearFinishedQuests() {
        HashMap<String, HashMap<String, Long>> jobHashMap = (HashMap<String, HashMap<String, Long>>) getJobProperty("activeJobs");
        jobHashMap.replace("" + JobType.GENERAL.getId(), new HashMap<>());
        updateJobProperty("activeJobs", jobHashMap);
    }

}
