package com.chinafocus.bookshelf.model.repository.shelves;

import io.reactivex.Observable;

public interface IShelvesRepository {

    Observable<String> getNetShelves(String category, String[] parms);

    Observable<String> getCacheShelves(String category);

}
