package com.paicbd.smsc.interpreter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paicbd.smsc.dto.MessageEvent;
import com.paicbd.smsc.dto.UtilsRecords;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PayloadInterpreterTest {

    @Test
    void interpretXmlPayload() throws JsonProcessingException {
        List<UtilsRecords.OptionalParameter> optionalParameters = List.of(
                new UtilsRecords.OptionalParameter((short) 1, "value1"),
                new UtilsRecords.OptionalParameter((short) 2, "value2")
        );

        MessageEvent event = MessageEvent.builder()
                .sourceAddr("123456789")
                .sourceAddrTon(2)
                .sourceAddrNpi(2)
                .destinationAddr("987654321")
                .destAddrTon(1)
                .destAddrNpi(1)
                .esmClass(64)
                .systemId("obed")
                .shortMessage("Hello")
                .registeredDelivery(1)
                .optionalParameters(optionalParameters)
                .isDlr(true)
                .checkSubmitSmResponse(true)
                .customParams(Map.of("opp", Map.of("key", "value")))
                .build();

        String xml = """
                <smpp>
                	<commandId>4</commandId>
                	<commandLength>47</commandLength>
                	<sequenceNumber>6</sequenceNumber>
                	<serviceType></serviceType>
                	<sourceAddress>
                		<address>{{sourceAddr:STRING}}</address>
                		<ton>{{sourceAddrTon:INT}}</ton>
                		<npi>{{sourceAddrNpi:INT}}</npi>
                	</sourceAddress>
                	<destAddress>
                		<address>{{destinationAddr:STRING}}</address>
                		<ton>{{destAddrTon:HEX}}</ton>
                		<npi>{{destAddrNpi:HEX}}</npi>
                	</destAddress>
                	<scheduleDeliveryTime></scheduleDeliveryTime>
                	<validityPeriod></validityPeriod>
                	<dataCoding>{{esmClass:HEX}}</dataCoding>
                	<protocolId>{{esmClass:HEX}}</protocolId>
                	<priority>{{esmClass:HEX}}</priority>
                	<registerDelivery>{{registeredDelivery:BOOLEAN}}</registerDelivery>
                	<replaceIfPresent>0x00</replaceIfPresent>
                	<messageLength>6</messageLength>
                	<message>{{shortMessage:STRING}}</message>
                	<clientId>{{systemId:STRING}}</clientId>
                	<host>127.0.0.1</host>
                	<esmClass>{{esmClass:HEX}}</esmClass>
                	<optParams>{{optionalParameters:LIST:ls}}</optParams>
                	<deliverSm>{{isDlr:BOOLEAN}}</deliverSm>
                	<unknownProperty>{{unknown:STRING}}</unknownProperty>
                	<inconvertibleBoolean>{{systemId:BOOLEAN}}</inconvertibleBoolean>
                	<check>{{checkSubmitSmResponse:BOOLEAN}}</check>
                	<cp>{{customParams.opp.key:MAP}}</cp>
                </smpp>
                """;

        PayloadFormat pf = PayloadFormat.valueOf("XML");
        String formatedXml = PayloadInterpreter.interpretPayloadForSend(xml, event, pf);
        System.out.println(formatedXml);
        assertNotNull(formatedXml);

        JsonNode node = ObjectsInterpreter.xmlStringToJsonNode(formatedXml);
        assertNotNull(node);

        assertEquals("123456789", node.get("sourceAddress").get("address").asText());
        assertEquals(2, node.get("sourceAddress").get("ton").asInt());
        assertEquals(2, node.get("sourceAddress").get("npi").asInt());
        assertEquals("987654321", node.get("destAddress").get("address").asText());
        assertEquals("0x01", node.get("destAddress").get("ton").asText());
        assertEquals("0x01", node.get("destAddress").get("npi").asText());
        assertEquals("0x40", node.get("dataCoding").asText());
        assertEquals("0x40", node.get("protocolId").asText());

        assertEquals("0x40", node.get("priority").asText());
        assertTrue(node.get("registerDelivery").asBoolean());
        assertEquals("0x00", node.get("replaceIfPresent").asText());
        assertEquals(6, node.get("messageLength").asInt());
        assertEquals("Hello", node.get("message").asText());
        assertEquals("obed", node.get("clientId").asText());
        assertEquals("value1", node.get("optParams").get("ls").get(0).get("value").asText());
    }

    @Test
    void interpretJsonPayload() throws JsonProcessingException {
        List<UtilsRecords.OptionalParameter> optionalParameters = List.of(
                new UtilsRecords.OptionalParameter((short) 1, "value1"),
                new UtilsRecords.OptionalParameter((short) 2, "value2")
        );

        MessageEvent event = MessageEvent.builder()
                .sourceAddr("123456789")
                .sourceAddrTon(2)
                .sourceAddrNpi(2)
                .destinationAddr("987654321")
                .destAddrTon(1)
                .destAddrNpi(1)
                .esmClass(64)
                .checkSubmitSmResponse(false)
                .systemId("obed")
                .shortMessage("Hello")
                .registeredDelivery(1)
                .optionalParameters(optionalParameters)
                .build();

        String json = """
                {
                	"commandId": 4,
                	"commandLength": 47,
                	"sequenceNumber": 6,
                	"serviceType": "",
                	"sourceAddress": {
                		"address": {{sourceAddr:STRING}},
                		"ton": {{sourceAddrTon:HEX}},
                		"npi": {{sourceAddrNpi:HEX}}
                	},
                	"destAddress": {
                		"address": {{destinationAddr:STRING}},
                		"ton": {{destAddrTon:HEX}},
                		"npi": {{destAddrNpi:HEX}}
                	},
                	"scheduleDeliveryTime": "",
                	"validityPeriod": "",
                	"dataCoding": {{esmClass:HEX}},
                	"protocolId": {{esmClass:HEX}},
                	"priority": {{esmClass:HEX}},
                	"registerDelivery": {{registeredDelivery:BOOLEAN}},
                	"replaceIfPresent": "0x00",
                	"messageLength": 6,
                	"message": {{shortMessage:STRING}},
                	"clientId": {{systemId:STRING}},
                	"host": "127.0.0.1",
                	"esmClass": {{esmClass:HEX}},
                	"opt": {{optionalParameters:LIST}},
                	"check": {{checkSubmitSmResponse:BOOLEAN}}
                }
                """;

        String formatedJson = PayloadInterpreter.interpretPayloadForSend(json, event, PayloadFormat.JSON);
        System.out.println(formatedJson);

        JsonNode node = new ObjectMapper().readTree(formatedJson);
        assertNotNull(node);

        assertNotNull(formatedJson);
        assertEquals("123456789", node.get("sourceAddress").get("address").asText());
        assertEquals("0x02", node.get("sourceAddress").get("ton").asText());
        assertEquals("0x02", node.get("sourceAddress").get("npi").asText());
        assertEquals("987654321", node.get("destAddress").get("address").asText());
        assertEquals("0x01", node.get("destAddress").get("ton").asText());
        assertEquals("0x01", node.get("destAddress").get("npi").asText());
        assertEquals("0x40", node.get("dataCoding").asText());
        assertEquals("0x40", node.get("protocolId").asText());
    }

    @Test
    void toXml() throws JsonProcessingException {
        String xml = """
                <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
                <dialog mapMessagesSize="1" userObject="{{systemId:STRING}}">
                  <unstructuredSSRequest_Request dataCodingScheme="{{dataCoding:HEX}}" string="{{shortMessage:STRING}}">
                    <msisdn nai="international_number" npi="ISDN" number="{{destinationAddr:STRING}}"/>
                  </unstructuredSSRequest_Request>
                </dialog>
                """;

        MessageEvent event = MessageEvent.builder()
                .systemId("obed")
                .dataCoding(64)
                .shortMessage("Hello")
                .destinationAddr("987654321")
                .build();

        String formatedXml = PayloadInterpreter.interpretPayloadForSend(xml, event, PayloadFormat.XML);
        System.out.println(formatedXml);
        assertNotNull(formatedXml);

        JsonNode node = ObjectsInterpreter.xmlStringToJsonNode(formatedXml);
        assertNotNull(node);

        assertEquals("obed", node.get("userObject").asText());
        assertEquals("0x40", node.get("unstructuredSSRequest_Request").get("dataCodingScheme").asText());
        assertEquals("Hello", node.get("unstructuredSSRequest_Request").get("string").asText());
        assertEquals("987654321", node.get("unstructuredSSRequest_Request").get("msisdn").get("number").asText());
    }

    @Test
    void complexXmlStringInMessageEvent() {
        String mapper = """
                <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
                <dialog mapMessagesSize="1" userObject="{{systemId:STRING}}">
                  <unstructuredSSRequest_Request dataCodingScheme="{{dataCoding:HEX}}" string="{{shortMessage:STRING}}">
                    <msisdn nai="international_number" npi="ISDN" number="{{destinationAddr:STRING}}"/>
                    <ussdString>{{sourceAddr:STRING}}</ussdString>
                  </unstructuredSSRequest_Request>
                </dialog>
                """;

        String request = """
                <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
                <dialog mapMessagesSize="1" userObject="obed">
                  <unstructuredSSRequest_Request dataCodingScheme="0x01" string="Paquete Sin Limite Activo, Detalle de saldo">
                    <msisdn nai="international_number" npi="ISDN" number="525532368999"/>
                    <ussdString>1</ussdString>
                  </unstructuredSSRequest_Request>
                </dialog>
                """;

        MessageEvent resultedMessageEvent = new MessageEvent();
        assertDoesNotThrow(() -> PayloadInterpreter.interpreterPayloadForReceive(request, mapper, resultedMessageEvent, PayloadFormat.XML));
        System.out.println(resultedMessageEvent);

        assertEquals("obed", resultedMessageEvent.getSystemId());
        assertEquals(1, resultedMessageEvent.getDataCoding());
        assertEquals("Paquete Sin Limite Activo, Detalle de saldo", resultedMessageEvent.getShortMessage());
        assertEquals("525532368999", resultedMessageEvent.getDestinationAddr());
        assertEquals("1", resultedMessageEvent.getSourceAddr());
    }

    @Test
    void complexJsonValuesInMessageEvent() {
        String mapper = """
                {
                  "dialog": {
                    "mapMessagesSize": 1,
                    "userObject": "{{systemId:STRING}}",
                    "unstructuredSSRequest_Request": {
                      "dataCodingScheme": "{{dataCoding:HEX}}",
                      "string": "{{shortMessage:STRING}}",
                      "msisdn": {
                        "nai": "international_number",
                        "npi": "ISDN",
                        "number": "{{destinationAddr:STRING}}"
                      },
                      "ussdString": "{{sourceAddr:STRING}}"
                    }
                  }
                }
                """;

        String request = """
                {
                  "dialog": {
                    "mapMessagesSize": 1,
                    "userObject": "obed",
                    "unstructuredSSRequest_Request": {
                      "dataCodingScheme": "0x01",
                      "string": "Paquete Sin Limite Activo, Detalle de saldo",
                      "msisdn": {
                        "nai": "international_number",
                        "npi": "ISDN",
                        "number": "525532368999"
                      },
                      "ussdString": "1"
                    }
                  }
                }
                """;

        MessageEvent resultedMessageEvent = new MessageEvent();
        assertDoesNotThrow(() -> PayloadInterpreter.interpreterPayloadForReceive(request, mapper, resultedMessageEvent, PayloadFormat.JSON));
        System.out.println(resultedMessageEvent);

        assertEquals("obed", resultedMessageEvent.getSystemId());
        assertEquals(1, resultedMessageEvent.getDataCoding());
        assertEquals("Paquete Sin Limite Activo, Detalle de saldo", resultedMessageEvent.getShortMessage());
        assertEquals("525532368999", resultedMessageEvent.getDestinationAddr());
        assertEquals("1", resultedMessageEvent.getSourceAddr());
    }

    @Test
    void xml019ToMessageEvent() {
        String xml = """
                <smpp>
                	<commandId>4</commandId>
                	<commandLength>47</commandLength>
                	<sequenceNumber>6</sequenceNumber>
                	<serviceType></serviceType>
                	<sourceAddress>
                		<address>6666</address>
                		<ton>0x01</ton>
                		<npi>0x01</npi>
                	</sourceAddress>
                	<destAddress>
                		<address>5555,333,111,222,123,322,122,331,876,666,544,345,775,126</address>
                		<ton>0x01</ton>
                		<npi>0x01</npi>
                	</destAddress>
                	<scheduleDeliveryTime></scheduleDeliveryTime>
                	<validityPeriod></validityPeriod>
                	<dataCoding>0x00</dataCoding>
                	<protocolId>0x00</protocolId>
                	<priority>0x00</priority>
                	<registerDelivery>0x01</registerDelivery>
                	<replaceIfPresent>0x00</replaceIfPresent>
                	<messageLength>6</messageLength>
                	<message>48656C6C6F21</message>
                	<clientId>server1-test1</clientId>
                	<host>127.0.0.1</host>
                	<esmClass>0x03</esmClass>
                	<optParams>
                		<pop>
                			<tag>0x01</tag>
                			<lenght>0x02</lenght>
                			<value>0x76</value>
                		</pop>
                		<pop>
                			<tag>0x02</tag>
                			<lenght>0x02</lenght>
                			<value>0x65</value>
                		</pop>
                	</optParams>
                </smpp>
                """;

        String xmlMapper = """
                <smpp>
                	<commandId>4</commandId>
                	<commandLength>47</commandLength>
                	<sequenceNumber>6</sequenceNumber>
                	<serviceType></serviceType>
                	<sourceAddress>
                		<address>{{sourceAddr:STRING}}</address>
                		<ton>{{sourceAddrTon:HEX}}</ton>
                		<npi>{{sourceAddrNpi:HEX}}</npi>
                	</sourceAddress>
                	<destAddress>
                		<address>{{destinationAddr:LIST}}</address>
                		<ton>{{destAddrTon:HEX}}</ton>
                		<npi>{{destAddrNpi:HEX}}</npi>
                	</destAddress>
                	<scheduleDeliveryTime></scheduleDeliveryTime>
                	<validityPeriod></validityPeriod>
                	<dataCoding>{{dataCoding:HEX}}</dataCoding>
                	<protocolId>0x00</protocolId>
                	<priority>0x00</priority>
                	<registerDelivery>{{registeredDelivery:HEX}}</registerDelivery>
                	<replaceIfPresent>0x00</replaceIfPresent>
                	<messageLength>6</messageLength>
                	<message>{{shortMessage:STRING}}</message>
                	<clientId>{{systemId:STRING}}</clientId>
                	<host>127.0.0.1</host>
                	<esmClass>{{esmClass:HEX}}</esmClass>
                	<optParams>{{optionalParameters:LIST:pop}}</optParams>
                </smpp>
                """;

        MessageEvent resultedMessageEvent = new MessageEvent();
        resultedMessageEvent.setMessageId("019");
        assertDoesNotThrow(() -> PayloadInterpreter.interpreterPayloadForReceive(xml, xmlMapper, resultedMessageEvent, PayloadFormat.XML));

        System.out.println(resultedMessageEvent);

        assertEquals("6666", resultedMessageEvent.getSourceAddr());
        assertEquals(1, resultedMessageEvent.getSourceAddrTon());
        assertEquals(1, resultedMessageEvent.getSourceAddrNpi());
        assertEquals("[5555, 333, 111, 222, 123, 322, 122, 331, 876, 666, 544, 345, 775, 126]", resultedMessageEvent.getDestinationAddr());
        assertEquals(1, resultedMessageEvent.getDestAddrTon());
        assertEquals(1, resultedMessageEvent.getDestAddrNpi());
        assertEquals(0, resultedMessageEvent.getDataCoding());
        assertEquals(0, resultedMessageEvent.getProtocolId());
        assertEquals(0, resultedMessageEvent.getPriorityFlag());
        assertEquals(1, resultedMessageEvent.getRegisteredDelivery());
        assertEquals(0, resultedMessageEvent.getReplaceIfPresent());
        assertEquals("48656C6C6F21", resultedMessageEvent.getShortMessage());
        assertEquals("server1-test1", resultedMessageEvent.getSystemId());
        assertEquals(3, resultedMessageEvent.getEsmClass());
        assertEquals(2, resultedMessageEvent.getOptionalParameters().size());
    }

    @Test
    void json019ToMessageEvent() {
        String json = """
                {
                  "commandId": 4,
                  "commandLength": 47,
                  "sequenceNumber": 6,
                  "serviceType": "",
                  "sourceAddress": {
                    "address": "6666",
                    "ton": "0x01",
                    "npi": "0x01"
                  },
                  "destAddress": {
                    "address": "5555,333,111,222,123,322,122,331,876,666,544,345,775,126",
                    "ton": "1",
                    "npi": "0x01"
                  },
                  "scheduleDeliveryTime": "",
                  "validityPeriod": "",
                  "dataCoding": "0x00",
                  "protocolId": "1",
                  "priority": "0x00",
                  "registerDelivery": true,
                  "replaceIfPresent": "0x00",
                  "messageLength": 6,
                  "message": "48656C6C6F21",
                  "clientId": "server1-test1",
                  "host": "127.0.0.1",
                  "esmClass": "0x03",
                  "optParams": [
                    {
                    "tag": "0x01",
                    "value": "0x76"
                    },
                    {
                    "tag": "0x02",
                    "value": "0x65"
                    }
                  ],
                  "isDlr": "0x01",
                  "rdId": 1,
                  "unknown": null,
                  "validityPeriod": 1200,
                  "check": true,
                  "allParams": {
                    "cp1": {
                      "key1": "value1"
                    },
                    "cp2": {
                      "key2": "value2"
                    }
                  }
                }""";

        String jsonMapper = """
                {
                  "commandId": "{{commandId:INT}}",
                  "commandLength": 47,
                  "sequenceNumber": 6,
                  "serviceType": "",
                  "sourceAddress": {
                    "address": "{{sourceAddr:STRING}}",
                    "ton": "{{sourceAddrTon:HEX}}",
                    "npi": "{{sourceAddrNpi:HEX}}"
                  },
                  "destAddress": {
                    "address": "{{destinationAddr:LIST}}",
                    "ton": "{{destAddrTon:INT}}",
                    "npi": "{{destAddrNpi:HEX}}"
                  },
                  "scheduleDeliveryTime": "",
                  "validityPeriod": "",
                  "dataCoding": "{{dataCoding:HEX}}",
                  "protocolId": "{{protocolId:BYTE}}",
                  "priority": "0x00",
                  "registerDelivery": "{{registeredDelivery:BOOLEAN}}",
                  "replaceIfPresent": "0x00",
                  "messageLength": 6,
                  "message": "{{shortMessage:STRING}}",
                  "clientId": "{{systemId:STRING}}",
                  "host": "127.0.0.1",
                  "esmClass": "{{esmClass:HEX}}",
                  "optParams": "{{optionalParameters:LIST}}",
                  "isDlr": "{{isDlr:HEX}}",
                  "rdId": "{{remoteDialogId:LONG}}",
                  "unknown": "{{imsi:STRING}}",
                  "validityPeriod": "{{validityPeriod:LONG}}",
                  "check": "{{checkSubmitSmResponse:BOOLEAN}}",
                  "allParams": "{{customParams:MAP}}"
                }
                """;

        MessageEvent resultedMessageEvent = new MessageEvent();
        resultedMessageEvent.setMessageId("019");
        assertDoesNotThrow(() -> PayloadInterpreter.interpreterPayloadForReceive(json, jsonMapper, resultedMessageEvent, PayloadFormat.JSON));

        System.out.println(resultedMessageEvent);

        assertEquals("6666", resultedMessageEvent.getSourceAddr());
        assertEquals(1, resultedMessageEvent.getSourceAddrTon());
        assertEquals(1, resultedMessageEvent.getSourceAddrNpi());
        assertEquals("[5555, 333, 111, 222, 123, 322, 122, 331, 876, 666, 544, 345, 775, 126]", resultedMessageEvent.getDestinationAddr());
        assertEquals(1, resultedMessageEvent.getDestAddrTon());
        assertEquals(1, resultedMessageEvent.getDestAddrNpi());
        assertEquals(0, resultedMessageEvent.getDataCoding());
        assertEquals(1, resultedMessageEvent.getProtocolId());
        assertEquals(0, resultedMessageEvent.getPriorityFlag());
        assertEquals(1, resultedMessageEvent.getRegisteredDelivery());
        assertEquals(0, resultedMessageEvent.getReplaceIfPresent());
        assertEquals("48656C6C6F21", resultedMessageEvent.getShortMessage());
        assertEquals("server1-test1", resultedMessageEvent.getSystemId());
        assertEquals(3, resultedMessageEvent.getEsmClass());
        assertEquals(2, resultedMessageEvent.getOptionalParameters().size());
        assertEquals(1, resultedMessageEvent.getRemoteDialogId());
        assertEquals(1200, resultedMessageEvent.getValidityPeriod());
    }

    @Test
    void invalidDataTypeProduceIllegalArgumentException() {
        String xml = """
                <smpp>
                	<commandId>4</commandId>
                	<commandLength>47</commandLength>
                	<sequenceNumber>6</sequenceNumber>
                	<serviceType></serviceType>
                	<sourceAddress>
                		<address>{{sourceAddr:STRING}}</address>
                		<ton>{{sourceAddrTon:INT}}</ton>
                		<npi>{{sourceAddrNpi:UNKNOWN}}</npi>
                	</sourceAddress>
                </smpp>
                """;

        MessageEvent event = MessageEvent.builder()
                .sourceAddr("123456789")
                .sourceAddrTon(2)
                .sourceAddrNpi(2)
                .build();

        assertThrows(IllegalArgumentException.class, () -> PayloadInterpreter.interpretPayloadForSend(xml, event, PayloadFormat.XML));
        assertThrows(IllegalArgumentException.class, () -> PayloadInterpreter.interpreterPayloadForReceive(xml, xml, event, PayloadFormat.XML));
    }
}