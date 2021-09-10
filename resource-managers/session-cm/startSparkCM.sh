#!/bin/bash
../../sbin/start-master.sh -h 127.0.0.1 -p 7077
../../sbin/start-slave.sh 127.0.0.1:7077 -c 6 -m 12000m