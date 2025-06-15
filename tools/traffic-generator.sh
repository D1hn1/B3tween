#!/bin/bash

if [ $# -ne 3 ]; then
	echo "Usage: $0 <proxy_ip:proxy_port> <proxy_user:proxy_pass> number_of_requests"
	echo " Write null in <proxy_user:proxy_pass> if proxy need no authentication"
	exit
fi

trap exit INT

URLS=(
	"https://google.com"
	"http://httpforever.com"
	"https://chatgpt.com"
	"https://github.com"
	"https://youtube.com"
	"https://amazon.es"
	"https://diariosur.es"
	"https://mozilla.org"
	"https://medium.com"
	"https://somebooks.es"
)

pickRandom() {
	URL_NUM=${#URLS[@]}
	RANDOM_NUM=$(($RANDOM%$URL_NUM))
	echo ${URLS[$RANDOM_NUM]}
}

AUTHENTICATION=$true

PROXY=$1
PROXY_CREDENTIALS=$2
NUM_REQUESTS=$3

proxy_user=$(echo $PROXY_CREDENTIALS | cut -d ":" -f 1)
proxy_pass=$(echo $PROXY_CREDENTIALS | cut -d ":" -f 2)
if [[ $proxy_user == "null" ]]; then
	AUTHENTICATION=$false
fi

for (( x=0; x<$NUM_REQUESTS; x++ )); do
	URL=$(pickRandom)
	if [ $AUTHENTICATION = $true ]; then
		timeout 2 curl -s -x $PROXY --proxy-user $PROXY_CREDENTIALS $URL &>/dev/null
	else
		timeout 2 curl -s -x $PROXY $URL &>/dev/null
	fi
	echo "Requested " $URL
done
