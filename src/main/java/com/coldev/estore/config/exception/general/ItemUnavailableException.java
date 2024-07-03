package com.coldev.estore.config.exception.general;

import com.coldev.estore.common.constant.MessageDictionary;

public class ItemUnavailableException extends ItemNotFoundException {
    public ItemUnavailableException(Long id, String itemType) {
        super(id, itemType, MessageDictionary.ITEM_NOT_AVAILABLE);
    }

    public ItemUnavailableException(Long id, String itemType, String customMessage) {
        super(id, itemType, customMessage);
    }
}
