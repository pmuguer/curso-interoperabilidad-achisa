// Se importan las clases requeridas para mensajes de tipo ADT
// Al importar tener en cuenta la version, aquí es 2.5
import ca.uhn.hl7v2.model.v25.message.ORM_O01
import ca.uhn.hl7v2.model.v25.group.ORM_O01_PATIENT
import ca.uhn.hl7v2.model.v25.segment.PID
import ca.uhn.hl7v2.model.v25.segment.ORC
import ca.uhn.hl7v2.model.v25.group.ORM_O01_ORDER_DETAIL
import ca.uhn.hl7v2.model.v25.group.ORM_O01_OBSERVATION
import ca.uhn.hl7v2.model.v25.segment.OBR
import ca.uhn.hl7v2.model.v25.segment.PV1
import ca.uhn.hl7v2.util.Terser

class ORMMessage extends HL7Message {
    // Clase que simplifica la creación de un mensaje ORM, usando HAPI
    // para la generación del mensaje y los segmentos que lo componen

    // Constructor, recibe el paciente para el que se crearán los mensajes
    def ORMMessage() {
        // Genera un nuevo mensaje ORM
        // Genero una nueva instancia de un mensaje ORM
        this.msg = new ORM_O01()
        // Genero el MSH con los atributos que van en todos los mensajes 
        this.initMSH()
    }

    def initMSH() {
        // Inicialización del header; primero uso la inicialización de la clase
        // base (HL7Message)        
        super.initMSH()
        // A continuación agrego los campos específicos para mensajes ORM
        def mshSegment = this.msg.getMSH()
        // Indico el tipo de mensaje (Cap 02, página 99)
        mshSegment.getMessageType().getMessageCode().setValue("ORM")
        // Indico el evento (Cap 02, página 101)
        mshSegment.getMessageType().getTriggerEvent().setValue("O01")
        // Indico la estructura del mensaje (Cap 02, página 102)
        mshSegment.getMessageType().getMessageStructure().setValue("ORM_O01")
    }

    def initPIDSegment(patient) {
        // patient: mapa con los datos del paciente
        // Registro los datos del paciente en el segmento PID
        ORM_O01_PATIENT orm_patient = this.msg.getPATIENT()
        PID pid = orm_patient.getPID()

        // Registro apellido, nombre, fecha de nacimiento y sexo
        pid.getPatientName(0).getFamilyName().getSurname().setValue(patient["surName"])
        pid.getPatientName(0).getGivenName().setValue(patient["givenName"])
        pid.getDateTimeOfBirth().getTime().setValue(patient["dateTimeOfBirth"])
        pid.getAdministrativeSex().setValue(patient["administrativeSex"])

        // Registro id0: Identificador único dentro del sistema público del GCBA
        pid.getPatientIdentifierList(0).getAssigningAuthority().getNamespaceID().setValue(patient["id0"]["assigningAuthority"])
        pid.getPatientIdentifierList(0).getIDNumber().setValue(patient["id0"]["idNumber"])
        // Registro id1: Identificador único para Argentina (DNI, asignado por RENAPER)
        pid.getPatientIdentifierList(1).getAssigningAuthority().getNamespaceID().setValue(patient["id1"]["assigningAuthority"])
        pid.getPatientIdentifierList(1).getIDNumber().setValue(patient["id1"]["idNumber"])
    }

    def initPV1Segment(admitDateTime, locationMap) {
        // Agrego los datos del segmento PV1
        // admitDateTime: string en formato "YYYYMMDDHHMMSS"
        // location: mapa con los datos de la ubicación

        PV1 patientVisit = this.msg.getPATIENT().getPATIENT_VISIT().getPV1()

        // Ver tabla 3.4.3.2. "O" corresponde a "Outpatient" (consultorios)
        patientVisit.getPatientClass().setValue("O")

        patientVisit.getAssignedPatientLocation().getPointOfCare().setValue(locationMap["pointOfCare"])
        patientVisit.getAssignedPatientLocation().getFacility().getNamespaceID().setValue(locationMap["facilityNameSpaceID"])
        patientVisit.getAssignedPatientLocation().getFacility().getUniversalID().setValue(locationMap["facilityUniversalID"])
        patientVisit.getAssignedPatientLocation().getBuilding().setValue(locationMap["building"])

        // Registro la fecha y hora de la
        patientVisit.getAdmitDateTime().getTime().setValue(admitDateTime)
    }


    def setORCSegment(orderControl, placerOrderNumber, transactionDateTime) {
        //def ORM_O01_ORDER_DETAIL orderDetail = this.msg.getORDER().getORDER_DETAIL()
        def ORC orcSegment = this.msg.getORDER().getORC()
        orcSegment.getOrderControl().setValue(orderControl)
        //orcSegment.getPlacerOrderNumber().setValue(placerOrderNumber)
        //ordSegment.getTransactionDateTime().setValue(transactionDateTime)
    }

    def setOBRSegmentUniversalServiceIdentifier(identifier, text, nameOfCodingSystem,
                                                alternateIdentifier, alternateText,
                                                nameOfAlternateCodingSystem) {
        // Una solicitud (ORM) puede contener varios segmentos OBR.
        // Esta función inicializa el USI para el primer segmento ORM de la solicitud

        // El atributo Universal Service Identifier es obligatorio,
        // representa la práctica solicitada
        // Es de tipo CE, que consiste de seis atributos
        // Aunque sólo los 3 primeros son obligatorios, dcm4chee muestra los
        // 3 últimos, por lo tanto los seteo también
        def ORM_O01_ORDER_DETAIL orderDetail = this.msg.getORDER().getORDER_DETAIL()

        def OBR obrSegment = orderDetail.getOBR()
        def universalServiceIdentifier = obrSegment.getUniversalServiceIdentifier()
        universalServiceIdentifier.getIdentifier().setValue(identifier)
        universalServiceIdentifier.getText().setValue(text)
        universalServiceIdentifier.getNameOfCodingSystem().setValue(nameOfCodingSystem)
        universalServiceIdentifier.getAlternateIdentifier().setValue(alternateIdentifier)
        universalServiceIdentifier.getAlternateText().setValue(alternateText)
        universalServiceIdentifier.getNameOfAlternateCodingSystem().setValue(nameOfAlternateCodingSystem)

    }

    def setOBRSegmentPlacerField1(placerField1) {
        // Una solicitud (ORM) puede contener varios segmentos OBR.
        // Esta función inicializa el placerField1 para el primer segmento ORM de la solicitud
        // Este atributo (nro 18 de OBR) es requerido por dcm4chee
        def ORM_O01_ORDER_DETAIL orderDetail = this.msg.getORDER().getORDER_DETAIL()
        def OBR obrSegment = orderDetail.getOBR()
        obrSegment.getPlacerField1().setValue(placerField1)
    }

    def setOBRSegmentPlacerField2(placerField2) {
        // Una solicitud (ORM) puede contener varios segmentos OBR.
        // Esta función inicializa el placerField2 para el primer segmento ORM de la solicitud
        // Este atributo (nro 19 de OBR) es requerido por dcm4chee
        def ORM_O01_ORDER_DETAIL orderDetail = this.msg.getORDER().getORDER_DETAIL()
        def OBR obrSegment = orderDetail.getOBR()
        obrSegment.getPlacerField2().setValue(placerField2)
    }

    def setOBRSegmentProcedureCode(identifier, text, nameOfCodingSystem) {
        // Una solicitud (ORM) puede contener varios segmentos OBR.
        // Esta función inicializa el placerField1 para el primer segmento ORM de la solicitud
        // Este atributo (nro 44 de OBR) es requerido por dcm4chee
        def ORM_O01_ORDER_DETAIL orderDetail = this.msg.getORDER().getORDER_DETAIL()

        def OBR obrSegment = orderDetail.getOBR()
        def procedureCode = obrSegment.getProcedureCode()
        procedureCode.getIdentifier().setValue(identifier)
        procedureCode.getText().setValue(text)
        procedureCode.getNameOfCodingSystem().setValue(nameOfCodingSystem)
    }

    def setZDSSegment(field1, field2, field3, field4) {
        // Para dcm4chee es necesario agregar el segmento ZDS
        // La forma de agregar este segmento la obtuve de:
        // * https://hapifhir.github.io/hapi-hl7v2/base/apidocs/ca/uhn/hl7v2/util/Terser.html
        // * https://sourceforge.net/p/hl7api/mailman/message/3794116/
        this.msg.addNonstandardSegment("ZDS")
        Terser terser = new Terser(this.msg)
        terser.set("ZDS-1", field1)
        terser.set("ZDS-2", field2)
        terser.set("ZDS-3", field3)
        terser.set("ZDS-4", field4)
    }

}
