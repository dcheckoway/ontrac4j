ontrac4j
========

Java client library for use with OnTrac's shipping API

http://www.ontrac.com

apisupport@ontrac.com

Usage:

OnTrac client = OnTrac.builder().account("12345").password("secret").build();

JAXB/XML mappings generated from the XSD files supplied by OnTrac via:

xjc -d src/main/java -p ontrac4j.xml -verbose <xsdFile>
