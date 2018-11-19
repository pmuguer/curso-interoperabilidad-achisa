// Se importan las clases requeridas para mensajes de tipo ADT
// Al importar tener en cuenta la version, aquí es 2.5
import ca.uhn.hl7v2.model.v25.message.ADT_A01
import ca.uhn.hl7v2.model.v25.segment.PID
import ca.uhn.hl7v2.model.v25.segment.PV1

import HL7Message

class ADTMessage extends HL7Message {
    // Clase que simplifica la creación de un mensaje ADT, usando HAPI
    // para la generación del mensaje y los segmentos que lo componen

    // Constructor, recibe el paciente para el que se crearán los mensajes
    def ADTMessage(patient, dateTimeOfMessage, messageControlID, location, admitDateTime) {
        // Genera un nuevo mensaje ADT, con los siguientes datos:
        // patient: mapa con los datos del paciente
        // dateTimeOfMessage: string en formato "YYYYMMDDHHMMSS"
        // messageControlID: string con id que identificará el mensaje
        // location: mapa con los datos de la ubicación de la internación
        // admitDateTime: string en formato "YYYYMMDDHHMMSS"

        // Genero una nueva instancia de un mensaje ADT
        this.msg = new ADT_A01()
       
        // Genero el MSH con los atributos que van en todos los mensajes 
        this.initMSH()

        // Inicializo el segmento PID con los datos del paciente
        this.initPIDSegment(patient)

        // Registro el timestamp del mensaje
        this.msg.getMSH().getDateTimeOfMessage().getTime().setValue(dateTimeOfMessage)
        // Indico el id de control
        this.msg.getMSH().getMessageControlID().setValue(messageControlID)

        // Registro los datos del segmento PV1 (patient visit)
        this.setPV1Data(admitDateTime, location)
    }

    def initPIDSegment(patient) {
        // Registro los datos del paciente en el segmento PID
        PID pid = this.msg.getPID()
        
        // Registro apellido, nombre, fecha de nacimiento y sexo
        pid.getPatientName(0).getFamilyName().getSurname().setValue(patient["surName"])
        pid.getPatientName(0).getGivenName().setValue(patient["givenName"])
        // Fecha de nacimiento: 21/08/1921
        pid.getDateTimeOfBirth().getTime().setValue(patient["dateTimeOfBirth"])
        pid.getAdministrativeSex().setValue(patient["administrativeSex"])
       
        // Registro id0: Identificador único dentro del sistema público del GCBA
        pid.getPatientIdentifierList(0).getAssigningAuthority().getNamespaceID().setValue(patient["id0"]["assigningAuthority"])
        pid.getPatientIdentifierList(0).getIDNumber().setValue(patient["id0"]["idNumber"])
        // Registro id1: Identificador único para Argentina (DNI, asignado por RENAPER)
        pid.getPatientIdentifierList(1).getAssigningAuthority().getNamespaceID().setValue(patient["id1"]["assigningAuthority"])
        pid.getPatientIdentifierList(1).getIDNumber().setValue(patient["id1"]["idNumber"])
    }

    def setPV1Data(admitDateTime, locationMap) {
        // Agrego los datos del segmento PV1, que forma parte del mensaje ADT
        PV1 patientVisit = this.msg.getPV1()

        // Los atributos opcionales significativos son:
        // Assigned Patient Location (PL)
        // Admission Type (Internación)
        // Admit date time
        // Discharge date time (supongo que sólo vale para las altas)

        // Indico patientClass. 
        // Ver tabla 3.4.3.2. "I" corresponde a "Inpatient" (internación)
        patientVisit.getPatientClass().setValue("I")

        // Asigned Patient Location: Represents an HL7 PL (Person Location) data type. This type consists of the following components:
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
        // Additional data about any location defined by these components can be added in the following components:
        // person location type, location description and location status.

        // Facility es de tipo HD (Hierarchic designator)
        // The facility ID, the optional fourth component of each patient location field
        // is required for
        // HL7 implementations that have more than a single healthcare facility with bed locations, since the same
        // <point of care> ^ <room> ^ <bed> combination may exist at more than one facility.
        // patientVisit.getAssignedPatientLocation().getFacility().setValue

        patientVisit.getAssignedPatientLocation().getFacility().getNamespaceID().setValue(locationMap["nameSpaceID"])
        patientVisit.getAssignedPatientLocation().getFacility().getUniversalID().setValue(locationMap["universalID"])
        patientVisit.getAssignedPatientLocation().getBuilding().setValue(locationMap["building"])
        patientVisit.getAssignedPatientLocation().getFloor().setValue(locationMap["floor"])
        patientVisit.getAssignedPatientLocation().getBed().setValue(locationMap["bed"])
        
        // Registro la fecha y hora del ingreso
        patientVisit.getAdmitDateTime().getTime().setValue("20181116081500")
    }
}
