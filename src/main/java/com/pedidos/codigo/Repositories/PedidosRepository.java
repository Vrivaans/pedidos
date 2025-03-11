package com.pedidos.codigo.Repositories;

import com.pedidos.codigo.Models.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidosRepository extends JpaRepository<Pedido,Long> {
}
