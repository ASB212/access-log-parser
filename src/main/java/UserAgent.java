public class UserAgent {
    private final String osType;
    private final String browser;

    public UserAgent(String userAgentString) {
        this.osType = parseOsType(userAgentString);
        this.browser = parseBrowser(userAgentString);
    }

    private String parseOsType(String userAgent) {
        if (userAgent == null) return "Unknown";
        if (userAgent.contains("Windows")) return "Windows";
        if (userAgent.contains("Mac OS")) return "macOS";
        if (userAgent.contains("Linux")) return "Linux";
        if (userAgent.contains("Android")) return "Android";
        if (userAgent.contains("iOS")) return "iOS";
        return "Unknown";
    }

    private String parseBrowser(String userAgent) {
        if (userAgent == null) return "Unknown";
        if (userAgent.contains("Edge")) return "Edge";
        if (userAgent.contains("Firefox")) return "Firefox";
        if (userAgent.contains("Chrome")) return "Chrome";
        if (userAgent.contains("Opera")) return "Opera";
        if (userAgent.contains("Safari")) return "Safari";
        return "Other";
    }

    public String getOsType() {
        return osType;
    }

    public String getBrowser() {
        return browser;
    }
}