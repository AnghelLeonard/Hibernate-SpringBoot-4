# Chapter A — Hypersistence Optimizer

The modules under this aggregator use [Hypersistence Optimizer](https://vladmihalcea.com/hypersistence-optimizer/),
which is a **commercial product**. Its artifact is not published to Maven Central, so it cannot
be resolved by a reader who has not bought this product.

**These modules are therefore not built by default.** This directory builds as an empty `pom`
and the rest of the repository stays green whether or not you have the dependency.

## Enabling them

Install the `jakarta`-classified artifact into your local Maven repository since the `jakarta`
qualifier is needed for Jakarta persistence (the default artifact targets `javax.persistence`):

```
io/hypersistence/hypersistence-optimizer/2.13.1/hypersistence-optimizer-2.13.1-jakarta.jar
```

Then one of the following applies:

| Your local repository | What you need to do |
| --- | --- |
| The default `~/.m2/repository` | Nothing. The modules are detected and built automatically. |
| Somewhere else (a `<localRepository>` in `settings.xml`) | Set `MAVEN_LOCAL_REPOSITORY` to it, e.g. `MAVEN_LOCAL_REPOSITORY=d:/.m2/repository`. |

You can also force the build regardless of detection:

```bash
mvn -P hypersistence-optimizer-in-default-repo test
```

## Why detection is done this way

Maven activates profiles *before* the POM's properties exist, and its file-based activation
interpolates only a few expressions — notably **not** `${settings.localRepository}`. What it
does interpolate is `${user.home}` and environment variables, which is exactly the pair of
profiles declared in `pom.xml`. Both may match at once; Maven merges the contributed module
lists as a set, so nothing is built twice.

The version is repeated inside the activation paths because a property cannot be used there.
Keep it in sync with `<hypersistence-optimizer.version>`.

## The modules

| Module | Item | What it shows |
| --- | --- | --- |
| `HibernateSpringBootOptimizerKickoff` | O1 | A deliberately suboptimal model, and the 14 issues the startup scan reports. |
| `HibernateSpringBootOptimizerMappingFixes` | O2 | The same model with every issue fixed — the scan reports nothing. |
| `HibernateSpringBootOptimizerRuntimeEvents` | O3 | A clean mapping that application code still misuses: N+1, unordered pagination, long sessions. |
| `HibernateSpringBootOptimizerEventFilters` | O4 | Event handlers, a narrow filter for one accepted trade-off, and the test that gates the build. |

All of them run against **PostgreSQL** (`bookstoredb`, `postgres`/`root`). MySQL would add
four `TableGeneratorEvent`s, because MySQL has no sequences and `@GeneratedValue` falls back
to a table generator — the Optimizer's advice there is `IDENTITY`, which this book avoids
since it disables JDBC batch inserts.
