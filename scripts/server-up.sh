#!/usr/bin/env bash
set -euo pipefail

APP_DIR="${APP_DIR:-/opt/wisdom-v2.0}"

cd "$APP_DIR"

if ! command -v docker >/dev/null 2>&1; then
  echo "Docker is not installed. Installing docker.io and docker-compose-plugin..."
  apt update
  apt install -y docker.io docker-compose-plugin git
  systemctl enable --now docker
fi

if ! docker compose version >/dev/null 2>&1; then
  echo "Docker Compose plugin is not available."
  exit 1
fi

if [ ! -f .env ]; then
  cp .env.example .env
  echo ".env has been created from .env.example."
  echo "Edit .env first, especially MYSQL_PASSWORD and JWT_SECRET, then run this script again."
  exit 1
fi

docker compose up -d --build
docker compose ps

echo
echo "Backend:"
echo "  http://82.157.124.170:8081/"
echo "  http://82.157.124.170:8081/swagger-ui.html"
echo
echo "Logs:"
echo "  cd $APP_DIR && docker compose logs -f wisdom-v2-backend"
