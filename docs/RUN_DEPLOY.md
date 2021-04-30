# 运行与部署文档

## 系统环境要求

- JDK 9+
- MySQL 8.0+
- maven 3+
- Elasticsearch 7.6.2

## 创建数据库并导入数据到MySQL

```mysql
CREATE DATABASE `questionnaire_admin`;
use questionnaire_admin;
```

导入数据：表结构和初始数据SQL文件[questionnaire_admin.sql](/db/questionnaire_admin.sql)

## 准备ElasticSearch

> ElasticSearch版本为7.6.2

1. 安装ElasticSearch <br>
    + 本地安装 <br>
      选择合适的包，windows选zip或者msi，linux选tar.gz。<br>
      [ElasticSearch下载](https://repo.huaweicloud.com/elasticsearch/7.6.2/)
    + Docker安装<br>
      ```bash
      docker pull elasticsearch:7.6.2
      ```
2. 安装ElasticSearch分词插件IK <br>
   进入elasticsearch的bin目录，执行如下命令直接安装：
   ```bash
   ./elasticsearch-plugin install https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v6.3.0/elasticsearch-analysis-ik-7.6.2.zip
   ```
   或者先下载到本地再安装：<br>
   [ik下载地址](https://github.com/medcl/elasticsearch-analysis-ik/releases)
   选择v7.6.2。然后执行命令：
   ```bash
   ./elasticsearch-plugin install file:///{your_path}/elasticsearch-analysis-ik-7.6.2.zip
   ```
3. 启动ElasticSearch <br>
   进入bin/目录，执行：
   ```bash
   ./elasticsearch
   ```
   验证是否启动成功：
   ```bash
   curl "http://{your_ip}:9200/_cat"
   ```

## 配置项目

1. 首先导入项目到IDE，推荐IDEA
2. 修改配置 <br>
   根据你的环境和需要在application-<profile>.properties修改数据库连接配置、Elasticsearch连接配置等配置项。

## 启动项目

进入项目根目录执行：

 ```bash
  mvn spring-boot:run
 ```

或者在IDEA中，右键执行QuestionnaireAdminApplication
