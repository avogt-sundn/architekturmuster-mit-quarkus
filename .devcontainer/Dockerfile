# See here for image contents: https://github.com/microsoft/vscode-dev-containers/blob/main/containers/java/.devcontainer/base.Dockerfile
# [Choice] Java version (use -bullseye variants on local arm64/Apple Silicon): 8, 11, 17, 8-bullseye, 11-bullseye, 17-bullseye, 8-buster, 11-buster, 17-buster
FROM mcr.microsoft.com/devcontainers/java:1-21-bullseye

# Avoid warnings by switching to noninteractive
# ENV DEBIAN_FRONTEND=noninteractive

ENV IGNORE_NOTICE=true


# BEGIN ------ install common utils: ping, dig, ncat, pip, yq
RUN apt-get update && apt-get install -y iputils-ping dnsutils file apache2-utils python3-pip httpie telnet \
    && pip install yq
#   END ------ install common utils: ping, dig, ncat, pip, yq
# BEGIN ------ Playwright Chromium
RUN apt-get install -y libglib2.0-0 \
        libnss3 \
        libnspr4 \
        libatk1.0-0 \
        libatk-bridge2.0-0 \
        libcups2 \
        libdbus-1-3 \
        libdrm2 \
        libxcb1 \
        libxkbcommon0 \
        libatspi2.0-0 \
        libx11-6 \
        libgbm1 \
        libpango-1.0-0 \
        libcairo2 \
        libasound2 \
    && apt-get install -y libxcomposite1 \
        libxdamage1 \
        libxfixes3 \
        libxrandr2

RUN apt-get update && apt-get install -y chromium

#   END ------ Playwright Chromium
#   BEGIN ------ install graalvm
RUN ARCH=$( if [ $(arch) = "aarch64" ]; then echo aarch64; else echo amd64; fi) \
    && echo "Loading GraalVM/Mandrel for $ARCH" && curl -L -o /tmp/mandrel.tgz https://github.com/graalvm/mandrel/releases/download/mandrel-23.1.1.0-Final/mandrel-java21-linux-$ARCH-23.1.1.0-Final.tar.gz \
    && cd /opt && tar xzf /tmp/mandrel.tgz && rm /tmp/mandrel.tgz \
    && ln -s /opt/mandrel-java21-23.1.1.0-Final /opt/mandrel

#   BEGIN ------ install kompose and kubectl and helm
RUN ARCH=$( if [ $(arch) = "aarch64" ]; then echo arm64; else echo amd64; fi) \
    && echo "Loading Kompose for $ARCH" && curl -L -o /tmp/kompose.tgz https://github.com/kubernetes/kompose/releases/download/v1.34.0/kompose-linux-$ARCH.tar.gz \
    && cd /opt && tar xzf /tmp/kompose.tgz && rm /tmp/kompose.tgz \
    && ln -s /opt/kompose-linux-$ARCH /usr/local/bin/kompose
RUN ARCH=$( if [ $(arch) = "aarch64" ]; then echo arm64; else echo amd64; fi) \
    && echo "Loading Kubectl for $ARCH" && curl -sSL -o /usr/local/bin/kubectl https://storage.googleapis.com/kubernetes-release/release/$(curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt)/bin/linux/$ARCH/kubectl \
    && chmod +x /usr/local/bin/kubectl \
    && curl -s https://raw.githubusercontent.com/helm/helm/main/scripts/get-helm-3 | bash -
RUN cd  /opt/ && git clone https://github.com/ahmetb/kubectx \
    && ln -s /opt/kubectx/kubectx /usr/local/bin/kubectx \
    && ln -s /opt/kubectx/kubens /usr/local/bin/kubens

#   END ----- kompose
# BEGIN ollama
RUN apt-get install -y python3 python3-pip git

RUN curl -fsSL https://ollama.com/install.sh | sh
# END ollama

# BEGIN plantuml extension
RUN apt-get update && apt-get install -y graphviz
# END   plantuml extension

LABEL traefik.enable="true" \
    traefik.http.services.organization-service.loadbalancer.server.port="8080" \
    traefik.http.routers.organization-service.rule="Host(`api.localhost.direct`)" \
    traefik.http.routers.organization-service.entrypoints="websecure" \
    traefik.http.routers.organization-service.middlewares="cors"

EXPOSE 8080
