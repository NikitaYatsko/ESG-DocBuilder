#!/usr/bin/env bash
# Downloads DejaVuSans.ttf into this directory.
# DejaVu fonts are freely available under the DejaVu Fonts License.
# Run this script once before building the project:
#   chmod +x src/main/resources/fonts/download-fonts.sh
#   ./src/main/resources/fonts/download-fonts.sh

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
FONT_FILE="$SCRIPT_DIR/DejaVuSans.ttf"

if [ -f "$FONT_FILE" ]; then
  echo "DejaVuSans.ttf already exists, skipping download."
  exit 0
fi

echo "Downloading DejaVuSans.ttf..."
curl -fsSL \
  "https://github.com/dejavu-fonts/dejavu-fonts/releases/download/version_2_37/dejavu-fonts-ttf-2.37.tar.bz2" \
  -o /tmp/dejavu-fonts.tar.bz2

tar -xjf /tmp/dejavu-fonts.tar.bz2 -C /tmp
cp /tmp/dejavu-fonts-ttf-2.37/ttf/DejaVuSans.ttf "$FONT_FILE"
rm -rf /tmp/dejavu-fonts.tar.bz2 /tmp/dejavu-fonts-ttf-2.37

echo "DejaVuSans.ttf downloaded successfully to $FONT_FILE"
