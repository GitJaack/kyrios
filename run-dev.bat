@echo off
REM Script pour lancer Kyrios en mode DEV (H2 Mémoire) - Windows
REM Usage: run-dev.bat

echo.
echo Demarrage de Kyrios - Mode DEV (H2 Memoire)
echo ==================================================
echo.
echo Base de donnees: H2 En Memoire (RAM)
echo Persistance: NON - Les donnees seront perdues a l'arret
echo Init Data: OUI - Donnees reinitialisees a chaque demarrage
echo.
echo Application: http://localhost:8080
echo.
echo Pour arreter: Ctrl+C
echo ==================================================
echo.

call mvn clean spring-boot:run "-Dspring-boot.run.arguments=--spring.profiles.active=dev"
pause
