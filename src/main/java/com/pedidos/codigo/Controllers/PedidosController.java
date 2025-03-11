package com.pedidos.codigo.Controllers;

import com.pedidos.codigo.DTO.PedidoDTO;
import com.pedidos.codigo.Models.Pedido;
import com.pedidos.codigo.Services.PedidosService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/pedidos")
public class PedidosController {
    private final PedidosService pedidosService;
    private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
    public PedidosController(PedidosService pedidosService){
        this.pedidosService = pedidosService;
    }

    @PostMapping("/crear")
    public Mono<ResponseEntity<Map<String,Object>>> crearPedido(@RequestBody PedidoDTO pedido){
        return pedidosService.validarJson(pedido)
                .then(pedidosService.procesarPedido(pedido))
                .map(resultado -> {
                    Map<String, Object> body = Map.of("success", true, "message", "Pedido creado", "datos", resultado);
                    return ResponseEntity.ok(body);
                }).onErrorResume(e -> {
                    Map<String, Object> bodyError = Map.of("error",true, "mensaje", "Hubo un error al procesar el pedido", "detalle", e.getMessage());
                    return Mono.just(ResponseEntity.badRequest().body(bodyError));
                });
    }

    @GetMapping("/ping")
    public Mono<ResponseEntity<Map<String, Object>>> ping(){
        return Mono.just(ResponseEntity.ok(Map.of("response","pong")));
    }

    @GetMapping("/getPedidos")
    public Mono<ResponseEntity<List<Pedido>>> getPedidos(){
        return pedidosService.getAllPedidos().map(pedidos -> ResponseEntity.ok(pedidos));
    }
}
