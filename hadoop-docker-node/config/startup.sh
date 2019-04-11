#!/usr/bin/env bash
stop-yarn.sh
stop-dfs.sh
/usr/local/livy/bin/livy-server stop
start-all.sh
/usr/local/livy/bin/livy-server start
hadoop fs -chmod 777 /