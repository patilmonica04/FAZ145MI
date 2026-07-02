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
package mvx.app.pgm;

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

/*
*Modification area - M3
*Nbr            Date   User id     Description
*99999999999999 999999 XXXXXXXXXX  x
*Modification area - Business partner
*Nbr            Date   User id     Description
*99999999999999 999999 XXXXXXXXXX  x
*Modification area - Customer
*Nbr            Date   User id     Description
*99999999999999 999999 XXXXXXXXXX  x
*/

/**
*<BR><B><FONT SIZE=+2>Api: ESM&R: Usage based depreciation interface</FONT></B><BR><BR>
*
* This class ...<BR><BR>
*
*/
public class FAS010MI extends MIBatch
{
   public void movexMain() {
      INIT();
      //   Accept conversation
      MICommon.initiate();
      MICommon.accept();
      while (MICommon.read()) {
         //   Execute command
         if (MICommon.isTransaction(GET_USER_INFO)) {
            MICommon.setTransaction(retrieveUserInfo.getMessage());
         } else if (MICommon.isTransaction(LST_FIXED_ASSET)) { 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	
            lstAssetUsage();         
         } else if (MICommon.isTransaction(UPD_ASSET_USAGE)) { 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	
            UpdAssetUsage();              	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	
         } else {
            MICommon.setTransactionError();
         }
         MICommon.write();
      }
      //   Deallocate
      MICommon.close();
      SETLR();
      return;
   } // end of method
   
   /**
   *    lstAssetUsage  - Execute command lstAssetUsage
   */
   public void lstAssetUsage() {
      boolean isRecordFound = false;
      int startingVPER = 0;
      sFAS010MIRLstAssetUsage inLstAssetUsage = (sFAS010MIRLstAssetUsage)MICommon.getInDS(sFAS010MIRLstAssetUsage.class);
      sFAS010MISLstAssetUsage outLstAssetUsage = (sFAS010MISLstAssetUsage)MICommon.getOutDS(sFAS010MISLstAssetUsage.class);
      if (MICommon.toNumericCompany(inLstAssetUsage.getQ0CONO())) { 	
         FADUS.setCONO(MICommon.getInt());
      } else {
         MICommon.setError("CONO");
         return;
      } 	
      if (inLstAssetUsage.getQ0DIVI().isBlank()) {
         //  MSGID=WDI0102	Division must be entered
         MICommon.setError("DIVI", "WDI0102");
         return; 	 	 	 	 	 	 	
      } else {
         FADUS.setDIVI().moveLeft(inLstAssetUsage.getQ0DIVI());
      }
      if (inLstAssetUsage.getQ0ASID().isBlank()) {
         //  MSGID=WAS3002	Fixed asset ID must be entered
         MICommon.setError("ASID", "WAS3002");
         return;
      } else {
         FADUS.setASID().moveLeft(inLstAssetUsage.getQ0ASID());
      }
      if (MICommon.toNumeric(inLstAssetUsage.getQ0SBNO())) {
      	FADUS.setSBNO(MICommon.getInt());
      } else {
      	MICommon.setError("SBNO");
      	return;
      }
      if (MICommon.toNumeric(inLstAssetUsage.getQ0DPTP())) {
      	FADUS.setDPTP(MICommon.getInt());
      } else {
      	MICommon.setError("DPTP");
      	return;
      }
      if (MICommon.toNumeric(inLstAssetUsage.getQ0VPER())) {
      	FADUS.setVPER(MICommon.getInt());
      } else {
      	MICommon.setError("VPER");
      	return;
      }
      //check if record exist
      if (FADUS.getVPER() != 0) {
         startingVPER = FADUS.getVPER();         
      }
      isRecordFound = FADUS.CHAIN("01", FADUS.getKey("01", 5));
      if (!isRecordFound) {
      	//  MSGID=WBU1103 Fixed Asset ID &1 does not exist
      	MICommon.setError("ASID", "WAS3003", inLstAssetUsage.getQ0ASID());
      	return;
      }      
      int noOfFAPeriod = 0; 	 	 	 	 	 	 	 	 	 	 	 	 	
      while (isRecordFound && noOfFAPeriod < MICommon.getMaxRecords()) { 	 	 	 	 	 	 	 	 	 	 	 	 	
         if (startingVPER == 0 ||
             startingVPER != 0  && FADUS.getVPER() >= startingVPER) {
            setLstAssetUsageOutputFields(outLstAssetUsage); 	
            MICommon.setData(outLstAssetUsage.get()); 	
            MICommon.reply(); 	
            noOfFAPeriod++;         
         }
         isRecordFound = FADUS.READE("01", FADUS.getKey("01", 5));
      }
      MICommon.clearBuffer();
   } //.end method lstAssetUsage 	

   /**
   *  Sets output fields for command LstAssetUsage
   */
   public void setLstAssetUsageOutputFields(sFAS010MISLstAssetUsage outLstAssetUsage) { 	
      outLstAssetUsage.setY0DMTS().moveLeftPad(FADUS.getDMTS());
      outLstAssetUsage.setY0VPER().moveLeftPad(MICommon.toAlpha(FADUS.getVPER()));	   
      outLstAssetUsage.setY0ADSH().moveLeftPad(MICommon.toAlpha(FADUS.getADSH(), XXDCCD));
      outLstAssetUsage.setY0USSH().moveLeftPad(MICommon.toAlpha(FADUS.getUSSH(), XXDCCD));
   } //.end method setLstAssetUsageOutputFields 	

   /**
   *    UpdAssetUsage  - Execute command UpdAssetUsage
   */
   public void UpdAssetUsage() {
      boolean isPeriodDepr = false;
      sFAS010MIRUpdAssetUsage inUpdAssetUsage = (sFAS010MIRUpdAssetUsage)MICommon.getInDS(sFAS010MIRUpdAssetUsage.class);      
      if (MICommon.toNumericCompany(inUpdAssetUsage.getQ1CONO())) { 	
         FADUS.setCONO(MICommon.getInt());
      } else {
          MICommon.setError("CONO");
         return;
      } 	
      if (inUpdAssetUsage.getQ1DIVI().isBlank()) {
      	 //  MSGID=WDI0102	Division must be entered	 	 	 	 	 	 	
	       MICommon.setError("DIVI", "WDI0102"); 	 	 	 	 	 	 	
	       return; 	 	 	 	 	 	 	
      } else {
         FADUS.setDIVI().moveLeft(inUpdAssetUsage.getQ1DIVI());
      }
      if (inUpdAssetUsage.getQ1ASID().isBlank()) {
      	 //  MSGID=WBU1102	Fixed Asset ID must be entered	 	 	 	 	 	 	
	       MICommon.setError("ASID", "WAS3002"); 	 	 	 	 	 	 	
	       return; 	 	 	 	 	 	 	
      } else {
         FADUS.setASID().moveLeft(inUpdAssetUsage.getQ1ASID());
      }      
      if (MICommon.toNumeric(inUpdAssetUsage.getQ1SBNO())) {
      	 FADUS.setSBNO(MICommon.getInt());
      } else {
      	 MICommon.setError("SBNO");
      	 return;
      }
       if (MICommon.toNumeric(inUpdAssetUsage.getQ1DPTP())) {
      	 FADUS.setDPTP(MICommon.getInt());
      } else {
      	 MICommon.setError("DPTP");
      	 return;
      }
      if (MICommon.toNumeric(inUpdAssetUsage.getQ1VPER())) {
      	 FADUS.setVPER(MICommon.getInt());
      } else {
      	 MICommon.setError("VPER");
      	 return;
      }
      if (MICommon.toNumeric(inUpdAssetUsage.getQ1USSH())) {
      	 FADUS.setUSSH(MICommon.getDouble());
      } else {
      	 MICommon.setError("USSH");
      	 return;
      }
      //check if record exist
      if (!FADUS.CHAIN_LOCK("01", FADUS.getKey("01", 6))) {
      	 //  MSGID=WBU1103 Fixed Asset &1 does not exist
      	 MICommon.setError("ASID", "WAS3003", inUpdAssetUsage.getQ1ASID());
      	 return;
      } else  {
         FASDM.setASID().moveLeftPad(FADUS.getASID());
         FASDM.setSBNO(FADUS.getSBNO());
         FASDM.setDPTP(FADUS.getDPTP());
         if (FASDM.CHAIN("00", FASDM.getKey("00"))) {
            if (FASDM.get3DEQ() == '0') {
               //  MSGID=FA00208 Only 3rd-party equipment are allowed for update.
                MICommon.setError("ASID", "FA00208", inUpdAssetUsage.getQ1ASID());
                return;
            }
            Expression addtlClause = Expression.createEQ("FFAHIS", "FHVATP", String.valueOf(FASDM.getVTDP()));            
            FieldSelection fieldSelection = new FieldSelection("FFAHIS", "70");
            fieldSelection.setExpression(addtlClause);
            FAHIS.setASID().moveLeftPad(FADUS.getASID());
            FAHIS.setSBNO(FADUS.getSBNO());
            FAHIS.setVPER(FADUS.getVPER());
            FAHIS.SETLL("70", FAHIS.getKey("70", 5));
            isPeriodDepr = FAHIS.READE("70", FAHIS.getKey("70", 5), fieldSelection);
            if (!isPeriodDepr) {
               //   MSGID=FA01002 The write-off has been registered
               MICommon.setError("VPER", "FA01002");
               return;
            }
         } else {
            //   MSGID=WVPE103 Value period &1 does not exist
            MICommon.setError("VPER", "WVPE103", inUpdAssetUsage.getQ1ASID());
            return;
         }      
      
         //- Check and convert used share from Alpha to Numeric
         this.PXDCCD = XXDCCD;
         this.PXFLDD = 13 + this.PXDCCD;
         this.PXEDTC = 'M';
         this.PXDCFM = MNDIV.getDCFM();
         this.PXNUM = 0d;
         this.PXALPH.clear();
         this.PXALPH.moveLeft(inUpdAssetUsage.getQ1USSH());
         SRCOMNUM.COMNUM();
         if (SRCOMNUM.PXNMER == 0) {
            FADUS.setUSSH(this.PXNUM);
            FADUS.setZUSE('*');
         } else {
            FADUS.setUSSH(0.0d);   
            FADUS.setZUSE(' ');
         }
         this.PXDFMI.move("TIME");
         this.PXDATI = 0;
         this.PXDFMO.move("YMD8");
         this.PXOPRM = 1;
         COMDAT();
         FADUS.setLMDT(this.PXDATO);         
         FADUS.setCHNO(FADUS.getCHNO() + 1);
         FADUS.setCHID().move(this.DSUSS);
         FADUS.UPDAT("01");
      }      
   } //.end method UpdAssetUsage    
   
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
   *    INIT - Init subroutine
   */
   public void INIT() {
      //   Init keyvalue
      FADUS.setCONO(LDAZD.CONO);
      FADUS.setDIVI().move(LDAZD.DIVI);
      FAHIS.setCONO(LDAZD.CONO);
      FAHIS.setDIVI().move(LDAZD.DIVI);
      FASDM.setCONO(LDAZD.CONO);
      FASDM.setDIVI().move(LDAZD.DIVI);
      //get company setup
      MNDIV.setCONO(FADUS.getCONO());
      MNDIV.setDIVI().moveLeft(FADUS.getDIVI());
      if (MNDIV.CHAIN("00", MNDIV.getKey("00"))) {
         SYTAB.setCONO(FADUS.getCONO());
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
         SYTAB.setDIVI().moveLeft(XSDIVI);
      }
   }

   // Movex MDB definitions   
   public mvx.db.dta.CSYTAB SYTAB;   
   public mvx.db.dta.CMNDIV MNDIV;
   public mvx.db.dta.FFADUS FADUS;
   public mvx.db.dta.FFAHIS FAHIS;
   public mvx.db.dta.FFASDM FASDM;
   // Movex MDB definitions end

   public void initMDB() {      
      MNDIV = (mvx.db.dta.CMNDIV)getMDB("CMNDIV", MNDIV);
      MNDIV.setAccessProfile("00", 'R');
      SYTAB = (mvx.db.dta.CSYTAB)getMDB("CSYTAB", SYTAB);
      SYTAB.setAccessProfile("00", 'R');
      FADUS = (mvx.db.dta.FFADUS)getMDB("FFADUS", FADUS);
      FADUS.setAccessProfile("01", 'U');
      FASDM = (mvx.db.dta.FFASDM)getMDB("FFASDM", FASDM);
      FASDM.setAccessProfile("70", 'R');
      FAHIS = (mvx.db.dta.FFAHIS)getMDB("FFAHIS", FAHIS);
      FAHIS.setAccessProfile("70", 'R');
   }

   public cMICommon MICommon = new cMICommon(this);
   public cRetrieveUserInfo retrieveUserInfo = new cRetrieveUserInfo(this);
   public sDSCUCD DSCUCD = new sDSCUCD(this);
   public MvxRecord rMNGCOR = new MvxRecord();
  
   public int XXDCCD;   
   public MvxString XSDIVI = new MvxString(3);//*LIKE CUCD
   
   //Static
   public static final String LST_FIXED_ASSET = "LstAssetUsage";
   public static final String UPD_ASSET_USAGE  = "UpdAssetUsage";

   public String getVarList(java.util.Vector v) { 	
      super.getVarList(v); 	
      v.addElement(MICommon);
      v.addElement(retrieveUserInfo);
      v.addElement(SYTAB);
      v.addElement(MNDIV);
      v.addElement(DSCUCD);
      v.addElement(FADUS);
      v.addElement(FAHIS);
      v.addElement(FASDM);
      v.addElement(XSDIVI);
      return version; 	
   } 	

   public void clearInstance() {
      super.clearInstance();      
      XXDCCD = 0;      
   }

   public final static String _version = "15";
   public final static String _release = "1";
   public final static String _spLevel = "0";
   public final static String _spNumber = "*NONE";
   public final static String _GUID = "0B8BE2D2CBF74971862F4EA81D362BEF";
   public final static String _tempFixComment = "";
   public final static String _build = "000000000001658";
   public final static String _pgmName = "FAS010MI";

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
