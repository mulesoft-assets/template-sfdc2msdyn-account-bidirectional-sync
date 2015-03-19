/**
 * Mule Anypoint Template
 * Copyright (c) MuleSoft, Inc.
 * All rights reserved.  http://www.mulesoft.com
 */

package org.mule.templates.integration;

import static org.mule.templates.builders.ObjectBuilder.anAccount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mule.MessageExchangePattern;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.processor.chain.InterceptingChainLifecycleWrapper;
import org.mule.processor.chain.SubflowInterceptingChainLifecycleWrapper;
import org.mule.templates.AbstractTemplatesTestCase;
import org.mule.templates.builders.ObjectBuilder;

import com.mulesoft.module.batch.BatchTestHelper;
import com.sforce.soap.partner.SaveResult;

/**
 * The objective of this class is validating the correct behavior of the flows
 * for this Mule Anypoint Template
 * 
 */
@SuppressWarnings("unchecked")
public class BidirectionalAccountSyncIT extends AbstractTemplatesTestCase {

	private static final String ANYPOINT_TEMPLATE_NAME = "sfdc2msdyn-account-bidirectional-sync";
	private static final String A_INBOUND_FLOW_NAME = "triggerSyncFromSalesforceFlow";
	private static final String B_INBOUND_FLOW_NAME = "triggerSyncFromDynamicsCrmFlow";
	private static final int TIMEOUT_MILLIS = 100;
  
	private static List<String> accountsCreatedInA = new ArrayList<String>();
	private static List<String> accountsCreatedInB = new ArrayList<String>();
	private static SubflowInterceptingChainLifecycleWrapper deleteAccountFromAFlow;
	private static SubflowInterceptingChainLifecycleWrapper deleteAccountFromBFlow;
	

	private SubflowInterceptingChainLifecycleWrapper createAccountInAFlow;
	private SubflowInterceptingChainLifecycleWrapper createAccountInBFlow;
	private InterceptingChainLifecycleWrapper queryAccountFromAFlow;
	private InterceptingChainLifecycleWrapper queryAccountFromBFlow;
	private BatchTestHelper batchTestHelper;

	@BeforeClass
	public static void beforeTestClass() {
		System.setProperty("page.size", "1000");

		// Set polling frequency to 10 seconds
		System.setProperty("polling.frequency", "10000");

		// Set default water-mark expression to current time
		System.clearProperty("sfdc.watermark.default.expression");
		DateTime now = new DateTime(DateTimeZone.UTC);
		DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		System.setProperty("sfdc.watermark.default.expression",	now.toString(dateFormat));
		
		System.clearProperty("msdyn.watermark.default.expression");
		System.setProperty("msdyn.watermark.default.expression", now.toString(dateFormat));		
	}

	@Before
	public void setUp() throws MuleException {
		stopAutomaticPollTriggering();
		getAndInitializeFlows();
		
		batchTestHelper = new BatchTestHelper(muleContext);
	}

	@After
	public void tearDown() throws MuleException, Exception {
		cleanUpSandboxesByRemovingTestAccounts();
	}

	private void stopAutomaticPollTriggering() throws MuleException {
		stopFlowSchedulers(A_INBOUND_FLOW_NAME);
		stopFlowSchedulers(B_INBOUND_FLOW_NAME);
	}

	private void getAndInitializeFlows() throws InitialisationException {
		// Flow for creating accounts in sfdc A instance
		createAccountInAFlow = getSubFlow("createAccountInAFlow");
		createAccountInAFlow.initialise();

		// Flow for creating accounts in sfdc B instance
		createAccountInBFlow = getSubFlow("createAccountInBFlow");
		createAccountInBFlow.initialise();

		// Flow for deleting accounts in sfdc A instance
		deleteAccountFromAFlow = getSubFlow("deleteAccountFromAFlow");
		deleteAccountFromAFlow.initialise();

		// Flow for deleting accounts in sfdc B instance
		deleteAccountFromBFlow = getSubFlow("deleteAccountFromBFlow");
		deleteAccountFromBFlow.initialise();

		// Flow for querying the account in A instance
		queryAccountFromAFlow = getSubFlow("queryAccountFromAFlow");
		queryAccountFromAFlow.initialise();

		// Flow for querying the account in B instance
		queryAccountFromBFlow = getSubFlow("queryAccountFromBFlow");
		queryAccountFromBFlow.initialise();
	}

	private static void cleanUpSandboxesByRemovingTestAccounts()
			throws MuleException, Exception {
		final List<String> idList = new ArrayList<String>();
		for (String account : accountsCreatedInA) {
			idList.add(account);
		}
		deleteAccountFromAFlow.process(getTestEvent(idList,	MessageExchangePattern.REQUEST_RESPONSE));
		idList.clear();
		for (String account : accountsCreatedInB) {
			idList.add(account);
		}
		deleteAccountFromBFlow.process(getTestEvent(idList,	MessageExchangePattern.REQUEST_RESPONSE));
	}
	
	@Test
	public void whenUpdatingAnAccountInInstanceBTheBelongingAccountGetsUpdatedInInstanceA()
			throws MuleException, Exception {

		// Build test accounts
		final String accountName = ANYPOINT_TEMPLATE_NAME + "-" + System.currentTimeMillis() + "Account";
		ObjectBuilder accountInA = anAccount()
				.with("Name", accountName)
				.with("Phone", "123456789")
				.with("Description", "Old description");
		
		ObjectBuilder accountInB = anAccount()
				.with("name", accountName)
				.with("telephone1", "123456789")
				.with("description", "Some nice description");

		// Create accounts in sand-boxes and keep track of them for posterior
		// cleaning up
		accountsCreatedInA.add(createTestAccountsInSfdcSandbox(accountInA.build(), createAccountInAFlow));

        // make a creation time gap between two accounts
        Thread.sleep(5000);

		accountsCreatedInB.add(createTestAccountsInMsDynSandbox(accountInB.build(), createAccountInBFlow));

		// Execution
		executeWaitAndAssertBatchJob(B_INBOUND_FLOW_NAME);

		// Assertions
		Map<String, String> retrievedAccountFromA = (Map<String, String>) queryAccount(accountInA.build(), queryAccountFromAFlow);
		Map<String, String> retrievedAccountFromB = (Map<String, String>) queryAccount(accountInB.build(), queryAccountFromBFlow);
		

		retrievedAccountFromA.remove("Id");
		retrievedAccountFromB.remove("accountid");
		
		Assert.assertEquals("Names of accounts are not synchronized between systems.", retrievedAccountFromA.get("Name"), retrievedAccountFromB.get("name"));
		Assert.assertEquals("Phones of accounts are not synchronized between systems.", retrievedAccountFromA.get("Phone"), retrievedAccountFromB.get("telephone1"));
		Assert.assertEquals("Descriptions of accounts are not synchronized between systems.", retrievedAccountFromA.get("Description"), retrievedAccountFromB.get("description"));
		
}

	private Object queryAccount(Map<String, Object> account,
			InterceptingChainLifecycleWrapper queryAccountFlow)
			throws MuleException, Exception {
		return queryAccountFlow.process(getTestEvent(account, MessageExchangePattern.REQUEST_RESPONSE)).getMessage().getPayload();
	}

	private String createTestAccountsInSfdcSandbox(Map<String, Object> account,
			InterceptingChainLifecycleWrapper createAccountFlow)
			throws MuleException, Exception {
		List<Map<String, Object>> salesforceAccounts = new ArrayList<Map<String, Object>>();
		salesforceAccounts.add(account);

		final List<SaveResult> payloadAfterExecution = (List<SaveResult>) createAccountFlow.process(getTestEvent(salesforceAccounts, MessageExchangePattern.REQUEST_RESPONSE)).getMessage().getPayload();
		return payloadAfterExecution.get(0).getId();
	}
	
	private String createTestAccountsInMsDynSandbox(Map<String, Object> account,
			InterceptingChainLifecycleWrapper createAccountFlow)
			throws MuleException, Exception {
		List<Map<String, Object>> msDynAccounts = new ArrayList<Map<String, Object>>();
		msDynAccounts.add(account);

		final MuleEvent event = createAccountFlow.process(getTestEvent(msDynAccounts, MessageExchangePattern.REQUEST_RESPONSE));
		HashMap<?,?> msDynAccountMap = (HashMap<?,?>) ((Iterator<?>) event.getMessage().getPayload()).next();
		
		return (String) msDynAccountMap.get("accountid");
	}

	private void executeWaitAndAssertBatchJob(String flowConstructName)
			throws Exception {

		// Execute synchronization
		runSchedulersOnce(flowConstructName);

		// Wait for the batch job execution to finish
		batchTestHelper.awaitJobTermination(TIMEOUT_MILLIS * 1000, 500);
		batchTestHelper.assertJobWasSuccessful();
	}

}
