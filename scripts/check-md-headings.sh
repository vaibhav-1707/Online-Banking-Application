#!/usr/bin/env bash
set -euo pipefail

# Simple MD022-like check: ensure one blank line after headings
# Usage: scripts/check-md-headings.sh [file...]

shopt -s nullglob

EXIT_CODE=0

check_file() {
  local file="$1"
  local line_num=0
  local prev_was_heading=0

  while IFS= read -r line || [[ -n "$line" ]]; do
    line_num=$((line_num+1))

    if [[ $prev_was_heading -eq 1 ]]; then
      if [[ -n "$line" ]]; then
        echo "${file}:${line_num}: MD022/blanks-around-headings: Expected 1 blank line below heading" >&2
        EXIT_CODE=1
      fi
      prev_was_heading=0
    fi

    if [[ "$line" =~ ^\#{1,6}[[:space:]] ]]; then
      prev_was_heading=1
    fi
  done < "$file"
}

FILES=("$@")
if [[ ${#FILES[@]} -eq 0 ]]; then
  mapfile -t FILES < <(git ls-files '*.md' 2>/dev/null || find . -type f -name '*.md')
fi

for f in "${FILES[@]}"; do
  [[ -f "$f" ]] || continue
  check_file "$f"
done

exit $EXIT_CODE


