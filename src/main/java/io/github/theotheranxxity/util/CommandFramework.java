package io.github.theotheranxxity.util;

import io.github.theotheranxxity.util.function.BiConsumer;
import io.github.theotheranxxity.util.function.TriConsumer;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.user.User;

import java.util.LinkedList;
import java.util.Arrays;
import java.util.NoSuchElementException;

@SuppressWarnings("ClassCanBeRecord")
public class CommandFramework {
    private final DiscordApi api;
    public final String prefix;

    public CommandFramework(DiscordApi api, String prefix) {
        this.api = api;
        this.prefix = prefix;
    }

    @SuppressWarnings("unused")
    public Command createUserCommand(String command, BiConsumer<String[], User> action) {
        api.addMessageCreateListener(event ->
                event.getMessageAuthor().asUser().ifPresent(user ->
                        event.getChannel().asPrivateChannel().ifPresent(privateChannel -> {
                            LinkedList<String> messageContent = new LinkedList<>(Arrays.asList(
                                    event.getMessageContent().split(" ")
                            ));
                            if (messageContent.getFirst().equals(prefix + command)) {
                                messageContent.removeFirst();
                                try {
                                    if (messageContent.getFirst() != null || !messageContent.getFirst().isEmpty())
                                        action.accept(messageContent.toArray(new String[25]), user);
                                    else
                                        action.accept(new String[]{}, user);
                                } catch (NoSuchElementException | IndexOutOfBoundsException exception) {
                                    action.accept(new String[]{}, user);
                                }
                            }
                        })
                )
        );
        return new Command(api, prefix, command);
    }

    @SuppressWarnings("unused")
    public Command createUserCommandWithMessage(String command, TriConsumer<String[], User, Message> action) {
        api.addMessageCreateListener(event ->
                event.getMessageAuthor().asUser().ifPresent(user ->
                        event.getChannel().asPrivateChannel().ifPresent(privateChannel -> {
                            LinkedList<String> messageContent = new LinkedList<>(Arrays.asList(
                                    event.getMessageContent().split(" ")
                            ));
                            if (messageContent.getFirst().equals(prefix + command)) {
                                messageContent.removeFirst();
                                try {
                                    if (messageContent.getFirst() != null || !messageContent.getFirst().isEmpty())
                                        action.accept(messageContent.toArray(new String[25]), user, event.getMessage());
                                    else
                                        action.accept(new String[]{}, user, event.getMessage());
                                } catch (NoSuchElementException | IndexOutOfBoundsException exception) {
                                    action.accept(new String[]{}, user, event.getMessage());
                                }
                            }
                        })
                )
        );
        return new Command(api, prefix, command);
    }

    @SuppressWarnings("unused")
    public Command createServerCommand(String command, BiConsumer<String[], ServerTextChannel> action) {
        api.addMessageCreateListener(event ->
                event.getChannel().asServerTextChannel().ifPresent(serverTextChannel -> {
                    LinkedList<String> messageContent = new LinkedList<>(Arrays.asList(
                            event.getMessageContent().split(" ")
                    ));
                    if (messageContent.getFirst().equals(prefix + command)) {
                        messageContent.removeFirst();
                        try {
                            if (messageContent.getFirst() != null || !messageContent.getFirst().isEmpty())
                                action.accept(messageContent.toArray(new String[25]), serverTextChannel);
                            else
                                action.accept(new String[]{}, serverTextChannel);
                        } catch (NoSuchElementException | IndexOutOfBoundsException exception) {
                            action.accept(new String[]{}, serverTextChannel);
                        }
                    }
                })
        );
        return new Command(api, prefix, command);
    }

    @SuppressWarnings("unused")
    public Command createServerCommandWithUser(String command, TriConsumer<String[], ServerTextChannel, User> action) {
        api.addMessageCreateListener(event ->
                event.getMessageAuthor().asUser().ifPresent(user ->
                        event.getChannel().asServerTextChannel().ifPresent(serverTextChannel -> {
                            LinkedList<String> messageContent = new LinkedList<>(Arrays.asList(
                                    event.getMessageContent().split(" ")
                            ));
                            if (messageContent.getFirst().equals(prefix + command)) {
                                messageContent.removeFirst();
                                try {
                                    if (messageContent.getFirst() != null || !messageContent.getFirst().isEmpty())
                                        action.accept(messageContent.toArray(new String[25]), serverTextChannel, user);
                                    else
                                        action.accept(new String[]{}, serverTextChannel, user);
                                } catch (NoSuchElementException | IndexOutOfBoundsException exception) {
                                    action.accept(new String[]{}, serverTextChannel, user);
                                }
                            }
                        })
                )
        );
        return new Command(api, prefix, command);
    }

    public Command createServerCommandWithMessage(String command, TriConsumer<String[], ServerTextChannel, Message> action) {
        api.addMessageCreateListener(event ->
                event.getServerTextChannel().ifPresent(serverTextChannel -> {
                    LinkedList<String> messageContent = new LinkedList<>(Arrays.asList(
                            event.getMessageContent().split(" ")
                    ));
                    if (messageContent.getFirst().equals(prefix + command)) {
                        messageContent.removeFirst();
                        try {
                            if (messageContent.getFirst() != null || !messageContent.getFirst().isEmpty())
                                action.accept(messageContent.toArray(new String[25]), serverTextChannel, event.getMessage());
                            else
                                action.accept(new String[]{}, serverTextChannel, event.getMessage());
                        } catch (NoSuchElementException | IndexOutOfBoundsException exception) {
                            action.accept(new String[]{}, serverTextChannel, event.getMessage());
                        }
                    }
                })
        );
        return new Command(api, prefix, command);
    }
}
