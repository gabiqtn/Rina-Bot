package com.rina.config.components;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public interface ButtonHandler {
    void onButtonInteraction(ButtonInteractionEvent event);
}
