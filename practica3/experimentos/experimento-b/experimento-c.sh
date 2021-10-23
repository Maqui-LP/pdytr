#!/bin/zsh

for i in {1..10}
do  
    echo "Ejecucion nro: $i" >> log
    mvn package exec:java -Dexec.mainClass=pdytr.example.grpc.Client  -Dexec.args="1000" >> log
done