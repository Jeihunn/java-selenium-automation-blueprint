# Selenium Grid

This folder contains the local Selenium Grid setup used by the project for remote browser execution. The environment is managed through `docker-compose.yml` and provides a hub plus dedicated `chrome`, `firefox`, and `edge` nodes.

## Purpose

- Provide a consistent Grid target for `execution.mode=grid`
- Standardize remote browser execution across local, shared, and CI environments
- Keep cross-browser execution predictable and easy to scale
- Offer a repeatable infrastructure baseline for parallel UI test runs

## Prerequisites

- Docker Engine
- Docker Compose
- An external Docker network named `qa-infra-net`
- A repository-root `.env` file

## Quick Start

1. Create the external Docker network if it does not already exist:

```powershell
docker network create qa-infra-net
```

2. Create `.env` from `.env.example` in the repository root and review the Grid values:

```env
# Required for execution.mode=grid
GRID_HOST=http://localhost:4444

# Required by the current grid/docker-compose.yml setup
GRID_USERNAME=admin
GRID_PASSWORD=admin
```

3. Start Selenium Grid:

```powershell
docker compose --env-file .env -f grid/docker-compose.yml up -d
```

4. Confirm that all services are up:

```powershell
docker compose --env-file .env -f grid/docker-compose.yml ps
```

## Configuration Notes

The framework reads Grid settings from the shared runtime configuration chain, including `.env`.

- `GRID_HOST`
  Absolute Selenium Grid URL, for example `http://localhost:4444`
- `GRID_USERNAME`
  Username used for Grid basic authentication
- `GRID_PASSWORD`
  Password used for Grid basic authentication

> [!IMPORTANT]
> In the current `grid/docker-compose.yml`, `GRID_USERNAME` and `GRID_PASSWORD` are required because the Selenium Hub router is started with authentication enabled.

> [!NOTE]
> Do not embed credentials inside `GRID_HOST`. The framework explicitly rejects URLs that contain user info and expects credentials to be provided through `GRID_USERNAME` and `GRID_PASSWORD`.

> [!NOTE]
> If `GRID_HOST` does not include a path, the framework automatically uses `/wd/hub` when building the remote WebDriver URL.

## Using Grid With This Project

To run tests through Selenium Grid:

1. Set `execution.mode=grid` in [`src/test/resources/config.properties`](../src/test/resources/config.properties), or override it with a JVM system property.
2. Make sure `GRID_HOST`, `GRID_USERNAME`, and `GRID_PASSWORD` are defined in `.env`, environment variables, or system properties.
3. Select the browser with `browser=chrome`, `browser=firefox`, or `browser=edge`.

Example:

```powershell
mvn clean test -Dexecution.mode=grid -Dbrowser=chrome
```

## Operational Commands

Check container status:

```powershell
docker compose --env-file .env -f grid/docker-compose.yml ps
```

Stop the Grid:

```powershell
docker compose --env-file .env -f grid/docker-compose.yml down
```

View service logs:

```powershell
docker compose --env-file .env -f grid/docker-compose.yml logs -f
```

## Troubleshooting

- If the containers do not start, verify that `qa-infra-net` exists before running `docker compose up`.
- If the hub stays unhealthy, confirm that `GRID_USERNAME` and `GRID_PASSWORD` are set and match the expected router credentials.
- If Grid execution fails inside the framework, make sure `GRID_HOST` is an absolute URL such as `http://localhost:4444`.
