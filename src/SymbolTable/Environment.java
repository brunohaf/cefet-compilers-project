package SymbolTable;

import java.util.*;
import Models.*;

public class Environment {
  private Hashtable table;
  protected Environment prev;

  public Environment(Environment n) {
    table = new Hashtable();
    prev = n;
  }

  public void put(Token w, Tag i) {
    table.put(w, i);
  }

  public Tag get(Token w) {
    for (Environment e = this; e != null; e = e.prev) {
      Tag found = (Tag) e.table.get(w);
      if (found != null)
        return found;
    }
    return null;
  }
}