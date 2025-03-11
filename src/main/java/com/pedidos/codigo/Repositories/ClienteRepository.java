package com.pedidos.codigo.Repositories;

import com.pedidos.codigo.Models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Cliente findByCodigo(String codigo);
}
