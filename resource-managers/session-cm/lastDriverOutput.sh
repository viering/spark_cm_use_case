#!/bin/sh
echo "\n######\n###\tWe will print the last 10 lines of the most recent stdout created by any driver (in the outfolder)\n######\n"
stdPath=$(find . -name "*stdout" -print0 | xargs -r -0 ls -1 -t | grep "Drv_" | head -1)
tail -n 10 $stdPath