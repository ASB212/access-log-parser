import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Statistics {
    private long totalTraffic;
    private LocalDateTime minTime;
    private LocalDateTime maxTime;
    private int requestCount;
    private final HashSet<String> existPages;
    private final HashMap<String, Integer> osRqstCount;

    public Statistics() {
        this.totalTraffic = 0;
        this.requestCount = 0;
        this.minTime = null;
        this.maxTime = null;
        this.existPages = new HashSet<>();
        this.osRqstCount = new HashMap<>();
        
    }

    public void addEntry(LogEntry entry) {
        this.totalTraffic += entry.getResponseSize();

        LocalDateTime entryTime = entry.getTime();
        if (this.minTime == null || entryTime.isBefore(this.minTime)) {
            this.minTime = entryTime;
        }
        if (this.maxTime == null || entryTime.isAfter(this.maxTime)) {
            this.maxTime = entryTime;
        }
        if (entry.getResponseCode() == 200) {
            existPages.add(entry.getPath());
        }
        String os = entry.getUserAgent().getOsType();
        osRqstCount.put(os, osRqstCount.getOrDefault(os, 0) + 1);
        requestCount++;
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
    public HashSet<String> getExistPages() {
       return new HashSet(existPages);
    }

    public HashMap<String, Double> getOsRqstCount() {
        HashMap<String, Double> result = new HashMap<>();
        int requestCount = osRqstCount.values().stream().mapToInt(Integer::intValue).sum();
        if (requestCount == 0) {
            return result;
        }

        for (Map.Entry<String, Integer> entry : osRqstCount.entrySet()) {

            double ratio = (double) entry.getValue() / requestCount;
            ratio = Math.round(ratio * 100) / 100.0;

            result.put(entry.getKey(), ratio);
        }

        return result;
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

    public int getRequestCount() {return requestCount;}
}