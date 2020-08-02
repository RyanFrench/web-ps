FROM centos:7

# Update all packages
RUN yum update -y && yum upgrade -y

# Where our service code lives
ENV SERVICE_HOME /root/web-ps/

# System deps
RUN yum install -y wget git java-1.8.0-openjdk-headless.x86_64 \
	&& yum clean all

# install Leiningen
ENV LEIN_INSTALL=/usr/local/bin/
ENV PATH=$PATH:$LEIN_INSTALL
ENV LEIN_ROOT true
RUN wget -q "https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein" \
         -O $LEIN_INSTALL/lein && \
    chmod +x $LEIN_INSTALL/lein && \
    lein

# Cleanup
RUN yum remove -y git wget && \
    yum clean all

# Where we are going to run commands from
WORKDIR $SERVICE_HOME

# Deploy service code
ADD project.clj $SERVICE_HOME/project.clj
ADD src/ $SERVICE_HOME/src

# Build the jar file
RUN lein uberjar

# Run the service on port 8080
EXPOSE 8080

CMD ["java", "-jar",  "target/web-ps.jar"]
