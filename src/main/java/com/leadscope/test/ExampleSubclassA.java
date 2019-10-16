package com.leadscope.test;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ExampleSubclassA extends ExampleAbstract {
  private String stringValue;

  public String getStringValue() {
    return stringValue;
  }

  public void setStringValue(String stringValue) {
    this.stringValue = stringValue;
  }

  public String toString() {
    return stringValue;
  }
}
