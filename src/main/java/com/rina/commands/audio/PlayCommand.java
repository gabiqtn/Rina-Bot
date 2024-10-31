package com.rina.commands.audio;

import com.rina.config.CommandHandler;
import com.rina.commands.audio.config.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class PlayCommand implements CommandHandler {

    @Override
    public void onSlashCommandEvent(SlashCommandInteractionEvent event) {
        final TextChannel channel = event.getChannel().asTextChannel();
        final Member self = event.getGuild().getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        if(selfVoiceState.inAudioChannel()) {
            channel.sendMessage("I'm already in a voice channel.").queue();
            return;
        }

        final Member member = event.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if(!memberVoiceState.inAudioChannel()) {
            channel.sendMessage("You need to be in a voice channel for this command work.").queue();
            return;
        }

        final AudioManager audioManager = event.getGuild().getAudioManager();
        final VoiceChannel memberChannel = memberVoiceState.getChannel().asVoiceChannel();
        audioManager.openAudioConnection(memberChannel);


        event.getJDA().getDirectAudioController().connect(memberChannel);
        PlayerManager manager = PlayerManager.getInstance();
        manager.loadAndPlay(event.getChannel().asTextChannel(), "https://www.youtube.com/watch?v=eK980efAnAk");
        manager.getGuildMusicManager(event.getGuild());


        channel.sendMessage(String.format("Connecting and Playng in `%s`", memberChannel.getName())).queue();
    }

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String getDescription() {
        return "🎶 Play Beats Lofi Radio 24/7";
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
