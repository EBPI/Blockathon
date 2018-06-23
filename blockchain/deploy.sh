#!/bin/bash
git pull
composer archive create -t dir -n .
ps axuwww| grep compose[r]-re| awk '{ print $2 }' | xargs kill
VERSION=`ls -t *bna | head -n1 | awk -F"@" '{ print $NF }' | awk -F"." '{ print $1"." $2"." $3 }'`
composer network install --card PeerAdmin@hlfv1 --archiveFile hypertrace\@$VERSION.bna
composer network upgrade -n hypertrace -V $VERSION -c PeerAdmin@hlfv1
nohup composer-rest-server --port 3001 -c Nike001@hypertrace -n always -w true &
nohup composer-rest-server --port 3002 -c PostNL@hypertrace -n always -w true &
nohup composer-rest-server --port 3003 -c Zalando@hypertrace -n always -w true &
nohup composer-rest-server --port 3004 -c Douane-1749@hypertrace -n always -w true &
nohup composer-rest-server -c admin@hypertrace -n always -w true &
sleep 30
tail -50 nohup.out
