package com.pedidos.codigo.Services;

import com.pedidos.codigo.DTO.PedidoDTO;
import com.pedidos.codigo.Interfaces.CRUD;
import com.pedidos.codigo.Models.*;
import com.pedidos.codigo.Repositories.ClienteRepository;
import com.pedidos.codigo.Repositories.EmpresaRepository;
import com.pedidos.codigo.Repositories.PedidosRepository;
import com.pedidos.codigo.Repositories.ProductoRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class PedidosService implements CRUD<Pedido> {
    private final ClienteRepository clienteRepository;
    private final EmpresaRepository empresaRepository;
    private final ProductoRepository productoRepository;
    private final PedidosRepository pedidosRepository;
    private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    public PedidosService (ClienteRepository clienteRepository,
                           EmpresaRepository empresaRepository,
                           ProductoRepository productoRepository,
                           PedidosRepository pedidosRepository
    ){
        this.clienteRepository = clienteRepository;
        this.empresaRepository = empresaRepository;
        this.productoRepository = productoRepository;
        this.pedidosRepository = pedidosRepository;
    }
    public Mono<Void> validarJson(@Valid PedidoDTO pedido){
        return Mono.empty();
    }

    public Mono<ResponseEntity<Map<String,Object>>> procesarPedido(PedidoDTO pedidoDTO){
        Pedido pedido = new Pedido();
        return Mono.zip(getClienteByCodigo(pedidoDTO.getCodigoCliente()),
                        getEmpresaByCodigo(pedidoDTO.getCodigoEmpresa()),
                        getItems(pedidoDTO.getListaItems()))
                .flatMap(tupla -> {
                    UUID uuid = UUID.randomUUID();
                    pedido.setCliente(tupla.getT1());
                    pedido.setEmpresa(tupla.getT2());
                    pedido.setFecha(pedidoDTO.getFecha());
                    pedido.setCodigo(uuid.toString());
                    asignarPedidoAItems(tupla.getT3(), pedido);
                    return save(pedido).then(Mono.just(ResponseEntity.ok(Map.of("pedido", pedido))));
                });
    }

    public void asignarPedidoAItems(List<PedidosItems> items, Pedido pedido){
        for (PedidosItems item : items) {
            item.setPedido(pedido);
        }
        pedido.getItems().addAll(items);
    }

    public Mono<List<PedidosItems>> getItems(Map<String, Integer> items) {
        return Flux.fromIterable(items.entrySet())
                .flatMap(entry -> getProductoByCodigo(entry.getKey())
                        .map(producto -> createPedidoItem(producto, entry.getValue())))
                .collectList();
    }


    public PedidosItems createPedidoItem(Producto producto, Integer cantidad){
        PedidosItems pedidosItems = new PedidosItems();
        pedidosItems.setCantidad(cantidad);
        pedidosItems.setProducto(producto);
        pedidosItems.setPrecioTotal(producto.getPrecio() * cantidad);
        return pedidosItems;
    }

    public Mono<Producto> getProductoByCodigo(String codigo) {
        return Mono.fromCallable(() -> productoRepository.findByCodigo(codigo)).subscribeOn(Schedulers.fromExecutor(executor));
    }

    public Mono<Cliente> getClienteByCodigo(String codigo){
        return Mono.fromCallable(() -> clienteRepository.findByCodigo(codigo)).subscribeOn(Schedulers.fromExecutor(executor));
    }

    public Mono<Empresa> getEmpresaByCodigo(String codigo){
        return Mono.fromCallable(() -> empresaRepository.findByCodigo(codigo)).subscribeOn(Schedulers.fromExecutor(executor));
    }

    @Override
    public Mono<String> save(Pedido pedido) {
        return Mono.fromCallable(() -> {
                    pedidosRepository.save(pedido);
                    return "Pedido creado";
                })
                .subscribeOn(Schedulers.fromExecutor(executor));
    }

    public Mono<List<Pedido>> getAllPedidos(){
        return Mono.fromCallable(() -> pedidosRepository.findAll()).subscribeOn(Schedulers.fromExecutor(executor));
    }


    @Override
    public Mono<Void> update(Pedido object) {
        return null;
    }

    @Override
    public Mono<Void> delete(Pedido object) {
        return null;
    }

    @Override
    public Mono<List<Pedido>> getAll() {
        return null;
    }

    @Override
    public Mono<Pedido> findByCodigo(String codigo) {
        return null;
    }
}
