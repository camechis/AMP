#!/bin/bash

if [$# -lt 3]
then
	echo "Usage: ./send-notification.sh [from] [to] [message]"
else
	from=$1
	to=$2
	while [ -n "$3" ]; do
	   message="$message $3"
	   shift
	done
	java -jar target/amp.examples.usernotifications-3.1.0.jar send conf/server-basicAuth.yaml $from $to $message
fi