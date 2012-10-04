package eu.ydp.empiria.test.selenium.choice;


public class Choice12AnswerAndPageSwitchTestCase extends ChoiceTestCaseBase {

	public void testAnswerAndPageSwitch1() throws Exception{
		performTestAnswerAndPageSwitch(IN_OPTION_1, IN_OPTION_2, IN_OPTION_3);
	}

	public void testAnswerAndPageSwitch2() throws Exception{
		performTestAnswerAndPageSwitch(IN_OPTION_2, IN_OPTION_1, IN_OPTION_3);
	}

	public void testAnswerAndPageSwitch3() throws Exception{
		performTestAnswerAndPageSwitch(IN_OPTION_3, IN_OPTION_1, IN_OPTION_2);
	}
	
	private void performTestAnswerAndPageSwitch(String selectedOption, String notSelectedOption1, String notSelectedOption2) throws Exception{
		openTestSingle("1");
		waitForPageLoad(0);
		// select answer
		click(IN_MODULE+selectedOption+SINGLE_OPTION_BUTTON);
		// switch pages
		navigateNextPage();
		waitForPageLoad(1);
		navigatePreviousPage();
		waitForPageLoad(0);
		// check whether the module is unlocked
		verifyFalse(selenium.isElementPresent(IN_MODULE+IN_ANYWHERE+SINGLE_OPTION_NOTSELECTED_DISABLED));
		verifyFalse(selenium.isElementPresent(IN_MODULE+IN_ANYWHERE+SINGLE_OPTION_SELECTED_DISABLED));
		// check whether the first option is selected 
		verifyTrue(selenium.isElementPresent(IN_MODULE+selectedOption+SINGLE_OPTION_SELECTED));
		// check whether the other options are not selected
		verifyFalse(selenium.isElementPresent(IN_MODULE+notSelectedOption1+SINGLE_OPTION_SELECTED));
		verifyFalse(selenium.isElementPresent(IN_MODULE+notSelectedOption2+SINGLE_OPTION_SELECTED));
	}


}
