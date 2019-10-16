package com.leadscope.test;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlSeeAlso({ExampleSubclassA.class, ExampleSubclassB.class})
public abstract class ExampleAbstract {

}
