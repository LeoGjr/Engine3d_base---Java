package com.dankicode.escape.level.block;

import com.dankicode.escape.Art;
import com.dankicode.escape.entities.*;

public class IceBlock extends Block {
	public IceBlock() {
		floorTex = 16;
	}

	public void tick() {
		super.tick();
		//Não precisamos setar pois foi retirado o sistema de cor dinamica
		//floorCol = Art.getCol(0x8080ff);
	}

	public double getWalkSpeed(Player player) {
		if (player.getSelectedItem() == Item.skates) return 0.05;
		return 1.4;
	}

	public double getFriction(Player player) {
		if (player.getSelectedItem() == Item.skates) return 0.98;
		return 1;
	}

	public boolean blocks(Entity entity) {
		if (entity instanceof Player) return false;
		if (entity instanceof Bullet) return false;
		if (entity instanceof EyeBossEntity) return false;
		if (entity instanceof EyeEntity) return false;
		return true;
	}
}
