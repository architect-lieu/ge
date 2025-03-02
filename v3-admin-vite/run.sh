#!/bin/bash
echo "======停止项目====="
docker rm -f nginx
echo "======启动项目====="echo "======启动项目====="
docker run -d --name nginx -p 80:80 \
 -v /service/script/dt-ui/html/:/usr/share/nginx/html \
 -v /service/nginx/conf.d:/etc/nginx/conf.d \
 -v /service/nginx/nginx.conf:/etc/nginx/nginx.conf \
 nginx
