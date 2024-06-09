package com.coldev.estore.config.exception.account;

import com.coldev.estore.common.constant.ConstantDictionary;
import com.coldev.estore.config.exception.general.ItemNotFoundException;

import static com.coldev.estore.common.constant.MessageDictionary.COULD_NOT_FIND_ACCOUNT_WITH_SUCH_USERNAME;

public class EstoreAccountNotFoundException extends ItemNotFoundException {

    public EstoreAccountNotFoundException(Long id, String itemType) {
        super(id, ConstantDictionary.ACCOUNT);
    }

    public EstoreAccountNotFoundException(String username) {
        super(COULD_NOT_FIND_ACCOUNT_WITH_SUCH_USERNAME + "Username: " + username);
    }
}

