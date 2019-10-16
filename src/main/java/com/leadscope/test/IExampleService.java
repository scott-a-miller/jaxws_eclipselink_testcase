package com.leadscope.test;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService(name = "ExampleService", targetNamespace = "http://leadscope.com")
public interface IExampleService {
  @WebMethod(operationName = "useAbstract", action = "urn:useAbstract")
  @WebResult(name = "useAbstractResult")
  String useAbstract(ExampleAbstract example);
}
