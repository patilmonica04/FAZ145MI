<?xml version="1.0" encoding="UTF-8"?>
<!--
  Program  : FAZ145MI
  Name     : Fixed Asset - Dispose
  Module   : FA (Fixed Assets)
  Type     : MI (Management Interface / Infor M3 API)
  Created  : 2026-07-02
  Description:
    This MI program handles all Fixed Asset disposal transactions.
    It exposes CRUD-style API transactions that allow external systems
    to create, retrieve, list, update and delete fixed-asset disposal
    records stored in the FFASLT table (Fixed Asset - Disposal Lines).
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

    <!-- ── AddDisposal ──────────────────────────────────────────── -->
    <Transaction
        name="AddDisposal"
        description="Add a new fixed-asset disposal record"
        type="Add">
      <InputParameters>
        <Parameter field="CONO" description="Company Number"         type="Numeric"  length="3"  mandatory="false"/>
        <Parameter field="ASID" description="Fixed Asset ID"         type="String"   length="10" mandatory="true"/>
        <Parameter field="SBNO" description="Sub-number"             type="Numeric"  length="3"  mandatory="true"/>
        <Parameter field="DISP" description="Disposal Date (YYYYMMDD)" type="Numeric" length="8" mandatory="true"/>
        <Parameter field="DSCD" description="Disposal Code"          type="String"   length="2"  mandatory="true"/>
        <Parameter field="DIPR" description="Disposal Proceeds"      type="Numeric"  length="17" mandatory="false"/>
        <Parameter field="DICU" description="Disposal Currency"      type="String"   length="3"  mandatory="false"/>
        <Parameter field="DIEX" description="Exchange Rate"          type="Numeric"  length="13" mandatory="false"/>
        <Parameter field="ANOT" description="Notes / Remark"         type="String"   length="60" mandatory="false"/>
        <Parameter field="RESP" description="Responsible Employee"   type="String"   length="10" mandatory="false"/>
        <Parameter field="DEPT" description="Department"             type="String"   length="8"  mandatory="false"/>
      </InputParameters>
      <OutputParameters>
        <Parameter field="CONO" description="Company Number"         type="Numeric"  length="3"/>
        <Parameter field="ASID" description="Fixed Asset ID"         type="String"   length="10"/>
        <Parameter field="SBNO" description="Sub-number"             type="Numeric"  length="3"/>
        <Parameter field="DISP" description="Disposal Date"          type="Numeric"  length="8"/>
        <Parameter field="STAT" description="Status"                 type="String"   length="2"/>
      </OutputParameters>
    </Transaction>

    <!-- ── GetDisposal ──────────────────────────────────────────── -->
    <Transaction
        name="GetDisposal"
        description="Get details of a specific fixed-asset disposal"
        type="Get">
      <InputParameters>
        <Parameter field="CONO" description="Company Number"         type="Numeric"  length="3"  mandatory="false"/>
        <Parameter field="ASID" description="Fixed Asset ID"         type="String"   length="10" mandatory="true"/>
        <Parameter field="SBNO" description="Sub-number"             type="Numeric"  length="3"  mandatory="true"/>
      </InputParameters>
      <OutputParameters>
        <Parameter field="CONO" description="Company Number"         type="Numeric"  length="3"/>
        <Parameter field="ASID" description="Fixed Asset ID"         type="String"   length="10"/>
        <Parameter field="SBNO" description="Sub-number"             type="Numeric"  length="3"/>
        <Parameter field="DISP" description="Disposal Date"          type="Numeric"  length="8"/>
        <Parameter field="DSCD" description="Disposal Code"          type="String"   length="2"/>
        <Parameter field="DIPR" description="Disposal Proceeds"      type="Numeric"  length="17"/>
        <Parameter field="DICU" description="Disposal Currency"      type="String"   length="3"/>
        <Parameter field="DIEX" description="Exchange Rate"          type="Numeric"  length="13"/>
        <Parameter field="ANOT" description="Notes / Remark"         type="String"   length="60"/>
        <Parameter field="RESP" description="Responsible Employee"   type="String"   length="10"/>
        <Parameter field="DEPT" description="Department"             type="String"   length="8"/>
        <Parameter field="STAT" description="Status"                 type="String"   length="2"/>
        <Parameter field="RGDT" description="Entry Date"             type="Numeric"  length="8"/>
        <Parameter field="RGTM" description="Entry Time"             type="Numeric"  length="6"/>
        <Parameter field="CHID" description="Changed By"             type="String"   length="10"/>
        <Parameter field="LMDT" description="Change Date"            type="Numeric"  length="8"/>
      </OutputParameters>
    </Transaction>

    <!-- ── LstDisposal ──────────────────────────────────────────── -->
    <Transaction
        name="LstDisposal"
        description="List fixed-asset disposal records"
        type="List">
      <InputParameters>
        <Parameter field="CONO" description="Company Number"         type="Numeric"  length="3"  mandatory="false"/>
        <Parameter field="ASID" description="Fixed Asset ID"         type="String"   length="10" mandatory="false"/>
        <Parameter field="DISP" description="Disposal Date From (YYYYMMDD)" type="Numeric" length="8" mandatory="false"/>
        <Parameter field="DSPF" description="Disposal Date To   (YYYYMMDD)" type="Numeric" length="8" mandatory="false"/>
        <Parameter field="DSCD" description="Disposal Code Filter"   type="String"   length="2"  mandatory="false"/>
        <Parameter field="STAT" description="Status Filter"          type="String"   length="2"  mandatory="false"/>
      </InputParameters>
      <OutputParameters>
        <Parameter field="CONO" description="Company Number"         type="Numeric"  length="3"/>
        <Parameter field="ASID" description="Fixed Asset ID"         type="String"   length="10"/>
        <Parameter field="SBNO" description="Sub-number"             type="Numeric"  length="3"/>
        <Parameter field="DISP" description="Disposal Date"          type="Numeric"  length="8"/>
        <Parameter field="DSCD" description="Disposal Code"          type="String"   length="2"/>
        <Parameter field="DIPR" description="Disposal Proceeds"      type="Numeric"  length="17"/>
        <Parameter field="DICU" description="Disposal Currency"      type="String"   length="3"/>
        <Parameter field="RESP" description="Responsible Employee"   type="String"   length="10"/>
        <Parameter field="DEPT" description="Department"             type="String"   length="8"/>
        <Parameter field="STAT" description="Status"                 type="String"   length="2"/>
      </OutputParameters>
    </Transaction>

    <!-- ── UpdDisposal ──────────────────────────────────────────── -->
    <Transaction
        name="UpdDisposal"
        description="Update an existing fixed-asset disposal record"
        type="Change">
      <InputParameters>
        <Parameter field="CONO" description="Company Number"         type="Numeric"  length="3"  mandatory="false"/>
        <Parameter field="ASID" description="Fixed Asset ID"         type="String"   length="10" mandatory="true"/>
        <Parameter field="SBNO" description="Sub-number"             type="Numeric"  length="3"  mandatory="true"/>
        <Parameter field="DISP" description="Disposal Date (YYYYMMDD)" type="Numeric" length="8" mandatory="false"/>
        <Parameter field="DSCD" description="Disposal Code"          type="String"   length="2"  mandatory="false"/>
        <Parameter field="DIPR" description="Disposal Proceeds"      type="Numeric"  length="17" mandatory="false"/>
        <Parameter field="DICU" description="Disposal Currency"      type="String"   length="3"  mandatory="false"/>
        <Parameter field="DIEX" description="Exchange Rate"          type="Numeric"  length="13" mandatory="false"/>
        <Parameter field="ANOT" description="Notes / Remark"         type="String"   length="60" mandatory="false"/>
        <Parameter field="RESP" description="Responsible Employee"   type="String"   length="10" mandatory="false"/>
        <Parameter field="DEPT" description="Department"             type="String"   length="8"  mandatory="false"/>
      </InputParameters>
      <OutputParameters>
        <Parameter field="CONO" description="Company Number"         type="Numeric"  length="3"/>
        <Parameter field="ASID" description="Fixed Asset ID"         type="String"   length="10"/>
        <Parameter field="SBNO" description="Sub-number"             type="Numeric"  length="3"/>
        <Parameter field="STAT" description="Status"                 type="String"   length="2"/>
        <Parameter field="LMDT" description="Change Date"            type="Numeric"  length="8"/>
      </OutputParameters>
    </Transaction>

    <!-- ── DltDisposal ──────────────────────────────────────────── -->
    <Transaction
        name="DltDisposal"
        description="Delete a fixed-asset disposal record"
        type="Delete">
      <InputParameters>
        <Parameter field="CONO" description="Company Number"         type="Numeric"  length="3"  mandatory="false"/>
        <Parameter field="ASID" description="Fixed Asset ID"         type="String"   length="10" mandatory="true"/>
        <Parameter field="SBNO" description="Sub-number"             type="Numeric"  length="3"  mandatory="true"/>
      </InputParameters>
      <OutputParameters>
        <Parameter field="CONO" description="Company Number"         type="Numeric"  length="3"/>
        <Parameter field="ASID" description="Fixed Asset ID"         type="String"   length="10"/>
        <Parameter field="SBNO" description="Sub-number"             type="Numeric"  length="3"/>
      </OutputParameters>
    </Transaction>

  </Transactions>

</MIProgram>
