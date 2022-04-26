package de.thedodo24.adventuria.common.job;

import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDatabase;
import com.arangodb.util.MapBuilder;
import com.arangodb.velocypack.VPackSlice;
import com.google.common.collect.Lists;
import de.thedodo24.adventuria.common.arango.CollectionManager;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Getter
public class JobManager extends CollectionManager<Job, Long> {

    private ArangoDatabase database;

    public JobManager(ArangoDatabase database) {
        super("jobs", (key -> new Job(key, "", JobType.WOOD)), database, ((key, obj) -> false));
        this.database = database;
    }

    public List<Job> getAllJobs() {
        ArangoCursor<VPackSlice> cursor = getDatabase().query("FOR job IN " + this.collection.name() + " RETURN {id: job._key}",
                null, null, VPackSlice.class);
        List<Job> bans = Lists.newArrayList();
        while(cursor.hasNext())
            bans.add(getOrGenerate(Long.parseLong(cursor.next().get("id").getAsString())));
        this.closeCursor(cursor);
        return bans;
    }

    public Job getJob(String s) {
        return getJob(JobType.fromString(s));
    }

    public Job getJob(JobType type) {
        if(type != null) {
            ArangoCursor<VPackSlice> cursor = getDatabase().query("FOR job IN " + this.collection.name() + " FILTER LIKE (job.type, @name, true) RETURN {id: job._key}",
                    new MapBuilder().put("name", type.toString()).get(), null, VPackSlice.class);
            long id = -1;
            while(cursor.hasNext())
                id = Long.parseLong(cursor.next().get("id").getAsString());
            this.closeCursor(cursor);
            if(id >= 0) {
                return get(id);
            }
        }
        return null;
    }

    public void createJobs() {
        JobType.jobTypes().forEach(jobType -> getOrGenerate(jobType.getId(), (key -> new Job(key, jobType.getName(), jobType))));
    }
}
