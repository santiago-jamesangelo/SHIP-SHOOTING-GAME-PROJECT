package miniprojtemplate;

class ImmortalTimer extends Thread {
	private Dog myDog;
	private int time;
	private final static int IMMORTAL_TIME = 5;

	ImmortalTimer(Dog dog){
		this.myDog = dog;
		this.time = ImmortalTimer.IMMORTAL_TIME;
	}

	void refresh(){
		this.time = ImmortalTimer.IMMORTAL_TIME;
	}

	/*
     * Counts down and ends the immortality state after designated time
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
		this.myDog.setImmortal(false);
		this.myDog.setType(0);
	}

	@Override
	public void run(){
		this.countDown();
	}
}
