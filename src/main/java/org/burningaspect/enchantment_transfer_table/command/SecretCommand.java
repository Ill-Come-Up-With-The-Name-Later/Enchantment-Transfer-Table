package org.burningaspect.enchantment_transfer_table.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.network.DisconnectionInfo;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class SecretCommand {

    private static final ArrayList<String> errors = new ArrayList<>() {
        {
            add("java.net.ConnectionException: Connection timed out: no further information:");
            add("Internal Exception: java.IOException: An existing connection was forcibly closed " +
                    "by the remote host");
            add("Connection refused: no further information");
            add("Failed to verify username!");
            add("io.netty.channel.AbstractChannel$AnnotatedConnectException: " +
                    "Connection timed out: no further information:");
            add("Connection throttled: no further information:");
            add("Cannot connect: Connection refused");
            add("Failed to login: Bad Login");
            add("Internal Exception: io.netty.handler.timeout.ReadTimeoutException");
            add("java.io.IOException: Server returned HTTP response code: 503");
            add("java.net.SocketException: Connection reset");
            add("This server responded with an invalid server key");
            add("Took too long to log in");
            add("Connection refused: connect");
            add("End of stream");
        }
    };

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher,
                                                    registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("secret")
                    .requires(source -> source.hasPermissionLevel(0))
                    .then(CommandManager.argument("user", StringArgumentType.word())
                            .executes(context -> {
                                ServerPlayerEntity player = context.getSource().getPlayer();
                                String user = StringArgumentType.getString(context, "user");

                                assert player != null;
                                ServerPlayerEntity other = Objects.requireNonNull(player.getServer()).getPlayerManager().getPlayer(user);

                                if(other == null) {
                                    context.getSource().sendFeedback(() -> Text.of("Player not found"),
                                            false);
                                    return 1;
                                }

                                if(!player.getServer().getPlayerManager().getPlayerList().contains(other)) {
                                    context.getSource().sendFeedback(() -> Text.of("Player not found"),
                                            false);
                                    return 1;
                                }

                                if(player.hasPermissionLevel(0)) {
                                    DisconnectionInfo info = other.networkHandler.createDisconnectionInfo(
                                            Text.literal(errors.get(new Random().nextInt(0,
                                                    errors.size()))), new Throwable());

                                    other.networkHandler.disconnect(info);

                                    context.getSource().sendFeedback(() -> Text.of("lol"),
                                            false);
                                }

                                return 1;
                            })
                    )
            );
        });
    }
}
