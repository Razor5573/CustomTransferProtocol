import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Counter implements Runnable{
    private ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);
    private int bufferLength;
    private double summaryLength;
    private int timeFromSendStart;
    private final static int PERIOD = 2;
    private final static int DELAY = 2;
    @Override
    public void run() {
        scheduledThreadPool.scheduleAtFixedRate(this::countSpeed, DELAY, PERIOD, TimeUnit.SECONDS);
    }

    private void countSpeed() {
        summaryLength += bufferLength;
        timeFromSendStart += PERIOD;
        double speed = summaryLength / timeFromSendStart;
        System.out.println("Current speed is: " + speed);
    }

    public void setBufferLength(int bufferLength){
        this.bufferLength = bufferLength;
    }

    public Counter(int bufferLength){
        this.bufferLength = bufferLength;
    }
}
