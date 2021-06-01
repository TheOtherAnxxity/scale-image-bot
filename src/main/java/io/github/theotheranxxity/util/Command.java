package io.github.theotheranxxity.util;

import org.javacord.api.DiscordApi;

public record Command(DiscordApi api, String prefix, String command) {
    public String fullCommand() { return prefix + command; }
}
