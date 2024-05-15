#!/bin/bash

cygwin=false
linux=false
case "$(uname)" in
CYGWIN*)
  cygwin=true
  ;;
Linux*)
  linux=true
  ;;
esac

get_pid() {
  STR=$1
  PID=$2
  if $cygwin; then
    JAVA_CMD="$JAVA_HOME\bin\java"
    JAVA_CMD=$(cygpath --path --unix "$JAVA_CMD")
    JAVA_PID=$(ps |grep "$JAVA_CMD" |awk '{print $1}')
  else
    if $linux; then
      if [ -n "$PID" ]; then
        JAVA_PID=$(ps -C java -f --width 1000 | grep "$STR" | grep "$PID" | grep -v grep | awk '{print $2}')
      else
        JAVA_PID=$(ps -C java -f --width 1000 | grep "$STR" | grep -v grep | awk '{print $2}')
      fi
    else
      if [ -n "$PID" ]; then
        JAVA_PID=$(ps aux | grep "$STR" | grep "$PID" | grep -v grep | awk '{print $2}')
      else
        JAVA_PID=$(ps aux | grep "$STR" | grep -v grep | awk '{print $2}')
      fi
    fi
  fi
  echo "$JAVA_PID"
}

APP_MAIN='hcframe-single'
PWD=$(dirname "$0")
PID_PATH=$PWD/hcframe-single.pid

if [ ! -f "$PID_PATH" ]; then
  echo "$APP_MAIN is not running."
  exit
fi

pid=$(cat "$PID_PATH")
if [ "$pid" == "" ]; then
  pid=$(get_pid "$APP_MAIN")
  echo "$pid"
fi

echo -e "$(hostname): stopping $APP_MAIN $pid ... "
kill "$pid"

LOOPS=0
while (true); do
  gpid=$(get_pid $APP_MAIN "$pid")
  if [ -z "$gpid" ] || [ "$gpid" == "" ]; then
    echo "Oook! cost:$LOOPS"
    rm "$PID_PATH"
    break
  fi
 let LOOPS=LOOPS+1
  sleep 1
done
