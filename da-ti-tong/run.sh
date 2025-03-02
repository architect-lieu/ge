#!/bin/bash
docker rm -f datitong
docker rmi datitong
docker build -t datitong .
docker run -d --name datitong -p 8080:8080 -p 5005:5005 datitong