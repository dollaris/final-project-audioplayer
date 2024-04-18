@echo off
mvn clean compile assembly:single
java -jar .\target\MediaPlayer-1.0-SNAPSHOT-jar-with-dependencies.jar
