package de.thedodo24.xenrodsystem.common.inventory;

import de.thedodo24.xenrodsystem.common.CommonModule;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.UUID;

@Getter
public class ClickableItem {

    private ItemStack itemStack;
    private SimpleInventory nextInventory;

    @Getter
    private static HashMap<String, HashMap<String, ClickableItem>> clickableItems = new HashMap<>();

    public ClickableItem(ItemStack stack, String key, SimpleInventory nextInventory, UUID uuid) {
        this.itemStack = stack;
        ItemMeta meta = itemStack.getItemMeta();
        meta.getPersistentDataContainer().set(CommonModule.getInstance().getClickableItemKey(), PersistentDataType.STRING, key);
        itemStack.setItemMeta(meta);
        this.nextInventory = nextInventory;
        if(clickableItems.containsKey(uuid.toString())) {
            HashMap<String, ClickableItem> clickableItemsList = clickableItems.get(uuid.toString());
            clickableItemsList.put(key, this);
            clickableItems.replace(uuid.toString(), clickableItemsList);
        } else {
            HashMap<String, ClickableItem> clickableItemHashMap = new HashMap<>();
            clickableItemHashMap.put(key, this);
            clickableItems.put(uuid.toString(), clickableItemHashMap);
        }
    }

}
