@echo off

echo "begin deploy smart-system"

%设置本地文件路径%
set localFile=D:\code\project\smart-system-server\jeecg-boot-module-system\target\jeecg-boot-module-system-2.4.6.jar
​
%设置服务器ip地址%
set host=47.243.135.135
%设置服务器登录用户名%
set user=root
%设置需要上传的位置路径%
set remotePath=/opt/smart-system/smart-system-server
​%执行scp命令上传文件%
scp %localFile% %user%@%host%:%remotePath%

%pause脚本执行完成之后需要手动关闭，如需直接关闭，替换成exit即可%
exit