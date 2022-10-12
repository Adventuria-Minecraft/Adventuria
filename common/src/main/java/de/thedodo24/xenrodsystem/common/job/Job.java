package de.thedodo24.xenrodsystem.common.job;

import com.arangodb.entity.BaseDocument;
import com.google.common.collect.Lists;
import de.thedodo24.xenrodsystem.common.CommonModule;
import de.thedodo24.xenrodsystem.common.arango.ArangoWritable;
import de.thedodo24.xenrodsystem.common.quests.Quest;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class Job implements ArangoWritable<Long> {

    private Long key;

    private Map<String, Object> values = new HashMap<>();

    public Job(Long key, String name, JobType type) {
        this.key = key;
        values.put("name", name);
        values.put("type", type.toString());
        if(type.equals(JobType.GENERAL)) {
            List<Quest> shuffledQuestList = CommonModule.getInstance().getManager().getQuestManager().getShuffledQuestList(JobType.GENERAL);
            values.put("activeQuests", CommonModule.getInstance().getManager().getQuestManager().getFinishedQuests(5, shuffledQuestList).stream().map(Quest::getKey).toList());
        }
    }
    @Override
    public Long getKey() {
        return key;
    }

    @Override
    public void read(BaseDocument document) {
        this.values = document.getProperties();
    }

    @Override
    public void save(BaseDocument document) {
        document.setProperties(values);
    }

    public <Type> void updateProperty(String property, Type value) {
        this.updateProperty$(property, value);
    }

    private <Type> void  updateProperty$(String property, Type value) {
        if(this.values.containsKey(property))
            this.values.replace(property, value);
        else
            this.values.put(property, value);
        CommonModule.getInstance().getManager().getJobManager().save(this);
    }

    public void deleteProperty(String property) {
        this.values.remove(property);
        CommonModule.getInstance().getManager().getJobManager().save(this);
    }

    public boolean isSetProperty(String property) {
        return this.values.containsKey(property);
    }

    public String getName() {
        return (String) values.get("name");
    }

    public JobType getType() {
        return JobType.fromString((String) values.get("type"));
    }

    public List<Quest> getActiveQuests() {
        if(isSetProperty("activeQuests")) {
            List<Long> longList = (List<Long>) values.get("activeQuests");
            return longList.stream().map(l -> CommonModule.getInstance().getManager().getQuestManager().get(l)).toList();
        }
        return Lists.newArrayList();
    }

    public void replaceActiveQuests(List<Quest> quests) {
        if(isSetProperty("activeQuests")) {
            updateProperty("activeQuests", quests.stream().map(Quest::getKey).toList());
        }
    }
}
