package miniprojtemplate;

import java.util.Random;

import javafx.scene.image.Image;

public class CatBoss extends Cat{
	public final static Image CATBOSS_IMAGE = new Image("images/catboss.png",CatBoss.CATBOSS_WIDTH,CatBoss.CATBOSS_WIDTH,false,false);
	public final static int CATBOSS_WIDTH = 240;

	private boolean alive;
	private boolean moveRight; //attribute that will determine if the cat boss will initially move to the right

	private int speed;
	private int damage;
	private int health;

	public static final int BOSS_CAT_DAMAGE = 50;
	public static final int BOSS_CAT_HEALTH = 3000;

	CatBoss(int x, int y){
		super(x,y);
		this.alive = true;
		this.health = CatBoss.BOSS_CAT_HEALTH;
		this.loadImage(CatBoss.CATBOSS_IMAGE);
		/*
		 *TODO: Randomize speed of cat boss and moveRight's initial value
		 */
		Random r = new Random();
		this.speed = Cat.MIN_CAT_SPEED + r.nextInt(Cat.MAX_CAT_SPEED);
		if (r.nextInt(2) == 0){
			this.moveRight = false;
		}else{
			this.moveRight = true;
		}
		this.damage = CatBoss.BOSS_CAT_DAMAGE;
	}

	//method that changes the x position of the cat boss
	void move(){
		/*
		 * TODO: 				If moveRight is true and if the fish hasn't reached the right boundary yet,
		 *    						move the fish to the right by changing the x position of the fish depending on its speed
		 *    					else if it has reached the boundary, change the moveRight value / move to the left
		 * 					 Else, if moveRight is false and if the fish hasn't reached the left boundary yet,
		 * 	 						move the fish to the left by changing the x position of the fish depending on its speed.
		 * 						else if it has reached the boundary, change the moveRight value / move to the right
		 */

		if(moveRight){
			if(this.x <= GameStage.WINDOW_WIDTH - CatBoss.CATBOSS_WIDTH){
				this.x += this.speed;
			}else{
				this.moveRight = false;
			}
		}else{
			if(this.x >= 0){
				this.x -= this.speed;
			}else{
				this.moveRight = true;
			}
		}
	}

	void getHit(Dog dog){
		this.health-=dog.getStrength();
		if(this.health<=0){
			this.die();
			dog.gainScore();
		}
	}

	int getHealth(){
		return this.health;
	}

	private void die(){
		this.setVisible(false);
		this.alive = false;
	}

	//getter
	public boolean isAlive() {
		return this.alive;
	}

	//method that checks the collision between the dog and the cat boss
	void checkCollision(Dog dog){
		for	(int i = 0; i < dog.getBullets().size(); i++)	{
			if (this.collidesWith(dog.getBullets().get(i))){
				this.getHit(dog);
				dog.getBullets().get(i).setVisible(false);
			}
		}
		if(this.collidesWith(dog)){
			if(dog.getImmortal() == false){
				if(dog.getbossDamageDelayed() == false){
					dog.getHit(this.damage);	//if dog is not immortal, then dog's strength is reduced by cat boss' damage
					dog.delayBossDamage();
				}
			}
		}
	}
}
