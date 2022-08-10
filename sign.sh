#!/bin/bash
for f in "$PWD/core/build/libs"/*.jar
do
	echo Signing "${f}"
	gpg -ab ${f}
done
for f in "$PWD/core/build/libs"/*.pom
do
	echo Signing "${f}"
	gpg -ab ${f}
done

for f in "$PWD/bukkit/build/libs"/*.jar
do
	echo Signing "${f}"
	gpg -ab ${f}
done
for f in "$PWD/bukkit/build/libs"/*.pom
do
	echo Signing "${f}"
	gpg -ab ${f}
done

for f in "$PWD/velocity/build/libs"/*.jar
do
	echo Signing "${f}"
	gpg -ab ${f}
done
for f in "$PWD/velocity/build/libs"/*.pom
do
	echo Signing "${f}"
	gpg -ab ${f}
done