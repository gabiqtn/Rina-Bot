package com.rina.config;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import com.rina.config.components.AutoCompleteHandler;
import com.rina.config.components.ButtonHandler;
import com.rina.config.components.SelectMenuHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class CommandManager extends ListenerAdapter {
    private static final Map<String, CommandHandler> slashMap = new HashMap<>();
    private static final Map<String, ButtonHandler> buttonMap = new HashMap<>();
    private static final Map<String, SelectMenuHandler> selectMenuMap = new HashMap<>();
    private static final Map<String, AutoCompleteHandler> autoCompleteMap = new HashMap<>();
    private final CommandListUpdateAction globalCommandData;
    private final List<CommandListUpdateAction> guildCommandDataList = new ArrayList<>();

    public CommandManager(JDA jda) {
        this.globalCommandData = jda.updateCommands();

        for (Guild guild : jda.getGuilds()) {
            guildCommandDataList.add(guild.updateCommands());
        }

        ClassGraph classGraph = new ClassGraph();
        List<CommandHandler> handlers = new ArrayList<>();

        classGraph.enableClassInfo()
                .scan()
                .getClassesImplementing(CommandHandler.class)
                .forEach(classInfo -> {
                    try {
                        CommandHandler handler = (CommandHandler) classInfo.loadClass().getDeclaredConstructor().newInstance();
                        handlers.add(handler);
                    } catch (RuntimeException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                        throw new RuntimeException("Unable to add Slash with the reason " + e);
                    }
                });

        registerCommand(handlers);

        try (ScanResult scanResult = new ClassGraph()
                .enableAllInfo()
                .scan()) {

            scanResult.getClassesImplementing(ButtonHandler.class.getName()).forEach(classInfo -> {
                try {
                    ButtonHandler handler = (ButtonHandler) classInfo.loadClass().getDeclaredConstructor().newInstance();
                    buttonMap.put(classInfo.getSimpleName(), handler);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            scanResult.getClassesImplementing(SelectMenuHandler.class.getName()).forEach(classInfo -> {
                try {
                    SelectMenuHandler handler = (SelectMenuHandler) classInfo.loadClass().getDeclaredConstructor().newInstance();
                    selectMenuMap.put(classInfo.getSimpleName(), handler);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void registerCommand(CommandHandler handler) {
        slashMap.put(handler.getName(), handler);

        if (handler.isSpecificGuild()) {
            for (CommandListUpdateAction guildCommandData : guildCommandDataList) {
                guildCommandData.addCommands(handler.getData());
            }
        } else {
            globalCommandData.addCommands(handler.getData());
        }
    }

    public void registerCommand(List<CommandHandler> handlerList) {
        handlerList.forEach(this::registerCommand);
        queueCommands();
    }

    private void queueCommands() {
        globalCommandData.queue();
        guildCommandDataList.forEach(CommandListUpdateAction::queue);
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        CommandHandler handler = slashMap.get(event.getName());

        if (handler != null) {
            handler.onSlashCommandEvent(event);
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        // Find the appropriate handler for the button interaction
        for (ButtonHandler handler : buttonMap.values()) {
            handler.onButtonInteraction(event);
        }
    }

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        for (SelectMenuHandler handler : selectMenuMap.values()) {
            handler.onSelectMenuInteraction(event);
        }
    }

    @Override
    public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {
        for (AutoCompleteHandler handler : autoCompleteMap.values()) {
            handler.onCommandAutoComplete(event);
        }
    }
}