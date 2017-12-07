this project is spring boot project, for browse base ETL directory
and check log/data files, setting.

HAL browser in default
localhost:8080/
localhost:8080/browser

actuator info
http://localhost:8080/actuator


security in package gosigma.etl_log.security, data source, special properties
gosec.xxxx
gosec.datasource

; H2 console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

db initial script is added to datasource bean
		// scripts added to datasource
		EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
		return builder.addScripts("schema-sec.sql", "data-sec.sql").build();
		
inject properties
    @Value( "${jdbc.url}" )
    private String jdbcUrl;
