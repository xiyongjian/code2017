tomcat

server, conf/context.xml, setup resource

<GlobalNamingResources>
	<Resource name="jdbc/DatabaseName" auth="Container"
		type="javax.sql.DataSource" username="user" password="password"
		url="jdbc:mysql://localhost:3306/DB_USERS" driverClassName="com.mysql.jdbc.Driver"
		initialSize="5" maxWait="5000" maxActive="120" maxIdle="5"
		validationQuery="select 1" poolPreparedStatements="true" />
</GlobalNamingResources>

or 
<Resource auth="Container" 
driverClassName="com.mysql.jdbc.Driver" 
maxActive="100"  maxIdle="30" maxWait="10000"
name="jdbc/TestDB" type="javax.sql.DataSource" 
url="jdbc:mysql://localhost:3306/test?rewriteBatchedStatements=true" 
username="username"  password="password" />

webapp, web.xml, add reference
<resource-ref >
<description >MySQL Datasource example </description >
<res-ref-name >jdbc/TestDB </res-ref-name >
<res-type >javax.sql.DataSource </res-type >
<res-auth >Container </res-auth >
</resource-ref >

