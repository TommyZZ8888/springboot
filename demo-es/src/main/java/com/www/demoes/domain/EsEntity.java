package com.www.demoes.domain;

/**
 * @author admin
 * @date 2021-03-30 19:36
 */
public class EsEntity<T> {
    private String id;
    private T data;

    public EsEntity(){}

    public EsEntity(String id, T data) {
        this.id = id;
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
