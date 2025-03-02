# our final base image
FROM openjdk:17

WORKDIR /da-ti-tong
COPY ./zfont/* /usr/share/fonts/truetype/dejavu/
COPY ./da-ti-tong-1.0.jar ./

ENTRYPOINT ["sh", "-c", "java -Duser.timezone=GMT+08 -Dspring.profiles.active=dev \
-XX:+PrintGCDetails  \
--add-opens java.base/java.lang=ALL-UNNAMED \
--add-opens java.base/java.util=ALL-UNNAMED \
--add-opens java.base/java.nio=ALL-UNNAMED \
--add-opens java.base/sun.nio.ch=ALL-UNNAMED \
--add-opens java.base/java.lang.invoke=ALL-UNNAMED \
-Djava.util.Arrays.useLegacyMergeSort=true \
-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 \
-Xmx700m -Xms700m -Xmn350m -XX:+UseParallelGC -XX:-OmitStackTraceInFastThrow -jar  ./da-ti-tong-1.0.jar"]
