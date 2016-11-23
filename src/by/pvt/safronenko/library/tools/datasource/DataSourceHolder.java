package by.pvt.safronenko.library.tools.datasource;

/**
 * Holder for data source.
 */
public final class DataSourceHolder {

    /**
     * Instance of data source, global for application.
     * Volatile field for atomic writes and reads.
     */
    private static volatile DataSource dataSource;

    /**
     * Returns current entity manager.
     *
     * @return Entity manager.
     * @throws IllegalStateException If there is no current entity manager.
     */
    public static DataSource getDataSource() {
        DataSource em = dataSource;
        if (em == null) {
            throw new IllegalStateException("Entity manager was not initialized.");
        }
        return em;
    }

    /**
     * Sets data source instance.
     *
     * @param dataSource Data source.
     */
    public static void setDataSource(DataSource dataSource) {
        DataSourceHolder.dataSource = dataSource;
    }
}
