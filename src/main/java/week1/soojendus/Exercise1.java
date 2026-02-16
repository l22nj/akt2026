package week1.soojendus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Exercise1 {
    static void main(String[] args) throws IOException {
        String filename = args[0];

        var lines = Files.readAllLines(new File(filename).toPath());

        var sum = 0;
        for (var line: lines) {
            sum += Integer.parseInt(line.trim());
        }
        System.out.println(sum);
    }
}
