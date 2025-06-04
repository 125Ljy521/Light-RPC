# Light-RPC 项目

一个使用 Java 手写的轻量级 RPC 框架，具备自定义协议、动态代理、序列化、负载均衡、注册中心等核心功能。

---

## 🌟 项目亮点

- ✳️ 基于 Socket 实现的客户端-服务端通信
- ✳️ JDK 动态代理 + 反射调用
- ✳️ 支持 JDK / JSON 序列化方式
- ✳️ 支持随机 / 轮询负载均衡
- ✳️ 简单远程注册中心（支持多服务实例注册发现）
- ✳️ 多端口模拟多个服务实例，展示负载均衡效果

---

## 📦 项目结构说明
```
light-rpc/
├── light-rpc-api         # 公共接口定义
├── light-rpc-client      # 客户端逻辑，生成动态代理对象
├── light-rpc-core        # 编解码器、协议、注册中心、负载均衡等核心功能
├── light-rpc-registry    # 注册中心服务端（支持远程注册）
├── light-rpc-server      # 服务端逻辑 + 实现类
├── pom.xml               # 父级 Maven 配置
└── README.md             # 项目说明文件
```

---
## 💻 技术栈

- Java 17
- Socket 编程
- JDK 动态代理
- 自定义协议（含请求头+内容）
- 自定义序列化（JDK/JSON）
- Maven 构建工具

---

## 🚀 如何运行

1. 启动注册中心（可选）：运行 RegistryServer.java
2. 启动多个服务端实例：运行 ServerApplication8080.java / ServerApplication8081.java
3. 启动客户端：运行 ClientApplication.java

你会看到客户端自动通过注册中心发现服务并执行远程调用。

---

## 📚 学习价值

- 理解 RPC 框架本质
- 掌握动态代理、反射、序列化、负载均衡等关键技术
- 打通分布式通信核心原理

---

## 🔧 后续计划（TODO）

- 支持 Netty 通信替换 Socket
- 支持 Spring Boot Starter 自动装配
- 引入服务治理（如注册中心 Nacos/ZooKeeper）
- 增加超时重试、熔断降级机制
- 集成日志追踪链路

---

## 📄 许可协议

MIT License © 2025 Light-RPC SheldonLi