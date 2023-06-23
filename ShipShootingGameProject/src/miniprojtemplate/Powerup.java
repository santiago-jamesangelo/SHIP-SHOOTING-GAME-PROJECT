package miniprojtemplate;

import java.util.Random;
import javafx.scene.image.Image;

public class Powerup extends Sprite {
	private int type;
	private final static Image MEAT_IMAGE = new Image("images/meat.png",Powerup.POWERUP_WIDTH,Powerup.POWERUP_WIDTH,false,false);
	private final static Image BONE_IMAGE = new Image("images/dogbones.png",Powerup.POWERUP_WIDTH,Powerup.POWERUP_WIDTH,false,false);

	public final static int POWERUP_WIDTH = 50;
	public final static int POWERUP_TIMER = 5;

	public Powerup(int x, int y) {
		super(x, y);

		Random r = new Random();
		this.type = r.nextInt(2); //randomizes the powerup to be spawned
		this.loadImage(this.type==0?Powerup.MEAT_IMAGE:Powerup.BONE_IMAGE); //0 = meat spawned, 1 = bone spawned
	}

	//method that checks the collision between the dog and powerup
	void checkCollision(Dog dog){
		if(this.type == 0 && this.collidesWith(dog)){
			System.out.println(dog.getName() + " gained double strength!");
			dog.gainStrength();		//dog doubles strength after getting the meat
			this.setVisible(false);
		}else if(this.type == 1 && this.collidesWith(dog)){
			System.out.println(dog.getName() + " is immortal!");
			dog.tempImmortality();		//dog becomes immortal for 5s after getting the bone
			this.setVisible(false);
		}
	}
}
