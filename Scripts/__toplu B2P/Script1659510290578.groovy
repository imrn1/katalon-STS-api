import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys


WebUI.callTestCase(findTestCase('_Custom Cases/_GenerateUniqeID'), [:], FailureHandling.STOP_ON_FAILURE)

def amount = "$amount".toString();
if(amount.contains(",")) {
	 amount = amount.replace(',',".")
}

println("amount : "+amount)
WS.sendRequestAndVerify(findTestObject('Postman/Transaction/BusinessToPersonalTransfer', [('ext_transaction_id') : GlobalVariable.uniqe_id
            , ('sender_account_number') : "$sender_account_number", ('sender_wallet_number') : "$sender_wallet_number", ('currency_code') : "$currency_code"
            , ('description') : "$description", ('amount') : amount, ('receiver_wallet_number') : "$receiver_wallet_number"]))



CustomKeywords.'writeFile.WriteFile.writeExel'('BusinessToPersonalTransfer',"toplu para transferi (B2P)","0","","",'B2P',"")
 
WebUI.callTestCase(findTestCase('_Verify Cases/verify status'), [('status') : '0'])




