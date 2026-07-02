# Student Portal Automation

Selenium + Cucumber-JVM + TestNG BDD suite for the [Student Portal](https://github.com/VibhavKul/Student_Portal) React app, with a custom ExtentReports 5 listener, SLF4J/Logback logging, and headless Chrome support.

## CI pipeline: instant webhook trigger, with polling as a safety net only

This repo does **not** rely on any workflow in [`Student_Portal`](https://github.com/VibhavKul/Student_Portal) - that repo has no CI/CD files of its own, and nothing here assumes it ever will. There are two ways a test run gets kicked off, one primary and one backup:

**Primary: instant, via webhook.**

1. Vercel finishes deploying `student-portal` to production and fires its own "Deployment Succeeded" webhook.
2. A separate small project, **vercel-webhook-relay**, listens for that webhook and - once it confirms the deployment - fires a `repository_dispatch` event (type `vercel-deployment-succeeded`) at this repo, with a payload containing `deployment_id` and `url`.
3. [`.github/workflows/selenium-tests.yml`](.github/workflows/selenium-tests.yml) wakes up on that event, trusts the payload (the relay already confirmed the deployment is live - no need to ask Vercel again), and runs the suite headless against production immediately. Typically seconds after the deploy finishes, not minutes.

Setting up the webhook itself and its GitHub PAT happens entirely inside the **vercel-webhook-relay** project - there's nothing to configure in this repo for that part.

**Backup: a 30-minute poll, in case a webhook delivery is ever missed.**

If the relay is down, a webhook delivery fails, or anything else breaks the instant path, this repo doesn't just sit there indefinitely blind to new deployments. Every 30 minutes, `selenium-tests.yml` also runs on its own and asks the Vercel API directly for the most recent `READY` **production** deployment, comparing its ID against the last one this repo already tested (tracked in the committed file [`.github/last-deployment.txt`](.github/last-deployment.txt)):
- **Unchanged?** Logs "no new deployment" and exits immediately - no Chrome, no Maven, no wasted CI minutes.
- **New?** (meaning the webhook path missed it) Runs the suite headless, just like the instant path would have, and updates the tracker file.

You can also trigger a run on demand at any time, independent of both paths above: GitHub repo -> **Actions** tab -> **Selenium Tests** workflow -> **Run workflow** button. A manual run always executes the suite unconditionally, with no dedup check and no tracker file update (that check exists purely to avoid redundant automated runs, not to second-guess an explicit manual request).

Every run logs which of the three triggers fired it (`repository_dispatch`, `schedule`, or `workflow_dispatch`) right at the start of the job, for easy debugging in the Actions log.

If any scenario fails, the `mvn clean test` step exits non-zero, so the whole workflow run shows a red X in the Actions tab.

### Why a committed file instead of GitHub Actions cache, for the tracker?

`actions/cache` is designed for reproducible build inputs (e.g. `~/.m2` keyed by a pom.xml hash), not small mutable state you overwrite on every run - cache entries are effectively immutable once saved under a key, and unused entries get evicted after 7 days, which would silently reset "last tested deployment" state. A plain file committed to the repo has neither problem: it's trivial to read/write with shell commands, never expires, and its history is inspectable with `git log -- .github/last-deployment.txt`.

### Secrets you need to add (this repo's Settings -> Secrets and variables -> Actions -> New repository secret)

These are only used by the 30-minute polling safety net - the instant webhook path doesn't call the Vercel API at all, it just trusts the relay's payload.

| Secret | Required? | What it's for |
|---|---|---|
| `VERCEL_TOKEN` | Yes | Authenticates the API call that checks deployment status |
| `VERCEL_PROJECT_ID` | Yes | Tells the Vercel API which project's deployments to look at |
| `VERCEL_TEAM_ID` | Only if the project is under a Vercel Team (not a personal account) | Disambiguates the project when the token has access to multiple teams |

**How to generate `VERCEL_TOKEN`:** vercel.com -> click your avatar (top right) -> **Settings** -> **Tokens** -> **Create Token**. Name it anything (e.g. `github-actions-poll`), pick an expiry (or none), and set the scope to your personal account or the team that owns the `student-portal` project. Copy the token immediately - Vercel only shows it once.

**How to find `VERCEL_PROJECT_ID`:** vercel.com -> open the `student-portal` project -> **Settings** tab -> **General** (should be the default page) -> scroll to the **Project ID** field near the top and copy it (it looks like `prj_xxxxxxxxxxxxxxxxxxxxxxxxxxxx`).

**How to find `VERCEL_TEAM_ID`** (skip this if the project is under your personal account, not a Team): click the account/team switcher in the top-left of the Vercel dashboard -> if it shows a team name rather than your personal username, that project is under a Team -> go to that Team's **Settings** -> **General** -> the **Team ID** field is near the top (looks like `team_xxxxxxxxxxxxxxxxxxxxxxxxxxxx`). If the switcher only ever shows your personal account, there's no Team ID to add - leave that secret unset and the workflow's `teamId` query parameter is simply omitted.

Add each one: repo -> **Settings** -> **Secrets and variables** -> **Actions** -> **New repository secret** -> paste the name and value exactly as above.

There is nothing new to configure in *this* repo for the webhook path itself - the GitHub PAT used to fire `repository_dispatch`, and the Vercel webhook subscription, both live in the vercel-webhook-relay project's own settings, not here.

One more thing worth checking once: the "Record processed deployment ID" step pushes a commit back to `main` using the workflow's own permissions. If this repo ever gets branch protection rules requiring PR review on `main`, that push will start failing - either exempt the `github-actions[bot]` actor from that rule, or switch the workflow to open a PR instead of pushing directly.

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
