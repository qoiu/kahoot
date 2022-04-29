package com.example.kahoot;

public interface Read<T> {

    interface From<T> extends Read<T> {
        T read(String from);
    }
    interface Default<T> extends Read<T> {
        T read();
    }
}
