package config;

public final class DbConfig {
    private DbConfig() {}

    public static final String URL = "jdbc:postgresql://localhost:5432/mydb";
    public static final String USER = "postgres";
    public static final String PASSWORD = "postgres";
}
