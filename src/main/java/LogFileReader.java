import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LogFileReader {
    private File file;

    public LogFileReader(File file) {
        this.file = file;
    }

    public void readFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineCount = 0;
            int maxLength = 0;
            int minLength = Integer.MAX_VALUE;

            while ((line = reader.readLine()) != null) {
                lineCount++;
                int length = line.length();

                if (length > 1024) {
                    throw new LineException("В строке № " + lineCount + ": Кол-во cимволов = " + length + " > допустимого значения 1024 ");
                }

                if (length > maxLength) {
                    maxLength = length;
                }
                if (length < minLength) {
                    minLength = length;
                }
            }

            System.out.println("Кол-во строк в файле: " + lineCount);
            System.out.println("Самая длинная строка в файле: " + maxLength);
            System.out.println("Самая короткая строка в файле: " + (minLength == Integer.MAX_VALUE ? 0 : minLength));

        } catch (LineException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}