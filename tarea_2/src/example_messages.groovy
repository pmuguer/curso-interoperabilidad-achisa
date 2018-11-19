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

def patientMap1 = [:]
// Registro apellido, nombre, fecha de nacimiento y sexo
// Como indico que se usa el encoding ISO-8859-1 puedo usar acentos
// (Nota: este archivo está guardado como latin-1)
patientMap1["surName"] = "Ramón"
patientMap1["givenName"] = "Carrillo"
// Fecha de nacimiento: 19/09/1932
patientMap1["dateTimeOfBirth"] = "19200821"
patientMap1["administrativeSex"] = "M"
patientMap1["id0"] = id0
patientMap1["id1"] = id1

def locationMap1 = [:]
locationMap1["nameSpaceID"] = "SistemaPublicoGCBA"
locationMap1["universalID"] = "HospCarrillo"
locationMap1["building"] = "Edificio Principal"
locationMap1["floor"] = "2o piso"
locationMap1["bed"] = "203"

def patientMap2 = [:]
// Registro apellido, nombre, fecha de nacimiento y sexo
patientMap2["surName"] = "Houssay"
patientMap2["givenName"] = "Bernardo"
// Fecha de nacimiento: 21/08/1921
patientMap2["dateTimeOfBirth"] = "19200821"
patientMap2["administrativeSex"] = "M"
patientMap2["id0"] = id0
patientMap2["id1"] = id1

def locationMap2 = [:]
locationMap2["nameSpaceID"] = "SistemaPublicoGCBA"
locationMap2["universalID"] = "HospPiñero"
locationMap2["building"] = "Edificio Principal"
locationMap2["floor"] = "3er piso"
locationMap2["bed"] = "301"


def patientMap3 = [:]
// Registro apellido, nombre, fecha de nacimiento y sexo
patientMap3["surName"] = "Houssay"
patientMap3["givenName"] = "Bernardo"
// Fecha de nacimiento: 21/08/1921
patientMap3["dateTimeOfBirth"] = "19200821"
patientMap3["administrativeSex"] = "M"
patientMap3["id0"] = id0
patientMap3["id1"] = id1

def locationMap3 = [:]
locationMap3["nameSpaceID"] = "SistemaPrivadoGCBA"
locationMap3["universalID"] = "HospItaliano"
locationMap3["building"] = "Edificio Principal"
locationMap3["floor"] = "5o piso"
locationMap3["bed"] = "507"




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
adtMessage1.setDateTimeOfMessage("20181118081615")
adtMessage1.initPIDSegment(patientMap1)
adtMessage1.initPV1Segment("20181118081500", locationMap1)
String encodedMessage1 = adtMessage1.er7Encode()

adtMessage2 = new ADTMessage()
def controlID2 = "1002"
adtMessage2.setMessageControlID(controlID2)
adtMessage2.initSendingApplication("1", "1")
adtMessage2.initReceivingApplication("2", "2")
adtMessage2.setDateTimeOfMessage("20181119105036")
adtMessage2.initPIDSegment(patientMap2)
adtMessage2.initPV1Segment("20181119103025", locationMap2)
String encodedMessage2 = adtMessage2.er7Encode()

adtMessage3 = new ADTMessage()
def controlID3 = "1003"
adtMessage3.setMessageControlID(controlID3)
adtMessage3.initSendingApplication("1", "1")
adtMessage3.initReceivingApplication("2", "2")
adtMessage3.setDateTimeOfMessage("20181119172531")
adtMessage3.initPIDSegment(patientMap3)
adtMessage3.initPV1Segment("20181119151535", locationMap3)
String encodedMessage3 = adtMessage3.er7Encode()

println("\nEnviando mensaje ADT; valor del segmento MSH-10 (Message Control ID) = " + adtMessage1.getMessageControlID())
//println encodedMessage.normalize()
def MLLPClient cli1 = new MLLPClient(config.SERVER_HOST, config.SERVER_PORT, controlID1, encodedMessage1)
println("\nEnviando mensaje ADT; valor del segmento MSH-10 (Message Control ID) = " + adtMessage2.getMessageControlID())
def MLLPClient cli2 = new MLLPClient(config.SERVER_HOST, config.SERVER_PORT, controlID2, encodedMessage2)
println("\nEnviando mensaje ADT; valor del segmento MSH-10 (Message Control ID) = " + adtMessage3.getMessageControlID())
def MLLPClient cli3 = new MLLPClient(config.SERVER_HOST, config.SERVER_PORT, controlID3, encodedMessage3)
