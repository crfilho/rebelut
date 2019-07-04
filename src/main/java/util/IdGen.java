package util;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class IdGen {
    private IdGen() { }
    private static AtomicLong id = new AtomicLong(1000L);
    public static long genAccID() { return id.incrementAndGet(); }
    public static synchronized String genTxID() { return UUID.randomUUID().toString(); }
}