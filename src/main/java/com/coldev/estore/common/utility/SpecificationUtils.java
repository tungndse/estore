package com.coldev.estore.common.utility;

import com.coldev.estore.domain.FilterRequest;
import com.coldev.estore.domain.dto.combo.request.ComboFilterRequest;
import com.coldev.estore.domain.dto.product.request.ProductFilterRequest;
import com.coldev.estore.domain.entity.Combo;
import com.coldev.estore.domain.entity.Product;
import com.coldev.estore.infrastructure.repository.specification.GeneralSpecifications;
import com.coldev.estore.infrastructure.repository.specification.ProductSpecifications;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class SpecificationUtils {


    public static List<Specification<Product>> getSpecifications(ProductFilterRequest filter) {

        return new ArrayList<>() {{
            if (filter.getSearchKey() != null) add(GeneralSpecifications.searchByName(filter));
            if (filter.getDescriptionContainsString() != null)
                add(GeneralSpecifications.descriptionContainsString(filter));

            if (filter.getCategory() != null) add(ProductSpecifications.hasCategory(filter));
            if (filter.getPriceMin() != null || filter.getPriceMax() != null)
                add(ProductSpecifications.filterByPrices(filter));
            if (filter.getQuantityMin() != null) add(ProductSpecifications.hasQuantityMin(filter));

        }};
    }

    public static List<Specification<Combo>> getSpecifications(ComboFilterRequest comboFilterRequest) {
        return new ArrayList<>() {{
            if (comboFilterRequest.getSearchKey() != null)
                add(GeneralSpecifications.searchByName(comboFilterRequest));
            if (comboFilterRequest.getDescriptionContainsString() != null)
                add(GeneralSpecifications.descriptionContainsString(comboFilterRequest));

            // TODO 1: Create Specification method in ComboSpecifications
            // TODO 2: Integrate into this

        }};

    }


}
