@echo off
:: 检查是否以管理员身份运行
net session >nul 2>&1
if %errorLevel% NEQ 0 (
    echo 请求以管理员权限重新运行...
    powershell -Command "Start-Process cmd -ArgumentList '/c %~f0' -Verb RunAs"
    exit /b
)

:: 设置 nssm 的路径
set NSSM_PATH=E:\Others\Study\nssm-2.24\nssm-2.24\win64\nssm.exe

:: 重启 Spring Boot 应用程序服务
%NSSM_PATH% restart SpringBootApp

:: 重启 Vue 应用程序服务
%NSSM_PATH% restart VueApp

echo Services restarted successfully.

pause
