Este proyecto tiene el único objetivo de mostrar ciertas capacidades en el uso de herramientas como WebFlux,
virtual threads con Java 21 para adaptar código bloqueante a un entorno reactivo para mantener activo
el hilo principal del sistema, y para manejar varios procesos en simultáneo.
El código de python lo único que hace es simular muchas peticiones en simultáneo hasta por 60 segundos.
Explicación:
Para simplificar el desarrollo, profundicé las funcionalidades del servicio de pedidos y el resto de los datos
de los otros modelos los inserté mediante querys.
A la hora de procesar los pedidos, el servicio busca obtener con los códigos los datos mapeados de JPA, 
para esto es un Mono.zip que ejecute de forma simultánea los métodos que traen los datos con JPA usando hilos virtuales
que gestiona la JVM. Una vez que se obtienen los datos necesarios se arma el pedido y se abre otro hilo virtual
guardar el pedido en la db. Usé hilos virtuales para hacer las peticiones a la db con JPA porque este tipo
de operaciones son bloqueantes, pero al ejecutarse en otros hilos permite que el hilo principal se mantenga libre y activo para otra tarea.