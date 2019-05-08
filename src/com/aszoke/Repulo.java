package com.aszoke;

import java.util.Random;

public class Repulo implements Runnable {

  private Torony torony;
  private String nev;

  public Repulo(Torony torony, String nev) {
    this.torony = torony;
    this.nev = nev;
  }

  @Override
  public void run() {
    leszallas();
    leszalltam();
    varakozasDokkoloban(new Random().nextInt(2000));
    felszallas();
    felszalltam();
  }

  private void leszallas() {
    log("Leszallnek.");
    try {
      torony.leszallnek(nev);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private void leszalltam() {
    log("Leszalltam.");
    torony.leszalltam(nev);
  }

  private void felszallas() {
    log("Felszallnek.");
    try {
      torony.felszallnek(nev);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private void felszalltam() {
    log("Felszalltam.");
    torony.felszalltam(nev);
  }

  private void varakozasDokkoloban(int mennyit) {
    try {
      log("Varok " + mennyit + " millisecet a dokkoloban.");
      Thread.sleep(mennyit);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private void log(String s) {
    System.out.println(String.format("REPULO %s: %s", nev, s));
  }
}
