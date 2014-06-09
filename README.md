ontrac4j
========

Java client library for use with OnTrac's shipping API

http://www.ontrac.com

apisupport@ontrac.com

Normal Usage:

`OnTrac client = OnTrac.builder().account("12345").password("secret").build();`

Test Environment Usage:

`OnTrac client = OnTrac.builder().test().account("37").password("testpass").build();`

JAXB/XML mappings were generated from the XSD files supplied by OnTrac by first commenting out the "Dim" type in OnTracRateResponse.xml (there were two, which caused duplicate issues with XJC), and then running:

`xjc -extension xsd/simpleMode.xsd -d src/main/java -p ontrac4j.xml -verbose xsd/OnTrac*.xsd`
