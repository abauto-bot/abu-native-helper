#!/usr/bin/env bash
set -euo pipefail

echo "===== ACTIVE APP CODE SAFETY SCAN ====="

# Scan only active Android source/manifest/resources.
# Do NOT scan docs, because docs intentionally mention blocked dangerous terms.
TARGETS="app/src/main"

BAD_REGEX="RECORD_AUDIO|MediaProjectionManager|ImageReader|dispatchGesture|getRootInActiveWindow|android.intent.category.HOME|DEVICE_ADMIN"

if grep -R -E "$BAD_REGEX" -n $TARGETS; then
  echo "❌ Active app code contains dangerous control string. Stop."
  exit 1
else
  echo "✅ active app code safety scan clean"
fi

echo
echo "===== DISABLED SERVICE CONFIRMATION ====="
grep -R "android:enabled=\"false\"" -n app/src/main/AndroidManifest.xml || {
  echo "❌ Disabled service markers missing"
  exit 1
}

echo "✅ disabled services confirmed"
