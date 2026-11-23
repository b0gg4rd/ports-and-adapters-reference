FROM ${docker.image.base}

ENV TZ=UTC

COPY artifact/${project.build.finalName} .

ENTRYPOINT ["./${project.build.finalName}"]

EXPOSE ${application.port}
