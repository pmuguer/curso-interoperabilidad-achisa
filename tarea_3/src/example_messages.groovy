import config
import ORMMessage
import MLLPClient
import PatientData

pd = new PatientData()

// Mensaje 1 =============================================================
ormMessage1 = new ORMMessage()
ormMessage1.initPIDSegment(pd.patient1Map)
// "NW": nueva orden
ormMessage1.setORCSegment("NW", "141414", "20181210000000")
ormMessage1.setOBRSegmentUniversalServiceIdentifier(
        "39345-4",
        "XR Knee - left Sunrise and (Tunnel W standing)", "LN",
        "39345-4",
        "XR Knee - left Sunrise and (Tunnel W standing)", "LN")
ormMessage1.setOBRSegmentPlacerField1("XR99992")
ormMessage1.setOBRSegmentPlacerField2("XR99991")
ormMessage1.setOBRSegmentProcedureCode(
        "39345-4",
        "XR Knee - left Sunrise and (Tunnel W standing)", "LN")

def patient1LocationMap = [:]
patient1LocationMap["pointOfCare"] = ""
patient1LocationMap["facilityNameSpace"] = "GCBA_SIST_PUBLICO"
patient1LocationMap["facilityUniversalID"] = "FACILITY_HOSP_ARGERICH_SERV_CLINICA_MEDICA"
patient1LocationMap["building"] = "Edificio Principal"

ormMessage1.initPV1Segment("20181209000000", patient1LocationMap)
String message1ControlID = "100199"
ormMessage1.setMessageControlID(message1ControlID)
ormMessage1.setDateTimeOfMessage("201812110000")
ormMessage1.setSendingApplication("GCBA_SIST_PUBLICO", "APP_SIGEHOS_HCE")
ormMessage1.setSendingFacility("GCBA_SIST_PUBLICO",
        "FACILITY_HOSP_ARGERICH_SERV_CLINICA_MEDICA")
ormMessage1.setReceivingApplication("GCBA_SIST_PUBLICO",
        "APP_RIS_ARGERICH")
ormMessage1.setReceivingFacility("GCBA_SIST_PUBLICO",
        "FACILITY_HOSP_ARGERICH_SERV_DIAG_IMAGENES")
ormMessage1.setZDSSegment("1.2.3", "100", "Application", "DICOM")

String encodedORMMessage1 = ormMessage1.er7Encode()
def MLLPClient cli1 = new MLLPClient(config.SERVER_HOST,
    config.SERVER_PORT, message1ControlID, encodedORMMessage1)
// =======================================================================


// Mensaje 2 =============================================================
ormMessage2 = new ORMMessage()
ormMessage2.initPIDSegment(pd.patient1Map)
// "NW": nueva orden
ormMessage2.setORCSegment("NW", "141414", "20181210000000")
ormMessage2.setOBRSegmentUniversalServiceIdentifier(
        "39345-4",
        "XR Knee - left Sunrise and (Tunnel W standing)", "LN",
        "39345-4",
        "XR Knee - left Sunrise and (Tunnel W standing)", "LN")
ormMessage2.setOBRSegmentPlacerField1("XR99992")
ormMessage2.setOBRSegmentPlacerField2("XR99991")
ormMessage2.setOBRSegmentProcedureCode(
        "39345-4",
        "XR Knee - left Sunrise and (Tunnel W standing)", "LN")

def patient2LocationMap = [:]

patient2LocationMap["pointOfCare"] = ""
patient2LocationMap["facilityNameSpace"] = "GCBA_SIST_PUBLICO"
patient2LocationMap["facilityUniversalID"] = "FACILITY_HOSP_ARGERICH_SERV_TRAUMATOLOGIA"
patient2LocationMap["building"] = "Edificio 2"

ormMessage2.initPV1Segment("20181209000000", patient2LocationMap)
String message2ControlID = "100199"
ormMessage2.setMessageControlID(message1ControlID)
ormMessage2.setDateTimeOfMessage("201812110000")
ormMessage2.setSendingApplication("GCBA_SIST_PUBLICO", "APP_SIGEHOS_HCE")
ormMessage2.setSendingFacility("GCBA_SIST_PUBLICO",
        "FACILITY_HOSP_ARGERICH_SERV_CLINICA_MEDICA")
ormMessage2.setReceivingApplication("GCBA_SIST_PUBLICO",
        "APP_RIS_ARGERICH")
ormMessage2.setReceivingFacility("GCBA_SIST_PUBLICO",
        "FACILITY_HOSP_ARGERICH_SERV_DIAG_IMAGENES")
ormMessage2.setZDSSegment("1.2.3", "100", "Application", "DICOM")

String encodedORMMessage2 = ormMessage2.er7Encode()
def MLLPClient cli2 = new MLLPClient(config.SERVER_HOST, config.SERVER_PORT,
    message1ControlID, encodedORMMessage2)
// =======================================================================



// Mensaje 3 =============================================================
ormMessage3 = new ORMMessage()
ormMessage3.initPIDSegment(pd.patient1Map)
// "NW": nueva orden
ormMessage3.setORCSegment("NW", "141414", "20181210000000")
ormMessage3.setOBRSegmentUniversalServiceIdentifier(
        "39345-4",
        "XR Knee - left Sunrise and (Tunnel W standing)", "LN",
        "39345-4",
        "XR Knee - left Sunrise and (Tunnel W standing)", "LN")
ormMessage3.setOBRSegmentPlacerField1("XR99992")
ormMessage3.setOBRSegmentPlacerField2("XR99991")
ormMessage3.setOBRSegmentProcedureCode(
        "39345-4",
        "XR Knee - left Sunrise and (Tunnel W standing)", "LN")

def patient3LocationMap = [:]
patient3LocationMap["patient3VisitpointOfCare"] = ""
patient3LocationMap["patient3VisitFacilityNameSpace"] = "GCBA_SIST_PUBLICO"
patient3LocationMap["patient3VisitFacilityUniversalID"] = "FACILITY_HOSP_ARGERICH_SERV_PEDIATRIA"
patient3LocationMap["patient3VisitBuilding"] = "Edificio 3"

ormMessage3.initPV1Segment("20181209000000", patient3LocationMap)
String message3ControlID = "100199"
ormMessage3.setMessageControlID(message3ControlID)
ormMessage3.setDateTimeOfMessage("201812110000")
ormMessage3.setSendingApplication("GCBA_SIST_PUBLICO", "APP_SIGEHOS_HCE")
ormMessage3.setSendingFacility("GCBA_SIST_PUBLICO",
        "FACILITY_HOSP_ARGERICH_SERV_CLINICA_MEDICA")
ormMessage3.setReceivingApplication("GCBA_SIST_PUBLICO",
        "APP_RIS_ARGERICH")
ormMessage3.setReceivingFacility("GCBA_SIST_PUBLICO",
        "FACILITY_HOSP_ARGERICH_SERV_DIAG_IMAGENES")
ormMessage3.setZDSSegment("1.2.3", "100", "Application", "DICOM")

String encodedORMMessage3 = ormMessage3.er7Encode()
def MLLPClient cli3 = new MLLPClient(config.SERVER_HOST, config.SERVER_PORT,
    message1ControlID, encodedORMMessage3)
// =======================================================================
