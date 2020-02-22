/*
 * MIT License
 *
 * Copyright (c) 2019-2020 i509VCB
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.i509.fabric.bulkyshulkies.extension;

import me.i509.fabric.bulkyshulkies.BulkyShulkies;
import me.i509.fabric.bulkyshulkies.api.block.base.AbstractShulkerBoxBlock;
import me.i509.fabric.bulkyshulkies.block.cursed.slab.CursedSlabShulkerBox;
import me.i509.fabric.bulkyshulkies.block.material.copper.CopperShulkerBoxBlock;
import me.i509.fabric.bulkyshulkies.block.material.diamond.DiamondShulkerBoxBlock;
import me.i509.fabric.bulkyshulkies.block.material.gold.GoldShulkerBoxBlock;
import me.i509.fabric.bulkyshulkies.block.material.iron.IronShulkerBoxBlock;
import me.i509.fabric.bulkyshulkies.block.material.obsidian.ObsidianShulkerBoxBlock;
import me.i509.fabric.bulkyshulkies.block.material.platinum.PlatinumShulkerBoxBlock;
import me.i509.fabric.bulkyshulkies.block.material.silver.SilverShulkerBoxBlock;
import me.i509.fabric.bulkyshulkies.container.GenericContainer11x7;
import me.i509.fabric.bulkyshulkies.container.GenericContainer13x7;
import me.i509.fabric.bulkyshulkies.container.GenericContainer9x7;
import me.i509.fabric.bulkyshulkies.container.ScrollableContainer;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.kyrptonaught.quickshulker.api.ItemStackInventory;
import net.kyrptonaught.quickshulker.api.QuickOpenableRegistry;
import net.kyrptonaught.quickshulker.api.RegisterQuickShulker;
import net.minecraft.block.Block;
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

		QuickOpenableRegistry.register(((player, stack) -> ContainerProviderRegistry.INSTANCE.openContainer(QS$SHULKER_SCROLLABLE_CONTAINER, player,
				writer -> writer.writeInt(player.inventory.getSlotWithStack(stack)))),
				CopperShulkerBoxBlock.class, IronShulkerBoxBlock.class, SilverShulkerBoxBlock.class, CursedSlabShulkerBox.class);

		QuickOpenableRegistry.register(((player, stack) -> ContainerProviderRegistry.INSTANCE.openContainer(QS$SHULKER_13x7_CONTAINER, player,
				writer -> writer.writeInt(player.inventory.getSlotWithStack(stack)))),
				ObsidianShulkerBoxBlock.class, PlatinumShulkerBoxBlock.class);

		QuickOpenableRegistry.register(((player, stack) -> ContainerProviderRegistry.INSTANCE.openContainer(QS$SHULKER_11x7_CONTAINER, player,
				writer -> writer.writeInt(player.inventory.getSlotWithStack(stack)))),
				DiamondShulkerBoxBlock.class);

		QuickOpenableRegistry.register(((player, stack) -> ContainerProviderRegistry.INSTANCE.openContainer(QS$SHULKER_9x7_CONTAINER, player,
				writer -> writer.writeInt(player.inventory.getSlotWithStack(stack)))),
				GoldShulkerBoxBlock.class);
	}

	public static GenericContainer13x7 create13x7(int syncId, Identifier identifier, PlayerEntity player, PacketByteBuf buf) {
		int invSlot = buf.readInt();
		ItemStack stack = player.inventory.getInvStack(invSlot);
		return new GenericContainer13x7(syncId, ShulkerBoxSlot::new, player.inventory, new ItemStackInventory(stack, getSlotCount(stack)), new TranslatableText("container.shulkerBox"));
	}

	public static GenericContainer11x7 create11x7(int syncId, Identifier identifier, PlayerEntity player, PacketByteBuf buf) {
		int invSlot = buf.readInt();
		ItemStack stack = player.inventory.getInvStack(invSlot);
		return new GenericContainer11x7(syncId, ShulkerBoxSlot::new, player.inventory, new ItemStackInventory(stack, getSlotCount(stack)), new TranslatableText("container.shulkerBox"));
	}

	public static GenericContainer9x7 create9x7(int syncId, Identifier identifier, PlayerEntity player, PacketByteBuf buf) {
		int invSlot = buf.readInt();
		ItemStack stack = player.inventory.getInvStack(invSlot);
		return new GenericContainer9x7(syncId, ShulkerBoxSlot::new, player.inventory, new ItemStackInventory(stack, getSlotCount(stack)), new TranslatableText("container.shulkerBox"));
	}

	public static ScrollableContainer createScrollable(int syncId, Identifier identifier, PlayerEntity player, PacketByteBuf buf) {
		int invSlot = buf.readInt();
		ItemStack stack = player.inventory.getInvStack(invSlot);
		return new ScrollableContainer(syncId, ShulkerBoxSlot::new, player.inventory, new ItemStackInventory(stack, getSlotCount(stack)), new TranslatableText("container.shulkerBox"));
	}

	public static int getSlotCount(ItemStack stack) {
		if (stack.getItem() instanceof BlockItem) {
			Block block = ((BlockItem) stack.getItem()).getBlock();
			if (block instanceof AbstractShulkerBoxBlock)
				return ((AbstractShulkerBoxBlock) block).getSlotCount();
		}
		return 0;
	}
}
