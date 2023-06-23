package miniprojtemplate;

class BossDamageTimer extends Thread {
	private Dog myDog;
	private int time;
	private final static int DELAY_TIME = 2;

	BossDamageTimer(Dog dog){
		this.myDog = dog;
		this.time = BossDamageTimer.DELAY_TIME;
	}

	void refresh(){
		this.time = BossDamageTimer.DELAY_TIME;
	}

	/*
     * Counts down and delays the damage of the boss to prevent instant death due to continuous collision
     * */
	private void countDown(){
		while(this.time!=0){
			try{
				Thread.sleep(1000);
				this.time--;
			}catch(InterruptedException e){
				System.out.println(e.getMessage());
			}
		}
		this.myDog.setbossDamageDelayed(false);
	}

	@Override
	public void run(){
		this.countDown();
	}
}
