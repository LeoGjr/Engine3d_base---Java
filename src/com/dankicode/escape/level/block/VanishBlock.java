package com.dankicode.escape.level.block;

import com.dankicode.escape.Sound;
import com.dankicode.escape.entities.Item;
import com.dankicode.escape.gui.RubbleSprite;
import com.dankicode.escape.level.Level;

public class VanishBlock extends SolidBlock {
	private boolean gone = false;

	public VanishBlock() {
		tex = 1;
	}

	public boolean use(Level level, Item item) {
		if (gone) return false;

		gone = true;
		blocksMotion = false;
		solidRender = false;
		Sound.crumble.play();

		for (int i = 0; i < 32; i++) {
			RubbleSprite sprite = new RubbleSprite();
			sprite.col = col;
			addSprite(sprite);
		}

		return true;
	}
}
