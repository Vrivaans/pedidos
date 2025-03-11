package com.pedidos.codigo.Interfaces;

import reactor.core.publisher.Mono;

import java.util.List;

public interface CRUD<T> {
    public Mono<String> save(T object);
    public Mono<Void> update(T object);
    public Mono<Void> delete(T object);
    public Mono<List<T>> getAll();
    public Mono<T> findByCodigo(String codigo);
}
