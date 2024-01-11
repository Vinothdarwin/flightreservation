#!/bin/sh
sed -i 's/license.key=/license.key=edd30a0099ba623763b7893aafb88c7f/g;s/My Application/Flight Reservation System/g' /apminsight.conf
java -javaagent:/apminsight-javaagent.jar -jar app.jar