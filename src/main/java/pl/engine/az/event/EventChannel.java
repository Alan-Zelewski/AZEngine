package pl.engine.az.event;

import java.util.Collections;
import java.util.List;

public class EventChannel<T> {
    private final RingBuffer<T> buffer;
    private final OverflowPolicy policy;
    private int writePointer = 0;

    public EventChannel(int initialCapacity, OverflowPolicy policy) {
        this.buffer = new RingBuffer<>(initialCapacity);
        this.policy = policy;
    }

    public void push(T event) {
        // Tu znajdzie się logika obsługi OverflowPolicy
        buffer.write(event, writePointer);
        writePointer++;
    }

    public List<T> read(EventReader<T> reader) {
        int start = reader.cursorIndex;
        int end = writePointer;

        if(start >= end) {
            return Collections.emptyList();
        }

        // Logika wykrywania zgubionych zdarzeń:
        int unreadCount = end - start;
        if (unreadCount > buffer.capacity()) {
            // Zbyt wolny czytnik - kursor zostaje wyrównany do najstarszego
            // dostępnego elementu w buforze kołowym
            start = end - buffer.capacity();
        }

        List<T> events = buffer.readRange(start, end);
        reader.cursorIndex = end;

        return events;
    }
}
