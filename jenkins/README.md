# Jenkins

This folder contains the Jenkins assets used to run the automation framework in a CI-friendly way. The setup is designed for Selenium Grid-based execution, parameterized pipeline runs, report publishing, and email notifications.

## Purpose

- Run the UI automation suite through a Jenkins pipeline
- Execute tests against Selenium Grid in a repeatable way
- Publish JUnit and Allure results for build visibility
- Allow browser, parallelism, retry, and tag filtering to be controlled through job parameters
- Share execution results by email after successful or failed runs

## Prerequisites

- Docker Engine
- Docker Compose
- An external Docker network named `qa-infra-net`
- A reachable Selenium Grid environment
- A Jenkins Maven tool named `maven`
- Jenkins plugins required by the pipeline, including:
  - Git
  - Pipeline
  - JUnit
  - Allure
  - Email Extension
  - ANSI Color

## Quick Start

1. Create the shared Docker network if it does not already exist:

```powershell
docker network create qa-infra-net
```

2. Start Jenkins from the repository root:

```powershell
docker compose -f jenkins/docker-compose.yml up -d --build
```

3. Open Jenkins:

```text
http://localhost:8080
```

4. Complete Jenkins initial setup and install the plugins required by the pipeline.

5. Configure a Maven tool named `maven` in Jenkins Global Tool Configuration.

6. Create a Pipeline job that points to this repository and uses [`jenkins/Jenkinsfile`](./Jenkinsfile).

## Supported Parameters

- `BROWSER`: `chrome`, `firefox`, `edge`
- `BROWSER_HEADLESS`: runs the browser in headless mode when enabled
- `DATAPROVIDER_THREADS`: parallel test thread count, validated between `1` and `10`
- `TEST_TAGS`: Cucumber tag expression
- `RETRY_ENABLED`: enables retry logic for failed tests
- `RETRY_COUNT`: maximum retry attempts
- `EMAIL_RECIPIENTS`: comma-separated recipient list for the execution summary

## Required Credentials

Create these Jenkins credentials before running the pipeline:

- `automation-env-file`
  Type: Secret file
  Value: a `.env` file containing the runtime configuration and secrets required by the framework

> [!NOTE]
> The pipeline copies this file into the workspace as `.env` only for the duration of the build, then removes it after test execution.

Example `.env` content:

```env
GRID_HOST=http://selenium-hub:4444
GRID_USERNAME=admin
GRID_PASSWORD=admin
STANDARD_USER_PASSWORD=secret_sauce
LOCKED_OUT_USER_PASSWORD=secret_sauce
```

> [!NOTE]
> Do not place credentials inside `GRID_HOST`. Keep authentication values in `GRID_USERNAME` and `GRID_PASSWORD`.

## Troubleshooting

- If Jenkins starts but the job cannot reach Selenium Grid, verify that the `.env` file stored in `automation-env-file` uses the Jenkins runtime network path. For the Docker setup in this repository, `GRID_HOST=http://selenium-hub:4444` is the expected value.
- If test data resolution fails with messages such as `Secret not found: STANDARD_USER_PASSWORD`, verify that the key exists in the `automation-env-file` credential and is not blank.
- If the pipeline fails before test execution, confirm that the Maven tool is configured with the exact name `maven`.
- If Allure publishing fails, verify that the Allure plugin is installed and properly configured in Jenkins.
- If email sending fails, verify Jenkins email configuration and the Email Extension plugin setup.
- If the job runs on a non-Linux node, remember that the current pipeline uses `sh`, so it expects a Unix-like shell environment.

## Stop Jenkins

```powershell
docker compose -f jenkins/docker-compose.yml down
```
