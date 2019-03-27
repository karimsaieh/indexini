#!/usr/bin/env bash
start-all.sh
/usr/local/livy/bin/livy-server start
hadoop fs -chmod 777 /