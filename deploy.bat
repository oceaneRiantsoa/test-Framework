@echo off

REM Configuration des variables
SET APP_NAME=FirstServlet
SET SRC_DIR=src\main\java
SET WEB_DIR=src\main\webapp
SET BUILD_DIR=build
SET LIB_DIR=lib
SET TOMCAT_WEBAPPS=C:\xampp\tomcat\webapps
SET SERVLET_API_JAR=%LIB_DIR%\servlet-api.jar

REM Nettoyage de l'ancien build
if exist %BUILD_DIR% (
    rmdir /s /q %BUILD_DIR%
) 
mkdir %BUILD_DIR%\WEB-INF\classes
mkdir %BUILD_DIR%\WEB-INF\lib

REM Compilation des fichiers .java et création du JAR
echo Compilation des fichiers Java...
dir /b /s %SRC_DIR%\*.java > sources.txt
javac -cp "%SERVLET_API_JAR%" -d %BUILD_DIR%\WEB-INF\classes @sources.txt
del sources.txt

cd %BUILD_DIR%\WEB-INF\classes
jar cvf ..\lib\frontServlet.jar com\itu\demo\*.class
cd ..\..\..

REM Supprime les .class car ils sont maintenant dans le JAR
rmdir /s /q %BUILD_DIR%\WEB-INF\classes

REM Copie des fichiers de configuration web
xcopy %WEB_DIR%\* %BUILD_DIR%\ /s /e /y

REM Création de l'archive WAR
cd %BUILD_DIR%
jar -cvf %APP_NAME%.war *
cd ..

REM Déploiement sur Tomcat
if exist %TOMCAT_WEBAPPS%\%APP_NAME% (
    rmdir /s /q %TOMCAT_WEBAPPS%\%APP_NAME%
)
xcopy %BUILD_DIR%\%APP_NAME%.war %TOMCAT_WEBAPPS%\ /y

echo Déploiement terminé. Vérifiez dans Tomcat.
