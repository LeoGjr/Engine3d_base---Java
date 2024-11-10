package com.dankicode.escape.level.block;

import com.dankicode.escape.Sound;
import com.dankicode.escape.entities.Item;
import com.dankicode.escape.level.Level;

public class FinalUnlockBlock extends SolidBlock {
	private boolean pressed = false;

	public FinalUnlockBlock() {
		tex = 8 + 3;
	}

	public boolean use(Level level, Item item) {
		if (pressed) return false;
		if (level.player.keys < 4) return false;

		Sound.click1.play();
		pressed = true;
		level.trigger(id, true);

		return true;
	}
}
