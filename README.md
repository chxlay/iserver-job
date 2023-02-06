
### iserver-job-demo

#### 介绍
1.  定时任务 Quartz 独立模块贡献给对 Quartz 比较陌生的朋友使用
2.  本项目中，核心模块为： iserver-common-quartz(此模块为封装的 quartz-starter),以及服务启动运行模块 iserver-quartz
3.  其他 common 模块可忽略（iserver-common-core、iserver-common-data）,以使用者项目中的主体架构为准,此处编写这两个模块完全是为了服务能独立运行测试
4.  整合后的项目中只需要引入 quartz 核心依赖,其他依赖以您的项目中使用的依赖为准
5.  此项目为从整体项目中提取出来的，为了快速示意，不可能完整的抽取独立，只能以不报错，最简化独立出来示例，整合项目是需要结合自身项目需求进行处理
#### 软件架构
SpringMVC、SpringBoot、Mybatis-Plus


#### 安装教程

1.  修改 iserver-quartz 下 /resource/application.yml 文件的配置信息（MySQL 连接信息）,然后直接启动
2.  创建项目模块数据库,如： iserver_job, 执行 SQL 脚本（iserver-quartz -->> /resource/sql/ijob_schema.sql 文件）
3.  docker 运行测试的朋友,请修改 docker/.env 文件中连接信息

#### 使用说明

1.  修改 iserver-quartz 下 /resource/application.yml 文件的配置信息（MySQL 连接信息）,然后直接启动
2.  创建项目模块数据库,如： iserver_job, 执行 SQL 脚本（iserver-quartz -->> /resource/sql/ijob_schema.sql 文件）

#### 参与贡献

1.  作者：Alay 独自开发及维护

#### 注意：
此项目仅仅为示例的 Demo, 存在大量 Bug 使用者自行修复，关于 通用的 Quartz 封装修复的在 https://gitee.com/chxlay/iserver-common ，关于业务模块 Job中存在的bug，比如 Controller 中的 Bug 在其他项目中有修复，目前未迁移到此项目，如有需要的可以留言，或有时间，我会将使用过程中发现并修复后的代码同步到该项目
