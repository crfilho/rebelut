package util;
import java.util.UUID;
import java.util.Random;

public class IdGen {
    private IdGen() { }
    private static volatile long id = 1000L;
    public static synchronized long genAccID() { return id++; } // new Random().nextLong();
    public static synchronized String genTxID() { return UUID.randomUUID().toString(); }
}