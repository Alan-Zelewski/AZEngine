package pl.engine.az.system;

public enum SystemPhase {
    /**
     * Pobieranie wejścia od użytkownika,
     * aktualizacja stanu klawiatury/myszy/kontrolera.
     */
    INPUT,


    /**
     * Główna logika gry:
     * AI, gameplay, skrypty, questy itd.
     */
    LOGIC,


    /**
     * Aktualizacja komponentów świata:
     * ruch, transformacje, animacje.
     */
    UPDATE,


    /**
     * Przygotowanie danych do renderowania.
     */
    RENDER_PREPARE,


    /**
     * Rysowanie świata.
     */
    RENDER
}
