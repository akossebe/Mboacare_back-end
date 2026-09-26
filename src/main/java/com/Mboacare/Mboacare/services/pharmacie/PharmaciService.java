package com.Mboacare.Mboacare.services.pharmacie;

import com.Mboacare.Mboacare.dto.PharmaciReqdto;
import com.Mboacare.Mboacare.dto.PharmaciResdto;
import com.Mboacare.Mboacare.entities.Pharmaci;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PharmaciService {
    void addPharmaci(PharmaciReqdto pharmaciReqdto);
    PharmaciResdto getPharmaciById(String idPharmaci);
    List<PharmaciResdto> getAllPharmaci();
    void updatePharmaci(String idPharmaci, PharmaciReqdto pharmaciReqdto);
    void deletePharmaci(String id);
    Page<PharmaciResdto> getPaginated(int page, int size, String sortBy, String direction);
    List<Pharmaci> findPharmaciByMedicamentNom(String nomMedicament);
    List<Object[]> countMedicamentParPharmaci();
}
