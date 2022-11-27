package customKeyword

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.testobject.ResponseObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows

import internal.GlobalVariable

import java.time.format.DateTimeFormatter as DateTimeFormatter
import java.time.LocalDateTime as LocalDateTime



public class CustomMethods{

	public static void setGlobalValueNull(String globalVariable){
		//println("globalVariable before : "+ GlobalVariable[String.valueOf(globalVariable)] )
		GlobalVariable[String.valueOf(globalVariable)] = null;
		//println("globalVariable after : "+ GlobalVariable[String.valueOf(globalVariable)] )
	}


	public static Boolean isNullOrEmpty(String str) {
		return (str == null || str.allWhitespace)
	}


	public static String GenerateUniqeNumber(Integer characterNumber = 14 ) {

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern('SSyyyymmddHHMMSS')
		LocalDateTime now = LocalDateTime.now()

		characterNumber =  characterNumber > 14 ? 14 : characterNumber

		def ID = dtf.format(now)

		println("generated uniqe_id : "+ ID)
		return ID.substring(0,characterNumber)
	}


	public static String GetKYCStatus(String accountNumber, Enums.AccountType accountType  ) {

		String kyc_level_status = "none"
		if(!isNullOrEmpty(accountNumber)) {

			if(accountType.equals(Enums.AccountType.personal)) {
				ResponseObject GetPersonalAccountResponse = WS.sendRequestAndVerify(findTestObject('Postman/Account/Get Personal Account',
						[('account_number') : accountNumber ]))
				try {
					kyc_level_status = WS.getElementPropertyValue(GetPersonalAccountResponse, "payload.kyc_level")
				}catch(Exception ex) {
					kyc_level_status = "none"
				}
			}else {
				def GetBusinessAccountResponse = WS.sendRequestAndVerify(findTestObject('Postman/Account/Get Business Account',
						[('account_number') : accountNumber ]))
				try {
					kyc_level_status = WS.getElementPropertyValue(GetBusinessAccountResponse, "payload.kyc_level")
				}catch(Exception ex) {
					kyc_level_status = "none"
				}
			}
		}
		return kyc_level_status
	}


	public static Enums.AccountType DefineAccountType(String content, String personalPattern = "_p_",String businessPattern="_b_") {
		
		println("---------------  DefineAccountType -------------")
		println("content: "+content + " personalPattern: "+personalPattern +" businessPattern: "+businessPattern)
		
		if(personalPattern!=null && content.toLowerCase().contains(personalPattern.toLowerCase()))
			return Enums.AccountType.personal
		else if(businessPattern!=null && content.toLowerCase().contains(businessPattern.toLowerCase()))
			return Enums.AccountType.business
			
		throw new Exception("hesap türü belirlenemedi") 	
				
	}
}




