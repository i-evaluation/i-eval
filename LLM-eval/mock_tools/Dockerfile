# 使用 OpenJDK 17 作为基础镜像
FROM azul/zulu-openjdk:17-latest

# 设置工作目录
WORKDIR /app

# 创建日志目录
RUN mkdir -p /app/logs

# 复制 JAR 文件到容器
COPY target/mocktool-0.0.1-SNAPSHOT.jar /app/mocktool.jar

# 暴露端口
EXPOSE 8089

# 设置时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 启动应用
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=qc", "/app/mocktool.jar"]

