package ru.study.t5_mcsrv.mapping;

public interface Mappable<T, R> {
    R map(T obj);
}
