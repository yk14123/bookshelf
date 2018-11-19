package com.chinafocus.bookshelf.model.repository.shelves;


import com.chinafocus.bookshelf.model.lru.LruMap;
import com.chinafocus.bookshelf.model.lru.SoftMap;

public class ShelvesRepositoryFactory {

    public static IShelvesRepository getInstance() {

//        return ShelvesRepository.Holder.INSTANCE;
//        return VolRepository.Holder.INSTANCE;
        return LruMap.getInstance().createOrGetElement(VolRepository.class.getName(), VolRepository.class, new SoftMap.ICreateObjectAble<VolRepository>() {
            @Override
            public VolRepository createObject() {
                return (VolRepository) VolRepository.Holder.INSTANCE;
            }
        });

    }

}
