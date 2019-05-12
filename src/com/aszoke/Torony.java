package com.aszoke;

import com.aszoke.Dokkolo.DokkoloAllapot;
import com.aszoke.Kifutopalya.KifutopalyaAllapot;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class Torony {

  //private static final Integer OSSZES_DOKKOLO_SZAMA = 5;
  private static final Integer LESZALLAS_IDEJE = 100;
  private static final Integer FELSZALLAS_IDEJE = 100;

  //private boolean kifutoPalyaSzabad = true;
  private Kifutopalya kifutopalya;
  private List<Dokkolo> dokkolok;
  // private int szabadDokkolokSzama = OSSZES_DOKKOLO_SZAMA;

  private List<String> leszalltRepulok = new ArrayList<>();
  private List<String> felszalltRepulok = new ArrayList<>();

  public Torony(Kifutopalya kifutopalya, List<Dokkolo> dokkolok) {
    this.kifutopalya = kifutopalya;
    this.dokkolok = dokkolok;
  }

  public synchronized void leszallnek(Repulo repulo) throws InterruptedException {
    if (dokkolok.stream()
        .filter(dokkolo -> dokkolo.getHasznalo() != null)
        .anyMatch(dokkolo -> dokkolo.getHasznalo().equals(repulo))) {
      throw new RuntimeException("Olyan repulo akar leszallni, aminek mar fenn van tartva dokkolo "
          + "vagy mar egy dokkoloban parkol!");
    }

    logFoglaltsaggal(repulo.getNev() + " szamu repulo le szeretne szallni.");

    while (!kifutopalya.getAllapot().equals(KifutopalyaAllapot.SZABAD)
        || !getSzabadDokkolo().isPresent()) {
      log("A kifuto foglalt vagy nincs szabad dokkolo. " + repulo.getNev()
          + " most nem szallhat le.");
      wait();
    }

    Optional<Dokkolo> szabadDokkolo = getSzabadDokkolo();
    log(szabadDokkolo.get().getId() + " szamu dokkolot fenntartom " + repulo.getNev()
        + " szamu repulonek.");
    getSzabadDokkolo().get().fenntart(repulo);

    log(repulo.getNev() + " szamu repulo leszallhat.");
    //kifutoPalyaSzabad = false;
    kifutopalya.leszall(repulo);

    varakozas(LESZALLAS_IDEJE);
  }

  public synchronized void leszalltam(Repulo repulo) {
    //kifutoPalyaSzabad = true;
    if (!kifutopalya.getLeszalloRepulo().equals(repulo)) {
      throw new RuntimeException("Nem ennek a repulonek kellett volna leszallnia! A kifutopalyat "
          + "masik repulonek foglaltam.");
    }

    Optional<Dokkolo> fenntartottDokkolo = getFenntartottDokkolo(repulo);
    if (!fenntartottDokkolo.isPresent()) {
      throw new RuntimeException("Leszallt egy olyan repulo, aminek nem volt fenntartva "
          + "dokkolo!.");
    }

    log(fenntartottDokkolo.get().getId() + " szamu dokkoloba beparkolt " + repulo.getNev()
        + " szamu repulo.");
    fenntartottDokkolo.get().beparkol(repulo);
    //szabadDokkolokSzama--;

    kifutopalya.szabadraAllit();
    leszalltRepulok.add(repulo.getNev());
    logFoglaltsaggal(repulo.getNev() + " szamu repulo leszallt.");
    notifyAll();
  }

  public synchronized void felszallnek(Repulo repulo) throws InterruptedException {
    Optional<Dokkolo> foglaltDokkolo = getFoglaltDokkolo(repulo);
    if (!foglaltDokkolo.isPresent()) {
      throw new RuntimeException("Fel akar szallni egy repulo, ami nem parkol egyik "
          + "dokkoloban sem!");
    }

    logFoglaltsaggal(repulo.getNev() + " fel szeretne szallni.");

    while (!kifutopalya.getAllapot().equals(KifutopalyaAllapot.SZABAD)) {
      log(repulo.getNev() + " most nem szallhat fel.");
      wait();
    }

    log(repulo.getNev() + " szamu repulo felszallhat.");
    //kifutoPalyaSzabad = false;
    kifutopalya.felszall(repulo);

    varakozas(FELSZALLAS_IDEJE);
  }

  public synchronized void felszalltam(Repulo repulo) {
    Optional<Dokkolo> foglaltDokkolo = getFoglaltDokkolo(repulo);
    if (!foglaltDokkolo.isPresent()) {
      throw new RuntimeException("Felszallt egy repulo, ami nem is parkol egyik "
          + "dokkoloban sem!");
    }

    if (!kifutopalya.getFelszalloRepulo().equals(repulo)) {
      throw new RuntimeException("Nem ennek a repulonek kellett volna felszallnia! "
          + "A kifutopalyat egy masik repulonek foglaltam.");
    }

    //kifutoPalyaSzabad = true;
    foglaltDokkolo.get().szabadraAllit();
    kifutopalya.szabadraAllit();
    //szabadDokkolokSzama++;
    felszalltRepulok.add(repulo.getNev());
    logFoglaltsaggal(repulo.getNev() + " szamu repulo felszallt.");
    notifyAll();
  }

  private Optional<Dokkolo> getSzabadDokkolo() {
    return dokkolok.stream()
        .filter(dokkolo -> dokkolo.getAllapot().equals(DokkoloAllapot.SZABAD))
        .findAny();
  }

  private Optional<Dokkolo> getFenntartottDokkolo(Repulo repulo) {
    return dokkolok.stream()
        .filter(dokkolo -> dokkolo.getAllapot().equals(DokkoloAllapot.FENNTARTVA) &&
            dokkolo.getHasznalo().equals(repulo))
        .findAny();
  }

  private Optional<Dokkolo> getFoglaltDokkolo(Repulo repulo) {
    return dokkolok.stream()
        .filter(dokkolo -> dokkolo.getAllapot().equals(DokkoloAllapot.FOGLALT) &&
            dokkolo.getHasznalo().equals(repulo))
        .findAny();
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
    System.out.println(String.format("TORONY: %s A kifutopalya %s. Szabad dokkolok: %d / %d. "
            + "Reszletek: %s",
        s,
        kifutopalya.getAllapot().equals(KifutopalyaAllapot.SZABAD) ? "szabad" : "foglalt",
        dokkolok.stream()
            .filter(dokkolo -> dokkolo.getAllapot().equals(DokkoloAllapot.SZABAD))
            .count(),
        dokkolok.size(),
        dokkolokAllapotaAsString()));
  }

  private String dokkolokAllapotaAsString() {
    String msg = "Dokkolo %d %s %s";
    StringBuilder sb = new StringBuilder();
    sb.append("(");
    for (Dokkolo d : dokkolok) {
      sb.append(
          String.format(msg,
              d.getId(),
              d.getAllapot(),
              d.getHasznalo() != null ? d.getHasznalo().getNev() + " repulonek," : ","));
    }
    sb.append(")");
    return sb.toString();
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
