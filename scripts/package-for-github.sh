#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")/.."

TS="$(date +%F-%H%M%S)"
OUT="exports/abu-native-helper-v10.1-github-$TS.zip"

mkdir -p exports

zip -r "$OUT" \
  settings.gradle.kts \
  build.gradle.kts \
  app \
  docs \
  .github \
  scripts \
  README.md \
  -x "*/build/*" \
  -x "*.apk"

echo "Created: $OUT"
