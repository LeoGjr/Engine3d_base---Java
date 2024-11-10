package com.dankicode.escape.gui;

import java.util.Random;

import com.dankicode.escape.*;
import com.dankicode.escape.entities.Item;
import com.dankicode.escape.level.block.Block;

public class Screen extends Bitmap {
	private static final int PANEL_HEIGHT = 29;

	private Bitmap testBitmap;
	private Bitmap3D viewport;

	public Screen(int width, int height) {
		super(width, height);

		viewport = new Bitmap3D(width, height);

		Random random = new Random();
		testBitmap = new Bitmap(64, 64);
		for (int i = 0; i < 64 * 64; i++) {
			testBitmap.pixels[i] = random.nextInt() * (random.nextInt(5) / 4);
		}
	}

	int time = 0;

	public void render(Game game, boolean hasFocus) {
		if (game.level == null) {
			fill(0, 0, width, height, 0);
		} else {
			boolean itemUsed = game.player.itemUseTime > 0;
			Item item = game.player.items[game.player.selectedSlot];

			if (game.pauseTime > 0) {
				fill(0, 0, width, height, 0);
				String[] messages = { "Entering " + game.level.name, };
				for (int y = 0; y < messages.length; y++) {
					draw(messages[y], (width - messages[y].length() * 6) / 2, (viewport.height - messages.length * 8) / 2 + y * 8 + 1, 0x111111);
					draw(messages[y], (width - messages[y].length() * 6) / 2, (viewport.height - messages.length * 8) / 2 + y * 8, 0x555544);
				}
			} else {
				viewport.render(game);
				viewport.postProcess(game.level);

				Block block = game.level.getBlock((int) (game.player.x + 0.5), (int) (game.player.z + 0.5));
				if (block.messages != null && hasFocus) {
					for (int y = 0; y < block.messages.length; y++) {
						viewport.draw(block.messages[y], (width - block.messages[y].length() * 6) / 2, (viewport.height - block.messages.length * 8) / 2 + y * 8 + 1, 0x111111);
						viewport.draw(block.messages[y], (width - block.messages[y].length() * 6) / 2, (viewport.height - block.messages.length * 8) / 2 + y * 8, 0x555544);
					}
				}

				draw(viewport, 0, 0);
				int xx = (int) (game.player.turnBob * 32);
				int yy = (int) (Math.sin(game.player.bobPhase * 0.4) * 1 * game.player.bob + game.player.bob * 2);

				if (itemUsed) xx = yy = 0;
				xx += width / 2;
				yy += height - PANEL_HEIGHT - 15 * 3;
				if (item != Item.none) {
					scaleDraw(Art.items, 3, xx, yy, 16 * item.icon + 1, 16 + 1 + (itemUsed ? 16 : 0), 15, 15, Art.getCol(item.color));
				}

			}

			
		}

		

		if (!hasFocus) {
			for (int i = 0; i < pixels.length; i++) {
				pixels[i] = (pixels[i] & 0xfcfcfc) >> 2;
			}
			if (System.currentTimeMillis() / 450 % 2 != 0) {
				String msg = "Click to focus!";
				draw(msg, (width - msg.length() * 6) / 2, height / 3 + 4, 0xffffff);
			}
		}
	}
}