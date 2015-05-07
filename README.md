
# Anypoint Template: Salesforce to MS Dynamics Account bidirectional sync

+ [License Agreement](#licenseagreement)
+ [Use Case](#usecase)
+ [Considerations](#considerations)
	* [Salesforce Considerations](#salesforceconsiderations)
	* [Microsoft Dynamics CRM Considerations](#msdynamicsconsiderations)
+ [Run it!](#runit)
	* [Running on premise](#runonopremise)
	* [Running on Studio](#runonstudio)
	* [Running on Mule ESB stand alone](#runonmuleesbstandalone)
	* [Running on CloudHub](#runoncloudhub)
	* [Deploying your Anypoint Template on CloudHub](#deployingyouranypointtemplateoncloudhub)
	* [Properties to be configured (With examples)](#propertiestobeconfigured)
+ [API Calls](#apicalls)
+ [Customize It!](#customizeit)
	* [config.xml](#configxml)
	* [businessLogic.xml](#businesslogicxml)
	* [endpoints.xml](#endpointsxml)
	* [errorHandling.xml](#errorhandlingxml)


# License Agreement <a name="licenseagreement"/>
Note that using this template is subject to the conditions of this [License Agreement](AnypointTemplateLicense.pdf).
Please review the terms of the license before downloading and using this template. In short, you are allowed to use the template for free with Mule ESB Enterprise Edition, CloudHub, or as a trial in Anypoint Studio.

# Use Case <a name="usecase"/>
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
  
  
The question for recent changes since a certain moment in nothing but a [poll inbound](http://www.mulesoft.org/documentation/display/current/Poll+Reference) with a watermark defined.

# Considerations <a name="considerations"/>

To make this Anypoint Template run, there are certain preconditions that must be considered. All of them deal with the preparations in both, that must be made in order for all to run smoothly.

**Failling to do so could lead to unexpected behavior of the template.**

**Note:** You need to install Java Cryptography Extensions to be able to connect to MS Dynamics. Please [choose](http://www.oracle.com/technetwork/java/javase/downloads/index.html) a relevant version according to your Java installation.



## Salesforce Considerations <a name="salesforceconsiderations"/>

There may be a few things that you need to know regarding Salesforce, in order for this template to work.

In order to have this template working as expected, you should be aware of your own Salesforce field configuration.

###FAQ

 - Where can I check that the field configuration for my Salesforce instance is the right one?

    [Salesforce: Checking Field Accessibility for a Particular Field][1]

- Can I modify the Field Access Settings? How?

    [Salesforce: Modifying Field Access Settings][2]


[1]: https://help.salesforce.com/HTViewHelpDoc?id=checking_field_accessibility_for_a_particular_field.htm&language=en_US
[2]: https://help.salesforce.com/HTViewHelpDoc?id=modifying_field_access_settings.htm&language=en_US

### As source of data

If the user configured in the template for the source system does not have at least *read only* permissions for the fields that are fetched, then a *InvalidFieldFault* API fault will show up.

```
java.lang.RuntimeException: [InvalidFieldFault [ApiQueryFault [ApiFault  exceptionCode='INVALID_FIELD'
exceptionMessage='
Account.Phone, Account.Rating, Account.RecordTypeId, Account.ShippingCity
^
ERROR at Row:1:Column:486
No such column 'RecordTypeId' on entity 'Account'. If you are attempting to use a custom field, be sure to append the '__c' after the custom field name. Please reference your WSDL or the describe call for the appropriate names.'
]
row='1'
column='486'
]
]
```

### As destination of data

There are no particular considerations for this Anypoint Template regarding Salesforce as data destination.





## Microsoft Dynamics CRM Considerations <a name="msdynamicsconsiderations"/>

### As source of data

In order for this Anypoint Template to work, a custom field **new_salesforceid** has to be defined for Accounts. Please find more information [here](https://technet.microsoft.com/en-us/library/dn531187.aspx).

There are no other particular considerations for this Anypoint Template regarding Microsoft Dynamics CRM as data origin.
### As destination of data

In order for this Anypoint Template to work, a custom field **new_salesforceid** has to be defined for Accounts. Please find more information [here](https://technet.microsoft.com/en-us/library/dn531187.aspx).

There are no other particular considerations for this Anypoint Template regarding Microsoft Dynamics CRM as data destination.


# Run it! <a name="runit"/>
Simple steps to get Salesforce to MS Dynamics Account bidirectional sync running.
See below.

## Running on premise <a name="runonopremise"/>
In this section we detail the way you should run your Anypoint Template on your computer.


### Where to Download Mule Studio and Mule ESB
First thing to know if you are a newcomer to Mule is where to get the tools.

+ You can download Mule Studio from this [Location](http://www.mulesoft.com/platform/mule-studio)
+ You can download Mule ESB from this [Location](http://www.mulesoft.com/platform/soa/mule-esb-open-source-esb)


### Importing an Anypoint Template into Studio
Mule Studio offers several ways to import a project into the workspace, for instance: 

+ Anypoint Studio generated Deployable Archive (.zip)
+ Anypoint Studio Project from External Location
+ Maven-based Mule Project from pom.xml
+ Mule ESB Configuration XML from External Location

You can find a detailed description on how to do so in this [Documentation Page](http://www.mulesoft.org/documentation/display/current/Importing+and+Exporting+in+Studio).


### Running on Studio <a name="runonstudio"/>
Once you have imported you Anypoint Template into Anypoint Studio you need to follow these steps to run it:

+ Locate the properties file `mule.dev.properties`, in src/main/resources
+ Complete all the properties required as per the examples in the section [Properties to be configured](#propertiestobeconfigured)
+ Once that is done, right click on you Anypoint Template project folder 
+ Hover you mouse over `"Run as"`
+ Click on  `"Mule Application"`


### Running on Mule ESB stand alone <a name="runonmuleesbstandalone"/>
Complete all properties in one of the property files, for example in [mule.prod.properties] (../master/src/main/resources/mule.prod.properties) and run your app with the corresponding environment variable to use it. To follow the example, this will be `mule.env=prod`. 


## Running on CloudHub <a name="runoncloudhub"/>
While [creating your application on CloudHub](http://www.mulesoft.org/documentation/display/current/Hello+World+on+CloudHub) (Or you can do it later as a next step), you need to go to Deployment > Advanced to set all environment variables detailed in **Properties to be configured** as well as the **mule.env**.


### Deploying your Anypoint Template on CloudHub <a name="deployingyouranypointtemplateoncloudhub"/>
Mule Studio provides you with really easy way to deploy your Template directly to CloudHub, for the specific steps to do so please check this [link](http://www.mulesoft.org/documentation/display/current/Deploying+Mule+Applications#DeployingMuleApplications-DeploytoCloudHub)


## Properties to be configured (With examples) <a name="propertiestobeconfigured"/>
In order to use this Mule Anypoint Template you need to configure properties (Credentials, configurations, etc.) either in properties file or in CloudHub as Environment Variables. Detail list with examples:
### Application configuration
**Application configuration**
+ polling.frequency `10000`  
These are the miliseconds (also different time units can be used) that will run between two different checks for updates in Salesforce and MS Dynamics

**SalesForce Connector configuration for company A**
+ sfdc.username `salesforce.user@mail.com`
+ sfdc.password `salesforcePass`
+ sfdc.securityToken `wJFJAf6lw3vH86bDLWSjpfJC`
+ sfdc.url `https://login.salesforce.com/services/Soap/u/32.0`
+ sfdc.integration.user.id `00520000003LtvGAAS`
+ sfdc.watermark.default.expression `2015-04-01T19:40:27.000Z`

**MS Dynamics Connector configuration for company B**
+ dynamicscrm.username `msDynamicsUser@@yourOrg.onmicrosoft.com`
+ dynamicscrm.password `msDynamicsPass`
+ dynamicscrm.url `https://htesting.api.crm4.dynamics.com/XRMServices/2011/Organization.svc`
+ dynamicscrm.watermark.default.expression `2015-04-01T19:40:27Z`
+ dynamicscrm.integration.user.id `534679675`
+ dynamicscrm.integration.ownerid `534679675`

# API Calls <a name="apicalls"/>
Salesforce imposes limits on the number of API Calls that can be made. Therefore calculating this amount may be an important factor to consider. The template calls to the API can be calculated using the formula:

***1 + X + X / 200***

Being ***X*** the number of Accounts to be synchronized on each run. 

The division by ***200*** is because, by default, Accounts are gathered in groups of 200 for each Upsert API Call in the commit step. Also consider that this calls are executed repeatedly every polling cycle.	

For instance if 10 records are fetched from origin instance, then 12 api calls will be made (1 + 10 + 1).


# Customize It!<a name="customizeit"/>
This brief guide intends to give a high level idea of how this Anypoint Template is built and how you can change it according to your needs.
As mule applications are based on XML files, this page will be organized by describing all the XML that conform the Anypoint Template.
Of course more files will be found such as Test Classes and [Mule Application Files](http://www.mulesoft.org/documentation/display/current/Application+Format), but to keep it simple we will focus on the XMLs.

Here is a list of the main XML files you'll find in this application:

* [config.xml](#configxml)
* [endpoints.xml](#endpointsxml)
* [businessLogic.xml](#businesslogicxml)
* [errorHandling.xml](#errorhandlingxml)


## config.xml<a name="configxml"/>
Configuration for Connectors and [Properties Place Holders](http://www.mulesoft.org/documentation/display/current/Configuring+Properties) are set in this file. **Even you can change the configuration here, all parameters that can be modified here are in properties file, and this is the recommended place to do it so.** Of course if you want to do core changes to the logic you will probably need to modify this file.

In the visual editor they can be found on the *Global Element* tab.


## businessLogic.xml<a name="businesslogicxml"/>
Functional aspect of the Template is implemented in this XML, directed by one flow responsible of excecuting the logic.
For the purpose of this particular Template there are two [Batch Jobs](http://www.mulesoft.org/documentation/display/current/Batch+Processing). which handles all the logic of it. 
The first *fromSalesforceBatch* batch job is called for synchranization of Accounts from Salesforce to MS Dynamics. 
If the Account already exists in MS Dynamics, the last modified date are compared and according to the result, the Account is updated or not. 
On the other hand, if the Account does not exist, it is created.
The second *fromDynamicsCrmBatch* batch job works in the same way, but in the opposite direction.



## endpoints.xml<a name="endpointsxml"/>
This is the file where you will found the inbound and outbound sides of your integration app. These flows has Exception Strategy that basically consists on invoking the *defaultChoiseExceptionStrategy* defined in *errorHandling.xml* file.
It is intented to define the application API.



## errorHandling.xml<a name="errorhandlingxml"/>
This is the right place to handle how your integration will react depending on the different exceptions. 
This file holds a [Choice Exception Strategy](http://www.mulesoft.org/documentation/display/current/Choice+Exception+Strategy) that is referenced by the main flow in the business logic.



