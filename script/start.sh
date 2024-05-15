#! /bin/bash
PWD=$(dirname "$0")
PID_PATH=$PWD/hcframe-single.pid

if [ -f "$PID_PATH" ]; then
  echo "hcframe-single is running."
  exit
fi

source /etc/profile
nohup java -jar libs/hcframe-single-0.0.1-SNAPSHOT.jar  >/dev/null 2>&1 & echo $! > hcframe-single.pid
