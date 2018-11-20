package com.chinafocus.bookshelf.model.repository.shelves;


public class ShelvesRepositoryFactory {

    public static IShelvesRepository getInstance() {
        return ShelvesRepository.Holder.INSTANCE;
    }
}
