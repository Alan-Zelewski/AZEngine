package pl.engine.az.event;

import java.util.ArrayList;
import java.util.List;

public class RingBuffer<T> {
    private final Object[] buffer;
    private final int capacity;

    public RingBuffer(int capacity) {
        this.capacity = capacity;
        this.buffer = new Object[capacity];
    }

    public int capacity() {
        return capacity;
    }

    public void write(T event, int writePointer) {
        buffer[writePointer % capacity] = event;
    }

    @SuppressWarnings("unchecked")
    public List<T> readRange(int startAbsolute, int ednAbsolute) {
        int count = ednAbsolute - startAbsolute;
        List<T> result = new ArrayList<>(count);
        for (int i = startAbsolute; i < ednAbsolute; i++) {
            result.add((T) buffer[1%capacity]);
        }
        return result;
    }
}
