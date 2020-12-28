package com.tpy.core.manager;

import java.util.List;

public interface InsertFun1Manager<T> {

    InsertFun2Manager insert(T t);

    InsertFun2Manager insertBatch(List<T> list);
}
