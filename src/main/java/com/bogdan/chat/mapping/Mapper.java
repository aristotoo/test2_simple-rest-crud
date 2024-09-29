package com.bogdan.chat.mapping;

public interface Mapper<T,E> {
    T create(E entity);
    E map (T entity);
}
