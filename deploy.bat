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

REM Copier les bibliothèques tierces (Gson, etc.) AVANT la compilation
echo Copie des bibliotheques...
xcopy /y "%LIB_DIR%\*.jar" "%BUILD_DIR%\WEB-INF\lib\" >nul
if errorlevel 1 (
  echo Erreur lors de la copie des bibliotheques
  exit /b 1
)

REM Compilation avec toutes les libs dans le classpath
echo Compilation des fichiers Java...
dir /b /s %SRC_DIR%\*.java > sources.txt
javac -parameters -cp "%SERVLET_API_JAR%;%LIB_DIR%\*" -d %BUILD_DIR%\WEB-INF\classes @sources.txt
if errorlevel 1 (
  echo Erreur de compilation
  del sources.txt
  exit /b 1
)
del sources.txt

REM Copier ressources web
echo Copie des ressources web...
xcopy "%WEB_DIR%\*" "%BUILD_DIR%\" /s /e /y >nul

REM Créer le WAR
echo Creation du WAR...
cd %BUILD_DIR%
if exist %APP_NAME%.war del %APP_NAME%.war
jar -cvf %APP_NAME%.war * >nul
cd ..

REM Déployer sur Tomcat
echo Deploiement sur Tomcat...
if exist "%TOMCAT_WEBAPPS%\%APP_NAME%" rmdir /s /q "%TOMCAT_WEBAPPS%\%APP_NAME%"
if exist "%TOMCAT_WEBAPPS%\%APP_NAME%.war" del "%TOMCAT_WEBAPPS%\%APP_NAME%.war"
copy /y "%BUILD_DIR%\%APP_NAME%.war" "%TOMCAT_WEBAPPS%\" >nul

echo.
echo ========================================
echo Deploiement termine avec succes !
echo ========================================
echo Application: %APP_NAME%
echo URL: http://localhost:8081/%APP_NAME%/
echo.
echo Verifiez Tomcat si necessaire.
echo ========================================
pause

ENDLOCAL