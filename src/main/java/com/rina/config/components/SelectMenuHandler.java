package com.rina.config.components;

import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;

public interface SelectMenuHandler {
    void onSelectMenuInteraction(StringSelectInteractionEvent event);
}
