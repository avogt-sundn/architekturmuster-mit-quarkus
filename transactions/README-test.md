# Szenarien

1. XA Transaktion mit zwei sql dbs
   1. mit H2 in-memory (XA fähig): [zum Test](./src/test/java/quarkitecture/RegisterNewCustomersResourceTest.java)
   2. 
2. sd


## Wie wird getestet

### Von der Kommandozeile mit httpie

Das Kommando `http` wird von dem tool httpie implementiert (im Devcontainer enthalten).

- es arbeitet wie curl gegen eine URL
- aus einzelnen Parametern wird ein json Objekt erzeugt
- ohne weiteres wird `application/json` gesendet

````bash
http -v localhost:8080/hello name=Armin

POST /hello HTTP/1.1
Accept: application/json, */*;q=0.5
Accept-Encoding: gzip, deflate
Connection: keep-alive
Content-Length: 17
Content-Type: application/json
Host: localhost:8080
User-Agent: HTTPie/2.2.0

{
    "name": "Armin"
}

HTTP/1.1 201 Created
Content-Type: application/json;charset=UTF-8
content-length: 40

{
    "id": 1,
    "name": "Armin",
    "timestamp": null
}
````