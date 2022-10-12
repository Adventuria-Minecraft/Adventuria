package de.thedodo24.xenrodsystem.common.utils;

import de.thedodo24.xenrodsystem.common.CommonModule;

import java.util.Date;
import java.util.logging.Level;

public class Ontime {

    public static void checkTime() {
        final long currentTime = System.currentTimeMillis();
        if(currentTime >= CommonModule.getInstance().getNextWeek()) {
            CommonModule.getInstance().getManager().getUserManager().getUsers().forEach(user -> {
                if(CommonModule.getInstance().getPlayerOnline().containsKey(user.getKey())) {
                    long ontime = currentTime - CommonModule.getInstance().getPlayerOnline().get(user.getKey());
                    long sinceMidnight = currentTime - CommonModule.getInstance().getNextWeek();
                    long afkTime = 0;
                    if(CommonModule.getInstance().getAfkPlayer().containsKey(user.getKey())){
                        afkTime = currentTime - CommonModule.getInstance().getAfkPlayer().get(user.getKey());
                        CommonModule.getInstance().getAfkPlayer().replace(user.getKey(), currentTime);
                    }
                    long beforeMidnightAFK = 0;
                    if((afkTime - sinceMidnight) > 0)
                        beforeMidnightAFK = afkTime - sinceMidnight;
                    else
                        sinceMidnight -= afkTime;
                    user.updateOntime(ontime - afkTime);
                    user.addOntimeHistory(user.getWeekOntime(), user.getAfkTime() + beforeMidnightAFK);
                    user.setWeekOntime(sinceMidnight);
                    user.setDayOntime(sinceMidnight);
                    user.updateAfkTime(afkTime);
                    CommonModule.getInstance().getPlayerOnline().replace(user.getKey(), currentTime);
                } else {
                    user.addOntimeHistory(user.getWeekOntime(), user.getAfkTime());
                    user.setWeekOntime(0);
                    user.setDayOntime(0);
                }
            });
            CommonModule.getInstance().setTimes();
        } else if(currentTime >= CommonModule.getInstance().getNextDay()) {
            CommonModule.getInstance().getManager().getUserManager().getUsers().forEach(user -> {
                if(CommonModule.getInstance().getPlayerOnline().containsKey(user.getKey())) {
                    long ontime = currentTime - CommonModule.getInstance().getPlayerOnline().get(user.getKey());
                    long sinceMidnight = currentTime - CommonModule.getInstance().getNextDay();
                    long afkTime = 0;
                    if(CommonModule.getInstance().getAfkPlayer().containsKey(user.getKey())){
                        afkTime = currentTime - CommonModule.getInstance().getAfkPlayer().get(user.getKey());
                        CommonModule.getInstance().getAfkPlayer().replace(user.getKey(), currentTime);
                    }
                    if((sinceMidnight - afkTime) > 0)
                        sinceMidnight -= afkTime;
                    user.updateOntime(ontime - afkTime);
                    user.setDayOntime(sinceMidnight);
                    user.updateAfkTime(afkTime);
                    CommonModule.getInstance().getPlayerOnline().replace(user.getKey(), currentTime);
                } else {
                    user.setDayOntime(0);
                }
            });
            CommonModule.getInstance().getPlugin().getLogger().log(Level.INFO, "Setted next day to " + CommonModule.getInstance().getDateFormat().format(new Date(CommonModule.getInstance().setNextDay())));
        }
    }
}
