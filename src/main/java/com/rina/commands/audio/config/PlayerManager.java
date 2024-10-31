package com.rina.commands.audio.config;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class PlayerManager {
    private static PlayerManager INSTANCE;
    private final Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager audioPlayerManager;

    public PlayerManager() {
        this.musicManagers = new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();

        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
    }

    public synchronized GuildMusicManager getGuildMusicManager(Guild guild) {
        long guildId = guild.getIdLong();
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(audioPlayerManager);
            musicManagers.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;
    }

    public void loadAndPlay(TextChannel textChannel, String trackUrl) {
        GuildMusicManager musicManager = getGuildMusicManager(textChannel.getGuild());

        audioPlayerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                EmbedBuilder embed = new EmbedBuilder()
                        .setAuthor(track.getInfo().title, null, "https://cdn.discordapp.com/attachments/1185407108288610358/1267348174289240105/f2c19720-0079-448b-b6d4-03d39a7f5f2e.jpeg?ex=66a87576&is=66a723f6&hm=d89b3eb002d22c692dca840d393499dce93894328be48accb5109f4a09974717&")
                        .setColor(Color.PINK)
                        .setDescription("# \\\uD83C\uDFB6 Official Songs from Dreamy Beats.\n**\\\uD83D\uDD14 Youtube Channel:\n- https://www.youtube.com/@beatslofiyt\n\\\uD83C\uDFB6 Listen to Music on Discord:\n https://bit.ly/dreamy-bot\n\\\uD83D\uDCE9 Official Discord Community:\n- https://bit.ly/dreamybeats-community**");
                textChannel.sendMessageEmbeds(embed.build()).queue();
                play(musicManager, track);

            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {

            }

            @Override
            public void noMatches() {
                textChannel.sendMessage("Not find! " + trackUrl).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                if (exception.getMessage().contains("403")) {
                    textChannel.sendMessage("Não foi possível reproduzir o vídeo. Verifique se ele não está ao vivo ou restrito.").queue();
                } else {
                    textChannel.sendMessage("Erro ao carregar a faixa: " + exception.getMessage()).queue();
                }
            }
        });
    }

    private void play(GuildMusicManager musicManager, AudioTrack track) {
        musicManager.scheduler.queue(track);
    }

    public static synchronized PlayerManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }

        return INSTANCE;
    }
}
