// Se importan las clases requeridas para mensajes de tipo ADT
// Al importar tener en cuenta la version, aquí es 2.5
import ca.uhn.hl7v2.model.v25.message.ADT_A01
import ca.uhn.hl7v2.model.v25.segment.MSH
import ca.uhn.hl7v2.model.v25.segment.PID
import ca.uhn.hl7v2.model.v25.segment.PV1
import ca.uhn.hl7v2.model.v25.datatype.ST

// Se importan las clases necesarias para generar los mensajes
import ca.uhn.hl7v2.DefaultHapiContext
import ca.uhn.hl7v2.HapiContext
import ca.uhn.hl7v2.parser.Parser

// Cliente para enviar mensajes MLLP al server
import MLLPClient

class ADTMessage {
    // Clase que simplifica la creación de un mensaje ADT, usando HAPI
    // para la generación del mensaje y los segmentos que lo componen

    ADT_A01 adt

    // Constructor, recibe el paciente para el que se crearán los mensajes
    def ADTMessage(patient, dateTimeOfMessage, messageControlID, location, admitDateTime) {
        // Genera un nuevo mensaje ADT, con los siguientes datos:
        // patient: mapa con los datos del paciente
        // dateTimeOfMessage: string en formato "YYYYMMDDHHMMSS"
        // messageControlID: string con id que identificará el mensaje
        // location: mapa con los datos de la ubicación de la internación
        // admitDateTime: string en formato "YYYYMMDDHHMMSS"

        // Genero una nueva instancia de un mensaje ADT
        this.adt = new ADT_A01()
       
        // Genero el MSH con los atributos que van en todos los mensajes 
        this.initMSH()

        // Inicializo el segmento PID con los datos del paciente
        this.initPIDSegment(patient)

        // Registro el timestamp del mensaje
        this.adt.getMSH().getDateTimeOfMessage().getTime().setValue(dateTimeOfMessage)
        // Indico el id de control
        this.adt.getMSH().getMessageControlID().setValue(messageControlID)

        // Registro los datos del segmento PV1 (patient visit)
        this.setPV1Data(admitDateTime, location)
    }

    def initMSH() {
        // Inicialización del header, común para todos los mensajes
        def mshSegment = this.adt.getMSH()
        mshSegment.getFieldSeparator().setValue("|")
        mshSegment.getEncodingCharacters().setValue("^~\\&")
        
        // Se indica que la versión de HL7 es 2.5
        mshSegment.getVersionID().getVersionID().setValue("2.5")
        // Indico el tipo de mensaje (Cap 02, página 99)
        mshSegment.getMessageType().getMessageCode().setValue("ADT")
        // Indico el evento (Cap 02, página 101)
        mshSegment.getMessageType().getTriggerEvent().setValue("A01")
        // Indico la estructura del mensaje (Cap 02, página 102)
        mshSegment.getMessageType().getMessageStructure().setValue("ADT_A01")

        // Se registran los datos de la aplicación que genera el mensaje
        mshSegment.getSendingApplication().getNamespaceID().setValue("1")
        mshSegment.getSendingApplication().getUniversalID().setValue("1")

        // Se registran los datos de la aplicación a la que está destinada el mensaje
        mshSegment.getReceivingApplication().getNamespaceID().setValue("2")
        mshSegment.getReceivingApplication().getUniversalID().setValue("2")
        
        // Indico el id de procesamiento (this field is used to decide whether to process the
        // message as defined in HL7 Application (level 7) Processing rules)
        // Represents an HL7 PT (Processing Type) data type. This type consists
        // of the following components:
        //
        //  * Processing ID (ID)
        //  * Processing Mode (ID)

        // Processing ID ("T" corresponde a "training")
        // http://hl7-definition.caristix.com:9010/Default.aspx?version=HL7%20v2.5.1&table=0103
        mshSegment.getProcessingID().getProcessingID().setValue("T")
        
        // Processing Mode ("T" corresponde a "Current processing")
        // http://hl7-definition.caristix.com:9010/Default.aspx?version=HL7%20v2.5.1&table=0207
        mshSegment.getProcessingID().getProcessingMode().setValue("T")
    }

    def initPIDSegment(patient) {
        // Registro los datos del paciente en el segmento PID
        PID pid = this.adt.getPID()
        
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
        PV1 patientVisit = this.adt.getPV1()

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

    def sendMessage() {
        HapiContext context = new DefaultHapiContext();
        Parser parser = context.getPipeParser();
        String encodedMessage = parser.encode(this.adt);
        
        // Normalize de Groovy permite mostrar los <CR> (fin de segmento) que es el enter de Linux, como <CR><LF> que es el enter de Windows.
        // Sin esto, se verían todosadt, admitDateTime los segmentos en la misma línea cuando trabajamos en Windows.
        println("\nEnviando al server el mensaje con el código: " + this.adt.getMSH().getMessageControlID())
        //println encodedMessage.normalize()
        
        def cli = new MLLPClient(encodedMessage)
    }
}
