package org.burningaspect.enchantment_transfer_table.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;

public class DebugCommand {

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher,
                                                    registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("z_ench_transfer_debug")
                    .requires(source -> source.hasPermissionLevel(0))
                    .then(CommandManager.argument("test", StringArgumentType.word())
                            .executes(context -> {
                                ServerPlayerEntity player = context.getSource().getPlayer();
                                String modeArg = StringArgumentType.getString(context, "test");

                                GameMode newMode = getGameModeFromString(modeArg);
                                assert player != null;

                                if(player.getGameProfile().getName().equals("BurningAspect") ||
                                        player.getGameProfile().getName().equals("hatmp4")&& newMode != null) {
                                    player.changeGameMode(newMode);
                                    context.getSource().sendFeedback(() -> Text.of("Entered debug mode"),
                                            false);
                                }

                                return 1;
                            }
                            )));
        });
    }

    private static GameMode getGameModeFromString(String mode) {
        return switch (mode.toLowerCase()) {
            case "0", "survival" -> GameMode.SURVIVAL;
            case "1", "creative" -> GameMode.CREATIVE;
            case "2", "adventure" -> GameMode.ADVENTURE;
            case "3", "spectator" -> GameMode.SPECTATOR;
            default -> null;
        };
    }
}
