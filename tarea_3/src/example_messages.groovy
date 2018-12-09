import config
import ORMMessage
import MLLPClient
import PatientData

pd = new PatientData()

// Mensaje 1 =============================================================
def timestamp1 = "20181210000000" 
String message1ControlID = "100001"
String dicomCode1 = "100"

ormMessage1 = new ORMMessage()
ormMessage1.initPIDSegment(pd.patient1Map)
// "NW": nueva orden
ormMessage1.setORCSegment("NW", message1ControlId, timestamp1)
// Indico la práctica solicitada (Universal Service Identifier)
// Uso codificación Loinc ("LN")
ormMessage1.setOBRSegmentUniversalServiceIdentifier(
        "39345-4",
        "XR Knee - left Sunrise and (Tunnel W standing)", "LN",
        "39345-4",
        "XR Knee - left Sunrise and (Tunnel W standing)", "LN")
// Text sent by the placer will be returned with the results.
ormMessage1.setOBRSegmentPlacerField1("XR999000")
// Text sent by the placer will be returned with the results.
ormMessage1.setOBRSegmentPlacerField2("XR999000")
// Este atributo es el que muestra dcm4chee, repito el valor de USI
ormMessage1.setOBRSegmentProcedureCode(
        "39345-4",
        "XR Knee - left Sunrise and (Tunnel W standing)", "LN")

// Ubicación del paciente, para el segmento PV1
def patient1LocationMap = [:]
patient1LocationMap["pointOfCare"] = ""
patient1LocationMap["facilityNameSpace"] = "GCBA_SIST_PUBLICO"
patient1LocationMap["facilityUniversalID"] = "FACILITY_HOSP_ARGERICH_SERV_CLINICA_MEDICA"
patient1LocationMap["building"] = "Edificio Principal"
ormMessage1.initPV1Segment(timestamp1, patient1LocationMap)

// Identificador del mensaje
ormMessage1.setMessageControlID(message1ControlID)
ormMessage1.setDateTimeOfMessage(timestamp1)

// Sending/Receiving Application/Facility
ormMessage1.setSendingApplication("GCBA_SIST_PUBLICO", "APP_SIGEHOS_HCE")
ormMessage1.setSendingFacility("GCBA_SIST_PUBLICO",
        "FACILITY_HOSP_ARGERICH_SERV_CLINICA_MEDICA")
ormMessage1.setReceivingApplication("GCBA_SIST_PUBLICO",
        "APP_RIS_ARGERICH")
ormMessage1.setReceivingFacility("GCBA_SIST_PUBLICO",
        "FACILITY_HOSP_ARGERICH_SERV_DIAG_IMAGENES")

// Segmento requerido por dcm4chee
ormMessage1.setZDSSegment("1.2.3", dicomCode1, "Application", "DICOM")

String encodedORMMessage1 = ormMessage1.er7Encode()
// Envío el mensaje 1
def MLLPClient cli1 = new MLLPClient(config.SERVER_HOST,
    config.SERVER_PORT, message1ControlID, encodedORMMessage1)
// =======================================================================


// Mensaje 2 =============================================================
def timestamp2 = "20181211000000" 
String message2ControlID = "100002"
def dicomCode2 = "200"

ormMessage2 = new ORMMessage()
ormMessage2.initPIDSegment(pd.patient1Map)
// "NW": nueva orden
ormMessage2.setORCSegment("NW", message2ControlID, timestamp2)
ormMessage2.setOBRSegmentUniversalServiceIdentifier(
        "36781-3",
        "MRA Abdominal veins", "LN",
        "36781-3",
        "MRA Abdominal veins", "LN")
ormMessage2.setOBRSegmentPlacerField1(message2ControlID)
ormMessage2.setOBRSegmentPlacerField2(message2ControlID)
ormMessage2.setOBRSegmentProcedureCode(
        "36781-3",
        "MRA Abdominal veins", "LN")

def patient2LocationMap = [:]

patient2LocationMap["pointOfCare"] = ""
patient2LocationMap["facilityNameSpace"] = "GCBA_SIST_PUBLICO"
patient2LocationMap["facilityUniversalID"] = "FACILITY_HOSP_ARGERICH_SERV_TRAUMATOLOGIA"
patient2LocationMap["building"] = "Edificio 2"

ormMessage2.initPV1Segment(timestamp2, patient2LocationMap)
ormMessage2.setMessageControlID(message1ControlID)
ormMessage2.setDateTimeOfMessage(timestamp2)
ormMessage2.setSendingApplication("GCBA_SIST_PUBLICO", "APP_SIGEHOS_HCE")
ormMessage2.setSendingFacility("GCBA_SIST_PUBLICO",
        "FACILITY_HOSP_ARGERICH_SERV_CLINICA_MEDICA")
ormMessage2.setReceivingApplication("GCBA_SIST_PUBLICO",
        "APP_RIS_ARGERICH")
ormMessage2.setReceivingFacility("GCBA_SIST_PUBLICO",
        "FACILITY_HOSP_ARGERICH_SERV_DIAG_IMAGENES")
ormMessage2.setZDSSegment("1.2.3", dicomCode2, "Application", "DICOM")

String encodedORMMessage2 = ormMessage2.er7Encode()
def MLLPClient cli2 = new MLLPClient(config.SERVER_HOST, config.SERVER_PORT,
    message1ControlID, encodedORMMessage2)
// =======================================================================


// Mensaje 3 =============================================================
def timestamp3 = "20181212000000" 
String message3ControlID = "100002"
dicomCode3 = "300"

ormMessage3 = new ORMMessage()
ormMessage3.initPIDSegment(pd.patient1Map)
// "NW": nueva orden
ormMessage3.setORCSegment("NW", message3ControlID, timestamp3)
ormMessage3.setOBRSegmentUniversalServiceIdentifier(
        "80056-6",
        "Mitral valve A-wave duration by US.doppler", "LN",
        "80056-6",
        "Mitral valve A-wave duration by US.doppler", "LN")
ormMessage3.setOBRSegmentPlacerField1(message3ControlID)
ormMessage3.setOBRSegmentPlacerField2(message3ControlID)
ormMessage3.setOBRSegmentProcedureCode(
        "80056-6",
        "Mitral valve A-wave duration by US.doppler", "LN")

def patient3LocationMap = [:]
patient3LocationMap["patient3VisitpointOfCare"] = ""
patient3LocationMap["patient3VisitFacilityNameSpace"] = "GCBA_SIST_PUBLICO"
patient3LocationMap["patient3VisitFacilityUniversalID"] = "FACILITY_HOSP_ARGERICH_SERV_PEDIATRIA"
patient3LocationMap["patient3VisitBuilding"] = "Edificio 3"

ormMessage3.initPV1Segment(timestamp3, patient3LocationMap)
ormMessage3.setMessageControlID(message3ControlID)
ormMessage3.setDateTimeOfMessage(timestamp3)
ormMessage3.setSendingApplication("GCBA_SIST_PUBLICO", "APP_SIGEHOS_HCE")
ormMessage3.setSendingFacility("GCBA_SIST_PUBLICO",
        "FACILITY_HOSP_ARGERICH_SERV_CLINICA_MEDICA")
ormMessage3.setReceivingApplication("GCBA_SIST_PUBLICO",
        "APP_RIS_ARGERICH")
ormMessage3.setReceivingFacility("GCBA_SIST_PUBLICO",
        "FACILITY_HOSP_ARGERICH_SERV_DIAG_IMAGENES")
ormMessage3.setZDSSegment("1.2.3", dicomCode3, "Application", "DICOM")

String encodedORMMessage3 = ormMessage3.er7Encode()
def MLLPClient cli3 = new MLLPClient(config.SERVER_HOST, config.SERVER_PORT,
    message1ControlID, encodedORMMessage3)
// =======================================================================
