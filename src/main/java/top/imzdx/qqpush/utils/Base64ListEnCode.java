package top.imzdx.qqpush.utils;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

public class Base64ListEnCode {
    public static void main(String[] args) {
        final Base64.Encoder encoder = Base64.getEncoder();
        StringBuilder sb = new StringBuilder();
        FileReader fileReader = new FileReader("static/敏感词.txt");
        List<String> readLines = fileReader.readLines();
        for (String line : readLines) {
            sb.append(encoder.encodeToString(line.getBytes(StandardCharsets.UTF_8))).append("\n");
        }
        FileWriter fileWriter = new FileWriter("static/敏感词.txt");
        fileWriter.write(sb.toString());
    }
}
