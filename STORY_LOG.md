# Story / Feature Test Coverage Log — Student_Portal_Automation

> Tracks Selenium/Cucumber test coverage added for each feature, in order. Updated
> automatically by the `test-writer` subagent after each completed ticket (see CLAUDE.md).
> Format: newest entries at the bottom.

---

## Initial Build

**Date:** (fill in)
**What:** Framework scaffolded — Java, Maven, Cucumber, TestNG, Selenium 4 (Chrome),
WebDriverManager, ExtentReports 5.x, SLF4J/Logback. Structure: pages/, stepdefinitions/,
hooks/, runners/, utils/, listeners/, features/. Config externalized via config.properties.
**Initial scenarios:** Login positive, login negative (2 scenarios)
**Status:** ✅ Working — running via 5-min polling CI + on-demand UI trigger

---

## PBB-786 — Forgot Password Popup

**Date:** (fill in)
**Coverage added:** Scenario verifying the "Forgot Password" link is visible, clicking it
shows the popup with correct message, and the OK button closes it.
**Tags:** (fill in if known)
**Status:** ✅ Passing

---

## PBB-800 — Father's Name Field

**Date:** (fill in)
**Coverage added:** 3 scenarios — (1) positive: value entered is correctly displayed on
Details page, (2) negative: empty field blocks submission, (3) negative: invalid
characters rejected.
**Tags:** @positive/@negative @fatherName
**Locators used:** `father-name-input`, `father-name-display`
**Status:** ✅ Passing

---

## PBB-801 — Mother's Maiden Name Field

**Date:** (fill in)
**Coverage added:** 3 scenarios — same pattern as Father's Name (positive display,
empty-field validation, invalid-character validation).
**Tags:** @positive/@negative @motherMaidenName
**Locators used:** `mother-maiden-name-input`, `mother-maiden-name-display`
**Status:** ✅ Passing

---

## PBB-802 — Course/Program Dropdown

**Date:** (fill in)
**Coverage added:** 3 scenarios — (1) positive: selected course displayed correctly,
(2) negative: submission blocked when placeholder left selected, (3) positive: dropdown
contains the exact expected list of 10 options.
**Tags:** @positive/@negative @courseProgram
**Locators used:** `course-program-select`
**Notes:** Replaced old free-text Course/Program Page Object method (sendKeys) with
Selenium `Select` class (selectByVisibleText). Removed obsolete invalid-character checks
since a dropdown can't receive free text.
**Status:** ✅ Passing

---

## Total Scenario Count (as of last entry above)

**Running total:** (fill in — update each time a new entry is added)

---

## Tooling / Infrastructure Milestones (not features, but relevant history)

- **CLAUDE.md** added — enforces commit-directly-to-main policy.
- **test-writer subagent** added (`.claude/agents/test-writer.md`) — handles new test
  coverage end-to-end given a ticket + data-testid values.
- **CI trigger evolution:** started as 5-min polling only → attempted webhook-based
  instant trigger (abandoned, signature bug) → reverted to polling, which remains the
  active mechanism. UI-triggered on-demand runs (via "Run Automation Pack" button) added
  separately using `workflow_dispatch` with a `run_tag` input.
- **Real pass/fail reporting fix:** initial "Run Automation Pack" UI summary was
  incorrectly counting GitHub Actions pipeline steps instead of actual Cucumber scenario
  results — fixed to parse the real Cucumber JSON report.

---

## Template for New Entries

```
## PBB-XXX — <Short Title>

**Date:** YYYY-MM-DD
**Coverage added:** <scenarios added/updated, one line each>
**Tags:** <@positive/@negative + feature tag>
**Locators used:** <data-testid values consumed>
**Status:** <Passing | Failing — investigating | Blocked>
```
