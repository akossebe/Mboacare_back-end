package com.logonedigital.MBOAcare.service.medicament;

import com.logonedigital.MBOAcare.dto.MedicamentReqdto;
import com.logonedigital.MBOAcare.dto.MedicamentResdto;
import com.logonedigital.MBOAcare.entity.Medicament;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MedicamentService {
    void addMedicament(MedicamentReqdto medicamentReqdto);
    MedicamentResdto getMedicamentById(String idMedicament);
    List<MedicamentResdto> getAllMedicament();
    void updateMedicament(String idMedicament, MedicamentReqdto medicamentReqdto);
    void deleteMedicament(String idMedicament);
    Page<MedicamentResdto> getPaginated(int page, int size, String sortBy);
    List<MedicamentResdto> findMedicamentByForme(String forme);
}
