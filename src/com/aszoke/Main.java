package com.aszoke;

import java.util.ArrayList;
import java.util.List;

public class Main {

  private static final Integer REPULOK_SZAMA = 5;
  private static final Integer DOKKOLOK_SZAMA = 2;

  public static void main(String[] args) {

    List<Dokkolo> dokkolok = new ArrayList<>();
    for (int i = 0; i < DOKKOLOK_SZAMA; i++) {
      dokkolok.add(new Dokkolo(i));
    }
    Kifutopalya kifutopalya = new Kifutopalya();
    Torony torony = new Torony(kifutopalya, dokkolok);
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
