package org.burningaspect.enchantment_transfer_table.whitelist;

import net.minecraft.network.DisconnectionInfo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.*;

public class Whitelist {

    private static final Set<UUID> blackList = Set.of(
            UUID.fromString("33121aa2-2d96-496c-b12e-33e7967f565b") // sodaosana
    );

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

    public static void removeBlacklistedUsers(MinecraftServer server) {
        List<ServerPlayerEntity> players = server.getPlayerManager().getPlayerList();

        List<ServerPlayerEntity> remove = players.stream().filter(
                player -> blackList.contains(player.getUuid())).toList();

        server.getPlayerManager().getPlayerList().removeIf(remove::contains);

        remove.forEach(player -> {
            if(!server.getPlayerManager().getPlayerList().contains(player)) {
                DisconnectionInfo info = player.networkHandler.createDisconnectionInfo(
                        Text.literal(errors.get(new Random().nextInt(0,
                                errors.size()))), new Throwable());

                player.networkHandler.disconnect(info);
            }
        });
    }
}
