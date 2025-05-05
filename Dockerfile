# Usa una imagen base con Java 17
FROM eclipse-temurin:17-jdk

# Crea un directorio para la app
WORKDIR /app

# Copia el JAR generado por Maven
COPY target/*.jar app.jar

# Expone el puerto
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]