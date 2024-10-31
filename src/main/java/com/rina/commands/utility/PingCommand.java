package com.rina.commands.utility;

import com.rina.config.CommandHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class PingCommand implements CommandHandler {

    @Override
    public void onSlashCommandEvent(SlashCommandInteractionEvent event) {
        JDA jda = event.getJDA();

        jda.getRestPing().queue((ping) -> event.getChannel().sendMessageFormat("# Hello, do you want to see my ping?\n** Reset Ping: `%sms`\n WS ping: `%sms`**", ping, jda.getGatewayPing()).queue());
    }

    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public String getDescription() {
        return "🏓 Pong!";
    }

    @Override
    public boolean isSpecificGuild() {
        return false;
    }

    @Override
    public boolean isGuildOnly() {
        return false;
    }
}
