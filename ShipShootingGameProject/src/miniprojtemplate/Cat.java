package miniprojtemplate;

import javafx.scene.image.Image;
import java.util.Random;

public class Cat extends Sprite {
	public final static Image CAT_IMAGE = new Image("images/cat.png",Cat.CAT_WIDTH,Cat.CAT_WIDTH,false,false);
	public final static int CAT_WIDTH = 80;

	private boolean alive; //attribute that will determine if the cat boss will initially move to the right
	private boolean moveRight;

	private int speed;
	private int damage;

	public static final int MIN_CAT_DAMAGE = 30;
	public static final int MAX_CAT_DAMAGE = 40;
	public static final int MIN_CAT_SPEED = 1;
	public static final int MAX_CAT_SPEED = 5;

	Cat(int x, int y){
		super(x,y);
		this.alive = true;
		this.loadImage(Cat.CAT_IMAGE);
		/*
		 *TODO: Randomize speed of cat and moveRight's initial value
		 */
		Random r = new Random();
		this.speed = Cat.MIN_CAT_SPEED + r.nextInt(Cat.MAX_CAT_SPEED);
		if (r.nextInt(2) == 0){
			this.moveRight = false;
		}else{
			this.moveRight = true;
		}
		this.damage = Cat.MIN_CAT_DAMAGE + r.nextInt(Cat.MAX_CAT_DAMAGE - Cat.MIN_CAT_DAMAGE + 1);
	}

	//method that changes the x position of the cat
	void move(){
		/*
		 * TODO: 				If moveRight is true and if the fish hasn't reached the right boundary yet,
		 *    						move the fish to the right by changing the x position of the fish depending on its speed
		 *    					else if it has reached the boundary, change the moveRight value / move to the left
		 * 					 Else, if moveRight is false and if the fish hasn't reached the left boundary yet,
		 * 	 						move the fish to the left by changing the x position of the fish depending on its speed.
		 * 						else if it has reached the boundary, change the moveRight value / move to the right
		 */

		//testing
		if(moveRight){
			if(this.x <= GameStage.WINDOW_WIDTH - Cat.CAT_WIDTH){
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

	//method that simulates the death of a cat
	private void die(){
		this.setVisible(false);
	}

	//getter
	public boolean isAlive() {
		return this.alive;
	}

	//method that checks the collision between the dog and a cat
	void checkCollision(Dog dog){
		for	(int i = 0; i < dog.getBullets().size(); i++)	{
			if (this.collidesWith(dog.getBullets().get(i))){
				this.die();		//if the cat collides with a bullet, it instantly dies
				dog.gainScore();	//the dog will gain a point for every kill with the bullets
				dog.getBullets().get(i).setVisible(false);	//the bullet disappears
			}
		}
		if(this.collidesWith(dog)){
			if(dog.getImmortal() == false){
				dog.getHit(this.damage);	//if dog is not immortal, then dog's strength is reduced by cat's damage
			}
			this.die();		//cat dies when it collides with the dog
		}
	}
}
