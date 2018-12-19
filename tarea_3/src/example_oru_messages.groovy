import config
import ORUMessage
import MLLPClient
import PatientData

pd = new PatientData()

// Mensaje 1 =============================================================
def timestamp1 = "20181210000000" 
String message1ControlID = "22221"
// String dicomCode1 = "100"

oruMessage1 = new ORUMessage()
oruMessage1.initPIDSegment(pd.patient1Map)
/*
oruMessage1.setORCSegment("NW", message1ControlID, timestamp1)
*/

/* // Indico la práctica solicitada (Universal Service Identifier)
// Uso codificación Loinc ("LN")
oruMessage1.setOBRSegmentUniversalServiceIdentifier(
        "39345-4",
        "XR Knee - left Sunrise and (Tunnel W standing)", "LN",
        "39345-4",
        "XR Knee - left Sunrise and (Tunnel W standing)", "LN")
*/

// Text sent by the placer will be returned with the results.
// ormMessage1.setOBRSegmentPlacerField1("XR999000")
// Text sent by the placer will be returned with the results.
// ormMessage1.setOBRSegmentPlacerField2("XR999000")
// Este atributo es el que muestra dcm4chee, repito el valor de USI

// ormMessage1.setOBRSegmentProcedureCode(
//        "39345-4",
//        "XR Knee - left Sunrise and (Tunnel W standing)", "LN")

// Ubicación del paciente, para el segmento PV1
def patient1LocationMap = [:]
patient1LocationMap["pointOfCare"] = ""
patient1LocationMap["facilityNameSpace"] = "GCBA_SIST_PUBLICO"
patient1LocationMap["facilityUniversalID"] = "FACILITY_HOSP_ARGERICH_SERV_CLINICA_MEDICA"
patient1LocationMap["building"] = "Edificio Principal"
oruMessage1.initPV1Segment(timestamp1, patient1LocationMap)

// Identificador del mensaje
oruMessage1.setMessageControlID(message1ControlID)
oruMessage1.setDateTimeOfMessage(timestamp1)

// Sending/Receiving Application/Facility
oruMessage1.setSendingApplication("GCBA_SIST_PUBLICO", "APP_SIGEHOS_HCE")
oruMessage1.setSendingFacility("GCBA_SIST_PUBLICO",
        "FACILITY_HOSP_ARGERICH_SERV_CLINICA_MEDICA")
oruMessage1.setReceivingApplication("GCBA_SIST_PUBLICO",
        "APP_RIS_ARGERICH")
oruMessage1.setReceivingFacility("GCBA_SIST_PUBLICO",
        "FACILITY_HOSP_ARGERICH_SERV_DIAG_IMAGENES")


// para que los mensajes funcionen hay que convertir string a ¿? (TS de HAPI)
//oruMessage1.setOBRSegmentObservationDateTime(timestamp1)
//oruMessage1.setOBRSegmentResultsRptStatusChngDateTime(timestamp1)
oruMessage1.setOBXSegmentStudyInstanceUID("1.2.3.4.5")
oruMessage1.setOBXSegmentSeriesInstanceUID("2.3.4.5.6")
oruMessage1.setOBXSegmentSOPInstanceUID("3.4.5.6.7")
oruMessage1.setOBXSegmentSRInstanceUID("4.5.6.7.8")
oruMessage1.setOBXSegmentSRText("Resultado de RX")
/*

String encodedORUMessage1 = oruMessage1.er7Encode()
// Envío el mensaje 1
def MLLPClient cli1 = new MLLPClient(config.SERVER_HOST,
    config.SERVER_PORT, message1ControlID, encodedORUMessage1)
// =======================================================================

*/
