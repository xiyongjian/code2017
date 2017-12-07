package gosigma.etl_log.security;

import java.sql.SQLException;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.init.ScriptException;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "secEntityManagerFactory", transactionManagerRef = "secTransactionManager", basePackages = {
		"gosigma.etl_log.security" })
public class SecDbConfig {
	public static Logger log = LoggerFactory.getLogger(SecDbConfig.class);

	@Bean(name = "secDataSource")
	@ConfigurationProperties(prefix = "gosec.datasource")
	public DataSource dataSource() {
		log.info("Entering...");
		return DataSourceBuilder.create().build();

		// scripts added to datasource
		// EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
		// return builder.addScripts("sec-init.sql").build();
	}
	
	//	// another way to initialize datasource by hand
	//	@Autowired
	//	private Environment env;
	//
	//	@Bean
	//	public DataSource productDataSource() {
	//
	//		DriverManagerDataSource dataSource = new DriverManagerDataSource();
	//		dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
	//		dataSource.setUrl(env.getProperty("product.jdbc.url"));
	//		dataSource.setUsername(env.getProperty("jdbc.user"));
	//		dataSource.setPassword(env.getProperty("jdbc.pass"));
	//
	//		return dataSource;
	//	}

	// run initialization scripts after done
	@Autowired
	private ApplicationContext applicationContext;
	private final String init_sql = "classpath:sec-init.sql";

	@PostConstruct
	public void initInnerDb() throws ScriptException, SQLException {
		log.info("Entering...");
		Resource resource = applicationContext.getResource(init_sql);
		ScriptUtils.executeSqlScript(dataSource().getConnection(), resource);
	}

	@Bean(name = "secEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean secEntityManagerFactory(EntityManagerFactoryBuilder builder,
			@Qualifier("secDataSource") DataSource dataSource) {
		log.info("Entering...");
		return builder.dataSource(dataSource).packages("gosigma.etl_log.security").persistenceUnit("sec").build();
	}

	@Bean(name = "secTransactionManager")
	public PlatformTransactionManager secTransactionManager(
			@Qualifier("secEntityManagerFactory") EntityManagerFactory secEntityManagerFactory) {
		log.info("Entering...");
		return new JpaTransactionManager(secEntityManagerFactory);
	}

}
