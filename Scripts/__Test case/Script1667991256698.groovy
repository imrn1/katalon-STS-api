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
import customKeyword.CustomMethods as Custom
import customKeyword.Enums as Enums
import customKeyword.Account as Account
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys
import customKeyword.Verify as Verify



//WebUI.callTestCase(findTestCase('_Custom Cases/_Token alma'), [('token') : GlobalVariable.token], FailureHandling.STOP_ON_FAILURE)



//WebUI.callTestCase(findTestCase('_Custom Cases/_Token alma'), [('token') : GlobalVariable.token], FailureHandling.STOP_ON_FAILURE)
Account account = new Account()

//bsn12sipay@hotmail.com
account.getAccountByEmail("$variable", null, 'hotmail.com', null, 'BSn')

println('account_number : ' + account.AccountNumber)

println('wallet_number : ' + account.Wallets.getAt(0).Number)

println('national_id : ' + account.KycInfo.NationalID)

WS.sendRequestAndVerify(findTestObject('Postman/Wallet/ListByFilter', [('account_number') : ('"' + account.AccountNumber) + 
            '"', //"wallet_number": ${wallet_number},
            //"national_id": ${national_id},
            ('page_size') : 10, ('page_index') : 1, ('order_column') : ('"' + 'Id') + '"', ('order_by') : ('"' + 'asc') + 
            '"']))


Verify.VerifyResponseProp("$expected_status")

/*
Dictionary status = new Hashtable()
status.put("status", '0')
status.put("code", "312")

*/
