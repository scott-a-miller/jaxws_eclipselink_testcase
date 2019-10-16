JAX WS Eclipselink Serialization Error Testcase
-----------------------------------------------

This project reproduces an error in the JAX WS reference implementation that occurs 
when serializing the subclass of an abstract class. It appears to only be triggered when
the SOAP message is being created via a SOAPHandler, and only when the Eclipselink
plugin JAXBContextFactory is being utilized.

A test case is provided that shows the failure, as well as three other cases that 
succeed when those conditions are not present.

The issue is that the xsi prefix is omitted from the object's type attribute as seen
in the HttpTransportPipe.dump:

    <?xml version='1.0' encoding='UTF-8'?>
    <S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/"  
                  xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
      <SOAP-ENV:Header/>
      <S:Body xmlns:ns0="http://leadscope.com">
          <ns0:useAbstract xmlns:ns0="http://leadscope.com" 
                           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
              <arg0 xmlns="http://www.w3.org/2001/XMLSchema-instance" 
                    type="ns0:exampleSubclassA">
                  <stringValue xmlns="">Foo</stringValue>
              </arg0>
          </ns0:useAbstract>
      </S:Body>
    </S:Envelope>`
 
Interestingly, the default namespace is set on that node, but of course does not apply to attributes.

Compared with the SOAP message for the cases where serialization succeeds:

    <?xml version='1.0' encoding='UTF-8'?>
    <S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
      <S:Body>
         <ns2:useAbstract xmlns:ns2="http://leadscope.com">
             <arg0 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
                   xsi:type="ns2:exampleSubclassA">
                 <stringValue>Foo</stringValue>
             </arg0>
         </ns2:useAbstract>
     </S:Body>
    </S:Envelope>

The effect is that with the type information missing, the deserializer is unable
to distinguish which concrete class to instantiate.

To build the wsdl and client artifacts, and run the tests:

    mvn install 