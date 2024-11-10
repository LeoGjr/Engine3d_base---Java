package com.dankicode.escape.entities;

import java.util.*;

import com.dankicode.escape.Sound;
import com.dankicode.escape.level.block.*;

public class Player extends Entity {
	public double bob, bobPhase, turnBob;
	public int selectedSlot = 0;
	public int itemUseTime;
	public double y, ya;
	public int hurtTime = 0;
	public static double health = 20;
	public int keys = 0;
	public int loot = 0;
	public boolean dead = false;
	private int deadTime = 0;
	public int ammo = 0;
	public int potions = 0;
	private Block lastBlock;
	
	public boolean attack = false;

	public Item[] items = new Item[8];
	
	public static double xplayer,zplayer;

	public Player() {
		r = 0.3;
		for (int i = 0; i < items.length; i++) {
			items[i] = Item.none;
		}
	}

	boolean sliding = false;
	public int time;

	public void tick(boolean up, boolean down, boolean left, boolean right, boolean turnLeft, boolean turnRight) {
		
		//rot = 0;
		Block onBlock = level.getBlock((int) (x + 0.5), (int) (z + 0.5));

		double fh = onBlock.getFloorHeight(this);


		lastBlock = onBlock;

		

		double rotSpeed = 0.05;
		double walkSpeed = 0.03 * onBlock.getWalkSpeed(this);

		if (turnLeft) rota += rotSpeed;
		if (turnRight) rota -= rotSpeed;

		double xm = 0;
		double zm = 0;
		if (up) zm--;
		if (down) zm++;
		if (left) xm--;
		if (right) xm++;
		double dd = xm * xm + zm * zm;
		if (dd > 0) dd = Math.sqrt(dd);
		else dd = 1;
		xm /= dd;
		zm /= dd;

		bob *= 0.6;
		turnBob *= 0.8;
		turnBob += rota;
		bob += Math.sqrt(xm * xm + zm * zm);
		bobPhase += Math.sqrt(xm * xm + zm * zm) * onBlock.getWalkSpeed(this);
		
		xa -= (xm * Math.cos(rot) + zm * Math.sin(rot)) * walkSpeed;
		za -= (zm * Math.cos(rot) - xm * Math.sin(rot)) * walkSpeed;
		

		move();

		double friction = onBlock.getFriction(this);
		xa *= friction;
		za *= friction;
		rot += rota;
		rota *= 0.4;
		
		zplayer = this.z;
		xplayer = this.x;
		
		
		
		if(attack) {
		attack = false;
		Sound.shoot.play();
		itemUseTime = 10;
		level.addEntity(new Bullet(this, x, z, rot, 1, 0, 0xffffff));
		ammo--;
		}
	}

	public void activate() {
		if (dead) return;
		if (itemUseTime > 0) return;
		Item item = items[selectedSlot];
		if (item == Item.pistol) {
			if (ammo > 0) {
				Sound.shoot.play();
				itemUseTime = 10;
				level.addEntity(new Bullet(this, x, z, rot, 1, 0, 0xffffff));
				ammo--;
			}
			return;
		}
		if (item == Item.potion) {
			if (potions > 0 && health < 20) {
				Sound.potion.play();
				itemUseTime = 20;
				health += 5 + random.nextInt(6);
				if (health > 20) health = 20;
				potions--;
			}
			return;
		}
		if (item == Item.key) itemUseTime = 10;
		if (item == Item.powerGlove) itemUseTime = 10;
		if (item == Item.cutters) itemUseTime = 10;

		double xa = (2 * Math.sin(rot));
		double za = (2 * Math.cos(rot));

		int rr = 3;
		int xc = (int) (x + 0.5);
		int zc = (int) (z + 0.5);
		List<Entity> possibleHits = new ArrayList<Entity>();
		for (int z = zc - rr; z <= zc + rr; z++) {
			for (int x = xc - rr; x <= xc + rr; x++) {
				List<Entity> es = level.getBlock(x, z).entities;
				for (int i = 0; i < es.size(); i++) {
					Entity e = es.get(i);
					if (e == this) continue;
					possibleHits.add(e);
				}
			}
		}

		int divs = 100;
		for (int i = 0; i < divs; i++) {
			double xx = x + xa * i / divs;
			double zz = z + za * i / divs;
			for (int j = 0; j < possibleHits.size(); j++) {
				Entity e = possibleHits.get(j);
				if (e.contains(xx, zz)) {
					if (e.use(this, items[selectedSlot])) {
						return;
					}
				}

			}
			int xt = (int) (xx + 0.5);
			int zt = (int) (zz + 0.5);
			if (xt != (int) (x + 0.5) || zt != (int) (z + 0.5)) {
				Block block = level.getBlock(xt, zt);
				if (block.use(level, items[selectedSlot])) {
					return;
				}
				if (block.blocks(this)) return;
			}
		}
	}

	public boolean blocks(Entity entity, double x2, double z2, double r2) {
		// if (entity instanceof Bullet && ((B))) return false;
		return super.blocks(entity, x2, z2, r2);
	}

	public Item getSelectedItem() {
		return items[selectedSlot];
	}

	public void addLoot(Item item) {
		if (item == Item.pistol) ammo += 20;
		if (item == Item.potion) potions += 1;
		for (int i = 0; i < items.length; i++) {
			if (items[i] == item) {
				if (level!=null) level.showLootScreen(item);
				return;
			}
		}

		for (int i = 0; i < items.length; i++) {
			if (items[i] == Item.none) {
				items[i] = item;
				selectedSlot = i;
				itemUseTime = 0;
				if (level!=null) level.showLootScreen(item);
				return;
			}
		}
	}

	public void hurt(Entity enemy, int dmg) {
		if (hurtTime > 0 || dead) return;

		hurtTime = 40;
		health -= dmg;

		if (health <= 0) {
			health = 0;
			Sound.death.play();
			dead = true;
		}

		Sound.hurt.play();
		
		System.out.println("perdi vida");

		double xd = enemy.x - x;
		double zd = enemy.z - z;
		double dd = Math.sqrt(xd * xd + zd * zd);
		xa -= xd / dd * 0.1;
		za -= zd / dd * 0.1;
		rota += (random.nextDouble() - 0.5) * 0.2;
	}

	protected void collide(Entity entity) {
		if (entity instanceof Bullet) {
			Bullet bullet = (Bullet) entity;
			if (bullet.owner.getClass() == this.getClass()) {
				return;
			}
			if (hurtTime > 0) return;
			entity.remove();
			hurt(entity, 1);
		}
		
		//A partir daqui você testa colisões com qualquer entidade!
		
		if(entity instanceof BatEntity) {
			
		}
		
		
	}

	public void win() {
		level.win();
	}
}
