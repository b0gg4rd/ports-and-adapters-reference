FROM ${docker.image.base}

COPY artifact/${project.build.finalName} .

ENTRYPOINT ["./${project.build.finalName}"]

EXPOSE ${application.port}
