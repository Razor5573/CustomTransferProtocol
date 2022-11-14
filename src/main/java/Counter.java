import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

public class Counter implements Runnable{
    private ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);
    private int uploadedSizeCount;
    private double summarySize;
    private int timeFromSendStart;
    private final static int PERIOD = 1;
    private final static int DELAY = 2;
    @Override
    public void run() {
        scheduledThreadPool.scheduleAtFixedRate(this::countSpeed, DELAY, PERIOD, TimeUnit.SECONDS);
    }

    private void countSpeed() {
        summarySize += uploadedSizeCount;
        timeFromSendStart += PERIOD;
        double averageSpeed = summarySize / timeFromSendStart;
        System.out.println("Average speed speed is: " + averageSpeed);
        System.out.println("Current speed is: " + uploadedSizeCount);
    }

    public void setUploadedSizeCount(int uploadedSizeCount){
        this.uploadedSizeCount = uploadedSizeCount;
    }

    public void shutdown(){
        scheduledThreadPool.shutdown();
    }

    public Counter(int uploadedSizeCount){
        this.uploadedSizeCount = uploadedSizeCount;
    }
}
