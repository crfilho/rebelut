package util;
import java.util.Random;

public class IdGen {
    private IdGen() { }
    private static volatile long id = 1000L;
    public static synchronized long genAccID() { return id++; }
    public static synchronized long genTxID() {
        return new Random().nextLong();
    }
}