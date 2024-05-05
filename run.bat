@echo off
javac -d bin -cp src src/utilities/*.java
javac -d bin -cp src src/gui/*.java
javac -d bin -cp src src/*.java
cd bin
java  Main
cd ..
pause