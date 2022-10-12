package de.thedodo24.xenrodsystem.common.inventory;

import de.thedodo24.xenrodsystem.common.CommonModule;
import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class InventoryManager {

    @Getter
    private static HashMap<UUID, SimpleInventory> simpleInventories = new HashMap<>();

    public static boolean checkInventory(Inventory inv) {
        return Arrays.stream(inv.getContents()).anyMatch(i -> i != null && i.hasItemMeta() && hasKeys(i));
    }

    public static SimpleInventory getSimpleInventory(Inventory inventory) {
        Optional<ItemStack> optional = Arrays.stream(inventory.getContents())
                .filter(i -> i != null && i.hasItemMeta())
                .filter(i -> i.getItemMeta().getPersistentDataContainer().has(CommonModule.getInstance().getInventoryKey()))
                .findAny();
        if(optional.isPresent()) {
            ItemStack controlStack = optional.get();
            UUID inventoryUUID = UUID.fromString(controlStack.getItemMeta().getPersistentDataContainer().get(CommonModule.getInstance().getInventoryKey(), PersistentDataType.STRING));
            if(simpleInventories.containsKey(inventoryUUID))
                return simpleInventories.get(inventoryUUID);
        }
        return null;
    }

    public static boolean hasKeys(ItemStack stack) {
        ItemMeta meta = stack.getItemMeta();
        NamespacedKey namespacedKey = CommonModule.getInstance().getInventoryType();
        if(meta.getPersistentDataContainer().has(namespacedKey)) {
            String metaKey = meta.getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING);
            return SimpleInventoryTypes.getKeys().contains(metaKey);
        }
        return false;
    }


}
