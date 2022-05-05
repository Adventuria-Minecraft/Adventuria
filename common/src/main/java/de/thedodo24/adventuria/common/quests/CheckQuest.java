package de.thedodo24.adventuria.common.quests;

import de.thedodo24.adventuria.common.arango.ArangoWritable;
import de.thedodo24.adventuria.common.job.JobType;

public class CheckQuest extends Quest implements ArangoWritable<Long> {


    public CheckQuest(Long key, JobType jobType, String questText) {
        super(key, jobType, questText);
        getValues().put("type", QuestType.CHECK.toString());
    }
}
