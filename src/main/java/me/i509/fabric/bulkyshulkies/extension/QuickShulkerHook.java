package me.i509.fabric.bulkyshulkies.extension;

import me.i509.fabric.bulkyshulkies.BulkyShulkies;
import me.i509.fabric.bulkyshulkies.api.block.base.AbstractShulkerBoxBlock;
import me.i509.fabric.bulkyshulkies.client.screen.Generic11x7Screen;
import me.i509.fabric.bulkyshulkies.client.screen.Generic13x7Screen;
import me.i509.fabric.bulkyshulkies.client.screen.Generic9x7Screen;
import me.i509.fabric.bulkyshulkies.client.screen.ScrollableScreen;
import me.i509.fabric.bulkyshulkies.container.*;
import me.i509.fabric.bulkyshulkies.registry.ShulkerBlocks;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.kyrptonaught.quickshulker.api.ItemStackInventory;
import net.kyrptonaught.quickshulker.api.QuickOpenableRegistry;
import net.kyrptonaught.quickshulker.api.RegisterQuickShulker;
import net.minecraft.container.ShulkerBoxSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

public class QuickShulkerHook implements RegisterQuickShulker {
   public static final Identifier QS$SHULKER_SCROLLABLE_CONTAINER = BulkyShulkies.id("qs_shulker_scrollable_container");
   public static final Identifier QS$SHULKER_9x7_CONTAINER = BulkyShulkies.id("qs_shulker_container_9x7");
   public static final Identifier QS$SHULKER_11x7_CONTAINER = BulkyShulkies.id("qs_shulker_container_11x7");
   public static final Identifier QS$SHULKER_13x7_CONTAINER = BulkyShulkies.id("qs_shulker_container_13x7");
   @Override
   public void registerProviders() {
      ContainerProviderRegistry.INSTANCE.registerFactory(QS$SHULKER_SCROLLABLE_CONTAINER, QuickShulkerHook::createScrollable);
      ContainerProviderRegistry.INSTANCE.registerFactory(QS$SHULKER_9x7_CONTAINER, QuickShulkerHook::create9x7);
      ContainerProviderRegistry.INSTANCE.registerFactory(QS$SHULKER_11x7_CONTAINER, QuickShulkerHook::create11x7);
      ContainerProviderRegistry.INSTANCE.registerFactory(QS$SHULKER_13x7_CONTAINER, QuickShulkerHook::create13x7);

      ScreenProviderRegistry.INSTANCE.registerFactory(QS$SHULKER_9x7_CONTAINER, Generic9x7Screen::createScreen);
      ScreenProviderRegistry.INSTANCE.registerFactory(QS$SHULKER_11x7_CONTAINER, Generic11x7Screen::createScreen);
      ScreenProviderRegistry.INSTANCE.registerFactory(QS$SHULKER_13x7_CONTAINER, Generic13x7Screen::createScreen);
      ScreenProviderRegistry.INSTANCE.registerFactory(QS$SHULKER_SCROLLABLE_CONTAINER, ScrollableScreen::createScreen);

      QuickOpenableRegistry.register(((player, stack) -> ContainerProviderRegistry.INSTANCE.openContainer(QS$SHULKER_SCROLLABLE_CONTAINER,player,
              writer -> writer.writeInt(player.inventory.getSlotWithStack(stack)))),
              ShulkerBlocks.COPPER_SHULKER_BOX, ShulkerBlocks.IRON_SHULKER_BOX, ShulkerBlocks.SILVER_SHULKER_BOX, ShulkerBlocks.SLAB_SHULKER_BOX);

      QuickOpenableRegistry.register(((player, stack) -> ContainerProviderRegistry.INSTANCE.openContainer(QS$SHULKER_13x7_CONTAINER,player,
              writer -> writer.writeInt(player.inventory.getSlotWithStack(stack)))),
              ShulkerBlocks.OBSIDIAN_SHULKER_BOX, ShulkerBlocks.PLATINUM_SHULKER_BOX);

      QuickOpenableRegistry.register(((player, stack) -> ContainerProviderRegistry.INSTANCE.openContainer(QS$SHULKER_11x7_CONTAINER,player,
              writer -> writer.writeInt(player.inventory.getSlotWithStack(stack)))),
              ShulkerBlocks.DIAMOND_SHULKER_BOX);

      QuickOpenableRegistry.register(((player, stack) -> ContainerProviderRegistry.INSTANCE.openContainer(QS$SHULKER_9x7_CONTAINER,player,
              writer -> writer.writeInt(player.inventory.getSlotWithStack(stack)))),
              ShulkerBlocks.GOLD_SHULKER_BOX);
   }
   public static GenericContainer13x7 create13x7(int syncId, Identifier identifier, PlayerEntity player, PacketByteBuf buf) {
      int invSlot = buf.readInt();
      ItemStack stack = player.inventory.getInvStack(invSlot);
      return  new GenericContainer13x7(syncId, ShulkerBoxSlot::new, player.inventory, new ItemStackInventory(stack, getSlotCount(stack)), new TranslatableText("container.shulkerBox"));
   }
   public static GenericContainer11x7 create11x7(int syncId, Identifier identifier, PlayerEntity player, PacketByteBuf buf) {
      int invSlot = buf.readInt();
      ItemStack stack = player.inventory.getInvStack(invSlot);
      return  new GenericContainer11x7(syncId, ShulkerBoxSlot::new, player.inventory, new ItemStackInventory(stack, getSlotCount(stack)), new TranslatableText("container.shulkerBox"));
   }
   public static GenericContainer9x7 create9x7(int syncId, Identifier identifier, PlayerEntity player, PacketByteBuf buf) {
      int invSlot = buf.readInt();
      ItemStack stack = player.inventory.getInvStack(invSlot);
      return  new GenericContainer9x7(syncId, ShulkerBoxSlot::new, player.inventory, new ItemStackInventory(stack, getSlotCount(stack)), new TranslatableText("container.shulkerBox"));
   }
   public static ScrollableContainer createScrollable(int syncId, Identifier identifier, PlayerEntity player, PacketByteBuf buf) {
      int invSlot = buf.readInt();
      ItemStack stack = player.inventory.getInvStack(invSlot);
      return  new ScrollableContainer(syncId, ShulkerBoxSlot::new, player.inventory, new ItemStackInventory(stack, getSlotCount(stack)), new TranslatableText("container.shulkerBox"));
   }
   public static int getSlotCount(ItemStack stack) {
      return ((AbstractShulkerBoxBlock) ((BlockItem) stack.getItem()).getBlock()).getSlotCount();

   }
}
