import config
import ADTMessage
import MLLPClient

// Registro id0: Identificador único dentro del sistema público del GCBA
def id0 = [:]
id0["assigningAuthority"] = "HCE_GCBA"
id0["idNumber"] = "201324"

// Registro id1: Identificador único para Argentina (DNI, asignado por RENAPER)
def id1 = [:]
id1["assigningAuthority"] = "RENAPER"
id1["idNumber"] = "10324121"

def patientMap = [:]
// Registro apellido, nombre, fecha de nacimiento y sexo
patientMap["surName"] = "Houssay"
patientMap["givenName"] = "Bernardo"
// Fecha de nacimiento: 21/08/1921
patientMap["dateTimeOfBirth"] = "19200821"
patientMap["administrativeSex"] = "M"
patientMap["id0"] = id0
patientMap["id1"] = id1

def locationMap = [:]
locationMap["nameSpaceID"] = "SistemaPublicoGCBA"
locationMap["universalID"] = "HospArgerich"
locationMap["building"] = "Edificio Principal"
locationMap["floor"] = "3er piso"
locationMap["bed"] = "301"

// Creo un mensaje ADT con las siguientes características:
// * para el paciente patient
// * con timestamp del mensaje = "20181115121200"
// * con id del mensaje = 1000
// * con ubicación del ingreso locationMap
// * con fecha y hora del ingreso = "20181116081500"

adtMessage1 = new ADTMessage()
def controlID1 = "1001"
adtMessage1.setMessageControlID(controlID1)
adtMessage1.initSendingApplication("1", "1")
adtMessage1.initReceivingApplication("2", "2")
adtMessage1.setDateTimeOfMessage("20181115121200")
adtMessage1.initPIDSegment(patientMap)
adtMessage1.initPV1Segment("20181116081500", locationMap)
String encodedMessage1 = adtMessage1.er7Encode()

adtMessage2 = new ADTMessage()
def controlID2 = "1002"
adtMessage2.setMessageControlID(controlID2)
adtMessage2.initSendingApplication("1", "1")
adtMessage2.initReceivingApplication("2", "2")
adtMessage2.setDateTimeOfMessage("20181115121200")
adtMessage2.initPIDSegment(patientMap)
adtMessage2.initPV1Segment("20181116081500", locationMap)
String encodedMessage2 = adtMessage2.er7Encode()

adtMessage3 = new ADTMessage()
def controlID3 = "1003"
adtMessage3.setMessageControlID(controlID3)
adtMessage3.initSendingApplication("1", "1")
adtMessage3.initReceivingApplication("2", "2")
adtMessage3.setDateTimeOfMessage("20181115121200")
adtMessage3.initPIDSegment(patientMap)
adtMessage3.initPV1Segment("20181116081500", locationMap)
String encodedMessage3 = adtMessage3.er7Encode()

println("\nEnviando mensaje ADT; valor del segmento MSH-10 (Message Control ID) = " + adtMessage1.getMessageControlID())
//println encodedMessage.normalize()
def MLLPClient cli1 = new MLLPClient(config.SERVER_HOST, config.SERVER_PORT, controlID1, encodedMessage1)
println("\nEnviando mensaje ADT; valor del segmento MSH-10 (Message Control ID) = " + adtMessage2.getMessageControlID())
def MLLPClient cli2 = new MLLPClient(config.SERVER_HOST, config.SERVER_PORT, controlID2, encodedMessage2)
println("\nEnviando mensaje ADT; valor del segmento MSH-10 (Message Control ID) = " + adtMessage3.getMessageControlID())
def MLLPClient cli3 = new MLLPClient(config.SERVER_HOST, config.SERVER_PORT, controlID3, encodedMessage3)
