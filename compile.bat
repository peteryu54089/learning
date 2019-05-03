@echo off
echo Start Time %DATE% %TIME%
echo.

SETLOCAL ENABLEDELAYEDEXPANSION
pushd %~dp0
call :GetUnixTime UNIX_TIME
IF NOT DEFINED CATALINA_HOME (
    set CATALINA_HOME="D:\xampp\tomcat"
)

echo CATALINA_HOME %CATALINA_HOME%
echo JDK_HOME %JDK_HOME%
echo.
set SRC_PATH="ssh\src"
set OUTPUT_PATH="ssh\out\batch\%UNIX_TIME%"
set WEB_DIR=%OUTPUT_PATH%\WebContent
set CLASS_OUT_DIR=%WEB_DIR%\WEB-INF\classes

mkdir %CLASS_OUT_DIR%
xcopy /e /y /h /q %SRC_PATH%\..\WebContent %WEB_DIR%


dir /s /b %SRC_PATH%\*.java > %OUTPUT_PATH%\files.txt
dir /s /b %SRC_PATH%\..\..\legacy_db2_driver_wrapper\src\*.java >> %OUTPUT_PATH%\files.txt

set RUN_CMD=^
javac -encoding utf8 ^
 -bootclasspath "%JDK_HOME%\jre\lib\rt.jar";"%JDK_HOME%\jre\lib\jce.jar" ^
 -cp %SRC_PATH%\..\WebContent\WEB-INF\lib\*;"%CATALINA_HOME%\lib\*"; ^
 -d %CLASS_OUT_DIR% ^
 @%OUTPUT_PATH%\files.txt

echo.
echo RUN %RUN_CMD%
echo.
%RUN_CMD%

explorer %WEB_DIR%

echo %UNIX_TIME% seconds have elapsed since 1970-01-01 00:00:00

popd
goto :final

:GetUnixTime
setlocal enableextensions
for /f %%x in ('wmic path win32_utctime get /format:list ^| findstr "="') do (
    set %%x)
set /a z=(14-100%Month%%%100)/12, y=10000%Year%%%10000-z
set /a ut=y*365+y/4-y/100+y/400+(153*(100%Month%%%100+12*z-3)+2)/5+Day-719469
set /a ut=ut*86400+100%Hour%%%100*3600+100%Minute%%%100*60+100%Second%%%100
endlocal & set "%1=%ut%" & goto :EOF

:final
echo End Time %DATE% %TIME%
IF NOT "%1" == "/nowait" pause
