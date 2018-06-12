#!/bin/bash
dm_file=/home/jenkins/services/old/aaas-api-data-manager*.jar
cher_file=/home/jenkins/services/old/aaas-app-checkfunds*.jar
aal_file=/home/jenkins/services/old/aaas-api-asset-allocation*.jar
dcl_file=/home/jenkins/services/old/aaas-funds-datacollection-server*.jar

dm_file_name=$(basename $dm_file)
cher_file_name=$(basename $cher_file)
aal_file_name=$(basename $aal_file)
dcl_file_name=$(basename $dcl_file)


dm_work=/home/jenkins/services/working/old/dm_work
cher_work=/home/jenkins/services/working/old/cher_work
aal_work=/home/jenkins/services/working/old/aal_work
dcl_work=/home/jenkins/services/working/old/dcl_work

kill $(ps aux | grep "${dm_work}/${dm_file_name}" | awk '{print $2}')
kill $(ps aux | grep "${cher_work}/${cher_file_name}" | awk '{print $2}')
kill $(ps aux | grep "${aal_work}/${aal_file_name}" | awk '{print $2}')
kill $(ps aux | grep "${dcl_work}/${dcl_file_name}" | awk '{print $2}')

if [ ! -f ${dm_file} ]; then
    echo "File not found! ${dm_file}"
fi
if [ ! -f ${cher_file} ]; then
    echo "File not found! ${dm_file}"
fi
if [ ! -f ${aal_file} ]; then
    echo "File not found! ${dm_file}"
fi
if [ ! -f ${dcl_file} ]; then
    echo "File not found! ${dm_file}"
fi

if [ ! -d ${dm_work} ]; then
	echo "File not found! ${dm_work}"
    mkdir -p ${dm_work}
fi
if [ ! -d ${cher_work} ]; then
	echo "File not found! ${cher_work}"
    mkdir -p ${cher_work}
fi
if [ ! -d ${aal_work} ]; then
	echo "File not found! ${aal_work}"
    mkdir -p ${aal_work}
fi
if [ ! -d ${dcl_work} ]; then
	echo "File not found! ${dcl_work}"
    mkdir -p ${dcl_work}
fi

cp ${dm_file} ${dm_work}
cp ${cher_file} ${cher_work}
cp ${aal_file} ${aal_work}
cp ${dcl_file} ${dcl_work}

chmod -R 755 ${dm_work}/../../

cd ${dm_work}
nohup java -jar -Dspring.profiles.active=dev ${dm_work}/${dm_file_name} 2>&1 &

cd ${cher_work}
nohup java -jar -Dspring.profiles.active=dev ${cher_work}/${cher_file_name} 2>&1 &

cd ${aal_work}
nohup java -jar -Dspring.profiles.active=dev ${aal_work}/${aal_file_name} 2>&1 &

cd ${dcl_work}
nohup java -jar -Dspring.profiles.active=dev ${dcl_work}/${dcl_file_name} 2>&1 &

exit
