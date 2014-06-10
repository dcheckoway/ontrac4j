//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.06.09 at 05:34:31 PM EDT 
//


package ontrac4j.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for TrackingShipment complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TrackingShipment">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Events" type="{}History"/>
 *         &lt;element name="Tracking" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Exp_Del_Date" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="ShipDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="Delivered" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Contact" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Addr1" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Addr2" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Addr3" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="City" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="State" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Zip" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Service" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="POD" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Error" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Reference" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Reference2" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Reference3" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ServiceCharge" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="FuelCharge" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="TotalChrg" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="Residential" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="Weight" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="Signature" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TrackingShipment", propOrder = {
    "events",
    "tracking",
    "expDelDate",
    "shipDate",
    "delivered",
    "name",
    "contact",
    "addr1",
    "addr2",
    "addr3",
    "city",
    "state",
    "zip",
    "service",
    "pod",
    "error",
    "reference",
    "reference2",
    "reference3",
    "serviceCharge",
    "fuelCharge",
    "totalChrg",
    "residential",
    "weight",
    "signature"
})
public class TrackingShipment {

    @XmlElement(name = "Events", required = true)
    protected History events;
    @XmlElement(name = "Tracking", required = true)
    protected String tracking;
    @XmlElement(name = "Exp_Del_Date", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar expDelDate;
    @XmlElement(name = "ShipDate", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar shipDate;
    @XmlElement(name = "Delivered")
    protected boolean delivered;
    @XmlElement(name = "Name", required = true)
    protected String name;
    @XmlElement(name = "Contact", required = true)
    protected String contact;
    @XmlElement(name = "Addr1", required = true)
    protected String addr1;
    @XmlElement(name = "Addr2", required = true)
    protected String addr2;
    @XmlElement(name = "Addr3", required = true)
    protected String addr3;
    @XmlElement(name = "City", required = true)
    protected String city;
    @XmlElement(name = "State", required = true)
    protected String state;
    @XmlElement(name = "Zip", required = true)
    protected String zip;
    @XmlElement(name = "Service", required = true)
    protected String service;
    @XmlElement(name = "POD", required = true)
    protected String pod;
    @XmlElement(name = "Error", required = true)
    protected String error;
    @XmlElement(name = "Reference", required = true)
    protected String reference;
    @XmlElement(name = "Reference2", required = true)
    protected String reference2;
    @XmlElement(name = "Reference3", required = true)
    protected String reference3;
    @XmlElement(name = "ServiceCharge")
    protected double serviceCharge;
    @XmlElement(name = "FuelCharge")
    protected double fuelCharge;
    @XmlElement(name = "TotalChrg")
    protected double totalChrg;
    @XmlElement(name = "Residential")
    protected boolean residential;
    @XmlElement(name = "Weight")
    protected double weight;
    @XmlElement(name = "Signature", required = true)
    protected String signature;

    /**
     * Gets the value of the events property.
     * 
     * @return
     *     possible object is
     *     {@link History }
     *     
     */
    public History getEvents() {
        return events;
    }

    /**
     * Sets the value of the events property.
     * 
     * @param value
     *     allowed object is
     *     {@link History }
     *     
     */
    public void setEvents(History value) {
        this.events = value;
    }

    /**
     * Gets the value of the tracking property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTracking() {
        return tracking;
    }

    /**
     * Sets the value of the tracking property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTracking(String value) {
        this.tracking = value;
    }

    /**
     * Gets the value of the expDelDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getExpDelDate() {
        return expDelDate;
    }

    /**
     * Sets the value of the expDelDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setExpDelDate(XMLGregorianCalendar value) {
        this.expDelDate = value;
    }

    /**
     * Gets the value of the shipDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getShipDate() {
        return shipDate;
    }

    /**
     * Sets the value of the shipDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setShipDate(XMLGregorianCalendar value) {
        this.shipDate = value;
    }

    /**
     * Gets the value of the delivered property.
     * 
     */
    public boolean isDelivered() {
        return delivered;
    }

    /**
     * Sets the value of the delivered property.
     * 
     */
    public void setDelivered(boolean value) {
        this.delivered = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the contact property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContact() {
        return contact;
    }

    /**
     * Sets the value of the contact property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContact(String value) {
        this.contact = value;
    }

    /**
     * Gets the value of the addr1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddr1() {
        return addr1;
    }

    /**
     * Sets the value of the addr1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddr1(String value) {
        this.addr1 = value;
    }

    /**
     * Gets the value of the addr2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddr2() {
        return addr2;
    }

    /**
     * Sets the value of the addr2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddr2(String value) {
        this.addr2 = value;
    }

    /**
     * Gets the value of the addr3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddr3() {
        return addr3;
    }

    /**
     * Sets the value of the addr3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddr3(String value) {
        this.addr3 = value;
    }

    /**
     * Gets the value of the city property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the value of the city property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCity(String value) {
        this.city = value;
    }

    /**
     * Gets the value of the state property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getState() {
        return state;
    }

    /**
     * Sets the value of the state property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setState(String value) {
        this.state = value;
    }

    /**
     * Gets the value of the zip property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZip() {
        return zip;
    }

    /**
     * Sets the value of the zip property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZip(String value) {
        this.zip = value;
    }

    /**
     * Gets the value of the service property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getService() {
        return service;
    }

    /**
     * Sets the value of the service property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setService(String value) {
        this.service = value;
    }

    /**
     * Gets the value of the pod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPOD() {
        return pod;
    }

    /**
     * Sets the value of the pod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPOD(String value) {
        this.pod = value;
    }

    /**
     * Gets the value of the error property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getError() {
        return error;
    }

    /**
     * Sets the value of the error property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setError(String value) {
        this.error = value;
    }

    /**
     * Gets the value of the reference property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReference() {
        return reference;
    }

    /**
     * Sets the value of the reference property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReference(String value) {
        this.reference = value;
    }

    /**
     * Gets the value of the reference2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReference2() {
        return reference2;
    }

    /**
     * Sets the value of the reference2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReference2(String value) {
        this.reference2 = value;
    }

    /**
     * Gets the value of the reference3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReference3() {
        return reference3;
    }

    /**
     * Sets the value of the reference3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReference3(String value) {
        this.reference3 = value;
    }

    /**
     * Gets the value of the serviceCharge property.
     * 
     */
    public double getServiceCharge() {
        return serviceCharge;
    }

    /**
     * Sets the value of the serviceCharge property.
     * 
     */
    public void setServiceCharge(double value) {
        this.serviceCharge = value;
    }

    /**
     * Gets the value of the fuelCharge property.
     * 
     */
    public double getFuelCharge() {
        return fuelCharge;
    }

    /**
     * Sets the value of the fuelCharge property.
     * 
     */
    public void setFuelCharge(double value) {
        this.fuelCharge = value;
    }

    /**
     * Gets the value of the totalChrg property.
     * 
     */
    public double getTotalChrg() {
        return totalChrg;
    }

    /**
     * Sets the value of the totalChrg property.
     * 
     */
    public void setTotalChrg(double value) {
        this.totalChrg = value;
    }

    /**
     * Gets the value of the residential property.
     * 
     */
    public boolean isResidential() {
        return residential;
    }

    /**
     * Sets the value of the residential property.
     * 
     */
    public void setResidential(boolean value) {
        this.residential = value;
    }

    /**
     * Gets the value of the weight property.
     * 
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Sets the value of the weight property.
     * 
     */
    public void setWeight(double value) {
        this.weight = value;
    }

    /**
     * Gets the value of the signature property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSignature() {
        return signature;
    }

    /**
     * Sets the value of the signature property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSignature(String value) {
        this.signature = value;
    }

}