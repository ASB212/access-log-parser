import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LogFileReader {
    final File file;

    public LogFileReader(File file) {
        this.file = file;
    }

    public void readFile() throws LineException {
        Statistics statistics = new Statistics();
        int googlebotCount = 0;
        int yandexbotCount = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineCount = 0;

            while ((line = reader.readLine()) != null) {
                lineCount++;
                int length = line.length();

                if (length > 1024) {
                    throw new LineException("В строке № " + lineCount + ": Кол-во cимволов = " + length + " > допустимого значения 1024 ");
                }

                LogEntry entry = new LogEntry(line);
                statistics.addEntry(entry);

                UserAgent userAgent = entry.getUserAgent();
                String userAgentString = userAgent.getBrowser() + "/" + userAgent.getOsType();
                if (userAgentString.contains("Googlebot")) {
                    googlebotCount++;
                } else if (userAgentString.contains("YandexBot")) {
                    yandexbotCount++;
                }
            }

            //System.out.println("Объём трафика: " + statistics.getTrafficRate() + " байт/час");
            //System.out.println("Общий объём: " + statistics.getRequestCount());
            //System.out.println("Список: " + statistics.getNotExistPages());
            //System.out.println("Доля ОС: " + statistics.getOsRqstCount());
            System.out.println("Доля браузер: " + statistics.getBrowserRqstCount());
        } catch (LineException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}