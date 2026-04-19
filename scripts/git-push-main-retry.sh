#!/usr/bin/env bash
# Used by mods/* semantic-release publish: rebase onto latest origin/main before push
# so long Gradle builds don't lose the race against other pushes to main.
set -euo pipefail
cd "$(git rev-parse --show-toplevel)"

max_attempts="${GIT_PUSH_MAX_ATTEMPTS:-5}"
for attempt in $(seq 1 "$max_attempts"); do
  git fetch origin "+refs/heads/main:refs/remotes/origin/main"
  # Gradle / collect-release-jars may leave a dirty tree; autostash around rebase.
  git rebase --autostash origin/main
  if git push origin main --follow-tags; then
    exit 0
  fi
  echo "git push failed (attempt ${attempt}/${max_attempts}), retrying…" >&2
  sleep "$((attempt * 4))"
done
echo "git push failed after ${max_attempts} attempts" >&2
exit 1
