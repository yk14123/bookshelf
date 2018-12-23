package com.chinafocus.bookshelf.model.base.shelvesrepository;


public class ShelvesRepositoryFactory {

    public static IShelvesRepository getInstance() {
        return ShelvesRepository.Holder.INSTANCE;
    }
}
