import ADTMessage

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

def patient = ""
adtMessage = new ADTMessage(patient, "20181115121200", "1000", locationMap, "20181116081500")
adtMessage.sendMessage()
