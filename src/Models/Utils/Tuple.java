package Models.Utils;

public class Tuple<T,K>{

  public T key;
  public K value;

  public Tuple(T key, K value) {
    this.key = key;
    this.value = value;
  }
}