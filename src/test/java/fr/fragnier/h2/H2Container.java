package fr.fragnier.h2;

import org.h2.Driver;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.utility.DockerImageName;

public class H2Container<SELF extends H2Container<SELF>> extends JdbcDatabaseContainer<SELF> {

    private String databaseName;

    public H2Container() {
        super(DockerImageName.parse("vanouche/h2-server:2.1.210"));
        addExposedPort(9092);
        withDatabaseName("test");
    }

    @Override
    public String getJdbcUrl() {
        return "jdbc:h2:tcp://" + getContainerIpAddress() + ":"
                + getMappedPort(9092) + "/" + getDatabaseName();
    }

    @Override
    public String getDriverClassName() {
        return Driver.class.getName();
    }

    @Override
    public String getUsername() {
        return "";
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    protected String getTestQueryString() {
        return "select 1;";
    }

    @Override
    public String getDatabaseName() {
        return databaseName;
    }

    @Override
    public SELF withDatabaseName(final String databaseName) {
        this.databaseName = databaseName;
        return self();
    }
}
