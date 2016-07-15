/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.1.2-b01-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.02.26 at 11:38:19 PM PST 
//


package com.mirth.connect.plugins.datatypes.edi;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.mirth.connect.model.x12 package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _SegmentTypeSyntax_QNAME = new QName("", "syntax");
    private final static QName _SegmentTypeMaxUse_QNAME = new QName("", "max_use");
    private final static QName _LoopTypeSegment_QNAME = new QName("", "segment");
    private final static QName _LoopTypeLoop_QNAME = new QName("", "loop");
    private final static QName _Transaction_QNAME = new QName("", "transaction");
    private final static QName _Repeat_QNAME = new QName("", "repeat");
    private final static QName _Count_QNAME = new QName("", "count");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.mirth.connect.model.x12
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SegmentType }
     * 
     */
    public SegmentType createSegmentType() {
        return new SegmentType();
    }

    /**
     * Create an instance of {@link TransactionType }
     * 
     */
    public TransactionType createTransactionType() {
        return new TransactionType();
    }

    /**
     * Create an instance of {@link ElementType }
     * 
     */
    public ElementType createElementType() {
        return new ElementType();
    }

    /**
     * Create an instance of {@link CompositeType }
     * 
     */
    public CompositeType createCompositeType() {
        return new CompositeType();
    }

    /**
     * Create an instance of {@link ValidCodesType }
     * 
     */
    public ValidCodesType createValidCodesType() {
        return new ValidCodesType();
    }

    /**
     * Create an instance of {@link ElementType.ValidCodes }
     * 
     */
    public ElementType.ValidCodes createElementTypeValidCodes() {
        return new ElementType.ValidCodes();
    }

    /**
     * Create an instance of {@link LoopType }
     * 
     */
    public LoopType createLoopType() {
        return new LoopType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "syntax", scope = SegmentType.class)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    public JAXBElement<String> createSegmentTypeSyntax(String value) {
        return new JAXBElement<String>(_SegmentTypeSyntax_QNAME, String.class, SegmentType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "max_use", scope = SegmentType.class)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    public JAXBElement<String> createSegmentTypeMaxUse(String value) {
        return new JAXBElement<String>(_SegmentTypeMaxUse_QNAME, String.class, SegmentType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SegmentType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "segment", scope = LoopType.class)
    public JAXBElement<SegmentType> createLoopTypeSegment(SegmentType value) {
        return new JAXBElement<SegmentType>(_LoopTypeSegment_QNAME, SegmentType.class, LoopType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoopType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "loop", scope = LoopType.class)
    public JAXBElement<LoopType> createLoopTypeLoop(LoopType value) {
        return new JAXBElement<LoopType>(_LoopTypeLoop_QNAME, LoopType.class, LoopType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "syntax", scope = LoopType.class)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    public JAXBElement<String> createLoopTypeSyntax(String value) {
        return new JAXBElement<String>(_SegmentTypeSyntax_QNAME, String.class, LoopType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TransactionType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "transaction")
    public JAXBElement<TransactionType> createTransaction(TransactionType value) {
        return new JAXBElement<TransactionType>(_Transaction_QNAME, TransactionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "repeat")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    public JAXBElement<String> createRepeat(String value) {
        return new JAXBElement<String>(_Repeat_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "count", substitutionHeadNamespace = "", substitutionHeadName = "repeat")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    public JAXBElement<String> createCount(String value) {
        return new JAXBElement<String>(_Count_QNAME, String.class, null, value);
    }

}