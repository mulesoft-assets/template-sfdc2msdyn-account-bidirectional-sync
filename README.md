
# Anypoint Template: Salesforce and MS Dynamics Account bidirectional sync	

<!-- Header (start) -->
Synchronizes account data between Salesforce and Microsoft Dynamics CRM in both directions. This template makes it fast to configure the fields to synchronize, how they map, and criteria on when to trigger the synchronization. 

This template can be triggered either using a polling mechanism or can be easily modified to work with Salesforce outbound messaging to make efficient Salesforce API calls. This template leverages watermarking functionality to make sure only the latest modified changes are synchronized. It also uses the batch functionality to effectively process many records at a time.

![49268e92-5a17-4645-bbdc-61bbcb135522-image.png](https://exchange2-file-upload-service-kprod.s3.us-east-1.amazonaws.com:443/49268e92-5a17-4645-bbdc-61bbcb135522-image.png)

![f379d05d-022f-4181-a3f1-28e707d21110-image.png](https://exchange2-file-upload-service-kprod.s3.us-east-1.amazonaws.com:443/f379d05d-022f-4181-a3f1-28e707d21110-image.png)
<!-- Header (end) -->

# License Agreement
This template is subject to the conditions of the <a href="https://s3.amazonaws.com/templates-examples/AnypointTemplateLicense.pdf">MuleSoft License Agreement</a>. Review the terms of the license before downloading and using this template. You can use this template for free with the Mule Enterprise Edition, CloudHub, or as a trial in Anypoint Studio. 
# Use Case
<!-- Use Case (start) -->
As an admin, I want to have my accounts synchronized between two different systems - Salesforce and MS Dynamics CRM.

## Template Overview

Let's say we want to keep accounts synchronized between a Salesforce instance and an MS Dynamics CRM instance. 

The integration behavior can be summarized as follows:

1. Ask Salesforce:
> What changes have there been since the last time we communicated?

2. For each of the updates fetched in the previous step (1), ask MS Dynamics:
> Should I apply the update received from Salesforce?

3. If MS Dynamics answers *Yes* to the previous question (2), then *upsert* (create or update depending on each particular case) MS Dynamics with the change.

4. Repeat previous steps (1 to 3) the other way around (using MS Dynamics as source instance and Salesforce as the target one).

 Repeat *ad infinitum*:

5. Ask Salesforce:
> What changes have been since the step 1?

And so on...
  
The question for recent changes since a certain moment in nothing but a [Scheduler](https://docs.mulesoft.com/mule4-user-guide/v/4.1/scheduler-concept) with a watermark defined.
<!-- Use Case (end) -->

# Considerations
<!-- Default Considerations (start) --><!-- Default Considerations (end) --><!-- Considerations (start) -->
To make this template run, there are certain preconditions that must be considered. All of them deal with the preparations in both, that must be made for the template to run smoothly. Failing to do so could lead to unexpected behavior of the template.

**Note:** You need to install Java Cryptography Extensions to connect to MS Dynamics. [Choose](http://www.oracle.com/technetwork/java/javase/downloads/index.html) a relevant version according to your Java installation.
<!-- Considerations (end) -->



## Salesforce Considerations

Here's what you need to know about Salesforce to get this template to work:

- Where can I check that the field configuration for my Salesforce instance is the right one? See: <a href="https://help.salesforce.com/HTViewHelpDoc?id=checking_field_accessibility_for_a_particular_field.htm&language=en_US">Salesforce: Checking Field Accessibility for a Particular Field</a>.
- Can I modify the Field Access Settings? How? See: <a href="https://help.salesforce.com/HTViewHelpDoc?id=modifying_field_access_settings.htm&language=en_US">Salesforce: Modifying Field Access Settings</a>.

### As a Data Source

If the user who configured the template for the source system does not have at least *read only* permissions for the fields that are fetched, an *InvalidFieldFault* API fault displays.

```
java.lang.RuntimeException: [InvalidFieldFault [ApiQueryFault 
[ApiFault  exceptionCode='INVALID_FIELD'
exceptionMessage='Account.Phone, Account.Rating, Account.RecordTypeId, 
Account.ShippingCity
^
ERROR at Row:1:Column:486
No such column 'RecordTypeId' on entity 'Account'. If you are attempting to 
use a custom field, be sure to append the '__c' after the custom field name. 
Reference your WSDL or the describe call for the appropriate names.'
]
row='1'
column='486'
]
]
```

### As a Data Destination

There are no considerations with using Salesforce as a data destination.

## Microsoft Dynamics CRM Considerations

### As a Data Source or Data Destination

For this template to work, a custom field **new_salesforceid** has to be defined for accounts. See [Microsoft's Create and edit fields](https://technet.microsoft.com/en-us/library/dn531187.aspx) for information on adding this field.

There are no other particular considerations for this template regarding Microsoft Dynamics CRM as a data origin or data destination.

# Run it!
Simple steps to get this template running.
<!-- Run it (start) -->
See below.
<!-- Run it (end) -->

## Running On Premises
Use this section to run this template on your computer.
<!-- Running on premise (start) -->

<!-- Running on premise (end) -->

### Where to Download Anypoint Studio and the Mule Runtime
If you are new to Mule, download this software:

+ [Download Anypoint Studio](https://www.mulesoft.com/platform/studio)
+ [Download Mule runtime](https://www.mulesoft.com/lp/dl/mule-esb-enterprise)

**Note:** Anypoint Studio requires JDK 8.
<!-- Where to download (start) -->

<!-- Where to download (end) -->

### Importing a Template into Studio
In Studio, click the Exchange X icon in the upper left of the taskbar, log in with your Anypoint Platform credentials, search for the template, and click Open.
<!-- Importing into Studio (start) -->

<!-- Importing into Studio (end) -->

### Running on Studio
After you import your template into Anypoint Studio, follow these steps to run it:

1. Locate the properties file `mule.dev.properties`, in src/main/resources.
2. Complete all the properties required as per the examples in the "Properties to Configure" section.
3. Right click the template project folder.
4. Hover your mouse over `Run as`.
5. Click `Mule Application (configure)`.
6. Inside the dialog, select Environment and set the variable `mule.env` to the value `dev`.
7. Click `Run`.
<!-- Running on Studio (start) -->

<!-- Running on Studio (end) -->

### Running on Mule Standalone
Update the properties in one of the property files, for example in mule.prod.properties, and run your app with a corresponding environment variable. In this example, use `mule.env=prod`. 


## Running on CloudHub
When creating your application in CloudHub, go to Runtime Manager > Manage Application > Properties to set the environment variables listed in "Properties to Configure" as well as the mule.env value.
<!-- Running on Cloudhub (start) -->

<!-- Running on Cloudhub (end) -->

### Deploying a Template in CloudHub
In Studio, right click your project name in Package Explorer and select Anypoint Platform > Deploy on CloudHub.
<!-- Deploying on Cloudhub (start) -->

<!-- Deploying on Cloudhub (end) -->

## Properties to Configure
To use this template, configure properties such as credentials, configurations, etc.) in the properties file or in CloudHub from Runtime Manager > Manage Application > Properties. The sections that follow list example values.
### Application Configuration
<!-- Application Configuration (start) -->
**Application Configuration**
 
+ scheduler.frequency `10000`
+ scheduler.startDelay 0 
The number of milliseconds (also different time units can be used) between checks for updates in Salesforce and MS Dynamics.

**SalesForce Connector Configuration for Company A**

+ sfdc.username `salesforce.user@mail.com`
+ sfdc.password `salesforcePass`
+ sfdc.securityToken `wJFJAf6lw3vH86bDLWSjpfJC`
+ sfdc.integration.user.id `00520000003LtvGAAS`

	**Note:** To find the correct *sfdc.integration.user.id* value, see [Salesforce Data Retrieval](https://www.mulesoft.com/exchange/org.mule.examples/salesforce-data-retrieval/).

+ sfdc.watermark.default.expression `2019-04-01T19:40:27.000Z`

**MS Dynamics Connector Configuration for Company B**

+ msdyn.authenticationRetries `3`
+ msdyn.username `msDynamicsUser@@yourOrg.onmicrosoft.com`
+ msdyn.password `msDynamicsPass`
+ msdyn.url `https://htesting.api.crm4.dynamics.com/XRMServices/2011/Organization.svc`
+ msdyn.watermark.default.expression `2019-04-01T19:40:27Z`
+ msdyn.integration.user.id `534679675`
<!-- Application Configuration (end) -->

# API Calls
<!-- API Calls (start) -->
Salesforce imposes limits on the number of API calls that can be made. Therefore calculating this amount may be an important factor to consider. 

The template calls to the API can be calculated using the formula:

* ***1 + X + X / 200*** -- Where ***X*** is the number of accounts to synchronize on each run. 

* Divide by ***200*** because by default accounts are gathered in groups of 200 for each Upsert API call in the commit step. Also consider that this calls are executed repeatedly every polling cycle.	

For instance if 10 records are fetched from origin instance, then 12 API calls are made (1 + 10 + 1).
<!-- API Calls (end) -->

# Customize It!
This brief guide provides a high level understanding of how this template is built and how you can change it according to your needs. As Mule applications are based on XML files, this page describes the XML files used with this template. More files are available such as test classes and Mule application files, but to keep it simple, we focus on these XML files:

* config.xml
* businessLogic.xml
* endpoints.xml
* errorHandling.xml

<!-- Customize it (start) -->
<!-- Customize it (end) -->

## config.xml
<!-- Default Config XML (start) -->
This file provides the configuration for connectors and configuration properties. Only change this file to make core changes to the connector processing logic. Otherwise, all parameters that can be modified should instead be in a properties file, which is the recommended place to make changes.
<!-- Default Config XML (end) -->

<!-- Config XML (start) -->

<!-- Config XML (end) -->

## businessLogic.xml
<!-- Default Business Logic XML (start) -->
Functional aspect of the Template is implemented in this XML, directed by one flow responsible of excecuting the logic. For the purpose of this particular Template there are two batch jobs, which handles all the logic of the template. The first *fromSalesforceBatch* batch job is called for synchronization of accounts from Salesforce to MS Dynamics. If the account already exists in MS Dynamics, the last modified date is compared and according to the result, the account is updated or not. On the other hand, if the account does not exist, it is created. The second *fromDynamicsCrmBatch* batch job works in the same way, but in the opposite direction.
<!-- Default Business Logic XML (end) -->

<!-- Business Logic XML (start) -->

<!-- Business Logic XML (end) -->

## endpoints.xml
<!-- Default Endpoints XML (start) -->
This file provides the inbound and outbound sides of your integration app. These flows have error handling consisting of invoking the *On error propagate* defined in *errorHandling.xml* file. This file defines the application API.
<!-- Default Endpoints XML (end) -->

<!-- Endpoints XML (start) -->

<!-- Endpoints XML (end) -->

## errorHandling.xml
<!-- Default Error Handling XML (start) -->
This file handles how your integration reacts depending on the different exceptions. This file provides error handling that is referenced by the main flow in the business logic.
<!-- Default Error Handling XML (end) -->

<!-- Error Handling XML (start) -->

<!-- Error Handling XML (end) -->

<!-- Extras (start) -->

<!-- Extras (end) -->
