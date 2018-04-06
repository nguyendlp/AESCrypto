#!/bin/bash

KEY='dRgUkXp2s5v8yfBaEbHdMbPeShVmYq3t'

for f in $1/*
do
	NOW=$(date +"%m%d%Y%H%M%S")
	java -jar cb9016.jar -i=$f -o=$STORAGE$NOW.zip -k=$KEY -u=$1
done