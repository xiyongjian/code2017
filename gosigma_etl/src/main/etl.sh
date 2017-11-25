# copy content, and must use dos2unix convert the format (CR/LF)
# cron job setting:
#       */2 * * * * /var/etl/etl.sh m02_IESO_RealtimeConstTotals 2>&1 | /usr/bin/logger -t ETL

cd /var/etl

script=$0
feed=$(echo $1 | sed 's/^.\{4\}//')
CLASSPATH=.:gosigma_etl.jar:$(find lib -type f -name '*.jar' | tr '\n' ':' | sed 's/:$//')
export CLASSPATH

echo $feed - start at $(date +%Y%m%d-%H%M%S.%N)
echo $feed - $0 $*
echo $feed - CLASSPATH=$CLASSPATH
shift
echo $feed - java gosigma.etl.${feed} --cron $*
java gosigma.etl.${feed} --cron $*
echo $feed - return : $?
