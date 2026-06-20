FROM ${docker.image.base}

COPY artifact/${project.build.finalName}.${project.packaging} .

ENTRYPOINT ["java","-Xmx64m","-Xms32m","-XX:MaxMetaspaceSize=24m","-XX:+UseSerialGC","-XX:+UseCompressedOops","-XX:MaxRAMPercentage=50","--enable-preview","-Duser.timezone=America/Mexico_City","-jar","${project.build.finalName}.${project.packaging}"]

EXPOSE ${application.port}
