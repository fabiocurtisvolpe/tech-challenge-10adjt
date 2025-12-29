# Estágio de Build
FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /app

# 1. Copia o pom.xml pai da raiz
COPY pom.xml .

# 2. Copia os poms dos módulos respeitando a sua estrutura (adjt/modulo)
# Se as pastas forem adjt/api, adjt/data, etc, usamos:
COPY adjt/api/pom.xml adjt/api/
COPY adjt/data/pom.xml adjt/data/
COPY adjt/domain/pom.xml adjt/domain/

# Baixa as dependências para fazer cache
RUN mvn dependency:go-offline -B

# 3. Copia todo o código fonte
COPY . .

# 4. Compila o projeto
RUN mvn clean package -DskipTests

# Estágio de Execução
FROM eclipse-temurin:25-jre
WORKDIR /app

# 5. Busca o JAR dentro da pasta target do módulo API
# O caminho deve ser adjt/api/target/
COPY --from=build /app/adjt/api/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]