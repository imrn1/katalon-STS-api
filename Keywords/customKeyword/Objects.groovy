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

import com.kms.katalon.core.testobject.ResponseObject
import internal.GlobalVariable
import customKeyword.Enums
import customKeyword.Verify

import com.google.gson.Gson;


public class Account {
	String AccountNumber;
	Enums.AccountType AccountType = null;
	KycInfo KycInfo;
	List<Wallet> Wallets;

	public Account() {
		KycInfo = new KycInfo();
		Wallets = new ArrayList<Wallet>();
	}

	public void getAccountByEmail(String email, Enums.AccountType accountType=null, String postfix="mail.com",
		String personalPattern = "_p_", String businessPattern = "_b_") {

		String[] x = email.split("@");

		if(accountType == null) {
			//accountType = email.toLowerCase().contains("_p_") ? Enums.AccountType.personal : email.toLowerCase().contains("_b_") ? Enums.AccountType.business : null
			accountType = CustomMethods.DefineAccountType(email, personalPattern, businessPattern)
			println("Account Type : " + accountType)	
		
		}

		if(x.length == 1)
			email +="@"+postfix;


		if(accountType == Enums.AccountType.personal) {
			ResponseObject response = WS.sendRequestAndVerify(findTestObject('Postman/Account/PersonalListByFilter', [
				('id') : '0',
				('tenant_id') : '0',
				('account_id') : '0',
				('email') : email,
				('page_size') : '50',
				('page_index') : '1',
				('order_column') : 'Id',
				('order_by') : 'asc'
			]))

			/*******************************************************/
			Verify.VerifyResponseProp("0", "status")
			/*******************************************************/

			WS.verifyGreaterThan(WS.getElementPropertyValue(response, "payload.results").size(),0)

			AccountNumber =	WS.getElementPropertyValue(response, "payload.results[0].account_number")
			AccountType = accountType
			KycInfo.FirstName = WS.getElementPropertyValue(response, "payload.results[0].user_kyc_info.first_name")
			KycInfo.LastName = WS.getElementPropertyValue(response, "payload.results[0].user_kyc_info.last_name")
			KycInfo.NationalID = WS.getElementPropertyValue(response, "payload.results[0].user_kyc_info.national_id")
			KycInfo.kycLevel = Enums.KycLevel.valueOf(WS.getElementPropertyValue(response, "payload.results[0].kyc_level"))


			ResponseObject WalletListResponse = getWalletList(AccountNumber)

			ArrayList walletList = WS.getElementPropertyValue(WalletListResponse, "payload.results")
			//println("walletList type : "+walletList.getClass() + "\nsize : "+ walletList.size())


			for(int i=0 ; i<walletList.size() ; i++) {
				//println("number : "+walletList[i]["number"])
				Wallet w = new Wallet();

				w.Name = walletList[i]["name"];

				w.Number = walletList[i]["number"];
				w.AccessLevelStatus =  walletList[i]["access_level_status"];

				Balance b = new Balance();
				b.available_balance =  (walletList[i]["payment_balance"]["available"])
				b.unavailable_balance =  (walletList[i]["payment_balance"]["unavailable"])
				w.PaymentBalance = b;

				b=new Balance();
				b.available_balance = (walletList[i]["cash_balance"]["available"])
				b.unavailable_balance =  (walletList[i]["cash_balance"]["unavailable"])
				w.CashBalance = b;

				TransactionLimits l= new TransactionLimits()
				l.max_balance = (walletList[i]["transaction_limits"]["max_balance"])
				l.topup_credit_limit = (walletList[i]["transaction_limits"]["topup_credit_limit"])
				l.topup_cash_limit = (walletList[i]["transaction_limits"]["topup_cash_limit"])
				l.withdrawal_limit = (walletList[i]["transaction_limits"]["withdrawal_limit"])
				l.payment_limit = (walletList[i]["transaction_limits"]["payment_limit"])
				l.wallet_to_wallet_limit = (walletList[i]["transaction_limits"]["wallet_to_wallet_limit"])
				w.Limits = l;

				w.TotalBalance = (walletList[i]["total_balance"])

				Wallets.add(w);
			}

			println("wallets : "+ new Gson().toJson(Wallets) )
		}else if(accountType == Enums.AccountType.business) {

			ResponseObject response = WS.sendRequestAndVerify(findTestObject('Postman/Account/BusinessListByFilter', [
				'tanant_id': 0,  
				'email' :  "\"" + email + "\"",
				'page_size' : 5,
				'page_index' : 1,
				"order_column": "\"Id\"",
				"order_by": "\"asc\""
			]))

			
			/*******************************************************/
			Verify.VerifyResponseProp("0", "status")
			/*******************************************************/

			WS.verifyGreaterThan(WS.getElementPropertyValue(response, "payload.results").size(),0)
			
			
			AccountNumber =	WS.getElementPropertyValue(response, "payload.results[0].account_number")
			AccountType = accountType

			ResponseObject WalletListResponse = getWalletList(AccountNumber)
			ArrayList walletList = WS.getElementPropertyValue(WalletListResponse, "payload.results")

			KycInfo.FirstName = walletList[0]["user_kyc_info"]["first_name"]
			KycInfo.LastName =  walletList[0]["user_kyc_info"]["last_name"]
			KycInfo.NationalID = walletList[0]["user_kyc_info"]["national_id"]
			KycInfo.kycLevel = Enums.KycLevel.valueOf( walletList[0]["kyc_level_status"])

			println("KycInfo : "+ new Gson().toJson(KycInfo) )
			
			for(int i=0 ; i<walletList.size() ; i++) {
				//println("number : "+walletList[i]["number"])
				Wallet w = new Wallet();

				w.Name = walletList[i]["name"];
				w.Number = walletList[i]["number"];
				w.AccessLevelStatus =  walletList[i]["access_level_status"];

				Balance b = new Balance();
				b.available_balance =  (walletList[i]["payment_balance"]["available"])
				b.unavailable_balance =  (walletList[i]["payment_balance"]["unavailable"])
				w.PaymentBalance = b;

				b=new Balance();
				b.available_balance = (walletList[i]["cash_balance"]["available"])
				b.unavailable_balance =  (walletList[i]["cash_balance"]["unavailable"])
				w.CashBalance = b;

				TransactionLimits l= new TransactionLimits()
				l.max_balance = (walletList[i]["transaction_limits"]["max_balance"])
				l.topup_credit_limit = (walletList[i]["transaction_limits"]["topup_credit_limit"])
				l.topup_cash_limit = (walletList[i]["transaction_limits"]["topup_cash_limit"])
				l.withdrawal_limit = (walletList[i]["transaction_limits"]["withdrawal_limit"])
				l.payment_limit = (walletList[i]["transaction_limits"]["payment_limit"])
				l.wallet_to_wallet_limit = (walletList[i]["transaction_limits"]["wallet_to_wallet_limit"])
				w.Limits = l;

				w.TotalBalance = (walletList[i]["total_balance"])

				Wallets.add(w);
			}
			println("wallets : "+ new Gson().toJson(Wallets) )

		}


	}

	public void getAccountByAccountNumber(String accountNumber) {
		ResponseObject WalletListResponse = getWalletList(accountNumber);

		WS.verifyGreaterThan(WS.getElementPropertyValue(WalletListResponse, "payload.results").size(),0)

		ArrayList walletList = WS.getElementPropertyValue(WalletListResponse, "payload.results")

		AccountNumber = accountNumber
		AccountType = Enums.AccountType.valueOf(walletList[0]["account_type"].toLowerCase())

		KycInfo.FirstName = walletList[0]["user_kyc_info.first_name"]
		KycInfo.LastName =  walletList[0]["user_kyc_info.last_name"]
		KycInfo.NationalID = walletList[0]["user_kyc_info.national_id"]
		KycInfo.kycLevel = Enums.KycLevel.valueOf( walletList[0]["kyc_level_status"])

		for(int i=0 ; i<walletList.size() ; i++) {

			Wallet w = new Wallet();

			w.Name = walletList[i]["name"];
			w.Number = walletList[i]["number"];
			w.AccessLevelStatus =  walletList[i]["access_level_status"];

			Balance b = new Balance();
			b.available_balance =  (walletList[i]["payment_balance"]["available"])
			b.unavailable_balance =  (walletList[i]["payment_balance"]["unavailable"])
			w.PaymentBalance = b;

			b=new Balance();
			b.available_balance = (walletList[i]["cash_balance"]["available"])
			b.unavailable_balance =  (walletList[i]["cash_balance"]["unavailable"])
			w.CashBalance = b;

			TransactionLimits l= new TransactionLimits()
			l.max_balance = (walletList[i]["transaction_limits"]["max_balance"])
			l.topup_credit_limit = (walletList[i]["transaction_limits"]["topup_credit_limit"])
			l.topup_cash_limit = (walletList[i]["transaction_limits"]["topup_cash_limit"])
			l.withdrawal_limit = (walletList[i]["transaction_limits"]["withdrawal_limit"])
			l.payment_limit = (walletList[i]["transaction_limits"]["payment_limit"])
			l.wallet_to_wallet_limit = (walletList[i]["transaction_limits"]["wallet_to_wallet_limit"])
			w.Limits = l;

			w.TotalBalance = (walletList[i]["total_balance"])

			Wallets.add(w);
		}
		println("wallets : "+ new Gson().toJson(Wallets) )

	}

	public ResponseObject getWalletList(String accountNumber) {
		return  WS.sendRequestAndVerify(findTestObject('Postman/Wallet/ListByFilter', [
			"account_number": "\"" + accountNumber +"\"",
			"page_size": 10,
			"page_index": 1,
			"order_column": "\"Id\"",
			"order_by": "\"asc\""
		]))
	}

}



public class Wallet {
	String Name;
	String Number;
	String AccessLevelStatus;
	Balance PaymentBalance;
	Balance CashBalance;
	TransactionLimits Limits;
	double TotalBalance;

	public Wallet() {
		PaymentBalance = new Balance();
		CashBalance = new Balance();
		Limits = new TransactionLimits();
	}
}

public class Balance{
	double available_balance = 0;
	double unavailable_balance = 0;
}

public class TransactionLimits{
	double max_balance;
	double topup_credit_limit;
	double topup_cash_limit;
	double withdrawal_limit;
	double payment_limit;
	double wallet_to_wallet_limit;
}

public class KycInfo {
	String FirstName;
	String LastName;
	Enums.KycLevel kycLevel;
	String NationalID;
}
