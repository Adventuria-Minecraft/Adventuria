package de.thedodo24.xenrodsystem.common.utils;

import de.thedodo24.xenrodsystem.common.job.JobType;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.UUID;

public class SkullItems {

    public static ItemStack getPlayerHead(OfflinePlayer p) {
        return getHead(p);
    }

    private static ItemStack get(String b64) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
        UUID hash = new UUID(b64.hashCode(), b64.hashCode());
        return Bukkit.getUnsafe().modifyItemStack(item, "{SkullOwner:{Id:\"" + hash + "\",Properties:{textures:[{Value:\"" + b64 + "\"}]}}}");
    }

    private static ItemStack setDisplayName(ItemStack itemStack, String displayName) {
        ItemMeta m = itemStack.getItemMeta();
        m.displayName(Component.text(displayName));
        itemStack.setItemMeta(m);
        return itemStack;
    }

    private static ItemStack setDisplayName(ItemStack itemStack, String displayName, List<String> lore) {
        ItemMeta m = itemStack.getItemMeta();
        m.displayName(Component.text(displayName));

        List<Component> loreFinal = lore.stream().map(s -> Component.text(s).asComponent()).toList();
        m.lore(loreFinal);
        itemStack.setItemMeta(m);
        return itemStack;
    }

    private static ItemStack getHead(OfflinePlayer p) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.displayName(Component.text("§2" + p.getName()));
        meta.setOwningPlayer(p);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getNumberSkull(int week, String displayName, List<String> lore) {
        return switch (week) {
            case 1 -> getGreenOneSkull(displayName, lore);
            case 2 -> getGreenTwoSkull(displayName, lore);
            case 3 -> getGreenThreeSkull(displayName, lore);
            case 4 -> getGreenFourSkull(displayName, lore);
            case 5 -> getGreenFiveSkull(displayName, lore);
            case 6 -> getGreenSixSkull(displayName, lore);
            case 7 -> getGreenSevenSkull(displayName, lore);
            case 8 -> getGreenEightSkull(displayName, lore);
            case 9 -> getGreenNineSkull(displayName, lore);
            default -> new ItemStack(Material.AIR);
        };
    }

    public static ItemStack getGreenPlusSkull(String displayName) {
        String b64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWZmMzE0MzFkNjQ1ODdmZjZlZjk4YzA2NzU4MTA2ODFmOGMxM2JmOTZmNTFkOWNiMDdlZDc4NTJiMmZmZDEifX19";
        ItemStack skull = get(b64);
        return setDisplayName(skull, displayName);
    }

    public static ItemStack getOrangeNewJob(String displayName) {
        String b64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTE0YzI2Y2U3ODU1ZDY1MjJiZmE4NmI3N2E3NGE5ZmRjNzkxMjc1MTNiMmI3OGFiZTQ3ODFhZDY2Njc2MTMzIn19fQ==";
        ItemStack skull = get(b64);
        return setDisplayName(skull, displayName);
    }

    public static ItemStack getRedPlusSkull(String displayName) {
        String b64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWM3MzFjM2M3MjNmNjdkMmNmYjFhMTE5MmI5NDcwODZmYmEzMmFlYTQ3MmQzNDdhNWVkNWQ3NjQyZjczYiJ9fX0=";
        ItemStack skull = get(b64);
        return setDisplayName(skull, displayName);
    }

    public static ItemStack getMoneySkull(String displayName) {
        String b64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmQwMDVlYmJmOTgxN2Q2OTI4ZGU4YmM1ZjdkMWMzODkyNzYwMjBhYzg3NjQ3ZDI4YWI4Zjk5ZWIzOWZmZGU3NiJ9fX0=";
        ItemStack skull = get(b64);
        return setDisplayName(skull, displayName);
    }

    public static ItemStack getOrangePlusSkull(String displayName) {
        String b64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTI1MGIzY2NlNzY2MzVlZjRjN2E4OGIyYzU5N2JkMjc0OTg2OGQ3OGY1YWZhNTY2MTU3YzI2MTJhZTQxMjAifX19";
        ItemStack skull = get(b64);
        return setDisplayName(skull, displayName);
    }

    public static ItemStack getOrangeMinusSkull(String displayName) {
        String b64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWNmZjkxZGM5OWQ1ODI4MDIzZWVkZjg3Mzc5OWQyNTUzNWRhZGU2NGEyZTE2YTNiNDk4YjQxMTNlYWZkNDk2NiJ9fX0=";
        ItemStack skull = get(b64);
        return setDisplayName(skull, displayName);
    }

    public static ItemStack getRedMinusSkull(String displayName, List<String> lore) {
        String b64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGU0YjhiOGQyMzYyYzg2NGUwNjIzMDE0ODdkOTRkMzI3MmE2YjU3MGFmYmY4MGMyYzViMTQ4Yzk1NDU3OWQ0NiJ9fX0=";
        ItemStack skull = get(b64);
        return setDisplayName(skull, displayName, lore);
    }

    public static ItemStack getArrowUpSkull(String displayName) {
        String b64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWRhMDI3NDc3MTk3YzZmZDdhZDMzMDE0NTQ2ZGUzOTJiNGE1MWM2MzRlYTY4YzhiN2JjYzAxMzFjODNlM2YifX19";
        ItemStack skull = get(b64);
        return setDisplayName(skull, displayName);
    }

    public static ItemStack getArrowDownSkull(String displayName) {
        String b64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTM4NTJiZjYxNmYzMWVkNjdjMzdkZTRiMGJhYTJjNWY4ZDhmY2E4MmU3MmRiY2FmY2JhNjY5NTZhODFjNCJ9fX0=";
        ItemStack skull = get(b64);
        return setDisplayName(skull, displayName);
    }

    public static ItemStack getMoneyBagSkull(String displayName) {
        String b64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmY3NWQxYjc4NWQxOGQ0N2IzZWE4ZjBhN2UwZmQ0YTFmYWU5ZTdkMzIzY2YzYjEzOGM4Yzc4Y2ZlMjRlZTU5In19fQ==";
        ItemStack skull = get(b64);
        return setDisplayName(skull, displayName);
    }

    public static ItemStack getBooksSkull(String displayName) {
        String b64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWNkMDgxMTY4Y2E4NjYwNGZjZjM3ODAwMzQ4Y2MxNzJjZTc0MDczOWRiM2NjMDgwZjA3ZjFhN2ZiZGZmZjQ4OSJ9fX0=";
        ItemStack skull = get(b64);
        return setDisplayName(skull, displayName);
    }

    public static ItemStack getArrowRightSkull(String displayName) {
        String b64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjMyY2E2NjA1NmI3Mjg2M2U5OGY3ZjMyYmQ3ZDk0YzdhMGQ3OTZhZjY5MWM5YWMzYTkxMzYzMzEzNTIyODhmOSJ9fX0=";
        ItemStack skull = get(b64);
        return setDisplayName(skull, displayName);
    }

    public static ItemStack getGreenOneSkull(String displayName, List<String> lore) {
        String b64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODg5OTE2OTc0Njk2NTNjOWFmODM1MmZkZjE4ZDBjYzljNjc3NjNjZmU2NjE3NWMxNTU2YWVkMzMyNDZjNyJ9fX0=";
        ItemStack skull = get(b64);
        return setDisplayName(skull, displayName, lore);
    }

    public static ItemStack getGreenTwoSkull(String displayName, List<String> lore) {
        String b64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTQ5NmMxNjJkN2M5ZTFiYzhjZjM2M2YxYmZhNmY0YjJlZTVkZWM2MjI2YzIyOGY1MmViNjVkOTZhNDYzNWMifX19";
        ItemStack skull = get(b64);
        return setDisplayName(skull, displayName, lore);
    }

    public static ItemStack getGreenThreeSkull(String displayName, List<String> lore) {
        String b64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzQyMjZmMmViNjRhYmM4NmIzOGI2MWQxNDk3NzY0Y2JhMDNkMTc4YWZjMzNiN2I4MDIzY2Y0OGI0OTMxMSJ9fX0=";
        ItemStack skull = get(b64);
        return setDisplayName(skull, displayName, lore);
    }

    public static ItemStack getGreenFourSkull(String displayName, List<String> lore) {
        String b64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjkyMGVjY2UxYzhjZGU1ZGJjYTU5MzhjNTM4NGY3MTRlNTViZWU0Y2NhODY2YjcyODNiOTVkNjllZWQzZDJjIn19fQ==";
        ItemStack skull = get(b64);
        return setDisplayName(skull, displayName, lore);
    }

    public static ItemStack getGreenFiveSkull(String displayName, List<String> lore) {
        String b64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTJjMTQyYWY1OWYyOWViMzVhYjI5YzZhNDVlMzM2MzVkY2ZjMmE5NTZkYmQ0ZDJlNTU3MmIwZDM4ODkxYjM1NCJ9fX0=";
        ItemStack skull = get(b64);
        return setDisplayName(skull, displayName, lore);
    }
    public static ItemStack getGreenSixSkull(String displayName, List<String> lore) {
        String b64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjRkZGIwM2FhOGM1ODQxNjhjNjNlY2UzMzdhZWZiMjgxNzc0Mzc3ZGI3MjMzNzI5N2RlMjU4YjRjY2E2ZmJhNCJ9fX0=";
        ItemStack skull = get(b64);
        return setDisplayName(skull, displayName, lore);
    }
    public static ItemStack getGreenSevenSkull(String displayName, List<String> lore) {
        String b64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDdkZTcwYjg4MzY4Y2UyM2ExYWM2ZDFjNGFkOTEzMTQ4MGYyZWUyMDVmZDRhODVhYjI0MTdhZjdmNmJkOTAifX19";
        ItemStack skull = get(b64);
        return setDisplayName(skull, displayName, lore);
    }
    public static ItemStack getGreenEightSkull(String displayName, List<String> lore) {
        String b64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDI2NDdhZTQ3YjZiNTFmNWE0NWViM2RjYWZhOWI4OGYyODhlZGU5Y2ViZGI1MmExNTllMzExMGU2YjgxMThlIn19fQ==";
        ItemStack skull = get(b64);
        return setDisplayName(skull, displayName, lore);
    }
    public static ItemStack getGreenNineSkull(String displayName, List<String> lore) {
        String b64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGFlNDYxYTQ0MzQxOTZkMzcyOTZhZDVhZGY2ZDlkNTc0NGEwNDE1ZGM2MWM0NzVhNmRmYTYyODU4MTQwNTIifX19";
        ItemStack skull = get(b64);
        return setDisplayName(skull, displayName, lore);
    }

    public static ItemStack getJobSkull(JobType jobType, String displayName, List<String> lore) {
        return switch (jobType) {
            case FISH -> getJobFishSkull(displayName, lore);
            case HUNT -> getJobHuntSkull(displayName, lore);
            case WOOD -> getJobWoodSkull(displayName, lore);
            case MINER -> getJobMinerSkull(displayName, lore);
            case BUTCHER -> getJobButcherSkull(displayName, lore);
            default -> getJobAllgemeinSkull(displayName, lore);
        };
    }

    private static ItemStack getJobWoodSkull(String displayName, List<String> lore) {
        String b64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzEyYzM0MTQzYjljNzZhY2ZmY2M0MDAzN2I3MWQ3OGVhYTQ1ODRiNDYwNDUwN2IwN2VhMjRkOTMwYTM2Nzc0MyJ9fX0=";
        ItemStack skull = get(b64);
        return setDisplayName(skull, displayName, lore);
    }

    private static ItemStack getJobFishSkull(String displayName, List<String> lore) {
        String b64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWQ3MzFlMGU4YmYwNjI5NjVlODkwNGU5ZWZjMWZkZTMxM2NmODRkNTMxNDg3YzI2Njg2NDk5MGJkY2NkN2I1NCJ9fX0=";
        ItemStack skull = get(b64);
        return setDisplayName(skull, displayName, lore);
    }

    private static ItemStack getJobHuntSkull(String displayName, List<String> lore) {
        String b64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmFhOTUyOTFmOTNhNzBkYjZmMWFhMWViODQ4MjRiOWRmM2I5ZmMwYTI1NDZmN2ZmODFhMTJjNTY3NjBmZWFhIn19fQ==";
        ItemStack skull = get(b64);
        return setDisplayName(skull, displayName, lore);
    }

    private static ItemStack getJobButcherSkull(String displayName, List<String> lore) {
        String b64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTFjZmM3YzUzYjdiNzBiNTI2ZmIxNWZkYjJiYjM1NzljZDk3YTg5NTRlNzEyODIwYmFiNmJkM2JjYmJhOWM0In19fQ==";
        ItemStack skull = get(b64);
        return setDisplayName(skull, displayName, lore);
    }

    private static ItemStack getJobMinerSkull(String displayName, List<String> lore) {
        String b64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGQzMTZhNjQwOTlkODk2ZjY2OWQwZjA4ODUyMDIxN2E4M2RlY2Q0YTNiNjdlNTdhZjg5YjMzZDIwYzMyMWYzNCJ9fX0=";
        ItemStack skull = get(b64);
        return setDisplayName(skull, displayName, lore);
    }

    private static ItemStack getJobAllgemeinSkull(String displayName, List<String> lore) {
        String b64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTZiYjlmYjk3YmE4N2NiNzI3Y2QwZmY0NzdmNzY5MzcwYmVhMTljY2JmYWZiNTgxNjI5Y2Q1NjM5ZjJmZWMyYiJ9fX0=";
        ItemStack skull = get(b64);
        return setDisplayName(skull, displayName, lore);
    }

    public static ItemStack getGreenCheckMark(String displayName, List<String> lore) {
        String b64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDMxMmNhNDYzMmRlZjVmZmFmMmViMGQ5ZDdjYzdiNTVhNTBjNGUzOTIwZDkwMzcyYWFiMTQwNzgxZjVkZmJjNCJ9fX0=";
        ItemStack skull = get(b64);
        return setDisplayName(skull, displayName, lore);
    }

}

