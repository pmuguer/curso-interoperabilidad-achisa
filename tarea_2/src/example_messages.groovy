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

adtMessage = new ADTMessage(patientMap, "20181115121200", "1000", locationMap, "20181116081500")

String encodedMessage = adtMessage.er7Encode()

println("\nEnviando al server el mensaje con el código: " + adtMessage.getMessageControlID())
//println encodedMessage.normalize()
        
def MLLPClient cli = new MLLPClient(encodedMessage)
