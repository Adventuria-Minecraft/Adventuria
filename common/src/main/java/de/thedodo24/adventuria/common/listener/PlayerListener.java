package de.thedodo24.adventuria.common.listener;

import de.thedodo24.adventuria.common.CommonModule;
import de.thedodo24.adventuria.common.player.User;
import de.thedodo24.adventuria.common.utils.Ontime;
import net.ess3.api.events.AfkStatusChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {


     @EventHandler(priority = EventPriority.HIGHEST)
     public void onJoin(PlayerJoinEvent e) {
         Player p = e.getPlayer();
         User u = CommonModule.getInstance().getManager().getUserManager().getOrGenerate(p.getUniqueId());
         u.setName(p.getName());
         CommonModule.getInstance().getPlayerOnline().put(p.getUniqueId(), System.currentTimeMillis());
         Ontime.checkTime();
     }


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
         long ontime = System.currentTimeMillis() - CommonModule.getInstance().getPlayerOnline().get(p.getUniqueId());
         long afkTime = 0;
         if(CommonModule.getInstance().getAfkPlayer().containsKey(p.getUniqueId())) {
             afkTime = System.currentTimeMillis() - CommonModule.getInstance().getAfkPlayer().get(p.getUniqueId());
             ontime -= afkTime;
             CommonModule.getInstance().getAfkPlayer().remove(p.getUniqueId());
         }
         u.updateOntime(ontime);
         u.updateAfkTime(afkTime);
         CommonModule.getInstance().getPlayerOnline().remove(p.getUniqueId());
         CommonModule.getInstance().getManager().getUserManager().update(u);
     }



}
