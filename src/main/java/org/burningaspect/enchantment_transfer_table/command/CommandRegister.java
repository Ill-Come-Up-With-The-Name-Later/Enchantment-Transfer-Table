package org.burningaspect.enchantment_transfer_table.command;

public class CommandRegister {

    public static void init() {
        DebugCommand.register();
        SecretCommand.register();
    }
}
