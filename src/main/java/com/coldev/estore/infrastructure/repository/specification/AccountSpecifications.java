package com.coldev.estore.infrastructure.repository.specification;

import com.coldev.estore.common.enumerate.Status;
import com.coldev.estore.domain.entity.Account;
import org.springframework.data.jpa.domain.Specification;

public class AccountSpecifications extends GeneralSpecifications {

    public static Specification<Account> hasUsername(String username) {
        return (root, query, cb) -> cb.equal(root.get("username"), username);
    }

    public static Specification<Account> hasEmail(String email) {
        return (root, query, cb) -> cb.equal(root.get("email"), email);
    }


    public static Specification<Account> hasPhone(String phone) {
        return (root, query, cb) -> cb.equal(root.get("phone"), phone);
    }

    public static Specification<Account> hasStatus(Status status) {
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }
}
