FROM node:11.13.0
RUN npm install -g @vue/cli
RUN apt-get update
RUN apt-get install -y bash git openssh-client openssh-server
RUN apt-get install -y \
    libgtk2.0-0 \
    libnotify-dev \
    libgconf-2-4 \
    libnss3 \
    libxss1 \
    libasound2 \
    xvfb