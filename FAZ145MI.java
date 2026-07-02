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
*   (c) COPYRIGHT 2013 INFOR.  ALL RIGHTS RESERVED.           *
*   THE WORD AND DESIGN MARKS SET FORTH HEREIN ARE            *
*   TRADEMARKS AND/OR REGISTERED TRADEMARKS OF INFOR          *
*   AND/OR ITS AFFILIATES AND SUBSIDIARIES. ALL RIGHTS        *
*   RESERVED.  ALL OTHER TRADEMARKS LISTED HEREIN ARE         *
*   THE PROPERTY OF THEIR RESPECTIVE OWNERS.                  *
*                                                             *
***************************************************************
*/
package mvx.app.pgm.customer;

import java.util.Vector;

import mvx.app.common.Batch;
import mvx.app.common.MIBatch;
import mvx.runtime.*;
import mvx.db.dta.*;
import mvx.app.util.*;
import mvx.app.plist.*;
import mvx.app.ds.*;
import mvx.util.*;
import mvx.db.common.Expression;
import mvx.db.common.FieldSelection;
import mvx.dsp.obj.FAS145DSP;

/*
*Modification area - M3
*Nbr            Date   User id     Description
*99999999999999 999999 XXXXXXXXXX  x
*Modification area - Business partner
*Nbr            Date   User id     Description
*99999999999999 999999 XXXXXXXXXX  x
*Modification area - Customer
*Nbr            Date   User id     Description*      USMOD-08 260701 XKEXTPAM01  Custom MI for Fixed Asset Disposal - FAZ145MI
*/

/**
*<BR><B><FONT SIZE=+2>Api: ESM&R: Usage based depreciation interface</FONT></B><BR><BR>
*
* This class ...<BR><BR>
*
*/
public class FAZ145MI extends MIBatch
{
	public void movexMain() {

		INIT();
		MICommon.initiate();
		MICommon.accept();
		while (MICommon.read()) {
			if (MICommon.isTransaction(GET_USER_INFO)) {
				MICommon.setTransaction(retrieveUserInfo.getMessage());
			} else if (MICommon.isTransaction("GetAsset")) {
				getAsset();
			} else if (MICommon.isTransaction("Dispose")) {
				Dispose();
			} else {
				MICommon.setTransactionError();
			}
			MICommon.write();
		}
		MICommon.close();
		SETLR();
		return;
	} // end of method
	public void getAsset() {}
   	
	public void Dispose() {
	      sFAZ145MIRDispose inDS = (sFAZ145MIRDispose)MICommon.getInDS(sFAZ145MIRDispose.class);
	     // sFAZ145MISDispose outDS = (sFAZ145MISDispose)MICommon.getOutDS(sFAZ145MISDispose.class);
	      
	         FASMA.setCONO(LDAZD.CONO); FASDM.setCONO(LDAZD.CONO); FASVL.setCONO(LDAZD.CONO);
	         FAHIS.setCONO(LDAZD.CONO); CR040.setCONO(LDAZD.CONO); SYPAR.setCONO(LDAZD.CONO); 
	         SYTAB.setCONO(LDAZD.CONO); FCHKF.setCONO(LDAZD.CONO); JBCMD.setCONO(LDAZD.CONO);
	         SYPER.setCONO(LDAZD.CONO); AATRA.setCONO(LDAZD.CONO); AMRUL.setCONO(LDAZD.CONO);
	         AACTL.setCONO(LDAZD.CONO);
	      
	      
	      if (inDS.getQ1DIVI().isBlank()) {
	         MICommon.setError("DIVI", "WDI0102"); return;
	      } else {
	         WWDIVI.moveLeft(inDS.getQ1DIVI());
	         FASMA.setDIVI().move(WWDIVI); FASDM.setDIVI().move(WWDIVI);
	         FAHIS.setDIVI().move(WWDIVI); SYPAR.setDIVI().move(WWDIVI); 
	         SYTAB.setDIVI().move(WWDIVI); FCHKF.setDIVI().move(WWDIVI);
	         JBCMD.setDIVI().move(WWDIVI); SYPER.setDIVI().move(WWDIVI);
	         AATRA.setDIVI().move(WWDIVI); AMRUL.setDIVI().move(WWDIVI); 
	         AACTL.setDIVI().move(WWDIVI);
	      }
	      
	      if (inDS.getQ1ASID().isBlank()) {
	         MICommon.setError("ASID", "WAS3002"); return;
	      } else {
	         WWASID.moveLeft(inDS.getQ1ASID());
	      }
	      
	      if (MICommon.toNumeric(inDS.getQ1SBNO())) {
	         WWSBNO = MICommon.getInt();
	      } else {
	         MICommon.setError("SBNO"); return;
	      }

	      // Concurrency Lock Check via FFCHKF (Verbatim PACHK logic)[cite: 2]
	      FCHKF.setSTCO().clear();
	      FCHKF.setSTCO().moveLeft("FAS145");
	      FCHKF.setKFL1().clear();
	      XCASID.move(WWASID);
	      XCSBNO.move(WWSBNO);
	      XCDIVI.move(WWDIVI);
	      FCHKF.setKFL1().move(XCKFL1);
	      IN91 = !FCHKF.CHAIN("00", FCHKF.getKey("00", 4));
	      if (!IN91) {
	         MICommon.setError("ASID", "FA14506"); return;
	      }

	      // Verify asset master presence via standardized global check rules indicators[cite: 2]
	      FASMA.setASID().move(WWASID);
	      FASMA.setSBNO(WWSBNO);
	      FASMA.setDIVI().move(WWDIVI);
	      IN91 = !FASMA.CHAIN("00", FASMA.getKey("00"));
	      if (IN91) {
	         MICommon.setError("ASID", "FA14513"); return;
	      } else {
	         WWFADS.move(FASMA.getFADS());
	      }
	      if (!IN91 && FASMA.getFAST() > 4 && FASMA.getFAST() != 8) {
	         MICommon.setError("ASID", "FA14510"); return;
	      }

	   // ---------------------------------------------------------------------
	   // Validate Accounting Date 
	   // ---------------------------------------------------------------------
	   if (!inDS.getQ1ACDT().isBlank()) {
	    
	   if (MICommon.toNumericDate(inDS.getQ1ACDT())) {
	      XXACDT = MICommon.getNumericDate();
	   } else {
	      MICommon.setError("ACDT", "XDT0001");
	      return;
	   }
	   }
	   // Determine Accounting Year/Period (same as FAS145)
	   this.PXDFMI.move("YMD8");
	   this.PXDATI = XXACDT;
	   this.PXDFMO.move("YMD8");
	   this.PXOPRM = 0;
	   this.PXDSEP = ' ';
	   COMDAT();

	   switch (MNDIV.getPTFA()) {
	      case 1:
	         XXACYP = this.PXCYP1;
	         break;
	      case 2:
	         XXACYP = this.PXCYP2;
	         break;
	      case 3:
	         XXACYP = this.PXCYP3;
	         break;
	      case 4:
	         XXACYP = this.PXCYP4;
	         break;
	      case 5:
	         XXACYP = this.PXCYP5;
	         break;
	   }

	   // ---------------------------------------------------------------------
	   // Validate Accounting Date Range (same logic as FAS145)
	   // ---------------------------------------------------------------------
	   XXSTCO.move(SYTAB.getSTCO());
	   XXSTKY.move(SYTAB.getSTKY());

	   SYTAB.setDIVI().move(WWDIVI);
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

	      if (XXACDT < DSFFNC.getDFFRDT() ||
	          XXACDT > DSFFNC.getDFTODT()) {

	         MICommon.setError("ACDT", "XDT0005");
	         return;
	      }

	   } else {

	      SYTAB.setDIVI().move(XSDIVI);

	      if (LDAZD.CMTP == 2) {
	         MICommon.setError("ACDT", "XFE0003");
	      } else {
	         MICommon.setError("ACDT", "XFE0002");
	      }

	      return;
	   }

	   SYTAB.setDIVI().move(XSDIVI);

	      if (!inDS.getQ1FAQT().isBlank()) {
	      this.PXALPH.moveRight(inDS.getQ1FAQT());
	      this.PXNUM = 0d;
	      this.PXDCCD = 0;
	      this.PXFLDD = 9;
	      this.PXDCFM = LDAZD.DCFM;
	      SRCOMNUM.COMNUM();
	      XXFAQT = this.PXNUM;
	      if (SRCOMNUM.PXNMER != 0) {
	         MICommon.setError("FAQT", "XNU000"); return;
	      }
	      }
	      XDFAQT = FASMA.getFAQT();
	      if (XXFAQT > (XDFAQT + EPS_9)) {
	         MICommon.setError("FAQT", "IP29103"); return;
	      }
	      
	      IN63 = !equals(this.PXNUM, XDFAQT, EPS_9);

	     /* if (inDS.getQ1VTXT().isBlank()) {
	         MICommon.setError("VTXT", "WVT0602"); return;
	      } else {
	         WWVTXT.moveLeft(inDS.getQ1VTXT());
	      }*/

	      // Validating limits from FAM configurations (FFNC)[cite: 2]
	      SYTAB.setSTCO().moveLeft("FFNC");
	      FEFEID.move("FA40");
	      FEFNCN.move(1);
	      SYTAB.setSTKY().move(FESTKY);
	      SYTAB.setLNCD().clear();
	      IN91 = !SYTAB.CHAIN("00", SYTAB.getKey("00"));
	      if (!IN91) {
	         DSFFNC.setDSFFNC().moveLeft(SYTAB.getPARM());
	         if (XXACDT < DSFFNC.getDFFRDT() || XXACDT > DSFFNC.getDFTODT()) {
	            MICommon.setError("ACDT", "XDT0005"); return;
	         }
	      } else {
	         MICommon.setError("ACDT", "XFE0002"); return;
	      }

	      // 2. FIXED ASSET VOUCHER CHECK FLOW (Verbatim PECHK Realignment with full Sync Macros)[cite: 2]
	      SYPAR.setCONO(LDAZD.CONO);
	      SYPAR.setDIVI().move(WWDIVI);
	      SYPAR.setSTCO().moveLeft("CRS750");
	      IN91 = !SYPAR.CHAIN("00", SYPAR.getKey("00"));
	      if (!IN91) {
	         CRS750DS.setCRS750DS().moveLeft(SYPAR.getPARM());
	      }

	      PLCHKVO.FVCMTP = LDAZD.CMTP;
	      PLCHKVO.FVACBC = CRS750DS.getPBACBC();
	      PLCHKVO.FVVSER.move(DSFFNC.getDFVSER());
	      PLCHKVO.FVACDT = XXACDT;
	      PLCHKVO.FVYEA4 = XXACYP / 100;
	      PLCHKVO.FVVONI = 0;
	      PLCHKVO.FVVTST = 0;
	      PLCHKVO.FVFETC = 0;

	      PLCHKVOSyncTo(); // Synchronize out to back-end structure parameters[cite: 2]

	      IN92 = PLCHKVO.CCHKVON(); // Execute standard voucher checking component[cite: 2]

	      PLCHKVOSyncFrom(); // Synchronize engine modifications back to API variables[cite: 2]

	      if (PLCHKVO.FVVERR == 1) {
	         MICommon.setError(
	            PLCHKVO.FVMSGI.toString(),
	            formatToString(PLCHKVO.FVMSGD));
	         return;
	      }

	      // Pull value type details parameters from FAS900 setup parameters[cite: 2]
	      SYPAR.setDIVI().move(WWDIVI);
	      SYPAR.setSTCO().moveLeft("FAS900");
	      IN91 = !SYPAR.CHAIN("00", SYPAR.getKey("00"));
	      if (!IN91) {
	         FAS900DS.setFAS900DS().moveLeft(SYPAR.getPARM());
	      }

	      FASVL.setCONO(LDAZD.CONO);
	      FASVL.setDIVI().move(WWDIVI);
	      FASVL.setASID().move(FASMA.getASID());
	      FASVL.setSBNO(FASMA.getSBNO());
	      FASVL.setVATP(FAS900DS.getFAVATT());
	      IN91 = !FASVL.CHAIN("00", FASVL.getKey("00"));
	      if (!IN91) {
	         XXPVAL = FASVL.getFAVA();
	         XXSCVA = FASVL.getFAVA();
	         XXPVAC = FASVL.getFAVC();
	         XXSCVC = FASVL.getFAVC();
	         XVCUCD.move(FASVL.getCUCD());
	      }

	      FASDM.setASID().move(FASMA.getASID());
	      FASDM.setSBNO(FASMA.getSBNO());
	      FASDM.setDIVI().move(FASMA.getDIVI());

	      // ----------------------------------------------------------------
	      // 3. TRANSACTION ENGINE METHOD EXECUTION
	      // ----------------------------------------------------------------
	      TFA100(); 
	      CRTSC();  
	      
	      //outDS.setY1ASID().moveLeft(FASMA.getASID());
	     // outDS.setY1STAT().moveLeft("9");
	     // MICommon.setData(outDS.get()); // Standard return to movexMain loop[cite: 1]
	   }
  
	public void TFA100() {
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
			XXPART = XXFAQT / XDFAQT;
			YYPART = 1d - XXPART;
			DIVASS();
			UFAH63();
		}
		if (IN63) {
			FASDM.setASID().move(WWNWFN);
			FASDM.setSBNO(WWNWSN);
		}
		FDTSC.setDIVI().move(FASDM.getDIVI());
		FASDM.SETLL("00", FASDM.getKey("00", 4));
		FASDM.setSelection("00", "FDFAST", "NE", "9");
		IN93 = !FASDM.READE("00", FASDM.getKey("00", 4));
		IN92 = FASDM.getErr("00");
		while (!IN93) {
			FDTSC.setDPTP(FASDM.getDPTP());
			IN91 = !FDTSC.CHAIN("00", FDTSC.getKey("00", 3));
			if (!IN91) {
				if ((FASDM.getDPMD() != 0 && forDisposeDPTP == 0)
						|| (FASDM.getDPTP() == forDisposeDPTP && forDisposeDPTP > 0)) {
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
					this.PXDIVI.moveLeftPad(MNDIV.getDIVI());
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
						fecthFA10FamEntryId();
						FAS100DS.setZWVTXT().moveLeftPad(WWVTXT);
					} else {
						FAS100DS.setZWACDT(this.CUDATE);
						FAS100DS.setZWVONO(0);
						FAS100DS.setZWVDSC().clear();
						FAS100DS.setZWVTXT().clear();
					}
					FAS100DS.setZWDLSC(0);
					FAS100DS.setZWAJSC(0);
					FAS100DS.setZWLITX().clear();
					FAS100DS.setZWPGNA().move("FAZ145MI");
					FAS100DS.setZWFDIV().move(WWDIVI);
					FAS100DS.setZWWFAM(DSDPTP.getFAWFAM());
					FAS100DS.setZWASID().move(FASDM.getASID());
					FAS100DS.setZWSBNO(FASDM.getSBNO());
					FAS100DS.setZWSPER(XXACDT);
					FAS100DS.setZWRGDT(0);
					FAS100DS.setZWRGTM(FAHIS.getRGTM());
					FAS100DS.setZWPSCD(0);
					FAS100DS.setZWJBNO(this.DSJNU.getInt());
					setGETVAL();
					PXGETVAL.GKCONO = FASDM.getCONO();
					PXGETVAL.GKDIVI.move(FASDM.getDIVI());
					PXGETVAL.GKDPTP = FASDM.getDPTP();
					PXGETVAL.GKASID.move(FASDM.getASID());
					PXGETVAL.GKSBNO = FASDM.getSBNO();
					PXGETVAL.GKTPER = 0;
					PXGETVAL.GKQTTP = 8;
					PXGETVAL.FFAGTVL();
					FHDEP2 = PXGETVAL.GKFAVA;
					FHDEPC = PXGETVAL.GKFAVC;
					ZWYPTS.move(FAS100DS.getZWYPTO());
					FAS100DS.setZDYEA4(ZWYPT4.getInt());
					UFAHIS();
					JBCMD.setBJLI().move("98");
					JBCMD.setBJLT().move("SLT");
					JBCMD.setFILE().clear();
					JBCMD.setQCMD().moveLeftPad(FAS100DS.getFAS100DS());
					JBCMD.setDATA().clear();
					IN91 = !JBCMD.CHAIN_LOCK("00", JBCMD.getKey("00"));

					if (IN91) {
						JBCMD.WRITE("00");
					} else {
						JBCMD.UPDAT("00");
					}
				}
			}
			IN93 = !FASDM.READE("00", FASDM.getKey("00", 4));
			IN92 = FASDM.getErr("00");
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
	      CR040.setJBNO(FAZ145DS.getZVJBNO());
	      CR040.setJBDT(FAZ145DS.getZVJBDT());
	      CR040.setJBTM(FAZ145DS.getZVJBTM());
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
	      FASDM.setCONO(LDAZD.CONO);
	      FASDM.setDIVI().move(WWDIVI);

	      if (IN63) {
	         FASDM.setASID().move(WWNWFN);
	         FASDM.setSBNO(WWNWSN);
	      } else {
	         FASDM.setASID().move(WWASID);
	         FASDM.setSBNO(WWSBNO);
	      }

	      FASDM.SETLL("00", FASDM.getKey("00", 4));

	      IN91 = !FASDM.READE("00", FASDM.getKey("00", 4));
	      IN92 = FASDM.getErr("00");

	      while (!IN91) {

	         // Current subfile row = current FFASDM record
	         WSDPTP = FASDM.getDPTP();

	         // Dispose only selected depreciation type if requested
	         if (forDisposeDPTP > 0 && WSDPTP != forDisposeDPTP) {
	            IN91 = !FASDM.READE("00", FASDM.getKey("00", 4));
	            IN92 = FASDM.getErr("00");
	            continue;
	         }
	            //   Retrieve value types for depreciations
	            FASDM.setCONO(LDAZD.CONO);
	            FASDM.setDIVI().move(WWDIVI);
	            FASDM.setASID().move(WWASID);
	            FASDM.setSBNO(WWSBNO);
	            if (IN63) {
	               FASDM.setASID().move(WWNWFN);
	               FASDM.setSBNO(WWNWSN);
	            }
	            FASDM.setDPTP(WSDPTP);
	            if (forDisposeDPTP == 0) {   
	               if (hasOtherDisposableDPTP(LDAZD.CONO, WWDIVI, WWASID, WWSBNO, FASDM.getDPTP(), true)) {
	                  willReverseAcqValue = false; 
	               } else {   
	                  willReverseAcqValue = true;  
	               } 
	               if (hasOtherDisposableDPTP(LDAZD.CONO, WWDIVI, WWASID, WWSBNO, FASDM.getDPTP(), true) && !willReverseAcqValue) {
	                  willReverseAcqValue = true;
	               }
	            }
	            IN84 = !FASDM.CHAIN("00", FASDM.getKey("00"));
	            IN92 = FASDM.getErr("00");
	            FASVL.setDIVI().move(WWDIVI);
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
	                  FAZ145DS.setZVVONO(PLCHKVO.FVVONO);
	                  FAZ145DS.setZVVDSC().move(DSFFNC.getDFVDSC());
	                  vonoRetrieved = true;
	               }
	               if (!IN91) {
	                  //   Retrieve value types for accumulated depreciations
	                  FASVL.setCONO(FASDM.getCONO());
	                  FASVL.setDIVI().move( WWDIVI);
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
	                     PXCRTVAC.PPDIVI.move( WWDIVI);
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
	                     CR040.setDIVI().move(WWDIVI);
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
	                     CR040.setVONO(FAZ145DS.getZVVONO());
	                     CR040.setVDSC().move(FAZ145DS.getZVVDSC());
	                     CR040.setVTXT().move(FAZ145DS.getZVVTXT());
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
	                     PXCRTVAC.PPDIVI.move( WWDIVI);
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
	                     CR040.setDIVI().move( WWDIVI);
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
	                     CR040.setVONO(FAZ145DS.getZVVONO());
	                     CR040.setVDSC().move(FAZ145DS.getZVVDSC());
	                     CR040.setVTXT().move(FAZ145DS.getZVVTXT());
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
	                  FASVL.setVATP( WXVTED);
	                  IN91 = !FASVL.CHAIN("00", FASVL.getKey("00"));
	                  if (!IN91 && !isBlank(FASVL.getFAVA(), EPS_2)) {
	                     XXDEPR += FASVL.getFAVA();
	                     XXDEPC += FASVL.getFAVC();
	                     //   Check accounting reference number for depreciation credit
	                     // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	                     //    Set params for CRTVACC (Scraps-Discarding of assets)
	                     PXCRTVAC.PPCONO = LDAZD.CONO;
	                     PXCRTVAC.PPDIVI.move( WWDIVI);
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
	                     CR040.setDIVI().move( WWDIVI);
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
	                     CR040.setVONO(FAZ145DS.getZVVONO());
	                     CR040.setVDSC().move(FAZ145DS.getZVVDSC());
	                     CR040.setVTXT().move(FAZ145DS.getZVVTXT());
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
	                     PXCRTVAC.PPDIVI.move( WWDIVI);
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
	                     CR040.setDIVI().move( WWDIVI);
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
	                     CR040.setVONO(FAZ145DS.getZVVONO());
	                     CR040.setVDSC().move(FAZ145DS.getZVVDSC());
	                     CR040.setVTXT().move(FAZ145DS.getZVVTXT());
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
	            PXCRTVAC.PPDIVI.moveLeftPad( WWDIVI);
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
	            FAHIS.setASID().move( WWASID);
	            FAHIS.setSBNO( WWSBNO);
	            if (IN63) {
	               FAHIS.setASID().move( WWNWFN);
	               FAHIS.setSBNO( WWNWSN);
	            }
	            FAHIS.setDIVI().move( WWDIVI);
	            FAHIS.setVPER(CR040.getACYP());
	            FAHIS.setCUCD().move(FASMA.getCUCD());
	            if (CRS750DS.getPBACBC() == 1) {
	               FAHIS.setVSER().move(DSFFNC.getDFVSER());
	            } else {
	               FAHIS.setVSER().clear();
	            }
	            if (DSDPTP.getFAWFAM() == 1) {
	               FAHIS.setVONO(FAZ145DS.getZVVONO());
	               FAHIS.setVDSC().move(FAZ145DS.getZVVDSC());
	            } else {
	               FAHIS.setVONO(0);
	               FAHIS.setVDSC().clear();
	               FAHIS.setVSER().clear();
	            }
	            FAHIS.setVATP( WSVATP);
	            FAHIS.setFAVA(XXSCVA);
	            FAHIS.setFAVC(XXSCVC);
	            if (IN63) {
	               FAHIS.setFAVA(XYSCVA);
	               FAHIS.setFAVC(XYSCVC);
	            }
	            WKDPTP.move(FASDM.getDPTP());
	            if (WKDPT1.getInt() == 9) {
	               if (IN63) {
	                  FASVL.setASID().move( WWNWFN);
	                  FASVL.setSBNO( WWNWSN);
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
	            FASVL.setASID().move( WWASID);
	            FASVL.setSBNO( WWSBNO);
	            if (IN63) {
	               FASVL.setASID().move( WWNWFN);
	               FASVL.setSBNO( WWNWSN);
	            }
	            FASVL.setDIVI().move( WWDIVI);
	            FASVL.setVATP( WSVATP);
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
	            FASDM.setDPTP( WSDPTP);            
	         // Update current depreciation book
	            if (FASDM.CHAIN_LOCK("00", FASDM.getKey("00"))) {
	                PRPPRT();
	                UFAHIS();
	                FASDM.setFAST(9);
	                FASDM.UPDAT("00");
	            }

	            // If disposing only one depreciation type, exit
	            if (forDisposeDPTP != 0) {
	                break;
	            }

	            // Read next depreciation book (MI replacement for getSFL())
	            IN91 = !FASDM.READE("00", FASDM.getKey("00", 4));
	            IN92 = FASDM.getErr("00");
	         }
	      
	      if (hasValidWFAM && DSDPTP.getFAWFAM() != 1) {  
	         DSDPTP.setFAWFAM(1);    
	      }  
	      //   - Init key with value type for fixed assets total acquisition
	      FAHIS.setCONO(LDAZD.CONO);
	      FAHIS.setDIVI().move( WWDIVI);
	      FAHIS.setASID().move( WWASID);
	      FAHIS.setSBNO( WWSBNO);
	      if (IN63) {
	         FAHIS.setASID().move( WWNWFN);
	         FAHIS.setSBNO( WWNWSN);
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
	         CR040.setDIVI().move( WWDIVI);
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
	               FAZ145DS.setZVVONO(PLCHKVO.FVVONO);
	               FAZ145DS.setZVVDSC().move(DSFFNC.getDFVDSC());
	               vonoRetrieved = true;
	            }
	         }
	         CR040.setVONO(FAZ145DS.getZVVONO());
	         CR040.setVDSC().move(FAZ145DS.getZVVDSC());
	         CR040.setVTXT().move( WWVTXT);
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
	         PXCRTVAC.PPDIVI.move( WWDIVI);
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
	            CR040.setVONO(FAZ145DS.getZVVONO());
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
	      FASMA.setASID().move( WWASID);
	      FASMA.setSBNO( WWSBNO);
	      if (IN63) {
	         FASMA.setASID().move( WWNWFN);
	         FASMA.setSBNO( WWNWSN);
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
	      FASVL.setASID().move(WWNWFN);
	      FASVL.setSBNO(WWNWSN);
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
	      FASVL.setVATP(WXVTED);
	      IN91 = !FASVL.CHAIN("00", FASVL.getKey("00"));
	      IN92 = FASVL.getErr("00");
	      if (!IN91) {
	         XYSCVA -= FASVL.getFAVA();
	         XYSCVC -= FASVL.getFAVC();
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
		      WXEODP = 0;
		      FDTED.setDIVI().move(WWDIVI);
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
		            WXEODP += FAHIS.getFAVA();
		            IN91 = !FAHIS.READE("10", FAHIS.getKey("10", 5));
		         }
		      }
		      //-----------------------------------------------------------------
		      //   Remaining value
		      XDVTDP = YXPVAL - WXEODP;
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
		      XCEODP.move(WXEODP);
		      XCPVAL.move(YXPVAL);
		      XCRVAL.move(XDVTDP);
		      XCADEP.move(WXADEP);
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
	   *    DIVASS - Divide an asset
	   */
	  public void DIVASS() {
	      FASMA.setASID().move(WWASID);
	      FASMA.setSBNO(WWSBNO);
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
	         this.WWNWFN.moveLeftPad(this.WWASID);
	         this.WWNWSN = this.WWSBNO + 1;
	         FASMA.setASID().move(WWNWFN);
	         FASMA.setSBNO(WWNWSN);
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
	      FASDM.setASID().move(WWASID);
	      FASDM.setSBNO(WWSBNO);
	      FASDM.SETLL("00", FASDM.getKey("00", 4));
	      IN93 = !FASDM.READE_LOCK("00", FASDM.getKey("00", 4));
	      IN92 = FASDM.getErr("00");
	      while (!IN93) {
	         double savedStopValue = FASDM.getSVAL();
	         FASDM.setSVAL(FASDM.getSVAL() * YYPART);
	         FASDM.UPDAT("00");
	         FASDM.setSVAL(savedStopValue * XXPART);
	         FASDM.setASID().move(WWNWFN);
	         FASDM.setSBNO(WWNWSN);
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
	              
	         FASDM.setASID().move(WWASID);
	         FASDM.setSBNO(WWSBNO);
	         IN93 = !FASDM.READE_LOCK("00", FASDM.getKey("00", 4));
		     IN92 = FASDM.getErr("00");
	         
	      }
	      FASVL.setASID().move(WWASID);
	      FASVL.setSBNO(WWSBNO);
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
	            FAHIS.setASID().move(WWNWFN);
	            FAHIS.setSBNO(WWNWSN);
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
	         FASVL.setASID().move(WWNWFN);
	         FASVL.setSBNO(WWNWSN);
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
			 
	         FASVL.setASID().move(WWASID);
	         FASVL.setSBNO(WWSBNO);
	         IN83 = !FASVL.READE_LOCK("00", FASVL.getKey("00", 4));
	         IN92 = FASVL.getErr("00");
	      }
	   }
	  
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
	      FAHIS.setRGTM(movexTime());
	      FAS100DS.setZWRGTM(FAHIS.getRGTM());
	      YYRGTM.move(FAHIS.getRGTM());
	      FAHIS.setCONO(FASDM.getCONO());
	      FAHIS.setDIVI().move(FASDM.getDIVI());
	      FAHIS.setASID().move(FASDM.getASID());
	      FAHIS.setSBNO(FASDM.getSBNO());
	      FAHIS.setVPER(FAS100DS.getZWYPTO());
	      FAHIS.setVATP(FASDM.getVTDP());
	      IN91 = !FAHIS.CHAIN("00", FAHIS.getKey("00"));
	      IN92 = FAHIS.getErr("00");
	      if (IN91) {
	         FAHIS.setVPER(FAS100DS.getZWYPTO());
	         FAHIS.setCUCD().move(SVCUCD);
	         if (!isBlank(FHDEP2, EPS_2)) {
	            FAHIS.setFAVA(mvxHalfAdjust(FHDEP2, getDecimalCode(MNDIV.getLOCD())));
	            FAHIS.setFAVC(mvxHalfAdjust(FHDEPC, getDecimalCode(FAHIS.getCUCD())));
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
	      if (IN63) {
	         FAHIS.setASID().move(WWASID);
	         FAHIS.setSBNO(WWSBNO);
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
	                        if (FAHIS.getVATP() == WXVTED) {
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
	   }
	  
	   public void RDDPTP() {
		      SYTAB.setSTCO().moveLeft("DPTP");
		      SYTAB.setLNCD().clear();
		      SYTAB.setSTKY().clear();
		      SYTAB.setSTKY().moveLeft(FASDM.getDPTP(), 2);
		      IN93 = !SYTAB.CHAIN("00", SYTAB.getKey("00"));
		      IN92 = SYTAB.getErr("00");
		      if (!IN93) {
		         DSDPTP.setDSDPTP().moveLeft(SYTAB.getPARM());
		      }
		   }
	   
	public void fecthFA10FamEntryId() {
		SYTAB.setCONO(LDAZD.CONO);
		SYTAB.setDIVI().move(WWDIVI);
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
     *    get the last TRNON in FCR040 and assign it to lastTRNO varaible
     */
    public void getLastTRNO() {
       //   get the current TRNO
       lastTRNO = CR040.getTRNO();
       CR040.setJBNO(FAZ145DS.getZVJBNO());
       CR040.setJBDT(FAZ145DS.getZVJBDT());
       CR040.setJBTM(FAZ145DS.getZVJBTM());
       CR040.setBCHN(0);       
       CR040.setTRNO(9999999);
       CR040.SETGT("00", CR040.getKey("00"));
       if (CR040.REDPE("00", CR040.getKey("00", 5))) {
          lastTRNO = CR040.getTRNO();
       }
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
	         FAHIS.setASID().move(WWNWFN);
	         FAHIS.setSBNO(WWNWSN);
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
	         FASVL.setASID().move(WWNWFN);
	         FASVL.setSBNO(WWNWSN);
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
    * convertNumToString - convert number to string using the decimal format
    * @param  amount = value for conversion
    *         dccd   = currency decimal format
    *         editCode = edit code to the formatting output
    * @return - string value format of the number
    */
   public MvxString convertNumToString (double amount, int dccd, char editCode) {      
      this.PXDCCD = dccd;
      this.PXFLDD = 13 + this.PXDCCD;
      this.PXEDTC = editCode;
      this.PXDCFM = LDAZD.DCFM;
      this.PXNUM = amount;            
      this.PXALPH.clear();
      SRCOMNUM.COMNUM();
      return this.PXALPH;      
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
   *    INIT - Init subroutine
   */
	public void INIT() {
		FASMA.setCONO(LDAZD.CONO);
		FASDM.setCONO(LDAZD.CONO);
		FASVL.setCONO(LDAZD.CONO);
		FAHIS.setCONO(LDAZD.CONO);
		SYTAB.setCONO(LDAZD.CONO);

		FASMA.setDIVI().move(LDAZD.DIVI);
		FASDM.setDIVI().move(LDAZD.DIVI);
		FASVL.setDIVI().move(LDAZD.DIVI);
		FAHIS.setDIVI().move(LDAZD.DIVI);

		MNDIV.setCONO(LDAZD.CONO);
		MNDIV.setDIVI().move(LDAZD.DIVI);

		if (MNDIV.CHAIN("00", MNDIV.getKey("00"))) {
			SYTAB.setCONO(LDAZD.CONO);
			XSDIVI.move(SYTAB.getDIVI());
			SYTAB.setDIVI().clear();
			SYTAB.setSTCO().moveLeft("CUCD");
			SYTAB.setSTKY().moveLeftPad(MNDIV.getLOCD());
			if (SYTAB.CHAIN("00", SYTAB.getKey("00"))) {
				DSCUCD.setDSCUCD().moveLeft(SYTAB.getPARM());
				XXDCCD = DSCUCD.getYQDCCD();
			} else {
				XXDCCD = LDAZD.LCDC;
			}
			SYTAB.setDIVI().move(XSDIVI);
		}
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

   public void PLCHKVOSyncTo() {
	      PLCHKVO.FVDIVI.move(FASVL.getDIVI());
	      PLCHKVO.FVCONO = FASVL.getCONO();
	   }

	   public void PLCHKVOSyncFrom() {
	      FASVL.setDIVI().move(PLCHKVO.FVDIVI);
	      FASVL.setCONO(PLCHKVO.FVCONO);
	   }
	   
   public cPLCHKVO PLCHKVO = new cPLCHKVO(this);
   public cMICommon MICommon = new cMICommon(this);
   public cRetrieveUserInfo retrieveUserInfo = new cRetrieveUserInfo(this);
   public cPXGETVAL PXGETVAL = new cPXGETVAL(this);
   public cPXCRTVAC PXCRTVAC = new cPXCRTVAC(this);
   public cPLCLCCU PLCLCCU = new cPLCLCCU(this);
   
   public sDSDPTP DSDPTP = new sDSDPTP(this);
   public sFAZ145DS FAZ145DS = new sFAZ145DS(this);
   public sFAS100DS FAS100DS = new sFAS100DS(this);
   public sFAS900DS FAS900DS = new sFAS900DS(this);
   public sDSMUST DSMUST = new sDSMUST(this);
   public sDSMUCU DSMUCU = new sDSMUCU(this);
   
   public Vector VectorOfCUCD;
   public Vector VectorOfDCCD;
  
   
   
   public boolean hasValidWFAM;
   public boolean vonoRetrieved;
   public boolean retValidation;
   public boolean willReverseAcqValue; 
   public boolean reverseAcqValue;
   public boolean disposeAcqValue;
   public boolean disposeErrorShown;
   public boolean disposeErrorShownPBCHK;
   public boolean continueDispose;
   public boolean forceWriteReveralAcquisitionCost;
  
   public int notValidDisposableDPTP;  
  
   public int[] allowedDPTP = new int[20];
   public FAS145DSP DSP;
  
  
   public sDSFEID DSFEID = new sDSFEID(this);
   public sDSCUCD DSCUCD = new sDSCUCD(this);
   public sDSFFNC DSFFNC = new sDSFFNC(this);
   public MvxRecord rMNGCOR = new MvxRecord();
   public MvxStruct rZWYPTS = new MvxStruct(6);
   public MvxString ZWYPTS = rZWYPTS.newInt(0, 6);
   public MvxString ZWYPT4 = rZWYPTS.newInt(0, 4);
   public MvxStruct rZZYEAR = new MvxStruct(6);    
   public MvxString ZZYEAR = rZZYEAR.newInt(0, 6);    
   public MvxString ZZYEA4 = rZZYEAR.newInt(0, 4);    
   public MvxString ZZPERI = rZZYEAR.newInt(4, 2);
   public MvxString XZBJNO = new MvxString(18); 
   public MvxString lastUsedCUCD = new MvxString(3);
   
   //*STRUCDEF rZABJNO{
   public MvxStruct rZABJNO = new MvxStruct(18);
   public MvxString ZABJNO = rZABJNO.newString(0, 18);
   public MvxString ZAJNU = rZABJNO.newInt(0, 6);
   public MvxString ZAPRD = rZABJNO.newInt(6, 6);
   public MvxString ZAPRT = rZABJNO.newInt(12, 6);
   
   public MvxStruct rYYTDTA = new MvxStruct(30);
   public MvxString YYTDTA = rYYTDTA.newString(0, 30);
   public MvxString YYDPTP = rYYTDTA.newInt(0, 2);
   public MvxString YYRGTM = rYYTDTA.newInt(2, 8);
  
   public double XXPART; public double YYPART; public double FHDEP2; public double FHDEPC;
   public double SVFAVA; public double SVFAVC; public double XXPVAL; public double XXSCVA; public double XXPVAC; 
   public double XXSCVC; public double YXPVAL; public double YYPVAL; public double XDACDP; public double XDADTY;
   public double XDVTDP; public double SVFAPA; public double SVFAPC; public double SVFAYA; public double SVFAYC;
   public double SVFACA; public double SVFACC; public double SVFAAA; public double SVFAAC; public double SVFAXA;
   public double SVFAXC; public double SVTOTA; public double SVTOTC; public double WKACAM; public double AVASCR;
   public double AVCSCR; public double XXACDP; public double XXACDC; public double XYSCVA; public int WSVATP;
   public double XYSCVC; public double XXFAVA;
   
   
   public double XXDEPR;//*LIKE FAVA
   public double XXDEPC;//*LIKE FAVC
   public double YYFAQT;
   public double XXFAQT;
   public double XDFAQT;
   public double S9FAVC;//*LIKE FAVC
   public double S9FAVA;//*LIKE FAVA
   
      
     
   
   public int XXACDT; public int CUDATE; public int WWSBNO; public int WWNWSN; public int forDisposeDPTP = 0;
   public int XXACYP; public int totalPeriods; public int XXCHNO; public int transNoCAATRA;
   public int X1CONO; public int WXEODP; public int WXVTED; public int WXADEP; public int totalDPTP;
   public int decimalCode; public int XLDPTP; public int XXDPTP; public int XXDCCD; public int XXSBNO;
   public int WSDPTP;  
   public int XXMUNI;//*LIKE MUST
   public int lastTRNO;
   
   
   
   public boolean PARTIALDISPOSAL;
   public MvxString XXASID = new MvxString(15);
   public MvxString XSDIVI = new MvxString(3);
   public MvxString XXSTCO = new MvxString(4);
   public MvxString XXSTKY = new MvxString(20);
   public MvxStruct rXCKFL1 = new MvxStruct(30);
   public MvxString XCKFL1 = rXCKFL1.newString(0, 30);
   public MvxString XCASID = rXCKFL1.newString(0, 10);
   public MvxString XCSBNO = rXCKFL1.newInt(10, 3);
   public MvxString XCDIVI = rXCKFL1.newString(13, 3);
   public MvxString X1DIVI = new MvxString(3);     
   public MvxString X1STCO = new MvxString(10);
   
   public MvxStruct rFESTKY = new MvxStruct(10);
   public MvxString FESTKY = rFESTKY.newString(0, 10);
   public MvxString FEFEID = rFESTKY.newString(0, 4);
   public MvxString FEFNCN = rFESTKY.newInt(4, 3);
   public MvxStruct rWKDPTP = new MvxStruct(2);
   public MvxString WKDPTP = rWKDPTP.newInt(0, 2);
   public MvxString WKDPT1 = rWKDPTP.newInt(0, 1);
   
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
   
   public sCRS750DS CRS750DS = new sCRS750DS(this);
   public sCAS900DS CAS900DS = new sCAS900DS(this);   
   
   public MvxString WWASID = new MvxString(10);
   public MvxString WWDIVI = new MvxString(3);
   public MvxString WWNWFN = new MvxString(10);
   public MvxString WWVTXT = new MvxString(40);
   public MvxString WWFADS = new MvxString(40);
   public MvxString SVCUCD = new MvxString(3);//*LIKE CUCD
   public MvxString XVCUCD = new MvxString(3);//*LIKE CUCD

   //Static
   public static final String GetAsset = "GetAsset";
   public static final String Dispose = "Dispose";

	public String getVarList(java.util.Vector v) {
		super.getVarList(v);

		v.addElement(XXSTCO);

		v.addElement(SYCAL);

		v.addElement(PXCRTVAC);
		v.addElement(rZABJNO);
		v.addElement(JBCMD);
		v.addElement(rZWYPTS);
		v.addElement(FASVL);

		v.addElement(DSFFNC);
		v.addElement(FAZ145DS);
		v.addElement(FASMA);
		v.addElement(FAS100DS);
		v.addElement(FASDM);
		v.addElement(XSDIVI);
		v.addElement(SYPAR);
		v.addElement(MNDIV);

		v.addElement(rFESTKY);

		v.addElement(CRS750DS);
		v.addElement(rWKDPTP);
		v.addElement(PXGETVAL);

		v.addElement(rXCKFL1);
		v.addElement(rYYTDTA);

		v.addElement(CR040);
		v.addElement(DSMUST);
		v.addElement(FCHKF);

		v.addElement(DSMUCU);

		v.addElement(rXCDATA);
		v.addElement(SYTAB);
		v.addElement(FAHIS);
		v.addElement(SYSTR);
		v.addElement(SYSTP);

		v.addElement(FDTSC);
		v.addElement(FAS900DS);
		v.addElement(SVCUCD);
		v.addElement(FDTED);

		v.addElement(DSDPTP);
		v.addElement(PLCHKVO);
		v.addElement(XXSTKY);
		v.addElement(XVCUCD);
		v.addElement(DSP);
		v.addElement(lastUsedCUCD);
		v.addElement(SYPER);

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
	     
	      XXDEPR = 0D;
	      XXDEPC = 0D;
	     
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
	    
	      YYFAQT = 0D;
	     
	      XXFAQT = 0D;
	      XXDPTP = 0;
	      XXACYP = 0;
	      YYPART = 0D;
	      XXACDT = 0;
	      XXACDP = 0D;
	      XLDPTP = 0;
	      XXACDC = 0D;
	      XXPART = 0D;
	   
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
	      
	   }	

  

   public final static String _version = "15";
   public final static String _release = "1";
   public final static String _spLevel = "0";
   public final static String _spNumber ="MAK_XKEXTPAM01_260702_14:15";
   public final static String _GUID = "0B8BE2D2CBF74971862F4EA81D362BEF";
   public final static String _tempFixComment = "";
   public final static String _build = "000000000001658";
   public final static String _pgmName="FAZ145MI";

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
   }

  public final static String [][] _standardModifications={};
} 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	
