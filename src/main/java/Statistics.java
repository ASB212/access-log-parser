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
    private final HashSet<String> notExistPages;
    private final HashMap<String, Integer> browserRqstCount;
    private int errorRqstCount;
    private int nonBotRqstCount;
    private final Set<String> uniqueNonBotIp;
    private final Map<LocalDateTime, Integer> visitsSecond;
    private final Set<String> refererDomain;
    private final Map<String, Integer> visitsUser;

    public Statistics() {
        this.totalTraffic = 0;
        this.requestCount = 0;
        this.minTime = null;
        this.maxTime = null;
        this.existPages = new HashSet<>();
        this.osRqstCount = new HashMap<>();
        this.notExistPages = new HashSet<>();
        this.browserRqstCount = new HashMap<>();
        this.errorRqstCount = 0;
        this.nonBotRqstCount = 0;
        this.uniqueNonBotIp = new HashSet<>();
        this.visitsSecond = new HashMap<>();
        this.refererDomain = new HashSet<>();
        this.visitsUser = new HashMap<>();
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
        } else if (entry.getResponseCode() == 404) {
            notExistPages.add(entry.getPath());
        }
        if (entry.getResponseCode() >= 400 && entry.getResponseCode() < 600) {
            errorRqstCount++;
        }
        if (!entry.getUserAgent().isBot()) {
            nonBotRqstCount++;
            uniqueNonBotIp.add(entry.getIpAddr());
            LocalDateTime second = entryTime.truncatedTo(ChronoUnit.SECONDS);
            visitsSecond.merge(second, 1, Integer::sum);
            visitsUser.merge(entry.getIpAddr(), 1, Integer::sum);
        }
        if (entry.getReferer() != null) {
            String domain = extractDomain(entry.getReferer());
            if (domain != null) {
                refererDomain.add(domain);
            }
        }
        String os = entry.getUserAgent().getOsType();
        osRqstCount.put(os, osRqstCount.getOrDefault(os, 0) + 1);
        requestCount++;
        String browser = entry.getUserAgent().getBrowser();
        browserRqstCount.put(browser, browserRqstCount.getOrDefault(browser, 0) + 1);
        requestCount++;
    }

    private String extractDomain(String referer) {
        if (referer == null || referer.equals("-") || referer.equals("\"-\"")) {
            return null;
        }

        try {
            String cleaned = referer.replaceAll("^\"|\"$|^https?://", "");

            String domain = cleaned.split("[/?&]")[0].split(":")[0];

            String[] parts = domain.split("\\.");
            return (parts.length >= 2) ? parts[parts.length - 2] + "." + parts[parts.length - 1] : domain;
        } catch (Exception e) {
            return null;
        }
    }

    public int getHighVisitsPerSecond() {
        return visitsSecond.values().stream()
                .max(Integer::compareTo)
                .orElse(0);
    }

    public Set<String> getRefererDomain() {
        return Collections.unmodifiableSet(refererDomain);
    }

    public int getMaxVisitsPerUser() {
        return visitsUser.values().stream()
                .max(Integer::compareTo)
                .orElse(0);
    }

    public double getAverageVisitsPerHour() {
        if (minTime == null || maxTime == null || minTime.equals(maxTime)) {
            return 0.0;
        }

        long hoursBetween = ChronoUnit.HOURS.between(minTime, maxTime);
        if (hoursBetween == 0) {
            return nonBotRqstCount;
        }
        return (double) nonBotRqstCount / hoursBetween;
    }

    public double getAverageErrorRqstsPerHour() {
        if (minTime == null || maxTime == null || minTime.equals(maxTime)) {
            return 0.0;
        }

        long hoursBetween = ChronoUnit.HOURS.between(minTime, maxTime);
        if (errorRqstCount == 0) {
            return 0.0;
        }
        return (double) hoursBetween / errorRqstCount;
    }

    public double getAverageVisitsPerUser() {
        if (uniqueNonBotIp.isEmpty()) {
            return 0.0;
        }
        return (double) nonBotRqstCount / uniqueNonBotIp.size();
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

    public HashSet<String> getNotExistPages() {
        return new HashSet(notExistPages);
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

    public HashMap<String, Double> getBrowserRqstCount() {
        HashMap<String, Double> result = new HashMap<>();
        int requestCount = browserRqstCount.values().stream().mapToInt(Integer::intValue).sum();
        if (requestCount == 0) {
            return result;
        }

        for (Map.Entry<String, Integer> entry : browserRqstCount.entrySet()) {

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

    public int getRequestCount() {
        return requestCount;
    }
}