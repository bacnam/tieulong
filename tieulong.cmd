@echo off
rem Runs tieulong.ps1 without changing the PowerShell execution policy.
powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0tieulong.ps1" %*
