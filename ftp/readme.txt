linux虚拟机上的ftp
账号：shanji
密码：179980
文件存放路径：/home/ftpfile/
端口：21

使用FtpUtil：
首先要创建一个entity/FtpEntity.java的实例，只有一个四个参数的构造方法，ip地址，端口，账号，密码
然后就可以调用FtpUtil里面上传下载方法，无须连接。但要用上述实例作为参数