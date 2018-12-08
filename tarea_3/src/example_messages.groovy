//import config
import ORMMessage
//import MLLPClient
import PatientData

pd = new PatientData()
println pd.patient1Map

ormMessage1 = new ORMMessage()
ormMessage1.initPIDSegment(pd.patient1Map)
// "NW": nueva orden
ormMessage1.setORCSegment("NW", "141414", "20181210000000")
ormMessage1.setOBRSegmentUniversalServiceIdentifier("39345-4",
        "XR Knee - left Sunrise and (Tunnel W standing)",
        "LN", "39345-4",
        "XR Knee - left Sunrise and (Tunnel W standing)", "LN")
ormMessage1.setOBRSegmentPlacerField1("XR999995")
ormMessage1.setOBRSegmentProcedureCode("39345-4", "XR Knee - left Sunrise and (Tunnel W standing)",
        "LN")

def patient1LocationMap = [:]
patient1LocationMap["pointOfCare"] = ""
patient1LocationMap["facilityNameSpace"] = "GCBA_SIST_PUBLICO"
patient1LocationMap["facilityUniversalID"] = "FACILITY_HOSP_ARGERICH_SERV_CLINICA_MEDICA"
patient1LocationMap["building"] = "Edificio Principal"


//initPV1Segment()

def patient2LocationMap = [:]
patient2LocationMap["pointOfCare"] = ""
patient2LocationMap["facilityNameSpace"] = "GCBA_SIST_PUBLICO"
patient2LocationMap["facilityUniversalID"] = "FACILITY_HOSP_ARGERICH_SERV_TRAUMATOLOGIA"
patient2LocationMap["building"] = "Edificio 2"
//initPV1Segment()

def patient3LocationMap = [:]
patient3LocationMap["patient3VisitpointOfCare"] = ""
patient3LocationMap["patient3VisitFacilityNameSpace"] = "GCBA_SIST_PUBLICO"
patient3LocationMap["patient3VisitFacilityUniversalID"] = "FACILITY_HOSP_ARGERICH_SERV_PEDIATRIA"
patient3LocationMap["patient3VisitBuilding"] = "Edificio 3"


// ============================================================================
// Genero mensaje ORM con los datos de la Solicitud 1
//ormMessage1 = new ORMMessage()













/*

// ============================================================================
// Genero mensaje ADT con los datos de la Internación 1
adtMessage1 = new ADTMessage()
def controlID1 = "1001"
adtMessage1.setMessageControlID(controlID1)
adtMessage1.initSendingApplication("1", "1")
adtMessage1.initReceivingApplication("2", "2")
adtMessage1.setDateTimeOfMessage("20181118081615")
adtMessage1.initPIDSegment(patientMap1)
adtMessage1.initPV1Segment("20181118081500", locationMap1)
String encodedMessage1 = adtMessage1.er7Encode()
// ============================================================================


// ============================================================================
// Genero mensaje ADT con los datos de la Internación 2
adtMessage2 = new ADTMessage()
def controlID2 = "1002"
adtMessage2.setMessageControlID(controlID2)
adtMessage2.initSendingApplication("1", "1")
adtMessage2.initReceivingApplication("2", "2")
adtMessage2.setDateTimeOfMessage("20181119105036")
adtMessage2.initPIDSegment(patientMap2)
adtMessage2.initPV1Segment("20181119103025", locationMap2)
String encodedMessage2 = adtMessage2.er7Encode()
// ============================================================================


// ============================================================================
// Genero mensaje ADT con los datos de la Internación 2
adtMessage3 = new ADTMessage()
def controlID3 = "1003"
adtMessage3.setMessageControlID(controlID3)
adtMessage3.initSendingApplication("1", "1")
adtMessage3.initReceivingApplication("2", "2")
adtMessage3.setDateTimeOfMessage("20181119172531")
adtMessage3.initPIDSegment(patientMap3)
adtMessage3.initPV1Segment("20181119151535", locationMap3)
String encodedMessage3 = adtMessage3.er7Encode()
// ============================================================================


// ============================================================================
// Genero conexiones con el server MLLP para enviar los tres mensajes
println("\nEnviando mensaje ADT; valor del segmento MSH-10 (Message Control ID) = " + adtMessage1.getMessageControlID())
//println encodedMessage.normalize()
def MLLPClient cli1 = new MLLPClient(config.SERVER_HOST, config.SERVER_PORT, controlID1, encodedMessage1)
println("\nEnviando mensaje ADT; valor del segmento MSH-10 (Message Control ID) = " + adtMessage2.getMessageControlID())
def MLLPClient cli2 = new MLLPClient(config.SERVER_HOST, config.SERVER_PORT, controlID2, encodedMessage2)
println("\nEnviando mensaje ADT; valor del segmento MSH-10 (Message Control ID) = " + adtMessage3.getMessageControlID())
def MLLPClient cli3 = new MLLPClient(config.SERVER_HOST, config.SERVER_PORT, controlID3, encodedMessage3)

*/
