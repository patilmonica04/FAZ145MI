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

