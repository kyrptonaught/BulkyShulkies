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

package me.i509.fabric.bulkyshulkies.mixin.compat;

import dev.emi.iteminventory.api.ItemInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import net.fabricmc.fabric.api.util.NbtType;

import me.i509.fabric.bulkyshulkies.BulkyShulkies;
import me.i509.fabric.bulkyshulkies.api.block.base.AbstractShulkerBoxBlock;
import me.i509.fabric.bulkyshulkies.api.inventory.AutoCloseableInventory;
import me.i509.fabric.bulkyshulkies.item.ShulkerBlockItem;

@Mixin(value = ShulkerBlockItem.class, remap = false)
public abstract class ShulkerBlockItemMixin_Compat extends BlockItem implements ItemInventory {
	@Shadow public abstract AbstractShulkerBoxBlock shadow$getBlock();

	public ShulkerBlockItemMixin_Compat(Block block, Settings settings) {
		super(null, null);
	}

	@Override
	public int getInvSize(ItemStack itemStack) {
		return this.shadow$getBlock().getSlotCount();
	}

	@Override
	public ItemStack getStack(ItemStack invStack, int slot) {
		try (AutoCloseableInventory inventory = this.impl$getInventory(invStack)) {
			return inventory.getInvStack(slot);
		}
	}

	@Override
	public void setStack(ItemStack invStack, int slot, ItemStack stackToSet) {
		try (AutoCloseableInventory inventory = this.impl$getInventory(invStack)) {
			inventory.setInvStack(slot, stackToSet);
		}
	}

	@Override
	public boolean canInsert(ItemStack invItem, int index, ItemStack stack) {
		return BulkyShulkies.getInstance().canInsertItem(stack);
	}

	private AutoCloseableInventory impl$getInventory(ItemStack invStack) {
		CompoundTag blockEntityTag = invStack.getSubTag("BlockEntityTag");
		ListTag items = new ListTag();

		if (blockEntityTag != null) {
			if (blockEntityTag.contains("Items", NbtType.LIST)) {
				items = blockEntityTag.getList("Items", NbtType.LIST);
			}
		}

		AutoCloseableInventory inventory = new AutoCloseableInventory(items, this.shadow$getBlock().getSlotCount(), invStack);
		return inventory;
	}
}
