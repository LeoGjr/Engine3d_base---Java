package com.dankicode.escape.level.block;

import com.dankicode.escape.Art;
import com.dankicode.escape.entities.*;

public class WaterBlock extends Block {
	int steps = 0;

	public WaterBlock() {
		//blocksMotion = true;
	}

	public void tick() {
		super.tick();
		steps--;
		if (steps <= 0) {
			floorTex = 8 + random.nextInt(3);
			floorCol = Art.getCol(0x0000ff);
			steps = 16;
		}
	}

	public boolean blocks(Entity entity) {
		if (entity instanceof Player) {
			if (((Player) entity).getSelectedItem() == Item.flippers) return false;
		}
		if (entity instanceof Bullet) return false;
		return blocksMotion;
	}

	public double getFloorHeight(Entity e) {
		return -0.5;
	}

	public double getWalkSpeed(Player player) {
		return 0.4;
	}

}
