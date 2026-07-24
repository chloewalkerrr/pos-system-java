# Point of Sale (POS) System

A desktop Point-of-Sale application built in Java and Swing. It models a real convenience-store checkout end to end: a manager back office for maintaining store data, and a cashier terminal for logging in, scanning items, taking payment, and closing out a shift.

## Background

This project started as an assignment for a college Object-Oriented Programming course, focused on applying core OOP design principles — domain modeling, data management, and UI layers — to a realistic checkout system. I've since extended it beyond the original coursework into a standalone project, adding a proper multi-user login system and general cleanup.

## Features

- **Two separate login paths** — a Manager Login for the back office, and a Cashier Login for the register — so each type of user only sees what they need
- **Manager back office** (password-gated) for maintaining stores, cashiers, registers, items, tax categories, UPCs, and prices
- **Cashier login** tied to a specific register and starting cash count, with each shift tracked as its own session
- **Item lookup by UPC**, with support for time-bound promotional pricing that automatically overrides regular pricing while active
- **Tax calculation** per item's tax category, with a tax-free sale override
- **Split-tender payments** — cash, check, and credit combined on the same sale — with automatic change calculation
- **End-of-session reconciliation** comparing expected vs. counted cash in the drawer
- **Reporting** — Item, Cashier, and Daily Sales reports, filterable by date

## Why two login flows

The original assignment only called for a single admin-style screen. While extending the project, I thought through how this would actually work if different people were using it — a cashier ringing up sales shouldn't need a manager's password just to clock in, and a manager shouldn't have to re-enter cashier credentials to check a report. That led to splitting the entry point into a welcome screen with two paths:

- **Manager Login** — a single password gate into the back office (Maintenance + Reports). Nothing here is tied to an individual person; it's just a lock on the back office.
- **Cashier Login** — checks against real cashier records (cashier number + password), plus a register number and starting cash count, and opens directly into the register flow. It has no route into the back office at all.

I worked through this usability problem with help from Claude (Anthropic's AI assistant), which helped me reason about the access-control gap and implement the fix.

## Architecture

The codebase is split into three packages to keep business logic, data loading, and UI concerns independent of one another:

```
src/
  PD/   Problem Domain — Store, Register, Session, Cashier, Sale, SaleLineItem,
        Payment hierarchy (Cash/Check/Credit), Item, UPC, Price/PromoPrice,
        TaxCategory/TaxRate
  DM/   Data Management — loads store data from CSV into the PD model
  UI/   Swing screens — welcome screen, manager back office, cashier terminal
docs/   Class diagram (Visual Paradigm) and reference image
```

`Store` is implemented as a singleton so the back office and the cashier terminal operate on the same in-memory data within a single run of the application. Currency values use `BigDecimal` throughout to avoid floating-point rounding errors.

See `docs/POS.png` for the full class diagram.

## Getting Started

**Prerequisites:** a Java JDK (8 or newer) and an IDE such as Eclipse or IntelliJ. No build tool or extra dependencies required — it's plain `.java` source files.

**Download the project:**

1. Click the green **Code** button at the top of this repository.
2. Choose **Download ZIP**, then unzip it on your computer (or use `git clone <repo-url>` if you have Git installed).

**Run it:**

1. Open your IDE and import the unzipped folder as an existing Java project (in Eclipse: **File → Import → Existing Projects into Workspace**, then select the folder).
2. Find `StartScreen.java` in the `UI` package, right-click it, and choose **Run As → Java Application**. This is the entry point.
3. On the welcome screen, choose one:
   - **Manager Login** — opens the back office (Maintenance + Reports). Default password is in `ManagerLoginScreen.java` as the `MANAGER_PASSWORD` constant.
   - **Cashier Login** — opens the register flow. Use one of the sample cashier numbers/passwords included in the bundled CSV data file (visible under Cashier Maintenance from the Manager side, or by opening the CSV directly).
4. Sample store, item, and pricing data loads automatically from the bundled CSV on first run.

> Data is held in memory for the length of a run and reloaded from CSV each time the app starts — edits made through the back office aren't persisted back to disk yet (see below).

## Possible Future Improvements

- Persist edits back to CSV (or move to a real database) instead of in-memory-only state
- Proper per-manager accounts instead of a single shared password
- Automated unit tests around tax, pricing, and payment calculations

## Design Documentation

The original UML design was created in Visual Paradigm. The `.vpp` file in `docs/` can be opened with [Visual Paradigm](https://www.visual-paradigm.com/) to view or edit the model.

## AI Assistance

I used Claude (Anthropic's AI assistant) while building the extra features of this project — mainly for debugging issues, helping design and implement the manager/cashier login split described above, and drafting this README. I reviewed, tested, and understood every change before including it here.
