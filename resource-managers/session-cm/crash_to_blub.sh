#!/bin/bash
workPWD="/home/dspadmin/git/spark_cm_use_case/work"
filename=`ls -Art $workPWD | tail -n 1`
filename=$workPWD"/"$filename
echo $(($(date +%s) - $(date +%s -r "$filename")))