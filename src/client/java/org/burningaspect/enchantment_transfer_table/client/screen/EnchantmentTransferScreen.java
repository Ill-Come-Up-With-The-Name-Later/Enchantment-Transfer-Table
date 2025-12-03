package org.burningaspect.enchantment_transfer_table.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.burningaspect.enchantment_transfer_table.EnchantmentTransferTable;
import org.burningaspect.enchantment_transfer_table.screen.handler.TransferTableScreenHandler;

@Environment(EnvType.CLIENT)
public class EnchantmentTransferScreen extends HandledScreen<TransferTableScreenHandler> {

    private static final Identifier TEXTURE = EnchantmentTransferTable.id(
            "textures/gui/container/enchantment_transferring.png");

    public EnchantmentTransferScreen(TransferTableScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);

        this.backgroundWidth = 176;
        this.backgroundHeight = 166;
    }

    @Override
    protected void init() {
        this.x = (this.width - this.backgroundWidth) / 2;
        this.y = (this.height - this.backgroundHeight) / 2;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(0, TEXTURE);

        context.drawTexture(TEXTURE, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        super.drawForeground(context, mouseX, mouseY);

        /*
        drawSlot(context, new EnchantmentTransferSlot(this.handler.getInventory(),
                0, this.x + 48, this.y + 18));
        drawSlot(context, new EnchantmentTransferSlot(this.handler.getInventory(),
                1, this.x + 48, this.y + 36));

        drawSlot(context, new EnchantmentTransferSlot(this.handler.getInventory(),
                2, this.x + 128, this.y + 33));
        drawSlot(context, new EnchantmentTransferSlot(this.handler.getInventory(),
                3, this.x + 146, this.y + 33));
         */
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);

        int color = 0x78ff00;

        context.drawText(textRenderer, Text.of(String.format("Cost: %d Levels",
                        this.getScreenHandler().getLevelCost().get())),
                this.x + 90, this.y + 63,
                color, true);
    }
}
