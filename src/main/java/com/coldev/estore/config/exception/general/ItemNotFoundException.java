package com.coldev.estore.config.exception.general;

public class ItemNotFoundException extends RuntimeException{

    public ItemNotFoundException(Long id, String itemType) {
        super("Cannot find item with id " + id + " and item type " + itemType);
    }

    public ItemNotFoundException(String message) {
        super(message);
    }


}
