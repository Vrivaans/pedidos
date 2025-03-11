package com.pedidos.codigo.Repositories;

import com.pedidos.codigo.Models.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    Empresa findByCodigo(String codigo);
}
