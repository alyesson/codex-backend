package br.com.codex.v1.configuration;

public class DatabaseContextHolder {
    private static final ThreadLocal<String> CONTEXT = new ThreadLocal<>();

    public static void setCurrentDb(String dbName) {
        CONTEXT.set(dbName);
    }

    public static String getCurrentDb() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
