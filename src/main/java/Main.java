import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int count = 0;

        while (true) {
            System.out.print("Введите путь к файлу в формате 'C:\\...\\file.log': ");
            String path = scanner.nextLine();
            File file = new File(path);
            boolean fileExists = file.exists();
            boolean isFile = file.isFile();

            if (!fileExists || !isFile) {
                System.out.println("Указанный файл не существует или указанный путь является путём к папке, а не к файлу.");
                continue;
            }

            count++;
            System.out.println("Путь указан верно.");
            System.out.println("Это файл № " + count + ".");
            LogFileReader logFileReader = new LogFileReader(file);
            logFileReader.readFile();
        }

    }
}

