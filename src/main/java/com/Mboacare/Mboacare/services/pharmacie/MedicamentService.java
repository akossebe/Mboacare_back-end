package com.Mboacare.Mboacare.services.pharmacie;

import com.Mboacare.Mboacare.dto.MedicamentReqdto;
import com.Mboacare.Mboacare.dto.MedicamentResdto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MedicamentService {
    void addMedicament(MedicamentReqdto medicamentReqdto);
    MedicamentResdto getMedicamentById(String id);
    List<MedicamentResdto> getAllMedicament();
    void updateMedicament(String id, MedicamentReqdto medicamentReqdto);
    void deleteMedicament(String id);
    Page<MedicamentResdto> getPaginated(int page, int size, String sortBy);
    List<com.Mboacare.Mboacare.entities.Medicament> findMedicamentByForme(String forme);
}
