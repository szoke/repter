package com.aszoke;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Torony {

  // private static final Integer OSSZES_DOKKOLO_SZAMA = 1;
  private static final Integer OSSZES_DOKKOLO_SZAMA = 10;
  private static final Integer LESZALLAS_IDEJE = 100;
  private static final Integer FELSZALLAS_IDEJE = 100;

  private boolean kifutoPalyaSzabad = true;
  private int szabadDokkolokSzama = OSSZES_DOKKOLO_SZAMA;

  private List<String> leszalltRepulok = new ArrayList<>();
  private List<String> felszalltRepulok = new ArrayList<>();

  public synchronized void leszallnek(String repuloNev) throws InterruptedException {
    logFoglaltsaggal(repuloNev + " le szeretne szallni.");

    while (!(kifutoPalyaSzabad && vanSzabadDokkolo())) {
      log(repuloNev + " most nem szallhat le.");
      wait();
    }

    log(repuloNev + " leszallhat.");
    kifutoPalyaSzabad = false;

    varakozas(LESZALLAS_IDEJE);
  }

  public synchronized void leszalltam(String repuloNev) {
    kifutoPalyaSzabad = true;
    szabadDokkolokSzama--;
    leszalltRepulok.add(repuloNev);
    logFoglaltsaggal(repuloNev + " leszallt.");
    notifyAll();
  }

  public synchronized void felszallnek(String repuloNev) throws InterruptedException {
    logFoglaltsaggal(repuloNev + " fel szeretne szallni.");

    while (!kifutoPalyaSzabad) {
      log(repuloNev + " most nem szallhat fel.");
      wait();
    }

    log(repuloNev + " felszallhat.");
    kifutoPalyaSzabad = false;

    varakozas(FELSZALLAS_IDEJE);
  }

  public synchronized void felszalltam(String repuloNev) {
    kifutoPalyaSzabad = true;
    szabadDokkolokSzama++;
    felszalltRepulok.add(repuloNev);
    logFoglaltsaggal(repuloNev + " felszallt.");
    notifyAll();
  }

  private boolean vanSzabadDokkolo() {
    return szabadDokkolokSzama > 0;
  }

  private void varakozas(int mennyit) {
    try {
      log("A felszallas/leszallas " + mennyit + " millisecig tart.");
      Thread.sleep(mennyit);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private void log(String s) {
    System.out.println(String.format("TORONY: %s ", s));
  }

  private void logFoglaltsaggal(String s) {
    System.out.println(String.format("TORONY: %s A kifutopalya %s. Szabad dokkolok: %d / %d",
        s,
        kifutoPalyaSzabad ? "szabad" : "foglalt",
        szabadDokkolokSzama,
        OSSZES_DOKKOLO_SZAMA));
  }

  public List<String> getLeszalltRepulok() {
    leszalltRepulok.sort(Comparator.naturalOrder());
    return leszalltRepulok;
  }

  public List<String> getFelszalltRepulok() {
    felszalltRepulok.sort(Comparator.naturalOrder());
    return felszalltRepulok;
  }
}
