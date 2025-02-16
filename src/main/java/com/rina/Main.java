package com.rina;

import com.rina.config.CommandManager;
import com.rina.config.EventListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.util.*;

public class Main extends ListenerAdapter {

    private static final List<Activity> activities = Arrays.asList(
            Activity.listening("\uD83C\uDFB6 Beats Lofi Radio 24/7 in YouTube!"),
            Activity.playing("\u2615 Use /play to start you music!"),
            Activity.playing("\u2728 youtube.com/@beatslofiyt")
    );

    public static void main(String[] args) {
        JDA bot = JDABuilder.create("YOUR_BOT_TOKEN",
                EnumSet.allOf(GatewayIntent.class)).enableCache(CacheFlag.VOICE_STATE).build();
        System.out.println("Instance online in " + bot.getSelfUser().getName());

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            private int currentActivityIndex = 0;

            @Override
            public void run() {
                bot.getPresence().setActivity(activities.get(currentActivityIndex));

                currentActivityIndex = (currentActivityIndex + 1) % activities.size();
            }
        }, 0, 5000);

        bot.addEventListener(new EventListener(), new CommandManager(bot));
    }
}
