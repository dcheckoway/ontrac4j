ontrac4j
========

Java client library for use with OnTrac's shipping API

http://www.ontrac.com

apisupport@ontrac.com

Normal Usage:

    import ontrac4j.OnTrac;
    import ontrac4j.xml.ShipmentRequest;
    import ontrac4j.xml.ShipmentResponse;
    import ontrac4j.xml.TrackingShipment;
    
    OnTrac ontrac = OnTrac.builder().account("12345").password("secret").build();
    
    ShipmentRequest shipmentRequest = new ShipmentRequest();
    shipmentRequest.setUID(...);
    shipmentRequest.setShipper(...);
    ...
    ShipmentResponse shipmentResponse = ontrac.createShipment(shipmentRequest);
    System.out.println("Shipment created: " + shipmentResponse.getTracking());
    System.out.println("Total charge: " + shipmentResponse.getTotalChrg());
    System.out.println("Transit days: " + shipmentResponse.getTransitDays());
        
    TrackingShipment trackingShipment = ontrac.trackShipment(trackingNumber);
    if (trackingShipment.isDelivered()) {
        System.out.println("Delivered to " + trackingShipment.getName() + " on " + trackingShipment.getExpDelDate());
    } else {
        System.out.println("Expected delivery date: " + trackingShipment.getExpDelDate());
    }


To use OnTrac's test site instead of production:

    OnTrac ontrac = OnTrac.builder().test().account("37").password("testpass").build();

JAXB/XML mappings were generated from the XSD files supplied by OnTrac by first commenting out the "Dim" type in OnTracRateResponse.xml (there were two, which caused duplicate issues with XJC), and then running:

    xjc -extension xsd/simpleMode.xsd -d src/main/java -p ontrac4j.xml -verbose xsd/OnTrac*.xsd
