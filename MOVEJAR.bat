@echo off
setlocal enabledelayedexpansion

SET HOUR=%time:~0,2%
SET dtStamp9=%date:~-4%%date:~4,2%%date:~7,2%_0%time:~1,1%%time:~3,2%%time:~6,2% 
SET dtStamp24=%date:~-4%%date:~4,2%%date:~7,2%_%time:~0,2%%time:~3,2%%time:~6,2%

if "%HOUR:~0,1%" == " " (SET dtStamp=%dtStamp9%) else (SET dtStamp=%dtStamp24%)
set var=%dtStamp: =%


copy "C:\Users\Chris\Google Drive\DUNGEON_V3A 2018\desktop\build\libs\desktop-1.0.jar" "C:\Users\Chris\Google Drive\DUNGEON\DEMOs 2018"
ren "C:\Users\Chris\Google Drive\DUNGEON\DEMOs 2018\desktop-1.0.jar" DUNGEON_vA!var!.jar

"C:\Program Files (x86)\Google\Drive\googledrivesync.exe"
exit