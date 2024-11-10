package com.dankicode.escape.level.block;

import com.dankicode.escape.*;
import com.dankicode.escape.entities.Item;
import com.dankicode.escape.entities.Player;
import com.dankicode.escape.gui.Sprite;
import com.dankicode.escape.level.Level;

public class ChestBlock extends Block {
	private boolean open = false;
	private Sprite chestSprite;

	public ChestBlock() {
		tex = 1;
		blocksMotion = true;

		chestSprite = new Sprite(0, 0, 0, 8 * 2 + 0, Art.getCol(0xffff00));
		addSprite(chestSprite);
	}

	public void tick() {
		super.tick();
		//double dist = Math.sqrt((Player.xplayer-x)*(Player.xplayer - x) + (Player.zplayer - this.y)*(Player.zplayer - this.y));
		
		//if(dist < 1) {
		//	System.out.println("perto do bau");
		//}
	}
	
	public boolean use(Level level, Item item) {
		if (open) return false;

		chestSprite.tex++;
		open = true;

		level.getLoot(id);
		Sound.treasure.play();

		return true;
	}
}
