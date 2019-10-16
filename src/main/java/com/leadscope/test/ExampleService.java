package com.leadscope.test;

import javax.jws.WebService;

@WebService(endpointInterface = "com.leadscope.test.IExampleService", targetNamespace = "http://leadscope.com")
public class ExampleService implements IExampleService {
  public String useAbstract(ExampleAbstract example) {
    if (example == null) {
      throw new IllegalArgumentException("useAbstract was passed a null example object");
    }
    return example.toString();
  }
}
