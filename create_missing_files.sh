#!/bin/bash
set -e

# ResourceExistException
cat << 'INNER_EOF' > src/main/java/com/Mboacare/Mboacare/exception/ResourceExistException.java
package com.Mboacare.Mboacare.exception;

public class ResourceExistException extends RuntimeException {
    public ResourceExistException(String message) {
        super(message);
    }
}
INNER_EOF

# PharmaciService
cat << 'INNER_EOF' > src/main/java/com/Mboacare/Mboacare/services/pharmacie/PharmaciService.java
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
INNER_EOF

# MedicamentService
cat << 'INNER_EOF' > src/main/java/com/Mboacare/Mboacare/services/pharmacie/MedicamentService.java
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
}
INNER_EOF

# StockService
cat << 'INNER_EOF' > src/main/java/com/Mboacare/Mboacare/services/pharmacie/StockService.java
package com.Mboacare.Mboacare.services.pharmacie;

import com.Mboacare.Mboacare.dto.StockReqdto;
import com.Mboacare.Mboacare.dto.StockResdto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface StockService {
    void addStock(StockReqdto stockReqdto);
    StockResdto getStockById(String idStock);
    List<StockResdto> getAllStock();
    void updateStock(String idStock, StockReqdto stockReqdto);
    void deleteStock(String idStock);
    Page<StockResdto> getPaginated(int page, int size, String sortBy);
}
INNER_EOF

chmod +x create_missing_files.sh
./create_missing_files.sh
