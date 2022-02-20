package de.thedodo24.adventuria.common.inventory;

import com.google.common.collect.Lists;
import de.thedodo24.adventuria.common.utils.ItemBuilder;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.UUID;

public enum SimpleInventoryTypes {

    JOB_MENU(new ItemBuilder(new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1)).modify().setDisplayName(" ").setKey("JOB_MENU").build()),
    JOB_GET_NEW(new ItemBuilder(new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1)).modify().setDisplayName(" ").setKey("JOB_GET_NEW").build());

    @Getter
    private final ItemStack checkItemStack;


    SimpleInventoryTypes(ItemStack checkItemStack) {
        this.checkItemStack = checkItemStack;
    }

    public static List<String> getKeys() {
        return Lists.newArrayList("JOB_MENU", "JOB_GET_NEW");
    }

}
