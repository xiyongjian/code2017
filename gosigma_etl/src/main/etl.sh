# copy content, and must use dos2unix convert the format (CR/LF)

# cron job setting:
#  	*/2 * * * * /var/etl/etl.sh 2>&1 | /usr/bin/logger -t ETL


cd /var/etl

export CLASSPATH=.
export CLASSPATH=$CLASSPATH:./gosigma_etl.jar
export CLASSPATH=$CLASSPATH:./lib/commons-compiler-3.0.7.jar
export CLASSPATH=$CLASSPATH:./lib/commons-csv-1.4.jar
export CLASSPATH=$CLASSPATH:./lib/commons-io-2.6.jar
export CLASSPATH=$CLASSPATH:./lib/commons-lang-2.6.jar
export CLASSPATH=$CLASSPATH:./lib/janino-3.0.7.jar
export CLASSPATH=$CLASSPATH:./lib/logback-classic-1.2.2.jar
export CLASSPATH=$CLASSPATH:./lib/logback-core-1.2.2.jar
export CLASSPATH=$CLASSPATH:./lib/mariadb-java-client-2.0.3.jar
export CLASSPATH=$CLASSPATH:./lib/slf4j-api-1.7.25.jar
echo $CLASSPATH

# java gosigma.etl.IESO_PUB_RealtimeConstTotals
echo $CLASSPATH
echo java gosigma.etl.IESO_RealtimeConstTotals
java gosigma.etl.IESO_RealtimeConstTotals

