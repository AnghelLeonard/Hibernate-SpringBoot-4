# Book chapters

The two chapters written around the example projects in this repository, in AsciiDoc,
rendered to **PDF**, **HTML**, **DocBook** and (with Pandoc) **Word**.

```bash
cd HibernateSpringBootHypersistence/HibernateSpringBootHypersistenceChapters

./generate-docs.sh      # Linux/macOS/Git Bash
generate-docs.bat       # Windows
```

Either script runs the Maven build (PDF, HTML, DocBook) and then converts the
DocBook to Word with Pandoc, skipping the Word step with a clear message when
Pandoc is not installed. Both accept `--no-clean` (incremental) and
`--skip-word`, and both convert *every* chapter found in `target/docbook`, so
adding a third chapter needs no edit to either script.

Maven directly, if you prefer:

```bash
mvn package          # -> target/pdf, target/html, target/docbook
mvn -P word package  # additionally -> target/word/*.docx (needs Pandoc)
```

| Chapter | Source | Covers |
| --- | --- | --- |
| Filling Hibernate's Gaps with Hypersistence Utils | `src/docs/asciidoc/chapter-hypersistence-utils.adoc` | `@Tsid`, JSON types, array types, PostgreSQL-specific types, `YearMonth`, `BaseJpaRepository`, `@Retry`, query debugging, `BatchSequenceGenerator` |
| Enforcing Best Practices with Hypersistence Optimizer | `src/docs/asciidoc/chapter-hypersistence-optimizer.adoc` | the startup scan, fixing every reported event, runtime events, event handlers/filters and the CI gate |

## Why AsciiDoc rather than Markdown

Every code listing in these chapters is **included from a real source file at build
time**, not copied into the prose:

```asciidoc
[source,java]
----
include::{sourcedir}/HibernateSpringBootBatchSequenceGenerator/src/test/java/com/bookstore/forum/BatchSequenceGeneratorTest.java[tag=batch-import]
----
```

The `tag=batch-import` refers to a pair of comments in the Java file itself:

```java
// tag::batch-import[]
@Test
public void batchImportCostsOneSequenceCallForTheWholeFetchSize() {
    ...
}
// end::batch-import[]
```

Asciidoctor pulls that region in and strips the `tag::` markers from the output. The
consequence is the point of the whole setup: **a listing in the book cannot drift from
the code that is actually compiled and tested.** If a refactoring renames a method, the
chapter shows the new name on the next build; if it deletes a tagged region, the build
says so.

Markdown has no include mechanism, so every snippet would be a copy waiting to go stale.
That is the reason for the format, and the reason the chapters are worth rebuilding
after any change to the example projects.

`{sourcedir}` resolves to the `examples.dir` property in `pom.xml`, which points at
the parent aggregator directory (`..`), i.e. the sibling example modules.

## Output formats

| Format | How | Notes |
| --- | --- | --- |
| PDF | `asciidoctorj-pdf` | Pure Java, always runs. Rouge syntax highlighting. |
| HTML | `asciidoctorj` | Pure Java, always runs. Useful for a quick read while writing. |
| DocBook | `asciidoctorj` | Pure Java, always runs. The intermediate format for Word, and a format publishers accept directly. |
| Word (`.docx`) | Pandoc, from the DocBook | Needs [Pandoc](https://pandoc.org) installed. |

There is no pure-Java AsciiDoc backend that writes `.docx`, so Word output shells out to
Pandoc, which reads the DocBook produced above. Pandoc is a single self-contained binary.

The `word` profile activates by itself once `PANDOC_HOME` is set, and can always be
forced with `mvn -P word package`. When Pandoc is absent the build still produces PDF,
HTML and DocBook — a missing Pandoc never breaks anything, the same rule the
Hypersistence Optimizer modules follow.
