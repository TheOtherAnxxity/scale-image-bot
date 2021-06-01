package io.github.theotheranxxity;

import io.github.theotheranxxity.util.CommandFramework;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAttachment;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.util.logging.ExceptionLogger;

import java.awt.*;
import java.awt.image.BufferedImage;

import static io.github.theotheranxxity.ScaleImage.getFileNameScaled;
import static io.github.theotheranxxity.ScaleImage.scaleImage;

@SuppressWarnings("unused")
public class ScaleImageBot {
    public static final DiscordApi API = new DiscordApiBuilder()
            .setToken(System.getenv("DISCORD_TOKEN"))
            .setAllIntents()
            .login().join();
    public static final User SELF = API.getYourself();
    public static final CommandFramework FRAMEWORK = new CommandFramework(ScaleImageBot.API, "");
    public static int width = 300;
    public static int height = 300;

    public static void main(String[] args) {
        API.setMessageCacheSize(50, 60 * 60);
        FRAMEWORK.createServerCommandWithMessage(SELF.getMentionTag(), ScaleImageBot::scaleWithNoArgs);
        FRAMEWORK.createServerCommandWithMessage(SELF.getNicknameMentionTag(), ScaleImageBot::scaleWithNoArgs);
        FRAMEWORK.createServerCommandWithMessage(SELF.getMentionTag(), ScaleImageBot::scaleWithWidthHeightUnsafe);
        FRAMEWORK.createServerCommandWithMessage(SELF.getNicknameMentionTag(), ScaleImageBot::scaleWithWidthHeightUnsafe);
    }

    private static void scaleWithNoArgs(String[] messageContent, ServerTextChannel serverTextChannel, Message message) {
        if (messageContent.length == 0) {
            if (message.getAttachments().size() == 0) {
                message.reply("There was no attachment on that message. \n" +
                        "Mention me in a message with an image to scale it.");
            }
            for (MessageAttachment attachment : message.getAttachments())
                if (attachment.isImage()) {
                    attachment.downloadAsImage().thenAccept(sourceImage -> {
                        int newHeight = sourceImage.getHeight() * (sourceImage.getHeight() / 2);
                        int newWidth = sourceImage.getWidth() * (sourceImage.getWidth() / 2);
                        try {
                            BufferedImage scaledImage = scaleImage(sourceImage, newWidth, newHeight);
                            long sizeBytes = (long) scaledImage.getData().getDataBuffer().getSize() * 4L;
                            if (sizeBytes <= 8e+6 || sizeBytes != 0)
                                new MessageBuilder()
                                        .addAttachment(scaledImage, getFileNameScaled(attachment.getFileName()))
                                        .send(serverTextChannel);
                            else
                                message.reply("Sorry, couldn't send the scaled image since it was too large" +
                                        " or it didn't scale properly and was 0 bytes in size.");
                        } catch (IllegalArgumentException illegalArgumentException) {
                            message.reply("Sorry, it looks like the image you gave was too large.");
                        }
                    }).exceptionally(ExceptionLogger.get());
                } else
                    message.reply("The attachment on that message was not an image. \n" +
                            "Mention me in a message with an image to scale it.");
        }
    }

    private static void scaleWithWidthHeightUnsafe(String[] messageContent, ServerTextChannel serverTextChannel, Message message) {
        try {
            if (!messageContent[0].isEmpty()) {
                width = Integer.parseUnsignedInt(messageContent[0]);
            }
            if (!messageContent[1].isEmpty()) {
                height = Integer.parseUnsignedInt(messageContent[1]);
            }
        } catch (NumberFormatException numberFormatException) {
            message.reply("Sorry, failed to parse your arguments as integers. Try again.");
            return;
        }

        if (message.getAttachments().size() == 0)
            message.reply("There was no attachment on that message. \n" +
                    "Mention me in a message with an image to scale it.");

        for (MessageAttachment attachment : message.getAttachments())
            if (attachment.isImage())
                attachment.downloadAsImage().thenAccept(sourceImage -> {
                    try {
                        if (messageContent[2] != null && messageContent[2].equalsIgnoreCase("true")) {
                            BufferedImage scaledImage = scaleImageHandler(sourceImage, width, height, true, messageContent[3]);
                            long sizeBytes = (long) scaledImage.getData().getDataBuffer().getSize() * 4L;
                            if (sizeBytes <= 8e+6 || sizeBytes != 0)
                                new MessageBuilder()
                                        .addAttachment(scaledImage, getFileNameScaled(attachment.getFileName()))
                                        .send(serverTextChannel);
                            else
                                message.reply("Sorry, couldn't send the scaled image since it was too large" +
                                        " or it didn't scale properly and was 0 bytes in size.");

                        } else {
                            BufferedImage scaledImage = scaleImageHandler(sourceImage, width, height, false, messageContent[3]);
                            long sizeBytes = (long) scaledImage.getData().getDataBuffer().getSize() * 4L;
                            if (sizeBytes <= 8e+6 || sizeBytes != 0)
                                new MessageBuilder()
                                        .addAttachment(scaledImage, getFileNameScaled(attachment.getFileName()))
                                        .send(serverTextChannel);
                            else
                                message.reply("Sorry, couldn't send the scaled image since it was too large" +
                                        " or it didn't scale properly and was 0 bytes in size.");
                        }
                    } catch (IllegalArgumentException illegalArgumentException) {
                        message.reply("Sorry, it looks like the image you gave was too big," +
                                " or the width/height you requested was too big.");
                    } catch (IllegalStateException illegalStateException) {
                        message.reply("Sorry, you can't distort an image's aspect ratio without using unsafe mode.");
                    }
                    catch (IndexOutOfBoundsException outOfBoundsException) {
                        try {
                            BufferedImage scaledImage = scaleImageHandler(sourceImage, width, height, false, "");
                            long sizeBytes = (long) scaledImage.getData().getDataBuffer().getSize() * 4L;
                            if (sizeBytes <= 8e+6 || sizeBytes != 0)
                                new MessageBuilder()
                                        .addAttachment(scaledImage, getFileNameScaled(attachment.getFileName()))
                                        .send(serverTextChannel);
                            else
                                message.reply("Sorry, couldn't send the scaled image since it was too large" +
                                        " or it didn't scale properly and was 0 bytes in size.");
                        } catch (IllegalArgumentException illegalArgumentException) {
                            message.reply("Sorry, it looks like the image you gave was too big," +
                                    " or the width/height you requested was too big.");
                        } catch (IllegalStateException illegalStateException) {
                            message.reply("Sorry, you can't distort an image's aspect ratio without using unsafe mode.");
                        }
                    }
                }).exceptionally(ExceptionLogger.get());
            else
                message.reply("The attachment on that message was not an image. \n" +
                        "Mention me in a message with an image to scale it.");

    }

    public static BufferedImage scaleImageHandler(BufferedImage sourceImage, int width, int height, boolean unsafe, String scaleType) {
        if (unsafe) {
            if (scaleType.isEmpty()) return scaleImage(sourceImage, width, height);
            else switch (scaleType) {
                case "nearest_neighbour" -> {
                    return scaleImage(
                            sourceImage, width, height,
                            RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR
                    );
                }
                case "bilinear" -> {
                    return scaleImage(
                            sourceImage, width, height,
                            RenderingHints.VALUE_INTERPOLATION_BILINEAR
                    );
                }
                case "bicubic" -> {
                    return scaleImage(
                            sourceImage, width, height,
                            RenderingHints.VALUE_INTERPOLATION_BICUBIC
                    );
                }
                default -> {
                    return scaleImage(sourceImage, width, height);
                }
            }
        } else if (height / width == sourceImage.getHeight() / sourceImage.getWidth())
            return scaleImage(sourceImage, width, height);
        else
            throw new IllegalStateException("You cannot distort an image's aspect ratio without using unsafe.");
    }
}
