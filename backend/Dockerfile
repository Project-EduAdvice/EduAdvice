# syntax=docker/dockerfile:1
# =====================================================================
# Image Docker du backend EduAdvice — build "multi-stage".
#
#   Étape 1 (build)   : JDK + Maven  → compile et produit le .jar
#   Étape 2 (runtime) : JRE seul     → exécute le .jar
#
# L'image finale ne contient ni le code source, ni Maven, ni le JDK :
# elle est plus petite et expose moins de surface d'attaque.
#
# Construire : docker build -t eduadvice-backend ./backend
# =====================================================================

# ---------- Étape 1 : build ----------
FROM eclipse-temurin:21-jdk AS build
WORKDIR /workspace

# 1) D'abord uniquement ce qui décrit les dépendances.
#    Tant que pom.xml ne change pas, Docker réutilise cette couche en cache
#    et ne retélécharge pas les dépendances à chaque build.
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN ./mvnw -B -q dependency:go-offline

# 2) Ensuite le code source (change souvent → couche reconstruite).
#    Les tests ne sont PAS lancés ici : ils tournent dans la CI, qui
#    dispose d'une base PostgreSQL de test.
COPY src/ src/
RUN ./mvnw -B -q package -DskipTests

# 3) On "éclate" le jar (format recommandé par Spring Boot pour CDS) :
#    extracted/app.jar (petit lanceur) + extracted/lib/ (dépendances).
RUN java -Djarmode=tools -jar target/*.jar extract --destination extracted \
 && mv extracted/*.jar extracted/app.jar

# ---------- Étape 2 : runtime ----------
FROM eclipse-temurin:21-jre-alpine

# Ne jamais exécuter l'application en root dans le conteneur.
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
WORKDIR /app

# Réglages JVM pour une petite instance (Render Free : 512 Mo, 0,1 CPU) :
#  - MaxRAMPercentage=75     : le tas Java utilise au plus 75 % de la RAM du conteneur
#  - UseSerialGC             : ramasse-miettes le plus léger, adapté à 1 CPU
#  - TieredStopAtLevel=1     : démarrage plus rapide (moins d'optimisation JIT)
#  - ExitOnOutOfMemoryError  : en cas de manque de mémoire, on s'arrête net
#                              → la plateforme redémarre le conteneur
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75 -XX:+UseSerialGC -XX:TieredStopAtLevel=1 -XX:+ExitOnOutOfMemoryError"

COPY --from=build --chown=spring:spring /workspace/extracted/ ./

# Démarrage ~2x plus rapide grâce à CDS (Class Data Sharing) :
#  - pendant le build, on fait une RÉPÉTITION du démarrage : la JVM enregistre
#    les classes chargées dans app.jsa, puis s'arrête (spring.context.exit).
#    Aucune base n'est disponible pendant le build : on désactive donc tout
#    accès à la base pour cette répétition uniquement ;
#  - au vrai démarrage, la JVM relit app.jsa au lieu de tout recharger.
RUN java -XX:ArchiveClassesAtExit=app.jsa \
      -Dspring.context.exit=onRefresh \
      -DDB_PASSWORD=unused \
      -Dspring.flyway.enabled=false \
      -Dspring.jpa.hibernate.ddl-auto=none \
      -Dspring.jpa.properties.hibernate.boot.allow_jdbc_metadata_access=false \
      -Dspring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect \
      -jar app.jar

EXPOSE 8080

# Vérification de santé utilisée par Docker en local.
HEALTHCHECK --interval=30s --timeout=5s --start-period=120s --retries=3 \
  CMD wget -qO- "http://localhost:${PORT:-8080}/actuator/health" || exit 1

# Forme "exec" (tableau JSON) : java devient le processus principal (PID 1)
# et reçoit directement le signal d'arrêt → arrêt propre (graceful shutdown).
ENTRYPOINT ["java", "-XX:SharedArchiveFile=app.jsa", "-jar", "app.jar"]
