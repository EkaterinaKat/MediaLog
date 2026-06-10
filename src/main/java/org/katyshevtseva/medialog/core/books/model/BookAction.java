package org.katyshevtseva.medialog.core.books.model;

public enum BookAction {
    READ_RUS("текст|рус", ""),
    READ_ENG("текст|eng", "eng"),
    READ_RUS_ENG("текст|рус текст|eng", "рус eng"),
    READ_2_RUS("текст|рус|*2", "*2"),
    LIS_RUS("аудио|рус", "аудио"),
    READ_LIS_RUS("текст|рус аудио|рус", "текст аудио");

    private final String fullInfo;
    private final String shortInfo;

    BookAction(String fullInfo, String shortInfo) {
        this.fullInfo = fullInfo;
        this.shortInfo = shortInfo;
    }

    public String getFullInfo() {
        return fullInfo;
    }

    public String getShortInfo() {
        return shortInfo;
    }

    @Override
    public String toString() {
        return fullInfo;
    }
}
