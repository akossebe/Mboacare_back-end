package com.logonedigital.MBOAcare.service.pharmaci;

import com.logonedigital.MBOAcare.dto.PharmaciReqdto;
import com.logonedigital.MBOAcare.dto.PharmaciResdto;
import com.logonedigital.MBOAcare.entity.Pharmaci;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PharmaciService {
    void addPharmaci(PharmaciReqdto pharmaciReqdto);
    PharmaciResdto getPharmaciById(String idPharmaci);
    List<PharmaciResdto> getAllPharmaci();
    void updatePharmaci(String idPharmaci, PharmaciReqdto pharmaciReqdto);
    void deletePharmaci(String idPharmaci);
    Page<PharmaciResdto> getPaginated(int page, int size, String sortBy, String direction);
    List<Pharmaci> findPharmaciByMedicamentNom(String nomMedicament);
    List<Object[]> countMedicamentParPharmaci();
}
