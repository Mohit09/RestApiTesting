package com.sbic.turbine.configuration;

import java.beans.PropertyVetoException;
import java.sql.SQLException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.security.UserGroupInformation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import com.cloudera.impala.jdbc41.DataSource;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Class responsible for creation of Impala Datasource which will be used to get
 * the connection from pool.
 * 
 *
 */
@org.springframework.context.annotation.Configuration
public class ImpalaDataSourceConfig {

	@Value("${impala.trust-store.key}")
	private String trustStore;
	@Value("${impala.krb.file}")
	private String krbFile;
	@Value("${impala.jaas.file}")
	private String jassFile;
	@Value("${impala.connection.url}")
	private String jdbcURL;
	@Value("${impala.jdbc.driver}")
	private String jdbcDriver;

	@Value("${impala.maxPoolSize}")
	private String connectionPoolSize;
	@Value("${impala.minimumIdleTime}")
	private String minimumIdleTime;
	@Value("${impala.connectionTimeOut}")
	private String connectionTimeOut;
	@Value("${impala.idleTimeOut}")
	private String idleTimeOut;
	@Value("${impala.spring.fqdn}")
	private String krb_kdc;
	@Value("${impala.domain}")
	private String impalaDomain;

	/**
	 * <p>
	 * Method is Responsible for creation of Impala Datasource.
	 * <p>
	 * <p>
	 * In this method <b>JAAS(Java Authentication and Authorization Service)</b>, It
	 * provides a standard configuration file format for specifying login context.
	 * It will use <b>Kerberos</b> authentication mechanism to do the authentication
	 * and get the datasource object created.
	 * 
	 * @return
	 * @throws SQLException
	 * @throws PropertyVetoException
	 */
	@Bean
	public HikariDataSource impalaDS() throws SQLException, PropertyVetoException {
		System.setProperty("java.security.krb5.realm", impalaDomain);
		System.setProperty("java.security.krb5.kdc", krb_kdc);
		System.setProperty("javax.security.auth.useSubjectCredsOnly", "false");
		System.setProperty("java.security.krb5.conf", krbFile);
		System.setProperty("java.security.auth.login.config", jassFile);
		System.setProperty("sun.security.provider.certpath", trustStore);

		Configuration conf = new Configuration();
		conf.set("hadoop.security.authentication", "kerberos");
		UserGroupInformation.setConfiguration(conf);

		DataSource impalaDS = new DataSource();
		impalaDS.setURL(jdbcURL);

		HikariDataSource hikariDS = new HikariDataSource();
		hikariDS.setDataSource(impalaDS);

		hikariDS.setMaximumPoolSize(Integer.valueOf(connectionPoolSize));
		hikariDS.setMinimumIdle(Integer.valueOf(minimumIdleTime));
		hikariDS.setAutoCommit(true);
		hikariDS.setConnectionTimeout(Integer.valueOf(connectionTimeOut));
		hikariDS.setIdleTimeout(Integer.valueOf(idleTimeOut));

		return hikariDS;
	}
}
