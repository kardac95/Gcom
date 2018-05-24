#!/bin/bash
gradle DistJar
mv build/libs/gcom-all-1.0-SNAPSHOT.jar build/libs/gcom.jar
for (( i = 0; i < $1; i++ )); do
	java -jar build/libs/gcom.jar $i $((i + 1337)) &
done

