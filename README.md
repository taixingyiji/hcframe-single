# HCFRAME-SINGLE 服务端

## windows使用Idea开发请设置编码
打开IDEA-Help-Edit Custom VM Options 在其中添加一行 -Dfile.encoding=UTF-8

## 运行环境
* java 1.8
* gradle
* maven 3.6+
* mysql 8 or mariadb 10

## 安装依赖包

``` shell
git clone https://github.com/taixingyiji/hcframe.git -b base-1.2.1-SNAPSHOT
mvn install:install-file -Dfile=lib/Dm7JdbcDriver18.jar -DgroupId=com.dm -DartifactId=Dm7JdbcDriver -Dversion=1.8 -Dpackaging=jar 
mvn install:install-file -Dfile=lib/ojdbc6.jar -DgroupId=com.oracle -DartifactId=ojdbc6 -Dversion=11.2.0.4 -Dpackaging=jar
cd hcframe-parent/ 
mvn install
cd ..
git checkout master
cd hcframe-parent/ 
mvn install
```

## 导入sql

sql文件位于`sql/COMMON.sql`

## 开发

修改`application-dev.yml`中数据库配置。

## 打包发行版
* 打包

`./gradlew build`

* 发布版本

发布包位于**build/distributions**目录下
