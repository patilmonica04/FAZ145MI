/*
***************************************************************
*                                                             *
*                           NOTICE                            *
*                                                             *
*   THIS SOFTWARE IS THE PROPERTY OF AND CONTAINS             *
*   CONFIDENTIAL INFORMATION OF INFOR AND/OR ITS AFFILIATES   *
*   OR SUBSIDIARIES AND SHALL NOT BE DISCLOSED WITHOUT PRIOR  *
*   WRITTEN PERMISSION. LICENSED CUSTOMERS MAY COPY AND       *
*   ADAPT THIS SOFTWARE FOR THEIR OWN USE IN ACCORDANCE WITH  *
*   THE TERMS OF THEIR SOFTWARE LICENSE AGREEMENT.            *
*   ALL OTHER RIGHTS RESERVED.                                *
*                                                             *
*   (c) COPYRIGHT 2016 INFOR.  ALL RIGHTS RESERVED.           *
*   THE WORD AND DESIGN MARKS SET FORTH HEREIN ARE            *
*   TRADEMARKS AND/OR REGISTERED TRADEMARKS OF INFOR          *
*   AND/OR ITS AFFILIATES AND SUBSIDIARIES. ALL RIGHTS        *
*   RESERVED.  ALL OTHER TRADEMARKS LISTED HEREIN ARE         *
*   THE PROPERTY OF THEIR RESPECTIVE OWNERS.                  *
*                                                             *
***************************************************************
*/
package mvx.app.pgm;

import mvx.app.common.*;
import mvx.runtime.*;
import mvx.db.dta.*;
import mvx.app.util.*;
import mvx.app.plist.*;
import mvx.app.ds.*;
import mvx.util.*;
import mvx.db.common.Expression;
import mvx.db.common.FieldSelection;

/*
*Modification area - M3
*Nbr            Date   User id     Description*     JT-951802 160810 DDEMIRIAN   Reversing JT-845751-049
*     JT-954940 160819 PHURST      Reversing JT-845751-049
*     JT-989904 161125 14866       MHS852 Add or MHS850MI AddWhsLine return error code WSTA101 Status � balance ID 3 is invalid
*     JT-998135 170109 PHURST      MHS850MI.AddWhsLine PO Receipts Sublot items
*    JT-1005729 170118 PHURST      MHS850MI/AddPickViaSblot fails with message Reference sublot ID does not exist
*    JT-1034250 170405 jrodriguez  Wrong decimal place is considered in MHS850MI.AddPOInspect
*    JT-1038146 170424 jbejerano   Error 'Reporting date cannot be prior to pick list creation date MW42050'
*    JT-1047327 170510 jbejerano   MHS850MI.AddROPick - Reporting date and time not correct in MHS852
*    JT-1087167 171016 phurst      Enable DO receipt of sublot with MHS850 AddDOReceipt (Qualifier 50)
*    JT-1103781 171107 gsimon      MHS850MI AddDOReceipt can't receive DO lines from different Consignors
*    JT-1112396 171213 12063       MHS850MI.AddDOReceipt Location does not exist or Status of balance id is not under inspection
*    JT-1131358 180208 jbejerano   MHS850MI qualifier 10 (MO receipt) does not allow receipt of MO in basic U/M
*    JT-1138524 180321 gsimon      MHS850MI.AddPickVIaPack CFVP with SSCC - accept wrong warehouse
*    JT-1154333 180403 jinfante    MHS850MI AddMOReceipt execution cause wrong transaction time in MWS070
*    JT-1172541 180522 dclaveria   Lot no with lowercase letters should not be possible to enter
*    JT-1176421 180611 12063       [ENHANCEMENT] Potency input at PO receipt via API
*    JT-1204953 180905 phurst      MHS850MI AddPickViaSblot
*    JT-1209964 180914 jbejerano   MHS850MI/AddDORecViaPack - "Some lines were not received" when a package (SSCC) is distributed to a third warehouse.
*    JT-1208365 180921 gsimon      Potency for MO and PO using MHS850MI
*    JT-1208027 181018 12063       [ENHANCEMENT] MHS850MI implement UTCMode
*    JT-1251686 190115 10716       Zero-receipt in MMS850MI/AddPOReceipt results to a fully received order line
*    JT-1260784 190222 msietere    MHS850MI AddPOReceipt error Trans date is a future date
*    JT-1260979 190315 12063       [ENHANCEMENT] MHS850MI AddPickByPacStk Add filters PLSX  to MHS850MI.AddPickByPackStk 
*Modification area - Business partner
*Nbr            Date   User id     Description
*99999999999999 999999 XXXXXXXXXX  x
*Modification area - Customer
*Nbr            Date   User id     Description
*99999999999999 999999 XXXXXXXXXX  x
*/

/**
*<BR><B><FONT SIZE=+2>Api: Received transactions</FONT></B><BR><BR>
*
* This class ...<BR><BR>
*
*/
public class MHS850MI extends MIBatch
{
   public void movexMain() {
      INIT();
      //   Accept conversation
      MICommon.initiate();
      MICommon.accept();
      while (MICommon.read()) {
         IN60 = false;
         XXIN60 = false;
         IN75 = false;
         XXERRM = false;
         this.PXALPH.clear();
         save_MSID.clear();
         save_MSGD.clear();

         switch (0) {
         default:// cas switch
            if (MICommon.isTransaction("GetUserInfo")) {
               MICommon.setTransaction(retrieveUserInfo.getMessage());
               break;
            }
            if (MICommon.isTransaction("AddCfmPickList")) {
               RCOM31();
               break;
            }
            if (MICommon.isTransaction("AddPickViaPack")) {
               RCOM38();
               break;
            }
            if (MICommon.isTransaction("AddCOPick")) {
               RCOM05();
               break;
            }
            if (MICommon.isTransaction("AddCOReturn")) {
               RCOM06();
               break;
            }
            if (MICommon.isTransaction("AddDO")) {
               RCOM29();
               break;
            }
            if (MICommon.isTransaction("AddDOPick")) {
               RCOM07();
               break;
            }
            if (MICommon.isTransaction("AddDOReceipt")) {
               RCOM08();
               break;
            }
            if (MICommon.isTransaction("AddMOPick")) {
               RCOM24();
               break;
            }
            if (MICommon.isTransaction("AddMOReceipt")) {
               RCOM25();
               break;
            }
            if (MICommon.isTransaction("AddPickViaRepNo")) {
               RCOM32();
               break;
            }
            if (MICommon.isTransaction("AddPutAwayConf")) {
               RCOM28();
               break;
            }
            if (MICommon.isTransaction("AddPOInspect")) {
               RCOM23();
               break;
            }
            if (MICommon.isTransaction("AddPOReceipt")) {
               RCOM09();
               break;
            }
            if (MICommon.isTransaction("AddReplPick")) {
               RCOM30();
               break;
            }
            if (MICommon.isTransaction("AddROPick")) {
               RCOM10();
               break;
            }
            if (MICommon.isTransaction("AddWhsHead")) {
               RCOM11();
               break;
            }
            if (MICommon.isTransaction("AddWhsLine")) {
               RCOM12();
               break;
            }
            if (MICommon.isTransaction("AddWhsPack")) {
               RCOM13();
               break;
            }
            if (MICommon.isTransaction("ChgWhsHead")) {
               RCOM14();
               break;
            }
            if (MICommon.isTransaction("ChgWhsLine")) {
               RCOM15();
               break;
            }
            if (MICommon.isTransaction("ChgWhsPack")) {
               RCOM16();
               break;
            }
            if (MICommon.isTransaction("DeleteWhsTran")) {
               RCOM17();
               break;
            }
            if (MICommon.isTransaction("GetMvxMsg")) {
               getMvxMsg();
               break;
            }
            if (MICommon.isTransaction("GetWhsHead")) {
               RCOM19();
               break;
            }
            if (MICommon.isTransaction("GetWhsLine")) {
               RCOM20();
               break;
            }
            if (MICommon.isTransaction("GetWhsPack")) {
               RCOM21();
               break;
            }
            if (MICommon.isTransaction("PrcWhsTran")) {
               RCOM22();
               break;
            }
            if (MICommon.isTransaction("SndWhsLine")) {
               RCOM26();
               break;
            }
            if (MICommon.isTransaction("SndWhsPack")) {
               RCOM27();
               break;
            }
            if (MICommon.isTransaction("LstMvxMsg")) {
               RCOM33();
               break;
            }
            if (MICommon.isTransaction("AddROReceipt")) {
               RCOM34();
               break;
            }
            if (MICommon.isTransaction("AddDORecViaPack")) {
               RCOM35();
               break;
            }
            //------------------------------------------------------
            if (MICommon.isTransaction("SndPOReceipt")) {
               RCOM36();
               break;
            }
            //------------------------------------------------------
            if (MICommon.isTransaction("AddPOClose")) {
               RCOM37();
               break;
            }
            //------------------------------------------------------
            if (MICommon.isTransaction("AddDOPackRec")) {
               RCOM40();
               break;
            }
            if (MICommon.isTransaction("AddPutAwayPack")) {
               RCOM41();
               break;
            }
            if (MICommon.isTransaction("AddPOPackInsp")) {
               RCOM43();
               break;
            }
            if (MICommon.isTransaction("AddPickByPacStk")) {
               RCOM44();
               break;
            }
            if (MICommon.isTransaction("AddPickSftPacLn")) {
               RCOM46();
               break;
            }
            if (MICommon.isTransaction("LstWhsLine")) {
               RCOM47();
               break;
            }
            if (MICommon.isTransaction("AddTransNotify")) {
               AddTransNotify();
               break;
            }
            if (MICommon.isTransaction("AddWOPick")) {
               AddWOPick();
               break;
            }
            //------------------------------------------------------
            if (MICommon.isTransaction("AddSubLine")) {
               AddSubLine();
               break;
            } 
            if (MICommon.isTransaction("ChgSubLine")) {
               ChgSubLine();
               break;
            }  
            if (MICommon.isTransaction("LstSubLine")) {
               LstSubLine();
               break;
            } 
            if (MICommon.isTransaction("LstItemSubLine")) {
               LstItemSubLine();
               break;
            }            
            if (MICommon.isTransaction("DeleteSubLine")) {
               DeleteSubLine();
               break;
            }
            if (MICommon.isTransaction("AddPOPutaway")) {
               addPOPutaway();
               break;
            }
            if (MICommon.isTransaction("AddPickViaSblot")) {
               addPickViaSublot();
               break;
            }
            MICommon.setTransactionError();
         }
         //-----------------------------------------------------------------
         if (X1OPC.EQ("534") || X1OPC.EQ("535") || X1OPC.EQ("536")) {
            save_MSID.moveLeft(this.MSGID);
            save_MSGD.moveLeftPad(this.MSGDTA);
            GENMBM();
            this.MSGID.moveLeftPad(save_MSID);
            this.MSGDTA.moveLeftPad(save_MSGD);
         }
         //------------------------------------------------------------------
         MICommon.write();
      }
      //   Deallocate
      MICommon.close();
      SETLR();
      return;
   }

   /**
   *    RCOM05 - Execute command - AddCOPick
   */
   public void RCOM05() {
      // ----------------------------------------------------------------
      //   Clear transaction records
      // ----------------------------------------------------------------
      sMHS850MIRAddCOPick inAddCOPick = (sMHS850MIRAddCOPick)MICommon.getInDS(sMHS850MIRAddCOPick.class);
      sMHS850MISAddCOPick outAddCOPick = (sMHS850MISAddCOPick)MICommon.getOutDS(sMHS850MISAddCOPick.class);

      HIHED.clear();
      HIPAC.clear();
      HILIN.clear();
      // ----------------------------------------------------------------
      //   Save process option.
      // ----------------------------------------------------------------
      XXPRFL.move(inAddCOPick.getQCPRFL());
      XXOPC.move("*ADD");
      if (MICommon.toNumericCompany(inAddCOPick.getQCCONO())) {
         XXCONO = MICommon.getInt();
      } else {
         MICommon.setError("CONO");
         return;
      }

      LDAZD.CONO = XXCONO;
      //-----------------------------------
      //    UTC mode
      UTCmode = isUTCmode(inAddCOPick.getQCUTCM());
      if (IN60) {
         MICommon.setError("UTCM", "WUTCM01", inAddCOPick.getQCUTCM());
         return;
      }
      //-----------------------------------
      // Message type
      XXE065.moveLeftPad(inAddCOPick.getQCE065());
      RE065();
      if (IN60) {
         return;
      }
      // Check Warehouse
      XXWHLO.moveLeftPad(inAddCOPick.getQCWHLO());
      RWHL();
      if (IN60) {
         return;
      }
      if (UTCmode) {
         //   Determine time zone for Warehouse
         if (!CRCalendar.retrieveTIZOForWHLO(PXMNGTZN, LDAZD.CONO, XXWHLO)) {
            IN60 = true;
            //   MSGID=S_00148 UTC time conversion failed.
            MICommon.setError("GEDT","S_00148");
            return;
         }
         //   time zone for warehouse
         TIZOforWHLO.move(CRCalendar.getTimeZone());
      }
      //-----------------------------------
      //   Retreive message number if blank
      if (inAddCOPick.getQCMSGN().isBlank()) {
         RVMSNR();
         inAddCOPick.setQCMSGN().moveLeft(PXRTVNBR.PXNBNR, 10);
      } else {
         HIHED.setCONO(LDAZD.CONO);
         HIHED.setMSGN().move(inAddCOPick.getQCMSGN());
         rAPIDS.clear();
         X0OPC.moveLeftPad("*GET");
         rPHEDPIpreCall();
         apCall("MHIHEDPI", rPHEDPI);
         rPHEDPIpostCall();
         //         IN60 = toBoolean(X0IN60.getChar());
      }
      //-----------------------------------
      //   SSCC/PANR/DLIX Validation
      //-----------------------------------
      PTRNSfound = false;
      if (!inAddCOPick.getQCDLIX().isBlank()) {
         inputDLIX.moveLeftPad(inAddCOPick.getQCDLIX());
      } else {
         inputDLIX.moveLeftPad(inAddCOPick.getQCRIDI());
      }
      inputPANR.moveLeftPad(inAddCOPick.getQCPACN());
      inputSSCC.moveLeftPad(inAddCOPick.getQCSSCC());
      numberPackage = 0;
      if (!validInput()) {
         return;
      }
      if (inAddCOPick.getQCDLIX().isBlank() && inAddCOPick.getQCRIDI().isBlank()) {
         PTRNS.setCONO(LDAZD.CONO);
         PTRNS.setDIPA(0);
         PTRNS.setINOU(1);
         if (!inAddCOPick.getQCPACN().isBlank()) {
            PTRNS.setPANR().moveLeft(inAddCOPick.getQCPACN());
            PTRNS.SETLL("85", PTRNS.getKey("85", 4));
            if (PTRNS.READE("85", PTRNS.getKey("85",4))) {
               if (PTRNS.READE("85", PTRNS.getKey("85",4))) {
                  //   MSGID-WDL0202 Delivery number must be entered
                  MICommon.setError( "PACN", "WDL0202");
                  return;
               }
               PTRNSfound = true;
               HILIN.setDLIX(PTRNS.getDLIX());
            }
         }
         if (!inAddCOPick.getQCSSCC().isBlank()) {
            PTRNS.setSSCC().moveLeft(inAddCOPick.getQCSSCC());
            if (!PTRNS.CHAIN("70", PTRNS.getKey("70", 3))) {
               //   MSGID-WSSCC03 SSCC number does not exist
               MICommon.setError( "SSCC", "WSSCC03", inAddCOPick.getQCSSCC());
               return;
            }
            PTRNSfound = true;
            HILIN.setDLIX(PTRNS.getDLIX());
         }
      }
      //   Default package number if blank
      if (inAddCOPick.getQCPACN().isBlank()) {
         inAddCOPick.setQCPACN().moveLeft(inAddCOPick.getQCMSGN());
      }
      HIPAC.setCONO(LDAZD.CONO);
      HIPAC.setMSGN().move(inAddCOPick.getQCMSGN());
      HIPAC.setPACN().move(inAddCOPick.getQCPACN());
      rAPIDS.clear();
      X0OPC.moveLeftPad("*GET");
      rPPACPIpreCall();
      apCall("MHIPACPI", rPPACPI);
      rPPACPIpostCall();
      //      IN60 = toBoolean(X0IN60.getChar());
      HILIN.setCONO(LDAZD.CONO);
      HILIN.setMSGN().move(inAddCOPick.getQCMSGN());
      HILIN.setPACN().move(inAddCOPick.getQCPACN());
      HILIN.setMSLN(0);
      //   Retreive facility for warehouse
      ITWHL.setCONO(LDAZD.CONO);
      ITWHL.setWHLO().moveLeft(inAddCOPick.getQCWHLO());
      if (!ITWHL.CHAIN("00", ITWHL.getKey("00"))) {
         //   MSGID=WWH0103 Warehouse &1 does not exist
         MICommon.setError( "WHLO", "WWH0103", inAddCOPick.getQCWHLO());
         return;
      } else {
         HIHED.setFACI().move(ITWHL.getFACI());
      }
      //   Format date/time
      if (inAddCOPick.getQCGEDT().isBlank() ||
            inAddCOPick.getQCGETM().isBlank()) {
         XXGEDT = movexDate();
         XXGETM = movexTime();
      } else {
         if (MICommon.toNumericDate(inAddCOPick.getQCGEDT())) {
            XXGEDT = MICommon.getNumericDate();
         } else {
            MICommon.setError("GEDT");
            return;
         }

         if (!MICommon.toNumeric(inAddCOPick.getQCGETM())) {
            MICommon.setError("GETM");
            return;
         }
         XXGETM = MICommon.getInt();
         // Date generated
         date = XXGEDT; 
         time = XXGETM / 100;
         if (UTCmode && date > 0) {
            if (!convertFromUTC()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("GEDT","S_00148");
               return;
            }
            XXGEDT = UTCdate;
            XXGETM = UTCtime * 100;
         }
      }
      // ----------------------------------------------------------------
      //   Move data from the transaction input structure (MHS850MI1)
      //   to the execution data formats (MHIHED/MHIPAC/MHILIN)
      // ----------------------------------------------------------------
      HIHED.setCONO(LDAZD.CONO);
      HIPAC.setCONO(LDAZD.CONO);
      HILIN.setCONO(LDAZD.CONO);
      HIHED.setQLFR().move("31  ");
      HIPAC.setQLFR().move("31  ");
      HILIN.setQLFR().move("31  ");
      //----------------------------------------
      //----------------------------------------
      HILIN.setTTYP(31);
      HIHED.setE0IO('I');
      HIHED.setWHLO().move(inAddCOPick.getQCWHLO());
      HIPAC.setWHLO().move(inAddCOPick.getQCWHLO());
      HILIN.setWHLO().move(inAddCOPick.getQCWHLO());
      HIHED.setMSGN().move(inAddCOPick.getQCMSGN());
      HIPAC.setMSGN().move(inAddCOPick.getQCMSGN());
      HILIN.setMSGN().move(inAddCOPick.getQCMSGN());
      HIHED.setPMSN().moveLeft(inAddCOPick.getQCMSGN());
      HIPAC.setPACN().move(inAddCOPick.getQCPACN());
      HILIN.setPACN().move(inAddCOPick.getQCPACN());
      HIHED.setGEDT(XXGEDT);
      HIPAC.setGEDT(XXGEDT);
      HILIN.setGEDT(XXGEDT);
      HIHED.setGETM(XXGETM);
      HIPAC.setGETM(XXGETM);
      HILIN.setGETM(XXGETM);
      HIHED.setE0PA().move(inAddCOPick.getQCE0PA());
      HIHED.setE0PB().move(inAddCOPick.getQCE0PA());
      HIHED.setE035(0);
      HIHED.setE065().move(inAddCOPick.getQCE065());
      HIHED.setCUNO().move(inAddCOPick.getQCCUNO());
      HIPAC.setCUNO().move(inAddCOPick.getQCCUNO());
      HILIN.setCUNO().move(inAddCOPick.getQCCUNO());
      HIHED.setADID().move(inAddCOPick.getQCADID());
      HIPAC.setADID().move(inAddCOPick.getQCADID());
      HILIN.setADID().move(inAddCOPick.getQCADID());
      HILIN.setITNO().move(inAddCOPick.getQCITNO());
      XXITNO.moveLeftPad(HILIN.setITNO());
      RITNO();
      //--------------------------------
      HILIN.setPOPN().move(inAddCOPick.getQCPOPN());
      HILIN.setALWQ().move(inAddCOPick.getQCALWQ());
      X0FLDD = 2;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddCOPick.getQCALWT());
      RNUM();
      HILIN.setALWT((int)this.PXNUM);
      X0FLDD = 1;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddCOPick.getQCOEND().getChar());
      RNUM();
      HILIN.setOEND((int)this.PXNUM);
      HILIN.setWHSL().move(inAddCOPick.getQCWHSL());
      HILIN.setBANO().move(inAddCOPick.getQCBANO());
      if (ITMAS.getINDI() == 0 && ITMAS.getBACD() == 8 && inAddCOPick.getQCBREF().isBlank()) {
         MICommon.setError( "BREF", "WBREF02");
         return;
      } else {
         HILIN.setBREF().move(inAddCOPick.getQCBREF());
      }
      HILIN.setBRE2().move(inAddCOPick.getQCBRE2());
      X0FLDD = 9;
      X0DCCD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddCOPick.getQCGRWE());
      RNUM();
      HIPAC.setGRWE(this.PXNUM);
      X0FLDD = 9;
      X0DCCD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddCOPick.getQCVOM3());
      RNUM();
      HIPAC.setVOM3(this.PXNUM);
      X0FLDD = 11;
      X0DCCD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddCOPick.getQCFRCP());
      RNUM();
      HIPAC.setFRCP(this.PXNUM);
      //-----------------------------
      HILIN.setCAMU().move(inAddCOPick.getQCCAMU());
      XXQTYN = 0d;
      XXQTYA.clear();
      XXQTYA.moveLeft(inAddCOPick.getQCQTYP());
      this.PXDCCD = X1DCCD;
      RQTY();
      HILIN.setDLQT(this.PXNUM);
      HILIN.setPAQT(this.PXNUM);
      XXQTYN = 0d;
      XXQTYA.clear();
      XXQTYA.moveLeft(inAddCOPick.getQCQTYO());
      this.PXDCCD = X1DCCD;
      RQTY();
      HILIN.setALQT(this.PXNUM);
      XXQTYN = 0d;
      XXQTYA.clear();
      XXQTYA.moveLeft(inAddCOPick.getQCCAWE());
      this.PXDCCD = X2DCCD;
      RQTY();
      HILIN.setCAWE(this.PXNUM);
      HILIN.setRIDN().move(inAddCOPick.getQCRIDN());
      X0FLDD = 6;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddCOPick.getQCRIDL());
      RNUM();
      HILIN.setRIDL((int)this.PXNUM);
      X0FLDD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddCOPick.getQCRIDX());
      RNUM();
      HILIN.setRIDX((int)this.PXNUM);
      X0FLDD = 11;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddCOPick.getQCRIDI());
      RNUM();
      HILIN.setRIDI((long)this.PXNUM);
      X0FLDD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddCOPick.getQCPLSX());
      RNUM();
      HILIN.setPLSX((int)this.PXNUM);
      if (!PTRNSfound) {
         X0FLDD = 11;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inAddCOPick.getQCDLIX());
         RNUM();
         HILIN.setDLIX((long)this.PXNUM);
      }
      if (!inAddCOPick.getQCPLRN().isBlank()) {
         X0FLDD = 11;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inAddCOPick.getQCPLRN());
         RNUM();
         HILIN.setPLRN((long)this.PXNUM);
      }
      //   Format reporting date
      if (!inAddCOPick.getQCRPDT().isBlank()) {
         if (!MICommon.toNumericDate(inAddCOPick.getQCRPDT())) {
            MICommon.setError("RPDT");
            return;
         }
         HILIN.setRPDT(MICommon.getNumericDate());
      }
      //--------------------------------------------------------------------
      if (!inAddCOPick.getQCRPTM().isBlank()) {
         X0FLDD = 6;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inAddCOPick.getQCRPTM());
         RNUM();
         HILIN.setRPTM((int)this.PXNUM);
      }
      if (!UTCmode && 
         !inAddCOPick.getQCRPDT().isBlank() &&
          inAddCOPick.getQCRPTM().isBlank()) {
         HILIN.setRPTM(movexTime());
      }
      if (UTCmode) {
         // Reporting date
         if (!inAddCOPick.getQCRPDT().isBlank() &&
             !inAddCOPick.getQCRPTM().isBlank()) {
            date = HILIN.getRPDT(); 
            time = HILIN.getRPTM() / 100;           
            if (!convertFromUTC()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("RPDT","S_00148");
               return;
            }
            HILIN.setRPDT(UTCdate);
            HILIN.setRPTM(UTCtime * 100);
         } else {
            IN60 = true;
            //   MSGID=S_00148 UTC time conversion failed.
            MICommon.setError("RPDT","S_00148");
            return;
         }
      }
      //--------------------------------------------------------------------
      X0FLDD = 7;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddCOPick.getQCAMKO());
      RNUM();
      HIPAC.setAMKO((int)this.PXNUM);
      HIPAC.setPACT().move(inAddCOPick.getQCPACT());
      HILIN.setUSD1().move(inAddCOPick.getQCUSD1());
      HILIN.setUSD2().move(inAddCOPick.getQCUSD2());
      HILIN.setUSD3().move(inAddCOPick.getQCUSD3());
      HILIN.setUSD4().move(inAddCOPick.getQCUSD4());
      HILIN.setUSD5().move(inAddCOPick.getQCUSD5());
      HIHED.setE0IO('I');
      if (!inAddCOPick.getQCPMSN().isBlank()) {
         HIHED.setPMSN().move(inAddCOPick.getQCPMSN());
      } else {
         HIHED.setPMSN().moveLeft(inAddCOPick.getQCMSGN());
      }
      HILIN.setTWSL().move(inAddCOPick.getQCTWSL());
      HIPAC.setPARE().move(inAddCOPick.getQCPARE());
      HIPAC.setSSCC().move(inAddCOPick.getQCSSCC());
      X0FLDD = 1;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddCOPick.getQCISMD().getChar());
      RNUM();
      HILIN.setISMD((int)this.PXNUM);
      HILIN.setLODO().moveLeftPad(inAddCOPick.getQCLODO());
      //-----------------------------------------------
      //   Validate/create transaction header data
      //-----------------------------------------------
      if (IN75) {
         IN60 = true;
         return;
      }
      //-----------------------------------------------
      RHEAD();
      if (IN60 && IN76) {
         return;
      }
      RPACK();
      if (IN60 && IN76) {
         return;
      }
      //   Validate/create transaction line data
      RLINE();
      if (IN60) {
         return;
      }

      outAddCOPick.setYCCONO().move(XXCONO);
      outAddCOPick.setYCMSGN().move(HIHED.getMSGN());
      if (MHS850DS.getJ7STNB() > 0L) {
         X0FLDD = 10;
         XXNUMN = (long)MHS850DS.getJ7STNB();
         XXNUMA.clear();
         RNUMO();
         outAddCOPick.setYCSTNB().moveLeft(this.PXALPH);
         X0FLDD = 7;
         XXNUMN = (int)MHS850DS.getJ7STRN();
         XXNUMA.clear();
         RNUMO();
         outAddCOPick.setYCSTRN().moveLeft(this.PXALPH);
      }

      MICommon.setData( outAddCOPick.get());
   }



   public void RCOM06() {
      sMHS850MIRAddCOReturn inAddCOReturn = (sMHS850MIRAddCOReturn)MICommon.getInDS(sMHS850MIRAddCOReturn.class);
      sMHS850MISAddCOReturn outAddCOReturn = (sMHS850MISAddCOReturn)MICommon.getOutDS(sMHS850MISAddCOReturn.class);

      HIHED.clear();
      HIPAC.clear();
      HILIN.clear();
      // ----------------------------------------------------------------
      //   Save process option.
      // ----------------------------------------------------------------
      XXPRFL.move(inAddCOReturn.getQDPRFL());
      XXOPC.move("*ADD");
      if (MICommon.toNumericCompany(inAddCOReturn.getQDCONO())) {
         XXCONO = MICommon.getInt();
      } else {
         MICommon.setError("CONO");
         return;
      }

      LDAZD.CONO = XXCONO;
      //-----------------------------------
      //    UTC mode
      UTCmode = isUTCmode(inAddCOReturn.getQDUTCM());
      if (IN60) {
         MICommon.setError("UTCM", "WUTCM01", inAddCOReturn.getQDUTCM());
         return;
      }
      //-----------------------------------
      // Message type
      XXE065.moveLeftPad(inAddCOReturn.getQDE065());
      RE065();
      if (IN60) {
         return;
      }
      // Check Warehouse
      XXWHLO.moveLeftPad(inAddCOReturn.getQDWHLO());
      RWHL();
      if (IN60) {
         return;
      }
      if (UTCmode) {
         //   Determine time zone for Warehouse
         if (!CRCalendar.retrieveTIZOForWHLO(PXMNGTZN, LDAZD.CONO, XXWHLO)) {
            IN60 = true;
            //   MSGID=S_00148 UTC time conversion failed.
            MICommon.setError("GEDT","S_00148");
            return;
         }
         //   time zone for warehouse
         TIZOforWHLO.move(CRCalendar.getTimeZone());
      }
      //-----------------------------------
      //   Retreive message number if blank
      if (inAddCOReturn.getQDMSGN().isBlank()) {
         RVMSNR();
         inAddCOReturn.setQDMSGN().moveLeft(PXRTVNBR.PXNBNR, 10);
      } else {
         HIHED.setCONO(LDAZD.CONO);
         HIHED.setMSGN().move(inAddCOReturn.getQDMSGN());
         rAPIDS.clear();
         X0OPC.moveLeftPad("*GET");
         rPHEDPIpreCall();
         apCall("MHIHEDPI", rPHEDPI);
         rPHEDPIpostCall();
         IN60 = toBoolean(X0IN60.getChar());
      }
      //   Default package number if blank
      if (inAddCOReturn.getQDPACN().isBlank()) {
         inAddCOReturn.setQDPACN().moveLeft(inAddCOReturn.getQDMSGN());
      }
      HIPAC.setCONO(LDAZD.CONO);
      HIPAC.setMSGN().move(inAddCOReturn.getQDMSGN());
      HIPAC.setPACN().move(inAddCOReturn.getQDPACN());
      rAPIDS.clear();
      X0OPC.moveLeftPad("*GET");
      rPPACPIpreCall();
      apCall("MHIPACPI", rPPACPI);
      rPPACPIpostCall();
      IN60 = toBoolean(X0IN60.getChar());
      HILIN.setCONO(LDAZD.CONO);
      HILIN.setMSGN().move(inAddCOReturn.getQDMSGN());
      HILIN.setPACN().move(inAddCOReturn.getQDPACN());
      HILIN.setMSLN(0);
      //   Retreive facility for warehouse
      ITWHL.setCONO(LDAZD.CONO);
      ITWHL.setWHLO().move(inAddCOReturn.getQDWHLO());
      if (!ITWHL.CHAIN("00", ITWHL.getKey("00"))) {
         //   MSGID=WWH0103 Warehouse &1 does not exist
         MICommon.setError( "WHLO", "WWH0103", inAddCOReturn.getQDWHLO());
         return;
      } else {
         HIHED.setFACI().move(ITWHL.getFACI());
      }
      //   Format date/time
      if (inAddCOReturn.getQDGEDT().isBlank() ||
            inAddCOReturn.getQDGETM().isBlank()) {
         XXGEDT = movexDate();
         XXGETM = movexTime();
      } else {
         if (!MICommon.toNumericDate(inAddCOReturn.getQDGEDT())) {
            MICommon.setError("GEDT");
            return;
         }
         XXGEDT = MICommon.getNumericDate();

         if (!MICommon.toNumeric(inAddCOReturn.getQDGETM())) {
            MICommon.setError("GETM");
            return;
         }
         XXGETM = MICommon.getInt();
         // Date generated
         date = XXGEDT; 
         time = XXGETM / 100;
         if (UTCmode && date > 0) {
            if (!convertFromUTC()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("GEDT","S_00148");
               return;
            }
            XXGEDT = UTCdate;
            XXGETM = UTCtime * 100;
         }
      }
      // ----------------------------------------------------------------
      //   Move data from the transaction input structure (MHS850MI1)
      //   to the execution data formats (MHIHED/MHIPAC/MHILIN)
      // ----------------------------------------------------------------
      HIHED.setCONO(LDAZD.CONO);
      HIPAC.setCONO(LDAZD.CONO);
      HILIN.setCONO(LDAZD.CONO);
      HIHED.setQLFR().move("30D ");
      HIPAC.setQLFR().move("30D ");
      HILIN.setQLFR().move("30D ");
      //-------------------------------------
      HILIN.setTTYP(30);
      HIHED.setE0IO('I');
      HIHED.setWHLO().move(inAddCOReturn.getQDWHLO());
      HIPAC.setWHLO().move(inAddCOReturn.getQDWHLO());
      HILIN.setWHLO().move(inAddCOReturn.getQDWHLO());
      HIHED.setMSGN().move(inAddCOReturn.getQDMSGN());
      HIPAC.setMSGN().move(inAddCOReturn.getQDMSGN());
      HILIN.setMSGN().move(inAddCOReturn.getQDMSGN());
      HIHED.setPMSN().moveLeft(inAddCOReturn.getQDMSGN());
      HIPAC.setPACN().move(inAddCOReturn.getQDPACN());
      HILIN.setPACN().move(inAddCOReturn.getQDPACN());
      HIHED.setGEDT(XXGEDT);
      HIPAC.setGEDT(XXGEDT);
      HILIN.setGEDT(XXGEDT);
      HIHED.setGETM(XXGETM);
      HIPAC.setGETM(XXGETM);
      HILIN.setGETM(XXGETM);
      HIHED.setE0PA().move(inAddCOReturn.getQDE0PA());
      HIHED.setE0PB().move(inAddCOReturn.getQDE0PA());
      HIHED.setE035(0);
      HIHED.setE065().move(inAddCOReturn.getQDE065());
      HIHED.setCUNO().move(inAddCOReturn.getQDCUNO());
      HIPAC.setCUNO().move(inAddCOReturn.getQDCUNO());
      HILIN.setCUNO().move(inAddCOReturn.getQDCUNO());
      HIHED.setADID().move(inAddCOReturn.getQDADID());
      HIPAC.setADID().move(inAddCOReturn.getQDADID());
      HILIN.setADID().move(inAddCOReturn.getQDADID());
      HILIN.setITNO().move(inAddCOReturn.getQDITNO());
      XXITNO.moveLeftPad(HILIN.setITNO());
      RITNO();
      //--------------------------------
      HILIN.setPOPN().move(inAddCOReturn.getQDPOPN());
      HILIN.setALWQ().move(inAddCOReturn.getQDALWQ());
      X0FLDD = 2;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddCOReturn.getQDALWT());
      RNUM();
      HILIN.setALWT((int)this.PXNUM);
      if (inAddCOReturn.getQDWHSL().isBlank()) {
         if (HILIN.getQLFR().EQ("50  ")) {
            //   If WHSL is blank and QLFR is 50, get WHSL from MITALO10
            ITALO.setTTYP(HILIN.getTTYP());
            ITALO.setRIDN().move(HILIN.getRIDN());
            ITALO.setRIDO(HILIN.getRIDO());
            ITALO.setRIDL(HILIN.getRIDL());
            ITALO.setRIDX(HILIN.getRIDX());
            ITALO.setRIDI(HILIN.getRIDI());
            ITALO.setWHSL().setMin();
            ITALO.SETLL("10", ITALO.getKey(LDAZD.CONO, "10", 8));
            if (ITALO.READE("10", ITALO.getKey(LDAZD.CONO, "10", 8))) {
               HILIN.setWHSL().move(ITALO.getWHSL());
            }
         } else {
            //   If WHSL is blank and QLFR is NOT 50, get WHSL from MITBAL00
            ITBAL.setWHLO().move(HILIN.getWHLO());
            ITBAL.setITNO().move(HILIN.getITNO());
            if (ITBAL.CHAIN("00", ITBAL.getKey(LDAZD.CONO, "00"))) {
               HILIN.setWHSL().move(ITBAL.getWHSL());
            }
         }
      } else {
         HILIN.setWHSL().move(inAddCOReturn.getQDWHSL());
      }
      HILIN.setBANO().move(inAddCOReturn.getQDBANO());
      HILIN.setCAMU().move(inAddCOReturn.getQDCAMU());
      XXQTYN = 0d;
      XXQTYA.clear();
      XXQTYA.moveLeft(inAddCOReturn.getQDQTY());
      this.PXDCCD = X1DCCD;
      RQTY();
      HILIN.setRVQA(this.PXNUM);
      XXQTYN = 0d;
      XXQTYA.clear();
      XXQTYA.moveLeft(inAddCOReturn.getQDCAWE());
      this.PXDCCD = X2DCCD;
      RQTY();
      HILIN.setCAWE(this.PXNUM);
      HILIN.setRIDN().move(inAddCOReturn.getQDRIDN());
      X0FLDD = 10;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddCOReturn.getQDREPN());
      RNUM();
      HILIN.setREPN((long)this.PXNUM);
      X0FLDD = 5;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddCOReturn.getQDRELI());
      RNUM();
      HILIN.setRELI((int)this.PXNUM);
      HILIN.setRESP().move(inAddCOReturn.getQDRESP());
      HILIN.setUSD1().move(inAddCOReturn.getQDUSD1());
      HILIN.setUSD2().move(inAddCOReturn.getQDUSD2());
      HILIN.setUSD3().move(inAddCOReturn.getQDUSD3());
      HILIN.setUSD4().move(inAddCOReturn.getQDUSD4());
      HILIN.setUSD5().move(inAddCOReturn.getQDUSD5());
      HILIN.setBREF().move(inAddCOReturn.getQDBREF());
      HILIN.setBRE2().move(inAddCOReturn.getQDBRE2());
      HIHED.setE0IO('I');
      if (!inAddCOReturn.getQDPMSN().isBlank()) {
         HIHED.setPMSN().move(inAddCOReturn.getQDPMSN());
      } else {
         HIHED.setPMSN().moveLeft(inAddCOReturn.getQDMSGN());
      }
      if (IN75) {
         IN60 = true;
         return;
      }
      //------------------------------------------
      RHEAD();
      if (IN60 && IN76) {
         return;
      }
      RPACK();
      if (IN60 && IN76) {
         return;
      }
      //   Validate/create transaction line data
      RLINE();
      if (IN60) {
         return;
      }

      outAddCOReturn.setYDCONO().move(XXCONO);
      outAddCOReturn.setYDMSGN().move(HIHED.getMSGN());

      MICommon.setData( outAddCOReturn.get());
   }

   public void RCOM07() {
      // ----------------------------------------------------------------
      //   Clear transaction records
      // ----------------------------------------------------------------
      sMHS850MIRAddDOPick inAddDOPick = (sMHS850MIRAddDOPick)MICommon.getInDS(sMHS850MIRAddDOPick.class);
      sMHS850MISAddDOPick outAddDOPick = (sMHS850MISAddDOPick)MICommon.getOutDS(sMHS850MISAddDOPick.class);

      HIHED.clear();
      HIPAC.clear();
      HILIN.clear();
      // ----------------------------------------------------------------
      //   Save process option.
      // ----------------------------------------------------------------
      XXPRFL.move(inAddDOPick.getQFPRFL());
      XXOPC.move("*ADD");
      if (MICommon.toNumericCompany(inAddDOPick.getQFCONO())) {
         XXCONO = MICommon.getInt();
      } else {
         MICommon.setError("CONO");
         return;
      }

      LDAZD.CONO = XXCONO;
      //-----------------------------------
      //    UTC mode
      UTCmode = isUTCmode(inAddDOPick.getQFUTCM());
      if (IN60) {
         MICommon.setError("UTCM", "WUTCM01", inAddDOPick.getQFUTCM());
         return;
      }
      //-----------------------------------
      // Message type
      XXE065.moveLeftPad(inAddDOPick.getQFE065());
      RE065();
      if (IN60) {
         return;
      }
      // Check Warehouse
      XXWHLO.moveLeftPad(inAddDOPick.getQFWHLO());
      RWHL();
      if (IN60) {
         return;
      }
      if (UTCmode) {
         //   Determine time zone for Warehouse
         if (!CRCalendar.retrieveTIZOForWHLO(PXMNGTZN, LDAZD.CONO, XXWHLO)) {
            IN60 = true;
            //   MSGID=S_00148 UTC time conversion failed.
            MICommon.setError("GEDT","S_00148");
            return;
         }
         //   time zone for warehouse
         TIZOforWHLO.move(CRCalendar.getTimeZone());
      }
      //-----------------------------------
      //   Retreive message number if blank
      if (inAddDOPick.getQFMSGN().isBlank()) {
         RVMSNR();
         inAddDOPick.setQFMSGN().moveLeft(PXRTVNBR.PXNBNR, 10);
      } else {
         HIHED.setCONO(LDAZD.CONO);
         HIHED.setMSGN().move(inAddDOPick.getQFMSGN());
         rAPIDS.clear();
         X0OPC.moveLeftPad("*GET");
         rPHEDPIpreCall();
         apCall("MHIHEDPI", rPHEDPI);
         rPHEDPIpostCall();
         //         IN60 = toBoolean(X0IN60.getChar());
      }
      //-----------------------------------
      //   SSCC/PANR/DLIX Validation
      //-----------------------------------
      PTRNSfound = false;
      inputDLIX.moveLeftPad(inAddDOPick.getQFRIDI());
      inputPANR.moveLeftPad(inAddDOPick.getQFPACN());
      inputSSCC.moveLeftPad(inAddDOPick.getQFSSCC());
      numberPackage = 0;
      if (!validInput()) {
         return;
      }
      if (inAddDOPick.getQFRIDI().isBlank()) {
         PTRNS.setCONO(LDAZD.CONO);
         PTRNS.setDIPA(0);
         PTRNS.setINOU(1);
         if (!inAddDOPick.getQFPACN().isBlank()) {
            PTRNS.setPANR().moveLeft(inAddDOPick.getQFPACN());
            PTRNS.SETLL("85", PTRNS.getKey("85", 4));
            if (PTRNS.READE("85", PTRNS.getKey("85",4))) {
               if (PTRNS.READE("85", PTRNS.getKey("85",4))) {
                  //   MSGID-WDL0202 Delivery number must be entered
                  MICommon.setError( "PACN", "WDL0202");
                  return;
               }
               PTRNSfound = true;
               HILIN.setDLIX(PTRNS.getDLIX());
            }
         }
         if (!inAddDOPick.getQFSSCC().isBlank()) {
            PTRNS.setSSCC().moveLeft(inAddDOPick.getQFSSCC());
            if (!PTRNS.CHAIN("70", PTRNS.getKey("70", 3))) {
               //   MSGID-WSSCC03 SSCC number does not exist
               MICommon.setError( "SSCC", "WSSCC03", inAddDOPick.getQFSSCC());
               return;
            }
            PTRNSfound = true;
            HILIN.setDLIX(PTRNS.getDLIX());
         }
      }
      //   Default package number if blank
      if (inAddDOPick.getQFPACN().isBlank()) {
         inAddDOPick.setQFPACN().moveLeft(inAddDOPick.getQFMSGN());
      }
      HIPAC.setCONO(LDAZD.CONO);
      HIPAC.setMSGN().move(inAddDOPick.getQFMSGN());
      HIPAC.setPACN().move(inAddDOPick.getQFPACN());
      rAPIDS.clear();
      X0OPC.moveLeftPad("*GET");
      rPPACPIpreCall();
      apCall("MHIPACPI", rPPACPI);
      rPPACPIpostCall();
      HILIN.setCONO(LDAZD.CONO);
      HILIN.setMSGN().move(inAddDOPick.getQFMSGN());
      HILIN.setPACN().move(inAddDOPick.getQFPACN());
      HILIN.setMSLN(0);
      //   Retreive facility for warehouse
      ITWHL.setCONO(LDAZD.CONO);
      ITWHL.setWHLO().move(inAddDOPick.getQFWHLO());
      if (!ITWHL.CHAIN("00", ITWHL.getKey("00"))) {
         //   MSGID=WWH0103 Warehouse &1 does not exist
         MICommon.setError( "WHLO", "WWH0103", inAddDOPick.getQFWHLO());
         return;
      } else {
         HIHED.setFACI().move(ITWHL.getFACI());
      }
      //   Format date/time
      if (inAddDOPick.getQFGEDT().isBlank() ||
            inAddDOPick.getQFGETM().isBlank()) {
         XXGEDT = movexDate();
         XXGETM = movexTime();
      } else {

         if (!MICommon.toNumericDate(inAddDOPick.getQFGEDT())) {
            MICommon.setError("GEDT");
            return;
         }
         XXGEDT = MICommon.getNumericDate();

         if (!MICommon.toNumeric(inAddDOPick.getQFGETM())) {
            MICommon.setError("GETM");
            return;
         }
         XXGETM = MICommon.getInt();
         // Date generated
         date = XXGEDT; 
         time = XXGETM / 100;
         if (UTCmode && date > 0) {
            if (!convertFromUTC()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("GEDT","S_00148");
               return;
            }
            XXGEDT = UTCdate;
            XXGETM = UTCtime * 100;
         }
      }
      // Get TRTP of an DO
      GHEAD.setCONO(LDAZD.CONO);
      GHEAD.setTRNR().move(inAddDOPick.getQFRIDN());
      if (GHEAD.CHAIN("00", GHEAD.getKey("00"))) {
         HILIN.setTRTP().move(GHEAD.getTRTP());
      }      
      // ----------------------------------------------------------------
      //   Move data from the transaction input structure (MHS850MI1)
      //   to the execution data formats (MHIHED/MHIPAC/MHILIN)
      // ----------------------------------------------------------------
      HIHED.setCONO(LDAZD.CONO);
      HIPAC.setCONO(LDAZD.CONO);
      HILIN.setCONO(LDAZD.CONO);
      HIHED.setQLFR().move("51  ");
      HIPAC.setQLFR().move("51  ");
      HILIN.setQLFR().move("51  ");
      //-----------------------------------------
      //---------------------------------------
      HILIN.setTTYP(51);
      HIHED.setE0IO('I');
      HIHED.setWHLO().move(inAddDOPick.getQFWHLO());
      HIPAC.setWHLO().move(inAddDOPick.getQFWHLO());
      HILIN.setWHLO().move(inAddDOPick.getQFWHLO());
      //-----------------------------------------------------------
      // Get correct customer for a DO
      GHEAD.setCONO(LDAZD.CONO);
      GHEAD.setTRNR().move(inAddDOPick.getQFRIDN());
      if (!GHEAD.CHAIN("00", GHEAD.getKey("00"))) {
         GHEAD.setTWLO().clear();
      } //�end
      //-----------------------------------------------------------
      HIHED.setCUNO().moveLeft(GHEAD.getTWLO());
      HIPAC.setCUNO().moveLeft(GHEAD.getTWLO());
      HILIN.setCUNO().moveLeft(GHEAD.getTWLO());
      HIHED.setMSGN().move(inAddDOPick.getQFMSGN());
      HIPAC.setMSGN().move(inAddDOPick.getQFMSGN());
      HILIN.setMSGN().move(inAddDOPick.getQFMSGN());
      HIHED.setPMSN().moveLeft(inAddDOPick.getQFMSGN());
      HIPAC.setPACN().move(inAddDOPick.getQFPACN());
      HILIN.setPACN().move(inAddDOPick.getQFPACN());
      HIHED.setGEDT(XXGEDT);
      HIPAC.setGEDT(XXGEDT);
      HILIN.setGEDT(XXGEDT);
      HIHED.setGETM(XXGETM);
      HIPAC.setGETM(XXGETM);
      HILIN.setGETM(XXGETM);
      HIHED.setE0PA().move(inAddDOPick.getQFE0PA());
      HIHED.setE0PB().move(inAddDOPick.getQFE0PA());
      HIHED.setE035(0);
      HIHED.setE065().move(inAddDOPick.getQFE065());
      HIHED.setADID().move(inAddDOPick.getQFADID());
      HIPAC.setADID().move(inAddDOPick.getQFADID());
      HILIN.setADID().move(inAddDOPick.getQFADID());
      HILIN.setITNO().move(inAddDOPick.getQFITNO());
      XXITNO.moveLeftPad(HILIN.setITNO());
      RITNO();
      //--------------------------------
      HILIN.setPOPN().move(inAddDOPick.getQFPOPN());
      HILIN.setALWQ().move(inAddDOPick.getQFALWQ());
      X0FLDD = 2;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddDOPick.getQFALWT());
      RNUM();
      HILIN.setALWT((int)this.PXNUM);
      X0FLDD = 1;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddDOPick.getQFOEND().getChar());
      RNUM();
      HILIN.setOEND((int)this.PXNUM);
      HILIN.setWHSL().move(inAddDOPick.getQFWHSL());
      HILIN.setBANO().move(inAddDOPick.getQFBANO());
      HILIN.setBREF().move(inAddDOPick.getQFBREF());
      HILIN.setBRE2().move(inAddDOPick.getQFBRE2());
      X0FLDD = 9;
      X0DCCD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddDOPick.getQFGRWE());
      RNUM();
      HIPAC.setGRWE(this.PXNUM);
      X0FLDD = 9;
      X0DCCD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddDOPick.getQFVOM3());
      RNUM();
      HIPAC.setVOM3(this.PXNUM);
      X0FLDD = 11;
      X0DCCD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddDOPick.getQFFRCP());
      RNUM();
      HIPAC.setFRCP(this.PXNUM);
      //-----------------------------
      HILIN.setCAMU().move(inAddDOPick.getQFCAMU());
      XXQTYN = 0d;
      XXQTYA.clear();
      XXQTYA.moveLeft(inAddDOPick.getQFQTYP());
      this.PXDCCD = X1DCCD;
      RQTY();
      HILIN.setDLQT(this.PXNUM);
      XXQTYN = 0d;
      XXQTYA.clear();
      XXQTYA.moveLeft(inAddDOPick.getQFQTYO());
      this.PXDCCD = X1DCCD;
      RQTY();
      HILIN.setALQT(this.PXNUM);
      XXQTYN = 0d;
      XXQTYA.clear();
      XXQTYA.moveLeft(inAddDOPick.getQFCAWE());
      this.PXDCCD = X2DCCD;
      RQTY();
      HILIN.setCAWE(this.PXNUM);
      HILIN.setRIDN().move(inAddDOPick.getQFRIDN());
      X0FLDD = 6;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddDOPick.getQFRIDL());
      RNUM();
      HILIN.setRIDL((int)this.PXNUM);
      X0FLDD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddDOPick.getQFRIDX());
      RNUM();
      HILIN.setRIDX((int)this.PXNUM);
      if (!PTRNSfound) {
         X0FLDD = 11;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inAddDOPick.getQFRIDI());
         RNUM();
         HILIN.setRIDI((long)this.PXNUM);
      }
      if (!inAddDOPick.getQFPLRN().isBlank()) {
         X0FLDD = 11;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inAddDOPick.getQFPLRN());
         RNUM();
         HILIN.setPLRN((long)this.PXNUM);
      }
      //   Format reporting date
      if (!inAddDOPick.getQFRPDT().isBlank()) {
         if (!MICommon.toNumericDate(inAddDOPick.getQFRPDT())) {
            MICommon.setError("RPDT");
            return;
         }
         HILIN.setRPDT(MICommon.getNumericDate());
      }
      //--------------------------------------------------------------------
      if (!inAddDOPick.getQFRPTM().isBlank()) {
         X0FLDD = 6;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inAddDOPick.getQFRPTM());
         RNUM();
         HILIN.setRPTM((int)this.PXNUM);
      }
      if (!UTCmode && 
         !inAddDOPick.getQFRPDT().isBlank() &&
            inAddDOPick.getQFRPTM().isBlank()) {
         HILIN.setRPTM(movexTime());
      }
      if (UTCmode) {
         // Reporting date
         if (!inAddDOPick.getQFRPDT().isBlank() &&
             !inAddDOPick.getQFRPTM().isBlank()) {
            date = HILIN.getRPDT(); 
            time = HILIN.getRPTM() / 100;           
            if (!convertFromUTC()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("RPDT","S_00148");
               return;
            }
            HILIN.setRPDT(UTCdate);
            HILIN.setRPTM(UTCtime * 100);
         } else {
            IN60 = true;
            //   MSGID=S_00148 UTC time conversion failed.
            MICommon.setError("RPDT","S_00148");
            return;
         }
      }
      //--------------------------------------------------------------------
      HILIN.setUSD1().move(inAddDOPick.getQFUSD1());
      HILIN.setUSD2().move(inAddDOPick.getQFUSD2());
      HILIN.setUSD3().move(inAddDOPick.getQFUSD3());
      HILIN.setUSD4().move(inAddDOPick.getQFUSD4());
      HILIN.setUSD5().move(inAddDOPick.getQFUSD5());
      X0FLDD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddDOPick.getQFPLSX());
      RNUM();
      HILIN.setPLSX((int)this.PXNUM);
      X0FLDD = 7;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddDOPick.getQFAMKO());
      RNUM();
      HIPAC.setAMKO((int)this.PXNUM);
      HIPAC.setPACT().move(inAddDOPick.getQFPACT());
      HIHED.setE0IO('I');
      if (!inAddDOPick.getQFPMSN().isBlank()) {
         HIHED.setPMSN().move(inAddDOPick.getQFPMSN());
      } else {
         HIHED.setPMSN().moveLeft(inAddDOPick.getQFMSGN());
      }
      HILIN.setTWSL().move(inAddDOPick.getQFTWSL());
      HIPAC.setPARE().move(inAddDOPick.getQFPARE());
      HIPAC.setSSCC().move(inAddDOPick.getQFSSCC());
      X0FLDD = 1;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddDOPick.getQFISMD().getChar());
      RNUM();
      HILIN.setISMD((int)this.PXNUM);
      HILIN.setLODO().moveLeftPad(inAddDOPick.getQFLODO());
      //--------------------------------------------
      //   Validate/create transaction header data
      //--------------------------------------------
      if (IN75) {
         IN60 = true;
         return;
      }
      //--------------------------------------------
      RHEAD();
      if (IN60 && IN76) {
         return;
      }
      RPACK();
      if (IN60 && IN76) {
         return;
      }
      //   Validate/create transaction line data
      RLINE();
      if (IN60) {
         return;
      }

      outAddDOPick.setYFCONO().move(XXCONO);
      outAddDOPick.setYFMSGN().move(HIHED.getMSGN());
      if (MHS850DS.getJ7STNB() > 0L) {
         X0FLDD = 10;
         XXNUMN = (long)MHS850DS.getJ7STNB();
         XXNUMA.clear();
         RNUMO();
         outAddDOPick.setYFSTNB().moveLeft(this.PXALPH);
         X0FLDD = 7;
         XXNUMN = (int)MHS850DS.getJ7STRN();
         XXNUMA.clear();
         RNUMO();
         outAddDOPick.setYFSTRN().moveLeft(this.PXALPH);
      }

      MICommon.setData( outAddDOPick.get());
   }

   public void RCOM08() {
      // ----------------------------------------------------------------
      //   Clear transaction records
      // ----------------------------------------------------------------
      sMHS850MIRAddDOReceipt inAddDOReceipt = (sMHS850MIRAddDOReceipt)MICommon.getInDS(sMHS850MIRAddDOReceipt.class);
      sMHS850MISAddDOReceipt outAddDOReceipt = (sMHS850MISAddDOReceipt)MICommon.getOutDS(sMHS850MISAddDOReceipt.class);

      HIHED.clear();
      HIPAC.clear();
      HILIN.clear();
      // ----------------------------------------------------------------
      //   Save process option.
      // ----------------------------------------------------------------
      XXPRFL.move(inAddDOReceipt.getQEPRFL());
      XXOPC.move("*ADD");
      if (MICommon.toNumericCompany(inAddDOReceipt.getQECONO())) {
         XXCONO = MICommon.getInt();
      } else {
         MICommon.setError("CONO");
         return;
      }
      //   Check Delivery number / Order index if blank
      //   MSGID=WDL0202 Delivery number must be entered
      if (inAddDOReceipt.getQERIDI().isBlank()){
         MICommon.setError("RIDI", "WDL0202");
         return;
      }

      LDAZD.CONO = XXCONO;
      //-----------------------------------
      //    UTC mode
      UTCmode = isUTCmode(inAddDOReceipt.getQEUTCM());
      if (IN60) {
         MICommon.setError("UTCM", "WUTCM01", inAddDOReceipt.getQEUTCM());
         return;
      }
      //-----------------------------------
      // Message type
      XXE065.moveLeftPad(inAddDOReceipt.getQEE065());
      RE065();
      if (IN60) {
         return;
      }
      // Check Warehouse
      XXWHLO.moveLeftPad(inAddDOReceipt.getQEWHLO());
      RWHL();
      if (IN60) {
         return;
      }
      if (UTCmode) {
         //   Determine time zone for Warehouse
         if (!CRCalendar.retrieveTIZOForWHLO(PXMNGTZN, LDAZD.CONO, XXWHLO)) {
            IN60 = true;
            //   MSGID=S_00148 UTC time conversion failed.
            MICommon.setError("GEDT","S_00148");
            return;
         }
         //   time zone for warehouse
         TIZOforWHLO.move(CRCalendar.getTimeZone());
      }
      
      // Check Location - only Inspection location allowed

      XXWHSL.moveLeftPad(inAddDOReceipt.getQEWHSL());
      
      ITMAS.setCONO(LDAZD.CONO);
      ITMAS.setITNO().move(inAddDOReceipt.getQEITNO());
      if (!ITMAS.CHAIN("00", ITMAS.getKey("00"))) {
         IN60 = true;
         // MSGID=WIT0103 Item number &1 does not exist
         MICommon.setError( "", "WIT0103", inAddDOReceipt.getQEITNO());
         return; 
      }
      //   Retrieve message number if blank
      if (inAddDOReceipt.getQEMSGN().isBlank()) {
         RVMSNR();
         inAddDOReceipt.setQEMSGN().moveLeft(PXRTVNBR.PXNBNR, 10);
      } else {
         HIHED.setCONO(LDAZD.CONO);
         HIHED.setMSGN().move(inAddDOReceipt.getQEMSGN());
         rAPIDS.clear();
         X0OPC.moveLeftPad("*GET");
         rPHEDPIpreCall();
         apCall("MHIHEDPI", rPHEDPI);
         rPHEDPIpostCall();
      }
      //   Default package number if blank
      if (inAddDOReceipt.getQEPACN().isBlank()) {
         inAddDOReceipt.setQEPACN().moveLeft(inAddDOReceipt.getQEMSGN());
      }
      HIPAC.setCONO(LDAZD.CONO);
      HIPAC.setMSGN().move(inAddDOReceipt.getQEMSGN());
      HIPAC.setPACN().move(inAddDOReceipt.getQEPACN());
      rAPIDS.clear();
      X0OPC.moveLeftPad("*GET");
      rPPACPIpreCall();
      apCall("MHIPACPI", rPPACPI);
      rPPACPIpostCall();
      HILIN.setCONO(LDAZD.CONO);
      HILIN.setMSGN().move(inAddDOReceipt.getQEMSGN());
      HILIN.setPACN().move(inAddDOReceipt.getQEPACN());
      HILIN.setMSLN(0);
      //   Retreive facility for warehouse
      ITWHL.setCONO(LDAZD.CONO);
      ITWHL.setWHLO().move(inAddDOReceipt.getQEWHLO());
      if (!ITWHL.CHAIN("00", ITWHL.getKey("00"))) {
         IN60 = true;
         //   MSGID=WWH0103 Warehouse &1 does not exist
         MICommon.setError( "WHLO", "WWH0103", inAddDOReceipt.getQEWHLO());
         return;
      } else {
         HIHED.setFACI().move(ITWHL.getFACI());
      }
      //   Format date/time
      if (inAddDOReceipt.getQEGEDT().isBlank() ||
            inAddDOReceipt.getQEGETM().isBlank()) {
         XXGEDT = movexDate();
         XXGETM = movexTime();
      } else {
         if (!MICommon.toNumericDate(inAddDOReceipt.getQEGEDT())) {
            MICommon.setError("GEDT");
            return;
         }
         XXGEDT = MICommon.getNumericDate();

         if (!MICommon.toNumeric(inAddDOReceipt.getQEGETM())) {
            MICommon.setError("GETM");
            return;
         }
         XXGETM = MICommon.getInt();
         // Date generated
         date = XXGEDT; 
         time = XXGETM / 100;
         if (UTCmode && date > 0) {
            if (!convertFromUTC()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("GEDT","S_00148");
               return;
            }
            XXGEDT = UTCdate;
            XXGETM = UTCtime * 100;
         }
      }
      // ----------------------------------------------------------------
      //   Move data from the transaction input structure (MHS850MI1)
      //   to the execution data formats (MHIHED/MHIPAC/MHILIN)
      // ----------------------------------------------------------------
      HIHED.setCONO(LDAZD.CONO);
      HIPAC.setCONO(LDAZD.CONO);
      HILIN.setCONO(LDAZD.CONO);
      HIHED.setQLFR().move("50  ");
      HIPAC.setQLFR().move("50  ");
      HILIN.setQLFR().move("50  ");
      //---------------------------------------
      //---------------------------------------
      HILIN.setTTYP(50);
      HIHED.setE0IO('I');
      HIHED.setWHLO().move(inAddDOReceipt.getQEWHLO());
      HIPAC.setWHLO().move(inAddDOReceipt.getQEWHLO());
      HILIN.setWHLO().move(inAddDOReceipt.getQEWHLO());
      HIHED.setMSGN().move(inAddDOReceipt.getQEMSGN());
      HIPAC.setMSGN().move(inAddDOReceipt.getQEMSGN());
      HILIN.setMSGN().move(inAddDOReceipt.getQEMSGN());
      HIHED.setPMSN().moveLeft(inAddDOReceipt.getQEMSGN());
      HIPAC.setPACN().move(inAddDOReceipt.getQEPACN());
      HILIN.setPACN().move(inAddDOReceipt.getQEPACN());
      HIHED.setGEDT(XXGEDT);
      HIPAC.setGEDT(XXGEDT);
      HILIN.setGEDT(XXGEDT);
      HIHED.setGETM(XXGETM);
      HIPAC.setGETM(XXGETM);
      HILIN.setGETM(XXGETM);
      HIHED.setE0PA().move(inAddDOReceipt.getQEE0PA());
      HIHED.setE0PB().move(inAddDOReceipt.getQEE0PA());
      HIHED.setE035(0);
      HIHED.setE065().move(inAddDOReceipt.getQEE065());
      HIHED.setSUNO().moveLeft(inAddDOReceipt.getQETWHL());
      HIPAC.setSUNO().moveLeft(inAddDOReceipt.getQETWHL());
      HILIN.setSUNO().moveLeft(inAddDOReceipt.getQETWHL());
      HIHED.setADID().move(inAddDOReceipt.getQEADID());
      HIPAC.setADID().move(inAddDOReceipt.getQEADID());
      HILIN.setADID().move(inAddDOReceipt.getQEADID());
      HILIN.setITNO().move(inAddDOReceipt.getQEITNO());
      XXITNO.moveLeftPad(HILIN.setITNO());
      RITNO();
      //--------------------------------
      HILIN.setPOPN().move(inAddDOReceipt.getQEPOPN());
      HILIN.setALWQ().move(inAddDOReceipt.getQEALWQ());
      X0FLDD = 2;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddDOReceipt.getQEALWT());
      RNUM();
      HILIN.setALWT((int)this.PXNUM);
      HILIN.setWHSL().move(inAddDOReceipt.getQEWHSL());
      HILIN.setBANO().move(inAddDOReceipt.getQEBANO());
      HILIN.setCAMU().move(inAddDOReceipt.getQECAMU());
      XXQTYN = 0d;
      XXQTYA.clear();
      XXQTYA.moveLeft(inAddDOReceipt.getQEQTY());
      this.PXDCCD = X1DCCD;
      RQTY();
      // Quantity recieved must be non-negative
      if (lessThan(this.PXNUM, 6, 0D)) { 
         // MSGID=XNU0003 Negative value is not permitted
         MICommon.setError("QTY", "XNU0003");
         return;
      }
      HILIN.setRVQA(this.PXNUM);
      HILIN.setRIDN().move(inAddDOReceipt.getQERIDN());
      X0FLDD = 6;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddDOReceipt.getQERIDO());
      RNUM();
      HILIN.setRIDO((int)this.PXNUM);
      X0FLDD = 6;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddDOReceipt.getQERIDL());
      RNUM();
      HILIN.setRIDL((int)this.PXNUM);
      // Compare from warehouse of the DO line to the WHLO entered           
      GLINE.setCONO(LDAZD.CONO);
      GLINE.setTRNR().move(inAddDOReceipt.getQERIDN());
      GLINE.setPONR(HILIN.getRIDL());
      if (GLINE.CHAIN("00", GLINE.getKey("00", 3)) &&    
          !inAddDOReceipt.getQETWHL().isBlank() &&    
          GLINE.getWHLO().NE(inAddDOReceipt.getQETWHL())) {       
         // MSGID=WFW0401 From warehouse &1 is invalid    
         MICommon.setError( "WHLO", "WFW0401", inAddDOReceipt.getQETWHL());
         return;
      } 
      X0FLDD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddDOReceipt.getQERIDX());
      RNUM();
      HILIN.setRIDX((int)this.PXNUM);
      X0FLDD = 11;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddDOReceipt.getQERIDI());
      RNUM();
      HILIN.setRIDI((long)this.PXNUM);
      X0FLDD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddDOReceipt.getQEPLSX());
      RNUM();
      HILIN.setPLSX((int)this.PXNUM);
      HILIN.setUSD1().move(inAddDOReceipt.getQEUSD1());
      HILIN.setUSD2().move(inAddDOReceipt.getQEUSD2());
      HILIN.setUSD3().move(inAddDOReceipt.getQEUSD3());
      HILIN.setUSD4().move(inAddDOReceipt.getQEUSD4());
      HILIN.setUSD5().move(inAddDOReceipt.getQEUSD5());
      HILIN.setBREF().move(inAddDOReceipt.getQEBREF());
      HILIN.setBRE2().move(inAddDOReceipt.getQEBRE2());
      XXQTYN = 0d;
      XXQTYA.clear();
      XXQTYA.moveLeft(inAddDOReceipt.getQECAWE());
      this.PXDCCD = X2DCCD;
      RQTY();
      HILIN.setCAWE(this.PXNUM);
      HIHED.setE0IO('I');
      if (!inAddDOReceipt.getQEPMSN().isBlank()) {
         HIHED.setPMSN().move(inAddDOReceipt.getQEPMSN());
      } else {
         HIHED.setPMSN().moveLeft(inAddDOReceipt.getQEMSGN());
      }
      X0FLDD = 1;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddDOReceipt.getQEOEND().getChar());
      RNUM();
      HILIN.setOEND((int)this.PXNUM);
      //--------------------------------------------------
      if (!inAddDOReceipt.getQERPDT().isBlank()) {
         if (!MICommon.toNumericDate(inAddDOReceipt.getQERPDT())) {
            MICommon.setError("RPDT");
            return;
         }
         HILIN.setRPDT(MICommon.getNumericDate());
      }
      //--------------------------------------------------------------------
      if (!inAddDOReceipt.getQERPTM().isBlank()) {
         X0FLDD = 6;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inAddDOReceipt.getQERPTM());
         RNUM();
         HILIN.setRPTM((int)this.PXNUM);
      }
      if (!UTCmode && 
         !inAddDOReceipt.getQERPDT().isBlank() &&
            inAddDOReceipt.getQERPTM().isBlank()) {
         HILIN.setRPTM(movexTime());
      }
      if (UTCmode) {
         // Reporting date
         if (!inAddDOReceipt.getQERPDT().isBlank() &&
             !inAddDOReceipt.getQERPTM().isBlank()) {
            date = HILIN.getRPDT(); 
            time = HILIN.getRPTM() / 100;           
            if (!convertFromUTC()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("RPDT","S_00148");
               return;
            }
            HILIN.setRPDT(UTCdate);
            HILIN.setRPTM(UTCtime * 100);
         } else {
            IN60 = true;
            //   MSGID=S_00148 UTC time conversion failed.
            MICommon.setError("RPDT","S_00148");
            return;
         }
      }
      HILIN.setTOCA().move(inAddDOReceipt.getQETOCA());
      if (HILIN.getTOCA().isBlank()) {
         HILIN.setTOCA().move(inAddDOReceipt.getQECAMU());
      }
      if (IN75) {
         IN60 = true;
         return;
      }
      if (LDAZD.MXQM == 1) {
         boolean qualityInspection = false;
         if (HILIN.getRPDT() == 0) {
            reportDate = movexDate(); 
         } else {
            reportDate = HILIN.getRPDT();
         }
         if (ITMAS.getQACD() == 2 ||
            ITMAS.getQACD() == 3) {

            MSSPE.setCONO(XXCONO);
            MSSPE.setQMGP().move(ITMAS.getQMGP());
            MSSPE.setITNO().clear();
            MSSPE.setSPEC().clear();
            MSSPE.setQSE1(0);
            MSSPE.setQSE2(0);
            MSSPE.SETLL("00", MSSPE.getKey("00"));
            while (MSSPE.READE("00", MSSPE.getKey("00", 3))){
               if (MSSPE.getSDOR() == 1 && reportDate >= MSSPE.getQSE1() && reportDate <= MSSPE.getQSI1()) {
                  qualityInspection = true;
               }
            }
            if (!qualityInspection) { 
               MSSPE.setCONO(XXCONO);
               MSSPE.setQMGP().clear();
               MSSPE.setITNO().move(ITMAS.getITNO());
               MSSPE.setSPEC().clear();
               MSSPE.setQSE1(0);
               MSSPE.setQSE2(0);
               MSSPE.SETLL("00", MSSPE.getKey("00"));
               while (MSSPE.READE("00", MSSPE.getKey("00", 3))){
                  if (MSSPE.getSDOR() == 1 && reportDate >= MSSPE.getQSE1() && reportDate <= MSSPE.getQSI1()) {
                     qualityInspection = true;
                  }
               }
            }
            if (qualityInspection) {   
               ITLOC.setCONO(XXCONO);
               ITLOC.setWHLO().move(XXWHLO);
               ITLOC.setITNO().move(ITMAS.getITNO());
               ITLOC.setWHSL().move(XXWHSL);
               ITLOC.setBANO().move(inAddDOReceipt.getQEBANO());
               getContainer(XXWHLO, XXWHSL, ITMAS.getITNO());
               if (!toContainer) {
                  ITLOC.setCAMU().clear();
               } else {
                  if (!inAddDOReceipt.getQETOCA().isBlank()) {
                     ITLOC.setCAMU().moveLeftPad(inAddDOReceipt.getQETOCA());
                  } else {
                     FTRNS.setCONO(XXCONO);
                     FTRNS.setWHLO().moveLeftPad(XXWHLO);
                     FTRNS.setDLIX(HILIN.getRIDI());
                     FTRNS.setRORC(5);
                     FTRNS.setRIDN().moveLeftPad(HILIN.getRIDN());
                     FTRNS.setRIDL(HILIN.getRIDL());
                     FTRNS.setRIDX(HILIN.getRIDX());
                     FTRNS.setBANO().moveLeftPad(inAddDOReceipt.getQEBANO());
                     if (FTRNS.CHAIN("50", FTRNS.getKey("50", 8))) {
                        ITLOC.setCAMU().moveLeftPad(FTRNS.getCAMU());
                     }
                  }
               }
               if (ITLOC.CHAIN("00", ITLOC.getKey("00", 6))) {
                  IN60 = true;
                  // Duplicate balance ID exists on location. Item Quality Inspection of DO must have other location. Change to new warehouse loc.    
                  MICommon.setError( "ITNO", "S_00626");
                  return;
               }
            }
         }
      }
      //----------------------------------------------------
      //   Validate/create transaction header data
      RHEAD();
      if (IN60 && IN76) {
         return;
      }
      RPACK();
      if (IN60 && IN76) {
         return;
      }
      //   Validate/create transaction line data
      RLINE();
      if (IN60) {
         return;
      }

      outAddDOReceipt.setYECONO().move(XXCONO);
      outAddDOReceipt.setYEMSGN().move(HIHED.getMSGN());

      MICommon.setData( outAddDOReceipt.get());
   }
   
   /**
   * get container
   */
   public void getContainer(MvxString WHLO, MvxString WHSL, MvxString ITNO) {
      toContainer = false;
      // Retreive MITBAL record
      ITBAL.setCONO(LDAZD.CONO);
      ITBAL.setWHLO().move(WHLO);
      ITBAL.setITNO().move(ITNO);
      if (ITBAL.CHAIN("00", ITBAL.getKey("00"))) {
         // Retreive MITPCE record
         ITPCE.setCONO(LDAZD.CONO);
         ITPCE.setWHLO().move(WHLO);
         ITPCE.setWHSL().move(WHSL);
         if (ITPCE.CHAIN("00", ITPCE.getKey("00"))) {
            if (ITBAL.getCOMG() > 0 && ITPCE.getCMNG() == 1) {
               toContainer = true;
            }
            if (ITBAL.getCOMG() == 0 || ITPCE.getCMNG() == 0) {
               toContainer = false;
            }
         }
      }
   }

   public void RCOM09() {
      // ----------------------------------------------------------------
      //   Clear transaction records
      // ----------------------------------------------------------------
      sMHS850MIRAddPOReceipt inAddPOReceipt = (sMHS850MIRAddPOReceipt)MICommon.getInDS(sMHS850MIRAddPOReceipt.class);
      sMHS850MISAddPOReceipt outAddPOReceipt = (sMHS850MISAddPOReceipt)MICommon.getOutDS(sMHS850MISAddPOReceipt.class);

      HIHED.clear();
      HIPAC.clear();
      HILIN.clear();
      HISUB.clear();
      // ----------------------------------------------------------------
      //   Save process option.
      // ----------------------------------------------------------------
      XXPRFL.move(inAddPOReceipt.getQHPRFL());
      XXOPC.move("*ADD");
      if (MICommon.toNumericCompany(inAddPOReceipt.getQHCONO())) {
         XXCONO = MICommon.getInt();
      } else {
         MICommon.setError("CONO");
         return;
      }

      LDAZD.CONO = XXCONO;
      //-----------------------------------
      //    UTC mode
      UTCmode = isUTCmode(inAddPOReceipt.getQHUTCM());
      if (IN60) {
         MICommon.setError("UTCM", "WUTCM01", inAddPOReceipt.getQHUTCM());
         return;
      }
      //-----------------------------------
      // Message type
      XXE065.moveLeftPad(inAddPOReceipt.getQHE065());
      RE065();
      if (IN60) {
         return;
      }
      // Check Warehouse
      XXWHLO.moveLeftPad(inAddPOReceipt.getQHWHLO());
      RWHL();
      if (IN60) {
         return;
      }
      if (UTCmode) {
         //   Determine time zone for Warehouse
         if (!CRCalendar.retrieveTIZOForWHLO(PXMNGTZN, LDAZD.CONO, XXWHLO)) {
            IN60 = true;
            //   MSGID=S_00148 UTC time conversion failed.
            MICommon.setError("GEDT","S_00148");
            return;
         }
         //   time zone for warehouse
         TIZOforWHLO.move(CRCalendar.getTimeZone());
      }
      //-----------------------------------
      //   Retreive message number if blank
      if (inAddPOReceipt.getQHMSGN().isBlank()) {
         RVMSNR();
         inAddPOReceipt.setQHMSGN().moveLeft(PXRTVNBR.PXNBNR, 10);
      } else {
         HIHED.setCONO(LDAZD.CONO);
         HIHED.setMSGN().move(inAddPOReceipt.getQHMSGN());
         rAPIDS.clear();
         X0OPC.moveLeftPad("*GET");
         rPHEDPIpreCall();
         apCall("MHIHEDPI", rPHEDPI);
         rPHEDPIpostCall();
         IN60 = toBoolean(X0IN60.getChar());
      }
      //   Default package number if blank
      if (inAddPOReceipt.getQHPACN().isBlank()) {
         inAddPOReceipt.setQHPACN().moveLeft(inAddPOReceipt.getQHMSGN());
      }
      HIPAC.setCONO(LDAZD.CONO);
      HIPAC.setMSGN().move(inAddPOReceipt.getQHMSGN());
      HIPAC.setPACN().move(inAddPOReceipt.getQHPACN());
      rAPIDS.clear();
      X0OPC.moveLeftPad("*GET");
      rPPACPIpreCall();
      apCall("MHIPACPI", rPPACPI);
      rPPACPIpostCall();
      toBoolean(X0IN60.getChar());
      HILIN.setCONO(LDAZD.CONO);
      HILIN.setMSGN().move(inAddPOReceipt.getQHMSGN());
      HILIN.setPACN().move(inAddPOReceipt.getQHPACN());
      HILIN.setMSLN(0);
      HISUB.setMSLN(1);
      //   Retreive facility for warehouse
      ITWHL.setCONO(LDAZD.CONO);
      ITWHL.setWHLO().move(inAddPOReceipt.getQHWHLO());
      if (!ITWHL.CHAIN("00", ITWHL.getKey("00"))) {
         //   MSGID=WWH0103 Warehouse &1 does not exist
         MICommon.setError( "WHLO", "WWH0103", inAddPOReceipt.getQHWHLO());
         return;
      } else {
         HIHED.setFACI().move(ITWHL.getFACI());
      }
      //   Format date/time
      if (inAddPOReceipt.getQHGEDT().isBlank() ||
            inAddPOReceipt.getQHGETM().isBlank()) {
         XXGEDT = movexDate();
         XXGETM = movexTime();
      } else {
         if (!MICommon.toNumericDate(inAddPOReceipt.getQHGEDT())) {
            MICommon.setError("GEDT");
            return;
         }
         XXGEDT = MICommon.getNumericDate();

         if (!MICommon.toNumeric(inAddPOReceipt.getQHGETM())) {
            MICommon.setError("GETM");
            return;
         }
         XXGETM = MICommon.getInt();
         // Date generated
         date = XXGEDT; 
         time = XXGETM / 100;
         if (UTCmode && date > 0) {
            if (!convertFromUTC()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("GEDT","S_00148");
               return;
            }
            XXGEDT = UTCdate;
            XXGETM = UTCtime * 100;
         }
      }
      // ----------------------------------------------------------------
      //   Move data from the transaction input structure (MHS850MI1)
      //   to the execution data formats (MHIHED/MHIPAC/MHILIN)
      // ----------------------------------------------------------------
      HIHED.setCONO(LDAZD.CONO);
      HIPAC.setCONO(LDAZD.CONO);
      HILIN.setCONO(LDAZD.CONO);
      HISUB.setCONO(LDAZD.CONO);
      HIHED.setQLFR().move("20  ");
      HIPAC.setQLFR().move("20  ");
      HILIN.setQLFR().move("20  ");
      HISUB.setQLFR().move("20  ");
      //---------------------------------------
      //---------------------------------------
      HILIN.setTTYP(25);
      HIHED.setE0IO('I');
      HIHED.setWHLO().move(inAddPOReceipt.getQHWHLO());
      HIPAC.setWHLO().move(inAddPOReceipt.getQHWHLO());
      HILIN.setWHLO().move(inAddPOReceipt.getQHWHLO());
      HIHED.setMSGN().move(inAddPOReceipt.getQHMSGN());
      HIPAC.setMSGN().move(inAddPOReceipt.getQHMSGN());
      HILIN.setMSGN().move(inAddPOReceipt.getQHMSGN());
      HISUB.setMSGN().move(inAddPOReceipt.getQHMSGN());
      HIHED.setPMSN().moveLeft(inAddPOReceipt.getQHMSGN());
      HIPAC.setPACN().move(inAddPOReceipt.getQHPACN());
      HILIN.setPACN().move(inAddPOReceipt.getQHPACN());
      HISUB.setPACN().move(inAddPOReceipt.getQHPACN());
      HIHED.setGEDT(XXGEDT);
      HIPAC.setGEDT(XXGEDT);
      HILIN.setGEDT(XXGEDT);
      HIHED.setGETM(XXGETM);
      HIPAC.setGETM(XXGETM);
      HILIN.setGETM(XXGETM);
      HIHED.setE0PA().move(inAddPOReceipt.getQHE0PA());
      HIHED.setE0PB().move(inAddPOReceipt.getQHE0PA());
      HIHED.setE035(0);
      HIHED.setE065().move(inAddPOReceipt.getQHE065());
      HIHED.setSUNO().move(inAddPOReceipt.getQHSUNO());
      X0FLDD = 1;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddPOReceipt.getQHSUTY().getChar());
      RNUM();
      HIHED.setSUTY((int)this.PXNUM);
      HIHED.setADID().move(inAddPOReceipt.getQHADID());
      HILIN.setITNO().move(inAddPOReceipt.getQHITNO());
      HISUB.setITNO().move(inAddPOReceipt.getQHITNO());
      XXITNO.moveLeftPad(HILIN.setITNO());
      RITNO();
      //--------------------------------
      HILIN.setPOPN().move(inAddPOReceipt.getQHPOPN());
      HILIN.setALWQ().move(inAddPOReceipt.getQHALWQ());
      X0FLDD = 2;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddPOReceipt.getQHALWT());
      RNUM();
      HILIN.setALWT((int)this.PXNUM);
      HILIN.setWHSL().move(inAddPOReceipt.getQHWHSL());
      HILIN.setBANO().move(inAddPOReceipt.getQHBANO());
      HISUB.setBANO().move(inAddPOReceipt.getQHBANO());
      HILIN.setCAMU().move(inAddPOReceipt.getQHCAMU());
      HILIN.setRIDN().move(inAddPOReceipt.getQHRIDN());
      X0FLDD = 6;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddPOReceipt.getQHRIDL());
      RNUM();
      HILIN.setRIDL((int)this.PXNUM);
      PLINE.setCONO(LDAZD.CONO);
      PLINE.setPUNO().moveLeft(HILIN.getRIDN());
      PLINE.setPNLI(HILIN.getRIDL());
      if (PLINE.CHAIN("00", PLINE.getKey("00", 3))) {
         if (ITMAS.getUNMS().NE(PLINE.getPUUN())) {
            ITAUN.setCONO(HILIN.getCONO());
            ITAUN.setITNO().move(HILIN.getITNO());
            ITAUN.setAUTP(1);
            ITAUN.setALUN().move(PLINE.getPUUN());
            if (ITAUN.CHAIN("00", ITAUN.getKey("00"))) {
               X1DCCD = ITAUN.getDCCD();
            }
         }
      }
      XXQTYN = 0d;
      XXQTYA.clear();
      XXQTYA.moveLeft(inAddPOReceipt.getQHQTY());
      this.PXDCCD = X1DCCD;
      RQTY();
      HILIN.setRVQA(this.PXNUM);
      X0FLDD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddPOReceipt.getQHRIDX());
      RNUM();
      HILIN.setRIDX((int)this.PXNUM);
      X0FLDD = 11;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddPOReceipt.getQHRIDI());
      RNUM();
      HILIN.setRIDI((long)this.PXNUM);
      HILIN.setUSD1().move(inAddPOReceipt.getQHUSD1());
      HILIN.setUSD2().move(inAddPOReceipt.getQHUSD2());
      HILIN.setUSD3().move(inAddPOReceipt.getQHUSD3());
      HILIN.setUSD4().move(inAddPOReceipt.getQHUSD4());
      HILIN.setUSD5().move(inAddPOReceipt.getQHUSD5());
      HILIN.setBREF().move(inAddPOReceipt.getQHBREF());
      HILIN.setBRE2().move(inAddPOReceipt.getQHBRE2());
      XXQTYN = 0d;
      XXQTYA.clear();
      XXQTYA.moveLeft(inAddPOReceipt.getQHCAWE());
      this.PXDCCD = X2DCCD;
      RQTY();
      HILIN.setCAWE(this.PXNUM);
      HISUB.setCAWE(this.PXNUM);
      if (ITMAS.getSUME() == 1 && HILIN.getCAWE() !=0d) {
         IN60 = true;
         //   MSGID=MH85015 CW Not allowed when Sublot controlled
         MICommon.setError( "", "MH85015");
         return;
      } else {
         if (ITMAS.getSUME() == 1 && HILIN.getCAWE() ==0d) {
            ITAUN.setCONO(HILIN.getCONO());
            ITAUN.setITNO().move(HILIN.getITNO());
            ITAUN.setAUTP(1);
            ITAUN.setALUN().move(ITMAS.getCWUN());
            if (ITAUN.CHAIN("00", ITAUN.getKey("00"))) {
               HILIN.setCAWE(ITAUN.getCOFA() * HILIN.getRVQA());
               HISUB.setCAWE(ITAUN.getCOFA());
            }
         }
      }
      
      X0FLDD = 1;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddPOReceipt.getQHOEND().getChar());
      RNUM();
      HILIN.setOEND((int)this.PXNUM);
      HIHED.setE0IO('I');
      if (!inAddPOReceipt.getQHPMSN().isBlank()) {
         HIHED.setPMSN().move(inAddPOReceipt.getQHPMSN());
      } else {
         HIHED.setPMSN().moveLeft(inAddPOReceipt.getQHMSGN());
      }

      if (!MICommon.toNumericDate(inAddPOReceipt.getQHEXPI())) {
         MICommon.setError("EXPI");
         return;
      }
      HILIN.setEXPI(MICommon.getNumericDate());
      //-----------------------------------------------------
      if (!inAddPOReceipt.getQHRPDT().isBlank()) {
         if (!MICommon.toNumericDate(inAddPOReceipt.getQHRPDT())) {
            MICommon.setError("RPDT");
            return;
         }
         HILIN.setRPDT(MICommon.getNumericDate());
      }
      //--------------------------------------------------------------------
      if (!inAddPOReceipt.getQHRPTM().isBlank()) {
         X0FLDD = 6;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inAddPOReceipt.getQHRPTM());
         RNUM();
         HILIN.setRPTM((int)this.PXNUM);
      }
      if (!UTCmode && 
         !inAddPOReceipt.getQHRPDT().isBlank() &&
            inAddPOReceipt.getQHRPTM().isBlank()) {
         HILIN.setRPTM(movexTime());
      }
      if (UTCmode) {
         // Reporting date
         if (!inAddPOReceipt.getQHRPDT().isBlank() &&
             !inAddPOReceipt.getQHRPTM().isBlank()) {
            date = HILIN.getRPDT(); 
            time = HILIN.getRPTM() / 100;           
            if (!convertFromUTC()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("RPDT","S_00148");
               return;
            }
            HILIN.setRPDT(UTCdate);
            HILIN.setRPTM(UTCtime * 100);
         } else {
            IN60 = true;
            //   MSGID=S_00148 UTC time conversion failed.
            MICommon.setError("RPDT","S_00148");
            return;
         }
      }
      //----------------------------------------------------
      //   Delivery note number and date
      HIHED.setSUDO().move(inAddPOReceipt.getQHSUDO());
      HIPAC.setSUDO().move(inAddPOReceipt.getQHSUDO());
      HILIN.setSUDO().move(inAddPOReceipt.getQHSUDO());
      if (!inAddPOReceipt.getQHDNDT().isBlank()) {
         if (!MICommon.toNumericDate(inAddPOReceipt.getQHDNDT())) {
            MICommon.setError("DNDT");
            return;
         }
         HIHED.setDNDT(MICommon.getNumericDate());
         HIPAC.setDNDT(MICommon.getNumericDate());
         HILIN.setDNDT(MICommon.getNumericDate());
      }
      // Potency
      if (!inAddPOReceipt.getQHPOCY().isBlank()) {
         X0FLDD = 5;
         X0DCCD = 2;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inAddPOReceipt.getQHPOCY());
         RNUM();
         HILIN.setPOCY(this.PXNUM);
      }   
      //----------------------------------------------------
      //   Validate/create transaction header data
      //----------------------------------------------------
      if (IN75) {
         IN60 = true;
         return;
      }
      //----------------------------------------------------
      RHEAD();
      if (IN60 && IN76) {
         return;
      }
      RPACK();
      if (IN60 && IN76) {
         return;
      }
      //   Validate/create transaction line data
      RLINE();
      if (IN60) {
         return;
      }
      //--------------------------------------------------  
      outAddPOReceipt.setYHCONO().move(XXCONO);
      outAddPOReceipt.setYHMSGN().move(HIHED.getMSGN());
      outAddPOReceipt.setYHPACN().move(HIPAC.getPACN());
      outAddPOReceipt.setYHMSLN().move(HILIN.getMSLN());

      MICommon.setData( outAddPOReceipt.get());
   }

   public void RCOM10() {
      // ----------------------------------------------------------------
      //   Clear transaction records
      // ----------------------------------------------------------------
      sMHS850MIRAddROPick inAddROPick = (sMHS850MIRAddROPick)MICommon.getInDS(sMHS850MIRAddROPick.class);
      sMHS850MISAddROPick outAddROPick = (sMHS850MISAddROPick)MICommon.getOutDS(sMHS850MISAddROPick.class);

      HIHED.clear();
      HIPAC.clear();
      HILIN.clear();
      // ----------------------------------------------------------------
      //   Save process option.
      // ----------------------------------------------------------------
      XXPRFL.move(inAddROPick.getQGPRFL());
      XXOPC.move("*ADD");
      if (MICommon.toNumericCompany(inAddROPick.getQGCONO())) {
         XXCONO = MICommon.getInt();
      } else {
         MICommon.setError("CONO");
         return;
      }

      LDAZD.CONO = XXCONO;
      //-----------------------------------
      //    UTC mode
      UTCmode = isUTCmode(inAddROPick.getQGUTCM());
      if (IN60) {
         MICommon.setError("UTCM", "WUTCM01", inAddROPick.getQGUTCM());
         return;
      }
      //-----------------------------------
      // Message type
      XXE065.moveLeftPad(inAddROPick.getQGE065());
      RE065();
      if (IN60) {
         return;
      }
      // Check Warehouse
      XXWHLO.moveLeftPad(inAddROPick.getQGWHLO());
      RWHL();
      if (IN60) {
         return;
      }
      if (UTCmode) {
         //   Determine time zone for Warehouse
         if (!CRCalendar.retrieveTIZOForWHLO(PXMNGTZN, LDAZD.CONO, XXWHLO)) {
            IN60 = true;
            //   MSGID=S_00148 UTC time conversion failed.
            MICommon.setError("GEDT","S_00148");
            return;
         }
         //   time zone for warehouse
         TIZOforWHLO.move(CRCalendar.getTimeZone());
      }
      //-----------------------------------
      //   Retreive message number if blank
      if (inAddROPick.getQGMSGN().isBlank()) {
         RVMSNR();
         inAddROPick.setQGMSGN().moveLeft(PXRTVNBR.PXNBNR, 10);
      } else {
         HIHED.setCONO(LDAZD.CONO);
         HIHED.setMSGN().move(inAddROPick.getQGMSGN());
         rAPIDS.clear();
         X0OPC.moveLeftPad("*GET");
         rPHEDPIpreCall();
         apCall("MHIHEDPI", rPHEDPI);
         rPHEDPIpostCall();
         toBoolean(X0IN60.getChar());
      }
      //-----------------------------------
      //   SSCC/PANR/DLIX Validation
      //-----------------------------------
      inputDLIX.moveLeftPad(inAddROPick.getQGRIDI());
      inputPANR.moveLeftPad(inAddROPick.getQGPACN());
      inputSSCC.moveLeftPad(inAddROPick.getQGSSCC());
      numberPackage = 0;
      if (!validInput()) {
         return;
      }
      if (inAddROPick.getQGRIDI().isBlank()) {
         retrieveDLIX();
         if (IN60) {
            return;
         }
         inAddROPick.setQGPACN().moveLeftPad(inputPANR);
      }
      //   Default package number if blank
      if (inAddROPick.getQGPACN().isBlank()) {
         inAddROPick.setQGPACN().moveLeft(inAddROPick.getQGMSGN());
      }
      HIPAC.setCONO(LDAZD.CONO);
      HIPAC.setMSGN().move(inAddROPick.getQGMSGN());
      HIPAC.setPACN().move(inAddROPick.getQGPACN());
      rAPIDS.clear();
      X0OPC.moveLeftPad("*GET");
      rPPACPIpreCall();
      apCall("MHIPACPI", rPPACPI);
      rPPACPIpostCall();
      toBoolean(X0IN60.getChar());
      HILIN.setCONO(LDAZD.CONO);
      HILIN.setMSGN().move(inAddROPick.getQGMSGN());
      HILIN.setPACN().move(inAddROPick.getQGPACN());
      HILIN.setMSLN(0);
      //   Retreive facility for warehouse
      ITWHL.setCONO(LDAZD.CONO);
      ITWHL.setWHLO().move(inAddROPick.getQGWHLO());
      if (!ITWHL.CHAIN("00", ITWHL.getKey("00"))) {
         //   MSGID=WWH0103 Warehouse &1 does not exist
         MICommon.setError( "WHLO", "WWH0103", inAddROPick.getQGWHLO());
         return;
      } else {
         HIHED.setFACI().move(ITWHL.getFACI());
      }
      //   Format date/time
      if (inAddROPick.getQGGEDT().isBlank() ||
            inAddROPick.getQGGETM().isBlank()) {
         XXGEDT = movexDate();
         XXGETM = movexTime();
      } else {
         if (!MICommon.toNumericDate(inAddROPick.getQGGEDT())) {
            MICommon.setError("GEDT");
            return;
         }
         XXGEDT = MICommon.getNumericDate();

         if (!MICommon.toNumeric(inAddROPick.getQGGETM())) {
            MICommon.setError("GETM");
            return;
         }
         XXGETM = MICommon.getInt();
         // Date generated
         date = XXGEDT; 
         time = XXGETM / 100;
         if (UTCmode && date > 0) {
            if (!convertFromUTC()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("GEDT","S_00148");
               return;
            }
            XXGEDT = UTCdate;
            XXGETM = UTCtime * 100;
         }
      }
      // Get TRTP of an RO
      GHEAD.setCONO(LDAZD.CONO);
      GHEAD.setTRNR().move(inAddROPick.getQGRIDN());
      if (GHEAD.CHAIN("00", GHEAD.getKey("00"))) {
         HILIN.setTRTP().move(GHEAD.getTRTP());
      } //�end
      // ----------------------------------------------------------------
      //   Move data from the transaction input structure (MHS850MI1)
      //   to the execution data formats (MHIHED/MHIPAC/MHILIN)
      // ----------------------------------------------------------------
      HIHED.setCONO(LDAZD.CONO);
      HIPAC.setCONO(LDAZD.CONO);
      HILIN.setCONO(LDAZD.CONO);
      HIHED.setQLFR().move("41  ");
      HIPAC.setQLFR().move("41  ");
      HILIN.setQLFR().move("41  ");
      //---------------------------------------
      //---------------------------------------
      HILIN.setTTYP(41);
      HIHED.setE0IO('I');
      HIHED.setWHLO().move(inAddROPick.getQGWHLO());
      HIPAC.setWHLO().move(inAddROPick.getQGWHLO());
      HILIN.setWHLO().move(inAddROPick.getQGWHLO());
      HIHED.setCUNO().moveLeft(inAddROPick.getQGWHLO());
      HIPAC.setCUNO().moveLeft(inAddROPick.getQGWHLO());
      HILIN.setCUNO().moveLeft(inAddROPick.getQGWHLO());
      HIHED.setMSGN().move(inAddROPick.getQGMSGN());
      HIPAC.setMSGN().move(inAddROPick.getQGMSGN());
      HILIN.setMSGN().move(inAddROPick.getQGMSGN());
      HIHED.setPMSN().moveLeft(inAddROPick.getQGMSGN());
      HIPAC.setPACN().move(inAddROPick.getQGPACN());
      HILIN.setPACN().move(inAddROPick.getQGPACN());
      HIHED.setGEDT(XXGEDT);
      HIPAC.setGEDT(XXGEDT);
      HILIN.setGEDT(XXGEDT);
      HIHED.setGETM(XXGETM);
      HIPAC.setGETM(XXGETM);
      HILIN.setGETM(XXGETM);
      HIHED.setE0PA().move(inAddROPick.getQGE0PA());
      HIHED.setE0PB().move(inAddROPick.getQGE0PA());
      HIHED.setE035(0);
      HIHED.setE065().move(inAddROPick.getQGE065());
      HIHED.setADID().move(inAddROPick.getQGADID());
      HIPAC.setADID().move(inAddROPick.getQGADID());
      HILIN.setADID().move(inAddROPick.getQGADID());
      HILIN.setITNO().move(inAddROPick.getQGITNO());
      XXITNO.moveLeftPad(HILIN.setITNO());
      RITNO();
      //--------------------------------
      HILIN.setPOPN().move(inAddROPick.getQGPOPN());
      HILIN.setALWQ().move(inAddROPick.getQGALWQ());
      X0FLDD = 2;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddROPick.getQGALWT());
      RNUM();
      HILIN.setALWT((int)this.PXNUM);
      HILIN.setWHSL().move(inAddROPick.getQGWHSL());
      HILIN.setBANO().move(inAddROPick.getQGBANO());
      HILIN.setBREF().move(inAddROPick.getQGBREF());
      HILIN.setBRE2().move(inAddROPick.getQGBRE2());
      HILIN.setCAMU().move(inAddROPick.getQGCAMU());
      XXQTYN = 0d;
      XXQTYA.clear();
      XXQTYA.moveLeft(inAddROPick.getQGQTYP());
      this.PXDCCD = X1DCCD;
      RQTY();
      HILIN.setDLQT(this.PXNUM);
      XXQTYN = 0d;
      XXQTYA.clear();
      XXQTYA.moveLeft(inAddROPick.getQGQTYO());
      this.PXDCCD = X1DCCD;
      RQTY();
      HILIN.setALQT(this.PXNUM);
      HILIN.setRIDN().move(inAddROPick.getQGRIDN());
      X0FLDD = 6;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddROPick.getQGRIDL());
      RNUM();
      HILIN.setRIDL((int)this.PXNUM);
      X0FLDD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddROPick.getQGRIDX());
      RNUM();
      HILIN.setRIDX((int)this.PXNUM);
      if (!PTRNSfound) {
         X0FLDD = 11;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inAddROPick.getQGRIDI());
         RNUM();
         HILIN.setRIDI((long)this.PXNUM);
      } else {
         HILIN.setRIDI(PTRNS.getDLIX());
      }
      if (!inAddROPick.getQGPLRN().isBlank()) {
         X0FLDD = 11;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inAddROPick.getQGPLRN());
         RNUM();
         HILIN.setPLRN((long)this.PXNUM);
      }
      //   Format reporting date
      if (!inAddROPick.getQGRPDT().isBlank()) {
         if (!MICommon.toNumericDate(inAddROPick.getQGRPDT())) {
            MICommon.setError("RPDT");
            return;
         }
         HILIN.setRPDT(MICommon.getNumericDate());
      }
      //--------------------------------------------------------------------
      if (!inAddROPick.getQGRPTM().isBlank()) {
         X0FLDD = 6;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inAddROPick.getQGRPTM());
         RNUM();
         HILIN.setRPTM((int)this.PXNUM);
      }
      if (!UTCmode && 
         !inAddROPick.getQGRPDT().isBlank() &&
            inAddROPick.getQGRPTM().isBlank()) {
         HILIN.setRPTM(movexTime());
      }
      if (UTCmode) {
         // Reporting date
         if (!inAddROPick.getQGRPDT().isBlank() &&
             !inAddROPick.getQGRPTM().isBlank()) {
            date = HILIN.getRPDT(); 
            time = HILIN.getRPTM() / 100;           
            if (!convertFromUTC()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("RPDT","S_00148");
               return;
            }
            HILIN.setRPDT(UTCdate);
            HILIN.setRPTM(UTCtime * 100);
         } else {
            IN60 = true;
            //   MSGID=S_00148 UTC time conversion failed.
            MICommon.setError("RPDT","S_00148");
            return;
         }
      }
      //--------------------------------------------------------------------
      HILIN.setUSD1().move(inAddROPick.getQGUSD1());
      HILIN.setUSD2().move(inAddROPick.getQGUSD2());
      HILIN.setUSD3().move(inAddROPick.getQGUSD3());
      HILIN.setUSD4().move(inAddROPick.getQGUSD4());
      HILIN.setUSD5().move(inAddROPick.getQGUSD5());
      XXQTYN = 0d;
      XXQTYA.clear();
      XXQTYA.moveLeft(inAddROPick.getQGCAWE());
      this.PXDCCD = X2DCCD;
      RQTY();
      HILIN.setCAWE(this.PXNUM);
      X0FLDD = 7;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddROPick.getQGAMKO());
      RNUM();
      HIPAC.setAMKO((int)this.PXNUM);
      HIPAC.setPACT().move(inAddROPick.getQGPACT());
      X0FLDD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddROPick.getQGPLSX());
      RNUM();
      HILIN.setPLSX((int)this.PXNUM);
      HIHED.setE0IO('I');
      if (!inAddROPick.getQGPMSN().isBlank()) {
         HIHED.setPMSN().move(inAddROPick.getQGPMSN());
      } else {
         HIHED.setPMSN().moveLeft(inAddROPick.getQGMSGN());
      }
      HILIN.setTWSL().move(inAddROPick.getQGTWSL());
      X0FLDD = 1;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddROPick.getQGOEND().getChar());
      RNUM();
      HILIN.setOEND((int)this.PXNUM);
      //------------------------------
      X0FLDD = 9;
      X0DCCD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddROPick.getQGGRWE());
      RNUM();
      HIPAC.setGRWE(this.PXNUM);
      X0FLDD = 9;
      X0DCCD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddROPick.getQGVOM3());
      RNUM();
      HIPAC.setVOM3(this.PXNUM);
      X0FLDD = 11;
      X0DCCD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddROPick.getQGFRCP());
      RNUM();
      HIPAC.setFRCP(this.PXNUM);
      HIPAC.setPARE().move(inAddROPick.getQGPARE());
      HIPAC.setSSCC().move(inAddROPick.getQGSSCC());
      X0FLDD = 1;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddROPick.getQGISMD().getChar());
      RNUM();
      HILIN.setISMD((int)this.PXNUM);
      HILIN.setLODO().moveLeftPad(inAddROPick.getQGLODO());
      //-----------------------------------------------------
      //   Validate/create transaction header data
      //-----------------------------------------------------
      if (IN75) {
         IN60 = true;
         return;
      }
      //-----------------------------------------------------
      RHEAD();
      if (IN60 && IN76) {
         return;
      }
      RPACK();
      if (IN60 && IN76) {
         return;
      }
      //   Validate/create transaction line data
      RLINE();
      if (IN60) {
         return;
      }

      outAddROPick.setYGCONO().move(XXCONO);
      outAddROPick.setYGMSGN().move(HIHED.getMSGN());
      if (MHS850DS.getJ7STNB() > 0L) {
         X0FLDD = 10;
         XXNUMN = (long)MHS850DS.getJ7STNB();
         XXNUMA.clear();
         RNUMO();
         outAddROPick.setYGSTNB().moveLeft(this.PXALPH);
         X0FLDD = 7;
         XXNUMN = (int)MHS850DS.getJ7STRN();
         XXNUMA.clear();
         RNUMO();
         outAddROPick.setYGSTRN().moveLeft(this.PXALPH);
      }

      MICommon.setData( outAddROPick.get());
   }

   public void RCOM11() {
      // ----------------------------------------------------------------
      //   Clear transaction records
      // ----------------------------------------------------------------
      sMHS850MIRAddWhsHead inAddWhsHead = (sMHS850MIRAddWhsHead)MICommon.getInDS(sMHS850MIRAddWhsHead.class);
      sMHS850MISAddWhsHead outAddWhsHead = (sMHS850MISAddWhsHead)MICommon.getOutDS(sMHS850MISAddWhsHead.class);

      HIHED.clear();
      HIPAC.clear();
      HILIN.clear();
      // ----------------------------------------------------------------
      //   Default process option.
      // ----------------------------------------------------------------
      XXPRFL.move("*CHK");
      XXOPC.move("*ADD");
      if (MICommon.toNumericCompany(inAddWhsHead.getQ0CONO())) {
         XXCONO = MICommon.getInt();
      } else {
         MICommon.setError("CONO");
         return;
      }
      LDAZD.CONO = XXCONO;
      //-----------------------------------
      //    UTC mode
      UTCmode = isUTCmode(inAddWhsHead.getQ0UTCM());
      if (IN60) {
         MICommon.setError("UTCM", "WUTCM01", inAddWhsHead.getQ0UTCM());
         return;
      }
      //   Retreive message number if blank
      if (inAddWhsHead.getQ0MSGN().isBlank()) {
         RVMSNR();
         inAddWhsHead.setQ0MSGN().moveLeft(PXRTVNBR.PXNBNR, 10);
      }
      //   Retreive facility for warehouse
      ITWHL.setCONO(LDAZD.CONO);
      ITWHL.setWHLO().move(inAddWhsHead.getQ0WHLO());
      if (!ITWHL.CHAIN("00", ITWHL.getKey("00"))) {
         //   MSGID=WWH0103 Warehouse &1 does not exist
         MICommon.setError( "WHLO", "WWH0103", inAddWhsHead.getQ0WHLO());
         return;
      } else {
         HIHED.setFACI().move(ITWHL.getFACI());
      }
      if (UTCmode) {
         //   Determine time zone for Warehouse
         XXWHLO.moveLeftPad(inAddWhsHead.getQ0WHLO());
         if (!CRCalendar.retrieveTIZOForWHLO(PXMNGTZN, LDAZD.CONO, XXWHLO)) {
            IN60 = true;
            //   MSGID=S_00148 UTC time conversion failed.
            MICommon.setError("GEDT","S_00148");
            return;
         }
         //   time zone for warehouse
         TIZOforWHLO.move(CRCalendar.getTimeZone());
      }
      //   Format date/time
      if (inAddWhsHead.getQ0GEDT().isBlank() ||
            inAddWhsHead.getQ0GETM().isBlank()) {
         XXGEDT = movexDate();
         XXGETM = movexTime();
      } else {
         if (!MICommon.toNumericDate(inAddWhsHead.getQ0GEDT())) {
            MICommon.setError("GEDT");
            return;
         }
         XXGEDT = MICommon.getNumericDate();

         if (!MICommon.toNumeric(inAddWhsHead.getQ0GETM())) {
            MICommon.setError("GETM");
            return;
         }
         XXGETM = MICommon.getInt();
         // Date generated
         date = XXGEDT; 
         time = XXGETM / 100;
         if (UTCmode && date > 0) {
            if (!convertFromUTC()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("GEDT","S_00148");
               return;
            }
            XXGEDT = UTCdate;
            XXGETM = UTCtime * 100;
         }
      }
      if (inAddWhsHead.getQ0CHGD().isBlank()) {
         XXCHGD = movexDate();
      } else {
         if (!MICommon.toNumericDate(inAddWhsHead.getQ0CHGD())) {
            MICommon.setError("CHGD");
            return;
         }
         XXCHGD = MICommon.getNumericDate();
      }
      // ----------------------------------------------------------------
      //   Move data from the transaction input structure (MHS850MI1)
      //   to the execution data formats (MHIHED/MHIPAC/MHILIN)
      // ----------------------------------------------------------------
      HIHED.setCONO(LDAZD.CONO);
      HIHED.setDIVI().move(inAddWhsHead.getQ0DIVI());
      HIHED.setWHLO().move(inAddWhsHead.getQ0WHLO());
      HIHED.setMSGN().move(inAddWhsHead.getQ0MSGN());
      if (!inAddWhsHead.getQ0E0IO().isBlank()) {
         HIHED.setE0IO(inAddWhsHead.getQ0E0IO().getChar());
      } else {
         HIHED.setE0IO('I');
      }
      HIHED.setQLFR().move(inAddWhsHead.getQ0QLFR());
      HIHED.setGEDT(XXGEDT);
      HIHED.setGETM(XXGETM);
      HIHED.setCHGD(XXCHGD);
      HIHED.setSTAT().move(inAddWhsHead.getQ0STAT());
      HIHED.setTRSL().move(inAddWhsHead.getQ0TRSL());
      HIHED.setTRSH().move(inAddWhsHead.getQ0TRSH());
      X0FLDD = 1;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWhsHead.getQ0ADTY().getChar());
      RNUM();
      HIHED.setADTY((int)this.PXNUM);
      HIHED.setADID().move(inAddWhsHead.getQ0ADID());
      if (!inAddWhsHead.getQ0PMSN().isBlank()) {
         HIHED.setPMSN().move(inAddWhsHead.getQ0PMSN());
      } else {
         HIHED.setPMSN().moveLeft(inAddWhsHead.getQ0MSGN());
      }
      HIHED.setE0PA().move(inAddWhsHead.getQ0E0PA());
      HIHED.setE0QA().move(inAddWhsHead.getQ0E0QA());
      HIHED.setE0PB().move(inAddWhsHead.getQ0E0PB());
      HIHED.setE0QB().move(inAddWhsHead.getQ0E0QB());
      HIHED.setDONR().move(inAddWhsHead.getQ0DONR());
      HIHED.setE007().move(inAddWhsHead.getQ0E007());
      HIHED.setE014().move(inAddWhsHead.getQ0E014());
      HIHED.setE026().move(inAddWhsHead.getQ0E026());
      HIHED.setE035(toInt(inAddWhsHead.getQ0E035().getChar()));
      HIHED.setE065().move(inAddWhsHead.getQ0E065());
      HIHED.setE052().move(inAddWhsHead.getQ0E052());
      HIHED.setE054().move(inAddWhsHead.getQ0E054());
      HIHED.setE051().move(inAddWhsHead.getQ0E051());
      HIHED.setE057().move(inAddWhsHead.getQ0E057());
      HIHED.setDLSP().move(inAddWhsHead.getQ0DLSP());
      HIHED.setEDFR().move(inAddWhsHead.getQ0EDFR());
      HIHED.setORCA().move(inAddWhsHead.getQ0ORCA());
      HIHED.setRIDN().move(inAddWhsHead.getQ0RIDN());
      X0FLDD = 6;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWhsHead.getQ0RIDO());
      RNUM();
      HIHED.setRIDO((int)this.PXNUM);
      X0FLDD = 11;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWhsHead.getQ0RIDI());
      RNUM();
      HIHED.setRIDI((long)this.PXNUM);
      X0FLDD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWhsHead.getQ0PLSX());
      RNUM();
      HIHED.setPLSX((int)this.PXNUM);
      HIHED.setCUNO().move(inAddWhsHead.getQ0CUNO());
      HIHED.setCUOR().move(inAddWhsHead.getQ0CUOR());
      HIHED.setSUDO().move(inAddWhsHead.getQ0SUDO());
      if (!MICommon.toNumericDate(inAddWhsHead.getQ0DNDT())) {
         MICommon.setError("DNDT");
         return;
      }
      HIHED.setDNDT(MICommon.getNumericDate());
      X0FLDD = 6;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWhsHead.getQ0DNTM());
      RNUM();
      HIHED.setDNTM((int)this.PXNUM);
      if (UTCmode) {
         // Reporting date
         if (!inAddWhsHead.getQ0DNDT().isBlank() &&
             !inAddWhsHead.getQ0DNTM().isBlank()) {
            date = HIHED.getDNDT(); 
            time = HIHED.getDNTM() / 100;           
            if (!convertFromUTC()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("DNDT","S_00148");
               return;
            }
            HIHED.setDNDT(UTCdate);
            HIHED.setDNTM(UTCtime * 100);
         }
      }
      HIHED.setPUSN().move(inAddWhsHead.getQ0PUSN());
      X0FLDD = 9;
      X0DCCD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWhsHead.getQ0GRWE());
      RNUM();
      HIHED.setGRWE(this.PXNUM);
      X0FLDD = 9;
      X0DCCD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWhsHead.getQ0VOL3());
      RNUM();
      HIHED.setVOL3(this.PXNUM);
      HIHED.setSUNO().move(inAddWhsHead.getQ0SUNO());
      X0FLDD = 1;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWhsHead.getQ0SUTY().getChar());
      RNUM();
      HIHED.setSUTY((int)this.PXNUM);
      HIHED.setSORN().move(inAddWhsHead.getQ0SORN());
      HIHED.setDPNR().move(inAddWhsHead.getQ0DPNR());
      HIHED.setRSAG().move(inAddWhsHead.getQ0RSAG());

      if (!MICommon.toNumericDate(inAddWhsHead.getQ0ARDT())) {
         MICommon.setError("ARDT");
         return;
      }
      HIHED.setARDT(MICommon.getNumericDate());

      /*
      * If timezone is entered we shall treat DLDT/DLTM and SHD4/SHTM special.
      */
      if(inAddWhsHead.getQ0TIZO().isBlank()) {
         if (!MICommon.toNumericDate(inAddWhsHead.getQ0DLDT())) {
            MICommon.setError("DLDT");
            return;
         }
         HIHED.setDLDT(MICommon.getNumericDate());

         X0FLDD = 6;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inAddWhsHead.getQ0DLTM());
         RNUM();
         HIHED.setDLTM((int)this.PXNUM);

         if (!MICommon.toNumericDate(inAddWhsHead.getQ0SHD4())) {
            MICommon.setError("SHD4");
            return;
         }
         HIHED.setSHD4(MICommon.getNumericDate());

         X0FLDD = 6;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inAddWhsHead.getQ0SHTM());
         RNUM();
         HIHED.setSHTM((int)this.PXNUM);

      } else { // Timezone handling
         // Check so entered timezone exists
         ITZON.setCONO(LDAZD.CONO);
         ITZON.setTIZO().moveLeftPad(inAddWhsHead.getQ0TIZO());
         if(!ITZON.EXISTS("00", ITZON.getKey("00", 2))) {
            MICommon.setError( "TIZO", "WO98603", inAddWhsHead.getQ0TIZO());
            return;
         }

         // Fields DLDT/DLTM
         PXMNGTZN.clear();
         if (!MICommon.toNumericDate(inAddWhsHead.getQ0DLDT())) {
            MICommon.setError("DLDT");
            return;
         }
         PXMNGTZN.PXDAIN = MICommon.getNumericDate();

         if(!MICommon.toNumeric(inAddWhsHead.getQ0DLTM())) {
            //   MSGID=WRD1901 Received Time &1 is invalid
            MICommon.setError( "DLTM", "WRD1901", inAddWhsHead.getQ0DLTM());
            return;
         }
         PXMNGTZN.PXTIMI = MICommon.getInt() / 100; // Dont want seconds.
         // Convert from the entered timezone to the timezone of the entered warehouse.
         PXMNGTZN.PXTZNI.move(inAddWhsHead.getQ0TIZO());
         PXMNGTZN.PXE0TB.moveLeftPad("WHLO");
         PXMNGTZN.PXE0IA.moveLeftPad(inAddWhsHead.getQ0WHLO());

         PXENV.move(toChar(true));
         PXOPC.moveLeftPad("*CNV");
         PXMNGTZN.APIDS.move(APIDS);
         PXMNGTZN.DRMNGTZN();
         APIDS.move(PXMNGTZN.APIDS);
         if (PXIN60.getChar() == toChar(false)) {
            HIHED.setDLDT(PXMNGTZN.PXDAOU);
            HIHED.setDLTM(PXMNGTZN.PXTIMO * 100);
         } else {
            MICommon.setError( "TIZO", PXMSID.toString(), inAddWhsHead.getQ0DLDT());
            return;
         }

         // Fields SHD4/SHTM
         PXMNGTZN.clear();
         if (!MICommon.toNumericDate(inAddWhsHead.getQ0SHD4())) {
            MICommon.setError("SHD4");
            return;
         }
         PXMNGTZN.PXDAIN = MICommon.getNumericDate();

         if(!MICommon.toNumeric(inAddWhsHead.getQ0SHTM())) {
            //   MSGID=WRD1901 Received Time &1 is invalid
            MICommon.setError( "SHTM", "WRD1901", inAddWhsHead.getQ0SHTM());
            return;
         }
         PXMNGTZN.PXTIMI = MICommon.getInt() / 100;
         // Convert from the entered timezone to the timezone of the entered warehouse.
         PXMNGTZN.PXTZNI.move(inAddWhsHead.getQ0TIZO());
         PXMNGTZN.PXE0TB.moveLeftPad("WHLO");
         PXMNGTZN.PXE0IA.moveLeftPad(inAddWhsHead.getQ0WHLO());

         PXENV.move(toChar(true));
         PXOPC.moveLeftPad("*CNV");
         PXMNGTZN.APIDS.move(APIDS);
         PXMNGTZN.DRMNGTZN();
         APIDS.move(PXMNGTZN.APIDS);
         if (PXIN60.getChar() == toChar(false)) {
            HIHED.setSHD4(PXMNGTZN.PXDAOU);
            HIHED.setSHTM(PXMNGTZN.PXTIMO * 100);
         } else {
            MICommon.setError( "TIZO", PXMSID.toString(), inAddWhsHead.getQ0SHD4());
            return;
         }
      }

      if (!MICommon.toNumericDate(inAddWhsHead.getQ0RCDT())) {
         MICommon.setError("RCDT");
         return;
      }
      HIHED.setRCDT(MICommon.getNumericDate());

      X0FLDD = 6;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWhsHead.getQ0RCTM());
      RNUM();
      HIHED.setRCTM((int)this.PXNUM);
      HIHED.setMODL().move(inAddWhsHead.getQ0MODL());
      HIHED.setE0B4().move(inAddWhsHead.getQ0E0B4());
      HIHED.setE0BH().move(inAddWhsHead.getQ0E0BH());
      HIHED.setE0B5().move(inAddWhsHead.getQ0E0B5());
      HIHED.setTEDL().move(inAddWhsHead.getQ0TEDL());
      HIHED.setYREF().move(inAddWhsHead.getQ0YREF());
      HIHED.setBOLN().move(inAddWhsHead.getQ0BOLN());
      HIHED.setFWRF().move(inAddWhsHead.getQ0FWRF());
      HIHED.setTFNO().move(inAddWhsHead.getQ0TFNO());
      HIHED.setVRNO().move(inAddWhsHead.getQ0VRNO());
      HIHED.setRESP().move(inAddWhsHead.getQ0RESP());
      X0FLDD = 13;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWhsHead.getQ0DTID());
      RNUM();
      HIHED.setDTID((long)this.PXNUM);
      X0FLDD = 13;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWhsHead.getQ0TXID());
      RNUM();
      HIHED.setTXID((long)this.PXNUM);
      HIHED.setUSD1().move(inAddWhsHead.getQ0USD1());
      HIHED.setUSD2().move(inAddWhsHead.getQ0USD2());
      HIHED.setUSD3().move(inAddWhsHead.getQ0USD3());
      HIHED.setUSD4().move(inAddWhsHead.getQ0USD4());
      HIHED.setUSD5().move(inAddWhsHead.getQ0USD5());
      HIHED.setRGDT(movexDate());
      HIHED.setRGTM(movexTime());
      HIHED.setLMDT(movexDate());
      HIHED.setCHNO(1);
      HIHED.setCHID().move(DSUSS);
      RHEAD();
      if (IN60) {
         return;
      }
      outAddWhsHead.setY0CONO().move(XXCONO);
      outAddWhsHead.setY0MSGN().move(HIHED.getMSGN());
      MICommon.setData( outAddWhsHead.get());
   }

   public void RCOM12() {
      // ----------------------------------------------------------------
      //   Clear transaction records
      // ----------------------------------------------------------------
      sMHS850MIRAddWhsLine inAddWhsLine = (sMHS850MIRAddWhsLine)MICommon.getInDS(sMHS850MIRAddWhsLine.class);
      sMHS850MISAddWhsLine outAddWhsLine = (sMHS850MISAddWhsLine)MICommon.getOutDS(sMHS850MISAddWhsLine.class);

      HIHED.clear();
      HIPAC.clear();
      HILIN.clear();
      // ----------------------------------------------------------------
      //   Default process option.
      // ----------------------------------------------------------------
      XXPRFL.move("*CHK");
      XXOPC.move("*ADD");
      if (MICommon.toNumericCompany(inAddWhsLine.getQ2CONO())) {
         XXCONO = MICommon.getInt();
      } else {
         MICommon.setError("CONO");
         return;
      }

      LDAZD.CONO = XXCONO;
      //-----------------------------------
      //    UTC mode
      UTCmode = isUTCmode(inAddWhsLine.getQ2UTCM());
      if (IN60) {
         MICommon.setError("UTCM", "WUTCM01", inAddWhsLine.getQ2UTCM());
         return;
      }
      // ----------------------------------------------------------------
      HIHED.setCONO(LDAZD.CONO);
      HIHED.setMSGN().move(inAddWhsLine.getQ2MSGN());
      rAPIDS.clear();
      X0OPC.moveLeftPad("*GET");
      rPHEDPIpreCall();
      apCall("MHIHEDPI", rPHEDPI);
      rPHEDPIpostCall();
      IN60 = toBoolean(X0IN60.getChar());
      if (IN60) {
         MICommon.setError( "", X0MSID.toString(), X0MSGD);
         return;
      }
      HIPAC.setCONO(LDAZD.CONO);
      HIPAC.setMSGN().move(inAddWhsLine.getQ2MSGN());
      HIPAC.setPACN().move(inAddWhsLine.getQ2PACN());
      rAPIDS.clear();
      X0OPC.moveLeftPad("*GET");
      rPPACPIpreCall();
      apCall("MHIPACPI", rPPACPI);
      rPPACPIpostCall();
      IN60 = toBoolean(X0IN60.getChar());
      if (IN60) {
         MICommon.setError( "", X0MSID.toString(), X0MSGD);
         return;
      }
      //   Retreive next line in sequence
      HILIN.setCONO(LDAZD.CONO);
      HILIN.setMSGN().move(inAddWhsLine.getQ2MSGN());
      HILIN.setPACN().move(inAddWhsLine.getQ2PACN());
      HILIN.setMSLN(0);
      //   Retreive facility for warehouse
      ITWHL.setCONO(LDAZD.CONO);
      ITWHL.setWHLO().move(inAddWhsLine.getQ2WHLO());
      if (!ITWHL.CHAIN("00", ITWHL.getKey("00"))) {
         //   MSGID=WWH0103 Warehouse &1 does not exist
         MICommon.setError( "WHLO", "WWH0103", inAddWhsLine.getQ2WHLO());
         return;
      } else {
         HILIN.setFACI().move(ITWHL.getFACI());
      }
      if (UTCmode) {
         //   Determine time zone for Warehouse
         XXWHLO.moveLeftPad(inAddWhsLine.getQ2WHLO());
         if (!CRCalendar.retrieveTIZOForWHLO(PXMNGTZN, LDAZD.CONO, XXWHLO)) {
            IN60 = true;
            //   MSGID=S_00148 UTC time conversion failed.
            MICommon.setError("GEDT","S_00148");
            return;
         }
         //   time zone for warehouse
         TIZOforWHLO.move(CRCalendar.getTimeZone());
      }
      //   Format date/time
      XXGEDT = movexDate();
      XXGETM = movexTime();
      if (inAddWhsLine.getQ2CHGD().isBlank()) {
         XXCHGD = movexDate();
      } else {
         if (!MICommon.toNumericDate(inAddWhsLine.getQ2CHGD())) {
            MICommon.setError("CHGD");
            return;
         }
         XXCHGD = MICommon.getNumericDate();
      }
      // ----------------------------------------------------------------
      //   Move data from the transaction input structure (MHS850MI1)
      //   to the execution data formats (MHIHED/MHIPAC/MHILIN)
      // ----------------------------------------------------------------
      HILIN.setCONO(LDAZD.CONO);
      HILIN.setDIVI().move(inAddWhsLine.getQ2DIVI());
      HILIN.setWHLO().move(inAddWhsLine.getQ2WHLO());
      HILIN.setMSGN().move(inAddWhsLine.getQ2MSGN());
      HILIN.setPACN().move(inAddWhsLine.getQ2PACN());
      HILIN.setQLFR().move(inAddWhsLine.getQ2QLFR());
      HILIN.setQLFS(inAddWhsLine.getQ2QLFS().getInt());
      HILIN.setFACI().move(inAddWhsLine.getQ2FACI());
      HILIN.setGEDT(XXGEDT);
      HILIN.setGETM(XXGETM);
      HILIN.setCHGD(XXCHGD);
      HILIN.setSTAT().move(inAddWhsLine.getQ2STAT());
      HILIN.setITNO().move(inAddWhsLine.getQ2ITNO());
      XXITNO.moveLeftPad(HILIN.setITNO());
      RITNO();
      //--------------------------------
      HILIN.setITDS().move(inAddWhsLine.getQ2ITDS());
      HILIN.setWHSL().move(inAddWhsLine.getQ2WHSL());
      HILIN.setTWSL().move(inAddWhsLine.getQ2TWSL());
      inAddWhsLine.getQ2BANO().toUpperCase();
      HILIN.setBANO().move(inAddWhsLine.getQ2BANO());
      HILIN.setCAMU().move(inAddWhsLine.getQ2CAMU());
      X0FLDD = 10;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWhsLine.getQ2REPN());
      RNUM();
      HILIN.setREPN((long)this.PXNUM);
      X0FLDD = 5;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWhsLine.getQ2RELI());
      RNUM();
      HILIN.setRELI((int)this.PXNUM);
      HILIN.setPOPN().move(inAddWhsLine.getQ2POPN());
      HILIN.setALWQ().move(inAddWhsLine.getQ2ALWQ());
      X0FLDD = 2;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWhsLine.getQ2ALWT());
      RNUM();
      HILIN.setALWT((int)this.PXNUM);
      XXQTYN = 0d;
      XXQTYA.clear();
      XXQTYA.moveLeft(inAddWhsLine.getQ2DLQT());
      this.PXDCCD = X1DCCD;
      RQTY();
      HILIN.setDLQT(this.PXNUM);
      HILIN.setUNIT().move(inAddWhsLine.getQ2UNIT());
      XXQTYN = 0d;
      XXQTYA.clear();
      XXQTYA.moveLeft(inAddWhsLine.getQ2DLQA());
      this.PXDCCD = X1DCCD;
      RQTY();
      HILIN.setDLQA(this.PXNUM);
      HILIN.setALUN().move(inAddWhsLine.getQ2ALUN());
      X0FLDD = 9;
      X0DCCD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWhsLine.getQ2VOL3());
      RNUM();
      HILIN.setVOL3(this.PXNUM);
      X0FLDD = 9;
      X0DCCD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWhsLine.getQ2NEWE());
      RNUM();
      HILIN.setNEWE(this.PXNUM);
      X0FLDD = 9;
      X0DCCD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWhsLine.getQ2GRWE());
      RNUM();
      HILIN.setGRWE(this.PXNUM);
      XXQTYN = 0d;
      XXQTYA.clear();
      XXQTYA.moveLeft(inAddWhsLine.getQ2D1QT());
      this.PXDCCD = X1DCCD;
      RQTY();
      HILIN.setD1QT(this.PXNUM);
      HILIN.setDLSP().move(inAddWhsLine.getQ2DLSP());
      HILIN.setPUUN().move(inAddWhsLine.getQ2PUUN());
      XXQTYN = 0d;
      XXQTYA.clear();
      XXQTYA.moveLeft(inAddWhsLine.getQ2RVQA());
      this.PXDCCD = X1DCCD;
      if (HILIN.getQLFR().EQ("10  ")) {
         if (HILIN.getPUUN().NE(ITMAS.getUNMS())) {
            if (cRefALUNext.getMITAUN(ITAUN, false, LDAZD.CONO, HILIN.setITNO(), 1, HILIN.getPUUN())) {
               this.PXDCCD = ITAUN.getDCCD();
            }
         }
      }
      RQTY();
      HILIN.setRVQA(this.PXNUM);
      XXQTYN = 0d;
      XXQTYA.clear();
      XXQTYA.moveLeft(inAddWhsLine.getQ2ALQT());
      this.PXDCCD = X1DCCD;
      RQTY();
      HILIN.setALQT(this.PXNUM);
      XXQTYN = 0d;
      XXQTYA.clear();
      XXQTYA.moveLeft(inAddWhsLine.getQ2PAQT());
      this.PXDCCD = X1DCCD;
      RQTY();
      HILIN.setPAQT(this.PXNUM);
      XXQTYN = 0d;
      XXQTYA.clear();
      XXQTYA.moveLeft(inAddWhsLine.getQ2CAWE());
      this.PXDCCD = X2DCCD;
      RQTY();
      HILIN.setCAWE(this.PXNUM);
      X0FLDD = 5;
      X0DCCD = 2;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWhsLine.getQ2POCY());
      RNUM();
      HILIN.setPOCY(this.PXNUM);
      X0FLDD = 1;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWhsLine.getQ2OEND().getChar());
      RNUM();
      HILIN.setOEND((int)this.PXNUM);
      HILIN.setBREF().move(inAddWhsLine.getQ2BREF());
      HILIN.setBRE2().move(inAddWhsLine.getQ2BRE2());
      HILIN.setFLOC().move(inAddWhsLine.getQ2FLOC());
      HILIN.setORCA().move(inAddWhsLine.getQ2ORCA());
      X0FLDD = 2;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWhsLine.getQ2TTYP());
      RNUM();
      HILIN.setTTYP((int)this.PXNUM);
      HILIN.setRIDN().move(inAddWhsLine.getQ2RIDN());
      X0FLDD = 6;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWhsLine.getQ2RIDO());
      RNUM();
      HILIN.setRIDO((int)this.PXNUM);
      X0FLDD = 6;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWhsLine.getQ2RIDL());
      RNUM();
      HILIN.setRIDL((int)this.PXNUM);
      X0FLDD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWhsLine.getQ2RIDX());
      RNUM();
      HILIN.setRIDX((int)this.PXNUM);
      X0FLDD = 11;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWhsLine.getQ2RIDI());
      RNUM();
      HILIN.setRIDI((long)this.PXNUM);
      X0FLDD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWhsLine.getQ2PLSX());
      RNUM();
      HILIN.setPLSX((int)this.PXNUM);
      X0FLDD = 11;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWhsLine.getQ2DLIX());
      RNUM();
      HILIN.setDLIX((long)this.PXNUM);
      if (!inAddWhsLine.getQ2PLRN().isBlank()) {
         X0FLDD = 11;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inAddWhsLine.getQ2PLRN());
         RNUM();
         HILIN.setPLRN((long)this.PXNUM);
      }
      //   Format reporting date
      if (!inAddWhsLine.getQ2RPDT().isBlank()) {
         if (!MICommon.toNumericDate(inAddWhsLine.getQ2RPDT())) {
            MICommon.setError("RPDT");
            return;
         }
         HILIN.setRPDT(MICommon.getNumericDate());
      }
      HILIN.setDNNO().move(inAddWhsLine.getQ2DNNO());
      HILIN.setCUOR().move(inAddWhsLine.getQ2CUOR());
      HILIN.setCUNO().move(inAddWhsLine.getQ2CUNO());
      HILIN.setADID().move(inAddWhsLine.getQ2ADID());
      HILIN.setSUNO().move(inAddWhsLine.getQ2SUNO());
      X0FLDD = 1;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWhsLine.getQ2SUTY().getChar());
      RNUM();
      HILIN.setSUTY((int)this.PXNUM);
      HILIN.setSUDO().move(inAddWhsLine.getQ2SUDO());

      if (!MICommon.toNumericDate(inAddWhsLine.getQ2DNDT())) {
         MICommon.setError("DNDT");
         return;
      }
      HILIN.setDNDT(MICommon.getNumericDate());

      if (!MICommon.toNumericDate(inAddWhsLine.getQ2EXPI())) {
         MICommon.setError("EXPI");
         return;
      }
      HILIN.setEXPI(MICommon.getNumericDate());

      if (!MICommon.toNumericDate(inAddWhsLine.getQ2CNDT())) {
         MICommon.setError("CNDT");
         return;
      }
      HILIN.setCNDT(MICommon.getNumericDate());

      if (!MICommon.toNumericDate(inAddWhsLine.getQ2SEDT())) {
         MICommon.setError("SEDT");
         return;
      }
      HILIN.setSEDT(MICommon.getNumericDate());

      HILIN.setQCRA(inAddWhsLine.getQ2QCRA().getChar());
      HILIN.setSCRE().move(inAddWhsLine.getQ2SCRE());
      // Get TRTP of an RO
      if (inAddWhsLine.getQ2TTYP().EQ("41") || HILIN.getQLFR().EQ("41  ")) {
         GHEAD.setCONO(LDAZD.CONO);
         GHEAD.setTRNR().move(inAddWhsLine.getQ2RIDN());
         if (GHEAD.CHAIN("00", GHEAD.getKey("00"))) {
            HILIN.setTRTP().move(GHEAD.getTRTP());
         }
      } else {
         HILIN.setTRTP().move(inAddWhsLine.getQ2TRTP());
      }  
      HILIN.setBREM().move(inAddWhsLine.getQ2BREM());
      HILIN.setSITE().move(inAddWhsLine.getQ2SITE());
      HILIN.setSITD().move(inAddWhsLine.getQ2SITD());
      HILIN.setPUPR(inAddWhsLine.setQ2PUPR().getDouble(17, 6));
      HILIN.setPPUN().move(inAddWhsLine.getQ2PPUN());
      HILIN.setCUCD().move(inAddWhsLine.getQ2CUCD());
      X0FLDD = 5;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWhsLine.getQ2PUCD());
      RNUM();
      HILIN.setPUCD((int)this.PXNUM);
      X0FLDD = 15;
      X0DCCD = 2;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWhsLine.getQ2LNAM());
      RNUM();
      HILIN.setLNAM(this.PXNUM);
      X0FLDD = 2;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWhsLine.getQ2VTCD());
      RNUM();
      HILIN.setVTCD((int)this.PXNUM);
      HILIN.setAGNB().move(inAddWhsLine.getQ2AGNB());
      HILIN.setRESP().move(inAddWhsLine.getQ2RESP());
      X0FLDD = 13;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWhsLine.getQ2DTID());
      RNUM();
      HILIN.setDTID((long)this.PXNUM);
      X0FLDD = 13;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWhsLine.getQ2TXID());
      RNUM();
      HILIN.setTXID((long)this.PXNUM);
      HILIN.setUSD1().move(inAddWhsLine.getQ2USD1());
      HILIN.setUSD2().move(inAddWhsLine.getQ2USD2());
      HILIN.setUSD3().move(inAddWhsLine.getQ2USD3());
      HILIN.setUSD4().move(inAddWhsLine.getQ2USD4());
      if (!inAddWhsLine.getQ2USD5().isBlank()) {
         X0FLDD = 6;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inAddWhsLine.getQ2USD5());
         RNUM();
         HILIN.setRPTM((int)this.PXNUM);
      }
      if (!UTCmode && 
         !inAddWhsLine.getQ2RPDT().isBlank() &&
            inAddWhsLine.getQ2USD5().isBlank()) {
         HILIN.setRPTM(movexTime());
      }
      if (UTCmode) {
         // Reporting date
         if (!inAddWhsLine.getQ2RPDT().isBlank() &&
             !inAddWhsLine.getQ2USD5().isBlank()) {
            date = HILIN.getRPDT(); 
            time = HILIN.getRPTM() / 100;           
            if (!convertFromUTC()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("RPDT","S_00148");
               return;
            }
            HILIN.setRPDT(UTCdate);
            HILIN.setRPTM(UTCtime * 100);
         } else {
            if (inAddWhsLine.getQ2RPDT().isBlank() ||
               inAddWhsLine.getQ2USD5().isBlank()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("RPDT","S_00148");
               return;
            }
         }
      }
      HILIN.setRSCD().move(inAddWhsLine.getQ2RSCD());

      if (!MICommon.toNumericDate(inAddWhsLine.getQ2MFDT())) {
         MICommon.setError("MFDT");
         return;
      }
      HILIN.setMFDT(MICommon.getNumericDate());
      //-----------------------------------
      X0FLDD = 1;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWhsLine.getQ2ISMD().getChar());
      RNUM();
      HILIN.setISMD((int)this.PXNUM);
      HILIN.setRGDT(movexDate());
      HILIN.setRGTM(movexTime());
      HILIN.setLMDT(movexDate());
      HILIN.setCHNO(1);
      HILIN.setCHID().move(DSUSS);
      if (IN75) {
         IN60 = true;
         return;
      }
      //-----------------------------------------------
      RLINE();
      if (IN60) {
         return;
      }

      outAddWhsLine.setY2CONO().move(XXCONO);
      outAddWhsLine.setY2MSGN().move(HIHED.getMSGN());
      outAddWhsLine.setY2PACN().move(HILIN.getPACN());
      X0FLDD = 5;
      XXNUMN = (double)HILIN.getMSLN();
      XXNUMA.clear();
      RNUM();
      MvxString MSLN = new MvxString(5);
      MSLN.moveRight(this.PXALPH);
      MSLN.replace(' ', '0');
      outAddWhsLine.setY2MSLN().move(MSLN);

      MICommon.setData( outAddWhsLine.get());
   }

   public void RCOM13() {
      // ----------------------------------------------------------------
      //   Clear transaction records
      // ----------------------------------------------------------------
      sMHS850MIRAddWhsPack inAddWhsPack = (sMHS850MIRAddWhsPack)MICommon.getInDS(sMHS850MIRAddWhsPack.class);
      sMHS850MISAddWhsPack outAddWhsPack = (sMHS850MISAddWhsPack)MICommon.getOutDS(sMHS850MISAddWhsPack.class);

      HIHED.clear();
      HIPAC.clear();
      HILIN.clear();
      // ----------------------------------------------------------------
      //   Default process option.
      // ----------------------------------------------------------------
      XXPRFL.move("*CHK");
      XXOPC.move("*ADD");
      if (MICommon.toNumericCompany(inAddWhsPack.getQ1CONO())) {
         XXCONO = MICommon.getInt();
      } else {
         MICommon.setError("CONO");
         return;
      }

      LDAZD.CONO = XXCONO;
      //-----------------------------------
      //    UTC mode
      UTCmode = isUTCmode(inAddWhsPack.getQ1UTCM());
      if (IN60) {
         MICommon.setError("UTCM", "WUTCM01", inAddWhsPack.getQ1UTCM());
         return;
      }
      // ----------------------------------------------------------------
      HIHED.setCONO(LDAZD.CONO);
      HIHED.setMSGN().move(inAddWhsPack.getQ1MSGN());
      rAPIDS.clear();
      X0OPC.moveLeftPad("*GET");
      rPHEDPIpreCall();
      apCall("MHIHEDPI", rPHEDPI);
      rPHEDPIpostCall();
      IN60 = toBoolean(X0IN60.getChar());
      if (IN60) {
         MICommon.setError( "", X0MSID.toString(), X0MSGD);
         return;
      }
      //   Default package number if blank
      if (inAddWhsPack.getQ1PACN().isBlank()) {
         inAddWhsPack.setQ1PACN().moveLeft(HIPAC.getMSGN());
      }
      if (UTCmode) {
         //   Determine time zone for Warehouse
         XXWHLO.moveLeftPad(inAddWhsPack.getQ1WHLO());
         if (!CRCalendar.retrieveTIZOForWHLO(PXMNGTZN, LDAZD.CONO, XXWHLO)) {
            IN60 = true;
            //   MSGID=S_00148 UTC time conversion failed.
            MICommon.setError("GEDT","S_00148");
            return;
         }
         //   time zone for warehouse
         TIZOforWHLO.move(CRCalendar.getTimeZone());
      }
      //   Format date/time
      if (inAddWhsPack.getQ1GEDT().isBlank() ||
            inAddWhsPack.getQ1GETM().isBlank()) {
         XXGEDT = movexDate();
         XXGETM = movexTime();
      } else {
         if (!MICommon.toNumericDate(inAddWhsPack.getQ1GEDT())) {
            MICommon.setError("GEDT");
            return;
         }
         XXGEDT = MICommon.getNumericDate();

         if (!MICommon.toNumeric(inAddWhsPack.getQ1GETM())) {
            MICommon.setError("GETM");
            return;
         }
         XXGETM = MICommon.getInt();
         // Date generated
         date = XXGEDT; 
         time = XXGETM / 100;
         if (UTCmode && date > 0) {
            if (!convertFromUTC()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("GEDT","S_00148");
               return;
            }
            XXGEDT = UTCdate;
            XXGETM = UTCtime * 100;
         }
      }

      if (inAddWhsPack.getQ1CHGD().isBlank()) {
         XXCHGD = movexDate();
      } else {
         if (!MICommon.toNumericDate(inAddWhsPack.getQ1CHGD())) {
            MICommon.setError("CHGD");
            return;
         }
         XXCHGD = MICommon.getNumericDate();
      }
      // ----------------------------------------------------------------
      //   Move data from the transaction input structure (MHS850MI1)
      //   to the execution data formats (MHIHED/MHIPAC/MHILIN)
      // ----------------------------------------------------------------
      HIPAC.setCONO(LDAZD.CONO);
      HIPAC.setDIVI().move(inAddWhsPack.getQ1DIVI());
      HIPAC.setWHLO().move(inAddWhsPack.getQ1WHLO());
      HIPAC.setMSGN().move(inAddWhsPack.getQ1MSGN());
      HIPAC.setPACN().move(inAddWhsPack.getQ1PACN());
      HIPAC.setQLFR().move(inAddWhsPack.getQ1QLFR());
      if (inAddWhsPack.setQ1QLFR().NE("29  ")) {
      }
      //---------------------------------------
      HIPAC.setGEDT(XXGEDT);
      HIPAC.setGETM(XXGETM);
      HIPAC.setCHGD(XXCHGD);
      HIPAC.setPARE().move(inAddWhsPack.getQ1PARE());
      X0FLDD = 1;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWhsPack.getQ1DIPA().getChar());
      RNUM();
      HIPAC.setDIPA((int)this.PXNUM);
      HIPAC.setSUDO().move(inAddWhsPack.getQ1SUDO());
      HIPAC.setDNNO().move(inAddWhsPack.getQ1DNNO());

      if (!MICommon.toNumericDate(inAddWhsPack.getQ1DNDT())) {
         MICommon.setError("DNDT");
         return;
      }
      HIPAC.setDNDT(MICommon.getNumericDate());

      HIPAC.setSTAT().move(inAddWhsPack.getQ1STAT());
      HIPAC.setTRSL().move(inAddWhsPack.getQ1TRSL());
      HIPAC.setTRSH().move(inAddWhsPack.getQ1TRSH());
      X0FLDD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWhsPack.getQ1PACO());
      RNUM();
      HIPAC.setPACO((int)this.PXNUM);
      X0FLDD = 1;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWhsPack.getQ1PACU().getChar());
      RNUM();
      HIPAC.setPACU((int)this.PXNUM);
      HIPAC.setPACC().move(inAddWhsPack.getQ1PACC());
      X0FLDD = 7;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWhsPack.getQ1AMKO());
      RNUM();
      HIPAC.setAMKO((int)this.PXNUM);
      HIPAC.setPACT().move(inAddWhsPack.getQ1PACT());
      HIPAC.setPACK().move(inAddWhsPack.getQ1PACK());
      HIPAC.setTEPA().move(inAddWhsPack.getQ1TEPA());
      HIPAC.setSORT().move(inAddWhsPack.getQ1SORT());
      HIPAC.setDLRM().move(inAddWhsPack.getQ1DLRM());
      HIPAC.setDLMO().move(inAddWhsPack.getQ1DLMO());
      HIPAC.setDLSP().move(inAddWhsPack.getQ1DLSP());
      X0FLDD = 9;
      X0DCCD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWhsPack.getQ1VOM3());
      RNUM();
      HIPAC.setVOM3(this.PXNUM);
      X0FLDD = 11;
      X0DCCD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWhsPack.getQ1FRCP());
      RNUM();
      HIPAC.setFRCP(this.PXNUM);
      X0FLDD = 9;
      X0DCCD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWhsPack.getQ1NEWE());
      RNUM();
      HIPAC.setNEWE(this.PXNUM);
      X0FLDD = 9;
      X0DCCD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWhsPack.getQ1GRWE());
      RNUM();
      HIPAC.setGRWE(this.PXNUM);
      X0FLDD = 9;
      X0DCCD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWhsPack.getQ1GRW3());
      RNUM();
      HIPAC.setGRW3(this.PXNUM);
      X0FLDD = 9;
      X0DCCD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWhsPack.getQ1GRW4());
      RNUM();
      HIPAC.setGRW4(this.PXNUM);
      X0FLDD = 9;
      X0DCCD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWhsPack.getQ1GRW5());
      RNUM();
      HIPAC.setGRW5(this.PXNUM);
      XXQTYN = 0d;
      XXQTYA.clear();
      XXQTYA.moveLeft(inAddWhsPack.getQ1D1QT());
      this.PXDCCD = X1DCCD;
      RQTY();
      HIPAC.setD1QT(this.PXNUM);
      X0FLDD = 9;
      X0DCCD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWhsPack.getQ1VOMT());
      RNUM();
      HIPAC.setVOMT(this.PXNUM);
      X0FLDD = 5;
      X0DCCD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWhsPack.getQ1PACL());
      RNUM();
      HIPAC.setPACL(this.PXNUM);
      X0FLDD = 5;
      X0DCCD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWhsPack.getQ1PACW());
      RNUM();
      HIPAC.setPACW(this.PXNUM);
      X0FLDD = 5;
      X0DCCD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWhsPack.getQ1PACH());
      RNUM();
      HIPAC.setPACH(this.PXNUM);
      HIPAC.setSSCC().move(inAddWhsPack.getQ1SSCC());
      HIPAC.setWHSL().move(inAddWhsPack.getQ1WHSL());
      HIPAC.setCUNO().move(inAddWhsPack.getQ1CUNO());
      HIPAC.setADID().move(inAddWhsPack.getQ1ADID());
      HIPAC.setSUNO().move(inAddWhsPack.getQ1SUNO());
      X0FLDD = 1;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWhsPack.getQ1SUTY().getChar());
      RNUM();
      HIPAC.setSUTY((int)this.PXNUM);
      HIPAC.setRESP().move(inAddWhsPack.getQ1RESP());
      X0FLDD = 13;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWhsPack.getQ1DTID());
      RNUM();
      HIPAC.setDTID((long)this.PXNUM);
      X0FLDD = 13;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWhsPack.getQ1TXID());
      RNUM();
      HIPAC.setTXID((long)this.PXNUM);
      HIPAC.setUSD1().move(inAddWhsPack.getQ1USD1());
      HIPAC.setUSD2().move(inAddWhsPack.getQ1USD2());
      HIPAC.setUSD3().move(inAddWhsPack.getQ1USD3());
      HIPAC.setUSD4().move(inAddWhsPack.getQ1USD4());
      HIPAC.setUSD5().move(inAddWhsPack.getQ1USD5());
      X0FLDD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWhsPack.getQ1RODN());
      RNUM();
      HIPAC.setRODN((int)this.PXNUM);
      X0FLDD = 7;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWhsPack.getQ1CONN());
      RNUM();
      HIPAC.setCONN((int)this.PXNUM);
      X0FLDD = 1;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWhsPack.getQ1CDEL().getChar());
      RNUM();
      HIPAC.setCDEL((int)this.PXNUM);
      X0FLDD = 11;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWhsPack.getQ1NDLX());
      RNUM();
      HIPAC.setNDLX((long)this.PXNUM);
      if (!MICommon.toNumericDate(inAddWhsPack.getQ1DSDT())) {
         MICommon.setError("DSDT");
         return;
      }
      HIPAC.setDSDT(MICommon.getNumericDate());
      X0FLDD = 4;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWhsPack.getQ1DSHM());
      RNUM();
      HIPAC.setDSHM((int)this.PXNUM);
      if (UTCmode) {
         // Reporting date
         if (!inAddWhsPack.getQ1DSDT().isBlank() &&
             !inAddWhsPack.getQ1DSHM().isBlank()) {
            date = HIPAC.getDSDT(); 
            time = HIPAC.getDSHM();           
            if (!convertFromUTC()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("DSDT","S_00148");
               return;
            }
            HIPAC.setDSDT(UTCdate);
            HIPAC.setDSHM(UTCtime);
         } else {
            if (inAddWhsPack.getQ1DSDT().isBlank() ||
               inAddWhsPack.getQ1DSHM().isBlank()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("DSDT","S_00148");
               return;
            }
         }
      }
      HIPAC.setROUT().move(inAddWhsPack.getQ1ROUT());
      HIPAC.setPPNB().move(inAddWhsPack.getQ1PPNB());
      HIPAC.setRGDT(movexDate());
      HIPAC.setRGTM(movexTime());
      HIPAC.setLMDT(movexDate());
      HIPAC.setCHNO(1);
      HIPAC.setCHID().move(DSUSS);
      RPACK();
      if (IN60) {
         return;
      }
      //   Format return parameters.
      outAddWhsPack.setY1CONO().move(XXCONO);
      outAddWhsPack.setY1MSGN().move(HIHED.getMSGN());
      outAddWhsPack.setY1PACN().move(HIPAC.getPACN());

      MICommon.setData( outAddWhsPack.get());
   }

   public void RCOM14() {
      sMHS850MIRChgWhsHead inChgWhsHead = (sMHS850MIRChgWhsHead)MICommon.getInDS(sMHS850MIRChgWhsHead.class);

      HIHED.clear();
      HIPAC.clear();
      HILIN.clear();
      // ----------------------------------------------------------------
      //   Default process option.
      // ----------------------------------------------------------------
      XXPRFL.move("*CHK");
      XXOPC.move("*UPD");
      if (MICommon.toNumericCompany(inChgWhsHead.getQ3CONO())) {
         XXCONO = MICommon.getInt();
      } else {
         MICommon.setError("CONO");
         return;
      }
      LDAZD.CONO = XXCONO;
      //-----------------------------------
      //    UTC mode
      UTCmode = isUTCmode(inChgWhsHead.getQ3UTCM());
      if (IN60) {
         MICommon.setError("UTCM", "WUTCM01", inChgWhsHead.getQ3UTCM());
         return;
      }
      // ----------------------------------------------------------------
      //   Retreive original record before testing for new values.
      // ----------------------------------------------------------------
      HIHED.setCONO(LDAZD.CONO);
      HIHED.setMSGN().move(inChgWhsHead.getQ3MSGN());
      rAPIDS.clear();
      X0OPC.moveLeftPad("*GET");
      rPHEDPIpreCall();
      apCall("MHIHEDPI", rPHEDPI);
      rPHEDPIpostCall();
      IN60 = toBoolean(X0IN60.getChar());
      if (IN60) {
         MICommon.setError( "", X0MSID.toString(), X0MSGD);
         return;
      }
      // ----------------------------------------------------------------
      //   Test for validity before performing transaction.
      // ----------------------------------------------------------------
      if (HIHED.getSTAT().GE("90")) {
         //   MSGID=XST0020 Status change are not permitted any longer
         MICommon.setError( "", "XST0020", HIHED.getSTAT());
         return;
      }
      // ----------------------------------------------------------------
      //   Move data from the transaction input structure (MHS850MI1)
      //   to the execution data formats (MHIHED/MHIPAC/MHILIN)
      // ----------------------------------------------------------------
      HIHED.setCONO(LDAZD.CONO);
      if (!inChgWhsHead.getQ3DIVI().isBlank()) {
         HIHED.setDIVI().move(inChgWhsHead.getQ3DIVI());
      }
      if (!inChgWhsHead.getQ3WHLO().isBlank()) {
         HIHED.setWHLO().move(inChgWhsHead.getQ3WHLO());
      }
      if (UTCmode) {
         //   Determine time zone for Warehouse
         XXWHLO.moveLeftPad(HIHED.getWHLO());
         if (!CRCalendar.retrieveTIZOForWHLO(PXMNGTZN, LDAZD.CONO, XXWHLO)) {
            IN60 = true;
            //   MSGID=S_00148 UTC time conversion failed.
            MICommon.setError("GEDT","S_00148");
            return;
         }
         //   time zone for warehouse
         TIZOforWHLO.move(CRCalendar.getTimeZone());
      }
      if (!inChgWhsHead.getQ3MSGN().isBlank()) {
         HIHED.setMSGN().move(inChgWhsHead.getQ3MSGN());
      }
      if (inChgWhsHead.getQ3E0IO().getChar() != ' ') {
         HIHED.setE0IO(inChgWhsHead.getQ3E0IO().getChar());
      }
      if (!inChgWhsHead.getQ3QLFR().isBlank()) {
         HIHED.setQLFR().move(inChgWhsHead.getQ3QLFR());
      }
      if (!inChgWhsHead.getQ3FACI().isBlank()) {
      }
      if (!inChgWhsHead.getQ3GEDT().isBlank()) {
         if (!MICommon.toNumericDate(inChgWhsHead.getQ3GEDT())) {
            MICommon.setError("GEDT");
            return;
         }
         HIHED.setGEDT(MICommon.getNumericDate());
      }

      if (!inChgWhsHead.getQ3GETM().isBlank()) {
         X0FLDD = 6;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsHead.getQ3GETM());
         RNUM();
         HIHED.setGETM((int)this.PXNUM);
      }
      // Date generated
      if (!inChgWhsHead.getQ3GEDT().isBlank() && !inChgWhsHead.getQ3GETM().isBlank()) {
         date = HIHED.getGEDT(); 
         time = HIHED.getGETM() / 100;
         if (UTCmode && date > 0) {
            if (!convertFromUTC()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("GEDT","S_00148");
               return;
            }
            HIHED.setGEDT(UTCdate);
            HIHED.setGETM(UTCtime * 100);
         }
      }
      if (!inChgWhsHead.getQ3CHGD().isBlank()) {
         if (!MICommon.toNumericDate(inChgWhsHead.getQ3CHGD())) {
            MICommon.setError("GEDT");
            return;
         }
         HIHED.setCHGD(MICommon.getNumericDate());
      }
      if (!inChgWhsHead.getQ3STAT().isBlank()) {
         HIHED.setSTAT().move(inChgWhsHead.getQ3STAT());
      }
      if (!inChgWhsHead.getQ3TRSL().isBlank()) {
         HIHED.setTRSL().move(inChgWhsHead.getQ3TRSL());
      }
      if (!inChgWhsHead.getQ3TRSH().isBlank()) {
         HIHED.setTRSH().move(inChgWhsHead.getQ3TRSH());
      }
      if (inChgWhsHead.getQ3ADTY().getChar() != ' ') {
         X0FLDD = 1;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsHead.getQ3ADTY().getChar());
         RNUM();
         HIHED.setADTY((int)this.PXNUM);
      }
      if (!inChgWhsHead.getQ3ADID().isBlank()) {
         HIHED.setADID().move(inChgWhsHead.getQ3ADID());
      }
      if (!inChgWhsHead.getQ3PMSN().isBlank()) {
         HIHED.setPMSN().move(inChgWhsHead.getQ3PMSN());
      }
      if (!inChgWhsHead.getQ3E0PA().isBlank()) {
         HIHED.setE0PA().move(inChgWhsHead.getQ3E0PA());
      }
      if (!inChgWhsHead.getQ3E0QA().isBlank()) {
         HIHED.setE0QA().move(inChgWhsHead.getQ3E0QA());
      }
      if (!inChgWhsHead.getQ3E0PB().isBlank()) {
         HIHED.setE0PB().move(inChgWhsHead.getQ3E0PB());
      }
      if (!inChgWhsHead.getQ3E0QB().isBlank()) {
         HIHED.setE0QB().move(inChgWhsHead.getQ3E0QB());
      }
      if (!inChgWhsHead.getQ3DONR().isBlank()) {
         HIHED.setDONR().move(inChgWhsHead.getQ3DONR());
      }
      if (!inChgWhsHead.getQ3E007().isBlank()) {
         HIHED.setE007().move(inChgWhsHead.getQ3E007());
      }
      if (!inChgWhsHead.getQ3E014().isBlank()) {
         HIHED.setE014().move(inChgWhsHead.getQ3E014());
      }
      if (!inChgWhsHead.getQ3E026().isBlank()) {
         HIHED.setE026().move(inChgWhsHead.getQ3E026());
      }
      if (inChgWhsHead.getQ3E035().getChar() != ' ') {
         HIHED.setE035(toInt(inChgWhsHead.getQ3E035().getChar()));
      }
      if (!inChgWhsHead.getQ3E065().isBlank()) {
         HIHED.setE065().move(inChgWhsHead.getQ3E065());
      }
      if (!inChgWhsHead.getQ3E052().isBlank()) {
         HIHED.setE052().move(inChgWhsHead.getQ3E052());
      }
      if (!inChgWhsHead.getQ3E054().isBlank()) {
         HIHED.setE054().move(inChgWhsHead.getQ3E054());
      }
      if (!inChgWhsHead.getQ3E051().isBlank()) {
         HIHED.setE051().move(inChgWhsHead.getQ3E051());
      }
      if (!inChgWhsHead.getQ3E057().isBlank()) {
         HIHED.setE057().move(inChgWhsHead.getQ3E057());
      }
      if (!inChgWhsHead.getQ3DLSP().isBlank()) {
         HIHED.setDLSP().move(inChgWhsHead.getQ3DLSP());
      }
      if (!inChgWhsHead.getQ3EDFR().isBlank()) {
         HIHED.setEDFR().move(inChgWhsHead.getQ3EDFR());
      }
      if (!inChgWhsHead.getQ3ORCA().isBlank()) {
         HIHED.setORCA().move(inChgWhsHead.getQ3ORCA());
      }
      if (!inChgWhsHead.getQ3RIDN().isBlank()) {
         HIHED.setRIDN().move(inChgWhsHead.getQ3RIDN());
      }
      if (!inChgWhsHead.getQ3RIDO().isBlank()) {
         X0FLDD = 6;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsHead.getQ3RIDO());
         RNUM();
         HIHED.setRIDO((int)this.PXNUM);
      }
      if (!inChgWhsHead.getQ3RIDI().isBlank()) {
         X0FLDD = 11;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsHead.getQ3RIDI());
         RNUM();
         HIHED.setRIDI((long)this.PXNUM);
      }
      if (!inChgWhsHead.getQ3PLSX().isBlank()) {
         X0FLDD = 3;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsHead.getQ3PLSX());
         RNUM();
         HIHED.setPLSX((int)this.PXNUM);
      }
      if (!inChgWhsHead.getQ3CUNO().isBlank()) {
         HIHED.setCUNO().move(inChgWhsHead.getQ3CUNO());
      }
      if (!inChgWhsHead.getQ3CUOR().isBlank()) {
         HIHED.setCUOR().move(inChgWhsHead.getQ3CUOR());
      }
      if (!inChgWhsHead.getQ3SUDO().isBlank()) {
         HIHED.setSUDO().move(inChgWhsHead.getQ3SUDO());
      }
      if (!inChgWhsHead.getQ3DNDT().isBlank()) {
         if (!MICommon.toNumericDate(inChgWhsHead.getQ3DNDT())) {
            MICommon.setError("DNDT");
            return;
         }
         HIHED.setDNDT(MICommon.getNumericDate());
      }
      if (!inChgWhsHead.getQ3DNTM().isBlank()) {
         X0FLDD = 6;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsHead.getQ3DNTM());
         RNUM();
         HIHED.setDNTM((int)this.PXNUM);
      }
      // Date generated
      if (!inChgWhsHead.getQ3DNDT().isBlank() && !inChgWhsHead.getQ3DNTM().isBlank()) {
         date = HIHED.getDNDT(); 
         time = HIHED.getDNTM() / 100;
         if (UTCmode && date > 0) {
            if (!convertFromUTC()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("GEDT","S_00148");
               return;
            }
            HIHED.setDNDT(UTCdate);
            HIHED.setDNTM(UTCtime * 100);
         }
      }
      if (!inChgWhsHead.getQ3PUSN().isBlank()) {
         HIHED.setPUSN().move(inChgWhsHead.getQ3PUSN());
      }
      if (!inChgWhsHead.getQ3GRWE().isBlank()) {
         X0FLDD = 9;
         X0DCCD = 3;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsHead.getQ3GRWE());
         RNUM();
         HIHED.setGRWE(this.PXNUM);
      }
      if (!inChgWhsHead.getQ3VOL3().isBlank()) {
         X0FLDD = 9;
         X0DCCD = 3;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsHead.getQ3VOL3());
         RNUM();
         HIHED.setVOL3(this.PXNUM);
      }
      if (!inChgWhsHead.getQ3SUNO().isBlank()) {
         HIHED.setSUNO().move(inChgWhsHead.getQ3SUNO());
      }
      if (inChgWhsHead.getQ3SUTY().getChar() != ' ') {
         X0FLDD = 1;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsHead.getQ3SUTY().getChar());
         RNUM();
         HIHED.setSUTY((int)this.PXNUM);
      }
      if (!inChgWhsHead.getQ3SORN().isBlank()) {
         HIHED.setSORN().move(inChgWhsHead.getQ3SORN());
      }
      if (!inChgWhsHead.getQ3DPNR().isBlank()) {
         HIHED.setDPNR().move(inChgWhsHead.getQ3DPNR());
      }
      if (!inChgWhsHead.getQ3RSAG().isBlank()) {
         HIHED.setRSAG().move(inChgWhsHead.getQ3RSAG());
      }
      if (!inChgWhsHead.getQ3DLDT().isBlank()) {
         if (!MICommon.toNumericDate(inChgWhsHead.getQ3DLDT())) {
            MICommon.setError("DLDT");
            return;
         }
         HIHED.setDLDT(MICommon.getNumericDate());
      }
      if (!inChgWhsHead.getQ3DLTM().isBlank()) {
         X0FLDD = 6;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsHead.getQ3DLTM());
         RNUM();
         HIHED.setDLTM((int)this.PXNUM);
      }
      // Date generated
      if (!inChgWhsHead.getQ3DLDT().isBlank() && !inChgWhsHead.getQ3DLTM().isBlank()) {
         date = HIHED.getDLDT(); 
         time = HIHED.getDLTM() / 100;
         if (UTCmode && date > 0) {
            if (!convertFromUTC()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("GEDT","S_00148");
               return;
            }
            HIHED.setDLDT(UTCdate);
            HIHED.setDLTM(UTCtime * 100);
         }
      }
      if (!inChgWhsHead.getQ3ARDT().isBlank()) {
         if (!MICommon.toNumericDate(inChgWhsHead.getQ3ARDT())) {
            MICommon.setError("ARDT");
            return;
         }
         HIHED.setARDT(MICommon.getNumericDate());
      }
      if (!inChgWhsHead.getQ3SHD4().isBlank()) {
         if (!MICommon.toNumericDate(inChgWhsHead.getQ3SHD4())) {
            MICommon.setError("SHD4");
            return;
         }
         HIHED.setSHD4(MICommon.getNumericDate());
      }
      if (!inChgWhsHead.getQ3SHTM().isBlank()) {
         X0FLDD = 6;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsHead.getQ3SHTM());
         RNUM();
         HIHED.setSHTM((int)this.PXNUM);
      }
      // Date generated
      if (!inChgWhsHead.getQ3SHD4().isBlank() && !inChgWhsHead.getQ3SHTM().isBlank()) {
         date = HIHED.getSHD4(); 
         time = HIHED.getSHTM() / 100;
         if (UTCmode && date > 0) {
            if (!convertFromUTC()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("GEDT","S_00148");
               return;
            }
            HIHED.setSHD4(UTCdate);
            HIHED.setSHTM(UTCtime * 100);
         }
      }
      if (!inChgWhsHead.getQ3RCDT().isBlank()) {
         if (!MICommon.toNumericDate(inChgWhsHead.getQ3RCDT())) {
            MICommon.setError("RCDT");
            return;
         }
         HIHED.setRCDT(MICommon.getNumericDate());
      }
      if (!inChgWhsHead.getQ3RCTM().isBlank()) {
         X0FLDD = 6;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsHead.getQ3RCTM());
         RNUM();
         HIHED.setRCTM((int)this.PXNUM);
      }
      // Date generated
      if (!inChgWhsHead.getQ3RCDT().isBlank() && !inChgWhsHead.getQ3RCTM().isBlank()) {
         date = HIHED.getRCDT(); 
         time = HIHED.getRCTM() / 100;
         if (UTCmode && date > 0) {
            if (!convertFromUTC()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("GEDT","S_00148");
               return;
            }
            HIHED.setRCDT(UTCdate);
            HIHED.setRCTM(UTCtime * 100);
         }
      }
      if (!inChgWhsHead.getQ3MODL().isBlank()) {
         HIHED.setMODL().move(inChgWhsHead.getQ3MODL());
      }
      if (!inChgWhsHead.getQ3E0B4().isBlank()) {
         HIHED.setE0B4().move(inChgWhsHead.getQ3E0B4());
      }
      if (!inChgWhsHead.getQ3E0BH().isBlank()) {
         HIHED.setE0BH().move(inChgWhsHead.getQ3E0BH());
      }
      if (!inChgWhsHead.getQ3E0B5().isBlank()) {
         HIHED.setE0B5().move(inChgWhsHead.getQ3E0B5());
      }
      if (!inChgWhsHead.getQ3TEDL().isBlank()) {
         HIHED.setTEDL().move(inChgWhsHead.getQ3TEDL());
      }
      if (!inChgWhsHead.getQ3YREF().isBlank()) {
         HIHED.setYREF().move(inChgWhsHead.getQ3YREF());
      }
      if (!inChgWhsHead.getQ3BOLN().isBlank()) {
         HIHED.setBOLN().move(inChgWhsHead.getQ3BOLN());
      }
      if (!inChgWhsHead.getQ3FWRF().isBlank()) {
         HIHED.setFWRF().move(inChgWhsHead.getQ3FWRF());
      }
      if (!inChgWhsHead.getQ3TFNO().isBlank()) {
         HIHED.setTFNO().move(inChgWhsHead.getQ3TFNO());
      }
      if (!inChgWhsHead.getQ3VRNO().isBlank()) {
         HIHED.setVRNO().move(inChgWhsHead.getQ3VRNO());
      }
      if (!inChgWhsHead.getQ3RESP().isBlank()) {
         HIHED.setRESP().move(inChgWhsHead.getQ3RESP());
      }
      if (!inChgWhsHead.getQ3DTID().isBlank()) {
         X0FLDD = 13;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsHead.getQ3DTID());
         RNUM();
         HIHED.setDTID((long)this.PXNUM);
      }
      if (!inChgWhsHead.getQ3TXID().isBlank()) {
         X0FLDD = 13;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsHead.getQ3TXID());
         RNUM();
         HIHED.setTXID((long)this.PXNUM);
      }
      if (!inChgWhsHead.getQ3USD1().isBlank()) {
         HIHED.setUSD1().move(inChgWhsHead.getQ3USD1());
      }
      if (!inChgWhsHead.getQ3USD2().isBlank()) {
         HIHED.setUSD2().move(inChgWhsHead.getQ3USD2());
      }
      if (!inChgWhsHead.getQ3USD3().isBlank()) {
         HIHED.setUSD3().move(inChgWhsHead.getQ3USD3());
      }
      if (!inChgWhsHead.getQ3USD4().isBlank()) {
         HIHED.setUSD4().move(inChgWhsHead.getQ3USD4());
      }
      if (!inChgWhsHead.getQ3USD5().isBlank()) {
         HIHED.setUSD5().move(inChgWhsHead.getQ3USD5());
      }

      RHEAD();
      if (IN60) {
         return;
      }
   }

   /**
   *    RCOM15 - Execute command - ChgWhsLine
   */
   public void RCOM15() {
      // ----------------------------------------------------------------
      //   Clear transaction records
      // ----------------------------------------------------------------
      sMHS850MIRChgWhsLine inChgWhsLine = (sMHS850MIRChgWhsLine)MICommon.getInDS(sMHS850MIRChgWhsLine.class);

      HIHED.clear();
      HIPAC.clear();
      HILIN.clear();
      // ----------------------------------------------------------------
      //   Default process option.
      // ----------------------------------------------------------------
      XXPRFL.move("*CHK");
      XXOPC.move("*UPD");
      if (MICommon.toNumericCompany(inChgWhsLine.getQ5CONO())) {
         XXCONO = MICommon.getInt();
      } else {
         MICommon.setError("CONO");
         return;
      }
      LDAZD.CONO = XXCONO;
      //-----------------------------------
      //    UTC mode
      UTCmode = isUTCmode(inChgWhsLine.getQ5UTCM());
      if (IN60) {
         MICommon.setError("UTCM", "WUTCM01", inChgWhsLine.getQ5UTCM());
         return;
      }
      // ----------------------------------------------------------------
      //   Retreive original record before testing for new values.
      // ----------------------------------------------------------------
      HILIN.setCONO(LDAZD.CONO);
      HILIN.setMSGN().move(inChgWhsLine.getQ5MSGN());
      HILIN.setPACN().move(inChgWhsLine.getQ5PACN());
      X0FLDD = 5;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inChgWhsLine.getQ5MSLN());
      RNUM();
      HILIN.setMSLN((int)this.PXNUM);
      rAPIDS.clear();
      X0OPC.moveLeftPad("*GET");
      rPLINPIpreCall();
      apCall("MHILINPI", rPLINPI);
      rPLINPIpostCall();
      IN60 = toBoolean(X0IN60.getChar());
      if (IN60) {
         MICommon.setError( "", X0MSID.toString(), X0MSGD);
         return;
      }
      // ----------------------------------------------------------------
      //   Test for validity before performing transaction.
      // ----------------------------------------------------------------
      if (HILIN.getSTAT().GE("90")) {
         //   MSGID=XST0020 Status change are not permitted any longer
         MICommon.setError( "", "XST0020", HILIN.getSTAT());
         return;
      }
      //   Get product data
      if (inChgWhsLine.getQ5ITNO().isBlank()) {
         inChgWhsLine.setQ5ITNO().move(HILIN.getITNO());
      }
      ITMAS.setCONO(LDAZD.CONO);
      ITMAS.setITNO().move(inChgWhsLine.getQ5ITNO());
      if (!ITMAS.CHAIN("00", ITMAS.getKey("00"))) {
         //   MSGID=WIT0103 Item number &1 does not exist
         MICommon.setError( "", "WIT0103", inChgWhsLine.getQ5ITNO());
         return;
      }
      XXITNO.moveLeftPad(inChgWhsLine.getQ5ITNO());
      RITNO();
      // ----------------------------------------------------------------
      //   Move data from the transaction input structure (MHS850MI1)
      //   to the execution data formats (MHIHED/MHIPAC/MHILIN)
      // ----------------------------------------------------------------
      HILIN.setCONO(LDAZD.CONO);
      if (!inChgWhsLine.getQ5DIVI().isBlank()) {
         HILIN.setDIVI().move(inChgWhsLine.getQ5DIVI());
      }
      if (!inChgWhsLine.getQ5WHLO().isBlank()) {
         HILIN.setWHLO().move(inChgWhsLine.getQ5WHLO());
      }
      if (UTCmode) {
         //   Determine time zone for Warehouse
         XXWHLO.moveLeftPad(HILIN.getWHLO());
         if (!CRCalendar.retrieveTIZOForWHLO(PXMNGTZN, LDAZD.CONO, XXWHLO)) {
            IN60 = true;
            //   MSGID=S_00148 UTC time conversion failed.
            MICommon.setError("GEDT","S_00148");
            return;
         }
         //   time zone for warehouse
         TIZOforWHLO.move(CRCalendar.getTimeZone());
      }
      if (!inChgWhsLine.getQ5MSGN().isBlank()) {
         HILIN.setMSGN().move(inChgWhsLine.getQ5MSGN());
      }
      if (!inChgWhsLine.getQ5PACN().isBlank()) {
         HILIN.setPACN().move(inChgWhsLine.getQ5PACN());
      }
      if (!inChgWhsLine.getQ5QLFR().isBlank()) {
         HILIN.setQLFR().move(inChgWhsLine.getQ5QLFR());
      }
      if (!inChgWhsLine.getQ5QLFS().isBlank()) {
         HILIN.setQLFS(inChgWhsLine.getQ5QLFS().getInt());
      }
      if (!inChgWhsLine.getQ5FACI().isBlank()) {
         HILIN.setFACI().move(inChgWhsLine.getQ5FACI());
      }
      if (!inChgWhsLine.getQ5CHGD().isBlank()) {
         if (!MICommon.toNumericDate(inChgWhsLine.getQ5CHGD())) {
            MICommon.setError("CHGD");
            return;
         }
         HILIN.setCHGD(MICommon.getNumericDate());
      }
      if (!inChgWhsLine.getQ5STAT().isBlank()) {
         HILIN.setSTAT().move(inChgWhsLine.getQ5STAT());
      }
      if (!inChgWhsLine.getQ5ITNO().isBlank()) {
         HILIN.setITNO().move(inChgWhsLine.getQ5ITNO());
      }
      if (!inChgWhsLine.getQ5ITDS().isBlank()) {
         HILIN.setITDS().move(inChgWhsLine.getQ5ITDS());
      }
      if (!inChgWhsLine.getQ5WHSL().isBlank()) {
         HILIN.setWHSL().move(inChgWhsLine.getQ5WHSL());
      }
      if (!inChgWhsLine.getQ5BANO().isBlank()) {
         HILIN.setBANO().move(inChgWhsLine.getQ5BANO());
      }
      if (!inChgWhsLine.getQ5CAMU().isBlank()) {
         HILIN.setCAMU().move(inChgWhsLine.getQ5CAMU());
      }
      if (!inChgWhsLine.getQ5REPN().isBlank()) {
         X0FLDD = 10;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsLine.getQ5REPN());
         RNUM();
         HILIN.setREPN((long)this.PXNUM);
      }
      if (!inChgWhsLine.getQ5RELI().isBlank()) {
         X0FLDD = 5;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsLine.getQ5RELI());
         RNUM();
         HILIN.setRELI((int)this.PXNUM);
      }
      if (!inChgWhsLine.getQ5POPN().isBlank()) {
         HILIN.setPOPN().move(inChgWhsLine.getQ5POPN());
      }
      if (!inChgWhsLine.getQ5ALWQ().isBlank()) {
         HILIN.setALWQ().move(inChgWhsLine.getQ5ALWQ());
      }
      if (!inChgWhsLine.getQ5ALWT().isBlank()) {
         X0FLDD = 2;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsLine.getQ5ALWT());
         RNUM();
         HILIN.setALWT((int)this.PXNUM);
      }
      if (!inChgWhsLine.getQ5DLQT().isBlank()) {
         XXQTYN = 0d;
         XXQTYA.clear();
         XXQTYA.moveLeft(inChgWhsLine.getQ5DLQT());
         this.PXDCCD = X1DCCD;
         RQTY();
         HILIN.setDLQT(this.PXNUM);
      }
      if (!inChgWhsLine.getQ5UNIT().isBlank()) {
         HILIN.setUNIT().move(inChgWhsLine.getQ5UNIT());
      }
      if (!inChgWhsLine.getQ5DLQA().isBlank()) {
         XXQTYN = 0d;
         XXQTYA.clear();
         XXQTYA.moveLeft(inChgWhsLine.getQ5DLQA());
         this.PXDCCD = X1DCCD;
         RQTY();
         HILIN.setDLQA(this.PXNUM);
      }
      if (!inChgWhsLine.getQ5ALUN().isBlank()) {
         HILIN.setALUN().move(inChgWhsLine.getQ5ALUN());
      }
      if (!inChgWhsLine.getQ5VOL3().isBlank()) {
         X0FLDD = 9;
         X0DCCD = 3;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsLine.getQ5VOL3());
         RNUM();
         HILIN.setVOL3(this.PXNUM);
      }
      if (!inChgWhsLine.getQ5NEWE().isBlank()) {
         X0FLDD = 9;
         X0DCCD = 3;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsLine.getQ5NEWE());
         RNUM();
         HILIN.setNEWE(this.PXNUM);
      }
      if (!inChgWhsLine.getQ5GRWE().isBlank()) {
         X0FLDD = 9;
         X0DCCD = 3;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsLine.getQ5GRWE());
         RNUM();
         HILIN.setGRWE(this.PXNUM);
      }
      if (!inChgWhsLine.getQ5D1QT().isBlank()) {
         XXQTYN = 0d;
         XXQTYA.clear();
         XXQTYA.moveLeft(inChgWhsLine.getQ5D1QT());
         this.PXDCCD = X1DCCD;
         RQTY();
         HILIN.setD1QT(this.PXNUM);
      }
      if (!inChgWhsLine.getQ5WHLO().isBlank()) {
         HILIN.setDLSP().move(inChgWhsLine.getQ5DLSP());
      }
      if (!inChgWhsLine.getQ5RVQA().isBlank()) {
         XXQTYN = 0d;
         XXQTYA.clear();
         XXQTYA.moveLeft(inChgWhsLine.getQ5RVQA());
         this.PXDCCD = X1DCCD;
         RQTY();
         HILIN.setRVQA(this.PXNUM);
      }
      if (!inChgWhsLine.getQ5PUUN().isBlank()) {
         HILIN.setPUUN().move(inChgWhsLine.getQ5PUUN());
      }
      if (!inChgWhsLine.getQ5ALQT().isBlank()) {
         XXQTYN = 0d;
         XXQTYA.clear();
         XXQTYA.moveLeft(inChgWhsLine.getQ5ALQT());
         this.PXDCCD = X1DCCD;
         RQTY();
         HILIN.setALQT(this.PXNUM);
      }
      if (!inChgWhsLine.getQ5PAQT().isBlank()) {
         XXQTYN = 0d;
         XXQTYA.clear();
         XXQTYA.moveLeft(inChgWhsLine.getQ5PAQT());
         this.PXDCCD = X1DCCD;
         RQTY();
         HILIN.setPAQT(this.PXNUM);
      }
      if (!inChgWhsLine.getQ5CAWE().isBlank()) {
         XXQTYN = 0d;
         XXQTYA.clear();
         XXQTYA.moveLeft(inChgWhsLine.getQ5CAWE());
         this.PXDCCD = X2DCCD;
         RQTY();
         HILIN.setCAWE(this.PXNUM);
      }
      if (!inChgWhsLine.getQ5POCY().isBlank()) {
         X0FLDD = 5;
         X0DCCD = 2;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsLine.getQ5POCY());
         RNUM();
         HILIN.setPOCY(this.PXNUM);
      }
      if (inChgWhsLine.getQ5OEND().getChar() != ' ') {
         X0FLDD = 1;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsLine.getQ5OEND().getChar());
         RNUM();
         HILIN.setOEND((int)this.PXNUM);
      }
      if (!inChgWhsLine.getQ5BREF().isBlank()) {
         HILIN.setBREF().move(inChgWhsLine.getQ5BREF());
      }
      if (!inChgWhsLine.getQ5BRE2().isBlank()) {
         HILIN.setBRE2().move(inChgWhsLine.getQ5BRE2());
      }
      if (!inChgWhsLine.getQ5FLOC().isBlank()) {
         HILIN.setFLOC().move(inChgWhsLine.getQ5FLOC());
      }
      if (!inChgWhsLine.getQ5ORCA().isBlank()) {
         HILIN.setORCA().move(inChgWhsLine.getQ5ORCA());
      }
      if (!inChgWhsLine.getQ5TTYP().isBlank()) {
         X0FLDD = 2;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsLine.getQ5TTYP());
         RNUM();
         HILIN.setTTYP((int)this.PXNUM);
      }
      if (!inChgWhsLine.getQ5RIDN().isBlank()) {
         HILIN.setRIDN().move(inChgWhsLine.getQ5RIDN());
      }
      if (!inChgWhsLine.getQ5RIDO().isBlank()) {
         X0FLDD = 6;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsLine.getQ5RIDO());
         RNUM();
         HILIN.setRIDO((int)this.PXNUM);
      }
      if (!inChgWhsLine.getQ5RIDL().isBlank()) {
         X0FLDD = 6;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsLine.getQ5RIDL());
         RNUM();
         HILIN.setRIDL((int)this.PXNUM);
      }
      if (!inChgWhsLine.getQ5RIDX().isBlank()) {
         X0FLDD = 3;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsLine.getQ5RIDX());
         RNUM();
         HILIN.setRIDX((int)this.PXNUM);
      }
      if (!inChgWhsLine.getQ5RIDI().isBlank()) {
         X0FLDD = 11;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsLine.getQ5RIDI());
         RNUM();
         HILIN.setRIDI((long)this.PXNUM);
      }
      if (!inChgWhsLine.getQ5PLSX().isBlank()) {
         X0FLDD = 3;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsLine.getQ5PLSX());
         RNUM();
         HILIN.setPLSX((int)this.PXNUM);
      }
      if (!inChgWhsLine.getQ5DLIX().isBlank()) {
         X0FLDD = 11;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsLine.getQ5DLIX());
         RNUM();
         HILIN.setDLIX((long)this.PXNUM);
      }
      if (!inChgWhsLine.getQ5PLRN().isBlank()) {
         X0FLDD = 11;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsLine.getQ5PLRN());
         RNUM();
         HILIN.setPLRN((long)this.PXNUM);
      }
      //   Format reporting date
      if (!inChgWhsLine.getQ5RPDT().isBlank()) {
         if (!MICommon.toNumericDate(inChgWhsLine.getQ5RPDT())) {
            MICommon.setError("RPDT");
            return;
         }
         HILIN.setRPDT(MICommon.getNumericDate());
      }
      if (!inChgWhsLine.getQ5DNNO().isBlank()) {
         HILIN.setDNNO().move(inChgWhsLine.getQ5DNNO());
      }
      if (!inChgWhsLine.getQ5CUOR().isBlank()) {
         HILIN.setCUOR().move(inChgWhsLine.getQ5CUOR());
      }
      if (!inChgWhsLine.getQ5CUNO().isBlank()) {
         HILIN.setCUNO().move(inChgWhsLine.getQ5CUNO());
      }
      if (!inChgWhsLine.getQ5ADID().isBlank()) {
         HILIN.setADID().move(inChgWhsLine.getQ5ADID());
      }
      if (!inChgWhsLine.getQ5SUNO().isBlank()) {
         HILIN.setSUNO().move(inChgWhsLine.getQ5SUNO());
      }
      if (inChgWhsLine.getQ5SUTY().getChar() != ' ') {
         X0FLDD = 1;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsLine.getQ5SUTY().getChar());
         RNUM();
         HILIN.setSUTY((int)this.PXNUM);
      }
      if (!inChgWhsLine.getQ5SUDO().isBlank()) {
         HILIN.setSUDO().move(inChgWhsLine.getQ5SUDO());
      }
      if (!inChgWhsLine.getQ5DNDT().isBlank()) {
         if (!MICommon.toNumericDate(inChgWhsLine.getQ5DNDT())) {
            MICommon.setError("DNDT");
            return;
         }
         HILIN.setDNDT(MICommon.getNumericDate());
      }
      if (!inChgWhsLine.getQ5EXPI().isBlank()) {
         if (!MICommon.toNumericDate(inChgWhsLine.getQ5EXPI())) {
            MICommon.setError("EXPI");
            return;
         }
         HILIN.setEXPI(MICommon.getNumericDate());
      }
      if (!inChgWhsLine.getQ5CNDT().isBlank()) {
         if (!MICommon.toNumericDate(inChgWhsLine.getQ5CNDT())) {
            MICommon.setError("CNDT");
            return;
         }
         HILIN.setCNDT(MICommon.getNumericDate());
      }
      if (!inChgWhsLine.getQ5SEDT().isBlank()) {
         if (!MICommon.toNumericDate(inChgWhsLine.getQ5SEDT())) {
            MICommon.setError("SEDT");
            return;
         }
         HILIN.setSEDT(MICommon.getNumericDate());
      }
      if (inChgWhsLine.getQ5QCRA().getChar() != ' ') {
         HILIN.setQCRA(inChgWhsLine.getQ5QCRA().getChar());
      }
      if (!inChgWhsLine.getQ5SCRE().isBlank()) {
         HILIN.setSCRE().move(inChgWhsLine.getQ5SCRE());
      }
      if (!inChgWhsLine.getQ5TRTP().isBlank()) {
         HILIN.setTRTP().move(inChgWhsLine.getQ5TRTP());
      }
      if (!inChgWhsLine.getQ5BREM().isBlank()) {
         HILIN.setBREM().move(inChgWhsLine.getQ5BREM());
      }
      if (!inChgWhsLine.getQ5SITE().isBlank()) {
         HILIN.setSITE().move(inChgWhsLine.getQ5SITE());
      }
      if (!inChgWhsLine.getQ5SITD().isBlank()) {
         HILIN.setSITD().move(inChgWhsLine.getQ5SITD());
      }
      if (!inChgWhsLine.getQ5PUPR().isBlank()) {
         X0FLDD = 17;
         X0DCCD = 6;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsLine.getQ5PUPR());
         RNUM();
         HILIN.setPUPR(this.PXNUM);
      }
      if (!inChgWhsLine.getQ5PPUN().isBlank()) {
         HILIN.setPPUN().move(inChgWhsLine.getQ5PPUN());
      }
      if (!inChgWhsLine.getQ5CUCD().isBlank()) {
         HILIN.setCUCD().move(inChgWhsLine.getQ5CUCD());
      }
      if (!inChgWhsLine.getQ5PUCD().isBlank()) {
         X0FLDD = 5;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsLine.getQ5PUCD());
         RNUM();
         HILIN.setPUCD((int)this.PXNUM);
      }
      if (!inChgWhsLine.getQ5LNAM().isBlank()) {
         X0FLDD = 15;
         X0DCCD = 2;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsLine.getQ5LNAM());
         RNUM();
         HILIN.setLNAM(this.PXNUM);
      }
      if (!inChgWhsLine.getQ5VTCD().isBlank()) {
         X0FLDD = 2;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsLine.getQ5VTCD());
         RNUM();
         HILIN.setVTCD((int)this.PXNUM);
      }
      if (!inChgWhsLine.getQ5AGNB().isBlank()) {
         HILIN.setAGNB().move(inChgWhsLine.getQ5AGNB());
      }
      if (!inChgWhsLine.getQ5RESP().isBlank()) {
         HILIN.setRESP().move(inChgWhsLine.getQ5RESP());
      }
      if (!inChgWhsLine.getQ5DTID().isBlank()) {
         X0FLDD = 13;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsLine.getQ5DTID());
         RNUM();
         HILIN.setDTID((long)this.PXNUM);
      }
      if (!inChgWhsLine.getQ5TXID().isBlank()) {
         X0FLDD = 13;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsLine.getQ5TXID());
         RNUM();
         HILIN.setTXID((long)this.PXNUM);
      }
      if (!inChgWhsLine.getQ5USD1().isBlank()) {
         HILIN.setUSD1().move(inChgWhsLine.getQ5USD1());
      }
      if (!inChgWhsLine.getQ5USD2().isBlank()) {
         HILIN.setUSD2().move(inChgWhsLine.getQ5USD2());
      }
      if (!inChgWhsLine.getQ5USD3().isBlank()) {
         HILIN.setUSD3().move(inChgWhsLine.getQ5USD3());
      }
      if (!inChgWhsLine.getQ5USD4().isBlank()) {
         HILIN.setUSD4().move(inChgWhsLine.getQ5USD4());
      }
      if (!inChgWhsLine.getQ5USD5().isBlank()) {
         X0FLDD = 6;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsLine.getQ5USD5());
         RNUM();
         HILIN.setRPTM((int)this.PXNUM);
      }
      if (!UTCmode &&
         !inChgWhsLine.getQ5RPDT().isBlank() &&
         inChgWhsLine.getQ5USD5().isBlank()) {
         HILIN.setRPTM(movexTime());
      }
      if (UTCmode) {
         // Reporting date
         if (!inChgWhsLine.getQ5RPDT().isBlank() &&
             !inChgWhsLine.getQ5USD5().isBlank()) {
            date = HILIN.getRPDT(); 
            time = HILIN.getRPTM() / 100;           
            if (!convertFromUTC()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("RPDT","S_00148");
               return;
            }
            HILIN.setRPDT(UTCdate);
            HILIN.setRPTM(UTCtime * 100);
         } else {
            if (inChgWhsLine.getQ5RPDT().isBlank() ||
               inChgWhsLine.getQ5USD5().isBlank()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("RPDT","S_00148");
               return;
            }
         }
      }
      if (!inChgWhsLine.getQ5RSCD().isBlank()) {
         HILIN.setRSCD().move(inChgWhsLine.getQ5RSCD());
      }
      if (!inChgWhsLine.getQ5MFDT().isBlank()) {
         if (!MICommon.toNumericDate(inChgWhsLine.getQ5MFDT())) {
            MICommon.setError("MFDT");
            return;
         }
         HILIN.setMFDT(MICommon.getNumericDate());
      }
      //-----------------------------------
      if (!inChgWhsLine.getQ5ISMD().isBlank()) {
         X0FLDD = 1;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsLine.getQ5ISMD().getChar());
         RNUM();
         HILIN.setISMD((int)this.PXNUM);
      }
      if (!inChgWhsLine.getQ5TWSL().isBlank()) {
         HILIN.setTWSL().move(inChgWhsLine.getQ5TWSL());
      }
      if (IN75) {
         IN60 = true;
         return;
      }
      //----------------------------------------
      RLINE();
      if (IN60) {
         return;
      }
   }

   /**
   *    RCOM16 - Execute command - ChgWhsPack
   */
   public void RCOM16() {
      // ----------------------------------------------------------------
      //   Clear transaction records
      // ----------------------------------------------------------------
      sMHS850MIRChgWhsPack inChgWhsPack = (sMHS850MIRChgWhsPack)MICommon.getInDS(sMHS850MIRChgWhsPack.class);

      HIHED.clear();
      HIPAC.clear();
      HILIN.clear();
      // ----------------------------------------------------------------
      //   Default process option.
      // ----------------------------------------------------------------
      XXPRFL.move("*CHK");
      XXOPC.move("*UPD");
      if (MICommon.toNumericCompany(inChgWhsPack.getQ4CONO())) {
         XXCONO = MICommon.getInt();
      } else {
         MICommon.setError("CONO");
         return;
      }
      LDAZD.CONO = XXCONO;
      //-----------------------------------
      //    UTC mode
      UTCmode = isUTCmode(inChgWhsPack.getQ4UTCM());
      if (IN60) {
         MICommon.setError("UTCM", "WUTCM01", inChgWhsPack.getQ4UTCM());
         return;
      }
      // ----------------------------------------------------------------
      //   Retreive original record before testing for new values.
      // ----------------------------------------------------------------
      HIPAC.setCONO(LDAZD.CONO);
      HIPAC.setMSGN().move(inChgWhsPack.getQ4MSGN());
      HIPAC.setPACN().move(inChgWhsPack.getQ4PACN());
      rAPIDS.clear();
      X0OPC.moveLeftPad("*GET");
      rPPACPIpreCall();
      apCall("MHIPACPI", rPPACPI);
      rPPACPIpostCall();
      IN60 = toBoolean(X0IN60.getChar());
      if (IN60) {
         MICommon.setError( "", X0MSID.toString(), X0MSGD);
         return;
      }
      // ----------------------------------------------------------------
      //   Test for validity before performing transaction.
      // ----------------------------------------------------------------
      if (HIPAC.getSTAT().GE("90")) {
         //   MSGID=XST0020 Status change are not permitted any longer
         MICommon.setError( "", "XST0020", HIPAC.getSTAT());
         return;
      }
      // ----------------------------------------------------------------
      //   Move data from the transaction input structure (MHS850MI1)
      //   to the execution data formats (MHIHED/MHIPAC/MHILIN)
      // ----------------------------------------------------------------
      HIPAC.setCONO(LDAZD.CONO);
      if (!inChgWhsPack.getQ4DIVI().isBlank()) {
         HIPAC.setDIVI().move(inChgWhsPack.getQ4DIVI());
      }
      if (!inChgWhsPack.getQ4WHLO().isBlank()) {
         HIPAC.setWHLO().move(inChgWhsPack.getQ4WHLO());
      }
      if (UTCmode) {
         //   Determine time zone for Warehouse
         XXWHLO.moveLeftPad(HIPAC.getWHLO());
         if (!CRCalendar.retrieveTIZOForWHLO(PXMNGTZN, LDAZD.CONO, XXWHLO)) {
            IN60 = true;
            //   MSGID=S_00148 UTC time conversion failed.
            MICommon.setError("GEDT","S_00148");
            return;
         }
         //   time zone for warehouse
         TIZOforWHLO.move(CRCalendar.getTimeZone());
      }
      if (!inChgWhsPack.getQ4MSGN().isBlank()) {
         HIPAC.setMSGN().move(inChgWhsPack.getQ4MSGN());
      }
      if (!inChgWhsPack.getQ4PACN().isBlank()) {
         HIPAC.setPACN().move(inChgWhsPack.getQ4PACN());
      }
      if (!inChgWhsPack.getQ4QLFR().isBlank()) {
         HIPAC.setQLFR().move(inChgWhsPack.getQ4QLFR());
      }
      if (!inChgWhsPack.getQ4GEDT().isBlank()) {
         if (!MICommon.toNumericDate(inChgWhsPack.getQ4GEDT())) {
            MICommon.setError("GEDT");
            return;
         }
         HIPAC.setGEDT(MICommon.getNumericDate());
      }
      if (!inChgWhsPack.getQ4GETM().isBlank()) {
         X0FLDD = 6;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsPack.getQ4GETM());
         RNUM();
         HIPAC.setGETM((int)this.PXNUM);
      }
      // Date generated
      if (!inChgWhsPack.getQ4GEDT().isBlank() && !inChgWhsPack.getQ4GETM().isBlank()) {
         date = HIPAC.getGEDT(); 
         time = HIPAC.getGETM() / 100;
         if (UTCmode && date > 0) {
            if (!convertFromUTC()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("GEDT","S_00148");
               return;
            }
            HIPAC.setGEDT(UTCdate);
            HIPAC.setGETM(UTCtime * 100);
         }
      }
      if (!inChgWhsPack.getQ4CHGD().isBlank()) {
         if (!MICommon.toNumericDate(inChgWhsPack.getQ4CHGD())) {
            MICommon.setError("CHGD");
            return;
         }
         HIPAC.setCHGD(MICommon.getNumericDate());
      }
      if (!inChgWhsPack.getQ4PARE().isBlank()) {
         HIPAC.setPARE().move(inChgWhsPack.getQ4PARE());
      }
      if (inChgWhsPack.getQ4DIPA().getChar() != ' ') {
         X0FLDD = 1;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsPack.getQ4DIPA().getChar());
         RNUM();
         HIPAC.setDIPA((int)this.PXNUM);
      }
      if (!inChgWhsPack.getQ4SUDO().isBlank()) {
         HIPAC.setSUDO().move(inChgWhsPack.getQ4SUDO());
      }
      if (!inChgWhsPack.getQ4DNNO().isBlank()) {
         HIPAC.setDNNO().move(inChgWhsPack.getQ4DNNO());
      }
      if (!inChgWhsPack.getQ4DNDT().isBlank()) {
         if (!MICommon.toNumericDate(inChgWhsPack.getQ4DNDT())) {
            MICommon.setError("DNDT");
            return;
         }
         HIPAC.setDNDT(MICommon.getNumericDate());
      }
      if (!inChgWhsPack.getQ4STAT().isBlank()) {
         HIPAC.setSTAT().move(inChgWhsPack.getQ4STAT());
      }
      if (!inChgWhsPack.getQ4TRSL().isBlank()) {
         HIPAC.setTRSL().move(inChgWhsPack.getQ4TRSL());
      }
      if (!inChgWhsPack.getQ4TRSH().isBlank()) {
         HIPAC.setTRSH().move(inChgWhsPack.getQ4TRSH());
      }
      if (!inChgWhsPack.getQ4PACO().isBlank()) {
         X0FLDD = 3;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsPack.getQ4PACO());
         RNUM();
         HIPAC.setPACO((int)this.PXNUM);
      }
      if (inChgWhsPack.getQ4PACU().getChar() != ' ') {
         X0FLDD = 1;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsPack.getQ4PACU().getChar());
         RNUM();
         HIPAC.setPACU((int)this.PXNUM);
      }
      if (!inChgWhsPack.getQ4PACC().isBlank()) {
         HIPAC.setPACC().move(inChgWhsPack.getQ4PACC());
      }
      if (!inChgWhsPack.getQ4AMKO().isBlank()) {
         X0FLDD = 7;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsPack.getQ4AMKO());
         RNUM();
         HIPAC.setAMKO((int)this.PXNUM);
      }
      if (!inChgWhsPack.getQ4PACT().isBlank()) {
         HIPAC.setPACT().move(inChgWhsPack.getQ4PACT());
      }
      if (!inChgWhsPack.getQ4PACK().isBlank()) {
         HIPAC.setPACK().move(inChgWhsPack.getQ4PACK());
      }
      if (!inChgWhsPack.getQ4TEPA().isBlank()) {
         HIPAC.setTEPA().move(inChgWhsPack.getQ4TEPA());
      }
      if (!inChgWhsPack.getQ4SORT().isBlank()) {
         HIPAC.setSORT().move(inChgWhsPack.getQ4SORT());
      }
      if (!inChgWhsPack.getQ4DLRM().isBlank()) {
         HIPAC.setDLRM().move(inChgWhsPack.getQ4DLRM());
      }
      if (!inChgWhsPack.getQ4DLMO().isBlank()) {
         HIPAC.setDLMO().move(inChgWhsPack.getQ4DLMO());
      }
      if (!inChgWhsPack.getQ4DLSP().isBlank()) {
         HIPAC.setDLSP().move(inChgWhsPack.getQ4DLSP());
      }
      if (!inChgWhsPack.getQ4VOM3().isBlank()) {
         X0FLDD = 9;
         X0DCCD = 3;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsPack.getQ4VOM3());
         RNUM();
         HIPAC.setVOM3(this.PXNUM);
      }
      if (!inChgWhsPack.getQ4FRCP().isBlank()) {
         X0FLDD = 11;
         X0DCCD = 3;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsPack.getQ4FRCP());
         RNUM();
         HIPAC.setFRCP(this.PXNUM);
      }
      if (!inChgWhsPack.getQ4NEWE().isBlank()) {
         X0FLDD = 9;
         X0DCCD = 3;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsPack.getQ4NEWE());
         RNUM();
         HIPAC.setNEWE(this.PXNUM);
      }
      if (!inChgWhsPack.getQ4GRWE().isBlank()) {
         X0FLDD = 9;
         X0DCCD = 3;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsPack.getQ4GRWE());
         RNUM();
         HIPAC.setGRWE(this.PXNUM);
      }
      if (!inChgWhsPack.getQ4GRW3().isBlank()) {
         X0FLDD = 9;
         X0DCCD = 3;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsPack.getQ4GRW3());
         RNUM();
         HIPAC.setGRW3(this.PXNUM);
      }
      if (!inChgWhsPack.getQ4GRW4().isBlank()) {
         X0FLDD = 9;
         X0DCCD = 3;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsPack.getQ4GRW4());
         RNUM();
         HIPAC.setGRW4(this.PXNUM);
      }
      if (!inChgWhsPack.getQ4GRW5().isBlank()) {
         X0FLDD = 9;
         X0DCCD = 3;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsPack.getQ4GRW5());
         RNUM();
         HIPAC.setGRW5(this.PXNUM);
      }
      if (!inChgWhsPack.getQ4D1QT().isBlank()) {
         XXQTYN = 0d;
         XXQTYA.clear();
         XXQTYA.moveLeft(inChgWhsPack.getQ4D1QT());
         this.PXDCCD = X1DCCD;
         RQTY();
         HIPAC.setD1QT(this.PXNUM);
      }
      if (!inChgWhsPack.getQ4VOMT().isBlank()) {
         X0FLDD = 9;
         X0DCCD = 3;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsPack.getQ4VOMT());
         RNUM();
         HIPAC.setVOMT(this.PXNUM);
      }
      if (!inChgWhsPack.getQ4PACL().isBlank()) {
         X0FLDD = 5;
         X0DCCD = 3;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsPack.getQ4PACL());
         RNUM();
         HIPAC.setPACL(this.PXNUM);
      }
      if (!inChgWhsPack.getQ4PACW().isBlank()) {
         X0FLDD = 5;
         X0DCCD = 3;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsPack.getQ4PACW());
         RNUM();
         HIPAC.setPACW(this.PXNUM);
      }
      if (!inChgWhsPack.getQ4PACH().isBlank()) {
         X0FLDD = 5;
         X0DCCD = 3;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsPack.getQ4PACH());
         RNUM();
         HIPAC.setPACH(this.PXNUM);
      }
      if (!inChgWhsPack.getQ4SSCC().isBlank()) {
         HIPAC.setSSCC().move(inChgWhsPack.getQ4SSCC());
      }
      if (!inChgWhsPack.getQ4WHSL().isBlank()) {
         HIPAC.setWHSL().move(inChgWhsPack.getQ4WHSL());
      }
      if (!inChgWhsPack.getQ4CUNO().isBlank()) {
         HIPAC.setCUNO().move(inChgWhsPack.getQ4CUNO());
      }
      if (!inChgWhsPack.getQ4ADID().isBlank()) {
         HIPAC.setADID().move(inChgWhsPack.getQ4ADID());
      }
      if (!inChgWhsPack.getQ4SUNO().isBlank()) {
         HIPAC.setSUNO().move(inChgWhsPack.getQ4SUNO());
      }
      if (inChgWhsPack.getQ4SUTY().getChar() != ' ') {
         X0FLDD = 1;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsPack.getQ4SUTY().getChar());
         RNUM();
         HIPAC.setSUTY((int)this.PXNUM);
      }
      if (!inChgWhsPack.getQ4RESP().isBlank()) {
         HIPAC.setRESP().move(inChgWhsPack.getQ4RESP());
      }
      if (!inChgWhsPack.getQ4DTID().isBlank()) {
         X0FLDD = 13;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsPack.getQ4DTID());
         RNUM();
         HIPAC.setDTID((long)this.PXNUM);
      }
      if (!inChgWhsPack.getQ4TXID().isBlank()) {
         X0FLDD = 13;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inChgWhsPack.getQ4TXID());
         RNUM();
         HIPAC.setTXID((long)this.PXNUM);
      }
      if (!inChgWhsPack.getQ4USD1().isBlank()) {
         HIPAC.setUSD1().move(inChgWhsPack.getQ4USD1());
      }
      if (!inChgWhsPack.getQ4USD2().isBlank()) {
         HIPAC.setUSD2().move(inChgWhsPack.getQ4USD2());
      }
      if (!inChgWhsPack.getQ4USD3().isBlank()) {
         HIPAC.setUSD3().move(inChgWhsPack.getQ4USD3());
      }
      if (!inChgWhsPack.getQ4USD4().isBlank()) {
         HIPAC.setUSD4().move(inChgWhsPack.getQ4USD4());
      }
      if (!inChgWhsPack.getQ4USD5().isBlank()) {
         HIPAC.setUSD5().move(inChgWhsPack.getQ4USD5());
      }
      if (!inChgWhsPack.getQ4PPNB().isBlank()) {
         HIPAC.setPPNB().move(inChgWhsPack.getQ4PPNB());
      }

      RPACK();
      if (IN60) {
         return;
      }
   }

   /**
   *    RCOM17 - Execute command - DltWhsTran
   */
   public void RCOM17() {
      sMHS850MIRDeleteWhsTran inDeleteWhsTran = (sMHS850MIRDeleteWhsTran)MICommon.getInDS(sMHS850MIRDeleteWhsTran.class);

      HIHED.clear();
      HIPAC.clear();
      HILIN.clear();
      // ----------------------------------------------------------------
      //   Default process option.
      // ----------------------------------------------------------------
      XXPRFL.move("*CHK");
      XXOPC.move("*DLT");
      if (MICommon.toNumericCompany(inDeleteWhsTran.getQ9CONO())) {
         XXCONO = MICommon.getInt();
      } else {
         MICommon.setError("CONO");
         return;
      }
      LDAZD.CONO = XXCONO;
      // ----------------------------------------------------------------
      //   Move data from the transaction input structure (MHS850MI1)
      //   to the execution data formats (MHIHED/MHIPAC/MHILIN)
      // ----------------------------------------------------------------
      HIHED.setCONO(LDAZD.CONO);
      HIPAC.setCONO(LDAZD.CONO);
      HILIN.setCONO(LDAZD.CONO);
      HIHED.setMSGN().move(inDeleteWhsTran.getQ9MSGN());
      HIPAC.setMSGN().move(inDeleteWhsTran.getQ9MSGN());
      HILIN.setMSGN().move(inDeleteWhsTran.getQ9MSGN());
      switch (0) {
      default:
         if (!inDeleteWhsTran.getQ9MSLN().isBlank()) {
            HILIN.setPACN().move(inDeleteWhsTran.getQ9PACN());
            X0FLDD = 5;
            XXNUMN = 0d;
            XXNUMA.clear();
            XXNUMA.moveLeft(inDeleteWhsTran.getQ9MSLN());
            RNUM();
            HILIN.setMSLN((int)this.PXNUM);
            rAPIDS.clear();
            X0OPC.moveLeftPad("*GET");
            rPLINPIpreCall();
            apCall("MHILINPI", rPLINPI);
            rPLINPIpostCall();
            IN60 = toBoolean(X0IN60.getChar());
            if (IN60) {
               MICommon.setError( "", X0MSID.toString(), X0MSGD);
               return;
            }
            //   Test for validity before performing transaction.
            if (HILIN.getSTAT().GE("90")) {
               //   MSGID=XST0020 Status change are not permitted any longer
               MICommon.setError( "", "XST0020", HILIN.getSTAT());
               return;
            }
            //   Validate/create transaction line data
            RLINE();
            if (IN60) {
               return;
            }
            break;
         }
         if (!inDeleteWhsTran.getQ9PACN().isBlank()) {
            // ----------------------------------------------------------------
            HIPAC.setPACN().move(inDeleteWhsTran.getQ9PACN());
            rAPIDS.clear();
            X0OPC.moveLeftPad("*GET");
            rPPACPIpreCall();
            apCall("MHIPACPI", rPPACPI);
            rPPACPIpostCall();
            IN60 = toBoolean(X0IN60.getChar());
            if (IN60) {
               MICommon.setError( "", X0MSID.toString(), X0MSGD);
               return;
            }
            //   Test for validity before performing transaction.
            if (HIPAC.getSTAT().GE("90")) {
               //   MSGID=XST0020 Status change are not permitted any longer
               MICommon.setError( "", "XST0020",HIPAC.getSTAT());
               return;
            }
            //   Validate/create transaction line data
            RPACK();
            if (IN60) {
               return;
            }
            break;
         }
         // ----------------------------------------------------------------
         rAPIDS.clear();
         X0OPC.moveLeftPad("*GET");
         rPHEDPIpreCall();
         apCall("MHIHEDPI", rPHEDPI);
         rPHEDPIpostCall();
         IN60 = toBoolean(X0IN60.getChar());
         if (IN60) {
            MICommon.setError( "", X0MSID.toString(), X0MSGD);
            return;
         }
         //   Test for validity before performing transaction.
         if (HIHED.getSTAT().GE("90")) {
            //   MSGID=XST0020 Status change are not permitted any longer
            MICommon.setError( "", "XST0020", HIHED.getSTAT());
            return;
         }
         //   Validate/create transaction line data
         RHEAD();
         if (IN60) {
            return;
         }
         // ----------------------------------------------------------------
         break;
      }
   }

   /**
   * Process transaction GetMvxMsg
   */
   public void getMvxMsg() {
      sMHS850MIRGetMvxMsg in = (sMHS850MIRGetMvxMsg)MICommon.getInDS(sMHS850MIRGetMvxMsg.class);
      sMHS850MISGetMvxMsg out = (sMHS850MISGetMvxMsg)MICommon.getOutDS(sMHS850MISGetMvxMsg.class);

      if (MICommon.toNumericCompany(in.getQBCONO())) {
         HIHED.setCONO(MICommon.getInt());
      } else {
         MICommon.setError("CONO");
         return;
      }

      if(in.getQBE0PB().isBlank()) {
         MICommon.setError( "E0PB", "SO94201"); // Mandatory fields are missing
         return;
      }
      HIHED.setE0PB().moveLeftPad(in.getQBE0PB()); // Partner
      if(in.getQBPMSN().isBlank()) {
         MICommon.setError( "PMSN", "SO94201"); // Mandatory fields are missing
         return;
      }
      HIHED.setPMSN().moveLeftPad(in.getQBPMSN()); // External message number
      if(!HIHED.CHAIN("00", HIHED.getKey("00"))) {
         MICommon.setError( "", "XRE0103"); // Record does not exist
         MICommon.setErrorCode("01");
         return;
      }
      out.setYBCONO().moveLeftPad(MICommon.toAlpha(HIHED.getCONO())); // Company
      out.setYBMSGN().moveLeftPad(HIHED.getMSGN()); // Message number
      MICommon.setData( out.get());
   }


   /**
   *    RCOM19 - Execute command - GetWhsHead
   */
   public void RCOM19() {
      // ----------------------------------------------------------------
      //   Move company from the transaction input structure (MHS850MI1)
      //   and test for validity.
      // ----------------------------------------------------------------
      sMHS850MIRGetWhsHead inGetWhsHead = (sMHS850MIRGetWhsHead)MICommon.getInDS(sMHS850MIRGetWhsHead.class);
      sMHS850MISGetWhsHead outGetWhsHead = (sMHS850MISGetWhsHead)MICommon.getOutDS(sMHS850MISGetWhsHead.class);

      if (MICommon.toNumericCompany(inGetWhsHead.getQ6CONO())) {
         XXCONO = MICommon.getInt();
      } else {
         MICommon.setError("CONO");
         return;
      }
      LDAZD.CONO = XXCONO;
    //-----------------------------------
      //    UTC mode
      UTCmode = isUTCmode(inGetWhsHead.getQ6UTCM());
      if (IN60) {
         MICommon.setError("UTCM", "WUTCM01", inGetWhsHead.getQ6UTCM());
         return;
      }
      //   Test header records for external message records
      HIHED.setCONO(LDAZD.CONO);
      HIHED.setMSGN().move(inGetWhsHead.getQ6MSGN());
      rAPIDS.clear();
      X0OPC.moveLeftPad("*GET");
      rPHEDPIpreCall();
      apCall("MHIHEDPI", rPHEDPI);
      rPHEDPIpostCall();
      IN60 = toBoolean(X0IN60.getChar());
      if (IN60) {
         MICommon.setError( "", X0MSID.toString(), X0MSGD);
         return;
      }
      // ----------------------------------------------------------------
      //   Move data from the execution data formats (MHIHED/MHIPAC/
      //   MHILIN) to the transaction output structure (MHS850MI2)
      // ----------------------------------------------------------------
      outGetWhsHead.setY6CONO().move(XXCONO);
      outGetWhsHead.setY6DIVI().move(HIHED.getDIVI());
      outGetWhsHead.setY6WHLO().move(HIHED.getWHLO());
      if (UTCmode) {
         //   Determine time zone for Warehouse
         XXWHLO.moveLeftPad(HIHED.getWHLO());
         if (!CRCalendar.retrieveTIZOForWHLO(PXMNGTZN, LDAZD.CONO, XXWHLO)) {
            IN60 = true;
            //   MSGID=S_00148 UTC time conversion failed.
            MICommon.setError("GEDT","S_00148");
            return;
         }
         //   time zone for warehouse
         TIZOforWHLO.move(CRCalendar.getTimeZone());
      }
      outGetWhsHead.setY6MSGN().move(HIHED.getMSGN());
      outGetWhsHead.setY6E0IO().move(HIHED.getE0IO());
      outGetWhsHead.setY6QLFR().move(HIHED.getQLFR());
      outGetWhsHead.setY6FACI().move(HIHED.getFACI());
      outGetWhsHead.setY6GEDT().move(MICommon.toAlphaDate(HIHED.getGEDT()));
      outGetWhsHead.setY6GETM().moveLeft(MICommon.toAlphaTime(HIHED.getGETM()));
      // Date generated
      date = HIHED.getGEDT(); 
      time = HIHED.getGETM() / 100;
      if (UTCmode && date > 0) {
         if (!convertToUTC()) {
            IN60 = true;
            //   MSGID=S_00148 UTC time conversion failed.
            MICommon.setError("GEDT","S_00148");
            return;
         }
         HIHED.setGEDT(UTCdate);
         HIHED.setGETM(UTCtime * 100);
         outGetWhsHead.setY6GEDT().move(MICommon.toAlphaDate(HIHED.getGEDT()));
         outGetWhsHead.setY6GETM().moveLeft(MICommon.toAlphaTime(HIHED.getGETM()));
      }
      outGetWhsHead.setY6CHGD().move(MICommon.toAlphaDate(HIHED.getCHGD()));
      outGetWhsHead.setY6STAT().move(HIHED.getSTAT());
      outGetWhsHead.setY6TRSL().move(HIHED.getTRSL());
      outGetWhsHead.setY6TRSH().move(HIHED.getTRSH());
      X0FLDD = 1;
      XXNUMN = (double)HIHED.getADTY();
      XXNUMA.clear();
      RNUMO();
      outGetWhsHead.setY6ADTY().moveLeft(this.PXALPH);
      outGetWhsHead.setY6ADID().move(HIHED.getADID());
      outGetWhsHead.setY6PMSN().move(HIHED.getPMSN());
      outGetWhsHead.setY6E0PA().move(HIHED.getE0PA());
      outGetWhsHead.setY6E0QA().move(HIHED.getE0QA());
      outGetWhsHead.setY6E0PB().move(HIHED.getE0PB());
      outGetWhsHead.setY6E0QB().move(HIHED.getE0QB());
      outGetWhsHead.setY6DONR().move(HIHED.getDONR());
      outGetWhsHead.setY6E007().move(HIHED.getE007());
      outGetWhsHead.setY6E014().move(HIHED.getE014());
      outGetWhsHead.setY6E026().move(HIHED.getE026());
      outGetWhsHead.setY6E035().move(toChar(HIHED.getE035()));
      outGetWhsHead.setY6E065().move(HIHED.getE065());
      outGetWhsHead.setY6E052().move(HIHED.getE052());
      outGetWhsHead.setY6E054().move(HIHED.getE054());
      outGetWhsHead.setY6E051().move(HIHED.getE051());
      outGetWhsHead.setY6E057().move(HIHED.getE057());
      outGetWhsHead.setY6DLSP().move(HIHED.getDLSP());
      outGetWhsHead.setY6EDFR().move(HIHED.getEDFR());
      outGetWhsHead.setY6ORCA().move(HIHED.getORCA());
      outGetWhsHead.setY6RIDN().move(HIHED.getRIDN());
      X0FLDD = 6;
      XXNUMN = (double)HIHED.getRIDO();
      XXNUMA.clear();
      RNUMO();
      outGetWhsHead.setY6RIDO().moveLeft(this.PXALPH);
      X0FLDD = 11;
      XXNUMN = (double)HIHED.getRIDI();
      XXNUMA.clear();
      RNUMO();
      outGetWhsHead.setY6RIDI().moveLeft(this.PXALPH);
      X0FLDD = 3;
      XXNUMN = (double)HIHED.getPLSX();
      XXNUMA.clear();
      RNUMO();
      outGetWhsHead.setY6PLSX().moveLeft(this.PXALPH);
      outGetWhsHead.setY6CUNO().move(HIHED.getCUNO());
      outGetWhsHead.setY6CUOR().move(HIHED.getCUOR());
      outGetWhsHead.setY6SUDO().move(HIHED.getSUDO());
      outGetWhsHead.setY6DNDT().move(MICommon.toAlphaDate(HIHED.getDNDT()));
      outGetWhsHead.setY6PUSN().move(HIHED.getPUSN());
      X0FLDD = 9;
      X0DCCD = 3;
      XXNUMN = HIHED.getGRWE();
      XXNUMA.clear();
      RNUMO();
      outGetWhsHead.setY6GRWE().moveLeft(this.PXALPH);
      X0FLDD = 9;
      X0DCCD = 3;
      XXNUMN = HIHED.getVOL3();
      XXNUMA.clear();
      RNUMO();
      outGetWhsHead.setY6VOL3().moveLeft(this.PXALPH);
      outGetWhsHead.setY6SUNO().move(HIHED.getSUNO());
      X0FLDD = 1;
      XXNUMN = (double)HIHED.getSUTY();
      XXNUMA.clear();
      RNUMO();
      outGetWhsHead.setY6SUTY().moveLeft(this.PXALPH);
      outGetWhsHead.setY6SORN().move(HIHED.getSORN());
      outGetWhsHead.setY6DPNR().move(HIHED.getDPNR());
      outGetWhsHead.setY6RSAG().move(HIHED.getRSAG());
      outGetWhsHead.setY6DLDT().move(MICommon.toAlphaDate(HIHED.getDLDT()));
      outGetWhsHead.setY6DLTM().moveLeft(MICommon.toAlphaTime(HIHED.getDLTM()));
      outGetWhsHead.setY6SHD4().move(MICommon.toAlphaDate(HIHED.getSHD4()));
      X0FLDD = 6;
      XXNUMN = (double)HIHED.getSHTM();
      XXNUMA.clear();
      RNUMO();
      outGetWhsHead.setY6SHTM().moveLeft(this.PXALPH);
      outGetWhsHead.setY6RCDT().move(MICommon.toAlphaDate(HIHED.getRCDT()));
      outGetWhsHead.setY6RCTM().moveLeft(MICommon.toAlphaTime(HIHED.getRCTM()));
      outGetWhsHead.setY6DNTM().moveLeft(MICommon.toAlphaTime(HIHED.getDNTM()));
      outGetWhsHead.setY6MODL().move(HIHED.getMODL());
      outGetWhsHead.setY6E0B4().move(HIHED.getE0B4());
      outGetWhsHead.setY6E0BH().move(HIHED.getE0BH());
      outGetWhsHead.setY6E0B5().move(HIHED.getE0B5());
      outGetWhsHead.setY6TEDL().move(HIHED.getTEDL());
      outGetWhsHead.setY6YREF().move(HIHED.getYREF());
      outGetWhsHead.setY6TFNO().move(HIHED.getTFNO());
      outGetWhsHead.setY6VRNO().move(HIHED.getVRNO());
      outGetWhsHead.setY6RESP().move(HIHED.getRESP());
      X0FLDD = 13;
      XXNUMN = (double)HIHED.getDTID();
      XXNUMA.clear();
      RNUMO();
      outGetWhsHead.setY6DTID().moveLeft(this.PXALPH);
      X0FLDD = 13;
      XXNUMN = (double)HIHED.getTXID();
      XXNUMA.clear();
      RNUMO();
      outGetWhsHead.setY6TXID().moveLeft(this.PXALPH);
      outGetWhsHead.setY6USD1().move(HIHED.getUSD1());
      outGetWhsHead.setY6USD2().move(HIHED.getUSD2());
      outGetWhsHead.setY6USD3().move(HIHED.getUSD3());
      outGetWhsHead.setY6USD4().move(HIHED.getUSD4());
      outGetWhsHead.setY6USD5().move(HIHED.getUSD5());
      outGetWhsHead.setY6RGDT().move(MICommon.toAlphaDate(HIHED.getRGDT()));
      outGetWhsHead.setY6RGTM().moveLeft(MICommon.toAlphaTime(HIHED.getRGTM()));
      outGetWhsHead.setY6LMDT().move(MICommon.toAlphaDate(HIHED.getLMDT()));
      X0FLDD = 3;
      XXNUMN = (double)HIHED.getCHNO();
      XXNUMA.clear();
      RNUMO();
      outGetWhsHead.setY6CHNO().moveLeft(this.PXALPH);
      outGetWhsHead.setY6CHID().move(HIHED.getCHID());
      // Date generated
      date = HIHED.getDLDT(); 
      time = HIHED.getDLTM() / 100;
      if (UTCmode && date > 0) {
         if (!convertToUTC()) {
            IN60 = true;
            //   MSGID=S_00148 UTC time conversion failed.
            MICommon.setError("GEDT","S_00148");
            return;
         }
         HIHED.setDLDT(UTCdate);
         HIHED.setDLTM(UTCtime * 100);
         outGetWhsHead.setY6DLDT().move(MICommon.toAlphaDate(HIHED.getDLDT()));
         outGetWhsHead.setY6DLTM().moveLeft(MICommon.toAlphaTime(HIHED.getDLTM()));
      }
      // Date generated
      date = HIHED.getRCDT(); 
      time = HIHED.getRCTM() / 100;
      if (UTCmode && date > 0) {
         if (!convertToUTC()) {
            IN60 = true;
            //   MSGID=S_00148 UTC time conversion failed.
            MICommon.setError("GEDT","S_00148");
            return;
         }
         HIHED.setRCDT(UTCdate);
         HIHED.setRCTM(UTCtime * 100);
         outGetWhsHead.setY6RCDT().move(MICommon.toAlphaDate(HIHED.getRCDT()));
         outGetWhsHead.setY6RCTM().moveLeft(MICommon.toAlphaTime(HIHED.getRCTM()));
      }
      // 
      date = HIHED.getDNDT(); 
      time = HIHED.getDNTM() / 100;
      if (UTCmode && date > 0) {
         if (!convertToUTC()) {
            IN60 = true;
            //   MSGID=S_00148 UTC time conversion failed.
            MICommon.setError("GEDT","S_00148");
            return;
         }
         HIHED.setDNDT(UTCdate);
         HIHED.setDNTM(UTCtime * 100);
         outGetWhsHead.setY6DNDT().move(MICommon.toAlphaDate(HIHED.getDNDT()));
         outGetWhsHead.setY6DNTM().moveLeft(MICommon.toAlphaTime(HIHED.getDNTM()));
      }
      // 
      date = HIHED.getSHD4(); 
      time = HIHED.getSHTM() / 100;
      if (UTCmode && date > 0) {
         if (!convertToUTC()) {
            IN60 = true;
            //   MSGID=S_00148 UTC time conversion failed.
            MICommon.setError("GEDT","S_00148");
            return;
         }
         HIHED.setSHD4(UTCdate);
         HIHED.setSHTM(UTCtime * 100);
         outGetWhsHead.setY6SHD4().move(MICommon.toAlphaDate(HIHED.getSHD4()));
         outGetWhsHead.setY6SHTM().moveLeft(MICommon.toAlphaTime(HIHED.getSHTM()));
      }
      MICommon.setData( outGetWhsHead.get());
   }

   public void RCOM20() {
      sMHS850MIRGetWhsLine inGetWhsLine = (sMHS850MIRGetWhsLine)MICommon.getInDS(sMHS850MIRGetWhsLine.class);
      sMHS850MISGetWhsLine outGetWhsLine = (sMHS850MISGetWhsLine)MICommon.getOutDS(sMHS850MISGetWhsLine.class);

      if (MICommon.toNumericCompany(inGetWhsLine.getQ8CONO())) {
         XXCONO = MICommon.getInt();
      } else {
         MICommon.setError("CONO");
         return;
      }
      LDAZD.CONO = XXCONO;
    //-----------------------------------
      //    UTC mode
      UTCmode = isUTCmode(inGetWhsLine.getQ8UTCM());
      if (IN60) {
         MICommon.setError("UTCM", "WUTCM01", inGetWhsLine.getQ8UTCM());
         return;
      }
      //   Test header records for external message records
      HILIN.setCONO(LDAZD.CONO);
      HILIN.setMSGN().move(inGetWhsLine.getQ8MSGN());
      HILIN.setPACN().move(inGetWhsLine.getQ8PACN());
      X0FLDD = 5;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inGetWhsLine.getQ8MSLN());
      RNUM();
      HILIN.setMSLN((int)this.PXNUM);
      rAPIDS.clear();
      X0OPC.moveLeftPad("*GET");
      rPLINPIpreCall();
      apCall("MHILINPI", rPLINPI);
      rPLINPIpostCall();
      IN60 = toBoolean(X0IN60.getChar());
      if (IN60) {
         MICommon.setError( "", X0MSID.toString(), X0MSGD);
         return;
      }
      //   Get product data
      ITMAS.setCONO(LDAZD.CONO);
      ITMAS.setITNO().move(HILIN.getITNO());
      if (!ITMAS.CHAIN("00", ITMAS.getKey("00"))) {
         //   MSGID=WIT0103 Item number &1 does not exist
         MICommon.setError( "", "WIT0103", ITMAS.getITNO());
         return;
      }
      // ----------------------------------------------------------------
      outGetWhsLine.setY8CONO().move(XXCONO);
      outGetWhsLine.setY8DIVI().move(HILIN.getDIVI());
      outGetWhsLine.setY8WHLO().move(HILIN.getWHLO());
      if (UTCmode) {
         //   Determine time zone for Warehouse
         XXWHLO.moveLeftPad(HILIN.getWHLO());
         if (!CRCalendar.retrieveTIZOForWHLO(PXMNGTZN, LDAZD.CONO, XXWHLO)) {
            IN60 = true;
            //   MSGID=S_00148 UTC time conversion failed.
            MICommon.setError("GEDT","S_00148");
            return;
         }
         //   time zone for warehouse
         TIZOforWHLO.move(CRCalendar.getTimeZone());
      }
      outGetWhsLine.setY8MSGN().move(HILIN.getMSGN());
      outGetWhsLine.setY8PACN().move(HILIN.getPACN());
      X0FLDD = 5;
      XXNUMN = (double)HILIN.getMSLN();
      XXNUMA.clear();
      RNUMO();
      outGetWhsLine.setY8MSLN().moveLeft(this.PXALPH);
      outGetWhsLine.setY8QLFR().move(HILIN.getQLFR());
      outGetWhsLine.setY8QLFS().move(HILIN.getQLFS());
      outGetWhsLine.setY8FACI().move(HILIN.getFACI());
      outGetWhsLine.setY8GEDT().move(MICommon.toAlphaDate(HILIN.getGEDT()));
      outGetWhsLine.setY8GETM().moveLeft(MICommon.toAlphaTime(HILIN.getGETM()));
      // Date generated
      date = HILIN.getGEDT(); 
      time = HILIN.getGETM() / 100;
      if (UTCmode && date > 0) {
         if (!convertToUTC()) {
            IN60 = true;
            //   MSGID=S_00148 UTC time conversion failed.
            MICommon.setError("GEDT","S_00148");
            return;
         }
         HIHED.setGEDT(UTCdate);
         HIHED.setGETM(UTCtime * 100);
         outGetWhsLine.setY8GEDT().move(MICommon.toAlphaDate(HIHED.getGEDT()));
         outGetWhsLine.setY8GETM().moveLeft(MICommon.toAlphaTime(HIHED.getGETM()));
      }
      outGetWhsLine.setY8CHGD().move(MICommon.toAlphaDate(HILIN.getCHGD()));
      outGetWhsLine.setY8STAT().move(HILIN.getSTAT());
      outGetWhsLine.setY8ITNO().move(HILIN.getITNO());
      outGetWhsLine.setY8ITDS().move(HILIN.getITDS());
      outGetWhsLine.setY8WHSL().move(HILIN.getWHSL());
      outGetWhsLine.setY8BANO().move(HILIN.getBANO());
      outGetWhsLine.setY8CAMU().move(HILIN.getCAMU());
      X0FLDD = 10;
      XXNUMN = (double)HILIN.getREPN();
      XXNUMA.clear();
      RNUMO();
      outGetWhsLine.setY8REPN().moveLeft(this.PXALPH);
      X0FLDD = 5;
      XXNUMN = (double)HILIN.getRELI();
      XXNUMA.clear();
      RNUMO();
      outGetWhsLine.setY8RELI().moveLeft(this.PXALPH);
      outGetWhsLine.setY8POPN().move(HILIN.getPOPN());
      outGetWhsLine.setY8ALWQ().move(HILIN.getALWQ());
      X0FLDD = 2;
      XXNUMA.clear();
      XXNUMN = (double)HILIN.getALWT();
      RNUMO();
      outGetWhsLine.setY8ALWT().moveLeft(this.PXALPH);
      XXQTYN = HILIN.getDLQT();
      XXQTYA.clear();
      this.PXDCCD = X1DCCD;
      RQTYO();
      outGetWhsLine.setY8DLQT().moveLeft(this.PXALPH);
      outGetWhsLine.setY8UNIT().move(HILIN.getUNIT());
      XXQTYN = HILIN.getDLQA();
      XXQTYA.clear();
      this.PXDCCD = X1DCCD;
      RQTYO();
      outGetWhsLine.setY8DLQA().moveLeft(this.PXALPH);
      outGetWhsLine.setY8ALUN().move(HILIN.getALUN());
      X0FLDD = 9;
      X0DCCD = 3;
      XXNUMN = HILIN.getVOL3();
      XXNUMA.clear();
      RNUMO();
      outGetWhsLine.setY8VOL3().moveLeft(this.PXALPH);
      X0FLDD = 9;
      X0DCCD = 3;
      XXNUMN = HILIN.getNEWE();
      XXNUMA.clear();
      RNUMO();
      outGetWhsLine.setY8NEWE().moveLeft(this.PXALPH);
      X0FLDD = 9;
      X0DCCD = 3;
      XXNUMN = HILIN.getGRWE();
      XXNUMA.clear();
      RNUMO();
      outGetWhsLine.setY8GRWE().moveLeft(this.PXALPH);
      XXQTYN = HILIN.getD1QT();
      XXQTYA.clear();
      this.PXDCCD = X1DCCD;
      RQTYO();
      outGetWhsLine.setY8D1QT().moveLeft(this.PXALPH);
      outGetWhsLine.setY8DLSP().move(HILIN.getDLSP());
      XXQTYN = HILIN.getRVQA();
      XXQTYA.clear();
      this.PXDCCD = X1DCCD;
      RQTYO();
      outGetWhsLine.setY8RVQA().moveLeft(this.PXALPH);
      outGetWhsLine.setY8PUUN().move(HILIN.getPUUN());
      XXQTYN = HILIN.getALQT();
      XXQTYA.clear();
      this.PXDCCD = X1DCCD;
      RQTYO();
      outGetWhsLine.setY8ALQT().moveLeft(this.PXALPH);
      XXQTYN = HILIN.getPAQT();
      XXQTYA.clear();
      this.PXDCCD = X1DCCD;
      RQTYO();
      outGetWhsLine.setY8PAQT().moveLeft(this.PXALPH);
      XXQTYN = HILIN.getCAWE();
      XXQTYA.clear();
      this.PXDCCD = X2DCCD;
      RQTYO();
      outGetWhsLine.setY8CAWE().moveLeft(this.PXALPH);
      X0FLDD = 5;
      X0DCCD = 2;
      XXNUMN = HILIN.getPOCY();
      XXNUMA.clear();
      RNUMO();
      outGetWhsLine.setY8POCY().moveLeft(this.PXALPH);
      X0FLDD = 1;
      XXNUMN = (double)HILIN.getOEND();
      XXNUMA.clear();
      RNUMO();
      outGetWhsLine.setY8OEND().moveLeft(this.PXALPH);
      outGetWhsLine.setY8BREF().move(HILIN.getBREF());
      outGetWhsLine.setY8BRE2().move(HILIN.getBRE2());
      outGetWhsLine.setY8FLOC().move(HILIN.getFLOC());
      outGetWhsLine.setY8ORCA().move(HILIN.getORCA());
      X0FLDD = 2;
      XXNUMN = (double)HILIN.getTTYP();
      XXNUMA.clear();
      RNUMO();
      outGetWhsLine.setY8TTYP().moveLeft(this.PXALPH);
      outGetWhsLine.setY8RIDN().move(HILIN.getRIDN());
      X0FLDD = 6;
      XXNUMN = (double)HILIN.getRIDO();
      XXNUMA.clear();
      RNUMO();
      outGetWhsLine.setY8RIDO().moveLeft(this.PXALPH);
      X0FLDD = 6;
      XXNUMN = (double)HILIN.getRIDL();
      XXNUMA.clear();
      RNUMO();
      outGetWhsLine.setY8RIDL().moveLeft(this.PXALPH);
      X0FLDD = 3;
      XXNUMN = (double)HILIN.getRIDX();
      XXNUMA.clear();
      RNUMO();
      outGetWhsLine.setY8RIDX().moveLeft(this.PXALPH);
      X0FLDD = 11;
      XXNUMN = (double)HILIN.getRIDI();
      XXNUMA.clear();
      RNUMO();
      outGetWhsLine.setY8RIDI().moveLeft(this.PXALPH);
      X0FLDD = 3;
      XXNUMN = (double)HILIN.getPLSX();
      XXNUMA.clear();
      RNUMO();
      outGetWhsLine.setY8PLSX().moveLeft(this.PXALPH);
      X0FLDD = 11;
      XXNUMN = (double)HILIN.getDLIX();
      XXNUMA.clear();
      RNUMO();
      outGetWhsLine.setY8DLIX().moveLeft(this.PXALPH);
      outGetWhsLine.setY8DNNO().move(HILIN.getDNNO());
      outGetWhsLine.setY8CUOR().move(HILIN.getCUOR());
      outGetWhsLine.setY8CUNO().move(HILIN.getCUNO());
      outGetWhsLine.setY8ADID().move(HILIN.getADID());
      outGetWhsLine.setY8SUNO().move(HILIN.getSUNO());
      X0FLDD = 1;
      XXNUMN = (double)HILIN.getSUTY();
      XXNUMA.clear();
      RNUMO();
      outGetWhsLine.setY8SUTY().moveLeft(this.PXALPH);
      outGetWhsLine.setY8SUDO().move(HILIN.getSUDO());
      outGetWhsLine.setY8DNDT().move(MICommon.toAlphaDate(HILIN.getDNDT()));
      outGetWhsLine.setY8EXPI().move(MICommon.toAlphaDate(HILIN.getEXPI()));
      outGetWhsLine.setY8CNDT().move(MICommon.toAlphaDate(HILIN.getCNDT()));
      outGetWhsLine.setY8SEDT().move(MICommon.toAlphaDate(HILIN.getSEDT()));
      outGetWhsLine.setY8QCRA().move(HILIN.getQCRA());
      outGetWhsLine.setY8SCRE().move(HILIN.getSCRE());
      outGetWhsLine.setY8BREM().move(HILIN.getBREM());
      outGetWhsLine.setY8TRTP().move(HILIN.getTRTP());
      outGetWhsLine.setY8SITE().move(HILIN.getSITE());
      outGetWhsLine.setY8SITD().move(HILIN.getSITD());
      outGetWhsLine.setY8PUPR().moveLeft(HILIN.getPUPR(), 17, 6);
      outGetWhsLine.setY8PPUN().move(HILIN.getPPUN());
      outGetWhsLine.setY8CUCD().move(HILIN.getCUCD());
      X0FLDD = 5;
      XXNUMN = (double)HILIN.getPUCD();
      XXNUMA.clear();
      RNUMO();
      outGetWhsLine.setY8PUCD().moveLeft(this.PXALPH);
      X0FLDD = 15;
      X0DCCD = 2;
      XXNUMN = HILIN.getLNAM();
      XXNUMA.clear();
      RNUMO();
      outGetWhsLine.setY8LNAM().moveLeft(this.PXALPH);
      X0FLDD = 2;
      XXNUMN = (double)HILIN.getVTCD();
      XXNUMA.clear();
      RNUMO();
      outGetWhsLine.setY8VTCD().moveLeft(this.PXALPH);
      outGetWhsLine.setY8AGNB().move(HILIN.getAGNB());
      outGetWhsLine.setY8RESP().move(HILIN.getRESP());
      X0FLDD = 13;
      XXNUMN = (double)HILIN.getDTID();
      XXNUMA.clear();
      RNUMO();
      outGetWhsLine.setY8DTID().moveLeft(this.PXALPH);
      X0FLDD = 13;
      XXNUMN = HILIN.getTXID();
      XXNUMA.clear();
      RNUMO();
      outGetWhsLine.setY8TXID().moveLeft(this.PXALPH);
      outGetWhsLine.setY8USD1().move(HILIN.getUSD1());
      outGetWhsLine.setY8USD2().move(HILIN.getUSD2());
      outGetWhsLine.setY8USD3().move(HILIN.getUSD3());
      outGetWhsLine.setY8USD4().move(HILIN.getUSD4());
      outGetWhsLine.setY8USD5().move(HILIN.getUSD5());
      outGetWhsLine.setY8RGDT().move(MICommon.toAlphaDate(HILIN.getRGDT()));
      outGetWhsLine.setY8RGTM().moveLeft(MICommon.toAlphaTime(HILIN.getRGTM()));
      outGetWhsLine.setY8LMDT().move(MICommon.toAlphaDate(HILIN.getLMDT()));
      X0FLDD = 3;
      XXNUMN = (double)HILIN.getCHNO();
      XXNUMA.clear();
      RNUMO();
      outGetWhsLine.setY8CHNO().moveLeft(this.PXALPH);
      outGetWhsLine.setY8CHID().move(HILIN.getCHID());
      MICommon.setData( outGetWhsLine.get());
   }

   public void RCOM21() {
      // ----------------------------------------------------------------
      //   Move company from the transaction input structure (MHS850MI1)
      //   and test for validity.
      // ----------------------------------------------------------------
      sMHS850MIRGetWhsPack inGetWhsPack = (sMHS850MIRGetWhsPack)MICommon.getInDS(sMHS850MIRGetWhsPack.class);
      sMHS850MISGetWhsPack outGetWhsPack = (sMHS850MISGetWhsPack)MICommon.getOutDS(sMHS850MISGetWhsPack.class);

      if (MICommon.toNumericCompany(inGetWhsPack.getQ7CONO())) {
         XXCONO = MICommon.getInt();
      } else {
         MICommon.setError("CONO");
         return;
      }
      LDAZD.CONO = XXCONO;
      //-----------------------------------
      //    UTC mode
      UTCmode = isUTCmode(inGetWhsPack.getQ7UTCM());
      if (IN60) {
         MICommon.setError("UTCM", "WUTCM01", inGetWhsPack.getQ7UTCM());
         return;
      }
      //   Test header records for external message records
      HIPAC.setCONO(LDAZD.CONO);
      HIPAC.setMSGN().move(inGetWhsPack.getQ7MSGN());
      HIPAC.setPACN().move(inGetWhsPack.getQ7PACN());
      rAPIDS.clear();
      X0OPC.moveLeftPad("*GET");
      rPPACPIpreCall();
      apCall("MHIPACPI", rPPACPI);
      rPPACPIpostCall();
      IN60 = toBoolean(X0IN60.getChar());
      if (IN60) {
         MICommon.setError( "", X0MSID.toString(), X0MSGD);
         return;
      }
      //   Default decimals to maximimum allowed since no specific
      //   product id.
      ITMAS.setDCCD(6);
      // ----------------------------------------------------------------
      outGetWhsPack.setY7CONO().move(XXCONO);
      outGetWhsPack.setY7DIVI().move(HIPAC.getDIVI());
      outGetWhsPack.setY7WHLO().move(HIPAC.getWHLO());
      if (UTCmode) {
         //   Determine time zone for Warehouse
         XXWHLO.moveLeftPad(HIPAC.getWHLO());
         if (!CRCalendar.retrieveTIZOForWHLO(PXMNGTZN, LDAZD.CONO, XXWHLO)) {
            IN60 = true;
            //   MSGID=S_00148 UTC time conversion failed.
            MICommon.setError("GEDT","S_00148");
            return;
         }
         //   time zone for warehouse
         TIZOforWHLO.move(CRCalendar.getTimeZone());
      }
      outGetWhsPack.setY7MSGN().move(HIPAC.getMSGN());
      outGetWhsPack.setY7PACN().move(HIPAC.getPACN());
      outGetWhsPack.setY7QLFR().move(HIPAC.getQLFR());
      outGetWhsPack.setY7GEDT().move(MICommon.toAlphaDate(HIPAC.getGEDT()));
      outGetWhsPack.setY7GETM().moveLeft(MICommon.toAlphaTime(HIPAC.getGETM()));
      // Date generated
      date = HIPAC.getGEDT(); 
      time = HIPAC.getGETM() / 100;
      if (UTCmode && date > 0) {
         if (!convertToUTC()) {
            IN60 = true;
            //   MSGID=S_00148 UTC time conversion failed.
            MICommon.setError("GEDT","S_00148");
            return;
         }
         HIPAC.setGEDT(UTCdate);
         HIPAC.setGETM(UTCtime * 100);
         outGetWhsPack.setY7GEDT().move(MICommon.toAlphaDate(HIPAC.getGEDT()));
         outGetWhsPack.setY7GETM().moveLeft(MICommon.toAlphaTime(HIPAC.getGETM()));
      }
      outGetWhsPack.setY7CHGD().move(MICommon.toAlphaDate(HIPAC.getCHGD()));
      outGetWhsPack.setY7PARE().move(HIPAC.getPARE());
      X0FLDD = 1;
      XXNUMN = HIPAC.getDIPA();
      XXNUMA.clear();
      RNUMO();
      outGetWhsPack.setY7DIPA().moveLeft(this.PXALPH);
      outGetWhsPack.setY7DSDT().move(MICommon.toAlphaDate(HIPAC.getDSDT()));
      X0FLDD = 4;
      XXNUMN = HIPAC.getDSHM();
      XXNUMA.clear();
      RNUMO();
      outGetWhsPack.setY7DSHM().moveLeft(this.PXALPH);
      // Date generated
      date = HIPAC.getDSDT(); 
      time = HIPAC.getDSHM() / 100;
      if (UTCmode && date > 0) {
         if (!convertToUTC()) {
            IN60 = true;
            //   MSGID=S_00148 UTC time conversion failed.
            MICommon.setError("GEDT","S_00148");
            return;
         }
         HIPAC.setDSDT(UTCdate);
         HIPAC.setDSHM(UTCtime * 100);
         outGetWhsPack.setY7DSDT().move(MICommon.toAlphaDate(HIPAC.getDSDT()));
         outGetWhsPack.setY7DSHM().moveLeft(MICommon.toAlphaTime(HIPAC.getDSHM()));
      }
      X0FLDD = 3;
      XXNUMN = HIPAC.getRODN();
      XXNUMA.clear();
      RNUMO();
      outGetWhsPack.setY7RODN().moveLeft(this.PXALPH);
      X0FLDD = 7;
      XXNUMN = HIPAC.getCONN();
      XXNUMA.clear();
      RNUMO();
      outGetWhsPack.setY7CONN().moveLeft(this.PXALPH);
      X0FLDD = 1;
      XXNUMN = HIPAC.getCDEL();
      XXNUMA.clear();
      RNUMO();
      outGetWhsPack.setY7CDEL().moveLeft(this.PXALPH);
      X0FLDD = 11;
      XXNUMN = HIPAC.getNDLX();
      XXNUMA.clear();
      RNUMO();
      outGetWhsPack.setY7NDLX().moveLeft(this.PXALPH);
      outGetWhsPack.setY7ROUT().move(HIPAC.getROUT());
      outGetWhsPack.setY7SUDO().move(HIPAC.getSUDO());
      outGetWhsPack.setY7DNNO().move(HIPAC.getDNNO());
      outGetWhsPack.setY7DNDT().move(MICommon.toAlphaDate(HIPAC.getDNDT()));
      outGetWhsPack.setY7STAT().move(HIPAC.getSTAT());
      outGetWhsPack.setY7TRSL().move(HIPAC.getTRSL());
      outGetWhsPack.setY7TRSH().move(HIPAC.getTRSH());
      X0FLDD = 3;
      XXNUMN = HIPAC.getPACO();
      XXNUMA.clear();
      RNUMO();
      outGetWhsPack.setY7PACO().moveLeft(this.PXALPH);
      X0FLDD = 1;
      XXNUMN = HIPAC.getPACU();
      XXNUMA.clear();
      RNUMO();
      outGetWhsPack.setY7PACU().moveLeft(this.PXALPH);
      outGetWhsPack.setY7PACC().move(HIPAC.getPACC());
      X0FLDD = 7;
      XXNUMN = HIPAC.getAMKO();
      XXNUMA.clear();
      RNUMO();
      outGetWhsPack.setY7AMKO().moveLeft(this.PXALPH);
      outGetWhsPack.setY7PACT().move(HIPAC.getPACT());
      outGetWhsPack.setY7PACK().move(HIPAC.getPACK());
      outGetWhsPack.setY7TEPA().move(HIPAC.getTEPA());
      outGetWhsPack.setY7SORT().move(HIPAC.getSORT());
      outGetWhsPack.setY7DLRM().move(HIPAC.getDLRM());
      outGetWhsPack.setY7DLMO().move(HIPAC.getDLMO());
      outGetWhsPack.setY7DLSP().move(HIPAC.getDLSP());
      X0FLDD = 9;
      X0DCCD = 3;
      XXNUMN = HIPAC.getVOM3();
      XXNUMA.clear();
      RNUMO();
      outGetWhsPack.setY7VOM3().moveLeft(this.PXALPH);
      X0FLDD = 11;
      X0DCCD = 3;
      XXNUMN = HIPAC.getFRCP();
      XXNUMA.clear();
      RNUMO();
      outGetWhsPack.setY7FRCP().moveLeft(this.PXALPH);
      X0DCCD = 3;
      XXNUMN = HIPAC.getNEWE();
      XXNUMA.clear();
      RNUMO();
      outGetWhsPack.setY7NEWE().moveLeft(this.PXALPH);
      X0FLDD = 9;
      X0DCCD = 3;
      XXNUMN = HIPAC.getGRWE();
      XXNUMA.clear();
      RNUMO();
      outGetWhsPack.setY7GRWE().moveLeft(this.PXALPH);
      X0FLDD = 9;
      X0DCCD = 3;
      XXNUMN = HIPAC.getGRW3();
      XXNUMA.clear();
      RNUMO();
      outGetWhsPack.setY7GRW3().moveLeft(this.PXALPH);
      X0FLDD = 9;
      X0DCCD = 3;
      XXNUMN = HIPAC.getGRW4();
      XXNUMA.clear();
      RNUMO();
      outGetWhsPack.setY7GRW4().moveLeft(this.PXALPH);
      X0FLDD = 9;
      X0DCCD = 3;
      XXNUMN = HIPAC.getGRW5();
      XXNUMA.clear();
      RNUMO();
      outGetWhsPack.setY7GRW5().moveLeft(this.PXALPH);
      XXQTYN = HIPAC.getD1QT();
      XXQTYA.clear();
      this.PXDCCD = X1DCCD;
      RQTYO();
      outGetWhsPack.setY7D1QT().moveLeft(this.PXALPH);
      X0FLDD = 9;
      X0DCCD = 3;
      XXNUMN = HIPAC.getVOMT();
      XXNUMA.clear();
      RNUMO();
      outGetWhsPack.setY7VOMT().moveLeft(this.PXALPH);
      X0FLDD = 5;
      X0DCCD = 3;
      XXNUMN = HIPAC.getPACL();
      XXNUMA.clear();
      RNUMO();
      outGetWhsPack.setY7PACL().moveLeft(this.PXALPH);
      X0FLDD = 5;
      X0DCCD = 3;
      XXNUMN = HIPAC.getPACW();
      XXNUMA.clear();
      RNUMO();
      outGetWhsPack.setY7PACW().moveLeft(this.PXALPH);
      X0FLDD = 5;
      X0DCCD = 3;
      XXNUMN = HIPAC.getPACH();
      XXNUMA.clear();
      RNUMO();
      outGetWhsPack.setY7PACH().moveLeft(this.PXALPH);
      outGetWhsPack.setY7SSCC().move(HIPAC.getSSCC());
      outGetWhsPack.setY7WHSL().move(HIPAC.getWHSL());
      outGetWhsPack.setY7CUNO().move(HIPAC.getCUNO());
      outGetWhsPack.setY7ADID().move(HIPAC.getADID());
      outGetWhsPack.setY7SUNO().move(HIPAC.getSUNO());
      X0FLDD = 1;
      XXNUMN = HIPAC.getSUTY();
      XXNUMA.clear();
      RNUMO();
      outGetWhsPack.setY7SUTY().moveLeft(this.PXALPH);
      outGetWhsPack.setY7RESP().move(HIPAC.getRESP());
      X0FLDD = 13;
      XXNUMN = HIPAC.getDTID();
      XXNUMA.clear();
      RNUMO();
      outGetWhsPack.setY7DTID().moveLeft(this.PXALPH);
      X0FLDD = 13;
      XXNUMN = HIPAC.getTXID();
      XXNUMA.clear();
      RNUMO();
      outGetWhsPack.setY7TXID().moveLeft(this.PXALPH);
      outGetWhsPack.setY7USD1().move(HIPAC.getUSD1());
      outGetWhsPack.setY7USD2().move(HIPAC.getUSD2());
      outGetWhsPack.setY7USD3().move(HIPAC.getUSD3());
      outGetWhsPack.setY7USD4().move(HIPAC.getUSD4());
      outGetWhsPack.setY7USD5().move(HIPAC.getUSD5());
      outGetWhsPack.setY7PPNB().move(HIPAC.getPPNB());
      outGetWhsPack.setY7RGDT().move(MICommon.toAlphaDate(HIPAC.getRGDT()));
      outGetWhsPack.setY7RGTM().moveLeft(MICommon.toAlphaTime(HIPAC.getRGTM()));
      outGetWhsPack.setY7LMDT().move(MICommon.toAlphaDate(HIPAC.getLMDT()));
      X0FLDD = 3;
      XXNUMN = HIPAC.getCHNO();
      XXNUMA.clear();
      RNUMO();
      outGetWhsPack.setY7CHNO().moveLeft(this.PXALPH);
      outGetWhsPack.setY7CHID().move(HIPAC.getCHID());
      MICommon.setData( outGetWhsPack.get());
   }

   public void RCOM22() {
      sMHS850MIRPrcWhsTran inPrcWhsTran = (sMHS850MIRPrcWhsTran)MICommon.getInDS(sMHS850MIRPrcWhsTran.class);

      whsTrans(inPrcWhsTran.getQACONO(), inPrcWhsTran.getQAMSGN(), inPrcWhsTran.getQAPACN(), inPrcWhsTran.getQAMSLN(), inPrcWhsTran.getQAPRFL());
   }

   public void whsTrans(MvxString CONO, MvxString MSGN, MvxString PACN, MvxString MSLN, MvxString PRFL) {
      if (MICommon.toNumericCompany(CONO)) {
         XXCONO = MICommon.getInt();
      } else {
         IN60 = true;
         MICommon.setError("CONO");
         return;
      }
      LDAZD.CONO = XXCONO;
      HIHED.setCONO(XXCONO);
      //   Test header records for external message records
      HIHED.setMSGN().move(MSGN);
      rAPIDS.clear();
      X0OPC.moveLeftPad("*GET");
      rPHEDPIpreCall();
      apCall("MHIHEDPI", rPHEDPI);
      rPHEDPIpostCall();
      IN60 = toBoolean(X0IN60.getChar());
      if (IN60) {
         MICommon.setError( "", X0MSID.toString(), X0MSGD);
         return;
      }
      if (!PACN.isBlank()) {
         HIPAC.setCONO(HIHED.getCONO());
         HIPAC.setMSGN().move(MSGN);
         HIPAC.setPACN().move(PACN);
         rAPIDS.clear();
         X0OPC.moveLeftPad("*GET");
         rPPACPIpreCall();
         apCall("MHIPACPI", rPPACPI);
         rPPACPIpostCall();
         IN60 = toBoolean(X0IN60.getChar());
         if (IN60) {
            MICommon.setError( "", X0MSID.toString(), X0MSGD);
            return;
         }
      }
      if (!MSLN.isBlank()) {
         HILIN.setCONO(HIHED.getCONO());
         HILIN.setMSGN().move(MSGN);
         HILIN.setPACN().move(PACN);
         X0FLDD = 5;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(MSLN);
         RNUM();
         HILIN.setMSLN((int)this.PXNUM);
         rAPIDS.clear();
         X0OPC.moveLeftPad("*GET");
         rPLINPIpreCall();
         apCall("MHILINPI", rPLINPI);
         rPLINPIpostCall();
         IN60 = toBoolean(X0IN60.getChar());
         if (IN60) {
            MICommon.setError( "", X0MSID.toString(), X0MSGD);
            return;
         }
      }
      HIQLF.setCONO(HIHED.getCONO());
      HIQLF.setQLFR().move(HIHED.getQLFR());
      IN91 = !HIQLF.CHAIN("00", HIQLF.getKey("00"));
      if (PRFL.EQ("*AUT")) {
         RAUT();
      } else {
         rAPIDS.clear();
         switch (0) {
         default:
            if (!MSLN.isBlank()) {
               X0OPC.moveLeftPad("MM_0019");
               break;
            }
            if (!PACN.isBlank()) {
               X0OPC.moveLeftPad("MM_0017");
               break;
            }
            X0OPC.moveLeftPad("MM_0015");
            break;
         }
         if (MICommon.isTransaction("AddCfmPickList") || MICommon.isTransaction("AddPOClose") || MICommon.isTransaction("AddPickSftPacLn")) {
            X0OPC.moveLeftPad("MM_0015");
         }
         if (PRFL.isBlank()) {
            rMHS870preCall();
            apCall("MHS870", rMHS870);
            rMHS870postCall();
            IN60 = toBoolean(X0IN60.getChar());
            if (IN60) {
               MICommon.setError( "", X0MSID.toString(), X0MSGD);
               return;
            }
         }
         if (PRFL.EQ("*EXE")) {
            switch (0) {
            default:
               if (!MSLN.isBlank()) {
                  X0OPC.moveLeftPad("MM_0020");
                  break;
               }
               if (!PACN.isBlank()) {
                  X0OPC.moveLeftPad("MM_0018");
                  break;
               }
               X0OPC.moveLeftPad("MM_0016");
               break;
            }
            if (MICommon.isTransaction("AddPickSftPacLn")) {
               X0OPC.moveLeftPad("MM_0016");
            }
            rMHS870preCall();
            apCall("MHS870", rMHS870);
            rMHS870postCall();
            IN60 = toBoolean(X0IN60.getChar());
            if (IN60) {
               MICommon.setError( "", X0MSID.toString(), X0MSGD);
               return;
            }
         }
      }
   }

   /**
   *    RCOM23 - Execute command - AddPOInspect
   */
   public void RCOM23() {
      sMHS850MIRAddPOInspect inAddPOInspect = (sMHS850MIRAddPOInspect)MICommon.getInDS(sMHS850MIRAddPOInspect.class);
      sMHS850MISAddPOInspect outAddPOInspect = (sMHS850MISAddPOInspect)MICommon.getOutDS(sMHS850MISAddPOInspect.class);

      HIHED.clear();
      HIPAC.clear();
      HILIN.clear();
      // ----------------------------------------------------------------
      //   Save process option.
      // ----------------------------------------------------------------
      XXPRFL.move(inAddPOInspect.getQIPRFL());
      XXOPC.move("*ADD");

      if (MICommon.toNumericCompany(inAddPOInspect.getQICONO())) {
         XXCONO = MICommon.getInt();
      } else {
         MICommon.setError("CONO");
         return;
      }

      LDAZD.CONO = XXCONO;
    //-----------------------------------
      //    UTC mode
      UTCmode = isUTCmode(inAddPOInspect.getQIUTCM());
      if (IN60) {
         MICommon.setError("UTCM", "WUTCM01", inAddPOInspect.getQIUTCM());
         return;
      }
      //-----------------------------------
      // Message type
      XXE065.moveLeftPad(inAddPOInspect.getQIE065());
      RE065();
      if (IN60) {
         return;
      }
      // Check Warehouse
      XXWHLO.moveLeftPad(inAddPOInspect.getQIWHLO());
      RWHL();
      if (IN60) {
         return;
      }
      if (UTCmode) {
         //   Determine time zone for Warehouse
         if (!CRCalendar.retrieveTIZOForWHLO(PXMNGTZN, LDAZD.CONO, XXWHLO)) {
            IN60 = true;
            //   MSGID=S_00148 UTC time conversion failed.
            MICommon.setError("GEDT","S_00148");
            return;
         }
         //   time zone for warehouse
         TIZOforWHLO.move(CRCalendar.getTimeZone());
      }
      //-----------------------------------
      //   Retreive message number if blank
      if (inAddPOInspect.getQIMSGN().isBlank()) {
         RVMSNR();
         inAddPOInspect.setQIMSGN().moveLeft(PXRTVNBR.PXNBNR, 10);
      } else {
         HIHED.setCONO(LDAZD.CONO);
         HIHED.setMSGN().move(inAddPOInspect.getQIMSGN());
         rAPIDS.clear();
         X0OPC.moveLeftPad("*GET");
         rPHEDPIpreCall();
         apCall("MHIHEDPI", rPHEDPI);
         rPHEDPIpostCall();
         //         IN60 = toBoolean(X0IN60.getChar());
      }
      //   Default package number if blank
      if (inAddPOInspect.getQIPACN().isBlank()) {
         inAddPOInspect.setQIPACN().moveLeft(inAddPOInspect.getQIMSGN());
      }
      HIPAC.setCONO(LDAZD.CONO);
      HIPAC.setMSGN().move(inAddPOInspect.getQIMSGN());
      HIPAC.setPACN().move(inAddPOInspect.getQIPACN());
      rAPIDS.clear();
      X0OPC.moveLeftPad("*GET");
      rPPACPIpreCall();
      apCall("MHIPACPI", rPPACPI);
      rPPACPIpostCall();
      IN60 = toBoolean(X0IN60.getChar());
      HILIN.setCONO(LDAZD.CONO);
      HILIN.setMSGN().move(inAddPOInspect.getQIMSGN());
      HILIN.setPACN().move(inAddPOInspect.getQIPACN());
      HILIN.setMSLN(0);
      //   Retreive facility for warehouse
      ITWHL.setCONO(LDAZD.CONO);
      ITWHL.setWHLO().move(inAddPOInspect.getQIWHLO());
      if (!ITWHL.CHAIN("00", ITWHL.getKey("00"))) {
         //   MSGID=WWH0103 Warehouse &1 does not exist
         MICommon.setError( "", "WWH0103", inAddPOInspect.getQIWHLO());
         return;
      } else {
         HIHED.setFACI().move(ITWHL.getFACI());
      }
      //   Format date/time
      if (inAddPOInspect.getQIGEDT().isBlank() ||
            inAddPOInspect.getQIGETM().isBlank()) {
         XXGEDT = movexDate();
         XXGETM = movexTime();
      } else {
         if (!MICommon.toNumericDate(inAddPOInspect.getQIGEDT())) {
            MICommon.setError("GEDT");
            return;
         }
         XXGEDT = MICommon.getNumericDate();

         if (!MICommon.toNumeric(inAddPOInspect.getQIGETM())) {
            MICommon.setError("GETM");
            return;
         }
         XXGETM = MICommon.getInt();
         // Date generated
         date = XXGEDT; 
         time = XXGETM / 100;
         if (UTCmode && date > 0) {
            if (!convertFromUTC()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("GEDT","S_00148");
               return;
            }
            XXGEDT = UTCdate;
            XXGETM = UTCtime * 100;
         }
      }
      // ----------------------------------------------------------------
      //   Move data from the transaction input structure (MHS850MI1)
      //   to the execution data formats (MHIHED/MHIPAC/MHILIN)
      // ----------------------------------------------------------------
      HIHED.setCONO(LDAZD.CONO);
      HIPAC.setCONO(LDAZD.CONO);
      HILIN.setCONO(LDAZD.CONO);
      HIHED.setQLFR().move("21  ");
      HIPAC.setQLFR().move("21  ");
      HILIN.setQLFR().move("21  ");
      //---------------------------------------
      //---------------------------------------
      HILIN.setTTYP(25);
      HIHED.setE0IO('I');
      HIHED.setWHLO().move(inAddPOInspect.getQIWHLO());
      HIPAC.setWHLO().move(inAddPOInspect.getQIWHLO());
      HILIN.setWHLO().move(inAddPOInspect.getQIWHLO());
      HIHED.setMSGN().move(inAddPOInspect.getQIMSGN());
      HIPAC.setMSGN().move(inAddPOInspect.getQIMSGN());
      HILIN.setMSGN().move(inAddPOInspect.getQIMSGN());
      HIHED.setPMSN().moveLeft(inAddPOInspect.getQIMSGN());
      HIPAC.setPACN().move(inAddPOInspect.getQIPACN());
      HILIN.setPACN().move(inAddPOInspect.getQIPACN());
      HIHED.setGEDT(XXGEDT);
      HIPAC.setGEDT(XXGEDT);
      HILIN.setGEDT(XXGEDT);
      HIHED.setGETM(XXGETM);
      HIPAC.setGETM(XXGETM);
      HILIN.setGETM(XXGETM);
      HIHED.setE0PA().move(inAddPOInspect.getQIE0PA());
      HIHED.setE0PB().move(inAddPOInspect.getQIE0PA());
      HIHED.setE035(0);
      HIHED.setE065().move(inAddPOInspect.getQIE065());
      HIHED.setSUNO().move(inAddPOInspect.getQISUNO());
      HIHED.setRESP().move(inAddPOInspect.getQIRESP());
      HILIN.setRESP().move(inAddPOInspect.getQIRESP());
      HIPAC.setRESP().move(inAddPOInspect.getQIRESP());
      X0FLDD = 1;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddPOInspect.getQISUTY().getChar());
      RNUM();
      HIHED.setSUTY((int)this.PXNUM);
      HIHED.setADID().move(inAddPOInspect.getQIADID());
      HILIN.setRIDN().move(inAddPOInspect.getQIRIDN());
      X0FLDD = 6;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddPOInspect.getQIRIDL());
      RNUM();
      HILIN.setRIDL((int)this.PXNUM);
      X0FLDD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddPOInspect.getQIRIDX());
      RNUM();
      HILIN.setRIDX((int)this.PXNUM);
      HILIN.setITNO().move(inAddPOInspect.getQIITNO());
      XXITNO.moveLeftPad(HILIN.setITNO());
      RITNO();
      //--------------------------------
      HILIN.setPOPN().move(inAddPOInspect.getQIPOPN());
      HILIN.setALWQ().move(inAddPOInspect.getQIALWQ());
      X0FLDD = 2;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddPOInspect.getQIALWT());
      RNUM();
      HILIN.setALWT((int)this.PXNUM);
      HILIN.setWHSL().move(inAddPOInspect.getQIWHSL());
      HILIN.setBANO().move(inAddPOInspect.getQIBANO());
      HILIN.setCAMU().move(inAddPOInspect.getQICAMU());
      HILIN.setBREM().move(inAddPOInspect.getQIBREM());
      HILIN.setBREF().move(inAddPOInspect.getQIBREF());
      HILIN.setBRE2().move(inAddPOInspect.getQIBRE2());
      HILIN.setQCRA(inAddPOInspect.getQIQCRA().getChar());
      HILIN.setSCRE().move(inAddPOInspect.getQISCRE());
      PLINE.setCONO(LDAZD.CONO);
      PLINE.setPUNO().moveLeft(HILIN.getRIDN());
      PLINE.setPNLI(HILIN.getRIDL());
      if (PLINE.CHAIN("00", PLINE.getKey("00", 3))) {
         ITMAS.setCONO(LDAZD.CONO);
         ITMAS.setITNO().move(PLINE.getITNO());
         if (ITMAS.CHAIN("00", ITMAS.getKey("00"))) {
            X1DCCD = ITMAS.getDCCD();
         }
         if (ITMAS.getUNMS().NE(PLINE.getPUUN())) {
            if (cRefALUNext.getMITAUN(ITAUN, false, LDAZD.CONO, ITMAS.getITNO(), 1, PLINE.getPUUN())) {
               X1DCCD = ITAUN.getDCCD();
            }
         }
      }
      XXQTYN = 0d;
      XXQTYA.clear();
      XXQTYA.moveLeft(inAddPOInspect.getQIQTY());
      this.PXDCCD = X1DCCD;
      RQTY();
      HILIN.setRVQA(this.PXNUM);
      X0FLDD = 1;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddPOInspect.getQIOEND().getChar());
      RNUM();
      HILIN.setOEND((int)this.PXNUM);

      if (!MICommon.toNumericDate(inAddPOInspect.getQIEXPI())) {
         MICommon.setError("EXPI");
         return;
      }
      HILIN.setEXPI(MICommon.getNumericDate());

      if (!MICommon.toNumericDate(inAddPOInspect.getQISEDT())) {
         MICommon.setError("SEDT");
         return;
      }
      HILIN.setSEDT(MICommon.getNumericDate());

      if (!MICommon.toNumericDate(inAddPOInspect.getQICNDT())) {
         MICommon.setError("CNDT");
         return;
      }
      HILIN.setCNDT(MICommon.getNumericDate());

      X0FLDD = 10;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddPOInspect.getQIREPN());
      RNUM();
      HILIN.setREPN((long)this.PXNUM);
      HILIN.setUSD1().move(inAddPOInspect.getQIUSD1());
      HILIN.setUSD2().move(inAddPOInspect.getQIUSD2());
      HILIN.setUSD3().move(inAddPOInspect.getQIUSD3());
      HILIN.setUSD4().move(inAddPOInspect.getQIUSD4());
      HILIN.setUSD5().move(inAddPOInspect.getQIUSD5());
      HIHED.setE0IO('I');
      if (!inAddPOInspect.getQIPMSN().isBlank()) {
         HIHED.setPMSN().move(inAddPOInspect.getQIPMSN());
      } else {
         HIHED.setPMSN().moveLeft(inAddPOInspect.getQIMSGN());
      }
      if (IN75) {
         IN60 = true;
         return;
      }
      //---------------------------------------------------
      RHEAD();
      if (IN60 && IN76) {
         return;
      }
      RPACK();
      if (IN60 && IN76) {
         return;
      }
      //   Validate/create transaction line data
      RLINE();
      if (IN60) {
         return;
      }

      outAddPOInspect.setYICONO().move(XXCONO);
      outAddPOInspect.setYIMSGN().move(HIHED.getMSGN());
      MICommon.setData( outAddPOInspect.get());
   }

   public void RCOM37() {
      // ----------------------------------------------------------------
      //   Clear transaction records
      // ----------------------------------------------------------------
      sMHS850MIRAddPOClose inAddPOClose = (sMHS850MIRAddPOClose)MICommon.getInDS(sMHS850MIRAddPOClose.class);
      sMHS850MISAddPOClose outAddPOClose = (sMHS850MISAddPOClose)MICommon.getOutDS(sMHS850MISAddPOClose.class);

      HIHED.clear();
      HIPAC.clear();
      HILIN.clear();
      // ----------------------------------------------------------------
      //   Save process option.
      // ----------------------------------------------------------------
      XXPRFL.move(inAddPOClose.getQVPRFL());
      XXOPC.move("*ADD");
      if (MICommon.toNumericCompany(inAddPOClose.getQVCONO())) {
         XXCONO = MICommon.getInt();
      } else {
         MICommon.setError("CONO");
         return;
      }

      LDAZD.CONO = XXCONO;
      //-----------------------------------
      // Message type
      XXE065.moveLeftPad(inAddPOClose.getQVE065());
      RE065();
      if (IN60) {
         return;
      }
      // Check Warehouse
      XXWHLO.moveLeftPad(inAddPOClose.getQVWHLO());
      RWHL();
      if (IN60) {
         return;
      }
      //-----------------------------------
      if (inAddPOClose.getQVE0PA().isBlank()) {
         //   MSGID = WE00A02  Partner does not exist
         MICommon.setError( "", "WE00A02");
         return;
      }
      if (inAddPOClose.getQVE065().isBlank()) {
         //   MSGID = ED01016  Message type does not exist for partner
         MICommon.setError( "", "ED01016");
         return;
      }
      if (inAddPOClose.getQVRIDN().isBlank()) {
         //   MSGID = CA30003  Order number must be entered
         MICommon.setError( "", "CA30003");
         return;
      }
      if (inAddPOClose.getQVRIDL().isBlank() &&
            !inAddPOClose.getQVRIDX().isBlank()) {
         //   MSGID = CA30003  Order line must be entered
         MICommon.setError( "", "WRI0202");
         return;
      }
      if (!inAddPOClose.getQVRIDL().isBlank() &&
            inAddPOClose.getQVRIDX().isBlank()) {
         //   MSGID = CA30003  Order line suffix must be entered
         MICommon.setError( "", "WPO0202");
         return;
      }
      //   Retreive message number if blank
      if (inAddPOClose.getQVMSGN().isBlank()) {
         RVMSNR();
         inAddPOClose.setQVMSGN().moveLeft(PXRTVNBR.PXNBNR, 10);
      } else {
         HIHED.setCONO(LDAZD.CONO);
         HIHED.setMSGN().move(inAddPOClose.getQVMSGN());
         rAPIDS.clear();
         X0OPC.moveLeftPad("*GET");
         rPHEDPIpreCall();
         apCall("MHIHEDPI", rPHEDPI);
         rPHEDPIpostCall();
         //         IN60 = toBoolean(X0IN60.getChar());
      }
      //   Default package number if blank
      if (inAddPOClose.getQVPACN().isBlank()) {
         inAddPOClose.setQVPACN().moveLeft(inAddPOClose.getQVMSGN());
      }
      HIPAC.setCONO(LDAZD.CONO);
      HIPAC.setMSGN().move(inAddPOClose.getQVMSGN());
      HIPAC.setPACN().move(inAddPOClose.getQVPACN());
      rAPIDS.clear();
      X0OPC.moveLeftPad("*GET");
      rPPACPIpreCall();
      apCall("MHIPACPI", rPPACPI);
      rPPACPIpostCall();
      ITWHL.setCONO(LDAZD.CONO);
      ITWHL.setWHLO().move(inAddPOClose.getQVWHLO());
      if (!ITWHL.CHAIN("00", ITWHL.getKey("00"))) {
         //   MSGID=WWH0103 Warehouse &1 does not exist
         MICommon.setError( "", "WWH0103", ITWHL.getWHLO());
         return;
      } else {
         HIHED.setFACI().move(ITWHL.getFACI());
      }
      HIHED.setCONO(LDAZD.CONO);
      HIPAC.setCONO(LDAZD.CONO);
      HIHED.setQLFR().move("23  ");
      HIPAC.setQLFR().move("23  ");
      //   Check length of package number
      HIHED.setE0IO('I');
      HIHED.setWHLO().move(inAddPOClose.getQVWHLO());
      HIPAC.setWHLO().move(inAddPOClose.getQVWHLO());
      HIHED.setMSGN().move(inAddPOClose.getQVMSGN());
      HIPAC.setMSGN().move(inAddPOClose.getQVMSGN());
      HIHED.setPMSN().moveLeft(inAddPOClose.getQVMSGN());
      HIPAC.setPACN().move(inAddPOClose.getQVPACN());
      HIHED.setE0PA().move(inAddPOClose.getQVE0PA());
      HIHED.setE0PB().move(inAddPOClose.getQVE0PA());
      HIHED.setE035(0);
      HIHED.setE065().move(inAddPOClose.getQVE065());
      if (!inAddPOClose.getQVPMSN().isBlank()) {
         HIHED.setPMSN().move(inAddPOClose.getQVPMSN());
      } else {
         HIHED.setPMSN().moveLeft(inAddPOClose.getQVMSGN());
      }
      PHEAD.setCONO(LDAZD.CONO);
      PHEAD.setPUNO().moveLeft(inAddPOClose.getQVRIDN());
      if (PHEAD.CHAIN("00", PHEAD.getKey("00", 2))) {
         HIHED.setSUNO().moveLeft(PHEAD.getSUNO());
         HIPAC.setSUNO().moveLeft(PHEAD.getSUNO());
         XXSUNO.move(PHEAD.getSUNO());
         IDMAS.setCONO(LDAZD.CONO);
         IDMAS.setSUNO().moveLeft(XXSUNO);
         if (IDMAS.CHAIN("00", IDMAS.getKey("00", 2))) {
            HIHED.setSUTY(IDMAS.getSUTY());
         }
      }
      //   Validate/create transaction header data
      RHEAD();
      if (IN60 && IN76) {
         return;
      }
      //   Validate/create transaction package data
      RPACK();
      if (IN60 && IN76) {
         return;
      }
      //   Validate/create transaction line data
      PLINE.setCONO(LDAZD.CONO);
      PLINE.setPUNO().moveLeft(inAddPOClose.getQVRIDN());
      if (inAddPOClose.getQVRIDL().isBlank()) {
         PLINE.SETLL("00", PLINE.getKey("00", 2));
         IN93 = !PLINE.READE("00", PLINE.getKey("00", 2));
      } else {
         X0FLDD = 6;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inAddPOClose.getQVRIDL());
         RNUM();
         HILIN.setRIDL((int)this.PXNUM);
         X0FLDD = 3;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inAddPOClose.getQVRIDX());
         RNUM();
         HILIN.setRIDX((int)this.PXNUM);
         PLINE.setPNLI(HILIN.getRIDL());
         PLINE.setPNLS(HILIN.getRIDX());
         PLINE.SETLL("00", PLINE.getKey("00", 4));
         IN93 = !PLINE.READE("00", PLINE.getKey("00", 4));
      }
      XXREAD = 1;
      while (!IN93) {
         if (PLINE.getPNLS() != 0) {
            HILIN.setRIDL(PLINE.getPNLI());
            HILIN.setRIDX(PLINE.getPNLS());
         } else {
            HILIN.setRIDL(PLINE.getPNLI());
         }
         //--------------------------------------------------------
         LDAZZ.TDTA.clear();
         if (inAddPOClose.getQVRIDL().isBlank()) {
            LDAZZ.TDTA.moveLeftPad("ALL");
         }
         if (!inAddPOClose.getQVRIDL().isBlank() && inAddPOClose.getQVRIDX().isBlank()) {
            LDAZZ.TDTA.moveLeftPad("LIN");
         }
         //--------------------------------------------------------
         HILIN.setCONO(LDAZD.CONO);
         HILIN.setMSGN().move(inAddPOClose.getQVMSGN());
         HILIN.setPACN().move(inAddPOClose.getQVPACN());
         HILIN.setQLFR().move("23  ");
         HILIN.setTTYP(20);
         HILIN.setWHLO().move(inAddPOClose.getQVWHLO());
         HILIN.setRIDN().move(inAddPOClose.getQVRIDN());
         HILIN.setMSLN(0);
         HILIN.setSUNO().moveLeft(PLINE.getSUNO());
         HILIN.setITNO().moveLeft(PLINE.getITNO());
         HILIN.setUSD1().move(inAddPOClose.getQVUSD1());
         HILIN.setUSD2().move(inAddPOClose.getQVUSD2());
         HILIN.setUSD3().move(inAddPOClose.getQVUSD3());
         HILIN.setUSD4().move(inAddPOClose.getQVUSD4());
         HILIN.setUSD5().move(inAddPOClose.getQVUSD5());
         RLINE();
         if (IN60) {
            return;
         }
         if (XXREAD == 1 && inAddPOClose.getQVRIDL().isBlank()) {
            IN93 = !PLINE.READE("00", PLINE.getKey("00", 2));
         } else if (XXREAD != 1 && inAddPOClose.getQVRIDL().isBlank()) {
            IN93 = true;
         }
         if (XXREAD == 1 && !inAddPOClose.getQVRIDL().isBlank()) {
            IN93 = !PLINE.READE("00", PLINE.getKey("00", 4));
         } else if (XXREAD != 1 &&
               !inAddPOClose.getQVRIDL().isBlank()) {
            IN93 = true;
         }
      }
      if (!XXPRFL.isBlank()) {
         if (XXPRFL.EQ("*AUT") && XXOPC.NE("*DLT")) {
            RAUT();
         } else {
            if (XXPRFL.EQ("*EXE") && XXOPC.NE("*DLT")) {
               rAPIDS.clear();
               X0OPC.moveLeftPad("*CHK");
               rPHEDPIpreCall();
               apCall("MHILINPI", rPHEDPI);
               rPHEDPIpostCall();
               IN60 = toBoolean(SRIMPI.PXIN60.getChar());
               if (IN60) {
                  XXERRM = true;
                  MICommon.setError( "", X0MSID.toString(), X0MSGD);
                  return;
               }
               X0FLDD = 3;
               MvxString MSLN = new MvxString(5);
               X0FLDD = 5;
               XXNUMN = (double)HILIN.getMSLN();
               XXNUMA.clear();
               RNUM();
               MSLN.moveRight(this.PXALPH);
               whsTrans(inAddPOClose.getQVCONO(), inAddPOClose.getQVMSGN(), inAddPOClose.getQVPACN(), MSLN, XXPRFL);
               if (IN60) {
                  XXERRM = true;
               }
            }
         }
      }

      outAddPOClose.setYVCONO().move(XXCONO);
      outAddPOClose.setYVMSGN().move(HIHED.getMSGN());
      MICommon.setData( outAddPOClose.get());
   }

   public void RCOM38() {
      // ----------------------------------------------------------------
      //   Add Pick Via Package
      // ----------------------------------------------------------------
      sMHS850MIRAddPickViaPack inAddPickViaPack = (sMHS850MIRAddPickViaPack)MICommon.getInDS(sMHS850MIRAddPickViaPack.class);
      sMHS850MISAddPickViaPack outAddPickViaPack = (sMHS850MISAddPickViaPack)MICommon.getOutDS(sMHS850MISAddPickViaPack.class);
      
      HIHED.clear();
      HIPAC.clear();
      HILIN.clear();
      XXHEER = false;
      // ----------------------------------------------------------------
      //   Save process option.
      // ----------------------------------------------------------------
      XXPRFL.move(inAddPickViaPack.getQUPRMD());
      XXOPC.move("*ADD");
      if (MICommon.toNumericCompany(inAddPickViaPack.getQUCONO())) {
         XXCONO = MICommon.getInt();
      } else {
         MICommon.setError("CONO");
         return;
      }

      LDAZD.CONO = XXCONO;
    //-----------------------------------
      //    UTC mode
      UTCmode = isUTCmode(inAddPickViaPack.getQUUTCM());
      if (IN60) {
         MICommon.setError("UTCM", "WUTCM01", inAddPickViaPack.getQUUTCM());
         return;
      }
      //-----------------------------------
      // Message type
      XXE065.moveLeftPad(inAddPickViaPack.getQUE065());
      RE065();
      if (IN60) {
         return;
      }
      //-----------------------------------
      // Check For Partner
      if (inAddPickViaPack.getQUE0PA().isBlank()) {
         //   MSGID = WE00A02  Partner does not exist
         MICommon.setError( "", "WE00A02");
         return;
      }
      MIPPT.setE0IO('I');
      MIPPT.setE0PA().moveLeft(inAddPickViaPack.getQUE0PA());
      if (!MIPPT.CHAIN("10", MIPPT.getKey(LDAZD.CONO, "10", 3))) {
         //   MSGID=WE00A03 Partner &1 does not exist
         MICommon.setError( "", "WE00A03", inAddPickViaPack.getQUE0PA());
         return;
      }
      if (inAddPickViaPack.getQUE065().isBlank()) {
         //   MSGID=ED01016   Message Type does not exist for Partner
         MICommon.setError( "", "ED01016");
         return;
      }
      MIPPT.setE065().moveLeft(inAddPickViaPack.getQUE065());
      if (!MIPPT.CHAIN("10", MIPPT.getKey(LDAZD.CONO, "10", 4))) {
         //   MSGID=MM98705 Message Type &1 is not found
         MICommon.setError( "", "MM98705", inAddPickViaPack.getQUE065());
         return;
      }
      // Check Warehouse
      XXWHLO.moveLeftPad(inAddPickViaPack.getQUWHLO());
      RWHL();
      if (IN60) {
         return;
      }
      if (UTCmode) {
         //   Determine time zone for Warehouse
         if (!CRCalendar.retrieveTIZOForWHLO(PXMNGTZN, LDAZD.CONO, XXWHLO)) {
            IN60 = true;
            //   MSGID=S_00148 UTC time conversion failed.
            MICommon.setError("GEDT","S_00148");
            return;
         }
         //   time zone for warehouse
         TIZOforWHLO.move(CRCalendar.getTimeZone());
      }
      //-----------------------------------
      //   Retreive facility for warehouse
      ITWHL.setCONO(LDAZD.CONO);
      ITWHL.setWHLO().move(inAddPickViaPack.getQUWHLO());
      if (!ITWHL.CHAIN("00", ITWHL.getKey("00"))) {
         //   MSGID=WWH0103 Warehouse &1 does not exist
         MICommon.setError( "", "WWH0103", inAddPickViaPack.getQUWHLO());
         return;
      } else {
         HIHED.setFACI().move(ITWHL.getFACI());
      }
      if (inAddPickViaPack.getQUPANR().isBlank() &&
            inAddPickViaPack.getQUSSCC().isBlank()) {
         MICommon.setError( "", "WPA5103", inAddPickViaPack.getQUPANR());
         return;
      }
      PTRNS.setCONO(LDAZD.CONO);
      PTRNS.setDIPA(0);
      X0FLDD = 11;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddPickViaPack.getQUDLIX());
      RNUM();
      PTRNS.setDLIX((long)this.PXNUM);
      PTRNS.setWHLO().move(inAddPickViaPack.getQUWHLO());
      if (!inAddPickViaPack.getQUPANR().isBlank()) {
         PTRNS.setPANR().move(inAddPickViaPack.getQUPANR());
         if (!inAddPickViaPack.getQUDLIX().isBlank()) {
            if (!PTRNS.CHAIN("00", PTRNS.getKey("00"))) {
               MICommon.setError( "", "WPA5103", inAddPickViaPack.getQUPANR());
               return;
            }
         } else {
            PTRNS.setINOU(1);
            PTRNS.SETLL("85", PTRNS.getKey("85", 4));
            if (PTRNS.READE("85", PTRNS.getKey("85",4))) {
               if (PTRNS.READE("85", PTRNS.getKey("85",4))) {
                  //   Delivery number must be entered
                  MICommon.setError( "", "WDL0202");
                  return;
               }
            } else {
               MICommon.setError( "", "WPA5103", inAddPickViaPack.getQUPANR());
               return;
            }
         }
      } else {
         PTRNS.setINOU(1);
         PTRNS.setDIPA(0);
         PTRNS.setSSCC().move(inAddPickViaPack.getQUSSCC());
         IN91 = !PTRNS.CHAIN("70", PTRNS.getKey("70", 6));
         if (inAddPickViaPack.getQUPANR().isBlank()) {
            if (!IN91) {
               HIPAC.setPACN().moveLeftPad(PTRNS.getPANR());
               HILIN.setPACN().moveLeftPad(PTRNS.getPANR());
            } else {
               MICommon.setError( "", "WPA5103", inAddPickViaPack.getQUPANR());
               return;
            }
         } else {
            PAC.clear();
            PAC.moveLeft(PTRNS.getPANR());
            TRIMPAC();
            if (PCC.EQ(inAddPickViaPack.getQUPANR()) && !IN91) {
               HIPAC.setPACN().moveLeftPad(PTRNS.getPANR());
               HILIN.setPACN().move(HIPAC.getPACN());
            } else {
               MICommon.setError( "", "WPA5103", inAddPickViaPack.getQUPANR());
               return;
            }
         }
      }
      if (inAddPickViaPack.getQUMSGN().isBlank()) {
         RVMSNR();
         XXMSGN.moveLeftPad(PXRTVNBR.PXNBNR, 10);
      } else {
         HIHED.setCONO(LDAZD.CONO);
         HIHED.setMSGN().move(inAddPickViaPack.getQUMSGN());
         rAPIDS.clear();
         X0OPC.moveLeftPad("*GET");
         rPHEDPIpreCall();
         apCall("MHIHEDPI", rPHEDPI);
         rPHEDPIpostCall();
      }
      if (inAddPickViaPack.getQUMSGN().isBlank()) {
         inAddPickViaPack.setQUMSGN().moveLeft(XXMSGN);
      }
      HIPAC.setCONO(LDAZD.CONO);
      HIPAC.setMSGN().move(inAddPickViaPack.getQUMSGN());
      HIPAC.setPACN().moveLeftPad(PTRNS.getPANR());
      HILIN.setPACN().moveLeft(HIPAC.getPACN());
      rAPIDS.clear();
      X0OPC.moveLeftPad("*GET");
      rPPACPIpreCall();
      apCall("MHIPACPI", rPPACPI);
      rPPACPIpostCall();
      //   Format date/time
      XXGEDT = movexDate();
      XXGETM = movexTime();
      // ----------------------------------------------------------------
      //   Move data from the transaction input structure (MHS850MI1)   >
      //   to the execution data formats (MHIHED/MHIPAC/MHILIN)         >
      // ----------------------------------------------------------------
      HDISH.setCONO(LDAZD.CONO);
      HDISH.setINOU(1);
      HDISH.setDLIX(PTRNS.getDLIX());
      if (!HDISH.CHAIN("00", HDISH.getKey("00"))) {
         // XRE0103 - Record not found
         MICommon.setError( "", "XRE0103");
         MICommon.setErrorCode("01");
         return;
      }
      // ----------------------------------------------------------------
      HIHED.setCONO(LDAZD.CONO);
      HIPAC.setCONO(LDAZD.CONO);
      HILIN.setCONO(LDAZD.CONO);
      HIHED.setQLFR().moveLeft("CFVP");
      HIPAC.setQLFR().move(HIHED.getQLFR());
      HILIN.setQLFR().move(HIHED.getQLFR());
      //---------------------------------------
      //---------------------------------------
      HIHED.setWHLO().move(inAddPickViaPack.getQUWHLO());
      HIPAC.setWHLO().move(inAddPickViaPack.getQUWHLO());
      HILIN.setWHLO().move(inAddPickViaPack.getQUWHLO());
      HILIN.setFACI().move(HIHED.getFACI());
      HIPAC.setPACT().move(PTRNS.getPACT());
      HIHED.setE0PA().move(inAddPickViaPack.getQUE0PA());
      HIHED.setE0PB().move(inAddPickViaPack.getQUE0PA());
      HIHED.setE065().move(inAddPickViaPack.getQUE065());
      HIHED.setMSGN().move(inAddPickViaPack.getQUMSGN());
      HIHED.setCUNO().move(HDISH.getCONA());
      HIHED.setE0IO('I');
      HIPAC.setMSGN().move(inAddPickViaPack.getQUMSGN());
      HIHED.setGEDT(XXGEDT);
      HIPAC.setGEDT(XXGEDT);
      HIHED.setGETM(XXGETM);
      HIPAC.setGETM(XXGETM);
      if (!inAddPickViaPack.getQUPMSN().isBlank()) {
         HIHED.setPMSN().move(inAddPickViaPack.getQUPMSN());
      } else {
         HIHED.setPMSN().moveLeft(inAddPickViaPack.getQUMSGN());
      }
      RHEAD();
      if (IN60 && IN76) {
         return;
      }
      RPACK();
      if (IN60 && IN76) {
         return;
      }
      //----------------------------------------------------------------------
      HILIN.setMSGN().move(inAddPickViaPack.getQUMSGN());
      HIPAC.setPACN().moveLeftPad(PTRNS.getPANR());
      HILIN.setPACN().moveLeftPad(PTRNS.getPANR());
      HILIN.setMSLN(0);
      HILIN.setQLFR().move(HIPAC.getQLFR());
      HILIN.setDLIX(HDISH.getDLIX());
      HILIN.setTWSL().move(inAddPickViaPack.getQUTWSL());
      //-----------------------------------------------------------------------------------------------
      HILIN.setGEDT(XXGEDT);
      HILIN.setGETM(XXGETM);
      //-----------------------------------------------------------------------------------------------
      if (!inAddPickViaPack.getQUUSD1().isBlank()) {
         HILIN.setUSD1().move(inAddPickViaPack.getQUUSD1());
      }
      if (!inAddPickViaPack.getQUUDS2().isBlank()) {
         HILIN.setUSD2().move(inAddPickViaPack.getQUUDS2());
      }
      if (!inAddPickViaPack.getQUUSD3().isBlank()) {
         HILIN.setUSD3().move(inAddPickViaPack.getQUUSD3());
      }
      if (!inAddPickViaPack.getQUUSD4().isBlank()) {
         HILIN.setUSD4().move(inAddPickViaPack.getQUUSD4());
      }
      if (!inAddPickViaPack.getQUUSD5().isBlank()) {
         HILIN.setUSD5().move(inAddPickViaPack.getQUUSD5());
      }
      //----------------------------------------------------------------------
      if (!MICommon.toNumericDate(inAddPickViaPack.getQURPDT())) {
         MICommon.setError("RPDT");
         return;
      }
      HILIN.setRPDT(MICommon.getNumericDate());
      //--------------------------------------------------------------------
      if (!inAddPickViaPack.getQURPTM().isBlank()) {
         X0FLDD = 6;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inAddPickViaPack.getQURPTM());
         RNUM();
         HILIN.setRPTM((int)this.PXNUM);
      }
      if (!inAddPickViaPack.getQURPDT().isBlank() &&
            inAddPickViaPack.getQURPTM().isBlank()) {
         HILIN.setRPTM(movexTime());
      }
      if (!UTCmode && 
         !inAddPickViaPack.getQURPDT().isBlank() &&
         inAddPickViaPack.getQURPTM().isBlank()) {
         HILIN.setRPTM(movexTime());
      }
      if (UTCmode) {
         // Reporting date
         if (!inAddPickViaPack.getQURPDT().isBlank() &&
             !inAddPickViaPack.getQURPTM().isBlank()) {
            date = HILIN.getRPDT(); 
            time = HILIN.getRPTM() / 100;           
            if (!convertFromUTC()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("RPDT","S_00148");
               return;
            }
            HILIN.setRPDT(UTCdate);
            HILIN.setRPTM(UTCtime * 100);
         } else {
            IN60 = true;
            //   MSGID=S_00148 UTC time conversion failed.
            MICommon.setError("RPDT","S_00148");
            return;
         }
      }
      //----------------------------------------------------------------------
      X0FLDD = 1;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddPickViaPack.getQUISMD());
      RNUM();
      HILIN.setISMD((int)this.PXNUM);
      HILIN.setLODO().moveLeftPad(inAddPickViaPack.getQULODO());
      //----------------------------------------------------------------------
      RLINE();
      if (IN60) {
         return;
      }
      //----------------------------------------------------------------------
      if (!XXPRFL.isBlank()) {
         if (XXPRFL.EQ("*AUT") && XXOPC.NE("*DLT")) {
            RAUT();
         } else {
            if (XXPRFL.EQ("*EXE") && XXOPC.NE("*DLT")) {
               rAPIDS.clear();
               X0OPC.moveLeftPad("*CHK");
               rPHEDPIpreCall();
               apCall("MHILINPI", rPHEDPI);
               rPHEDPIpostCall();
               IN60 = toBoolean(X0IN60.getChar());
               if (IN60) {
                  XXERRM = true;
                  MICommon.setError( "", X0MSID.toString(), X0MSGD);
                  return;
               }
               MvxString MSLN = new MvxString(5);
               X0FLDD = 5;
               XXNUMN = (double)HILIN.getMSLN();
               XXNUMA.clear();
               RNUM();
               MSLN.moveRight(this.PXALPH);
               whsTrans(inAddPickViaPack.getQUCONO(), HILIN.getMSGN(), HILIN.getPACN(), MSLN, XXPRFL);
               if (IN60) {
                  XXERRM = true;
               }
            }
         }
      }

      outAddPickViaPack.setYUCONO().move(XXCONO);
      outAddPickViaPack.setYUMSGN().move(HIHED.getMSGN());
      if (MHS850DS.getJ7STNB() > 0L) {
         X0FLDD = 10;
         XXNUMN = (long)MHS850DS.getJ7STNB();
         XXNUMA.clear();
         RNUMO();
         outAddPickViaPack.setYUSTNB().moveLeft(this.PXALPH);
         X0FLDD = 7;
         XXNUMN = (int)MHS850DS.getJ7STRN();
         XXNUMA.clear();
         RNUMO();
         outAddPickViaPack.setYUSTRN().moveLeft(this.PXALPH);
      }
      MICommon.setData( outAddPickViaPack.get());
   }




   public void RCOM40() {
      // ----------------------------------------------------------------
      //   Clear transaction records
      // ----------------------------------------------------------------
      sMHS850MIRAddDOPackRec inAddDOPackRec = (sMHS850MIRAddDOPackRec)MICommon.getInDS(sMHS850MIRAddDOPackRec.class);
      sMHS850MISAddDOPackRec outAddDOPackRec = (sMHS850MISAddDOPackRec)MICommon.getOutDS(sMHS850MISAddDOPackRec.class);

      HIHED.clear();
      HIPAC.clear();
      HILIN.clear();
      // ----------------------------------------------------------------
      //   Save process option.
      // ----------------------------------------------------------------
      XXPRFL.move(inAddDOPackRec.getQXPRMD());
      XXOPC.move("*ADD");
      if (MICommon.toNumericCompany(inAddDOPackRec.getQXCONO())) {
         XXCONO = MICommon.getInt();
      } else {
         MICommon.setError("CONO");
         return;
      }

      LDAZD.CONO = XXCONO;
      //-----------------------------------
      // Message type
      XXE065.moveLeftPad(inAddDOPackRec.getQXE065());
      RE065();
      if (IN60) {
         return;
      }
      // Check Warehouse
      XXWHLO.moveLeftPad(inAddDOPackRec.getQXWHLO());
      RWHL();
      if (IN60) {
         return;
      }
      //-----------------------------------
      //   Retreive message number if blank
      if (inAddDOPackRec.getQXMSGN().isBlank()) {
         RVMSNR();
         inAddDOPackRec.setQXMSGN().moveLeft(PXRTVNBR.PXNBNR, 10);
      } else {
         HIHED.setCONO(LDAZD.CONO);
         HIHED.setMSGN().move(inAddDOPackRec.getQXMSGN());
         rAPIDS.clear();
         X0OPC.moveLeftPad("*GET");
         rPHEDPIpreCall();
         apCall("MHIHEDPI", rPHEDPI);
         rPHEDPIpostCall();
      }
      PTRNSfound = false;
      inputDLIX.moveLeftPad(inAddDOPackRec.getQXDLIX());
      inputPANR.moveLeftPad(inAddDOPackRec.getQXPACN());
      inputSSCC.moveLeftPad(inAddDOPackRec.getQXSSCC());
      numberPackage = 0;
      if (!validInput()) {
         return;
      }
      //   Default package number if blank
      if (inAddDOPackRec.getQXPACN().isBlank()) {
         //If package number and SSCC no is blank receive per dlix
         if (inAddDOPackRec.getQXSSCC().isBlank()) {
            inAddDOPackRec.setQXPACN().moveLeftPad("RECEIVEALL");
         }
         //If package number is blank and SSCC no is filled try to find PACN
         if (!inAddDOPackRec.getQXSSCC().isBlank()) {
            PTRNS.setCONO(LDAZD.CONO);
            PTRNS.setINOU(2);
            PTRNS.setSSCC().move(inAddDOPackRec.getQXSSCC());
            if (PTRNS.CHAIN("70", PTRNS.getKey("70", 3))) {
               PTRNSfound = true;
               inAddDOPackRec.setQXPACN().moveLeftPad(PTRNS.getPANR());
            } else {
               //   MSGID-WSSCC03 SSCC number does not exist
               MICommon.setError( "", "WSSCC03", inAddDOPackRec.getQXSSCC());
               return;
            }
         }
      }
      HIPAC.setCONO(LDAZD.CONO);
      HIPAC.setMSGN().move(inAddDOPackRec.getQXMSGN());
      HIPAC.setPACN().move(inAddDOPackRec.getQXPACN());
      rAPIDS.clear();
      X0OPC.moveLeftPad("*GET");
      rPPACPIpreCall();
      apCall("MHIPACPI", rPPACPI);
      rPPACPIpostCall();
      HILIN.setCONO(LDAZD.CONO);
      HILIN.setMSGN().move(inAddDOPackRec.getQXMSGN());
      HILIN.setPACN().move(inAddDOPackRec.getQXPACN());
      HILIN.setMSLN(0);
      //   Retreive facility for warehouse
      ITWHL.setCONO(LDAZD.CONO);
      ITWHL.setWHLO().move(inAddDOPackRec.getQXWHLO());
      if (!ITWHL.CHAIN("00", ITWHL.getKey("00"))) {
         //   MSGID=WWH0103 Warehouse &1 does not exist
         MICommon.setError( "", "WWH0103", inAddDOPackRec.getQXWHLO());
         return;
      } else {
         HIHED.setFACI().move(ITWHL.getFACI());
      }
      //   Format date/time
      XXGEDT = movexDate();
      XXGETM = movexTime();
      // ----------------------------------------------------------------
      //   Move data from the transaction input structure (MHS850MI1)
      //   to the execution data formats (MHIHED/MHIPAC/MHILIN)
      // ----------------------------------------------------------------
      HIHED.setCONO(LDAZD.CONO);
      HIPAC.setCONO(LDAZD.CONO);
      HILIN.setCONO(LDAZD.CONO);
      HIHED.setQLFR().move("50PA");
      HIPAC.setQLFR().move("50PA");
      HILIN.setQLFR().move("50PA");
      //---------------------------------------
      //---------------------------------------
      HILIN.setTTYP(50);
      HIHED.setE0IO('I');
      HIHED.setWHLO().move(inAddDOPackRec.getQXWHLO());
      HIPAC.setWHLO().move(inAddDOPackRec.getQXWHLO());
      HILIN.setWHLO().move(inAddDOPackRec.getQXWHLO());
      HIHED.setMSGN().move(inAddDOPackRec.getQXMSGN());
      HIPAC.setMSGN().move(inAddDOPackRec.getQXMSGN());
      HILIN.setMSGN().move(inAddDOPackRec.getQXMSGN());
      HIHED.setPMSN().moveLeft(inAddDOPackRec.getQXMSGN());
      HIPAC.setPACN().move(inAddDOPackRec.getQXPACN());
      HILIN.setPACN().move(inAddDOPackRec.getQXPACN());
      HIPAC.setSSCC().move(inAddDOPackRec.getQXSSCC());
      HIHED.setGEDT(XXGEDT);
      HIPAC.setGEDT(XXGEDT);
      HILIN.setGEDT(XXGEDT);
      HIHED.setGETM(XXGETM);
      HIPAC.setGETM(XXGETM);
      HILIN.setGETM(XXGETM);
      HIHED.setE0PA().move(inAddDOPackRec.getQXE0PA());
      HIHED.setE0PB().move(inAddDOPackRec.getQXE0PA());
      HIHED.setE035(0);
      HIHED.setE065().move(inAddDOPackRec.getQXE065());
      HIHED.setCUNO().moveLeft(inAddDOPackRec.getQXWHLO());
      HILIN.setTWSL().move(inAddDOPackRec.getQXTWSL());
      HILIN.setWHSL().move(inAddDOPackRec.getQXTWSL());
      if (!PTRNSfound) {
         X0FLDD = 11;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inAddDOPackRec.getQXDLIX());
         RNUM();
         HILIN.setDLIX((long)this.PXNUM);
      } else {
         HILIN.setDLIX(PTRNS.getDLIX());
      }
      HIHED.setE0IO('I');
      //----------------------------------------------------
      //   Validate/create transaction header data
      RHEAD();
      if (IN60 && IN76) {
         return;
      }
      RPACK();
      if (IN60 && IN76) {
         return;
      }
      RLINE();
      if (IN60) {
         return;
      }

      outAddDOPackRec.setYXCONO().move(XXCONO);
      outAddDOPackRec.setYXMSGN().move(HIHED.getMSGN());
      MICommon.setData( outAddDOPackRec.get());
   }

   public void RCOM41() {
      // ----------------------------------------------------------------
      //   Clear transaction records
      // ----------------------------------------------------------------
      sMHS850MIRAddPutAwayPack inAddPutAwayPack = (sMHS850MIRAddPutAwayPack)MICommon.getInDS(sMHS850MIRAddPutAwayPack.class);
      sMHS850MISAddPutAwayPack outAddPutAwayPack = (sMHS850MISAddPutAwayPack)MICommon.getOutDS(sMHS850MISAddPutAwayPack.class);

      HIHED.clear();
      HIPAC.clear();
      HILIN.clear();
      // ----------------------------------------------------------------
      //   Save process option.
      // ----------------------------------------------------------------
      XXPRFL.move(inAddPutAwayPack.getQWPRMD());
      XXOPC.move("*ADD");
      if (MICommon.toNumericCompany(inAddPutAwayPack.getQWCONO())) {
         XXCONO = MICommon.getInt();
      } else {
         MICommon.setError("CONO");
         return;
      }

      LDAZD.CONO = XXCONO;
      //-----------------------------------
      // Message type
      XXE065.moveLeftPad(inAddPutAwayPack.getQWE065());
      RE065();
      if (IN60) {
         return;
      }
      // Check Warehouse
      XXWHLO.moveLeftPad(inAddPutAwayPack.getQWWHLO());
      RWHL();
      if (IN60) {
         return;
      }
      //-----------------------------------
      //   Retreive message number if blank
      //----------------------------------------------------------------
      if (inAddPutAwayPack.getQWMSGN().isBlank()) {
         RVMSNR();
         inAddPutAwayPack.setQWMSGN().moveLeft(PXRTVNBR.PXNBNR, 10);
      } else {
         HIHED.setCONO(LDAZD.CONO);
         HIHED.setMSGN().move(inAddPutAwayPack.getQWMSGN());
         rAPIDS.clear();
         X0OPC.moveLeftPad("*GET");
         rPHEDPIpreCall();
         apCall("MHIHEDPI", rPHEDPI);
         rPHEDPIpostCall();
         //         IN60 = toBoolean(X0IN60.getChar());
      }
      //   Default package number if blank
      if (inAddPutAwayPack.getQWPACN().isBlank()) {
         inAddPutAwayPack.setQWPACN().moveLeft(inAddPutAwayPack.getQWMSGN());
      }
      HIPAC.setCONO(LDAZD.CONO);
      HIPAC.setMSGN().move(inAddPutAwayPack.getQWMSGN());
      HIPAC.setPACN().move(inAddPutAwayPack.getQWPACN());
      rAPIDS.clear();
      X0OPC.moveLeftPad("*GET");
      rPPACPIpreCall();
      apCall("MHIPACPI", rPPACPI);
      rPPACPIpostCall();
      //   Retreive next line in sequence
      HILIN.setCONO(LDAZD.CONO);
      HILIN.setMSGN().move(inAddPutAwayPack.getQWMSGN());
      HILIN.setPACN().move(inAddPutAwayPack.getQWPACN());
      HILIN.setMSLN(0);
      //   Retreive facility for warehouse
      ITWHL.setCONO(LDAZD.CONO);
      ITWHL.setWHLO().move(inAddPutAwayPack.getQWWHLO());
      if (!ITWHL.CHAIN("00", ITWHL.getKey("00"))) {
         //   MSGID=WWH0103 Warehouse &1 does not exist
         MICommon.setError( "", "WWH0103", inAddPutAwayPack.getQWWHLO());
         return;
      } else {
         HIHED.setFACI().move(ITWHL.getFACI());
      }
      //   Format date/time
      XXGEDT = movexDate();
      XXGETM = movexTime();
      // ----------------------------------------------------------------
      //   Move data from the transaction input structure (MHS850MI1)   >
      //   to the execution data formats (MHIHED/MHIPAC/MHILIN)         >
      // ----------------------------------------------------------------
      HIHED.setCONO(LDAZD.CONO);
      HIPAC.setCONO(LDAZD.CONO);
      HILIN.setCONO(LDAZD.CONO);
      HIHED.setSTAT().move("10");
      HILIN.setSTAT().move("10");
      HIPAC.setSTAT().move("10");
      HIHED.setQLFR().move("PAPA");
      HIPAC.setQLFR().move("PAPA");
      HILIN.setQLFR().move("PAPA");
      //---------------------------------------
      //---------------------------------------
      HILIN.setTTYP(25);
      HIHED.setE0IO('I');
      HIHED.setWHLO().move(inAddPutAwayPack.getQWWHLO());
      HIPAC.setWHLO().move(inAddPutAwayPack.getQWWHLO());
      HILIN.setWHLO().move(inAddPutAwayPack.getQWWHLO());
      HIHED.setMSGN().move(inAddPutAwayPack.getQWMSGN());
      HIPAC.setMSGN().move(inAddPutAwayPack.getQWMSGN());
      HILIN.setMSGN().move(inAddPutAwayPack.getQWMSGN());
      HIHED.setPMSN().moveLeft(inAddPutAwayPack.getQWMSGN());
      HIPAC.setPACN().move(inAddPutAwayPack.getQWPACN());
      HILIN.setPACN().move(inAddPutAwayPack.getQWPACN());
      HIHED.setGEDT(XXGEDT);
      HIPAC.setGEDT(XXGEDT);
      HILIN.setGEDT(XXGEDT);
      HIHED.setGETM(XXGETM);
      HIPAC.setGETM(XXGETM);
      HILIN.setGETM(XXGETM);
      HIHED.setE0PA().move(inAddPutAwayPack.getQWE0PA());
      HIHED.setE0PB().move(inAddPutAwayPack.getQWE0PA());
      HIHED.setE065().move(inAddPutAwayPack.getQWE065());
      //--------------------------------
      HILIN.setWHSL().move(inAddPutAwayPack.getQWTWSL());
      HILIN.setTWSL().move(inAddPutAwayPack.getQWTWSL());
      HILIN.setTOCA().move(inAddPutAwayPack.getQWSPNO());
      this.PXDCCD = X1DCCD;
      HIHED.setE0IO('I');
      if (IN75) {
         IN60 = true;
         return;
      }
      //-------------------------------------------------
      RHEAD();
      if (IN60 && IN76) {
         return;
      }
      RPACK();
      if (IN60 && IN76) {
         return;
      }
      RLINE();
      if (IN60) {
         return;
      }

      outAddPutAwayPack.setYWCONO().move(XXCONO);
      outAddPutAwayPack.setYWMSGN().move(HIHED.getMSGN());
      MICommon.setData( outAddPutAwayPack.get());
   }


   // AddPOPackInsp
   public void RCOM43() {
      // ----------------------------------------------------------------
      //   Clear transaction records
      // ----------------------------------------------------------------
      sMHS850MIRAddPOPackInsp inAddPOPackInsp = (sMHS850MIRAddPOPackInsp)MICommon.getInDS(sMHS850MIRAddPOPackInsp.class);
      sMHS850MISAddPOPackInsp outAddPOPackInsp = (sMHS850MISAddPOPackInsp)MICommon.getOutDS(sMHS850MISAddPOPackInsp.class);

      HIHED.clear();
      HIPAC.clear();
      HILIN.clear();
      // ----------------------------------------------------------------
      //   Save process option.
      // ----------------------------------------------------------------
      XXPRFL.move(inAddPOPackInsp.getQTPRMD());
      XXOPC.move("*ADD");
      if (MICommon.toNumericCompany(inAddPOPackInsp.getQTCONO())) {
         XXCONO = MICommon.getInt();
      } else {
         MICommon.setError("CONO");
         return;
      }

      LDAZD.CONO = XXCONO;
    //-----------------------------------
      //    UTC mode
      UTCmode = isUTCmode(inAddPOPackInsp.getQTUTCM());
      if (IN60) {
         MICommon.setError("UTCM", "WUTCM01", inAddPOPackInsp.getQTUTCM());
         return;
      }
      //-----------------------------------
      // Message type
      XXE065.moveLeftPad(inAddPOPackInsp.getQTE065());
      RE065();
      if (IN60) {
         return;
      }
      // Check Warehouse
      XXWHLO.moveLeftPad(inAddPOPackInsp.getQTWHLO());
      RWHL();
      if (IN60) {
         return;
      }
      if (UTCmode) {
         //   Determine time zone for Warehouse
         if (!CRCalendar.retrieveTIZOForWHLO(PXMNGTZN, LDAZD.CONO, XXWHLO)) {
            IN60 = true;
            //   MSGID=S_00148 UTC time conversion failed.
            MICommon.setError("GEDT","S_00148");
            return;
         }
         //   time zone for warehouse
         TIZOforWHLO.move(CRCalendar.getTimeZone());
      }
      //-----------------------------------
      //   Retreive message number if blank
      if (inAddPOPackInsp.getQTMSGN().isBlank()) {
         RVMSNR();
         inAddPOPackInsp.setQTMSGN().moveLeft(PXRTVNBR.PXNBNR, 10);
      } else {
         HIHED.setCONO(LDAZD.CONO);
         HIHED.setMSGN().move(inAddPOPackInsp.getQTMSGN());
         rAPIDS.clear();
         X0OPC.moveLeftPad("*GET");
         rPHEDPIpreCall();
         apCall("MHIHEDPI", rPHEDPI);
         rPHEDPIpostCall();
      }
      //   Default package number if blank
      if (inAddPOPackInsp.getQTPACN().isBlank()) {
         inAddPOPackInsp.setQTPACN().moveLeft(inAddPOPackInsp.getQTMSGN());
      }
      HIPAC.setCONO(LDAZD.CONO);
      HIPAC.setMSGN().move(inAddPOPackInsp.getQTMSGN());
      HIPAC.setPACN().move(inAddPOPackInsp.getQTPACN());
      rAPIDS.clear();
      X0OPC.moveLeftPad("*GET");
      rPPACPIpreCall();
      apCall("MHIPACPI", rPPACPI);
      rPPACPIpostCall();
      IN60 = toBoolean(X0IN60.getChar());
      HILIN.setCONO(LDAZD.CONO);
      HILIN.setMSGN().move(inAddPOPackInsp.getQTMSGN());
      HILIN.setPACN().move(inAddPOPackInsp.getQTPACN());
      HILIN.setMSLN(0);
      //   Retreive facility for warehouse
      ITWHL.setCONO(LDAZD.CONO);
      ITWHL.setWHLO().move(inAddPOPackInsp.getQTWHLO());
      if (!ITWHL.CHAIN("00", ITWHL.getKey("00"))) {
         //   MSGID=WWH0103 Warehouse &1 does not exist
         MICommon.setError( "", "WWH0103", inAddPOPackInsp.getQTWHLO());
         return;
      } else {
         HIHED.setFACI().move(ITWHL.getFACI());
      }
      //   Format date/time
      if (inAddPOPackInsp.getQTGEDT().isBlank() ||
            inAddPOPackInsp.getQTGETM().isBlank()) {
         XXGEDT = movexDate();
         XXGETM = movexTime();
      } else {
         if (MICommon.toNumericDate(inAddPOPackInsp.getQTGEDT())) {
            XXGEDT = MICommon.getNumericDate();
         } else {
            MICommon.setError("GEDT");
            return;
         }

         if (!MICommon.toNumeric(inAddPOPackInsp.getQTGETM())) {
            MICommon.setError("GETM");
            return;
         }
         XXGETM = MICommon.getInt();
         // Date generated
         date = XXGEDT; 
         time = XXGETM / 100;
         if (UTCmode && date > 0) {
            if (!convertFromUTC()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("GEDT","S_00148");
               return;
            }
            XXGEDT = UTCdate;
            XXGETM = UTCtime * 100;
         }
      }
      // ----------------------------------------------------------------
      //   Move data from the transaction input structure (MHS850MI1)
      //   to the execution data formats (MHIHED/MHIPAC/MHILIN)
      // ----------------------------------------------------------------
      HIHED.setCONO(LDAZD.CONO);
      HIPAC.setCONO(LDAZD.CONO);
      HILIN.setCONO(LDAZD.CONO);
      HIHED.setQLFR().move("21PA");
      HIPAC.setQLFR().move("21PA");
      HILIN.setQLFR().move("21PA");
      //---------------------------------------
      //---------------------------------------
      HILIN.setTTYP(21);
      HIHED.setE0IO('I');
      HIHED.setWHLO().move(inAddPOPackInsp.getQTWHLO());
      HIPAC.setWHLO().move(inAddPOPackInsp.getQTWHLO());
      HILIN.setWHLO().move(inAddPOPackInsp.getQTWHLO());
      HIHED.setMSGN().move(inAddPOPackInsp.getQTMSGN());
      HIPAC.setMSGN().move(inAddPOPackInsp.getQTMSGN());
      HILIN.setMSGN().move(inAddPOPackInsp.getQTMSGN());
      HIHED.setPMSN().moveLeft(inAddPOPackInsp.getQTMSGN());
      HIHED.setCUNO().moveLeft(inAddPOPackInsp.getQTWHLO());
      HIPAC.setPACN().move(inAddPOPackInsp.getQTPACN());
      HILIN.setPACN().move(inAddPOPackInsp.getQTPACN());
      HILIN.setWHSL().move(inAddPOPackInsp.getQTTWSL());
      HIHED.setGEDT(XXGEDT);
      HIPAC.setGEDT(XXGEDT);
      HILIN.setGEDT(XXGEDT);
      HIHED.setGETM(XXGETM);
      HIPAC.setGETM(XXGETM);
      HILIN.setGETM(XXGETM);
      HIHED.setE0PA().move(inAddPOPackInsp.getQTE0PA());
      HIHED.setE0PB().move(inAddPOPackInsp.getQTE0PA());
      HIHED.setE035(0);
      HIHED.setE065().move(inAddPOPackInsp.getQTE065());
      HIHED.setRESP().move(inAddPOPackInsp.getQTRESP());
      HILIN.setRESP().move(inAddPOPackInsp.getQTRESP());
      HIPAC.setRESP().move(inAddPOPackInsp.getQTRESP());
      HILIN.setBREM().move(inAddPOPackInsp.getQTBREM());
      HILIN.setQCRA(inAddPOPackInsp.getQTQCRA().getChar());
      HILIN.setSCRE().move(inAddPOPackInsp.getQTSCRE());
      X0FLDD = 1;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddPOPackInsp.getQTOEND().getChar());
      RNUM();
      HILIN.setOEND((int)this.PXNUM);
      //----------------------------------------------------
      //   Validate/create transaction header data
      //----------------------------------------------------
      if (IN75) {
         IN60 = true;
         return;
      }
      //----------------------------------------------------
      RHEAD();
      if (IN60 && IN76) {
         return;
      }
      RPACK();
      if (IN60 && IN76) {
         return;
      }
      //   Validate/create transaction line data
      RLINE();
      if (IN60) {
         return;
      }

      outAddPOPackInsp.setYTCONO().move(XXCONO);
      outAddPOPackInsp.setYTMSGN().move(HIHED.getMSGN());
      MICommon.setData( outAddPOPackInsp.get());
   }


   public void RCOM44() {
      // ----------------------------------------------------------------
      //   Add Pick By Package In Stock
      // ----------------------------------------------------------------
      sMHS850MIRAddPickByPacStk inAddPickByPacStk = (sMHS850MIRAddPickByPacStk)MICommon.getInDS(sMHS850MIRAddPickByPacStk.class);
      sMHS850MISAddPickByPacStk outAddPickByPacStk = (sMHS850MISAddPickByPacStk)MICommon.getOutDS(sMHS850MISAddPickByPacStk.class);

      HIHED.clear();
      HIPAC.clear();
      HILIN.clear();
      // ----------------------------------------------------------------
      //   Save process option.
      // ----------------------------------------------------------------
      XXPRFL.move(inAddPickByPacStk.getQ0PRMD());
      XXOPC.move("*ADD");
      if (MICommon.toNumericCompany(inAddPickByPacStk.getQ0CONO())) {
         XXCONO = MICommon.getInt();
      } else {
         MICommon.setError("CONO");
         return;
      }

      LDAZD.CONO = XXCONO;
    //-----------------------------------
      //    UTC mode
      UTCmode = isUTCmode(inAddPickByPacStk.getQ0UTCM());
      if (IN60) {
         MICommon.setError("UTCM", "WUTCM01", inAddPickByPacStk.getQ0UTCM());
         return;
      }
      //-----------------------------------
      // Message type
      XXE065.moveLeftPad(inAddPickByPacStk.getQ0E065());
      RE065();
      if (IN60) {
         return;
      }
      //-----------------------------------
      // Check For Partner
      if (inAddPickByPacStk.getQ0E0PA().isBlank()) {
         IN60 = true;
         //   MSGID = WE00A02  Partner does not exist
         MICommon.setError( "", "WE00A02");
         return;
      }
      MIPPT.setE0IO('I');
      MIPPT.setE0PA().moveLeft(inAddPickByPacStk.getQ0E0PA());
      if (!MIPPT.CHAIN("10", MIPPT.getKey(LDAZD.CONO, "10", 3))) {
         //   MSGID=WE00A03 Partner &1 does not exist
         MICommon.setError( "", "WE00A03", inAddPickByPacStk.getQ0E0PA());
         return;
      }
      if (inAddPickByPacStk.getQ0E065().isBlank()) {
         //   MSGID=ED01016   Message Type does not exist for Partner
         MICommon.setError( "", "ED01016");
         return;
      }
      MIPPT.setE065().moveLeft(inAddPickByPacStk.getQ0E065());
      if (!MIPPT.CHAIN("10", MIPPT.getKey(LDAZD.CONO, "10", 4))) {
         //   MSGID=MM98705 Message Type &1 is not found
         MICommon.setError( "", "MM98705", inAddPickByPacStk.getQ0E065());
         return;
      }
      // Check Warehouse
      XXWHLO.moveLeftPad(inAddPickByPacStk.getQ0WHLO());
      RWHL();
      if (IN60) {
         return;
      }
      if (UTCmode) {
         //   Determine time zone for Warehouse
         if (!CRCalendar.retrieveTIZOForWHLO(PXMNGTZN, LDAZD.CONO, XXWHLO)) {
            IN60 = true;
            //   MSGID=S_00148 UTC time conversion failed.
            MICommon.setError("GEDT","S_00148");
            return;
         }
         //   time zone for warehouse
         TIZOforWHLO.move(CRCalendar.getTimeZone());
      }
      //-----------------------------------
      //   Retreive facility for warehouse
      ITWHL.setCONO(LDAZD.CONO);
      ITWHL.setWHLO().move(inAddPickByPacStk.getQ0WHLO());
      IN91 = !ITWHL.CHAIN("00", ITWHL.getKey("00"));
      if (IN91) {
         //   MSGID=WWH0103 Warehouse &1 does not exist
         MICommon.setError( "", "WWH0103", inAddPickByPacStk.getQ0WHLO());
         return;
      } else {
         HIHED.setFACI().move(ITWHL.getFACI());
      }
      inputDLIX.moveLeftPad(inAddPickByPacStk.getQ0DLIX());
      inputPANR.moveLeftPad(inAddPickByPacStk.getQ0PANR());
      inputSSCC.moveLeftPad(inAddPickByPacStk.getQ0SSCC());
      numberPackage = 1;
      if (!validInput()) {
         return;
      }
      PTRNS.setCONO(LDAZD.CONO);
      PTRNS.setDIPA(1);
      PTRNS.setDLIX(0L);
      PTRNS.setWHLO().clear();
      if (!inAddPickByPacStk.getQ0PANR().isBlank()) {
         PTRNS.setPANR().move(inAddPickByPacStk.getQ0PANR());
         if (!PTRNS.CHAIN("00", PTRNS.getKey("00"))) {
            MICommon.setError( "", "WPA5103", inAddPickByPacStk.getQ0PANR());
            return;
         }
      } else {
         PTRNS.setINOU(0);
         PTRNS.setSSCC().move(inAddPickByPacStk.getQ0SSCC());
         IN91 = !PTRNS.CHAIN("70", PTRNS.getKey("70", 5));
         if (inAddPickByPacStk.getQ0PANR().isBlank()) {
            if (!IN91) {
               HIPAC.setPACN().moveLeftPad(PTRNS.getPANR());
               HILIN.setPACN().moveLeftPad(PTRNS.getPANR());
            } else {
               MICommon.setError( "", "WPA5103", inAddPickByPacStk.getQ0PANR());
               return;
            }
         } else {
            PAC.clear();
            PAC.moveLeft(PTRNS.getPANR());
            TRIMPAC();
            if (PCC.EQ(inAddPickByPacStk.getQ0PANR()) &&
                  !IN91) {
               HIPAC.setPACN().moveLeftPad(PTRNS.getPANR());
               HILIN.setPACN().move(HIPAC.getPACN());
            } else {
               MICommon.setError( "", "WPA5103", inAddPickByPacStk.getQ0PANR());
               return;
            }
         }
      }
      if (inAddPickByPacStk.getQ0MSGN().isBlank()) {
         RVMSNR();
         XXMSGN.moveLeftPad(PXRTVNBR.PXNBNR, 10);
      } else {
         HIHED.setCONO(LDAZD.CONO);
         HIHED.setMSGN().move(inAddPickByPacStk.getQ0MSGN());
         rAPIDS.clear();
         X0OPC.moveLeftPad("*GET");
         rPHEDPIpreCall();
         apCall("MHIHEDPI", rPHEDPI);
         rPHEDPIpostCall();
         //         IN60 = toBoolean(X0IN60.getChar());
      }
      if (inAddPickByPacStk.getQ0MSGN().isBlank()) {
         inAddPickByPacStk.setQ0MSGN().moveLeft(XXMSGN);
      }
      HIPAC.setCONO(LDAZD.CONO);
      HIPAC.setMSGN().move(inAddPickByPacStk.getQ0MSGN());
      HIPAC.setPACN().moveLeftPad(PTRNS.getPANR());
      HILIN.setPACN().moveLeft(HIPAC.getPACN());
      rAPIDS.clear();
      X0OPC.moveLeftPad("*GET");
      rPPACPIpreCall();
      apCall("MHIPACPI", rPPACPI);
      rPPACPIpostCall();
      //   Format date/time
      XXGEDT = movexDate();
      XXGETM = movexTime();
      // ----------------------------------------------------------------
      //   Move data from the transaction input structure (MHS850MI1)   >
      //   to the execution data formats (MHIHED/MHIPAC/MHILIN)         >
      // ----------------------------------------------------------------
      HIHED.setCONO(LDAZD.CONO);
      HIPAC.setCONO(LDAZD.CONO);
      HILIN.setCONO(LDAZD.CONO);
      HIHED.setQLFR().moveLeft("CFPA");
      HIPAC.setQLFR().move(HIHED.getQLFR());
      HILIN.setQLFR().move(HIPAC.getQLFR());
      //---------------------------------------
      //---------------------------------------
      HIHED.setWHLO().move(inAddPickByPacStk.getQ0WHLO());
      HIPAC.setWHLO().move(inAddPickByPacStk.getQ0WHLO());
      HILIN.setWHLO().move(inAddPickByPacStk.getQ0WHLO());
      HILIN.setFACI().move(HIHED.getFACI());
      HIPAC.setPACT().move(PTRNS.getPACT());
      HIHED.setE0PA().move(inAddPickByPacStk.getQ0E0PA());
      HIHED.setE0PB().move(inAddPickByPacStk.getQ0E0PA());
      HIHED.setE065().move(inAddPickByPacStk.getQ0E065());
      HIHED.setMSGN().move(inAddPickByPacStk.getQ0MSGN());
      HIHED.setCUNO().move(HDISH.getCONA());
      HIHED.setE0IO('I');
      HIPAC.setMSGN().move(inAddPickByPacStk.getQ0MSGN());
      HIHED.setGEDT(XXGEDT);
      HIPAC.setGEDT(XXGEDT);
      HIHED.setGETM(XXGETM);
      HIPAC.setGETM(XXGETM);
      if (!inAddPickByPacStk.getQ0PMSN().isBlank()) {
         HIHED.setPMSN().move(inAddPickByPacStk.getQ0PMSN());
      } else {
         HIHED.setPMSN().moveLeft(inAddPickByPacStk.getQ0MSGN());
      }
      HILIN.setMSGN().move(inAddPickByPacStk.getQ0MSGN());
      HIPAC.setPACN().moveLeftPad(PTRNS.getPANR());
      HILIN.setPACN().moveLeftPad(PTRNS.getPANR());
      HILIN.setMSLN(0);
      HILIN.setTWSL().move(inAddPickByPacStk.getQ0TWSL());
      HILIN.setGEDT(XXGEDT);
      HILIN.setGETM(XXGETM);
      if (!inAddPickByPacStk.getQ0USD1().isBlank()) {
         HILIN.setUSD1().move(inAddPickByPacStk.getQ0USD1());
      }
      if (!inAddPickByPacStk.getQ0UDS2().isBlank()) {
         HILIN.setUSD2().move(inAddPickByPacStk.getQ0UDS2());
      }
      if (!inAddPickByPacStk.getQ0USD3().isBlank()) {
         HILIN.setUSD3().move(inAddPickByPacStk.getQ0USD3());
      }
      if (!inAddPickByPacStk.getQ0USD4().isBlank()) {
         HILIN.setUSD4().move(inAddPickByPacStk.getQ0USD4());
      }
      if (!inAddPickByPacStk.getQ0USD5().isBlank()) {
         HILIN.setUSD5().move(inAddPickByPacStk.getQ0USD5());
      }

      if (MICommon.toNumericDate(inAddPickByPacStk.getQ0RPDT())) {
         HILIN.setRPDT(MICommon.getNumericDate());
      } else {
         MICommon.setError("RPDT");
         return;
      }
      if (!inAddPickByPacStk.getQ0RPTM().isBlank()) {
         X0FLDD = 6;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inAddPickByPacStk.getQ0RPTM());
         RNUM();
         HILIN.setRPTM((int)this.PXNUM);
      }
      if (!UTCmode && 
         !inAddPickByPacStk.getQ0RPDT().isBlank() &&
         inAddPickByPacStk.getQ0RPTM().isBlank()) {
         HILIN.setRPTM(movexTime());
      }
      if (UTCmode) {
         // Reporting date
         if (!inAddPickByPacStk.getQ0RPDT().isBlank() &&
             !inAddPickByPacStk.getQ0RPTM().isBlank()) {
            date = HILIN.getRPDT(); 
            time = HILIN.getRPTM() / 100;           
            if (!convertFromUTC()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("RPDT","S_00148");
               return;
            }
            HILIN.setRPDT(UTCdate);
            HILIN.setRPTM(UTCtime * 100);
         } else {
            IN60 = true;
            //   MSGID=S_00148 UTC time conversion failed.
            MICommon.setError("RPDT","S_00148");
            return;
         }
      }
      X0FLDD = 1;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddPickByPacStk.getQ0ISMD());
      RNUM();
      HILIN.setISMD((int)this.PXNUM);
      HILIN.setLODO().moveLeftPad(inAddPickByPacStk.getQ0LODO());
      if (!inAddPickByPacStk.getQ0PLSX().isBlank()) {
         X0FLDD = 3;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inAddPickByPacStk.getQ0PLSX());
         RNUM();
         HILIN.setPLSX((int)this.PXNUM);
      } else {
         HILIN.setPLSX(0);
      }
      if (!inAddPickByPacStk.getQ0PLRN().isBlank()) {
         X0FLDD = 11;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inAddPickByPacStk.getQ0PLRN());
         RNUM();
         HILIN.setPLRN((int)this.PXNUM);
      } else {
         HILIN.setPLRN(0);
      }
      if (IN75) {
         IN60 = true;
         return;
      }
      RHEAD();
      if (IN60 && IN76) {
         return;
      }
      RPACK();
      if (IN60 && IN76) {
         return;
      }
      RLINE();
      if (IN60) {
         return;
      }
      outAddPickByPacStk.setY0CONO().move(XXCONO);
      outAddPickByPacStk.setY0MSGN().move(HIHED.getMSGN());
      if (MHS850DS.getJ7STNB() > 0L) {
         X0FLDD = 10;
         XXNUMN = (long)MHS850DS.getJ7STNB();
         XXNUMA.clear();
         RNUMO();
         outAddPickByPacStk.setY0STNB().moveLeft(this.PXALPH);
         X0FLDD = 7;
         XXNUMN = (int)MHS850DS.getJ7STRN();
         XXNUMA.clear();
         RNUMO();
         outAddPickByPacStk.setY0STRN().moveLeft(this.PXALPH);
      }
      MICommon.setData( outAddPickByPacStk.get());
   }                                                                             // 109-A End

   public void RCOM24() {
      // ----------------------------------------------------------------
      //   Clear transaction records
      // ----------------------------------------------------------------
      sMHS850MIRAddMOPick inAddMOPick = (sMHS850MIRAddMOPick)MICommon.getInDS(sMHS850MIRAddMOPick.class);
      sMHS850MISAddMOPick outAddMOPick = (sMHS850MISAddMOPick)MICommon.getOutDS(sMHS850MISAddMOPick.class);

      HIHED.clear();
      HIPAC.clear();
      HILIN.clear();
      // ----------------------------------------------------------------
      //   Save process option.
      // ----------------------------------------------------------------
      XXPRFL.move(inAddMOPick.getQJPRFL());
      XXOPC.move("*ADD");
      if (MICommon.toNumericCompany(inAddMOPick.getQJCONO())) {
         XXCONO = MICommon.getInt();
      } else {
         MICommon.setError("CONO");
         return;
      }

      LDAZD.CONO = XXCONO;
      //-----------------------------------
      //    UTC mode
      UTCmode = isUTCmode(inAddMOPick.getQJUTCM());
      if (IN60) {
         MICommon.setError("UTCM", "WUTCM01", inAddMOPick.getQJUTCM());
         return;
      }
      //-----------------------------------
      // Message type
      XXE065.moveLeftPad(inAddMOPick.getQJE065());
      RE065();
      if (IN60) {
         return;
      }
      // Check Warehouse
      XXWHLO.moveLeftPad(inAddMOPick.getQJWHLO());
      RWHL();
      if (IN60) {
         return;
      }
      if (UTCmode) {
         //   Determine time zone for Warehouse
         if (!CRCalendar.retrieveTIZOForWHLO(PXMNGTZN, LDAZD.CONO, XXWHLO)) {
            IN60 = true;
            //   MSGID=S_00148 UTC time conversion failed.
            MICommon.setError("GEDT","S_00148");
            return;
         }
         //   time zone for warehouse
         TIZOforWHLO.move(CRCalendar.getTimeZone());
      }
      //-----------------------------------
      //   Retreive message number if blank
      if (inAddMOPick.getQJMSGN().isBlank()) {
         RVMSNR();
         inAddMOPick.setQJMSGN().moveLeft(PXRTVNBR.PXNBNR, 10);
      } else {
         HIHED.setCONO(LDAZD.CONO);
         HIHED.setMSGN().move(inAddMOPick.getQJMSGN());
         rAPIDS.clear();
         X0OPC.moveLeftPad("*GET");
         rPHEDPIpreCall();
         apCall("MHIHEDPI", rPHEDPI);
         rPHEDPIpostCall();
      }
      //-----------------------------------
      //   SSCC/PANR/DLIX Validation
      //-----------------------------------
      PTRNSfound = false;
      inputDLIX.moveLeft(inAddMOPick.getQJDLIX());
      inputPANR.moveLeft(inAddMOPick.getQJPACN());
      inputSSCC.moveLeft(inAddMOPick.getQJSSCC());
      numberPackage = 0;
      if (!validInput()) {
         return;
      }
      if (inAddMOPick.getQJDLIX().isBlank()) {
         PTRNS.setCONO(LDAZD.CONO);
         PTRNS.setDIPA(0);
         PTRNS.setINOU(1);
         if (!inAddMOPick.getQJPACN().isBlank()) {
            PTRNS.setPANR().moveLeft(inAddMOPick.getQJPACN());
            PTRNS.SETLL("85", PTRNS.getKey("85", 4));
            if (PTRNS.READE("85", PTRNS.getKey("85",4))) {
               if (PTRNS.READE("85", PTRNS.getKey("85",4))) {
                  //   MSGID-WDL0202 Delivery number must be entered
                  MICommon.setError( "", "WDL0202");
                  return;
               }
               PTRNSfound = true;
               HILIN.setDLIX(PTRNS.getDLIX());
            }
         }
         if (!inAddMOPick.getQJSSCC().isBlank()) {
            PTRNS.setSSCC().moveLeft(inAddMOPick.getQJSSCC());
            if (PTRNS.CHAIN("70", PTRNS.getKey("70", 3))) {
               PTRNSfound = true;
               HILIN.setDLIX(PTRNS.getDLIX());
               inAddMOPick.setQJPACN().moveLeft(PTRNS.getPANR());
            }
         }
      }
      //   Default package number if blank
      if (inAddMOPick.getQJPACN().isBlank()) {
         inAddMOPick.setQJPACN().moveLeft(inAddMOPick.getQJMSGN());
      }
      HIPAC.setCONO(LDAZD.CONO);
      HIPAC.setMSGN().move(inAddMOPick.getQJMSGN());
      HIPAC.setPACN().move(inAddMOPick.getQJPACN());
      rAPIDS.clear();
      X0OPC.moveLeftPad("*GET");
      rPPACPIpreCall();
      apCall("MHIPACPI", rPPACPI);
      rPPACPIpostCall();
      HILIN.setCONO(LDAZD.CONO);
      HILIN.setMSGN().move(inAddMOPick.getQJMSGN());
      HILIN.setPACN().move(inAddMOPick.getQJPACN());
      HILIN.setMSLN(0);
      //   Retreive facility for warehouse
      ITWHL.setCONO(LDAZD.CONO);
      ITWHL.setWHLO().move(inAddMOPick.getQJWHLO());
      if (!ITWHL.CHAIN("00", ITWHL.getKey("00"))) {
         //   MSGID=WWH0103 Warehouse &1 does not exist
         MICommon.setError( "", "WWH0103", inAddMOPick.getQJWHLO());
         return;
      } else {
         HIHED.setFACI().move(ITWHL.getFACI());
      }
      //   Format date/time
      if (inAddMOPick.getQJGEDT().isBlank() ||
            inAddMOPick.getQJGETM().isBlank()) {
         XXGEDT = movexDate();
         XXGETM = movexTime();
      } else {
         if (MICommon.toNumericDate(inAddMOPick.getQJGEDT())) {
            XXGEDT = MICommon.getNumericDate();
         } else {
            MICommon.setError("GEDT");
            return;
         }
         if (!MICommon.toNumeric(inAddMOPick.getQJGETM())) {
            MICommon.setError("GETM");
            return;
         }
         XXGETM = MICommon.getInt();
         // Date generated
         date = XXGEDT; 
         time = XXGETM / 100;
         if (UTCmode && date > 0) {
            if (!convertFromUTC()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("GEDT","S_00148");
               return;
            }
            XXGEDT = UTCdate;
            XXGETM = UTCtime * 100;
         }
      }
      // ----------------------------------------------------------------
      //   Move data from the transaction input structure (MHS850MI1)
      //   to the execution data formats (MHIHED/MHIPAC/MHILIN)
      // ----------------------------------------------------------------
      HIHED.setCONO(LDAZD.CONO);
      HIPAC.setCONO(LDAZD.CONO);
      HILIN.setCONO(LDAZD.CONO);
      HIHED.setQLFR().move("11  ");
      HIPAC.setQLFR().move("11  ");
      HILIN.setQLFR().move("11  ");
      //----------------------------------------
      //---------------------------------------
      HILIN.setTTYP(11);
      HIHED.setE0IO('I');
      HIHED.setWHLO().move(inAddMOPick.getQJWHLO());
      HIPAC.setWHLO().move(inAddMOPick.getQJWHLO());
      HILIN.setWHLO().move(inAddMOPick.getQJWHLO());
      HIHED.setCUNO().moveLeft(inAddMOPick.getQJWHLO());
      HIPAC.setCUNO().moveLeft(inAddMOPick.getQJWHLO());
      HILIN.setCUNO().moveLeft(inAddMOPick.getQJWHLO());
      HIHED.setMSGN().move(inAddMOPick.getQJMSGN());
      HIPAC.setMSGN().move(inAddMOPick.getQJMSGN());
      HILIN.setMSGN().move(inAddMOPick.getQJMSGN());
      HIHED.setPMSN().moveLeft(inAddMOPick.getQJMSGN());
      HIPAC.setPACN().move(inAddMOPick.getQJPACN());
      HILIN.setPACN().move(inAddMOPick.getQJPACN());
      HIHED.setGEDT(XXGEDT);
      HIPAC.setGEDT(XXGEDT);
      HILIN.setGEDT(XXGEDT);
      HIHED.setGETM(XXGETM);
      HIPAC.setGETM(XXGETM);
      HILIN.setGETM(XXGETM);
      HIHED.setE0PA().move(inAddMOPick.getQJE0PA());
      HIHED.setE0PB().move(inAddMOPick.getQJE0PA());
      HIHED.setE035(0);
      HIHED.setE065().move(inAddMOPick.getQJE065());
      HIHED.setADID().move(inAddMOPick.getQJADID());
      HIPAC.setADID().move(inAddMOPick.getQJADID());
      HILIN.setADID().move(inAddMOPick.getQJADID());
      HILIN.setITNO().move(inAddMOPick.getQJITNO());
      XXITNO.moveLeftPad(HILIN.setITNO());
      RITNO();
      //--------------------------------
      HILIN.setPOPN().move(inAddMOPick.getQJPOPN());
      HILIN.setALWQ().move(inAddMOPick.getQJALWQ());
      X0FLDD = 2;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddMOPick.getQJALWT());
      RNUM();
      HILIN.setALWT((int)this.PXNUM);
      X0FLDD = 1;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddMOPick.getQJOEND().getChar());
      RNUM();
      HILIN.setOEND((int)this.PXNUM);
      HILIN.setWHSL().move(inAddMOPick.getQJWHSL());
      HILIN.setBANO().move(inAddMOPick.getQJBANO());
      HILIN.setCAMU().move(inAddMOPick.getQJCAMU());
      XXQTYN = 0d;
      XXQTYA.clear();
      XXQTYA.moveLeft(inAddMOPick.getQJQTYP());
      this.PXDCCD = X1DCCD;
      RQTY();
      HILIN.setDLQT(this.PXNUM);
      XXQTYN = 0d;
      XXQTYA.clear();
      XXQTYA.moveLeft(inAddMOPick.getQJQTYO());
      this.PXDCCD = X1DCCD;
      RQTY();
      HILIN.setALQT(this.PXNUM);
      HILIN.setRIDN().move(inAddMOPick.getQJRIDN());
      X0FLDD = 6;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddMOPick.getQJRIDL());
      RNUM();
      HILIN.setRIDL((int)this.PXNUM);
      X0FLDD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddMOPick.getQJRIDX());
      RNUM();
      HILIN.setRIDX((int)this.PXNUM);
      X0FLDD = 6;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddMOPick.getQJRIDO());
      RNUM();
      HILIN.setRIDO((int)this.PXNUM);
      if (!PTRNSfound) {
         X0FLDD = 11;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inAddMOPick.getQJDLIX());
         RNUM();
         HILIN.setDLIX((long)this.PXNUM);
      }
      if (!inAddMOPick.getQJPLRN().isBlank()) {
         X0FLDD = 11;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inAddMOPick.getQJPLRN());
         RNUM();
         HILIN.setPLRN((long)this.PXNUM);
      }
      //   Format reporting date
      if (!inAddMOPick.getQJRPDT().isBlank()) {
         if (!MICommon.toNumericDate(inAddMOPick.getQJRPDT())) {
            MICommon.setError("RPDT");
            return;
         }
         HILIN.setRPDT(MICommon.getNumericDate());
      }
      //--------------------------------------------------------------------
      if (!inAddMOPick.getQJRPTM().isBlank()) {
         X0FLDD = 6;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inAddMOPick.getQJRPTM());
         RNUM();
         HILIN.setRPTM((int)this.PXNUM);
      }
      if (!UTCmode && 
         !inAddMOPick.getQJRPDT().isBlank() &&
         inAddMOPick.getQJRPTM().isBlank()) {
         HILIN.setRPTM(movexTime());
      }
      if (UTCmode) {
         // Reporting date
         if (!inAddMOPick.getQJRPDT().isBlank() &&
             !inAddMOPick.getQJRPTM().isBlank()) {
            date = HILIN.getRPDT(); 
            time = HILIN.getRPTM() / 100;           
            if (!convertFromUTC()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("RPDT","S_00148");
               return;
            }
            HILIN.setRPDT(UTCdate);
            HILIN.setRPTM(UTCtime * 100);
         } else {
            IN60 = true;
            //   MSGID=S_00148 UTC time conversion failed.
            MICommon.setError("RPDT","S_00148");
            return;
         }
      }
      //--------------------------------------------------------------------
      X0FLDD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddMOPick.getQJPLSX());
      RNUM();
      HILIN.setPLSX((int)this.PXNUM);
      HILIN.setUSD1().move(inAddMOPick.getQJUSD1());
      HILIN.setUSD2().move(inAddMOPick.getQJUSD2());
      HILIN.setUSD3().move(inAddMOPick.getQJUSD3());
      HILIN.setUSD4().move(inAddMOPick.getQJUSD4());
      HILIN.setUSD5().move(inAddMOPick.getQJUSD5());
      XXQTYN = 0d;
      XXQTYA.clear();
      XXQTYA.moveLeft(inAddMOPick.getQJCAWE());
      this.PXDCCD = X2DCCD;
      RQTY();
      HILIN.setCAWE(this.PXNUM);
      HIHED.setE0IO('I');
      if (!inAddMOPick.getQJPMSN().isBlank()) {
         HIHED.setPMSN().move(inAddMOPick.getQJPMSN());
      } else {
         HIHED.setPMSN().moveLeft(inAddMOPick.getQJMSGN());
      }
      HILIN.setTWSL().move(inAddMOPick.getQJTWSL());
      X0FLDD = 9;
      X0DCCD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddMOPick.getQJGRWE());
      RNUM();
      HIPAC.setGRWE(this.PXNUM);
      X0FLDD = 9;
      X0DCCD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddMOPick.getQJVOM3());
      RNUM();
      HIPAC.setVOM3(this.PXNUM);
      X0FLDD = 11;
      X0DCCD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddMOPick.getQJFRCP());
      RNUM();
      HIPAC.setFRCP(this.PXNUM);
      //-----------------------------
      HIPAC.setPARE().move(inAddMOPick.getQJPARE());
      HIPAC.setSSCC().move(inAddMOPick.getQJSSCC());
      X0FLDD = 1;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddMOPick.getQJISMD().getChar());
      RNUM();
      HILIN.setISMD((int)this.PXNUM);
      HILIN.setLODO().moveLeftPad(inAddMOPick.getQJLODO());
      //-----------------------------------------------------------
      HIPAC.setPACT().move(inAddMOPick.getQJPACT());
      X0FLDD = 7;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddMOPick.getQJAMKO());
      RNUM();
      HIPAC.setAMKO((int)this.PXNUM);
      //-------------------------------------------------
      //   Validate/create transaction header data
      //-------------------------------------------------
      if (IN75) {
         IN60 = true;
         return;
      }
      //-------------------------------------------------
      RHEAD();
      if (IN60 && IN76) {
         return;
      }
      RPACK();
      if (IN60 && IN76) {
         return;
      }
      //   Validate/create transaction line data
      RLINE();
      if (IN60) {
         return;
      }

      outAddMOPick.setYJCONO().move(XXCONO);
      outAddMOPick.setYJMSGN().move(HIHED.getMSGN());
      if (MHS850DS.getJ7STNB() > 0L) {
         X0FLDD = 10;
         XXNUMN = (long)MHS850DS.getJ7STNB();
         XXNUMA.clear();
         RNUMO();
         outAddMOPick.setYJSTNB().moveLeft(this.PXALPH);
         X0FLDD = 7;
         XXNUMN = (int)MHS850DS.getJ7STRN();
         XXNUMA.clear();
         RNUMO();
         outAddMOPick.setYJSTRN().moveLeft(this.PXALPH);
      }
      MICommon.setData( outAddMOPick.get());
   }

   public void RCOM25() {
      // ----------------------------------------------------------------
      //   Clear transaction records
      // ----------------------------------------------------------------
      sMHS850MIRAddMOReceipt inAddMOReceipt = (sMHS850MIRAddMOReceipt)MICommon.getInDS(sMHS850MIRAddMOReceipt.class);
      sMHS850MISAddMOReceipt outAddMOReceipt = (sMHS850MISAddMOReceipt)MICommon.getOutDS(sMHS850MISAddMOReceipt.class);

      HIHED.clear();
      HIPAC.clear();
      HILIN.clear();
      // ----------------------------------------------------------------
      //   Save process option.
      // ----------------------------------------------------------------
      XXPRFL.move(inAddMOReceipt.getQKPRFL());
      XXOPC.move("*ADD");
      if (MICommon.toNumericCompany(inAddMOReceipt.getQKCONO())) {
         XXCONO = MICommon.getInt();
      } else {
         MICommon.setError("CONO");
         return;
      }
      LDAZD.CONO = XXCONO;
    //-----------------------------------
      //    UTC mode
      UTCmode = isUTCmode(inAddMOReceipt.getQKUTCM());
      if (IN60) {
         MICommon.setError("UTCM", "WUTCM01", inAddMOReceipt.getQKUTCM());
         return;
      }
      //-----------------------------------
      // Message type
      XXE065.moveLeftPad(inAddMOReceipt.getQKE065());
      RE065();
      if (IN60) {
         return;
      }
      // Check Warehouse
      XXWHLO.moveLeftPad(inAddMOReceipt.getQKWHLO());
      RWHL();
      if (IN60) {
         return;
      }
      if (UTCmode) {
         //   Determine time zone for Warehouse
         if (!CRCalendar.retrieveTIZOForWHLO(PXMNGTZN, LDAZD.CONO, XXWHLO)) {
            IN60 = true;
            //   MSGID=S_00148 UTC time conversion failed.
            MICommon.setError("GEDT","S_00148");
            return;
         }
         //   time zone for warehouse
         TIZOforWHLO.move(CRCalendar.getTimeZone());
      }
      //-----------------------------------
      //   Retreive message number if blank
      if (inAddMOReceipt.getQKMSGN().isBlank()) {
         RVMSNR();
         inAddMOReceipt.setQKMSGN().moveLeft(PXRTVNBR.PXNBNR, 10);
      } else {
         HIHED.setCONO(LDAZD.CONO);
         HIHED.setMSGN().move(inAddMOReceipt.getQKMSGN());
         rAPIDS.clear();
         X0OPC.moveLeftPad("*GET");
         rPHEDPIpreCall();
         apCall("MHIHEDPI", rPHEDPI);
         rPHEDPIpostCall();
      }
      //   Default package number if blank
      if (inAddMOReceipt.getQKPACN().isBlank()) {
         inAddMOReceipt.setQKPACN().moveLeft(inAddMOReceipt.getQKMSGN());
      }
      HIPAC.setCONO(LDAZD.CONO);
      HIPAC.setMSGN().move(inAddMOReceipt.getQKMSGN());
      HIPAC.setPACN().move(inAddMOReceipt.getQKPACN());
      rAPIDS.clear();
      X0OPC.moveLeftPad("*GET");
      rPPACPIpreCall();
      apCall("MHIPACPI", rPPACPI);
      rPPACPIpostCall();
      HILIN.setCONO(LDAZD.CONO);
      HILIN.setMSGN().move(inAddMOReceipt.getQKMSGN());
      HILIN.setPACN().move(inAddMOReceipt.getQKPACN());
      HILIN.setMSLN(0);
      //   Retreive facility for warehouse
      ITWHL.setCONO(LDAZD.CONO);
      ITWHL.setWHLO().move(inAddMOReceipt.getQKWHLO());
      if (!ITWHL.CHAIN("00", ITWHL.getKey("00"))) {
         //   MSGID=WWH0103 Warehouse &1 does not exist
         MICommon.setError( "", "WWH0103", inAddMOReceipt.getQKWHLO());
         return;
      } else {
         HIHED.setFACI().move(ITWHL.getFACI());
      }
      //   Format date/time
      if (inAddMOReceipt.getQKGEDT().isBlank() ||
            inAddMOReceipt.getQKGETM().isBlank()) {
         XXGEDT = movexDate();
         XXGETM = movexTime();
      } else {
         if (MICommon.toNumericDate(inAddMOReceipt.getQKGEDT())) {
            XXGEDT = MICommon.getNumericDate();
         } else {
            MICommon.setError("GEDT");
            return;
         }
         if (!MICommon.toNumeric(inAddMOReceipt.getQKGETM())) {
            MICommon.setError("GETM");
            return;
         }
         XXGETM = MICommon.getInt();
         // Date generated
         date = XXGEDT; 
         time = XXGETM / 100;
         if (UTCmode && date > 0) {
            if (!convertFromUTC()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("GEDT","S_00148");
               return;
            }
            XXGEDT = UTCdate;
            XXGETM = UTCtime * 100;
         }
      }
      // ----------------------------------------------------------------
      //   Move data from the transaction input structure (MHS850MI1)
      //   to the execution data formats (MHIHED/MHIPAC/MHILIN)
      // ----------------------------------------------------------------
      HIHED.setCONO(LDAZD.CONO);
      HIPAC.setCONO(LDAZD.CONO);
      HILIN.setCONO(LDAZD.CONO);
      HIHED.setQLFR().move("10  ");
      HIPAC.setQLFR().move("10  ");
      HILIN.setQLFR().move("10  ");
      //---------------------------------------
      //---------------------------------------
      HILIN.setTTYP(10);
      HIHED.setE0IO('I');
      HIHED.setWHLO().move(inAddMOReceipt.getQKWHLO());
      HIPAC.setWHLO().move(inAddMOReceipt.getQKWHLO());
      HILIN.setWHLO().move(inAddMOReceipt.getQKWHLO());
      HIHED.setMSGN().move(inAddMOReceipt.getQKMSGN());
      HIPAC.setMSGN().move(inAddMOReceipt.getQKMSGN());
      HILIN.setMSGN().move(inAddMOReceipt.getQKMSGN());
      if (!inAddMOReceipt.getQKPMSN().isBlank()) {
         HIHED.setPMSN().moveLeft(inAddMOReceipt.getQKMSGN());
      } else {
         HIHED.setPMSN().moveLeft(inAddMOReceipt.getQKMSGN());
      }
      HIPAC.setPACN().move(inAddMOReceipt.getQKPACN());
      HILIN.setPACN().move(inAddMOReceipt.getQKPACN());
      HIHED.setGEDT(XXGEDT);
      HIPAC.setGEDT(XXGEDT);
      HILIN.setGEDT(XXGEDT);
      HIHED.setGETM(XXGETM);
      HIPAC.setGETM(XXGETM);
      HILIN.setGETM(XXGETM);
      HIHED.setE0PA().move(inAddMOReceipt.getQKE0PA());
      HIHED.setE0PB().move(inAddMOReceipt.getQKE0PA());
      HIHED.setE035(0);
      HIHED.setE065().move(inAddMOReceipt.getQKE065());
      HIHED.setSUNO().moveLeft(inAddMOReceipt.getQKTWHL());
      HIPAC.setSUNO().moveLeft(inAddMOReceipt.getQKTWHL());
      HILIN.setSUNO().moveLeft(inAddMOReceipt.getQKTWHL());
      HIHED.setADID().move(inAddMOReceipt.getQKADID());
      HIPAC.setADID().move(inAddMOReceipt.getQKADID());
      HILIN.setADID().move(inAddMOReceipt.getQKADID());
      HILIN.setITNO().move(inAddMOReceipt.getQKITNO());
      XXITNO.moveLeftPad(HILIN.setITNO());
      RITNO();
      //--------------------------------
      HILIN.setPOPN().move(inAddMOReceipt.getQKPOPN());
      HILIN.setALWQ().move(inAddMOReceipt.getQKALWQ());
      X0FLDD = 2;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddMOReceipt.getQKALWT());
      RNUM();
      HILIN.setALWT((int)this.PXNUM);
      HILIN.setWHSL().move(inAddMOReceipt.getQKWHSL());
      HILIN.setBANO().move(inAddMOReceipt.getQKBANO());
      HILIN.setCAMU().move(inAddMOReceipt.getQKCAMU());
      WOHED.setCONO(LDAZD.CONO);
      WOHED.setFACI().move(ITWHL.getFACI());
      WOHED.setPRNO().moveLeftPad(HILIN.setITNO());
      WOHED.setMFNO().moveLeftPad(inAddMOReceipt.getQKRIDN());
      WOHED.CHAIN("00", WOHED.getKey("00"));
      //-------------------------------------------------
      if (WOHED.getMAUN().NE(ITMAS.getUNMS())) {
         ITAUN.setCONO(HILIN.getCONO());
         ITAUN.setITNO().move(HILIN.getITNO());
         ITAUN.setAUTP(1);
         ITAUN.setALUN().move(WOHED.getMAUN());
         if (ITAUN.CHAIN("00", ITAUN.getKey("00"))) {
            X1DCCD = ITAUN.getDCCD();
         }
      }
      HILIN.setPUUN().move(WOHED.getMAUN());
      //-------------------------------------------------
      XXQTYN = 0d;
      XXQTYA.clear();
      XXQTYA.moveLeft(inAddMOReceipt.getQKQTY());
      this.PXDCCD = X1DCCD;
      RQTY();
      HILIN.setRVQA(this.PXNUM);
      HILIN.setRIDN().move(inAddMOReceipt.getQKRIDN());
      X0FLDD = 6;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddMOReceipt.getQKRIDO());
      RNUM();
      HILIN.setRIDO((int)this.PXNUM);
      X0FLDD = 6;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddMOReceipt.getQKRIDL());
      RNUM();
      HILIN.setRIDL((int)this.PXNUM);
      X0FLDD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddMOReceipt.getQKRIDX());
      RNUM();
      HILIN.setRIDX((int)this.PXNUM);
      X0FLDD = 11;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddMOReceipt.getQKRIDI());
      RNUM();
      HILIN.setRIDI((long)this.PXNUM);
      X0FLDD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddMOReceipt.getQKPLSX());
      RNUM();
      HILIN.setPLSX((int)this.PXNUM);
      HILIN.setUSD1().move(inAddMOReceipt.getQKUSD1());
      HILIN.setUSD2().move(inAddMOReceipt.getQKUSD2());
      HILIN.setUSD3().move(inAddMOReceipt.getQKUSD3());
      HILIN.setUSD4().move(inAddMOReceipt.getQKUSD4());
      HILIN.setUSD5().move(inAddMOReceipt.getQKUSD5());
      HILIN.setBREF().move(inAddMOReceipt.getQKBREF());
      HILIN.setBRE2().move(inAddMOReceipt.getQKBRE2());
      XXQTYN = 0d;
      XXQTYA.clear();
      XXQTYA.moveLeft(inAddMOReceipt.getQKCAWE());
      this.PXDCCD = X2DCCD;
      RQTY();
      HILIN.setCAWE(this.PXNUM);
      HIHED.setE0IO('I');
      if (!inAddMOReceipt.getQKPMSN().isBlank()) {
         HIHED.setPMSN().move(inAddMOReceipt.getQKPMSN());
      } else {
         HIHED.setPMSN().moveLeft(inAddMOReceipt.getQKMSGN());
      }
      X0FLDD = 1;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddMOReceipt.getQKOEND().getChar());
      RNUM();
      HILIN.setOEND((int)this.PXNUM);
      //--------------------------------------------------
      if (!inAddMOReceipt.getQKRPDT().isBlank()) {
         if (!MICommon.toNumericDate(inAddMOReceipt.getQKRPDT())) {
            MICommon.setError("RPDT");
            return;
         }
         HILIN.setRPDT(MICommon.getNumericDate());
      }
      //--------------------------------------------------------------------
      if (!inAddMOReceipt.getQKRPTM().isBlank()) {
         X0FLDD = 6;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inAddMOReceipt.getQKRPTM());
         RNUM();
         HILIN.setRPTM((int)this.PXNUM);
      }
      if (inAddMOReceipt.getQKRPDT().isBlank() ||
         inAddMOReceipt.getQKRPTM().isBlank()) {
         // Retrieve current date and time in time zone of WHLO
         long timestamp = movexTimeDateMS();
         int transactionDate = CRCalendar.systemDate(timestamp);   
         int transactionTime = CRCalendar.systemTime(timestamp);  
         RTIME(timestamp);
         if (SSRPDT != 0) {
            transactionDate = SSRPDT;
            transactionTime = SSTRTM; //HHMM
         }
         if (inAddMOReceipt.getQKRPDT().isBlank()){
            HILIN.setRPDT(transactionDate);
         }
         if (inAddMOReceipt.getQKRPTM().isBlank()){
            HILIN.setRPTM(transactionTime);
         }
      }
      // Date generated
      if (!inAddMOReceipt.getQKRPDT().isBlank() &&
         !inAddMOReceipt.getQKRPTM().isBlank()) {
         date = HILIN.getRPDT(); 
         time = HILIN.getRPTM() / 100;
         if (UTCmode && date > 0) {
            if (!convertFromUTC()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("GEDT","S_00148");
               return;
            }
            HILIN.setRPDT(UTCdate);
            HILIN.setRPTM(UTCtime * 100);
         }
      }
      // Potency
      if (!inAddMOReceipt.getQKPOCY().isBlank()) {
         X0FLDD = 5;
         X0DCCD = 2;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inAddMOReceipt.getQKPOCY());
         RNUM();
         HILIN.setPOCY(this.PXNUM);
      }
      //--------------------------------------------------
      HILIN.setSTAS(inAddMOReceipt.getQKSTAS().getChar());
      //----------------------------------------------------
      //   Validate/create transaction header data
      //----------------------------------------------------
      if (IN75) {
         IN60 = true;
         return;
      }
      //----------------------------------------------------
      RHEAD();
      if (IN60 && IN76) {
         return;
      }
      RPACK();
      if (IN60 && IN76) {
         return;
      }
      //   Validate/create transaction line data
      RLINE();
      if (IN60) {
         return;
      }     
      outAddMOReceipt.setYKCONO().move(XXCONO);
      outAddMOReceipt.setYKMSGN().move(HIHED.getMSGN());
      MICommon.setData( outAddMOReceipt.get());
   }



   public void RCOM26() {
      // ----------------------------------------------------------------
      //   Clear transaction records
      // ----------------------------------------------------------------
      sMHS850MIRSndWhsLine inSndWhsLine = (sMHS850MIRSndWhsLine)MICommon.getInDS(sMHS850MIRSndWhsLine.class);

      HIHED.clear();
      HIPAC.clear();
      HILIN.clear();
      // ----------------------------------------------------------------
      //   Default process option.
      // ----------------------------------------------------------------
      XXPRFL.clear();
      XXOPC.move("*ADD");
      if (MICommon.toNumericCompany(inSndWhsLine.getQLCONO())) {
         XXCONO = MICommon.getInt();
      } else {
         MICommon.setError("CONO");
         return;
      }

      LDAZD.CONO = XXCONO;
    //-----------------------------------
      //    UTC mode
      UTCmode = isUTCmode(inSndWhsLine.getQLUTCM());
      if (IN60) {
         MICommon.setError("UTCM", "WUTCM01", inSndWhsLine.getQLUTCM());
         return;
      }
      // ----------------------------------------------------------------
      HIHED.setCONO(LDAZD.CONO);
      HIHED.setMSGN().move(inSndWhsLine.getQLMSGN());
      rAPIDS.clear();
      X0OPC.moveLeftPad("*GET");
      rPHEDPIpreCall();
      apCall("MHIHEDPI", rPHEDPI);
      rPHEDPIpostCall();
      IN60 = toBoolean(X0IN60.getChar());
      if (IN60) {
         MICommon.setError( "", X0MSID.toString(), X0MSGD);
         return;
      }
      HIPAC.setCONO(LDAZD.CONO);
      HIPAC.setMSGN().move(inSndWhsLine.getQLMSGN());
      HIPAC.setPACN().move(inSndWhsLine.getQLPACN());
      rAPIDS.clear();
      X0OPC.moveLeftPad("*GET");
      rPPACPIpreCall();
      apCall("MHIPACPI", rPPACPI);
      rPPACPIpostCall();
      IN60 = toBoolean(X0IN60.getChar());
      if (IN60) {
         MICommon.setError( "", X0MSID.toString(), X0MSGD);
         return;
      }
      //   Retreive next line in sequence
      HILIN.setCONO(LDAZD.CONO);
      HILIN.setMSGN().move(inSndWhsLine.getQLMSGN());
      HILIN.setPACN().move(inSndWhsLine.getQLPACN());
      HILIN.setMSLN(0);
      ITWHL.setCONO(LDAZD.CONO);
      ITWHL.setWHLO().move(inSndWhsLine.getQLWHLO());
      if (!ITWHL.CHAIN("00", ITWHL.getKey("00"))) {
         //   MSGID=WWH0103 Warehouse &1 does not exist
         MICommon.setError( "", "WWH0103", inSndWhsLine.getQLWHLO());
         return;
      } else {
         HILIN.setFACI().move(ITWHL.getFACI());
      }
      if (UTCmode) {
         //   Determine time zone for Warehouse
         XXWHLO.moveLeftPad(ITWHL.getWHLO());
         if (!CRCalendar.retrieveTIZOForWHLO(PXMNGTZN, LDAZD.CONO, XXWHLO)) {
            IN60 = true;
            //   MSGID=S_00148 UTC time conversion failed.
            MICommon.setError("GEDT","S_00148");
            return;
         }
         //   time zone for warehouse
         TIZOforWHLO.move(CRCalendar.getTimeZone());
      }
      //   Format date/time
      XXGEDT = movexDate();
      XXGETM = movexTime();
      if (inSndWhsLine.getQLCHGD().isBlank()) {
         XXCHGD = movexDate();
      } else {
         if (!MICommon.toNumericDate(inSndWhsLine.getQLCHGD())) {
            MICommon.setError("CHGD");
            return;
         }
         XXCHGD = MICommon.getNumericDate();
      }
      // ----------------------------------------------------------------
      //   Move data from the transaction input structure (MHS850MI1)
      //   to the execution data formats (MHIHED/MHIPAC/MHILIN)
      // ----------------------------------------------------------------
      HILIN.setCONO(LDAZD.CONO);
      HILIN.setDIVI().move(inSndWhsLine.getQLDIVI());
      HILIN.setWHLO().move(inSndWhsLine.getQLWHLO());
      HILIN.setMSGN().move(inSndWhsLine.getQLMSGN());
      HILIN.setPACN().move(inSndWhsLine.getQLPACN());
      HILIN.setQLFR().move(inSndWhsLine.getQLQLFR());
      HILIN.setQLFS(inSndWhsLine.getQLQLFS().getInt());
      HILIN.setFACI().move(inSndWhsLine.getQLFACI());
      HILIN.setGEDT(XXGEDT);
      HILIN.setGETM(XXGETM);
      HILIN.setCHGD(XXCHGD);
      HILIN.setSTAT().move(inSndWhsLine.getQLSTAT());
      HILIN.setITNO().move(inSndWhsLine.getQLITNO());
      XXITNO.moveLeftPad(HILIN.setITNO());
      RITNO();
      //---------------------------------
      HILIN.setITDS().move(inSndWhsLine.getQLITDS());
      HILIN.setWHSL().move(inSndWhsLine.getQLWHSL());
      inSndWhsLine.getQLBANO().toUpperCase();
      HILIN.setBANO().move(inSndWhsLine.getQLBANO());
      HILIN.setCAMU().move(inSndWhsLine.getQLCAMU());
      X0FLDD = 10;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inSndWhsLine.getQLREPN());
      RNUM();
      HILIN.setREPN((long)this.PXNUM);
      X0FLDD = 5;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inSndWhsLine.getQLRELI());
      RNUM();
      HILIN.setRELI((int)this.PXNUM);
      HILIN.setPOPN().move(inSndWhsLine.getQLPOPN());
      HILIN.setALWQ().move(inSndWhsLine.getQLALWQ());
      X0FLDD = 2;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inSndWhsLine.getQLALWT());
      RNUM();
      HILIN.setALWT((int)this.PXNUM);
      XXQTYN = 0d;
      XXQTYA.clear();
      XXQTYA.moveLeft(inSndWhsLine.getQLDLQT());
      this.PXDCCD = X1DCCD;
      RQTY();
      HILIN.setDLQT(this.PXNUM);
      HILIN.setUNIT().move(inSndWhsLine.getQLUNIT());
      XXQTYN = 0d;
      XXQTYA.clear();
      XXQTYA.moveLeft(inSndWhsLine.getQLDLQA());
      this.PXDCCD = X1DCCD;
      RQTY();
      HILIN.setDLQA(this.PXNUM);
      HILIN.setALUN().move(inSndWhsLine.getQLALUN());
      X0FLDD = 9;
      X0DCCD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inSndWhsLine.getQLVOL3());
      RNUM();
      HILIN.setVOL3(this.PXNUM);
      X0FLDD = 9;
      X0DCCD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inSndWhsLine.getQLNEWE());
      RNUM();
      HILIN.setNEWE(this.PXNUM);
      X0FLDD = 9;
      X0DCCD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inSndWhsLine.getQLGRWE());
      RNUM();
      HILIN.setGRWE(this.PXNUM);
      XXQTYN = 0d;
      XXQTYA.clear();
      XXQTYA.moveLeft(inSndWhsLine.getQLD1QT());
      this.PXDCCD = X1DCCD;
      RQTY();
      HILIN.setD1QT(this.PXNUM);
      HILIN.setDLSP().move(inSndWhsLine.getQLDLSP());
      HILIN.setPUUN().move(inSndWhsLine.getQLPUUN());
      XXQTYN = 0d;
      XXQTYA.clear();
      XXQTYA.moveLeft(inSndWhsLine.getQLRVQA());
      this.PXDCCD = X1DCCD;
      if (HILIN.getQLFR().EQ("10  ")) {
         if (HILIN.getPUUN().NE(ITMAS.getUNMS())) {
            if (cRefALUNext.getMITAUN(ITAUN, false, LDAZD.CONO, HILIN.setITNO(), 1, HILIN.getPUUN())) {
               this.PXDCCD = ITAUN.getDCCD();
            }
         }
      }
      RQTY();
      HILIN.setRVQA(this.PXNUM);
      XXQTYN = 0d;
      XXQTYA.clear();
      XXQTYA.moveLeft(inSndWhsLine.getQLALQT());
      this.PXDCCD = X1DCCD;
      RQTY();
      HILIN.setALQT(this.PXNUM);
      XXQTYN = 0d;
      XXQTYA.clear();
      XXQTYA.moveLeft(inSndWhsLine.getQLPAQT());
      this.PXDCCD = X1DCCD;
      RQTY();
      HILIN.setPAQT(this.PXNUM);
      XXQTYN = 0d;
      XXQTYA.clear();
      XXQTYA.moveLeft(inSndWhsLine.getQLCAWE());
      this.PXDCCD = X2DCCD;
      RQTY();
      HILIN.setCAWE(this.PXNUM);
      X0FLDD = 5;
      X0DCCD = 2;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inSndWhsLine.getQLPOCY());
      RNUM();
      HILIN.setPOCY(this.PXNUM);
      X0FLDD = 1;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inSndWhsLine.getQLOEND().getChar());
      RNUM();
      HILIN.setOEND((int)this.PXNUM);
      HILIN.setBREF().move(inSndWhsLine.getQLBREF());
      HILIN.setBRE2().move(inSndWhsLine.getQLBRE2());
      HILIN.setFLOC().move(inSndWhsLine.getQLFLOC());
      HILIN.setORCA().move(inSndWhsLine.getQLORCA());
      X0FLDD = 2;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inSndWhsLine.getQLTTYP());
      RNUM();
      HILIN.setTTYP((int)this.PXNUM);
      HILIN.setRIDN().move(inSndWhsLine.getQLRIDN());
      X0FLDD = 6;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inSndWhsLine.getQLRIDO());
      RNUM();
      HILIN.setRIDO((int)this.PXNUM);
      X0FLDD = 6;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inSndWhsLine.getQLRIDL());
      RNUM();
      HILIN.setRIDL((int)this.PXNUM);
      X0FLDD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inSndWhsLine.getQLRIDX());
      RNUM();
      HILIN.setRIDX((int)this.PXNUM);
      X0FLDD = 11;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inSndWhsLine.getQLRIDI());
      RNUM();
      HILIN.setRIDI((long)this.PXNUM);
      X0FLDD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inSndWhsLine.getQLPLSX());
      RNUM();
      HILIN.setPLSX((int)this.PXNUM);
      X0FLDD = 11;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inSndWhsLine.getQLDLIX());
      RNUM();
      HILIN.setDLIX((long)this.PXNUM);
      if (!inSndWhsLine.getQLPLRN().isBlank()) {
         X0FLDD = 11;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inSndWhsLine.getQLPLRN());
         RNUM();
         HILIN.setPLRN((long)this.PXNUM);
      }
      //   Format reporting date
      if (!inSndWhsLine.getQLRPDT().isBlank()) {
         if (!MICommon.toNumericDate(inSndWhsLine.getQLRPDT())) {
            MICommon.setError("RPDT");
            return;
         }
         HILIN.setRPDT(MICommon.getNumericDate());
      }
      HILIN.setDNNO().move(inSndWhsLine.getQLDNNO());
      HILIN.setCUOR().move(inSndWhsLine.getQLCUOR());
      HILIN.setCUNO().move(inSndWhsLine.getQLCUNO());
      HILIN.setADID().move(inSndWhsLine.getQLADID());
      HILIN.setSUNO().move(inSndWhsLine.getQLSUNO());
      X0FLDD = 1;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inSndWhsLine.getQLSUTY().getChar());
      RNUM();
      HILIN.setSUTY((int)this.PXNUM);
      HILIN.setSUDO().move(inSndWhsLine.getQLSUDO());

      if (!MICommon.toNumericDate(inSndWhsLine.getQLDNDT())) {
         MICommon.setError("DNDT");
         return;
      }
      HILIN.setDNDT(MICommon.getNumericDate());

      if (!MICommon.toNumericDate(inSndWhsLine.getQLEXPI())) {
         MICommon.setError("EXPI");
         return;
      }
      HILIN.setEXPI(MICommon.getNumericDate());

      if (!MICommon.toNumericDate(inSndWhsLine.getQLCNDT())) {
         MICommon.setError("CNDT");
         return;
      }
      HILIN.setCNDT(MICommon.getNumericDate());

      if (!MICommon.toNumericDate(inSndWhsLine.getQLSEDT())) {
         MICommon.setError("SEDT");
         return;
      }
      HILIN.setSEDT(MICommon.getNumericDate());

      HILIN.setQCRA(inSndWhsLine.getQLQCRA().getChar());
      HILIN.setSCRE().move(inSndWhsLine.getQLSCRE());
      HILIN.setTRTP().move(inSndWhsLine.getQLTRTP());
      HILIN.setBREM().move(inSndWhsLine.getQLBREM());
      HILIN.setSITE().move(inSndWhsLine.getQLSITE());
      HILIN.setSITD().move(inSndWhsLine.getQLSITD());
      HILIN.setPUPR(inSndWhsLine.setQLPUPR().getDouble(17, 6));
      HILIN.setPPUN().move(inSndWhsLine.getQLPPUN());
      HILIN.setCUCD().move(inSndWhsLine.getQLCUCD());
      X0FLDD = 5;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inSndWhsLine.getQLPUCD());
      RNUM();
      HILIN.setPUCD((int)this.PXNUM);
      X0FLDD = 15;
      X0DCCD = 2;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inSndWhsLine.getQLLNAM());
      RNUM();
      HILIN.setLNAM(this.PXNUM);
      X0FLDD = 2;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inSndWhsLine.getQLVTCD());
      RNUM();
      HILIN.setVTCD((int)this.PXNUM);
      HILIN.setAGNB().move(inSndWhsLine.getQLAGNB());
      HILIN.setRESP().move(inSndWhsLine.getQLRESP());
      X0FLDD = 13;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inSndWhsLine.getQLDTID());
      RNUM();
      HILIN.setDTID((long)this.PXNUM);
      X0FLDD = 13;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inSndWhsLine.getQLTXID());
      RNUM();
      HILIN.setTXID((long)this.PXNUM);
      HILIN.setUSD1().move(inSndWhsLine.getQLUSD1());
      HILIN.setUSD2().move(inSndWhsLine.getQLUSD2());
      HILIN.setUSD3().move(inSndWhsLine.getQLUSD3());
      HILIN.setUSD4().move(inSndWhsLine.getQLUSD4());
      if (!inSndWhsLine.getQLUSD5().isBlank()) {
         X0FLDD = 6;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inSndWhsLine.getQLUSD5());
         RNUM();
         HILIN.setRPTM((int)this.PXNUM);
      }
      if (!UTCmode &&
         !inSndWhsLine.getQLRPDT().isBlank() &&
         inSndWhsLine.getQLUSD5().isBlank()) {
         HILIN.setRPTM(movexTime());
      }
      if (UTCmode) {
         // Reporting date
         if (!inSndWhsLine.getQLRPDT().isBlank() &&
             !inSndWhsLine.getQLUSD5().isBlank()) {
            date = HILIN.getRPDT(); 
            time = HILIN.getRPTM() / 100;           
            if (!convertFromUTC()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("RPDT","S_00148");
               return;
            }
            HILIN.setRPDT(UTCdate);
            HILIN.setRPTM(UTCtime * 100);
         } else {
            if (inSndWhsLine.getQLRPDT().isBlank() ||
               inSndWhsLine.getQLUSD5().isBlank()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("RPDT","S_00148");
               return;
            }
         }
      }
      //-----------------------------------
      HILIN.setTWSL().move(inSndWhsLine.getQLTWSL());
      //-----------------------------------------------------------
      X0FLDD = 1;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inSndWhsLine.getQLISMD().getChar());
      RNUM();
      if (IN75) {
         IN60 = true;
         return;
      }
      HILIN.setISMD((int)this.PXNUM);

      HILIN.setRGDT(movexDate());
      HILIN.setRGTM(movexTime());
      HILIN.setLMDT(movexDate());
      HILIN.setCHNO(1);
      HILIN.setCHID().move(DSUSS);
      //------------------------------------------
      RLINE();
   }

   /**
   *    RCOM27 - Execute command - SndWhsPack
   */
   public void RCOM27() {
      // ----------------------------------------------------------------
      //   Clear transaction records
      // ----------------------------------------------------------------
      sMHS850MIRSndWhsPack inSndWhsPack = (sMHS850MIRSndWhsPack)MICommon.getInDS(sMHS850MIRSndWhsPack.class);

      HIHED.clear();
      HIPAC.clear();
      HILIN.clear();
      // ----------------------------------------------------------------
      //   Default process option.
      // ----------------------------------------------------------------
      XXPRFL.clear();
      XXOPC.move("*ADD");
      if (MICommon.toNumericCompany(inSndWhsPack.getQMCONO())) {
         XXCONO = MICommon.getInt();
      } else {
         MICommon.setError("CONO");
         return;
      }

      LDAZD.CONO = XXCONO;
      //-----------------------------------
      //    UTC mode
      UTCmode = isUTCmode(inSndWhsPack.getQMUTCM());
      if (IN60) {
         MICommon.setError("UTCM", "WUTCM01", inSndWhsPack.getQMUTCM());
         return;
      }
      if (UTCmode) {
         //   Determine time zone for Warehouse
         XXWHLO.moveLeftPad(inSndWhsPack.getQMWHLO());
         if (!CRCalendar.retrieveTIZOForWHLO(PXMNGTZN, LDAZD.CONO, XXWHLO)) {
            IN60 = true;
            //   MSGID=S_00148 UTC time conversion failed.
            MICommon.setError("GEDT","S_00148");
            return;
         }
         //   time zone for warehouse
         TIZOforWHLO.move(CRCalendar.getTimeZone());
      }
      // ----------------------------------------------------------------
      HIHED.setCONO(LDAZD.CONO);
      HIHED.setMSGN().move(inSndWhsPack.getQMMSGN());
      rAPIDS.clear();
      X0OPC.moveLeftPad("*GET");
      rPHEDPIpreCall();
      apCall("MHIHEDPI", rPHEDPI);
      rPHEDPIpostCall();
      IN60 = toBoolean(X0IN60.getChar());
      if (IN60) {
         MICommon.setError( "", X0MSID.toString(), X0MSGD);
         return;
      }
      //   Default package number if blank
      if (inSndWhsPack.getQMPACN().isBlank()) {
         inSndWhsPack.setQMPACN().moveLeft(HIPAC.getMSGN());
      }
      //   Format date/time
      if (inSndWhsPack.getQMGEDT().isBlank() ||
            inSndWhsPack.getQMGETM().isBlank()) {
         XXGEDT = movexDate();
         XXGETM = movexTime();
      } else {
         if (MICommon.toNumericDate(inSndWhsPack.getQMGEDT())) {
            XXGEDT = MICommon.getNumericDate();
         } else {
            MICommon.setError("GEDT");
            return;
         }
         if (!MICommon.toNumeric(inSndWhsPack.getQMGETM())) {
            MICommon.setError("GETM");
            return;
         }
         XXGETM = MICommon.getInt();
         // Date generated
         date = XXGEDT; 
         time = XXGETM / 100;
         if (UTCmode && date > 0) {
            if (!convertFromUTC()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("GEDT","S_00148");
               return;
            }
            XXGEDT = UTCdate;
            XXGETM = UTCtime * 100;
         }
      }
      if (inSndWhsPack.getQMCHGD().isBlank()) {
         XXCHGD = movexDate();
      } else {
         if (MICommon.toNumericDate(inSndWhsPack.getQMCHGD())) {
            XXCHGD = MICommon.getNumericDate();
         } else {
            MICommon.setError("CHGD");
            return;
         }
      }
      // ----------------------------------------------------------------
      //   Move data from the transaction input structure (MHS850MI1)
      //   to the execution data formats (MHIHED/MHIPAC/MHILIN)
      // ----------------------------------------------------------------
      HIPAC.setCONO(LDAZD.CONO);
      HIPAC.setDIVI().move(inSndWhsPack.getQMDIVI());
      HIPAC.setWHLO().move(inSndWhsPack.getQMWHLO());
      HIPAC.setMSGN().move(inSndWhsPack.getQMMSGN());
      HIPAC.setPACN().move(inSndWhsPack.getQMPACN());
      HIPAC.setQLFR().move(inSndWhsPack.getQMQLFR());
      HIPAC.setGEDT(XXGEDT);
      HIPAC.setGETM(XXGETM);
      HIPAC.setCHGD(XXCHGD);
      HIPAC.setPARE().move(inSndWhsPack.getQMPARE());
      X0FLDD = 1;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inSndWhsPack.getQMDIPA().getChar());
      RNUM();
      HIPAC.setDIPA((int)this.PXNUM);
      HIPAC.setSUDO().move(inSndWhsPack.getQMSUDO());
      HIPAC.setDNNO().move(inSndWhsPack.getQMDNNO());
      if (!MICommon.toNumericDate(inSndWhsPack.getQMDNDT())) {
         MICommon.setError("DNDT");
         return;
      }
      HIPAC.setDNDT(MICommon.getNumericDate());
      HIPAC.setSTAT().move(inSndWhsPack.getQMSTAT());
      HIPAC.setTRSL().move(inSndWhsPack.getQMTRSL());
      HIPAC.setTRSH().move(inSndWhsPack.getQMTRSH());
      X0FLDD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inSndWhsPack.getQMPACO());
      RNUM();
      HIPAC.setPACO((int)this.PXNUM);
      X0FLDD = 1;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inSndWhsPack.getQMPACU().getChar());
      RNUM();
      HIPAC.setPACU((int)this.PXNUM);
      HIPAC.setPACC().move(inSndWhsPack.getQMPACC());
      X0FLDD = 7;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inSndWhsPack.getQMAMKO());
      RNUM();
      HIPAC.setAMKO((int)this.PXNUM);
      HIPAC.setPACT().move(inSndWhsPack.getQMPACT());
      HIPAC.setPACK().move(inSndWhsPack.getQMPACK());
      HIPAC.setTEPA().move(inSndWhsPack.getQMTEPA());
      HIPAC.setSORT().move(inSndWhsPack.getQMSORT());
      HIPAC.setDLRM().move(inSndWhsPack.getQMDLRM());
      HIPAC.setDLMO().move(inSndWhsPack.getQMDLMO());
      HIPAC.setDLSP().move(inSndWhsPack.getQMDLSP());
      X0FLDD = 9;
      X0DCCD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inSndWhsPack.getQMVOM3());
      RNUM();
      HIPAC.setVOM3(this.PXNUM);
      X0FLDD = 11;
      X0DCCD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inSndWhsPack.getQMFRCP());
      RNUM();
      HIPAC.setFRCP(this.PXNUM);
      X0FLDD = 9;
      X0DCCD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inSndWhsPack.getQMNEWE());
      RNUM();
      HIPAC.setNEWE(this.PXNUM);
      X0FLDD = 9;
      X0DCCD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inSndWhsPack.getQMGRWE());
      RNUM();
      HIPAC.setGRWE(this.PXNUM);
      X0FLDD = 9;
      X0DCCD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inSndWhsPack.getQMGRW3());
      RNUM();
      HIPAC.setGRW3(this.PXNUM);
      X0FLDD = 9;
      X0DCCD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inSndWhsPack.getQMGRW4());
      RNUM();
      HIPAC.setGRW4(this.PXNUM);
      X0FLDD = 9;
      X0DCCD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inSndWhsPack.getQMGRW5());
      RNUM();
      HIPAC.setGRW5(this.PXNUM);
      XXQTYN = 0d;
      XXQTYA.clear();
      XXQTYA.moveLeft(inSndWhsPack.getQMD1QT());
      this.PXDCCD = X1DCCD;
      RQTY();
      HIPAC.setD1QT(this.PXNUM);
      X0FLDD = 9;
      X0DCCD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inSndWhsPack.getQMVOMT());
      RNUM();
      HIPAC.setVOMT(this.PXNUM);
      X0FLDD = 5;
      X0DCCD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inSndWhsPack.getQMPACL());
      RNUM();
      HIPAC.setPACL(this.PXNUM);
      X0FLDD = 5;
      X0DCCD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inSndWhsPack.getQMPACW());
      RNUM();
      HIPAC.setPACW(this.PXNUM);
      X0FLDD = 5;
      X0DCCD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inSndWhsPack.getQMPACH());
      RNUM();
      HIPAC.setPACH(this.PXNUM);
      HIPAC.setSSCC().move(inSndWhsPack.getQMSSCC());
      HIPAC.setWHSL().move(inSndWhsPack.getQMWHSL());
      HIPAC.setCUNO().move(inSndWhsPack.getQMCUNO());
      HIPAC.setADID().move(inSndWhsPack.getQMADID());
      HIPAC.setSUNO().move(inSndWhsPack.getQMSUNO());
      X0FLDD = 1;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inSndWhsPack.getQMSUTY().getChar());
      RNUM();
      HIPAC.setSUTY((int)this.PXNUM);
      HIPAC.setRESP().move(inSndWhsPack.getQMRESP());
      X0FLDD = 13;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inSndWhsPack.getQMDTID());
      RNUM();
      HIPAC.setDTID((long)this.PXNUM);
      X0FLDD = 13;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inSndWhsPack.getQMTXID());
      RNUM();
      HIPAC.setTXID((long)this.PXNUM);
      HIPAC.setUSD1().move(inSndWhsPack.getQMUSD1());
      HIPAC.setUSD2().move(inSndWhsPack.getQMUSD2());
      HIPAC.setUSD3().move(inSndWhsPack.getQMUSD3());
      HIPAC.setUSD4().move(inSndWhsPack.getQMUSD4());
      HIPAC.setUSD5().move(inSndWhsPack.getQMUSD5());
      X0FLDD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inSndWhsPack.getQMRODN());
      RNUM();
      HIPAC.setRODN((int)this.PXNUM);
      X0FLDD = 7;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inSndWhsPack.getQMCONN());
      RNUM();
      HIPAC.setCONN((int)this.PXNUM);
      X0FLDD = 1;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inSndWhsPack.getQMCDEL().getChar());
      RNUM();
      HIPAC.setCDEL((int)this.PXNUM);
      X0FLDD = 11;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inSndWhsPack.getQMNDLX());
      RNUM();
      HIPAC.setNDLX((long)this.PXNUM);

      if (!MICommon.toNumericDate(inSndWhsPack.getQMDSDT())) {
         MICommon.setError("DSDT");
         return;
      }
      HIPAC.setDSDT(MICommon.getNumericDate());
      X0FLDD = 4;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inSndWhsPack.getQMDSHM());
      RNUM();
      HIPAC.setDSHM((int)this.PXNUM);
      // Date generated
      if (!inSndWhsPack.getQMGEDT().isBlank() && !inSndWhsPack.getQMGETM().isBlank()) {
         date = HIPAC.getDSDT(); 
         time = HIPAC.getDSHM() / 100;
         if (UTCmode && date > 0) {
            if (!convertFromUTC()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("GEDT","S_00148");
               return;
            }
            HIPAC.setDSDT(UTCdate);
            HIPAC.setDSHM(UTCtime * 100);
         }
      }
      HIPAC.setROUT().move(inSndWhsPack.getQMROUT());
      HIPAC.setPPNB().move(inSndWhsPack.getQMPPNB());
      HIPAC.setRGDT(movexDate());
      HIPAC.setRGTM(movexTime());
      HIPAC.setLMDT(movexDate());
      HIPAC.setCHNO(1);
      HIPAC.setCHID().move(DSUSS);
      RPACK();
   }

   /**
   *    RCOM28 - Execute command - AddPutAwayConf
   */
   public void RCOM28() {
      // ----------------------------------------------------------------
      //   Clear transaction records
      // ----------------------------------------------------------------
      sMHS850MIRAddPutAwayConf inAddPutAwayConf = (sMHS850MIRAddPutAwayConf)MICommon.getInDS(sMHS850MIRAddPutAwayConf.class);
      sMHS850MISAddPutAwayConf outAddPutAwayConf = (sMHS850MISAddPutAwayConf)MICommon.getOutDS(sMHS850MISAddPutAwayConf.class);

      HIHED.clear();
      HIPAC.clear();
      HILIN.clear();
      // ----------------------------------------------------------------
      //   Save process option.
      // ----------------------------------------------------------------
      XXPRFL.move(inAddPutAwayConf.getQNPRMD());
      XXOPC.move("*ADD");
      if (MICommon.toNumericCompany(inAddPutAwayConf.getQNCONO())) {
         XXCONO = MICommon.getInt();
      } else {
         MICommon.setError("CONO");
         return;
      }

      LDAZD.CONO = XXCONO;
    //-----------------------------------
      //    UTC mode
      UTCmode = isUTCmode(inAddPutAwayConf.getQNUTCM());
      if (IN60) {
         MICommon.setError("UTCM", "WUTCM01", inAddPutAwayConf.getQNUTCM());
         return;
      }
      //-----------------------------------
      // Message type
      XXE065.moveLeftPad(inAddPutAwayConf.getQNE065());
      RE065();
      if (IN60) {
         return;
      }
      // Check Warehouse
      XXWHLO.moveLeftPad(inAddPutAwayConf.getQNWHLO());
      RWHL();
      if (IN60) {
         return;
      }
      if (UTCmode) {
         //   Determine time zone for Warehouse
         if (!CRCalendar.retrieveTIZOForWHLO(PXMNGTZN, LDAZD.CONO, XXWHLO)) {
            IN60 = true;
            //   MSGID=S_00148 UTC time conversion failed.
            MICommon.setError("GEDT","S_00148");
            return;
         }
         //   time zone for warehouse
         TIZOforWHLO.move(CRCalendar.getTimeZone());
      }
      //-----------------------------------
      //   Retreive message number if blank
      //----------------------------------------------------------------
      if (inAddPutAwayConf.getQNWHSL().EQ(inAddPutAwayConf.getQNTWSL()) &&
            !inAddPutAwayConf.getQNWHSL().isBlank()) {
         //   MSGID=MH85010
         MICommon.setError( "", "MH85010");
         return;
      }
      //----------------------------------------------------------------
      if (inAddPutAwayConf.getQNMSGN().isBlank()) {
         RVMSNR();
         inAddPutAwayConf.setQNMSGN().moveLeft(PXRTVNBR.PXNBNR, 10);
      } else {
         HIHED.setCONO(LDAZD.CONO);
         HIHED.setMSGN().move(inAddPutAwayConf.getQNMSGN());
         rAPIDS.clear();
         X0OPC.moveLeftPad("*GET");
         rPHEDPIpreCall();
         apCall("MHIHEDPI", rPHEDPI);
         rPHEDPIpostCall();
      }
      //   Default package number if blank
      if (inAddPutAwayConf.getQNPACN().isBlank()) {
         inAddPutAwayConf.setQNPACN().moveLeft(inAddPutAwayConf.getQNMSGN());
      }
      HIPAC.setCONO(LDAZD.CONO);
      HIPAC.setMSGN().move(inAddPutAwayConf.getQNMSGN());
      HIPAC.setPACN().move(inAddPutAwayConf.getQNPACN());
      rAPIDS.clear();
      X0OPC.moveLeftPad("*GET");
      rPPACPIpreCall();
      apCall("MHIPACPI", rPPACPI);
      rPPACPIpostCall();
      //   Retreive next line in sequence
      HILIN.setCONO(LDAZD.CONO);
      HILIN.setMSGN().move(inAddPutAwayConf.getQNMSGN());
      HILIN.setPACN().move(inAddPutAwayConf.getQNPACN());
      HILIN.setMSLN(0);
      //   Retreive facility for warehouse
      ITWHL.setCONO(LDAZD.CONO);
      ITWHL.setWHLO().move(inAddPutAwayConf.getQNWHLO());
      if (!ITWHL.CHAIN("00", ITWHL.getKey("00"))) {
         //   MSGID=WWH0103 Warehouse &1 does not exist
         MICommon.setError( "", "WWH0103", inAddPutAwayConf.getQNWHLO());
         return;
      } else {
         HIHED.setFACI().move(ITWHL.getFACI());
      }
      //   Format date/time
      if (inAddPutAwayConf.getQNGEDT().isBlank() ||
            inAddPutAwayConf.getQNGETM().isBlank()) {
         XXGEDT = movexDate();
         XXGETM = movexTime();
      } else {
         if (MICommon.toNumericDate(inAddPutAwayConf.getQNGEDT())) {
            XXGEDT = MICommon.getNumericDate();
         } else {
            MICommon.setError("GEDT");
            return;
         }
         if (!MICommon.toNumeric(inAddPutAwayConf.getQNGETM())) {
            MICommon.setError("GETM");
            return;
         }
         XXGETM = MICommon.getInt();
         // Date generated
         date = XXGEDT; 
         time = XXGETM / 100;
         if (UTCmode && date > 0) {
            if (!convertFromUTC()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("GEDT","S_00148");
               return;
            }
            XXGEDT = UTCdate;
            XXGETM = UTCtime * 100;
         }
      }
      // ----------------------------------------------------------------
      //   Move data from the transaction input structure (MHS850MI1)   >
      //   to the execution data formats (MHIHED/MHIPAC/MHILIN)         >
      // ----------------------------------------------------------------
      HIHED.setCONO(LDAZD.CONO);
      HIPAC.setCONO(LDAZD.CONO);
      HILIN.setCONO(LDAZD.CONO);
      HIHED.setSTAT().move("10");
      HILIN.setSTAT().move("10");
      HIPAC.setSTAT().move("10");
      HIHED.setQLFR().move("PACF");
      HIPAC.setQLFR().move("PACF");
      HILIN.setQLFR().move("PACF");
      //---------------------------------------
      //---------------------------------------
      HILIN.setTTYP(25);
      HIHED.setE0IO('I');
      HIHED.setWHLO().move(inAddPutAwayConf.getQNWHLO());
      HIPAC.setWHLO().move(inAddPutAwayConf.getQNWHLO());
      HILIN.setWHLO().move(inAddPutAwayConf.getQNWHLO());
      HIHED.setMSGN().move(inAddPutAwayConf.getQNMSGN());
      HIPAC.setMSGN().move(inAddPutAwayConf.getQNMSGN());
      HILIN.setMSGN().move(inAddPutAwayConf.getQNMSGN());
      HIHED.setPMSN().moveLeft(inAddPutAwayConf.getQNMSGN());
      HIPAC.setPACN().move(inAddPutAwayConf.getQNPACN());
      HILIN.setPACN().move(inAddPutAwayConf.getQNPACN());
      HIHED.setGEDT(XXGEDT);
      HIPAC.setGEDT(XXGEDT);
      HILIN.setGEDT(XXGEDT);
      HIHED.setGETM(XXGETM);
      HIPAC.setGETM(XXGETM);
      HILIN.setGETM(XXGETM);
      HIHED.setE0PA().move(inAddPutAwayConf.getQNE0PA());
      HIHED.setE0PB().move(inAddPutAwayConf.getQNE0PA());
      HIHED.setE065().move(inAddPutAwayConf.getQNE065());
      HILIN.setITNO().move(inAddPutAwayConf.getQNITNO());
      XXITNO.moveLeftPad(HILIN.setITNO());
      RITNO();
      //--------------------------------
      HILIN.setPOPN().move(inAddPutAwayConf.getQNPOPN());
      HILIN.setALWQ().move(inAddPutAwayConf.getQNALWQ());
      X0FLDD = 2;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddPutAwayConf.getQNALWT());
      RNUM();
      HILIN.setALWT((int)this.PXNUM);
      HILIN.setWHSL().move(inAddPutAwayConf.getQNWHSL());
      HILIN.setTWSL().move(inAddPutAwayConf.getQNTWSL());
      HILIN.setBANO().move(inAddPutAwayConf.getQNBANO());
      HILIN.setCAMU().move(inAddPutAwayConf.getQNCAMU());
      XXQTYN = 0d;
      XXQTYA.clear();
      XXQTYA.moveLeft(inAddPutAwayConf.getQNRVQA());
      this.PXDCCD = X1DCCD;
      RQTY();
      HILIN.setRVQA(this.PXNUM);
      HILIN.setRIDN().move(inAddPutAwayConf.getQNRIDN());
      X0FLDD = 6;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddPutAwayConf.getQNRIDL());
      RNUM();
      HILIN.setRIDL((int)this.PXNUM);
      X0FLDD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddPutAwayConf.getQNRIDX());
      RNUM();
      HILIN.setRIDX((int)this.PXNUM);
      X0FLDD = 10;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddPutAwayConf.getQNREPN());
      RNUM();
      HILIN.setREPN((long)this.PXNUM);
      HIHED.setE0IO('I');
      if (!inAddPutAwayConf.getQNPMSN().isBlank()) {
         HIHED.setPMSN().move(inAddPutAwayConf.getQNPMSN());
      } else {
         HIHED.setPMSN().moveLeft(inAddPutAwayConf.getQNMSGN());
      }
      X0FLDD = 10;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddPutAwayConf.getQNPANO());
      RNUM();
      HILIN.setPANO((long)this.PXNUM);
      HILIN.setORCA().move(inAddPutAwayConf.getQNORCA());
      if (IN75) {
         IN60 = true;
         return;
      }
      //-------------------------------------------------
      RHEAD();
      if (IN60 && IN76) {
         return;
      }
      RPACK();
      if (IN60 && IN76) {
         return;
      }
      //   Validate/create transaction line data
      RLINE();
      if (IN60) {
         return;
      }

      outAddPutAwayConf.setYNCONO().move(XXCONO);
      outAddPutAwayConf.setYNMSGN().move(HIHED.getMSGN());
      MICommon.setData( outAddPutAwayConf.get());
   }

   public void RCOM29() {
      // ----------------------------------------------------------------
      //   Clear transaction records
      // ----------------------------------------------------------------
      sMHS850MIRAddDO inAddDO = (sMHS850MIRAddDO)MICommon.getInDS(sMHS850MIRAddDO.class);
      sMHS850MISAddDO outAddDO = (sMHS850MISAddDO)MICommon.getOutDS(sMHS850MISAddDO.class);

      HIHED.clear();
      HIPAC.clear();
      HILIN.clear();
      // ----------------------------------------------------------------
      //   Save process option.
      // ----------------------------------------------------------------
      XXPRFL.move(inAddDO.getQPPRMD());
      XXOPC.move("*ADD");
      LDAZZ.TDA1.moveLeftPad(MICommon.getName());
      if (MICommon.toNumericCompany(inAddDO.getQPCONO())) {
         XXCONO = MICommon.getInt();
      } else {
         MICommon.setError("CONO");
         return;
      }

      LDAZD.CONO = XXCONO;
    //-----------------------------------
      //    UTC mode
      UTCmode = isUTCmode(inAddDO.getQPUTCM());
      if (IN60) {
         MICommon.setError("UTCM", "WUTCM01", inAddDO.getQPUTCM());
         return;
      }
      //-----------------------------------
      // Message type
      XXE065.moveLeftPad(inAddDO.getQPE065());
      RE065();
      if (IN60) {
         return;
      }
      // Check Warehouse
      XXWHLO.moveLeftPad(inAddDO.getQPWHLO());
      RWHL();
      if (IN60) {
         return;
      }
      if (UTCmode) {
         //   Determine time zone for Warehouse
         if (!CRCalendar.retrieveTIZOForWHLO(PXMNGTZN, LDAZD.CONO, XXWHLO)) {
            IN60 = true;
            //   MSGID=S_00148 UTC time conversion failed.
            MICommon.setError("GEDT","S_00148");
            return;
         }
         //   time zone for warehouse
         TIZOforWHLO.move(CRCalendar.getTimeZone());
      }
      //-----------------------------------
      //   Retreive message number if blank
      if (inAddDO.getQPMSGN().isBlank()) {
         RVMSNR();
         inAddDO.setQPMSGN().moveLeft(PXRTVNBR.PXNBNR, 10);
      } else {
         HIHED.setCONO(LDAZD.CONO);
         HIHED.setMSGN().move(inAddDO.getQPMSGN());
         rAPIDS.clear();
         X0OPC.moveLeftPad("*GET");
         rPHEDPIpreCall();
         apCall("MHIHEDPI", rPHEDPI);
         rPHEDPIpostCall();
      }
      //   Default package number if blank
      if (inAddDO.getQPPACN().isBlank()) {
         inAddDO.setQPPACN().moveLeft(inAddDO.getQPMSGN());
      }
      HIPAC.setCONO(LDAZD.CONO);
      HIPAC.setMSGN().move(inAddDO.getQPMSGN());
      HIPAC.setPACN().move(inAddDO.getQPPACN());
      rAPIDS.clear();
      X0OPC.moveLeftPad("*GET");
      rPPACPIpreCall();
      apCall("MHIPACPI", rPPACPI);
      rPPACPIpostCall();
      HILIN.setCONO(LDAZD.CONO);
      HILIN.setMSGN().move(inAddDO.getQPMSGN());
      HILIN.setPACN().move(inAddDO.getQPPACN());
      HILIN.setMSLN(0);
      XXMSLN = HILIN.getMSLN();
      //   Retreive facility for warehouse
      ITWHL.setCONO(LDAZD.CONO);
      ITWHL.setWHLO().move(inAddDO.getQPWHLO());
      if (!ITWHL.CHAIN("00", ITWHL.getKey("00"))) {
         //   MSGID=WWH0103 Warehouse &1 does not exist
         MICommon.setError( "", "WWH0103", inAddDO.getQPWHLO());
         return;
      } else {
         HIHED.setFACI().move(ITWHL.getFACI());
      }
      if (inAddDO.getQPRORC().isBlank()) {
         GTYPE.setCONO(LDAZD.CONO);
         GTYPE.setTRTP().moveLeftPad(inAddDO.getQPTRTP());
         if (GTYPE.CHAIN("00", GTYPE.getKey("00"))) {
            if (GTYPE.getTROC() != 0) {
               inAddDO.setQPRORC().move(GTYPE.getTROC());
            }
         }
      }
      //----------------------------------------------------------------
      //   Check QPOPNO
      this.PXDCCD = 0;
      this.PXFLDD = 4;
      this.PXEDTC = 'P';
      this.PXDCFM = '.';
      this.PXNUM = 0d;
      this.PXALPH.moveLeft(inAddDO.getQPOPNO());
      SRCOMNUM.COMNUM();
      if (SRCOMNUM.PXNMER != 0) {
         //   MSGID=MH85007
         MICommon.setError( "", "MH85007", inAddDO.getQPOPNO());
         return;
      }
      //   Format date/time
      if (inAddDO.getQPGEDT().isBlank() ||
            inAddDO.getQPGETM().isBlank()) {
         XXGEDT = movexDate();
         XXGETM = movexTime();
      } else {
         if (MICommon.toNumericDate(inAddDO.getQPGEDT())) {
            XXGEDT = MICommon.getNumericDate();
         } else {
            MICommon.setError("GEDT");
            return;
         }
         if (!MICommon.toNumeric(inAddDO.getQPGETM())) {
            MICommon.setError("GETM");
            return;
         }
         XXGETM = MICommon.getInt();
         // Date generated
         date = XXGEDT; 
         time = XXGETM / 100;
         if (UTCmode && date > 0) {
            if (!convertFromUTC()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("GEDT","S_00148");
               return;
            }
            XXGEDT = UTCdate;
            XXGETM = UTCtime * 100;
         }
      }
      // ----------------------------------------------------------------
      //   Move data from the transaction input structure (MHS850MI1)   >
      //   to the execution data formats (MHIHED/MHIPAC/MHILIN)         >
      // ----------------------------------------------------------------
      HIHED.setCONO(LDAZD.CONO);
      HIPAC.setCONO(LDAZD.CONO);
      HILIN.setCONO(LDAZD.CONO);
      //---------------------------------------
      HIHED.setSTAT().move("10");
      HILIN.setSTAT().move("10");
      HIPAC.setSTAT().move("10");
      //---------------------------------------
      HIHED.setQLFR().move("51CR");
      HIPAC.setQLFR().move("51CR");
      HILIN.setQLFR().move("51CR");
      //---------------------------------------
      //---------------------------------------
      GTYPE.setCONO(LDAZD.CONO);
      GTYPE.setTRTP().moveLeftPad(inAddDO.getQPTRTP());
      if (GTYPE.CHAIN("00", GTYPE.getKey("00"))) {
         HILIN.setTTYP(GTYPE.getTTYP());
      } else {
         HILIN.setTTYP(51);
      }
      HIHED.setWHLO().move(inAddDO.getQPWHLO());
      HIPAC.setWHLO().move(inAddDO.getQPWHLO());
      HILIN.setWHLO().move(inAddDO.getQPWHLO());
      HIHED.setMSGN().move(inAddDO.getQPMSGN());
      HIPAC.setMSGN().move(inAddDO.getQPMSGN());
      HILIN.setMSGN().move(inAddDO.getQPMSGN());
      HIHED.setPMSN().moveLeft(inAddDO.getQPMSGN());
      HIHED.setE0IO('I');
      //------------------------------------------------
      HIPAC.setPACN().move(inAddDO.getQPPACN());
      HILIN.setPACN().move(inAddDO.getQPPACN());
      HIHED.setGEDT(XXGEDT);
      HIPAC.setGEDT(XXGEDT);
      HILIN.setGEDT(XXGEDT);
      HIHED.setGETM(XXGETM);
      HIPAC.setGETM(XXGETM);
      HILIN.setGETM(XXGETM);
      HIHED.setE0PA().move(inAddDO.getQPE0PA());
      HIHED.setE0PB().move(inAddDO.getQPE0PB());
      HIHED.setE065().move(inAddDO.getQPE065());
      HIHED.setCUNO().move(inAddDO.getQPCUNO());
      HIPAC.setCUNO().move(inAddDO.getQPCUNO());
      HILIN.setCUNO().move(inAddDO.getQPCUNO());
      HIHED.setADID().move(inAddDO.getQPADID());
      HIPAC.setADID().move(inAddDO.getQPADID());
      HILIN.setADID().move(inAddDO.getQPADID());
      HILIN.setITNO().move(inAddDO.getQPITNO());
      XXITNO.moveLeftPad(HILIN.setITNO());
      RITNO();
      //--------------------------------
      HILIN.setPOPN().move(inAddDO.getQPPOPN());
      HILIN.setALWQ().move(inAddDO.getQPALWQ());
      X0FLDD = 2;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddDO.getQPALWT());
      RNUM();
      HILIN.setALWT((int)this.PXNUM);
      HILIN.setWHSL().move(inAddDO.getQPWHSL());
      HILIN.setTWSL().move(inAddDO.getQPTWSL());
      HILIN.setBANO().move(inAddDO.getQPBANO());
      HILIN.setCAMU().move(inAddDO.getQPCAMU());
      XXQTYN = 0d;
      XXQTYA.clear();
      XXQTYA.moveLeft(inAddDO.getQPDLQT());
      this.PXDCCD = X1DCCD;
      RQTY();
      // Quantity recieved must be non-negative
      if (lessThan(this.PXNUM, 6, 0D)) { 
         // MSGID=XNU0003 Negative value is not permitted
         MICommon.setError("DLQT", "XNU0003");
         return;
      }
      HILIN.setDLQT(this.PXNUM);
      XXQTYN = 0d;
      XXQTYA.clear();
      XXQTYA.moveLeft(inAddDO.getQPALQT());
      this.PXDCCD = X1DCCD;
      RQTY();
      // Quantity recieved must be non-negative
      if (lessThan(this.PXNUM, 6, 0D)) { 
         // MSGID=XNU0003 Negative value is not permitted
         MICommon.setError("ALQT", "XNU0003");
         return;
      }
      HILIN.setALQT(this.PXNUM);
      HILIN.setRIDN().move(inAddDO.getQPRIDN());
      X0FLDD = 6;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddDO.getQPRIDL());
      RNUM();
      HILIN.setRIDL((int)this.PXNUM);
      X0FLDD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddDO.getQPRIDX());
      RNUM();
      HILIN.setRIDX((int)this.PXNUM);
      X0FLDD = 11;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddDO.getQPRIDI());
      RNUM();
      HILIN.setRIDI((long)this.PXNUM);
      X0FLDD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddDO.getQPPLSX());
      RNUM();
      HILIN.setPLSX((int)this.PXNUM);
      X0FLDD = 11;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddDO.getQPDLIX());
      RNUM();
      HILIN.setDLIX((long)this.PXNUM);
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddDO.getQPREPN());
      RNUM();
      HILIN.setREPN((long)this.PXNUM);      
      //   Format reporting date
      if (!MICommon.toNumericDate(inAddDO.getQPRPDT())) {
         MICommon.setError("RPDT");
         return;
      }
      HILIN.setRPDT(MICommon.getNumericDate());
      //--------------------------------------------------------------------
      if (!inAddDO.getQPRPTM().isBlank()) {
         X0FLDD = 6;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inAddDO.getQPRPTM());
         RNUM();
         HILIN.setRPTM((int)this.PXNUM);
      }
      if (!UTCmode && 
         !inAddDO.getQPRPDT().isBlank() &&
         inAddDO.getQPRPTM().isBlank()) {
         HILIN.setRPTM(movexTime());
      }
      if (UTCmode) {
         // Reporting date
         if (!inAddDO.getQPRPDT().isBlank() &&
             !inAddDO.getQPRPTM().isBlank()) {
            date = HILIN.getRPDT(); 
            time = HILIN.getRPTM() / 100;           
            if (!convertFromUTC()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("RPDT","S_00148");
               return;
            }
            HILIN.setRPDT(UTCdate);
            HILIN.setRPTM(UTCtime * 100);
         } else {
            IN60 = true;
            //   MSGID=S_00148 UTC time conversion failed.
            MICommon.setError("RPDT","S_00148");
            return;
         }
      }
      //--------------------------------------------------------------------
      HILIN.setUSD1().move(inAddDO.getQPUSD1());
      HILIN.setUSD2().move(inAddDO.getQPUSD2());
      HILIN.setUSD3().move(inAddDO.getQPUSD3());
      HILIN.setUSD4().move(inAddDO.getQPUSD4());
      HILIN.setUSD5().move(inAddDO.getQPUSD5());
      HIHED.setRESP().move(inAddDO.getQPRESP());
      HIPAC.setRESP().move(inAddDO.getQPRESP());
      HILIN.setRESP().move(inAddDO.getQPRESP());
      HILIN.setTRTP().move(inAddDO.getQPTRTP());
      if (!inAddDO.getQPPMSN().isBlank()) {
         HIHED.setPMSN().move(inAddDO.getQPPMSN());
      } else {
         HIHED.setPMSN().moveLeft(inAddDO.getQPMSGN());
      }
      XXQTYN = 0d;
      XXQTYA.clear();
      XXQTYA.moveLeft(inAddDO.getQPCAWE());
      this.PXDCCD = X2DCCD;
      RQTY();
      HILIN.setCAWE(this.PXNUM);
      X0FLDD = 4;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddDO.getQPOPNO());
      RNUM();
      HILIN.setRIDO((int)this.PXNUM);
      HILIN.setRSCD().move(inAddDO.getQPRSCD());
      if (inAddDO.setQPRORC().NE(" ") &&
            inAddDO.setQPRORC().NE("0") &&
            inAddDO.setQPRORC().NE("1") &&
            inAddDO.setQPRORC().NE("2") &&
            inAddDO.setQPRORC().NE("3") &&
            inAddDO.setQPRORC().NE("4") &&
            inAddDO.setQPRORC().NE("5") &&
            inAddDO.setQPRORC().NE("6") &&
            inAddDO.setQPRORC().NE("7") &&
            inAddDO.setQPRORC().NE("8") &&
            inAddDO.setQPRORC().NE("9")) {
         MICommon.setError( "", "MH85012");
         return;
      }
      //------------------------------------
      if (inAddDO.setQPRORC().NE(" ")) {
         X0FLDD = 1;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inAddDO.getQPRORC());
         RNUM();
         //------------------------------------
         if (IN75) {
            //   MSGID=XNU0000 Numeric error
            return;
         }
      }
      //------------------------------------
      HILIN.setRORC((int)this.PXNUM);
      HILIN.setRORN().move(inAddDO.getQPRORN());
      X0FLDD = 6;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddDO.getQPRORL());
      RNUM();
      HILIN.setRORL((int)this.PXNUM);
      X0FLDD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddDO.getQPRORX());
      RNUM();
      HILIN.setRORX((int)this.PXNUM);
      HILIN.setBREF().move(inAddDO.getQPBREF());
      HILIN.setBRE2().move(inAddDO.getQPBRE2());
      HIHED.setCONO(LDAZD.CONO);
      HIHED.setMSGN().move(inAddDO.getQPMSGN());
      if (HIHED.CHAIN("00", HIHED.getKey("00"))) {
         XXOPC.move("*GET");
      } else {
         XXOPC.move("*ADD");
      }
      if (HIHED.getSTAT().GE("90")) {
         //   MSGID=XST0020 Status change are not permitted any longer
         MICommon.setError( "", "XST0020", HIHED.getSTAT());
         return;
      }
      //----------------------------------------------------
      if (IN75) {
         IN60 = true;
         return;
      }
      //----------------------------------------------------
      RHEAD();
      XHIN60 = IN60;
      if (IN60 && IN76) {
         return;
      }
      //----------------------------------------------------
      HIPAC.setCONO(LDAZD.CONO);
      HIPAC.setMSGN().move(inAddDO.getQPMSGN());
      HIPAC.setPACN().move(inAddDO.getQPPACN());
      if (HIPAC.CHAIN("00", HIPAC.getKey("00"))) {
         XXOPC.move("*GET");
      } else {
         XXOPC.move("*ADD");
      }
      //-------------------------------
      if (HIPAC.getSTAT().GE("90")) {
         //   MSGID=XST0020 Status change are not permitted any longer
         MICommon.setError( "", "XST0020", HIPAC.getSTAT());
         return;
      }
      //----------------------------------------------------
      RPACK();
      XPIN60 = IN60;
      if (IN60 && IN76) {
         return;
      }
      //   Validate/create transaction line data
      HILIN.setMSLN(XXMSLN);
      XXOPC.move("*ADD");
      RLINE();
      XLIN60 = IN60;
      if (IN60) {
         return;
      }
      outAddDO.setYPCONO().move(XXCONO);
      outAddDO.setYPMSGN().move(HIHED.getMSGN());

      if (!XHIN60 && !XPIN60 && !XLIN60 && !IN60) {
         XLWHLO.moveLeftPad(XXWHLO);
      }
      MICommon.setData( outAddDO.get());
   }

   public void RCOM30() {
      // ----------------------------------------------------------------
      //   Clear transaction records
      // ----------------------------------------------------------------
      sMHS850MIRAddReplPick inAddReplPick = (sMHS850MIRAddReplPick)MICommon.getInDS(sMHS850MIRAddReplPick.class);
      sMHS850MISAddReplPick outAddReplPick = (sMHS850MISAddReplPick)MICommon.getOutDS(sMHS850MISAddReplPick.class);

      HIHED.clear();
      HIPAC.clear();
      HILIN.clear();
      // ----------------------------------------------------------------
      //   Save process option.
      // ----------------------------------------------------------------
      XXPRFL.move(inAddReplPick.getQOPRFL());
      XXOPC.move("*ADD");
      if (MICommon.toNumericCompany(inAddReplPick.getQOCONO())) {
         XXCONO = MICommon.getInt();
      } else {
         MICommon.setError("CONO");
         return;
      }

      LDAZD.CONO = XXCONO;
    //-----------------------------------
      //    UTC mode
      UTCmode = isUTCmode(inAddReplPick.getQOUTCM());
      if (IN60) {
         MICommon.setError("UTCM", "WUTCM01", inAddReplPick.getQOUTCM());
         return;
      }
      //-----------------------------------
      // Message type
      XXE065.moveLeftPad(inAddReplPick.getQOE065());
      RE065();
      if (IN60) {
         return;
      }
      // Check Warehouse
      XXWHLO.moveLeftPad(inAddReplPick.getQOWHLO());
      RWHL();
      if (IN60) {
         return;
      }
      if (UTCmode) {
         //   Determine time zone for Warehouse
         if (!CRCalendar.retrieveTIZOForWHLO(PXMNGTZN, LDAZD.CONO, XXWHLO)) {
            IN60 = true;
            //   MSGID=S_00148 UTC time conversion failed.
            MICommon.setError("GEDT","S_00148");
            return;
         }
         //   time zone for warehouse
         TIZOforWHLO.move(CRCalendar.getTimeZone());
      }
      //-----------------------------------
      //   Retreive message number if blank
      if (inAddReplPick.getQOMSGN().isBlank()) {
         RVMSNR();
         inAddReplPick.setQOMSGN().moveLeft(PXRTVNBR.PXNBNR, 10);
      } else {
         HIHED.setCONO(LDAZD.CONO);
         HIHED.setMSGN().move(inAddReplPick.getQOMSGN());
         rAPIDS.clear();
         X0OPC.moveLeftPad("*GET");
         rPHEDPIpreCall();
         apCall("MHIHEDPI", rPHEDPI);
         rPHEDPIpostCall();
      }
      //-----------------------------------
      //   SSCC/PANR/DLIX Validation
      //-----------------------------------
      inputDLIX.moveLeftPad(inAddReplPick.getQORIDI());
      inputPANR.moveLeftPad(inAddReplPick.getQOPACN());
      inputSSCC.moveLeftPad(inAddReplPick.getQOSSCC());
      numberPackage = 0;
      if (!validInput()) {
         return;
      }
      if (inAddReplPick.getQORIDI().isBlank()) {
         retrieveDLIX();
         if (IN60) {
            return;
         }
         inAddReplPick.setQOPACN().moveLeftPad(inputPANR);
      }
      //   Default package number if blank
      if (inAddReplPick.getQOPACN().isBlank()) {
         inAddReplPick.setQOPACN().moveLeft(inAddReplPick.getQOMSGN());
      }
      HIPAC.setCONO(LDAZD.CONO);
      HIPAC.setMSGN().move(inAddReplPick.getQOMSGN());
      HIPAC.setPACN().move(inAddReplPick.getQOPACN());
      rAPIDS.clear();
      X0OPC.moveLeftPad("*GET");
      rPPACPIpreCall();
      apCall("MHIPACPI", rPPACPI);
      rPPACPIpostCall();
      HILIN.setCONO(LDAZD.CONO);
      HILIN.setMSGN().move(inAddReplPick.getQOMSGN());
      HILIN.setPACN().move(inAddReplPick.getQOPACN());
      HILIN.setMSLN(0);
      //   Retreive facility for warehouse
      ITWHL.setCONO(LDAZD.CONO);
      ITWHL.setWHLO().move(inAddReplPick.getQOWHLO());
      if (!ITWHL.CHAIN("00", ITWHL.getKey("00"))) {
         //   MSGID=WWH0103 Warehouse &1 does not exist
         MICommon.setError( "", "WWH0103", inAddReplPick.getQOWHLO());
         return;
      } else {
         HIHED.setFACI().move(ITWHL.getFACI());
      }
      //   Format date/time
      if (inAddReplPick.getQOGEDT().isBlank() ||
            inAddReplPick.getQOGETM().isBlank()) {
         XXGEDT = movexDate();
         XXGETM = movexTime();
      } else {
         if (MICommon.toNumericDate(inAddReplPick.getQOGEDT())) {
            XXGEDT = MICommon.getNumericDate();
         } else {
            MICommon.setError("GEDT");
            return;
         }
         if (!MICommon.toNumeric(inAddReplPick.getQOGETM())) {
            MICommon.setError("GETM");
            return;
         }
         XXGETM = MICommon.getInt();
         // Date generated
         date = XXGEDT; 
         time = XXGETM / 100;
         if (UTCmode && date > 0) {
            if (!convertFromUTC()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("GEDT","S_00148");
               return;
            }
            XXGEDT = UTCdate;
            XXGETM = UTCtime * 100;
         }
      }
      // ----------------------------------------------------------------
      //   Move data from the transaction input structure (MHS850MI1)   >
      //   to the execution data formats (MHIHED/MHIPAC/MHILIN)         >
      // ----------------------------------------------------------------
      HIHED.setCONO(LDAZD.CONO);
      HIPAC.setCONO(LDAZD.CONO);
      HILIN.setCONO(LDAZD.CONO);
      HIHED.setQLFR().move("92  ");
      HIPAC.setQLFR().move("92  ");
      HILIN.setQLFR().move("92  ");
      //---------------------------------------
      //---------------------------------------
      HILIN.setTTYP(92);
      HIHED.setE0IO('I');
      HIHED.setWHLO().move(inAddReplPick.getQOWHLO());
      HIPAC.setWHLO().move(inAddReplPick.getQOWHLO());
      HILIN.setWHLO().move(inAddReplPick.getQOWHLO());
      HIHED.setCUNO().moveLeft(inAddReplPick.getQOWHLO());
      HIPAC.setCUNO().moveLeft(inAddReplPick.getQOWHLO());
      HILIN.setCUNO().moveLeft(inAddReplPick.getQOWHLO());
      HIHED.setMSGN().move(inAddReplPick.getQOMSGN());
      HIPAC.setMSGN().move(inAddReplPick.getQOMSGN());
      HILIN.setMSGN().move(inAddReplPick.getQOMSGN());
      HIHED.setPMSN().moveLeft(inAddReplPick.getQOMSGN());
      HIPAC.setPACN().move(inAddReplPick.getQOPACN());
      HILIN.setPACN().move(inAddReplPick.getQOPACN());
      HIHED.setGEDT(XXGEDT);
      HIPAC.setGEDT(XXGEDT);
      HILIN.setGEDT(XXGEDT);
      HIHED.setGETM(XXGETM);
      HIPAC.setGETM(XXGETM);
      HILIN.setGETM(XXGETM);
      HIHED.setE0PA().move(inAddReplPick.getQOE0PA());
      HIHED.setE0PB().move(inAddReplPick.getQOE0PA());
      HIHED.setE035(0);
      HIHED.setE065().move(inAddReplPick.getQOE065());
      HIHED.setADID().move(inAddReplPick.getQOADID());
      HIPAC.setADID().move(inAddReplPick.getQOADID());
      HILIN.setADID().move(inAddReplPick.getQOADID());
      HILIN.setITNO().move(inAddReplPick.getQOITNO());
      XXITNO.moveLeftPad(HILIN.setITNO());
      RITNO();
      //--------------------------------
      HILIN.setPOPN().move(inAddReplPick.getQOPOPN());
      HILIN.setALWQ().move(inAddReplPick.getQOALWQ());
      X0FLDD = 2;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddReplPick.getQOALWT());
      RNUM();
      HILIN.setALWT((int)this.PXNUM);
      X0FLDD = 1;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddReplPick.getQOOEND().getChar());
      RNUM();
      HILIN.setOEND((int)this.PXNUM);
      HILIN.setWHSL().move(inAddReplPick.getQOWHSL());
      HILIN.setBANO().move(inAddReplPick.getQOBANO());
      HILIN.setCAMU().move(inAddReplPick.getQOCAMU());
      XXQTYN = 0d;
      XXQTYA.clear();
      XXQTYA.moveLeft(inAddReplPick.getQOQTYP());
      this.PXDCCD = X1DCCD;
      RQTY();
      HILIN.setDLQT(this.PXNUM);
      XXQTYN = 0d;
      XXQTYA.clear();
      XXQTYA.moveLeft(inAddReplPick.getQOQTYO());
      this.PXDCCD = X1DCCD;
      RQTY();
      HILIN.setALQT(this.PXNUM);
      HILIN.setRIDN().move(inAddReplPick.getQORIDN());
      X0FLDD = 6;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddReplPick.getQORIDL());
      RNUM();
      HILIN.setRIDL((int)this.PXNUM);
      X0FLDD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddReplPick.getQORIDX());
      RNUM();
      HILIN.setRIDX((int)this.PXNUM);
      if (!PTRNSfound) {
         X0FLDD = 11;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inAddReplPick.getQORIDI());
         RNUM();
         HILIN.setRIDI((long)this.PXNUM);
      } else {
         HILIN.setRIDI(PTRNS.getDLIX());
      }
      if (!inAddReplPick.getQOPLRN().isBlank()) {
         X0FLDD = 11;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inAddReplPick.getQOPLRN());
         RNUM();
         HILIN.setPLRN((long)this.PXNUM);
      }
      //   Format reporting date
      if (!inAddReplPick.getQORPDT().isBlank()) {
         if (!MICommon.toNumericDate(inAddReplPick.getQORPDT())) {
            MICommon.setError("RPDT");
            return;
         }
         HILIN.setRPDT(MICommon.getNumericDate());
      }
      //--------------------------------------------------------------------
      if (!inAddReplPick.getQORPTM().isBlank()) {
         X0FLDD = 6;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inAddReplPick.getQORPTM());
         RNUM();
         HILIN.setRPTM((int)this.PXNUM);
      }
      if (!UTCmode && 
         !inAddReplPick.getQORPDT().isBlank() &&
         inAddReplPick.getQORPTM().isBlank()) {
         HILIN.setRPTM(movexTime());
      }
      if (UTCmode) {
         // Reporting date
         if (!inAddReplPick.getQORPDT().isBlank() &&
             !inAddReplPick.getQORPTM().isBlank()) {
            date = HILIN.getRPDT(); 
            time = HILIN.getRPTM() / 100;           
            if (!convertFromUTC()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("RPDT","S_00148");
               return;
            }
            HILIN.setRPDT(UTCdate);
            HILIN.setRPTM(UTCtime * 100);
         } else {
            if (inAddReplPick.getQORPDT().isBlank() ||
               inAddReplPick.getQORPTM().isBlank()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("RPDT","S_00148");
               return;
            }
         }
      }
      //----------------------------------------------------
      HILIN.setUSD1().move(inAddReplPick.getQOUSD1());
      HILIN.setUSD2().move(inAddReplPick.getQOUSD2());
      HILIN.setUSD3().move(inAddReplPick.getQOUSD3());
      HILIN.setUSD4().move(inAddReplPick.getQOUSD4());
      HILIN.setUSD5().move(inAddReplPick.getQOUSD5());
      XXQTYN = 0d;
      XXQTYA.clear();
      XXQTYA.moveLeft(inAddReplPick.getQOCAWE());
      this.PXDCCD = X2DCCD;
      RQTY();
      HILIN.setCAWE(this.PXNUM);
      X0FLDD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddReplPick.getQOPLSX());
      RNUM();
      HILIN.setPLSX((int)this.PXNUM);
      //-----------------------------------------------
      HIPAC.setPARE().move(inAddReplPick.getQOPARE());
      HIPAC.setSSCC().move(inAddReplPick.getQOSSCC());
      X0FLDD = 1;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddReplPick.getQOISMD().getChar());
      RNUM();
      HILIN.setISMD((int)this.PXNUM);
      HILIN.setLODO().moveLeftPad(inAddReplPick.getQOLODO());
      //-----------------------------------------------
      HIHED.setE0IO('I');
      if (!inAddReplPick.getQOPMSN().isBlank()) {
         HIHED.setPMSN().move(inAddReplPick.getQOPMSN());
      } else {
         HIHED.setPMSN().moveLeft(inAddReplPick.getQOMSGN());
      }
      if (IN75) {
         IN60 = true;
         return;
      }
      //-----------------------------------------------
      RHEAD();
      if (IN60 && IN76) {
         return;
      }
      RPACK();
      if (IN60 && IN76) {
         return;
      }
      //   Validate/create transaction line data
      RLINE();
      if (IN60) {
         return;
      }

      outAddReplPick.setYOCONO().move(XXCONO);
      outAddReplPick.setYOMSGN().move(HIHED.getMSGN());
      if (MHS850DS.getJ7STNB() > 0L) {
         X0FLDD = 10;
         XXNUMN = (long)MHS850DS.getJ7STNB();
         XXNUMA.clear();
         RNUMO();
         outAddReplPick.setYOSTNB().moveLeft(this.PXALPH);
         X0FLDD = 7;
         XXNUMN = (int)MHS850DS.getJ7STRN();
         XXNUMA.clear();
         RNUMO();
         outAddReplPick.setYOSTRN().moveLeft(this.PXALPH);
      }
      MICommon.setData( outAddReplPick.get());
   }

   public void RCOM31() {
      // ----------------------------------------------------------------
      //   Clear transaction records
      // ----------------------------------------------------------------
      sMHS850MIRAddCfmPickList inAddCfmPickList = (sMHS850MIRAddCfmPickList)MICommon.getInDS(sMHS850MIRAddCfmPickList.class);
      sMHS850MISAddCfmPickList outAddCfmPickList = (sMHS850MISAddCfmPickList)MICommon.getOutDS(sMHS850MISAddCfmPickList.class);

      HIHED.clear();
      HIPAC.clear();
      HILIN.clear();
      // ----------------------------------------------------------------
      //   Save process option.
      // ----------------------------------------------------------------
      XXPRFL.move(inAddCfmPickList.getQQPRMD());
      XXOPC.move("*ADD");
      if (MICommon.toNumericCompany(inAddCfmPickList.getQQCONO())) {
         XXCONO = MICommon.getInt();
      } else {
         MICommon.setError("CONO");
         return;
      }

      LDAZD.CONO = XXCONO;
    //-----------------------------------
      //    UTC mode
      UTCmode = isUTCmode(inAddCfmPickList.getQQUTCM());
      if (IN60) {
         MICommon.setError("UTCM", "WUTCM01", inAddCfmPickList.getQQUTCM());
         return;
      }
      //-----------------------------------
      // Message type
      XXE065.moveLeftPad(inAddCfmPickList.getQQE065());
      RE065();
      if (IN60) {
         return;
      }
      // Check Warehouse
      XXWHLO.moveLeftPad(inAddCfmPickList.getQQWHLO());
      RWHL();
      if (IN60) {
         return;
      }
      if (UTCmode) {
         //   Determine time zone for Warehouse
         if (!CRCalendar.retrieveTIZOForWHLO(PXMNGTZN, LDAZD.CONO, XXWHLO)) {
            IN60 = true;
            //   MSGID=S_00148 UTC time conversion failed.
            MICommon.setError("GEDT","S_00148");
            return;
         }
         //   time zone for warehouse
         TIZOforWHLO.move(CRCalendar.getTimeZone());
      }
      //-----------------------------------
      //   Retreive message number if blank
      if (inAddCfmPickList.getQQPLSX().isBlank()) {
         //   MSGID=WPL1802
         MICommon.setError( "", "WPL1802");
         return;
      }
      if (inAddCfmPickList.getQQMSGN().isBlank()) {
         RVMSNR();
         inAddCfmPickList.setQQMSGN().moveLeft(PXRTVNBR.PXNBNR, 10);
      } else {
         HIHED.setCONO(LDAZD.CONO);
         HIHED.setMSGN().move(inAddCfmPickList.getQQMSGN());
         rAPIDS.clear();
         X0OPC.moveLeftPad("*GET");
         rPHEDPIpreCall();
         apCall("MHIHEDPI", rPHEDPI);
         rPHEDPIpostCall();
      }
      //   Default package number if blank
      if (inAddCfmPickList.getQQPACN().isBlank()) {
         inAddCfmPickList.setQQPACN().moveLeft(inAddCfmPickList.getQQMSGN());
      }
      HIPAC.setCONO(LDAZD.CONO);
      HIPAC.setMSGN().move(inAddCfmPickList.getQQMSGN());
      HIPAC.setPACN().move(inAddCfmPickList.getQQPACN());
      rAPIDS.clear();
      X0OPC.moveLeftPad("*GET");
      rPPACPIpreCall();
      apCall("MHIPACPI", rPPACPI);
      rPPACPIpostCall();
      //   Retreive facility for warehouse
      ITWHL.setCONO(LDAZD.CONO);
      ITWHL.setWHLO().move(inAddCfmPickList.getQQWHLO());
      if (!ITWHL.CHAIN("00", ITWHL.getKey("00"))) {
         //   MSGID=WWH0103 Warehouse &1 does not exist
         MICommon.setError( "", "WWH0103", inAddCfmPickList.getQQWHLO());
         return;
      } else {
         HIHED.setFACI().move(ITWHL.getFACI());
      }
      //   Format date/time
      if (inAddCfmPickList.getQQGEDT().isBlank() ||
            inAddCfmPickList.getQQGETM().isBlank()) {
         XXGEDT = movexDate();
         XXGETM = movexTime();
      } else {
         if (MICommon.toNumericDate(inAddCfmPickList.getQQGEDT())) {
            XXGEDT = MICommon.getNumericDate();
         } else {
            MICommon.setError("GEDT");
            return;
         }
         if (!MICommon.toNumeric(inAddCfmPickList.getQQGETM())) {
            MICommon.setError("GETM");
            return;
         }
         XXGETM = MICommon.getInt();
         // Date generated
         date = XXGEDT; 
         time = XXGETM / 100;
         if (UTCmode && date > 0) {
            if (!convertFromUTC()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("GEDT","S_00148");
               return;
            }
            XXGEDT = UTCdate;
            XXGETM = UTCtime * 100;
         }
      }
      // ----------------------------------------------------------------
      //   Move data from the transaction input structure (MHS850MI1)   >
      //   to the execution data formats (MHIHED/MHIPAC/MHILIN)         >
      // ----------------------------------------------------------------
      // Check MHPICH picking status
      HPICH.setCONO(LDAZD.CONO);
      X0FLDD = 11;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddCfmPickList.getQQDLIX());
      RNUM();
      HPICH.setDLIX((long)this.PXNUM);
      X0FLDD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddCfmPickList.getQQPLSX());
      RNUM();
      HPICH.setPLSX((int)this.PXNUM);
      IN91 = !HPICH.CHAIN("00", HPICH.getKey("00"));
      if (IN91) {
         //   MSGID=MH85011
         MICommon.setError( "", "MH85011");
         return;
      }
      if (!IN91 && HPICH.getPISS().GE("70")) {
         //   MSGID=MH85006
         MICommon.setError( "", "MH85006");
         return;
      }
      // ----------------------------------------------------------------
      HIHED.setCONO(LDAZD.CONO);
      HIPAC.setCONO(LDAZD.CONO);
      HIHED.setQLFR().move("CFMP");
      HIPAC.setQLFR().move("CFMP");
      //---------------------------------------
      //---------------------------------------
      HIHED.setWHLO().move(inAddCfmPickList.getQQWHLO());
      HIPAC.setWHLO().move(inAddCfmPickList.getQQWHLO());
      HIHED.setE0PA().move(inAddCfmPickList.getQQE0PA());
      HIHED.setE0PB().move(inAddCfmPickList.getQQE0PA());
      HIHED.setE065().move(inAddCfmPickList.getQQE065());
      HIHED.setMSGN().move(inAddCfmPickList.getQQMSGN());
      HIHED.setE0IO('I');
      HIPAC.setMSGN().move(inAddCfmPickList.getQQMSGN());
      HIPAC.setPACN().move(inAddCfmPickList.getQQPACN());
      HIHED.setGEDT(XXGEDT);
      HIPAC.setGEDT(XXGEDT);
      HIHED.setGETM(XXGETM);
      HIPAC.setGETM(XXGETM);
      if (!inAddCfmPickList.getQQPMSN().isBlank()) {
         HIHED.setPMSN().move(inAddCfmPickList.getQQPMSN());
      } else {
         HIHED.setPMSN().moveLeft(inAddCfmPickList.getQQMSGN());
      }
      RHEAD();
      if (IN60 && IN76) {
         return;
      }
      RPACK();
      if (IN60 && IN76) {
         return;
      }
      //   Validate/create transaction line data
      ITALO.setCONO(LDAZD.CONO);
      X0FLDD = 11;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddCfmPickList.getQQDLIX());
      RNUM();
      ITALO.setRIDI((long)this.PXNUM);
      X0FLDD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddCfmPickList.getQQPLSX());
      RNUM();
      ITALO.setPLSX((int)this.PXNUM);
      ITALO.setWHLO().moveLeft(inAddCfmPickList.getQQWHLO());
      ITALO.SETLL("30", ITALO.getKey(LDAZD.CONO, "30", 4));
      while (ITALO.READE("30", ITALO.getKey(LDAZD.CONO, "30", 4))) {
         //----------------------------------------------------------------------
         HILIN.setCONO(LDAZD.CONO);
         HILIN.setWHLO().move(ITALO.getWHLO());
         //----------------------------------------------------------------------
         HDISL.setCONO(LDAZD.CONO);
         HDISL.setDLIX(ITALO.getRIDI());
         HDISL.setRORC(ITALO.getTTYP()/10);
         HDISL.setRIDN().move(ITALO.getRIDN());
         HDISL.setRIDL(ITALO.getRIDL());
         HDISL.setRIDX(ITALO.getRIDX());
         if (HDISL.CHAIN("00", HDISL.getKey("00"))) {
            HILIN.setFACI().move(HDISL.getFACI());
         } else {
            HILIN.setFACI().move(ITWHL.getFACI());
         }
         //----------------------------------------------------------------------
         HILIN.setMSGN().move(inAddCfmPickList.getQQMSGN());
         HILIN.setPACN().move(inAddCfmPickList.getQQPACN());
         HILIN.setMSLN(0);
         HILIN.setQLFR().move("CFMP");
         HILIN.setTTYP(ITALO.getTTYP());
         HILIN.setDLIX(ITALO.getRIDI());
         HILIN.setPLSX(ITALO.getPLSX());
         HILIN.setCAWE(ITALO.getCAWE());
         HILIN.setALQT(ITALO.getALQT());
         HILIN.setPAQT(ITALO.getPAQT());
         HILIN.setDLQT(ITALO.getALQT());
         HILIN.setDLQA(ITALO.getALQT());
         HILIN.setWHSL().move(ITALO.getWHSL());
         HILIN.setBANO().move(ITALO.getBANO());
         HILIN.setCAMU().move(ITALO.getCAMU());
         HILIN.setITNO().move(ITALO.getITNO());
         HILIN.setOEND(ITALO.getOEND());
         //----------------------------------------------------------------------
         ITMAS.setCONO(LDAZD.CONO);
         ITMAS.setITNO().move(ITALO.getITNO());
         if (ITMAS.CHAIN("00", ITMAS.getKey("00"))) {
            HILIN.setITDS().move(ITMAS.getITDS());
         } else {
            HILIN.setITDS().clear();
         }
         //----------------------------------------------------------------------
         HILIN.setALWT(0);
         HILIN.setALWQ().clear();
         ITPOP.setCONO(LDAZD.CONO);
         ITPOP.setALWT(2);
         ITPOP.setITNO().move(ITALO.getITNO());
         ITPOP.setALWQ().move(ITWHL.getALWQ());
         if (ITPOP.CHAIN("30", ITPOP.getKey("30", 4))) {
            HILIN.setPOPN().move(ITPOP.getPOPN());
            HILIN.setALWQ().move(ITPOP.getALWQ());
            HILIN.setALWT(ITPOP.getALWT());
         } else {
            ITPOP.setALWQ().clear();
            ITPOP.SETLL("30", ITPOP.getKey("30", 4));
            if (ITPOP.READE("30", ITPOP.getKey("30", 3))) {
               HILIN.setPOPN().move(ITPOP.getPOPN());
               HILIN.setALWQ().move(ITPOP.getALWQ());
               HILIN.setALWT(ITPOP.getALWT());
            } else {
               HILIN.setPOPN().clear();
            }
         }
         //----------------------------------------------------------------------
         HILIN.setPACN().move(inAddCfmPickList.getQQPACN());
         HILIN.setTWSL().move(inAddCfmPickList.getQQTWSL());
         HILIN.setREPN(ITALO.getREPN());
         HILIN.setRIDN().move(ITALO.getRIDN());
         HILIN.setRIDL(ITALO.getRIDL());
         HILIN.setRIDX(ITALO.getRIDX());
         HILIN.setRIDO(ITALO.getRIDO());
         //----------------------------------------------------------------------
         HILIN.setPLRN(ITALO.getPLRN());
         //-----------------------------------------------------------------------------------------------
         HILIN.setGEDT(XXGEDT);
         HILIN.setGETM(XXGETM);
         //-----------------------------------------------------------------------------------------------
         HPICD.setCONO(LDAZD.CONO);
         HPICD.setE0PA().move(inAddCfmPickList.getQQE0PA());
         HPICD.setWHLO().move(ITALO.getWHLO());
         HPICD.setDLIX(ITALO.getRIDI());
         HPICD.setPLSX(ITALO.getPLSX());
         HPICD.setTTYP(ITALO.getTTYP());
         HPICD.setRIDN().move(ITALO.getRIDN());
         HPICD.setRIDL(ITALO.getRIDL());
         HPICD.setRIDX(ITALO.getRIDX());
         HPICD.setRIDO(ITALO.getRIDO());
         HPICD.setWHSL().move(ITALO.getWHSL());
         HPICD.setBANO().move(ITALO.getBANO());
         HPICD.setCAMU().move(ITALO.getCAMU());
         if (!HPICD.CHAIN("00", HPICD.getKey("00"))) {
            HPICD.setUSD1().clear();
            HPICD.setUSD2().clear();
            HPICD.setUSD3().clear();
            HPICD.setUSD4().clear();
            HPICD.setUSD5().clear();
         }
         //-----------------------------------------------------------------------------------------------
         if (!inAddCfmPickList.getQQUSD1().isBlank()) {
            HILIN.setUSD1().move(inAddCfmPickList.getQQUSD1());
         } else {
            HILIN.setUSD1().move(HPICD.getUSD1());
         }
         if (!inAddCfmPickList.getQQUDS2().isBlank()) {
            HILIN.setUSD2().move(inAddCfmPickList.getQQUDS2());
         } else {
            HILIN.setUSD2().move(HPICD.getUSD2());
         }
         if (!inAddCfmPickList.getQQUSD3().isBlank()) {
            HILIN.setUSD3().move(inAddCfmPickList.getQQUSD3());
         } else {
            HILIN.setUSD3().move(HPICD.getUSD3());
         }
         if (!inAddCfmPickList.getQQUSD4().isBlank()) {
            HILIN.setUSD4().move(inAddCfmPickList.getQQUSD4());
         } else {
            HILIN.setUSD4().move(HPICD.getUSD4());
         }
         if (!inAddCfmPickList.getQQUSD5().isBlank()) {
            HILIN.setUSD5().move(inAddCfmPickList.getQQUSD5());
         } else {
            HILIN.setUSD5().move(HPICD.getUSD5());
         }
         //----------------------------------------------------------------------
         if (!MICommon.toNumericDate(inAddCfmPickList.getQQRPDT())) {
            MICommon.setError("RPDT");
            return;
         }
         HILIN.setRPDT(MICommon.getNumericDate());
         //--------------------------------------------------------------------
         if (!inAddCfmPickList.getQQRPTM().isBlank()) {
            X0FLDD = 6;
            XXNUMN = 0d;
            XXNUMA.clear();
            XXNUMA.moveLeft(inAddCfmPickList.getQQRPTM());
            RNUM();
            HILIN.setRPTM((int)this.PXNUM);
         }
         if (!UTCmode && 
            !inAddCfmPickList.getQQRPDT().isBlank() &&
               inAddCfmPickList.getQQRPTM().isBlank()) {
            HILIN.setRPTM(movexTime());
         }
         if (UTCmode) {
            // Reporting date
            if (!inAddCfmPickList.getQQRPDT().isBlank() &&
                !inAddCfmPickList.getQQRPTM().isBlank()) {
               date = HILIN.getRPDT(); 
               time = HILIN.getRPTM() / 100;           
               if (!convertFromUTC()) {
                  IN60 = true;
                  //   MSGID=S_00148 UTC time conversion failed.
                  MICommon.setError("RPDT","S_00148");
                  return;
               }
               HILIN.setRPDT(UTCdate);
               HILIN.setRPTM(UTCtime * 100);
            } else {
               if (inAddCfmPickList.getQQRPDT().isBlank() ||
                   inAddCfmPickList.getQQRPTM().isBlank()) {
                  IN60 = true;
                  //   MSGID=S_00148 UTC time conversion failed.
                  MICommon.setError("RPDT","S_00148");
                  return;
               }
            }
         }
         //----------------------------------------------------------------------
         X0FLDD = 1;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inAddCfmPickList.getQQISMD());
         RNUM();
         HILIN.setISMD((int)this.PXNUM);
         HILIN.setLODO().moveLeftPad(inAddCfmPickList.getQQLODO());
         //----------------------------------------------------------------------
         RLINE();
         if (IN60) {
            return;
         }
      }

      //----------------------------------------------------------------------

      if (!XXPRFL.isBlank()) {
         if (XXPRFL.EQ("*AUT") && XXOPC.NE("*DLT")) {
            RAUT();
         } else {
            if (XXPRFL.EQ("*EXE") && XXOPC.NE("*DLT")) {
               rAPIDS.clear();
               X0OPC.moveLeftPad("*CHK");
               rPHEDPIpreCall();
               apCall("MHILINPI", rPHEDPI);
               rPHEDPIpostCall();
               IN60 = toBoolean(X0IN60.getChar());
               if (IN60) {
                  XXERRM = true;
                  MICommon.setError( "", X0MSID.toString(), X0MSGD);
                  return;
               }
               MvxString MSLN = new MvxString(5);
               X0FLDD = 5;
               XXNUMN = (double)HILIN.getMSLN();
               XXNUMA.clear();
               RNUM();
               MSLN.moveRight(this.PXALPH);
               whsTrans(inAddCfmPickList.getQQCONO(), HILIN.getMSGN(), HILIN.getPACN(), MSLN, XXPRFL);
               if (IN60) {
                  XXERRM = true;
               }
            }
         }
      }
      //----------------------------------------------------------------------
      outAddCfmPickList.setYQCONO().move(XXCONO);
      outAddCfmPickList.setYQMSGN().move(HIHED.getMSGN());
      MICommon.setData( outAddCfmPickList.get());
   }

   public void RCOM32() {
      // ----------------------------------------------------------------
      //   Clear transaction records
      // ----------------------------------------------------------------
      sMHS850MIRAddPickViaRepNo inAddPickViaRepNo = (sMHS850MIRAddPickViaRepNo)MICommon.getInDS(sMHS850MIRAddPickViaRepNo.class);
      sMHS850MISAddPickViaRepNo outAddPickViaRepNo = (sMHS850MISAddPickViaRepNo)MICommon.getOutDS(sMHS850MISAddPickViaRepNo.class);

      HIHED.clear();
      HIPAC.clear();
      HILIN.clear();
      // ----------------------------------------------------------------
      //   Save process option.
      // ----------------------------------------------------------------
      XXPRFL.move(inAddPickViaRepNo.getQRPRMD());
      XXOPC.move("*ADD");
      if (MICommon.toNumericCompany(inAddPickViaRepNo.getQRCONO())) {
         XXCONO = MICommon.getInt();
      } else {
         MICommon.setError("CONO");
         return;
      }

      LDAZD.CONO = XXCONO;
    //-----------------------------------
      //    UTC mode
      UTCmode = isUTCmode(inAddPickViaRepNo.getQRUTCM());
      if (IN60) {
         MICommon.setError("UTCM", "WUTCM01", inAddPickViaRepNo.getQRUTCM());
         return;
      }
      //-----------------------------------
      // Message type
      XXE065.moveLeftPad(inAddPickViaRepNo.getQRE065());
      RE065();
      if (IN60) {
         return;
      }
      // Check Warehouse
      XXWHLO.moveLeftPad(inAddPickViaRepNo.getQRWHLO());
      RWHL();
      if (IN60) {
         return;
      }
      if (UTCmode) {
         //   Determine time zone for Warehouse
         if (!CRCalendar.retrieveTIZOForWHLO(PXMNGTZN, LDAZD.CONO, XXWHLO)) {
            IN60 = true;
            //   MSGID=S_00148 UTC time conversion failed.
            MICommon.setError("GEDT","S_00148");
            return;
         }
         //   time zone for warehouse
         TIZOforWHLO.move(CRCalendar.getTimeZone());
      }
      //-----------------------------------
      //   Retreive message number if blank
      if (inAddPickViaRepNo.getQRMSGN().isBlank()) {
         RVMSNR();
         inAddPickViaRepNo.setQRMSGN().moveLeft(PXRTVNBR.PXNBNR, 10);
      } else {
         HIHED.setCONO(LDAZD.CONO);
         HIHED.setMSGN().move(inAddPickViaRepNo.getQRMSGN());
         rAPIDS.clear();
         X0OPC.moveLeftPad("*GET");
         rPHEDPIpreCall();
         apCall("MHIHEDPI", rPHEDPI);
         rPHEDPIpostCall();
      }
      //   Default package number if blank
      if (inAddPickViaRepNo.getQRPACN().isBlank()) {
         inAddPickViaRepNo.setQRPACN().moveLeft(inAddPickViaRepNo.getQRMSGN());
      }
      HIPAC.setCONO(LDAZD.CONO);
      HIPAC.setMSGN().move(inAddPickViaRepNo.getQRMSGN());
      HIPAC.setPACN().move(inAddPickViaRepNo.getQRPACN());
      rAPIDS.clear();
      X0OPC.moveLeftPad("*GET");
      rPPACPIpreCall();
      apCall("MHIPACPI", rPPACPI);
      rPPACPIpostCall();
      //   Retreive next line in sequence
      HILIN.setCONO(LDAZD.CONO);
      HILIN.setMSGN().move(inAddPickViaRepNo.getQRMSGN());
      HILIN.setPACN().move(inAddPickViaRepNo.getQRPACN());
      HILIN.setMSLN(0);
      //   Retreive facility for warehouse
      ITWHL.setCONO(LDAZD.CONO);
      ITWHL.setWHLO().move(inAddPickViaRepNo.getQRWHLO());
      if (!ITWHL.CHAIN("00", ITWHL.getKey("00"))) {
         //   MSGID=WWH0103 Warehouse &1 does not exist
         MICommon.setError( "", "WWH0103", inAddPickViaRepNo.getQRWHLO());
         return;
      } else {
         HIHED.setFACI().move(ITWHL.getFACI());
      }
      //   Format date/time
      if (inAddPickViaRepNo.getQRGEDT().isBlank() ||
            inAddPickViaRepNo.getQRGETM().isBlank()) {
         XXGEDT = movexDate();
         XXGETM = movexTime();
      } else {
         if (MICommon.toNumericDate(inAddPickViaRepNo.getQRGEDT())) {
            XXGEDT = MICommon.getNumericDate();
         } else {
            MICommon.setError("GEDT");
            return;
         }
         if (!MICommon.toNumeric(inAddPickViaRepNo.getQRGETM())) {
            MICommon.setError("GETM");
            return;
         }
         XXGETM = MICommon.getInt();
         // Date generated
         date = XXGEDT; 
         time = XXGETM / 100;
         if (UTCmode && date > 0) {
            if (!convertFromUTC()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("GEDT","S_00148");
               return;
            }
            XXGEDT = UTCdate;
            XXGETM = UTCtime * 100;
         }
      }
      // ----------------------------------------------------------------
      //   Move data from the transaction input structure (MHS850MI1)   >
      //   to the execution data formats (MHIHED/MHIPAC/MHILIN)         >
      // ----------------------------------------------------------------
      HIHED.setCONO(LDAZD.CONO);
      HIPAC.setCONO(LDAZD.CONO);
      HILIN.setCONO(LDAZD.CONO);
      HIHED.setQLFR().move("PLRN");
      HIPAC.setQLFR().move("PLRN");
      HIHED.setE0IO('I');
      HILIN.setQLFR().move("PLRN");
      //---------------------------------------
      //---------------------------------------
      HIHED.setWHLO().move(inAddPickViaRepNo.getQRWHLO());
      HIPAC.setWHLO().move(inAddPickViaRepNo.getQRWHLO());
      HILIN.setWHLO().move(inAddPickViaRepNo.getQRWHLO());
      HIHED.setMSGN().move(inAddPickViaRepNo.getQRMSGN());
      HIPAC.setMSGN().move(inAddPickViaRepNo.getQRMSGN());
      HILIN.setMSGN().move(inAddPickViaRepNo.getQRMSGN());
      HIHED.setPMSN().moveLeft(inAddPickViaRepNo.getQRMSGN());
      HIPAC.setPACN().move(inAddPickViaRepNo.getQRPACN());
      HILIN.setPACN().move(inAddPickViaRepNo.getQRPACN());
      HIHED.setGEDT(XXGEDT);
      HIPAC.setGEDT(XXGEDT);
      HILIN.setGEDT(XXGEDT);
      HIHED.setGETM(XXGETM);
      HIPAC.setGETM(XXGETM);
      HILIN.setGETM(XXGETM);
      HIHED.setE0PA().move(inAddPickViaRepNo.getQRE0PA());
      HIHED.setE0PB().move(inAddPickViaRepNo.getQRE0PA());
      HIHED.setE035(0);
      HIHED.setE065().move(inAddPickViaRepNo.getQRE065());
      X0FLDD = 1;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddPickViaRepNo.getQROEND().getChar());
      RNUM();
      HILIN.setOEND((int)this.PXNUM);
      HILIN.setWHSL().move(inAddPickViaRepNo.getQRWHSL());
      HILIN.setTWSL().move(inAddPickViaRepNo.getQRTWSL());
      HILIN.setBANO().move(inAddPickViaRepNo.getQRBANO());
      HILIN.setCAMU().move(inAddPickViaRepNo.getQRCAMU());
      X0FLDD = 11;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddPickViaRepNo.getQRPLRN());
      RNUM();
      HILIN.setPLRN((long)this.PXNUM);
      ITALO.setCONO(HILIN.getCONO());
      ITALO.setPLRN(HILIN.getPLRN());
      if (ITALO.CHAIN("50", ITALO.getKey("50", 2))) {
         XXITNO.moveLeftPad(ITALO.getITNO());
         RITNO();
      }
      XXQTYN = 0d;
      XXQTYA.clear();
      XXQTYA.moveLeft(inAddPickViaRepNo.getQRQTYP());
      this.PXDCCD = X1DCCD;
      RQTY();
      HILIN.setDLQT(this.PXNUM);
      HILIN.setPAQT(this.PXNUM);
      XXQTYN = 0d;
      XXQTYA.clear();
      XXQTYA.moveLeft(inAddPickViaRepNo.getQRQTYO());
      this.PXDCCD = X1DCCD;
      RQTY();
      HILIN.setALQT(this.PXNUM);
      X0FLDD = 7;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddPickViaRepNo.getQRAMKO());
      RNUM();
      HIPAC.setAMKO((int)this.PXNUM);
      HIPAC.setPACT().move(inAddPickViaRepNo.getQRPACT());
      if (!inAddPickViaRepNo.getQRRPDT().isBlank()) {
         if (!MICommon.toNumericDate(inAddPickViaRepNo.getQRRPDT())) {
            MICommon.setError("RPDT");
            return;
         }
         HILIN.setRPDT(MICommon.getNumericDate());
      }
      //--------------------------------------------------------------------
      if (!inAddPickViaRepNo.getQRRPTM().isBlank()) {
         X0FLDD = 6;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inAddPickViaRepNo.getQRRPTM());
         RNUM();
         HILIN.setRPTM((int)this.PXNUM);
      }
      if (!UTCmode && 
         !inAddPickViaRepNo.getQRRPDT().isBlank() &&
         inAddPickViaRepNo.getQRRPTM().isBlank()) {
         HILIN.setRPTM(movexTime());
      }
      if (UTCmode) {
         // Reporting date
         if (!inAddPickViaRepNo.getQRRPDT().isBlank() &&
             !inAddPickViaRepNo.getQRRPTM().isBlank()) {
            date = HILIN.getRPDT(); 
            time = HILIN.getRPTM() / 100;           
            if (!convertFromUTC()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("RPDT","S_00148");
               return;
            }
            HILIN.setRPDT(UTCdate);
            HILIN.setRPTM(UTCtime * 100);
         } else {
            if (inAddPickViaRepNo.getQRRPDT().isBlank() ||
               inAddPickViaRepNo.getQRRPTM().isBlank()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("RPDT","S_00148");
               return;
            }
         }
      }
      //--------------------------------------------------------------------
      XXQTYN = 0d;
      XXQTYA.clear();
      XXQTYA.moveLeft(inAddPickViaRepNo.getQRCAWE());
      this.PXDCCD = X2DCCD;
      RQTY();
      HILIN.setCAWE(this.PXNUM);
      //--------------------------------------------------------------------
      HILIN.setUSD1().move(inAddPickViaRepNo.getQRUSD1());
      HILIN.setUSD2().move(inAddPickViaRepNo.getQRUSD2());
      HILIN.setUSD3().move(inAddPickViaRepNo.getQRUSD3());
      HILIN.setUSD4().move(inAddPickViaRepNo.getQRUSD4());
      HILIN.setUSD5().move(inAddPickViaRepNo.getQRUSD5());
      HIPAC.setPARE().move(inAddPickViaRepNo.getQRPARE());
      HIPAC.setSSCC().move(inAddPickViaRepNo.getQRSSCC());
      HILIN.setBREF().move(inAddPickViaRepNo.getQRBREF());
      HILIN.setBRE2().move(inAddPickViaRepNo.getQRBRE2());
      if (!inAddPickViaRepNo.getQRPMSN().isBlank()) {
         HIHED.setPMSN().move(inAddPickViaRepNo.getQRPMSN());
      } else {
         HIHED.setPMSN().moveLeft(inAddPickViaRepNo.getQRMSGN());
      }
      X0FLDD = 1;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddPickViaRepNo.getQRISMD().getChar());
      RNUM();
      HILIN.setISMD((int)this.PXNUM);
      HILIN.setLODO().moveLeftPad(inAddPickViaRepNo.getQRLODO());
      //---------------------------------------
      X0FLDD = 9;
      X0DCCD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddPickViaRepNo.getQRGRWE());
      RNUM();
      HIPAC.setGRWE(this.PXNUM);
      X0FLDD = 9;
      X0DCCD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddPickViaRepNo.getQRVOM3());
      RNUM();
      HIPAC.setVOM3(this.PXNUM);
      X0FLDD = 11;
      X0DCCD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddPickViaRepNo.getQRFRCP());
      RNUM();
      HIPAC.setFRCP(this.PXNUM);
      //-----------------------------
      //   Validate/create transaction header data
      if (IN75) {
         IN60 = true;
         return;
      }
      RHEAD();
      if (IN60 && IN76) {
         return;
      }
      //   Validate/create transaction package data
      RPACK();
      if (IN60 && IN76) {
         return;
      }
      //   Validate/create transaction line data
      RLINE();
      if (IN60) {
         return;
      }

      outAddPickViaRepNo.setYRCONO().move(XXCONO);
      outAddPickViaRepNo.setYRMSGN().move(HIHED.getMSGN());
      if (MHS850DS.getJ7STNB() > 0L) {
         X0FLDD = 10;
         XXNUMN = (long)MHS850DS.getJ7STNB();
         XXNUMA.clear();
         RNUMO();
         outAddPickViaRepNo.setYRSTNB().moveLeft(this.PXALPH);
         X0FLDD = 7;
         XXNUMN = (int)MHS850DS.getJ7STRN();
         XXNUMA.clear();
         RNUMO();
         outAddPickViaRepNo.setYRSTRN().moveLeft(this.PXALPH);
      }
      MICommon.setData( outAddPickViaRepNo.get());
   }

   public void RCOM33() {
      sMHS850MIRLstMvxMsg inLstMvxMsg = (sMHS850MIRLstMvxMsg)MICommon.getInDS(sMHS850MIRLstMvxMsg.class);
      sMHS850MISLstMvxMsg outLstMvxMsg = (sMHS850MISLstMvxMsg)MICommon.getOutDS(sMHS850MISLstMvxMsg.class);

      if (MICommon.toNumericCompany(inLstMvxMsg.getQSCONO())) {
         XXCONO = MICommon.getInt();
      } else {
         MICommon.setError("CONO");
         return;
      }
      LDAZD.CONO = XXCONO;
      //   Test header records for external message records
      HIHED.setCONO(LDAZD.CONO);
      if(inLstMvxMsg.getQSE0PB().isBlank()) {
         MICommon.setError( "E0PB", "SO94201"); // Mandatory fields are missing
         return;
      }
      HIHED.setE0PB().move(inLstMvxMsg.getQSE0PB());
      HIHED.setPMSN().move(inLstMvxMsg.getQSPMSN());
      IN91 = HIHED.EXISTSLL("40", HIHED.getKey("40", 3));
      if (inLstMvxMsg.getQSPMSN().isBlank()) {
         IN91 = !HIHED.READE("40", HIHED.getKey("40", 2));
      } else {
         IN91 = !HIHED.READE("40", HIHED.getKey("40", 3));
      }
      XXRCD = 0;
      while (!IN91 && XXRCD != MICommon.getMaxRecords()) {
         if (inLstMvxMsg.getQSSTAT().isBlank() ||
               !inLstMvxMsg.getQSSTAT().isBlank() &&
               inLstMvxMsg.getQSSTAT().EQ(HIHED.getSTAT())) {

            outLstMvxMsg.setYSCONO().move(XXCONO);
            outLstMvxMsg.setYSMSGN().move(HIHED.getMSGN());
            outLstMvxMsg.setYSSTAT().move(HIHED.getSTAT());
            // ----------------------------------------------------------------
            //   Send data to the client
            // ----------------------------------------------------------------
            MICommon.setData(outLstMvxMsg.get());
            MICommon.reply();
         }
         if (inLstMvxMsg.getQSPMSN().isBlank()) {
            IN91 = !HIHED.READE("40", HIHED.getKey("40", 2));
         } else {
            IN91 = !HIHED.READE("40", HIHED.getKey("40", 3));
         }
         XXRCD++;
      }
      //--------------------------------------------------
      MICommon.clearBuffer();
   }



   /**
   *    RCOM34 - Execute command - AddROReceipt
   */
   public void RCOM34() {
      // ----------------------------------------------------------------
      //   Clear transaction records
      // ----------------------------------------------------------------
      sMHS850MIRAddROReceipt inAddROReceipt = (sMHS850MIRAddROReceipt)MICommon.getInDS(sMHS850MIRAddROReceipt.class);
      sMHS850MISAddROReceipt outAddROReceipt = (sMHS850MISAddROReceipt)MICommon.getOutDS(sMHS850MISAddROReceipt.class);

      HIHED.clear();
      HIPAC.clear();
      HILIN.clear();
      // ----------------------------------------------------------------
      //   Save process option.
      // ----------------------------------------------------------------
      XXPRFL.move(inAddROReceipt.getQYPRFL());
      XXOPC.move("*ADD");
      if (MICommon.toNumericCompany(inAddROReceipt.getQYCONO())) {
         XXCONO = MICommon.getInt();
      } else {
         MICommon.setError("CONO");
         return;
      }

      LDAZD.CONO = XXCONO;
    //-----------------------------------
      //    UTC mode
      UTCmode = isUTCmode(inAddROReceipt.getQYUTCM());
      if (IN60) {
         MICommon.setError("UTCM", "WUTCM01", inAddROReceipt.getQYUTCM());
         return;
      }
      //-----------------------------------
      // Message type
      XXE065.moveLeftPad(inAddROReceipt.getQYE065());
      RE065();
      if (IN60) {
         return;
      }
      // Check Warehouse
      XXWHLO.moveLeftPad(inAddROReceipt.getQYWHLO());
      RWHL();
      if (IN60) {
         return;
      }
      if (UTCmode) {
         //   Determine time zone for Warehouse
         if (!CRCalendar.retrieveTIZOForWHLO(PXMNGTZN, LDAZD.CONO, XXWHLO)) {
            IN60 = true;
            //   MSGID=S_00148 UTC time conversion failed.
            MICommon.setError("GEDT","S_00148");
            return;
         }
         //   time zone for warehouse
         TIZOforWHLO.move(CRCalendar.getTimeZone());
      }
      //-----------------------------------
      //   Retreive message number if blank
      if (inAddROReceipt.getQYMSGN().isBlank()) {
         RVMSNR();
         inAddROReceipt.setQYMSGN().moveLeft(PXRTVNBR.PXNBNR, 10);
      } else {
         HIHED.setCONO(LDAZD.CONO);
         HIHED.setMSGN().move(inAddROReceipt.getQYMSGN());
         rAPIDS.clear();
         X0OPC.moveLeftPad("*GET");
         rPHEDPIpreCall();
         apCall("MHIHEDPI", rPHEDPI);
         rPHEDPIpostCall();
      }
      //   Default package number if blank
      if (inAddROReceipt.getQYPACN().isBlank()) {
         inAddROReceipt.setQYPACN().moveLeft(inAddROReceipt.getQYMSGN());
      }
      HIPAC.setCONO(LDAZD.CONO);
      HIPAC.setMSGN().move(inAddROReceipt.getQYMSGN());
      HIPAC.setPACN().move(inAddROReceipt.getQYPACN());
      rAPIDS.clear();
      X0OPC.moveLeftPad("*GET");
      rPPACPIpreCall();
      apCall("MHIPACPI", rPPACPI);
      rPPACPIpostCall();
      HILIN.setCONO(LDAZD.CONO);
      HILIN.setMSGN().move(inAddROReceipt.getQYMSGN());
      HILIN.setPACN().move(inAddROReceipt.getQYPACN());
      HILIN.setMSLN(0);
      //   Retreive facility for warehouse
      ITWHL.setCONO(LDAZD.CONO);
      ITWHL.setWHLO().move(inAddROReceipt.getQYWHLO());
      if (!ITWHL.CHAIN("00", ITWHL.getKey("00"))) {
         //   MSGID=WWH0103 Warehouse &1 does not exist
         MICommon.setError( "", "WWH0103", inAddROReceipt.getQYWHLO());
         return;
      } else {
         HIHED.setFACI().move(ITWHL.getFACI());
      }
      //   Format date/time
      if (inAddROReceipt.getQYGEDT().isBlank() ||
            inAddROReceipt.getQYGETM().isBlank()) {
         XXGEDT = movexDate();
         XXGETM = movexTime();
      } else {
         if (MICommon.toNumericDate(inAddROReceipt.getQYGEDT())) {
            XXGEDT = MICommon.getNumericDate();
         } else {
            MICommon.setError("GEDT");
            return;
         }
         if (!MICommon.toNumeric(inAddROReceipt.getQYGETM())) {
            MICommon.setError("GETM");
            return;
         }
         XXGETM = MICommon.getInt();
         // Date generated
         date = XXGEDT; 
         time = XXGETM / 100;
         if (UTCmode && date > 0) {
            if (!convertFromUTC()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("GEDT","S_00148");
               return;
            }
            XXGEDT = UTCdate;
            XXGETM = UTCtime * 100;
         }
      }
      // ----------------------------------------------------------------
      //   Move data from the transaction input structure (MHS850MI1)   >
      //   to the execution data formats (MHIHED/MHIPAC/MHILIN)         >
      // ----------------------------------------------------------------
      HIHED.setCONO(LDAZD.CONO);
      HIPAC.setCONO(LDAZD.CONO);
      HILIN.setCONO(LDAZD.CONO);
      HIHED.setQLFR().move("40  ");
      HIPAC.setQLFR().move("40  ");
      HILIN.setQLFR().move("40  ");
      //---------------------------------------
      //---------------------------------------
      HILIN.setTTYP(40);
      HIHED.setE0IO('I');
      HIHED.setWHLO().move(inAddROReceipt.getQYWHLO());
      HIPAC.setWHLO().move(inAddROReceipt.getQYWHLO());
      HILIN.setWHLO().move(inAddROReceipt.getQYWHLO());
      HIHED.setMSGN().move(inAddROReceipt.getQYMSGN());
      HIPAC.setMSGN().move(inAddROReceipt.getQYMSGN());
      HILIN.setMSGN().move(inAddROReceipt.getQYMSGN());
      HIHED.setPMSN().moveLeft(inAddROReceipt.getQYMSGN());
      HIPAC.setPACN().move(inAddROReceipt.getQYPACN());
      HILIN.setPACN().move(inAddROReceipt.getQYPACN());
      HIHED.setGEDT(XXGEDT);
      HIPAC.setGEDT(XXGEDT);
      HILIN.setGEDT(XXGEDT);
      HIHED.setGETM(XXGETM);
      HIPAC.setGETM(XXGETM);
      HILIN.setGETM(XXGETM);
      HIHED.setE0PA().move(inAddROReceipt.getQYE0PA());
      HIHED.setE0PB().move(inAddROReceipt.getQYE0PA());
      HIHED.setE035(0);
      HIHED.setE065().move(inAddROReceipt.getQYE065());
      HIHED.setADID().move(inAddROReceipt.getQYADID());
      HIPAC.setADID().move(inAddROReceipt.getQYADID());
      HILIN.setADID().move(inAddROReceipt.getQYADID());
      HILIN.setITNO().move(inAddROReceipt.getQYITNO());
      XXITNO.moveLeftPad(HILIN.setITNO());
      RITNO();
      //--------------------------------
      HILIN.setPOPN().move(inAddROReceipt.getQYPOPN());
      HILIN.setALWQ().move(inAddROReceipt.getQYALWQ());
      X0FLDD = 2;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddROReceipt.getQYALWT());
      RNUM();
      HILIN.setALWT((int)this.PXNUM);
      HILIN.setWHSL().move(inAddROReceipt.getQYWHSL());
      HILIN.setBANO().move(inAddROReceipt.getQYBANO());
      HILIN.setCAMU().move(inAddROReceipt.getQYCAMU());
      HILIN.setTOCA().move(inAddROReceipt.getQYCAMU());
      XXQTYN = 0d;
      XXQTYA.clear();
      XXQTYA.moveLeft(inAddROReceipt.getQYQTY());
      this.PXDCCD = X1DCCD;
      RQTY();
      HILIN.setRVQA(this.PXNUM);
      HILIN.setRIDN().move(inAddROReceipt.getQYRIDN());
      X0FLDD = 6;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddROReceipt.getQYRIDO());
      RNUM();
      HILIN.setRIDO((int)this.PXNUM);
      X0FLDD = 6;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddROReceipt.getQYRIDL());
      RNUM();
      HILIN.setRIDL((int)this.PXNUM);
      X0FLDD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddROReceipt.getQYRIDX());
      RNUM();
      HILIN.setRIDX((int)this.PXNUM);
      X0FLDD = 11;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddROReceipt.getQYRIDI());
      RNUM();
      HILIN.setRIDI((long)this.PXNUM);
      HILIN.setUSD1().move(inAddROReceipt.getQYUSD1());
      HILIN.setUSD2().move(inAddROReceipt.getQYUSD2());
      HILIN.setUSD3().move(inAddROReceipt.getQYUSD3());
      HILIN.setUSD4().move(inAddROReceipt.getQYUSD4());
      HILIN.setUSD5().move(inAddROReceipt.getQYUSD5());
      HILIN.setBREF().move(inAddROReceipt.getQYBREF());
      HILIN.setBRE2().move(inAddROReceipt.getQYBRE2());
      XXQTYN = 0d;
      XXQTYA.clear();
      XXQTYA.moveLeft(inAddROReceipt.getQYCAWE());
      this.PXDCCD = X2DCCD;
      RQTY();
      HILIN.setCAWE(this.PXNUM);
      HIHED.setE0IO('I');
      if (!inAddROReceipt.getQYPMSN().isBlank()) {
         HIHED.setPMSN().move(inAddROReceipt.getQYPMSN());
      } else {
         HIHED.setPMSN().moveLeft(inAddROReceipt.getQYMSGN());
      }
      X0FLDD = 1;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddROReceipt.getQYOEND().getChar());
      RNUM();
      HILIN.setOEND((int)this.PXNUM);
      if (!inAddROReceipt.getQYRPDT().isBlank()) {
         if (!MICommon.toNumericDate(inAddROReceipt.getQYRPDT())) {
            MICommon.setError("RPDT");
            return;
         }
         HILIN.setRPDT(MICommon.getNumericDate());
      }
      if (!inAddROReceipt.getQYRPTM().isBlank()) {
         X0FLDD = 6;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inAddROReceipt.getQYRPTM());
         RNUM();
         HILIN.setRPTM((int)this.PXNUM);
      }
      if (!UTCmode && 
         !inAddROReceipt.getQYRPDT().isBlank() &&
         inAddROReceipt.getQYRPTM().isBlank()) {
         HILIN.setRPTM(movexTime());
      }
      if (UTCmode) {
         // Reporting date
         if (!inAddROReceipt.getQYRPDT().isBlank() &&
             !inAddROReceipt.getQYRPTM().isBlank()) {
            date = HILIN.getRPDT(); 
            time = HILIN.getRPTM() / 100;           
            if (!convertFromUTC()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("RPDT","S_00148");
               return;
            }
            HILIN.setRPDT(UTCdate);
            HILIN.setRPTM(UTCtime * 100);
         } else {
            if (inAddROReceipt.getQYRPDT().isBlank() ||
               inAddROReceipt.getQYRPTM().isBlank()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("RPDT","S_00148");
               return;
            }
         }
      }
      //   Validate/create transaction header data
      //---------------------------------------------------
      if (IN75) {
         IN60 = true;
         return;
      }
      //---------------------------------------------------
      RHEAD();
      if (IN60 && IN76) {
         return;
      }
      //   Validate/create transaction package data
      RPACK();
      if (IN60 && IN76) {
         return;
      }
      //   Validate/create transaction line data
      RLINE();
      if (IN60) {
         return;
      }

      outAddROReceipt.setYTCONO().move(XXCONO);
      outAddROReceipt.setYTMSGN().move(HIHED.getMSGN());
      MICommon.setData( outAddROReceipt.get());
   }

   public void RCOM35() {
      // ----------------------------------------------------------------
      //   Clear transaction records
      // ----------------------------------------------------------------
      sMHS850MIRAddDORecViaPack inAddDORecViaPack = (sMHS850MIRAddDORecViaPack)MICommon.getInDS(sMHS850MIRAddDORecViaPack.class);

      HIHED.clear();
      HIPAC.clear();
      HILIN.clear();
      int numberOfKeys = 3;
      // ----------------------------------------------------------------
      //   Save process option.
      // ----------------------------------------------------------------
      XXPRFL.move(inAddDORecViaPack.getQZPRFL());
      XXOPC.move("*ADD");
      if (MICommon.toNumericCompany(inAddDORecViaPack.getQZCONO())) {
         XXCONO = MICommon.getInt();
      } else {
         MICommon.setError("CONO");
         return;
      }
      LDAZD.CONO = XXCONO;

      //-----------------------------------
      // Message type
      XXE065.moveLeftPad(inAddDORecViaPack.getQZE065());
      RE065();
      if (IN60) {
         return;
      }
      //-----------------------------------
      // Check For Partner
      if (inAddDORecViaPack.getQZE0PA().isBlank()) {
         //   MSGID = WE00A02  Partner does not exist
         MICommon.setError( "", "WE00A02");
         return;
      }
      MIPPT.setE0IO('I');
      MIPPT.setE0PA().moveLeft(inAddDORecViaPack.getQZE0PA());
      if (!MIPPT.CHAIN("10", MIPPT.getKey(LDAZD.CONO, "10", 3))) {
         //   MSGID=WE00A03 Partner &1 does not exist
         MICommon.setError( "", "WE00A03", inAddDORecViaPack.getQZE0PA());
         return;
      }
      if (inAddDORecViaPack.getQZE065().isBlank()) {
         //   MSGID=ED01016   Message Type does not exist for Partner
         MICommon.setError( "", "ED01016");
         return;
      }
      MIPPT.setE065().moveLeft(inAddDORecViaPack.getQZE065());
      if (!MIPPT.CHAIN("10", MIPPT.getKey(LDAZD.CONO, "10", 4))) {
         //   MSGID=MM98705 Message Type &1 is not found
         MICommon.setError( "", "MM98705", inAddDORecViaPack.getQZE065());
         return;
      }
      //-----------------------------------
      //   SSCC/PANR/DLIX Validation
      //-----------------------------------
      inputDLIX.moveLeftPad(inAddDORecViaPack.getQZDLIX());
      inputPANR.moveLeftPad(inAddDORecViaPack.getQZPACN());
      inputSSCC.moveLeftPad(inAddDORecViaPack.getQZSSCC());
      numberPackage = 0;
      if (!validInput()) {
         return;
      }
      //---------------------------------------------------------
      //  Get SSCC number
      if (!inAddDORecViaPack.getQZPACN().isBlank() && !inAddDORecViaPack.getQZDLIX().isBlank() && inAddDORecViaPack.getQZSSCC().isBlank()) {
         //Get warehouse
         HDISH.setCONO(LDAZD.CONO);
         HDISH.setINOU(2);
         this.PXDCCD = 0;
         this.PXFLDD = 11;
         this.PXEDTC = 'P';
         this.PXDCFM = '.';
         this.PXNUM = 0d;
         this.PXALPH.clear();
         this.PXALPH.moveLeft(inAddDORecViaPack.getQZDLIX());
         SRCOMNUM.COMNUM();
         HDISH.setDLIX((long)this.PXNUM);
         if (HDISH.CHAIN("00", HDISH.getKey("00"))) {
            PTRNS.setWHLO().moveLeft(HDISH.getWHLO());
         }
         PTRNS.setCONO(LDAZD.CONO);
         PTRNS.setINOU(2);
         PTRNS.setDIPA(0);
         PTRNS.setDLIX((long)this.PXNUM);
         PTRNS.setPANR().moveLeftPad(inAddDORecViaPack.getQZPACN());
         if (PTRNS.CHAIN("00", PTRNS.getKey("00"))) {
            inAddDORecViaPack.setQZSSCC().moveLeft(PTRNS.getSSCC());
         }
      }
      if (!inAddDORecViaPack.getQZPACN().isBlank() && inAddDORecViaPack.getQZDLIX().isBlank()) {
         PTRNS.setCONO(LDAZD.CONO);
         PTRNS.setDIPA(0);
         PTRNS.setINOU(2);
         PTRNS.setPANR().moveLeftPad(inAddDORecViaPack.getQZPACN());
         PTRNS.SETLL("85", PTRNS.getKey("85", 4));
         if (PTRNS.READE("85", PTRNS.getKey("85",4))) {
            if (PTRNS.READE("85", PTRNS.getKey("85",4))) {
               //   MSGID-WDL0202 Delivery number must be entered
               MICommon.setError( "", "WDL0202");
               return;
            }
            inAddDORecViaPack.setQZSSCC().moveLeft(PTRNS.getSSCC());
         }
      }
      //---------------------------------------------------------
      if (inAddDORecViaPack.getQZSSCC().isBlank()) {
         //   MSGID=MM98711 SSCC Number must be entered
         MICommon.setError( "", "MM98711");
         return;
      }
      // check for MHIHEDPI
      HIHED.setCONO(LDAZD.CONO);
      RVMSNR();
      inAddDORecViaPack.setQZMSGN().moveLeft(PXRTVNBR.PXNBNR, 10);
      HIHED.setMSGN().move(inAddDORecViaPack.getQZMSGN());
      rAPIDS.clear();
      X0OPC.moveLeftPad("*GET");
      rPHEDPIpreCall();
      apCall("MHIHEDPI", rPHEDPI);
      rPHEDPIpostCall();
      //Get warehouse
      if (!inAddDORecViaPack.getQZDLIX().isBlank()) {
         HDISH.setCONO(LDAZD.CONO);
         HDISH.setINOU(2);
         this.PXDCCD = 0;
         this.PXFLDD = 11;
         this.PXEDTC = 'P';
         this.PXDCFM = '.';
         this.PXNUM = 0d;
         this.PXALPH.clear();
         this.PXALPH.moveLeft(inAddDORecViaPack.getQZDLIX());
         SRCOMNUM.COMNUM();
         HDISH.setDLIX((long)this.PXNUM);
         if (HDISH.CHAIN("00", HDISH.getKey("00"))) {
            PTRNS.setWHLO().move(HDISH.getWHLO());
            PTRNS.setDLIX(HDISH.getDLIX());
         }
      }  
      PTRNS.setCONO(LDAZD.CONO);
      PTRNS.setINOU(2);
      PTRNS.setSSCC().move(inAddDORecViaPack.getQZSSCC());
      if (PTRNS.count("70", PTRNS.getKey("70", numberOfKeys)) > 1) {
         // The SSCC exist on more than one record. DLIX must be provided to find the correct record
         numberOfKeys = 6;
      }
      if (inAddDORecViaPack.getQZDLIX().isBlank() && numberOfKeys == 6) {
         //   MSGID=WDL0203 Delivery number must be entered         
         MICommon.setError( "", "WDL0202", inAddDORecViaPack.getQZDLIX());
         return;
      }
      IN91 = !PTRNS.CHAIN("70", PTRNS.getKey("70", numberOfKeys));
      if (inAddDORecViaPack.getQZPACN().isBlank()) {
         if (!IN91) {
            inAddDORecViaPack.setQZPACN().moveLeft(PTRNS.getPANR());
            HIPAC.setPACN().moveLeft(inAddDORecViaPack.getQZPACN());
            HILIN.setPACN().moveLeft(inAddDORecViaPack.getQZPACN());
         } else {
            MICommon.setError( "", "WPA5103", inAddDORecViaPack.getQZPACN());
            return;
         }
      } else {
         PAC.clear();
         PAC.moveLeft(PTRNS.getPANR());
         TRIMPAC();
         if (PCC.EQ(inAddDORecViaPack.getQZPACN())) {
            HIPAC.setPACN().moveLeft(inAddDORecViaPack.getQZPACN());
            HILIN.setPACN().moveLeft(inAddDORecViaPack.getQZPACN());
         } else {
            MICommon.setError( "", "WPA5103", inAddDORecViaPack.getQZPACN());
            return;
         }
      }
      HIPAC.setCONO(LDAZD.CONO);
      HIPAC.setMSGN().move(inAddDORecViaPack.getQZMSGN());
      rAPIDS.clear();
      X0OPC.moveLeftPad("*GET");
      rPPACPIpreCall();
      apCall("MHIPACPI", rPPACPI);
      rPPACPIpostCall();
      // ----------------------------------------------------------------
      //   Move data from the transaction input structure (MHS850MI1)   >
      //   to the execution data formats (MHIHED/MHIPAC/MHILIN)         >
      // ----------------------------------------------------------------
      // Set Company number
      HIHED.setCONO(LDAZD.CONO);
      HIPAC.setCONO(LDAZD.CONO);
      HILIN.setCONO(LDAZD.CONO);
      // Set Message number
      HIHED.setMSGN().move(inAddDORecViaPack.getQZMSGN());
      HIPAC.setMSGN().move(inAddDORecViaPack.getQZMSGN());
      HILIN.setMSGN().move(inAddDORecViaPack.getQZMSGN());
      HIHED.setPMSN().moveLeft(inAddDORecViaPack.getQZMSGN());
      // Set Partner AND MESSAGE TYPE
      HIHED.setE0PA().move(inAddDORecViaPack.getQZE0PA());
      HIHED.setE0PB().move(inAddDORecViaPack.getQZE0PA());
      HIHED.setE065().move(inAddDORecViaPack.getQZE065());
      HIHED.setQLFR().move("50P2");
      HIPAC.setQLFR().move("50P2");
      HILIN.setQLFR().move("50P2");
      // Set Transaction type
      HILIN.setTTYP(50);
      HIHED.setE0IO('I');
      // Set SSCC
      HIPAC.setSSCC().move(inAddDORecViaPack.getQZSSCC());
      HILIN.setRORC(5);
      HIHED.setE035(0);
      // Set Delivery and Warehouse
      PTRNS.setCONO(LDAZD.CONO);
      PTRNS.setINOU(2);
      PTRNS.setSSCC().move(inAddDORecViaPack.getQZSSCC());
      PTRNS.setWHLO().move(HDISH.getWHLO());
      PTRNS.setDLIX(HDISH.getDLIX());
      IN91 = !PTRNS.CHAIN("70", PTRNS.getKey("70", numberOfKeys));
      if (inAddDORecViaPack.getQZDLIX().isBlank()) {
         if (!IN91) {
            HILIN.setDLIX(PTRNS.getDLIX());
         }
      } else {
         this.PXDCCD = 0;
         this.PXFLDD = 11;
         this.PXEDTC = 'P';
         this.PXDCFM = '.';
         this.PXNUM = 0d;
         this.PXALPH.clear();
         this.PXALPH.moveLeft(inAddDORecViaPack.getQZDLIX());
         SRCOMNUM.COMNUM();
         HILIN.setDLIX((long)this.PXNUM);
      }
      if (!IN91) {
         HIHED.setWHLO().moveLeft(PTRNS.getWHLO());
         HIPAC.setWHLO().move(PTRNS.getWHLO());
         HILIN.setWHLO().moveLeft(PTRNS.getWHLO());
      }
      //Set for Supplier no if Order Catagory is 5
      HDISH.setCONO(LDAZD.CONO);
      HDISH.setINOU(2);
      if (inAddDORecViaPack.getQZDLIX().isBlank()) {
         HDISH.setDLIX(PTRNS.getDLIX());
      } else {
         HDISH.setDLIX((long)this.PXNUM);
      }
      if (HDISH.CHAIN("00", HDISH.getKey("00"))) {
         HIHED.setSUNO().moveLeft(HDISH.getCONB());
      }
      // Set Facility
      ITWHL.setCONO(LDAZD.CONO);
      ITWHL.setWHLO().moveLeft(HDISH.getWHLO());
      if (ITWHL.CHAIN("00", ITWHL.getKey("00"))) {
         HIHED.setFACI().move(ITWHL.getFACI());
         HILIN.setFACI().move(ITWHL.getFACI());
      }
      // Set Order number , Order Line
      FTRNS.setCONO(LDAZD.CONO);
      FTRNS.setWHLO().moveLeft(HDISH.getWHLO());
      if (inAddDORecViaPack.getQZDLIX().isBlank()) {
         FTRNS.setDLIX(PTRNS.getDLIX());
      } else {
         FTRNS.setDLIX((long)this.PXNUM);
      }
      IN91 = !FTRNS.CHAIN("50", FTRNS.getKey("50", 3));
      if (inAddDORecViaPack.getQZRIDN().isBlank()) {
         if (!IN91) {
            HIHED.setRIDN().move(FTRNS.getRIDN());
         }//. end if
      } else if (inAddDORecViaPack.setQZRIDN().NE(FTRNS.getRIDN())) {
         //   MSGID=MO29003 Order number not in Transanction file
         MICommon.setError( "", "MO29003");
         return;
      } else {
         HIHED.setRIDN().move(inAddDORecViaPack.getQZRIDN());
      }

      inAddDORecViaPack.setQZITNO().move(FTRNS.getITNO());
      if (!inAddDORecViaPack.getQZRIDL().isBlank()) {
         this.PXDCCD = 0;
         this.PXFLDD = 6;
         this.PXEDTC = 'P';
         this.PXDCFM = '.';
         this.PXNUM = 0d;
         this.PXALPH.clear();
         this.PXALPH.moveLeft(inAddDORecViaPack.getQZRIDL());
         SRCOMNUM.COMNUM();
         int ridl = (int)this.PXNUM;
         this.PXDCCD = 0;
         this.PXFLDD = 3;
         this.PXEDTC = 'P';
         this.PXDCFM = '.';
         this.PXNUM = 0d;
         this.PXALPH.clear();
         this.PXALPH.moveLeft(inAddDORecViaPack.getQZRIDX());
         SRCOMNUM.COMNUM();
         int ridx = (int)this.PXNUM;
         if (FTRNS.getRIDL() != ridl || FTRNS.getRIDX() != ridx) {
            //   MSGID=XIM0039 Order line does not exist
            MICommon.setError( "", "XIM0039");
            return;
         }
      }
      //   Validate/create transaction header data
      RHEAD();
      if (IN60 && IN76) {
         return;
      }
      //   Validate/create transaction package data
      RPACK();
      if (IN60 && IN76) {
         return;
      }
      //   Validate/create transaction line data
      RLINE();
      if (IN60) {
         return;
      }

   }





   /**
   *    RCOM36 - Execute command - SndPOReceipt
   */
   public void RCOM36() {
      sMHS850MIRSndPOReceipt inSndPOReceipt = (sMHS850MIRSndPOReceipt)MICommon.getInDS(sMHS850MIRSndPOReceipt.class);

      HIHED.clear();
      HIPAC.clear();
      HILIN.clear();
      // -------------------------------------------------------------
      //   Save process option.
      // -------------------------------------------------------------
      XXPRFL.move(inSndPOReceipt.getQHPRFL());
      XXOPC.move("*ADD");
      if (MICommon.toNumericCompany(inSndPOReceipt.getQHCONO())) {
         XXCONO = MICommon.getInt();
      } else {
         MICommon.setError("CONO");
         return;
      }

      LDAZD.CONO = XXCONO;
    //-----------------------------------
      //    UTC mode
      UTCmode = isUTCmode(inSndPOReceipt.getQHUTCM());
      if (IN60) {
         MICommon.setError("UTCM", "WUTCM01", inSndPOReceipt.getQHUTCM());
         return;
      }
      //-----------------------------------
      // Message type
      XXE065.moveLeftPad(inSndPOReceipt.getQHE065());
      RE065();
      if (IN60) {
         return;
      }
      // Check Warehouse
      XXWHLO.moveLeftPad(inSndPOReceipt.getQHWHLO());
      RWHL();
      if (IN60) {
         return;
      }
      if (UTCmode) {
         //   Determine time zone for Warehouse
         if (!CRCalendar.retrieveTIZOForWHLO(PXMNGTZN, LDAZD.CONO, XXWHLO)) {
            IN60 = true;
            //   MSGID=S_00148 UTC time conversion failed.
            MICommon.setError("GEDT","S_00148");
            return;
         }
         //   time zone for warehouse
         TIZOforWHLO.move(CRCalendar.getTimeZone());
      }
      //-----------------------------------
      //   Retreive message number if blank
      if (inSndPOReceipt.getQHMSGN().isBlank()) {
         RVMSNR();
         inSndPOReceipt.setQHMSGN().moveLeft(PXRTVNBR.PXNBNR, 10);
      } else {
         HIHED.setCONO(LDAZD.CONO);
         HIHED.setMSGN().move(inSndPOReceipt.getQHMSGN());
         rAPIDS.clear();
         X0OPC.moveLeftPad("*GET");
         rPHEDPIpreCall();
         apCall("MHIHEDPI", rPHEDPI);
         rPHEDPIpostCall();
      }
      //   Default package number if blank
      if (inSndPOReceipt.getQHPACN().isBlank()) {
         inSndPOReceipt.setQHPACN().moveLeft(inSndPOReceipt.getQHMSGN());
      }
      HIPAC.setCONO(LDAZD.CONO);
      HIPAC.setMSGN().move(inSndPOReceipt.getQHMSGN());
      HIPAC.setPACN().move(inSndPOReceipt.getQHPACN());
      rAPIDS.clear();
      X0OPC.moveLeftPad("*GET");
      rPPACPIpreCall();
      apCall("MHIPACPI", rPPACPI);
      rPPACPIpostCall();
      HILIN.setCONO(LDAZD.CONO);
      HILIN.setMSGN().move(inSndPOReceipt.getQHMSGN());
      HILIN.setPACN().move(inSndPOReceipt.getQHPACN());
      HILIN.setMSLN(0);
      //   Retreive facility for warehouse
      ITWHL.setCONO(LDAZD.CONO);
      ITWHL.setWHLO().move(inSndPOReceipt.getQHWHLO());
      if (!ITWHL.CHAIN("00", ITWHL.getKey("00"))) {
         //   MSGID=WWH0103 Warehouse &1 does not exist
         MICommon.setError( "", "WWH0103", inSndPOReceipt.getQHWHLO());
         return;
      } else {
         HIHED.setFACI().move(ITWHL.getFACI());
      }
      //   Format date/time
      if (inSndPOReceipt.getQHGEDT().isBlank() ||
            inSndPOReceipt.getQHGETM().isBlank()) {
         XXGEDT = movexDate();
         XXGETM = movexTime();
      } else {
         if (MICommon.toNumericDate(inSndPOReceipt.getQHGEDT())) {
            XXGEDT = MICommon.getNumericDate();
         } else {
            MICommon.setError("GEDT");
            return;
         }
         if (!MICommon.toNumeric(inSndPOReceipt.getQHGETM())) {
            MICommon.setError("GETM");
            return;
         }
         XXGETM = MICommon.getInt();
         // Date generated
         date = XXGEDT; 
         time = XXGETM / 100;
         if (UTCmode && date > 0) {
            if (!convertFromUTC()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("GEDT","S_00148");
               return;
            }
            XXGEDT = UTCdate;
            XXGETM = UTCtime * 100;
         }
      }
      // ----------------------------------------------------------
      //   Move data from the transaction input structure (MHS850MI1)
      //   to the execution data formats (MHIHED/MHIPAC/MHILIN)
      // ----------------------------------------------------------
      HIHED.setCONO(LDAZD.CONO);
      HIPAC.setCONO(LDAZD.CONO);
      HILIN.setCONO(LDAZD.CONO);
      HIHED.setQLFR().move("20  ");
      HIPAC.setQLFR().move("20  ");
      HILIN.setQLFR().move("20  ");
      //-----------------------------------------------------------
      //-----------------------------------------------------------
      HILIN.setTTYP(25);
      HIHED.setE0IO('I');
      HIHED.setWHLO().move(inSndPOReceipt.getQHWHLO());
      HIPAC.setWHLO().move(inSndPOReceipt.getQHWHLO());
      HILIN.setWHLO().move(inSndPOReceipt.getQHWHLO());
      HIHED.setMSGN().move(inSndPOReceipt.getQHMSGN());
      HIPAC.setMSGN().move(inSndPOReceipt.getQHMSGN());
      HILIN.setMSGN().move(inSndPOReceipt.getQHMSGN());
      HIHED.setPMSN().moveLeft(inSndPOReceipt.getQHMSGN());
      HIPAC.setPACN().move(inSndPOReceipt.getQHPACN());
      HILIN.setPACN().move(inSndPOReceipt.getQHPACN());
      HIHED.setGEDT(XXGEDT);
      HIPAC.setGEDT(XXGEDT);
      HILIN.setGEDT(XXGEDT);
      HIHED.setGETM(XXGETM);
      HIPAC.setGETM(XXGETM);
      HILIN.setGETM(XXGETM);
      HIHED.setE0PA().move(inSndPOReceipt.getQHE0PA());
      HIHED.setE0PB().move(inSndPOReceipt.getQHE0PA());
      HIHED.setE035(0);
      HIHED.setE065().move(inSndPOReceipt.getQHE065());
      HIHED.setSUNO().move(inSndPOReceipt.getQHSUNO());
      X0FLDD = 1;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inSndPOReceipt.getQHSUTY().getChar());
      RNUM();
      HIHED.setSUTY((int)this.PXNUM);
      HIHED.setADID().move(inSndPOReceipt.getQHADID());
      HILIN.setITNO().move(inSndPOReceipt.getQHITNO());
      XXITNO.moveLeftPad(HILIN.setITNO());
      RITNO();
      HILIN.setPOPN().move(inSndPOReceipt.getQHPOPN());
      HILIN.setALWQ().move(inSndPOReceipt.getQHALWQ());
      X0FLDD = 2;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inSndPOReceipt.getQHALWT());
      RNUM();
      HILIN.setALWT((int)this.PXNUM);
      HILIN.setWHSL().move(inSndPOReceipt.getQHWHSL());
      HILIN.setBANO().move(inSndPOReceipt.getQHBANO());
      HILIN.setCAMU().move(inSndPOReceipt.getQHCAMU());
      XXQTYN = 0d;
      XXQTYA.clear();
      XXQTYA.moveLeft(inSndPOReceipt.getQHQTY());
      this.PXDCCD = X1DCCD;
      RQTY();
      HILIN.setRVQA(this.PXNUM);
      HILIN.setRIDN().move(inSndPOReceipt.getQHRIDN());
      X0FLDD = 6;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inSndPOReceipt.getQHRIDL());
      RNUM();
      HILIN.setRIDL((int)this.PXNUM);
      X0FLDD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inSndPOReceipt.getQHRIDX());
      RNUM();
      HILIN.setRIDX((int)this.PXNUM);
      X0FLDD = 11;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inSndPOReceipt.getQHRIDI());
      RNUM();
      HILIN.setRIDI((long)this.PXNUM);
      HILIN.setUSD1().move(inSndPOReceipt.getQHUSD1());
      HILIN.setUSD2().move(inSndPOReceipt.getQHUSD2());
      HILIN.setUSD3().move(inSndPOReceipt.getQHUSD3());
      HILIN.setUSD4().move(inSndPOReceipt.getQHUSD4());
      HILIN.setUSD5().move(inSndPOReceipt.getQHUSD5());
      HILIN.setBREF().move(inSndPOReceipt.getQHBREF());
      HILIN.setBRE2().move(inSndPOReceipt.getQHBRE2());
      XXQTYN = 0d;
      XXQTYA.clear();
      XXQTYA.moveLeft(inSndPOReceipt.getQHCAWE());
      this.PXDCCD = X2DCCD;
      RQTY();
      HILIN.setCAWE(this.PXNUM);
      X0FLDD = 1;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inSndPOReceipt.getQHOEND().getChar());
      RNUM();
      HILIN.setOEND((int)this.PXNUM);
      HIHED.setE0IO('I');
      if (!inSndPOReceipt.getQHPMSN().isBlank()) {
         HIHED.setPMSN().move(inSndPOReceipt.getQHPMSN());
      } else {
         HIHED.setPMSN().moveLeft(inSndPOReceipt.getQHMSGN());
      }

      if (!MICommon.toNumericDate(inSndPOReceipt.getQHEXPI())) {
         MICommon.setError("EXPI");
         return;
      }
      HILIN.setEXPI(MICommon.getNumericDate());

      if (!inSndPOReceipt.getQHRPDT().isBlank()) {
         if (!MICommon.toNumericDate(inSndPOReceipt.getQHRPDT())) {
            MICommon.setError("RPDT");
            return;
         }
         HILIN.setRPDT(MICommon.getNumericDate());
      }
      if (!inSndPOReceipt.getQHRPTM().isBlank()) {
         X0FLDD = 6;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inSndPOReceipt.getQHRPTM());
         RNUM();
         HILIN.setRPTM((int)this.PXNUM);
      }
      if (!UTCmode && 
         !inSndPOReceipt.getQHRPDT().isBlank() &&
            inSndPOReceipt.getQHRPTM().isBlank()) {
         HILIN.setRPTM(movexTime());
      }
      if (UTCmode) {
         // Reporting date
         if (!inSndPOReceipt.getQHRPDT().isBlank() &&
             !inSndPOReceipt.getQHRPTM().isBlank()) {
            date = HILIN.getRPDT(); 
            time = HILIN.getRPTM() / 100;           
            if (!convertFromUTC()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("RPDT","S_00148");
               return;
            }
            HILIN.setRPDT(UTCdate);
            HILIN.setRPTM(UTCtime * 100);
         } else {
            if (inSndPOReceipt.getQHRPDT().isBlank() ||
               inSndPOReceipt.getQHRPTM().isBlank()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("RPDT","S_00148");
               return;
            }
         }
      }
      //   Delivery note number and date
      HIHED.setSUDO().move(inSndPOReceipt.getQHSUDO());
      HIPAC.setSUDO().move(inSndPOReceipt.getQHSUDO());
      HILIN.setSUDO().move(inSndPOReceipt.getQHSUDO());
      if (!inSndPOReceipt.getQHDNDT().isBlank()) {
         if (!MICommon.toNumericDate(inSndPOReceipt.getQHDNDT())) {
            MICommon.setError("DNDT");
            return;
         }
         HIHED.setDNDT(MICommon.getNumericDate());
         HIPAC.setDNDT(MICommon.getNumericDate());
         HILIN.setDNDT(MICommon.getNumericDate());
      }
      //----------------------------------------------------
      //   Validate/create transaction header data
      //----------------------------------------------------
      if (IN75) {
         IN60 = true;
         return;
      }
      //----------------------------------------------------
      RHEAD();
      if (IN60 && IN76) {
         return;
      }
      //   Validate/create transaction package data
      RPACK();
      if (IN60 && IN76) {
         return;
      }
      //   Validate/create transaction line data
      RLINE();
      if (IN60) {
         return;
      }
   }


   //-----------------------------------------------------------------
   public void RCOM46() {
      // ----------------------------------------------------------------
      //   Add Pick By Cubed Allocation
      // ----------------------------------------------------------------
      sMHS850MIRAddPickSftPacLn inAddPickSftPacLn = (sMHS850MIRAddPickSftPacLn)MICommon.getInDS(sMHS850MIRAddPickSftPacLn.class);
      sMHS850MISAddPickSftPacLn outAddPickSftPacLn = (sMHS850MISAddPickSftPacLn)MICommon.getOutDS(sMHS850MISAddPickSftPacLn.class);

      HIHED.clear();
      HIPAC.clear();
      HILIN.clear();
      // ----------------------------------------------------------------
      //   Save process option.
      // ----------------------------------------------------------------
      XXPRFL.move(inAddPickSftPacLn.getQYPRMD());
      XXOPC.move("*ADD");
      //-----------------------------------
      //    UTC mode
      UTCmode = isUTCmode(inAddPickSftPacLn.getQYUTCM());
      if (IN60) {
         MICommon.setError("UTCM", "WUTCM01", inAddPickSftPacLn.getQYUTCM());
         return;
      }
      //-----------------------------------
      // Message type
      XXE065.moveLeftPad(inAddPickSftPacLn.getQYE065());
      RE065();
      if (IN60) {
         return;
      }
      //-----------------------------------
      // Check For Partner
      if (inAddPickSftPacLn.getQYE0PA().isBlank()) {
         //   MSGID = WE00A02  Partner does not exist
         MICommon.setError( "", "WE00A02");
         return;
      }
      MIPPT.setE0IO('I');
      MIPPT.setE0PA().moveLeft(inAddPickSftPacLn.getQYE0PA());
      if (!MIPPT.CHAIN("10", MIPPT.getKey(LDAZD.CONO, "10", 3))) {
         //   MSGID=WE00A03 Partner &1 does not exist
         MICommon.setError( "", "WE00A03", inAddPickSftPacLn.getQYE0PA());
         return;
      }
      if (inAddPickSftPacLn.getQYE065().isBlank()) {
         //   MSGID=ED01016   Message Type does not exist for Partner
         MICommon.setError( "", "ED01016");
         return;
      }
      MIPPT.setE065().moveLeft(inAddPickSftPacLn.getQYE065());
      if (!MIPPT.CHAIN("10", MIPPT.getKey(LDAZD.CONO, "10", 4))) {
         //   MSGID=MM98705 Message Type &1 is not found
         MICommon.setError( "", "MM98705", inAddPickSftPacLn.getQYE065());
         return;
      }
      // Check Warehouse
      XXWHLO.moveLeftPad(inAddPickSftPacLn.getQYWHLO());
      RWHL();
      if (IN60) {
         return;
      }
      if (UTCmode) {
         //   Determine time zone for Warehouse
         if (!CRCalendar.retrieveTIZOForWHLO(PXMNGTZN, LDAZD.CONO, XXWHLO)) {
            IN60 = true;
            //   MSGID=S_00148 UTC time conversion failed.
            MICommon.setError("GEDT","S_00148");
            return;
         }
         //   time zone for warehouse
         TIZOforWHLO.move(CRCalendar.getTimeZone());
      }
      //-----------------------------------
      //   Retrieve facility for warehouse
      ITWHL.setCONO(LDAZD.CONO);
      ITWHL.setWHLO().move(inAddPickSftPacLn.getQYWHLO());
      if (!ITWHL.CHAIN("00", ITWHL.getKey("00"))) {
         IN60 = true;
         //   MSGID=WWH0103 Warehouse &1 does not exist
         MICommon.setError( "", "WWH0103", inAddPickSftPacLn.getQYWHLO());
         return;
      } else {
         HIHED.setFACI().move(ITWHL.getFACI());
      }

      //   Delivery number
      HDISH.setCONO(LDAZD.CONO);
      HDISH.setINOU(1);
      X0FLDD = 11;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddPickSftPacLn.getQYDLIX());
      RNUM();
      HILIN.setDLIX((long)this.PXNUM);
      HDISH.setDLIX((long)this.PXNUM);
      if (!HDISH.CHAIN("00", HDISH.getKey("00"))) {
         MICommon.setError( "", "WDL0203", MICommon.toAlpha(HDISH.getDLIX()));
         return;
      }
      //   Package in stock
      if (inAddPickSftPacLn.getQYPACN().isBlank() &&
            inAddPickSftPacLn.getQYSSCC().isBlank()) {
         MICommon.setError( "", "WPA5103", inAddPickSftPacLn.getQYPACN());
         return;
      }
      PTRNS.setCONO(LDAZD.CONO);
      PTRNS.setDIPA(0);
      PTRNS.setDLIX(HDISH.getDLIX());
      PTRNS.setWHLO().move(ITWHL.getWHLO());
      if (!inAddPickSftPacLn.getQYPACN().isBlank()) {
         PTRNS.setPANR().move(inAddPickSftPacLn.getQYPACN());
         if (!PTRNS.CHAIN("00", PTRNS.getKey("00"))) {
            MICommon.setError( "", "WPA5103", inAddPickSftPacLn.getQYPACN());
            return;
         }
      } else {
         PTRNS.setINOU(0);
         PTRNS.setSSCC().move(inAddPickSftPacLn.getQYSSCC());
         IN91 = !PTRNS.CHAIN("70", PTRNS.getKey("70", 5));
         if (inAddPickSftPacLn.getQYPACN().isBlank()) {
            if (!IN91) {
               HIPAC.setPACN().moveLeftPad(PTRNS.getPANR());
               HILIN.setPACN().moveLeftPad(PTRNS.getPANR());
            } else {
               MICommon.setError( "", "WPA5103", inAddPickSftPacLn.getQYPACN());
               return;
            }
         } else {
            PAC.clear();
            PAC.moveLeft(PTRNS.getPANR());
            TRIMPAC();
            if (PCC.EQ(inAddPickSftPacLn.getQYPACN()) &&
                  !IN91) {
               HIPAC.setPACN().moveLeftPad(PTRNS.getPANR());
               HILIN.setPACN().moveLeftPad(PTRNS.getPANR());
            } else {
               MICommon.setError( "", "WPA5103", inAddPickSftPacLn.getQYPACN());
               return;
            }
         }
      }
      //   Get product data
      if (!inAddPickSftPacLn.getQYPOPN().isBlank()) {
         HILIN.setPOPN().move(inAddPickSftPacLn.getQYPOPN());
         if (!inAddPickSftPacLn.getQYALWT().isBlank()) {
            X0FLDD = 2;
            XXNUMN = 0d;
            XXNUMA.clear();
            XXNUMA.moveLeft(inAddPickSftPacLn.getQYALWT());
            RNUM();
            HILIN.setALWT((int)this.PXNUM);
         }
         ITPOP.setPOPN().move(HILIN.getPOPN());
         ITPOP.setALWT(HILIN.getALWT());
         if (!ITPOP.CHAIN("50", ITPOP.getKey(LDAZD.CONO, "50", 3))) {
            ITPOP.clearNOKEY("50");
         } else {
            HILIN.setALWQ().move(ITPOP.getALWQ());
         }
      }
      if (inAddPickSftPacLn.getQYITNO().isBlank()) {
         inAddPickSftPacLn.setQYITNO().move(ITPOP.getITNO());
      }
      HILIN.setITNO().move(inAddPickSftPacLn.getQYITNO());
      XXITNO.moveLeftPad(HILIN.setITNO());
      RITNO();
      //   Issue method
      X0FLDD = 1;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddPickSftPacLn.getQYISMD());
      RNUM();
      HILIN.setISMD((int)this.PXNUM);
      HILIN.setLODO().moveLeftPad(inAddPickSftPacLn.getQYLODO());
      //   Check if item in the package
      FTRNS.setCONO(LDAZD.CONO);
      FTRNS.setWHLO().move(ITWHL.getWHLO());
      FTRNS.setDLIX(HDISH.getDLIX());
      FTRNS.setITNO().move(HILIN.getITNO());
      FTRNS.setPANR().move(PTRNS.getPANR());
      FTRNS.SETLL("30", FTRNS.getKey("30", 5));
      IN93 = !FTRNS.READE("30", FTRNS.getKey("30", 5));
      while (!IN93) {
         FTRND.setCONO(FTRNS.getCONO());
         FTRND.setWHLO().move(FTRNS.getWHLO());
         FTRND.setDLIX(FTRNS.getDLIX());
         FTRND.setPANR().moveLeft(FTRNS.getPANR());
         FTRND.setRORC(FTRNS.getRORC());
         FTRND.setRIDN().move(FTRNS.getRIDN());
         FTRND.setRIDL(FTRNS.getRIDL());
         FTRND.setRIDX(FTRNS.getRIDX());
         FTRND.setBANO().move(FTRNS.getBANO());
         FTRND.setCAMU().move(FTRNS.getCAMU());
         // Record to report found - continue
         if (FTRND.CHAIN("00", FTRND.getKey("00", 10))) {
            if (FTRND.getPISS().LT("70")) {
               if (HILIN.getISMD() == 0 ||
                     FTRND.getPISS().NE("50") &&
                     HILIN.getISMD() == 1 ||
                     FTRND.getPISS().NE("60") &&
                     HILIN.getISMD() == 2) {
                  break;
               }
            }
         }
         IN93 = !FTRNS.READE("30", FTRNS.getKey("30", 5));
      }
      //   Nothing to report
      if (IN93) {
         MICommon.setError( "", "MW42072");
         this.MSGDTA.moveLeft(inAddPickSftPacLn.getQYITNO());
         IN60 = true;
         return;
      }
      //   Transaction quantity bU/M
      if (!inAddPickSftPacLn.getQYTRQT().isBlank()) {
         XXQTYN = 0d;
         XXQTYA.clear();
         XXQTYA.moveLeft(inAddPickSftPacLn.getQYTRQT());
         this.PXDCCD = X1DCCD;
         RQTY();
         HILIN.setDLQT(this.PXNUM);
      }
      //   Message number
      if (inAddPickSftPacLn.getQYMSGN().isBlank()) {
         RVMSNR();
         XXMSGN.moveLeftPad(PXRTVNBR.PXNBNR, 10);
      } else {
         HIHED.setCONO(LDAZD.CONO);
         HIHED.setMSGN().move(inAddPickSftPacLn.getQYMSGN());
         rAPIDS.clear();
         X0OPC.moveLeftPad("*GET");
         rPHEDPIpreCall();
         apCall("MHIHEDPI", rPHEDPI);
         rPHEDPIpostCall();
      }
      if (inAddPickSftPacLn.getQYMSGN().isBlank()) {
         inAddPickSftPacLn.setQYMSGN().moveLeft(XXMSGN);
      }
      HIPAC.setCONO(LDAZD.CONO);
      HIPAC.setMSGN().move(inAddPickSftPacLn.getQYMSGN());
      HIPAC.setPACN().moveLeftPad(PTRNS.getPANR());
      HILIN.setPACN().moveLeft(HIPAC.getPACN());
      rAPIDS.clear();
      X0OPC.moveLeftPad("*GET");
      rPPACPIpreCall();
      apCall("MHIPACPI", rPPACPI);
      rPPACPIpostCall();
      //   Format date/time
      XXGEDT = movexDate();
      XXGETM = movexTime();
      // ----------------------------------------------------------------
      //   Move data from the transaction input structure (MHS850MI1)   >
      //   to the execution data formats (MHIHED/MHIPAC/MHILIN)         >
      // ----------------------------------------------------------------
      HIHED.setCONO(LDAZD.CONO);
      HIPAC.setCONO(LDAZD.CONO);
      HILIN.setCONO(LDAZD.CONO);
      HIHED.setQLFR().moveLeft("CFSL");
      HIPAC.setQLFR().move(HIHED.getQLFR());
      HILIN.setQLFR().move(HIPAC.getQLFR());
      HIHED.setWHLO().move(inAddPickSftPacLn.getQYWHLO());
      HIPAC.setWHLO().move(inAddPickSftPacLn.getQYWHLO());
      HILIN.setWHLO().move(inAddPickSftPacLn.getQYWHLO());
      HILIN.setFACI().move(HIHED.getFACI());
      HIPAC.setPACT().move(PTRNS.getPACT());
      HIHED.setE0PA().move(inAddPickSftPacLn.getQYE0PA());
      HIHED.setE0PB().move(inAddPickSftPacLn.getQYE0PA());
      HIHED.setE065().move(inAddPickSftPacLn.getQYE065());
      HIHED.setMSGN().move(inAddPickSftPacLn.getQYMSGN());
      HIHED.setCUNO().move(HDISH.getCONA());
      HIHED.setE0IO('I');
      HIPAC.setMSGN().move(inAddPickSftPacLn.getQYMSGN());
      HIHED.setGEDT(XXGEDT);
      HIPAC.setGEDT(XXGEDT);
      HIHED.setGETM(XXGETM);
      HIPAC.setGETM(XXGETM);
      HIHED.setPMSN().moveLeft(inAddPickSftPacLn.getQYMSGN());
      HILIN.setMSGN().move(inAddPickSftPacLn.getQYMSGN());
      HIPAC.setPACN().moveLeftPad(PTRNS.getPANR());
      HILIN.setPACN().moveLeftPad(PTRNS.getPANR());
      HILIN.setMSLN(0);
      HILIN.setTWSL().move(inAddPickSftPacLn.getQYTWSL());
      HILIN.setWHSL().move(inAddPickSftPacLn.getQYWHSL());
      HILIN.setBANO().move(inAddPickSftPacLn.getQYBANO());
      HILIN.setCAMU().move(inAddPickSftPacLn.getQYCAMU());
      HILIN.setGEDT(XXGEDT);
      HILIN.setGETM(XXGETM);
      if (!inAddPickSftPacLn.getQYUSD1().isBlank()) {
         HILIN.setUSD1().move(inAddPickSftPacLn.getQYUSD1());
      }
      if (!inAddPickSftPacLn.getQYUSD2().isBlank()) {
         HILIN.setUSD2().move(inAddPickSftPacLn.getQYUSD2());
      }
      if (!inAddPickSftPacLn.getQYUSD3().isBlank()) {
         HILIN.setUSD3().move(inAddPickSftPacLn.getQYUSD3());
      }
      if (!inAddPickSftPacLn.getQYUSD4().isBlank()) {
         HILIN.setUSD4().move(inAddPickSftPacLn.getQYUSD4());
      }
      if (!inAddPickSftPacLn.getQYUSD5().isBlank()) {
         HILIN.setUSD5().move(inAddPickSftPacLn.getQYUSD5());
      }

      if (!MICommon.toNumericDate(inAddPickSftPacLn.getQYRPDT())) {
         MICommon.setError("RPDT");
         return;
      }
      HILIN.setRPDT(MICommon.getNumericDate());

      if (!inAddPickSftPacLn.getQYRPTM().isBlank()) {
         X0FLDD = 6;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inAddPickSftPacLn.getQYRPTM());
         RNUM();
         HILIN.setRPTM((int)this.PXNUM);
      }
      if (!UTCmode && 
         !inAddPickSftPacLn.getQYRPDT().isBlank() &&
            inAddPickSftPacLn.getQYRPTM().isBlank()) {
         HILIN.setRPTM(movexTime());
      }
      if (UTCmode) {
         // Reporting date
         if (!inAddPickSftPacLn.getQYRPDT().isBlank() &&
             !inAddPickSftPacLn.getQYRPTM().isBlank()) {
            date = HILIN.getRPDT(); 
            time = HILIN.getRPTM() / 100;           
            if (!convertFromUTC()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("RPDT","S_00148");
               return;
            }
            HILIN.setRPDT(UTCdate);
            HILIN.setRPTM(UTCtime * 100);
         } else {
            IN60 = true;
            //   MSGID=S_00148 UTC time conversion failed.
            MICommon.setError("RPDT","S_00148");
            return;
         }
      }
      if (!inAddPickSftPacLn.getQYTWSL().isBlank()) {
         HILIN.setTWSL().move(inAddPickSftPacLn.getQYTWSL());
      }
      if (IN75) {
         IN60 = true;
         return;
      }
      RHEAD();
      if (IN60 && IN76) {
         return;
      }
      RPACK();
      if (IN60 && IN76) {
         return;
      }
      XXTQTY = HILIN.getDLQT();
      PTRNS.setCONO(HILIN.getCONO());
      PTRNS.setDLIX(HILIN.getDLIX());
      PTRNS.setPAII().moveLeft(HILIN.getPACN());
      PTRNS.SETLL("05", PTRNS.getKey("05", 3));
      FTRND.setPANR().moveLeft(HILIN.getPACN());
      ITALO.setRIDI(HILIN.getDLIX());
      ITALO.SETLL("30", ITALO.getKey(LDAZD.CONO, "30", 2));
      IN93 = !ITALO.READE("30", ITALO.getKey(LDAZD.CONO, "30", 2));
      while (!IN93) {
         if (HILIN.getITNO().EQ(ITALO.getITNO())) {
            FTRND.setCONO(ITALO.getCONO());
            FTRND.setWHLO().move(ITALO.getWHLO());
            FTRND.setDLIX(ITALO.getRIDI());
            FTRND.setRORC(ITALO.getTTYP()/10);
            FTRND.setRIDN().move(ITALO.getRIDN());
            FTRND.setRIDL(ITALO.getRIDL());
            FTRND.setRIDX(ITALO.getRIDX());
            FTRND.setPLRN(ITALO.getPLRN());
            FTRND.setBANO().move(ITALO.getBANO());
            FTRND.setCAMU().move(ITALO.getCAMU());
            IN91 = !FTRND.CHAIN("00", FTRND.getKey("00"));
            // Ignore records already processed
            if (IN91 ||
                  FTRND.getPISS().GE("70") ||
                  HILIN.getISMD() == 1 &&
                  FTRND.getPISS().EQ("50") ||
                  HILIN.getISMD() == 2 &&
                  FTRND.getPISS().EQ("60")) {
            } else {
               // Quantity check
               if (XXTQTY > (FTRND.getDLQT() + EPS_6)) {
                  HILIN.setDLQT(FTRND.getDLQT());
                  HILIN.setDLQA(FTRND.getDLQT());
                  XXTQTY = XXTQTY - FTRND.getDLQT();
               } else {
                  HILIN.setDLQT(XXTQTY);
                  HILIN.setDLQA(XXTQTY);
                  XXTQTY = 0d;
               }
               //----------------------------------------------------------------------
               HDISL.setCONO(LDAZD.CONO);
               HDISL.setDLIX(ITALO.getRIDI());
               HDISL.setRORC(ITALO.getTTYP()/10);
               HDISL.setRIDN().move(ITALO.getRIDN());
               HDISL.setRIDL(ITALO.getRIDL());
               HDISL.setRIDX(ITALO.getRIDX());
               if (HDISL.CHAIN("00", HDISL.getKey("00"))) {
                  HILIN.setFACI().move(HDISL.getFACI());
               } else {
                  HILIN.setFACI().move(ITWHL.getFACI());
               }
               //----------------------------------------------------------------------
               HILIN.setMSLN(0);
               HILIN.setTTYP(ITALO.getTTYP());
               HILIN.setDLIX(ITALO.getRIDI());
               HILIN.setPLSX(ITALO.getPLSX());
               HILIN.setCAWE(ITALO.getCAWE());
               if (!inAddPickSftPacLn.getQYCAWE().isBlank()) {
                  XXQTYN = 0d;
                  XXQTYA.clear();
                  XXQTYA.moveLeft(inAddPickSftPacLn.getQYCAWE());
                  this.PXDCCD = X1DCCD;
                  RQTY();
                  HILIN.setCAWE(this.PXNUM);
               }
               HILIN.setALQT(FTRND.getDLQT());
               HILIN.setPAQT(FTRND.getDLQT());
               //----------------------------------------------------------------------
               ITMAS.setCONO(LDAZD.CONO);
               ITMAS.setITNO().move(ITALO.getITNO());
               if (ITMAS.CHAIN("00", ITMAS.getKey("00"))) {
                  HILIN.setITDS().move(ITMAS.getITDS());
               } else {
                  HILIN.setITDS().clear();
               }
               //----------------------------------------------------------------------
               if (inAddPickSftPacLn.getQYPOPN().isBlank()) {
                  HILIN.setALWT(0);
                  HILIN.setALWQ().clear();
                  ITPOP.setCONO(LDAZD.CONO);
                  ITPOP.setALWT(2);
                  ITPOP.setITNO().move(ITALO.getITNO());
                  ITPOP.setALWQ().move(ITWHL.getALWQ());
                  if (ITPOP.CHAIN("30", ITPOP.getKey("30", 4))) {
                     HILIN.setPOPN().move(ITPOP.getPOPN());
                     HILIN.setALWQ().move(ITPOP.getALWQ());
                     HILIN.setALWT(ITPOP.getALWT());
                  } else {
                     ITPOP.setALWQ().clear();
                     ITPOP.SETLL("30", ITPOP.getKey("30", 4));
                     if (ITPOP.READE("30", ITPOP.getKey("30", 3))) {
                        HILIN.setPOPN().move(ITPOP.getPOPN());
                        HILIN.setALWQ().move(ITPOP.getALWQ());
                        HILIN.setALWT(ITPOP.getALWT());
                     } else {
                        HILIN.setPOPN().clear();
                     }
                  }
               }
               //----------------------------------------------------------------------
               HILIN.setREPN(ITALO.getREPN());
               HILIN.setRIDN().move(ITALO.getRIDN());
               HILIN.setRIDL(ITALO.getRIDL());
               HILIN.setRIDX(ITALO.getRIDX());
               HILIN.setRIDO(ITALO.getRIDO());
               //----------------------------------------------------------------------
               HILIN.setPLRN(ITALO.getPLRN());
               //-----------------------------------------------------------------------------------------------
               HPICD.setCONO(LDAZD.CONO);
               HPICD.setE0PA().move(inAddPickSftPacLn.getQYE0PA());
               HPICD.setWHLO().move(ITALO.getWHLO());
               HPICD.setDLIX(ITALO.getRIDI());
               HPICD.setPLSX(ITALO.getPLSX());
               HPICD.setTTYP(ITALO.getTTYP());
               HPICD.setRIDN().move(ITALO.getRIDN());
               HPICD.setRIDL(ITALO.getRIDL());
               HPICD.setRIDX(ITALO.getRIDX());
               HPICD.setRIDO(ITALO.getRIDO());
               HPICD.setWHSL().move(ITALO.getWHSL());
               HPICD.setBANO().move(ITALO.getBANO());
               HPICD.setCAMU().move(ITALO.getCAMU());
               if (!HPICD.CHAIN("00", HPICD.getKey("00"))) {
                  HPICD.setUSD1().clear();
                  HPICD.setUSD2().clear();
                  HPICD.setUSD3().clear();
                  HPICD.setUSD4().clear();
                  HPICD.setUSD5().clear();
               }
               //----------------------------------------------------------------------
               RLINE();
               if (IN60) {
                  return;
               }
            }
         }
         // No more to report
         if  (isBlank(XXTQTY, EPS_6)) {
            IN93 = true;
         } else {
            // Process included in packages
            IN93 = !PTRNS.READE("05", PTRNS.getKey("05", 3));
            if (IN93) {
               IN93 = !ITALO.READE("30", ITALO.getKey(LDAZD.CONO, "30", 2));
               FTRND.setPANR().moveLeft(HILIN.getPACN());
               PTRNS.setPAII().moveLeft(HILIN.getPACN());
               PTRNS.SETLL("05", PTRNS.getKey("05", 3));
            } else {
               FTRND.setPANR().moveLeft(PTRNS.getPANR());
               IN93 = !ITALO.CHAIN("30", ITALO.getKey("30"));
               // Current suffix processed - position to next
               if (IN93) {
                  ITALO.SETGT("30", ITALO.getKey(LDAZD.CONO, "30"));
                  IN93 = !ITALO.READE("30", ITALO.getKey(LDAZD.CONO, "30", 2));
                  FTRND.setPANR().moveLeft(HILIN.getPACN());
                  PTRNS.setPAII().moveLeft(HILIN.getPACN());
                  PTRNS.SETLL("05", PTRNS.getKey("05", 3));
               }
            }
         }
      } // end while

      if (!XXPRFL.isBlank()) {
         if (XXPRFL.EQ("*AUT") && XXOPC.NE("*DLT")) {
            RAUT();
         } else {
            if (XXPRFL.EQ("*EXE") && XXOPC.NE("*DLT")) {
               rAPIDS.clear();
               X0OPC.moveLeftPad("*CHK");
               rPHEDPIpreCall();
               apCall("MHILINPI", rPHEDPI);
               rPHEDPIpostCall();
               IN60 = toBoolean(SRIMPI.PXIN60.getChar());
               if (IN60) {
                  XXERRM = true;
                  MICommon.setError( "", X0MSID.toString(), X0MSGD);
                  this.MSGDTA.move(X0MSGD);
                  return;
               }
               MvxString MSLN = new MvxString(5);
               X0FLDD = 5;
               XXNUMN = (double)HILIN.getMSLN();
               XXNUMA.clear();
               RNUM();
               MSLN.moveRight(this.PXALPH);
               //---------------------------------
               whsTrans(MICommon.toAlpha(HILIN.getCONO()), HILIN.getMSGN(), HILIN.getPACN(), MSLN, XXPRFL);
               if (IN60) {
                  XXERRM = true;
               }
            }
         }
      }

      outAddPickSftPacLn.setYYCONO().move(LDAZD.CONO);
      outAddPickSftPacLn.setYYMSGN().move(HIHED.getMSGN());
      if (MHS850DS.getJ7STNB() > 0L) {
         X0FLDD = 10;
         XXNUMN = (long)MHS850DS.getJ7STNB();
         XXNUMA.clear();
         RNUMO();
         outAddPickSftPacLn.setYYSTNB().moveLeft(this.PXALPH);
         X0FLDD = 7;
         XXNUMN = (int)MHS850DS.getJ7STRN();
         XXNUMA.clear();
         RNUMO();
         outAddPickSftPacLn.setYYSTRN().moveLeft(this.PXALPH);
      }
      MICommon.setData( outAddPickSftPacLn.get());
   }


   public void RCOM47() {
      sMHS850MIRLstWhsLine inLstWhsLine = (sMHS850MIRLstWhsLine)MICommon.getInDS(sMHS850MIRLstWhsLine.class);
      sMHS850MISLstWhsLine outLstWhsLine = (sMHS850MISLstWhsLine)MICommon.getOutDS(sMHS850MISLstWhsLine.class);

      HILIN.setCONO(LDAZD.CONO);
    //-----------------------------------
      //    UTC mode
      UTCmode = isUTCmode(inLstWhsLine.getQ0UTCM());
      if (IN60) {
         MICommon.setError("UTCM", "WUTCM01", inLstWhsLine.getQ0UTCM());
         return;
      }
      if (inLstWhsLine.getQ0MSGN().isBlank()) {
         //   MSGID = WMS3703
         MICommon.setError( "", "WMS3703");
         return;
      }

      HILIN.setMSGN().move(inLstWhsLine.getQ0MSGN());
      HILIN.setPACN().move(inLstWhsLine.getQ0PACN());
      X0FLDD = 5;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inLstWhsLine.getQ0MSLN());
      RNUM();
      HILIN.setMSLN((int)this.PXNUM);

      // check nbr of keys
      if (!HILIN.getPACN().isBlank()) {
         nbrOfKeys = 3;
      } else {
         nbrOfKeys = 2;
      }

      HILIN.SETLL("00", HILIN.getKey("00"));
      int rcd = 0;
      while(HILIN.READE("00", HILIN.getKey("00",  nbrOfKeys)) && rcd < XXMRCD) {
         outLstWhsLine.setY0CONO().moveLeftPad(MICommon.toAlpha(HILIN.getCONO())); // Company
         outLstWhsLine.setY0MSGN().moveLeftPad(HILIN.getMSGN());
         outLstWhsLine.setY0PACN().moveLeftPad(HILIN.getPACN());
         outLstWhsLine.setY0MSLN().moveLeftPad(MICommon.toAlpha(HILIN.getMSLN()));
         outLstWhsLine.setY0DIVI().moveLeftPad(HILIN.getDIVI());
         outLstWhsLine.setY0WHLO().moveLeftPad(HILIN.getWHLO());
         if (UTCmode) {
            //   Determine time zone for Warehouse
            XXWHLO.moveLeftPad(HILIN.getWHLO());
            if (!CRCalendar.retrieveTIZOForWHLO(PXMNGTZN, LDAZD.CONO, XXWHLO)) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("GEDT","S_00148");
               return;
            }
            //   time zone for warehouse
            TIZOforWHLO.move(CRCalendar.getTimeZone());
         }
         outLstWhsLine.setY0QLFR().moveLeftPad(HILIN.getQLFR());
         outLstWhsLine.setY0QLFS().moveLeftPad(MICommon.toAlpha(HILIN.getQLFS()));
         outLstWhsLine.setY0FACI().moveLeftPad(HILIN.getFACI());
         outLstWhsLine.setY0GEDT().moveLeftPad(MICommon.toAlphaDate(HILIN.getGEDT()));
         outLstWhsLine.setY0GETM().moveLeftPad(MICommon.toAlphaTime(HILIN.getGETM()));
         // Date generated
         date = HILIN.getGEDT(); 
         time = HILIN.getGETM() / 100;
         if (UTCmode && date > 0) {
            if (!convertFromUTC()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("GEDT","S_00148");
               return;
            }
            HILIN.setGEDT(UTCdate);
            HILIN.setGETM(UTCtime * 100);
            outLstWhsLine.setY0GEDT().moveLeftPad(MICommon.toAlphaDate(HILIN.getGEDT()));
            outLstWhsLine.setY0GETM().moveLeftPad(MICommon.toAlphaTime(HILIN.getGETM()));
         }
         outLstWhsLine.setY0CHGD().moveLeftPad(MICommon.toAlphaDate(HILIN.getCHGD()));
         outLstWhsLine.setY0STAT().moveLeftPad(HILIN.getSTAT());
         outLstWhsLine.setY0ITNO().moveLeftPad(HILIN.getITNO());
         XXITNO.moveLeftPad(HILIN.setITNO());
         RITNO();
         outLstWhsLine.setY0ITDS().moveLeftPad(HILIN.getITDS());
         outLstWhsLine.setY0WHSL().moveLeftPad(HILIN.getWHSL());
         outLstWhsLine.setY0TWSL().moveLeftPad(HILIN.getTWSL());
         outLstWhsLine.setY0RSCD().moveLeftPad(HILIN.getRSCD());
         outLstWhsLine.setY0BANO().moveLeftPad(HILIN.getBANO());
         outLstWhsLine.setY0CAMU().moveLeftPad(HILIN.getCAMU());
         outLstWhsLine.setY0REPN().moveLeftPad(MICommon.toAlpha(HILIN.getREPN()));
         outLstWhsLine.setY0RELI().moveLeftPad(MICommon.toAlpha(HILIN.getRELI()));
         outLstWhsLine.setY0POPN().moveLeftPad(HILIN.getPOPN());
         outLstWhsLine.setY0ALWQ().moveLeftPad(HILIN.getALWQ());
         outLstWhsLine.setY0ALWT().moveLeftPad(MICommon.toAlpha(HILIN.getALWT()));
         outLstWhsLine.setY0DLQT().moveLeftPad(MICommon.toAlpha(HILIN.getDLQT(), X1DCCD));
         outLstWhsLine.setY0UNIT().moveLeftPad(HILIN.getUNIT());
         outLstWhsLine.setY0DLQA().moveLeftPad(MICommon.toAlpha(HILIN.getDLQA(), X1DCCD));
         outLstWhsLine.setY0ALUN().moveLeftPad(HILIN.getALUN());
         outLstWhsLine.setY0VOL3().moveLeftPad(MICommon.toAlpha(HILIN.getVOL3(), 3));
         outLstWhsLine.setY0NEWE().moveLeftPad(MICommon.toAlpha(HILIN.getNEWE(), 3));
         outLstWhsLine.setY0GRWE().moveLeftPad(MICommon.toAlpha(HILIN.getGRWE(), 3));
         outLstWhsLine.setY0D1QT().moveLeftPad(MICommon.toAlpha(HILIN.getD1QT(), X1DCCD));
         outLstWhsLine.setY0DLSP().moveLeftPad(HILIN.getDLSP());
         outLstWhsLine.setY0RVQA().moveLeftPad(MICommon.toAlpha(HILIN.getRVQA(), X1DCCD));
         outLstWhsLine.setY0PUUN().moveLeftPad(HILIN.getPUUN());
         outLstWhsLine.setY0ALQT().moveLeftPad(MICommon.toAlpha(HILIN.getALQT(), X1DCCD));
         outLstWhsLine.setY0PAQT().moveLeftPad(MICommon.toAlpha(HILIN.getPAQT(), X1DCCD));
         outLstWhsLine.setY0CAWE().moveLeftPad(MICommon.toAlpha(HILIN.getCAWE(), X2DCCD));
         outLstWhsLine.setY0POCY().moveLeftPad(MICommon.toAlpha(HILIN.getPOCY(), 2));
         outLstWhsLine.setY0OEND().moveLeftPad(MICommon.toAlpha(HILIN.getOEND()));
         outLstWhsLine.setY0BREF().moveLeftPad(HILIN.getBREF());
         outLstWhsLine.setY0BRE2().moveLeftPad(HILIN.getBRE2());
         outLstWhsLine.setY0FLOC().moveLeftPad(HILIN.getFLOC());
         outLstWhsLine.setY0ORCA().moveLeftPad(HILIN.getORCA());
         outLstWhsLine.setY0TTYP().moveLeftPad(MICommon.toAlpha(HILIN.getTTYP()));
         outLstWhsLine.setY0RIDN().moveLeftPad(HILIN.getRIDN());
         outLstWhsLine.setY0RIDO().moveLeftPad(MICommon.toAlpha(HILIN.getRIDO()));
         outLstWhsLine.setY0RIDL().moveLeftPad(MICommon.toAlpha(HILIN.getRIDL()));
         outLstWhsLine.setY0RIDX().moveLeftPad(MICommon.toAlpha(HILIN.getRIDX()));
         outLstWhsLine.setY0RIDI().moveLeftPad(MICommon.toAlpha(HILIN.getRIDI()));
         outLstWhsLine.setY0PLSX().moveLeftPad(MICommon.toAlpha(HILIN.getPLSX()));
         outLstWhsLine.setY0DLIX().moveLeftPad(MICommon.toAlpha(HILIN.getDLIX()));
         outLstWhsLine.setY0DNNO().moveLeftPad(HILIN.getDNNO());
         outLstWhsLine.setY0CUOR().moveLeftPad(HILIN.getCUOR());
         outLstWhsLine.setY0CUNO().moveLeftPad(HILIN.getCUNO());
         outLstWhsLine.setY0ADID().moveLeftPad(HILIN.getADID());
         outLstWhsLine.setY0SUNO().moveLeftPad(HILIN.getSUNO());
         outLstWhsLine.setY0SUTY().moveLeftPad(MICommon.toAlpha(HILIN.getSUTY()));
         outLstWhsLine.setY0SUDO().moveLeftPad(HILIN.getSUDO());
         outLstWhsLine.setY0DNDT().moveLeftPad(MICommon.toAlphaDate(HILIN.getDNDT()));
         outLstWhsLine.setY0EXPI().moveLeftPad(MICommon.toAlphaDate(HILIN.getEXPI()));
         outLstWhsLine.setY0CNDT().moveLeftPad(MICommon.toAlphaDate(HILIN.getCNDT()));
         outLstWhsLine.setY0SEDT().moveLeftPad(MICommon.toAlphaDate(HILIN.getSEDT()));
         outLstWhsLine.setY0QCRA().moveLeftPad(HILIN.getQCRA());
         outLstWhsLine.setY0SCRE().moveLeftPad(HILIN.getSCRE());
         outLstWhsLine.setY0BREM().moveLeftPad(HILIN.getBREM());
         outLstWhsLine.setY0TRTP().moveLeftPad(HILIN.getTRTP());
         outLstWhsLine.setY0SITE().moveLeftPad(HILIN.getSITE());
         outLstWhsLine.setY0SITD().moveLeftPad(HILIN.getSITD());
         outLstWhsLine.setY0PUPR().moveLeftPad(MICommon.toAlpha(HILIN.getPUPR(), 6));
         outLstWhsLine.setY0PPUN().moveLeftPad(HILIN.getPPUN());
         outLstWhsLine.setY0CUCD().moveLeftPad(HILIN.getCUCD());
         outLstWhsLine.setY0PUCD().moveLeftPad(MICommon.toAlpha(HILIN.getPUCD()));
         outLstWhsLine.setY0LNAM().moveLeftPad(MICommon.toAlpha(HILIN.getLNAM(), 2));
         outLstWhsLine.setY0VTCD().moveLeftPad(MICommon.toAlpha(HILIN.getVTCD()));
         outLstWhsLine.setY0AGNB().moveLeftPad(HILIN.getAGNB());
         outLstWhsLine.setY0RESP().moveLeftPad(HILIN.getRESP());
         outLstWhsLine.setY0DTID().moveLeftPad(MICommon.toAlpha(HILIN.getDTID()));
         outLstWhsLine.setY0TXID().moveLeftPad(MICommon.toAlpha(HILIN.getTXID()));
         outLstWhsLine.setY0USD1().moveLeftPad(HILIN.getUSD1());
         outLstWhsLine.setY0USD2().moveLeftPad(HILIN.getUSD2());
         outLstWhsLine.setY0USD3().moveLeftPad(HILIN.getUSD3());
         outLstWhsLine.setY0USD4().moveLeftPad(HILIN.getUSD4());
         outLstWhsLine.setY0USD5().moveLeftPad(HILIN.getUSD5());
         outLstWhsLine.setY0RGDT().moveLeftPad(MICommon.toAlphaDate(HILIN.getRGDT()));
         outLstWhsLine.setY0RGTM().moveLeftPad(MICommon.toAlphaTime(HILIN.getRGTM()));
         outLstWhsLine.setY0LMDT().moveLeftPad(MICommon.toAlphaDate(HILIN.getLMDT()));
         outLstWhsLine.setY0CHNO().moveLeftPad(MICommon.toAlpha(HILIN.getCHNO()));
         outLstWhsLine.setY0CHID().moveLeftPad(HILIN.getCHID());
         MICommon.setData( outLstWhsLine.get());
         MICommon.reply();
         rcd++;
      }
      MICommon.clearBuffer();
   }

   public void AddTransNotify() {
      // ----------------------------------------------------------------
      //   Clear transaction records
      // ----------------------------------------------------------------
      sMHS850MIRAddTransNotify inAddTransNotify = (sMHS850MIRAddTransNotify)MICommon.getInDS(sMHS850MIRAddTransNotify.class);
      sMHS850MISAddTransNotify outAddTransNotify = (sMHS850MISAddTransNotify)MICommon.getOutDS(sMHS850MISAddTransNotify.class);

      HIHED.clear();
      HIPAC.clear();
      HILIN.clear();
      // ----------------------------------------------------------------
      //   Save process option.
      // ----------------------------------------------------------------
      XXPRFL.move(inAddTransNotify.getQVPRFL());
      XXOPC.move("*ADD");
      if (MICommon.toNumericCompany(inAddTransNotify.getQVCONO())) {
         XXCONO = MICommon.getInt();
      } else {
         MICommon.setError("CONO");
         return;
      }

      LDAZD.CONO = XXCONO;
      //-----------------------------------
      // Message type
      XXE065.moveLeftPad(inAddTransNotify.getQVE065());
      RE065();
      if (IN60) {
         return;
      }
      // Check Warehouse
      XXWHLO.moveLeftPad(inAddTransNotify.getQVWHLO());
      RWHL();
      if (IN60) {
         return;
      }
      if (UTCmode) {
         //   Determine time zone for Warehouse
         if (!CRCalendar.retrieveTIZOForWHLO(PXMNGTZN, LDAZD.CONO, XXWHLO)) {
            IN60 = true;
            //   MSGID=S_00148 UTC time conversion failed.
            MICommon.setError("GEDT","S_00148");
            return;
         }
         //   time zone for warehouse
         TIZOforWHLO.move(CRCalendar.getTimeZone());
      }
      //-----------------------------------
      ITWHL.setCONO(LDAZD.CONO);
      ITWHL.setWHLO().move(inAddTransNotify.getQVWHLO());
      if (!ITWHL.CHAIN("00", ITWHL.getKey("00"))) {
         //   MSGID=WWH0103 Warehouse &1 does not exist
         MICommon.setError( "", "WWH0103", inAddTransNotify.getQVWHLO());
         return;
      }
      if (inAddTransNotify.getQVE0PA().isBlank()) {
         //   MSGID = WE00A02  Partner does not exist
         MICommon.setError( "", "WE00A02");
         return;
      }
      if (inAddTransNotify.getQVE065().isBlank()) {
         //   MSGID = ED01016  Message type does not exist for partner
         MICommon.setError( "", "ED01016");
         return;
      }
      if (inAddTransNotify.getQVE0B4().isBlank() && !HIHED.getSUDO().isBlank()) {
         if (inAddTransNotify.getQVSUNO().isBlank()) {
            //   MSGID=WSU0102 Supplier number must be entered
            MICommon.setError( "", "WSU0102");
            return;
         }

      }

      if (inAddTransNotify.getQVSUDO().isBlank() && inAddTransNotify.getQVE0B4().isBlank() && inAddTransNotify.getQVBOLN().isBlank() ) {
         //   MSGID=WSUD202 Delivery note number must be entered
         MICommon.setError( "", "WSUD202");
         this.MSGDTA.moveLeft(HIHED.getSUDO());
         return;
      }

      //   Retreive message number if blank
      if (inAddTransNotify.getQVMSGN().isBlank()) {
         RVMSNR();
         inAddTransNotify.setQVMSGN().moveLeft(PXRTVNBR.PXNBNR, 10);
      } else {
         HIHED.setCONO(LDAZD.CONO);
         HIHED.setMSGN().move(inAddTransNotify.getQVMSGN());
         rAPIDS.clear();
         X0OPC.moveLeftPad("*GET");
         rPHEDPIpreCall();
         apCall("MHIHEDPI", rPHEDPI);
         rPHEDPIpostCall();
      }
      HIPAC.setCONO(LDAZD.CONO);
      HIPAC.setMSGN().move(inAddTransNotify.getQVMSGN());
      HIPAC.setPACN().move("DUMMY");
      rAPIDS.clear();
      X0OPC.moveLeftPad("*GET");
      rPPACPIpreCall();
      apCall("MHIPACPI", rPPACPI);
      rPPACPIpostCall();
      ITWHL.setCONO(LDAZD.CONO);
      ITWHL.setWHLO().move(inAddTransNotify.getQVWHLO());
      if (!ITWHL.CHAIN("00", ITWHL.getKey("00"))) {
         //   MSGID=WWH0103 Warehouse &1 does not exist
         MICommon.setError( "", "WWH0103", inAddTransNotify.getQVWHLO());
         return;
      } else {
         HIHED.setFACI().move(ITWHL.getFACI());
      }
      HIHED.setCONO(LDAZD.CONO);
      HIPAC.setCONO(LDAZD.CONO);
      HIHED.setQLFR().move("27  ");
      HIPAC.setQLFR().move("27  ");
      //   Check length of package number
      HIHED.setE0IO('I');
      HIHED.setWHLO().move(inAddTransNotify.getQVWHLO());
      HIPAC.setWHLO().move(inAddTransNotify.getQVWHLO());
      HIHED.setMSGN().move(inAddTransNotify.getQVMSGN());
      HIPAC.setMSGN().move(inAddTransNotify.getQVMSGN());
      HIHED.setPMSN().moveLeft(inAddTransNotify.getQVMSGN());
      HIHED.setE0PA().move(inAddTransNotify.getQVE0PA());
      HIHED.setE0PB().move(inAddTransNotify.getQVE0PA());
      HIHED.setE035(0);
      HIHED.setE065().move(inAddTransNotify.getQVE065());
      if (!inAddTransNotify.getQVPMSN().isBlank()) {
         HIHED.setPMSN().move(inAddTransNotify.getQVPMSN());
      } else {
         HIHED.setPMSN().moveLeft(inAddTransNotify.getQVMSGN());
      }

      HIHED.setSUDO().move(inAddTransNotify.getQVSUDO());
      HIHED.setSUNO().move(inAddTransNotify.getQVSUNO());
      HIHED.setEDFR().move(inAddTransNotify.getQVEDFR());
      HIHED.setPUSN().move(inAddTransNotify.getQVPUSN());
      HIHED.setSORN().move(inAddTransNotify.getQVSORN());
      HIHED.setE0B4().move(inAddTransNotify.getQVE0B4());   //Transport ID departure
      HIHED.setE0BH().move(inAddTransNotify.getQVE0BH());
      HIHED.setBOLN().move(inAddTransNotify.getQVBOLN());
      HIHED.setFWRF().move(inAddTransNotify.getQVFWRF());
      HIHED.setMODL().move(inAddTransNotify.getQVMODL());
      HIHED.setTEDL().move(inAddTransNotify.getQVTEDL());
      HIHED.setTFNO().move(inAddTransNotify.getQVTFNO());

      if (!MICommon.toNumericDate(inAddTransNotify.getQVDNDT())) {
         MICommon.setError("DNDT");
         return;
      }
      HIHED.setDNDT(MICommon.getNumericDate());

      if (!MICommon.toNumericDate(inAddTransNotify.getQVSHD4())) {
         MICommon.setError("SHD4");
         return;
      }
      HIHED.setSHD4(MICommon.getNumericDate());

      if (!MICommon.toNumericDate(inAddTransNotify.getQVARDT())) {
         MICommon.setError("ARDT");
         return;
      }
      HIHED.setARDT(MICommon.getNumericDate());

      //   Validate/create transaction header data
      RHEAD();
      if (IN60 && IN76) {
         return;
      }
      //   Validate/create transaction package data
      RPACK();
      if (IN60 && IN76) {
         return;
      }
      //--------------------------------------------------------
      HILIN.setCONO(LDAZD.CONO);
      HILIN.setMSGN().move(inAddTransNotify.getQVMSGN());
      HILIN.setPACN().move("DUMMY");
      HILIN.setQLFR().move("27  ");
      HILIN.setTTYP(20);
      HILIN.setWHLO().move(inAddTransNotify.getQVWHLO());
      HILIN.setMSLN(0);
      RLINE();
      if (IN60) {
         return;
      }

      if (!XXPRFL.isBlank()) {
         if (XXPRFL.EQ("*AUT") && XXOPC.NE("*DLT")) {
            RAUT();
         } else {
            if (XXPRFL.EQ("*EXE") && XXOPC.NE("*DLT")) {
               whsTrans(inAddTransNotify.getQVCONO(), HIHED.getMSGN(), new MvxString(5), new MvxString(5), XXPRFL);
               if (IN60) {
                  XXERRM = true;
               }
            }
         }
      }

      outAddTransNotify.setYVCONO().move(XXCONO);
      outAddTransNotify.setYVMSGN().move(HIHED.getMSGN());
      MICommon.setData( outAddTransNotify.get());
   }

   public void AddWOPick() {
      // ----------------------------------------------------------------
      //   Clear transaction records
      // ----------------------------------------------------------------
      sMHS850MIRAddWOPick inAddWOPick = (sMHS850MIRAddWOPick)MICommon.getInDS(sMHS850MIRAddWOPick.class);
      sMHS850MISAddWOPick outAddWOPick = (sMHS850MISAddWOPick)MICommon.getOutDS(sMHS850MISAddWOPick.class);

      HIHED.clear();
      HIPAC.clear();
      HILIN.clear();
      // ----------------------------------------------------------------
      //   Save process option.
      // ----------------------------------------------------------------
      XXPRFL.move(inAddWOPick.getQ0PRFL());
      XXOPC.move("*ADD");
      if (MICommon.toNumericCompany(inAddWOPick.getQ0CONO())) {
         XXCONO = MICommon.getInt();
      } else {
         MICommon.setError("CONO");
         return;
      }

      LDAZD.CONO = XXCONO;
    //-----------------------------------
      //    UTC mode
      UTCmode = isUTCmode(inAddWOPick.getQ0UTCM());
      if (IN60) {
         MICommon.setError("UTCM", "WUTCM01", inAddWOPick.getQ0UTCM());
         return;
      }
      //-----------------------------------
      // Message type
      XXE065.moveLeftPad(inAddWOPick.getQ0E065());
      RE065();
      if (IN60) {
         return;
      }
      // Check Warehouse
      XXWHLO.moveLeftPad(inAddWOPick.getQ0WHLO());
      RWHL();
      if (IN60) {
         return;
      }
      if (UTCmode) {
         //   Determine time zone for Warehouse
         if (!CRCalendar.retrieveTIZOForWHLO(PXMNGTZN, LDAZD.CONO, XXWHLO)) {
            IN60 = true;
            //   MSGID=S_00148 UTC time conversion failed.
            MICommon.setError("GEDT","S_00148");
            return;
         }
         //   time zone for warehouse
         TIZOforWHLO.move(CRCalendar.getTimeZone());
      }
      //-----------------------------------
      //   Retreive message number if blank
      if (inAddWOPick.getQ0MSGN().isBlank()) {
         RVMSNR();
         inAddWOPick.setQ0MSGN().moveLeft(PXRTVNBR.PXNBNR, 10);
      } else {
         HIHED.setCONO(LDAZD.CONO);
         HIHED.setMSGN().move(inAddWOPick.getQ0MSGN());
         rAPIDS.clear();
         X0OPC.moveLeftPad("*GET");
         rPHEDPIpreCall();
         apCall("MHIHEDPI", rPHEDPI);
         rPHEDPIpostCall();
      }
      //-----------------------------------
      //   SSCC/PANR/DLIX Validation
      //-----------------------------------
      PTRNSfound = false;
      inputDLIX.moveLeft(inAddWOPick.getQ0DLIX());
      inputPANR.moveLeft(inAddWOPick.getQ0PACN());
      inputSSCC.moveLeft(inAddWOPick.getQ0SSCC());
      numberPackage = 0;
      if (!validInput()) {
         return;
      }
      if (inAddWOPick.getQ0DLIX().isBlank()) {
         PTRNS.setCONO(LDAZD.CONO);
         PTRNS.setDIPA(0);
         PTRNS.setINOU(1);
         if (!inAddWOPick.getQ0PACN().isBlank()) {
            PTRNS.setPANR().moveLeft(inAddWOPick.getQ0PACN());
            PTRNS.SETLL("85", PTRNS.getKey("85", 4));
            if (PTRNS.READE("85", PTRNS.getKey("85",4))) {
               if (PTRNS.READE("85", PTRNS.getKey("85",4))) {
                  //   MSGID-WDL0202 Delivery number must be entered
                  MICommon.setError( "", "WDL0202");
                  return;
               }
               PTRNSfound = true;
               HILIN.setDLIX(PTRNS.getDLIX());
            }
         }
         if (!inAddWOPick.getQ0SSCC().isBlank()) {
            PTRNS.setSSCC().moveLeft(inAddWOPick.getQ0SSCC());
            if (PTRNS.CHAIN("70", PTRNS.getKey("70", 3))) {
               PTRNSfound = true;
               HILIN.setDLIX(PTRNS.getDLIX());
               inAddWOPick.setQ0PACN().moveLeft(PTRNS.getPANR());
            }
         }
      }
      //   Default package number if blank
      if (inAddWOPick.getQ0PACN().isBlank()) {
         inAddWOPick.setQ0PACN().moveLeft(inAddWOPick.getQ0MSGN());
      }
      HIPAC.setCONO(LDAZD.CONO);
      HIPAC.setMSGN().move(inAddWOPick.getQ0MSGN());
      HIPAC.setPACN().move(inAddWOPick.getQ0PACN());
      rAPIDS.clear();
      X0OPC.moveLeftPad("*GET");
      rPPACPIpreCall();
      apCall("MHIPACPI", rPPACPI);
      rPPACPIpostCall();
      HILIN.setCONO(LDAZD.CONO);
      HILIN.setMSGN().move(inAddWOPick.getQ0MSGN());
      HILIN.setPACN().move(inAddWOPick.getQ0PACN());
      HILIN.setMSLN(0);
      //   Retreive facility for warehouse
      ITWHL.setCONO(LDAZD.CONO);
      ITWHL.setWHLO().move(inAddWOPick.getQ0WHLO());
      if (!ITWHL.CHAIN("00", ITWHL.getKey("00"))) {
         //   MSGID=WWH0103 Warehouse &1 does not exist
         MICommon.setError( "", "WWH0103", inAddWOPick.getQ0WHLO());
         return;
      } else {
         HIHED.setFACI().move(ITWHL.getFACI());
      }
      //   Format date/time
      if (inAddWOPick.getQ0GEDT().isBlank() ||
            inAddWOPick.getQ0GETM().isBlank()) {
         XXGEDT = movexDate();
         XXGETM = movexTime();
      } else {
         if (MICommon.toNumericDate(inAddWOPick.getQ0GEDT())) {
            XXGEDT = MICommon.getNumericDate();
         } else {
            MICommon.setError("GEDT");
            return;
         }
         if (!MICommon.toNumeric(inAddWOPick.getQ0GETM())) {
            MICommon.setError("GETM");
            return;
         }
         XXGETM = MICommon.getInt();
         // Date generated
         date = XXGEDT; 
         time = XXGETM / 100;
         if (UTCmode && date > 0) {
            if (!convertFromUTC()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("GEDT","S_00148");
               return;
            }
            XXGEDT = UTCdate;
            XXGETM = UTCtime * 100;
         }
      }
      // ----------------------------------------------------------------
      //   Move data from the transaction input structure (MHS850MI1)
      //   to the execution data formats (MHIHED/MHIPAC/MHILIN)
      // ----------------------------------------------------------------
      HIHED.setCONO(LDAZD.CONO);
      HIPAC.setCONO(LDAZD.CONO);
      HILIN.setCONO(LDAZD.CONO);
      HIHED.setQLFR().move("61  ");
      HIPAC.setQLFR().move("61  ");
      HILIN.setQLFR().move("61  ");
      //----------------------------------------
      //---------------------------------------
      HILIN.setTTYP(61);
      HIHED.setE0IO('I');
      HIHED.setWHLO().move(inAddWOPick.getQ0WHLO());
      HIPAC.setWHLO().move(inAddWOPick.getQ0WHLO());
      HILIN.setWHLO().move(inAddWOPick.getQ0WHLO());
      HIHED.setCUNO().moveLeft(inAddWOPick.getQ0WHLO());
      HIPAC.setCUNO().moveLeft(inAddWOPick.getQ0WHLO());
      HILIN.setCUNO().moveLeft(inAddWOPick.getQ0WHLO());
      HIHED.setMSGN().move(inAddWOPick.getQ0MSGN());
      HIPAC.setMSGN().move(inAddWOPick.getQ0MSGN());
      HILIN.setMSGN().move(inAddWOPick.getQ0MSGN());
      HIHED.setPMSN().moveLeft(inAddWOPick.getQ0MSGN());
      HIPAC.setPACN().move(inAddWOPick.getQ0PACN());
      HILIN.setPACN().move(inAddWOPick.getQ0PACN());
      HIHED.setGEDT(XXGEDT);
      HIPAC.setGEDT(XXGEDT);
      HILIN.setGEDT(XXGEDT);
      HIHED.setGETM(XXGETM);
      HIPAC.setGETM(XXGETM);
      HILIN.setGETM(XXGETM);
      HIHED.setE0PA().move(inAddWOPick.getQ0E0PA());
      HIHED.setE0PB().move(inAddWOPick.getQ0E0PA());
      HIHED.setE035(0);
      HIHED.setE065().move(inAddWOPick.getQ0E065());
      HIHED.setADID().move(inAddWOPick.getQ0ADID());
      HIPAC.setADID().move(inAddWOPick.getQ0ADID());
      HILIN.setADID().move(inAddWOPick.getQ0ADID());
      HILIN.setITNO().move(inAddWOPick.getQ0ITNO());
      XXITNO.moveLeftPad(HILIN.setITNO());
      RITNO();
      //--------------------------------
      HILIN.setPOPN().move(inAddWOPick.getQ0POPN());
      HILIN.setALWQ().move(inAddWOPick.getQ0ALWQ());
      X0FLDD = 2;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWOPick.getQ0ALWT());
      RNUM();
      HILIN.setALWT((int)this.PXNUM);
      X0FLDD = 1;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWOPick.getQ0OEND().getChar());
      RNUM();
      HILIN.setOEND((int)this.PXNUM);
      HILIN.setWHSL().move(inAddWOPick.getQ0WHSL());
      HILIN.setBANO().move(inAddWOPick.getQ0BANO());
      HILIN.setCAMU().move(inAddWOPick.getQ0CAMU());
      XXQTYN = 0d;
      XXQTYA.clear();
      XXQTYA.moveLeft(inAddWOPick.getQ0QTYP());
      this.PXDCCD = X1DCCD;
      RQTY();
      HILIN.setDLQT(this.PXNUM);
      XXQTYN = 0d;
      XXQTYA.clear();
      XXQTYA.moveLeft(inAddWOPick.getQ0QTYO());
      this.PXDCCD = X1DCCD;
      RQTY();
      HILIN.setALQT(this.PXNUM);
      HILIN.setRIDN().move(inAddWOPick.getQ0RIDN());
      X0FLDD = 6;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWOPick.getQ0RIDL());
      RNUM();
      HILIN.setRIDL((int)this.PXNUM);
      X0FLDD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWOPick.getQ0RIDX());
      RNUM();
      HILIN.setRIDX((int)this.PXNUM);
      X0FLDD = 6;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWOPick.getQ0RIDO());
      RNUM();
      HILIN.setRIDO((int)this.PXNUM);
      if (!PTRNSfound) {
         X0FLDD = 11;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inAddWOPick.getQ0DLIX());
         RNUM();
         HILIN.setDLIX((long)this.PXNUM);
      }
      if (!inAddWOPick.getQ0PLRN().isBlank()) {
         X0FLDD = 11;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inAddWOPick.getQ0PLRN());
         RNUM();
         HILIN.setPLRN((long)this.PXNUM);
      }
      //   Format reporting date
      if (!inAddWOPick.getQ0RPDT().isBlank()) {
         if (!MICommon.toNumericDate(inAddWOPick.getQ0RPDT())) {
            MICommon.setError("RPDT");
            return;
         }
         HILIN.setRPDT(MICommon.getNumericDate());
      }
      //--------------------------------------------------------------------
      if (!inAddWOPick.getQ0RPTM().isBlank()) {
         X0FLDD = 6;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inAddWOPick.getQ0RPTM());
         RNUM();
         HILIN.setRPTM((int)this.PXNUM);
      }
      if (!UTCmode && 
         !inAddWOPick.getQ0RPDT().isBlank() &&
            inAddWOPick.getQ0RPTM().isBlank()) {
         HILIN.setRPTM(movexTime());
      }
      if (UTCmode) {
         // Reporting date
         if (!inAddWOPick.getQ0RPDT().isBlank() &&
             !inAddWOPick.getQ0RPTM().isBlank()) {
            date = HILIN.getRPDT(); 
            time = HILIN.getRPTM() / 100;           
            if (!convertFromUTC()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("RPDT","S_00148");
               return;
            }
            HILIN.setRPDT(UTCdate);
            HILIN.setRPTM(UTCtime * 100);
         } else {
            if (inAddWOPick.getQ0RPDT().isBlank() ||
               inAddWOPick.getQ0RPTM().isBlank()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("RPDT","S_00148");
               return;
            }
         }
      }
      //--------------------------------------------------------------------
      X0FLDD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWOPick.getQ0PLSX());
      RNUM();
      HILIN.setPLSX((int)this.PXNUM);
      HILIN.setUSD1().move(inAddWOPick.getQ0USD1());
      HILIN.setUSD2().move(inAddWOPick.getQ0USD2());
      HILIN.setUSD3().move(inAddWOPick.getQ0USD3());
      HILIN.setUSD4().move(inAddWOPick.getQ0USD4());
      HILIN.setUSD5().move(inAddWOPick.getQ0USD5());
      XXQTYN = 0d;
      XXQTYA.clear();
      XXQTYA.moveLeft(inAddWOPick.getQ0CAWE());
      this.PXDCCD = X2DCCD;
      RQTY();
      HILIN.setCAWE(this.PXNUM);
      HIHED.setE0IO('I');
      if (!inAddWOPick.getQ0PMSN().isBlank()) {
         HIHED.setPMSN().move(inAddWOPick.getQ0PMSN());
      } else {
         HIHED.setPMSN().moveLeft(inAddWOPick.getQ0MSGN());
      }
      HILIN.setTWSL().move(inAddWOPick.getQ0TWSL());
      X0FLDD = 9;
      X0DCCD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWOPick.getQ0GRWE());
      RNUM();
      HIPAC.setGRWE(this.PXNUM);
      X0FLDD = 9;
      X0DCCD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWOPick.getQ0VOM3());
      RNUM();
      HIPAC.setVOM3(this.PXNUM);
      X0FLDD = 11;
      X0DCCD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWOPick.getQ0FRCP());
      RNUM();
      HIPAC.setFRCP(this.PXNUM);
      //-----------------------------
      HIPAC.setPARE().move(inAddWOPick.getQ0PARE());
      HIPAC.setSSCC().move(inAddWOPick.getQ0SSCC());
      X0FLDD = 1;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWOPick.getQ0ISMD().getChar());
      RNUM();
      HILIN.setISMD((int)this.PXNUM);
      HILIN.setLODO().moveLeftPad(inAddWOPick.getQ0LODO());
      //-----------------------------------------------------------
      HIPAC.setPACT().move(inAddWOPick.getQ0PACT());
      X0FLDD = 7;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddWOPick.getQ0AMKO());
      RNUM();
      HIPAC.setAMKO((int)this.PXNUM);
      //-------------------------------------------------
      //   Validate/create transaction header data
      //-------------------------------------------------
      if (IN75) {
         IN60 = true;
         return;
      }
      //-------------------------------------------------
      RHEAD();
      if (IN60 && IN76) {
         return;
      }
      RPACK();
      if (IN60 && IN76) {
         return;
      }
      //   Validate/create transaction line data
      RLINE();
      if (IN60) {
         return;
      }

      outAddWOPick.setY0CONO().move(XXCONO);
      outAddWOPick.setY0MSGN().move(HIHED.getMSGN());
      if (MHS850DS.getJ7STNB() > 0L) {
         X0FLDD = 10;
         XXNUMN = (long)MHS850DS.getJ7STNB();
         XXNUMA.clear();
         RNUMO();
         outAddWOPick.setY0STNB().moveLeft(this.PXALPH);
         X0FLDD = 7;
         XXNUMN = (int)MHS850DS.getJ7STRN();
         XXNUMA.clear();
         RNUMO();
         outAddWOPick.setY0STRN().moveLeft(this.PXALPH);
      }
      MICommon.setData( outAddWOPick.get());
   }


   public void AddSubLine() {
      // ----------------------------------------------------------------
      //   Clear transaction records
      // ----------------------------------------------------------------
      sMHS850MIRAddSubLine inAddSubLine = (sMHS850MIRAddSubLine)MICommon.getInDS(sMHS850MIRAddSubLine.class);

      HISUB.clear();
      HILIN.clear();
      
      if (MICommon.toNumericCompany(inAddSubLine.getS1CONO())) {
         XXCONO = MICommon.getInt();
      } else {
         MICommon.setError("CONO");
         return;
      }

      LDAZD.CONO = XXCONO;      
 
      // ----------------------------------------------------------------
      //   Retreive original record before testing for new values.
      // ----------------------------------------------------------------
      HILIN.setCONO(LDAZD.CONO);
      HILIN.setMSGN().move(inAddSubLine.getS1MSGN());
      HILIN.setPACN().move(inAddSubLine.getS1PACN());
      X0FLDD = 5;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddSubLine.getS1MSLN());
      RNUM();
      HILIN.setMSLN((int)this.PXNUM);
      rAPIDS.clear();
      X0OPC.moveLeftPad("*GET");
      rPLINPIpreCall();
      apCall("MHILINPI", rPLINPI);
      rPLINPIpostCall();
      IN60 = toBoolean(X0IN60.getChar());
      if (IN60) {
         MICommon.setError( "", X0MSID.toString(), X0MSGD);
         return;
      }
      // ----------------------------------------------------------------
      //   Test for validity before performing transaction.
      // ----------------------------------------------------------------
      if (HILIN.getSTAT().GE("90")) {
         //   MSGID=XST0020 Status change are not permitted any longer
         MICommon.setError( "", "XST0020", HILIN.getSTAT());
         return;
      }
      //   Get product data
      if (inAddSubLine.getS1ITNO().isBlank()) {
         inAddSubLine.setS1ITNO().move(HILIN.getITNO());
      }
      ITMAS.setCONO(LDAZD.CONO);
      ITMAS.setITNO().move(inAddSubLine.getS1ITNO());
      if (!ITMAS.CHAIN("00", ITMAS.getKey("00"))) {
         //   MSGID=WIT0103 Item number &1 does not exist 
         MICommon.setError( "", "WIT0103", inAddSubLine.getS1ITNO());
         return;
      }
      XXITNO.moveLeftPad(inAddSubLine.getS1ITNO());
      RITNO();
      
      if (inAddSubLine.getS1ITNO().NE(HILIN.getITNO())) {
         //   MSGID=S_00168  Item number on sublot do not correlate with the message line
         MICommon.setError( "", "S_00168");   
         return;
      }
      
      if (inAddSubLine.getS1BANO().isBlank()) {
         inAddSubLine.setS1BANO().move(HILIN.getBANO());
      }
      if (inAddSubLine.getS1BANO().NE(HILIN.getBANO())) {
         //   MSGID=S_00168  Lot number on sublot do not correlate with the message line
         MICommon.setError( "", "S_00168");    
         return;
      }
      
      if (inAddSubLine.getS1QLFR().isBlank()) {
         inAddSubLine.setS1QLFR().move(HILIN.getQLFR());
      }
      if (inAddSubLine.getS1QLFR().NE( HILIN.getQLFR())) {
         //   MSGID=S_00170  Qualifier on sublot do not correlate with the message line
         MICommon.setError( "", "S_00170");  
         return;
      }      

      if (inAddSubLine.getS1BANT().isBlank()) {
         //   MSGID=WBANT02 Reference sublot ID must be entered
         MICommon.setError("BANT", "WBANT02", inAddSubLine.getS1BANT());  
         return;
      }
      // Validate that BANT doesn't excists on any other record
      inputMSGN.moveLeftPad(inAddSubLine.getS1MSGN());
      inputPACN.moveLeftPad(inAddSubLine.getS1PACN());
      XXNUMA.moveLeft(inAddSubLine.getS1MSLN());
      RNUM();
      if (IN75) {
         IN60 = true;
         return;
      }
      inputMSLN = (int)this.PXNUM;
      inputBANT.moveLeftPad(inAddSubLine.getS1BANT());
      validateBANT();
      if (IN60) {
         //MSGID=WBANT04  Reference sublot ID &1 already exists
         MICommon.setError("BANT", "WBANT04", inAddSubLine.getS1BANT());
         return; 
      }
      HISUB.setCONO(LDAZD.CONO);
      HISUB.setMSGN().move(inAddSubLine.getS1MSGN());
      HISUB.setPACN().move(inAddSubLine.getS1PACN());
      XXNUMA.moveLeft(inAddSubLine.getS1MSLN());
      RNUM();
      if (IN75) {
         IN60 = true;
         return;
      }
      HISUB.setMSLN(inputMSLN);  
      HISUB.SETGT("00", HISUB.getKey("00", 4));
      if (HISUB.REDPE("00", HISUB.getKey("00", 4))) {
        HISUB.setSUBL(HISUB.getSUBL() + 1);
      } else {
         HISUB.setSUBL(1);
      } 
      
      HISUB.clearNOKEY("00");  
     
      HISUB.setBANO().move(inAddSubLine.getS1BANO());
      HISUB.setBANT().move(inAddSubLine.getS1BANT());
      HISUB.setITNO().move(inAddSubLine.getS1ITNO());
      HISUB.setQLFR().move(inAddSubLine.getS1QLFR());
      if (inAddSubLine.getS1CAWE().isBlank() && ITMAS.getACTI() >= 2 && HISUB.getQLFR().NE("50  ")) {
         //   MSGID=PM06036 Catch weight value must be grater than zero
         MICommon.setError("CAWE", "PM06036");  
         return;
      }     
      if (HISUB.getQLFR().NE("50  ")) {
         XXQTYN = 0d;
         XXQTYA.clear();
         XXQTYA.moveLeft(inAddSubLine.getS1CAWE());
         this.PXDCCD = X2DCCD;
         RQTY();
         if (IN75) {
            IN60 = true;
            return;
         }
         HISUB.setCAWE(this.PXNUM);
      } else {
         HISUB.setCAWE(0d);
      }
      rAPIDS.clear();
      X0OPC.moveLeftPad("*CHK");
      rPSUBPIpreCall();
      apCall("MHISUBPI", rPSUBPI);
      rPSUBPIpostCall();
      IN60 = toBoolean(X0IN60.getChar());
      if (IN60) {
         MICommon.setError( "", X0MSID.toString(), X0MSGD);
         return;
      }
      XXGEDT = movexDate();
      XXGETM = movexTime();
      HISUB.setGEDT(XXGEDT);
      HISUB.setGETM(XXGETM);
      HISUB.setLMDT(XXGEDT);
      HISUB.setCHNO(1);
      HISUB.setCHID().move(DSUSS);  
      if (!HISUB.WRITE_CHK("00")) {    
         //   MSGID=XRE0104 Record already exists  
         MICommon.setError("SUBL", "XRE0104");  
         return;  
      }      
      
   }
 
   public void ChgSubLine() {
      // ----------------------------------------------------------------
      //   Clear transaction records
      // ----------------------------------------------------------------
      sMHS850MIRChgSubLine inChgSubLine = (sMHS850MIRChgSubLine)MICommon.getInDS(sMHS850MIRChgSubLine.class);

      HISUB.clear();
      HILIN.clear();
      
      // ----------------------------------------------------------------
      //   Default process option.
      // ----------------------------------------------------------------
      if (MICommon.toNumericCompany(inChgSubLine.getS2CONO())) {
         XXCONO = MICommon.getInt();
      } else {
         MICommon.setError("CONO");
         return;
      }
      LDAZD.CONO = XXCONO;
      
      // ----------------------------------------------------------------
      //   Retreive original record before testing for new values.
      // ----------------------------------------------------------------     
      HISUB.setCONO(LDAZD.CONO);
      HISUB.setMSGN().move(inChgSubLine.getS2MSGN());
      HISUB.setPACN().move(inChgSubLine.getS2PACN());
      XXNUMA.moveLeft(inChgSubLine.getS2MSLN());
      RNUM();
      HISUB.setMSLN((int)this.PXNUM);       
      XXNUMA.moveLeft(inChgSubLine.getS2SUBL());
      RNUM();
      HISUB.setSUBL((int)this.PXNUM);    
      if (!HISUB.CHAIN("00", HISUB.getKey("00"))) {
         //   MSGID=WSUBL03 Subline number &1 does not exist 
         MICommon.setError("SUBL", "WSUBL03", inChgSubLine.getS2SUBL());   
         return;  
      }          

      // ----------------------------------------------------------------
      //   Retreive HILIN testing for new values.
      // ----------------------------------------------------------------
      HILIN.setCONO(LDAZD.CONO);
      HILIN.setMSGN().move(inChgSubLine.getS2MSGN());
      HILIN.setPACN().move(inChgSubLine.getS2PACN());
      X0FLDD = 5;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inChgSubLine.getS2MSLN());
      RNUM();
      HILIN.setMSLN((int)this.PXNUM);
      rAPIDS.clear();
      X0OPC.moveLeftPad("*GET");
      rPLINPIpreCall();
      apCall("MHILINPI", rPLINPI);
      rPLINPIpostCall();
      IN60 = toBoolean(X0IN60.getChar());
      if (IN60) {
         MICommon.setError( "", X0MSID.toString(), X0MSGD);
         return;
      }
      // ----------------------------------------------------------------
      //   Test for validity before performing transaction.
      // ----------------------------------------------------------------
      if (HILIN.getSTAT().GE("90")) {
         //   MSGID=XST0020 Status change are not permitted any longer
         MICommon.setError( "", "XST0020", HILIN.getSTAT());
         return;
      }
      //   Get product data
      if (inChgSubLine.getS2ITNO().isBlank()) {
         inChgSubLine.setS2ITNO().move(HISUB.getITNO());
      }
      ITMAS.setCONO(LDAZD.CONO);
      ITMAS.setITNO().move(inChgSubLine.getS2ITNO());
      if (!ITMAS.CHAIN("00", ITMAS.getKey("00"))) {
         //   MSGID=WIT0103 Item number &1 does not exist
         MICommon.setError( "", "WIT0103", inChgSubLine.getS2ITNO());
         return;
      }
      XXITNO.moveLeftPad(inChgSubLine.getS2ITNO());
      RITNO();
 
      if (inChgSubLine.getS2ITNO().NE(HILIN.getITNO())) {
         //   MSGID=S_00168  Item number on sublot do not correlate with the message line
         MICommon.setError( "", "S_00168");
         return;
      }
      
      if (inChgSubLine.getS2BANO().isBlank()) {
         inChgSubLine.setS2BANO().move(HISUB.getBANO());
      }
      if (inChgSubLine.getS2BANO().NE(HILIN.getBANO())) {
         //   MSGID=S_00168  Lot number on sublot do not correlate with the message line
         MICommon.setError( "", "S_00168");   
         return;
      }
      if (!inChgSubLine.getS2BANT().isBlank() && inChgSubLine.getS2BANT().NE(HISUB.getBANT())) {
         // Validate that BANT doesn't excists on any other record
         inputMSGN.moveLeftPad(HISUB.getMSGN());
         inputPACN.moveLeftPad(HISUB.getPACN());
         inputMSLN = HISUB.getMSLN();
         inputSUBL = HISUB.getSUBL();
         inputBANT.moveLeftPad(inChgSubLine.getS2BANT());
         validateBANT();
         if (IN60) {
            //MSGID=WBANT04  Reference sublot ID &1 already exists
            MICommon.setError("BANT", "WBANT04", inChgSubLine.getS2BANT());
            return; 
         }
         // ----------------------------------------------------------------
         //   Restore original record after test of BANT.
         // ----------------------------------------------------------------     
         HISUB.setCONO(LDAZD.CONO);
         HISUB.setMSGN().move(inputMSGN);
         HISUB.setPACN().move(inputPACN);
         HISUB.setMSLN(inputMSLN);       
         HISUB.setSUBL(inputSUBL);    
         if (!HISUB.CHAIN("00", HISUB.getKey("00"))) {
            //   MSGID=WSUBL03 Subline number &1 does not exist 
            MICommon.setError("SUBL", "WSUBL03", inChgSubLine.getS2SUBL());   
            return;  
         }
      }
      if (inChgSubLine.getS2QLFR().isBlank()) {
         inChgSubLine.setS2QLFR().move(HISUB.getQLFR());
      }
      if (inChgSubLine.getS2QLFR().NE(HILIN.getQLFR())) {
         //   MSGID=S_00170  Qualifier on sublot do not correlate with the message line
         MICommon.setError( "", "S_00170");
         return;
      }     
      
      if (HISUB.CHAIN_LOCK("00", HISUB.getKey("00"))) {
      
            // Set Values for HISUB
         if (!inChgSubLine.getS2BANO().isBlank()) {
            HISUB.setBANO().move(inChgSubLine.getS2BANO());
         }
         if (!inChgSubLine.getS2QLFR().isBlank()) {
            HISUB.setQLFR().move(inChgSubLine.getS2QLFR());
         } 
         if (!inChgSubLine.getS2BANT().isBlank()) {
            HISUB.setBANT().move(inChgSubLine.getS2BANT());
         } 
         if (!inChgSubLine.getS2ITNO().isBlank()) {
            HISUB.setITNO().move(inChgSubLine.getS2ITNO());
         }       
         if (HISUB.getQLFR().NE("50  ")) {
            if (inChgSubLine.getS2CAWE().isBlank() && ITMAS.getACTI() >= 2) {
               // MSGID=PM06036 Catch weight value must be grater than zero
               MICommon.setError("CAWE", "PM06036");
               return;
            } else {
               XXQTYN = 0d;
               XXQTYA.clear();
               XXQTYA.moveLeft(inChgSubLine.getS2CAWE());
               this.PXDCCD = X2DCCD;
               RQTY();
               HISUB.setCAWE(this.PXNUM);
            }
         } else {
            HISUB.setCAWE(0d);
         } 
         HISUB.UPDAT("00");
      } else {
         HISUB.UNLOCK("00");  
         //   MSGID=XRE0103 Record does not exist  
         MICommon.setError("SUBL", "XRE0103");  
         return;         
      }
      
   }

   
   public void LstSubLine() {
      // ----------------------------------------------------------------
      //   Clear transaction records
      // ----------------------------------------------------------------
      sMHS850MIRLstSubLine inLstSubLine = (sMHS850MIRLstSubLine)MICommon.getInDS(sMHS850MIRLstSubLine.class);
      sMHS850MISLstSubLine outLstSubLine = (sMHS850MISLstSubLine)MICommon.getOutDS(sMHS850MISLstSubLine.class);  
      
      if (MICommon.toNumericCompany(inLstSubLine.getS3CONO())) {
         XXCONO = MICommon.getInt();
      } else {
         MICommon.setError("CONO");
         return;
      }
      LDAZD.CONO = XXCONO;
      
      //   Checks Message 
      if (inLstSubLine.getS3MSGN().isBlank()) {
         //   MSGID=WMS3702 Message Number must be entered
         MICommon.setError("MSGN", "WMS3702");
         return; 
      }

      // Check fields
      // - Company
      HISUB.setCONO(LDAZD.CONO);
      // - Sublot
      HISUB.setMSGN().moveLeftPad(inLstSubLine.getS3MSGN());
      HISUB.setPACN().moveLeftPad(inLstSubLine.getS3PACN());
      HISUB.setMSLN(inLstSubLine.getS3MSLN().getInt());
      HISUB.setITNO().moveLeftPad(inLstSubLine.getS3ITNO());
      HISUB.setBANO().moveLeftPad(inLstSubLine.getS3BANO());
      HISUB.setBANT().moveLeftPad(inLstSubLine.getS3BANT());

      Expression exp = Expression.createEQ("MHISUB", "G4CONO", String.valueOf(HISUB.getCONO()));  
      if (!inLstSubLine.getS3MSGN().isBlank()) {
         exp = exp.AND(Expression.createEQ("MHISUB", "G4MSGN", inLstSubLine.getS3MSGN().toStringRTrim()));  
      }    
      if (!inLstSubLine.getS3PACN().isBlank()) {
         exp = exp.AND(Expression.createEQ("MHISUB", "G4PACN", inLstSubLine.getS3PACN().toStringRTrim()));  
      }       
      if (!inLstSubLine.getS3MSLN().isBlank()) {
         exp = exp.AND(Expression.createEQ("MHISUB", "G4MSLN", inLstSubLine.getS3MSLN().toStringRTrim()));  
      }         
     
      if (!inLstSubLine.getS3ITNO().isBlank()) {
         exp = exp.AND(Expression.createEQ("MHISUB", "G4ITNO", inLstSubLine.getS3ITNO().toStringRTrim()));  
      }      
      
      if (!inLstSubLine.getS3BANO().isBlank()) {
         exp = exp.AND(Expression.createEQ("MHISUB", "G4BANO", inLstSubLine.getS3BANO().toStringRTrim()));  
      }      
      if (!inLstSubLine.getS3BANT().isBlank()) {
         exp = exp.AND(Expression.createEQ("MHISUB", "G4BANT", inLstSubLine.getS3BANT().toStringRTrim()));  
      }     
      
      // List Sublots
      
      FieldSelection fs = new FieldSelection("MHISUB", "00");  
      fs.setExpression(exp);
      HISUB.SETLL_SCAN("00", HISUB.getKey("00", 2));
      int noOfSubLines = 0;

      while (noOfSubLines < MICommon.getMaxRecords() &&
         HISUB.READE("00", HISUB.getKey("00", 2),fs))
      {
         noOfSubLines++;
         outLstSubLine.setL3MSGN().move(HISUB.getMSGN());
         outLstSubLine.setL3PACN().move(HISUB.getPACN());
         outLstSubLine.setL3MSLN().move(HISUB.getMSLN());
         outLstSubLine.setL3SUBL().move(HISUB.getSUBL());
         outLstSubLine.setL3ITNO().move(HISUB.getITNO());
         outLstSubLine.setL3BANO().move(HISUB.getBANO());
         outLstSubLine.setL3CAWE().move(HISUB.getCAWE());
         outLstSubLine.setL3BANT().move(HISUB.getBANT());
         outLstSubLine.setL3GEDT().moveLeftPad(MICommon.toAlphaDate(HISUB.getRGDT()));
         outLstSubLine.setL3GETM().moveLeftPad(MICommon.toAlpha(HISUB.getGETM()));
         outLstSubLine.setL3LMDT().moveLeftPad(MICommon.toAlphaDate(HISUB.getLMDT()));
         
         // ----------------------------------------------------------------
         //   Send data to the client
         // ----------------------------------------------------------------
         MICommon.setData(outLstSubLine.get());
         MICommon.reply();
      }
      //   Empty reply buffer
      MICommon.clearBuffer();
   }
   
    
   public void LstItemSubLine() {
      // ----------------------------------------------------------------
      //   Clear transaction records
      // ----------------------------------------------------------------
      sMHS850MIRLstItemSubLine inLstItemSubLine = (sMHS850MIRLstItemSubLine)MICommon.getInDS(sMHS850MIRLstItemSubLine.class);
      sMHS850MISLstItemSubLine outLstItemSubLine = (sMHS850MISLstItemSubLine)MICommon.getOutDS(sMHS850MISLstItemSubLine.class);  

      if (MICommon.toNumericCompany(inLstItemSubLine.getS4CONO())) {
         XXCONO = MICommon.getInt();
      } else {
         MICommon.setError("CONO");
         return;
      }
      LDAZD.CONO = XXCONO;
      //   Checks Message 
      if (inLstItemSubLine.getS4MSGN().isBlank()) {
         //   MSGID=WMS3702 Message Number must be entered
         MICommon.setError("MSGN", "WMS3702");
         return; 
      }   
      
      // Check fields
      // - Company
      HISUB.setCONO(LDAZD.CONO);
      // - Sublot
      HISUB.setMSGN().moveLeftPad(inLstItemSubLine.getS4MSGN());   
  
      Expression exp = Expression.createEQ("MHISUB", "G4CONO", String.valueOf(HISUB.getCONO()));  
      if (!inLstItemSubLine.getS4MSGN().isBlank()) {
         exp = exp.AND(Expression.createEQ("MHISUB", "G4MSGN", inLstItemSubLine.getS4MSGN().toStringRTrim()));  
      }     
      if (!inLstItemSubLine.getS4ITNO().isBlank()) {
         exp = exp.AND(Expression.createEQ("MHISUB", "G4ITNO", inLstItemSubLine.getS4ITNO().toStringRTrim()));  
      }      
      
      if (!inLstItemSubLine.getS4BANO().isBlank()) {
         exp = exp.AND(Expression.createEQ("MHISUB", "G4BANO", inLstItemSubLine.getS4BANO().toStringRTrim()));  
      }      
      if (!inLstItemSubLine.getS4PACN().isBlank()) {
         exp = exp.AND(Expression.createEQ("MHISUB", "G4PACN", inLstItemSubLine.getS4PACN().toStringRTrim()));  
      }     
      if (!inLstItemSubLine.getS4MSLN().isBlank()) {
         exp = exp.AND(Expression.createEQ("MHISUB", "G4MSLN", inLstItemSubLine.getS4MSLN().toStringRTrim()));  
      }      

      
      FieldSelection fs = new FieldSelection("MHISUB", "10");  
      fs.setExpression(exp);
      HISUB.SETLL_SCAN("10", HISUB.getKey("10", 2));
      int noOfSubLines = 0;
      
      while (noOfSubLines < MICommon.getMaxRecords() &&
         HISUB.READE("10", HISUB.getKey("10", 2),fs))
      {
         noOfSubLines++;
         outLstItemSubLine.setL4MSGN().move(HISUB.getMSGN());
         outLstItemSubLine.setL4PACN().move(HISUB.getPACN());
         outLstItemSubLine.setL4MSLN().move(HISUB.getMSLN());
         outLstItemSubLine.setL4SUBL().move(HISUB.getSUBL());
         outLstItemSubLine.setL4ITNO().move(HISUB.getITNO());
         outLstItemSubLine.setL4BANO().move(HISUB.getBANO());
         outLstItemSubLine.setL4CAWE().move(HISUB.getCAWE());
         outLstItemSubLine.setL4BANT().move(HISUB.getBANT());
         outLstItemSubLine.setL4GEDT().moveLeftPad(MICommon.toAlphaDate(HISUB.getRGDT()));
         outLstItemSubLine.setL4GETM().moveLeftPad(MICommon.toAlpha(HISUB.getGETM()));
         outLstItemSubLine.setL4LMDT().moveLeftPad(MICommon.toAlphaDate(HISUB.getLMDT()));
         // ----------------------------------------------------------------
         //   Send data to the client
         // ----------------------------------------------------------------
         MICommon.setData(outLstItemSubLine.get());
         MICommon.reply();         
      }     
      //   Empty reply buffer
      MICommon.clearBuffer();      
   }  
   
   public void DeleteSubLine() {
      // ----------------------------------------------------------------
      //   Clear transaction records
      // ----------------------------------------------------------------
      sMHS850MIRDeleteSubLine inDeleteSubLine = (sMHS850MIRDeleteSubLine)MICommon.getInDS(sMHS850MIRDeleteSubLine.class);

      if (MICommon.toNumericCompany(inDeleteSubLine.getS5CONO())) {
         XXCONO = MICommon.getInt();
      } else {
         MICommon.setError("CONO");
         return;
      }
      LDAZD.CONO = XXCONO;
      //   Checks Message 
      if (inDeleteSubLine.getS5MSGN().isBlank()) {
         //   MSGID=WMS3702 Message Number must be entered
         MICommon.setError("MSGN", "WMS3702");
         return; 
      }   
      if (inDeleteSubLine.getS5PACN().isBlank()) {
         //   MSGID=WGR0602 Package number must be entered
         MICommon.setError("PACN", "WGR0602");
         return; 
      } 
      
      
      // Check fields
      // - Company
      HISUB.setCONO(LDAZD.CONO);
      // - Sublot
      HISUB.setMSGN().moveLeftPad(inDeleteSubLine.getS5MSGN());
      HISUB.setPACN().moveLeftPad(inDeleteSubLine.getS5PACN());  

      Expression exp = Expression.createEQ("MHISUB", "G4CONO", String.valueOf(HISUB.getCONO()));  
      
      if (!inDeleteSubLine.getS5MSGN().isBlank()) {
         exp = exp.AND(Expression.createEQ("MHISUB", "G4MSGN", inDeleteSubLine.getS5MSGN().toStringRTrim()));  
      } 
      if (!inDeleteSubLine.getS5PACN().isBlank()) {
         exp = exp.AND(Expression.createEQ("MHISUB", "G4PACN", inDeleteSubLine.getS5PACN().toStringRTrim()));  
      }    
      if (!inDeleteSubLine.getS5MSLN().isBlank()) {
         exp = exp.AND(Expression.createEQ("MHISUB", "G4MSLN", inDeleteSubLine.getS5MSLN().toStringRTrim()));  
      } 
      if (!inDeleteSubLine.getS5SUBL().isBlank()) {
         exp = exp.AND(Expression.createEQ("MHISUB", "G4SUBL", inDeleteSubLine.getS5SUBL().toStringRTrim())); 
      }
      if (!inDeleteSubLine.getS5ITNO().isBlank()) {
         exp = exp.AND(Expression.createEQ("MHISUB", "G4ITNO", inDeleteSubLine.getS5ITNO().toStringRTrim()));  
      }            
      if (!inDeleteSubLine.getS5BANO().isBlank()) {
         exp = exp.AND(Expression.createEQ("MHISUB", "G4BANO", inDeleteSubLine.getS5BANO().toStringRTrim()));  
      }
      
      FieldSelection fs = new FieldSelection("MHISUB", "00");  
      fs.setExpression(exp);
      HISUB.SETLL_SCAN("00", HISUB.getKey("00", 3));
      int noOfSubLines = 0;
      
      while (noOfSubLines < MICommon.getMaxRecords() &&
         HISUB.READE("00", HISUB.getKey("00", 3),fs))
      {
         noOfSubLines++;
         HISUB.DELET("00", HISUB.getKey("00"));
      }     

      //   Empty reply buffer
      MICommon.clearBuffer();
      
   }
   
   /**
    * Validate that the reference sub ID (BANT) entered is unique on the item/lot number on the message number
    */
    public void validateBANT() {
       // Declaration
       boolean found_MHISUB = false;
       int xCount = 0;
       // Check if sublots already exist for this MSGN/PACN/MSLN
       HISUB.setCONO(XXCONO);
       HISUB.setMSGN().move(inputMSGN);
       HISUB.setPACN().move(inputPACN);
       HISUB.setMSLN(inputMSLN);
       HISUB.SETLL("00", HISUB.getKey("00",4));
       while (HISUB.READE("00", HISUB.getKey("00",4))){
          if (HISUB.getBANT().EQ(inputBANT)) {
             IN60 = true;
          }
       }
    }   

   public void addPOPutaway() {
      // ----------------------------------------------------------------
      //   Clear transaction records
      // ----------------------------------------------------------------
      sMHS850MIRAddPOPutaway inAddPOPutaway = (sMHS850MIRAddPOPutaway)MICommon.getInDS(sMHS850MIRAddPOPutaway.class);
      sMHS850MISAddPOPutaway outAddPOPutaway = (sMHS850MISAddPOPutaway)MICommon.getOutDS(sMHS850MISAddPOPutaway.class);

      HIHED.clear();
      HIPAC.clear();
      HILIN.clear();
      HISUB.clear();
      // ----------------------------------------------------------------
      //   Save process option.
      // ----------------------------------------------------------------
      XXPRFL.move(inAddPOPutaway.getQ0PRFL());
      XXOPC.move("*ADD");
      if (MICommon.toNumericCompany(inAddPOPutaway.getQ0CONO())) {
         XXCONO = MICommon.getInt();
      } else {
         MICommon.setError("CONO");
         return;
      }

      LDAZD.CONO = XXCONO;
    //-----------------------------------
      //    UTC mode
      UTCmode = isUTCmode(inAddPOPutaway.getQ0UTCM());
      if (IN60) {
         MICommon.setError("UTCM", "WUTCM01", inAddPOPutaway.getQ0UTCM());
         return;
      }
      //-----------------------------------
      // Message type
      XXE065.moveLeftPad(inAddPOPutaway.getQ0E065());
      RE065();
      if (IN60) {
         return;
      }
      // Check Warehouse
      XXWHLO.moveLeftPad(inAddPOPutaway.getQ0WHLO());
      RWHL();
      if (IN60) {
         return;
      }
      if (UTCmode) {
         //   Determine time zone for Warehouse
         if (!CRCalendar.retrieveTIZOForWHLO(PXMNGTZN, LDAZD.CONO, XXWHLO)) {
            IN60 = true;
            //   MSGID=S_00148 UTC time conversion failed.
            MICommon.setError("GEDT","S_00148");
            return;
         }
         //   time zone for warehouse
         TIZOforWHLO.move(CRCalendar.getTimeZone());
      }
      //-----------------------------------
      //   Retreive message number if blank
      if (inAddPOPutaway.getQ0MSGN().isBlank()) {
         RVMSNR();
         inAddPOPutaway.setQ0MSGN().moveLeft(PXRTVNBR.PXNBNR, 10);
      } else {
         HIHED.setCONO(LDAZD.CONO);
         HIHED.setMSGN().move(inAddPOPutaway.getQ0MSGN());
         rAPIDS.clear();
         X0OPC.moveLeftPad("*GET");
         rPHEDPIpreCall();
         apCall("MHIHEDPI", rPHEDPI);
         rPHEDPIpostCall();
         IN60 = toBoolean(X0IN60.getChar());
      }
      //   Default package number if blank
      if (inAddPOPutaway.getQ0PACN().isBlank()) {
         inAddPOPutaway.setQ0PACN().moveLeft(inAddPOPutaway.getQ0MSGN());
      }
      HIPAC.setCONO(LDAZD.CONO);
      HIPAC.setMSGN().move(inAddPOPutaway.getQ0MSGN());
      HIPAC.setPACN().move(inAddPOPutaway.getQ0PACN());
      rAPIDS.clear();
      X0OPC.moveLeftPad("*GET");
      rPPACPIpreCall();
      apCall("MHIPACPI", rPPACPI);
      rPPACPIpostCall();
      toBoolean(X0IN60.getChar());
      HILIN.setCONO(LDAZD.CONO);
      HILIN.setMSGN().move(inAddPOPutaway.getQ0MSGN());
      HILIN.setPACN().move(inAddPOPutaway.getQ0PACN());
      HILIN.setMSLN(0);
      HISUB.setMSLN(1);
      //   Retreive facility for warehouse
      ITWHL.setCONO(LDAZD.CONO);
      ITWHL.setWHLO().move(inAddPOPutaway.getQ0WHLO());
      if (!ITWHL.CHAIN("00", ITWHL.getKey("00"))) {
         //   MSGID=WWH0103 Warehouse &1 does not exist
         MICommon.setError( "WHLO", "WWH0103", inAddPOPutaway.getQ0WHLO());
         return;
      } else {
         HIHED.setFACI().move(ITWHL.getFACI());
      }
      //   Format date/time
      if (inAddPOPutaway.getQ0GEDT().isBlank() ||
            inAddPOPutaway.getQ0GETM().isBlank()) {
         XXGEDT = movexDate();
         XXGETM = movexTime();
      } else {
         if (!MICommon.toNumericDate(inAddPOPutaway.getQ0GEDT())) {
            MICommon.setError("GEDT");
            return;
         }
         XXGEDT = MICommon.getNumericDate();

         if (!MICommon.toNumeric(inAddPOPutaway.getQ0GETM())) {
            MICommon.setError("GETM");
            return;
         }
         XXGETM = MICommon.getInt();
         // Date generated
         date = XXGEDT; 
         time = XXGETM / 100;
         if (UTCmode && date > 0) {
            if (!convertFromUTC()) {
               IN60 = true;
               //   MSGID=S_00148 UTC time conversion failed.
               MICommon.setError("GEDT","S_00148");
               return;
            }
            XXGEDT = UTCdate;
            XXGETM = UTCtime * 100;
         }
      }
      // ----------------------------------------------------------------
      //   Move data from the transaction input structure (MHS850MI1)
      //   to the execution data formats (MHIHED/MHIPAC/MHILIN)
      // ----------------------------------------------------------------
      HIHED.setCONO(LDAZD.CONO);
      HIPAC.setCONO(LDAZD.CONO);
      HILIN.setCONO(LDAZD.CONO);
      HISUB.setCONO(LDAZD.CONO);
      HIHED.setQLFR().move("22  ");
      HIPAC.setQLFR().move("22  ");
      HILIN.setQLFR().move("22  ");
      HISUB.setQLFR().move("22  ");
      //---------------------------------------
      //---------------------------------------
      HILIN.setTTYP(25);
      HIHED.setE0IO('I');
      HIHED.setWHLO().move(inAddPOPutaway.getQ0WHLO());
      HIPAC.setWHLO().move(inAddPOPutaway.getQ0WHLO());
      HILIN.setWHLO().move(inAddPOPutaway.getQ0WHLO());
      HIHED.setMSGN().move(inAddPOPutaway.getQ0MSGN());
      HIPAC.setMSGN().move(inAddPOPutaway.getQ0MSGN());
      HILIN.setMSGN().move(inAddPOPutaway.getQ0MSGN());
      HISUB.setMSGN().move(inAddPOPutaway.getQ0MSGN());
      HIHED.setPMSN().moveLeft(inAddPOPutaway.getQ0MSGN());
      HIPAC.setPACN().move(inAddPOPutaway.getQ0PACN());
      HILIN.setPACN().move(inAddPOPutaway.getQ0PACN());
      HISUB.setPACN().move(inAddPOPutaway.getQ0PACN());
      HIHED.setGEDT(XXGEDT);
      HIPAC.setGEDT(XXGEDT);
      HILIN.setGEDT(XXGEDT);
      HIHED.setGETM(XXGETM);
      HIPAC.setGETM(XXGETM);
      HILIN.setGETM(XXGETM);
      HIHED.setE0PA().move(inAddPOPutaway.getQ0E0PA());
      HIHED.setE0PB().move(inAddPOPutaway.getQ0E0PA());
      HIHED.setE035(0);
      HIHED.setE065().move(inAddPOPutaway.getQ0E065());
      HIHED.setSUNO().move(inAddPOPutaway.getQ0SUNO());
      X0FLDD = 1;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddPOPutaway.getQ0SUTY().getChar());
      RNUM();
      HIHED.setSUTY((int)this.PXNUM);
      HIHED.setADID().move(inAddPOPutaway.getQ0ADID());
      HILIN.setITNO().move(inAddPOPutaway.getQ0ITNO());
      HISUB.setITNO().move(inAddPOPutaway.getQ0ITNO());
      XXITNO.moveLeftPad(HILIN.setITNO());
      RITNO();
      //--------------------------------
      HILIN.setPOPN().move(inAddPOPutaway.getQ0POPN());
      HILIN.setALWQ().move(inAddPOPutaway.getQ0ALWQ());
      X0FLDD = 2;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddPOPutaway.getQ0ALWT());
      RNUM();
      HILIN.setALWT((int)this.PXNUM);
      HILIN.setWHSL().move(inAddPOPutaway.getQ0WHSL());
      HILIN.setBANO().move(inAddPOPutaway.getQ0BANO());
      HISUB.setBANO().move(inAddPOPutaway.getQ0BANO());
      HILIN.setCAMU().move(inAddPOPutaway.getQ0CAMU());
      HILIN.setRIDN().move(inAddPOPutaway.getQ0RIDN());
      X0FLDD = 6;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddPOPutaway.getQ0RIDL());
      RNUM();
      HILIN.setRIDL((int)this.PXNUM);
      PLINE.setCONO(LDAZD.CONO);
      PLINE.setPUNO().moveLeft(HILIN.getRIDN());
      PLINE.setPNLI(HILIN.getRIDL());
      if (PLINE.CHAIN("00", PLINE.getKey("00", 3))) {
         if (ITMAS.getUNMS().NE(PLINE.getPUUN())) {
            ITAUN.setCONO(HILIN.getCONO());
            ITAUN.setITNO().move(HILIN.getITNO());
            ITAUN.setAUTP(1);
            ITAUN.setALUN().move(PLINE.getPUUN());
            if (ITAUN.CHAIN("00", ITAUN.getKey("00"))) {
               X1DCCD = ITAUN.getDCCD();
            }
         }
      }
      X0FLDD = 10;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddPOPutaway.getQ0REPN());
      RNUM();
      HILIN.setREPN((long)this.PXNUM);
      XXQTYN = 0d;
      XXQTYA.clear();
      XXQTYA.moveLeft(inAddPOPutaway.getQ0QTY());
      this.PXDCCD = X1DCCD;
      RQTY();
      HILIN.setRVQA(this.PXNUM);
      X0FLDD = 3;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddPOPutaway.getQ0RIDX());
      RNUM();
      HILIN.setRIDX((int)this.PXNUM);
      HILIN.setUSD1().move(inAddPOPutaway.getQ0USD1());
      HILIN.setUSD2().move(inAddPOPutaway.getQ0USD2());
      HILIN.setUSD3().move(inAddPOPutaway.getQ0USD3());
      HILIN.setUSD4().move(inAddPOPutaway.getQ0USD4());
      HILIN.setUSD5().move(inAddPOPutaway.getQ0USD5());
      HILIN.setBREF().move(inAddPOPutaway.getQ0BREF());
      HILIN.setBRE2().move(inAddPOPutaway.getQ0BRE2());
      XXQTYN = 0d;
      XXQTYA.clear();
      XXQTYA.moveLeft(inAddPOPutaway.getQ0CAWE());
      this.PXDCCD = X2DCCD;
      RQTY();
      HILIN.setCAWE(this.PXNUM);
      HISUB.setCAWE(this.PXNUM);
      if (ITMAS.getSUME() == 1 && HILIN.getCAWE() !=0d) {
         IN60 = true;
         //   MSGID=MH85015 CW Not allowed when Sublot controlled
         MICommon.setError( "", "MH85015");
         return;
      } else {
         if (ITMAS.getSUME() == 1 && HILIN.getCAWE() ==0d) {
            ITAUN.setCONO(HILIN.getCONO());
            ITAUN.setITNO().move(HILIN.getITNO());
            ITAUN.setAUTP(1);
            ITAUN.setALUN().move(ITMAS.getCWUN());
            if (ITAUN.CHAIN("00", ITAUN.getKey("00"))) {
               HILIN.setCAWE(ITAUN.getCOFA() * HILIN.getRVQA());
               HISUB.setCAWE(ITAUN.getCOFA());
            }
         }
      }
      X0FLDD = 1;
      XXNUMN = 0d;
      XXNUMA.clear();
      XXNUMA.moveLeft(inAddPOPutaway.getQ0OEND().getChar());
      RNUM();
      HILIN.setOEND((int)this.PXNUM);
      HIHED.setE0IO('I');
      if (!inAddPOPutaway.getQ0PMSN().isBlank()) {
         HIHED.setPMSN().move(inAddPOPutaway.getQ0PMSN());
      } else {
         HIHED.setPMSN().moveLeft(inAddPOPutaway.getQ0MSGN());
      }

      if (!MICommon.toNumericDate(inAddPOPutaway.getQ0EXPI())) {
         MICommon.setError("EXPI");
         return;
      }
      HILIN.setEXPI(MICommon.getNumericDate());
      HIHED.setRESP().move(inAddPOPutaway.getQ0RESP());
      HILIN.setRESP().move(inAddPOPutaway.getQ0RESP());
      HIPAC.setRESP().move(inAddPOPutaway.getQ0RESP());
      //----------------------------------------------------
      //   Validate/create transaction header data
      //----------------------------------------------------
      if (IN75) {
         IN60 = true;
         return;
      }
      //----------------------------------------------------
      RHEAD();
      if (IN60 && IN76) {
         return;
      }
      RPACK();
      if (IN60 && IN76) {
         return;
      }
      //   Validate/create transaction line data
      RLINE();
      if (IN60) {
         return;
      }
      if (ITMAS.getSUME() == 1) {
         if (ITBAL.getCOMG() == 7) {            
            XXERRM = true;
            IN60 = true;
            // MSGID=PP30079 Reciept of sublots are not allowed for container mgt = 7
            MICommon.setError( "", "PP30079");
            X1OPC.moveLeft("534");
            return;
         } else {
            if (!XXPRFL.isBlank()) {
               X1OPC.clear();
               X0OPC.moveLeftPad("*VALIDATE");
               RSUB();         
               if (IN60) {
                  return;
               }
            }
         }
      }
      outAddPOPutaway.setY0CONO().move(XXCONO);
      outAddPOPutaway.setY0MSGN().move(HIHED.getMSGN());
      outAddPOPutaway.setY0MSLN().move(HILIN.getMSLN());

      MICommon.setData( outAddPOPutaway.get());
   }

   public void addPickViaSublot() {
      // ----------------------------------------------------------------
      //   Clear transaction records
      // ----------------------------------------------------------------
      sMHS850MIRAddPickViaSblot inAddPickViaSblot = (sMHS850MIRAddPickViaSblot)MICommon.getInDS(sMHS850MIRAddPickViaSblot.class);
      sMHS850MISAddPickViaSblot outAddPickViaSblot = (sMHS850MISAddPickViaSblot)MICommon.getOutDS(sMHS850MISAddPickViaSblot.class);

      HIHED.clear();
      HIPAC.clear();
      HILIN.clear();
      HISUB.clear();
   
      headerFound = false;
      packFound = false;
      
      // ----------------------------------------------------------------
      //   Save process option.
      // ----------------------------------------------------------------
      XXPRFL.move(inAddPickViaSblot.getISPRMD());
      XXOPC.move("*ADD");
      if (MICommon.toNumericCompany(inAddPickViaSblot.getISCONO())) {
         XXCONO = MICommon.getInt();
      } else {
         MICommon.setError("CONO");
         return;
      }

      LDAZD.CONO = XXCONO;
    //-----------------------------------
      //    UTC mode
      UTCmode = isUTCmode(inAddPickViaSblot.getISUTCM());
      if (IN60) {
         MICommon.setError("UTCM", "WUTCM01", inAddPickViaSblot.getISUTCM());
         return;
      }
      //-----------------------------------
      // Message type
      XXE065.moveLeftPad(inAddPickViaSblot.getISE065());
      RE065();
      if (IN60) {
         return;
      }
      // Check Warehouse
      XXWHLO.moveLeftPad(inAddPickViaSblot.getISWHLO());
      RWHL();
      if (IN60) {
      return;
      }
      if (UTCmode) {
         //   Determine time zone for Warehouse
         if (!CRCalendar.retrieveTIZOForWHLO(PXMNGTZN, LDAZD.CONO, XXWHLO)) {
            IN60 = true;
            //   MSGID=S_00148 UTC time conversion failed.
            MICommon.setError("GEDT","S_00148");
            return;
         }
         //   time zone for warehouse
         TIZOforWHLO.move(CRCalendar.getTimeZone());
      }
      //-----------------------------------
      //   Retreive message number if blank
      if (inAddPickViaSblot.getISMSGN().isBlank()) {
         RVMSNR();
         inAddPickViaSblot.setISMSGN().moveLeft(PXRTVNBR.PXNBNR, 10);
      } else {
         HIHED.setCONO(LDAZD.CONO);
         HIHED.setMSGN().move(inAddPickViaSblot.getISMSGN());
         rAPIDS.clear();
         X0OPC.moveLeftPad("*GET");
         rPHEDPIpreCall();
         apCall("MHIHEDPI", rPHEDPI);
         rPHEDPIpostCall();
         IN60 = toBoolean(X0IN60.getChar());
         //  Header found - check for update mode
         if (!IN60) {
            //  Test for validity before performing transaction.
            if (HIHED.getSTAT().GE("90")) {
               //   MSGID=XST0020 Status change are not permitted any longer
               MICommon.setError( "", "XST0020", HIHED.getSTAT());
               return;
            }
            headerFound = true;
         }
         IN60 = false;
      }
      //   Default package number if blank
      if (inAddPickViaSblot.getISPACN().isBlank()) {
         inAddPickViaSblot.setISPACN().moveLeft(inAddPickViaSblot.getISMSGN());
      }
      HIPAC.setCONO(LDAZD.CONO);
      HIPAC.setMSGN().move(inAddPickViaSblot.getISMSGN());
      HIPAC.setPACN().move(inAddPickViaSblot.getISPACN());
      rAPIDS.clear();
      X0OPC.moveLeftPad("*GET");
      rPPACPIpreCall();
      apCall("MHIPACPI", rPPACPI);
      rPPACPIpostCall();
      IN60 = toBoolean(X0IN60.getChar());
      //  Package found - check for update mode
      if (!IN60) {
         packFound = true;
      }
      IN60 = false;
      if (headerFound && packFound) {
         HILIN.setCONO(LDAZD.CONO);
         HILIN.setMSGN().move(inAddPickViaSblot.getISMSGN());
         HILIN.setPACN().move(inAddPickViaSblot.getISPACN());
         X0FLDD = 5;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inAddPickViaSblot.getISMSLN());
         RNUM();
         HILIN.setMSLN((int)this.PXNUM);
         rAPIDS.clear();
         X0OPC.moveLeftPad("*GET");
         rPLINPIpreCall();
         apCall("MHILINPI", rPLINPI);
         rPLINPIpostCall();
         IN60 = toBoolean(X0IN60.getChar());
         //  Line found - switch to update mode to enable addition of further sublots
         if (!IN60) {
            XXOPC.move("*UPD");
         }
      }
      //  ADD MODE
      if (XXOPC.EQ("*ADD")) { 
         //   Retreive next line in sequence
         HILIN.setCONO(LDAZD.CONO);
         HILIN.setMSGN().move(inAddPickViaSblot.getISMSGN());
         HILIN.setPACN().move(inAddPickViaSblot.getISPACN());
         HILIN.setMSLN(0);
         HISUB.setMSLN(1);
         //   Retreive facility for warehouse
         ITWHL.setCONO(LDAZD.CONO);
         ITWHL.setWHLO().move(inAddPickViaSblot.getISWHLO());
         if (!ITWHL.CHAIN("00", ITWHL.getKey("00"))) {
            //   MSGID=WWH0103 Warehouse &1 does not exist
            MICommon.setError( "", "WWH0103", inAddPickViaSblot.getISWHLO());
            return;
         } else {
            HIHED.setFACI().move(ITWHL.getFACI());
         }
         //   Format date/time
         if (inAddPickViaSblot.getISGEDT().isBlank() ||
            inAddPickViaSblot.getISGETM().isBlank()) {
            XXGEDT = movexDate();
            XXGETM = movexTime();
         } else {
            if (MICommon.toNumericDate(inAddPickViaSblot.getISGEDT())) {
               XXGEDT = MICommon.getNumericDate();
            } else {
               MICommon.setError("GEDT");
               return;
            }
            if (!MICommon.toNumeric(inAddPickViaSblot.getISGETM())) {
               MICommon.setError("GETM");
               return;
            }
            XXGETM = MICommon.getInt();
            // Date generated
            date = XXGEDT; 
            time = XXGETM / 100;
            if (UTCmode && date > 0) {
               if (!convertFromUTC()) {
                  IN60 = true;
                  //   MSGID=S_00148 UTC time conversion failed.
                  MICommon.setError("GEDT","S_00148");
                  return;
               }
               XXGEDT = UTCdate;
               XXGETM = UTCtime * 100;
            }
         }
         // ----------------------------------------------------------------
         //   Move data from the transaction input structure (MHS850MI1)   >
         //   to the execution data formats (MHIHED/MHIPAC/MHILIN)         >
         // ----------------------------------------------------------------
         HIHED.setCONO(LDAZD.CONO);
         HIPAC.setCONO(LDAZD.CONO);
         HILIN.setCONO(LDAZD.CONO);
         HISUB.setCONO(LDAZD.CONO);
         HIHED.setQLFR().move("SUBL");
         HIPAC.setQLFR().move("SUBL");
         HIHED.setE0IO('I');
         HILIN.setQLFR().move("SUBL");
         HISUB.setQLFR().move("SUBL");
         //---------------------------------------
         HIHED.setWHLO().move(inAddPickViaSblot.getISWHLO());
         HIPAC.setWHLO().move(inAddPickViaSblot.getISWHLO());
         HILIN.setWHLO().move(inAddPickViaSblot.getISWHLO());
         HIHED.setMSGN().move(inAddPickViaSblot.getISMSGN());
         HIPAC.setMSGN().move(inAddPickViaSblot.getISMSGN());
         HILIN.setMSGN().move(inAddPickViaSblot.getISMSGN());
         HISUB.setMSGN().move(inAddPickViaSblot.getISMSGN());
         HIHED.setPMSN().moveLeft(inAddPickViaSblot.getISMSGN());
         HIPAC.setPACN().move(inAddPickViaSblot.getISPACN());
         HILIN.setPACN().move(inAddPickViaSblot.getISPACN());
         HISUB.setPACN().move(inAddPickViaSblot.getISPACN());
         HIPAC.setPACT().move(inAddPickViaSblot.getISPACT());
         HIHED.setGEDT(XXGEDT);
         HIPAC.setGEDT(XXGEDT);
         HILIN.setGEDT(XXGEDT);
         HISUB.setGEDT(XXGEDT);
         HIHED.setGETM(XXGETM);
         HIPAC.setGETM(XXGETM);
         HILIN.setGETM(XXGETM);
         HISUB.setGETM(XXGETM);
         HIHED.setE0PA().move(inAddPickViaSblot.getISE0PA());
         HIHED.setE0PB().move(inAddPickViaSblot.getISE0PA());
         HIHED.setE035(0);
         HIHED.setE065().move(inAddPickViaSblot.getISE065());
         X0FLDD = 1;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inAddPickViaSblot.getISOEND().getChar());
         RNUM();
         HILIN.setOEND((int)this.PXNUM);
         HILIN.setTWSL().move(inAddPickViaSblot.getISTWSL());
         HILIN.setWHSL().move(inAddPickViaSblot.getISWHSL());
         HILIN.setBANO().move(inAddPickViaSblot.getISBANO());
         HILIN.setCAMU().move(inAddPickViaSblot.getISCAMU());
         X0FLDD = 11;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inAddPickViaSblot.getISPLRN());
         RNUM();
         HILIN.setPLRN((long)this.PXNUM);
         ITALO.setCONO(HILIN.getCONO());
         ITALO.setPLRN(HILIN.getPLRN());
         if (ITALO.CHAIN("50", ITALO.getKey("50", 2))) {
            HILIN.setITNO().move(ITALO.getITNO());
            HILIN.setWHSL().move(ITALO.getWHSL());
            HILIN.setBANO().move(ITALO.getBANO());
            HILIN.setCAMU().move(ITALO.getCAMU());
            HISUB.setBANO().move(ITALO.getBANO());
            HISUB.setITNO().move(ITALO.getITNO());
            ITMAS.setCONO(HISUB.getCONO());
            ITMAS.setITNO().move(HISUB.getITNO());
            ITMAS.CHAIN("00", ITMAS.getKey("00"));
            //  Transfer soft to hard
            if (ITALO.getSOFT() == 1) {
               HILIN.setWHSL().move(inAddPickViaSblot.getISWHSL());
               HILIN.setBANO().move(inAddPickViaSblot.getISBANO());
               HISUB.setBANO().move(inAddPickViaSblot.getISBANO());
               HILIN.setCAMU().move(inAddPickViaSblot.getISCAMU());
            }
         }
         HISUB.setBANT().move(inAddPickViaSblot.getISBANT());
         if (!inAddPickViaSblot.getISRPDT().isBlank()) {
            if (!MICommon.toNumericDate(inAddPickViaSblot.getISRPDT())) {
               MICommon.setError("RPDT");
               return;
            }
            HILIN.setRPDT(MICommon.getNumericDate());
         }
         //--------------------------------------------------------------------
         if (!inAddPickViaSblot.getISRPTM().isBlank()) {
            X0FLDD = 6;
            XXNUMN = 0d;
            XXNUMA.clear();
            XXNUMA.moveLeft(inAddPickViaSblot.getISRPTM());
            RNUM();
            HILIN.setRPTM((int)this.PXNUM);
         }
         if (!UTCmode && 
            !inAddPickViaSblot.getISRPDT().isBlank() &&
            inAddPickViaSblot.getISRPTM().isBlank()) {
            HILIN.setRPTM(movexTime());
         }
         if (UTCmode) {
            // Reporting date
            if (!inAddPickViaSblot.getISRPDT().isBlank() &&
                !inAddPickViaSblot.getISRPTM().isBlank()) {
               date = HILIN.getRPDT(); 
               time = HILIN.getRPTM() / 100;           
               if (!convertFromUTC()) {
                  IN60 = true;
                  //   MSGID=S_00148 UTC time conversion failed.
                  MICommon.setError("RPDT","S_00148");
                  return;
               }
               HILIN.setRPDT(UTCdate);
               HILIN.setRPTM(UTCtime * 100);
            } else {
               if (inAddPickViaSblot.getISRPDT().isBlank() ||
                  inAddPickViaSblot.getISRPTM().isBlank()) {
                  IN60 = true;
                  //   MSGID=S_00148 UTC time conversion failed.
                  MICommon.setError("RPDT","S_00148");
                  return;
               }
            }
         }
         //--------------------------------------------------------------------
         HILIN.setUSD1().move(inAddPickViaSblot.getISUSD1());
         HILIN.setUSD2().move(inAddPickViaSblot.getISUSD2());
         HILIN.setUSD3().move(inAddPickViaSblot.getISUSD3());
         HILIN.setUSD4().move(inAddPickViaSblot.getISUSD4());
         HILIN.setUSD5().move(inAddPickViaSblot.getISUSD5());
         HILIN.setBREF().move(inAddPickViaSblot.getISBREF());
         HILIN.setBRE2().move(inAddPickViaSblot.getISBRE2());
         if (!inAddPickViaSblot.getISPMSN().isBlank()) {
            HIHED.setPMSN().move(inAddPickViaSblot.getISPMSN());
         } else {
            HIHED.setPMSN().moveLeft(inAddPickViaSblot.getISMSGN());
         }
         X0FLDD = 1;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inAddPickViaSblot.getISISMD().getChar());
         RNUM();
         HILIN.setISMD((int)this.PXNUM);
         //-----------------------------
         //   Write subline record if reference ID passed
         writeSubline = false;
         if (!HISUB.getBANT().isBlank()) {
            writeSubline = true;
         }
         //  UPDATE MODE
      } else {
         //   Format date/time
         if (!inAddPickViaSblot.getISGEDT().isBlank() &&
               !inAddPickViaSblot.getISGETM().isBlank()) {
            if (MICommon.toNumericDate(inAddPickViaSblot.getISGEDT())) {
               XXGEDT = MICommon.getNumericDate();
            } else {
               MICommon.setError("GEDT");
               return;
            }
            if (!MICommon.toNumeric(inAddPickViaSblot.getISGETM())) {
               MICommon.setError("GETM");
               return;
            }
            XXGETM = MICommon.getInt();
            HIHED.setGEDT(XXGEDT);
            HIPAC.setGEDT(XXGEDT);
            HILIN.setGEDT(XXGEDT);
            HIHED.setGETM(XXGETM);
            HIPAC.setGETM(XXGETM);
            HILIN.setGETM(XXGETM);
         }
         // ----------------------------------------------------------------
         //   Move data from the transaction input structure (MHS850MI1)   >
         //   to the execution data formats (MHIHED/MHIPAC/MHILIN)         >
         // ----------------------------------------------------------------
         HISUB.setCONO(LDAZD.CONO);
         HISUB.setQLFR().move("SUBL");
         //---------------------------------------
         HISUB.setMSGN().move(HILIN.getMSGN());
         HISUB.setPACN().move(HILIN.getPACN());
         HISUB.setMSLN(HILIN.getMSLN());
         HISUB.setGEDT(HIHED.getGEDT());
         HISUB.setGETM(HIHED.getGETM());
         if (!inAddPickViaSblot.getISE0PA().isBlank()) {
            HIHED.setE0PA().move(inAddPickViaSblot.getISE0PA());
            HIHED.setE0PB().move(inAddPickViaSblot.getISE0PA());
         }
         if (!inAddPickViaSblot.getISE065().isBlank()) {
            HIHED.setE065().move(inAddPickViaSblot.getISE065());
         }
         if (inAddPickViaSblot.getISOEND().getChar() != ' ') {
            X0FLDD = 1;
            XXNUMN = 0d;
            XXNUMA.clear();
            XXNUMA.moveLeft(inAddPickViaSblot.getISOEND().getChar());
            RNUM();
            HILIN.setOEND((int)this.PXNUM);
         }
         if (!inAddPickViaSblot.getISTWSL().isBlank()) {
            HILIN.setTWSL().move(inAddPickViaSblot.getISTWSL());
         }
         //  Transfer soft to hard
         if (!inAddPickViaSblot.getISWHSL().isBlank()) {
            HILIN.setWHSL().move(inAddPickViaSblot.getISWHSL());
         }
         if (!inAddPickViaSblot.getISBANO().isBlank()) {
            HILIN.setBANO().move(inAddPickViaSblot.getISBANO());
         }
         if (!inAddPickViaSblot.getISCAMU().isBlank()) {
            HILIN.setCAMU().move(inAddPickViaSblot.getISCAMU());
         }
         X0FLDD = 11;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inAddPickViaSblot.getISPLRN());
         RNUM();
         checkPLRNForUpdate = ((long)this.PXNUM);
         if (checkPLRNForUpdate != HILIN.getPLRN()) {
            //  MSGID=WWO0202 Reporting number must be entered
            MICommon.setError( "", "WWO0202", inAddPickViaSblot.getISPLRN());
            return;
         }
         HILIN.setPLRN((long)this.PXNUM);
         ITALO.setCONO(HILIN.getCONO());
         ITALO.setPLRN(HILIN.getPLRN());
         if (ITALO.CHAIN("50", ITALO.getKey("50", 2))) {
            HILIN.setITNO().move(ITALO.getITNO());
            HILIN.setWHSL().move(ITALO.getWHSL());
            HILIN.setBANO().move(ITALO.getBANO());
            HILIN.setCAMU().move(ITALO.getCAMU());
            HISUB.setBANO().move(ITALO.getBANO());
            HISUB.setITNO().move(ITALO.getITNO());
            ITMAS.setCONO(HISUB.getCONO());
            ITMAS.setITNO().move(HISUB.getITNO());
            ITMAS.CHAIN("00", ITMAS.getKey("00"));
            //  Transfer soft to hard
            if (ITALO.getSOFT() == 1) {
               if (!inAddPickViaSblot.getISWHSL().isBlank()) {
                  HILIN.setWHSL().move(inAddPickViaSblot.getISWHSL());
               }
               if (!inAddPickViaSblot.getISBANO().isBlank()) {
                  HILIN.setBANO().move(inAddPickViaSblot.getISBANO());
                  HISUB.setBANO().move(inAddPickViaSblot.getISBANO());
               }
               if (!inAddPickViaSblot.getISCAMU().isBlank()) {
                  HILIN.setCAMU().move(inAddPickViaSblot.getISCAMU());
               }
            }
         }
         if (!inAddPickViaSblot.getISBANT().isBlank()) {
            HISUB.setBANT().move(inAddPickViaSblot.getISBANT());
            writeSubline = true;
         } else {
            //  MSGID=WBANT02 Reference sublot ID must be entered
            MICommon.setError( "", "WBANT02", inAddPickViaSblot.getISBANT());
            return;
         }
         if (!inAddPickViaSblot.getISRPDT().isBlank()) {
            if (!MICommon.toNumericDate(inAddPickViaSblot.getISRPDT())) {
               MICommon.setError("RPDT");
               return;
            }
            HILIN.setRPDT(MICommon.getNumericDate());
         }
         //--------------------------------------------------------------------
         if (!inAddPickViaSblot.getISRPTM().isBlank()) {
            X0FLDD = 6;
            XXNUMN = 0d;
            XXNUMA.clear();
            XXNUMA.moveLeft(inAddPickViaSblot.getISRPTM());
            RNUM();
            HILIN.setRPTM((int)this.PXNUM);
         }
         //--------------------------------------------------------------------
         if (!inAddPickViaSblot.getISUSD1().isBlank()) {
            HILIN.setUSD1().move(inAddPickViaSblot.getISUSD1());
         }
         if (!inAddPickViaSblot.getISUSD2().isBlank()) {
            HILIN.setUSD2().move(inAddPickViaSblot.getISUSD2());
         }
         if (!inAddPickViaSblot.getISUSD3().isBlank()) {
            HILIN.setUSD3().move(inAddPickViaSblot.getISUSD3());
         }
         if (!inAddPickViaSblot.getISUSD4().isBlank()) {
            HILIN.setUSD4().move(inAddPickViaSblot.getISUSD4());
         }
         if (!inAddPickViaSblot.getISUSD5().isBlank()) {
            HILIN.setUSD5().move(inAddPickViaSblot.getISUSD5());
         }
         if (!inAddPickViaSblot.getISBREF().isBlank()) {
            HILIN.setBREF().move(inAddPickViaSblot.getISBREF());
         }
         if (!inAddPickViaSblot.getISBRE2().isBlank()) {
            HILIN.setBRE2().move(inAddPickViaSblot.getISBRE2());
         }
         if (!inAddPickViaSblot.getISPMSN().isBlank()) {
            HIHED.setPMSN().move(inAddPickViaSblot.getISPMSN());
         }
         if (!inAddPickViaSblot.getISISMD().isBlank()) {
            X0FLDD = 1;
            XXNUMN = 0d;
            XXNUMA.clear();
            XXNUMA.moveLeft(inAddPickViaSblot.getISISMD().getChar());
            RNUM();
            HILIN.setISMD((int)this.PXNUM);
         }
      }
      //-----------------------------
      //   Validate/create transaction header data
      if (IN75) {
         IN60 = true;
         return;
      }
      RHEAD();
      if (IN60 && IN76) {
         return;
      }
      RPACK();
      if (IN60 && IN76) {
         return;
      }
      //   Validate/create transaction line data
      RLINE();
      if (IN60) {
         return;
      }
      outAddPickViaSblot.setOSCONO().move(XXCONO);
      outAddPickViaSblot.setOSMSGN().move(HIHED.getMSGN());
      if (MHS850DS.getJ7STNB() > 0L) {
         X0FLDD = 10;
         XXNUMN = (long)MHS850DS.getJ7STNB();
         XXNUMA.clear();
         RNUMO();
         outAddPickViaSblot.setOSSTNB().moveLeft(this.PXALPH);
         X0FLDD = 7;
         XXNUMN = (int)MHS850DS.getJ7STRN();
         XXNUMA.clear();
         RNUMO();
         outAddPickViaSblot.setOSSTRN().moveLeft(this.PXALPH);
      }
      MICommon.setData( outAddPickViaSblot.get());
   }
   
   /**
   *    RHEAD - Validate create transaction header data
   */
   public void RHEAD() {
      // ----------------------------------------------------------------
      //   Call PI programs for validation/processing
      // ----------------------------------------------------------------
      IN76 = false;
      IN77 = false;
      X1OPC.clear();
      rAPIDS.clear();
      X0OPC.moveLeftPad(XXOPC);
      if (XXOPC.EQ("*ADD")) {
         HIHED.setSTAT().move("10");
      }
      rPHEDPIpreCall();
      apCall("MHIHEDPI", rPHEDPI);
      rPHEDPIpostCall();
      IN60 = toBoolean(X0IN60.getChar());
      if (IN60) {
         XXERRM = true;
         IN76 = true;
         MICommon.setError( "", X0MSID.toString(), X0MSGD);
         X1OPC.moveLeft("534");
         return;
      }
      if (!XXPRFL.isBlank() && XXOPC.NE("*DLT")) {
         rAPIDS.clear();
         X0OPC.moveLeftPad("*CHK");
         rPHEDPIpreCall();
         apCall("MHIHEDPI", rPHEDPI);
         rPHEDPIpostCall();
         IN60 = toBoolean(X0IN60.getChar());
         if (IN60) {
            IN77 = true;
            XXERRM = true;
            MICommon.setError( "", X0MSID.toString(), X0MSGD);
            X1OPC.moveLeft("534");
            XXIN60 = true;
            //--------------------------------------------------------
            rAPIDS.clear();
            X0OPC.moveLeftPad("*UPD");
            rPHEDPIpreCall();
            apCall("MHIHEDPI", rPHEDPI);
            rPHEDPIpostCall();
            X0OPC.moveLeftPad("*CHK");
            //--------------------------------------------------------
            return;
         }
      }
   }

   /**
   *    RPACK - Validate create transaction package data
   */
   public void RPACK() {
      // ----------------------------------------------------------------
      //   Call PI programs for validation/processing
      // ----------------------------------------------------------------
      IN76 = false;
      IN78 = false;
      X1OPC.clear();
      rAPIDS.clear();
      X0OPC.moveLeftPad(XXOPC);
      rPHEDPIpreCall();
      apCall("MHIPACPI", rPHEDPI);
      rPHEDPIpostCall();
      IN60 = toBoolean(X0IN60.getChar());
      if (IN60) {
         XXERRM = true;
         IN76 = true;
         MICommon.setError( "", X0MSID.toString(), X0MSGD);
         X1OPC.moveLeft("535");
         return;
      }
      if (!XXPRFL.isBlank() && XXOPC.NE("*DLT")) {
         rAPIDS.clear();
         X0OPC.moveLeftPad("*CHK");
         rPHEDPIpreCall();
         apCall("MHIPACPI", rPHEDPI);
         rPHEDPIpostCall();
         IN60 = toBoolean(X0IN60.getChar());
         if (IN60) {
            IN78 = true;
            XXERRM = true;
            MICommon.setError( "", X0MSID.toString(), X0MSGD);
            X1OPC.moveLeft("535");
            XXIN60 = true;
            //--------------------------------------------------------
            rAPIDS.clear();
            X0OPC.moveLeftPad("*UPD");
            rPHEDPIpreCall();
            apCall("MHIPACPI", rPHEDPI);
            rPHEDPIpostCall();
            X0OPC.moveLeftPad("*CHK");
            //--------------------------------------------------------
            return;
         }
      }
   }

   /**
   *    RLINE - Validate create transaction line data
   */
   public void RLINE() {
      X1OPC.clear();
      // ----------------------------------------------------------------
      //   Call PI programs for validation/processing
      // ----------------------------------------------------------------
      if (XXOPC.EQ("*ADD") && XXPRFL.isBlank()) {
         rAPIDS.clear();
         X0OPC.moveLeftPad(XXOPC);
         rPHEDPIpreCall();
         apCall("MHILINPI", rPHEDPI);
         rPHEDPIpostCall();
         IN60 = toBoolean(X0IN60.getChar());
         if (IN60) {
            XXERRM = true;
            MICommon.setError( "", X0MSID.toString(), X0MSGD);
            X1OPC.moveLeft("536");
            XXIN60 = true;
            checkForSublots();
            return;
         }
         checkForSublots();
      }

      if (XXPRFL.isBlank() || XXPRFL.EQ("*CHK")) {
         rAPIDS.clear();
         X0OPC.moveLeftPad("*CHK");
         rPHEDPIpreCall();
         apCall("MHILINPI", rPHEDPI);
         rPHEDPIpostCall();
         IN60 = toBoolean(X0IN60.getChar());
         // SndWhsLine should create a record regardless of errors
         if (IN60 && !MICommon.isTransaction("SndWhsLine")) {
            XXERRM = true;
            MICommon.setError( "", X0MSID.toString(), X0MSGD);
            X1OPC.moveLeft("536");
            //--------------------------------------------------------
            rAPIDS.clear();
            X0OPC.moveLeftPad("*UPD");
            rPHEDPIpreCall();
            apCall("MHILINPI", rPHEDPI);
            rPHEDPIpostCall();
            IN60 = toBoolean(X0IN60.getChar());
            X0OPC.moveLeftPad("*CHK");
            //--------------------------------------------------------
            return;
         }
         if (IN60 && MICommon.isTransaction("SndWhsLine")) {
            HILIN.setSTAT().move("35");
         } else {
            HILIN.setSTAT().move("40");
         }
         checkForSublots();
      }

      //-------------------------------------------------------
      if (!XXPRFL.isBlank()) {
         rAPIDS.clear();
         X0OPC.moveLeftPad(XXOPC);
         rPHEDPIpreCall();
         apCall("MHILINPI", rPHEDPI);
         rPHEDPIpostCall();
         IN60 = toBoolean(X0IN60.getChar());
         if (IN60) {
            XXERRM = true;
            MICommon.setError( "", X0MSID.toString(), X0MSGD);
            X1OPC.moveLeft("536");
            XXIN60 = true;
            return;
         }
         if (ITMAS.getSUME() == 1) {
            // Validate/create subline transactions
            rAPIDS.clear();
            X0OPC.clear();
            if (MICommon.isTransaction("AddPickViaSblot")) {
               X0OPC.moveLeft("*ADDCPY");
            } else if (MICommon.isTransaction("AddWhsLine") ||
               MICommon.isTransaction("ChgWhsLine") ||
               MICommon.isTransaction("DelWhsLine")) {
               X0OPC.clear();
            } else {
            X0OPC.moveLeftPad("*VALIDATE");
            }
            //  Only MHISUBPI for AddPickViaSblot transaction if Reference ID passed
            if (writeSubline &&
               X0OPC.EQ("*ADDCPY") ||
               X0OPC.EQ("*VALIDATE")) {
            RSUB();         
            if (IN60) {
               return;
            }
         }
      }
      }
      //-------------------------------------------------------
      if (XXPRFL.isBlank() || XXPRFL.EQ("*CHK")) {
         // Add/SndWhsLine should call MHS870 in line mode
         if (MICommon.isTransaction("AddWhsLine") || MICommon.isTransaction("SndWhsLine")) {
            X0OPC.moveLeftPad("MM_0019");
         } else {
            X0OPC.moveLeftPad("MM_0015");
         }
         rMHS870preCall();
         apCall("MHS870", rMHS870);
         rMHS870postCall();
         IN60 = toBoolean(X0IN60.getChar());
         if (IN60) {
            MICommon.setError( "", X0MSID.toString(), X0MSGD);
            XXIN60 = true;
            return;
         }
      }
      //-------------------------------------------------------
      if (IN77 || IN78) {
         XXIN60 = true;
      }
      //-------------------------------------------------------
      if (!IN77 && !IN78) {
         if (!MICommon.isTransaction("AddCfmPickList") && !MICommon.isTransaction("AddPOClose") && !MICommon.isTransaction("AddPickViaPack") && !MICommon.isTransaction("AddPickSftPacLn")) {
            // PRFL = *AUT via autojob
            // PRFL = *EXE call MHS870
            if (!XXPRFL.isBlank()) {
               if (XXPRFL.EQ("*AUT") && XXOPC.NE("*DLT")) {
                  RAUT();
               } else {
                  if (XXPRFL.EQ("*EXE") && XXOPC.NE("*DLT")) {
                     rAPIDS.clear();
                     X0OPC.moveLeftPad("*CHK");
                     rPHEDPIpreCall();
                     apCall("MHILINPI", rPHEDPI);
                     rPHEDPIpostCall();
                     IN60 = toBoolean(X0IN60.getChar());
                     if (IN60) {
                        XXERRM = true;
                        MICommon.setError( "", X0MSID.toString(), X0MSGD);
                        X1OPC.moveLeft("536");
                        XXIN60 = true;
                        //--------------------------------------
                        rAPIDS.clear();
                        X0OPC.moveLeftPad("*UPD");
                        rPHEDPIpreCall();
                        apCall("MHILINPI", rPHEDPI);
                        rPHEDPIpostCall();
                        X0OPC.moveLeftPad("*CHK");
                        return;
                        //--------------------------------------
                     }
                     X0FLDD = 5;
                     XXNUMN = (double)HILIN.getMSLN();
                     XXNUMA.clear();
                     RNUM();
                     MvxString MSLN = new MvxString(5);
                     MSLN.moveRight(this.PXALPH);
                     whsTrans(MICommon.toAlpha(HILIN.getCONO()), HILIN.getMSGN(), HILIN.getPACN(), MSLN, XXPRFL);
                     if (IN60) {
                        XXERRM = true;
                     }
                  }
               }
            }
         }
      }
   }

   public void checkForSublots() {
      //  Sublots apply - create subline
      if (MICommon.isTransaction("AddPickViaSblot") &&
         writeSubline) {
         if (ITMAS.getSUME() == 1) {
            
            // Validate/create subline transactions
            rAPIDS.clear();
            X0OPC.clear();
            X0OPC.moveLeft("*ADDCPY");
            RSUB();
            writeSubline = false;
         }
      }
   }

   /**
   *    RAUT Write to file MHS855
   */
   public void RAUT() {
      HM855.setCONO(HIHED.getCONO());
      HM855.setWHLO().move(HIHED.getWHLO());
      HM855.setE0PA().move(HIHED.getE0PA());
      HM855.setQLFR().move(HIHED.getQLFR());
      HM855.setMSNR().move(HIHED.getMSGN());
      HM855.setSTAT().move(HIHED.getSTAT());
      HM855.setPGNM().moveLeftPad("MHS850MI");
      this.PXCONO = HIHED.getCONO();
      this.PXDIVI.clear();
      this.PXDFMI.move("TIME");
      this.PXDFMO.move("YMD8");
      this.PXOPRM = 1;
      HM855.setRGDT(this.PXDATO);
      HM855.setRGTM(movexTime());
      HM855.setCHID().move(HIHED.getCHID());
      HM855.WRITE("RR");
   }

   /**
   *    RQTY - Convert Qty to From Alpha Numeric
   */
   public void RQTY() {
      this.PXFLDD = 9 + this.PXDCCD;
      this.PXEDTC = 'P';
      this.PXDCFM = '.';
      this.PXNUM = XXQTYN;
      this.PXALPH.clear();
      this.PXALPH.move(XXQTYA);
      SRCOMNUM.COMNUM();
      if (SRCOMNUM.PXNMER != 0) {
         IN75 = true;
         //   MSGID=XNU0000 Numeric error
         MICommon.setError( "", "XNU0000");
         this.MSGID.moveRight(SRCOMNUM.PXNMER, 1);
         return;
      }
      
   }

   /**
   *    RQTYO - Convert Qty to From Alpha Numeric
   */
   public void RQTYO() {
      this.PXDCCD = ITMAS.getDCCD();
      this.PXFLDD = 9 + ITMAS.getDCCD();
      this.PXEDTC = 'P';
      this.PXDCFM = '.';
      this.PXALPH.clear();
      this.PXNUM = XXQTYN;
      SRCOMNUM.COMNUM();
      XC = check(' ', this.PXALPH) + 1;
      this.PXALPH.setSubstringPad(this.PXALPH, XC - 1);
      this.PXDCCD = 0;
      this.PXFLDD = 0;
   }

   /**
   *    RNUM - Convert Numeric to From Alpha Numeric
   */
   public void RNUM() {
      this.PXDCCD = X0DCCD;
      this.PXFLDD = X0FLDD;
      this.PXEDTC = 'P';
      this.PXDCFM = '.';
      this.PXNUM = XXNUMN;
      this.PXALPH.clear();
      this.PXALPH.move(XXNUMA);
      SRCOMNUM.COMNUM();
      if (SRCOMNUM.PXNMER != 0) {
         IN75 = true;
         //   MSGID=XNU0000 Numeric error
         MICommon.setError( "", "XNU000" + SRCOMNUM.PXNMER);
      }
      X0DCCD = 0;
   }

   /**
   *    RNUMO - Convert numeric output
   */
   public void RNUMO() {
      //   Convert numeric to correct format
      this.PXDCCD = X0DCCD;
      this.PXFLDD = X0FLDD;
      this.PXEDTC = 'P';
      this.PXDCFM = '.';
      this.PXALPH.clear();
      this.PXNUM = XXNUMN;
      SRCOMNUM.COMNUM();
      XC = check(' ', this.PXALPH) + 1;
      this.PXALPH.setSubstringPad(this.PXALPH, XC - 1);
      X0DCCD = 0;
   }

   /**
   *    RE065  - Check Message Type
   */
   public void RE065() {
      // Message Type should not be blank
      if (XXE065.isBlank()) {
         IN60 = true;
         //   MSGID=WE06502   Message Type must be entered
         MICommon.setError( "", "WE06502");
         return;
      }
   }

   /**
   *    RWHL  - Check Warehouse
   */
   public void RWHL() {
      // Warehouse should not be blank
      if (XXWHLO.isBlank()) {
         IN60 = true;
         //   MSGID=WWH0102 Warehouse must be entered
         MICommon.setError( "", "WWH0102");
         return;
      }
      // Valid warehouse
      ITWHL.setCONO(LDAZD.CONO);
      ITWHL.setWHLO().move(XXWHLO);
      if (!ITWHL.CHAIN("00", ITWHL.getKey("00"))) {
         IN60 = true;
         //   MSGID=WWH0103 Warehouse &1 does not exist
         MICommon.setError( "", "WWH0103", XXWHLO);
         return;
      }
   }
   

   /**
   *    RITNO - Get item master record
   */
   public void RITNO() {
      X1DCCD = 0;
      X2DCCD = 0;
      ITMAS.setCONO(LDAZD.CONO);
      ITMAS.setITNO().move(XXITNO);
      if (ITMAS.CHAIN("00", ITMAS.getKey("00"))) {
         X1DCCD = ITMAS.getDCCD();
         X2DCCD = ITMAS.getDCCD();
      }
      if (ITMAS.getACTI() > 0) {
         if (ITMAS.getCAWP() == 0) {
            if (ITMAS.getPPUN().NE(ITMAS.getUNMS())) {
               ITAUN.setCONO(ITMAS.getCONO());
               ITAUN.setITNO().move(ITMAS.getITNO());
               ITAUN.setAUTP(2);
               ITAUN.setALUN().move(ITMAS.getPPUN());
               if (ITAUN.CHAIN("00", ITAUN.getKey("00"))) {
                  X2DCCD = ITAUN.getDCCD();
               }
            }
         } else {
            if (ITMAS.getCWUN().NE(ITMAS.getUNMS())) {
               ITAUN.setCONO(ITMAS.getCONO());
               ITAUN.setITNO().move(ITMAS.getITNO());
               ITAUN.setAUTP(1);
               ITAUN.setALUN().move(ITMAS.getCWUN());
               if (ITAUN.CHAIN("00", ITAUN.getKey("00"))) {
                  X2DCCD = ITAUN.getDCCD();
               }
            }
         }
      }
   }

   /**
   *    GENMBM - Generate Mail Box Message
   */
   public void GENMBM() {
      //   Clear parameter list
      CRS428DS.setCRS428DS().clear();
      //   Move fields to parameter list
      if (X1OPC.EQ("534")) {
         CRS428DS.setC1CONO(HIHED.getCONO());
         CRS428DS.setC1MTPE().move("534");
         CRS428DS.setC1REC2().move(this.DSUSS);
         CRS428DS.setC1ADAT(movexDate());
         CRS428DS.setC1PAR1().moveLeft(HIHED.getMSGN());
         CRS428DS.setC1PAR2().moveLeft(this.MSGID);
         //-----------------------------------------
         CRS428PDS.setC2PGNM().moveLeft("MHS850");
         CRS428PDS.setC2FL01().moveLeft("ZZTDTA");
         CRS428PDS.setC2DT01().moveLeft(HIHED.getMSGN());
         CRS428PDS.setC2QTTP(1);
         CRS428PDS.setC2PICC().move("BI");
         CRS428PDS.setC2OPT2().clear();
         //-----------------------------------------
      }
      if (X1OPC.EQ("535")) {
         CRS428DS.setC1CONO(HIPAC.getCONO());
         CRS428DS.setC1MTPE().move("535");
         CRS428DS.setC1REC2().move(this.DSUSS);
         CRS428DS.setC1ADAT(movexDate());
         CRS428DS.setC1PAR1().moveLeft(HIPAC.getMSGN());
         CRS428DS.setC1PAR2().moveLeft(HIPAC.getPACN());
         CRS428DS.setC1PAR3().moveLeft(this.MSGID);
         //-----------------------------------------
         CRS428PDS.setC2PGNM().moveLeft("MHS851");
         CRS428PDS.setC2FL01().moveLeft("ZZTDTA");
         CRS428PDS.setC2DT01().moveLeft(HIPAC.getMSGN());
         CRS428PDS.setC2FL02().moveLeft("ZZTDA1");
         CRS428PDS.setC2DT02().moveLeft(HIPAC.getWHLO());
         CRS428PDS.setC2QTTP(1);
         CRS428PDS.setC2PICC().move("BI");
         CRS428PDS.setC2OPT2().clear();
         //-----------------------------------------
      }
      if (X1OPC.EQ("536")) {
         CRS428DS.setC1CONO(HILIN.getCONO());
         CRS428DS.setC1MTPE().move("536");
         CRS428DS.setC1REC2().move(this.DSUSS);
         CRS428DS.setC1ADAT(movexDate());
         CRS428DS.setC1PAR1().moveLeft(HILIN.getMSGN());
         CRS428DS.setC1PAR2().moveLeft(HILIN.getPACN());
         CRS428DS.setC1PAR3().moveLeft(HILIN.getMSLN(), 5);
         CRS428DS.setC1PAR4().moveLeft(this.MSGID);
         //-----------------------------------------
         CRS428PDS.setC2PGNM().moveLeft("MHS852");
         CRS428PDS.setC2FL01().moveLeft("ZZTDTA");
         CRS428PDS.setC2DT01().moveLeft(HILIN.getMSGN());
         CRS428PDS.setC2FL02().moveLeft("ZZTDA1");
         CRS428PDS.setC2DT02().moveLeft(HILIN.getMSLN(), 5);
         CRS428PDS.setC2FL03().moveLeft("ZZTDA2");
         CRS428PDS.setC2DT03().moveLeft(HILIN.getWHLO());
         CRS428PDS.setC2DT03().move(HILIN.getQLFR());
         CRS428PDS.setC2QTTP(1);
         CRS428PDS.setC2PICC().move("BI");
         CRS428PDS.setC2OPT2().clear();
         //-----------------------------------------
      }
      rPL428preCall();
      apCall("CRS428", rPL428);
      rPL428postCall();
   }

   /**
   *    RVMSNR - Retrieve messagenumber
   */
   public void RVMSNR() {
      this.PXCONO = LDAZD.CONO;
      this.PXDIVI.clear();
      PXRTVNBR.PXNBTY.move("17");
      PXRTVNBR.PXNBID = '1';
      PXRTVNBR.PXDATE = 0;
      PXRTVNBR.CRTVNBR();
   }

   /**
   *    TRIMPAC - Triming package number
   */
   public void TRIMPAC() {
      Z1 = 0;
      Z3 = 0;
      PCC.clear();
      do {
         if (PAC.charAt(Z1) != ' ') {
            PCC.setCharAt(Z3, PAC.charAt(Z1));
            Z3++;
         }
         Z1++;
      } while (!(Z1 == 20));
   }

   /*
   *   validInput
   */
   public boolean validInput() {
      if (inputDLIX.isBlank() && inputPANR.isBlank() && inputSSCC.isBlank()) {
         IN60 = true;
         //   MSGID-MW44006 SSCC number or Delivery number or Package number must be entered
         MICommon.setError( "", "MW44006");
         return false;
      }
      //   Delivery
      if (!inputDLIX.isBlank()) {
         HDISH.setCONO(LDAZD.CONO);
         HDISH.setINOU(1);
         X0FLDD = 11;
         XXNUMN = 0d;
         XXNUMA.clear();
         XXNUMA.moveLeft(inputDLIX);
         RNUM();
         HILIN.setDLIX((long)this.PXNUM);
         HDISH.setDLIX((long)this.PXNUM);
         if (!HDISH.CHAIN("00", HDISH.getKey("00"))) {
            IN60 = true;
            MICommon.setError( "", "WDL0203", MICommon.toAlpha(HDISH.getDLIX()));
            return false;
         }
      }
      if (numberPackage == 1 && inputPANR.isBlank() && inputSSCC.isBlank()) {
         IN60 = true;
         MICommon.setError( "", "WPA5103", inputPANR);
         return false;
      }
      return true;
   }
   /**
   *   retrieveDLIX - via PANR or SSCC
   */
   public void retrieveDLIX() {
      PTRNSfound = false;
      PTRNS.setCONO(LDAZD.CONO);
      PTRNS.setDIPA(0);
      PTRNS.setINOU(1);
      if (!inputPANR.isBlank()) {
         PTRNS.setPANR().moveLeftPad(inputPANR);
         PTRNS.SETLL("85", PTRNS.getKey("85", 4));
         if (PTRNS.READE("85", PTRNS.getKey("85",4))) {
            if (PTRNS.READE("85", PTRNS.getKey("85",4))) {
               //   MSGID-WDL0202 Delivery number must be entered
               MICommon.setError( "", "WDL0202");
               IN60 = true;
               return;
            }
            if (!inputSSCC.isBlank() && PTRNS.getSSCC().NE(inputSSCC)) {
               //   MSGID-MM_0056 Package number and SSCC refer to different packages
               MICommon.setError("PACN", "MM_0056");
               IN60 = true;
               return;
            }
            PTRNSfound = true;
         }
      }
      if (!inputSSCC.isBlank()) {
         PTRNS.setSSCC().moveLeftPad(inputSSCC);
         if (PTRNS.CHAIN("70", PTRNS.getKey("70", 3))) {
            PTRNSfound = true;
            inputPANR.moveLeftPad(PTRNS.getPANR());
         }
      }
   }
   
   /**
   *    RSUB - Validate create sublot transactions
   */
   public void RSUB() {
      // ----------------------------------------------------------------
      //   Call PI programs for validation/processing
      // ----------------------------------------------------------------
      rPSUBPIpreCall();
      apCall("MHISUBPI", rPSUBPI);
      rPSUBPIpostCall();
      IN60 = toBoolean(X0IN60.getChar());
      if (IN60) {
         XXERRM = true;
         MICommon.setError( "", X0MSID.toString(), X0MSGD);
         X1OPC.moveLeft("536");
         XXIN60 = true;
         return;
      }
   }

   /**
    * Converts timestime into warehouse timezone
    * @param timestamp system date & time
    */
   public void RTIME(long timestamp) {
      if (CRCalendar.retrieveTIZOForWHLO(PXMNGTZN, LDAZD.CONO, ITWHL.getWHLO())) {
         int date = CRCalendar.systemDate(timestamp);
         int time = CRCalendar.systemTime(timestamp);
         int seconds = time % 100; //HHMMSS -> SS
         time = time / 100; //HHMMSS -> HHMM
         fromTIZO.moveLeftPad(cRefTIZOext.SYS());
         toTIZO.move(CRCalendar.getTimeZone());
         if (CRCalendar.convertToTIZO(PXMNGTZN, LDAZD.CONO, CRCalendar.getTZDivison(), 
            fromTIZO, toTIZO, date, time)) 
         {
            SSRPDT = CRCalendar.getTZDate();
            SSTRTM = CRCalendar.getTZTime()*100 + seconds; //HHMMSS
         }
      }
   }

   /**
    * Checks if UTC mode is used for date+time fields in the
    * transaction.
    * @param UTCM
    * The UTC mode field from the transaction
    * @return
    * True if UTC mode is used.
    * Sets IN60=true if the UTCM field is incorrectly set.
    */
   public boolean isUTCmode(MvxString UTCM) {
      if (MICommon.toNumeric(UTCM)) {
         if (MICommon.getInt() == 1) {
            return true;
         } else {
            return false;
         }
      } else {
         IN60 = true;
         return false;
      }
   }
   
   /**
    * Converts to UTC. <BR><BR>
    *
    * The results are returned in variables UTCdate and UTCtime.
    * @param fromDate
    * date to convert. YYYYMMDD
    * @param fromTime
    * time to convert. HHMM
    * @param fromTIZO
    * The time zone to convert from.
    * @return
    * True if successful.
    */
   public boolean convertToUTC() {
      toTIZO.moveLeftPad(cRefTIZOext.UTC());
      if (CRCalendar.convertToTIZO(PXMNGTZN, LDAZD.CONO, CRCalendar.getTZDivison(), fromTIZO, toTIZO, date, time)) {
         UTCdate = CRCalendar.getTZDate();
         UTCtime = CRCalendar.getTZTime(); //HHMM
         return true;
      }
      return false;
   }
   
   /**
    * Converts from UTC to the time zone of warehouse. <BR>
    * Input: TXDSDT, TXDSHM <BR>
    * Output: TXDSDT, TXDSHM <BR>
    * @return
    * True if successful.
    */
   public boolean convertFromUTC() {   
      if (CRCalendar.retrieveTIZOForWHLO(PXMNGTZN, LDAZD.CONO, XXWHLO)) {
         int HHMM = time; //HHMM
         fromTIZO.moveLeftPad(cRefTIZOext.UTC());
         toTIZO.move(CRCalendar.getTimeZone());
         if (CRCalendar.convertToTIZO(PXMNGTZN, LDAZD.CONO, CRCalendar.getTZDivison(), fromTIZO, toTIZO, date, HHMM)) {
            UTCdate = CRCalendar.getTZDate();
            UTCtime = CRCalendar.getTZTime();
            return true;
         }
      }
      return false;
   }
   
   /**
   *    SETLR - End of program
   */
   public void SETLR() {
      INLR = true;
      super.SETLR(INLR);
   }
      
   /**
   *    INIT - Init subroutine
   */
   public void INIT() {
      XXDIVI.clear();
      XXRCD = 0;
      XXREAD = 0;
      XXE065.clear();
      XXWHLO.clear();
      XXWHSL.clear();
      toContainer = false;
      SRIMPI.XXDTFM.move("YMD8");
      SRIMPI.XXDSEP.move(' ');
   }


   public MvxString XFPGNM = cRefPGNM.likeDef();
   
   // Movex MDB definitions
   public mvx.db.dta.CSYCAL SYCAL;
   public mvx.db.dta.MHIHED HIHED;
   public mvx.db.dta.MHIPAC HIPAC;
   public mvx.db.dta.MITBAL ITBAL;
   public mvx.db.dta.MITPOP ITPOP;
   public mvx.db.dta.MITWHL ITWHL;
   public mvx.db.dta.MITLOC ITLOC;           
   public mvx.db.dta.MITALO ITALO;
   public mvx.db.dta.MHILIN HILIN;
   public mvx.db.dta.MHISUB HISUB;  
   public mvx.db.dta.MITMAS ITMAS;
   public mvx.db.dta.MHIQLF HIQLF;
   public mvx.db.dta.MHPICH HPICH;
   public mvx.db.dta.MHPICD HPICD;
   public mvx.db.dta.MHM855 HM855;
   public mvx.db.dta.MHDISL HDISL;
   public mvx.db.dta.MITAUN ITAUN;
   public mvx.db.dta.MWOHED WOHED;
   public mvx.db.dta.MMIPPT MIPPT;
   public mvx.db.dta.MPTRNS PTRNS;
   public mvx.db.dta.MFTRNS FTRNS;
   public mvx.db.dta.MITBIL ITBIL;
   public mvx.db.dta.MHDISH HDISH;
   public mvx.db.dta.MGLINE GLINE;
   public mvx.db.dta.MPHEAD PHEAD;
   public mvx.db.dta.MPLINE PLINE;
   public mvx.db.dta.CIDMAS IDMAS;
   public mvx.db.dta.MFTRND FTRND;
   public mvx.db.dta.MGHEAD GHEAD;
   public mvx.db.dta.MGTYPE GTYPE;
   public mvx.db.dta.CITZON ITZON;
   public mvx.db.dta.QMSSPE MSSPE;
   public mvx.db.dta.MITPCE ITPCE;
   public mvx.db.dta.MILOMA ILOMA;
   // Movex MDB definitions end

   public void initMDB() {
      HIQLF = (mvx.db.dta.MHIQLF)getMDB("MHIQLF", HIQLF);
      HIQLF.setAccessProfile("00", 'R');
      ITMAS = (mvx.db.dta.MITMAS)getMDB("MITMAS", ITMAS);
      ITMAS.setAccessProfile("00", 'R');
      HILIN = (mvx.db.dta.MHILIN)getMDB("MHILIN", HILIN);
      HILIN.setAccessProfile("00", 'R');
      ITALO = (mvx.db.dta.MITALO)getMDB("MITALO", ITALO);
      ITALO.setAccessProfile("10", 'R');
      ITALO.setAccessProfile("30", 'R');
      ITALO.setAccessProfile("50", 'R');
      ITWHL = (mvx.db.dta.MITWHL)getMDB("MITWHL", ITWHL);
      ITWHL.setAccessProfile("00", 'R');
      ITLOC = (mvx.db.dta.MITLOC)getMDB("MITLOC", ITLOC);    
      ITLOC.setAccessProfile("00", 'R');                      
      ITPOP = (mvx.db.dta.MITPOP)getMDB("MITPOP", ITPOP);
      ITPOP.setAccessProfile("20", 'R');
      ITPOP.setAccessProfile("30", 'R');
      ITPOP.setAccessProfile("50", 'R');
      ITBAL = (mvx.db.dta.MITBAL)getMDB("MITBAL", ITBAL);
      ITBAL.setAccessProfile("00", 'R');
      HIHED = (mvx.db.dta.MHIHED)getMDB("MHIHED", HIHED);
      HIHED.setAccessProfile("40", 'R');
      HIHED.setAccessProfile("00", 'R');
      HPICH = (mvx.db.dta.MHPICH)getMDB("MHPICH", HPICH);
      HPICH.setAccessProfile("00", 'R');
      HPICD = (mvx.db.dta.MHPICD)getMDB("MHPICD", HPICD);
      HPICD.setAccessProfile("00", 'R');
      SYCAL = (mvx.db.dta.CSYCAL)getMDB("CSYCAL", SYCAL);
      SYCAL.setAccessProfile("00", 'R');
      HM855 = (mvx.db.dta.MHM855)getMDB("MHM855", HM855);
      HM855.setAccessProfile("RR", 'R');
      HIPAC = (mvx.db.dta.MHIPAC)getMDB("MHIPAC", HIPAC);
      HIPAC.setAccessProfile("00", 'R');
      HDISL = (mvx.db.dta.MHDISL)getMDB("MHDISL", HDISL);
      HDISL.setAccessProfile("00", 'R');
      ITAUN = (mvx.db.dta.MITAUN)getMDB("MITAUN", ITAUN);
      ITAUN.setAccessProfile("00", 'R');
      WOHED = (mvx.db.dta.MWOHED)getMDB("MWOHED", WOHED);
      WOHED.setAccessProfile("00", 'R');
      MIPPT = (mvx.db.dta.MMIPPT)getMDB("MMIPPT", MIPPT);
      MIPPT.setAccessProfile("10", 'R');
      PTRNS = (mvx.db.dta.MPTRNS)getMDB("MPTRNS", PTRNS);
      PTRNS.setAccessProfile("05", 'R');
      PTRNS.setAccessProfile("70", 'R');
      PTRNS.setAccessProfile("85", 'R');
      FTRNS = (mvx.db.dta.MFTRNS)getMDB("MFTRNS", FTRNS);
      FTRNS.setAccessProfile("50", 'R');
      ITBIL = (mvx.db.dta.MITBIL)getMDB("MITBIL", ITBIL);
      ITBIL.setAccessProfile("00", 'R');
      HDISH = (mvx.db.dta.MHDISH)getMDB("MHDISH", HDISH);
      HDISH.setAccessProfile("00", 'R');
      GLINE = (mvx.db.dta.MGLINE)getMDB("MGLINE", GLINE);
      GLINE.setAccessProfile("00", 'R');
      PHEAD = (mvx.db.dta.MPHEAD)getMDB("MPHEAD", PHEAD);
      PHEAD.setAccessProfile("00", 'R');
      IDMAS = (mvx.db.dta.CIDMAS)getMDB("CIDMAS", IDMAS);
      IDMAS.setAccessProfile("00", 'R');
      PLINE = (mvx.db.dta.MPLINE)getMDB("MPLINE", PLINE);
      PLINE.setAccessProfile("00", 'R');
      HILIN = (mvx.db.dta.MHILIN)getMDB("MHILIN", HILIN);
      HISUB = (mvx.db.dta.MHISUB)getMDB("MHISUB", HISUB);
      FTRND = (mvx.db.dta.MFTRND)getMDB("MFTRND", FTRND);
      FTRND.setAccessProfile("00", 'R');
      GHEAD = (mvx.db.dta.MGHEAD)getMDB("MGHEAD", GHEAD);
      GHEAD.setAccessProfile("00", 'R');
      GTYPE = (mvx.db.dta.MGTYPE)getMDB("MGTYPE", GTYPE);
      GTYPE.setAccessProfile("00", 'R');
      ITZON = (mvx.db.dta.CITZON)getMDB("CITZON", ITZON);
      ITZON.setAccessProfile("00", 'R');
      GTYPE = (mvx.db.dta.MGTYPE)getMDB("MGTYPE", GTYPE);
      GTYPE.setAccessProfile("00", 'R');
      MSSPE = (mvx.db.dta.QMSSPE)getMDB("QMSSPE", MSSPE);
      ITPCE = (mvx.db.dta.MITPCE)getMDB("MITPCE", ITPCE);
      ILOMA = (mvx.db.dta.MILOMA)getMDB("MILOMA", ILOMA);
   }

   public cMICommon MICommon = new cMICommon(this);
   public cCRCommon CRCommon = new cCRCommon(this);
   public cRetrieveUserInfo retrieveUserInfo = new cRetrieveUserInfo(this);
   public int XXGEDT;//*LIKE GEDT
   public int reportDate;
   public MvxString XXE065 = new MvxString(6);//*LIKE E065                //
   public MvxString XXWHLO = cRefWHLO.likeDef();
   public MvxString XXWHSL = cRefWHSL.likeDef();
   public boolean toContainer;
   public MvxString XXMSGN = new MvxString(15);//*LIKE MSGN
   public MvxString PAC = new MvxString(20);
   public MvxString PCC = new MvxString(20);
   public MvxString save_MSID = new MvxString(7);//*LIKE MSID
   public MvxString save_MSGD = new MvxString(256);//*LIKE MSGDTA
   public int Z3;//*LIKE XXN20
   public int Z2;//*LIKE XXN20
   public int Z1;//*LIKE XXN20
   public int X0FLDD;//*LIKE FLDD
   public double XXQTYN;//*LIKE XXN299
   public MvxString XXQTYA = new MvxString(29);//*LIKE ALPH
   public cSRIMPI SRIMPI = new cSRIMPI(this);
   public MvxString inputDLIX = new MvxString(11);
   public MvxString inputPANR = new MvxString(20);
   public MvxString inputSSCC = new MvxString(18);
   public MvxString inputMSGN = new MvxString(15);
   public MvxString inputPACN = new MvxString(20);
   public int inputMSLN;//*LIKE MSLN
   public int inputSUBL;//*LIKE SUBL
   public int date = 0;
   public int time = 0;
   public int UTCdate;
   public int UTCtime;
   public MvxString inputBANT = new MvxString(36);
   public int numberPackage;
   public MvxString XLWHLO = cRefWHLO.likeDef();
   public MvxString TIZOforWHLO = cRefTIZO.likeDef();
   public boolean XHIN60;
   public boolean XPIN60;
   public boolean XLIN60;
   public double XXTQTY;//*LIKE TQTY
   public long checkPLRNForUpdate;
   public boolean headerFound;
   public boolean packFound;
   //*PARAM rPHEDPI{
   public MvxRecord rPHEDPI = new MvxRecord();// len = 2664

   public void rPHEDPIpreCall() {// insert param into record for call
      rPHEDPI.reset();
      rPHEDPI.set(APIDS);
      rPHEDPI.set(SRIMPI.PXFLDI);
      rPHEDPI.setMDBRecord(HIHED);
      rPHEDPI.setMDBRecord(HIPAC);
      rPHEDPI.setMDBRecord(HILIN);
   }

   public void rPHEDPIpostCall() {// extract param from record after call

      rPHEDPI.reset();
      rPHEDPI.getString(APIDS);
      rPHEDPI.getString(SRIMPI.PXFLDI);
      rPHEDPI.getMDBRecord(HIHED);
      rPHEDPI.getMDBRecord(HIPAC);
      rPHEDPI.getMDBRecord(HILIN);
   }
   
   //*PARAM rHISUBPI{
   public MvxRecord rHISUBPI = new MvxRecord();// len = 2664

   public void rHISUBPIpreCall() {// insert param into record for call
      rPHEDPI.reset();
      rPHEDPI.set(APIDS);
      rPHEDPI.set(SRIMPI.PXFLDI);
      rPHEDPI.setMDBRecord(HISUB);
   }

   public void rHISUBPIpostCall() {// extract param from record after call

      rPHEDPI.reset();
      rPHEDPI.getString(APIDS);
      rPHEDPI.getString(SRIMPI.PXFLDI);
      rPHEDPI.getMDBRecord(HIHED);
      rPHEDPI.getMDBRecord(HIPAC);
      rPHEDPI.getMDBRecord(HILIN);
   }

   public double XXNUMN;//*LIKE XXN299
   public MvxString XXNUMA = new MvxString(29);//*LIKE ALPH
   public int XXMSLN;//*LIKE MSLN
   //*PARAM rPPACPI{
   public MvxRecord rPPACPI = new MvxRecord();// len = 2664

   public void rPPACPIpreCall() {// insert param into record for call
      rPPACPI.reset();
      rPPACPI.set(APIDS);
      rPPACPI.set(SRIMPI.PXFLDI);
      rPPACPI.setMDBRecord(HIHED);
      rPPACPI.setMDBRecord(HIPAC);
      rPPACPI.setMDBRecord(HILIN);
   }

   public void rPPACPIpostCall() {// extract param from record after call

      rPPACPI.reset();
      rPPACPI.getString(APIDS);
      rPPACPI.getString(SRIMPI.PXFLDI);
      rPPACPI.getMDBRecord(HIHED);
      rPPACPI.getMDBRecord(HIPAC);
      rPPACPI.getMDBRecord(HILIN);
   }

   public int XXRCD;//*LIKE XXN40
   //*PARAM rMHS870{
   public MvxRecord rMHS870 = new MvxRecord();// len = 2959

   public void rMHS870preCall() {// insert param into record for call
      rMHS870.reset();
      rMHS870.set(APIDS);
      rMHS870.set(SRIMPI.PXFLDI);
      rMHS870.setMDBRecord(HIHED);
      rMHS870.setMDBRecord(HIPAC);
      rMHS870.setMDBRecord(HILIN);
      rMHS870.setMDBRecord(HIQLF);
      rMHS870.set(MHS850DS.getMHS850DS());
   }

   public void rMHS870postCall() {// extract param from record after call

      rMHS870.reset();
      rMHS870.getString(APIDS);
      rMHS870.getString(SRIMPI.PXFLDI);
      rMHS870.getMDBRecord(HIHED);
      rMHS870.getMDBRecord(HIPAC);
      rMHS870.getMDBRecord(HILIN);
      rMHS870.getMDBRecord(HIQLF);
      rMHS870.getString(MHS850DS.setMHS850DS());
   }


   public boolean XXERRM;
   public boolean XXHEER;
   public boolean XXIN60;
   public boolean PTRNSfound;
   public boolean writeSubline;
   public boolean UTCmode;
   //*STRUCDEF rAPIDS{
   public MvxStruct rAPIDS = new MvxStruct(413);
   public MvxString APIDS = rAPIDS.newString(0, 413);
   public MvxString X0ENV = rAPIDS.newChar(0);
   public MvxString X0OPC = rAPIDS.newString(1, 10);
   public MvxString X0IN60 = rAPIDS.newChar(11);
   public MvxString X0MSID = rAPIDS.newString(12, 7);
   public MvxString X0MSGD = rAPIDS.newString(19, 256);
   public MvxString X0MSG = rAPIDS.newString(275, 128);
   public MvxString X0CHID = rAPIDS.newString(403, 10);
   public MvxString PXENV = rAPIDS.newChar(0);
   public MvxString PXOPC = rAPIDS.newString(1, 10);
   public MvxString PXIN60 = rAPIDS.newChar(11);
   public MvxString PXMSID = rAPIDS.newString(12, 7);
   public MvxString PXMSGD = rAPIDS.newString(19, 256);
   public MvxString PXMSG = rAPIDS.newString(275, 128);
   public MvxString PXCHID = rAPIDS.newString(403, 10);
   public cPXRTVNBR PXRTVNBR = new cPXRTVNBR(this);
   public int X0DCCD;//*LIKE DCCD
   public int X1DCCD;//*LIKE DCCD
   public int X2DCCD;//*LIKE DCCD
   //---------------------------------------------------------------
   public MvxString XXPRFL = new MvxString(4);//*LIKE XXA4
   public MvxString XXDIVI = new MvxString(3);//*LIKE XXA3
   public MvxString XXOPC = new MvxString(4);//*LIKE XXA4
   public int XC;//*LIKE XXN20
   public MvxString XXITNO = new MvxString(15);//*LIKE ITNO
   public MvxString X1OPC = new MvxString(10);//*LIKE XXA10
   //*PARAM rPLINPI{
   public MvxRecord rPLINPI = new MvxRecord();// len = 2664

   public void rPLINPIpreCall() {// insert param into record for call
      rPLINPI.reset();
      rPLINPI.set(APIDS);
      rPLINPI.set(SRIMPI.PXFLDI);
      rPLINPI.setMDBRecord(HIHED);
      rPLINPI.setMDBRecord(HIPAC);
      rPLINPI.setMDBRecord(HILIN);
   }

   public void rPLINPIpostCall() {// extract param from record after call

      rPLINPI.reset();
      rPLINPI.getString(APIDS);
      rPLINPI.getString(SRIMPI.PXFLDI);
      rPLINPI.getMDBRecord(HIHED);
      rPLINPI.getMDBRecord(HIPAC);
      rPLINPI.getMDBRecord(HILIN);
   }

   public sMHS850DS MHS850DS = new sMHS850DS(this);
   public sCRS428PDS CRS428PDS = new sCRS428PDS(this);
   public sCRS428DS CRS428DS = new sCRS428DS(this);
   //*PARAM rPL428{
   public MvxRecord rPL428 = new MvxRecord();// len = 266

   public void rPL428preCall() {// insert param into record for call
      rPL428.reset();
      rPL428.set(CRS428DS.getCRS428DS());
      rPL428.set(CRS428PDS.getCRS428PDS());
   }

   public void rPL428postCall() {// extract param from record after call
      rPL428.reset();
      rPL428.getString(CRS428DS.setCRS428DS());
      rPL428.getString(CRS428PDS.setCRS428PDS());
   }
   public MvxString XXSUNO = new MvxString(10);//*LIKE SUNO
   public int XXCHGD;//*LIKE CHGD
   public int XXCONO;//*LIKE CONO
   public int XXGETM;//*LIKE GETM
   public int XXREAD;//*LIKE XXN10
   public int nbrOfKeys;

   public cPXMNGTZN PXMNGTZN = new cPXMNGTZN(this);
   
   public MvxString PXFLDI = new MvxString(10);//*LIKE XXA10
   public MvxRecord rPSUBPI = new MvxRecord();// len = 2664
   public void rPSUBPIpreCall() {// insert param into record for call
      rPSUBPI.reset();
      rPSUBPI.set(APIDS);
      rPSUBPI.set(PXFLDI);
      rPSUBPI.setMDBRecord(HIHED);
      rPSUBPI.setMDBRecord(HIPAC);
      rPSUBPI.setMDBRecord(HILIN);
      rPSUBPI.setMDBRecord(HISUB);
   }

   public void rPSUBPIpostCall() {// extract param from record after call

      rPSUBPI.reset();
      rPSUBPI.getString(APIDS);
      rPSUBPI.getString(PXFLDI);
      rPSUBPI.getMDBRecord(HIHED);
      rPSUBPI.getMDBRecord(HIPAC);
      rPSUBPI.getMDBRecord(HILIN);
      rPSUBPI.setMDBRecord(HISUB);
   }

   public int SSRPDT;
   public int SSTRTM;
   public MvxString fromTIZO = cRefTIZO.likeDef();
   public MvxString toTIZO = cRefTIZO.likeDef();
   public cCRCalendar CRCalendar = new cCRCalendar(this);
   
   public String getVarList(java.util.Vector v) {
      super.getVarList(v);
      v.addElement(SYCAL);
      v.addElement(HM855);
      v.addElement(HIHED);
      v.addElement(HIPAC);
      v.addElement(XXQTYA);
      v.addElement(SRIMPI);
      v.addElement(ITBAL);
      v.addElement(ITPOP);
      v.addElement(ITZON);
      v.addElement(ITWHL);
      v.addElement(WOHED);
      v.addElement(ITALO);
      v.addElement(XXNUMA);
      v.addElement(rAPIDS);
      v.addElement(PXRTVNBR);
      v.addElement(XXPRFL);
      v.addElement(HILIN);
      v.addElement(HISUB);     
      v.addElement(HPICH);
      v.addElement(HPICD);
      v.addElement(HDISL);
      v.addElement(ITMAS);
      v.addElement(ITLOC);    
      v.addElement(ITAUN);
      v.addElement(XXDIVI);
      v.addElement(XXOPC);
      v.addElement(XXITNO);
      v.addElement(HIQLF);
      v.addElement(PAC);
      v.addElement(PCC);
      v.addElement(X1OPC);
      v.addElement(CRS428DS);
      v.addElement(CRS428PDS);
      v.addElement(XXWHLO);
      v.addElement(XXWHSL);
      v.addElement(toContainer);
      v.addElement(XXE065);
      v.addElement(XXSUNO);
      v.addElement(MIPPT);
      v.addElement(PTRNS);
      v.addElement(FTRNS);
      v.addElement(FTRND);
      v.addElement(ITBIL);
      v.addElement(HDISH);
      v.addElement(GLINE);
      v.addElement(PHEAD);
      v.addElement(PLINE);
      v.addElement(IDMAS);
      v.addElement(XXMSGN);
      v.addElement(retrieveUserInfo);
      v.addElement(save_MSID);
      v.addElement(save_MSGD);
      v.addElement(GHEAD);
      v.addElement(MHS850DS);
      v.addElement(inputDLIX);
      v.addElement(inputPANR);
      v.addElement(inputSSCC);
      v.addElement(MICommon);
      v.addElement(CRCommon);      
      v.addElement(GTYPE);
      v.addElement(XLWHLO);
      v.addElement(PXMNGTZN);
      v.addElement(GTYPE);
      v.addElement(XFPGNM);
      v.addElement(PXFLDI);
      v.addElement(inputMSGN);
      v.addElement(inputPACN);
      v.addElement(inputBANT);
      v.addElement(MSSPE);
      v.addElement(ITPCE);
      v.addElement(CRCalendar);
      v.addElement(fromTIZO);
      v.addElement(toTIZO);
      v.addElement(ILOMA);
      return version;
   }

   public void clearInstance() {
      super.clearInstance();
      XXGEDT = 0;
      X0FLDD = 0;
      XXQTYN = 0D;
      XXNUMN = 0D;
      XXMSLN = 0;
      XXRCD = 0;
      XXERRM = false;
      XXHEER = false;
      XXIN60 = false;
      X0DCCD = 0;
      X1DCCD = 0;
      X2DCCD = 0;
      XC = 0;
      Z1 = 0;
      Z2 = 0;
      Z3 = 0;
      XXCHGD = 0;
      XXCONO = 0;
      XXGETM = 0;
      XXREAD = 0;
      PTRNSfound = false;
      numberPackage = 0;
      XHIN60 = false;
      XPIN60 = false;
      XLIN60 = false;
      XXTQTY = 0D;
      nbrOfKeys = 0;
      writeSubline = false;
      checkPLRNForUpdate = 0L;
      headerFound = false;
      packFound = false;
      inputMSLN = 0;
      inputSUBL = 0;
      toContainer = false;
      reportDate = 0;
      SSRPDT = 0;
      SSTRTM = 0;
      UTCmode = false;
      UTCdate = 0;
      UTCtime = 0;
      date = 0;
      time = 0;
   }

   public String getVer() {
      return version;
   }

   public final String version = "Pgm.Name: MHS850MI, " + "Source creation date: Tue Feb 12 14:33:55 CET 2002, " + "ID number: 1013520835047";

   public String getVersion() {
      return _version;
   } //�end of method getVersion

   public String getRelease() {
      return _release;
   } //�end of method getRelease

   public String getSpLevel() {
      return _spLevel;
   } //�end of method getSpLevel

   public String getSpNumber() {
      return _spNumber;
   } //�end of method getSpNumber

   public final static String _version = "15";
   public final static String _release = "1";
   public final static String _spLevel = "4";
   public final static String _spNumber = "MAK_12063_190225_10:26";
   public final static String _GUID = "E0BAC9952C3949daB05AD0F28087BCE6";
   public final static String _tempFixComment = "";
   public final static String _build = "000000000000702";
   public final static String _pgmName = "MHS850MI";

   public String getGUID() {
      return _GUID;
   } //�end of method getGUID

   public String getTempFixComment() {
      return _tempFixComment;
   } //�end of method getTempFixComment

   public String getVersionInformation() {
      return _version + '.' + _release + '.' + _spLevel + ':' + _spNumber;
   } //�end of method getVersionInformation

   public String getBuild() {
      return (_version + _release + _build + "      " + _pgmName + "                                   ").substring(0, 34);
   } //�end of method getBuild
   public final static String MI_VERSION = "5ea6";

   public String [][] getStandardModification() {
      return _standardModifications;
   } // end of method [][] getStandardModification()

   public final static String [][] _standardModifications={
      {"JT-951802","160810","DDEMIRIAN","Reversing JT-845751-049"},
      {"JT-954940","160819","PHURST","Reversing JT-845751-049"},
      {"JT-989904","161125","14866","MHS852 Add or MHS850MI AddWhsLine return error code WSTA101 Status � balance ID 3 is invalid"},
      {"JT-998135","170109","PHURST","MHS850MI.AddWhsLine PO Receipts Sublot items"},
      {"JT-1005729","170118","PHURST","MHS850MI/AddPickViaSblot fails with message Reference sublot ID does not exist"},
      {"JT-1034250","170405","jrodriguez","Wrong decimal place is considered in MHS850MI.AddPOInspect"},
      {"JT-1038146","170424","jbejerano","Error 'Reporting date cannot be prior to pick list creation date MW42050'"},
      {"JT-1047327","170510","jbejerano","MHS850MI.AddROPick - Reporting date and time not correct in MHS852"},
      {"JT-1087167","171016","phurst","Enable DO receipt of sublot with MHS850 AddDOReceipt (Qualifier 50)"},
      {"JT-1103781","171107","gsimon","MHS850MI AddDOReceipt can't receive DO lines from different Consignors"},
      {"JT-1112396","171213","12063","MHS850MI.AddDOReceipt Location does not exist or Status of balance id is not under inspection"},
      {"JT-1131358","180208","jbejerano","MHS850MI qualifier 10 (MO receipt) does not allow receipt of MO in basic U/M"},
      {"JT-1138524","180321","gsimon","MHS850MI.AddPickVIaPack CFVP with SSCC - accept wrong warehouse"},
      {"JT-1154333","180403","jinfante","MHS850MI AddMOReceipt execution cause wrong transaction time in MWS070"},
      {"JT-1172541","180522","dclaveria","Lot no with lowercase letters should not be possible to enter"},
      {"JT-1176421","180611","12063","[ENHANCEMENT] Potency input at PO receipt via API"},
      {"JT-1204953","180905","phurst","MHS850MI AddPickViaSblot"},
      {"JT-1209964","180914","jbejerano","MHS850MI/AddDORecViaPack - \"Some lines were not received\" when a package (SSCC) is distributed to a third warehouse."},
      {"JT-1208365","180921","gsimon","Potency for MO and PO using MHS850MI"},
      {"JT-1208027","181018","12063","[ENHANCEMENT] MHS850MI implement UTCMode"},
      {"JT-1251686","190115","10716","Zero-receipt in MMS850MI/AddPOReceipt results to a fully received order line"},
      {"JT-1260784","190222","msietere","MHS850MI AddPOReceipt error Trans date is a future date"},
      {"JT-1260979","190315","12063","[ENHANCEMENT] MHS850MI AddPickByPacStk Add filters PLSX  to MHS850MI.AddPickByPackStk"}
   };
}