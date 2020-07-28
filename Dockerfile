FROM tomcat:latest

# Change working directory
WORKDIR /usr/local/tomcat

# Copy War file 
COPY ./target/EasyTV_RBMM_Restful_WS.war /usr/local/tomcat/webapps

#change tomcat default port
RUN sed -i 's/port="8080"/port="8081"/' /usr/local/tomcat/conf/server.xml

#print out tomacat server file content
RUN cat /usr/local/tomcat/conf/server.xml

# Expose tomcat port
EXPOSE 8081

# Run tomcat
CMD ["./bin/catalina.sh", "run"]

#docker build --tag rbmm:v1 .
#docker image save -o C:\Users\salgan\Desktop\Docker_Images\rbmm_v1.tar rbmm:v1
#docker run -it --rm -p 8888:8080 