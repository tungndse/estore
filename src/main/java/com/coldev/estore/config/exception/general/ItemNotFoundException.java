package com.coldev.estore.config.exception.general;

public class ItemNotFoundException extends RuntimeException {

    public ItemNotFoundException(Long id, String itemType) {
        super(
                id != null ? "Cannot find item with id " + id + " and item type " + itemType
                : "Cannot find item " + itemType
        );

    }

    public ItemNotFoundException(String message) {
        super(message);
    }


}
