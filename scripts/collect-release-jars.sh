#!/usr/bin/env bash
# Collect Fabric + NeoForge remapped JARs for @semantic-release/github (CI + local).
# Copies exactly one jar per loader matching mod_version from gradle.properties — avoids
# stale extra jars from older builds (would otherwise publish wrong/duplicate artifacts).
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"

mod_version=$(grep '^mod_version=' gradle.properties | head -1 | cut -d= -f2- | tr -d '\r')
base=$(grep '^archives_base_name=' gradle.properties | head -1 | cut -d= -f2- | tr -d '\r')
if [[ -z "${mod_version:-}" || -z "${base:-}" ]]; then
  echo "collect-release-jars: could not read mod_version or archives_base_name from gradle.properties" >&2
  exit 1
fi

rm -rf release-jars
mkdir -p release-jars

for platform in fabric neoforge; do
  jar="${platform}/build/libs/${base}-${platform}-${mod_version}.jar"
  if [[ ! -f "$jar" ]]; then
    echo "collect-release-jars: expected jar not found: $jar" >&2
    echo "Listing ${platform}/build/libs:" >&2
    ls -la "${platform}/build/libs" >&2 || true
    exit 1
  fi
  cp "$jar" "release-jars/$(basename "$jar")"
done

ls -la release-jars/
