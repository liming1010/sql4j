package com.tpy.core.manager;

public interface UpdateManager<T> {

    UpdateFun1Manager update(T t1);

    UpdateFun4Manager updateById(T t);

    /**
     * 逻辑删除
     * @param t
     * @return
     */
    UpdateFun1Manager delByLogic(T t);

    /**
     * 根据PrimaryKkey删除
     * @param t
     * @return
     */
    UpdateFun4Manager delByLogicByPrimaryKkey(T t);

    /**
     * 物理删除，删除了没有了
     * @param t
     * @return
     */
    UpdateFun1Manager delByPhysics(T t);

    /**
     * 物理删除，删除了没有了
     * @param t
     * @return
     */
    UpdateFun4Manager delPhysicsByPrimaryKkey(T t);
}
