package de.thedodo24.adventuria.common.quests;

import com.arangodb.entity.BaseDocument;
import com.google.common.collect.Lists;
import de.thedodo24.adventuria.common.CommonModule;
import de.thedodo24.adventuria.common.arango.ArangoWritable;
import de.thedodo24.adventuria.common.job.JobType;
import de.thedodo24.adventuria.common.player.User;
import de.thedodo24.adventuria.common.utils.Language;
import it.unimi.dsi.fastutil.Hash;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class Quest implements ArangoWritable<Long> {

    private Long key;

    Map<String, Object> values = new HashMap<>();

    private static HashMap<Long, CollectQuest> collectQuestMap = new HashMap<>();
    private static HashMap<Long, EntityQuest> entityQuestMap = new HashMap<>();

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

    public List<String> getQuestLore(User user, boolean finished) {
        List<String> lore = Lists.newArrayList("", "§9» Aufgabe:", getQuestText());
        if(!finished) {
            if (getQuestType().equals(QuestType.COLLECT)) {
                CollectQuest cq = toCollectQuest();
                if (cq.isCountedAsAll()) {
                    lore.add((user.getQuestProgress(this) >= cq.getCollectMap().get(cq.getCollectMap().keySet().stream().findFirst().get()) ? "§a" : "§7") + user.getQuestProgress(this) + "/" + cq.getCollectMap().get(cq.getCollectMap().keySet().stream().findFirst().get()) + (user.getQuestProgress(this) >= cq.getCollectMap().get(cq.getCollectMap().keySet().stream().findFirst().get()) ? " §a✓" : "§7"));
                } else {
                    cq.getCollectMap().keySet().forEach(m -> lore.add((user.getQuestProgress(this, m) >= cq.getCollectMap().get(m) ? "§a" : "§7") + Language.getMaterials().get(m.translationKey()) + ": " + user.getQuestProgress(this, m) + "/" + cq.getCollectMap().get(m) + (user.getQuestProgress(this, m) >= cq.getCollectMap().get(m) ? " §a✓" : "§7")));
                }
            } else if(getQuestType().equals(QuestType.ENTITY)) {
                EntityQuest eq = toEntityQuest();
                if(eq.getEntityQuestType().equals(EntityQuests.KILL)) {
                    if (eq.isCountAsAll()) {
                        lore.add((user.getQuestProgress(this) >= eq.getEntityHashMap().get(eq.getEntityHashMap().keySet().stream().findFirst().get()) ? "§a" : "§7") + user.getQuestProgress(this) + "/" + eq.getEntityHashMap().get(eq.getEntityHashMap().keySet().stream().findFirst().get()) + (user.getQuestProgress(this) >= eq.getEntityHashMap().get(eq.getEntityHashMap().keySet().stream().findFirst().get()) ? " §a✓" : "§7"));
                    } else {
                        eq.getEntityHashMap().keySet().forEach(m -> lore.add((user.getQuestProgress(this, m) >= eq.getEntityHashMap().get(m) ? "§a" : "§7") + Language.getMaterials().get(m.translationKey()) + ": " + user.getQuestProgress(this, m) + "/" + eq.getEntityHashMap().get(m) + (user.getQuestProgress(this, m) >= eq.getEntityHashMap().get(m) ? " §a✓" : "§7")));
                    }
                } else {
                    lore.add("§c× Nicht abgeschlossen");
                }
            }
        } else {
            lore.add("§a✓ Abgeschlossen");
        }
        return lore;
    }

    public QuestType getQuestType() {
        return QuestType.valueOf((String) values.get("type"));
    }

    public CollectQuest toCollectQuest() {
        if(collectQuestMap.containsKey(getKey())) {
            return collectQuestMap.get(getKey());
        } else {
            HashMap<String, Long> collectMap = (HashMap<String, Long>) getValues().get("collectMap");
            HashMap<Material, Long> collectMaterialMap = new HashMap<>();
            collectMap.forEach((s, i) -> collectMaterialMap.put(Material.getMaterial(s), i));
            CollectQuest cq = new CollectQuest(key, getJobType(), collectMaterialMap, (boolean) getValues().get("countAsAll"),
                    CollectQuests.valueOf((String) getValues().get("collectQuestType")),
                    getQuestText());
            collectQuestMap.put(getKey(), cq);
            return cq;
        }

    }

    public EntityQuest toEntityQuest() {
        if(entityQuestMap.containsKey(getKey())) {
            return entityQuestMap.get(getKey());
        } else {
            HashMap<String, Long> collectMap = (HashMap<String, Long>) getValues().get("collectMap");
            HashMap<EntityType, Long> collectEntityMap = new HashMap<>();
            collectMap.forEach((k, v) -> collectEntityMap.put(EntityType.valueOf(k), v));
            EntityQuest eq = new EntityQuest(key, getJobType(), getQuestText(), EntityQuests.valueOf((String) getValues().get("entityQuestType")), collectEntityMap, (boolean) getValues().get("countAsAll"));
            entityQuestMap.put(getKey(), eq);
            return eq;
        }
    }

}
