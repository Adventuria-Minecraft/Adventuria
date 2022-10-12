package de.thedodo24.xenrodsystem.common.inventory;

import com.google.common.collect.Lists;
import de.thedodo24.xenrodsystem.common.CommonModule;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Getter
public class SimpleInventory {

    private UUID inventoryKey;
    private Inventory inventory;
    private SimpleInventoryTypes simpleInventoryType;
    private final List<ClickableItem> clickableItems = Lists.newArrayList();

    public SimpleInventory(String title, InventoryType inventoryType, InventoryHolder holder, SimpleInventoryTypes simpleInventoryType) {
        inventoryKey = UUID.randomUUID();
        this.simpleInventoryType = simpleInventoryType;
        InventoryManager.getSimpleInventories().put(inventoryKey, this);
        inventory = Bukkit.getServer().createInventory(holder, inventoryType, Component.text(title));
    }

    public SimpleInventory(String title, int size, InventoryHolder holder, SimpleInventoryTypes simpleInventoryType) {
        inventoryKey = UUID.randomUUID();
        this.simpleInventoryType = simpleInventoryType;
        InventoryManager.getSimpleInventories().put(inventoryKey, this);
        inventory = Bukkit.getServer().createInventory(holder, size, Component.text(title));
    }

    public void delete() {
        InventoryManager.getSimpleInventories().remove(inventoryKey);
    }

    public void insertGlassPane(int[] places, UUID inventoryKey) {
        for (int place : places) {
            inventory.setItem(place, getCheckItemStack(inventoryKey));
        }
    }

    public ItemStack getCheckItemStack(UUID inventoryKey) {
        ItemStack stack = simpleInventoryType.getCheckItemStack();
        ItemMeta meta = stack.getItemMeta();
        NamespacedKey key = CommonModule.getInstance().getInventoryKey();
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, inventoryKey.toString());
        stack.setItemMeta(meta);
        return stack;
    }

    public void addClickableItem(ClickableItem item) {
        clickableItems.add(item);
    }

    public ClickableItem getClickableItem(String key) {
        Optional<ClickableItem> optional =  clickableItems.stream()
                .filter(i -> i.getItemStack().hasItemMeta())
                .filter(i -> i.getItemStack().getItemMeta().getPersistentDataContainer().has(CommonModule.getInstance().getClickableItemKey()))
                .filter(i -> i.getItemStack().getItemMeta().getPersistentDataContainer().get(CommonModule.getInstance().getClickableItemKey(), PersistentDataType.STRING).equalsIgnoreCase(key))
                .findFirst();
        return optional.orElse(null);
    }

}
