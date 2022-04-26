package de.thedodo24.adventuria.common.job;

import com.google.common.collect.Lists;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Getter
public enum JobType {


    WOOD("Holzfäller", 1L),
    FISH("Fischer", 2L),
    HUNT("Jäger", 3L),
    BUTCHER("Metzger", 4L),
    MINER("Bergarbeiter", 5L),
    GENERAL("Allgemein", 100L),
    EXTRA("Extra", 200L),
    EVENT("Event", 300L);


    private String name;
    private Long id;

    JobType(String name, Long id) {
        this.name = name;
        this.id = id;
    }

    public static JobType byId(int id) {
        Optional<JobType> optional = jobTypes().stream().filter(type -> type.getId() == id).findAny();
        return optional.orElse(GENERAL);
    }

    public static JobType fromString(String s) {
        return switch (s.toLowerCase()) {
            case "wood" -> WOOD;
            case "fish" -> FISH;
            case "hunt" -> HUNT;
            case "butcher" -> BUTCHER;
            case "miner" -> MINER;
            default -> null;
        };
    }

    public static List<JobType> jobTypes() {
        return Arrays.asList(values());
    }

    public static List<JobType> realJobTypes() {
        return Arrays.stream(values()).filter(j -> j != EXTRA && j != EVENT && j != GENERAL).toList();
    }

    public boolean isRealJob() {
        return realJobTypes().contains(this);
    }
}
