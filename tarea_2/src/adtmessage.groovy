import ca.uhn.hl7v2.DefaultHapiContext
import ca.uhn.hl7v2.HapiContext
import ca.uhn.hl7v2.parser.Parser

// Importante! al importar tener en cuenta la version, aquí es 2.5
import ca.uhn.hl7v2.model.v25.message.ADT_A01
import ca.uhn.hl7v2.model.v25.segment.MSH
import ca.uhn.hl7v2.model.v25.segment.PID
import ca.uhn.hl7v2.model.v25.segment.PV1
import ca.uhn.hl7v2.model.v25.datatype.ST

import MLLPClient

// Crea el mensaje ADT, ver que la versión de HL7 se establece de forma automática
// El tipo de mensaje y evento, si interpreto bien, están determinados por la propia
// clase: tipo de mensaje = "ADT" y evento = "A01"
ADT_A01 adt = new ADT_A01()

MSH mshSegment = adt.getMSH()
mshSegment.getFieldSeparator().setValue("|")
mshSegment.getEncodingCharacters().setValue("^~\\&")
mshSegment.getSendingApplication().getNamespaceID().setValue("1")
mshSegment.getSendingApplication().getUniversalID().setValue("1")

// Nota: los campos de tipo ID tienen que hacer referencia
// a tablas de HL7


// mshSegment.getSendingApplication().getNamespaceID().setValue("TestSendingSystem")
// mshSegment.getSequenceNumber().setValue("123")


mshSegment.getReceivingApplication().getNamespaceID().setValue("2")
mshSegment.getReceivingApplication().getUniversalID().setValue("2")
// Clase TS (timestamp)
mshSegment.getDateTimeOfMessage().getTime().setValue("20181115121200")
mshSegment.getMessageControlID().setValue("1000")
//Definition: This field is used to decide whether to process the message as defined in HL7 Application (level
//        7) Processing rules.


// Represents an HL7 PT (Processing Type) data type. This type consists of the following components:

// Processing ID (ID)
// http://hl7-definition.caristix.com:9010/Default.aspx?version=HL7%20v2.5.1&table=0103

// T = "training"
mshSegment.getProcessingID().getProcessingID().setValue("T")
// Processing Mode (ID)
// http://hl7-definition.caristix.com:9010/Default.aspx?version=HL7%20v2.5.1&table=0207
// T = "Current processing"
mshSegment.getProcessingID().getProcessingMode().setValue("T")

// Nota: la versión de HL7 debería estar implícita, chequear

// Pone datos en el segmento PID
PID pid = adt.getPID()


pid.getPatientName(0).getFamilyName().getSurname().setValue("Pazos")
pid.getPatientName(0).getGivenName().setValue("Pablo")
pid.getPatientIdentifierList(0).getIDNumber().setValue("123456")


PV1 patientVisit = adt.getPV1()

// Ver tabla 3.4.3.2. "I" corresponde a "Inpatient", es decir,
// internación
patientVisit.getPatientClass().setValue("I")


// Los atributos opcionales significativos son:
// Assigned Patient Location (PL)
// Admission Type (Internación)
// Admit date time
// Supongo que discharge sólo vale para las altas
// Discharge date time




// Represents an HL7 PL (Person Location) data type. This type consists of the following components:

// Point of Care (IS)
// Room (IS)
// Bed (IS)
// Facility (HD)
// Location Status (IS)
// Person Location Type (IS)
// Building (IS)
// Floor (IS)
// Location Description (ST)
// Comprehensive Location Identifier (EI)
// Assigning Authority for Location (HD)

// De http://www.hl7.eu/refactored/dtPL.html
// This data type contains several location identifiers that should be thought of in the following order
// from the most general to the most specific: facility, building, floor, point of care, room, bed.
// Additional data about any location defined by these components can be added in the following components: person location type, location description and location status.

patientVisit.getAssignedPatientLocation().getFacility().getNamespaceID().setValue("SistemaPublicoGCBA")
patientVisit.getAssignedPatientLocation().getFacility().getUniversalID().setValue("HospArgerich")
patientVisit.getAssignedPatientLocation().getBuilding().setValue("Edificio Principal")
patientVisit.getAssignedPatientLocation().getFloor().setValue("1er piso")
patientVisit.getAssignedPatientLocation().getBed().setValue("301")
// Facility es de tipo HD (Hierarchic designator)
// The facility ID, the optional fourth component of each patient location field
// is required for
// HL7 implementations that have more than a single healthcare facility with bed locations, since the same
// <point of care> ^ <room> ^ <bed> combination may exist at more than one facility.
// patientVisit.getAssignedPatientLocation().getFacility().setValue
patientVisit.getAdmitDateTime().getTime().setValue("20181116081500")

HapiContext context = new DefaultHapiContext();
Parser parser = context.getPipeParser();
String encodedMessage = parser.encode(adt);

cli = new MLLPClient(encodedMessage)

//println "ER7"

// Normalize de Groovy permite mostrar los <CR> (fin de segmento) que es el enter de Linux, como <CR><LF> que es el enter de Windows.
// Sin esto, se verían todos los segmentos en la misma línea cuando trabajamos en Windows.
//println encodedMessage.normalize()


// Ahora se muestra en XML
//parser = context.getXMLParser();
//encodedMessage = parser.encode(adt);
//println "XML Encoded Message:"
//println encodedMessage