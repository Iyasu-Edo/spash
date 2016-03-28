#!/bin/bash

# Compute the bin path
pushd `dirname $0` > /dev/null
SPASH_BIN=`pwd`
popd > /dev/null

# Compute the base path
SPASH_BASE=`dirname $SPASH_BIN`

# Compute the conf path
SPASH_CONF=$SPASH_BASE/conf

# Compute the lib path
SPASH_LIB=$SPASH_BASE/lib

# Jar list
APP_JAR_LIST=`ls -dm $SPASH_LIB/* | tr -d ' \r\n'`

# Main Jar
APP_MAIN_JAR=`echo $APP_JAR_LIST | tr ',' '\n' | grep spash-core`

# Jar Classpath
APP_CLASSPATH=`echo $APP_JAR_LIST | tr ',' '\n' | grep -v spash-core | tr '\n' ':'`

# Jar csv
APP_JARS_CSV=`echo $APP_CLASSPATH | tr ':' ','`

echo "Using classpath: $APP_CLASSPATH"
echo "Using main jar: $APP_MAIN_JAR"

echo "Starting Spash"

eval "spark-submit --class it.nerdammer.spash.shell.Spash \
--master yarn --deploy-mode client \
--jars $APP_JARS_CSV \
$APP_MAIN_JAR"

