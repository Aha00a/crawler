#!/bin/sh

target/universal/stage/bin/crawler -Dconfig.file=conf/application.conf -Duser.timezone=Asia/Seoul -Dhttp.port=13000 -Dpidfile.path=RUNNING_PID_13000

