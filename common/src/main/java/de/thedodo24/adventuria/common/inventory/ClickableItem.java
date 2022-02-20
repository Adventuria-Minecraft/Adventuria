package de.thedodo24.adventuria.common.inventory;

import de.thedodo24.adventuria.common.CommonModule;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;

@Getter
public class ClickableItem {

    private ItemStack itemStack;
    private SimpleInventory nextInventory;

    @Getter
    private static HashMap<String, ClickableItem> clickableItems = new HashMap<>();

    public ClickableItem(ItemStack stack, String key, SimpleInventory nextInventory) {
        this.itemStack = stack;
        ItemMeta meta = itemStack.getItemMeta();
        meta.getPersistentDataContainer().set(CommonModule.getInstance().getClickableItemKey(), PersistentDataType.STRING, key);
        itemStack.setItemMeta(meta);
        this.nextInventory = nextInventory;
        clickableItems.put(key, this);
    }

}
