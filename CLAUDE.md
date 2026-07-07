# Claude Code Instructions — Student_Portal_Automation

## Branching Policy (CRITICAL)
- ALWAYS commit and push directly to the `main` branch.
- NEVER create a new branch unless explicitly instructed to in a specific request.
- The GitHub Actions workflow (selenium-tests.yml) and its scheduled polling trigger only operate correctly when changes are on `main`.

## Project Context
- Java Selenium BDD framework: Maven + Cucumber + TestNG + Selenium 4 (Chrome) + WebDriverManager + ExtentReports 5.x
- Tests the live app at the URL in config.properties (app.url)
- Workflow triggers: (1) polls Vercel API every 5 min for new deployments, (2) workflow_dispatch with run_tag input for on-demand runs triggered by the app's "Run Automation Pack" UI button
- Do not modify config.properties values (app.url, project.folder) without explicit instruction — these are intentionally set for the current environment.

## Before Making Changes
- Read this file first.
- Confirm you are committing to `main` before finishing any task.
