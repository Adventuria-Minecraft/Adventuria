package de.thedodo24.xenrodsystem.common.listener;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import de.thedodo24.xenrodsystem.common.CommonModule;
import de.thedodo24.xenrodsystem.common.player.CustomScoreboardType;
import de.thedodo24.xenrodsystem.common.player.User;
import de.thedodo24.xenrodsystem.common.utils.ManagerScoreboard;
import de.thedodo24.xenrodsystem.common.utils.Ontime;
import de.thedodo24.xenrodsystem.common.utils.SkullItems;
import de.thedodo24.xenrodsystem.common.utils.TimeFormat;
import net.ess3.api.events.AfkStatusChangeEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class PlayerListener implements Listener {


     @EventHandler(priority = EventPriority.HIGHEST)
     public void onJoin(PlayerJoinEvent e) {
         Player p = e.getPlayer();
         User u = CommonModule.getInstance().getManager().getUserManager().getOrGenerate(p.getUniqueId());
         u.setName(p.getName());
         CommonModule.getInstance().playerOnline.put(p.getUniqueId(), System.currentTimeMillis());
         Ontime.checkTime();
         if(!u.isSetProperty("scoreboard")) {
             u.getValues().put("scoreboard", new HashMap<String, HashMap<String, String>>() {{
                 put("0", new HashMap<>() {{
                     put("type", CustomScoreboardType.MONEY.toString());
                     put("value", "");
                 }});
                 put("1", new HashMap<>() {{
                     put("type", CustomScoreboardType.ONLINE.toString());
                     put("value", "");
                 }});
             }});
         } else {
             if(!((Map<String, Map<String, String>>) u.getProperty("scoreboard")).containsKey("0")) {
                 u.getValues().replace("scoreboard", new HashMap<String, HashMap<String, String>>() {{
                     put("0", new HashMap<>() {{
                         put("type", CustomScoreboardType.MONEY.toString());
                         put("value", "");
                     }});
                     put("1", new HashMap<>() {{
                         put("type", CustomScoreboardType.ONLINE.toString());
                         put("value", "");
                     }});
                 }});
             }
         }
         new ManagerScoreboard(p);
         ManagerScoreboard.getScoreboardMap().forEach((key, val) -> val.sendScoreboard(Bukkit.getPlayer(key)));
     }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(e.getWhoClicked() instanceof Player p) {
            ItemStack item = e.getCurrentItem();
            if(item != null) {
                if(item.hasItemMeta()) {
                    if(e.getSlotType() != InventoryType.SlotType.OUTSIDE) {
                        InventoryView inv = p.getOpenInventory();
                        JsonElement titleElement = GsonComponentSerializer.gson().serializeToTree(inv.title()).getAsJsonObject().get("text");
                        if(titleElement != null) {
                            String title = titleElement.getAsString();
                            if ("§aWähle einen Spieler aus:".equals(title)) {
                                e.setCancelled(true);
                                if (item.getItemMeta().hasDisplayName()) {
                                    String displayName = GsonComponentSerializer.gson().serializeToTree(item.getItemMeta().displayName()).getAsJsonObject().get("text").getAsString();
                                    String playerName = displayName.substring(2);
                                    Inventory inventory = Bukkit.createInventory(null, 9, Component.text("§a» " + playerName));
                                    User u = CommonModule.getInstance().getManager().getUserManager().getByName(playerName);
                                    Map<String, Long> ontimeHistoryMap = u.getOntimeHistoryMap();
                                    Map<String, Long> afkHistoryMap = u.getAfkTimeHistoryMap();
                                    for (int i = 0; i < 10; i++) {
                                        int week = 9 - i;
                                        if (ontimeHistoryMap.containsKey(String.valueOf(week)) && afkHistoryMap.containsKey(String.valueOf(week))) {
                                            String ontime = TimeFormat.getInDays(ontimeHistoryMap.get(String.valueOf(week)));
                                            String afkTime;
                                            if (afkHistoryMap.containsKey(String.valueOf(week + 1))) {
                                                afkTime = TimeFormat.getInDays(afkHistoryMap.get(String.valueOf(week)) - afkHistoryMap.get(String.valueOf(week + 1)));
                                            } else {
                                                afkTime = TimeFormat.getInDays(afkHistoryMap.get(String.valueOf(week)));
                                            }
                                            inventory.setItem(i,
                                                    SkullItems.getNumberSkull(week,
                                                            (week == 1 ? "§aLetzte Woche" : (week == 2 ? "§aVorletzte Woche" : "§aVor " + week + " Wochen")),
                                                            Lists.newArrayList("§7» Ontime: §a" + ontime,
                                                                    "§7» AFK-Zeit: §a" + afkTime)));
                                        }
                                    }
                                    p.openInventory(inventory);
                                    p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                                }
                                return;
                            }
                            if(title.startsWith("§a» ")) {
                                e.setCancelled(true);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
     public void onAfkStatus(AfkStatusChangeEvent e) {
         Player p = e.getAffected().getBase();
         boolean afk = e.getValue();
         User u = CommonModule.getInstance().getManager().getUserManager().get(p.getUniqueId());
         if(afk)
             CommonModule.getInstance().getAfkPlayer().put(p.getUniqueId(), System.currentTimeMillis());
         else
             if(CommonModule.getInstance().getAfkPlayer().containsKey(p.getUniqueId())) {
                 long afkTime = System.currentTimeMillis() - CommonModule.getInstance().getAfkPlayer().get(p.getUniqueId());
                 CommonModule.getInstance().getAfkPlayer().remove(p.getUniqueId());
                 u.updateAfkTime(afkTime);
                 if(CommonModule.getInstance().getPlayerOnline().containsKey(p.getUniqueId()))
                     CommonModule.getInstance().getPlayerOnline().replace(p.getUniqueId(), CommonModule.getInstance().getPlayerOnline().get(p.getUniqueId()) + afkTime);
             }
     }

     @EventHandler (priority = EventPriority.HIGHEST)
     public void onQuit(PlayerQuitEvent e) {
         Ontime.checkTime();
         Player p = e.getPlayer();
         User u = CommonModule.getInstance().getManager().getUserManager().get(p.getUniqueId());
         if(CommonModule.getInstance().getPlayerOnline().containsKey(p.getUniqueId())) {
             long ontime = System.currentTimeMillis() - CommonModule.getInstance().getPlayerOnline().get(p.getUniqueId());
             if(CommonModule.getInstance().getAfkPlayer().containsKey(p.getUniqueId())) {
                 long afkTime = System.currentTimeMillis() - CommonModule.getInstance().getAfkPlayer().get(p.getUniqueId());
                 ontime -= afkTime;
                 CommonModule.getInstance().getAfkPlayer().remove(p.getUniqueId());
                 u.updateAfkTime(afkTime);
             }
             u.updateOntime(ontime);
             CommonModule.getInstance().getPlayerOnline().remove(p.getUniqueId());
             CommonModule.getInstance().getManager().getUserManager().update(u);
         }
     }



}
