#!/bin/sh
service="gts"
serviceAccount="gts"
group="ampere"
gts_logs="/var/log/gts/"
home="/opt/$service"

grep -i $group /etc/group
if [ ! $? -eq 0 ]; then
	/usr/sbin/groupadd $group
fi
if [ ! -d $gts_logs ]; then
	mkdir -p $gts_logs
	chgrp -R $group $gts_logs
	chmod -R 775 $gts_logs
fi
useradd -r -M -G $group -d $home $serviceAccount
/sbin/chkconfig --add $service
