FROM eclipse-temurin:22-jdk-noble

ARG USERNAME=spring
ARG USER_UID=1010
ARG USER_GID=$USER_UID

ENV LANG=en_US.UTF-8

RUN apt-get update && export DEBIAN_FRONTEND=noninteractive \
    && apt-get -y install --no-install-recommends maven apt-utils ca-certificates curl sudo htop iputils-ping  traceroute dnsutils iotop net-tools\
    && apt-get clean && rm -rf /var/lib/apt/lists/*

RUN install -m 0755 -d /etc/apt/keyrings \
    && curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc \
    && chmod a+r /etc/apt/keyrings/docker.asc \
    && echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu $(. /etc/os-release && echo "$VERSION_CODENAME") stable" | tee /etc/apt/sources.list.d/docker.list

RUN apt-get update && export DEBIAN_FRONTEND=noninteractive \
    && apt-get -y install --no-install-recommends docker-ce-cli docker-compose-plugin \
    && apt-get clean && rm -rf /var/lib/apt/lists/*


RUN java --version
RUN mvn --version

RUN groupadd --gid $USER_GID $USERNAME \
   && useradd --uid $USER_UID --gid $USER_GID -m $USERNAME \
   && echo $USERNAME ALL=\(root\) NOPASSWD:ALL > /etc/sudoers.d/$USERNAME \
   && chmod 0440 /etc/sudoers.d/$USERNAME

USER $USERNAME
