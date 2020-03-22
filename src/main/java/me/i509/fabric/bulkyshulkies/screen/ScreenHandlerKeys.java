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

package me.i509.fabric.bulkyshulkies.screen;

import net.minecraft.util.Identifier;

import me.i509.fabric.bulkyshulkies.BulkyShulkies;

public final class ScreenHandlerKeys {
	public static final Identifier SHULKER_SCROLLABLE_CONTAINER = BulkyShulkies.id("shulker_scrollable_container");
	public static final Identifier ENDER_SLAB = BulkyShulkies.id("ender_slab_container");
	public static final Identifier SHULKER_9x7_CONTAINER = BulkyShulkies.id("shulker_container_9x7");
	public static final Identifier SHULKER_11x7_CONTAINER = BulkyShulkies.id("shulker_container_11x7");
	public static final Identifier SHULKER_13x7_CONTAINER = BulkyShulkies.id("shulker_container_13x7");
	public static final Identifier SHULKER_HELMET = BulkyShulkies.id("shulker_helmet");

	private ScreenHandlerKeys() {
		// NO-OP
	}
}
