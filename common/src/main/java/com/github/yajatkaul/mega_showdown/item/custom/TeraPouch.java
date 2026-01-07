package com.github.yajatkaul.mega_showdown.item.custom;

import com.github.yajatkaul.mega_showdown.screen.custom.handler.TeraPouchScreenHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TeraPouch extends Item {
    public TeraPouch(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack stack = player.getItemInHand(interactionHand);

        if (player.level().isClientSide || player.isCrouching()) {
            return InteractionResultHolder.pass(stack);
        }

        player.openMenu(
                new SimpleMenuProvider(
                        (id, playerInventory, playerEntity) ->
                                new TeraPouchScreenHandler(id, playerInventory, stack, level),
                        Component.translatable("menu.tera_pouch")
                )
        );
        return InteractionResultHolder.success(stack);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(Component.translatable("tooltip.mega_showdown.tera_pouch.tooltip"));
        super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);
    }
}
