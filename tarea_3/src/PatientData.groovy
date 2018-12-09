class PatientData {
    def patient1Map = [:]
    def patient2Map = [:]
    def patient3Map = [:]

    def PatientData() {
        // ============================================================================
        // Datos de paciente 1
        // Registro id0: Identificador único dentro del sistema público del GCBA
        def patient1Id0 = [:]
        patient1Id0["assigningAuthority"] = "HCE_GCBA"
        patient1Id0["idNumber"] = "201324"
        
        // Registro id1: Identificador único para Argentina (DNI, asignado por RENAPER)
        def patient1Id1 = [:]
        patient1Id1["assigningAuthority"] = "RENAPER"
        patient1Id1["idNumber"] = "10324121"
       
        // Registro apellido, nombre, fecha de nacimiento y sexo
        // Como indico que se usa el encoding ISO-8859-1 puedo usar acentos
        // (Nota: este archivo está guardado como latin-1)
        this.patient1Map["surName"] = "Jaime"
        this.patient1Map["givenName"] = "Olsen"
        this.patient1Map["dateTimeOfBirth"] = "44300821"
        this.patient1Map["administrativeSex"] = "M"
        this.patient1Map["id0"] = patient1Id0
        this.patient1Map["id1"] = patient1Id1
        // ============================================================================
        
        // ============================================================================
        // Datos de paciente 2
        // Registro id0: Identificador único dentro del sistema público del GCBA
        def patient2Id0 = [:]
        patient2Id0["assigningAuthority"] = "HCE_GCBA"
        patient2Id0["idNumber"] = "123658"
        
        // Registro id1: Identificador único para Argentina (DNI, asignado por RENAPER)
        def patient2Id1 = [:]
        patient1Id1["assigningAuthority"] = "RENAPER"
        patient1Id1["idNumber"] = "14344823"
        
        // Registro apellido, nombre, fecha de nacimiento y sexo
        this.patient2Map["surName"] = "Houssay"
        this.patient2Map["givenName"] = "Bernardo"
        this.patient2Map["dateTimeOfBirth"] = "19200821"
        this.patient2Map["administrativeSex"] = "M"
        this.patient2Map["id0"] = patient2Id0
        this.patient2Map["id1"] = patient1Id1
        // ============================================================================
        
        
        // ============================================================================
        // Datos de paciente 3
        // Registro id0: Identificador único dentro del sistema público del GCBA
        def patient3Id0 = [:]
        patient3Id0["assigningAuthority"] = "HCE_GCBA"
        patient3Id0["idNumber"] = "642304"
        
        // Registro id1: Identificador único para Argentina (DNI, asignado por RENAPER)
        def patient3Id1 = [:]
        patient3Id1["assigningAuthority"] = "RENAPER"
        patient3Id1["idNumber"] = "22032832"
        
        // Registro apellido, nombre, fecha de nacimiento y sexo
        this.patient3Map["surName"] = "Houssay"
        this.patient3Map["givenName"] = "Bernardo"
        this.patient3Map["dateTimeOfBirth"] = "19200821"
        this.patient3Map["administrativeSex"] = "M"
        this.patient3Map["id0"] = patient3Id0
        this.patient3Map["id1"] = patient3Id1
        // ============================================================================
    }
}
