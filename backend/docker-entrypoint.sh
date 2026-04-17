#!/bin/sh
set -eu

if [ -n "${SPRING_DATASOURCE_PASSWORD_FILE:-}" ]; then
  SPRING_DATASOURCE_PASSWORD="$(cat "$SPRING_DATASOURCE_PASSWORD_FILE")"
  export SPRING_DATASOURCE_PASSWORD
fi

exec java -jar app.jar