#!/usr/bin/zsh

echo "killall -9 java"| ssh sgx-server1
echo "killall -9 java"| ssh sgx-server2
echo "killall -9 java"| ssh sgx-server3
echo "killall -9 java"| ssh sgx-server4

