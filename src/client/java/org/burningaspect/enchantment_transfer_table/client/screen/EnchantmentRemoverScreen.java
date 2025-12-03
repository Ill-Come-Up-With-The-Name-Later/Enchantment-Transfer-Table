package org.burningaspect.enchantment_transfer_table.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ScrollableWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemStackSet;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.burningaspect.enchantment_transfer_table.EnchantmentTransferTable;
import org.burningaspect.enchantment_transfer_table.misc.EnchantmentHelperExpansion;
import org.burningaspect.enchantment_transfer_table.screen.handler.EnchantmentRemoverScreenHandler;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class EnchantmentRemoverScreen extends HandledScreen<EnchantmentRemoverScreenHandler> {

    private static final Identifier TEXTURE = EnchantmentTransferTable.id(
            "textures/gui/container/enchantment_remover.png");

    private static final int COST_COLOR = 0x78ff00;

    public EnchantmentRemoverScreen(EnchantmentRemoverScreenHandler handler, PlayerInventory inventory, Text title) {
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
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);

        drawMouseoverTooltip(context, mouseX, mouseY);
        drawEnchantButtons();
    }

    public void drawEnchantButtons() {
        AtomicReference<ItemStack> item = new AtomicReference<>(this.handler.getSlot(0).getStack());

        if(!this.handler.getSlot(2).getStack().isEmpty()) {
            item = new AtomicReference<>(this.handler.getSlot(2).getStack());
        }

        ItemStack shard = this.handler.getSlot(1).getStack();

        if(!(item.get() == null || item.get().isEmpty()) && !shard.isEmpty()) {
            ItemEnchantmentsComponent enchantments = EnchantmentHelper.getEnchantments(item.get());
            AtomicInteger buttonNum = new AtomicInteger(1);
            int height = 20;

            AtomicReference<ItemStack> finalItem = item;
            enchantments.getEnchantmentEntries().forEach((
                    enchantment) -> {
                ButtonWidget button = ButtonWidget.builder(Text.literal(enchantment.getKey().getIdAsString()), x -> {
                    assert this.client != null;
                    PlayerEntity player = this.client.player;
                    assert player != null;

                    this.getScreenHandler().setLevelCost(enchantment.getIntValue() * 5);

                    if(!this.getScreenHandler().getSlot(1).getStack().isEmpty()) {
                        if(player.experienceLevel >= this.getScreenHandler().getLevelCost().get()
                                || player.isInCreativeMode()) {
                            if(!player.getAbilities().creativeMode) {
                                player.experienceLevel -= this.handler.getLevelCost().get();
                            }

                            this.handler.getSlot(0).setStack(ItemStack.EMPTY);
                            this.handler.getSlot(1).getStack().decrement(1);

                            player.getInventory().insertStack(EnchantmentHelperExpansion.
                                    removeEnchantment(finalItem.get(), enchantment));

                            this.close();
                        }
                    }
                }).dimensions(184, 50 + ((buttonNum.get() - 1) * height), 109, height)
                        .tooltip(Tooltip.of(Text.literal("Cost: " + enchantment.getIntValue() * 5 + " Levels")
                                .withColor(COST_COLOR))).build();

                buttonNum.addAndGet(1);
                addDrawableChild(button);
            });
        }
    }
}
