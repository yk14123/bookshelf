package com.chinafocus.bookshelf.model.base.shelvesrepository;

import io.reactivex.Observable;

public interface IShelvesRepository {

    Observable<String> getNetShelves(String category, String[] parms);

    Observable<String> getCacheShelves(String category, String[] parms);

}
