//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.08.22 at 03:39:38 PM CST 
//


package com.original.service.channel.protocols.email.vendor;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence maxOccurs="unbounded">
 *         &lt;element ref="{}EMailServer"/>
 *       &lt;/sequence>
 *       &lt;attribute name="emailpath" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "eMailServer"
})
@XmlRootElement(name = "EMailServers")
public class EMailServers {

    @XmlElement(name = "EMailServer", required = true)
    protected List<EMailServer> eMailServer;
    @XmlAttribute(name = "emailpath")
    protected String emailpath;

    /**
     * Gets the value of the eMailServer property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the eMailServer property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEMailServer().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EMailServer }
     * 
     * 
     */
    public List<EMailServer> getEMailServer() {
        if (eMailServer == null) {
            eMailServer = new ArrayList<EMailServer>();
        }
        return this.eMailServer;
    }

    /**
     * Gets the value of the emailpath property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmailpath() {
        return emailpath;
    }

    /**
     * Sets the value of the emailpath property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmailpath(String value) {
        this.emailpath = value;
    }

}
