
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Statistics {
    private long totalTraffic;
    private LocalDateTime minTime;
    private LocalDateTime maxTime;
    private int requestCount;

    public Statistics() {
        this.totalTraffic = 0;
        this.requestCount = 0;
        this.minTime = null;
        this.maxTime = null;
    }

    public void addEntry(LogEntry entry) {
        this.totalTraffic += entry.getResponseSize();
        this.requestCount++;

        LocalDateTime entryTime = entry.getTime();
        if (this.minTime == null || entryTime.isBefore(this.minTime)) {
            this.minTime = entryTime;
        }
        if (this.maxTime == null || entryTime.isAfter(this.maxTime)) {
            this.maxTime = entryTime;
        }
    }

    public double getTrafficRate() {
        if (minTime == null || maxTime == null || minTime.equals(maxTime)) {
            return 0.0;
        }

        long hoursBetween = ChronoUnit.HOURS.between(minTime, maxTime);
        if (hoursBetween == 0) {
            return totalTraffic;
        }
        return (double) totalTraffic / hoursBetween;
    }

    public long getTotalTraffic() {
        return totalTraffic;
    }

    public LocalDateTime getMinTime() {
        return minTime;
    }

    public LocalDateTime getMaxTime() {
        return maxTime;
    }

    public int getRequestCount() {
        return requestCount;
    }
}