package de.thedodo24.adventuria.common.quests;

import com.arangodb.entity.BaseDocument;
import de.thedodo24.adventuria.common.CommonModule;
import de.thedodo24.adventuria.common.arango.ArangoWritable;
import de.thedodo24.adventuria.common.job.JobType;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class Quest implements ArangoWritable<Long> {

    private Long key;

    private Map<String, Object> values = new HashMap<>();

    public Quest(Long key, JobType jobType, String questText) {
        this.key = key;
        values.put("job", jobType.toString());
        values.put("questText", questText);
    }

    @Override
    public Long getKey() {
        return key;
    }

    @Override
    public void read(BaseDocument document) {
        values = document.getProperties();
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
        CommonModule.getInstance().getManager().getQuestManager().save(this);
    }

    public void deleteProperty(String property) {
        this.values.remove(property);
        CommonModule.getInstance().getManager().getQuestManager().save(this);
    }

    public boolean isSetProperty(String property) {
        return this.values.containsKey(property);
    }

    public JobType getJobType() {
        return JobType.valueOf((String) getValues().get("job"));
    }

    public String getQuestText() {
        return (String) getValues().get("questText");
    }

}
