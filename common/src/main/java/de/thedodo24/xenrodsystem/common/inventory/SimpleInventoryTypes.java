package de.thedodo24.xenrodsystem.common.inventory;

import de.thedodo24.xenrodsystem.common.utils.ItemBuilder;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public enum SimpleInventoryTypes {

    JOB_MENU(new ItemBuilder(new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1)).modify().setDisplayName(" ").setKey("JOB_MENU").build()),
    JOB_GET_NEW(new ItemBuilder(new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1)).modify().setDisplayName(" ").setKey("JOB_GET_NEW").build()),
    QUEST_MENU(new ItemBuilder(new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1)).modify().setDisplayName(" ").setKey("QUEST_MENU").build());

    @Getter
    private final ItemStack checkItemStack;


    SimpleInventoryTypes(ItemStack checkItemStack) {
        this.checkItemStack = checkItemStack;
    }

    public static List<String> getKeys() {
        return Arrays.stream(values()).map(Enum::toString).toList();
    }

}
