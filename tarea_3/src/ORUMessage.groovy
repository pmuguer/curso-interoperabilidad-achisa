// Se importan las clases requeridas para mensajes de tipo ORU
// Al importar tener en cuenta la version, aquí es 2.5
import ca.uhn.hl7v2.model.v25.message.ORU_R01
import ca.uhn.hl7v2.model.v25.group.ORU_R01_PATIENT
import ca.uhn.hl7v2.model.v25.segment.PID
import ca.uhn.hl7v2.model.v25.segment.OBR
import ca.uhn.hl7v2.model.v25.segment.PV1
import ca.uhn.hl7v2.model.v25.group.ORU_R01_PATIENT_RESULT
import ca.uhn.hl7v2.model.v25.group.ORU_R01_ORDER_OBSERVATION
import ca.uhn.hl7v2.model.v25.group.ORU_R01_SPECIMEN
import ca.uhn.hl7v2.model.v25.segment.OBX
import ca.uhn.hl7v2.model.v25.datatype.HD
import ca.uhn.hl7v2.model.v25.datatype.TX


class ORUMessage extends HL7Message {
    // Clase que simplifica la creación de un mensaje ORM, usando HAPI
    // para la generación del mensaje y los segmentos que lo componen

    // Constructor, recibe el paciente para el que se crearán los mensajes
    def ORUMessage() {
        // Genera un nuevo mensaje ORM
        // Genero una nueva instancia de un mensaje ORM
        this.msg = new ORU_R01()
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
        mshSegment.getMessageType().getMessageCode().setValue("ORU")
        // Indico el evento (Cap 02, página 101)
        mshSegment.getMessageType().getTriggerEvent().setValue("R01")
        // Indico la estructura del mensaje (Cap 02, página 102)
        mshSegment.getMessageType().getMessageStructure().setValue("ORU_R01")
    }

    def initPIDSegment(patient) {
        // patient: mapa con los datos del paciente
        // Registro los datos del paciente en el segmento PID
        def ORU_R01_PATIENT_RESULT patientResult = this.msg.getPATIENT_RESULT()
        def ORU_R01_PATIENT oru_patient = patientResult.getPATIENT()
        def PID pid = oru_patient.getPID()

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

        PV1 patientVisit = this.msg.getPATIENT_RESULT().getPATIENT().getVISIT().getPV1()

        // Ver tabla 3.4.3.2. "O" corresponde a "Outpatient" (consultorios)
        patientVisit.getPatientClass().setValue("O")

        patientVisit.getAssignedPatientLocation().getPointOfCare().setValue(locationMap["pointOfCare"])
        patientVisit.getAssignedPatientLocation().getFacility().getNamespaceID().setValue(locationMap["facilityNameSpaceID"])
        patientVisit.getAssignedPatientLocation().getFacility().getUniversalID().setValue(locationMap["facilityUniversalID"])
        patientVisit.getAssignedPatientLocation().getBuilding().setValue(locationMap["building"])

        // Registro la fecha y hora de la
        patientVisit.getAdmitDateTime().getTime().setValue(admitDateTime)
    }

    /*
    def setORCSegment(orderControl, placerOrderNumber, transactionDateTime) {
        //def ORM_O01_ORDER_DETAIL orderDetail = this.msg.getORDER().getORDER_DETAIL()
        def ORC orcSegment = this.msg.getORDER().getORC()
        orcSegment.getOrderControl().setValue(orderControl)
        //orcSegment.getPlacerOrderNumber().setValue(placerOrderNumber)
        //ordSegment.getTransactionDateTime().setValue(transactionDateTime)
    }
    */

    def setOBRSegmentUniversalServiceIdentifier(identifier, text, nameOfCodingSystem,
                                                alternateIdentifier, alternateText,
                                                nameOfAlternateCodingSystem) {

        def ORU_R01_PATIENT_RESULT patientResult = this.msg.getPATIENT_RESULT()
        def ORU_R01_ORDER_OBSERVATION orderObservation = patientResult.getORDER_OBSERVATION()
        def OBR obrSegment = orderObservation.getOBR()

        def universalServiceIdentifier = obrSegment.getUniversalServiceIdentifier()
        universalServiceIdentifier.getIdentifier().setValue(identifier)
        universalServiceIdentifier.getText().setValue(text)
        universalServiceIdentifier.getNameOfCodingSystem().setValue(nameOfCodingSystem)
        universalServiceIdentifier.getAlternateIdentifier().setValue(alternateIdentifier)
        universalServiceIdentifier.getAlternateText().setValue(alternateText)
        universalServiceIdentifier.getNameOfAlternateCodingSystem().setValue(nameOfAlternateCodingSystem)

    }

    def setOBRSegmentPlacerField1(placerField1) {
        def ORU_R01_PATIENT_RESULT patientResult = this.msg.getPATIENT_RESULT()
        def ORU_R01_ORDER_OBSERVATION orderObservation = patientResult.getORDER_OBSERVATION()
        def OBR obrSegment = orderObservation.getOBR()
        obrSegment.getPlacerField1().setValue(placerField1)
    }

    def setOBRSegmentPlacerField2(placerField2) {
        def ORU_R01_PATIENT_RESULT patientResult = this.msg.getPATIENT_RESULT()
        def ORU_R01_ORDER_OBSERVATION orderObservation = patientResult.getORDER_OBSERVATION()
        def OBR obrSegment = orderObservation.getOBR()
        obrSegment.getPlacerField2().setValue(placerField2)
    }

    def setOBRSegmentProcedureCode(identifier, text, nameOfCodingSystem) {
        def ORU_R01_PATIENT_RESULT patientResult = this.msg.getPATIENT_RESULT()
        def ORU_R01_ORDER_OBSERVATION orderObservation = patientResult.getORDER_OBSERVATION()
        def OBR obrSegment = orderObservation.getOBR()

        def procedureCode = obrSegment.getProcedureCode()
        procedureCode.getIdentifier().setValue(identifier)
        procedureCode.getText().setValue(text)
        procedureCode.getNameOfCodingSystem().setValue(nameOfCodingSystem)
    }

    def setOBRSegmentObservationDateTime(dateTime) {
        def ORU_R01_PATIENT_RESULT patientResult = this.msg.getPATIENT_RESULT()
        def ORU_R01_ORDER_OBSERVATION orderObservation = patientResult.getORDER_OBSERVATION()
        def OBR obrSegment = orderObservation.getOBR()
        obrSegment.getObr7_ObservationDateTime().parse(dateTime)
    }

    def setOBRSegmentResultsRptStatusChngDateTime(dateTime) {
        def ORU_R01_PATIENT_RESULT patientResult = this.msg.getPATIENT_RESULT()
        def ORU_R01_ORDER_OBSERVATION orderObservation = patientResult.getORDER_OBSERVATION()
        def OBR obrSegment = orderObservation.getOBR()
        obrSegment.getObr22_ResultsRptStatusChngDateTime().parse(dateTime)
    }

    def setOBXSegmentStudyInstanceUID(studyInstanceUID) {
        def ORU_R01_PATIENT_RESULT patientResult = this.msg.getPATIENT_RESULT()
        def ORU_R01_ORDER_OBSERVATION orderObservation = patientResult.getORDER_OBSERVATION()
        def ORU_R01_SPECIMEN orderSpecimen = orderObservation.getSPECIMEN()
        def OBX obx = orderSpecimen.getOBX(0)

        obx.getObx1_SetIDOBX().setValue("1")
        // Se indica de qué tipo es el valor que se registrará en el
        // atributo 5 (observation value). En este caso es HD (Hyerarchic Designator)
        obx.getObx2_ValueType().setValue("HD")
        obx.getObx3_ObservationIdentifier().getCe2_Text().setValue("Study Instance UID")
        // Creo una instancia de HD (que es el tipo del atributo OBX-5 indicado
        // en OBX-2, y asigno el valor al atributo OBX-5
        HD hd = new HD(this.msg)
        hd.getHd1_NamespaceID().setValue(studyInstanceUID)
        obx.getObservationValue(0).setData(hd)
    }

    def setOBXSegmentSeriesInstanceUID(seriesInstanceUID) {
        def ORU_R01_PATIENT_RESULT patientResult = this.msg.getPATIENT_RESULT()
        def ORU_R01_ORDER_OBSERVATION orderObservation = patientResult.getORDER_OBSERVATION()
        def ORU_R01_SPECIMEN orderSpecimen = orderObservation.getSPECIMEN()
        def OBX obx = orderSpecimen.getOBX(1)

        obx.getObx1_SetIDOBX().setValue("2")
        // Se indica de qué tipo es el valor que se registrará en el
        // atributo 5 (observation value). En este caso es HD (Hyerarchic Designator)
        obx.getObx2_ValueType().setValue("HD")
        obx.getObx3_ObservationIdentifier().getCe2_Text().setValue("Series Instance UID")
        // Creo una instancia de HD (que es el tipo del atributo OBX-5 indicado
        // en OBX-2, y asigno el valor al atributo OBX-5
        HD hd = new HD(this.msg)
        hd.getHd1_NamespaceID().setValue(seriesInstanceUID)
        obx.getObservationValue(0).setData(hd)
    }

    def setOBXSegmentSOPInstanceUID(sopInstanceUID) {
        def ORU_R01_PATIENT_RESULT patientResult = this.msg.getPATIENT_RESULT()
        def ORU_R01_ORDER_OBSERVATION orderObservation = patientResult.getORDER_OBSERVATION()
        def ORU_R01_SPECIMEN orderSpecimen = orderObservation.getSPECIMEN()
        def OBX obx = orderSpecimen.getOBX(2)

        obx.getObx1_SetIDOBX().setValue("3")
        // Se indica de qué tipo es el valor que se registrará en el
        // atributo 5 (observation value). En este caso es HD (Hyerarchic Designator)
        obx.getObx2_ValueType().setValue("HD")
        obx.getObx3_ObservationIdentifier().getCe2_Text().setValue("SOP Instance UID")
        // Creo una instancia de HD (que es el tipo del atributo OBX-5 indicado
        // en OBX-2, y asigno el valor al atributo OBX-5
        HD hd = new HD(this.msg)
        hd.getHd1_NamespaceID().setValue(sopInstanceUID)
        obx.getObservationValue(0).setData(hd)
    }

    def setOBXSegmentSRInstanceUID(srInstanceUID) {
        def ORU_R01_PATIENT_RESULT patientResult = this.msg.getPATIENT_RESULT()
        def ORU_R01_ORDER_OBSERVATION orderObservation = patientResult.getORDER_OBSERVATION()
        def ORU_R01_SPECIMEN orderSpecimen = orderObservation.getSPECIMEN()
        def OBX obx = orderSpecimen.getOBX(3)

        obx.getObx1_SetIDOBX().setValue("4")
        // Se indica de qué tipo es el valor que se registrará en el
        // atributo 5 (observation value). En este caso es HD (Hyerarchic Designator)
        obx.getObx2_ValueType().setValue("HD")
        obx.getObx3_ObservationIdentifier().getCe2_Text().setValue("SR Instance UID")
        // Creo una instancia de HD (que es el tipo del atributo OBX-5 indicado
        // en OBX-2, y asigno el valor al atributo OBX-5
        HD hd = new HD(this.msg)
        hd.getHd1_NamespaceID().setValue(srInstanceUID)
        obx.getObservationValue(0).setData(hd)
    }

    def setOBXSegmentSRText(srText) {
        def ORU_R01_PATIENT_RESULT patientResult = this.msg.getPATIENT_RESULT()
        def ORU_R01_ORDER_OBSERVATION orderObservation = patientResult.getORDER_OBSERVATION()
        def ORU_R01_SPECIMEN orderSpecimen = orderObservation.getSPECIMEN()
        def OBX obx = orderSpecimen.getOBX(4)

        obx.getObx1_SetIDOBX().setValue("5")
        // Se indica de qué tipo es el valor que se registrará en el
        // atributo 5 (observation value). En este caso es HD (Hyerarchic Designator)
        obx.getObx2_ValueType().setValue("TX")
        obx.getObx3_ObservationIdentifier().getCe2_Text().setValue("SR Text")
        // Creo una instancia de HD (que es el tipo del atributo OBX-5 indicado
        // en OBX-2, y asigno el valor al atributo OBX-5
        TX tx = new TX(this.msg)
        tx.setValue(srText)
        obx.getObservationValue(0).setData(tx)
    }

}
