1. xxl-job-admin调度中心
   https://github.com/xuxueli/xxl-job.git

克隆 调度中心代码

$ git clone https://github.com/xuxueli/xxl-job.git
1.1. 创建调度中心的表结构
数据库脚本地址：/xxl-job/doc/db/tables_xxl_job.sql

1.2. 修改 application.properties
server.port=18080

spring.datasource.url=jdbc:mysql://127.0.0.1:3306/xxl_job?Unicode=true&characterEncoding=UTF-8&useSSL=false
spring.datasource.username=root
spring.datasource.password=root
1.3. 修改日志配置文件 logback.xml
<property name="log.path" value="logs/xxl-job/xxl-job-admin.log"/>
1.4. 启动调度中心
Run XxlJobAdminApplication

2. 编写执行器业务
 ...