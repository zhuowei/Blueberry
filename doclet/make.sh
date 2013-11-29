#!/bin/sh
# setting tools jar directory for my machine. Change it to your machine's path if needed
JAVA_TOOLS_JAR=/usr/lib/jvm/java-6-openjdk-i386/lib/tools.jar
mkdir bin
javac -cp src:$JAVA_TOOLS_JAR -d bin `find src -type f -printf "%p "`
