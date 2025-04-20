import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LogFileReader {
    final File file;

    public LogFileReader(File file) {
        this.file = file;
    }

    public void readFile() {
        int requests = 0;
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

                requests++;

                String userAgent = userAgent(line);
                if (userAgent != null) {
                    if (userAgent.contains("Googlebot")) {
                        googlebotCount++;
                    } else if (userAgent.contains("YandexBot")) {
                        yandexbotCount++;
                    }
                }
            }

            System.out.println("Всего запросов: " + requests);
            System.out.println("доля запросов Googlebot: " + googlebotCount + " (" + (googlebotCount * 100.0 / requests) + "%)");
            System.out.println("доля запросов YandexBot: " + yandexbotCount + " (" + (yandexbotCount * 100.0 / requests) + "%)");

        } catch (LineException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String userAgent(String logLine) {
        int start = logLine.indexOf("\" \"") + 3;
        int end = logLine.lastIndexOf("\"");
        if (start >= 3 && end > start) {
            return logLine.substring(start, end);
        }
        return null;
    }
}