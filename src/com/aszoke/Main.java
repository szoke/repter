package com.aszoke;

import java.util.ArrayList;
import java.util.List;

public class Main {

  // private static final Integer REPULOK_SZAMA = 1;
  // private static final Integer REPULOK_SZAMA = 2;
  private static final Integer REPULOK_SZAMA = 10;
  // private static final Integer REPULOK_SZAMA = 44;

  private static Torony torony = new Torony();

  public static void main(String[] args) {

    List<Thread> repulok = new ArrayList<>(REPULOK_SZAMA);

    for (int i = 0; i < REPULOK_SZAMA; i++) {
      Thread t = new Thread(new Repulo(torony, String.valueOf(i)));
      repulok.add(t);
      t.start();
    }

    System.out.println("join() hivasa minden szalra...");

    for (Thread repulo : repulok) {
      try {
        repulo.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    System.out.println("Minden szal join()-olt.");

    System.out.println("--- main() vege ---");
    System.out.println("Leszallt repulok szama: " + torony.getLeszalltRepulok().size());
    System.out.println("Leszallt repulok: " + torony.getLeszalltRepulok());
    System.out.println("Felszallt repulok szama: " + torony.getFelszalltRepulok().size());
    System.out.println("Felszallt repulok: " + torony.getFelszalltRepulok());
  }
}
