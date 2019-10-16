package com.leadscope.test;

import org.junit.Assert;
import org.junit.Test;

import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Service;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.PortInfo;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class ExampleTestCase {
  @Test
  public void testEclipselinkServiceWithHandler() throws Throwable {
    testService("com.sun.xml.ws.db.toplink.JAXBContextFactory", new TestHandlerResolver());
  }

  @Test
  public void testEclipselinkServiceWithoutHandler() throws Throwable {
    testService("com.sun.xml.ws.db.toplink.JAXBContextFactory", null);
  }

  @Test
  public void testGlassfishServiceWithHandler() throws Throwable {
    testService("com.sun.xml.ws.db.glassfish.JAXBRIContextFactory", new TestHandlerResolver());
  }

  @Test
  public void testGlassfishServiceWithoutHandler() throws Throwable {
    testService("com.sun.xml.ws.db.glassfish.JAXBRIContextFactory", null);
  }

  private void testService(String jaxbContextFactoryClassName, HandlerResolver resolverHandler) throws Throwable {
    System.setProperty("com.sun.xml.ws.spi.db.BindingContextFactory", jaxbContextFactoryClassName);

    System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
    System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");

    Endpoint endpoint = Endpoint.publish("http://localhost:8080/example", new ExampleService());
    Assert.assertTrue(endpoint.isPublished());

    try {
      Assert.assertEquals("http://schemas.xmlsoap.org/wsdl/soap/http", endpoint.getBinding().getBindingID());

      URL wsdlDocumentLocation = new URL("http://localhost:8080/example?wsdl");
      String namespaceURI = "http://leadscope.com";
      String servicePart = "ExampleServiceService";
      String portName = "ExampleServicePort";
      QName serviceQN = new QName(namespaceURI, servicePart);
      QName portQN = new QName(namespaceURI, portName);

      // Creates a service instance
      Service service = Service.create(wsdlDocumentLocation, serviceQN);
      if (resolverHandler != null) {
        service.setHandlerResolver(resolverHandler);
      }

      IExampleService example = service.getPort(portQN, IExampleService.class);

      ExampleSubclassA value = new ExampleSubclassA();
      value.setStringValue("Foo");

      String abstractResult = example.useAbstract(value);

      Assert.assertEquals("Should have string value in passed abstract object", "Foo", abstractResult);
    }
    finally {
      endpoint.stop();
      Assert.assertFalse(endpoint.isPublished());
    }
  }

  private class TestHandlerResolver implements HandlerResolver {
    private List<Handler> handlerChain = Arrays.asList((Handler)new TestHandler());

    public List<Handler> getHandlerChain(PortInfo pi) {
      return handlerChain;
    }
  }

  private static class TestHandler implements SOAPHandler<SOAPMessageContext> {
    public void close(MessageContext arg0) {
    }

    public Set<QName> getHeaders() {
      return null;
    }

    public boolean handleFault(SOAPMessageContext mhc) {
      return true;
    }

    public boolean handleMessage(SOAPMessageContext mhc) {
      mhc.getMessage();
      return true;
    }
  }
}
