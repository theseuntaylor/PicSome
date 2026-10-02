#!/usr/bin/env bash
# Runs the end-to-end suite on the connected device/emulator and collects a dated artifact:
#   e2e-artifacts/<timestamp>/report/       AGP HTML test report
#   e2e-artifacts/<timestamp>/screenshots/  per-test screenshots and the downloaded photo
# Usage: scripts/e2e.sh [TestMethodName]
set -uo pipefail
cd "$(dirname "$0")/.."

target="com.theseuntaylor.picsomeapp.e2e.PicSomeE2ETest${1:+#$1}"
out="e2e-artifacts/$(date +%Y%m%d-%H%M%S)"

./gradlew connectedDebugAndroidTest \
    -Pandroid.testInstrumentationRunnerArguments.class="$target" --console=plain
status=$?

mkdir -p "$out"
cp -R app/build/reports/androidTests/connected/debug "$out/report" 2>/dev/null
cp -R app/build/outputs/connected_android_test_additional_output/debugAndroidTest/connected "$out/screenshots" 2>/dev/null
echo "E2E artifact: $out (exit $status)"
exit $status
