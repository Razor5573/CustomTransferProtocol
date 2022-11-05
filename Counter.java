import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Counter implements Runnable{
    ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);
    private int bufferLength;
    private double summaryLength;
    private int timeFromSendStart;
    private final int period = 2;
    private double speed;

    @Override
    public void run() {
        scheduledThreadPool.scheduleAtFixedRate(this::countSpeed, 2, period, TimeUnit.SECONDS);
    }

    private void countSpeed() {
        summaryLength += bufferLength;
        timeFromSendStart += period;
        speed = summaryLength/timeFromSendStart;
        System.out.println("Current speed is: " + speed);
    }

    void setBufferLength(int bufferLength){
        this.bufferLength = bufferLength;
    }

    Counter(int bufferLength){
        this.bufferLength = bufferLength;
    }
}
