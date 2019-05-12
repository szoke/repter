package com.aszoke;

public class Kifutopalya {

  private KifutopalyaAllapot allapot = KifutopalyaAllapot.SZABAD;
  private Repulo leszalloRepulo;
  private Repulo felszalloRepulo;

  public void leszall(Repulo repulo) {
    this.allapot = KifutopalyaAllapot.FOGLALT;
    this.leszalloRepulo = repulo;
  }

  public void felszall(Repulo repulo) {
    this.allapot = KifutopalyaAllapot.FOGLALT;
    this.felszalloRepulo = repulo;
  }

  public void szabadraAllit() {
    this.allapot = KifutopalyaAllapot.SZABAD;
    this.leszalloRepulo = null;
  }

  public KifutopalyaAllapot getAllapot() {
    return allapot;
  }

  public Repulo getLeszalloRepulo() {
    return leszalloRepulo;
  }

  public Repulo getFelszalloRepulo() {
    return felszalloRepulo;
  }

  public enum KifutopalyaAllapot {
    SZABAD,
    FOGLALT
  }
}

