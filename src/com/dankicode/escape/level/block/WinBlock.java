package com.dankicode.escape.level.block;

import com.dankicode.escape.entities.*;

public class WinBlock extends Block {
	public void addEntity(Entity entity) {
		super.addEntity(entity);
		if (entity instanceof Player) {
			((Player)entity).win();
		}
	}
}
