package com.rina.commands.audio;

import com.rina.config.CommandHandler;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.List;

public class StopCommand implements CommandHandler {

    @Override
    public void onSlashCommandEvent(SlashCommandInteractionEvent event) {
        final TextChannel channel = event.getChannel().asTextChannel();
        final Member self = event.getGuild().getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        if(!selfVoiceState.inAudioChannel()) {
            channel.sendMessage("I'm not on any channel").queue();
            return;
        }

        AudioManager audioManager = event.getGuild().getAudioManager();

        audioManager.closeAudioConnection();
        channel.sendMessage(String.format("I left the channel `%s`", channel.getName())).queue();
    }

    @Override
    public String getName() {
        return "stop";
    }

    @Override
    public String getDescription() {
        return "🎶 Stop Beats Lofi Radio 24/7";
    }

    @Override
    public boolean isSpecificGuild() {
        return false;
    }

    @Override
    public boolean isGuildOnly() {
        return true;
    }
}