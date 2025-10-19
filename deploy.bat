@echo off
SETLOCAL

REM Configuration
SET APP_NAME=FirstServlet
SET SRC_DIR=src\main\java
SET WEB_DIR=src\main\webapp
SET BUILD_DIR=build
SET LIB_DIR=lib
SET TOMCAT_WEBAPPS=C:\xampp\tomcat\webapps
SET SERVLET_API_JAR=%LIB_DIR%\servlet-api.jar

REM Nettoyage
if exist %BUILD_DIR% rmdir /s /q %BUILD_DIR%
mkdir %BUILD_DIR%
mkdir %BUILD_DIR%\WEB-INF
mkdir %BUILD_DIR%\WEB-INF\classes
mkdir %BUILD_DIR%\WEB-INF\lib

REM Compilation
echo Compilation des fichiers Java...
dir /b /s %SRC_DIR%\*.java > sources.txt
javac -cp "%SERVLET_API_JAR%" -d %BUILD_DIR%\WEB-INF\classes @sources.txt
if errorlevel 1 (
  echo Erreur de compilation
  del sources.txt
  exit /b 1
)
del sources.txt

REM Copier librairies tierces (si besoin)
REM xcopy /y lib\*.jar %BUILD_DIR%\WEB-INF\lib\

REM Copier ressources web
xcopy "%WEB_DIR%\*" "%BUILD_DIR%\" /s /e /y >nul

REM Créer le WAR
cd %BUILD_DIR%
if exist %APP_NAME%.war del %APP_NAME%.war
jar -cvf %APP_NAME%.war *
cd ..

REM Déployer sur Tomcat
if exist "%TOMCAT_WEBAPPS%\%APP_NAME%" rmdir /s /q "%TOMCAT_WEBAPPS%\%APP_NAME%"
copy /y "%BUILD_DIR%\%APP_NAME%.war" "%TOMCAT_WEBAPPS%\" >nul

echo Déploiement terminé. Vérifiez Tomcat logs si la ressource n'est pas disponible.
ENDLOCAL