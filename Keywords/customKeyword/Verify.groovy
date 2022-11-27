package customKeyword

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

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
import com.kms.katalon.core.util.KeywordUtil;

import java.util.Map;
import java.util.HashMap;


public class Verify {

	public static void VerifyResponseProp(String expected_value,String responseProp) {

		String responsePropValue = WS.getElementPropertyValue(GlobalVariable.responseObject, responseProp).toString()
		boolean check = responsePropValue == expected_value ? true: false

		if(check) {
			KeywordUtil.logInfo( "expected_"+responseProp +" doğrulaması başarılı >> expected_"+responseProp  +" : "+expected_value +" response "+responseProp + " " +responsePropValue)
		}else {
			KeywordUtil.markWarning("expected_"+responseProp +" doğrulaması başarısız >> expected_"+responseProp  +" : "+expected_value +" response "+responseProp + " : " +responsePropValue)
			KeywordUtil.logInfo("expected_"+responseProp +" doğrulaması başarısız >> expected_"+responseProp  +" : "+expected_value +" response "+responseProp + " " +responsePropValue)
			KeywordUtil.markFailedAndStop("expected_"+responseProp +" doğrulaması başarısız")
		}
	}

	//public static void VerifyResponseProp(ArrayList expected_values, ArrayList responseProp) {
	public static void VerifyResponseProp(String status) {

		Map<String, String> statusList = SetExpectedStatus(status)

		for (Map.Entry<String,String> entry : statusList.entrySet()) {

			println("Key = " + entry.getKey() +", Value = " + entry.getValue());

			// responseStatus için kontrol sağlanmalı
			if( entry.getKey().toLowerCase().equals("responsestatuscode")) {
				String responsePropValue = WS.getResponseStatusCode(GlobalVariable.responseObject)
				boolean check = responsePropValue.toString() ==  entry.getValue() ? true: false

				if(check) {
					KeywordUtil.logInfo( "expected_"+entry.getKey() +" doğrulaması başarılı >> expected_"+entry.getKey()  +" : "+entry.getValue() +" response "+entry.getKey() + " " +responsePropValue)
				}else {
					KeywordUtil.markWarning("expected_"+entry.getKey() +" doğrulaması başarısız >> expected_"+entry.getKey()  +" : "+entry.getValue() +" response "+entry.getKey() + " : " +responsePropValue)
					KeywordUtil.logInfo("expected_"+entry.getKey() +" doğrulaması başarısız >> expected_"+entry.getKey()  +" : "+entry.getValue() +" response "+entry.getKey() + " " +responsePropValue)
					KeywordUtil.markFailedAndStop("expected_"+entry.getKey() +" doğrulaması başarısız")
				}

			}


			String responsePropValue = WS.getElementPropertyValue(GlobalVariable.responseObject, entry.getKey().toLowerCase())
			boolean check = responsePropValue.toString() ==  entry.getValue() ? true: false

			if(check) {
				KeywordUtil.logInfo( "expected_"+entry.getKey() +" doğrulaması başarılı >> expected_"+entry.getKey()  +" : "+entry.getValue() +" response "+entry.getKey() + " " +responsePropValue)
			}else {
				KeywordUtil.markWarning("expected_"+entry.getKey() +" doğrulaması başarısız >> expected_"+entry.getKey()  +" : "+entry.getValue() +" response "+entry.getKey() + " : " +responsePropValue)
				KeywordUtil.logInfo("expected_"+entry.getKey() +" doğrulaması başarısız >> expected_"+entry.getKey()  +" : "+entry.getValue() +" response "+entry.getKey() + " " +responsePropValue)
				KeywordUtil.markFailedAndStop("expected_"+entry.getKey() +" doğrulaması başarısız")
			}
		}

	}


	// statusList = status:0,code:null,ResponseStatusCode:200
	public static  Map<String, String> SetExpectedStatus(String statusList){

		Map<String, String> hashMap = new HashMap<String, String>();
		String[] list =  statusList.split(',')

		for(String pair : list) {
			hashMap.put(pair.split(':')[0] , pair.split(':')[1] )
		}

		println("hashMap : "+ hashMap.toString())
		return hashMap
	}




}
