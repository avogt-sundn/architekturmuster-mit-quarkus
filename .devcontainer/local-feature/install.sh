#!/bin/bash
chmod 777 ./postCreateCommand.sh
su ${_REMOTE_USER} -c 'bash postCreateCommand.sh'