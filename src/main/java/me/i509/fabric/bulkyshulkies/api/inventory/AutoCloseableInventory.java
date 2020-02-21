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

package me.i509.fabric.bulkyshulkies.api.inventory;

import net.minecraft.inventory.BasicInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public class AutoCloseableInventory extends BasicInventory implements AutoCloseable {
	private ListTag items;
	private ItemStack itemRef;

	public AutoCloseableInventory(ListTag items, int slotCount, ItemStack stack) {
		super(slotCount);
		this.items = items;
		this.readTags(items);
	}

	public void readTags(ListTag listTag) {
		int j;

		for (j = 0; j < this.getInvSize(); ++j) {
			this.setInvStack(j, ItemStack.EMPTY);
		}

		for (j = 0; j < listTag.size(); ++j) {
			CompoundTag compoundTag = listTag.getCompound(j);
			int k = compoundTag.getByte("Slot") & 255;

			if (k >= 0 && k < this.getInvSize()) {
				this.setInvStack(k, ItemStack.fromTag(compoundTag));
			}
		}
	}

	public ListTag getTags() {
		ListTag listTag = new ListTag();

		for (int i = 0; i < this.getInvSize(); ++i) {
			ItemStack itemStack = this.getInvStack(i);

			if (!itemStack.isEmpty()) {
				CompoundTag compoundTag = new CompoundTag();
				compoundTag.putByte("Slot", (byte) i);
				itemStack.toTag(compoundTag);
				listTag.add(compoundTag);
			}
		}

		return listTag;
	}

	@Override
	public void close() {
		this.itemRef.getOrCreateSubTag("BlockEntityTag").put("Items", this.getTags());
	}
}
