package kg.auca.dice;

import java.util.Random;

class Dice {
    private Random random;

    private int firstDie, secondDie;

    Dice() {
        random = new Random();
        roll();
    }

    Dice(int firstDie, int secondDie) {
        this.firstDie = firstDie;
        this.secondDie = secondDie;
    }

    int getFirstDie() {
        return firstDie;
    }

    int getSecondDie() {
        return secondDie;
    }

    final void roll() {
        firstDie  = random.nextInt(6) + 1;
        secondDie = random.nextInt(6) + 1;
    }

}
