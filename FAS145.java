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
import mvx.dsp.common.GenericDSP;
import mvx.dsp.obj.*;
import mvx.util.*;

import java.util.Vector;


/*
*Modification area - M3
*Nbr            Date   User id     Description*     JT-942397 160823 15291       When Disposing Part of an Asset no Cost Transaction
*     JT-972965 161104 11972       FAS145 : Disposal of an asset is mixing up values in the fixed asset values (FFAHIS file)
*     JT-996063 161222 15291       Disposal of FA in status 8 not possible in FAS145
*    JT-1012250 170306 jparial     Missing disposal transactions when related option(CTRL + 11)  is used for an asset with 2 DPTP (WFAM=0 & WFAM=1)
*    JT-1038631 170412 jtan1       Creates a dump if FA40-550 and FA40-500 is setup with identical accounting strings
*    JT-1058520 170616 14013       FAS145 : The program dumps with a record lock timeout
*    JT-1072037 170725 jtan1       after split of an asset the original asset calculates incorrect values for further transactions
*    JT-1075261 170818 jtan1       Disposed asset FFAHIS and voucher not created even after correcting the entries GLS037
*    JT-1089356 170922 jparial     Incorrect period posted in FFAHIS for YTD reversal. Dispose period should be used as the period for depreciation reversal.
*    JT-1100096 171110 fcofino     Disposal of an asset with multiple Depreciation type (WFAM=0 and WFAM=1) needs twice attemp(14) before getting the error message
*    JT-1117347 171215 JPARIAL     FAS145: Cannot use F14 to dispose an asset if one of the depreciation types connected has no GL update but both basis for depreciation is the same as VATT
*    JT-1155693 180413 JPARIAL     Transaction number is not incremented properly in certain database types
*Modification area - Business partner
*Nbr            Date   User id     Description
*99999999999999 999999 XXXXXXXXXX  x
*Modification area - Customer
*Nbr            Date   User id     Description
*99999999999999 999999 XXXXXXXXXX  x
*/

/**
*<BR><B><FONT SIZE=+2>Fnc: Scrap of assets - select</FONT></B><BR><BR>
*
* This class ...<BR><BR>
*
*/
public class FAS145 extends Interactive
{
   /**
   *    PAINZ - Init
   */
   public void PAINZ() {
      IN62 = true;
      //   Check printerfile layout if proposal
      this.PXCONO = LDAZD.CONO;
      this.PXDIVI.move(LDAZD.DIVI);
      PXCHKPRL.PXPRTF.moveLeft("FAS146PF");
      IN79 = true;
      IN78 = false;
      PXCHKPRL.PXUSID.move(this.DSUSS);
      PXCHKPRL.PXDEVD.move(this.DSJNA);
      IN92 = PXCHKPRL.CCHKPRL();
      if (PXCHKPRL.PXLER == '1') {
         IN61 = true;
      } else {
         IN61 = false;
      }
      //   Check printerfile layout if update
      this.PXCONO = LDAZD.CONO;
      this.PXDIVI.move(LDAZD.DIVI);
      PXCHKPRL.PXPRTF.moveLeft("GLS041PF");
      IN78 = true;
      IN79 = false;
      PXCHKPRL.PXUSID.move(this.DSUSS);
      PXCHKPRL.PXDEVD.move(this.DSJNA);
      IN92 = PXCHKPRL.CCHKPRL();
      if (PXCHKPRL.PXLER == '1') {
         IN61 = true;
      } else {
         IN61 = false;
      }
      //   Start parameters
      SYSTP.setPGNM().move(this.DSPGM);
      CSYSTPSyncTo();
      IN91 = !SYSTP.CHAIN("00", KSYSTP());
      CSYSTPSyncFrom();
      if (IN91) {
         CSYSTPSyncTo();
         CSYSTPSyncTo();
         SYSTP.clearNOKEY("00");
         CSYSTPSyncFrom();
         FAS145DS.setFAS145DS().clear();
         moveToIN(41, "10");
      } else {
         FAS145DS.setFAS145DS().moveLeft(SYSTP.getPARA());
         moveToIN(41, "01");
      }
      DSP.WWASID.move(FAS145DS.getZVASID());
      DSP.WWSBNO = FAS145DS.getZVSBNO();
      if (IN88) {
         DSP.WWDIVI.move(FAS145DS.getZVDIVI());
      } else {
         DSP.WWDIVI.move(LDAZD.DIVI);
      }
      DSP.WWVTXT.move(FAS145DS.getZVVTXT());
      DSP.WWLITX.move(FAS145DS.getZVLITX());
      this.XXCHNO = SYPAR.getCHNO();
      this.PXDFMI.move("YMD8");
      this.PXDATI = SYPAR.getRGDT();
      this.PXDFMO.moveLeftPad(LDAZD.DTFM);
      this.PXOPRM = 1;
      this.PXDSEP = LDAZD.DSEP;
      COMDAT();
      DSP.WWRGDT.moveLeft(this.PXDALO);
      this.PXDFMI.move("YMD8");
      this.PXDATI = SYPAR.getLMDT();
      this.PXDFMO.moveLeftPad(LDAZD.DTFM);
      this.PXOPRM = 1;
      this.PXDSEP = LDAZD.DSEP;
      COMDAT();
      DSP.WWLMDT.moveLeft(this.PXDALO);
   }

   /**
   *    PADSP - Display
   */
   public void PADSP_F23() {
   }

   public void PADSP_F13() {
   }

   public void PADSP_F15() {
      picSetMethod('D');
      apCall("CWRKSPL");
      return;
   }

   public void PADSP_F14() {
      picSetMethod('D');
      this.PXCONO = LDAZD.CONO;
      this.PXDIVI.move(LDAZD.DIVI);
      PXCHKPRL.PXPRTF.moveLeft("FAS146PF");
      PXCHKPRL.PXUSID.move(this.DSUSS);
      PXCHKPRL.PXDEVD.move(this.DSJNA);
      //   CALL=MNS215 Display List Layout
      PXMNS215SyncTo();
      IN92 = PXMNS215.MNS215();
      PXMNS215SyncFrom();
      return;
   }

   public String PADSP_getWriteNames() {
      return "A0";
   }

   public String PADSP_getReadNames() {
      return "A0";
   }

   /**
   *    PACHK - Check
   */
   public void PACHK() {
      IN63 = false;
      DSP.WWFAQT.clear();
      //   If MUF, check division
      if (IN88) {
         if (DSP.WWDIVI.isBlank()) {
            //   - Division must be entered
            picSetMethod('D');
            IN60 = true;
            DSP.setFocus(6, 18, "WWDIVI");
            //   MSGID=WDI0102 Division must be entered
            COMPMQ("WDI0102");
            return;
         }
         //   Check division
         MNDIV.setCONO(LDAZD.CONO);
         MNDIV.setDIVI().move(DSP.WWDIVI);
         IN91 = !MNDIV.CHAIN("00", MNDIV.getKey("00"));
         IN92 = MNDIV.getErr("00");
         if (IN91) {
            picSetMethod('D');
            IN60 = true;
            DSP.setFocus(6, 18, "WWDIVI");
            //   MSGID=WDI0103 Division &1 does not exist
            COMPMQ("WDI0103", formatToString(DSP.WWDIVI));
            return;
         }
         //   Check access authority for division
         PLCHKAD.ADCONO = LDAZD.CONO;
         PLCHKAD.ADCMTP = LDAZD.CMTP;
         PLCHKAD.ADFDIV.move(DSP.WWDIVI);
         PLCHKAD.ADTDIV.clear();
         PLCHKAD.ADRESP.move(LDAZD.RESP);
         IN92 = PLCHKAD.CCHKACD();
         if (PLCHKAD.ADAERR == 1) {
            picSetMethod('D');
            IN60 = true;
            DSP.setFocus(6, 18, "WWDIVI");
            COMPMQ(PLCHKAD.ADMSGI, formatToString(PLCHKAD.ADMSGD));
            return;
         }
         //   Read CRS750 for PBACBC (Book of account used ?)
         XSDIVI.move(SYPAR.getDIVI());
         SYPAR.setCONO(LDAZD.CONO);
         SYPAR.setDIVI().move(DSP.WWDIVI);
         SYPAR.setSTCO().moveLeft("CRS750");
         IN91 = !SYPAR.CHAIN("00", SYPAR.getKey("00"));
         IN92 = SYPAR.getErr("00");
         //   If not found and MUF/central ICF, try division blank (central)
         if (IN91 && LDAZD.CMTP == 2 && !SYPAR.getDIVI().isBlank()) {
            SYPAR.setDIVI().clear();
            IN91 = !SYPAR.CHAIN("00", SYPAR.getKey("00"));
            IN92 = SYPAR.getErr("00");
         }
         if (!IN91) {
            CRS750DS.setCRS750DS().moveLeft(SYPAR.getPARM());
         } else {
            CRS750DS.setCRS750DS().clear();
         }
         SYPAR.setDIVI().move(XSDIVI);
      } else {
         MNDIV.setCONO(LDAZD.CONO);
         MNDIV.setDIVI().move(DSP.WWDIVI);
         IN91 = !MNDIV.CHAIN("00", MNDIV.getKey("00"));
         IN92 = MNDIV.getErr("00");
      }
      //   - Asset id must be entered
      if (DSP.WWASID.isBlank()) {
         picSetMethod('D');
         IN60 = true;
         DSP.setFocus(7, 18, "WWASID");
         //   MSGID=WAS3002 Fixed asset must be entered
         COMPMQ("WAS3002");
         return;
      }
      //   - Subnumber must be entered
      if (DSP.WWSBNO == 0) {
         picSetMethod('D');
         IN60 = true;
         DSP.setFocus(7, 31, "WWSBNO");
         //   MSGID=WSB3002 Subnumber must be entered
         COMPMQ("WSB3002");
         return;
      }
      //   - Scrap for assets is running
      FCHKF.setSTCO().clear();
      FCHKF.setSTCO().moveLeft("FAS145");
      FCHKF.setKFL1().clear();
      XCASID.move(DSP.WWASID);
      XCSBNO.move(DSP.WWSBNO);
      XCDIVI.move(DSP.WWDIVI);
      FCHKF.setKFL1().move(XCKFL1);
      IN91 = !FCHKF.CHAIN("00", FCHKF.getKey("00", 4));
      if (!IN91) {
         picSetMethod('D');
         IN60 = true;
         IN62 = true;
         DSP.setFocus(7, 18, "WWASID");
         //   MSGID=FA14506 Disposition for selected fixed asset in progress
         COMPMQ("FA14506");
         return;
      }
      FASMA.setASID().move(DSP.WWASID);
      FASMA.setSBNO(DSP.WWSBNO);
      FASMA.setDIVI().move(DSP.WWDIVI);
      IN91 = !FASMA.CHAIN("00", FASMA.getKey("00"));
      IN92 = FASMA.getErr("00");
      if (IN91) {
         picSetMethod('D');
         IN60 = true;
         DSP.setFocus(7, 18, "WWASID");
         //   MSGID=FA14513 Fixed asset &1 subnumber &2 does not exist
         XMASID.move(DSP.WWASID);
         XMSBNO.move(DSP.WWSBNO);
         COMPMQ("FA14513", formatToString(XXSMSG));
         return;
      } else {
         DSP.WWFADS.move(FASMA.getFADS());
      }
      if (!IN91 && FASMA.getFAST() > 4 && FASMA.getFAST() != 8) {
         picSetMethod('D');
         IN60 = true;
         DSP.setFocus(7, 18, "WWASID");
         if (FASMA.getFAST() == 9) {
            //   MSGID=FA14511 Fixed asset &1 subnumber &2 has been sold
            this.MSGID.move("FA14511");
         }
         if (FASMA.getFAST() == 9 && FASMA.getPYNO().isBlank()) {
            //   MSGID=FA14510 Fixed asset &1 subnumber &2 is disposed
            this.MSGID.move("FA14510");
         }
         if (FASMA.getFAST() == 7) {
            //   MSGID=FA14509 Fixed asset &1 subnumber &2 has budget status
            this.MSGID.move("FA14509");
         }
         if (FASMA.getFAST() == 6) {
            //   MSGID=FA14512 Fixed asset &1 subnumber &2 has service order status
            this.MSGID.move("FA14512");
         }
         if (FASMA.getFAST() == 5) {
            //   MSGID=FA14508 Fixed asset &1 subnumber &2 has been preliminarily enter
            this.MSGID.move("FA14508");
         }
         XMASID.move(DSP.WWASID);
         XMSBNO.move(DSP.WWSBNO);
         this.MSGDTA.moveLeft(XXSMSG);
         COMPMQ();
         return;
      }
   }

   /**
   *    PAUPD - Update
   */
   public void PAUPD() {
      CSYSTPSyncTo();
      IN91 = !SYSTP.CHAIN_LOCK("00", KSYSTP());
      IN92 = SYSTP.getErr("00");
      CSYSTPSyncFrom();
      FAS145DS.setZVASID().move(DSP.WWASID);
      FAS145DS.setZVSBNO(DSP.WWSBNO);
      FAS145DS.setZVDIVI().move(DSP.WWDIVI);
      FAS145DS.setZVLITX().move(DSP.WWLITX);
      FAS145DS.setZVPGNA().move(this.DSPGM);
      SYSTP.setPARA().moveLeftPad(FAS145DS.getFAS145DS());
      this.PXDFMI.move("TIME");
      this.PXDATI = 0;
      this.PXDFMO.move("YMD8");
      this.PXOPRM = 1;
      COMDAT();
      SYPAR.setLMDT(this.PXDATO);
      SYPAR.setCHID().move(this.DSUSS);
      SYPAR.setCHNO(SYPAR.getCHNO() + 1);
      this.XXCHNO = SYPAR.getCHNO();
      if (!IN91) {
         CSYSTPSyncTo();
         SYSTP.UPDAT("00");
      } else {
         SYPAR.setRGDT(SYPAR.getLMDT());
         SYPAR.setRGTM(movexTime());
         CSYSTPSyncTo();
         SYSTP.WRITE("00");
      }
      //   Next step
      picSetMethod('D');
      picPush('B', 'I');
   }

   /**
   *    PAPMT - Prompt
   */
   public boolean PAPMT() {
      //   Next step
      picSetMethod('D');
      DSP.restoreFocus();
      //   Prompt fixed assets master file
      if (DSP.hasFocus(7, 18, 7, 33, "WWASID")) {
         if (DSP.X0CPOS != 28) {
            if (DSP.X0CPOS != 29) {
               if (DSP.X0CPOS != 30) {
                  this.PXFILE.moveLeft("FFASMA06");
                  this.PXMBR.clear();
                  this.PXOPT = '1';
                  this.PXKVA1.moveLeft(LDAZD.CONO, 3);
                  this.PXKVA2.moveLeft(DSP.WWDIVI);
                  this.PXKVA3.moveLeft(DSP.WWASID);
                  this.PXKVA4.moveLeft(DSP.WWSBNO, 3);
                  this.PXKTY1.move("EQ");
                  this.PXKTY2.move("EQ");
                  this.PXKTY3.clear();
                  this.PXFLD1.moveLeftPad("FMASID");
                  this.PXFLD2.moveLeftPad("FMSBNO");
                  this.PXFLD3.moveLeftPad("FMFADS");
                  this.PXF11 = '1';
                  COMF04();
                  if (this.PXTPGM.EQ("CRS990")) {
                     DSP.WWASID.moveLeft(this.PXKVA3);
                     DSP.WWDIVI.moveLeft(this.PXKVA2);
                     DSP.WWSBNO = this.PXKVA4.getIntLeft(3);
                  }
                  //   Check if jump to work with fixed assets should be done
                  if (this.PXFKEY == F11) {
                     this.PXPGNM.clear();
                     this.PXPGNM.moveLeft("FAS001");
                     COMSTK();
                     if (this.PXSTER == '1') {
                     //   MSGID=XST0001 The program &1 is not now available due to a recursive c
                        COMPMQ("XST0001", formatToString(this.PXPGNM));
                     } else {
                        LDAZZ.PICC.move("BI");
                        XFPGNM.move(LDAZZ.FPNM);
                        LDAZZ.FPNM.move(this.DSPGM);
                        LDAZZ.TPGM.clear();
                        LDAZZ.TDTA.clear();
                        LDAZZ.TDA1.clear();
                        LDAZZ.TDA2.clear();
                        LDAZZ.TDA3.clear();
                        LDAZZ.TDA1.moveLeft(DSP.WWASID);
                        LDAZZ.TDA2.moveLeft(DSP.WWSBNO, 3);
                        LDAZZ.TDA3.moveLeft('1');
                        LDAZZ.DIVI.move(DSP.WWDIVI);
                        //   CALL=FAS001 Fixed Asset. Open
                        apCall("FAS001");
                        if (LDAZZ.TPGM.EQ("FAS001")) {
                           DSP.WWASID.moveLeft(LDAZZ.TDA1);
                           DSP.WWSBNO = LDAZZ.TDA2.getIntLeft(3);
                           DSP.WWDIVI.move(LDAZZ.DIVI);
                        }
                        LDAZZ.FPNM.move(XFPGNM);
                     }
                  }
                  return true;
               }
            }
         }
      }
      // ----------------------------------------------------------------
      //   Prompt divisions
      //   If central unit
      if (IN88) {
         if (DSP.X0CLIN == 6) {
            if (DSP.X0CPOS >= 18 && DSP.X0CPOS <= 20) {
               this.PXFILE.moveLeft("CMNDIV00");
               this.PXMBR.clear();
               this.PXOPT = '1';
               this.PXKVA1.moveLeft(LDAZD.CONO, 3);
               this.PXKVA2.moveLeft(DSP.WWDIVI);
               this.PXKTY1.move("EQ");
               this.PXFLD1.moveLeftPad("CCCONO");
               this.PXFLD2.moveLeftPad("CCDIVI");
               this.PXFLD3.moveLeftPad("CCCONM");
               this.PXF11 = '1';
               COMF04();
               if (this.PXTPGM.EQ("CRS990")) {
                  DSP.WWDIVI.moveLeft(this.PXKVA2);
               }
               //   Check if jump to work with divisions
               if (this.PXFKEY == F11) {
                  this.PXPGNM.clear();
                  this.PXPGNM.moveLeft("MNS100");
                  COMSTK();
                  if (this.PXSTER == '1') {
                  //   MSGID=XST0001 The program &1 is not now available due to a recursive c
                     COMPMQ("XST0001", formatToString(this.PXPGNM));
                  } else {
                     LDAZZ.PICC.move("BI");
                     XFPGNM.move(LDAZZ.FPNM);
                     LDAZZ.FPNM.move(this.DSPGM);
                     LDAZZ.TPGM.clear();
                     LDAZZ.CONO = LDAZD.CONO;
                     LDAZZ.DIVI.move(DSP.WWDIVI);
                     //   CALL=MNS100 Company. Connect Division
                     apCall("MNS100");
                     if (LDAZZ.TPGM.EQ("MNS100")) {
                        DSP.WWDIVI.move(LDAZZ.DIVI);
                     }
                     LDAZZ.FPNM.move(XFPGNM);
                  }
               }
               return true;
            }
         }
      }
      //   MSGID=XF04001 F4 is not permitted in this position on the panel
      COMPMQ("XF04001");
      return true;
   }

   /**
   *    PBINZ - Init
   */
   public void PBINZ() { 
      totalDPTP = 0;
      disposeErrorShown = false;
      disposeErrorShownPBCHK = false;
      forDisposeDPTP = 0;
      //   - Position cursor
      DSP.setFocus(7, 2, "WWDPTP");
      //   - Init of subfile information
      XXDPTP = DSP.WWDPTP;
      XLDPTP = 0;
      this.WSRGTM = movexTime();
      DSP.XBRRNA = 0;
      DSP.XBRRNM = 0;
      IN94 = true;
      IN95 = false;
      DSP.clearSFL("BC");
      IN94 = false;
      IN95 = false;
      IN96 = false;
      //   - Retrieve parameter for general ledger
      SYPAR.setDIVI().move(DSP.WWDIVI);
      SYPAR.setSTCO().moveLeft("CRS750");
      IN91 = !SYPAR.CHAIN("00", SYPAR.getKey("00"));
      IN92 = SYPAR.getErr("00");
      //   If not found and MUF, try division blank (central)
      if (IN91 && LDAZD.CMTP == 2 && !SYPAR.getDIVI().isBlank()) {
         SYPAR.setDIVI().clear();
         IN91 = !SYPAR.CHAIN("00", SYPAR.getKey("00"));
         IN92 = SYPAR.getErr("00");
      }
      if (!IN91) {
         CRS750DS.setCRS750DS().moveLeft(SYPAR.getPARM());
      }
      //   - Retrieve parameter for purchasing of assets
      SYPAR.setDIVI().move(DSP.WWDIVI);
      SYPAR.setSTCO().moveLeft("FAS900");
      IN91 = !SYPAR.CHAIN("00", SYPAR.getKey("00"));
      IN92 = SYPAR.getErr("00");
      if (!IN91) {
         FAS900DS.setFAS900DS().moveLeft(SYPAR.getPARM());
      }
      if (XXMUNI == 1 && DSP.WWDIVI.NE(XMDIVI)) {
         SYTAB.setDIVI().move(DSP.WWDIVI);
         SYTAB.setSTCO().moveLeftPad("MUST");
         SYTAB.setSTKY().clear();
         SYTAB.setLNCD().clear();
         IN91 = !SYTAB.CHAIN("00", SYTAB.getKey("00"));
         if (!IN91) {
            DSMUST.setDSMUST().moveLeft(SYTAB.getPARM());
         } else {
            DSMUST.setDSMUST().clear();
         }
         if (DSMUST.getMCMUST() == 2 && MNDIV.getLOCD().NE(XCLOCD)) {
            XSDIVI.move(SYTAB.getDIVI());
            SYTAB.setDIVI().move(DSP.WWDIVI);
            SYTAB.setSTCO().moveLeftPad("MUCU");
            SYTAB.setSTKY().moveLeftPad(MNDIV.getLOCD());
            SYTAB.setLNCD().clear();
            IN91 = !SYTAB.CHAIN("00", SYTAB.getKey("00"));
            SYTAB.setDIVI().move(XSDIVI);
            if (!IN91) {
               DSMUCU.setDSMUCU().moveLeft(SYTAB.getPARM());
            } else {
               DSMUCU.setDSMUCU().clear();
            }
            XCLOCD.move(MNDIV.getLOCD());
         }
         XMDIVI.move(DSP.WWDIVI);
      }
      //   Retrieve value types for Total acquisition values
      FASVL.setCONO(LDAZD.CONO);
      FASVL.setDIVI().move(DSP.WWDIVI);
      FASVL.setASID().move(FASMA.getASID());
      FASVL.setSBNO(FASMA.getSBNO());
      FASVL.setVATP(FAS900DS.getFAVATT());
      IN91 = !FASVL.CHAIN("00", FASVL.getKey("00"));
      this.PXALPH.clear();
      XXPVAL = 0d;
      XXSCVA = 0d;
      XXSCVC = 0d;
      if (!IN91) {
         if (!isBlank(FASVL.getFAVA(), EPS_2)) {
            //   Convert base value to alpha
            this.PXDCCD = LDAZD.LCDC;
            this.PXFLDD = 13 + this.PXDCCD;
            this.PXEDTC = 'J';
            this.PXDCFM = LDAZD.DCFM;
            this.PXNUM = FASVL.getFAVA();
            XXPVAL = FASVL.getFAVA();
            XXSCVA = FASVL.getFAVA();
            XXPVAC = FASVL.getFAVC();
            XXSCVC = FASVL.getFAVC();
            XVCUCD.move(FASVL.getCUCD());
            SRCOMNUM.COMNUM();
         }
         DSP.WWPVAL.moveRight(this.PXALPH);
      }
      FASDM.setASID().move(FASMA.getASID());
      FASDM.setSBNO(FASMA.getSBNO());
      FASDM.setDIVI().move(FASMA.getDIVI());
      populateAllowedDPTP();
      //   - Set limit & build subfile
      FDTSC.setDIVI().move(DSP.WWDIVI);
      FDTSC.setDPTP(DSP.WWDPTP);
      FDTSC.SETLL_SCAN("00", FDTSC.getKey("00"));
      PBINZ_SETLL();
      PBROL();
   }

   /**
   *    PBDSP - Display
   */
   public void PBDSP_F13() {
   }

   public void PBDSP_F04() {
   }

   public void PBDSP_F14() {
      IN60 = false;  
      if (!disposeErrorShown) {  
         if (checkIfAllDisposableDPTP(LDAZD.CONO, DSP.WWDIVI, DSP.WWASID, DSP.WWSBNO)) {
            if (totalDPTP == 1) {
               validateDPTPError(LDAZD.CONO, DSP.WWDIVI, DSP.WWASID, DSP.WWSBNO, notValidDisposableDPTP);
               if (!continueDispose) {
                  picSetMethod('D');   
                  IN60 = true;   
                  DSP.setFocus("WWFAQT");
                  disposeErrorShown = true;
               } else {
                  picSetMethod('D');
                  picPush('E', 'I');
               }
            }else {
               picSetMethod('D');
               picPush('E', 'I');
            }
         } else if (validateDPTPError(LDAZD.CONO, DSP.WWDIVI, DSP.WWASID, DSP.WWSBNO, notValidDisposableDPTP)) {
            if (retValidation) {
               disposeErrorShown = false;
            } else {
               disposeErrorShown = true;
            }
            if (!continueDispose) {
               picSetMethod('D');   
               IN60 = true;   
               DSP.setFocus("WWFAQT");
            } else {
               picSetMethod('D');
               picPush('E', 'I');
            }
         } else {    
            picSetMethod('D');   
            IN60 = true;   
            DSP.setFocus("WWFAQT");
            disposeErrorShown = true;    
         
         }  
      } else {    
         if (totalDPTP == 1) {
            disposeSingleDPTP(LDAZD.CONO, DSP.WWDIVI, DSP.WWASID, DSP.WWSBNO, notValidDisposableDPTP);
            picSetMethod('D');
            picPush('E', 'I');
         } else {
            picSetMethod('D');   
            IN60 = true;   
            DSP.setFocus("WWFAQT");    
            //  MSGID=FA14515 Depreciation type &1 must be disposed first  
            COMPMQ("FA14515", String.valueOf(notValidDisposableDPTP));  
            disposeErrorShown = false;    
            notValidDisposableDPTP = 0;   
         } 
      }
      return;
   }
   
   public String PBDSP_getWriteNames() {
      return "BF,BC";
   }

   public String PBDSP_getReadNames() {
      return "BC";
   }

   /**
   *    PBCHK - Check
   */
   public void PBCHK() {
      IN60 = false;
      DSP.WBRRNA = 0;
      DSP.WSOPT2.clear();
      FDTSC.setDPTP(DSP.WWDPTP);
      //   New start position
PBCHKX:
      do // goto PBCHKX loop
      {
         if (DSP.WWDPTP != XXDPTP) {
            picSetMethod('I');
            break PBCHKX;
         }
         forDisposeDPTP = 0;
         if (DSP.WSOPT2.isBlank() && DSP.XBRRNM != 0) {
            do {
               DSP.WBRRNA = 0;
               IN93 = DSP.readSFL("BS");
               forDisposeDPTP = DSP.WSDPTP;
            } while (!(IN93 || !DSP.WSOPT2.isBlank()));
         }
         //       No options
         if (DSP.WSOPT2.isBlank()) {
            picSetMethod('I');
            break PBCHKX;
         }
         //   Check option
         this.XXOPT2.move(DSP.WSOPT2);
         if (PxCHKoption()) {
            break PBCHKX;
         }
         if (this.XXOPT2.EQ("11")) {
            option11 = true;
         }
         disposeAcqValue = false;
         if (totalDPTP > 1) {    
            if (forDisposeDPTP > 0 && this.XXOPT2.EQ("11")) {  
               if (hasValidDisposableDPTP(LDAZD.CONO, DSP.WWDIVI, DSP.WWASID, DSP.WWSBNO)) {    
                  if (hasOtherDisposableDPTP(LDAZD.CONO, DSP.WWDIVI, DSP.WWASID, DSP.WWSBNO, forDisposeDPTP, false)) {
                     willReverseAcqValue = false;
                     disposeSingleDPTP(LDAZD.CONO, DSP.WWDIVI, DSP.WWASID, DSP.WWSBNO, forDisposeDPTP);
                  } else {    
                     willReverseAcqValue = true;   
                  } 
                  if (checkIfAllDisposableDPTP(LDAZD.CONO, DSP.WWDIVI, DSP.WWASID, DSP.WWSBNO)) {     
                     picSetMethod('D');   
                     picPush('E', 'I');   
                  } else {       
                     if (notValidDisposableDPTP != forDisposeDPTP) {    
                        picSetMethod('D');   
                        IN60 = true;   
                        DSP.setFocus("WWFAQT");    
                        //  MSGID=FA14515 Depreciation type &1 must be disposed first  
                        COMPMQ("FA14515", String.valueOf(notValidDisposableDPTP));     
                        disposeErrorShown = false;    
                        notValidDisposableDPTP = 0;   
                     } else {    
                        picSetMethod('D');   
                        picPush('E', 'I');   
                     }  
                  }  
               } else {
                  if (!disposeErrorShownPBCHK) {
                     picSetMethod('D');   
                     IN60 = true;   
                     DSP.setFocus("WWFAQT"); 
                     validateDPTPError(LDAZD.CONO, DSP.WWDIVI, DSP.WWASID, DSP.WWSBNO, notValidDisposableDPTP);
                     disposeErrorShownPBCHK = true;
                  }
               } 
               break PBCHKX;  
            }  
            if (disposeErrorShown && notValidDisposableDPTP !=  0) {    
               picSetMethod('D');   
               IN60 = true;   
               DSP.setFocus("WWFAQT");    
               //  MSGID=FA14515 Depreciation type &1 must be disposed first  
               COMPMQ("FA14515", String.valueOf(notValidDisposableDPTP));  
               disposeErrorShown = false;    
               notValidDisposableDPTP = 0;   
            }  
         } else {    
            forDisposeDPTP = 0;  
            PBDSP_F14();
         }
         //   No options
         if (DSP.WSOPT2.isBlank()) {
            picSetMethod('D');
         }
         break;
      } while (true);
      option11 = false;
      //   Sflnxtchg
      PBCHK_ExecuteOption();
      if (DSP.WBRRNA != 0 && IN60) {
         IN60 = true;
         DSP.updateSFL("BS");
      }
   }

   /**
   *    PBUPD - Update
   */
   public void PBUPD() {
      //   Next step
      if (DSP.WBRRNA != 0) {
         picSetMethod('C');
      } else {
         picSetMethod('I');
      }
      //   F12=Previous
      if (F12 == DSP.X0FKEY) {
         picSetMethod('D');
      }
      //   No subfile record
      if (DSP.WBRRNA == 0) {
         return;
      }
      //   - F12 was pressed and not copy from picture E
      if (F12 == DSP.X0FKEY) {
         picSetMethod('D');
         IN60 = true;
         this.XLPIC1 = picGetPrevTopPanel();
         if (this.XLPIC1 == 'C') {
            IN60 = true;
            DSP.updateSFL("BS");
            return;
         }
      }
   }

   /**
   *    PBROL - Roll subfile
   */
   public void PBROL() {
      DSP.XBRRNA = DSP.XBRRNM;
      DSP.restPosSFL('B');
      //   Restore last position
      if (DSP.XBRRNM > 0) {
         FDTSC.setDPTP(XLDPTP);
         FDTSC.SETGT_SCAN("00", FDTSC.getKey("00"));
         PBROL_SETGT();
      }
      IN93 = !FDTSC.READE("00", FDTSC.getKey("00", 2));
      IN92 = FDTSC.getErr("00");
      PBROL_READE1();
      while (!IN93 && this.XXSPGB > 0) {
         FASDM.setDPTP(FDTSC.getDPTP());
         //   - Check record
         IN91 = !FASDM.CHAIN("00", FASDM.getKey("00"));
         IN92 = FASDM.getErr("00");
         if (!IN91) {
            if (FDTSC.isQualifiedForSLF() && FASDM.getFAST() != 9) {
               this.XXSPGB--;
               DSP.XBRRNA++;
               DSP.WBRRNA = DSP.XBRRNA;
               DSP.XBRRNM++;
               XLDPTP = FDTSC.getDPTP();
               PBMSF();
               IN60 = true;
               DSP.writeSFL("BS");
               totalDPTP++;
            }
         }
         //   Read records
         IN93 = !FDTSC.READE("00", FDTSC.getKey("00", 2));
         IN92 = FDTSC.getErr("00");
         PBROL_READE2();
      }
      if (IN93) {
         IN96 = true;
         DSP.writeEofSFL("BS");
      }
      //   If records exists then display the subfile
      if (DSP.XBRRNM > 0) {
         IN95 = true;
         DSP.XBRRNA = DSP.XBRRNM - 1;
         DSP.trunkPosSFL('B', 13);
         DSP.XBRRNA++;
      }
   }

   /**
   *    PBMSF - Move information to subfile fields
   */
   public void PBMSF() {
      DSP.moveDSintoSFLpre("B");// do standard sync for SFL buf
      PBMSF_AfterSFLPre();
      DSP.WSDPTP = FDTSC.getDPTP();
      DSP.WSVATP = FDTSC.getVATP();
      //   Retrieve depreciation types descriptions
      SYTAB.setSTCO().moveLeft("DPTP");
      SYTAB.setDIVI().move(DSP.WWDIVI);
      SYTAB.setSTKY().clear();
      SYTAB.setSTKY().moveLeft(FDTSC.getDPTP(), 2);
      SYTAB.setLNCD().clear();
      IN91 = !SYTAB.CHAIN("00", SYTAB.getKey("00"));
      IN92 = SYTAB.getErr("00");
      if (!IN91) {
         DSP.WSTX15.move(SYTAB.getTX15());
         DSDPTP.setDSDPTP().moveLeft(SYTAB.getPARM());
         DSP.WSADPT = DSDPTP.getFAADPT();
      } else {
         DSP.WSTX15.clear();
         DSP.WSADPT = 0;
      }
      //   - Retrieve start value  for accumulated depreciation
      FASVL.setASID().move(DSP.WWASID);
      FASVL.setSBNO(DSP.WWSBNO);
      FASVL.setDIVI().move(DSP.WWDIVI);
      FASVL.setVATP(FASDM.getBVAT());
      IN91 = !FASVL.CHAIN("00", FASVL.getKey("00"));
      IN92 = FASVL.getErr("00");
      if (!IN91 && !isBlank(FASVL.getFAVA(), EPS_2)) {
         XXPVAL = FASVL.getFAVA();
         XXPVAC = FASVL.getFAVC();
         WYPVAL = FASVL.getFAVC();
         DSP.WXPVAL = FASVL.getFAVA();
         DSP.WXADEP = FASVL.getFAVC();
      } else {
         XXPVAL = 0d;
         XXPVAC = 0d;
      }
      //   - Retrieve value type accumulated depreciations
      XCACDP.move(0d);
      DSP.WSDVAL.clear();
      DSP.WSSVAL.clear();
      FASVL.setASID().move(DSP.WWASID);
      FASVL.setSBNO(DSP.WWSBNO);
      FASVL.setDIVI().move(DSP.WWDIVI);
      FASVL.setVATP(FASDM.getVTAD());
      XXACDP = 0d;
      DSP.WXACDP = 0d;
      DSP.WXADTY = 0d;
      DSP.WXEODP = 0d;
      DSP.WXPVAL = 0d;
      DSP.WXRVAL = 0d;
      IN91 = !FASVL.CHAIN("00", FASVL.getKey("00"));
      IN92 = FASVL.getErr("00");
      if (!IN91) {
         //   Convert acc depreciations to alpha
         this.PXALPH.clear();
         if (!isBlank(FASVL.getFAVA(), EPS_2)) {
            XXACDP = FASVL.getFAVA();
            XXACDC = FASVL.getFAVC();
         }
      }
      //   Retrieve value types for accelerated depreciations
      FDTED.setDIVI().move(DSP.WWDIVI);
      FDTED.setDPTP(FDTSC.getDPTP());
      IN91 = !FDTED.CHAIN("00", FDTED.getKey("00"));
      IN92 = FDTED.getErr("00");
      if (!IN91) {
         FASVL.setASID().move(DSP.WWASID);
         FASVL.setSBNO(DSP.WWSBNO);
         FASVL.setDIVI().move(DSP.WWDIVI);
         FASVL.setVATP(FDTED.getVATP());
         DSP.WXVTED = FDTED.getVATP();
         IN91 = !FASVL.CHAIN("00", FASVL.getKey("00"));
         IN92 = FASVL.getErr("00");
         if (!IN91) {
            if (!isBlank(FASVL.getFAVA(), EPS_2)) {
               XXACDP += FASVL.getFAVA();
               DSP.WXEODP = FASVL.getFAVA();
               XXACDC += FASVL.getFAVC();
            }
         }
      }
      //   - Retrieve value type accumulated depreciations this year
      FASVL.setASID().move(DSP.WWASID);
      FASVL.setSBNO(DSP.WWSBNO);
      FASVL.setDIVI().move(DSP.WWDIVI);
      FASVL.setVATP(FASDM.getADTY());
      IN91 = !FASVL.CHAIN("00", FASVL.getKey("00"));
      IN92 = FASVL.getErr("00");
      if (!IN91) {
         if (!isBlank(FASVL.getFAVA(), EPS_2)) {
            XXACDP += FASVL.getFAVA();
            XXACDC += FASVL.getFAVC();
         }
      }
      //   Convert total depreciation values to alpha
      this.PXALPH.clear();
      if (!isBlank(XXACDP, EPS_2)) {
         this.PXDCCD = LDAZD.LCDC;
         this.PXFLDD = 13 + this.PXDCCD;
         this.PXEDTC = 'J';
         this.PXDCFM = LDAZD.DCFM;
         this.PXNUM = XXACDP;
         SRCOMNUM.COMNUM();
      }
      DSP.WSDVAL.moveRight(this.PXALPH);
      //   Calculate and convert rest value to alpha
      XXRVAL = 0d;
      XXRVAC = 0d;
      XXRVAL = XXPVAL - XXACDP;
      XXRVAC = XXPVAC - XXACDC;
      DSP.WXPVAL = XXPVAL;
      this.PXALPH.clear();
      if (!isBlank(XXRVAL, EPS_2)) {
         this.PXDCCD = LDAZD.LCDC;
         this.PXFLDD = 13 + this.PXDCCD;
         this.PXEDTC = 'J';
         this.PXDCFM = LDAZD.DCFM;
         this.PXNUM = XXRVAL;
         SRCOMNUM.COMNUM();
      }
      DSP.WSSVAL.moveRight(this.PXALPH);
      PBMSF_BeforeSFLPost();
      DSP.moveDSintoSFLpost("B");// do standard sync for SFL buf
   }

   /**
   *    PEINZ - Init
   */
   public void PEINZ() {
      IN62 = true;
      disposeErrorShown = false;
      //   Check printerfile layout if update
      this.PXCONO = LDAZD.CONO;
      this.PXDIVI.move(LDAZD.DIVI);
      PXCHKPRL.PXPRTF.moveLeft("GLS041PF");
      PXCHKPRL.PXUSID.move(this.DSUSS);
      PXCHKPRL.PXDEVD.move(this.DSJNA);
      IN92 = PXCHKPRL.CCHKPRL();
      if (PXCHKPRL.PXLER == '1') {
         IN61 = true;
      } else {
         IN61 = false;
      }
      //   Start parameters
      SYPAR.setCONO(LDAZD.CONO);
      SYSTP.setPGNM().move(this.DSPGM);
      CSYSTPSyncTo();
      IN91 = !SYSTP.CHAIN("00", KSYSTP());
      CSYSTPSyncFrom();
      if (IN91) {
         CSYSTPSyncTo();
         CSYSTPSyncTo();
         SYSTP.clearNOKEY("00");
         CSYSTPSyncFrom();
         FAS145DS.setFAS145DS().clear();
         FAS145DS.setZVDIVI().move(DSP.WWDIVI);
         moveToIN(41, "10");
      } else {
         FAS145DS.setFAS145DS().moveLeft(SYSTP.getPARA());
         moveToIN(41, "01");
      }
      DSP.WWDIVI.move(FAS145DS.getZVDIVI());
      DSP.WWLITX.move(FAS145DS.getZVLITX());
      DSP.WWVTXT.move(FAS145DS.getZVVTXT());
      this.XXCHNO = SYPAR.getCHNO();
      this.PXDFMI.move("YMD8");
      this.PXDATI = SYPAR.getRGDT();
      this.PXDFMO.moveLeftPad(LDAZD.DTFM);
      this.PXOPRM = 1;
      this.PXDSEP = LDAZD.DSEP;
      COMDAT();
      DSP.WWRGDT.moveLeft(this.PXDALO);
      this.PXDFMI.move("YMD8");
      this.PXDATI = SYPAR.getLMDT();
      this.PXDFMO.moveLeftPad(LDAZD.DTFM);
      this.PXOPRM = 1;
      this.PXDSEP = LDAZD.DSEP;
      COMDAT();
      DSP.WWLMDT.moveLeft(this.PXDALO);
      if (DSP.WWFAQT.isBlank()) {
         //   Convert quantity base value to alpha
         this.PXDCCD = 0;
         this.PXFLDD = 9;
         this.PXEDTC = 'M';
         this.PXDCFM = ' ';
         this.PXALPH.clear();
         this.PXNUM = FASMA.getFAQT();
         SRCOMNUM.COMNUM();
         DSP.WWFAQT.moveRight(this.PXALPH);
      }
   }

   /**
   *    PEDSP - Display
   */
   public void PEDSP_F23() {
   }

   public void PEDSP_F13() {
   }

   public void PEDSP_F15() {
      picSetMethod('D');
      apCall("CWRKSPL");
      return;
   }

   public void PEDSP_F14() {
      picSetMethod('D');
      this.PXCONO = LDAZD.CONO;
      this.PXDIVI.move(LDAZD.DIVI);
      PXCHKPRL.PXPRTF.moveLeft("FAS146PF");
      PXCHKPRL.PXUSID.move(this.DSUSS);
      PXCHKPRL.PXDEVD.move(this.DSJNA);
      //   CALL=MNS215 Display List Layout
      PXMNS215SyncTo();
      IN92 = PXMNS215.MNS215();
      PXMNS215SyncFrom();
      return;
   }

   public String PEDSP_getWriteNames() {
      return "E0";
   }

   public String PEDSP_getReadNames() {
      return "E0";
   }

   /**
   *    PECHK - Check
   */
   public void PECHK() {
      //   - Accounting date must be entered
      if (DSP.WWACDT.isBlank()) {
         picSetMethod('D');
         IN60 = true;
         DSP.setFocus(7, 18, "WWACDT");
         //   MSGID=WACD102 Accounting date must be entered
         COMPMQ("WACD102");
         return;
      }
      //   Check if accounting date is valid
      if (!DSP.WWACDT.isBlank()) {
         this.PXDFMI.moveLeftPad(LDAZD.DTFM);
         this.PXDALI.moveLeftPad(DSP.WWACDT);
         this.PXDFMO.move("YMD8");
         this.PXOPRM = 0;
         COMDAT();
         XXACDT = this.PXDATO;
         if (this.PXDTER == 1) {
            picSetMethod('D');
            IN60 = true;
            DSP.setFocus(7, 18, "WWACDT");
            //   MSGID=XDT0001 Incorrect date
            COMPMQ("XDT0001");
            return;
         }
         if (MNDIV.getPTFA() == 1) {
            XXACYP = this.PXCYP1;
         }
         if (MNDIV.getPTFA() == 2) {
            XXACYP = this.PXCYP2;
         }
         if (MNDIV.getPTFA() == 3) {
            XXACYP = this.PXCYP3;
         }
         if (MNDIV.getPTFA() == 4) {
            XXACYP = this.PXCYP4;
         }
         if (MNDIV.getPTFA() == 5) {
            XXACYP = this.PXCYP5;
         }
      }
      if (DSP.WWFAQT.isBlank() || DSP.WWFAQT.LE("          0")) {    
         picSetMethod('D');   
         IN60 = true;   
         DSP.setFocus("WWFAQT");    
         //   MSGID=PP26006 Quantity must be entered  
         COMPMQ("PP26006");      
         return;  
      }  
      XXSTCO.move(SYTAB.getSTCO());
      XXSTKY.move(SYTAB.getSTKY());
      SYTAB.setDIVI().move(DSP.WWDIVI);
      XSDIVI.move(SYTAB.getDIVI());
      SYTAB.setSTCO().moveLeft("FFNC");
      FEFEID.move("FA40");
      FEFNCN.move(1);
      SYTAB.setSTKY().move(FESTKY);
      SYTAB.setLNCD().clear();
      IN91 = !SYTAB.CHAIN("00", SYTAB.getKey("00"));
      IN92 = SYTAB.getErr("00");
      if (!IN91) {
         DSFFNC.setDSFFNC().moveLeft(SYTAB.getPARM());
         WDACDT.move(DSP.WWACDT);
         this.PXDFMI.move("YMD8");
         this.PXDATI = DSFFNC.getDFFRDT();
         this.PXDFMO.moveLeftPad(LDAZD.DTFM);
         this.PXOPRM = 1;
         this.PXDSEP = ' ';
         COMDAT();
         WDFRDT.moveLeft(this.PXDALO);
         this.PXDFMI.move("YMD8");
         this.PXDATI = DSFFNC.getDFTODT();
         this.PXDFMO.moveLeftPad(LDAZD.DTFM);
         this.PXOPRM = 1;
         this.PXDSEP = ' ';
         COMDAT();
         WDTODT.moveLeft(this.PXDALO);
         //   - Accounting date must be in range of date limits
         if (XXACDT >= DSFFNC.getDFFRDT() &&
             XXACDT <= DSFFNC.getDFTODT()) {
         // dummy then part
         } else {
            picSetMethod('D');
            IN60 = true;
            DSP.setFocus(7, 18, "WWACDT");
            //   MSGID=XDT0005 Accounting date &3 is not within the valid range, which
            COMPMQ("XDT0005", formatToString(WDSMSG));
            return;
         }
      } else {
         picSetMethod('D');
         IN60 = true;
         if (!IN62) {
            DSP.setFocus(8, 18, "WWVONO");
         } else {
            DSP.setFocus(7, 18, "WWACDT");
         }
         if (LDAZD.CMTP == 2) {
         //   MSGID=XFE0003 Division &3 FAM entry ID &2 function number &1 is missin
            this.MSGID.move("XFE0003");
            MSGDTD.move(SYTAB.getDIVI());
         } else {
            //   MSGID=XFE0002 FAM entry ID &2 function number &1 is missing
            this.MSGID.move("XFE0002");
         }
         SYTAB.setDIVI().move(XSDIVI);
         MSGDTN.move(1L);
         MSGDTF.move("FA40");
         COMPMQ();
         return;
      }
      SYTAB.setDIVI().move(XSDIVI);
      SYTAB.setSTCO().move(XXSTCO);
      SYTAB.setSTKY().move(XXSTKY);
      FASVL.setCONO(LDAZD.CONO);
      PLCHKVO.FVCMTP = LDAZD.CMTP;
      FASVL.setDIVI().move(DSP.WWDIVI);
      PLCHKVO.FVACBC = CRS750DS.getPBACBC();
      PLCHKVO.FVVSER.move(DSFFNC.getDFVSER());
      PLCHKVO.FVACDT = XXACDT;
      PLCHKVO.FVYEA4 = (int)(XXACYP/100);
      PLCHKVO.FVVONI = 0;
      PLCHKVO.FVVTST = 0;
      PLCHKVO.FVFETC = 0;
      PLCHKVOSyncTo();
      IN92 = PLCHKVO.CCHKVON();
      PLCHKVOSyncFrom();
      PLCHKVOSyncTo();
      if (PLCHKVO.FVVERR == 1) {
         picSetMethod('D');
         IN60 = true;
         DSP.setFocus(8, 18, "WWVONO");
         COMPMQ(PLCHKVO.FVMSGI, formatToString(PLCHKVO.FVMSGD));
         return;
      }
      PLCHKVOSyncTo();
      if (PLCHKVO.FVMVMA != 2) {
         PLCHKVOSyncTo();
         if (PLCHKVO.FVMVMA == 0) {
            IN62 = false;
         }
         PLCHKVOSyncTo();
         if (PLCHKVO.FVMVMA == 1 && IN62) {
            PLCHKVO.FVFETC = 1;
            PLCHKVO.FVFEID.move("FA40");
            PLCHKVOSyncTo();
            IN92 = PLCHKVO.CCHKVON();
            PLCHKVOSyncFrom();
            DSP.WWVONO = PLCHKVO.FVVONO;
            picSetMethod('D');
            IN60 = true;
            DSP.setFocus(8, 18, "WWVONO");
            IN62 = false;
            return;
         }
         PLCHKVO.FVVTST = 1;
         PLCHKVO.FVFETC = 0;
         PLCHKVO.FVVONI = DSP.WWVONO;
         PLCHKVOSyncTo();
         IN92 = PLCHKVO.CCHKVON();
         PLCHKVOSyncFrom();
         PLCHKVOSyncTo();
         if (PLCHKVO.FVVERR == 1) {
            picSetMethod('D');
            IN60 = true;
            DSP.setFocus(8, 18, "WWVONO");
            COMPMQ(PLCHKVO.FVMSGI, formatToString(PLCHKVO.FVMSGD));
            return;
         }
      }
      this.PXALPH.moveRight(DSP.WWFAQT);
      this.PXNUM = 0d;
      this.PXDCCD = 0;
      this.PXFLDD = 9;
      this.PXDCFM = LDAZD.DCFM;
      SRCOMNUM.COMNUM();
      XXFAQT = this.PXNUM;
      if (SRCOMNUM.PXNMER != 0) {
         picSetMethod('D');
         IN60 = true;
         DSP.setFocus(9, 18, "WWFAQT");
         //   Msgid=xnu000
         this.MSGDTA.clear();
         COMPMQ((new MvxString(7)).moveLeft("XNU000").moveRight(SRCOMNUM.PXNMER, 1));
         return;
      }
      XDFAQT = FASMA.getFAQT();
      if (XXFAQT > (XDFAQT + EPS_9)) {
         picSetMethod('D');
         IN60 = true;
         DSP.setFocus(9, 18, "WWFAQT");
         //   Msgid=xnu000
         this.MSGDTA.clear();
         COMPMQ("IP29103");
         return;
      }
      if (!IN63 && !equals(this.PXNUM, XDFAQT, EPS_9)) {
         IN63 = true;
         picSetMethod('D');
         IN60 = true;
         return;
      }
      if (IN63 && equals(this.PXNUM, XDFAQT, EPS_9)) {
         DSP.WWNWFN.clear();
         DSP.WWNWSN = 0;
         IN63 = false;
         picSetMethod('D');
         IN60 = true;
         return;
      }
      if (IN63) {
         if (DSP.WWNWFN.isBlank()) {
            //   - Asset id  must be entered
            picSetMethod('D');
            IN60 = true;
            DSP.setFocus(10, 18, "WWNWFN");
            //   MSGID=WAS3002 Fixed asset must be entered
            COMPMQ("WAS3002");
            return;
         }
         //   - Asset sub number must be entered
         if (DSP.WWNWSN == 0) {
            picSetMethod('D');
            IN60 = true;
            DSP.setFocus(10, 31, "WWNWSN");
            //   MSGID=WSB3002 Subnumber must be entered
            COMPMQ("WSB3002");
            return;
         }
         FASMA.setASID().move(DSP.WWNWFN);
         FASMA.setSBNO(DSP.WWNWSN);
         IN91 = !FASMA.CHAIN("00", FASMA.getKey("00"));
         IN92 = FASMA.getErr("00");
         if (!IN91) {
            picSetMethod('D');
            IN60 = true;
            DSP.setFocus(10, 18, "WWNWFN");
            //   MSGID=FA00104 Fixed asset &1 subnumber &2 already exists
            XMASID.move(DSP.WWNWFN);
            XMSBNO.move(DSP.WWNWSN);
            COMPMQ("FA00104", formatToString(XXSMSG));
            return;
         }
      }
      //   Voucher text must be entered
      if (DSP.WWVTXT.isBlank()) {
         picSetMethod('D');
         IN60 = true;
         DSP.setFocus(14, 18, "WWVTXT");
         //   MSGID=WVT0602 Voucher text must be entered
         COMPMQ("WVT0602");
         return;
      }
   }

   /**
   *    PEUPD - Update
   */
   public void PEUPD() {
      CSYSTPSyncTo();
      IN91 = !SYSTP.CHAIN_LOCK("00", KSYSTP());
      IN92 = SYSTP.getErr("00");
      CSYSTPSyncFrom();
      FAS145DS.setZVASID().move(DSP.WWASID);
      FAS145DS.setZVSBNO(DSP.WWSBNO);
      FAS145DS.setZVDIVI().move(DSP.WWDIVI);
      FAS145DS.setZVACDT(XXACDT);
      FAS145DS.setZVVONO(DSP.WWVONO);
      FAS145DS.setZVLITX().move(DSP.WWLITX);
      FAS145DS.setZVVTXT().move(DSP.WWVTXT);
      FAS145DS.setZVPGNA().move(this.DSPGM);
      SYSTP.setPARA().moveLeftPad(FAS145DS.getFAS145DS());
      this.PXDFMI.move("TIME");
      this.PXDATI = 0;
      this.PXDFMO.move("YMD8");
      this.PXOPRM = 1;
      COMDAT();
      SYPAR.setLMDT(this.PXDATO);
      SYPAR.setCHID().move(this.DSUSS);
      SYPAR.setCHNO(SYPAR.getCHNO() + 1);
      this.XXCHNO = SYPAR.getCHNO();
      if (!IN91) {
         CSYSTPSyncTo();
         SYSTP.UPDAT("00");
      } else {
         SYPAR.setRGDT(SYPAR.getLMDT());
         SYPAR.setRGTM(movexTime());
         CSYSTPSyncTo();
         SYSTP.WRITE("00");
      }
      //   Next step
      picSetMethod('D');
      picPush('O', 'I');
   }

   /**
   *    PEPMT - Prompt
   */
   public boolean PEPMT() {
      //   Next step
      picSetMethod('D');
      DSP.restoreFocus();
      // ----------------------------------------------------------------
      //   Prompt standardtexts
      if (DSP.hasFocus(14, 18, 14, 57, "WWVTXT")) {
         this.PXFILE.moveLeft("FSTATX00");
         this.PXMBR.clear();
         this.PXOPT = '1';
         this.PXKVA1.moveLeft(LDAZD.CONO, 3);
         this.PXKVA2.moveLeft(LDAZD.DIVI);
         this.PXKTY1.move("EQ");
         this.PXKTY2.move("EQ");
         this.PXFLD1.moveLeftPad("GESXID");
         this.PXFLD2.moveLeftPad("GESXTX");
         this.PXF11 = '1';
         COMF04();
         if (this.PXTPGM.EQ("CRS990")) {
            DSP.WWVTXT.moveLeft(this.PXFVA2);
         }
         //   Check if jump to work with standard texts should be done
         if (this.PXFKEY == F11) {
            this.PXPGNM.clear();
            this.PXPGNM.moveLeft("CRS540");
            COMSTK();
            if (this.PXSTER == '1') {
            //   MSGID=XST0001 The program &1 is not now available due to a recursive c
               COMPMQ("XST0001", formatToString(this.PXPGNM));
            } else {
               LDAZZ.PICC.move("BI");
               XFPGNM.move(LDAZZ.FPNM);
               LDAZZ.FPNM.move(this.DSPGM);
               LDAZZ.TPGM.clear();
               LDAZZ.TDA1.clear();
               LDAZZ.TDA2.clear();
               //   CALL=CRS540 Voucher Text. Open Standard
               apCall("CRS540");
               if (LDAZZ.TPGM.EQ("CRS540")) {
                  DSP.WWVTXT.moveLeft(LDAZZ.TDA2);
               }
               LDAZZ.FPNM.move(XFPGNM);
            }
         }
         return true;
      }
      //   MSGID=XF04001 F4 is not permitted in this position on the panel
      COMPMQ("XF04001");
      return true;
   }

   /**
   *    POMAIN - Handle program MNS210   Select output parameter
   */
   public void POMAIN() {
      //   Batch jobnumber (CMBJNO)
      JBCMD.setBJNO().moveLeftPad(this.getBJNO());
      ZABJNO.move(JBCMD.getBJNO());
      //   Registr depreciation
      TFA100();
      IN78 = false;
      if (IN63) {
         FAS145DS.setZVASID().move(DSP.WWNWFN);
         FAS145DS.setZVSBNO(DSP.WWNWSN);
      }
      //   Batch jobnumber (CMBJNO)
      JBCMD.setBJNO().moveLeftPad(this.getBJNO());
      ZABJNO.move(JBCMD.getBJNO());
      do {
         IN91 = JBCMD.DELET("00", JBCMD.getKey("00", 1));
      } while (!(IN91));
      //   Common data
      JBCMD.clearNOKEY("00");
      JBCMD.setCONO(LDAZD.CONO);
      JBCMD.setDIVI().move(LDAZD.DIVI);
      JBCMD.setJNA().move(this.DSJNA);
      JBCMD.setJNU(this.DSJNU.getInt());
      JBCMD.setLMDT(SYPAR.getLMDT());
      JBCMD.setCHID().move(this.DSUSS);
      JBCMD.setCHNO(1);
      JBCMD.setRGDT(JBCMD.getLMDT());
      JBCMD.setRGTM(movexTime());
      //   Select printer data
      DSP.clearSFL("O0");
      this.PXCONO = LDAZD.CONO;
      this.PXDIVI.move(LDAZD.DIVI);
      PXCHKPRL.PXPRTF.moveLeft("FAS146PF");
      PXCHKPRL.PXUSID.move(this.DSUSS);
      PXCHKPRL.PXDEVD.move(this.DSJNA);
      PXMNS210.PXMSID.move("FA14501");
      PXMNS210.DSQCMD.clear();
      PXMNS210SyncTo();
      PXMNS210.DSPRDA.clear();
      PXMNS210SyncFrom();
      PXMNS210SyncTo();
      IN92 = PXMNS210.MNS210();
      PXMNS210SyncFrom();
      if (F03 == LDAZZ.FKEY || F12 == LDAZZ.FKEY) {
         // Printout using default values
         IN92 = PXMNS210.MNS211();
         PXMNS210SyncFrom();
         LDAZZ.FKEY = ' ';
      }
      //   Line 01 - Save OVRPRTF command
      JBCMD.setBJLI().move("01");
      JBCMD.setBJLT().move("PRT");
      JBCMD.setFILE().move(PXCHKPRL.PXPRTF);
      JBCMD.setQCMD().move(PXMNS210.DSQCMD);
      JBCMD.setDATA().move(PXMNS210.DSPRDA);
      IN91 = !JBCMD.CHAIN_LOCK("00", JBCMD.getKey("00"));
      if (IN91) {
         JBCMD.WRITE("00");
      }
      PXCHKPRL.PXPRTF.moveLeft("GLS041PF");
      IN78 = true;
      DSP.clearSFL("O0");
      this.PXCONO = LDAZD.CONO;
      this.PXDIVI.move(LDAZD.DIVI);
      PXCHKPRL.PXUSID.move(this.DSUSS);
      PXCHKPRL.PXDEVD.move(this.DSJNA);
      PXMNS210.PXMSID.move("FA14501");
      PXMNS210.DSQCMD.clear();
      PXMNS210SyncTo();
      PXMNS210.DSPRDA.clear();
      PXMNS210SyncFrom();
      PXMNS210SyncTo();
      IN92 = PXMNS210.MNS210();
      PXMNS210SyncFrom();
      if (F03 == LDAZZ.FKEY || F12 == LDAZZ.FKEY) {
         // Printout using default values
         IN92 = PXMNS210.MNS211();
         PXMNS210SyncFrom();
         LDAZZ.FKEY = ' ';
      }
      //   Line 02 - Save OVRPRTF command
      JBCMD.setBJLI().move("02");
      JBCMD.setBJLT().move("PRT");
      JBCMD.setFILE().move(PXCHKPRL.PXPRTF);
      JBCMD.setQCMD().move(PXMNS210.DSQCMD);
      JBCMD.setDATA().move(PXMNS210.DSPRDA);
      IN91 = !JBCMD.CHAIN_LOCK("00", JBCMD.getKey("00"));
      if (IN91) {
         JBCMD.WRITE("00");
      }
      //   Select job data
      DSP.clearSFL("O0");
      this.PXCONO = LDAZD.CONO;
      this.PXDIVI.move(LDAZD.DIVI);
      PXMNS230.PXJOB.moveLeft("FAS146");
      PXCHKPRL.PXUSID.move(this.DSUSS);
      PXCHKPRL.PXDEVD.move(this.DSJNA);
      PXMNS210.PXMSID.move("FA14501");
      PXMNS230.PXBJNO.move(JBCMD.getBJNO());
      PXMNS230.PXPGM.moveLeft("FAS146CL");
      PXMNS210SyncTo();
      PXMNS210.DSQCMD.clear();
      PXMNS210SyncFrom();
      PXMNS230SyncTo();
      PXMNS230.DSJBDA.clear();
      PXMNS230SyncFrom();
      PXMNS230SyncTo();
      IN92 = PXMNS230.MNS230();
      PXMNS230SyncFrom();
      //   F03=End
      if (F03 == LDAZZ.FKEY) {
         do {
            IN91 = JBCMD.DELET("00", JBCMD.getKey("00", 1));
         } while (!(IN91));
         picFinish();
         return;
      }
      //   F12=Previous
      if (F12 == LDAZZ.FKEY) {
         do {
            IN91 = JBCMD.DELET("00", JBCMD.getKey("00", 1));
         } while (!(IN91));
         picPop();
         return;
      }
      CSYSTPSyncTo();
      IN91 = !SYSTP.CHAIN_LOCK("00", KSYSTP());
      IN92 = SYSTP.getErr("00");
      CSYSTPSyncFrom();
      FAS145DS.setZVJBNO(ZAJNU.getInt());
      FAS145DS.setZVJBDT(ZAPRD.getInt());
      FAS145DS.setZVJBTM(ZAPRT.getInt());
      SYSTP.setPARA().moveLeftPad(FAS145DS.getFAS145DS());
      this.PXDFMI.move("TIME");
      this.PXDATI = 0;
      this.PXDFMO.move("YMD8");
      this.PXOPRM = 1;
      COMDAT();
      SYPAR.setLMDT(this.PXDATO);
      SYPAR.setCHID().move(this.DSUSS);
      SYPAR.setCHNO(SYPAR.getCHNO() + 1);
      this.XXCHNO = SYPAR.getCHNO();
      if (!IN91) {
         CSYSTPSyncTo();
         SYSTP.UPDAT("00");
      } else {
         SYPAR.setRGDT(SYPAR.getLMDT());
         SYPAR.setRGTM(movexTime());
         CSYSTPSyncTo();
         SYSTP.WRITE("00");
      }
      //   Line 90 - Save job data
      JBCMD.setBJLI().move("90");
      JBCMD.setBJLT().move("JOB");
      JBCMD.setFILE().move(PXMNS230.PXPGM);
      JBCMD.setQCMD().move(PXMNS210.DSQCMD);
      JBCMD.setDATA().move(PXMNS230.DSJBDA);
      IN91 = !JBCMD.CHAIN_LOCK("00", JBCMD.getKey("00"));
      if (IN91) {
         JBCMD.WRITE("00");
      }
      //   Line 98 - Save select data list
      JBCMD.setBJLI().move("98");
      JBCMD.setBJLT().move("SLT");
      JBCMD.setFILE().clear();
      JBCMD.setQCMD().moveLeftPad(FAS145DS.getFAS145DS());
      JBCMD.setDATA().clear();
      IN91 = !JBCMD.CHAIN_LOCK("00", JBCMD.getKey("00"));
      if (IN91) {
         JBCMD.WRITE("00");
      }
      //   Line 99 - Save select data list
      JBCMD.setBJLI().move("99");
      JBCMD.setBJLT().move("SLT");
      JBCMD.setFILE().clear();
      GLS040DS.setZWUPCD(1);
      GLS040DS.setZWDLCD(1);
      GLS040DS.setZWNOOV(1);
      GLS040DS.setZWSTCF(9);
      GLS040DS.setZWPGNM().moveLeft("FAS145");
      GLS040DS.setZWYEA4(0);
      GLS040DS.setZWJRNO(0);
      JBCMD.setQCMD().moveLeftPad(GLS040DS.getGLS040DS());
      JBCMD.setDATA().clear();
      IN91 = !JBCMD.CHAIN_LOCK("00", JBCMD.getKey("00"));
      if (IN91) {
         JBCMD.WRITE("00");
      }
      CRTSC();
      //   Execute job
      if (LDAZZ.DBGM == 1) {
      //   CALL=FAS146CL Disposition
         pFAS146CLpreCall();
         apCall("FAS146CL", pFAS146CL);
         pFAS146CLpostCall();
      } else {
         rQCMDpreCall();
         apCall("QCMDEXC", rQCMD);
         rQCMDpostCall();
      }
      picPop(3);
   }

   /**
   *    Tfa100 - call fas101 fas102 fas103 fas104
   *             For the registrt of depreciation
   */
   public void TFA100() {
      boolean isDisposalRegistered = false;
      JBCMD.clearNOKEY("00");
      JBCMD.setCONO(LDAZD.CONO);
      JBCMD.setDIVI().move(LDAZD.DIVI);
      JBCMD.setJNA().move(this.DSJNA);
      JBCMD.setJNU(this.DSJNU.getInt());
      JBCMD.setLMDT(SYPAR.getLMDT());
      JBCMD.setCHID().move(this.DSUSS);
      JBCMD.setCHNO(1);
      JBCMD.setRGDT(JBCMD.getLMDT());
      YYFAQT = XXFAQT;
      if (!equals(YYFAQT, XDFAQT, EPS_9)) {
         XXPART = 0d;
         YYPART = 0d;
         XXPART = XXFAQT/XDFAQT;
         YYPART = 1d - XXPART;
         DIVASS();
         UFAH63();
      }
      if (IN63) {
         FASDM.setASID().move(DSP.WWNWFN);
         FASDM.setSBNO(DSP.WWNWSN);
      }
      FDTSC.setDIVI().move(FASDM.getDIVI());
      FASDM.SETLL("00", FASDM.getKey("00", 4));
      FASDM.setSelection("00", "FDFAST", "NE", "9");
      IN93 = !FASDM.READE("00", FASDM.getKey("00", 4));
      IN92 = FASDM.getErr("00");
      while (!IN93) {
         FDTSC.setDPTP(FASDM.getDPTP());
         if (FDTSC.CHAIN("00", FDTSC.getKey("00", 3))) { 
            if (FASDM.getDPMD() != 0 && forDisposeDPTP == 0 ||
               FASDM.getDPTP() == forDisposeDPTP && forDisposeDPTP > 0) {
               if (FOOBAR > 0) {
                  //   Batch jobnumber (CMBJNO)
                  JBCMD.setBJNO().moveLeftPad(this.getBJNO());
                  ZABJNO.move(JBCMD.getBJNO());
               }
               FOOBAR = 1;
               JBCMD.setRGTM(movexTime());
               FAS100DS.setZWDEUC(1);
               FAS100DS.setZWDPTP(FASDM.getDPTP());
               RDDPTP();
               FAS100DS.setZWDTPE(0);
               FAS100DS.setZWADPT(DSDPTP.getFAADPT());
               FAS100DS.setZWYPFR(0);
               FAS100DS.setZWYYPP(0);
               FAS100DS.setZDYEA4(0);
               FAS100DS.setZWYPTO(0);
               //   Fetch year, period from CSYCAL
               this.PXDIVI.moveLeftPad(MNDIV.setDIVI());
               this.PXDFMI.move("YMD8");
               this.PXDATI = XXACDT;
               this.PXOPRM = 0;
               this.PXDSEP = ' ';
               COMDAT();
               switch (MNDIV.getPTFA()) {
                  case 1:
                     FAS100DS.setZWYPTO(this.PXCYP1);
                     break;
                  case 2:
                     FAS100DS.setZWYPTO(this.PXCYP2);
                     break;
                  case 3:
                     FAS100DS.setZWYPTO(this.PXCYP3);
                     break;
                  case 4:
                     FAS100DS.setZWYPTO(this.PXCYP4);
                     break;
                  case 5:
                     FAS100DS.setZWYPTO(this.PXCYP5);
                     break;
               }
               if (DSDPTP.getFAWFAM() == 1) {
                  FAS100DS.setZWACDT(XXACDT);
                  FAS100DS.setZWVONO(0);
                  FAS100DS.setZWVDSC().clear();
                  FAS100DS.setZWVTXT().clear();
                  // Get the journal heading from CRS405 for FA10
                  // and use that as voucher text
                  fecthFA10FamEntryId();
                  FAS100DS.setZWVTXT().moveLeftPad(FAS145DS.getZVVTXT());
               } else {
                  FAS100DS.setZWACDT(this.CUDATE);
                  FAS100DS.setZWVONO(0);
                  FAS100DS.setZWVDSC().clear();
                  FAS100DS.setZWVTXT().clear();
               }
               FAS100DS.setZWDLSC(0);
               FAS100DS.setZWAJSC(0);
               FAS100DS.setZWLITX().clear();
               FAS100DS.setZWPGNA().move(this.DSPGM);
               FAS100DS.setZWFDIV().move(DSP.WWDIVI);
               FAS100DS.setZWWFAM(DSDPTP.getFAWFAM());
               FAS100DS.setZWASID().move(FASDM.getASID());
               FAS100DS.setZWSBNO(FASDM.getSBNO());
               FAS100DS.setZWSPER(XXACDT);
               FAS100DS.setZWRGDT(0);
               FAS100DS.setZWRGTM(FAHIS.getRGTM());
               FAS100DS.setZWPSCD(0);
               FAS100DS.setZWJBNO(ZAJNU.getInt());
               FAS100DS.setZWJBDT(ZAPRD.getInt());
               FAS100DS.setZWJBTM(ZAPRT.getInt());
               setGETVAL();   
               PXGETVAL.GKCONO = FASDM.getCONO();
               PXGETVAL.GKDIVI.move(FASDM.getDIVI());
               PXGETVAL.GKDPTP = FASDM.getDPTP();
               PXGETVAL.GKASID.move(FASDM.getASID());
               PXGETVAL.GKSBNO = FASDM.getSBNO();
               PXGETVAL.GKTPER = 0;
               PXGETVAL.GKQTTP = 8;
               //   Check ffasdm
               PXGETVAL.FFAGTVL();
               FHDEP2 = PXGETVAL.GKFAVA;
               FHDEPC = PXGETVAL.GKFAVC;
               ZWYPTS.move(FAS100DS.getZWYPTO());
               FAS100DS.setZDYEA4(ZWYPT4.getInt());
               UFAHIS();
               //   Line 98 - Save select data list
               JBCMD.setBJLI().move("98");
               JBCMD.setBJLT().move("SLT");
               JBCMD.setFILE().clear();
               JBCMD.setQCMD().moveLeftPad(FAS100DS.getFAS100DS());
               JBCMD.setDATA().clear();
               IN91 = !JBCMD.CHAIN_LOCK("00", JBCMD.getKey("00"));
               IN92 = JBCMD.getErr("00");
               if (IN91) {
                  JBCMD.WRITE("00");
               } else {
                  JBCMD.UPDAT("00");
               }
               this.PXCONO = LDAZD.CONO;
               this.PXDIVI.move(LDAZD.DIVI);
               PXCHKPRL.PXPRTF.moveLeft("FAS101PF");
               PXCHKPRL.PXUSID.move(this.DSUSS);
               PXCHKPRL.PXDEVD.move(this.DSJNA);
               //   MSGID=FA10001 Depreciation - proposal/update in progress
               PXMNS210.PXMSID.move("FA10001");
               PXMNS210.DSQCMD.clear();
               PXMNS210SyncTo();
               PXMNS210.DSPRDA.clear();
               PXMNS210SyncFrom();
               PXMNS210SyncTo();
               IN92 = PXMNS210.MNS210();
               PXMNS210SyncFrom();
               if (F03 == LDAZZ.FKEY || F12 == LDAZZ.FKEY) {
                  // Printout using default values
                  PXMNS210.MNS211();
                  PXMNS210SyncFrom();
                  LDAZZ.FKEY = ' ';
               }
               this.PXCONO = LDAZD.CONO;
               this.PXDIVI.move(LDAZD.DIVI);
               PXCHKPRL.PXPRTF.moveLeft("FAS102PF");
               PXCHKPRL.PXUSID.move(this.DSUSS);
               PXCHKPRL.PXDEVD.move(this.DSJNA);
               //   MSGID=FA10001 Depreciation - proposal/update in progress
               PXMNS210.PXMSID.move("FA10001");
               PXMNS210.DSQCMD.clear();
               PXMNS210SyncTo();
               PXMNS210.DSPRDA.clear();
               PXMNS210SyncFrom();
               PXMNS210SyncTo();
               IN92 = PXMNS210.MNS210();
               PXMNS210SyncFrom();
               if (F03 == LDAZZ.FKEY || F12 == LDAZZ.FKEY) {
                  // Printout using default values
                  IN92 = PXMNS210.MNS211();
                  PXMNS210SyncFrom();
                  LDAZZ.FKEY = ' ';
               }
               this.PXCONO = LDAZD.CONO;
               this.PXDIVI.move(LDAZD.DIVI);
               PXCHKPRL.PXPRTF.moveLeft("FAS103PF");
               PXCHKPRL.PXUSID.move(this.DSUSS);
               PXCHKPRL.PXDEVD.move(this.DSJNA);
               //   MSGID=FA10001 Depreciation - proposal/update in progress
               PXMNS210.PXMSID.move("FA10001");
               PXMNS210.DSQCMD.clear();
               PXMNS210SyncTo();
               PXMNS210.DSPRDA.clear();
               PXMNS210SyncFrom();
               PXMNS210SyncTo();
               IN92 = PXMNS210.MNS210();
               PXMNS210SyncFrom();
               if (F03 == LDAZZ.FKEY || F12 == LDAZZ.FKEY) {
                  // Printout using default values
                  PXMNS210.MNS211();
                  PXMNS210SyncFrom();
                  LDAZZ.FKEY = ' ';
               }
               PXCHKPRL.PXPRTF.moveLeft("GLS041PF");
               IN78 = true;
               this.PXCONO = LDAZD.CONO;
               this.PXDIVI.move(LDAZD.DIVI);
               PXCHKPRL.PXUSID.move(this.DSUSS);
               PXCHKPRL.PXDEVD.move(this.DSJNA);
               //   MSGID=FA10001 Depreciation - proposal/update in progress
               PXMNS210.PXMSID.move("FA10001");
               PXMNS210.DSQCMD.clear();
               PXMNS210SyncTo();
               PXMNS210.DSPRDA.clear();
               PXMNS210SyncFrom();
               PXMNS210SyncTo();
               IN92 = PXMNS210.MNS210();
               PXMNS210SyncFrom();
               if (F03 == LDAZZ.FKEY || F12 == LDAZZ.FKEY) {
                  // Printout using default values
                  IN92 = PXMNS210.MNS211();
                  PXMNS210SyncFrom();
                  LDAZZ.FKEY = ' ';
               }
               //   Line 02 - Save OVRPRTF command
               JBCMD.setBJLI().move("02");
               JBCMD.setBJLT().move("PRT");
               JBCMD.setFILE().move(PXCHKPRL.PXPRTF);
               JBCMD.setQCMD().move(PXMNS210.DSQCMD);
               JBCMD.setDATA().move(PXMNS210.DSPRDA);
               IN91 = !JBCMD.CHAIN_LOCK("00", JBCMD.getKey("00"));
               if (IN91) {
                  JBCMD.WRITE("00");
               }
               //   Line 99 - Save select data list
               JBCMD.setBJLI().move("99");
               JBCMD.setBJLT().move("SLT");
               JBCMD.setFILE().clear();
               GLS040DS.setZWUPCD(1);
               GLS040DS.setZWDLCD(1);
               GLS040DS.setZWNOOV(1);
               GLS040DS.setZWSTCF(9);
               GLS040DS.setZWPGNM().moveLeft("FAS100");
               GLS040DS.setZWYEA4(0);
               GLS040DS.setZWJRNO(0);
               JBCMD.setQCMD().moveLeftPad(GLS040DS.getGLS040DS());
               JBCMD.setDATA().clear();
               IN91 = !JBCMD.CHAIN_LOCK("00", JBCMD.getKey("00"));
               if (IN91) {
                  JBCMD.WRITE("00");
               }
               YYDPTP.move(FASDM.getDPTP());
               LDAZZ.TDTA.moveLeft(YYTDTA);
               PXMNS230.PXBJNO.move(JBCMD.getBJNO());
               PXFAS101.PYUPCD.move(GLS040DS.getZWUPCD());
               PXFAS101.PYDLSC.move(FAS100DS.getZWDLSC());
               PXFAS101.PYAJSC.move(FAS100DS.getZWAJSC());
               PXFAS101.PYWFAM.move(FAS100DS.getZWWFAM());
               PXFAS101.PYADPT = FAS100DS.getZWADPT();
               PXFAS101.PYTRNO = XXTRNO;
               PXFAS101.PYPSCD = FAS100DS.getZWPSCD();
               //   CALL=FAS101 Error report - Depreciation Proposal
               PXFAS101SyncTo();
               IN92 = PXFAS101.FAS101();
               PXFAS101SyncFrom();
               JBCMD.setBJNO().move(PXMNS230.PXBJNO);
               GLS040DS.setZWUPCD(PXFAS101.PYUPCD.getInt());
               FAS100DS.setZWDLSC(PXFAS101.PYDLSC.getInt());
               FAS100DS.setZWAJSC(PXFAS101.PYAJSC.getInt());
               FAS100DS.setZWWFAM(PXFAS101.PYWFAM.getInt());
               FAS100DS.setZWADPT(PXFAS101.PYADPT);
               XXTRNO = PXFAS101.PYTRNO;
               FAS100DS.setZWPSCD(PXFAS101.PYPSCD);
               //   CALL=FAS102 Depreciation - Detailed
               if (greaterThan(XXRVAL, 2, 0d)) {   
                  pFAS102preCall();
                  apCall("FAS102", pFAS102);
                  pFAS102postCall();
                  pFAS103preCall();
                  apCall("FAS103", pFAS103);
                  pFAS103postCall();
               }
               this.PXCONO = LDAZD.CONO;
               this.PXDIVI.move(LDAZD.DIVI);
               PXMNS230.PXJOB.moveLeft("GLS040");
               PXCHKPRL.PXUSID.move(this.DSUSS);
               PXCHKPRL.PXDEVD.move(this.DSJNA);
               //   MSGID=FA10001 Depreciation - proposal/update in progress
               PXMNS210.PXMSID.move("FA10001");
               PXMNS230.PXBJNO.move(JBCMD.getBJNO());
               PXMNS230.PXPGM.moveLeft("FAS102CL");
               PXMNS210SyncTo();
               PXMNS210.DSQCMD.clear();
               PXMNS210SyncFrom();
               PXMNS230SyncTo();
               PXMNS230.DSJBDA.clear();
               PXMNS230SyncFrom();
               PXMNS230SyncTo();
               IN92 = PXMNS230.MNS230();
               PXMNS230SyncFrom();
               //   F03=End
               if (F03 == LDAZZ.FKEY) {
                  do {
                     IN91 = JBCMD.DELET("00", JBCMD.getKey("00", 1));
                  } while (!(IN91));
                  if (GLS040DS.getZWUPCD() == 1) {
                     picFinish();
                  } else {
                     picPop();
                  }
                  return;
               }
               //   F12=Previous
               if (F12 == LDAZZ.FKEY) {
                  do {
                     IN91 = JBCMD.DELET("00", JBCMD.getKey("00", 1));
                  } while (!(IN91));
                  picPop();
                  return;
               }
               //   Line 90 - Save job data
               JBCMD.setBJLI().move("90");
               JBCMD.setBJLT().move("JOB");
               JBCMD.setFILE().moveLeft("FAS102CL");
               JBCMD.setQCMD().move(PXMNS210.DSQCMD);
               JBCMD.setDATA().move(PXMNS230.DSJBDA);
               IN91 = !JBCMD.CHAIN_LOCK("00", JBCMD.getKey("00"));
               if (IN91) {
                  JBCMD.WRITE("00");
               }
               //   Line 97 - Save select data list
               JBCMD.setBJLI().move("97");
               JBCMD.setBJLT().move("SLT");
               JBCMD.setFILE().clear();
               GLS040DS.setZWUPCD(1);
               GLS040DS.setZWDLCD(1);
               GLS040DS.setZWNOOV(1);
               GLS040DS.setZWSTCF(9);
               GLS040DS.setZWPGNM().moveLeft("FAS145");
               GLS040DS.setZWYEA4(0);
               GLS040DS.setZWJRNO(0);
               JBCMD.setQCMD().moveLeftPad(GLS040DS.getGLS040DS());
               JBCMD.setDATA().clear();
               IN91 = !JBCMD.CHAIN_LOCK("00", JBCMD.getKey("00"));
               if (IN91) {
                  JBCMD.WRITE("00");
               }
               //   Line 99 - Save select data list
               JBCMD.setBJLI().move("99");
               JBCMD.setBJLT().move("SLT");
               JBCMD.setFILE().clear();
               GLS040DS.setZWUPCD(1);
               GLS040DS.setZWDLCD(1);
               GLS040DS.setZWNOOV(1);
               GLS040DS.setZWSTCF(9);
               GLS040DS.setZWPGNM().moveLeft("FAS100");
               GLS040DS.setZWYEA4(0);
               GLS040DS.setZWJRNO(0);
               JBCMD.setQCMD().moveLeftPad(GLS040DS.getGLS040DS());
               JBCMD.setDATA().clear();
               IN91 = !JBCMD.CHAIN_LOCK("00", JBCMD.getKey("00"));
               if (IN91) {
                  JBCMD.WRITE("00");
               }
               //   retrieve values for printout FAS146
               PRPPRT();
               if (LDAZZ.DBGM == 1) {
                  //   CALL=FAS102CL Depreciation - Detailed
                  pFAS102CLpreCall();
                  apCall("FAS102CL", pFAS102CL);
                  pFAS102CLpostCall();
               } else {
                  rQCMDpreCall();
                  apCall("QCMDEXC", rQCMD);
                  rQCMDpostCall();
               }
               if (forDisposeDPTP != 0) {
                  isDisposalRegistered = true;
               }
            }
         }
         if (!isDisposalRegistered) {
            IN93 = !FASDM.READE("00", FASDM.getKey("00", 4));
            IN92 = FASDM.getErr("00");
         } else {
            IN93 = !isDisposalRegistered;
            break;
         }
      }                             
   }

   /**
   *    setGETVAL
   *    Sets values for sales/ scrap date
   */
   public void setGETVAL() {  
      int totalPeriods = 0;
      // Get Accounting period from CSYPER   
      SYPER.setCONO(LDAZD.CONO);    
      SYPER.setDIVI().move(LDAZD.DIVI);   
      SYPER.setPETP(LDAZD.PTFA);    
      SYPER.setYEA4((int)(FAS100DS.getZWYPTO()/100));       
      SYPER.setPERI(FAS100DS.getZWYPTO()%100);  
      switch (DSDPTP.getFABELZ() ) {   
         case 0:  
            PXGETVAL.GKFPER = FAS100DS.getZWYPTO();   
            PXGETVAL.GKSPER = FAS100DS.getZWSPER();   
            break;   
         case 1:  
            SYPER.CHAIN("00", SYPER.getKey("00"));    
            if (SYPER.REDPE("00", SYPER.getKey("00", 3))) {    
               PXGETVAL.GKFPER = ((100*SYPER.getYEA4())+SYPER.getPERI());     
               PXGETVAL.GKSPER = SYPER.getTDAT();  
            } else {    
               PXGETVAL.GKFPER = FAS100DS.getZWYPTO();   
               PXGETVAL.GKSPER = FAS100DS.getZWSPER();   
            }     
            break;   
         case 2:  
            SYPER.SETLL("00", SYPER.getKey("00", 4));    
            IN91 = !SYPER.REDPE("00", SYPER.getKey("00", 3));  
            ZZYEA4.move(SYPER.getYEA4());    
            ZZPERI.move(SYPER.getPERI());    
            PXGETVAL.GKFPER = toInt(ZZYEAR);    
            PXGETVAL.GKSPER = SYPER.getTDAT();     
            break;   
         case 3:  
            if (SYPER.CHAIN("00", SYPER.getKey("00"))) {       
               PXGETVAL.GKFPER = ((100*SYPER.getYEA4())+SYPER.getPERI());  
               PXGETVAL.GKSPER = SYPER.getTDAT();  
            } else {    
               PXGETVAL.GKFPER = FAS100DS.getZWYPTO();   
               PXGETVAL.GKSPER = FAS100DS.getZWSPER();      
            }     
            break;   
         case 4:  
            SYPER.SETGT("00", SYPER.getKey("00", 4));    
            if (SYPER.REDPE("00", SYPER.getKey("00", 4))) {       
               PXGETVAL.GKFPER = ((100*SYPER.getYEA4())+SYPER.getPERI());  
               PXGETVAL.GKSPER = SYPER.getTDAT();  
            } else {       
               PXGETVAL.GKFPER = FAS100DS.getZWYPTO();   
               PXGETVAL.GKSPER = FAS100DS.getZWSPER();   
            }     
            break; 
         case 5:  
            PXGETVAL.GKSPER = dateMinus(FAS100DS.getZWSPER());               
            PXGETVAL.GKFPER = periodFetch(PXGETVAL.GKSPER);
            FAS100DS.setZWYPTO(PXGETVAL.GKFPER);
            break;
         case 6:
            //   Semi annual
            if (getStartPeriod() == FAS100DS.getZWSPER()) {
               //   This scenario is sell/scarp is the sme with the purhcase date. 
               //   Since this will result 1 day adjusment (for validated period).the date must be less than 1 day               
               PXGETVAL.GKFPER = FAS100DS.getZWYPTO();   
               PXGETVAL.GKSPER = FAS100DS.getZWSPER();   
               SYPER.setPERI((int)(PXGETVAL.GKFPER / 100));
               SYPER.setYEA4(FAS100DS.getZWYPTO()%100);
               SYPER.CHAIN("00", SYPER.getKey("00"));                    
            } else {
               totalPeriods = (int)getNumberOfPeriods(FAS100DS.getZWYPTO()/100);
               if (FAS100DS.getZWYPTO()%100 <= ((int)(totalPeriods/2))) {
                  //   First period of the year              
                  FAS100DS.setZWYPTO((int)(FAS100DS.getZWYPTO()/100) * 100 + (int)(totalPeriods/2));
               } else {
                  //   2nd half of the year
                  FAS100DS.setZWYPTO((int)(FAS100DS.getZWYPTO()/100) * 100 + (int)(totalPeriods));
               }  
               SYPER.setPERI(FAS100DS.getZWYPTO()%100);   
               SYPER.CHAIN("00", SYPER.getKey("00"));                 
               PXGETVAL.GKFPER = FAS100DS.getZWYPTO();
               PXGETVAL.GKSPER = SYPER.getTDAT();
            }
            break;                        
         default:    
            PXGETVAL.GKFPER = FAS100DS.getZWYPTO();   
            PXGETVAL.GKSPER = FAS100DS.getZWSPER();   
            break;   
      }  
   }  

   /**
   * Retrieves Fam entry ID for FA10 from CRS405 data.
   * <p>
   * Loads the DSFEID data structure with the Fam entry ID for FA10.
   */
   public void fecthFA10FamEntryId() {
      SYTAB.setCONO(LDAZD.CONO);
      SYTAB.setDIVI().move(DSP.WWDIVI);
      SYTAB.setSTCO().moveLeftPad("FEID");
      SYTAB.setSTKY().moveLeftPad("FA10");
      SYTAB.setLNCD().clear();
      if (SYTAB.CHAIN("00", SYTAB.getKey("00"))) {
         DSFEID.setDSFEID().moveLeftPad(SYTAB.getPARM());
      } else {
         DSFEID.clear();
      }
   }

   /**
   *    Ufahis
   */
   public void UFAHIS() {
      DLTDPL();
      YYTDTA.clear();
      this.PXDFMI.move("TIME");
      this.PXDATI = 0;
      this.PXDFMO.move("YMD8");
      this.PXOPRM = 1;
      COMDAT();
      FAHIS.setRGDT(this.PXDATO);
      FAS100DS.setZWRGDT(this.PXDATO);
      FAHIS.setLMDT(this.PXDATO);
      FAHIS.setRGTM((int)this.WSRGTM);
      FAS100DS.setZWRGTM(FAHIS.getRGTM());
      YYRGTM.move(FAHIS.getRGTM());
      //   Create and update  records in ffahis
      FAHIS.setCONO(FASDM.getCONO());
      FAHIS.setDIVI().move(FASDM.getDIVI());
      FAHIS.setASID().move(FASDM.getASID());
      FAHIS.setSBNO(FASDM.getSBNO());
      FAHIS.setVPER(FAS100DS.getZWYPTO());
      FAHIS.setVATP(FASDM.getVTDP());
      IN91 = !FAHIS.CHAIN("00", FAHIS.getKey("00"));
      IN92 = FAHIS.getErr("00");
      if (IN91) {
         //   Save amounts  and dates
         FAHIS.setVPER(FAS100DS.getZWYPTO());
         FAHIS.setCUCD().move(SVCUCD);
         if (!isBlank(FHDEP2, EPS_2)) {
            FAHIS.setFAVA(mvxHalfAdjust(FHDEP2, getDecimalCode(MNDIV.getLOCD())));
            FAHIS.setFAVC(mvxHalfAdjust(FHDEPC, getDecimalCode(FAHIS.getCUCD())));
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
            FAHIS.setCHID().move(this.DSUSS);
            FAHIS.setCHNO(1);
            FAHIS.setTSTM().clear();
            FAHIS.WRITE("00");
         }
      }
      WKACAM = 0d;
      WKACAM = SVFAVA - FHDEP2;
      if (!isBlank(WKACAM, EPS_2)) {
         FAHIS.setVATP(FASDM.getVTDP());
         FAHIS.setVPER(FAS100DS.getZWYPTO());
         IN93 = !FAHIS.CHAIN_LOCK("10", FAHIS.getKey("10", 6));
         IN92 = FAHIS.getErr("10");
         while (!IN93) {
            if (FAHIS.getRGDT() != FAS100DS.getZWRGDT() ||
                FAHIS.getRGTM() != FAS100DS.getZWRGTM()) {
               FAHIS.setFAVA(FAHIS.getFAVA() + SVFAVA);
               FAHIS.setFAVC(FAHIS.getFAVC() + SVFAVC);
               FAHIS.setFAVA(mvxHalfAdjust(FAHIS.getFAVA() - FHDEP2, getDecimalCode(MNDIV.getLOCD())));
               FAHIS.setFAVC(mvxHalfAdjust(FAHIS.getFAVC() - FHDEPC, getDecimalCode(FAHIS.getCUCD())));
               this.PXDFMI.move("TIME");
               this.PXDATI = 0;
               this.PXDFMO.move("YMD8");
               this.PXOPRM = 1;
               COMDAT();
               FAHIS.setLMDT(this.PXDATO);
               FAHIS.setCHID().move(this.DSUSS);
               FAHIS.setCHNO(FAHIS.getCHNO() + 1);
               FAHIS.UPDAT("10");
            }
            IN93 = !FAHIS.READE_LOCK("10", FAHIS.getKey("10", 6));
            IN92 = FAHIS.getErr("10");
         }
      }
   }

   public void UFAH63() {
      SVFAPA = 0d;
      SVFAPC = 0d;
      SVFAYA = 0d;
      SVFAYC = 0d;
      SVFACA = 0d;
      SVFACC = 0d;
      SVFAXA = 0d;
      SVFAXC = 0d;
      SVFAAA = 0d;
      SVFAAC = 0d;
      SVTOTA = 0d;
      SVTOTC = 0d;
      //   Divide an asset=updat values for FA kept (remaining)
      if (IN63) {
         FAHIS.setASID().move(DSP.WWASID);
         FAHIS.setSBNO(DSP.WWSBNO);
         FAHIS.setVATP(0);
         FAHIS.setVPER(0);
         FAHIS.SETLL("10", FAHIS.getKey("10", 6));
         IN91 = !FAHIS.READE_LOCK("10", FAHIS.getKey("00", 4));
         IN92 = FAHIS.getErr("10");
         while (!IN91) {
            FAHIS.setFAVA(mvxHalfAdjust((double)FAHIS.getFAVA() * YYPART, getDecimalCode(MNDIV.getLOCD())));
            FAHIS.setFAVC(mvxHalfAdjust((double)FAHIS.getFAVC() * YYPART, getDecimalCode(FAHIS.getCUCD())));
            if (FAHIS.getVATP() == FASDM.getVTDP()) {
               SVFAPA += FAHIS.getFAVA();
               SVFAPC += FAHIS.getFAVC();
            } else {
               if (FAHIS.getVATP() == FASDM.getADTY()) {
                  SVFAYA += FAHIS.getFAVA();
                  SVFAYC += FAHIS.getFAVC();
               } else {
                  if (FAHIS.getVATP() == FASDM.getVTAD()) {
                     SVFACA += FAHIS.getFAVA();
                     SVFACC += FAHIS.getFAVC();
                  } else {
                     if (FAHIS.getVATP() == FASDM.getBVAT()) {
                        SVFAAA += FAHIS.getFAVA();
                        SVFAAC += FAHIS.getFAVC();
                     } else {
                        if (FAHIS.getVATP() == DSP.WXVTED) {
                           SVFAXA += FAHIS.getFAVA();
                           SVFAXC += FAHIS.getFAVC();
                        }
                     }
                  }
               }
            }
            this.PXDFMI.move("TIME");
            this.PXDATI = 0;
            this.PXDFMO.move("YMD8");
            this.PXOPRM = 1;
            COMDAT();
            FAHIS.setLMDT(this.PXDATO);
            FAHIS.setCHID().move(this.DSUSS);
            FAHIS.setCHNO(FAHIS.getCHNO() + 1);
            FAHIS.UPDAT("10");
            IN91 = !FAHIS.READE_LOCK("10", FAHIS.getKey("00", 4));
            IN92 = FAHIS.getErr("10");
         }
      }
      //   updatFASVL with values from FAHIS because of rounding issues on diff t
      /*FASVL.setASID().move(DSP.WWASID);
      FASVL.setSBNO(DSP.WWSBNO);
      FASVL.setVATP(0);
      FASVL.SETLL("00", FASVL.getKey("00"));
      IN91 = !FASVL.READE_LOCK("00", FASVL.getKey("00", 4));
      IN92 = FASVL.getErr("00");
      while (!IN91) {
      if (FASVL.getVATP() == FASDM.getVTDP()) {
      FASVL.setFAVA(SVFAPA);
      FASVL.setFAVC(SVFAPC);
      } else {
      if (FASVL.getVATP() == FASDM.getADTY()) {
      FASVL.setFAVA(SVFAYA);
      FASVL.setFAVC(SVFAYC);
      } else {
      if (FASVL.getVATP() == FASDM.getVTAD()) {
      FASVL.setFAVA(SVFACA);
      FASVL.setFAVC(SVFACC);
      } else {
      if (FASVL.getVATP() == DSP.WXVTED) {
      FASVL.setFAVA(SVFAXA);
      FASVL.setFAVC(SVFAXC);
      }
      }
      FASVL.setCUCD().move(XVCUCD);
      FASVL.setFAVA(mvxHalfAdjust(FASVL.getFAVA(), getDecimalCode(MNDIV.getLOCD())));
      FASVL.setFAVC(mvxHalfAdjust(FASVL.getFAVC(), getDecimalCode(FASVL.getCUCD())));
      this.PXDFMI.move("TIME");
      this.PXDATI = 0;
      this.PXDFMO.move("YMD8");
      this.PXOPRM = 1;
      COMDAT();
      FASVL.setLMDT(this.PXDATO);
      FASVL.setCHID().move(this.DSUSS);
      FASVL.setCHNO(FASVL.getCHNO() + 1);
      FASVL.UPDAT("00");
      IN91 = !FASVL.READE_LOCK("00", FASVL.getKey("00"));
      IN92 = FASVL.getErr("00");
      }
      SVTOTA = SVFAPA + SVFAYA;
      SVTOTA += SVFACA;
      SVTOTA += SVFAXA;
      SVTOTC = SVFAPC + SVFAYC;
      SVTOTC += SVFACC;
      SVTOTC += SVFAXC;
      if (!equals(SVTOTA, SVFAAA, EPS_2)) {
      FAHIS.setASID().move(DSP.WWASID);
      FAHIS.setSBNO(DSP.WWSBNO);
      FAHIS.setVATP(FASDM.getBVAT());
      IN91 = !FAHIS.CHAIN_LOCK("00", FAHIS.getKey("00", 5));
      IN92 = FAHIS.getErr("00");
      if (!IN91) {
      FAHIS.setFAVA(mvxHalfAdjust(SVTOTA, getDecimalCode(MNDIV.getLOCD())));
      FAHIS.setFAVC(mvxHalfAdjust(SVTOTC, getDecimalCode(FAHIS.getCUCD())));
      FAHIS.UPDAT("00");
      }
      FAHIS.setASID().move(DSP.WWASID);
      FAHIS.setSBNO(DSP.WWSBNO);
      FASVL.setVATP(FASDM.getBVAT());
      IN91 = !FASVL.CHAIN_LOCK("00", FASVL.getKey("00"));
      IN92 = FASVL.getErr("00");
      if (!IN91) {
      FASVL.setFAVA(mvxHalfAdjust(SVTOTA, getDecimalCode(MNDIV.getLOCD())));
      FASVL.setFAVC(mvxHalfAdjust(SVTOTC, getDecimalCode(FAHIS.getCUCD())));
      FASVL.UPDAT("00");
      }
      FASVL.setVATP(FASDM.getVTAD());
      IN91 = !FASVL.CHAIN_LOCK("00", FASVL.getKey("00"));
      IN92 = FASVL.getErr("00");
      if (!IN91) {
      FASVL.setFAVA(mvxHalfAdjust(SVFACA, getDecimalCode(MNDIV.getLOCD())));
      FASVL.setFAVC(mvxHalfAdjust(SVFACC, getDecimalCode(FAHIS.getCUCD())));
      FASVL.UPDAT("00");
      }
      FASVL.setVATP(FASDM.getADTY());
      IN91 = !FASVL.CHAIN_LOCK("00", FASVL.getKey("00"));
      IN92 = FASVL.getErr("00");
      if (!IN91) {
      FASVL.setFAVA(mvxHalfAdjust(SVFAYA, getDecimalCode(MNDIV.getLOCD())));
      FASVL.setFAVC(mvxHalfAdjust(SVFAYC, getDecimalCode(FAHIS.getCUCD())));
      FASVL.UPDAT("00");
      }
      FASVL.setVATP(FASDM.getVTDP());
      IN91 = !FASVL.CHAIN_LOCK("00", FASVL.getKey("00"));
      IN92 = FASVL.getErr("00");
      if (!IN91) {
      FASVL.setFAVA(mvxHalfAdjust(SVFAPA, getDecimalCode(MNDIV.getLOCD())));
      FASVL.setFAVC(mvxHalfAdjust(SVFAPC, getDecimalCode(FAHIS.getCUCD())));
      FASVL.UPDAT("00");
      }
      //   Correction in new asset to balance amounts
      SVFAPA = 0d;
      SVFAPC = 0d;
      SVFAYA = 0d;
      SVFAYC = 0d;
      SVFACA = 0d;
      SVFACC = 0d;
      SVFAXA = 0d;
      SVFAXC = 0d;
      SVFAAA = 0d;
      SVFAAC = 0d;
      FAHIS.setASID().move(DSP.WWNWFN);
      FAHIS.setSBNO(DSP.WWNWSN);
      FAHIS.setVATP(0);
      FAHIS.setVPER(0);
      FAHIS.SETLL("10", FAHIS.getKey("10", 6));
      IN91 = !FAHIS.READE("10", FAHIS.getKey("00", 4));
      IN92 = FAHIS.getErr("10");
      while (!IN91) {
      if (FAHIS.getVATP() == FASDM.getADTY()) {
      // *         FHVATP    IFEQ FDVTDP
      // *                   ADD  FHFAVA    SVFAPA
      // *                   ADD  FHFAVA    SVFAPC
      // *                   ELSE
      SVFAYA += FAHIS.getFAVA();
      SVFAYC += FAHIS.getFAVA();
      } else {
      if (FAHIS.getVATP() == FASDM.getVTAD()) {
      SVFACA += FAHIS.getFAVA();
      SVFACC += FAHIS.getFAVA();
      } else {
      if (FAHIS.getVATP() == FASDM.getBVAT()) {
      SVFAAA += FAHIS.getFAVA();
      SVFAAC += FAHIS.getFAVA();
      } else {
      // *         FHVATP    IFEQ WXVTED
      // *                   ADD  FHFAVA    SVFAXA
      // *                   ADD  FHFAVA    SVFAXC
      // *                   ENDIF
      }
      // *                   ENDIF
      IN91 = !FAHIS.READE("10", FAHIS.getKey("00", 4));
      IN92 = FAHIS.getErr("10");
      }
      //   Read fasvl
      //   CHECK IF SUM (VTAD+ADTY+VTED+VTDP) BALANCE ACQUISITION AMOUNT
      FAHIS.setASID().move(DSP.WWNWFN);
      FAHIS.setSBNO(DSP.WWNWSN);
      FAHIS.setVATP(FASDM.getBVAT());
      IN91 = !FAHIS.CHAIN_LOCK("00", FAHIS.getKey("00", 5));
      IN92 = FAHIS.getErr("00");
      if (!IN91) {
      FAHIS.setFAVA(mvxHalfAdjust(DSP.WXPVAL - SVTOTA, getDecimalCode(MNDIV.getLOCD())));
      FAHIS.setFAVC(mvxHalfAdjust(WYPVAL - SVTOTC, getDecimalCode(FAHIS.getCUCD())));
      FAHIS.UPDAT("00");
      }
      FASVL.setASID().move(DSP.WWNWFN);
      FASVL.setSBNO(DSP.WWNWSN);
      FASVL.setVATP(FASDM.getBVAT());
      IN91 = !FASVL.CHAIN_LOCK("00", FASVL.getKey("00"));
      IN92 = FASVL.getErr("00");
      if (!IN91) {
      FASVL.setFAVA(mvxHalfAdjust(DSP.WXPVAL - SVTOTA, getDecimalCode(MNDIV.getLOCD())));
      FASVL.setFAVC(mvxHalfAdjust(WYPVAL - SVTOTC, getDecimalCode(FAHIS.getCUCD())));
      FASVL.UPDAT("00");
      }
      FASVL.setVATP(FASDM.getVTAD());
      IN91 = !FASVL.CHAIN_LOCK("00", FASVL.getKey("00"));
      IN92 = FASVL.getErr("00");
      if (!IN91) {
      FASVL.setFAVA(mvxHalfAdjust(SVFACA, getDecimalCode(MNDIV.getLOCD())));
      FASVL.setFAVC(mvxHalfAdjust(SVFACC, getDecimalCode(FAHIS.getCUCD())));
      FASVL.UPDAT("00");
      }
      FASVL.setVATP(FASDM.getADTY());
      IN91 = !FASVL.CHAIN_LOCK("00", FASVL.getKey("00"));
      IN92 = FASVL.getErr("00");
      if (!IN91) {
      FASVL.setFAVA(mvxHalfAdjust(SVFAYA, getDecimalCode(MNDIV.getLOCD())));
      FASVL.setFAVC(mvxHalfAdjust(SVFAYC, getDecimalCode(FAHIS.getCUCD())));
      FASVL.UPDAT("00");
      }
      } */
      JBCMD.setCONO(FASVL.getCONO());
      JBCMD.setDIVI().move(LDAZD.DIVI);
      JBCMD.setFILE().moveLeft("FAS011");
      JBCMD.setBJLI().move("98");
      JBCMD.setBJLT().move("SLT");
      FASDM.setCONO(FASMA.getCONO());
      FASDM.setDIVI().move(FASMA.getDIVI());
      FASDM.setASID().move(DSP.WWASID);
      FASDM.setSBNO(DSP.WWSBNO);
      FASDM.SETLL("00", FASDM.getKey("00", 4));
      IN93 = !FASDM.READE("00", FASDM.getKey("00", 4));
      do {
         JBCMD.setBJNO().moveLeftPad(this.getBJNO());
         JBCMD.setBJLI().move("98");
         FAS010DS.setFAS010DS().clear();
         FAS010DS.setZJCONO(FASVL.getCONO());
         FAS010DS.setZJDIVI().move(FASDM.getDIVI());
         FAS010DS.setZJASID().move(DSP.WWASID);
         FAS010DS.setZJSBNO(DSP.WWSBNO);
         FAS010DS.setZJFATP().move(FASDM.getFATP());
         FAS010DS.setZJDPTP(FASDM.getDPTP());
         JBCMD.setQCMD().moveLeftPad(FAS010DS.getFAS010DS());
         JBCMD.setDATA().clear();
         JBCMD.setJNA().move(this.DSJNA);
         JBCMD.setJNU(this.DSJNU.getInt());
         JBCMD.setLMDT(SYSTP.getLMDT());
         JBCMD.setCHID().move(this.DSUSS);
         JBCMD.setCHNO(1);
         JBCMD.setRGDT(JBCMD.getLMDT());
         JBCMD.setRGTM(movexTime());
         JBCMD.WRITE("00");
         pFAS011preCall();
         apCall("CCRTDEP", pFAS011);
         IN91 = JBCMD.DELET("00", JBCMD.getKey("00"));
         IN93 = !FASDM.READE("00", FASDM.getKey("00", 4));
      } while (!IN93);// end do
   }

   public void pFAS011preCall() {
      pFAS011.reset();
      pFAS011.set(JBCMD.getBJNO());
   }

   /**
   *    DLTDPL - DELETE DEPR PLAN : RECORDS BETWEEN 'YTD' AND SALE PERIOD
   */
   public void DLTDPL() {
      SVFAVA = 0d;
      SVFAVC = 0d;
      SVCUCD.clear();
      FAHIS.setCONO(FASDM.getCONO());
      FAHIS.setDIVI().move(FASDM.getDIVI());
      FAHIS.setASID().move(FASDM.getASID());
      FAHIS.setSBNO(FASDM.getSBNO());
      if (IN63) {
         FAHIS.setASID().move(DSP.WWNWFN);
         FAHIS.setSBNO(DSP.WWNWSN);
      }
      FAHIS.setVATP(FASDM.getVTDP());
      FAHIS.SETLL("00", FAHIS.getKey("00", 5));
      IN93 = !FAHIS.READE_LOCK("00", FAHIS.getKey("00", 5));
      IN92 = FAHIS.getErr("00");
      while (!IN93) {
         SVCUCD.move(FAHIS.getCUCD());
         SVFAVA += FAHIS.getFAVA();
         SVFAVC += FAHIS.getFAVC();
         IN92 = FAHIS.DELET("00", FAHIS.getKey("00", 5));
         IN93 = !FAHIS.READE_LOCK("00", FAHIS.getKey("00", 5));
         IN92 = FAHIS.getErr("00");
      }
      if (FASDM.getBELZ() == 1) {   
         FAHIS.setVATP(0);
         IN91 = !FAHIS.CHAIN_LOCK("00", FAHIS.getKey("00", 5));
         IN92 = FAHIS.getErr("00");
         if (!IN91) {
            FAHIS.DELET("00");
         }
      }
      if (IN63) {
         FASVL.setASID().move(DSP.WWNWFN);
         FASVL.setSBNO(DSP.WWNWSN);
      }
      FASVL.setVATP(FASDM.getVTDP());
      IN91 = !FASVL.CHAIN_LOCK("00", FASVL.getKey("00"));
      if (!IN91) {
         FASVL.DELET("00");
      }
      if (FASDM.getBELZ() == 1) {   
         FASVL.setVATP(0);
         IN91 = !FASVL.CHAIN_LOCK("00", FASVL.getKey("00"));
         if (!IN91) {
            FASVL.DELET("00");
         }
      }
      if (SVCUCD.isBlank()) {
         SVCUCD.move(FASMA.getCUCD());
      }
   }

   public void PRPPRT() {
      // Initialize values    
      YXPVAL = 0d;   
      YYPVAL = 0d;   
      XDACDP = 0d;   
      XDADTY = 0d;   

      FASVL.setASID().move(FASDM.getASID());
      FASVL.setSBNO(FASDM.getSBNO());
      FASVL.setVATP(FASDM.getBVAT());
      IN91 = !FASVL.CHAIN("00", FASVL.getKey("00"));
      IN92 = FASVL.getErr("00");
      if (!IN91) {
         YXPVAL = FASVL.getFAVA();
         YYPVAL = FASVL.getFAVC();
      }
      FASVL.setVATP(FASDM.getVTAD());
      IN91 = !FASVL.CHAIN("00", FASVL.getKey("00"));
      IN92 = FASVL.getErr("00");
      if (!IN91) {
         XDACDP = FASVL.getFAVA();
      }
      FASVL.setVATP(FASDM.getADTY());
      IN91 = !FASVL.CHAIN("00", FASVL.getKey("00"));
      IN92 = FASVL.getErr("00");
      if (!IN91) {
         XDADTY = FASVL.getFAVA();
      }
      //-----------------------------------------------------------------
      DSP.WXEODP = 0;
      FDTED.setDIVI().move(DSP.WWDIVI);
      FDTED.setDPTP(FASDM.getDPTP());
      IN91 = !FDTED.CHAIN("00", FDTED.getKey("00"));
      if (!IN91) {
         FAHIS.setASID().move(FASDM.getASID());
         FAHIS.setSBNO(FASDM.getSBNO());
         FAHIS.setVATP(FDTED.getVATP());
         FAHIS.setVPER(0);
         FAHIS.SETLL("10", FAHIS.getKey("10", 5));
         IN91 = !FAHIS.READE("10", FAHIS.getKey("10", 5));
         while (!IN91) {
            DSP.WXEODP += FAHIS.getFAVA();
            IN91 = !FAHIS.READE("10", FAHIS.getKey("10", 5));
         }
      }
      //-----------------------------------------------------------------
      //   Remaining value
      XDVTDP = YXPVAL - DSP.WXEODP;
      XDVTDP -= XDACDP;
      XDVTDP -= XDADTY;
      //   Init key to FFCHKF
      FCHKF.setSTCO().clear();
      FCHKF.setSTCO().moveLeft("FAS145");
      FCHKF.setKFL1().clear();
      FCHKF.setKFL2().clear();
      XCASID.move(FASDM.getASID());
      XCSBNO.move(FASDM.getSBNO());
      XCDIVI.move(FASDM.getDIVI());
      FCHKF.setKFL1().move(XCKFL1);
      FCHKF.setKFL2().moveLeft(FASDM.getDPTP(), 2);
      IN91 = !FCHKF.CHAIN_LOCK("00", FCHKF.getKey("00"));
      IN92 = FCHKF.getErr("00");
      FCHKF.setDATA().clear();
      XCACDP.move(XDACDP);
      XCADTY.move(XDADTY);
      XCEODP.move(DSP.WXEODP);
      XCPVAL.move(YXPVAL);
      XCRVAL.move(XDVTDP);
      XCADEP.move(DSP.WXADEP);
      XCDPTP.move(FASDM.getDPTP());
      FDTSC.setDPTP(FASDM.getDPTP());
      IN97 = !FDTSC.CHAIN("00", FDTSC.getKey("00"));
      if (!IN97) {
         XCVATP.move(FDTSC.getVATP());
      } else {
         XCVATP.move(0);
      }
      FCHKF.setDATA().move(XCDATA);
      this.PXDFMI.move("TIME");
      this.PXDATI = 0;
      this.PXDFMO.move("YMD8");
      this.PXOPRM = 1;
      COMDAT();
      FCHKF.setLMDT(this.PXDATO);
      FCHKF.setCHID().move(this.DSUSS);
      FCHKF.setCHNO(FCHKF.getCHNO() + 1);
      this.XXCHNO = FCHKF.getCHNO();
      if (!IN91) {
         FCHKF.UPDAT("00");
      } else {
         FCHKF.setRGDT(FCHKF.getLMDT());
         FCHKF.setRGTM(movexTime());
         FCHKF.WRITE("00");
      }
   }

   /**
   *    Read dsdptp
   */
   public void RDDPTP() {
      SYTAB.setSTCO().moveLeft("DPTP");
      SYTAB.setLNCD().clear();
      SYTAB.setSTKY().clear();
      SYTAB.setSTKY().moveLeft(FASDM.getDPTP(), 2);
      //   - Check record
      IN93 = !SYTAB.CHAIN("00", SYTAB.getKey("00"));
      IN92 = SYTAB.getErr("00");
      if (!IN93) {
         DSDPTP.setDSDPTP().moveLeft(SYTAB.getPARM());
      }
   }

   /**
   *    CRTSC  - Create in file FCR040, in file FFAHIS and update
   *             values in file FFASVL
   */
   public void CRTSC() {
      boolean isRecordInSubFile = false;
      hasValidWFAM = false;   
      vonoRetrieved = false;
      CR040.setJBNO(FAS145DS.getZVJBNO());
      CR040.setJBDT(FAS145DS.getZVJBDT());
      CR040.setJBTM(FAS145DS.getZVJBTM());
      PLCHKVOSyncTo();      
      //   Get year and period to FCR040
      CR040.setACDT(XXACDT);
      //   Fetch year, period from CSYCAL
      this.PXDIVI.moveLeftPad(MNDIV.setDIVI());
      this.PXDFMI.move("YMD8");
      this.PXDATI = CR040.getACDT();
      this.PXOPRM = 0;
      this.PXDSEP = ' ';
      COMDAT();
      if (MNDIV.getPTFA() == 1) {
         CR040.setACYP(this.PXCYP1);
         CR040.setYEA4((int)(this.PXCYP1/100));
      }
      if (MNDIV.getPTFA() == 2) {
         CR040.setACYP(this.PXCYP2);
         CR040.setYEA4((int)(this.PXCYP2/100));
      }
      if (MNDIV.getPTFA() == 3) {
         CR040.setACYP(this.PXCYP3);
         CR040.setYEA4((int)(this.PXCYP3/100));
      }
      if (MNDIV.getPTFA() == 4) {
         CR040.setACYP(this.PXCYP4);
         CR040.setYEA4((int)(this.PXCYP4/100));
      }
      if (MNDIV.getPTFA() == 5) {
         CR040.setACYP(this.PXCYP5);
         CR040.setYEA4((int)(this.PXCYP5/100));
      }
      //   - Only if any records in subfile to update
      if (DSP.WBRRNA != 0) {
         //find the correct depreciation type if pointer was moved
         if (forDisposeDPTP > 0 && forDisposeDPTP != DSP.WSDPTP) {
            DSP.setSubfileFocus("BS", "*ROW", 1);
            DSP.WBRRNA = 1;
            do {
               isRecordInSubFile = DSP.getSFL("BS", DSP.WBRRNA);
               DSP.WBRRNA++;
            } while (!(isRecordInSubFile || forDisposeDPTP == DSP.WSDPTP));
         } else if (forDisposeDPTP == 0) {
            DSP.setSubfileFocus("BS", "*ROW", 1);
            DSP.WBRRNA = 1;
            isRecordInSubFile = DSP.getSFL("BS", DSP.WBRRNA);
         }
         while (!isRecordInSubFile && (forDisposeDPTP == 0 ||
                DSP.WSDPTP == forDisposeDPTP && forDisposeDPTP > 0)) {
            //   Retrieve value types for depreciations
            FASDM.setCONO(LDAZD.CONO);
            FASDM.setDIVI().move(DSP.WWDIVI);
            FASDM.setASID().move(DSP.WWASID);
            FASDM.setSBNO(DSP.WWSBNO);
            if (IN63) {
               FASDM.setASID().move(DSP.WWNWFN);
               FASDM.setSBNO(DSP.WWNWSN);
            }
            FASDM.setDPTP(DSP.WSDPTP);
            if (forDisposeDPTP == 0) {   
               if (hasOtherDisposableDPTP(LDAZD.CONO, DSP.WWDIVI, DSP.WWASID, DSP.WWSBNO, FASDM.getDPTP(), true)) {
                  willReverseAcqValue = false; 
               } else {   
                  willReverseAcqValue = true;  
               } 
               if (hasOtherDisposableDPTP(LDAZD.CONO, DSP.WWDIVI, DSP.WWASID, DSP.WWSBNO, FASDM.getDPTP(), true) && !willReverseAcqValue) {
                  willReverseAcqValue = true;
               }
            }
            IN84 = !FASDM.CHAIN("00", FASDM.getKey("00"));
            IN92 = FASDM.getErr("00");
            FASVL.setDIVI().move(DSP.WWDIVI);
            FASVL.setASID().move(FASDM.getASID());
            FASVL.setSBNO(FASDM.getSBNO());
            FASVL.setVATP(FASDM.getBVAT());
            if (FASVL.CHAIN("00", FASVL.getKey("00"))) {
               XXSCVA = FASVL.getFAVA();
               XXSCVC = FASVL.getFAVC();
            }
            if (!IN84) {
               SYTAB.setDIVI().move(FASDM.getDIVI());
               SYTAB.setSTCO().moveLeftPad("DPTP");
               SYTAB.setSTKY().moveLeftPad(FASDM.getDPTP(), 2);
               IN91 = !SYTAB.CHAIN("00", SYTAB.getKey("00"));
               IN92 = SYTAB.getErr("00");
               if (IN91) {
                  DSDPTP.setFAEIDP(1);
                  DSDPTP.setFAWFAM(0);
               } else {
                  DSDPTP.setDSDPTP().moveLeft(SYTAB.getPARM());
               }
               if (PLCHKVO.FVMVMA == 2 &&
                   DSDPTP.getFAWFAM() == 1 &&
                   !isBlank(FASVL.getFAVA(), EPS_2)) {
                  PLCHKVO.FVFETC = 1;
                  PLCHKVOSyncTo();
                  IN92 = PLCHKVO.CCHKVON();
                  PLCHKVOSyncFrom();
                  FAS145DS.setZVVONO(PLCHKVO.FVVONO);
                  FAS145DS.setZVVDSC().move(DSFFNC.getDFVDSC());
                  vonoRetrieved = true;
               }
               if (!IN91) {
                  //   Retrieve value types for accumulated depreciations
                  FASVL.setCONO(FASDM.getCONO());
                  FASVL.setDIVI().move(DSP.WWDIVI);
                  FASVL.setASID().move(FASDM.getASID());
                  FASVL.setSBNO(FASDM.getSBNO());
                  FASVL.setVATP(FASDM.getVTAD());
                  XXACDP = 0d;
                  XXDEPR = 0d;
                  XXDEPC = 0d;
                  IN91 = !FASVL.CHAIN("00", FASVL.getKey("00"));
                  if (!IN91 && !isBlank(FASVL.getFAVA(), EPS_2)) {
                     XXDEPR += FASVL.getFAVA();
                     XXDEPC += FASVL.getFAVC();
                     //   Check accounting reference number for depreciation credit
                     // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
                     //    Set params for CRTVACC (Scraps-Value decuction)
                     PXCRTVAC.PPCONO = FASMA.getCONO();
                     PXCRTVAC.PPDIVI.move(DSP.WWDIVI);
                     PXCRTVAC.PPEVEN.move("FA40");
                     PXCRTVAC.PPACTY.move("500 ");
                     PXCRTVAC.PPFDAT = XXACDT;
                     PXCRTVAC.PPCMTP = LDAZD.CMTP;
                     PXCRTVAC.PPLANC.move(LDAZD.LANC);
                     PXCRTVAC.PPDCFM.moveRight(LDAZD.DCFM);
                     PXCRTVAC.PPASID.move(FASMA.getASID());
                     PXCRTVAC.PPSBNO = FASMA.getSBNO();
                     PXCRTVAC.PPDPTP = FASDM.getDPTP();
                     //    Create accounting references
                     IN92 = PXCRTVAC.CRTVACC();
                     CR040.setDIVI().move(DSP.WWDIVI);
                     CR040.setACA()[0].move(PXCRTVAC.PPAIT1);
                     CR040.setACA()[1].move(PXCRTVAC.PPAIT2);
                     CR040.setACA()[2].move(PXCRTVAC.PPAIT3);
                     CR040.setACA()[3].move(PXCRTVAC.PPAIT4);
                     CR040.setACA()[4].move(PXCRTVAC.PPAIT5);
                     CR040.setACA()[5].move(PXCRTVAC.PPAIT6);
                     CR040.setACA()[6].move(PXCRTVAC.PPAIT7);
                     // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
                     CR040.setBCHN(0);
                     CR040.setTRNO(CR040.getTRNO() + 1);
                     CR040.setTRCD(1);
                     if (CRS750DS.getPBACBC() == 1) {
                        CR040.setVSER().move(DSFFNC.getDFVSER());
                     } else {
                        CR040.setVSER().clear();
                     }
                     CR040.setVONO(FAS145DS.getZVVONO());
                     CR040.setVDSC().move(FAS145DS.getZVVDSC());
                     CR040.setVTXT().move(FAS145DS.getZVVTXT());
                     CR040.setFEID().move("FA40");
                     CR040.setFNCN(1);
                     CR040.setTDSC().clear();
                     CR040.setACQT(0d);
                     CR040.setDCAM(LDAZD.LCDC);
                     if (IN88) {
                        CR040.setCUCD().move(SVCUCD);
                     } else {
                        CR040.setCUCD().move(MNDIV.getLOCD());
                     }
                     if (XXMUNI == 1 && DSMUST.getMCMUST() == 2) {
                        CR040.setARAT(DSMUCU.getMCARAT());
                     } else {
                        CR040.setARAT(1d);
                     }
                     CR040.setCRTP(1);
                     CR040.setPGNM().moveLeftPad("FAS145");
                     this.PXDFMI.move("TIME");
                     this.PXDATI = 0;
                     this.PXDFMO.move("YMD8");
                     this.PXOPRM = 1;
                     COMDAT();
                     CR040.setRGDT(this.PXDATO);
                     CR040.setLMDT(this.PXDATO);
                     CR040.setRGTM(movexTime());
                     CR040.setCHID().move(this.DSUSS);
                     CR040.setACAM(FASVL.getFAVA());
                     CR040.setCUAM(FASVL.getFAVA());
                     CR040.setDBCR(' ');
                     if (CRS750DS.getPBDCNY() == 1) {
                        if (CR040.getCUAM() < (0d - EPS_2)) {
                           CR040.setDBCR(CRS750DS.getPBDBNG());
                        } else {
                           CR040.setDBCR(CRS750DS.getPBDBPS());
                        }
                     }
                     if (DSDPTP.getFAEIDP() == 2) {
                        CR040.setEICD(1);
                     } else {
                        CR040.setEICD(0);
                     }
                     if (DSDPTP.getFAWFAM() == 1) {
                        CR040.WRITE("10");
                     }
                     // Handle equipment Accounting
                     wrtCAATRA();
                  }
                  //   Retrieve value types for accumulated depreciations this year
                  FASVL.setVATP(FASDM.getADTY());
                  IN91 = !FASVL.CHAIN("00", FASVL.getKey("00"));
                  if (!IN91 && !isBlank(FASVL.getFAVA(), EPS_2)) {
                     XXDEPR += FASVL.getFAVA();
                     XXDEPC += FASVL.getFAVC();
                     //   Check accounting reference number for depreciation credit
                     // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
                     //    Set params for CRTVACC (Scraps-Value decuction)
                     PXCRTVAC.PPCONO = FASMA.getCONO();
                     PXCRTVAC.PPDIVI.move(DSP.WWDIVI);
                     PXCRTVAC.PPEVEN.move("FA40");
                     PXCRTVAC.PPACTY.move("500 ");
                     PXCRTVAC.PPFDAT = XXACDT;
                     PXCRTVAC.PPCMTP = LDAZD.CMTP;
                     PXCRTVAC.PPLANC.move(LDAZD.LANC);
                     PXCRTVAC.PPDCFM.moveRight(LDAZD.DCFM);
                     PXCRTVAC.PPASID.move(FASMA.getASID());
                     PXCRTVAC.PPSBNO = FASMA.getSBNO();
                     PXCRTVAC.PPDPTP = FASDM.getDPTP();
                     //    Create accounting references
                     IN92 = PXCRTVAC.CRTVACC();
                     CR040.setDIVI().move(DSP.WWDIVI);
                     CR040.setACA()[0].move(PXCRTVAC.PPAIT1);
                     CR040.setACA()[1].move(PXCRTVAC.PPAIT2);
                     CR040.setACA()[2].move(PXCRTVAC.PPAIT3);
                     CR040.setACA()[3].move(PXCRTVAC.PPAIT4);
                     CR040.setACA()[4].move(PXCRTVAC.PPAIT5);
                     CR040.setACA()[5].move(PXCRTVAC.PPAIT6);
                     CR040.setACA()[6].move(PXCRTVAC.PPAIT7);
                     CR040.setBCHN(0);
                     CR040.setTRNO(CR040.getTRNO() + 1);
                     CR040.setTRCD(1);
                     if (CRS750DS.getPBACBC() == 1) {
                        CR040.setVSER().move(DSFFNC.getDFVSER());
                     } else {
                        CR040.setVSER().clear();
                     }
                     CR040.setVONO(FAS145DS.getZVVONO());
                     CR040.setVDSC().move(FAS145DS.getZVVDSC());
                     CR040.setVTXT().move(FAS145DS.getZVVTXT());
                     CR040.setFEID().move("FA40");
                     CR040.setFNCN(1);
                     CR040.setTDSC().clear();
                     CR040.setACQT(0d);
                     CR040.setDCAM(LDAZD.LCDC);
                     if (IN88) {
                        CR040.setCUCD().move(SVCUCD);
                     } else {
                        CR040.setCUCD().move(MNDIV.getLOCD());
                     }
                     if (XXMUNI == 1 && DSMUST.getMCMUST() == 2) {
                        CR040.setARAT(DSMUCU.getMCARAT());
                     } else {
                        CR040.setARAT(1d);
                     }
                     CR040.setCRTP(1);
                     CR040.setPGNM().moveLeftPad("FAS145");
                     this.PXDFMI.move("TIME");
                     this.PXDATI = 0;
                     this.PXDFMO.move("YMD8");
                     this.PXOPRM = 1;
                     COMDAT();
                     CR040.setRGDT(this.PXDATO);
                     CR040.setLMDT(this.PXDATO);
                     CR040.setRGTM(movexTime());
                     CR040.setCHID().move(this.DSUSS);
                     CR040.setACAM(FASVL.getFAVA());
                     CR040.setCUAM(FASVL.getFAVA());
                     CR040.setDBCR(' ');
                     if (CRS750DS.getPBDCNY() == 1) {
                        if (CR040.getCUAM() < (0d - EPS_2)) {
                           CR040.setDBCR(CRS750DS.getPBDBNG());
                        } else {
                           CR040.setDBCR(CRS750DS.getPBDBPS());
                        }
                     }
                     if (DSDPTP.getFAEIDP() == 2) {
                        CR040.setEICD(1);
                     } else {
                        CR040.setEICD(0);
                     }
                     if (DSDPTP.getFAWFAM() == 1) {
                        CR040.WRITE("10");
                     }
                     // Handle equipment Accounting
                     wrtCAATRA();
                  }
                  //   Retrieve value types for accelerated depreciations
                  FASVL.setVATP(DSP.WXVTED);
                  IN91 = !FASVL.CHAIN("00", FASVL.getKey("00"));
                  if (!IN91 && !isBlank(FASVL.getFAVA(), EPS_2)) {
                     XXDEPR += FASVL.getFAVA();
                     XXDEPC += FASVL.getFAVC();
                     //   Check accounting reference number for depreciation credit
                     // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
                     //    Set params for CRTVACC (Scraps-Discarding of assets)
                     PXCRTVAC.PPCONO = LDAZD.CONO;
                     PXCRTVAC.PPDIVI.move(DSP.WWDIVI);
                     PXCRTVAC.PPEVEN.move("FA40");
                     PXCRTVAC.PPACTY.move("500 ");
                     PXCRTVAC.PPFDAT = XXACDT;
                     PXCRTVAC.PPCMTP = LDAZD.CMTP;
                     PXCRTVAC.PPLANC.move(LDAZD.LANC);
                     PXCRTVAC.PPDCFM.moveRight(LDAZD.DCFM);
                     PXCRTVAC.PPASID.move(FASMA.getASID());
                     PXCRTVAC.PPSBNO = FASMA.getSBNO();
                     PXCRTVAC.PPDPTP = FASDM.getDPTP();
                     //    Create accounting references
                     IN92 = PXCRTVAC.CRTVACC();
                     CR040.setDIVI().move(DSP.WWDIVI);
                     CR040.setACA()[0].move(PXCRTVAC.PPAIT1);
                     CR040.setACA()[1].move(PXCRTVAC.PPAIT2);
                     CR040.setACA()[2].move(PXCRTVAC.PPAIT3);
                     CR040.setACA()[3].move(PXCRTVAC.PPAIT4);
                     CR040.setACA()[4].move(PXCRTVAC.PPAIT5);
                     CR040.setACA()[5].move(PXCRTVAC.PPAIT6);
                     CR040.setACA()[6].move(PXCRTVAC.PPAIT7);
                     // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
                     CR040.setBCHN(0);
                     CR040.setTRNO(CR040.getTRNO() + 1);
                     CR040.setTRCD(1);
                     if (CRS750DS.getPBACBC() == 1) {
                        CR040.setVSER().move(DSFFNC.getDFVSER());
                     } else {
                        CR040.setVSER().clear();
                     }
                     CR040.setVONO(FAS145DS.getZVVONO());
                     CR040.setVDSC().move(FAS145DS.getZVVDSC());
                     CR040.setVTXT().move(FAS145DS.getZVVTXT());
                     CR040.setFEID().move("FA40");
                     CR040.setFNCN(1);
                     CR040.setTDSC().clear();
                     CR040.setACQT(0d);
                     CR040.setDCAM(LDAZD.LCDC);
                     if (IN88) {
                        CR040.setCUCD().move(SVCUCD);
                     } else {
                        CR040.setCUCD().move(MNDIV.getLOCD());
                     }
                     if (XXMUNI == 1 && DSMUST.getMCMUST() == 2) {
                        CR040.setARAT(DSMUCU.getMCARAT());
                     } else {
                        CR040.setARAT(1d);
                     }
                     CR040.setCRTP(1);
                     CR040.setPGNM().moveLeftPad("FAS145");
                     this.PXDFMI.move("TIME");
                     this.PXDATI = 0;
                     this.PXDFMO.move("YMD8");
                     this.PXOPRM = 1;
                     COMDAT();
                     CR040.setRGDT(this.PXDATO);
                     CR040.setLMDT(this.PXDATO);
                     CR040.setRGTM(movexTime());
                     CR040.setCHID().move(this.DSUSS);
                     CR040.setACAM(FASVL.getFAVA());
                     CR040.setCUAM(FASVL.getFAVA());
                     CR040.setDBCR(' ');
                     if (CRS750DS.getPBDCNY() == 1) {
                        if (CR040.getCUAM() < (0d - EPS_2)) {
                           CR040.setDBCR(CRS750DS.getPBDBNG());
                        } else {
                           CR040.setDBCR(CRS750DS.getPBDBPS());
                        }
                     }
                     if (DSDPTP.getFAEIDP() == 2) {
                        CR040.setEICD(1);
                     } else {
                        CR040.setEICD(0);
                     }
                     if (DSDPTP.getFAWFAM() == 1) {
                        CR040.WRITE("10");
                     }
                     // Handle equipment Accounting
                     wrtCAATRA();
                  }
                  if (!isBlank(XXDEPR, EPS_2)) {
                     //   Check accounting reference number for credit of depreciations
                     //   Matrix reference in FFDTSC move to subfile field WXACRA
                     // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
                     //    Set params for CRTVACC (Scraps-Discarding of assets)
                     PXCRTVAC.PPCONO = LDAZD.CONO;
                     PXCRTVAC.PPDIVI.move(DSP.WWDIVI);
                     PXCRTVAC.PPEVEN.move("FA40");
                     PXCRTVAC.PPACTY.move("570 ");
                     PXCRTVAC.PPFDAT = XXACDT;
                     PXCRTVAC.PPCMTP = LDAZD.CMTP;
                     PXCRTVAC.PPLANC.move(LDAZD.LANC);
                     PXCRTVAC.PPDCFM.moveRight(LDAZD.DCFM);
                     PXCRTVAC.PPASID.move(FASMA.getASID());
                     PXCRTVAC.PPSBNO = FASMA.getSBNO();
                     PXCRTVAC.PPDPTP = FASDM.getDPTP();
                     //    Create accounting references
                     IN92 = PXCRTVAC.CRTVACC();
                     CR040.setDIVI().move(DSP.WWDIVI);
                     CR040.setACA()[0].move(PXCRTVAC.PPAIT1);
                     CR040.setACA()[1].move(PXCRTVAC.PPAIT2);
                     CR040.setACA()[2].move(PXCRTVAC.PPAIT3);
                     CR040.setACA()[3].move(PXCRTVAC.PPAIT4);
                     CR040.setACA()[4].move(PXCRTVAC.PPAIT5);
                     CR040.setACA()[5].move(PXCRTVAC.PPAIT6);
                     CR040.setACA()[6].move(PXCRTVAC.PPAIT7);
                     // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
                     CR040.setBCHN(0);
                     CR040.setTRNO(CR040.getTRNO() + 1);
                     CR040.setTRCD(1);
                     if (CRS750DS.getPBACBC() == 1) {
                        CR040.setVSER().move(DSFFNC.getDFVSER());
                     } else {
                        CR040.setVSER().clear();
                     }
                     CR040.setVONO(FAS145DS.getZVVONO());
                     CR040.setVDSC().move(FAS145DS.getZVVDSC());
                     CR040.setVTXT().move(FAS145DS.getZVVTXT());
                     CR040.setFEID().move("FA40");
                     CR040.setFNCN(1);
                     CR040.setTDSC().clear();
                     CR040.setACQT(0d);
                     CR040.setDCAM(LDAZD.LCDC);
                     if (IN88) {
                        CR040.setCUCD().move(SVCUCD);
                     } else {
                        CR040.setCUCD().move(MNDIV.getLOCD());
                     }
                     if (XXMUNI == 1 && DSMUST.getMCMUST() == 2) {
                        CR040.setARAT(DSMUCU.getMCARAT());
                     } else {
                        CR040.setARAT(1d);
                     }
                     CR040.setCRTP(1);
                     CR040.setPGNM().moveLeftPad("FAS145");
                     this.PXDFMI.move("TIME");
                     this.PXDATI = 0;
                     this.PXDFMO.move("YMD8");
                     this.PXOPRM = 1;
                     COMDAT();
                     CR040.setRGDT(this.PXDATO);
                     CR040.setLMDT(this.PXDATO);
                     CR040.setRGTM(movexTime());
                     CR040.setCHID().move(this.DSUSS);
                     CR040.setACAM(-(XXDEPR));
                     CR040.setCUAM(-(XXDEPR));
                     CR040.setDBCR(' ');
                     if (CRS750DS.getPBDCNY() == 1) {
                        if (CR040.getCUAM() < (0d - EPS_2)) {
                           CR040.setDBCR(CRS750DS.getPBDBNG());
                        } else {
                           CR040.setDBCR(CRS750DS.getPBDBPS());
                        }
                     }
                     if (DSDPTP.getFAEIDP() == 2) {
                        CR040.setEICD(1);
                     } else {
                        CR040.setEICD(0);
                     }
                     if (DSDPTP.getFAWFAM() == 1) {
                        CR040.WRITE("10");
                     }
                     // Handle equipment Accounting
                     wrtCAATRA();
                  }
               }
               if (IN63) {
                  RTVFSV();
               }
            }
            XXSCVA -= XXDEPR;
            XXSCVC -= XXDEPC;
            //   Create record in FFAHIS
            FAHIS.setAIT1().clear();
            FAHIS.setAIT2().clear();
            FAHIS.setAIT3().clear();
            FAHIS.setAIT4().clear();
            FAHIS.setAIT5().clear();
            FAHIS.setAIT6().clear();
            FAHIS.setAIT7().clear();
            //   Set params for CRTVACC (Scraps-Discarding of assets)
            PXCRTVAC.PPCONO = LDAZD.CONO;
            PXCRTVAC.PPDIVI.moveLeftPad(DSP.WWDIVI);
            PXCRTVAC.PPEVEN.moveLeftPad("FA40");
            PXCRTVAC.PPACTY.moveLeftPad("570 ");
            PXCRTVAC.PPFDAT = XXACDT;
            PXCRTVAC.PPCMTP = LDAZD.CMTP;
            PXCRTVAC.PPLANC.move(LDAZD.LANC);
            PXCRTVAC.PPDCFM.moveRight(LDAZD.DCFM);
            PXCRTVAC.PPASID.moveLeftPad(FASMA.getASID());
            PXCRTVAC.PPSBNO = FASMA.getSBNO();
            PXCRTVAC.PPDPTP = FASDM.getDPTP();
            //   Create accounting references
            IN92 = PXCRTVAC.CRTVACC();
            if (PXCRTVAC.PPNMER == 0) {
               FAHIS.setAIT1().moveLeftPad(PXCRTVAC.PPAIT1);
               FAHIS.setAIT2().moveLeftPad(PXCRTVAC.PPAIT2);
               FAHIS.setAIT3().moveLeftPad(PXCRTVAC.PPAIT3);
               FAHIS.setAIT4().moveLeftPad(PXCRTVAC.PPAIT4);
               FAHIS.setAIT5().moveLeftPad(PXCRTVAC.PPAIT5);
               FAHIS.setAIT6().moveLeftPad(PXCRTVAC.PPAIT6);
               FAHIS.setAIT7().moveLeftPad(PXCRTVAC.PPAIT7);
            }
            FAHIS.setCONO(CR040.getCONO());
            FAHIS.setASID().move(DSP.WWASID);
            FAHIS.setSBNO(DSP.WWSBNO);
            if (IN63) {
               FAHIS.setASID().move(DSP.WWNWFN);
               FAHIS.setSBNO(DSP.WWNWSN);
            }
            FAHIS.setDIVI().move(DSP.WWDIVI);
            FAHIS.setVPER(CR040.getACYP());
            FAHIS.setCUCD().move(FASMA.getCUCD());
            if (CRS750DS.getPBACBC() == 1) {
               FAHIS.setVSER().move(DSFFNC.getDFVSER());
            } else {
               FAHIS.setVSER().clear();
            }
            if (DSDPTP.getFAWFAM() == 1) {
               FAHIS.setVONO(FAS145DS.getZVVONO());
               FAHIS.setVDSC().move(FAS145DS.getZVVDSC());
            } else {
               FAHIS.setVONO(0);
               FAHIS.setVDSC().clear();
               FAHIS.setVSER().clear();
            }
            FAHIS.setVATP(DSP.WSVATP);
            FAHIS.setFAVA(XXSCVA);
            FAHIS.setFAVC(XXSCVC);
            if (IN63) {
               FAHIS.setFAVA(XYSCVA);
               FAHIS.setFAVC(XYSCVC);
            }
            WKDPTP.move(FASDM.getDPTP());
            if (WKDPT1.getInt() == 9) {
               if (IN63) {
                  FASVL.setASID().move(DSP.WWNWFN);
                  FASVL.setSBNO(DSP.WWNWSN);
               }
               FASVL.setVATP(FASDM.getADTY());
               IN91 = !FASVL.CHAIN("00", FASVL.getKey("00"));
               IN92 = FASVL.getErr("00");
               if (!IN91) {
                  S9FAVA = FASVL.getFAVA();
                  S9FAVC = FASVL.getFAVC();
                  FAHIS.setFAVA(-(S9FAVA));
                  FAHIS.setFAVC(-(S9FAVC));
               }
            }
            FAHIS.setFAVA(mvxHalfAdjust(FAHIS.getFAVA(), getDecimalCode(MNDIV.getLOCD())));
            FAHIS.setFAVC(mvxHalfAdjust(FAHIS.getFAVC(), getDecimalCode(FAHIS.getCUCD())));
            FAHIS.setLMDT(CR040.getRGDT());
            FAHIS.setRGTM(CR040.getRGTM());
            FAHIS.setRGDT(CR040.getRGDT());
            FAHIS.setCHNO(CR040.getCHNO());
            FAHIS.setCHID().move(CR040.getCHID());
            FAHIS.setTSTM().clear();
            FAHIS.WRITE("00");
            //   - Create value type for scrap value
            FASVL.setASID().move(DSP.WWASID);
            FASVL.setSBNO(DSP.WWSBNO);
            if (IN63) {
               FASVL.setASID().move(DSP.WWNWFN);
               FASVL.setSBNO(DSP.WWNWSN);
            }
            FASVL.setDIVI().move(DSP.WWDIVI);
            FASVL.setVATP(DSP.WSVATP);
            IN91 = !FASVL.CHAIN_LOCK("00", FASVL.getKey("00"));
            if (IN91) {
               FASVL.clearNOKEY("00");
               FASVL.setLMDT(CR040.getRGDT());
               FASVL.setCHID().move(CR040.getCHID());
               FASVL.setCHNO(FASVL.getCHNO() + CR040.getCHNO());
               FASVL.setRGDT(CR040.getRGDT());
               FASVL.setRGTM(CR040.getRGTM());
               FASVL.setFAVA(XXSCVA);
               FASVL.setFAVC(XXSCVC);
               if (IN63) {
                  FASVL.setFAVA(mvxHalfAdjust(XYSCVA, getDecimalCode(MNDIV.getLOCD())));
                  FASVL.setFAVC(mvxHalfAdjust(XYSCVC, getDecimalCode(XVCUCD)));
               }
               if (WKDPT1.getInt() == 9) {
                  FASVL.setFAVA(-(S9FAVA));
                  FASVL.setFAVC(-(S9FAVC));
                  if (IN63) {
                     FASVL.setFAVA(mvxHalfAdjust((double)S9FAVA * XXPART, getDecimalCode(MNDIV.getLOCD())));
                     FASVL.setFAVA(-(FASVL.getFAVA()));
                     FASVL.setFAVC(mvxHalfAdjust((double)S9FAVC * XXPART, getDecimalCode(XVCUCD)));
                     FASVL.setFAVC(-(FASVL.getFAVC()));
                  }
               }
               FASVL.setCUCD().move(XVCUCD);
               FASVL.WRITE("00");
            } else {
               if (!IN63) {
                  FASVL.setFAVA(FASVL.getFAVA() + XXSCVA);
                  FASVL.setFAVC(FASVL.getFAVC() + XXSCVC);
               } else {
                  FASVL.setFAVA(FASVL.getFAVA() + XYSCVA);
                  FASVL.setFAVC(FASVL.getFAVC() + XYSCVC);
               }
               FASVL.setFAVA(mvxHalfAdjust(FASVL.getFAVA(), getDecimalCode(MNDIV.getLOCD())));
               FASVL.setFAVC(mvxHalfAdjust(FASVL.getFAVC(), getDecimalCode(FASVL.getCUCD())));
               FASVL.setCHID().move(CR040.getCHID());
               FASVL.setCHNO(FASVL.getCHNO() + 1);
               FASVL.setLMDT(CR040.getRGDT());
               FASVL.UPDAT("00");
            }
            XXSCVA = XXDEPR;
            XXSCVC = XXDEPC;
            if (DSDPTP.getFAWFAM() != 1) {   
               hasValidWFAM = true;    
            }  
            //   - Derogatory depreciation : scrap of assets
            //   Check accounting reference number for credit of depreciations
            //   Matrix reference in FFDTSC move to subfile field WXACRA
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -                        
            //update FASDM's status to sold/disposed
            FASDM.setDPTP(DSP.WSDPTP);            
            if (forDisposeDPTP != 0) {
               isRecordInSubFile = true;
               //update FASDM's status to sold/disposed
               FASDM.setDPTP(forDisposeDPTP);
            } else {
               forDisposeDPTP = DSP.WSDPTP;
               DSP.WBRRNA++;
               isRecordInSubFile = DSP.getSFL("BS", DSP.WBRRNA);
               //update FASDM's status to sold/disposed
               FASDM.setDPTP(forDisposeDPTP);
               forDisposeDPTP = 0;
            }             
            if (FASDM.CHAIN_LOCK("00", FASDM.getKey("00"))) { 
               //   Retrieve values for printout FAS146
               PRPPRT();
               UFAHIS();
               FASDM.setFAST(9);
               FASDM.UPDAT("00");               
            }
            if (forDisposeDPTP != 0) {
               break;
            }
         }
      }
      if (hasValidWFAM && DSDPTP.getFAWFAM() != 1) {  
         DSDPTP.setFAWFAM(1);    
      }  
      //   - Init key with value type for fixed assets total acquisition
      FAHIS.setCONO(LDAZD.CONO);
      FAHIS.setDIVI().move(DSP.WWDIVI);
      FAHIS.setASID().move(DSP.WWASID);
      FAHIS.setSBNO(DSP.WWSBNO);
      if (IN63) {
         FAHIS.setASID().move(DSP.WWNWFN);
         FAHIS.setSBNO(DSP.WWNWSN);
      }
      if (disposeAcqValue && willReverseAcqValue) {
         FAHIS.setVATP(FASDM.getBVAT());
      } else {
         FAHIS.setVATP(FAS900DS.getFAVATT());
      }
      CR040.setEICD(0);
      FAHIS.SETLL("00", FAHIS.getKey("00", 5));
      XXFAVA = 0d;
      while (FAHIS.READE("00", FAHIS.getKey("00", 5))) {
         // Accumulate all transactions with value type = Acquisition cost
         XXFAVA = mvxHalfAdjust((XXFAVA + FAHIS.getFAVA()), 2);
      }
      if (!isBlank(XXFAVA, EPS_2)) {
         CR040.setDIVI().move(DSP.WWDIVI);
         CR040.setACA()[0].move(FAHIS.getAIT1());
         CR040.setACA()[1].move(FAHIS.getAIT2());
         CR040.setACA()[2].move(FAHIS.getAIT3());
         CR040.setACA()[3].move(FAHIS.getAIT4());
         CR040.setACA()[4].move(FAHIS.getAIT5());
         CR040.setACA()[5].move(FAHIS.getAIT6());
         CR040.setACA()[6].move(FAHIS.getAIT7());
         CR040.setBCHN(0);
         CR040.setTRCD(1);
         CR040.setTRNO(CR040.getTRNO() + 1);
         if (CRS750DS.getPBACBC() == 1) {
            CR040.setVSER().move(DSFFNC.getDFVSER());
         } else {
            CR040.setVSER().clear();
         }
         // Retrieve verification number 
         if (vonoRetrieved == false) {
            if (PLCHKVO.FVMVMA == 2 &&
               DSDPTP.getFAWFAM() == 1) {
               PLCHKVO.FVFETC = 1;
               PLCHKVOSyncTo();
               IN92 = PLCHKVO.CCHKVON();
               PLCHKVOSyncFrom();
               FAS145DS.setZVVONO(PLCHKVO.FVVONO);
               FAS145DS.setZVVDSC().move(DSFFNC.getDFVDSC());
               vonoRetrieved = true;
            }
         }
         CR040.setVONO(FAS145DS.getZVVONO());
         CR040.setVDSC().move(FAS145DS.getZVVDSC());
         CR040.setVTXT().move(DSP.WWVTXT);
         CR040.setFEID().move("FA40");
         CR040.setFNCN(1);
         CR040.setTDSC().clear();
         CR040.setDCQT(0);
         CR040.setDCAM(LDAZD.LCDC);
         if (IN88) {
            CR040.setCUCD().move(SVCUCD);
         } else {
            CR040.setCUCD().move(MNDIV.getLOCD());
         }
         if (XXMUNI == 1 && DSMUST.getMCMUST() == 2) {
            CR040.setARAT(DSMUCU.getMCARAT());
         } else {
            CR040.setARAT(1d);
         }
         CR040.setCRTP(1);
         CR040.setPGNM().moveLeftPad("FAS145");
         this.PXDFMI.move("TIME");
         this.PXDATI = 0;
         this.PXDFMO.move("YMD8");
         this.PXOPRM = 1;
         COMDAT();
         CR040.setRGDT(this.PXDATO);
         CR040.setLMDT(CR040.getRGDT());
         CR040.setCHID().move(this.DSUSS);
         CR040.setRGTM(movexTime());
         CR040.setACAM(-(XXFAVA));
         CR040.setCUAM(-(XXFAVA));
         CR040.setACQT(XXFAQT);
         CR040.setDBCR(' ');
         if (CRS750DS.getPBDCNY() == 1) {
            if (CR040.getCUAM() < (0d - EPS_2)) {
               CR040.setDBCR(CRS750DS.getPBDBNG());
            } else {
               CR040.setDBCR(CRS750DS.getPBDBPS());
            }
         }
         if (DSDPTP.getFAWFAM() == 1 && willReverseAcqValue) {
            CR040.WRITE("10");
         }
         getLastTRNO(); 
         //   Check accounting reference reversals of tot acquisition
         //   Matrix reference in FAS945
         // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
         //    Set params for CRTVACC (Scraps-Purchase of assets)
         PXCRTVAC.PPCONO = LDAZD.CONO;
         PXCRTVAC.PPDIVI.move(DSP.WWDIVI);
         PXCRTVAC.PPEVEN.move("FA40");
         PXCRTVAC.PPACTY.move("550 ");
         PXCRTVAC.PPFDAT = XXACDT;
         PXCRTVAC.PPCMTP = LDAZD.CMTP;
         PXCRTVAC.PPLANC.move(LDAZD.LANC);
         PXCRTVAC.PPDCFM.moveRight(LDAZD.DCFM);
         PXCRTVAC.PPASID.move(FASMA.getASID());
         PXCRTVAC.PPSBNO = FASMA.getSBNO();
         PXCRTVAC.PPDPTP = FASDM.getDPTP();
         //    Create accounting references
         IN92 = PXCRTVAC.CRTVACC();
         CR040.setACA()[0].move(PXCRTVAC.PPAIT1);
         CR040.setACA()[1].move(PXCRTVAC.PPAIT2);
         CR040.setACA()[2].move(PXCRTVAC.PPAIT3);
         CR040.setACA()[3].move(PXCRTVAC.PPAIT4);
         CR040.setACA()[4].move(PXCRTVAC.PPAIT5);
         CR040.setACA()[5].move(PXCRTVAC.PPAIT6);
         CR040.setACA()[6].move(PXCRTVAC.PPAIT7);
         // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
         IN91 = !CR040.CHAIN_LOCK("10", CR040.getKey("10", 11));
         IN92 = CR040.getErr("10");
         // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
         if (IN91 || forceWriteReveralAcquisitionCost) {
            if (!IN91) {
               CR040.UNLOCK("10");
            }
            CR040.setBCHN(0);
            CR040.setTRCD(1);
            CR040.setFEID().move("FA40");
            CR040.setFNCN(1);
            CR040.setTRNO(lastTRNO + 1);
            if (CRS750DS.getPBACBC() == 1) {
               CR040.setVSER().move(DSFFNC.getDFVSER());
            } else {
               CR040.setVSER().clear();
            }
            CR040.setVONO(FAS145DS.getZVVONO());
            if (XXMUNI == 1 && DSMUST.getMCMUST() == 2) {
               CR040.setARAT(DSMUCU.getMCARAT());
            } else {
               CR040.setARAT(1d);
            }
            CR040.setCRTP(1);
            CR040.setPGNM().moveLeftPad("FAS145");
            this.PXDFMI.move("TIME");
            this.PXDATI = 0;
            this.PXDFMO.move("YMD8");
            this.PXOPRM = 1;
            COMDAT();
            CR040.setRGDT(this.PXDATO);
            CR040.setLMDT(this.PXDATO);
            CR040.setRGTM(movexTime());
            CR040.setCHID().move(this.DSUSS);
            CR040.setACAM(XXFAVA);
            CR040.setCUAM(XXFAVA);
            if (CRS750DS.getPBDCNY() == 1) {
               if (CR040.getCUAM() < (0d - EPS_2)) {
                  CR040.setDBCR(CRS750DS.getPBDBNG());
               } else {
                  CR040.setDBCR(CRS750DS.getPBDBPS());
               }
            }
            if (DSDPTP.getFAWFAM() == 1 && willReverseAcqValue) {
               CR040.WRITE("10");
            }
         } else {
            CR040.setACAM(CR040.getACAM() + XXFAVA);
            CR040.setCUAM(CR040.getCUAM() + XXFAVA);
            CR040.setDBCR(' ');
            if (CRS750DS.getPBDCNY() == 1) {
               if (CR040.getCUAM() < (0d - EPS_2)) {
                  CR040.setDBCR(CRS750DS.getPBDBNG());
               } else {
                  CR040.setDBCR(CRS750DS.getPBDBPS());
               }
            }
            this.PXDFMI.move("TIME");
            this.PXDATI = 0;
            this.PXDFMO.move("YMD8");
            this.PXOPRM = 1;
            COMDAT();
            CR040.setLMDT(this.PXDATO);
            CR040.setCHID().move(this.DSUSS);
            CR040.setCHNO(CR040.getCHNO() + 1);
            if (isBlank(CR040.getACAM(), EPS_2)) {
               CR040.DELET("10");
            } else {
               CR040.UPDAT("10");
            }
         }
         // Handle equipment Accounting
         wrtCAATRA();
      }
      FASMA.setASID().move(DSP.WWASID);
      FASMA.setSBNO(DSP.WWSBNO);
      if (IN63) {
         FASMA.setASID().move(DSP.WWNWFN);
         FASMA.setSBNO(DSP.WWNWSN);
      }
      //   - Check record
      IN91 = !FASMA.CHAIN_LOCK("00", FASMA.getKey("00"));
      IN92 = FASMA.getErr("00");
      if (!IN91) {
         if (!hasActiveDPTP(FASMA.getCONO(), FASMA.getDIVI(), FASMA.getASID(), FASMA.getSBNO())) {
            FASMA.setFAST(9);
         }
         FASMA.setSPER(XXACDT);
         FASMA.setPYNO().clear();
         FASMA.UPDAT("00");
      }
   }

   /**
   *    RTVFSV - Retrieve amounts from FFASVL for new asset
   */
   public void RTVFSV() {
      FASVL.setASID().move(DSP.WWNWFN);
      FASVL.setSBNO(DSP.WWNWSN);
      FASVL.setVATP(FASDM.getBVAT());
      IN91 = !FASVL.CHAIN("00", FASVL.getKey("00"));
      IN92 = FASVL.getErr("00");
      if (!IN91) {
         XYSCVA = FASVL.getFAVA();
         XYSCVC = FASVL.getFAVC();
      }
      FASVL.setVATP(FASDM.getVTAD());
      IN91 = !FASVL.CHAIN("00", FASVL.getKey("00"));
      IN92 = FASVL.getErr("00");
      if (!IN91) {
         XYSCVA -= FASVL.getFAVA();
         XYSCVC -= FASVL.getFAVC();
      }
      FASVL.setVATP(FASDM.getADTY());
      IN91 = !FASVL.CHAIN("00", FASVL.getKey("00"));
      IN92 = FASVL.getErr("00");
      if (!IN91) {
         XYSCVA -= FASVL.getFAVA();
         XYSCVC -= FASVL.getFAVC();
      }
      FASVL.setVATP(DSP.WXVTED);
      IN91 = !FASVL.CHAIN("00", FASVL.getKey("00"));
      IN92 = FASVL.getErr("00");
      if (!IN91) {
         XYSCVA -= FASVL.getFAVA();
         XYSCVC -= FASVL.getFAVC();
      }
   }

   /**
   *    DIVASS - Divide an asset
   */
   public void DIVASS() {
      FASMA.setASID().move(DSP.WWASID);
      FASMA.setSBNO(DSP.WWSBNO);
      IN91 = !FASMA.CHAIN_LOCK("00", FASMA.getKey("00"));
      IN92 = FASMA.getErr("00");
      FASMA.setFAQT(FASMA.getFAQT() - XXFAQT);
      this.PXDFMI.move("TIME");
      this.PXDATI = 0;
      this.PXDFMO.move("YMD8");
      this.PXOPRM = 1;
      COMDAT();
      FASMA.setLMDT(this.PXDATO);
      FASMA.setRGTM(movexTime());
      FASMA.setCHID().move(this.DSUSS);
      FASMA.setCHNO(FASMA.getCHNO() + 1);
      if (!IN91) {
         FASMA.UPDAT("00");
         FASMA.setASID().move(DSP.WWNWFN);
         FASMA.setSBNO(DSP.WWNWSN);
         FASMA.setFAQT(XXFAQT);
         FASMA.setSPER(0);
         FASMA.setPYNO().clear();
         FASMA.setCINO().clear();
         FASMA.setSCUC().clear();
         FASMA.setSRAT(0d);
         FASMA.setFAST(1);
         this.PXDFMI.move("TIME");
         this.PXDATI = 0;
         this.PXDFMO.move("YMD8");
         this.PXOPRM = 1;
         COMDAT();
         FASMA.setLMDT(this.PXDATO);
         FASMA.setRGDT(FASMA.getLMDT());
         FASMA.setRGTM(movexTime());
         FASMA.setCHID().move(this.DSUSS);
         FASMA.setCHNO(1);
         IN91 = !FASMA.CHAIN_LOCK("00", FASMA.getKey("00"));
         IN92 = FASMA.getErr("00");
         if (IN91) {
            FASMA.WRITE("00");
         }
      }
      //   Depreciation rules for assets
      FASDM.setASID().move(DSP.WWASID);
      FASDM.setSBNO(DSP.WWSBNO);
      FASDM.SETLL("00", FASDM.getKey("00", 4));
      IN93 = !FASDM.READE_LOCK("00", FASDM.getKey("00", 4));
      IN92 = FASDM.getErr("00");
      while (!IN93) {
         double savedStopValue = FASDM.getSVAL();
         FASDM.setSVAL(FASDM.getSVAL() * YYPART);
         FASDM.UPDAT("00");
         FASDM.setSVAL(savedStopValue * XXPART);
         FASDM.setASID().move(DSP.WWNWFN);
         FASDM.setSBNO(DSP.WWNWSN);
         this.PXDFMI.move("TIME");
         this.PXDATI = 0;
         this.PXDFMO.move("YMD8");
         this.PXOPRM = 1;
         COMDAT();
         FASDM.setRGDT(this.PXDATO);
         FASDM.setRGTM(movexTime());
         FASDM.setCHNO(FASDM.getCHNO() + 1);
         FASDM.setCHID().move(this.DSUSS);
         FASDM.WRITE("00");
         FASDM.setASID().move(DSP.WWASID);
         FASDM.setSBNO(DSP.WWSBNO);
         IN93 = !FASDM.READE_LOCK("00", FASDM.getKey("00", 4));
         IN92 = FASDM.getErr("00");
      }
      //   Accumulated depreciations
      FASVL.setASID().move(DSP.WWASID);
      FASVL.setSBNO(DSP.WWSBNO);
      FASVL.setVATP(0);
      FASVL.SETLL("00", FASVL.getKey("00"));
      IN83 = !FASVL.READE_LOCK("00", FASVL.getKey("00", 4));
      IN92 = FASVL.getErr("00");
      while (!IN83) {
         AVASCR = mvxHalfAdjust((double)FASVL.getFAVA() * XXPART, getDecimalCode(MNDIV.getLOCD()));
         FASVL.setFAVA(FASVL.getFAVA() - AVASCR);
         AVCSCR = mvxHalfAdjust((double)FASVL.getFAVC() * XXPART, getDecimalCode(FASVL.getCUCD()));
         FASVL.setFAVC(FASVL.getFAVC() - AVCSCR);
         FASVL.UPDAT("00");
         FAHIS.setASID().move(FASVL.getASID());
         FAHIS.setSBNO(FASVL.getSBNO());
         FAHIS.setVATP(FASVL.getVATP());
         FAHIS.setVPER(0);
         FAHIS.SETLL("10", FASVL.getKey("00"));
         IN93 = !FAHIS.READE_LOCK("10", FASVL.getKey("00"));
         IN92 = FAHIS.getErr("10");
         while (!IN93) {
            FAHIS.setASID().move(DSP.WWNWFN);
            FAHIS.setSBNO(DSP.WWNWSN);
            FAHIS.setFAVA(mvxHalfAdjust((double)FAHIS.getFAVA() * XXPART, getDecimalCode(MNDIV.getLOCD())));
            FAHIS.setFAVC(mvxHalfAdjust((double)FAHIS.getFAVC() * XXPART, getDecimalCode(FAHIS.getCUCD())));
            FAHIS.setRGDT(this.PXDATO);
            FAHIS.setRGTM(movexTime());
            FAHIS.setCHNO(FAHIS.getCHNO() + 1);
            FAHIS.setCHID().move(this.DSUSS);
            FAHIS.setTSTM().clear();
            FAHIS.WRITE("10");
            IN93 = !FAHIS.READE_LOCK("10", FASVL.getKey("00"));
            IN92 = FAHIS.getErr("10");
         }
         FASVL.setASID().move(DSP.WWNWFN);
         FASVL.setSBNO(DSP.WWNWSN);
         FASVL.setFAVA(mvxHalfAdjust(AVASCR, getDecimalCode(MNDIV.getLOCD())));
         FASVL.setFAVC(mvxHalfAdjust(AVCSCR, getDecimalCode(FASVL.getCUCD())));
         this.PXDFMI.move("TIME");
         this.PXDATI = 0;
         this.PXDFMO.move("YMD8");
         this.PXOPRM = 1;
         COMDAT();
         FASVL.setRGDT(this.PXDATO);
         FASVL.setRGTM(movexTime());
         FASVL.setCHNO(FASVL.getCHNO() + 1);
         FASVL.setCHID().move(this.DSUSS);
         FASVL.WRITE_CHK("00");
         FASVL.setASID().move(DSP.WWASID);
         FASVL.setSBNO(DSP.WWSBNO);
         IN83 = !FASVL.READE_LOCK("00", FASVL.getKey("00", 4));
         IN92 = FASVL.getErr("00");
      }
   }

   /**
   *    SAVSTR - Save start values
   */
   public void SAVSTR() {
      //   Update start values
      IN91 = !SYSTR.CHAIN_LOCK("00", KSYSTR());
      IN92 = SYSTR.getErr("00");
      if (!IN91) {
         this.PXDFMI.move("TIME");
         this.PXDATI = 0;
         this.PXDFMO.move("YMD8");
         this.PXOPRM = 1;
         COMDAT();
         SYSTR.setLMDT(this.PXDATO);
         CSIN52.move(toChar(IN52));
         CSIN53.move(toChar(IN53));
         CSIN54.move(toChar(IN54));
         SYSTR.setPAR1().moveLeft(XXPAR1);
         SYSTR.setPAR2().moveLeft(FAS145DS.getFAS145DS());
         SYSTR.UPDAT("00");
      }
   }
   
   /**
    *   populateDPTP - populate the allowedDPTP variable
    */
   public void populateAllowedDPTP() {    
      int index = 0;    
      FDTSC.setDIVI().move(DSP.WWDIVI);   
      FDTSC.SETLL("00", FDTSC.getKey("00", 2));    
      while (FDTSC.READE("00", FDTSC.getKey("00", 2))) {    
         allowedDPTP[index++] = FDTSC.getDPTP();   
         if (index >= allowedDPTP.length) {     
            int[] tempDPTP = new int[allowedDPTP.length + 10];    
            for (int ctr = 0; ctr < allowedDPTP.length; ctr++) {  
               tempDPTP[ctr] = allowedDPTP[ctr];   
            }  
            allowedDPTP = tempDPTP;    
         }  
      }        
   }

   public int getDecimalCode(MvxString CUCD) {
      if (VectorOfCUCD == null) {
         VectorOfCUCD = new Vector(10, 10);
         VectorOfDCCD = new Vector(10, 10);
      }
      if (CUCD.EQ(lastUsedCUCD)) {
         return decimalCode;
      }
      if (CUCD.EQ(LDAZD.LOCD)) {
         lastUsedCUCD.move(CUCD);
         return decimalCode = LDAZD.LCDC;
      }
      if (VectorOfCUCD.contains(CUCD)) {
         lastUsedCUCD.move(CUCD);
         return decimalCode = ((Integer)VectorOfDCCD.get(VectorOfCUCD.indexOf(CUCD))).intValue();
      } else {
         lastUsedCUCD.move(CUCD);
         SYTAB.setCONO(LDAZD.CONO);
         SYTAB.setDIVI().clear();
         SYTAB.setSTCO().moveLeftPad("CUCD");
         SYTAB.setSTKY().moveLeftPad(CUCD);
         SYTAB.setLNCD().clear();
         IN91 = !SYTAB.CHAIN("00", SYTAB.getKey("00"));
         if (!IN91) {
            DSCUCD.setDSCUCD().moveLeft(SYTAB.getPARM());
            decimalCode = DSCUCD.getYQDCCD();
         } else {
            decimalCode = LDAZD.LCDC;
         }
         VectorOfCUCD.add(CUCD);
         VectorOfDCCD.add(new Integer(decimalCode));
         return decimalCode;
      }
   }
   
   /**
    *   hasActiveDPTP - checks Assets Depreciation types for active (0) status
    *   
    *   @param CONO - company
    *   @param DIVI - division
    *   @param ASID - fixed asset
    *   @param SBNO - subnumber
    *   @return true if asset is fully depreciated
    */
   public boolean hasActiveDPTP(int CONO, MvxString DIVI, MvxString ASID, int SBNO) {  
      //   We loop on all the assets' Depreciation types    
      FASDM.setCONO(CONO);    
      FASDM.setDIVI().moveLeftPad(DIVI);
      FASDM.setASID().moveLeftPad(ASID);  
      FASDM.setSBNO(SBNO);    
      FASDM.setDPTP(0);    
      FASDM.SETLL("00", FASDM.getKey("00"));    
      while (FASDM.READE("00", FASDM.getKey("00", 4))) {    
         if (FASDM.getFAST() == 0 || FASDM.getFAST() == 1 || FASDM.getFAST() == 8) {   
            //  Depreciation type is active              
            if (lookUpEQ(allowedDPTP, 0, FASDM.getDPTP()) >= 0) {                   
               return true;   
            }  
         }  
      }  
      return false;  
   }

   /**
    * hasOtherDisposableDPTP - Checks records if there is/are other disposable DPTPs that is other than
    *                          BVAT == VATT and FIM = 1.
    * @param cono - current company
    * @param divi - current division
    * @param asid - current fixed asset ID
    * @param sbno - current sub number
    * @param currentDPTP - depreciation type to be disposed
    * @return True if there are other valid DPTP in FA, False otherwise.
    */
   public boolean hasOtherDisposableDPTP(int cono, MvxString divi, MvxString asid, int sbno, int currentDPTP, boolean checkWFAM) {   
      boolean retValue = false;  
      MvxRecord savedFASDM = FASDM.getKey("00");   
      // Now retrieve other DPTP in FASDM    
      notValidDisposableDPTP = 0;   
      FASDM.setCONO(cono);    
      FASDM.setDIVI().moveLeftPad(divi);  
      FASDM.setASID().moveLeftPad(asid);  
      FASDM.setSBNO(sbno);    
      FASDM.SETLL("00", FASDM.getKey("00", 4));    
      FASDM.setSelection("00", "FDDPTP", "NE", String.valueOf(currentDPTP)); 
      FASDM.setSelection("00", "FDFAST", "NE", "9");  
      while (FASDM.READE("00", FASDM.getKey("00", 4))) {
         if (checkWFAM) {
            DSDPTP.setDSDPTP().clear();   
            DSDPTP = getDPTPInfo(FASDM.getDPTP(), DSDPTP);
            if (FASDM.getBVAT() == FAS900DS.getFAVATT() && DSDPTP.getFAWFAM() == 1) {  
               retValue = true;  
               break;   
            } else {    
               notValidDisposableDPTP = FASDM.getDPTP();    
               IN60 = true;     
            }  
         } else {
            if (FASDM.getBVAT() == FAS900DS.getFAVATT()) {  
               retValue = true;  
               break;   
            } else {    
               notValidDisposableDPTP = FASDM.getDPTP();    
               IN60 = true;     
            }
         } 
      }  
      FASDM.CHAIN("00", savedFASDM);   
      return retValue;  
   }

   /**
    * 
    * @param cono - current company
    * @param divi - current division
    * @param asid - current fixed asset ID
    * @param sbno - current sub number    
    * @return True if all are valid DPTP in FA, False otherwise.
    */
   public boolean checkIfAllDisposableDPTP(int cono, MvxString divi, MvxString asid, int sbno) {   
      boolean retValue = true;   
      FDTSC.setCONO(cono);    
      FDTSC.setDIVI().moveLeftPad(divi);  
      // Now retrieve other DPTP in FASDM    
      FASDM.setCONO(cono);    
      FASDM.setDIVI().moveLeftPad(divi);  
      FASDM.setASID().moveLeftPad(asid);  
      FASDM.setSBNO(sbno);    
      FASDM.SETLL("00", FASDM.getKey("00", 4));    
      FASDM.setSelection("00", "FDFAST", "NE", "9");  
      while (FASDM.READE("00", FASDM.getKey("00", 4))) {    
         // before checking the depreciation type, make sure it is on the allowed to be disposed depreciation type list (ABS945)    
         FDTSC.setDPTP(FASDM.getDPTP());  
         if (FDTSC.CHAIN("00", FDTSC.getKey("00", 3))) {    
            DSDPTP.setDSDPTP().clear();   
            DSDPTP = getDPTPInfo(FASDM.getDPTP(), DSDPTP);
            if (totalDPTP == 1) {    
               notValidDisposableDPTP = FASDM.getDPTP();    
               break;   
            } else {
               if (option11 && DSDPTP.getFAWFAM() == 0) {    
                  retValue = false;    
                  notValidDisposableDPTP = FASDM.getDPTP();    
                  break;   
               }
               if (FASDM.getBVAT() != FAS900DS.getFAVATT() && DSDPTP.getFAWFAM() == 1) {    
                  retValue = false;    
                  notValidDisposableDPTP = FASDM.getDPTP();    
                  break;   
               }
            }
         }  
      }  
      return retValue;  
   }

   /**
    * 
    * @param cono - current company
    * @param divi - current division
    * @param asid - current fixed asset ID
    * @param sbno - current sub number
    * @param currentDPTP - depreciation type to be disposed
    * @return True if there is a valid DPTP in FA, False otherwise.
    */
   public boolean hasValidDisposableDPTP(int cono, MvxString divi, MvxString asid, int sbno) {  
      boolean retValue = false;  
      MvxRecord savedFASDM = FASDM.getKey("00");   
      // Now retrieve other DPTP in FASDM    
      FASDM.setCONO(cono);    
      FASDM.setDIVI().moveLeftPad(divi);  
      FASDM.setASID().moveLeftPad(asid);  
      FASDM.setSBNO(sbno);    
      FASDM.SETLL("00", FASDM.getKey("00", 4));    
      FASDM.setSelection("00", "FDBVAT", "EQ", String.valueOf(FAS900DS.getFAVATT()));  
      FASDM.setSelection("00", "FDFAST", "NE", "9");        
      while (FASDM.READE("00", FASDM.getKey("00", 4))) {    
         DSDPTP.setDSDPTP().clear();   
         DSDPTP = getDPTPInfo(FASDM.getDPTP(), DSDPTP);  
         retValue = true;
         notValidDisposableDPTP = FASDM.getDPTP();
         break;
      }  
      FASDM.CHAIN("00", savedFASDM);   
      return retValue;  
   }

   /**
    * 
    * @param cono - current company
    * @param divi - current division
    * @param asid - current fixed asset ID
    * @param sbno - current sub number
    * @param currentDPTP - depreciation type to be disposed
    * @return True if there is a valid DPTP in FA, False otherwise.
    */
   public boolean validateDPTPError(int CONO, MvxString DIVI, MvxString ASID, int SBNO, int DPTP) {   
      boolean retValue = false;  
      continueDispose = false;
      MvxRecord savedFASDM = FASDM.getKey("00");   
      // Now retrieve DPTP in FASDM    
      FASDM.setCONO(CONO);    
      FASDM.setDIVI().moveLeftPad(DIVI);  
      FASDM.setASID().moveLeftPad(ASID);  
      FASDM.setSBNO(SBNO);    
      FASDM.setDPTP(DPTP);    
      if (FASDM.CHAIN("00", FASDM.getKey("00"))) {       
         DSDPTP.setDSDPTP().clear();
         DSDPTP = getDPTPInfo(FASDM.getDPTP(), DSDPTP);
         if (FASDM.getBVAT() != FAS900DS.getFAVATT() && DSDPTP.getFAWFAM() == 1) {    
            //  MSGID=FA14517 Depreciation type &1 has Transfer to FAM but different base depreciation value type from FAS900.   
            COMPMQ("FA14517", String.valueOf(DPTP));  
            retValue = true;
            continueDispose = false;
            retValidation = false;
         } else if (DSDPTP.getFAWFAM() == 0 && totalDPTP != 1) {
            //  MSGID=FA14515 Depreciation type &1 must be disposed first  
            COMPMQ("FA14515", String.valueOf(notValidDisposableDPTP));         
            retValue = true;
            continueDispose = false;
            retValidation = true;
         } else {
            continueDispose = true;
            retValue = true;
            retValidation = false;
         }
      }  
      if (!retValue) {  
         //  MSGID=FA14514 Depreciation type &1 has the same base value type without Transfer to FAM  
         COMPMQ("FA14514", String.valueOf(DPTP));  
         continueDispose = false;
      }  
      FASDM.CHAIN("00", savedFASDM);   
      return retValue;  
   }

   /**
   *    Returns the new date minus 1
   *
   *    @return integer period (YYYYMMDD)
   */
   public int dateMinus(int minus) {
      this.PXDFMI.clear();
      this.PXDATI = minus;
      this.PXDFMI.move("YMD8");
      this.PXDFMO.move("CDNO");
      this.PXOPRM = 0;
      COMDAT();
      this.PXDATO -= 1;
      this.PXDATI = this.PXDATO;
      this.PXDFMI.move("CDNO");
      this.PXDFMO.move("YMD8");
      this.PXOPRM = 0;
      COMDAT();
      return this.PXDATO;
   } 
   
   /**
   *    Fetch year - period from CSYCAL
   *
   *    @param period
   */
   public int periodFetch(int period) {   
      this.PXDFMI.move("YMD8");
      this.PXDATI = period;
      this.PXOPRM = 0;
      this.PXDSEP = ' ';
      COMDAT();
      switch (MNDIV.getPTFA()) {
         case 1:
            return this.PXCYP1;
         case 2:
            return this.PXCYP2;
         case 3:
            return this.PXCYP3;  
         case 4:
            return this.PXCYP4;  
         case 5:
            return this.PXCYP5;
         default:
            return period;      
      }
      
   }
   
   /*       
   *    wrtCAATRA - Write transactions to CAATRA, Equipment Accounting     
   */       
   public void wrtCAATRA() {     
       //   Get Cost accounting parameters          
       if (X1CONO !=PXCRTVAC.PPCONO ||        
           X1DIVI.NE(PXCRTVAC.PPDIVI) ||         
           X1STCO.NE("CAS900")) {          
           SYPAR.setCONO(PXCRTVAC.PPCONO);          
           SYPAR.setDIVI().move(PXCRTVAC.PPDIVI);      
           SYPAR.setSTCO().moveLeftPad("CAS900");         
           X1CONO = SYPAR.getCONO();       
           X1DIVI.move(SYPAR.getDIVI());         
           X1STCO.moveLeftPad(SYPAR.getSTCO());        
           IN91 = !SYPAR.CHAIN("00", SYPAR.getKey("00"));          
           if (IN91) {      
              CAS900DS.setCAS900DS().clear();          
           } else {         
              CAS900DS.setCAS900DS().moveLeft(SYPAR.getPARM());          
           }          
       }              
       // Check if accounting model is used (CAS900 - Inst. param)          
       if (CAS900DS.getP2ACMU() == 1) {          
          // Check if accounting Model Line Rules exists in CAMRUL (CAS390)          
          AMRUL.setCONO(PXCRTVAC.PPCONO);        
          AMRUL.setDIVI().moveLeftPad(PXCRTVAC.PPDIVI);         
          AMRUL.setEVEN().moveLeftPad(PXCRTVAC.PPEVEN);         
          AMRUL.setACTY().moveLeftPad(PXCRTVAC.PPACTY);         
          AMRUL.setMOLN(0);          
          AMRUL.setMOID().clear();                  
          if (AMRUL.CHAIN("10", AMRUL.getKey("10",4))) {        
             if (ZABJNO.NE(XZBJNO)) {        
                transNoCAATRA = 1;         
             } else {      
                transNoCAATRA++;        
             }        
             // Write transaction to CAATRA         
             AATRA.setCONO(AMRUL.getCONO());        
             AATRA.setDIVI().moveLeftPad(AMRUL.getDIVI());         
             AATRA.setBJNO().moveLeftPad(ZABJNO);         
             AATRA.setSEQN(transNoCAATRA);                             
             if (!AATRA.CHAIN("00", AATRA.getKey("00",4))) {         
                AATRA.clearNOKEY("00");          

                // Set status 10 (new record)          
                AATRA.setSTAT().moveLeft("20");        

                // Set values from param.list PXCRTVAC          
                loadCAATRA();     

                // Set values from FCR040        
                PLCLCCU.FZCONO =AATRA.getCONO();          
                PLCLCCU.FZDIVI.moveLeft(AATRA.getDIVI());          
                PLCLCCU.FZDMCU = 0;        
                PLCLCCU.FZCRTP = CRS750DS.getPBTCRT();          
                PLCLCCU.FZCUTD = CR040.getACDT();         
                PLCLCCU.FZARAT = 0d;          
                PLCLCCU.FZRAFA = 0;        
                PLCLCCU.FZCUAM = 0d;          
                PLCLCCU.FZACAM = CR040.getACAM();      
                PLCLCCU.FZTEST = 0;        
                PLCLCCU.FZCMTP = LDAZD.CMTP;        
                PLCLCCU.FZVERR = 0;        
                PLCLCCU.FZMSGN = 0;        
                PLCLCCU.FZCUCD.move(CRS750DS.getPBTHCC());         
                PLCLCCU.FZMSGI.clear();          
                PLCLCCU.FZMSGA.clear();          
                PLCLCCU.CCLCCUR();         
                if (PLCLCCU.FZVERR == 1) {          
                   AATRA.setCURC(0d);         
                } else {          
                   AATRA.setCURC(PLCLCCU.FZACAM);         
                }        

                AATRA.setACDT(CR040.getACDT());        
                AATRA.setACAM(CR040.getACAM());        

                this.PXDFMI.move("TIME");        
                this.PXDATI = 0;        
                this.PXDFMO.move("YMD8");        
                this.PXOPRM = 1;        
                COMDAT();         
                AATRA.setRGDT(this.PXDATO);         
                AATRA.setRGTM(movexTime());         
                AATRA.setLMDT(AATRA.getRGDT());        
                AATRA.setCHID().move(this.DSUSS);         
                AATRA.setCHNO(1);          

                AATRA.WRITE_CHK("00");        

                // Write transaction to CAACTL         
                if (AATRA.getBJNO().NE(XZBJNO)) {        
                   XZBJNO.moveLeft(AATRA.getBJNO());         
                   AACTL.setCONO(AATRA.getCONO());        
                   AACTL.setDIVI().moveLeftPad(AATRA.getDIVI());         
                   AACTL.setBJNO().moveLeftPad(AATRA.getBJNO());         
                   if (!AACTL.CHAIN("00", AACTL.getKey("00"))) {         
                      AACTL.clearNOKEY("00");          
                      AACTL.setBJNO().moveLeftPad(AATRA.getBJNO());         
                      AACTL.setBJN2().moveLeftPad(AATRA.getBJNO());         
                      AACTL.setPGNM().moveLeft(this.DSPGM);        
                      AACTL.setSTAT().moveLeft("20");        

                      this.PXDFMI.move("TIME");        
                      this.PXDATI = 0;        
                      this.PXDFMO.move("YMD8");        
                      this.PXOPRM = 1;        
                      COMDAT();         
                      AACTL.setRGDT(this.PXDATO);         
                      AACTL.setRGTM(movexTime());         
                      AACTL.setLMDT(AACTL.getRGDT());        
                      AACTL.setCHID().move(this.DSUSS);         
                      AACTL.setCHNO(1);          

                      AACTL.WRITE_CHK("00");     
                   }     
                }        
             }        
          }        
       }     
    } 
    
    /**
    *    loadCAATRA- Load fieldvalues from PXCRTVAC
    */
    public void loadCAATRA() {          
       AATRA.setINPX().moveLeft(PXCRTVAC.PPINPX);      
       AATRA.setIVNO(PXCRTVAC.PPIVNO);     
       AATRA.setYEA4(PXCRTVAC.PPYEA4);        
       AATRA.setDLIX(PXCRTVAC.PPDLIX);        
       AATRA.setVTCD(PXCRTVAC.PPVTCD);        
       AATRA.setPONR(PXCRTVAC.PPPONR);     
       AATRA.setPOSX(PXCRTVAC.PPPOSX);        
       AATRA.setSBNO(PXCRTVAC.PPSBNO);        
       AATRA.setDPTP(PXCRTVAC.PPDPTP);     
       AATRA.setMFNO().move(PXCRTVAC.PPMFNO);          
       AATRA.setMSEQ(PXCRTVAC.PPMSEQ);     
       AATRA.setOPNO(PXCRTVAC.PPOPNO);        
       AATRA.setTMRN(PXCRTVAC.PPTMRN);        
       AATRA.setTRNO(PXCRTVAC.PPTRNO);        
       AATRA.setREPN(PXCRTVAC.PPREPN);        
       AATRA.setRELI(PXCRTVAC.PPRELI);        
       AATRA.setDIPO(PXCRTVAC.PPDIPO);        
       AATRA.setRORC(PXCRTVAC.PPRORC);     
       AATRA.setFRTO(PXCRTVAC.PPFRTO);        
       AATRA.setREWK(PXCRTVAC.PPREWK);        
       AATRA.setORNO().moveLeftPad(PXCRTVAC.PPORNO);         
       AATRA.setWHLO().moveLeftPad(PXCRTVAC.PPWHLO);         
       AATRA.setCUNO().moveLeftPad(PXCRTVAC.PPCUNO);         
       AATRA.setASID().moveLeftPad(PXCRTVAC.PPASID);         
       AATRA.setTRNR().moveLeftPad(PXCRTVAC.PPTRNR);         
       AATRA.setSUNO().moveLeftPad(PXCRTVAC.PPSUNO);         
       AATRA.setCRID().moveLeftPad(PXCRTVAC.PPCRID);         
       AATRA.setFACI().moveLeftPad(PXCRTVAC.PPFACI);         
       AATRA.setBKID().moveLeftPad(PXCRTVAC.PPBKID);         
       AATRA.setCUCD().moveLeftPad(PXCRTVAC.PPCUCD);         
       AATRA.setPYCD().moveLeftPad(PXCRTVAC.PPPYCD);         
       AATRA.setPYME().moveLeftPad(PXCRTVAC.PPPYME);         
       AATRA.setCEID().moveLeftPad(PXCRTVAC.PPCEID);         
       AATRA.setSINO().moveLeftPad(PXCRTVAC.PPSINO);         
       AATRA.setCFC1().moveLeftPad(PXCRTVAC.PPCFC1);         
       AATRA.setCFI1().moveLeftPad(PXCRTVAC.PPCFI1);         
       AATRA.setEMID().moveLeftPad(PXCRTVAC.PPEMID);         
       AATRA.setPROJ().moveLeftPad(PXCRTVAC.PPPROJ);         
       AATRA.setELNO().moveLeftPad(PXCRTVAC.PPELNO);         
       AATRA.setCHRI().moveLeftPad(PXCRTVAC.PPCHRI);         
       AATRA.setCCOM().moveLeftPad(PXCRTVAC.PPCCOM);         
       AATRA.setEVEN().moveLeftPad(PXCRTVAC.PPEVEN);         
       AATRA.setACTY().moveLeftPad(PXCRTVAC.PPACTY);      
       AATRA.setRORN().moveLeftPad(PXCRTVAC.PPRORN);         
       AATRA.setPYTP().moveLeftPad(PXCRTVAC.PPPYTP);         
       AATRA.setADIV().moveLeftPad(PXCRTVAC.PPADIV);         
       AATRA.setPRNO().moveLeftPad(FASMA.getITNO());         
   }

   /**
    *    returns the number of periods of the fiscal year of the year passed
    */   
    public int getNumberOfPeriods(int inYear) {
       SYPER.setYEA4(inYear);
       //   Check if greater than CRS910
       SYPER.SETGT("00", SYPER.getKey("00", 4)); 
       if (SYPER.REDPE("00", SYPER.getKey("00", 3))) {          
          return SYPER.getPERI();
       } else {
          return 1;
       }
    }
    
    /**
     *   getStartPeriod - retrieve period based on FFASDM DBPC
     */
    public int getStartPeriod() {
       FASMA.setASID().move(FASDM.getASID());
       FASMA.setSBNO(FASDM.getSBNO());
       FASMA.setDIVI().move(FASDM.getDIVI());
       FASMA.CHAIN("00", FASMA.getKey("00"));           
       switch (FASDM.getDPBC()) {         
          case 1:
             return FASMA.getPPER();
          case 2:
             return FASMA.getMPER();            
          case 3:
             return FASMA.getAPER();            
          case 4:
             return FASMA.getBPER();
          default:
             return 0;
       }
    }  
    
    /**
     * 
     * @param dptp    -  Depreciation type to be retrieved.
     * @param DSDPTP  -  depreciation type datastructure
     * @return sDSDPTP data structure.
     */
    public sDSDPTP getDPTPInfo(int dptp, sDSDPTP DSDPTP) {   
       SYTAB.setSTCO().moveLeft("DPTP");   
       SYTAB.setDIVI().move(DSP.WWDIVI);   
       SYTAB.setSTKY().clear();   
       SYTAB.setSTKY().moveLeft(dptp, 2);  
       SYTAB.setLNCD().clear();   
       if (SYTAB.CHAIN("00", SYTAB.getKey("00"))) {    
          DSDPTP.setDSDPTP().moveLeft(SYTAB.getPARM());   
       } else {    
          DSDPTP.setDSDPTP().clear();   
       }  
       return DSDPTP;    
    }

    /**
     * disposeSingleDPTP - checks DPTP to be disposed if BVAT != VATT and WFAM = 1. 
     *                     This will allow CRTSC to print the total acquisition cost of the disposal.
     * @param cono - current company
     * @param divi - current division
     * @param asid - current fixed asset ID
     * @param sbno - current sub number
     * @param currentDPTP - depreciation type to be disposed
     */
    public void disposeSingleDPTP(int cono, MvxString divi, MvxString asid, int sbno, int currentDPTP) {
       FASDM.setCONO(cono);    
       FASDM.setDIVI().moveLeftPad(divi);  
       FASDM.setASID().moveLeftPad(asid);  
       FASDM.setSBNO(sbno);    
       FASDM.SETLL("00", FASDM.getKey("00", 4));    
       FASDM.setSelection("00", "FDDPTP", "EQ", String.valueOf(currentDPTP)); 
       FASDM.setSelection("00", "FDFAST", "NE", "9");  
       while (FASDM.READE("00", FASDM.getKey("00", 4))) {
          DSDPTP.setDSDPTP().clear();   
          DSDPTP = getDPTPInfo(FASDM.getDPTP(), DSDPTP);  
          if (FASDM.getBVAT() != FAS900DS.getFAVATT() && DSDPTP.getFAWFAM() == 1) {  
             willReverseAcqValue = true;
             disposeAcqValue = true;
          }
       }
    }
    

    /**
     *    get the last TRNON in FCR040 and assign it to lastTRNO varaible
     */
    public void getLastTRNO() {
       //   get the current TRNO
       lastTRNO = CR040.getTRNO();
       CR040.setJBNO(FAS145DS.getZVJBNO());
       CR040.setJBDT(FAS145DS.getZVJBDT());
       CR040.setJBTM(FAS145DS.getZVJBTM());
       CR040.setBCHN(0);       
       CR040.setTRNO(9999999);
       CR040.SETGT("00", CR040.getKey("00"));
       if (CR040.REDPE("00", CR040.getKey("00", 5))) {
          lastTRNO = CR040.getTRNO();
       }
    }
    
    
   /**
   *    INIT - Init subroutine
   */
   public void INIT() {
      hasValidWFAM = false;         
      // this is to set the accounting reversal to write in FCR040 even if there's already existing account 
      forceWriteReveralAcquisitionCost = true;
      FOOBAR = 0;
      CR040.setCONO(LDAZD.CONO);
      JBCMD.setCONO(LDAZD.CONO);
      JBCMD.setDIVI().move(LDAZD.DIVI);
      SYPAR.setCONO(LDAZD.CONO);
      SYPAR.setDIVI().move(LDAZD.DIVI);
      SYTAB.setCONO(LDAZD.CONO);
      SYTAB.setDIVI().move(LDAZD.DIVI);
      FASMA.setCONO(LDAZD.CONO);
      FASDM.setCONO(LDAZD.CONO);
      FDTSC.setCONO(LDAZD.CONO);
      FDTSC.setDIVI().move(LDAZD.DIVI);
      FDTED.setCONO(LDAZD.CONO);
      FDTED.setDIVI().move(LDAZD.DIVI);
      FASVL.setCONO(LDAZD.CONO);
      FCHKF.setCONO(LDAZD.CONO);
      FCHKF.setDIVI().move(LDAZD.DIVI);
      this.PXCONO = LDAZD.CONO;
      this.PXDIVI.move(LDAZD.DIVI);
      FAHIS.setCONO(LDAZD.CONO);
      FAHIS.setDIVI().move(LDAZD.DIVI);
      this.PXPGNM.move(this.DSPGM);
      this.PXAUPF.moveRight(LDAZD.AUPF);
      this.PXDFMI.move("TIME");
      this.PXDATI = 0;
      this.PXDFMO.move("YMD8");
      this.PXOPRM = 1;
      COMDAT();
      this.CUDATE = this.PXDATO;
      //   Check authority
      PXAUTCHK.CAUTCHK();
      this.PXO.move(this.PXALOP);
      //   Not allowed to run program
      if (this.PXALPG == 0) {
         //   MSGID=XAU0002 You are not authorized to run the program &1
         SRCOMRCM.MSGLVL.moveLeft("*PRV");
         COMPMQ("XAU0002", formatToString(this.DSPGM));
         SETLR();
         return;
      }
      //   Field authority
      moveToIN(1, LDAZD.AUFI);
      //   Clear display
      //   Check start values
      IN91 = !SYSTR.CHAIN("00", KSYSTR());
      IN92 = SYSTR.getErr("00");
      if (IN91) {
         SYSTR.clear();
         FAS145DS.setFAS145DS().clear();
         SYSTR.setPAR1().moveLeft(XXPAR1);
         SYSTR.setCONO(LDAZD.CONO);
         SYSTR.setDIVI().move(LDAZD.DIVI);
         SYSTR.setPGNM().move(this.DSPGM);
         SYSTR.setRESP().move(LDAZD.RESP);
         SYSTR.setRGDT(this.CUDATE);
         SYSTR.setLMDT(SYSTR.getRGDT());
         SYSTR.WRITE("00");
      }
      XXPAR1.moveLeft(SYSTR.getPAR1());
      FAS145DS.setFAS145DS().moveLeft(SYSTR.getPAR2());
      IN52 = toBoolean(CSIN52.getChar());
      IN53 = toBoolean(CSIN53.getChar());
      IN54 = toBoolean(CSIN54.getChar());
      // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
      //   Test if MUF installation and central division
      if (LDAZD.CMTP == 2 && LDAZD.DIVI.isBlank()) {
         IN88 = true;
      } else {
         DSP.WWDIVI.move(LDAZD.DIVI);
      }
      //   Check start picture
      if (!LDAZZ.FPNM.isBlank() && !LDAZZ.PICC.isBlank()) {
         DSP.WWASID.moveLeft(LDAZZ.TDTA);
         DSP.WWSBNO = LDAZZ.TDA1.getIntLeft(3);
         picSet("AI");
      } else {
         picSetMethod('I');
      }
      SYSTR.setRGDT(0);      
      //   - Retrieve parameter for general ledger
      SYPAR.setCONO(LDAZD.CONO);
      SYPAR.setSTCO().moveLeft("CRS750");
      IN91 = !SYPAR.CHAIN("00", SYPAR.getKey("00"));
      IN92 = SYPAR.getErr("00");
      //   If not found and MUF, try division blank (central)
      if (IN91 && LDAZD.CMTP == 2 && !SYPAR.getDIVI().isBlank()) {
         XSDIVI.move(SYPAR.getDIVI());
         SYPAR.setDIVI().clear();
         IN91 = !SYPAR.CHAIN("00", SYPAR.getKey("00"));
         IN92 = SYPAR.getErr("00");
         SYPAR.setDIVI().move(XSDIVI);
      }
      if (!IN91) {
         CRS750DS.setCRS750DS().moveLeft(SYPAR.getPARM());
      }
   }

   /**
   *    PERROR - Error handling if no valid picture
   */
   public void PERROR() {
      picSet("AI");
   }

   String getApName() {
      return "FAS145AP";
   }

   // Movex MDB definitions
   public mvx.db.dta.CJBCMD JBCMD;
   public mvx.db.dta.FFASVL FASVL;
   public mvx.db.dta.FFASMA FASMA;
   public mvx.db.dta.FFASDM FASDM;
   public mvx.db.dta.CSYPAR SYPAR;
   public mvx.db.dta.CMNDIV MNDIV;
   public mvx.db.dta.FCR040 CR040;
   public mvx.db.dta.FFCHKF FCHKF;
   public mvx.db.dta.CSYTAB SYTAB;
   public mvx.db.dta.FFAHIS FAHIS;
   public mvx.db.dta.CSYSTP SYSTP;
   public mvx.db.dta.FFDTSC FDTSC;
   public mvx.db.dta.FFDTED FDTED;
   public mvx.db.dta.CSYPER SYPER; 
   public mvx.db.dta.CAATRA AATRA;     
   public mvx.db.dta.CAMRUL AMRUL;     
   public mvx.db.dta.CAACTL AACTL;
   // Movex MDB definitions end

   public void initMDB() {
      FDTED = (mvx.db.dta.FFDTED)getMDB("FFDTED", FDTED);
      FDTED.setAccessProfile("00", 'R');
      FDTSC = (mvx.db.dta.FFDTSC)getMDB("FFDTSC", FDTSC);
      FDTSC.setAccessProfile("00", 'R');
      SYSTP = (mvx.db.dta.CSYSTP)getMDB("CSYSTP", SYSTP);
      SYSTP.setAccessProfile("00", 'U');
      SYSTR.setAccessProfile("00", 'U');
      FAHIS = (mvx.db.dta.FFAHIS)getMDB("FFAHIS", FAHIS);
      FAHIS.setAccessProfile("10", 'U');
      FAHIS.setAccessProfile("00", 'U');
      SYTAB = (mvx.db.dta.CSYTAB)getMDB("CSYTAB", SYTAB);
      SYTAB.setAccessProfile("00", 'R');
      FCHKF = (mvx.db.dta.FFCHKF)getMDB("FFCHKF", FCHKF);
      FCHKF.setAccessProfile("00", 'U');
      CR040 = (mvx.db.dta.FCR040)getMDB("FCR040", CR040);
      CR040.setAccessProfile("10", 'U');
      MNDIV = (mvx.db.dta.CMNDIV)getMDB("CMNDIV", MNDIV);
      MNDIV.setAccessProfile("00", 'R');
      SYPAR = (mvx.db.dta.CSYPAR)getMDB("CSYPAR", SYPAR);
      SYPAR.setAccessProfile("00", 'R');
      FASDM = (mvx.db.dta.FFASDM)getMDB("FFASDM", FASDM);
      FASDM.setAccessProfile("00", 'R');
      FASMA = (mvx.db.dta.FFASMA)getMDB("FFASMA", FASMA);
      FASMA.setAccessProfile("00", 'U');
      FASVL = (mvx.db.dta.FFASVL)getMDB("FFASVL", FASVL);
      FASVL.setAccessProfile("00", 'U');
      JBCMD = (mvx.db.dta.CJBCMD)getMDB("CJBCMD", JBCMD);
      JBCMD.setAccessProfile("00", 'U');
      SYCAL.setAccessProfile("00", 'R');
      SYPER = (mvx.db.dta.CSYPER)getMDB("CSYPER", SYPER);   
      SYPER.setAccessProfile("00", 'R');  
      AATRA = (mvx.db.dta.CAATRA)getMDB("CAATRA", AATRA);      
      AATRA.setAccessProfile("00", 'R');     
      AMRUL = (mvx.db.dta.CAMRUL)getMDB("CAMRUL", AMRUL);      
      AMRUL.setAccessProfile("10", 'R');     
      AACTL = (mvx.db.dta.CAACTL)getMDB("CAACTL", AACTL);      
      AACTL.setAccessProfile("00", 'U'); 
   }

   public void initDSP() {
      if (DSP == null) {
         DSP = new FAS145DSP(this);
      }
   }

   public sGLS040DS GLS040DS = new sGLS040DS(this);
   public MvxString XXSTCO = new MvxString(10);//*LIKE STCO
   public double FHDEPC;//*LIKE FAVC
   public double FHDEP2;//*LIKE FAVA
   public double XDADTY;//*LIKE FAVA
   public cPLCHKAD PLCHKAD = new cPLCHKAD(this);
   public double S9FAVC;//*LIKE FAVC
   public double S9FAVA;//*LIKE FAVA
   public double XDFAQT;//*LIKE XXN299
   //*STRUCDEF rWDSMSG{
   public MvxStruct rWDSMSG = new MvxStruct(18);
   public MvxString WDSMSG = rWDSMSG.newString(0, 18);
   public MvxString WDFRDT = rWDSMSG.newString(0, 6);
   public MvxString WDTODT = rWDSMSG.newString(6, 6);
   public MvxString WDACDT = rWDSMSG.newString(12, 6);
   public cPXCRTVAC PXCRTVAC = new cPXCRTVAC(this);
   //*STRUCDEF rZABJNO{
   public MvxStruct rZABJNO = new MvxStruct(18);
   public MvxString ZABJNO = rZABJNO.newString(0, 18);
   public MvxString ZAJNU = rZABJNO.newInt(0, 6);
   public MvxString ZAPRD = rZABJNO.newInt(6, 6);
   public MvxString ZAPRT = rZABJNO.newInt(12, 6);
   //*STRUCDEF rZWYPTS{
   public MvxStruct rZWYPTS = new MvxStruct(6);
   public MvxString ZWYPTS = rZWYPTS.newInt(0, 6);
   public MvxString ZWYPT4 = rZWYPTS.newInt(0, 4);
   public double AVCSCR;//*LIKE FAVC
   public int FOOBAR;//*LIKE XXN10
   public int XXTRNO;//*LIKE TRNO
   public MvxRecord pFAS011 = new MvxRecord();
   public sFAS010DS FAS010DS = new sFAS010DS(this);
   public int lastTRNO;

   public void rFNDTTMSyncTo() {
      FNRGTM.move(FDTSC.getRGTM());
      FNRGDT.move(FDTSC.getRGDT());
   }

   public void rFNDTTMSyncFrom() {
      FDTSC.setRGTM(FNRGTM.getInt());
      FDTSC.setRGDT(FNRGDT.getInt());
   }

   //*STRUCDEF rFNDTTM{
   public MvxStruct rFNDTTM = new MvxStruct(14);
   public MvxString FNDTTM = rFNDTTM.newString(0, 14);
   public MvxString FNRGDT = rFNDTTM.newInt(0, 8);
   public MvxString FNRGTM = rFNDTTM.newInt(8, 6);
   public sDSFFNC DSFFNC = new sDSFFNC(this);
   public sFAS145DS FAS145DS = new sFAS145DS(this);
   public sFAS100DS FAS100DS = new sFAS100DS(this);
   public sDSFEID DSFEID = new sDSFEID(this);
   public double XXDEPR;//*LIKE FAVA
   public double XXDEPC;//*LIKE FAVC
   public double WYPVAL;//*LIKE FAVA
   public double YXPVAL;//*LIKE FAVA
   public double YYPVAL;//*LIKE FAVA
   public double XDACDP;//*LIKE FAVA
   public double XDVTDP;//*LIKE FAVA
   public double SVTOTC;//*LIKE FAVA
   public double SVTOTA;//*LIKE FAVA
   public MvxString XSDIVI = new MvxString(3);//*LIKE DIVI
   public double SVFAYC;//*LIKE FAVA
   public double SVFAYA;//*LIKE FAVA
   public double SVFAXC;//*LIKE FAVA
   public double SVFAXA;//*LIKE FAVA
   public double SVFAVC;//*LIKE FAVC
   public double SVFAVA;//*LIKE FAVA
   public double XXSCVC;//*LIKE FAVC
   public double XXSCVA;//*LIKE FAVA
   public double SVFAPC;//*LIKE FAVA
   public double SVFAPA;//*LIKE FAVA
   public double XXFAVA;
   public int XXMUNI;//*LIKE MUST
   public double[] PXNU = new double[9];//*LIKE XXN299
   public double AVASCR;//*LIKE FAVA
   public MvxString[] PXKY = newMvxStrings(15, 30);//*LIKE XXA30
   public MvxString[] PXJT = newMvxStrings(9, 15);//*LIKE XXA15
   public MvxString[] PXJO = newMvxStrings(9, 3);//*LIKE XXA3
   public MvxString[] PXJF = newMvxStrings(9, 15);//*LIKE XXA15
   //*STRUCDEF rXXPAR1{
   public MvxStruct rXXPAR1 = new MvxStruct(100);
   public MvxString XXPAR1 = rXXPAR1.newString(0, 100);
   public MvxString CSIN52 = rXXPAR1.newChar(11);
   public MvxString CSIN53 = rXXPAR1.newChar(12);
   public MvxString CSIN54 = rXXPAR1.newChar(13);
   public MvxString[] PXFI = newMvxStrings(9, 10);//*LIKE XXA10
   public double SVFACC;//*LIKE FAVA
   public double SVFACA;//*LIKE FAVA
   //*STRUCDEF rMSGDTA{
   public MvxString MSGDTN = rMSGDTA.newLong(0, 15);
   public MvxString MSGDTF = rMSGDTA.newString(8, 4);
   public MvxString MSGDTD = rMSGDTA.newString(12, 3);
   //*STRUCDEF rFESTKY{
   public MvxStruct rFESTKY = new MvxStruct(10);
   public MvxString FESTKY = rFESTKY.newString(0, 10);
   public MvxString FEFEID = rFESTKY.newString(0, 4);
   public MvxString FEFNCN = rFESTKY.newInt(4, 3);
   public double SVFAAC;//*LIKE FAVA
   public double SVFAAA;//*LIKE FAVA
   public double XYSCVC;//*LIKE FAVC
   public double XYSCVA;//*LIKE FAVA
   public MvxString[] PXAL = newMvxStrings(9, 50);//*LIKE XXA50
   public MvxString Z0PICC = new MvxString(2);//*LIKE XXA2
   public sCRS750DS CRS750DS = new sCRS750DS(this);
   //*STRUCDEF rWKDPTP{
   public MvxStruct rWKDPTP = new MvxStruct(2);
   public MvxString WKDPTP = rWKDPTP.newInt(0, 2);
   public MvxString WKDPT1 = rWKDPTP.newInt(0, 1);
   public cPXGETVAL PXGETVAL = new cPXGETVAL(this);
   
   public int transNoCAATRA;     
   public int X1CONO;      
   public MvxString X1DIVI = new MvxString(3);     
   public MvxString X1STCO = new MvxString(10);       
   public sCAS900DS CAS900DS = new sCAS900DS(this);   
   public cPLCLCCU PLCLCCU = new cPLCLCCU(this);
   public MvxString XZBJNO = new MvxString(18); 
   //*KEY KSYSTR{

   public MvxRecord KSYSTR() {
      keyKSYSTR.reset();
      keyKSYSTR.set(LDAZD.CONO, 3);//*LIKE CONO
      keyKSYSTR.set(LDAZD.DIVI);//*LIKE DIVI
      keyKSYSTR.set(this.DSPGM);//*LIKE PGM
      keyKSYSTR.set(LDAZD.RESP);//*LIKE RESP
      return keyKSYSTR;
   }

   public MvxRecord keyKSYSTR = new MvxRecord(48);
   //*KEY KSYSTP{

   public MvxRecord KSYSTP() {
      keyKSYSTP.reset();
      keyKSYSTP.set(SYPAR.getCONO(), 3);//*LIKE CONO
      keyKSYSTP.set(SYPAR.getDIVI());//*LIKE DIVI
      keyKSYSTP.set(SYSTP.getPGNM());//*LIKE PGNM
      return keyKSYSTP;
   }

   public MvxRecord keyKSYSTP = new MvxRecord(28);
   //*STRUCDEF rXXSMSG{
   public MvxStruct rXXSMSG = new MvxStruct(15);
   public MvxString XXSMSG = rXXSMSG.newString(0, 15);
   public MvxString XMASID = rXXSMSG.newString(0, 10);
   public MvxString XMSBNO = rXXSMSG.newString(10, 3);
   //*STRUCDEF rXCKFL1{
   public MvxStruct rXCKFL1 = new MvxStruct(30);
   public MvxString XCKFL1 = rXCKFL1.newString(0, 30);
   public MvxString XCASID = rXCKFL1.newString(0, 10);
   public MvxString XCSBNO = rXCKFL1.newInt(10, 3);
   public MvxString XCDIVI = rXCKFL1.newString(13, 3);
   public double PXLEN;//*LIKE XXN155
   public double YYFAQT;//*LIKE XXN299
   //*STRUCDEF rYYTDTA{
   public MvxStruct rYYTDTA = new MvxStruct(30);
   public MvxString YYTDTA = rYYTDTA.newString(0, 30);
   public MvxString YYDPTP = rYYTDTA.newInt(0, 2);
   public MvxString YYRGTM = rYYTDTA.newInt(2, 8);

   public void PXFAS101SyncTo() {
      PXFAS101.PXBJNO.move(PXMNS230.PXBJNO);
   }

   public void PXFAS101SyncFrom() {
      PXMNS230.PXBJNO.move(PXFAS101.PXBJNO);
   }

   public cPXFAS101 PXFAS101 = new cPXFAS101(this);
   public char XXPRDE = ' ';//*LIKE XXA1
   public sDSMUST DSMUST = new sDSMUST(this);
   //*PARAM pFAS102CL{
   public MvxRecord pFAS102CL = new MvxRecord();// len = 18

   public void pFAS102CLpreCall() {// insert param into record for call
      pFAS102CL.reset();
      pFAS102CL.set(JBCMD.getBJNO());
   }

   public void pFAS102CLpostCall() {// extract param from record after call

      pFAS102CL.reset();
      pFAS102CL.getString(JBCMD.setBJNO());
   }

   public MvxStruct rZZYEAR = new MvxStruct(6);    
   public MvxString ZZYEAR = rZZYEAR.newInt(0, 6);    
   public MvxString ZZYEA4 = rZZYEAR.newInt(0, 4);    
   public MvxString ZZPERI = rZZYEAR.newInt(4, 2);   
   
   public double XXFAQT;//*LIKE FAQT
   public MvxString XFPGNM = new MvxString(10);//*LIKE PGNM
   public sDSMUCU DSMUCU = new sDSMUCU(this);
   public MvxString XCLOCD = new MvxString(3);//*LIKE LOCD
   //*STRUCDEF rXCDATA{
   public MvxStruct rXCDATA = new MvxStruct(256);
   public MvxString XCDATA = rXCDATA.newString(0, 256);
   public MvxString XCPVAL = rXCDATA.newDouble(0, 15, 2);
   public MvxString XCACDP = rXCDATA.newDouble(15, 15, 2);
   public MvxString XCEODP = rXCDATA.newDouble(30, 15, 2);
   public MvxString XCADTY = rXCDATA.newDouble(45, 15, 2);
   public MvxString XCADEP = rXCDATA.newDouble(60, 15, 2);
   public MvxString XCRVAL = rXCDATA.newDouble(75, 15, 2);
   public MvxString XCDPTP = rXCDATA.newInt(90, 2);
   public MvxString XCVATP = rXCDATA.newInt(92, 2);
   public int XXDPTP;//*LIKE DPTP
   public int XXACYP;//*LIKE CYP1
   public double YYPART;//*LIKE XXN98
   public int decimalCode;
   public MvxString lastUsedCUCD = new MvxString(3);
   public Vector VectorOfCUCD;
   public Vector VectorOfDCCD;
   public sDSCUCD DSCUCD = new sDSCUCD(this);
   //*PARAM pFAS146CL{
   public MvxRecord pFAS146CL = new MvxRecord();// len = 18

   public void pFAS146CLpreCall() {// insert param into record for call
      pFAS146CL.reset();
      pFAS146CL.set(JBCMD.getBJNO());
   }

   public void pFAS146CLpostCall() {// extract param from record after call

      pFAS146CL.reset();
      pFAS146CL.getString(JBCMD.setBJNO());
   }

   public int XXACDT;//*LIKE DATO
   public double XXACDP;//*LIKE FAVA
   public int XLDPTP;//*LIKE DPTP
   public double XXACDC;//*LIKE FAVC
   public int forDisposeDPTP;

   public void CSYSTPSyncTo() {
      SYSTP.setLMDT(SYPAR.getLMDT());
      SYSTP.setDIVI().move(SYPAR.getDIVI());
      SYSTP.setRGTM(SYPAR.getRGTM());
      SYSTP.setRGDT(SYPAR.getRGDT());
      SYSTP.setCHNO(SYPAR.getCHNO());
      SYSTP.setCHID().move(SYPAR.getCHID());
      SYSTP.setCONO(SYPAR.getCONO());
   }

   public void CSYSTPSyncFrom() {
      SYPAR.setLMDT(SYSTP.getLMDT());
      SYPAR.setDIVI().move(SYSTP.getDIVI());
      SYPAR.setRGTM(SYSTP.getRGTM());
      SYPAR.setRGDT(SYSTP.getRGDT());
      SYPAR.setCHNO(SYSTP.getCHNO());
      SYPAR.setCHID().move(SYSTP.getCHID());
      SYPAR.setCONO(SYSTP.getCONO());
   }

   public MvxString XMDIVI = new MvxString(3);//*LIKE DIVI
   public double XXPART;//*LIKE XXN98
   public cPXCHKPRL PXCHKPRL = new cPXCHKPRL(this);
   public sFAS900DS FAS900DS = new sFAS900DS(this);
   public double XXRVAL;//*LIKE FAVA
   public double XXRVAC;//*LIKE FAVC
   public MvxString SVCUCD = new MvxString(3);//*LIKE CUCD
   public boolean forceWriteReveralAcquisitionCost;
   public boolean option11;
   //*PARAM rQCMD{
   public MvxRecord rQCMD = new MvxRecord();// len = 2015

   public void rQCMDpreCall() {// insert param into record for call
      rQCMD.reset();
      rQCMD.set(PXMNS210.DSQCMD);
      rQCMD.set(2000d, 15, 5);
   }

   public void rQCMDpostCall() {// extract param from record after call
      rQCMD.reset();
      rQCMD.getString(PXMNS210.DSQCMD);
      PXLEN = rQCMD.getDouble(15, 5);
   }

   public int XXN70;//*LIKE XXN70

   public void PXMNS230SyncTo() {
      PXMNS230.PXUSID.move(PXCHKPRL.PXUSID);
      PXMNS230.DSQCMD.move(PXMNS210.DSQCMD);
      PXMNS230.PXDEVD.move(PXCHKPRL.PXDEVD);
      PXMNS230.PXMSID.move(PXMNS210.PXMSID);
   }

   public void PXMNS230SyncFrom() {
      PXCHKPRL.PXUSID.move(PXMNS230.PXUSID);
      PXMNS210.DSQCMD.move(PXMNS230.DSQCMD);
      PXCHKPRL.PXDEVD.move(PXMNS230.PXDEVD);
      PXMNS210.PXMSID.move(PXMNS230.PXMSID);
   }

   public cPXMNS230 PXMNS230 = new cPXMNS230(this);

   public void PXMNS215SyncTo() {
      PXMNS215.PXUSID.move(PXCHKPRL.PXUSID);
      PXMNS215.PXDEVD.move(PXCHKPRL.PXDEVD);
      PXMNS215.PXPRTF.move(PXCHKPRL.PXPRTF);
   }

   public void PXMNS215SyncFrom() {
      PXCHKPRL.PXUSID.move(PXMNS215.PXUSID);
      PXCHKPRL.PXDEVD.move(PXMNS215.PXDEVD);
      PXCHKPRL.PXPRTF.move(PXMNS215.PXPRTF);
   }

   public cPXMNS215 PXMNS215 = new cPXMNS215(this);

   public void PXMNS210SyncTo() {
      PXMNS210.PXUSID.move(PXCHKPRL.PXUSID);
      PXMNS210.CMBJNO.move(JBCMD.getBJNO());
      PXMNS210.PXDEVD.move(PXCHKPRL.PXDEVD);
      PXMNS210.PXPRTF.move(PXCHKPRL.PXPRTF);
   }

   public void PXMNS210SyncFrom() {
      PXCHKPRL.PXUSID.move(PXMNS210.PXUSID);
      JBCMD.setBJNO().move(PXMNS210.CMBJNO);
      PXCHKPRL.PXDEVD.move(PXMNS210.PXDEVD);
      PXCHKPRL.PXPRTF.move(PXMNS210.PXPRTF);
   }

   public cPXMNS210 PXMNS210 = new cPXMNS210(this);
   public double WKACAM;//*LIKE FAVA
   public sDSDPTP DSDPTP = new sDSDPTP(this);

   public void PLCHKVOSyncTo() {
      PLCHKVO.FVDIVI.move(FASVL.getDIVI());
      PLCHKVO.FVCONO = FASVL.getCONO();
   }

   public void PLCHKVOSyncFrom() {
      FASVL.setDIVI().move(PLCHKVO.FVDIVI);
      FASVL.setCONO(PLCHKVO.FVCONO);
   }

   public cPLCHKVO PLCHKVO = new cPLCHKVO(this);
   //*PARAM pFAS103{
   public MvxRecord pFAS103 = new MvxRecord();// len = 19

   public void pFAS103preCall() {// insert param into record for call
      pFAS103.reset();
      pFAS103.set(JBCMD.getBJNO());
      pFAS103.set(GLS040DS.getZWUPCD(), 1);
   }

   public void pFAS103postCall() {// extract param from record after call

      pFAS103.reset();
      pFAS103.getString(JBCMD.setBJNO());
      GLS040DS.setZWUPCD(pFAS103.getInt(1));
   }

   //*PARAM pFAS102{
   public MvxRecord pFAS102 = new MvxRecord();// len = 19

   public void pFAS102preCall() {// insert param into record for call
      pFAS102.reset();
      pFAS102.set(JBCMD.getBJNO());
      pFAS102.set(GLS040DS.getZWUPCD(), 1);
   }

   public void pFAS102postCall() {// extract param from record after call

      pFAS102.reset();
      pFAS102.getString(JBCMD.setBJNO());
      GLS040DS.setZWUPCD(pFAS102.getInt(1));
   }

   public MvxString XXSTKY = new MvxString(10);//*LIKE STKY
   public double XXPVAL;//*LIKE FAVA
   public double XXPVAC;//*LIKE FAVC
   public MvxString XVCUCD = new MvxString(3);//*LIKE CUCD
   public boolean willReverseAcqValue; 
   public boolean reverseAcqValue;
   public boolean disposeAcqValue;
   public boolean disposeErrorShown;
   public boolean disposeErrorShownPBCHK;
   public boolean continueDispose;
   public int notValidDisposableDPTP;  
   public int totalDPTP;  
   public int[] allowedDPTP = new int[20];
   public FAS145DSP DSP;
   public boolean hasValidWFAM;
   public boolean vonoRetrieved;
   public boolean retValidation;

   public GenericDSP getDSP() {
      return (GenericDSP)DSP;
   }

   public String getVarList(java.util.Vector v) {
      super.getVarList(v);
      v.addElement(GLS040DS);
      v.addElement(XXSTCO);
      v.addElement(PLCHKAD);
      v.addElement(SYCAL);
      v.addElement(rWDSMSG);
      v.addElement(PXCRTVAC);
      v.addElement(rZABJNO);
      v.addElement(JBCMD);
      v.addElement(rZWYPTS);
      v.addElement(FASVL);
      v.addElement(rFNDTTM);
      v.addElement(DSFFNC);
      v.addElement(FAS145DS);
      v.addElement(FASMA);
      v.addElement(FAS100DS);
      v.addElement(FASDM);
      v.addElement(XSDIVI);
      v.addElement(SYPAR);
      v.addElement(MNDIV);
      v.addElement(PXNU);
      v.addElement(PXKY);
      v.addElement(PXJT);
      v.addElement(PXJO);
      v.addElement(PXJF);
      v.addElement(rXXPAR1);
      v.addElement(PXFI);
      v.addElement(rFESTKY);
      v.addElement(PXAL);
      v.addElement(Z0PICC);
      v.addElement(CRS750DS);
      v.addElement(rWKDPTP);
      v.addElement(PXGETVAL);
      v.addElement(rXXSMSG);
      v.addElement(rXCKFL1);
      v.addElement(rYYTDTA);
      v.addElement(PXFAS101);
      v.addElement(CR040);
      v.addElement(DSMUST);
      v.addElement(FCHKF);
      v.addElement(XFPGNM);
      v.addElement(DSMUCU);
      v.addElement(XCLOCD);
      v.addElement(rXCDATA);
      v.addElement(SYTAB);
      v.addElement(FAHIS);
      v.addElement(SYSTR);
      v.addElement(SYSTP);
      v.addElement(XMDIVI);
      v.addElement(PXCHKPRL);
      v.addElement(FDTSC);
      v.addElement(FAS900DS);
      v.addElement(SVCUCD);
      v.addElement(FDTED);
      v.addElement(PXMNS230);
      v.addElement(PXMNS215);
      v.addElement(PXMNS210);
      v.addElement(DSDPTP);
      v.addElement(PLCHKVO);
      v.addElement(XXSTKY);
      v.addElement(XVCUCD);
      v.addElement(DSP);
      v.addElement(lastUsedCUCD);
      v.addElement(SYPER);    
      v.addElement(FAS010DS);
      v.addElement(DSFEID);
      v.addElement(VectorOfCUCD);
      v.addElement(VectorOfDCCD);
      v.addElement(DSCUCD);
      v.addElement(X1DIVI);      
      v.addElement(X1STCO);      
      v.addElement(CAS900DS);    
      v.addElement(PLCLCCU); 
      v.addElement(AATRA);       
      v.addElement(AMRUL);       
      v.addElement(AACTL); 
      v.addElement(XZBJNO);
      v.addElement(allowedDPTP); 
      v.addElement(rZZYEAR);   
      return version;
   }

   public void clearInstance() {
      super.clearInstance();
      FHDEPC = 0D;
      FHDEP2 = 0D;
      XDADTY = 0D;
      S9FAVC = 0D;
      S9FAVA = 0D;
      XDFAQT = 0D;
      AVCSCR = 0D;
      FOOBAR = 0;
      XXTRNO = 0;
      XXDEPR = 0D;
      XXDEPC = 0D;
      WYPVAL = 0D;
      YXPVAL = 0D;
      YYPVAL = 0D;
      XDACDP = 0D;
      XDVTDP = 0D;
      SVTOTC = 0D;
      SVTOTA = 0D;
      SVFAYC = 0D;
      SVFAYA = 0D;
      SVFAXC = 0D;
      SVFAXA = 0D;
      SVFAVC = 0D;
      SVFAVA = 0D;
      XXSCVC = 0D;
      XXSCVA = 0D;
      SVFAPC = 0D;
      SVFAPA = 0D;
      XXMUNI = 0;
      AVASCR = 0D;
      SVFACC = 0D;
      SVFACA = 0D;
      SVFAAC = 0D;
      SVFAAA = 0D;
      XYSCVC = 0D;
      XYSCVA = 0D;
      PXLEN = 0D;
      YYFAQT = 0D;
      XXPRDE = ' ';
      XXFAQT = 0D;
      XXDPTP = 0;
      XXACYP = 0;
      YYPART = 0D;
      XXACDT = 0;
      XXACDP = 0D;
      XLDPTP = 0;
      XXACDC = 0D;
      XXPART = 0D;
      XXRVAL = 0D;
      XXRVAC = 0D;
      XXN70 = 0;
      WKACAM = 0D;
      XXPVAL = 0D;
      XXPVAC = 0D;
      XXFAVA = 0D;
      decimalCode = 0;
      forDisposeDPTP = 0;
      hasValidWFAM = false; 
      X1CONO = 0;  
      transNoCAATRA = 0;
      totalDPTP = 0;
      willReverseAcqValue = false;  
      reverseAcqValue = false;
      disposeErrorShown = false;    
      notValidDisposableDPTP = 0;
      vonoRetrieved = false;
      disposeErrorShownPBCHK = false;
      disposeAcqValue = false;
      continueDispose = false;
      forceWriteReveralAcquisitionCost = false;
      lastTRNO = 0;
      retValidation = false;
      option11 = false;
   }

   public String getVer() {
      return version;
   }

   public final String version = "Pgm.Name: FAS145, " + "Source creation date: Thu Jan 24 14:46:33 CET 2002, " + "ID number: 1011879993708";

   public String getVersion() {
      return _version;
   }

   public String getRelease() {
      return _release;
   }

   public String getSpLevel() {
      return _spLevel;
   }

   public String getSpNumber() {
      return _spNumber;
   }

   public final static String _version = "15";
   public final static String _release = "1";
   public final static String _spLevel = "4";
   public final static String _spNumber = "MAK_JPARIAL_180405_08:33";
   public final static String _GUID = "196CAA43E4D84fcf9D5364F1F8C1F5BD";
   public final static String _tempFixComment = "";
   public final static String _build = "000000000000143";
   public final static String _pgmName = "FAS145";

   public String getGUID() {
      return _GUID;
   }

   public String getTempFixComment() {
      return _tempFixComment;
   }

   public String getVersionInformation() {
      return _version + '.' + _release + '.' + _spLevel + ':' + _spNumber;
   }

   public String getBuild() {
      return (_version + _release + _build + "      " + _pgmName + "                                   ").substring(0, 34);
   }

   public String [][] getStandardModification() {
      return _standardModifications;
   } // end of method [][] getStandardModification()

   public final static String [][] _standardModifications={
      {"JT-942397","160823","15291","When Disposing Part of an Asset no Cost Transaction"},
      {"JT-972965","161104","11972","FAS145 : Disposal of an asset is mixing up values in the fixed asset values (FFAHIS file)"},
      {"JT-996063","161222","15291","Disposal of FA in status 8 not possible in FAS145"},
      {"JT-1012250","170306","jparial","Missing disposal transactions when related option(CTRL + 11)  is used for an asset with 2 DPTP (WFAM=0 & WFAM=1)"},
      {"JT-1038631","170412","jtan1","Creates a dump if FA40-550 and FA40-500 is setup with identical accounting strings"},
      {"JT-1058520","170616","14013","FAS145 : The program dumps with a record lock timeout"},
      {"JT-1072037","170725","jtan1","after split of an asset the original asset calculates incorrect values for further transactions"},
      {"JT-1075261","170818","jtan1","Disposed asset FFAHIS and voucher not created even after correcting the entries GLS037"},
      {"JT-1089356","170922","jparial","Incorrect period posted in FFAHIS for YTD reversal. Dispose period should be used as the period for depreciation reversal."},
      {"JT-1100096","171110","fcofino","Disposal of an asset with multiple Depreciation type (WFAM=0 and WFAM=1) needs twice attemp(14) before getting the error message"},
      {"JT-1117347","171215","JPARIAL","FAS145: Cannot use F14 to dispose an asset if one of the depreciation types connected has no GL update but both basis for depreciation is the same as VATT"},
      {"JT-1155693","180413","JPARIAL","Transaction number is not incremented properly in certain database types"}
   };
}