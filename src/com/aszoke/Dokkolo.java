package com.aszoke;

public class Dokkolo {

  private Integer id;
  private DokkoloAllapot allapot = DokkoloAllapot.SZABAD;
  private Repulo hasznalo;

  public Dokkolo(Integer id) {
    this.id = id;
  }

  public void fenntart(Repulo repulo) {
    this.allapot = DokkoloAllapot.FENNTARTVA;
    this.hasznalo = repulo;
  }

  public void beparkol(Repulo repulo) {
    this.allapot = DokkoloAllapot.FOGLALT;
    this.hasznalo = repulo;
  }

  public void szabadraAllit() {
    this.allapot = DokkoloAllapot.SZABAD;
    this.hasznalo = null;
  }

  public Integer getId() {
    return id;
  }

  public DokkoloAllapot getAllapot() {
    return allapot;
  }

  public Repulo getHasznalo() {
    return hasznalo;
  }

  public enum DokkoloAllapot {
    SZABAD,
    FENNTARTVA,
    FOGLALT;
  }
}

