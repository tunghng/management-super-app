# Industrial Park Management API

- [Industrial Park Management API](#industrial-park-management-api)
- [Introduction](#introduction)
- [Quick Start](#quick-start)
- [Start Application](#start-application)
    - [Run specific application](#run-specific-application)
    - [Build Image Automatically](#build-image-automatically)
- [Docs](#docs)

# Introduction

Industrial Park Management is a software application that allows companies or factories to manage various business
processes such as accounting, human resources, support tickets, system news and inventory managements, among others.
This software designed to streamline and automate these processes, reducing costs and improving efficiency. This system
integrated these processes into a single platform, eliminating the need for multiple software application. It supported
both web and mobile application.

# Quick Start

Clone `.example.env` to `.env` and fill database account

```bash
docker compose -f docker-compose.yml up -d
```

# Start Application

Clone `.example.env` to `.env` and fill database account

```bash
docker compose -f docker-compose.local.yml up -d
```

## Run specific application

Set profile in run configuration with

```bash
spring.profiles.active=local
```

## Build Image Automatically

Make sure you compile module, build docker image and push to docker hub:

```bash
mvn clean compile jib:build
```


# Docs

- [Project Architecture](docs/architecture.md)
- **Postman Collection for Dev**:
    - [API](https://www.postman.com/universal-firefly-833621/workspace/iot-workspace/collection/16454492-02bf60cd-da85-48ac-aeb2-390b4d0250c9?action=share&creator=16437465)
    - [env](https://www.postman.com/universal-firefly-833621/workspace/iot-workspace/environment/16437465-e7785008-ea29-4490-b9c4-fdb93306f972)
