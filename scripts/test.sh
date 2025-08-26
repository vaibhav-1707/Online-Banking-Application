#!/usr/bin/env bash
set -euo pipefail

script_dir="$(cd "$(dirname "$0")" && pwd)"

echo "Running markdown heading spacing checks..."
"$script_dir/check-md-headings.sh" IMPLEMENTATION_SUMMARY.md

echo "All checks passed."


