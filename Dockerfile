# This is the development image.

FROM tobot/tobot-baseimage

RUN apt-get update

# Mount to host
VOLUME /tobot

# Install dependencies and build frontend
RUN make frontend-build-prod

# Build backend
RUN make backend-build
EXPOSE 5032

CMD [ "make", "backend-run" ]
