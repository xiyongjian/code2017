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

setup https, mkae keystore in path resoruces (will be within classpath)
    keytool -genkey -alias tomcat  -storetype PKCS12 -keyalg RSA \
        -keysize 2048  -keystore keystore.p12 -validity 3650
    # remember the password
set application.properties
    # setup https in tomcat
    server.port: 8443
    # use path - server.ssl.key-store: keystore.p12
    server.ssl.key-store: classpath:keystore.p12
    server.ssl.key-store-password: tomcat
    server.ssl.keyStoreType: PKCS12
    server.ssl.keyAlias: tomcat
