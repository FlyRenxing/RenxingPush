package top.imzdx.qqpush.utils;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

public class Base64ListEnCode {
    public static void main(String[] args) {
        final Base64.Encoder encoder = Base64.getEncoder();
        final Base64.Decoder decoder = Base64.getDecoder();
        StringBuilder sb = new StringBuilder();
        FileReader fileReader = new FileReader("static/badWord.txt");
        List<String> readLines = fileReader.readLines();
        for (String line : readLines) {
            sb.append(encoder.encodeToString(line.getBytes(StandardCharsets.UTF_8))).append("\n");
//            sb.append(new String(decoder.decode(line), StandardCharsets.UTF_8)).append("\n");
        }
        FileWriter fileWriter = new FileWriter("static/badWord.txt");
        fileWriter.write(sb.toString());
    }
}
