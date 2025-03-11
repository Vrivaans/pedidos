import requests
import json
import time
from itertools import cycle

url = "http://localhost:8080/pedidos/crear"
clientes = cycle(["CODE2", "CODE3", "cliente"])
empresas = cycle(["TEST1", "TEST2", "TEST3"])
productos = cycle(["prod1", "prod2", "prod3"])
requests_per_second = 10
interval = 1 / requests_per_second
duration = 60  # Duración en segundos


def send_request(data):
    headers = {'Content-type': 'application/json'}
    try:
        response = requests.post(url, data=json.dumps(data), headers=headers, timeout=1)
        print(f"Request sent with data: {data}, Status code: {response.status_code}")
    except requests.exceptions.Timeout:
        print(f"Request timed out for data: {data}")
    except requests.exceptions.RequestException as e:
        print(f"Request failed for data: {data}, Error: {e}")


start_time = time.time()

for _ in range(duration * requests_per_second):  # Envía solicitudes durante 60 segundos
    data = {
        "codigoCliente": next(clientes),
        "codigoEmpresa": next(empresas),
        "listaItems": {
            next(productos): 2,
            next(productos): 5,
            next(productos): 1,
        },
        "fecha": "2024-04-26"
    }

    send_request(data)

    elapsed_time = time.time() - start_time
    sleep_time = interval - elapsed_time % interval

    time.sleep(sleep_time if sleep_time > 0 else 0)
