@echo off
REM Script pour lancer Kyrios en mode RECETTE avec persistance (Windows)
REM Usage: run-recette.bat

echo.
echo Demarrage de Kyrios - Mode RECETTE (H2 Fichier Persistant)
echo ==================================================
echo.
echo Base de donnees: H2 Fichier (data\kyriosdb.mv.db)
echo Persistance: OUI - Les donnees seront conservees entre redemarrages
echo Init Data: OUI au premier demarrage seulement
echo.
echo Console H2: http://localhost:8080/h2-console
echo Application: http://localhost:8080
echo.
echo Pour arreter: Ctrl+C
echo ==================================================
echo.

call mvn clean spring-boot:run "-Dspring-boot.run.arguments=--spring.profiles.active=recette"
pause
