import java.util.Random;
import java.util.concurrent.Semaphore;

public class Dining_Philosopher {
	private static final int Num_Philosophers = 5;

	public static void main(String[] args) {
		Semaphore[] chopsticks = new Semaphore[Num_Philosophers];

		for (int i = 0; i < Num_Philosophers; i++) {
			chopsticks[i] = new Semaphore(1);
		}
		Philosopher[] philosophers = new Philosopher[Num_Philosophers];

		for (int i = 0; i < Num_Philosophers; i++) {
			philosophers[i] = new Philosopher(i, chopsticks[i], chopsticks[(i + 1) % Num_Philosophers]);
			new Thread(philosophers[i]).start();
		}

	}
}

class Philosopher implements Runnable {
	private Random numGenerator = new Random();

	private int id;
	private Semaphore leftChopstick;
	private Semaphore rightChopstick;

	public Philosopher(int id, Semaphore leftChopstick, Semaphore rightChopstick) {
		this.id = id;
		this.leftChopstick = leftChopstick;
		this.rightChopstick = rightChopstick;
	}

	public void run() {
		try {
			while (true) {
				think();
				pickUpLeftChopstick();
				pickUpRightChopstick();
				eat();
				putDownChopsticks();
			}
		} catch (InterruptedException e) {
			System.out.println("Philosopher " + id + " was interrupted.\n");
		}
	}

	private void think() throws InterruptedException {
		System.out.println("Philosopher " + id + " is Thinking.\n");
		System.out.flush();
		Thread.sleep(numGenerator.nextInt(10));
	}

	private void pickUpLeftChopstick() throws InterruptedException {
		if (leftChopstick.availablePermits() == 0) {
			System.out.println("Philosopher " + id + " is waiting for left chopstick");
		}
		leftChopstick.acquire();
		System.out.println("Philosopher " + id + " is holding left chopstick.\n");
		System.out.flush();

	}

	private void pickUpRightChopstick() throws InterruptedException {
		if (rightChopstick.availablePermits() == 0) {
			System.out.println("Philosopher " + id + " is waiting for right chopstick");
		}
		rightChopstick.acquire();
		System.out.println("Philosopher " + id + " is holding right chopstick.\n");
		System.out.flush();
	}

	private void eat() throws InterruptedException{
		System.out.println("Philosopher "+id+" is eating.\n");
		System.out.flush();
		Thread.sleep(numGenerator.nextInt(10));
	}
	private void putDownChopsticks() {
		leftChopstick.release();
		rightChopstick.release();
	}
}
