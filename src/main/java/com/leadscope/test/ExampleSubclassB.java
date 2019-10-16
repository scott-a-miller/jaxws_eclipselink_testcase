package com.leadscope.test;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ExampleSubclassB extends ExampleAbstract {
  private int intValue;

  public int getIntValue() {
    return intValue;
  }

  public void setIntValue(int intValue) {
    this.intValue = intValue;
  }

  public String toString() {
    return String.valueOf(intValue);
  }
}
