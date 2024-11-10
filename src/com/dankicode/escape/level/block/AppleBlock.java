package com.dankicode.escape.level.block;

import com.dankicode.escape.*;
import com.dankicode.escape.entities.Item;
import com.dankicode.escape.entities.Player;
import com.dankicode.escape.gui.Sprite;
import com.dankicode.escape.level.Level;

public class AppleBlock extends Block {
	private boolean open = false;
	private Sprite appleSprite;

	public AppleBlock() {
		tex = 1;
		blocksMotion = true;

		appleSprite = new Sprite(0, 0, 0, 8 * 5 + 0, Art.getCol(0xffff00));
		addSprite(appleSprite);
	}
	
	public void tick() {
		double distance = Math.sqrt((Player.xplayer - x) * (Player.xplayer - x) + (Player.zplayer - y) * (Player.zplayer - y));
		if(distance < 1) {
			System.out.println("Estou perto da maçã!");
			this.sprites.remove(appleSprite);
			blocksMotion = false;
		}
	}

	
	

}