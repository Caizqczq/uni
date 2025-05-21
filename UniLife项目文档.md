# UniLife项目文档

## 目录
- [一、项目概述](#一项目概述)
    - [1.1 项目简介](#11-项目简介)
    - [1.2 技术栈](#12-技术栈)
    - [1.3 项目结构](#13-项目结构)

## 一、项目概述

### 1.1 项目简介

UniLife 是一款面向学生的在线论坛，致力于提升校园生活体验。其口号为 "有你生活，优你生活"，核心功能包括论坛交流、学习资源共享、课程管理、AI 学习辅助等。项目初期以网站形式开发，后续可能扩展为移动端应用。

### 1.2 技术栈

#### 前端技术栈
- 框架：Vue 3 + TypeScript
- 构建工具：Vite
- UI组件库：Element Plus
- HTTP客户端：Axios
- 路由：Vue Router

#### 后端技术栈
- 框架：Spring Boot 3
- 数据库：MySQL
- ORM框架：MyBatis
- 缓存：Redis
- 认证：JWT
- 邮件服务：Spring Mail
- API文档：Knife4j

### 1.3 项目结构

#### 前端结构
```
Front/vue-unilife/
├── public/             # 静态资源
├── src/
│   ├── assets/         # 资源文件
│   ├── components/     # 组件
│   ├── utils/          # 工具类
│   ├── views/          # 页面
│   ├── router/         # 路由
│   ├── store/          # 状态管理
│   ├── App.vue         # 根组件
│   └── main.ts         # 入口文件
└── package.json        # 项目配置
```

#### 后端结构
```
unilife-server/
├── src/main/java/com/unilife/
│   ├── common/         # 通用类
│   ├── config/         # 配置类
│   ├── controller/     # 控制器
│   ├── interceptor/    # 拦截器
│   ├── mapper/         # 数据访问层
│   ├── model/          # 数据模型
│   │   ├── dto/        # 数据传输对象
│   │   ├── entity/     # 实体类
│   │   └── vo/         # 视图对象
│   ├── service/        # 服务层
│   │   └── impl/       # 服务实现
│   └── utils/          # 工具类
└── src/main/resources/
    ├── mappers/        # MyBatis映射文件
    └── application.yml # 应用配置
```
