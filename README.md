# Spring Boot Docker Compose with Devcontainers and Codespaces

This repository contains the source code for the Medium
article: [Ain’t No Mountain High Enough: Devcontainers, GitHub Codespaces, and IntelliJ](https://medium.com/code-and-coffee/aint-no-mountain-high-enough-devcontainers-github-codespaces-and-intellij-8ad4a42e2b3b).

## Overview

This project demonstrates how to set up a Spring Boot application with github Codespaces, featuring MariaDB, Redis, and
NGINX for frontend integration. It also highlights the use of Testcontainers for robust integration testing.

## Prerequisites

- **IntelliJ Ultimate** & **JetBrains Gateway**: This repository is best opened with IntelliJ Ultimate for optimal configuration and support.

## Run Configuration

There is a run configuration named **"TeamaApplication"** which starts the Docker Compose environment outside of
github Codespaces.

## Components

- **MariaDB**: Database
- **Redis**: Data cache
- **NGINX**: Web server for frontend integration

## Environment Variables

The project contains a `.env.template` that can be copied and modified to create your own `.env` file with the necessary
environment variables.

---

For more details, please refer to
the [Ain’t No Mountain High Enough: Devcontainers, GitHub Codespaces, and IntelliJ](https://medium.com/code-and-coffee/aint-no-mountain-high-enough-devcontainers-github-codespaces-and-intellij-8ad4a42e2b3b).

