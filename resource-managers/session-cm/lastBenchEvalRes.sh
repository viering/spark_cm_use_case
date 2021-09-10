#!/bin/sh
echo "\n######\n###\tWe will print the result of the provided query (must be spezified as 01-22) of both CMs\n######\n"

query="Q$1.dat"
echo $query
echo "\nResult from Session CM (last 10 lines):\n"
lastSesRes=$(find ./output/test/tpch -name $query -print0 | xargs -r -0 ls -1 -t | head -1)
tail -n 10 $lastSesRes

echo "\n\n\nResult from Spark CM (last 10 lines):\n"

lastSparkRes=$(find . -maxdepth 1 -name $query -print0 | xargs -r -0 ls -1 -t | head -1)
tail -n 10 $lastSparkRes

echo "\n\n\nDifference between those two (via diff):\n"

diff $lastSesRes $lastSparkRes