package com.rina.config.components;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public interface AutoCompleteHandler {
    void onCommandAutoComplete(CommandAutoCompleteInteractionEvent event);
}
