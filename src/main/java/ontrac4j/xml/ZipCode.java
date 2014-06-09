//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.06.09 at 02:36:41 PM EDT 
//


package ontrac4j.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ZipCode complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ZipCode">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="zipCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="saturdayServiced" type="{http://www.w3.org/2001/XMLSchema}unsignedByte"/>
 *         &lt;element name="pickupServiced" type="{http://www.w3.org/2001/XMLSchema}unsignedByte"/>
 *         &lt;element name="palletizedServiced" type="{http://www.w3.org/2001/XMLSchema}unsignedByte"/>
 *         &lt;element name="sortCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ZipCode", propOrder = {
    "zipCode",
    "saturdayServiced",
    "pickupServiced",
    "palletizedServiced",
    "sortCode"
})
public class ZipCode {

    @XmlElement(required = true)
    protected String zipCode;
    @XmlSchemaType(name = "unsignedByte")
    protected short saturdayServiced;
    @XmlSchemaType(name = "unsignedByte")
    protected short pickupServiced;
    @XmlSchemaType(name = "unsignedByte")
    protected short palletizedServiced;
    @XmlElement(required = true)
    protected String sortCode;

    /**
     * Gets the value of the zipCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * Sets the value of the zipCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZipCode(String value) {
        this.zipCode = value;
    }

    /**
     * Gets the value of the saturdayServiced property.
     * 
     */
    public short getSaturdayServiced() {
        return saturdayServiced;
    }

    /**
     * Sets the value of the saturdayServiced property.
     * 
     */
    public void setSaturdayServiced(short value) {
        this.saturdayServiced = value;
    }

    /**
     * Gets the value of the pickupServiced property.
     * 
     */
    public short getPickupServiced() {
        return pickupServiced;
    }

    /**
     * Sets the value of the pickupServiced property.
     * 
     */
    public void setPickupServiced(short value) {
        this.pickupServiced = value;
    }

    /**
     * Gets the value of the palletizedServiced property.
     * 
     */
    public short getPalletizedServiced() {
        return palletizedServiced;
    }

    /**
     * Sets the value of the palletizedServiced property.
     * 
     */
    public void setPalletizedServiced(short value) {
        this.palletizedServiced = value;
    }

    /**
     * Gets the value of the sortCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSortCode() {
        return sortCode;
    }

    /**
     * Sets the value of the sortCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSortCode(String value) {
        this.sortCode = value;
    }

}
