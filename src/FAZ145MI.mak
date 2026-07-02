<?xml version="1.0" encoding="UTF-8"?>
<!--
  Program     : FAZ145MI
  Name        : Fixed Asset - Dispose
  Module      : FA (Fixed Assets)
  Type        : MI (Management Interface / Infor M3 API)
  Package     : mvx.app.pgm.customer
  Source      : FAZ145MI.java
  SP Number   : MAK_XKEXTPAM01_260702_14:15
  Modification: USMOD-08 260701 XKEXTPAM01  Custom MI for Fixed Asset Disposal

  Description:
    Custom MI program that replicates the FAS145 (Fixed Asset Dispose) UI
    program logic as a callable API.  Allows automation tools, ION workflows,
    and Mashup scripts to trigger fixed-asset disposals programmatically.

  Core engine methods:
    TFA100() - Submits depreciation transaction commands to CJBCMD
    CRTSC()  - Creates FCR040 voucher lines, FFAHIS history, and
               updates FFASVL / FFASDM status to Disposed (FAST=9)
-->
<MIProgram
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:noNamespaceSchemaLocation="../schema/MIProgram.xsd"
    program="FAZ145MI"
    description="Fixed Asset - Dispose"
    module="FA"
    type="MI"
    version="1.0">

  <!-- ================================================================
       T R A N S A C T I O N   L I S T
       ================================================================ -->
  <Transactions>

    <!-- ── GetAsset ─────────────────────────────────────────────── -->
    <Transaction
        name="GetAsset"
        description="Retrieve fixed-asset master data for a given asset/sub-number"
        type="Get">
      <InputParameters>
        <Parameter field="CONO" description="Company Number"         type="Numeric"  length="3"  mandatory="false"/>
        <Parameter field="DIVI" description="Division"               type="String"   length="3"  mandatory="true"/>
        <Parameter field="ASID" description="Fixed Asset ID"         type="String"   length="10" mandatory="true"/>
        <Parameter field="SBNO" description="Sub-number"             type="Numeric"  length="3"  mandatory="true"/>
      </InputParameters>
      <OutputParameters>
        <Parameter field="CONO" description="Company Number"         type="Numeric"  length="3"/>
        <Parameter field="DIVI" description="Division"               type="String"   length="3"/>
        <Parameter field="ASID" description="Fixed Asset ID"         type="String"   length="10"/>
        <Parameter field="SBNO" description="Sub-number"             type="Numeric"  length="3"/>
        <Parameter field="FADS" description="Asset Description"      type="String"   length="40"/>
        <Parameter field="FAST" description="Fixed Asset Status"     type="Numeric"  length="2"/>
        <Parameter field="FAQT" description="Asset Quantity"         type="Numeric"  length="9"/>
        <Parameter field="CUCD" description="Currency Code"          type="String"   length="3"/>
      </OutputParameters>
    </Transaction>

    <!-- ── Dispose ──────────────────────────────────────────────── -->
    <!--
      Replicates the F13-Dispose action from FAS145 panel.
      Engine flow:
        1. Validate DIVI, ASID, SBNO  (FFCHKF concurrency lock + FFASMA existence/status)
        2. Validate accounting date    (ACDT must be within open FFNC FA40 period)
        3. Validate voucher            (PLCHKVO.CCHKVON)
        4. Optional partial-disposal   (FAQT < total → DIVASS / UFAH63 to split)
        5. TFA100                      (loop FFASDM depreciation books → write CJBCMD)
        6. CRTSC                       (loop FFASDM → CRTVACC → write FCR040/FFAHIS/FFASVL,
                                        set FFASDM.FAST = 9, update FFASMA)
    -->
    <Transaction
        name="Dispose"
        description="Execute fixed-asset disposal – mirrors FAS145 F13 action"
        type="Execute">
      <InputParameters>
        <Parameter field="CONO" description="Company Number"
            type="Numeric"  length="3"  mandatory="false"/>
        <Parameter field="DIVI" description="Division"
            type="String"   length="3"  mandatory="true"/>
        <Parameter field="ASID" description="Fixed Asset ID"
            type="String"   length="10" mandatory="true"/>
        <Parameter field="SBNO" description="Sub-number"
            type="Numeric"  length="3"  mandatory="true"/>
        <Parameter field="ACDT" description="Accounting Date (YYYYMMDD)"
            type="Numeric"  length="8"  mandatory="false"/>
        <Parameter field="FAQT" description="Disposal Quantity (blank = full disposal)"
            type="Numeric"  length="9"  mandatory="false"/>
        <Parameter field="VTXT" description="Voucher Text"
            type="String"   length="40" mandatory="false"/>
      </InputParameters>
      <OutputParameters>
        <Parameter field="CONO" description="Company Number"         type="Numeric"  length="3"/>
        <Parameter field="ASID" description="Fixed Asset ID"         type="String"   length="10"/>
        <Parameter field="STAT" description="Resulting Asset Status" type="String"   length="2"/>
      </OutputParameters>
    </Transaction>

  </Transactions>

</MIProgram>
