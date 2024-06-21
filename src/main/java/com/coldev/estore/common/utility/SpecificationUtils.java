package com.coldev.estore.common.utility;

import com.coldev.estore.domain.dto.account.request.AccountFilerRequest;
import com.coldev.estore.domain.dto.combo.request.ComboFilterRequest;
import com.coldev.estore.domain.dto.product.request.ProductFilterRequest;
import com.coldev.estore.domain.entity.Account;
import com.coldev.estore.domain.entity.Combo;
import com.coldev.estore.domain.entity.Product;
import com.coldev.estore.infrastructure.repository.specification.AccountSpecifications;
import com.coldev.estore.infrastructure.repository.specification.ComboSpecifications;
import com.coldev.estore.infrastructure.repository.specification.GeneralSpecifications;
import com.coldev.estore.infrastructure.repository.specification.ProductSpecifications;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SpecificationUtils {

    public static List<Specification<Product>> getSpecifications(ProductFilterRequest filter) {

        return new ArrayList<>() {{
            if (filter.getSearchKey() != null) add(GeneralSpecifications.nameContains(filter));
            if (filter.getDescriptionContains() != null)
                add(GeneralSpecifications.descriptionContains(filter));

            if (filter.getCategory() != null) add(ProductSpecifications.hasCategory(filter));
            if (filter.getPriceMin() != null || filter.getPriceMax() != null)
                add(ProductSpecifications.filterByPrices(filter));
            if (filter.getQuantityMin() != null) add(ProductSpecifications.hasQuantityMin(filter));

        }};
    }

    public static List<Specification<Combo>> getSpecifications(ComboFilterRequest comboFilterRequest) {
        List<Specification<Combo>> specifications = new ArrayList<>();

        if (comboFilterRequest.getSearchKey() != null) {
            specifications.add(GeneralSpecifications.nameContains(comboFilterRequest));
        }
        if (comboFilterRequest.getDescriptionContains() != null) {
            specifications.add(GeneralSpecifications.descriptionContains(comboFilterRequest));
        }
        if (comboFilterRequest.getDiscountPercentMin() != null) {
            specifications.add(ComboSpecifications.hasDiscountPercentGreaterThanOrEqualTo(comboFilterRequest.getDiscountPercentMin()));
        }
        if (comboFilterRequest.getDiscountPercentMax() != null) {
            specifications.add(ComboSpecifications.hasDiscountPercentLessThanOrEqualTo(comboFilterRequest.getDiscountPercentMax()));
        }
        if (comboFilterRequest.getDiscountValueMin() != null) {
            specifications.add(ComboSpecifications.hasDiscountValueGreaterThanOrEqualTo(comboFilterRequest.getDiscountValueMin()));
        }
        if (comboFilterRequest.getDiscountPercentMax() != null) {
            specifications.add(ComboSpecifications.hasDiscountValueLessThanOrEqualTo(comboFilterRequest.getDiscountValueMax()));
        }
        if (comboFilterRequest.getByProductId() != null) {
            specifications.add(ComboSpecifications.hasProductId(comboFilterRequest.getByProductId()));
        }
        if (comboFilterRequest.getByProductCount() != null) {
            specifications.add(ComboSpecifications.hasProductCount(comboFilterRequest.getByProductCount()));
        }

        if (comboFilterRequest.getStatus() != null) {
            specifications.add(ComboSpecifications.hasAnyStatuses(Collections.singleton(comboFilterRequest.getStatus())));
        }

        return specifications;

    }


    public static List<Specification<Account>> getSpecifications(AccountFilerRequest filterRequest) {
        List<Specification<Account>> specifications = new ArrayList<>();

        if (filterRequest.getSearchKey() != null) {
            specifications.add(GeneralSpecifications.nameContains(filterRequest));
        }
        if (filterRequest.getDescriptionContains() != null) {
            specifications.add(GeneralSpecifications.descriptionContains(filterRequest));
        }

        if (filterRequest.getUsername() != null) {
            specifications.add(AccountSpecifications.hasUsername(filterRequest.getUsername()));
        }

        if (filterRequest.getEmail() != null) {
            specifications.add(AccountSpecifications.hasEmail(filterRequest.getEmail()));
        }

        if (filterRequest.getPhone() != null) {
            specifications.add(AccountSpecifications.hasPhone(filterRequest.getPhone()));
        }

        if (filterRequest.getStatus() != null) {
            specifications.add(AccountSpecifications.hasStatus(filterRequest.getStatus()));
        }

        return specifications;
    }
}
