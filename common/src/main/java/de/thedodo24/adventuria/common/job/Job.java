package de.thedodo24.adventuria.common.job;

import com.arangodb.entity.BaseDocument;
import de.thedodo24.adventuria.common.CommonModule;
import de.thedodo24.adventuria.common.arango.ArangoWritable;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class Job implements ArangoWritable<Integer> {

    private Integer key;

    private Map<String, Object> values = new HashMap<>();

    public Job(Integer key, String name, JobType type) {
        this.key = key;
        values.put("name", name);
        values.put("type", type.toString());
    }


    @Override
    public Integer getKey() {
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


}
