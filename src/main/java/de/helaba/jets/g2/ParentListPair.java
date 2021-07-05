package de.helaba.jets.g2;

public class ParentListPair<P,L extends Iterable> {
    private P parentEntity;
    private L entityList;


    public  ParentListPair(P parentEntity, L entyList) {
        this.parentEntity = parentEntity;
        this.entityList = entyList;
    }

    public L getEntityList() {
        return entityList;
    }

    public P getParentEntity() {
        return parentEntity;
    }
}
