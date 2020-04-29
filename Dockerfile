FROM tomcat:latest

# Change working directory
WORKDIR /usr/local/tomcat

# Copy War file 
#RUN rm /usr/local/tomcat/conf/tomcat-users.xml

# Copy War file 
COPY ./target/EasyTV_RBMM_Restful_WS.war /usr/local/tomcat/webapps

# Copy War file 
#COPY ./tomcat-users.xml /usr/local/tomcat/conf

RUN cat /usr/local/tomcat/conf/tomcat-users.xml

# Expose tomcat port
EXPOSE 8080

# Run tomcat
CMD ["./bin/catalina.sh", "run"]

#docker build --tag rbmm:v1 .
#docker image save -o C:\Users\salgan\Desktop\Docker_Images\rbmm_v1.tar rbmm:v1
#docker run -it --rm -p 8888:8080 