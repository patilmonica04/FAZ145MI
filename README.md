# FAZ145MI – Fixed Asset: Dispose

Custom Infor M3 Management Interface (MI) program that exposes the
**FAS145 Fixed Asset Dispose** panel logic as a callable API.

---

## Overview

`FAZ145MI` is a MAK (customer-extension) MI program that replaces the
need for an operator to open the `FAS145` interactive panel and press
**F13 – Dispose**.  External systems (Infor ION, Mashup, RPA bots,
or any HTTP/REST client via the M3 MI framework) can trigger a full
or partial fixed-asset disposal by calling the `Dispose` transaction.

| Attribute       | Value                                     |
|-----------------|-------------------------------------------|
| Program         | FAZ145MI                                  |
| Java class      | `mvx.app.pgm.customer.FAZ145MI`           |
| Source file     | `FAZ145MI.java`                           |
| Module          | FA – Fixed Assets                         |
| Type            | MI (MIBatch – Management Interface)       |
| SP Number       | `MAK_XKEXTPAM01_260702_14:15`             |
| Modification    | USMOD-08 260701 XKEXTPAM01                |
| Standard UI ref | FAS145 (`FAS145.java`)                    |
| MI template ref | FAS010MI (`FAS010MI.java`)                |

---

## Repository Structure

```
FAZ145MI/
├── README.md
├── FAS010MI.java          ← Standard MI template (reference)
├── FAS145.java            ← Standard FA Dispose UI program (reference)
├── FAZ145MI.java          ← Custom MI implementation (deploy this)
├── schema/
│   ├── MIProgram.xsd      ← XSD for the top-level .mak descriptor
│   └── Transaction.xsd    ← XSD for individual transaction files
└── src/
    ├── FAZ145MI.mak        ← Program descriptor (transactions list)
    └── transactions/
        ├── GetAsset.xml    ← Retrieve asset master data before disposal
        └── Dispose.xml     ← Execute the disposal (core transaction)
```

---

## Transactions

### GetAsset – Retrieve fixed-asset master record

Pre-disposal lookup: use this to verify the asset exists and check its
current `FAST` status before calling `Dispose`.

| # | Field | Description | Type | Len | Mandatory |
|---|-------|-------------|------|-----|-----------|
| **Input** | | | | | |
| 1 | CONO | Company Number | Numeric | 3 | No |
| 2 | DIVI | Division | String | 3 | **Yes** |
| 3 | ASID | Fixed Asset ID | String | 10 | **Yes** |
| 4 | SBNO | Sub-number | Numeric | 3 | **Yes** |
| **Output** | | | | | |
| 1 | CONO | Company Number | Numeric | 3 | |
| 2 | DIVI | Division | String | 3 | |
| 3 | ASID | Fixed Asset ID | String | 10 | |
| 4 | SBNO | Sub-number | Numeric | 3 | |
| 5 | FADS | Asset Description | String | 40 | |
| 6 | FAST | Fixed Asset Status | Numeric | 2 | |
| 7 | FAQT | Asset Quantity | Numeric | 9 | |
| 8 | CUCD | Currency Code | String | 3 | |

---

### Dispose – Execute fixed-asset disposal

Core transaction.  Mirrors the **F13 Dispose** action in `FAS145`.

| # | Field | Description | Type | Len | Mandatory |
|---|-------|-------------|------|-----|-----------|
| **Input** | | | | | |
| 1 | CONO | Company Number | Numeric | 3 | No |
| 2 | DIVI | Division | String | 3 | **Yes** |
| 3 | ASID | Fixed Asset ID | String | 10 | **Yes** |
| 4 | SBNO | Sub-number | Numeric | 3 | **Yes** |
| 5 | ACDT | Accounting Date (YYYYMMDD) | Numeric | 8 | No |
| 6 | FAQT | Disposal Quantity | Numeric | 9 | No |
| 7 | VTXT | Voucher Text | String | 40 | No |
| **Output** | | | | | |
| 1 | CONO | Company Number | Numeric | 3 | |
| 2 | ASID | Fixed Asset ID | String | 10 | |
| 3 | STAT | Resulting Asset Status | String | 2 | |

#### Key notes

- **ACDT blank** → system date is used.
- **FAQT blank** → full disposal of all remaining quantity.
- **FAQT < FFASMA.FAQT** → partial disposal: `DIVASS()` splits the
  asset and `UFAH63()` creates a new sub-asset for the retained portion.
- **STAT = 9** in the output confirms a successful disposal.

---

## Disposal Engine Flow

```
Dispose()
  │
  ├─ 1. Concurrency lock   FFCHKF.CHAIN("00")          error FA14506
  ├─ 2. Asset validation   FFASMA.CHAIN("00")           error FA14513 / FA14510
  ├─ 3. Accounting date    COMDAT() + CSYTAB/FFNC       error XDT0001 / XDT0005
  ├─ 4. Voucher check      PLCHKVO.CCHKVON()
  ├─ 5. Partial disposal?  DIVASS() + UFAH63()          (IN63 = true)
  │
  ├─ TFA100()
  │    └─ loop FFASDM depreciation books
  │         └─ write CJBCMD batch command → triggers FAS100 engine
  │
  └─ CRTSC()
       └─ loop FFASDM depreciation books
            ├─ CRTVACC()   resolve 7-dim accounting string (event FA40)
            ├─ FCR040      write voucher lines
            │               - Debit  : acquisition cost (acty 500)
            │               - Credit : accumulated depreciation (acty 500)
            │               - Credit : net book value / scrap (acty 570)
            ├─ FFAHIS      write history record
            ├─ FFASVL      update/create value record
            ├─ FFASDM      set FAST = 9 (Disposed)
            └─ wrtCAATRA() equipment cost accounting (CAATRA/CAMRUL/CAACTL)
```

---

## Error Codes

| Code | Description |
|------|-------------|
| WDI0102 | Division must be entered. |
| WAS3002 | Fixed Asset ID must be entered. |
| FA14506 | Asset is locked by another process (FFCHKF). |
| FA14513 | Fixed Asset / Sub-number does not exist. |
| FA14510 | Fixed Asset status is invalid for disposal (FAST > 4 and ≠ 8). |
| XDT0001 | Accounting date has an invalid format. |
| XDT0005 | Accounting date is outside the open FA40 function period. |
| XFE0002 | FA function period FA40 is not defined for this division. |
| XFE0003 | FA function period FA40 is not defined (multi-company). |
| XNU000  | Disposal quantity is not a valid number. |
| IP29103 | Disposal quantity exceeds the total asset quantity. |

---

## Key Tables / Programs

| Name | Type | Purpose |
|------|------|---------|
| FAS145 | UI program | Standard FA Dispose interactive panel (reference) |
| FAS010MI | MI template | Standard MI pattern this program is modelled on |
| FFASMA | Table | Fixed Asset Master (key: CONO/DIVI/ASID/SBNO) |
| FFASDM | Table | Depreciation method records per asset |
| FFASVL | Table | Asset value lines (acquisition, depreciation, NBV) |
| FFAHIS | Table | Fixed Asset transaction history |
| FFCHKF | Table | Concurrency lock for FA programs |
| FCR040 | Table | Voucher transaction lines (general ledger) |
| CJBCMD | Table | Batch job command queue |
| CSYTAB | Table | System tables (FFNC function periods, DPTP codes…) |
| CSYPAR | Table | System parameters (CRS750, FAS900) |
| CMNDIV | Table | Division master (PTFA period type, LOCD currency) |
| CAATRA | Table | Equipment accounting transactions |
| CAMRUL | Table | Accounting rules for equipment |
| CAACTL | Table | Accounting control (equipment) |


---

## Overview

`FAZ145MI` is an M3 API (MI) program that exposes CRUD-style transactions for the
`FFASLT` (Fixed Asset Disposal Line) table.  External systems—such as Infor ION,
Mashup scripts, or third-party integrators—use these transactions to create, query,
update and delete fixed-asset disposal records without requiring direct interactive
access to the M3 panel `FAZ145`.

| Attribute   | Value                        |
|-------------|------------------------------|
| Program     | FAZ145MI                     |
| Module      | FA – Fixed Assets            |
| Type        | MI (Management Interface)    |
| Base table  | FFASLT                       |
| Version     | 1.0                          |

---

## Repository Structure

```
FAZ145MI/
├── README.md                          ← This file
├── schema/
│   ├── MIProgram.xsd                  ← XSD for the top-level .mak file
│   └── Transaction.xsd                ← XSD for individual transaction files
└── src/
    ├── FAZ145MI.mak                   ← Program definition (all transactions)
    └── transactions/
        ├── AddDisposal.xml            ← Add a new disposal record
        ├── GetDisposal.xml            ← Retrieve a single disposal record
        ├── LstDisposal.xml            ← List / search disposal records
        ├── UpdDisposal.xml            ← Update an existing disposal record
        └── DltDisposal.xml            ← Delete a pending disposal record
```

---

## Transactions

### AddDisposal – Add a new fixed-asset disposal record

| # | Field | Description | Type | Len | Mandatory |
|---|-------|-------------|------|-----|-----------|
| **Input** | | | | | |
| 1 | CONO | Company Number | Numeric | 3 | No |
| 2 | ASID | Fixed Asset ID | String | 10 | **Yes** |
| 3 | SBNO | Sub-number | Numeric | 3 | **Yes** |
| 4 | DISP | Disposal Date (YYYYMMDD) | Numeric | 8 | **Yes** |
| 5 | DSCD | Disposal Code | String | 2 | **Yes** |
| 6 | DIPR | Disposal Proceeds | Numeric | 17 | No |
| 7 | DICU | Disposal Currency | String | 3 | No |
| 8 | DIEX | Exchange Rate | Numeric | 13 | No |
| 9 | ANOT | Notes / Remark | String | 60 | No |
| 10 | RESP | Responsible Employee | String | 10 | No |
| 11 | DEPT | Department | String | 8 | No |
| **Output** | | | | | |
| 1 | CONO | Company Number | Numeric | 3 | |
| 2 | ASID | Fixed Asset ID | String | 10 | |
| 3 | SBNO | Sub-number | Numeric | 3 | |
| 4 | DISP | Disposal Date | Numeric | 8 | |
| 5 | STAT | Status | String | 2 | |

---

### GetDisposal – Get details of a specific disposal record

| # | Field | Description | Type | Len | Mandatory |
|---|-------|-------------|------|-----|-----------|
| **Input** | | | | | |
| 1 | CONO | Company Number | Numeric | 3 | No |
| 2 | ASID | Fixed Asset ID | String | 10 | **Yes** |
| 3 | SBNO | Sub-number | Numeric | 3 | **Yes** |
| **Output** | | | | | |
| 1 | CONO | Company Number | Numeric | 3 | |
| 2 | ASID | Fixed Asset ID | String | 10 | |
| 3 | SBNO | Sub-number | Numeric | 3 | |
| 4 | DISP | Disposal Date | Numeric | 8 | |
| 5 | DSCD | Disposal Code | String | 2 | |
| 6 | DIPR | Disposal Proceeds | Numeric | 17 | |
| 7 | DICU | Disposal Currency | String | 3 | |
| 8 | DIEX | Exchange Rate | Numeric | 13 | |
| 9 | ANOT | Notes / Remark | String | 60 | |
| 10 | RESP | Responsible Employee | String | 10 | |
| 11 | DEPT | Department | String | 8 | |
| 12 | STAT | Status | String | 2 | |
| 13 | RGDT | Entry Date | Numeric | 8 | |
| 14 | RGTM | Entry Time | Numeric | 6 | |
| 15 | CHID | Changed By | String | 10 | |
| 16 | LMDT | Change Date | Numeric | 8 | |

---

### LstDisposal – List / search disposal records

| # | Field | Description | Type | Len | Mandatory |
|---|-------|-------------|------|-----|-----------|
| **Input** | | | | | |
| 1 | CONO | Company Number | Numeric | 3 | No |
| 2 | ASID | Fixed Asset ID (filter) | String | 10 | No |
| 3 | DISP | Disposal Date From | Numeric | 8 | No |
| 4 | DSPF | Disposal Date To | Numeric | 8 | No |
| 5 | DSCD | Disposal Code (filter) | String | 2 | No |
| 6 | STAT | Status (filter) | String | 2 | No |
| **Output** | | | | | |
| 1 | CONO | Company Number | Numeric | 3 | |
| 2 | ASID | Fixed Asset ID | String | 10 | |
| 3 | SBNO | Sub-number | Numeric | 3 | |
| 4 | DISP | Disposal Date | Numeric | 8 | |
| 5 | DSCD | Disposal Code | String | 2 | |
| 6 | DIPR | Disposal Proceeds | Numeric | 17 | |
| 7 | DICU | Disposal Currency | String | 3 | |
| 8 | RESP | Responsible Employee | String | 10 | |
| 9 | DEPT | Department | String | 8 | |
| 10 | STAT | Status | String | 2 | |

---

### UpdDisposal – Update an existing disposal record

| # | Field | Description | Type | Len | Mandatory |
|---|-------|-------------|------|-----|-----------|
| **Input** | | | | | |
| 1 | CONO | Company Number | Numeric | 3 | No |
| 2 | ASID | Fixed Asset ID | String | 10 | **Yes** |
| 3 | SBNO | Sub-number | Numeric | 3 | **Yes** |
| 4 | DISP | Disposal Date (YYYYMMDD) | Numeric | 8 | No |
| 5 | DSCD | Disposal Code | String | 2 | No |
| 6 | DIPR | Disposal Proceeds | Numeric | 17 | No |
| 7 | DICU | Disposal Currency | String | 3 | No |
| 8 | DIEX | Exchange Rate | Numeric | 13 | No |
| 9 | ANOT | Notes / Remark | String | 60 | No |
| 10 | RESP | Responsible Employee | String | 10 | No |
| 11 | DEPT | Department | String | 8 | No |
| **Output** | | | | | |
| 1 | CONO | Company Number | Numeric | 3 | |
| 2 | ASID | Fixed Asset ID | String | 10 | |
| 3 | SBNO | Sub-number | Numeric | 3 | |
| 4 | STAT | Status | String | 2 | |
| 5 | LMDT | Change Date | Numeric | 8 | |

---

### DltDisposal – Delete a pending disposal record

| # | Field | Description | Type | Len | Mandatory |
|---|-------|-------------|------|-----|-----------|
| **Input** | | | | | |
| 1 | CONO | Company Number | Numeric | 3 | No |
| 2 | ASID | Fixed Asset ID | String | 10 | **Yes** |
| 3 | SBNO | Sub-number | Numeric | 3 | **Yes** |
| **Output** | | | | | |
| 1 | CONO | Company Number | Numeric | 3 | |
| 2 | ASID | Fixed Asset ID | String | 10 | |
| 3 | SBNO | Sub-number | Numeric | 3 | |

> **Note:** Only records with **Status 20** (Pending) may be deleted.

---

## Field Reference

| Field | Full Name | Valid Values / Notes |
|-------|-----------|----------------------|
| CONO | Company Number | Integer; defaults to user's logged-in company |
| ASID | Fixed Asset ID | 10-char alphanumeric; must exist in FFASLM |
| SBNO | Sub-number | Numeric; must exist under the given ASID |
| DISP | Disposal Date | YYYYMMDD; must be within an open FA period |
| DSCD | Disposal Code | 2-char; validated against CSYTAB type `DSCD` |
| DIPR | Disposal Proceeds | Decimal (≥ 0); amount in DICU currency |
| DICU | Disposal Currency | ISO 4217 3-char code; defaults to base currency |
| DIEX | Exchange Rate | Decimal; rate to base currency; defaults to 1 |
| ANOT | Notes | Free text; max 60 chars |
| RESP | Responsible Employee | Employee ID from CMNPRS |
| DEPT | Department | Department code from MNDEPT |
| STAT | Status | `20` Pending · `60` Disposed/Posted · `90` Reversed |
| RGDT | Entry Date | System-generated; YYYYMMDD |
| RGTM | Entry Time | System-generated; HHMMSS |
| CHID | Changed By | System-generated; last user to update |
| LMDT | Change Date | System-generated; YYYYMMDD |

---

## Error Codes

| Code | Transaction | Message |
|------|-------------|---------|
| WF01001 | AddDisposal | Fixed Asset {ASID}/{SBNO} does not exist. |
| WF01002 | AddDisposal | Fixed Asset {ASID}/{SBNO} is already disposed. |
| WF01003 | AddDisposal | Disposal Code {DSCD} is invalid. |
| WF01004 | AddDisposal | Disposal Date {DISP} is outside an open FA period. |
| WF01005 | AddDisposal | Currency {DICU} is not defined in the system. |
| WF01006 | AddDisposal | Disposal Proceeds must be greater than or equal to zero. |
| WF01007 | AddDisposal | A disposal record already exists for {ASID}/{SBNO}. |
| WF02001 | GetDisposal | No disposal record found for {ASID}/{SBNO} in company {CONO}. |
| WF03001 | LstDisposal | Disposal Date From {DISP} is after Disposal Date To {DSPF}. |
| WF04001 | UpdDisposal | No disposal record found for {ASID}/{SBNO} in company {CONO}. |
| WF04002 | UpdDisposal | Disposal record {ASID}/{SBNO} has status {STAT} and cannot be updated. |
| WF04003 | UpdDisposal | Disposal Code {DSCD} is invalid. |
| WF04004 | UpdDisposal | Disposal Date {DISP} is outside an open FA period. |
| WF04005 | UpdDisposal | Currency {DICU} is not defined in the system. |
| WF04006 | UpdDisposal | Disposal Proceeds must be greater than or equal to zero. |
| WF05001 | DltDisposal | No disposal record found for {ASID}/{SBNO} in company {CONO}. |
| WF05002 | DltDisposal | Disposal record {ASID}/{SBNO} has status {STAT}; only pending records (status 20) may be deleted. |

---

## Related Programs / Tables

| Name | Type | Purpose |
|------|------|---------|
| FAZ145 | Interactive | M3 panel – Fixed Asset Dispose |
| FFASLM | Table | Fixed Asset Sub-number Master |
| FFASLT | Table | Fixed Asset Disposal Lines |
| FGLEDG | Table | General Ledger Entries |
| CSYTAB | Table | System Tables (DSCD, CUCD codes) |
| CMNPRS | Table | Persons / Employees |
| MNDEPT | Table | Departments |
