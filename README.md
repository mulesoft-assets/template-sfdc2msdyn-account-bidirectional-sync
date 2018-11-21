
# Anypoint Template: Salesforce and MS Dynamics Account bidirectional sync	

<!-- Header (start) -->

<!-- Header (end) -->

# License Agreement
This template is subject to the conditions of the <a href="https://s3.amazonaws.com/templates-examples/AnypointTemplateLicense.pdf">MuleSoft License Agreement</a>. Review the terms of the license before downloading and using this template. You can use this template for free with the Mule Enterprise Edition, CloudHub, or as a trial in Anypoint Studio. 
# Use Case
<!-- Use Case (start) -->
As an admin, I want to have my accounts synchronized between two different systems - Salesforce and MS Dynamics.

**Template overview** 

Let's say we want to keep Accounts synchronized between a Salesforce instance and a MS Dynamics CRM instance. Then, the integration behavior can be summarized just in the following steps:

1. Ask Salesforce:
> *What changes have there been since the last time I got in touch with you?*

2. For each of the updates fetched in the previous step (1.), ask MS Dynamics:
> *Does the update received from Salesforce should be applied?*

3. If MS Dynamics answers for the previous question (2.) is *Yes*, then *upsert* (create or update depending each particular case) MS Dynamics with the belonging change.

4. Repeat previous steps (1. to 3.) the other way around (using MS Dynamics as source instance and Salesforce as the target one)

 Repeat *ad infinitum*:

5. Ask Salesforce:
> *What changes have there been since the question I've made in the step 1.?*

And so on...
  
  
The question for recent changes since a certain moment in nothing but a [Scheduler](https://docs.mulesoft.com/mule4-user-guide/v/4.1/scheduler-concept) with a watermark defined.
<!-- Use Case (end) -->

# Considerations
<!-- Default Considerations (start) -->

<!-- Default Considerations (end) -->

<!-- Considerations (start) -->
To make this Anypoint Template run, there are certain preconditions that must be considered. All of them deal with the preparations in both, that must be made in order for all to run smoothly.

**Failing to do so could lead to unexpected behavior of the template.**

**Note:** You need to install Java Cryptography Extensions to be able to connect to MS Dynamics. Please [choose](http://www.oracle.com/technetwork/java/javase/downloads/index.html) a relevant version according to your Java installation.
<!-- Considerations (end) -->



## Salesforce Considerations

Here's what you need to know about Salesforce to get this template to work:

- Where can I check that the field configuration for my Salesforce instance is the right one? See: <a href="https://help.salesforce.com/HTViewHelpDoc?id=checking_field_accessibility_for_a_particular_field.htm&language=en_US">Salesforce: Checking Field Accessibility for a Particular Field</a>.
- Can I modify the Field Access Settings? How? See: <a href="https://help.salesforce.com/HTViewHelpDoc?id=modifying_field_access_settings.htm&language=en_US">Salesforce: Modifying Field Access Settings</a>.

### As a Data Source

If the user who configured the template for the source system does not have at least *read only* permissions for the fields that are fetched, then an *InvalidFieldFault* API fault displays.

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

### As a Data Source

In order for this Anypoint Template to work, a custom field **new_salesforceid** has to be defined for Accounts. Please find more information [here](https://technet.microsoft.com/en-us/library/dn531187.aspx).

There are no other particular considerations for this Anypoint Template regarding Microsoft Dynamics CRM as data origin.
### As a Data Destination

In order for this Anypoint Template to work, a custom field **new_salesforceid** has to be defined for Accounts. Please find more information [here](https://technet.microsoft.com/en-us/library/dn531187.aspx).

There are no other particular considerations for this Anypoint Template regarding Microsoft Dynamics CRM as data destination.



# Run it!
Simple steps to get this template running.
<!-- Run it (start) -->
See below.
<!-- Run it (end) -->

## Running On Premises
In this section we help you run this template on your computer.
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

+ Locate the properties file `mule.dev.properties`, in src/main/resources.
+ Complete all the properties required as per the examples in the "Properties to Configure" section.
+ Right click the template project folder.
+ Hover your mouse over `Run as`.
+ Click `Mule Application (configure)`.
+ Inside the dialog, select Environment and set the variable `mule.env` to the value `dev`.
+ Click `Run`.
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
**Application configuration**
 
+ scheduler.frequency `10000`
+ scheduler.startDelay 0 
These are the miliseconds (also different time units can be used) that will run between two different checks for updates in Salesforce and MS Dynamics

**SalesForce Connector configuration for company A**

+ sfdc.username `salesforce.user@mail.com`
+ sfdc.password `salesforcePass`
+ sfdc.securityToken `wJFJAf6lw3vH86bDLWSjpfJC`
+ sfdc.integration.user.id `00520000003LtvGAAS`

	**Note:** To find out the correct *sfdc.integration.user.id* value, please, refer to example project **Salesforce Data Retrieval** in [Anypoint Exchange](http://www.mulesoft.org/documentation/display/current/Anypoint+Exchange).

+ sfdc.watermark.default.expression `2015-04-01T19:40:27.000Z`

**MS Dynamics Connector configuration for company B**

+ msdyn.authenticationRetries `3`
+ msdyn.username `msDynamicsUser@@yourOrg.onmicrosoft.com`
+ msdyn.password `msDynamicsPass`
+ msdyn.url `https://htesting.api.crm4.dynamics.com/XRMServices/2011/Organization.svc`
+ msdyn.watermark.default.expression `2015-04-01T19:40:27Z`
+ msdyn.integration.user.id `534679675`
<!-- Application Configuration (end) -->

# API Calls
<!-- API Calls (start) -->
Salesforce imposes limits on the number of API Calls that can be made. Therefore calculating this amount may be an important factor to consider. The template calls to the API can be calculated using the formula:

***1 + X + X / 200***

Being ***X*** the number of Accounts to be synchronized on each run. 

The division by ***200*** is because, by default, Accounts are gathered in groups of 200 for each Upsert API Call in the commit step. Also consider that this calls are executed repeatedly every polling cycle.	

For instance if 10 records are fetched from origin instance, then 12 api calls will be made (1 + 10 + 1).
<!-- API Calls (end) -->

# Customize It!
This brief guide provides a high level understanding of how this template is built and how you can change it according to your needs. As Mule applications are based on XML files, this page describes the XML files used with this template. More files are available such as test classes and Mule application files, but to keep it simple, we focus on these XML files:

* config.xml
* businessLogic.xml
* endpoints.xml
* errorHandling.xml<!-- Customize it (start) -->

<!-- Customize it (end) -->

## config.xml
<!-- Default Config XML (start) -->
This file provides the configuration for connectors and configuration properties. Only change this file to make core changes to the connector processing logic. Otherwise, all parameters that can be modified should instead be in a properties file, which is the recommended place to make changes.<!-- Default Config XML (end) -->

<!-- Config XML (start) -->

<!-- Config XML (end) -->

## businessLogic.xml
<!-- Default Business Logic XML (start) -->
Functional aspect of the Template is implemented in this XML, directed by one flow responsible of excecuting the logic.
For the purpose of this particular Template there are two [Batch Jobs](http://www.mulesoft.org/documentation/display/current/Batch+Processing). which handles all the logic of it. 
The first *fromSalesforceBatch* batch job is called for synchranization of Accounts from Salesforce to MS Dynamics. 
If the Account already exists in MS Dynamics, the last modified date are compared and according to the result, the Account is updated or not. 
On the other hand, if the Account does not exist, it is created.
The second *fromDynamicsCrmBatch* batch job works in the same way, but in the opposite direction.<!-- Default Business Logic XML (end) -->

<!-- Business Logic XML (start) -->

<!-- Business Logic XML (end) -->

## endpoints.xml
<!-- Default Endpoints XML (start) -->
This is the file where you will found the inbound and outbound sides of your integration app. These flows has Error handling that basically consists on invoking the *On error propagate* defined in *errorHandling.xml* file.
It is intented to define the application API.<!-- Default Endpoints XML (end) -->

<!-- Endpoints XML (start) -->

<!-- Endpoints XML (end) -->

## errorHandling.xml
<!-- Default Error Handling XML (start) -->
This file handles how your integration reacts depending on the different exceptions. This file provides error handling that is referenced by the main flow in the business logic.<!-- Default Error Handling XML (end) -->

<!-- Error Handling XML (start) -->

<!-- Error Handling XML (end) -->

<!-- Extras (start) -->

<!-- Extras (end) -->
