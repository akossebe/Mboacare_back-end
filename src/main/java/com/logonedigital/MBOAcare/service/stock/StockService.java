package com.logonedigital.MBOAcare.service.stock;

import com.logonedigital.MBOAcare.dto.StockReqdto;
import com.logonedigital.MBOAcare.dto.StockResdto;
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
