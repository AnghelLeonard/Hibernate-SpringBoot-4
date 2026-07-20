#!/usr/bin/env bash
#
# Renders the AsciiDoc chapters to PDF (and HTML, and DocBook), then converts the
# DocBook to Word via Pandoc.
#
# The PDF is produced by asciidoctorj-pdf, which is pure Java, so Maven alone is
# enough for it. There is no pure-Java AsciiDoc backend that writes .docx, so the
# Word step shells out to Pandoc (https://pandoc.org), a single self-contained
# binary, which reads the DocBook that the same Maven build produces.
#
# Usage:
#   ./generate-docs.sh              # clean build -> PDF, HTML, DocBook, Word
#   ./generate-docs.sh --no-clean   # incremental, skips `mvn clean`
#   ./generate-docs.sh --skip-word  # PDF/HTML/DocBook only, never looks for Pandoc
#
set -euo pipefail

cd "$(dirname "$0")"

CLEAN=clean
SKIP_WORD=false

for arg in "$@"; do
  case "$arg" in
    --no-clean)  CLEAN="" ;;
    --skip-word) SKIP_WORD=true ;;
    -h|--help)   sed -n '3,14p' "$0" | cut -c3- ; exit 0 ;;
    *) echo "Unknown option: $arg (try --help)" >&2 ; exit 2 ;;
  esac
done

DOCBOOK_DIR=target/docbook
WORD_DIR=target/word

# --- 1. AsciiDoc -> PDF + HTML + DocBook -------------------------------------

# The build runs under the JDK 26 toolchain declared by the parent aggregator.
if [[ -z "${JAVA_HOME_26:-}" ]]; then
  echo "Note: JAVA_HOME_26 is not set. If the toolchains plugin fails with"
  echo "      'Non-existing JDK home', export it to your JDK 26 installation."
fi

echo "==> Rendering the chapters with Maven"
mvn $CLEAN package

# --- 2. DocBook -> Word ------------------------------------------------------

if [[ "$SKIP_WORD" == true ]]; then
  echo "==> Skipping Word output (--skip-word)"
  exit 0
fi

# Pandoc may be on the PATH or under PANDOC_HOME, which is also what activates
# the `word` Maven profile.
PANDOC=""
if command -v pandoc >/dev/null 2>&1; then
  PANDOC=pandoc
elif [[ -n "${PANDOC_HOME:-}" && -x "${PANDOC_HOME}/pandoc" ]]; then
  PANDOC="${PANDOC_HOME}/pandoc"
elif [[ -n "${PANDOC_HOME:-}" && -x "${PANDOC_HOME}/bin/pandoc" ]]; then
  PANDOC="${PANDOC_HOME}/bin/pandoc"
fi

if [[ -z "$PANDOC" ]]; then
  echo
  echo "WARNING: Pandoc was not found, so no .docx was written."
  echo "         PDF, HTML and DocBook are in target/ and are complete."
  echo "         Install Pandoc from https://pandoc.org/installing.html"
  echo "         (or 'apt install pandoc' / 'brew install pandoc'), or set"
  echo "         PANDOC_HOME to the directory holding the binary, then re-run."
  exit 0
fi

mkdir -p "$WORD_DIR"

# Every chapter, rather than a hard-coded list, so a third chapter needs no edit.
for docbook in "$DOCBOOK_DIR"/*.xml; do
  chapter="$(basename "$docbook" .xml)"
  echo "==> $chapter.docx"
  "$PANDOC" \
    --from=docbook \
    --to=docx \
    --highlight-style=tango \
    --toc \
    --output="$WORD_DIR/$chapter.docx" \
    "$docbook"
done

echo
echo "Done:"
echo "  PDF     target/pdf"
echo "  HTML    target/html"
echo "  DocBook target/docbook"
echo "  Word    target/word"
