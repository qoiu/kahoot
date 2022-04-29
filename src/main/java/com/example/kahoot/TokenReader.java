package com.example.kahoot;

import java.io.FileReader;
import java.io.IOException;

public class TokenReader implements Read.From<String> {
    @Override
    public String read(String from) {
        try {
            FileReader reader = new FileReader(from);
            int c;
            StringBuilder str = new StringBuilder();
            while ((c = reader.read()) != -1) {
                str.append((char) c);
            }
            reader.close();
            return str.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
