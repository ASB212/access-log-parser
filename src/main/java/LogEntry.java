import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LogEntry {
    private final String ipAddr;
    private final LocalDateTime time;
    private final HttpMethod method;
    private final String path;
    private final int responseCode;
    private final long responseSize;
    private final String referer;
    private final UserAgent userAgent;

    public LogEntry(String logLine) {
        String[] parts = logLine.split(" ");

        this.ipAddr = parts[0];

        String dateTime = parts[3].substring(1) + " " + parts[4].substring(0, parts[4].length() - 1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);
        this.time = LocalDateTime.parse(dateTime, formatter);

        String request = parts[5] + " " + parts[6] + " " + parts[7];
        request = request.substring(1, request.length() - 1);
        String[] requestParts = request.split(" ");
        this.method = HttpMethod.valueOf(requestParts[0]);
        this.path = requestParts[1];

        this.responseCode = Integer.parseInt(parts[8]);
        this.responseSize = Long.parseLong(parts[9]);


        if (parts[10].equals("\"-\"")) {
            this.referer = null;
        } else {
            this.referer = parts[10].substring(1, parts[10].length() - 1);
        }

        StringBuilder userAgentBuilder = new StringBuilder();
        for (int i = 11; i < parts.length; i++) {
            userAgentBuilder.append(parts[i]).append(" ");
        }
        String userAgentStr = userAgentBuilder.toString().trim();
        userAgentStr = userAgentStr.substring(1, userAgentStr.length() - 1);
        this.userAgent = new UserAgent(userAgentStr);
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public long getResponseSize() {
        return responseSize;
    }

    public String getReferer() {
        return referer;
    }

    public UserAgent getUserAgent() {
        return userAgent;
    }
}