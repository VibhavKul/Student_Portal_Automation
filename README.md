# Student Portal Automation

Selenium + Cucumber-JVM + TestNG BDD suite for the [Student Portal](https://github.com/VibhavKul/Student_Portal) React app, with a custom ExtentReports 5 listener, SLF4J/Logback logging, and headless Chrome support.

## CI pipeline: how a run gets triggered end-to-end

The app and this test suite live in two separate repos, and Vercel doesn't know this repo exists. The chain that connects a deploy to a test run:

1. You push to `main` in [`Student_Portal`](https://github.com/VibhavKul/Student_Portal).
2. Vercel picks up the push and starts building/deploying automatically (this is just Vercel's normal Git integration - nothing here configures that part).
3. `Student_Portal`'s own workflow, [`.github/workflows/trigger-tests.yml`](https://github.com/VibhavKul/Student_Portal/blob/main/.github/workflows/trigger-tests.yml), polls the Vercel API for that exact commit's deployment until it reports `readyState: READY` (or fails fast if the deploy errors out).
4. Once ready, that workflow fires a `repository_dispatch` event (type `vercel-deployment-succeeded`) at this repo.
5. That triggers [`.github/workflows/selenium-tests.yml`](.github/workflows/selenium-tests.yml) here, which runs `mvn clean test -Dheadless=true` against the live production URL in `config.properties` and publishes the results.

`selenium-tests.yml` also accepts a manual `workflow_dispatch` trigger at any time from the Actions tab, independent of the above chain, if you want an on-demand run.

Two GitHub secrets have to be added by hand in the `Student_Portal` repo's settings for step 3-4 to work - see the comments at the top of `trigger-tests.yml` for exactly how to generate and add `VERCEL_TOKEN` and `GH_PAT_FOR_DISPATCH`. Nothing needs to be added to this repo's secrets - `selenium-tests.yml` doesn't call out to any external service.

## Running locally

Against the same production URL CI uses, with the browser visible so you can watch it:

```bash
mvn clean test -Dheadless=false
```

`headless=false` is already the default in `config.properties`, so plain `mvn clean test` does the same thing. To run headless locally (e.g. to reproduce a CI-only failure):

```bash
mvn clean test -Dheadless=true
```

To run a subset of scenarios:

```bash
mvn clean test -Dcucumber.filter.tags="@positive or @negative"
```

Any property in `config.properties` can be overridden per-run without editing the file, via `-D<key>=<value>` (e.g. `-Dapp.url=http://localhost:5173/` to point at a local dev server instead of production) or an equivalent environment variable (e.g. `APP_URL=...`). See `ConfigReader.resolve()` for the precedence order.

## Where to find results

**Locally:** `test-output/reports/Extent-Report-<timestamp>.html` (plus `test-output/screenshots/` and `test-output/logs/`), and the secondary Cucumber HTML/JSON reports under `target/cucumber-reports/`.

**From a CI run:** GitHub repo -> **Actions** tab -> the workflow run -> **Artifacts** section at the bottom of the run summary page -> download `automation-reports`. It contains the same `test-output/` (Extent report, screenshots, logs) and `target/cucumber-reports/` / `target/surefire-reports/` as a local run. This is uploaded via `actions/upload-artifact` with `if: always()`, so it's there even when a scenario fails.

The workflow also publishes a pass/fail summary as a GitHub check (via `dorny/test-reporter`, parsing the JUnit-format XML Surefire writes to `target/surefire-reports/TEST-*.xml`) - visible directly on the workflow run page without needing to open the Extent report.

If any scenario fails, the `mvn clean test` step exits non-zero, so the whole workflow run shows a red X in the Actions tab and (if applicable) as a commit/PR check.

## Project structure

- `pages/` - Page Object Model (`LoginPage`, `HomePage`, `DetailsPage`), PageFactory + `@FindBy`, explicit waits only
- `stepdefinitions/` - thin Cucumber steps, delegate to page objects
- `hooks/Hooks.java` - driver init/teardown, per-step screenshot capture
- `runners/TestNGCucumberRunner.java` - Cucumber-TestNG integration
- `utils/` - `ConfigReader`, `DriverManager`, `WaitUtils`, `ScreenshotUtil`, `ExtentReportManager`, `ScenarioContext`
- `listeners/` - `ExtentReportListener` (custom Cucumber event listener driving the Extent report), `TestNGListener` (SLF4J lifecycle logging)
- `testdata/StudentTestData.java` - builder-based form data
- `src/test/resources/features/StudentPortal.feature` - the Gherkin scenarios
