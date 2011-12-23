@echo off
set TMP_CLASSPATH=%CLASSPATH%

set CLASSPATH=%CLASSPATH%;.\jasper\bin\
rem Add all jars....
for %%i in (".\jasper\lib\*.jar") do call ".\jasper\cpappend.cmd" %%i
for %%i in (".\jasper\lib\*.zip") do call ".\jasper\cpappend.cmd" %%i

set INTERFACE_CLASSPATH=%CLASSPATH%
set CLASSPATH=%TMP_CLASSPATH%

java -cp "%INTERFACE_CLASSPATH%" XmlJasperInterface %*

